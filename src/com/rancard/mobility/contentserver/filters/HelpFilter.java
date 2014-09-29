package com.rancard.mobility.contentserver.filters;

import com.rancard.common.MobileNetworkOperator;
import com.rancard.common.ServiceDefinition;
import com.rancard.common.ServiceSubscription;
import com.rancard.mobility.common.ServiceMatcher;
import com.rancard.mobility.common.SimpleServiceMatcher;
import com.rancard.mobility.contentserver.BaseServlet;
import com.rancard.mobility.contentserver.serviceinterfaces.config.ConfigureResponse;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Mustee on 4/11/2014.
 */
public class HelpFilter extends BaseServlet implements Filter {
    private final Logger logger = Logger.getLogger(HelpFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info(" Query String: " + ((HttpServletRequest) servletRequest).getQueryString());

        String keyword = servletRequest.getParameter("keyword");
        String msg = servletRequest.getParameter("msg");
        String dest = servletRequest.getParameter("dest");
        String msisdn = servletRequest.getParameter("msisdn");
        String smsc = servletRequest.getParameter("smsc");

        PrintWriter out = servletResponse.getWriter();
        
        try {
            logger.fine("Check that smsc and msisdn are not empty or null.");
            String message;

            if (StringUtils.isAnyBlank(smsc, msisdn, dest)) {
                logger.severe("Not all required parameters have been set.");
                out.print("Not all required parameters have been set.");
                return;
            }

            final String messageCaps = (StringUtils.isBlank(keyword) ? "" : keyword.trim()) + " " + (StringUtils.isBlank(msg) ? "" : msg.trim());

            List<MobileNetworkOperator> operators = servletRequest.getAttribute("operators") == null ?
                    MobileNetworkOperator.getAll() : (List<MobileNetworkOperator>) servletRequest.getAttribute("operators");
            if (!MobileNetworkOperator.isValidMSISDN(operators, smsc, msisdn)) {
                logger.severe("MSISDN is not valid.");
                message = "Sorry. Your mobile number may not be formatted correctly.";
                out.print(message);
                ConfigureResponse.responseConfigurer((HttpServletResponse) servletResponse, null, smsc, message, true);
                return;
            }

            logger.info("MSISDN is valid. Will format.");
            msisdn = MobileNetworkOperator.formatToInternationalFormat(operators, smsc, msisdn);

            if (!(messageCaps.trim().contains(HELP + " ") || messageCaps.trim().equalsIgnoreCase(HELP))) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            String helpMessage = messageCaps.replace(HELP + " ", "").replace(HELP, "").trim();
            ServiceDefinition service = null;
            if (StringUtils.isBlank(helpMessage)) {
                ServiceSubscription subscription = ServiceSubscription.getLastSubscription(msisdn);
                if (subscription != null) {
                    service = ServiceDefinition.find(subscription.getAccountID(), subscription.getKeyword());
                }
            } else {
                ServiceMatcher matcher = new SimpleServiceMatcher();
                service = matcher.matchService(helpMessage, ServiceDefinition.findBySMSCAndShortCode(smsc, dest), 0.7);
            }

            if(service == null){
                logger.info("Service is null");
                Properties property = Utils.loadPropertyFile("rmcs.properties");
                
                message = "Try out our services! Send MUSIC, SPORTS, NEWS or anything of interest to " + dest + ". If you like them, send your friend's number to "
                        + dest + " to share the fun with them.";
                
                if (dest.trim().equals("1983")) {
                    message = property.getProperty("1983_help_message");
                } else if (dest.trim().equals("1984")) {
                    message = property.getProperty("1984_help_message");
                } else if (dest.trim().equals("1987")) {
                    message = property.getProperty("1987_help_message");
                } else if (dest.trim().equals("1988")) {
                    message = property.getProperty("1988_help_message");
                } else if (dest.trim().equals("1989")) {
                    message = property.getProperty("1989_help_message");
                }
                
            } else {
                ServiceDefinition helpService = ServiceDefinition.find(service.getAccountID(), "HELP");
                
                if(helpService == null){
                    logger.info("Cant find help for service.");
                    message = "Try out our services! Send MUSIC, SPORTS, NEWS or anything of interest to " + dest + ". If you like them, send your friend's number to "
                            + dest + " to share the fun with them.";
                } else{
                    message = helpService.getDefaultMessage();
                }
            }

            out.print(message);
            ConfigureResponse.responseConfigurer((HttpServletResponse) servletResponse, null, smsc, message, true);

        } catch (Exception e) {
            logger.severe("Service look-up threw an exception: " + e.getMessage());
            out.print("We're sorry we couldn't find what you were looking for. You can send HELP to "
                    + dest.substring(dest.indexOf("+") + 1) + " for options.");
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }

        }
    }
}
