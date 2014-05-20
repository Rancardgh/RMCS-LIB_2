package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.Message;
import com.rancard.mobility.contentserver.RepositoryManager;
import com.rancard.mobility.contentserver.uploadsBean;
import com.rancard.util.ExtManager;
import java.io.BufferedOutputStream;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContentPreview
        extends HttpServlet
{
    private static final String CONTENT_TYPE = "text/html";
    private static final String DOC_TYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";

    public void init()
            throws ServletException
    {}

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String s = request.getProtocol().toLowerCase();
        s = s.substring(0, s.indexOf("/")).toLowerCase();



        String baseUrl = getServletContext().getInitParameter("contentServerPublicURL");

        Message replyPg = new Message();

        String listId = request.getParameter("listId");
        String contentId = request.getParameter("id");
        if (listId == null) {
            listId = "";
        }
        if (contentId == null) {
            contentId = "";
        }
        uploadsBean contentItem = null;
        try
        {
            contentItem = RepositoryManager.fetchFile(listId, contentId);
            int typeId = ExtManager.getTypeForFormat(contentItem.getFormat());

            String mimetype = new String();
            if ((typeId == 1) || (typeId == 2) || (typeId == 3)) {
                mimetype = "application/x-shockwave-flash";
            } else if ((typeId == 4) || (typeId == 5) || (typeId == 6) || (typeId == 7)) {
                mimetype = "image/jpeg";
            } else if (typeId == 9) {
                mimetype = "image/gif";
            }
            streamBinaryData(contentItem.getPreviewStream(), mimetype, response);
        }
        catch (Exception ex)
        {
            response.sendError(404);
        }
        finally
        {
            contentItem = null;
            replyPg = null;
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doGet(request, response);
    }

    private boolean streamBinaryData(byte[] by, String formatMIMEType, HttpServletResponse response)
            throws Exception
    {
        boolean streamStatus = true;
        String ErrorStr = null;
        BufferedOutputStream bos = null;

        ServletOutputStream sout = response.getOutputStream();
        try
        {
            response.setContentType(formatMIMEType);

            sout.write(by);
            sout.flush();

        }
        catch (Exception e)
        {
            streamStatus = false;
            e.printStackTrace();
            ErrorStr = "Error Streaming the Data";
            sout.println(ErrorStr);
        }
        finally
        {
            try
            {
                if (bos != null) {
                    bos.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return streamStatus;
    }

    public void destroy() {}
}
