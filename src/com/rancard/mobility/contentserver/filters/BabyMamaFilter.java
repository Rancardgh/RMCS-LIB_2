package com.rancard.mobility.contentserver.filters;

import com.rancard.common.Metadata;
import com.rancard.common.MobileNetworkOperator;
import com.rancard.common.ServiceSubscription;
import com.rancard.common.SystemSMSQueue;
import com.rancard.common.key.ServiceSubscriptionKey;
import com.rancard.mobility.contentserver.BaseServlet;
import com.rancard.mobility.contentserver.EMF;
import com.rancard.util.DateUtil;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mustee on 4/8/2014.
 */
public class BabyMamaFilter extends BaseServlet implements Filter {
    private final Logger logger = Logger.getLogger(BabyMamaFilter.class.getName());
    private final String accountID = "180";
    private final String babyMamaKeyword = "BABY MAMA";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("Query String: " + ((HttpServletRequest) servletRequest).getQueryString());

        EntityManager em = EMF((HttpServletRequest)servletRequest).createEntityManager();

        String keyword = servletRequest.getParameter("keyword");
        String msg = servletRequest.getParameter("msg");
        String dest = servletRequest.getParameter("dest");
        String msisdn = servletRequest.getParameter("msisdn");
        String smsc = servletRequest.getParameter("smsc");

        PrintWriter out = servletResponse.getWriter();

        try {
            logger.fine("Check that smsc and msisdn are not empty or null.");

            if (StringUtils.isAnyBlank(smsc, msisdn, dest)) {
                logger.severe("Not all required parameters have been set.");
                out.print("Not all required parameters have been set.");
                return;
            }

            final String messageCaps = (StringUtils.isBlank(keyword) ? "" : keyword.trim()) + " " + (StringUtils.isBlank(msg) ? "" : msg.trim());
            if(StringUtils.isBlank(messageCaps)){
                logger.severe("Message is blank.");
                out.print("We're sorry we couldn't find what you were looking for. You can send HELP to "
                        + dest.substring(dest.indexOf("+") + 1) + " for options.");
                return;
            }
            logger.fine("Message is OK: " + messageCaps + ". Check if MSISDN is valid: " + msisdn);

            String match = matchesBabyMamaDate(messageCaps);
            if (!smsc.toUpperCase().contains("MTNGH") || StringUtils.isBlank(match)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            List<MobileNetworkOperator> operators = servletRequest.getAttribute("operators") == null ?
                    MobileNetworkOperator.getAll(em) : (List<MobileNetworkOperator>) servletRequest.getAttribute("operators");
            if (!MobileNetworkOperator.isValidMSISDN(operators, smsc, msisdn)) {
                logger.severe("MSISDN is not valid.");
                out.print("Sorry. Your mobile number may not be formatted correctly.");
                return;
            }

            logger.info("MSISDN is valid. Will format.");
            msisdn = MobileNetworkOperator.formatToInternationalFormat(operators, smsc, msisdn);

            ServiceSubscription subscription = em.find(ServiceSubscription.class, new ServiceSubscriptionKey(accountID, babyMamaKeyword, msisdn));
            String message;
            if (subscription != null) {
                Date date = parseBabyMamaDate(match);

                if (!isValidDate(date)) {
                    message = "Sorry the date you sent is not correct. Please try again.";
                } else {
                    int days = (int) (((new Date()).getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
                    Map<Metadata, String> metaDataMap = Utils.createMetaDataMap(subscription.getMetaData());
                    if (metaDataMap.containsKey(Metadata.BABY_MAMA_DATE)) {
                        message = "We have updated the date of your last menses."
                                + " We estimate that u may be " + (days / 7) + " weeks " + (days % 7) + " days pregnant.Txt STOP Baby Mama to exit.";
                    } else {
                        message = "Welcome!Baby Mama will be at ur side every step of the way throughout this period."
                                + "We estimate that u may be " + (days / 7) + " weeks " + (days % 7) + " days pregnant.Txt STOP Baby Mama to exit.";
                    }
                    metaDataMap.put(Metadata.BABY_MAMA_DATE, DateUtil.formatToShort(date));
                    em.getTransaction().begin();
                    subscription.setMetaData(Utils.createMetaDataString(metaDataMap));
                    em.getTransaction().commit();

                    SystemSMSQueue systemSMSQueue = SystemSMSQueue.find(em, accountID, babyMamaKeyword, days / 7);
                    if (systemSMSQueue != null) {
                        Utils.sendContent(msisdn, dest, accountID, babyMamaKeyword, "MTNGH2", "121123531334226", systemSMSQueue.getMessage());
                    }
                }
            } else {
                message = "Trying to subscribe to Baby Mama? Send BABY MAMA to 1984.";
            }

            out.print(message);
            ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH");
        } catch (Exception e) {
            logger.severe("Service look-up threw an exception: " + e.getMessage());
            out.print("We're sorry we couldn't find what you were looking for. You can send HELP to "
                    + dest.substring(dest.indexOf("+") + 1) + " for options.");
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if(em!=null){
                em.clear();
                em.close();
            }
        }
    }

    private String matchesBabyMamaDate(String msg) {
        final String[] regex = {"[0-3][0-9] [0-1][0-9] [1-2][0-9][0-9][0-9]",
                "[0-3][0-9]-[0-1][0-2]-[1-2][0-9][0-9][0-9]",
                "[0-3][0-9]/[0-1][0-2]/[1-2][0-9][0-9][0-9]",
                "[0-3][0-9]\\[0-1][0-2]\\[1-2][0-9][0-9][0-9]"
        };
        String match = null;

        for (String r : regex) {
            Pattern pattern = Pattern.compile(r);
            Matcher matcher = pattern.matcher(msg);

            if (matcher.find()) {
                match = matcher.group();
                break;
            }
        }
        return match;
    }

    private Date parseBabyMamaDate(String msg) {
        final String[] formats = {"dd MM yyyy", "dd-MM-yyyy", "dd/MM/yyyy", "dd\\MM\\yyyy"};

        if (msg == null || msg.trim().equals("")) {
            return null;
        }
        Date date = null;
        for (String f : formats) {
            try {
                DateFormat format = new SimpleDateFormat(f);
                date = format.parse(msg);
            } catch (ParseException e) {
                System.out.println(new Date() + ": [service-access:ERROR]: Could not parse string: " + e.getMessage());
            }

            if (date != null) {
                break;
            }
        }
        return date;
    }

    private boolean isValidDate(Date date) {
        if (date == null || date.after(new Date())) {
            return false;
        }
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.MONTH, -10);

        return date.compareTo(calender.getTime()) >= 0;
    }
}
