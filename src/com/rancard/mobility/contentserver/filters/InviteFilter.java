package com.rancard.mobility.contentserver.filters;

import com.rancard.common.*;
import com.rancard.mobility.common.ThreadedMessageSender;
import com.rancard.mobility.contentserver.BaseServlet;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Mustee on 4/5/2014.
 */
public class InviteFilter extends BaseServlet implements Filter {
    private final Logger logger = Logger.getLogger(InviteFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("Query String: " + ((HttpServletRequest) servletRequest).getQueryString());

        String keyword = servletRequest.getParameter("keyword");
        String msg = servletRequest.getParameter("msg");
        String dest = servletRequest.getParameter("dest");
        String msisdn = servletRequest.getParameter("msisdn");
        String smsc = servletRequest.getParameter("smsc");

        PrintWriter out = servletResponse.getWriter();

        try {
            logger.fine("Check that smsc and msisdn are not empty or null.");

            if (StringUtils.isAnyBlank(smsc, msisdn, dest)) {
                logger.severe("Not all required parameters have been set.");
                configureResponse((HttpServletResponse) servletResponse, null, smsc, dest);
                out.print("Not all required parameters have been set.");
                return;
            }

            final String messageCaps = (StringUtils.isBlank(keyword) ? "" : keyword.trim()) + " " + (StringUtils.isBlank(msg) ? "" : msg.trim());
            if (StringUtils.isBlank(messageCaps)) {
                logger.severe("Message is blank.");
                out.print("We're sorry we couldn't find what you were looking for. You can send HELP to "
                        + dest.substring(dest.indexOf("+") + 1) + " for options.");
                return;
            }
            logger.fine("Message is OK: " + messageCaps + ". Check if MSISDN is valid: " + msisdn);

            List<MobileNetworkOperator> operators = servletRequest.getAttribute("operators") == null ?
                    MobileNetworkOperator.getAll() : (List<MobileNetworkOperator>) servletRequest.getAttribute("operators");
            Set<String> msisdns = Utils.getMSISDNs(operators, messageCaps);

            if (msisdns.size() == 0 && StringUtils.containsIgnoreCase(messageCaps, INVITE)) {
                logger.severe("A valid phone number could not be found.");
                configureResponse((HttpServletResponse) servletResponse, null, smsc, dest);
                out.print(VMCampaign.DEFAULT_HOW_TO_MESSAGE);
                return;
            } else if (msisdns.size() == 0) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            if (!MobileNetworkOperator.isValidMSISDN(operators, smsc, msisdn)) {
                logger.severe("MSISDN is not valid.");
                configureResponse((HttpServletResponse) servletResponse, null, smsc, dest);
                out.print("Sorry, Your mobile number may not be formatted correctly.");
                return;
            }

            logger.info("MSISDN is valid. Will format.");
            msisdn = MobileNetworkOperator.formatToInternationalFormat(operators, smsc, msisdn);

            ServiceSubscription serviceSubscription = ServiceSubscription.getLastSubscription(msisdn);
            if (serviceSubscription == null) {
                configureResponse((HttpServletResponse) servletResponse, null, smsc, dest);
                out.print("Sorry please subscribe to one of our services to invite your friends.");
                return;
            }

            VMCampaign campaign = VMCampaign.findByService(serviceSubscription.getAccountID(), serviceSubscription.getKeyword());
            CPConnection cnxn = (campaign == null) ? null : CPConnection.getCPConnection(campaign.getAccountID(), smsc);
            if (campaign == null || cnxn == null) {
                configureResponse((HttpServletResponse) servletResponse, null, smsc, dest);
                out.print(VMCampaign.DEFAULT_FOLLOW_UP_ERROR_MSG);
                return;
            }

            msisdns = convertMSISDNs(msisdns, operators, smsc);

            //COmmented out to allow multiple invites
            /*if (VMTransaction.vmTransactionsExists(em, campaign.getCampaignID(), msisdns)) {
                configureResponse(em, (HttpServletResponse) servletResponse, campaign, smsc, dest);
                out.print(campaign.getAlreadyInvitedMessage() == null ? VMCampaign.DEFAULT_ALREADY_INVITED_MESSAGE : campaign.getAlreadyInvitedMessage());
                return;
            }*/

            for (String num : msisdns) {
                num = MobileNetworkOperator.formatToInternationalFormat(operators, smsc, num);

                (new Thread(new ThreadedMessageSender(cnxn, num, campaign.getMessageSender(), campaign.getMessage(), null, serviceSubscription.getAccountID(),
                        serviceSubscription.getKeyword(), "RMCS", smsc, 0, 0))).start();

                if(VMTransaction.find(campaign.getCampaignID(), num) == null) {
                    VMTransaction transaction = new VMTransaction(campaign.getCampaignID(), msisdn, num, new Date(), VMTransactionStatus.INV_SENT,
                            campaign.getKeyword(), null, null);
                    VMTransaction.createVMTransaction(transaction);
                }
            }

            configureResponse((HttpServletResponse) servletResponse, campaign, smsc, dest);
            out.print(campaign.getFollowUpMessageSuccess() == null ? VMCampaign.DEFAULT_FOLLOW_UP_SUCCESS_MSG : campaign.getFollowUpMessageSuccess());

        } catch (Exception e) {
            logger.severe("Service look-up threw an exception: " + e.getMessage());
            try {
                configureResponse((HttpServletResponse) servletResponse, null, smsc, dest);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            out.print("We're sorry we couldn't find what you were looking for. You can send HELP to "
                    + dest.substring(dest.indexOf("+") + 1) + " for options.");
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }

        }

    }

    private void configureResponse(HttpServletResponse servletResponse, VMCampaign vmCampaign, String smsc, String shortCode) throws Exception {
        servletResponse.setHeader("X-Kannel-SMSC", getSMSC(vmCampaign, smsc));
        servletResponse.setHeader("X-Kannel-From", (vmCampaign == null) ? shortCode : vmCampaign.getMessageSender());
    }

    private String getSMSC(VMCampaign vmCampaign, String smsc) throws Exception {
        CPSite cpSite = (vmCampaign == null) ? null : CPSite.getSMSSite(vmCampaign.getAccountID());
        if(cpSite == null){
            return smsc;
        }

        ServiceExperienceConfig config = ServiceExperienceConfig.find(vmCampaign.getKeyword(), vmCampaign.getAccountID(), cpSite.getSiteId());
        Map<Metadata, String> metaData = config == null ? null : Utils.createMetaDataMap(config.getMetaData());
        String freeSMSC = (metaData == null) ? null : metaData.get(Metadata.FREE_SMSC);


        if (freeSMSC == null) {
            if (smsc.toUpperCase().contains("MTNGH")) {
                return "MTNGH";
            }
            return smsc;
        } else {
            return freeSMSC;
        }
    }

    private Set<String> convertMSISDNs(Set<String> msisdns, List<MobileNetworkOperator> operators, String smsc) {
        Set<String> nums = new HashSet<String>();
        for (String msisdn : msisdns) {
            nums.add(MobileNetworkOperator.formatToInternationalFormat(operators, smsc, msisdn));
        }

        return nums;
    }
}
