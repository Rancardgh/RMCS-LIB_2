package com.rancard.mobility.contentserver.filters;

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
    private final String utvBaseURL = "https://app.rancardmobility.com/rmcsselfcaretest/";

    public void init(FilterConfig filterConfig)
            throws ServletException {
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
            if (StringUtils.isAnyBlank(new CharSequence[]{smsc, msisdn, dest})) {
                this.logger.severe("Not all required parameters have been set.");
                out.print("Not all required parameters have been set.");
            } else {
                String messageCaps = (StringUtils.isBlank(keyword) ? "" : keyword.trim()) + " " + (StringUtils.isBlank(msg) ? "" : msg.trim());
                if (StringUtils.isBlank(messageCaps.trim())) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else if ((!dest.equalsIgnoreCase("1987")) || ((!messageCaps.trim().equalsIgnoreCase("UTV")) && (!hasUser(msisdn)))) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    this.logger.info("Will process UTV request");
                    if (messageCaps.trim().equalsIgnoreCase("UTV")) {
                        out.print(addUser(msisdn, "", ""));
                    } else if (messageCaps.split(" ").length == 2) {
                        out.print(addUser(msisdn, messageCaps.split(" ")[0], messageCaps.split(" ")[1]));
                    } else {
                        out.print(addMessage(msisdn, messageCaps, "MTN GHANA"));
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

    private boolean hasUser(String msisdn)
            throws Exception {
        String url = "https://app.rancardmobility.com/rmcsselfcaretest/users?msisdn=" + URLEncoder.encode(msisdn, "UTF-8");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            this.logger.info("About to make request to check the: " + url);

            response = client.execute(get);
            boolean bool;
            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
            return false;
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

    private String addUser(String msisdn, String name, String location)
            throws IOException {
        String url = "https://app.rancardmobility.com/rmcsselfcaretest/users";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://app.rancardmobility.com/rmcsselfcaretest/users");
        CloseableHttpResponse response = null;
        try {
            this.logger.info("About to make request to add user: https://app.rancardmobility.com/rmcsselfcaretest/users");

            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(new UrlEncodedFormEntity(Arrays.asList(new NameValuePair[]{new BasicNameValuePair("msisdn", msisdn), new BasicNameValuePair("name", name), new BasicNameValuePair("location", location)}), "UTF-8"));

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

    private String addMessage(String msisdn, String message, String network)
            throws IOException {
        String url = "https://app.rancardmobility.com/rmcsselfcaretest/messages";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://app.rancardmobility.com/rmcsselfcaretest/messages");
        CloseableHttpResponse response = null;
        try {
            this.logger.info("About to make request to add user: https://app.rancardmobility.com/rmcsselfcaretest/messages");

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
}
