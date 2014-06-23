package com.rancard.mobility.contentserver.filters;

import com.rancard.common.MobileNetworkOperator;
import com.rancard.mobility.contentserver.BaseServlet;
import com.rancard.mobility.contentserver.serviceinterfaces.config.ConfigureResponse;
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

        if (StringUtils.isAnyBlank(country, msisdn)) {
            logger.severe("Not all required parameters have been set.");
            return;
        }

        logger.info("Adding prediction.");
        try {
            Jedis jedis = new Jedis("192.168.1.245", 6380);
            jedis.connect();
            jedis.select(1);
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


            /*List<MobileNetworkOperator> operators = servletRequest.getAttribute("operators") == null ?
                    MobileNetworkOperator.getAll() : (List<MobileNetworkOperator>) servletRequest.getAttribute("operators");
            if (!MobileNetworkOperator.isValidMSISDN(operators, smsc, msisdn)) {
                logger.severe("MSISDN is not valid.");
                message = "Sorry. Your mobile number may not be formatted correctly.";
                out.print(message);
                ConfigureResponse.responseConfigurer((HttpServletResponse) servletResponse, null, smsc, message, true);
                return;
            }*/

            //logger.info("MSISDN is valid. Will format.");
            //msisdn = MobileNetworkOperator.formatToInternationalFormat(operators, smsc, msisdn);

            final String messageCaps = (StringUtils.isBlank(keyword) ? "" : keyword.trim()) + " " + (StringUtils.isBlank(msg) ? "" : msg.trim());
            if (StringUtils.isBlank(messageCaps.trim())) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            String country = (getCountry(messageCaps.trim()) != null) ? getCountry(messageCaps.trim()) : getCountry(keyword.trim());
            if (!(dest.equalsIgnoreCase("1988") && !StringUtils.isBlank(country))) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            logger.info("Country found: " + country);
            final String url = "http://192.168.1.244:19202/rmcs2/worldcup?msisdn=" + URLEncoder.encode(msisdn, "UTF-8") + "&country=" + URLEncoder.encode(getCountry(country), "UTF-8");

            ((HttpServletResponse) servletResponse).setHeader("X-Kannel-DLR-Mask", "8");
            ((HttpServletResponse) servletResponse).setHeader("X-Kannel-DLR-Url", url);
            ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH2");


            Properties property = Utils.loadPropertyFile("rmcs.properties");
            String responseMessage = (property == null) ? null : property.getProperty("worldcup_promo_message");
            out.print((responseMessage != null) ? responseMessage : "Thanks for your prediction. Get World Cup updates on your phone. Simply send FBALL to 1988 on all networks, send more predictions to win. Enjoy!");
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

    private Map<String, String[]> getCountries() {
        Map<String, String[]> countries = new HashMap<String, String[]>();
        countries.put("GHANA", new String[]{"GH", "GANA", "GHANA"});
        countries.put("BRAZIL", new String[]{"BR", "BRAZIL", "BRASIL", "BRA"});
        countries.put("CROATIA", new String[]{"HR", "CR", "CROATIA"});
        countries.put("MEXICO", new String[]{"MX", "ME", "MEXICO", "MEX"});
        countries.put("SPAIN", new String[]{"ES", "SP", "SPAIN", "ESP"});
        countries.put("NETHERLANDS", new String[]{"HOLLAND", "HOLAND", "NL", "NE"});
        countries.put("CHILE", new String[]{"CL", "CH", "CHILE", "CHILI"});
        countries.put("AUSTRALIA", new String[]{"AU", "AUSTRALIA", "AUS"});
        countries.put("COLOMBIA", new String[]{"CO", "COLOMBIA", "COLUMBIA", "COLUMBI", "CLB", "COL"});
        countries.put("GREECE", new String[]{"GR", "GREECE", "GREEZE"});
        countries.put("IVORY", new String[]{"CI", "IV", "COTE D'IVOIRE", "COTE DIVOIRE", "IVORY COAST", "IVORY"});
        countries.put("JAPAN", new String[]{"JP", "JA", "JAPAN"});
        countries.put("URUGUAY", new String[]{"UY", "UR", "URUGUAY"});
        countries.put("COSTA", new String[]{"CR", "COSTA RICA", "COSTA"});
        countries.put("ENGLAND", new String[]{"EN", "ENGLAND", "UK"});
        countries.put("ITALY", new String[]{"IT", "ITALY"});
        countries.put("SWITZERLAND", new String[]{"SZ", "SW", "SWITZERLAND"});
        countries.put("ECUADOR", new String[]{"EC", "ECUADOR"});
        countries.put("HONDURAS", new String[]{"HN", "HO", "HONDURAS"});
        countries.put("ARGENTINA", new String[]{"AR", "ARGENTINA"});
        countries.put("BOSNIA AND HERZEGOVINA", new String[]{"BA", "BO", "BOSNIA AND HERZEGOVINA", "BOSNIA"});
        countries.put("IRAN", new String[]{"IR", "IRAN"});
        countries.put("NIGERIA", new String[]{"NG", "NI", "NIGERIA"});
        countries.put("GERMANY", new String[]{"GE", "GERMANY"});
        countries.put("PORTUGAL", new String[]{"PT", "PORTUGAL", "PO"});
        countries.put("CAMEROUN", new String[]{"CA", "CAMERON", "CAMEROON", "CM", "CAM", "CAMEROUN", "CMR"});
        countries.put("USA", new String[]{"US", "USA"});
        countries.put("BELGIUM", new String[]{"BE", "BELGIUM"});
        countries.put("ALGERIA", new String[]{"AL", "ALGERIA"});
        countries.put("RUSSIA", new String[]{"RU", "RUSSIA", "RUSIA"});
        countries.put("KOREA REPUBLIC", new String[]{"KO", "KR", "KOREA REPUBLIC"});
        countries.put("FRANCE", new String[]{"FR", "FRA", "FRANCE"});

        return countries;
    }


    private String getCountry(String country) {
        Map<String, String[]> countries = getCountries();

        for (String key : countries.keySet()) {
            for (String c : countries.get(key))
                if (c.equalsIgnoreCase(country)) {
                    return key;
                }
        }

        return null;
    }
}
