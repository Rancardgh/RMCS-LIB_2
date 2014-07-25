package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.Feedback;
import com.rancard.common.uidGen;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.InfoService;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserServiceTransaction;
import com.rancard.util.URLUTF8Encoder;
import com.rancard.util.payment.PaymentManager;
import com.rancard.util.payment.PricePoint;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class sendinfo
        extends HttpServlet
        implements RequestDispatcher {
    public void init()
            throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
        }
        String fullContextPath = "http://192.168.1.243:81" + request.getContextPath();
        boolean skipMessagingFromCallBack = false;


        PrintWriter out = response.getWriter();
        CPConnections cnxn = new CPConnections();

        String provId = (String) request.getAttribute("acctId");
        provId = provId == null ? request.getParameter("acctId") : provId;


        String siteType = (String) request.getAttribute("site_type");
        siteType = siteType == null ? request.getParameter("site_type") : siteType;

        String lang = (String) request.getAttribute("default_lang");
        String override_msg = (String) request.getAttribute("override_msg");
        String override_keyword = (String) request.getAttribute("override_keyword");
        lang = lang == null ? request.getParameter("default_lang") : lang;
        String shortcode = request.getParameter("dest") == null ? "" : request.getParameter("dest");
        String siteId = request.getParameter("siteId") == null ? "" : request.getParameter("siteId");

        String msisdn = request.getParameter("msisdn");


        String kw = request.getParameter("keyword");
        String alreadyBilled = request.getParameter("pre_billed");


        System.out.println(new java.util.Date() + ":@com.rancard.mobility.contentserver.serviceinterfaces.sendinfo..");
        if (kw == null) {
            kw = (String) request.getAttribute("keyword");
        }
        if (alreadyBilled == null) {
            alreadyBilled = (String) request.getAttribute("pre_billed");
        }
        if ((override_keyword != null) && (!override_keyword.equals(""))) {
            kw = override_keyword;
        }
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
        String message = "";
        if ((provId == null) || (provId.equals(""))) {
            try {
                if (siteType.equals("2")) {
                    message = feedback.getUserFriendlyDescription("10004");
                } else {
                    message = feedback.formDefaultMessage("10004");
                }
                System.out.println(new java.util.Date() + ":Feedback.MISSING_INVALID_PROV_ID: " + message);
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
            if (!isAsciiPrintable) {
                System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                request.setAttribute("X-Kannel-Coding", "2");
                if (request.getAttribute("X-Kannel-Coding") != null) {
                    System.out.println("Request contains X-Kannel-Coding attribute");
                }
            }
            out.println(message);
            return;
        }
        if ((kw == null) || (kw.equals(""))) {
            try {
                if (siteType.equals("2")) {
                    message = feedback.getUserFriendlyDescription("10001");
                } else {
                    message = feedback.formDefaultMessage("10001");
                }
                System.out.println(new java.util.Date() + ":NO_SUCH_SERVICE (keyword empty): " + message);
            } catch (Exception ex) {
                message = ex.getMessage();

                System.out.println(new java.util.Date() + ":error: " + message);
            }
            boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
            if (!isAsciiPrintable) {
                System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                request.setAttribute("X-Kannel-Coding", "2");
                if (request.getAttribute("X-Kannel-Coding") != null) {
                    System.out.println("Request contains X-Kannel-Coding attribute");
                }
            }
            out.println(message);
            return;
        }
        System.out.println(new java.util.Date() + ":Info to be sent for keyword " + kw.toUpperCase() + " on account with ID " + provId);


        InfoService is = new InfoService();


        Calendar calendar = Calendar.getInstance();
        java.util.Date today = new java.util.Date(calendar.getTime().getTime());
        String dateString = com.rancard.util.Date.formatDate(today, "dd-MM-yyyy");
        is.setAccountId(provId);
        is.setKeyword(kw);
        is.setPublishDate(dateString);

        calendar = null;
        today = null;
        try {
            is.viewInfoService();
        } catch (Exception e) {
            System.out.println(new java.util.Date() + ":error: " + e.getMessage());
            try {
                if (siteType.equals("2")) {
                    message = feedback.getUserFriendlyDescription("10001");
                } else {
                    message = feedback.formDefaultMessage("10001");
                }
                System.out.println(new java.util.Date() + ":NO_SUCH_SERVICE: " + message);
            } catch (Exception ex) {
                message = ex.getMessage();

                System.out.println(new java.util.Date() + ":error: " + message);
            }
            boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
            if (!isAsciiPrintable) {
                System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                request.setAttribute("X-Kannel-Coding", "2");
                if (request.getAttribute("X-Kannel-Coding") != null) {
                    System.out.println("Request contains X-Kannel-Coding attribute");
                }
            }
            out.println(message);
            return;
        }
        String info = "";
        String compactInfo = "";
        if ((is.getMessage() == null) || (is.getMessage().equals(""))) {
            info = is.getDefaultMessage();
        } else {
            info = is.getMessage();
        }
        if ((msisdn != null) && (!msisdn.equals(""))) {
            String number = msisdn;
            if (number.indexOf("+") != -1) {
                StringBuffer sb = new StringBuffer(number);
                sb.deleteCharAt(number.indexOf("+"));
                number = sb.toString();

                sb = null;
            }
            number = number.trim();
            try {
                Long.parseLong(number);
                msisdn = "+" + number;
            } catch (NumberFormatException e) {
                System.out.println(new java.util.Date() + ":error parsing msisdn: " + e.getMessage());
                try {
                    if (siteType.equals("2")) {
                        message = feedback.getUserFriendlyDescription("2000");
                    } else {
                        message = feedback.formDefaultMessage("2000");
                    }
                    System.out.println(new java.util.Date() + ":MISSING_INVALID_MSISDN: " + message);
                } catch (Exception ex) {
                    message = ex.getMessage();

                    System.out.println(new java.util.Date() + ":error: " + message);
                }
                boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
                if (!isAsciiPrintable) {
                    System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                    request.setAttribute("X-Kannel-Coding", "2");
                    if (request.getAttribute("X-Kannel-Coding") != null) {
                        System.out.println("Request contains X-Kannel-Coding attribute");
                    }
                }
                out.println(message);
                return;
            }
            try {
                cnxn = CPConnections.getConnection(provId, msisdn);
            } catch (Exception e) {
                System.out.println(new java.util.Date() + ":error: " + e.getMessage());
                try {
                    if (siteType.equals("2")) {
                        message = feedback.getUserFriendlyDescription("8002");
                    } else {
                        message = feedback.formDefaultMessage("8002");
                    }
                    System.out.println(new java.util.Date() + ":UNSUPPORTED_NETWORK:" + message);
                } catch (Exception ex) {
                    message = ex.getMessage();

                    System.out.println(new java.util.Date() + ":" + message);
                }
                boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
                if (!isAsciiPrintable) {
                    System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                    request.setAttribute("X-Kannel-Coding", "2");
                    if (request.getAttribute("X-Kannel-Coding") != null) {
                        System.out.println("Request contains X-Kannel-Coding attribute");
                    }
                }
                out.println(message);
                return;
            }
            System.out.println(new java.util.Date() + ":" + msisdn + ":Checking if billing required for content...");

            boolean billed = false;
            String pricePointString = is.getPricing();
            if ((pricePointString == null) || ("".equals(pricePointString))) {
                pricePointString = "";
                billed = true;

                System.out.println(new java.util.Date() + ":" + msisdn + ":No pricePoint found on content. Billing not required!");
            }
            if ((info == null) || ("".equals(info))) {
                billed = true;

                info = "No info is currently available for " + is.getServiceName() + ". Please try again later.";

                System.out.println(new java.util.Date() + ": " + msisdn + ":No info currently available for:" + is.getServiceName() + "(" + is.getKeyword() + ")");
                if ((pricePointString != null) && (!"".equals(pricePointString))) {
                    pricePointString = "";
                    System.out.println(new java.util.Date() + ": " + msisdn + ":Do not bill subscriber:" + msisdn);
                }
            }
            if ((alreadyBilled != null) && ("1".equals(alreadyBilled))) {
                billed = true;
                pricePointString = "";
                System.out.println(new java.util.Date() + ": " + msisdn + ":Pre-billed request: Content already paid for. deliver content!");
            }
            if (!pricePointString.equals("")) {
                try {
                    System.out.println(new java.util.Date() + ": " + msisdn + ":About to bill...");

                    String[] itemPricePoints = pricePointString.split(",");
                    if ((itemPricePoints == null) || (itemPricePoints[0] == null) || (itemPricePoints[0].equals(""))) {
                        System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR:BILLING_MECH_FAILURE:PricePoint Missing!");
                        throw new Exception("11000");
                    }
                    PricePoint pricePoint = PaymentManager.viewPricePointFor(itemPricePoints, msisdn);
                    String pricePointId = pricePoint.getPricePointId();
                    System.out.println(new java.util.Date() + ":pricePoint ID:" + pricePointId);
                    if ((pricePointId == null) || ("".equals(pricePointId))) {
                        System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR: Invalid pricePointID(s):" + pricePointString);
                        billed = true;

                        throw new Exception("11000");
                    }
                    System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": Entering initiatePayment from sendinfo");

                    String transactionId = "";
                    if (pricePoint.getBillingMech().equals("4")) {
                        transactionId = uidGen.genUID("", 5);
                    } else if ((siteType.equals("1")) || (siteType.equals("0"))) {
                        transactionId = uidGen.genUID("MP-", 5);
                    } else {
                        transactionId = uidGen.genUID("OD-", 5);
                    }
                    String completeTransnxnUrl = fullContextPath + "/sendinfo_push.jsp?msisdn=" + URLEncoder.encode(msisdn, "UTF-8") + "&keyword=" + URLEncoder.encode(kw.toUpperCase(), "UTF-8") + "&alert_count=" + is.getMsgId() + "&dest=" + URLEncoder.encode(shortcode, "UTF-8") + "&siteId=" + URLEncoder.encode(siteId, "UTF-8") + "&transId=" + URLEncoder.encode(transactionId, "UTF-8");
                    if (pricePoint.getBillingMech().equals("3")) {
                        completeTransnxnUrl = completeTransnxnUrl + "&sender=KEYWORD";
                    }
                    UserServiceTransaction trans = new UserServiceTransaction();
                    trans.setAccountId(provId);

                    trans.setDate(new Timestamp(new java.util.Date().getTime()));
                    trans.setKeyword(kw.toUpperCase());
                    trans.setMsg("on-demand");
                    trans.setMsisdn(msisdn);
                    trans.setPricePoint(pricePointId);
                    trans.setTransactionId(transactionId);
                    trans.setIsBilled(0);
                    trans.setIsCompleted(0);
                    trans.setCallBackUrl(completeTransnxnUrl);

                    boolean transactionCreated = false;
                    int isCompleted = 0;
                    try {
                        ServiceManager.createTransaction(trans);
                        System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " created!");
                        transactionCreated = true;
                    } catch (Exception e) {
                        transactionCreated = false;
                        System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT created! Error message: " + e.getMessage());
                    }
                    if (transactionCreated) {
                        try {
                            billed = PaymentManager.initiatePayment(pricePoint, cnxn, msisdn, transactionId, "", completeTransnxnUrl, shortcode, trans.getKeyword());
                        } catch (Exception e) {
                            if (e.getMessage().equals("READ_TIMEOUT")) {
                                message = "We've received your request for a " + is.getServiceName() + " item. Please be patient while we process it.";
                                skipMessagingFromCallBack = false;
                                isCompleted = 0;
                            } else if (e.getMessage().equals("INSUFFICIENT_CREDIT")) {
                                message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please top up and then send " + is.getKeyword().toUpperCase() + " to " + shortcode;

                                skipMessagingFromCallBack = true;
                                isCompleted = 1;
                            } else if (e.getMessage().equals("ERROR")) {
                                message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please try again. Send " + is.getKeyword().toUpperCase() + " to " + shortcode + ". You've not been billed.";

                                isCompleted = 1;
                                skipMessagingFromCallBack = true;
                            }
                        }
                        request.setAttribute("x-kannel-header-binfo", transactionId);
                        System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": Completed initiatePayment from sendinfo with result: " + billed);
                        if (billed) {
                            trans.setIsBilled(1);
                            trans.setIsCompleted(1);
                        } else {
                            trans.setIsBilled(0);
                            trans.setIsCompleted(isCompleted);
                        }
                        try {
                            ServiceManager.updateTransaction(trans.getTransactionId(), trans.getIsCompleted(), trans.getIsBilled());
                            System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " updated!");
                        } catch (Exception e) {
                            System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT updated! Error message: " + e.getMessage());
                        }
                    }
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
                    boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
                    if (!isAsciiPrintable) {
                        System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                        request.setAttribute("X-Kannel-Coding", "2");
                        if (request.getAttribute("X-Kannel-Coding") != null) {
                            System.out.println("Request contains X-Kannel-Coding attribute");
                        }
                    }
                    out.println(message);
                    return;
                }
            }
            compactInfo = info.replaceAll("\r\n", ".");
            System.out.println(new java.util.Date() + ":Destination number: " + msisdn);
            System.out.println(new java.util.Date() + ":Info: " + compactInfo);


            System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": About to send content. billed: " + billed);
            if (billed) {
                if ((override_msg == null) || (override_msg.equals(""))) {
                    boolean isAsciiPrintable = StringUtils.isAsciiPrintable(compactInfo);
                    if (!isAsciiPrintable) {
                        System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                        request.setAttribute("X-Kannel-Coding", "2");
                        if (request.getAttribute("X-Kannel-Coding") != null) {
                            System.out.println("Request contains X-Kannel-Coding attribute");
                        }
                    }
                    out.println(compactInfo);
                } else {
                    boolean isAsciiPrintable = StringUtils.isAsciiPrintable(override_msg);
                    if (!isAsciiPrintable) {
                        System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                        request.setAttribute("X-Kannel-Coding", "2");
                        if (request.getAttribute("X-Kannel-Coding") != null) {
                            System.out.println("Request contains X-Kannel-Coding attribute");
                        }
                    }
                    out.println(override_msg);
                }
            } else {
                boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
                if (!isAsciiPrintable) {
                    System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                    request.setAttribute("X-Kannel-Coding", "2");
                    if (request.getAttribute("X-Kannel-Coding") != null) {
                        System.out.println("Request contains X-Kannel-Coding attribute");
                    }
                }
                out.println(message);
            }
            request.setAttribute("log_thirdPartyCPId", is.getOwnerId());


            is = null;

            return;
        }
        try {
            if (siteType.equals("2")) {
                message = feedback.getUserFriendlyDescription("2000");
            } else {
                message = feedback.formDefaultMessage("2000");
            }
            System.out.println(new java.util.Date() + ":MISSING_INVALID_MSISDN: " + message);
        } catch (Exception ex) {
            message = ex.getMessage();

            System.out.println(new java.util.Date() + ":error: " + message);
        }
        boolean isAsciiPrintable = StringUtils.isAsciiPrintable(message);
        if (!isAsciiPrintable) {
            System.out.println("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
            request.setAttribute("X-Kannel-Coding", "2");
            if (request.getAttribute("X-Kannel-Coding") != null) {
                System.out.println("Request contains X-Kannel-Coding attribute");
            }
        }
        out.println(message);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy() {
    }

    public void forward(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        doGet(req, resp);
    }

    public void include(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        doGet(req, resp);
    }

    public void updateOTTransaction(String kw, String provId, String alertCount, String msisdn, String pricePointId, int isBilled, int isCompleted, String transId) {
        String svrAddr = "";
        String transactionId = transId;
        UserServiceTransaction trans = null;


        trans = new UserServiceTransaction();
        svrAddr = "http://msg.rancardmobility.com:8080/ot.rms/sendsms?to=%2b2000&text=" + kw + "&conn=OT:5511&username=otsms&password=o1t1s1m1s1&serviceId=&price=0&from=" + URLUTF8Encoder.encode(msisdn);

        trans.setAccountId(provId);
        trans.setKeyword(kw);
        trans.setMsg(alertCount);
        trans.setMsisdn(msisdn);
        trans.setTransactionId(transactionId);
        trans.setCallBackUrl(svrAddr);
        trans.setPricePoint(pricePointId);
        trans.setIsBilled(isBilled);
        trans.setIsCompleted(isCompleted);

        System.out.println(new java.util.Date() + ":updating transaction for OT:transId=" + transactionId + ":msisdn=" + msisdn + ":keyword=" + kw + ":billed=" + isBilled + ":completed=" + isCompleted);
        try {
            ServiceManager.createTransaction(trans);
        } catch (Exception e) {
            System.out.println(new java.util.Date() + ":sendinfo:error updating OTTransaction:" + e.getMessage());
        }
    }
}
