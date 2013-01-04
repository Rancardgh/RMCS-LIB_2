package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.mobility.contentserver.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;


public class redeemticket extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html";

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        String ticketId = request.getParameter("ticketId");
        com.rancard.common.Message reply = new com.rancard.common.Message();
        PrintWriter out = response.getWriter();

        String s = request.getProtocol().toLowerCase();
        s = s.substring(0, s.indexOf("/")).toLowerCase();
        String baseUrl = s + "://" + request.getServerName() + ":" +
                         request.getServerPort() + request.getContextPath() +
                         "/";

        //check for missing ticket
        try {
            if (ticketId == null || ticketId.equals("")) {
                throw new Exception();
            }
        } catch (Exception e) {
            reply.setMessage(com.rancard.common.Feedback.MISSING_INVALID_TICKET_ID);
            reply.setStatus(false);
            out.println(reply.getMessage());
            return;
        }

        //ticket has been provided
        com.rancard.mobility.contentserver.Transaction failedDownload =
                new com.rancard.mobility.contentserver.Transaction();

        String formatId = new String();
        //check if transaction with the provided ticket exists
        try {
            failedDownload = failedDownload.viewTransaction(ticketId);
            formatId = failedDownload.getTicketID().split("-")[1]; ;
        } catch (Exception e) {
            reply.setMessage(com.rancard.common.Feedback.EXPIRED_TICKET_ID);
            reply.setStatus(false);
            out.println(reply.getMessage());
            return;
        }

        //transaction exists. check whether download was completed
        if (!failedDownload.getTicketID().equals("") &&
            !failedDownload.getDownloadCompleted()) {
            //transaction was not completed. new download can proceed

            //retrieve content
            com.rancard.mobility.contentserver.ContentItem item =
                    failedDownload.getContentItem();

            String initDownloadRUL = baseUrl + "senddownloadrequest?id=" +
                                     item.getid() + "&listId=" + item.getListId() +
                                     "&formatId=" + formatId + "&to=" +
                                     failedDownload.getSubscriberMSISDN() +
                                     "&pin=" + failedDownload.getPin() +
                                     "&phoneId=" + failedDownload.getPhoneId();

            HttpClient httpclient = new HttpClient();
            GetMethod httpget = new GetMethod(initDownloadRUL);

            try {
                int statusCode = httpclient.executeMethod(httpget);
                if (statusCode == HttpStatus.SC_BAD_REQUEST ||
                    statusCode == HttpStatus.SC_CONFLICT ||
                    statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR ||
                    statusCode == HttpStatus.SC_REQUEST_TIMEOUT) {
                    throw new HttpException(new Integer(statusCode).toString());
                }
                failedDownload.removeTransaction(failedDownload.getTicketID());
            } catch (Exception e) {
                reply.setMessage("Error xxx: Server Error." + e.getMessage());
                reply.setStatus(false);
                out.println(reply.getMessage());

            } finally {
                httpget.releaseConnection();
            }
        } else {
            //transaction was completed. new download cannot proceed
            reply.setMessage(com.rancard.common.Feedback.MISSING_INVALID_TICKET_ID);
            reply.setStatus(false);
            out.println(reply.getMessage());
        }
    }

    //Process the HTTP Post request
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }

    //Clean up resources
    public void destroy() {
    }
}
