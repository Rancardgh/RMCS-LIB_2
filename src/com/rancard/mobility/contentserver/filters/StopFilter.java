package com.rancard.mobility.contentserver.filters;

import com.rancard.mobility.contentserver.BaseServlet;
import com.rancard.mobility.contentserver.StopMechanicsFactory;
import com.rancard.mobility.contentserver.serviceinterfaces.config.ConfigureResponse;
import org.apache.commons.lang3.StringUtils;
import com.rancard.common.MobileNetworkOperator;

import javax.persistence.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Mustee on 3/30/2014.
 */
public class StopFilter extends BaseServlet implements Filter {
    private final Logger logger = Logger.getLogger(StopFilter.class.getName());


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info(" Query String: " + ((HttpServletRequest) servletRequest).getQueryString());
        EntityManager em = EMF((HttpServletRequest) servletRequest).createEntityManager();

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
            if (StringUtils.isBlank(messageCaps)) {
                logger.severe("Message is blank.");
                message = "We're sorry we couldn't find what you were looking for. You can send HELP to " + dest.substring(dest.indexOf("+") + 1) + " for options.";
                out.print(message);
                ConfigureResponse.responseConfigurer((HttpServletResponse) servletResponse, null, smsc, message, true);
                return;
            }
            logger.fine("Message is OK: " + messageCaps + ". Check if MSISDN is valid: " + msisdn);

            if (!(StringUtils.containsIgnoreCase(messageCaps, STOP + " ") || messageCaps.trim().equalsIgnoreCase(STOP))) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

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

            message = StopMechanicsFactory.processStop(em, (HttpServletResponse) servletResponse, msisdn, messageCaps, smsc, dest);
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
            if (em != null) {
                em.close();
            }
        }
    }
}
