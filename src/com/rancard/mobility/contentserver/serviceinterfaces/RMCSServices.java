package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.*;
import com.rancard.common.key.ServiceExperienceConfigKey;
import com.rancard.common.key.ServiceSubscriptionKey;
import com.rancard.mobility.common.ServiceMatcher;
import com.rancard.mobility.common.SimpleServiceMatcher;
import com.rancard.mobility.common.ThreadedMessageSender;
import com.rancard.mobility.contentserver.BaseServlet;
import com.rancard.mobility.contentserver.DefaultViralMarketing;
import com.rancard.mobility.contentserver.EMF;
import com.rancard.mobility.contentserver.ViralMarketing;
import com.rancard.mobility.contentserver.payment.PaymentManager;
import com.rancard.mobility.contentserver.serviceinterfaces.config.ConfigureResponse;
import com.rancard.util.DateUtil;
import com.rancard.util.UidGen;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Mustee on 3/30/2014.
 */
public class RMCSServices extends BaseServlet {
    private final Logger logger = Logger.getLogger(RMCSServices.class.getName());


    @Override
    public void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received Request. Query String: " + req.getQueryString());
        EntityManager em = EMF(req).createEntityManager();

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

            List<MobileNetworkOperator> operators = req.getAttribute("operators") == null ? MobileNetworkOperator.getAll(em) : (List<MobileNetworkOperator>) req.getAttribute("operators");
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
            ServiceDefinition service = serviceMatcher.matchService(messageCaps, ServiceDefinition.findBySMSCAndShortCode(em, smsc, dest), 0.4);
            if (service == null) {
                logger.severe("Service could not be found.");
                message = "Try out our services! Send MUSIC, SPORTS, NEWS or anything of interest to " + dest + ". If you like them, send your friend's number to "
                        + dest + " to share the fun with them.";
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, null, smsc, message, true);
                return;
            }

            if (service.getServiceDefinitionKey().getAccountID().equals("177")) {
                logger.info("This is JobMatch so we do stuff");
                ServiceSubscription subscription = em.find(ServiceSubscription.class, new ServiceSubscriptionKey("177", "GENERAL", msisdn));
                if (StringUtils.equalsIgnoreCase(service.getServiceDefinitionKey().getKeyword(), "JobMatch") || StringUtils.equalsIgnoreCase(service.getServiceDefinitionKey().getKeyword(), "Match")
                        || StringUtils.equalsIgnoreCase(service.getServiceDefinitionKey().getKeyword(), "Job")) {
                    if (subscription == null) {
                        Date date = new Date();
                        ServiceSubscription.createSubscription(em, new ServiceSubscription(new ServiceSubscriptionKey(service.getServiceDefinitionKey().getAccountID(),
                                "GENERAL", msisdn), date, DateUtil.formatToTimeStamp(DateUtil.addDaysToDate(date, 1)), 1, 1, Channel.SMS, null));

                        ConfigureResponse.responseConfigurer(resp, null, smsc, service.getDefaultMessage(), true);
                        out.print(service.getDefaultMessage());
                        return;
                    }
                } else {
                    if (subscription != null) {
                        ServiceSubscription.deleteSubscription(em, new ServiceSubscriptionKey(service.getServiceDefinitionKey().getAccountID(), "GENERAL", msisdn));
                    }
                }
            }

            CPSite site = CPSite.getSMSSite(em, service.getServiceDefinitionKey().getAccountID());
            if (site == null) {
                logger.severe("CPSite not found.");
                message = "Sorry your request could not be completed.";
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, null, smsc, message, true);
                return;
            }

            CPConnection cnxn = CPConnection.getCPConnection(em, service.getServiceDefinitionKey().getAccountID(), smsc);
            if (cnxn == null) {
                logger.severe("CPConnection not found.");
                message = "Sorry your request could not be completed.";
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, null, smsc, message, true);
                return;
            }

            ServiceExperienceConfig serviceExperienceConfig = em.find(ServiceExperienceConfig.class, new ServiceExperienceConfigKey(service.getServiceDefinitionKey().getAccountID(),
                    site.getSiteID(), service.getServiceDefinitionKey().getKeyword()));
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
                        PromotionalCampaign campaign = serviceExperienceConfig.getPromotionalCampaign();
                        if (campaign != null) {
                            pushMsg = campaign.getPromoMessage();
                            pushMsgSender = serviceExperienceConfig.getPromoMessageSender();
                        }
                    } else {
                        keyword = metaDataMap.get(Metadata.OVERRIDE_KEYWORD) == null ?
                                service.getServiceDefinitionKey().getKeyword() : metaDataMap.get(Metadata.OVERRIDE_KEYWORD);

                        ServiceSubscription subscription = em.find(ServiceSubscription.class, new ServiceSubscriptionKey(
                                service.getServiceDefinitionKey().getAccountID(), keyword, msisdn));
                        if (subscription != null) {
                            if (subscription.getNextSubscriptionDate() == null) {
                                logger.info("Subscription already exists");

                                pushMsg = serviceExperienceConfig.getAlreadySubscribedMessage();
                                pushMsgSender = StringUtils.isBlank(serviceExperienceConfig.getAlreadySubscribedMessageSender())
                                        ? dest : serviceExperienceConfig.getAlreadySubscribedMessageSender();
                            } else {
                                boolean allowExtension = metaDataMap.containsKey(Metadata.ALLOW_EXTENSION) && metaDataMap.get(Metadata.ALLOW_EXTENSION).equalsIgnoreCase("TRUE");
                                Date nextSubscriptionDate = DateUtil.convertFromTimeStampFormat(subscription.getNextSubscriptionDate());
                                if (nextSubscriptionDate.after(new Date()) && allowExtension) {
                                    Date newDate = DateUtil.addDaysToDate(nextSubscriptionDate, serviceExperienceConfig.getSubscriptionInterval());
                                    em.getTransaction().begin();
                                    subscription.setNextSubscriptionDate(DateUtil.formatToTimeStamp(newDate));
                                    em.getTransaction().commit();

                                    pushMsg = "Dear subscriber, your subscription to " + service.getServiceName() + " has been extended and it will now expire on " + DateUtil.formatToShort(newDate);
                                } else if (!allowExtension) {
                                    pushMsg = serviceExperienceConfig.getAlreadySubscribedMessage();
                                } else {
                                    Date newDate = DateUtil.addDaysToDate(new Date(), serviceExperienceConfig.getSubscriptionInterval());
                                    em.getTransaction().begin();
                                    subscription.setNextSubscriptionDate(DateUtil.formatToTimeStamp(newDate));
                                    em.getTransaction().commit();

                                    pushMsg = "Dear subscriber, your subscription to " + service.getServiceName() + " has been renewed and it will now expire on " + DateUtil.formatToShort(newDate);

                                }

                                pushMsgSender = StringUtils.isBlank(serviceExperienceConfig.getAlreadySubscribedMessageSender()) ? dest : serviceExperienceConfig.getAlreadySubscribedMessageSender();
                            }
                        } else {
                            Date now = new Date();
                            if (metaDataMap.containsKey(Metadata.FREEMIUM) && metaDataMap.get(Metadata.FREEMIUM).equalsIgnoreCase("NO")) {
                                ServiceSubscription.createSubscription(em, new ServiceSubscription(new ServiceSubscriptionKey(service.getServiceDefinitionKey().getAccountID(),
                                        service.getServiceDefinitionKey().getKeyword(), msisdn), now, DateUtil.formatToTimeStamp(DateUtil.addDaysToDate(now,
                                        serviceExperienceConfig.getSubscriptionInterval())), 1, 1, Channel.SMS, null));
                            } else {
                                ServiceSubscription.createSubscription(em, new ServiceSubscription(new ServiceSubscriptionKey(service.getServiceDefinitionKey().getAccountID(),
                                        service.getServiceDefinitionKey().getKeyword(), msisdn), now, DateUtil.formatToTimeStamp(DateUtil.addDaysToDate(now,
                                        serviceExperienceConfig.getSubscriptionInterval())), 1, 1, Channel.SMS, null));

                            }

                            Utils.postSubscriptionNotification(msisdn, service.getServiceDefinitionKey().getKeyword());

                            pushMsg = serviceExperienceConfig.getWelcomeMessage();
                            pushMsgSender = StringUtils.isBlank(serviceExperienceConfig.getWelcomeMessageSender()) ? dest : serviceExperienceConfig.getWelcomeMessageSender();

                            VMCampaign campaign = VMCampaign.findByService(em, service);
                            ViralMarketing viralMarketing = new DefaultViralMarketing(campaign);
                            viralMarketing.sendHowToMessage(cnxn, msisdn, smsc, dest);
                            viralMarketing.updateVMTransaction(em, cnxn, msisdn, smsc);

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
                        (new Thread(new ThreadedMessageSender(cnxn, msisdn, pushMsgSender, pushMsg, null, service.getServiceDefinitionKey().getAccountID(),
                                service.getServiceDefinitionKey().getKeyword(), "RMCS", smsc, 0, 0))).start();
                    }

                }
            }

            SystemSMSQueue systemSMSQueue = SystemSMSQueue.find(em, service.getServiceDefinitionKey().getAccountID(), service.getServiceDefinitionKey().getKeyword());
            message = (systemSMSQueue == null || StringUtils.isBlank(systemSMSQueue.getMessage())) ? service.getDefaultMessage() : systemSMSQueue.getMessage();
            boolean isFree = false;
            if (StringUtils.isBlank(message)) {
                message = "No info is currently available for " + service.getServiceName() + ". Please try again later.";
                out.print(message);
                ConfigureResponse.responseConfigurer(resp, serviceExperienceConfig, smsc, message, true);
                return;
            }

            if (!((alreadyBilled != null && Boolean.getBoolean(alreadyBilled)) || StringUtils.isBlank(service.getPricing()))) {
                PricePoint pricePoint = em.find(PricePoint.class, service.getPricing());

                if (pricePoint == null) {
                    logger.severe("Price point could not be found.");
                    message = "Sorry your request could not be completed.";
                    out.print(message);
                    ConfigureResponse.responseConfigurer(resp, serviceExperienceConfig, smsc, message, true);
                    return;
                }

                String transactionID = UidGen.generateSecureUID();
                String completeTransnxnUrl = fullContextPath + "/sendinfo_push.jsp?msisdn=" + URLEncoder.encode(msisdn, "UTF-8")
                        + "&keyword=" + URLEncoder.encode(service.getServiceDefinitionKey().getKeyword().toUpperCase(), "UTF-8") + "&dest=" + URLEncoder.encode(dest, "UTF-8")
                        + "&siteId=" + URLEncoder.encode(site.getSiteID(), "UTF-8") + "&transId=" + URLEncoder.encode(transactionID, "UTF-8");
                if (pricePoint.getBillingMech().equals(PricePoint.OT_BILL)) {
                    completeTransnxnUrl = completeTransnxnUrl + "&sender=KEYWORD";
                }

                Transaction transaction = new Transaction(transactionID, service, msisdn, new Date(), message,
                        completeTransnxnUrl, false, false, pricePoint);
                Transaction.createTransaction(em, transaction);

                boolean billed = PaymentManager.doPayment(pricePoint, cnxn, msisdn, message, null, completeTransnxnUrl, dest, service.getServiceDefinitionKey().getKeyword());
                req.setAttribute("x-kannel-header-binfo", transactionID);
                logger.info(service.getServiceDefinitionKey().getKeyword() + ": " + msisdn + ": Completed initiatePayment from sendinfo with result: " + billed);
                if (billed) {
                    em.getTransaction().begin();
                    transaction.setIsBilled(true);
                    transaction.setIsCompleted(true);
                    em.getTransaction().commit();

                    isFree = true;
                } else {
                    em.getTransaction().begin();
                    transaction.setIsCompleted(true);
                    em.getTransaction().commit();

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
            out.print("We're sorry we couldn't find what you were looking for. You can send HELP to " + dest.substring(dest.indexOf("+") + 1) + " for options.");
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

