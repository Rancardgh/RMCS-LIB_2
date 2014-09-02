package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.Feedback;
import com.rancard.common.Message;
import com.rancard.mobility.common.FonCapabilityMtrx;
import com.rancard.mobility.contentprovider.User;
import com.rancard.mobility.contentserver.CPSite;
import com.rancard.mobility.contentserver.ContentAdapter;
import com.rancard.mobility.contentserver.ContentItem;
import com.rancard.mobility.contentserver.Format;
import com.rancard.mobility.contentserver.RepositoryManager;
import com.rancard.mobility.contentserver.Transaction;
import com.rancard.mobility.contentserver.uploadsBean;
import com.unwiredtec.rtcreator.Ringtone;
import com.unwiredtec.rtcreator.RingtoneConvertException;
import com.unwiredtec.rtcreator.RingtoneParseException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.wurfl.wurflapi.CapabilityMatrix;
import net.sourceforge.wurfl.wurflapi.UAManager;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class downloadcontent
        extends HttpServlet {
    private static final String CONTENT_TYPE = "text/vnd.wap.wml";
    private static final int IMAGE = 5;

    public void init()
            throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String message = "";
        String siteType = "2";

        String s = request.getProtocol().toLowerCase();
        s = s.substring(0, s.indexOf("/")).toLowerCase();

        String baseUrl = getServletContext().getInitParameter("contentServerPublicURL");
        Message replyPg = new Message();
        PrintWriter out = null;


        Transaction download = new Transaction();


        String ticketID = request.getParameter("ticketId");
        if (ticketID == null) {
            ticketID = (String) request.getAttribute("ticketId");
        }
        String supportsDrm = request.getParameter("drm");
        if (supportsDrm == null) {
            supportsDrm = (String) request.getAttribute("drm");
        }
        String lang = "";
        if ((lang == null) || (lang.equals(""))) {
            lang = "en";
        }
        Feedback feedback = (Feedback) getServletContext().getAttribute("feedback_" + lang);
        if (feedback == null) {
            try {
                feedback = new Feedback();
            } catch (Exception e) {
            }
        }
        System.out.println("Received request to complete download with transaction ID: " + ticketID);
        System.out.println("Date received: " + Calendar.getInstance().getTime().toString());
        if (ticketID == null) {
            ticketID = "";
        }
        if ((supportsDrm == null) || (supportsDrm.length() <= 0)) {
            supportsDrm = "no";
        }
        try {
            System.out.println("Looking up transaction details for transaction ID: " + ticketID + " ......");

            download = Transaction.viewTransaction(ticketID);

            System.out.println("Found transaction details for transaction ID: " + ticketID);
            System.out.println("user's mobile number: " + download.getSubscriberMSISDN());
            System.out.println("request came from site with ID: " + download.getSiteId());
        } catch (Exception e) {
            System.out.println("Could not find transaction details for transaction ID: " + ticketID);
            System.out.println("Exception thrown carries message: " + e.getMessage());


            out = response.getWriter();
            try {
                if (siteType.equals("2")) {
                    message = feedback.getUserFriendlyDescription("3000");
                } else {
                    message = feedback.formDefaultMessage("3000");
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            out.println(message);
            return;
        }
        if (!download.getDownloadCompleted()) {
            try {
                if (updateBandwidthAllocation(download) == true) {
                    System.out.println("Streaming data....");


                    streamData(response, request, download, supportsDrm);

                    System.out.println("Streaming data complete....");
                } else {
                    throw new Exception("4004");
                }
            } catch (ConnectException ex) {
                out = response.getWriter();
                try {
                    if (siteType.equals("2")) {
                        message = feedback.getUserFriendlyDescription("5003");
                    } else {
                        message = feedback.formDefaultMessage("5003");
                    }
                } catch (Exception e) {
                    message = ex.getMessage();
                }
                out.println(message);
                return;
            } catch (HttpException e) {
                out = response.getWriter();
                try {
                    if (siteType.equals("2")) {
                        message = feedback.getUserFriendlyDescription("5003");
                    } else {
                        message = feedback.formDefaultMessage("5003");
                    }
                } catch (Exception ex) {
                    message = ex.getMessage();
                }
                out.println(message);
                return;
            } catch (IOException e) {
                out = response.getWriter();
                try {
                    if (siteType.equals("2")) {
                        message = feedback.getUserFriendlyDescription("5003");
                    } else {
                        message = feedback.formDefaultMessage("5003");
                    }
                } catch (Exception ex) {
                    message = ex.getMessage();
                }
                out.println(message);
                return;
            } catch (Exception e) {
                try {
                    if (siteType.equals("2")) {
                        message = feedback.getUserFriendlyDescription(e.getMessage());
                    } else {
                        message = feedback.formDefaultMessage(e.getMessage());
                    }
                } catch (Exception ex) {
                    message = ex.getMessage();
                }
                out = response.getWriter();
                out.println(message);
                return;
            }
        } else {
            try {
                if (siteType.equals("2")) {
                    message = feedback.getUserFriendlyDescription("3001");
                } else {
                    message = feedback.formDefaultMessage("3001");
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            out = response.getWriter();
            out.println(message);
            return;
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy() {
    }

    public void streamData(HttpServletResponse response, HttpServletRequest request, Transaction download, String supportsDrm)
            throws Exception {
        if (download.getFormat().getPushBearer().equals("WAP")) {
            System.out.println(new Date() + ":Request to streamBinaryConent...");
            if (supportsDrm.equalsIgnoreCase("yes")) {
                if (streamBinaryData(download, response, request)) {
                    Transaction.updateDownloadStatus(download.getTicketID(), true);
                } else {
                    System.out.println(new Date() + ":Unable to streamBinaryData: Feedback.NO_CONTENT_AT_LOCATION");

                    throw new Exception("1001");
                }
            } else if (streamBinaryDataNoDrm(download, response, request)) {
                Transaction.updateDownloadStatus(download.getTicketID(), true);
            } else {
                System.out.println(new Date() + ":Unable to streamBinaryData: Feedback.NO_CONTENT_AT_LOCATION");

                throw new Exception("1001");
            }
        } else {
            System.out.println(new Date() + ":Request to stream Character based content...");
            if (!download.getContentItem().islocal()) {
                if (streamCharacterData(download.getContentItem().getDownloadUrl(), download.getFormat().getMimeType(), new PrintWriter(response.getOutputStream()), response, request)) {
                    Transaction.updateDownloadStatus(download.getTicketID(), true);
                } else {
                    System.out.println(new Date() + ":Unable to stream Character based content:Feedback.NO_CONTENT_AT_LOCATION");

                    throw new Exception("1001");
                }
            } else {
                uploadsBean upload = new uploadsBean();
                upload.setid(download.getContentItem().getid());
                upload.setlist_id(download.getContentItem().getListId());
                new RepositoryManager();
                upload = RepositoryManager.fetchFile(upload.getlist_id(), upload.getid());
                if (streamCharacterData(upload.getDataStream(), download.getFormat().getMimeType(), new PrintWriter(response.getOutputStream()), response, request)) {
                    Transaction.updateDownloadStatus(download.getTicketID(), true);
                } else {
                    throw new Exception("1001");
                }
            }
        }
    }

    private boolean streamCharacterData(String urlstr, String formatMIMEType, PrintWriter outstr, HttpServletResponse resp, HttpServletRequest request) {
        String ErrorStr = null;
        boolean streamStatus = true;
        try {
            resp.setContentType(formatMIMEType);
            InputStream in = null;
            try {
                URL url = new URL(urlstr);
                URLConnection urlc = url.openConnection();
                int length = urlc.getContentLength();
                in = urlc.getInputStream();
                resp.setContentLength(length);
                if (request.getHeader("accept").indexOf("vnd.oma.drm.message") != -1) {
                    resp.setHeader("Content-Type", "application/vnd.oma.drm.message; boundary=foo");


                    outstr.println();
                    outstr.println("--rmcs");
                    outstr.println("Content-Type: " + formatMIMEType);
                    outstr.println("");
                    int ch;
                    while ((ch = in.read()) != -1) {
                        outstr.print((char) ch);
                    }
                    outstr.println();
                    outstr.println("--rmcs--");
                    outstr.println();
                } else {
                    resp.setHeader("Content-Type", formatMIMEType);
                    resp.setHeader("x-drm", "noforward");
                    int ch;
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

    private boolean streamCharacterData(byte[] item, String formatMIMEType, PrintWriter outstr, HttpServletResponse resp, HttpServletRequest request) {
        String ErrorStr = null;
        boolean streamStatus = true;
        try {
            resp.setContentType(formatMIMEType);
            InputStream in = null;
            try {
                in = new ByteArrayInputStream(item);
                int ch;
                if (request.getHeader("accept").indexOf("vnd.oma.drm.message") != -1) {
                    resp.setHeader("Content-Type", "application/vnd.oma.drm.message; boundary=foo");


                    outstr.println();
                    outstr.println("--rmcs");
                    outstr.println("Content-Type: " + formatMIMEType);
                    outstr.println("");

                    while ((ch = in.read()) != -1) {
                        outstr.print((char) ch);
                    }
                    outstr.println();
                    outstr.println("--rmcs--");
                    outstr.println();
                } else {
                    resp.setHeader("Content-Type", formatMIMEType);
                    resp.setHeader("x-drm", "noforward");
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

    public byte[] convert(byte[] in, String format)
            throws Exception {
        byte[] convertedTone = null;
        try {
            Ringtone tone = Ringtone.openRingtone(in);
            convertedTone = tone.getRingtoneBytes(format);
        } catch (RingtoneConvertException ex) {
            throw new Exception("Error converting tones");
        } catch (RingtoneParseException ex) {
        }
        return convertedTone;
    }

    private boolean streamBinaryData(Transaction download, HttpServletResponse resp, HttpServletRequest request)
            throws Exception {
        boolean streamStatus = false;
        String ErrorStr = null;
        String formatMIMEType = download.getContentItem().getFormat().getMimeType();

        BufferedOutputStream bos = null;
        BufferedInputStream isr = null;
        BufferedInputStream bis = null;
        ServletOutputStream outstr = resp.getOutputStream();

        byte[] item = null;
        if (download.getContentItem().islocal()) {
            uploadsBean upload = new uploadsBean();
            upload.setid(download.getContentItem().getid());
            upload.setlist_id(download.getContentItem().getListId());
            try {
                new RepositoryManager();
                upload = RepositoryManager.fetchFile(upload.getlist_id(), upload.getid());
            } catch (Exception ex1) {
                return streamStatus;
            }
            item = upload.getDataStream();
        } else {
            String urlstr = download.getContentItem().getDownloadUrl();
            HttpClient httpclient = new HttpClient();
            GetMethod httpget = new GetMethod(urlstr);
            try {
                int statusCode = httpclient.executeMethod(httpget);
                if (statusCode != 200) {
                    throw new HttpException(new Integer(statusCode).toString());
                }
                long resplength = httpget.getResponseContentLength();
                Long size = new Long(resplength);
                int length = size.intValue();

                resp.setContentLength(length);

                InputStream in = httpget.getResponseBodyAsStream();
                bis = new BufferedInputStream(in);
                bos = new BufferedOutputStream(resp.getOutputStream());

                item = new byte[length];
                int bytesRead = 0;
                while (-1 != (bytesRead = bis.read(item, 0, item.length))) {
                }
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
                String ua = request.getHeader("User-Agent");
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
        try {
            int length = item.length;
            length += new String("--rmcs\r\nContent-Type: " + formatMIMEType + "\r\nContent-Transfer-Encoding: binary\r\n\r\n\r\n--rmcs--").length();


            InputStream in = new ByteArrayInputStream(item);
            isr = new BufferedInputStream(in);
            bos = new BufferedOutputStream(outstr);


            resp.setHeader("Content-Type", "application/vnd.oma.drm.message; boundary=rmcs");

            resp.setContentType("application/vnd.oma.drm.message");
            resp.setContentLength(length);


            bos.write("--rmcs".getBytes());
            bos.write("\r\n".getBytes());
            bos.write(new String("Content-Type: " + formatMIMEType).getBytes());
            bos.write("\r\n".getBytes());
            bos.write("Content-Transfer-Encoding: binary".getBytes());
            bos.write("\r\n".getBytes());
            bos.write("\r\n".getBytes());
            for (int i = 0; i < item.length; i++) {
                bos.write(item[i]);
            }
            bos.write("\r\n".getBytes());
            bos.write("--rmcs--".getBytes());

            bos.flush();

            return true;
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
    }

    private boolean streamBinaryDataNoDrm(Transaction download, HttpServletResponse resp, HttpServletRequest request)
            throws Exception {
        boolean streamStatus = false;
        String ErrorStr = null;
        String formatMIMEType = download.getContentItem().getFormat().getMimeType();

        resp.setContentType(formatMIMEType);
        resp.setHeader("Content-Disposition", "attachment;filename=" + download.getContentItem().gettitle() + "." + download.getContentItem().getFormat().getFileExt());
        BufferedOutputStream bos = null;
        BufferedInputStream isr = null;
        BufferedInputStream bis = null;
        ServletOutputStream outstr = resp.getOutputStream();

        byte[] item = null;
        if (download.getContentItem().islocal()) {
            uploadsBean upload = new uploadsBean();
            upload.setid(download.getContentItem().getid());
            upload.setlist_id(download.getContentItem().getListId());
            try {
                new RepositoryManager();
                upload = RepositoryManager.fetchFile(upload.getlist_id(), upload.getid());
            } catch (Exception ex1) {
                return streamStatus;
            }
            item = upload.getDataStream();
        } else {
            String urlstr = download.getContentItem().getDownloadUrl();
            HttpClient httpclient = new HttpClient();
            GetMethod httpget = new GetMethod(urlstr);
            try {
                int statusCode = httpclient.executeMethod(httpget);
                if (statusCode != 200) {
                    throw new HttpException(new Integer(statusCode).toString());
                }
                long resplength = httpget.getResponseContentLength();
                Long size = new Long(resplength);
                int length = size.intValue();

                resp.setContentLength(length);

                InputStream in = httpget.getResponseBodyAsStream();
                bis = new BufferedInputStream(in);
                bos = new BufferedOutputStream(resp.getOutputStream());

                item = new byte[length];
                int bytesRead = 0;
                while (-1 != (bytesRead = bis.read(item, 0, item.length))) {
                }
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
                String ua = request.getHeader("User-Agent");
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
        try {
            InputStream in = new ByteArrayInputStream(item);
            isr = new BufferedInputStream(in);
            bos = new BufferedOutputStream(outstr);
            for (int i = 0; i < item.length; i++) {
                bos.write(item[i]);
            }
            bos.flush();

            return true;
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
    }

    private boolean canSupportDrmFwdLck(String ua)
            throws Exception {
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
                System.out.println("Setting up compatibility matrix");

                fcm = (FonCapabilityMtrx) getServletContext().getAttribute("capabilitiesMtrx");
                if (fcm == null) {
                    System.out.println("Setup of compatibility matrix DONE");

                    throw new Exception();
                }
                capaValue = fcm.getCapabilitiesManager().getCapabilityForDevice(fcm.getUAManager().getDeviceIDFromUA(phoneId), "oma_v_1_0_forwardlock");
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

    private byte[] adaptContent(byte[] dataStream, String ua, String ext)
            throws Exception {
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
                width = Integer.parseInt(fcm.getCapabilitiesManager().getCapabilityForDevice(fcm.getUAManager().getDeviceIDFromUA(phoneId), "max_image_width"));


                height = Integer.parseInt(fcm.getCapabilitiesManager().getCapabilityForDevice(fcm.getUAManager().getDeviceIDFromUA(phoneId), "max_image_height"));
            } catch (Exception e) {
            }
        }
        dataStream = ContentAdapter.resizeImage(dataStream, width, height, ext);


        return dataStream;
    }

    private boolean updateBandwidthAllocation(Transaction download) {
        boolean done = true;
        User user = new User();
        CPSite initSite = download.getSite();
        ContentItem item = download.getContentItem();
        try {
            user = user.viewDealer(initSite.getCpId());
            if (user != null) {
                System.out.println("Updating bandwidth allocation for " + user.getName());

                double bandwidth = user.getBandwidthBalance();
                System.out.println("User bandwidth: " + bandwidth);
                System.out.println("Item size: " + item.getSize());
                if (bandwidth - item.getSize().longValue() < 0.0D) {
                    throw new Exception("4004");
                }
                System.out.println("Site has enough bandwidth");
                double result = bandwidth - item.getSize().longValue();
                user.updateDealer(user.getId(), result, user.getInboxBalance(), user.getOutboxBalance());
            } else {
                throw new Exception("10004");
            }
        } catch (Exception e) {
            done = false;
        }
        return done;
    }
}
