package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.mobility.contentserver.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.apache.commons.httpclient.HttpException;
import com.rancard.mobility.contentserver.uploadsBean;
import java.sql.PreparedStatement;
import com.rancard.common.DConnect;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

public class ContentPreview extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html";
    private static final String DOC_TYPE =
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
            "  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        String s = request.getProtocol().toLowerCase();
        s = s.substring(0, s.indexOf("/")).toLowerCase();
        //String baseUrl = s + "://" + request.getServerName() + ":" +
                        // request.getServerPort() + request.getContextPath() +
                         //"/";
        String baseUrl = this.getServletContext().getInitParameter("contentServerPublicURL");//

        com.rancard.common.Message replyPg = new com.rancard.common.Message();
        // this represents an instance of a transaction
        String listId = request.getParameter("listId");
        String contentId = request.getParameter("id");

        if (listId == null) {
            listId = "";
        }
        if (contentId == null) {
            contentId = "";
        }

        uploadsBean contentItem = null;
        try {
            contentItem =  RepositoryManager.fetchFile(listId, contentId);
            int typeId = com.rancard.util.ExtManager.getTypeForFormat(contentItem.getFormat());

            String mimetype = new String();
            if (typeId == 1 || typeId == 2 || typeId == 3) {
                mimetype = "application/x-shockwave-flash";
            } else if(typeId == 4 || typeId == 5 || typeId == 6 ||  typeId == 7){
                mimetype = "image/jpeg";
            } else if(typeId == 9){
                mimetype = "image/gif";
            }
            
            streamBinaryData(contentItem.getPreviewStream(),
                             mimetype, response);
        } catch (Exception ex) {
            response.sendError(response.SC_NOT_FOUND);
        }finally{
            contentItem = null;
            replyPg = null;
        }
    }

    //Process the HTTP Post request
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }

    /*public uploadsBean fetchFile(java.lang.String list_id, java.lang.String id) throws
            Exception, SQLException {
        uploadsBean uploads = new uploadsBean();
        java.io.InputStream filein;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            SQL =
     "select previewfile , filename from  uploads where list_id=? and id=?";
            prepstat = con.prepareStatement(SQL);

            prepstat.setString(1, list_id);
            prepstat.setString(2, id);
            rs = prepstat.executeQuery();
            if (!rs.next()) {

                throw new Exception("File not found");

            }
            uploads.setid(id);
            uploads.setlist_id(list_id);
            uploads.setfilename(rs.getString("filename"));
            java.sql.Blob b = rs.getBlob("previewfile");
     uploads.setDataStream(b.getBytes(1, new Long(b.length()).intValue()));
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (prepstat != null) {
                    prepstat.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqlee) {}
        }

        return uploads;
         }*/

    private boolean streamBinaryData(byte[] by,
                                     String formatMIMEType,
                                     HttpServletResponse response) throws
            Exception {

        boolean streamStatus = true;
        String ErrorStr = null;
        BufferedOutputStream bos = null;
        //Setup the ServletOutput Stream
        ServletOutputStream sout = response.getOutputStream();
        try {
            response.setContentType(formatMIMEType);
            //byte by[] = fetchFile(listId, id).getDataStream();
            sout.write(by);
            sout.flush();
        } catch (Exception e) {
            streamStatus = false;
            e.printStackTrace();
            ErrorStr = "Error Streaming the Data";
            sout.println(ErrorStr);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return streamStatus;
    }

    //Clean up resources
    public void destroy() {
    }
}
