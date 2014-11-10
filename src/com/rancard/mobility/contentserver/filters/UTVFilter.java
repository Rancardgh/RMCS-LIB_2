package com.rancard.mobility.contentserver.filters;

import com.google.gson.Gson;
import com.rancard.common.Channel;
import com.rancard.common.ServiceDefinition;
import com.rancard.common.ServiceSubscription;
import com.rancard.mobility.contentserver.BaseServlet;
import com.rancard.mobility.contentserver.StopMechanics;
import com.rancard.util.DateUtil;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Ahmed on 8/28/2014.
 */
public class UTVFilter extends BaseServlet implements Filter {
    private final Logger logger = Logger.getLogger(UTVFilter.class.getName());
    private final String utvBaseURL = "http://app.rancardmobility.com/UTV";

    public void init(FilterConfig filterConfig)
            throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msisdn = req.getParameter("msisdn");
        String message = req.getParameter("message");

        PrintWriter out = resp.getWriter();
        try {
            if (StringUtils.isAnyBlank(msisdn, message)) {
                this.logger.severe("Not all required parameters have been set.");
                out.print("Not all required parameters have been set.");
                return;
            }

            out.print(addMessage(msisdn, message, "MTN GHANA"));

        } catch (Exception e) {
            this.logger.severe("Problem processing UTV request: " + e.getMessage());
            out.print("Something went wrong. Please try again later.");
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        this.logger.info(" Query String: " + ((HttpServletRequest) servletRequest).getQueryString());

        String keyword = servletRequest.getParameter("keyword");
        String msg = servletRequest.getParameter("msg");
        String dest = servletRequest.getParameter("dest");
        String msisdn = servletRequest.getParameter("msisdn");
        String smsc = servletRequest.getParameter("smsc");

        PrintWriter out = servletResponse.getWriter();
        try {
            this.logger.fine("Check that smsc and msisdn are not empty or null.");
            if (StringUtils.isAnyBlank(smsc, msisdn, dest)) {
                this.logger.severe("Not all required parameters have been set.");
                out.print("Not all required parameters have been set.");
            } else {
                String messageCaps = (StringUtils.isBlank(keyword) ? "" : keyword.trim()) + " " + (StringUtils.isBlank(msg) ? "" : msg.trim());
                User user = getUser(msisdn);
                if (StringUtils.isBlank(messageCaps.trim())) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else if (!((dest.equalsIgnoreCase("1987") && messageCaps.trim().equalsIgnoreCase("UTV")) || (dest.equalsIgnoreCase("1987") && user != null)
                        || (StopMechanics.isUnsubscriptionMessage(messageCaps) && messageCaps.contains("UTV")))) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    this.logger.info("Will process UTV request");
                    if (messageCaps.trim().equalsIgnoreCase("UTV")) {
                        if(ServiceSubscription.find(Utils.formatToInternationalFormatGH(msisdn), "215", "UTV") == null) {
                            ServiceSubscription.createSubscription(new ServiceSubscription("215", "UTV", Utils.formatToInternationalFormatGH(msisdn), new Date(),
                                    DateUtil.addDaysToDate(new Date(), 1), 1, 1, Channel.SMS, null));
                        }
                        out.print(addUser(msisdn, "", ""));
                        ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH");
                    } else if ((messageCaps.trim().equalsIgnoreCase("STOP") || messageCaps.replace(" ", "").equalsIgnoreCase("STOPUTV")) && user != null) {
                        ServiceSubscription.delete(Utils.formatToInternationalFormatGH(msisdn), "215", "UTV");
                        out.print(removeUser(Long.toString(user.msisdn)));
                        ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH");
                    }  else if ((messageCaps.trim().split(",").length >= 2 || messageCaps.trim().split(" ").length >= 2) && user != null
                            && (user.location == null || user.location.equals("")) && (user.name == null || user.name.equals(""))) {
                        if (messageCaps.split(",").length >= 2) {
                            String location = messageCaps.trim().split(",")[messageCaps.trim().split(",").length - 1];
                            String name = messageCaps.replace(location, "").trim();
                            out.print(addUser(Long.toString(user.msisdn), name, location));
                        } else {
                            String location = messageCaps.trim().split(" ")[messageCaps.trim().split(" ").length - 1];
                            String name = messageCaps.replace(location, "").trim();
                            out.print(addUser(Long.toString(user.msisdn), name, location));
                        }
                        ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH");
                    } else if (user != null) {
                        if (StringUtils.isAnyBlank(user.location, user.name)) {
                            out.print("Welcome to U-Chat service. You will receive daily updates. Please send your NAME followed by your LOCATION to 1987 to continue.");
                            ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH");
                            return;
                        }

                        ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH2");

                        if (isValid(messageCaps)) {
                            final String url = servletRequest.getScheme() + "://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort()
                                    + ((HttpServletRequest) servletRequest).getContextPath() + "/utv?msisdn=" + URLEncoder.encode(msisdn, "UTF-8") + "&message="
                                    + URLEncoder.encode(messageCaps, "UTF-8");
                            logger.info("Callback url: " + url);
                            ((HttpServletResponse) servletResponse).setHeader("X-Kannel-DLR-Mask", "8");
                            ((HttpServletResponse) servletResponse).setHeader("X-Kannel-DLR-Url", url);
                            out.print(getRandomMessage(user.name));
                        } else {
                            out.print("You are not allowed to send this.");
                        }
                    }


                }
            }
        } catch (Exception e) {
            this.logger.severe("Problem processing UTV request: " + e.getMessage());
            out.print("Something went wrong. Please try again later.");
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private String getRandomMessage(String name) {
        String[] messages = {"Thanks for your contribution. Your message will be displayed shortly. Tell your friends about U-Chat. Text INVITE followed by your friend's number to 1987.",
                "Thanks for your contribution. Your message has been queued & will be displayed shortly. Keep texting!Tell your friends about U-Chat. Click here http://goo.gl/rCBvEW",
                name + " Thanks for your contribution. Your message will be displayed shortly. Keep texting!"};
        return messages[new Random().nextInt(3)];
    }


    private User getUser(String msisdn) throws Exception {
        final String url = utvBaseURL + "/users?msisdn=" + URLEncoder.encode(msisdn, "UTF-8");

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            this.logger.info("About to make request to check the: " + url);

            response = client.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                return new Gson().fromJson(EntityUtils.toString(response.getEntity()), User.class);
            }
            return null;
        } catch (Exception e) {
            this.logger.severe("Error making content: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    private String addUser(String msisdn, String name, String location) throws IOException {
        final String url = utvBaseURL + "/users";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            this.logger.info("About to make request to add user.");

            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(new UrlEncodedFormEntity(Arrays.asList(new BasicNameValuePair("msisdn", msisdn),
                    new BasicNameValuePair("name", name), new BasicNameValuePair("location", location), new BasicNameValuePair("action", "ADD")), "UTF-8"));

            response = client.execute(post);
            String resp = EntityUtils.toString(response.getEntity());
            this.logger.info("Response is: " + resp);

            return resp;
        } catch (IOException e) {
            this.logger.severe("Error adding UTV user: " + e.getMessage());
            throw new IOException(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    private String removeUser(String msisdn) throws IOException {
        final String url = utvBaseURL + "/users";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            this.logger.info("About to make request to add user.");

            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(new UrlEncodedFormEntity(Arrays.asList(new NameValuePair[]{new BasicNameValuePair("msisdn", msisdn),
                    new BasicNameValuePair("action", "REMOVE")}), "UTF-8"));

            response = client.execute(post);
            String resp = EntityUtils.toString(response.getEntity());
            this.logger.info("Response is: " + resp);

            return resp;
        } catch (IOException e) {
            this.logger.severe("Error adding UTV user: " + e.getMessage());
            throw new IOException(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    private String addMessage(String msisdn, String message, String network) throws IOException {
        final String url = utvBaseURL + "/messages";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            this.logger.info("About to make request to add messages.");

            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(new UrlEncodedFormEntity(Arrays.asList(new NameValuePair[]{new BasicNameValuePair("msisdn", msisdn), new BasicNameValuePair("message", message), new BasicNameValuePair("network", network)}), "UTF-8"));

            response = client.execute(post);
            String resp = EntityUtils.toString(response.getEntity());
            this.logger.info("Response is: " + resp);

            return resp;
        } catch (IOException e) {
            this.logger.severe("Error adding UTV message: " + e.getMessage());
            throw new IOException(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }


    public class User {
        private long msisdn;
        private String name;
        private String location;

        public User(long msisdn, String name, String location) {
            this.msisdn = msisdn;
            this.name = name;
            this.location = location;
        }
    }

    private String[] censorWords = {"asshole", "penis", "ass", "bitch", "boob", "bastard", "bitch", "cock", "cocksucker", "cunt", "dick", "damn", "darn", "fag", "fuck",
            "fcuk", "fck", "faggot", "motherfucker", "nigger", "nigga", "niga", "porn", "shit", "anus", "butt", "arse", "arsehole", "assbag", "assbandit", "assbanger",
            "assbite", "assclown", "asscock", "asscracker", "asses", "assface", "assfuck", "assfucker", "assgoblin", "asshat", "ass-hat", "asshead", "asshole", "asshopper",
            "ass-jabber", "assjacker", "asslick", "asslicker", "assmonkey", "assmunch", "assmuncher", "assnigger", "asspirate", "ass-pirate", "assshit", "assshole",
            "asssucker", "asswad", "asswipe", "axwound", "butt", "Buttlicker", "butts", "homosexual", "idiot", "jerk", "pennis", "pussy", "rear-loving", "vagina",
            "wanker", "stupid", "fool", "fucker", "aboa", "odwan", "buulu", "duna", "gyimi gyimi", "gyimii", "jimi jimi", "jimii", "koti", "kwasia", "ohi3", "ony3",
            "ots3", "shua", "shwua", "kurasini", "ekurase", "shwoa", "wotiriso", "womaametwe", "womaametw3", "etwe", "etw3", "3tw3", "3twe", "nkwasiasem", "ogyimifuor",
            "wagyimi"};

    private boolean isValid(String message) {
        String[] words = message.split(" ");
        for (String word : words) {
            if (isBadWord(word)) {
                return false;
            }
        }

        return true;
    }

    private boolean isBadWord(String word) {
        for (String censorWord : censorWords) {
            if (censorWord.equalsIgnoreCase(word)) {
                return true;
            }
        }

        return false;
    }


}
