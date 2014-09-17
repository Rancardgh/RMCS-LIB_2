package com.rancard.mobility.contentserver.filters;

import com.google.gson.Gson;
import com.rancard.mobility.contentserver.BaseServlet;
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
import java.util.logging.Logger;

/**
 * Created by Ahmed on 8/28/2014.
 */
public class UTVFilter extends BaseServlet
        implements Filter {
    private final Logger logger = Logger.getLogger(UTVFilter.class.getName());
    private final String utvBaseURL = "http://app.rancardmobility.com/rmcsselfcaretest";

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
                } else if (!((dest.equalsIgnoreCase("1987") && messageCaps.trim().equalsIgnoreCase("UTV")) || (dest.equalsIgnoreCase("1987") && user != null))) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    this.logger.info("Will process UTV request");
                    if (messageCaps.trim().equalsIgnoreCase("UTV")) {
                        out.print(addUser(msisdn, "", ""));
                        ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH");
                    } else if (messageCaps.trim().equalsIgnoreCase("STOP") && user != null) {
                        out.print(removeUser(Long.toString(user.msisdn)));
                        ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH");
                    } else if (messageCaps.split(",").length == 2 && user != null) {
                        out.print(addUser(Long.toString(user.msisdn), messageCaps.split(",")[0].trim(), messageCaps.split(",")[1].trim()));
                        ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH");
                    } else if (user != null) {
                        if(user.location == null|| user.location.equals("") || user.name == null|| user.name.equals("")){
                            out.print("Please send your NAME followed by your LOCATION to 1987 to continue.");
                            ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH");
                            return;
                        }

                        final String url = servletRequest.getScheme() + "://" + servletRequest.getServerName() + ":" + servletRequest.getServerPort()
                                + ((HttpServletRequest) servletRequest).getContextPath() + "/utv?msisdn=" + URLEncoder.encode(msisdn, "UTF-8") + "&message="
                                + URLEncoder.encode(messageCaps, "UTF-8");
                        logger.info("Callback url: " + url);
                        ((HttpServletResponse) servletResponse).setHeader("X-Kannel-DLR-Mask", "8");
                        ((HttpServletResponse) servletResponse).setHeader("X-Kannel-DLR-Url", url);
                        ((HttpServletResponse) servletResponse).setHeader("X-Kannel-SMSC", "MTNGH2");
                        out.print(user.name + " thanks for your contribution. Your message has been queued & will be displayed shortly. Keep texting!Tell your friends about U-Chat.");
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
}
