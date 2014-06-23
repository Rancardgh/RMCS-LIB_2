package com.rancard.mobility.contentserver.filters;

import com.rancard.common.MobileNetworkOperator;
import com.rancard.common.ServiceDefinition;
import com.rancard.common.ServiceSubscription;
import com.rancard.common.key.ServiceDefinitionKey;
import com.rancard.mobility.common.ServiceMatcher;
import com.rancard.mobility.common.SimpleServiceMatcher;
import com.rancard.mobility.contentserver.BaseServlet;
import com.rancard.mobility.contentserver.EMF;
import com.rancard.mobility.contentserver.serviceinterfaces.config.ConfigureResponse;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        EntityManager em = EMF((HttpServletRequest)servletRequest).createEntityManager();

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
                    MobileNetworkOperator.getAll(em) : (List<MobileNetworkOperator>) servletRequest.getAttribute("operators");
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
                ServiceSubscription subscription = ServiceSubscription.getLastSubscription(em, msisdn);
                if (subscription != null) {
                    service = subscription.getServiceDefinition();
                }
            } else {
                ServiceMatcher matcher = new SimpleServiceMatcher();
                service = matcher.matchService(helpMessage, ServiceDefinition.findBySMSCAndShortCode(em, smsc, dest), 0.7);
            }

            if(service == null){
                logger.info("Service is null");
                message = "Try out our services! Send MUSIC, SPORTS, NEWS or anything of interest to " + dest + ". If you like them, send your friend's number to "
                        + dest + " to share the fun with them.";
            }else {

                ServiceDefinition helpService = em.find(ServiceDefinition.class, new ServiceDefinitionKey(service.getServiceDefinitionKey().getAccountID(), "HELP"));

                if(helpService == null){
                    logger.info("Cant find help for service.");
                    message = "Try out our services! Send MUSIC, SPORTS, NEWS or anything of interest to " + dest + ". If you like them, send your friend's number to "
                            + dest + " to share the fun with them.";
                }else{
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
