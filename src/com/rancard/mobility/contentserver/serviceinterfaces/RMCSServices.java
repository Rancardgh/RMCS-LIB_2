package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.*;
import com.rancard.mobility.common.ServiceMatcher;
import com.rancard.mobility.common.SimpleServiceMatcher;
import com.rancard.mobility.common.ThreadedMessageSender;
import com.rancard.mobility.contentserver.BaseServlet;
import com.rancard.mobility.contentserver.DefaultViralMarketing;
import com.rancard.mobility.contentserver.ViralMarketing;
import com.rancard.mobility.contentserver.payment.PaymentManager;
import com.rancard.mobility.contentserver.serviceinterfaces.config.ConfigureResponse;
import com.rancard.util.DateUtil;
import com.rancard.util.UidGen;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Mustee on 3/30/2014.
 */
public class RMCSServices extends BaseServlet {
    private final Logger logger = Logger.getLogger(RMCSServices.class.getName());


    @Override
    public void doGet(HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received Request. Query String: " + req.getQueryString());

        String keyword = req.getParameter("keyword");
        String msg = req.getParameter("msg");
        String dest = req.getParameter("dest");
        String msisdn = req.getParameter("msisdn");
        String smsc = req.getParameter("smsc");
        String alreadyBilled = req.getParameter("pre_billed");

        final String fullContextPath = "http://192.168.1.243:81" + req.getContextPath();

        PrintWriter out = resp.getWriter();
        try {
            logger.fine("Check that smsc and msisdn are not empty or null.");
            String message;

            if (StringUtils.isAnyBlank(smsc, msisdn, dest)) {
                logger.severe("Not all required parameters have been set.");
                out.write("Not all required parameters have been set.");
                return;
            }

            String messageCaps = (StringUtils.isBlank(keyword) ? "" : keyword.trim()) + " " + (StringUtils.isBlank(msg) ? "" : msg.trim());
            logger.info("Check if message is OK: " + messageCaps);
            if (StringUtils.isBlank(messageCaps)) {
                logger.info("Check is message is not OK: " + messageCaps);
                message = "Try out our services! Send MUSIC, SPORTS, NEWS or anything of interest to " + dest + ". If you like them, send your friend's number to "
                        + dest + " to share the fun with them.";
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, null, smsc, message, true);
                return;
            }

            List<MobileNetworkOperator> operators = req.getAttribute("operators") == null ? MobileNetworkOperator.getAll() : (List<MobileNetworkOperator>) req.getAttribute("operators");
            if (!MobileNetworkOperator.isValidMSISDN(operators, smsc, msisdn)) {
                logger.severe("MSISDN is not valid.");
                message = "Sorry. Your mobile number may not be formatted correctly.";
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, null, smsc, message, true);
                return;
            }

            logger.info("MSISDN is valid. Will format.");
            msisdn = MobileNetworkOperator.formatToInternationalFormat(operators, smsc, msisdn);

            ServiceMatcher serviceMatcher = new SimpleServiceMatcher();
            ServiceDefinition service = serviceMatcher.matchService(messageCaps, ServiceDefinition.findBySMSCAndShortCode(smsc, dest), 0.4);
            if (service == null) {
                logger.severe("Service could not be found.");
                Properties properties = Utils.loadPropertyFile("rmcs.properties");
                String prop = properties.getProperty("nothing_found_message");

                message = (prop != null) ? prop : "We're sorry we couldn't find what you were looking for. You can send HELP to " + dest.substring(dest.indexOf("+") + 1) + " for options.";
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, null, smsc, message, true);
                return;
            }

            if (service.getAccountID().equals("177")) {
                logger.info("This is JobMatch so we do stuff");
                ServiceSubscription subscription = ServiceSubscription.find(msisdn, "177", "GENERAL");
                if (StringUtils.equalsIgnoreCase(service.getKeyword(), "JobMatch") || StringUtils.equalsIgnoreCase(service.getKeyword(), "Match") || StringUtils.equalsIgnoreCase(service.getKeyword(), "Job")) {
                    if (subscription == null) {
                        Date date = new Date();
                        ServiceSubscription.createSubscription(new ServiceSubscription(service.getAccountID(), "GENERAL", msisdn, date, DateUtil.addDaysToDate(date, 1), 1, 1, Channel.SMS, null));

                        ConfigureResponse.responseConfigurer(resp, null, smsc, service.getDefaultMessage(), true);
                        out.print(service.getDefaultMessage());
                        return;
                    }
                } else {
                    if (subscription != null) {
                        ServiceSubscription.delete(msisdn, service.getAccountID(), "GENERAL");
                    }
                }
            }

            CPSite site = CPSite.getSMSSite(service.getAccountID());
            if (site == null) {
                logger.severe("CPSite not found.");
                message = "Sorry your request could not be completed.";
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, null, smsc, message, true);
                return;
            }

            CPConnection cnxn = CPConnection.getCPConnection(service.getAccountID(), smsc);
            if (cnxn == null) {
                logger.severe("CPConnection not found.");
                message = "Sorry your request could not be completed.";
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, null, smsc, message, true);
                return;
            }

            ServiceExperienceConfig serviceExperienceConfig = ServiceExperienceConfig.find(service.getKeyword(), service.getAccountID(), site.getSiteId());
            Map<Metadata, String> metaDataMap = new EnumMap<Metadata, String>(Metadata.class);
            if (serviceExperienceConfig == null) {
                logger.warning("Service experience config not found.");
            } else {
                logger.info("Processing service experience.");
                metaDataMap = Utils.createMetaDataMap(serviceExperienceConfig.getMetaData());
                if (metaDataMap.containsKey(Metadata.PROCESS_SERVICE_EXPERIENCE) && metaDataMap.get(Metadata.PROCESS_SERVICE_EXPERIENCE).equalsIgnoreCase("FALSE")) {
                    logger.info("Service experience found but will not be processed.");
                } else {
                    String pushMsg = null;
                    String pushMsgSender = null;
                    if (!service.isSubscription()) {
                        PromotionalCampaign campaign = StringUtils.isBlank(serviceExperienceConfig.getPromoId()) ? null : PromotionalCampaign.find(serviceExperienceConfig.getPromoId());
                        if (campaign != null) {
                            pushMsg = campaign.getPromoMessage();
                            pushMsgSender = serviceExperienceConfig.getPromoMessageSender();
                        }
                    } else {
                        keyword = metaDataMap.get(Metadata.OVERRIDE_KEYWORD) == null ? service.getKeyword() : metaDataMap.get(Metadata.OVERRIDE_KEYWORD);

                        ServiceSubscription subscription = ServiceSubscription.find(msisdn, service.getAccountID(), keyword);
                        if (subscription != null) {
                            if (subscription.getNextSubscriptionDate() == null) {
                                logger.info("Subscription already exists");

                                pushMsg = serviceExperienceConfig.getAlreadySubscribedMessage();
                                pushMsgSender = StringUtils.isBlank(serviceExperienceConfig.getAlreadySubscribedMessageSender())
                                        ? dest : serviceExperienceConfig.getAlreadySubscribedMessageSender();
                            } else {
                                boolean allowExtension = metaDataMap.containsKey(Metadata.ALLOW_EXTENSION) && metaDataMap.get(Metadata.ALLOW_EXTENSION).equalsIgnoreCase("TRUE");
                                if (subscription.getNextSubscriptionDate().after(new Date()) && allowExtension) {
                                    subscription.setNextSubscriptionDate(DateUtil.addDaysToDate(subscription.getNextSubscriptionDate(), serviceExperienceConfig.getSubscriptionInterval()));
                                    ServiceSubscription.update(subscription);

                                    pushMsg = "Dear subscriber, your subscription to " + service.getServiceName() + " has been extended and it will now expire on " + DateUtil.formatToShort(subscription.getNextSubscriptionDate());
                                } else if (!allowExtension) {
                                    pushMsg = serviceExperienceConfig.getAlreadySubscribedMessage();
                                } else {
                                    subscription.setNextSubscriptionDate(DateUtil.addDaysToDate(new Date(), serviceExperienceConfig.getSubscriptionInterval()));
                                    ServiceSubscription.update(subscription);

                                    pushMsg = "Dear subscriber, your subscription to " + service.getServiceName() + " has been renewed and it will now expire on " + DateUtil.formatToShort(subscription.getNextSubscriptionDate());

                                }

                                pushMsgSender = StringUtils.isBlank(serviceExperienceConfig.getAlreadySubscribedMessageSender()) ? dest : serviceExperienceConfig.getAlreadySubscribedMessageSender();
                            }
                        } else {
                            Date now = new Date();
                            if (metaDataMap.containsKey(Metadata.FREEMIUM) && metaDataMap.get(Metadata.FREEMIUM).equalsIgnoreCase("NO")) {
                                ServiceSubscription.createSubscription(new ServiceSubscription(service.getAccountID(), service.getKeyword(), msisdn, now,
                                        DateUtil.addDaysToDate(now, serviceExperienceConfig.getSubscriptionInterval()), 1, 1, Channel.SMS, null));
                            } else {
                                ServiceSubscription.createSubscription(new ServiceSubscription(service.getAccountID(), service.getKeyword(), msisdn, now,
                                        DateUtil.addDaysToDate(now, serviceExperienceConfig.getSubscriptionInterval()), 1, 0, Channel.SMS, null));

                            }

                            Utils.postSubscriptionNotification(msisdn, service.getKeyword());

                            pushMsg = serviceExperienceConfig.getWelcomeMessage();
                            pushMsgSender = StringUtils.isBlank(serviceExperienceConfig.getWelcomeMessageSender()) ? dest : serviceExperienceConfig.getWelcomeMessageSender();

                            VMCampaign campaign = VMCampaign.findByService(service.getAccountID(), service.getKeyword());
                            ViralMarketing viralMarketing = new DefaultViralMarketing(campaign);
                            viralMarketing.sendHowToMessage(cnxn, msisdn, smsc, dest);
                            viralMarketing.updateVMTransaction(cnxn, msisdn, smsc);

                        }
                    }

                    if (metaDataMap.containsKey(Metadata.SEND_CONTENT) && metaDataMap.get(Metadata.SEND_CONTENT).equalsIgnoreCase("FALSE")) {
                        if (StringUtils.isAnyBlank(pushMsg, pushMsgSender)) {
                            logger.warning("Message to be pushed or its short code is null");
                        } else {
                            out.print(pushMsg);
                            ConfigureResponse.responseConfigurer(resp, null, smsc, pushMsg, true);
                            return;
                        }
                    } else {
                        (new Thread(new ThreadedMessageSender(cnxn, msisdn, pushMsgSender, pushMsg, null, service.getAccountID(),
                                service.getKeyword(), "RMCS", smsc, 0, 0))).start();
                    }

                }
            }

            SystemSMSQueue systemSMSQueue = SystemSMSQueue.find(service.getAccountID(), service.getKeyword());
            message = (systemSMSQueue == null || StringUtils.isBlank(systemSMSQueue.getMessage())) ? service.getDefaultMessage() : systemSMSQueue.getMessage();
            boolean isFree = false;
            if (StringUtils.isBlank(message)) {
                message = "No info is currently available for " + service.getServiceName() + ". Please try again later.";
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, serviceExperienceConfig, smsc, message, true);
                return;
            }

            if (!((alreadyBilled != null && Boolean.getBoolean(alreadyBilled)) || StringUtils.isBlank(service.getPricing()))) {
                PricePoint pricePoint = PricePoint.find(service.getPricing());

                if (pricePoint == null) {
                    logger.severe("Price point could not be found.");
                    message = "Sorry your request could not be completed.";
                    out.print(message);
                    ConfigureResponse.responseConfigurer(resp, serviceExperienceConfig, smsc, message, true);
                    return;
                }

                String transactionID = UidGen.generateSecureUID();
                String completeTransnxnUrl = fullContextPath + "/sendinfo_push.jsp?msisdn=" + URLEncoder.encode(msisdn, "UTF-8")
                        + "&keyword=" + URLEncoder.encode(service.getKeyword().toUpperCase(), "UTF-8") + "&dest=" + URLEncoder.encode(dest, "UTF-8")
                        + "&siteId=" + URLEncoder.encode(site.getSiteId(), "UTF-8") + "&transId=" + URLEncoder.encode(transactionID, "UTF-8");
                if (pricePoint.getBillingMech().equals(PricePoint.OT_BILL)) {
                    completeTransnxnUrl = completeTransnxnUrl + "&sender=KEYWORD";
                }

                Transaction transaction = new Transaction(transactionID, service.getAccountID(), service.getKeyword(), msisdn, new Date(), message,
                        completeTransnxnUrl, false, false, pricePoint.getPricePointID());
                Transaction.createTransaction(transaction);

                boolean billed = PaymentManager.doPayment(pricePoint, cnxn, msisdn, message, null, completeTransnxnUrl, dest, service.getKeyword());
                req.setAttribute("x-kannel-header-binfo", transactionID);
                logger.info(service.getKeyword() + ": " + msisdn + ": Completed initiatePayment from sendinfo with result: " + billed);
                if (billed) {

                } else {


                    message = "We've received your request for a " + service.getServiceName() + " item. Please be patient while we process it.";
                    out.print(message);
                    ConfigureResponse.responseConfigurer(resp, serviceExperienceConfig, smsc, message, true);
                    return;
                }

            }

            message = message.replace("\n", "").replace("\r", "");
            if (metaDataMap.containsKey(Metadata.FREE_CONTENT) && metaDataMap.get(Metadata.FREE_CONTENT).equalsIgnoreCase("TRUE")) {
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, serviceExperienceConfig, smsc, message, true);
            } else {
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, serviceExperienceConfig, smsc, message, isFree);
            }
        } catch (Exception e) {
            logger.severe("Service look-up threw an exception: " + e.getMessage());
            Properties properties = Utils.loadPropertyFile("rmcs.properties");
            String prop = properties.getProperty("nothing_found_message");

            out.print(prop != null ? prop.replace("@@short_code@@", dest.substring(dest.indexOf("+") + 1))
                    : "We're sorry we couldn't find what you were looking for. You can send HELP to " + dest.substring(dest.indexOf("+") + 1) + " for options.");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


}

