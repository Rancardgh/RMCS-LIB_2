package com.rancard.mobility.contentserver.filters;

import com.rancard.mobility.contentserver.BaseServlet;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Mustee on 5/31/2014.
 */
public class WorldCupFilter extends BaseServlet implements Filter {
    private final Logger logger = Logger.getLogger(WorldCupFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info(" Query String: " + req.getQueryString());
        String country = req.getParameter("country");
        String msisdn = req.getParameter("msisdn");
        String dest = req.getParameter("dest");

        if (StringUtils.isAnyBlank(country, msisdn)) {
            logger.severe("Not all required parameters have been set.");
            return;
        }

        logger.info("Adding prediction.");
        try {
            Jedis jedis = new Jedis("192.168.1.245", 6380);
            jedis.connect();
            if(dest.equals("1988")) {
                jedis.select(1);
            }else{
                jedis.select(2);
            }
            jedis.set(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-" + msisdn, country);
            jedis.disconnect();
        } catch (Exception e) {
            logger.severe("Problem connecting to redis: " + e.getMessage());
        }
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
            //String message;

            if (StringUtils.isAnyBlank(smsc, msisdn, dest)) {
                logger.severe("Not all required parameters have been set.");
                out.print("Not all required parameters have been set.");
                return;
            }


            final String messageCaps = (StringUtils.isBlank(keyword) ? "" : keyword.trim()) + " " + (StringUtils.isBlank(msg) ? "" : msg.trim());
            if (StringUtils.isBlank(messageCaps.trim())) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            Properties properties = Utils.loadPropertyFile("rmcs.properties");
            String country = getCountry(properties, messageCaps.split(" "));
            if (!((dest.equalsIgnoreCase("1988") && !StringUtils.isBlank(country)) || (dest.equalsIgnoreCase("1987") && !StringUtils.isBlank(country)))) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            logger.info("Country found: " + country);
            final String url = "http://192.168.1.243:82/rmcs2/worldcup?msisdn=" + URLEncoder.encode(msisdn, "UTF-8") + "&country=" + URLEncoder.encode(country, "UTF-8") + "&dest=" + dest;

            ((HttpServletResponse) servletResponse).setHeader("X-Kannel-DLR-Mask", "8");
            ((HttpServletResponse) servletResponse).setHeader("X-Kannel-DLR-Url", url);
            ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH2");


            Properties property = Utils.loadPropertyFile("rmcs.properties");
            String responseMessage = (property == null) ? null : property.getProperty("worldcup_promo_message");
            int tally = getTally(msisdn, dest);
            out.print((responseMessage != null) ? responseMessage.replace("@@tally@@", Integer.toString(tally)) : "Thanks for your prediction. Get World Cup updates on your phone. Simply send FBALL to 1988 on all networks, send more predictions to win. Enjoy!");
        } catch (Exception e) {
            logger.severe("Problem processing world cup promo: " + e.getMessage());
            out.print("Something went wrong. Please try again later.");
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    private String getCountry(Properties properties, String country) {

        for (Object key : properties.keySet()) {
            if (key.toString().startsWith("wc-")) {
                for (String c : properties.getProperty(key.toString()).split(",")) {
                    if (c.trim().equalsIgnoreCase(country.trim())) {
                        return key.toString().split("-")[1];
                    }
                }
            }
        }
        return null;
    }

    private String getCountry(Properties properties, String[] messages) {
        for (String c : messages) {
            if (!StringUtils.isBlank(c)) {
                String country = getCountry(properties, c);
                if (country != null) {
                    return country;
                }
            }
        }
        return null;
    }

    private int getTally(String msisdn, String dest){
        int tally = 0;
        logger.info("Getting tally of prediction.");
        try {
            Jedis jedis = new Jedis("192.168.1.245", 6380);
            jedis.connect();
            if(dest.equals("1988")) {
                jedis.select(1);
            }else{
                jedis.select(2);
            }
            Set<String> keys = jedis.keys(new SimpleDateFormat("yyyyMMdd").format(new Date()) + "*-" + msisdn);
            tally = keys.size();
            jedis.disconnect();
        } catch (Exception e) {
            logger.severe("Problem connecting to redis: " + e.getMessage());
        }

        return tally;
    }
}
