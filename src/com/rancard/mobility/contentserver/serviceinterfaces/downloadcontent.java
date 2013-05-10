package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.mobility.contentserver.*;
import com.unwiredtec.rtcreator.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import java.net.URL;
import java.net.URLConnection;
import com.rancard.common.Feedback;
import com.rancard.mobility.common.FonCapabilityMtrx;
import com.rancard.mobility.contentserver.Transaction;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import java.util.List;
//import com.rancard.common.DConnect;
//import com.rancard.mobility.contentserver.uploadsBean;
//import java.sql.ResultSet;
//import java.sql.Connection;
//import java.sql.SQLException;
//import com.rancard.mobility.contentserver.Format;
//import java.sql.PreparedStatement;
//import net.sourceforge.wurfl.wurflapi.WurflDevice;

public class downloadcontent extends HttpServlet {

    private static final String CONTENT_TYPE = "text/vnd.wap.wml";
    private static final int IMAGE = 5;

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String message = "";
        String siteType = CPSite.SMS;

        String s = request.getProtocol().toLowerCase();
        s = s.substring(0, s.indexOf("/")).toLowerCase();
        //String baseUrl = s + "://" + request.getServerName () + ":" + request.getServerPort () + request.getContextPath () + "/";
        String baseUrl = this.getServletContext().getInitParameter("contentServerPublicURL");//
        com.rancard.common.Message replyPg = new com.rancard.common.Message();
        PrintWriter out = response.getWriter();

        // this represents an instance of a transaction
        Transaction download = new Transaction();

        // get request variables
        String ticketID = request.getParameter("ticketId");
        if (ticketID == null) {
            ticketID = (String) request.getAttribute("ticketId");
        }

        String supportsDrm = request.getParameter("drm");
        if (supportsDrm == null) {
            supportsDrm = (String) request.getAttribute("drm");
        }
        
        String lang = "";
        if (lang == null || lang.equals("")) {
            lang = "en";
        }
        Feedback feedback = (Feedback) this.getServletContext().getAttribute("feedback_" + lang);
        if (feedback == null) {
            try {
                feedback = new Feedback();
            } catch (Exception e) {
            }
        }

        //logging statements
        System.out.println("Received request to complete download with transaction ID: " + ticketID);
        System.out.println("Date received: " + java.util.Calendar.getInstance().getTime().toString());
        //end of logging

        if (ticketID == null) {
            ticketID = "";
        }

        if (supportsDrm == null || supportsDrm.length() <= 0) {
            supportsDrm = "no";
        }
        
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = (String) request.getAttribute("keyword");
        }
        
        String accId = request.getParameter("accId");
        if (accId == null) {
            accId = (String) request.getAttribute("accId");
        }
        
        UserService service = null;   

        try {
            // check if direct download request
            if (keyword != null && accId != null) {
                service = ServiceManager.viewService(keyword, accId);
                if (service == null) {
                    throw new Exception(String.format("Service not found for keyword = %s and account = %s", keyword, accId));
                }
            } else {
                // get details of this transaction            

                //logging statements
                System.out.println("Looking up transaction details for transaction ID: " + ticketID + " ......");
                //end of logging
                download = Transaction.viewTransaction(ticketID);
                //logging statements
                System.out.println("Found transaction details for transaction ID: " + ticketID);
                System.out.println("user's mobile number: " + download.getSubscriberMSISDN());
                System.out.println("request came from site with ID: " + download.getSiteId());
            }
            //end of logging
        } catch (Exception e) {
            //logging statements
            System.out.println("Could not find transaction details for transaction ID: " + ticketID);
            System.out.println("Exception thrown carries message: " + e.getMessage());
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_TICKET_ID);
                } else {
                    message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_TICKET_ID);
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            out.println(message);
            return;
        }

        // check if transaction is valid i.e has not been used before and transaction Id exists
        if (!download.getDownloadCompleted() || service != null) {
            try {
                // handle direct download request
                if (service != null) {
                    ContentListDB contentDB = new ContentListDB();
                    List<ContentItem> contentItems = (List<ContentItem>) contentDB.getRecentlyAdded(service.getAccountId(), Integer.parseInt(service.getServiceType()), service.getKeyword());
                    if (contentItems == null || contentItems.isEmpty()) {
                        throw new Exception("No content found");
                    }
                    System.out.println("Streaming data...");
                    streamData(response, request, contentItems.get(0), supportsDrm);
                    System.out.println("Streaming data complete....");
                }
                else if (updateBandwidthAllocation(download) == true) {
                    // stream data based on format , location of content
                    //logging statements
                    System.out.println("Streaming data....");
                    //end of logging
                    //response.setContentType("application/x-download");
                    //response.setHeader("Content-Disposition", "attachment;filename="+download.getContentItem().gettitle());
                    streamData(response, request, download.getContentItem(), supportsDrm);
                    Transaction.updateDownloadStatus(download.getTicketID(), true);
                    //logging statements
                    System.out.println("Streaming data complete....");
                    //end of logging
                } else {
                    throw new Exception(Feedback.BANDWIDTH_EXCEEDED);
                }
            } catch (java.net.ConnectException ex) {
                System.out.println(new Date() + ": Exception> " + ex.getMessage());
                try {
                    if (siteType.equals(CPSite.SMS)) {
                        message = feedback.getUserFriendlyDescription(Feedback.CONNECTION_ERROR);
                    } else {
                        message = feedback.formDefaultMessage(Feedback.CONNECTION_ERROR);
                    }
                } catch (Exception e) {
                    message = ex.getMessage();
                }
                out.println(message);
                return;
            } catch (HttpException e) {
                System.out.println(new Date() + ": Exception> " + e.getMessage());
                try {
                    if (siteType.equals(CPSite.SMS)) {
                        message = feedback.getUserFriendlyDescription(Feedback.CONNECTION_ERROR);
                    } else {
                        message = feedback.formDefaultMessage(Feedback.CONNECTION_ERROR);
                    }
                } catch (Exception ex) {
                    message = ex.getMessage();
                }
                out.println(message);
                return;
            } catch (IOException e) {
                System.out.println(new Date() + ": Exception> " + e.getMessage());
                try {
                    if (siteType.equals(CPSite.SMS)) {
                        message = feedback.getUserFriendlyDescription(Feedback.CONNECTION_ERROR);
                    } else {
                        message = feedback.formDefaultMessage(Feedback.CONNECTION_ERROR);
                    }
                } catch (Exception ex) {
                    message = ex.getMessage();
                }
                out.println(message);
                return;
            } catch (Exception e) {
                System.out.println(new Date() + ": Exception> " + e.getMessage());
                try {
                    if (siteType.equals(CPSite.SMS)) {
                        message = feedback.getUserFriendlyDescription(e.getMessage());
                    } else {
                        message = feedback.formDefaultMessage(e.getMessage());
                    }
                } catch (Exception ex) {
                    message = ex.getMessage();
                }
                out.println(message);
                return;
            }
        } else {
            /*replyPg.setMessage (Feedback.EXPIRED_TICKET_ID);
            replyPg.setStatus (false);
            System.out.println ("Download ticket has expired.");
            out = response.getWriter ();
            out.println ("Download ticket has expired.");
            return;*/
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.EXPIRED_TICKET_ID);
                } else {
                    message = feedback.formDefaultMessage(Feedback.EXPIRED_TICKET_ID);
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
//            out = response.getWriter();
            out.println(message);
            return;
        }

    }

    //Process the HTTP Post request
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }

    //Clean up resources
    public void destroy() {
    }
//    
//    public void streamData(HttpServletResponse response,
//            HttpServletRequest request, Transaction download, String supportsDrm) throws
//            Exception {
//
//        // determine bearer
//        // determine location
//        // stream data
//        if (download.getFormat().getPushBearer().equals("WAP")) {
//            // binary content over packet switched network
//            System.out.println(new java.util.Date() + ":Request to streamBinaryConent...");
//            if (supportsDrm.equalsIgnoreCase("yes")) {
//                if (streamBinaryData(download, response, request)) {
//                    //download.updateTransaction (download.getTicketID (), true, true);
//                    download.updateDownloadStatus(download.getTicketID(), true);
//                } else {
//                    System.out.println(new java.util.Date() + ":Unable to streamBinaryData: Feedback.NO_CONTENT_AT_LOCATION");
//
//                    throw new Exception(Feedback.NO_CONTENT_AT_LOCATION);
//                }
//            } else {
//                if (streamBinaryDataNoDrm(download, response, request)) {
//                    //download.updateTransaction (download.getTicketID (), true, true);
//                    download.updateDownloadStatus(download.getTicketID(), true);
//                } else {
//                    System.out.println(new java.util.Date() + ":Unable to streamBinaryData: Feedback.NO_CONTENT_AT_LOCATION");
//
//                    throw new Exception(Feedback.NO_CONTENT_AT_LOCATION);
//                }
//            }
//            // character based content
//        } else {
//            System.out.println(new java.util.Date() + ":Request to stream Character based content...");
//            if (!download.getContentItem().islocal()) {
//                // stream  text content form remote host
//                if (streamCharacterData(download.getContentItem().getDownloadUrl(), download.getFormat().getMimeType(), new PrintWriter(response.getOutputStream()), response, request)) {
//                    //download.updateTransaction (download.getTicketID (), true, true);
//                    download.updateDownloadStatus(download.getTicketID(), true);
//                } else {
//                    System.out.println(new java.util.Date() + ":Unable to stream Character based content:Feedback.NO_CONTENT_AT_LOCATION");
//
//                    throw new Exception(Feedback.NO_CONTENT_AT_LOCATION);
//                }
//            } else {
//                com.rancard.mobility.contentserver.uploadsBean upload = new com.rancard.mobility.contentserver.uploadsBean();
//                upload.setid(download.getContentItem().getid());
//                upload.setlist_id(download.getContentItem().getListId());
//                upload = new RepositoryManager().fetchFile(upload.getlist_id(),
//                        upload.getid());
//                // stream text content from local host
//                if (streamCharacterData( /*new File(uc.getContentLocation() +item.getDownloadUrl()),*/upload.getDataStream(), download.getFormat().getMimeType(),
//                        new PrintWriter(response.getOutputStream()), response, request)) {
//                    //download.updateTransaction (download.getTicketID (), true, true);
//                    download.updateDownloadStatus(download.getTicketID(), true);
//                } else {
//                    throw new Exception(Feedback.NO_CONTENT_AT_LOCATION);
//                }
//            }
//        }
//    }
    

    public void streamData(HttpServletResponse response,
            HttpServletRequest request, ContentItem content, String supportsDrm) throws
            Exception {

        // determine bearer
        // determine location
        // stream data
        if (content.getFormat().getPushBearer().equals("WAP")) {
            // binary content over packet switched network
            System.out.println(new java.util.Date() + ":Request to streamBinaryConent...");
            if (supportsDrm.equalsIgnoreCase("yes")) {                
                if (!streamBinaryData(content, response, request)) {
                    System.out.println(new java.util.Date() + ":Unable to streamBinaryData: Feedback.NO_CONTENT_AT_LOCATION");
                    throw new Exception(Feedback.NO_CONTENT_AT_LOCATION);
                }
            } else {                
                if (!streamBinaryDataNoDrm(content, response, request)) {
                    System.out.println(new java.util.Date() + ":Unable to streamBinaryData: Feedback.NO_CONTENT_AT_LOCATION");
                    throw new Exception(Feedback.NO_CONTENT_AT_LOCATION);
                }
            }
            // character based content
        } else {
            System.out.println(new java.util.Date() + ":Request to stream Character based content...");
            if (!content.islocal()) {
                // stream  text content form remote host
                if (!streamCharacterData(content.getDownloadUrl(), content.getFormat().getMimeType(), new PrintWriter(response.getOutputStream()), response, request)) {
                    System.out.println(new java.util.Date() + ":Unable to stream Character based content:Feedback.NO_CONTENT_AT_LOCATION");
                    throw new Exception(Feedback.NO_CONTENT_AT_LOCATION);
                }
            } else {
                com.rancard.mobility.contentserver.uploadsBean upload = new com.rancard.mobility.contentserver.uploadsBean();
                upload.setid(content.getid());
                upload.setlist_id(content.getListId());
                upload = new RepositoryManager().fetchFile(upload.getlist_id(),
                        upload.getid());
                // stream text content from local host
                if (!streamCharacterData(upload.getDataStream(), content.getFormat().getMimeType(),
                        new PrintWriter(response.getOutputStream()), response, request)) {
                    throw new Exception(Feedback.NO_CONTENT_AT_LOCATION);
                }
            }
        }
    }

    /*
     * This Method Handles streaming Character data for external files
     * <p>
     * @param String urlstr ex: http;//localhost/test.pdf etc.
     * @param String format ex: xml or html etc.
     * @param PrintWriter outstr
     * @param HttpServletResponse resp
     */
    private boolean streamCharacterData(String urlstr, String formatMIMEType,
            PrintWriter outstr,
            HttpServletResponse resp,
            HttpServletRequest request) {
        String ErrorStr = null;
        boolean streamStatus = true;
        try {
            //find the right mime type and set it as contenttype
            resp.setContentType(formatMIMEType);
            InputStream in = null;
            try {
                URL url = new URL(urlstr);
                URLConnection urlc = url.openConnection();
                int length = urlc.getContentLength();
                in = urlc.getInputStream();
                resp.setContentLength(length);
                int ch;

                if (request.getHeader("accept").indexOf("vnd.oma.drm.message")
                        != -1) {
                    //we can apply the OMA-DRM 1.0 Forward Lock Wrapper
                    //so set the content type to be a protected object
                    //and deliver it wrapped in the OMA-DRM format
                    resp.setHeader("Content-Type",
                            "application/vnd.oma.drm.message; boundary=foo");

                    outstr.println();
                    outstr.println("--rmcs");
                    outstr.println("Content-Type: " + formatMIMEType);
                    outstr.println("");
                    //byte by[] = fetchFile(listId, id).getDataStream();
                    //outstr.write();
                    while ((ch = in.read()) != -1) {
                        outstr.print((char) ch);
                    }
                    outstr.println();
                    outstr.println("--rmcs--");
                    outstr.println();
                } else {
                    //We didn't find support for the OMA-DRM 1.0 Spec
                    //So use "Phase 0" Forward Lock. Will not fail on older devices
                    // InputStream in = new ByteArrayInputStream();
                    resp.setHeader("Content-Type", formatMIMEType);
                    resp.setHeader("x-drm", "noforward");
                    // byte by[] = fetchFile(listId, id).getDataStream();
                    //outstr.write(by);
                    while ((ch = in.read()) != -1) {
                        outstr.print((char) ch);
                    }
                }
                outstr.flush();

            } catch (Exception e) {
                e.printStackTrace();
                ErrorStr = "Error Streaming the Data";
                outstr.print(ErrorStr);
                streamStatus = false;
            } finally {
                if (in != null) {
                    in.close();
                }
                if (outstr != null) {
                    outstr.flush();
                    outstr.close();
                }
                streamStatus = false;
            }
        } catch (Exception e) {
            streamStatus = false;
            e.printStackTrace();
        }
        return streamStatus;
    }

    //This Method Handles streaming Character data for local files
    private boolean streamCharacterData(byte[] item, String formatMIMEType,
            PrintWriter outstr,
            HttpServletResponse resp,
            HttpServletRequest request) {
        String ErrorStr = null;
        boolean streamStatus = true;
        try {
            //find the right mime type and set it as contenttype
            resp.setContentType(formatMIMEType);
            InputStream in = null;
            try {
                in = new ByteArrayInputStream(item); // FileInputStream(item);

                int ch;

                if (request.getHeader("accept").indexOf("vnd.oma.drm.message")
                        != -1) {
                    //we can apply the OMA-DRM 1.0 Forward Lock Wrapper
                    //so set the content type to be a protected object
                    //and deliver it wrapped in the OMA-DRM format
                    resp.setHeader("Content-Type",
                            "application/vnd.oma.drm.message; boundary=foo");

                    outstr.println();
                    outstr.println("--rmcs");
                    outstr.println("Content-Type: " + formatMIMEType);
                    outstr.println("");
                    //byte by[] = fetchFile(listId, id).getDataStream();
                    //outstr.write();
                    while ((ch = in.read()) != -1) {
                        outstr.print((char) ch);
                    }
                    outstr.println();
                    outstr.println("--rmcs--");
                    outstr.println();
                } else {
                    //We didn't find support for the OMA-DRM 1.0 Spec
                    //So use "Phase 0" Forward Lock. Will not fail on older devices
                    // InputStream in = new ByteArrayInputStream();
                    resp.setHeader("Content-Type", formatMIMEType);
                    resp.setHeader("x-drm", "noforward");
                    // byte by[] = fetchFile(listId, id).getDataStream();
                    //outstr.write(by);
                    while ((ch = in.read()) != -1) {
                        outstr.print((char) ch);
                    }
                }

                while ((ch = in.read()) != -1) {
                    outstr.print((char) ch);
                }

            } catch (Exception e) {
                e.printStackTrace();
                ErrorStr = "Error Streaming the Data";
                outstr.print(ErrorStr);
                streamStatus = false;
            } finally {
                if (in != null) {
                    in.close();
                }
                if (outstr != null) {
                    outstr.flush();
                    outstr.close();
                }
                streamStatus = false;
            }
        } catch (Exception e) {
            streamStatus = false;
            e.printStackTrace();
        }
        return streamStatus;
    }

    public byte[] convert(byte[] in, String format) throws Exception {
        byte[] convertedTone = null;

        try {
            // convert a melody and put the results
            // into a byte array named nokia
            Ringtone tone = Ringtone.openRingtone(in);
            convertedTone = tone.getRingtoneBytes(format);
            //return convertedTone;
        } catch (RingtoneConvertException ex) {
            throw new Exception("Error converting tones");
        } catch (RingtoneParseException ex) {
        }

        return convertedTone;
    }

    private boolean streamBinaryData(ContentItem content,
            HttpServletResponse resp,
            HttpServletRequest request) throws
            Exception {

        System.out.println( new Date() + ": Streaming with DRM enabled...");
        boolean streamStatus = false;
        String ErrorStr = null;
        String formatMIMEType = content.getFormat().getMimeType();
        BufferedOutputStream bos = null;
        BufferedInputStream isr = null;
        BufferedInputStream bis = null;
        ServletOutputStream outstr = resp.getOutputStream();

        byte[] item = null;

        if (content.islocal()) {
            com.rancard.mobility.contentserver.uploadsBean upload = new com.rancard.mobility.contentserver.uploadsBean();
            upload.setid(content.getid());
            upload.setlist_id(content.getListId());

            try {
                upload = new RepositoryManager().fetchFile(upload.getlist_id(),
                        upload.getid());
            } catch (Exception ex1) {
                return streamStatus;
            }

            item = upload.getDataStream();
        } else {
            String urlstr = content.getDownloadUrl();
            HttpClient httpclient = new HttpClient();
            GetMethod httpget = new GetMethod(urlstr);

            try {
                int statusCode = httpclient.executeMethod(httpget);
                if (statusCode != HttpStatus.SC_OK) {
                    throw new HttpException(new Integer(statusCode).toString());
                }

                long resplength = httpget.getResponseContentLength();
                Long size = new Long(resplength);
                int length = size.intValue();

                resp.setContentLength(length);
                // Use Buffered Stream for reading/writing.
                InputStream in = httpget.getResponseBodyAsStream();
                bis = new BufferedInputStream(in);
                bos = new BufferedOutputStream(resp.getOutputStream());

                item = new byte[length];
                int bytesRead = 0;
                while (-1 != (bytesRead = bis.read(item, 0, item.length))) {
                }
            } catch (Exception e) {
                streamStatus = false;
                e.printStackTrace();
                ErrorStr = "Error Streaming the Data";
                try {
                    resp.getOutputStream().print(ErrorStr);
                } catch (IOException ex2) {
                }
                return streamStatus;
            } finally {
                httpget.releaseConnection();

                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException ex3) {
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException ex4) {
                    }
                }
            }
        }

        //content adaptation
        String ua = request.getHeader("User-Agent");

        /*if (download.getContentItem().gettype().intValue() == IMAGE) {
        item = adaptContent(item, ua, download.getContentItem().
        getFormat().getFileExt());
        }*/

        //stream bytes as a response
        try {
            //resp.setContentType(formatMIMEType);
            int length = item.length;
            length = length
                    + new String("--rmcs\r\nContent-Type: " + formatMIMEType
                    + "\r\nContent-Transfer-Encoding: binary\r\n\r\n\r\n--rmcs--").length();

            //resp.setContentLength(length);
            // Use Buffered Stream for reading/writing.
            InputStream in = new ByteArrayInputStream(item);
            isr = new BufferedInputStream(in);
            bos = new BufferedOutputStream(outstr);

            //Check to see what type of DRM should be applied
            //If we find support for oma_v_1_0_forwardlock
            //if (canSupportDrmFwdLck (download.getPhoneId ())) {

            //---- This works. Do not touch!!-----
            resp.setHeader("Content-Type",
                    "application/vnd.oma.drm.message; boundary=rmcs");
            resp.setContentType("application/vnd.oma.drm.message");
            resp.setContentLength(length);
            //bos.write("\r\n".getBytes());
            //bos.write("\r\n".getBytes());
            bos.write("--rmcs".getBytes());
            bos.write("\r\n".getBytes());
            bos.write(new String("Content-Type: " + formatMIMEType).getBytes());
            bos.write("\r\n".getBytes());
            bos.write("Content-Transfer-Encoding: binary".getBytes());
            bos.write("\r\n".getBytes());
            bos.write("\r\n".getBytes());

            // Simple read/write loop.
            for (int i = 0; i < item.length; i++) {
                bos.write(item[i]);
            }

            bos.write("\r\n".getBytes());
            bos.write("--rmcs--".getBytes());
            //bos.write("\r\n".getBytes());
            bos.flush();

            streamStatus = true;
            //} else {
            //    //We didn't find support for oma_v_1_0_forwardlock
            //    //So use "Phase 0" Forward Lock. Will not fail on older devices
            //    resp.setHeader ("Content-Type", formatMIMEType);
            //    resp.setHeader ("x-drm", "noforward");
            //    resp.setContentType (formatMIMEType);
            //    for (int i = 0; i < item.length; i++) {
            //        bos.write (item[i]);
            //    }

            //    streamStatus = true;
            // }
        } catch (Exception e) {
            streamStatus = false;
            e.printStackTrace();
            ErrorStr = "Error Streaming the Data";
            try {
                outstr.print(ErrorStr);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return streamStatus;
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }

                if (bos != null) {
                    bos.close();
                }
                if (outstr != null) {
                    outstr.flush();
                    outstr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return streamStatus;
    }

    private boolean streamBinaryDataNoDrm(ContentItem content,
            HttpServletResponse resp,
            HttpServletRequest request) throws
            Exception {

        System.out.println( new Date() + ": Streaming with DRM disabled...");
        boolean streamStatus = false;
        String ErrorStr = null;
        String formatMIMEType = content.getFormat().getMimeType();
        //file headers
        resp.setContentType(formatMIMEType);
        resp.setHeader("Content-Disposition", "attachment;filename=" + content.gettitle() + "." + content.getFormat().getFileExt());
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        ServletOutputStream outstr = resp.getOutputStream();
        byte[] item = null;

        if (content.islocal()) {
            System.out.println( new Date() + ": Sending content from RMCS server");
            com.rancard.mobility.contentserver.uploadsBean upload = new com.rancard.mobility.contentserver.uploadsBean();
            upload.setid(content.getid());
            upload.setlist_id(content.getListId());

            try {
                upload = new RepositoryManager().fetchFile(upload.getlist_id(),
                        upload.getid());
            } catch (Exception ex1) {
                return streamStatus;
            }

            item = upload.getDataStream();
        } else {
            System.out.println( new Date() + ": Sending content from external source with url = "+ content.getDownloadUrl());
            String urlstr = content.getDownloadUrl();
            HttpClient httpclient = new HttpClient();
            GetMethod httpget = new GetMethod(urlstr);

            try {
                int statusCode = httpclient.executeMethod(httpget);
                if (statusCode != HttpStatus.SC_OK) {
                    throw new HttpException(new Integer(statusCode).toString());
                }

                long resplength = httpget.getResponseContentLength();
                Long size = new Long(resplength);
                int length = size.intValue();

                resp.setContentLength(length);
                // Use Buffered Stream for reading/writing.
                InputStream in = httpget.getResponseBodyAsStream();
                bis = new BufferedInputStream(in);
                bos = new BufferedOutputStream(resp.getOutputStream());

                item = new byte[length];
                int bytesRead = 0;
                while (-1 != (bytesRead = bis.read(item, 0, item.length))) {
                }
            } catch (Exception e) {
                System.out.println(new Date() + ": Exception> " + e.getMessage());
                streamStatus = false;
                e.printStackTrace();
                ErrorStr = "Error Streaming the Data";
                try {
                    resp.getOutputStream().print(ErrorStr);
                } catch (IOException ex2) {
                }
                return streamStatus;
            } finally {
                httpget.releaseConnection();

                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException ex3) {
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException ex4) {
                    }
                }
            }
        }

        //content adaptation
        String ua = request.getHeader("User-Agent");

        /*if (download.getContentItem().gettype().intValue() == IMAGE) {
        item = adaptContent(item, ua, download.getContentItem().
        getFormat().getFileExt());
        }*/

        //stream bytes as a response
        try {
            //resp.setContentType(formatMIMEType);
            outstr.write(item);
            streamStatus = true;
        } catch (Exception e) {
            streamStatus = false;
            System.out.println(new Date() + ":Exception> " + e.getMessage());
            outstr.print("Error Streaming the Data");
            return streamStatus;
        } finally {
            try {
                if (outstr != null) {
                    outstr.flush();
                    outstr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return streamStatus;
    }

    private boolean canSupportDrmFwdLck(String ua) throws Exception {
        boolean canSupport = true;
        String phoneId = new String();
        if (ua == null) {
            phoneId = "";
        } else {
            phoneId = ua;
        }

        if (!phoneId.equals("")) {
            FonCapabilityMtrx fcm = null;
            String capaValue = null;

            try {
                //fcm = new com.rancard.mobility.common.FonCapabilityMtrx (wurflpath + "wurfl.xml"); -- Deprecated
                //logging statements
                System.out.println("Setting up compatibility matrix");
                //end of logging
                fcm = (FonCapabilityMtrx) this.getServletContext().getAttribute("capabilitiesMtrx");
                if (fcm == null) {
                    //logging statements
                    System.out.println("Setup of compatibility matrix DONE");
                    //end of logging
                    throw new Exception();
                }
                capaValue = fcm.getCapabilitiesManager().
                        getCapabilityForDevice(
                        fcm.getUAManager().getDeviceIDFromUA(
                        phoneId),
                        "oma_v_1_0_forwardlock");
                if (capaValue.equals("true")) {
                    canSupport = true;
                } else {
                    canSupport = false;
                }

            } catch (Exception e) {
            }
        }
        return canSupport;
    }

    private byte[] adaptContent(byte[] dataStream, String ua, String ext) throws Exception {

        String phoneId = new String();
        int width = 90;
        int height = 35;

        if (ua == null) {
            phoneId = "";
        } else {
            phoneId = ua;
        }

        if (!phoneId.equals("")) {
            FonCapabilityMtrx fcm = null;

            try {
                fcm = new FonCapabilityMtrx();
                width = Integer.parseInt(fcm.getCapabilitiesManager().
                        getCapabilityForDevice(
                        fcm.getUAManager().
                        getDeviceIDFromUA(phoneId),
                        "max_image_width"));

                height = Integer.parseInt(fcm.getCapabilitiesManager().
                        getCapabilityForDevice(
                        fcm.getUAManager().
                        getDeviceIDFromUA(phoneId),
                        "max_image_height"));

            } catch (Exception e) {
            }
        }

        //do adaptation here
        //--------------------
        dataStream = ContentAdapter.resizeImage(dataStream, width, height, ext);
        //--------------------

        return dataStream;
    }

    private boolean updateBandwidthAllocation(Transaction download) {
        boolean done = true;
        com.rancard.mobility.contentprovider.User user = new com.rancard.mobility.contentprovider.User();
        CPSite initSite = download.getSite();
        ContentItem item = download.getContentItem();

        try {
            user = user.viewDealer(initSite.getCpId());
            if (user != null) {
                System.out.println("Updating bandwidth allocation for " + user.getName());

                double bandwidth = user.getBandwidthBalance();
                System.out.println("User bandwidth: " + bandwidth);
                System.out.println("Item size: " + item.getSize());
                if (bandwidth - item.getSize() < 0) {
                    throw new Exception(Feedback.BANDWIDTH_EXCEEDED);
                } else {
                    System.out.println("Site has enough bandwidth");
                    double result = bandwidth - item.getSize();
                    user.updateDealer(user.getId(), result, user.getInboxBalance(), user.getOutboxBalance());
                }
            } else {
                throw new Exception(Feedback.MISSING_INVALID_PROV_ID);
            }
        } catch (Exception e) {
            done = false;
        }
        return done;
    }
    /*public static void main(String[] args){
    try{
    downloadcontent dc = new downloadcontent();
    System.out.println("with null: " + dc.canSupportDrmFwdLck(null));
    System.out.println("with \"\": " + dc.canSupportDrmFwdLck(""));
    System.out.println("with nokia_6680_ver1: " + dc.canSupportDrmFwdLck("nokia_6680_ver1"));
    System.out.println("with acer_pro80_ver1: " + dc.canSupportDrmFwdLck("acer_pro80_ver1"));
    System.out.println("with amoi_a9b_ver1: " + dc.canSupportDrmFwdLck("amoi_a9b_ver1"));

    }catch(Exception e){
    System.out.println(e.getMessage());
    }
    }*/
}
