/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

import com.rancard.mobility.common.ThreadedMessageSender;
import com.rancard.mobility.common.ThreadedPostman;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.common.services.UserService;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nii
 */
public class VMServiceManager {

    public static void createUser(String msisdn, String accountId, String keyword,
            String username, int points) throws Exception {
        VMUser user = new VMUser(new Date(), msisdn, accountId, keyword, username, points);
        VMUserDB.createUser(user);
    }

    public static VMUser viewUser(String keyword, String accountId, String msisdn) throws
            Exception {
        return VMUserDB.viewUser(keyword, accountId, msisdn);
    }

    public static void addPoints(String keyword, String accountId, String msisdn, int point) throws
            Exception {
        VMUserDB.addPoints(keyword, accountId, msisdn, point);
    }

    public static void createTransaction(String campaignID, String recruiterMsisdn,
            String recipientMsisdn, VMTransactionStatus status, String itemID, String category, String shortURL) throws Exception {
        VMTransaction transaction = new VMTransaction(new Date(), campaignID, recruiterMsisdn, recipientMsisdn, status,
                itemID, category, shortURL);
        VMTransactionDB.createTransaction(transaction);
    }

    public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn) throws
            Exception {
        return VMTransactionDB.viewTransaction(campaignId, recipientMsisdn);
    }

    public static VMTransaction viewTransaction(String accountId, String keyword, String recipientMsisdn) throws
            Exception {
        return VMTransactionDB.viewTransaction(accountId, keyword, recipientMsisdn);
    }

    public static void updateTransactionStatus(String campaignId, String recipientMsisdn, VMTransactionStatus status) throws
            Exception {
        VMTransactionDB.updateTransactionStatus(campaignId, recipientMsisdn, status);
    }

    public static void updateTransactionStatus(String campaignId, String recipientMsisdn, VMTransactionStatus status,
            int points) throws Exception {
        VMTransactionDB.updateTransactionStatus(campaignId, recipientMsisdn, status, points);
    }

    public static void createCampaign(String campaignID, String networkID, String accountID, String keyword, String messageSender,
            String message, String howToMessage, String followUpMessageSuccess, String followUpMessageError,
            String inviteAcceptedMessage, String alreadyInvitedMessage, boolean isActive, int pushWaitTime) throws Exception {

        VMCampaign campaign = new VMCampaign(campaignID, accountID, keyword, messageSender, message, howToMessage,
                followUpMessageSuccess, followUpMessageError, inviteAcceptedMessage, alreadyInvitedMessage, new Date(),
                isActive, pushWaitTime);
        VMCampaignDB.createCampaign(campaign);
    }

    public static VMCampaign viewCampaign(String campaignId) throws
            Exception {
        return VMCampaignDB.viewCampaign(campaignId);
    }

    public static VMCampaign viewCampaign(String accountId, String keyword) throws
            Exception {
        return VMCampaignDB.viewCampaign(accountId, keyword);
    }

    public static void updateCampaignMessage(String campaignId, String message) throws
            Exception {
        VMCampaignDB.updateCampaignMessage(campaignId, message);
    }

    public static void updateCampaign(VMCampaign campaign, String update_accountId, String update_keyword) throws Exception {
        VMCampaignDB.updateCampaign(campaign, update_accountId, update_keyword);
    }

    public static String sendInvitation(String campaignID, String recipList, String recipRegex,
            String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits,
            String msisdn, boolean useMsisdnAsSender) throws Exception {

        VMCampaign campaign = VMServiceManager.viewCampaign(campaignID);

        if (campaign == null) {
            return VMCampaign.DEFAULT_FOLLOW_UP_ERROR_MSG;
        } else {
            return sendInvitation(campaign.getAccountID(), campaign.getKeyword(), recipList,
                    recipRegex, recipUnifiedPrefix, senderUnifiedPrefix, numSignificantDigits,
                    msisdn, useMsisdnAsSender);
        }

    }

    public static void handleViralMarketing(UserService srvc, String msisdn) {
        try {
            VMCampaign campaign = VMCampaignDB.viewCampaign(srvc.getAccountId(), srvc.getKeyword());
            if (campaign == null || !campaign.getIsActive()) {
                return;
            }

            CPConnections cnxn = CPConnections.getConnection(srvc.getAccountId(), msisdn);
            VMUser vmUser = VMUserDB.viewUser(srvc.getKeyword(), srvc.getAccountId(), msisdn);
            String username = (vmUser == null || vmUser.getUsername() == null || vmUser.getUsername().equals("")) ? msisdn : vmUser.getUsername();

            String howToMessage = (campaign.getHowToMessage() == null || campaign.getHowToMessage().equals("")) 
                    ? VMCampaign.getDefaultHowToMessage(campaign.getMessageSender()) : campaign.getHowToMessage();
            new Thread(new ThreadedMessageSender(cnxn, msisdn, campaign.getMessageSender(),
                    VMServiceManager.escapeVMMessage(howToMessage, username, srvc.getKeyword(), srvc.getServiceName(),
                    vmUser == null ? 0 : vmUser.getPoints()), campaign.getPushWaitTime())).start();

            VMTransaction trans = VMTransactionDB.viewTransaction(campaign.getCampaignID(), msisdn);
            if (trans != null && trans.getStatus() == VMTransactionStatus.INV_SENT) {
                VMServiceManager.updateTransactionStatus(trans.getCampaignID(), msisdn, VMTransactionStatus.INV_ACCEPTED, 10);

                String success = (campaign.getInviteAcceptedMessage() == null || campaign.getInviteAcceptedMessage().equals(""))
                        ? VMCampaign.getDefaultInvitationAcceptedMessage(trans.getRecipientMsisdn()) : campaign.getInviteAcceptedMessage();
                new Thread(new ThreadedMessageSender(cnxn, trans.getRecruiterMsisdn(), campaign.getMessageSender(),
                        VMServiceManager.escapeVMMessage(success, username, srvc.getKeyword(), srvc.getServiceName(),
                        vmUser == null ? 0 : vmUser.getPoints()), campaign.getPushWaitTime())).start();
            }

        } catch (Exception ex) {
            System.out.println(new Date() + "\t[" + VMServiceManager.class + "]\tERROR\tException caught processing viral marketing " + ex.getMessage());
        }
    }

    public static String sendInvitation(String accountID, String keyword, String recipList, String recipRegex,
            String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits,
            String msisdn, boolean useMsisdnAsSender) throws Exception {

        return sendInvitation(accountID, keyword, recipList, recipRegex, recipUnifiedPrefix,
                senderUnifiedPrefix, numSignificantDigits, msisdn, useMsisdnAsSender, null);
    }

    public static String sendInvitation(String accountID, String keyword, String recipList, String recipRegex,
            String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits,
            String msisdn, boolean useMsisdnAsSender, String usersName) throws Exception {

        VMCampaign campaign;
        String campaignMessage;
        String followUpMessage = null;
        try {
            campaign = VMServiceManager.viewCampaign(accountID, keyword);

            if (campaign == null) {
                return VMCampaign.DEFAULT_FOLLOW_UP_ERROR_MSG;
            }

            if (usersName != null) {
                campaignMessage = (campaign.getMessage() + " -" + usersName).trim();

                if (campaignMessage.length() > 160) {
                    for (int i = usersName.split(" ").length - 1; i > 0; i--) {
                        if ((campaign.getMessage() + " -" + usersName.split(" ")[i]).trim().length() < 160 && usersName.split(" ")[i].trim().length() > 0) {
                            campaignMessage = campaign.getMessage() + " -" + usersName.split(" ")[i];
                            break;
                        }
                    }

                    if (campaignMessage.length() > 160) {
                        campaignMessage = campaign.getMessage();
                    }
                }
            } else {
                campaignMessage = campaign.getMessage();
            }



            String campaignMessageSender;
            if (useMsisdnAsSender) {
                campaignMessageSender = normalizeNumber(msisdn, senderUnifiedPrefix, numSignificantDigits);
            } else {
                campaignMessageSender = campaign.getMessageSender();
            }

            if (VMUserDB.viewUser(keyword, accountID, msisdn) == null) {
                VMServiceManager.createUser(msisdn, campaign.getAccountID(), campaign.getKeyword(), "", 0);
            }

            Map<String, String> params = new HashMap<String, String>();

            Pattern pattern = Pattern.compile(recipRegex);
            for (int i = 0; i < recipList.split(" ").length; i++) {
                String recipient = recipList.split(" ")[i];
                if (recipient.equalsIgnoreCase("FROM") || recipient.equalsIgnoreCase("FRM") || recipient.equalsIgnoreCase("FM")) {
                    break;
                }

                Matcher matcher = pattern.matcher(recipient);

                if (matcher.matches()) {
                    String formattedRecipient = normalizeNumber(recipient, recipUnifiedPrefix, numSignificantDigits);

                    // Check to make sure subscriber isn't being silly by sending to himself
                    if (!msisdn.equalsIgnoreCase(formattedRecipient)) {

                        if (VMServiceManager.viewTransaction(campaign.getCampaignID(), formattedRecipient) == null) {
                            VMServiceManager.createTransaction(campaign.getCampaignID(), msisdn, formattedRecipient, VMTransactionStatus.INV_SENT,
                                    keyword, null, null);

                            CPConnections cnxn = CPConnections.getConnection(campaign.getAccountID(), msisdn);
                            (new Thread(new ThreadedMessageSender(cnxn, formattedRecipient, campaignMessageSender, campaignMessage, 0))).start();

                            params.put("recruiter", msisdn.substring(msisdn.indexOf("+") + 1));
                            params.put("recipient", formattedRecipient.substring(formattedRecipient.indexOf("+") + 1));
                            params.put("keyword", campaign.getKeyword());
                            new ThreadedPostman(ThreadedPostman.RNDVU_CONNECT_USER_API_TMPLT, params).run();

                            followUpMessage = (campaign.getFollowUpMessageSuccess() == null) ? VMCampaign.DEFAULT_FOLLOW_UP_SUCCESS_MSG : campaign.getFollowUpMessageSuccess();
                        } else {
                            followUpMessage = (campaign.getAlreadyInvitedMessage() == null) ? VMCampaign.DEFAULT_ALREADY_INVITED_MESSAGE : campaign.getAlreadyInvitedMessage();
                        }
                    }
                } else {
                    followUpMessage = (campaign.getFollowUpMessageError() == null) ? VMCampaign.DEFAULT_FOLLOW_UP_ERROR_MSG : campaign.getFollowUpMessageError();
                }
            }

            return followUpMessage;

        } catch (Exception e) {
            System.out.println(new Date() + ": " + VMServiceManager.class + ":ERROR Problem getting campaign. Probably related to the database: " + e.getMessage());
            return VMCampaign.DEFAULT_FOLLOW_UP_ERROR_MSG;
        }
    }

    public static String escapeVMMessage(String message, String displayName, String keyword, String serviceName, int points) {
        final String insertions = "display_name=" + displayName + "&keyword=" + keyword
                + "&service_name=" + serviceName + "&points=" + points;
        return com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, message);
    }

    private static String normalizeNumber(String msisdn, String unifiedPrefix, int numDigits) throws Exception {

        if (numDigits > msisdn.length()) {
            throw new Exception("Cannot normalize number. Invalid num_digits parameter passed");
        }

        return unifiedPrefix + msisdn.substring(msisdn.length() - numDigits, msisdn.length());
    }

    //the 3 functions below are for promotional campaigns.
    public static PromoImpression viewPromoImpression(long hashCode) throws
            Exception {
        return PromoImpressionDB.viewPromoImpression(hashCode);
    }

    public static PromoImpression viewPromoImpression(String msisdn, String keyword, String accountID) throws
            Exception {
        return PromoImpressionDB.viewPromoImpression(msisdn, keyword, accountID);
    }

    public static void updatePromoViewDate(PromoImpression impression) throws Exception {
        PromoImpressionDB.updatePromoViewDate(impression);
    }

    public static void createPromoImpression(PromoImpression promoImpression) throws
            Exception {
        PromoImpressionDB.createEntry(promoImpression);
    }

    public static void updateAdRespSummary(PromoImpression promoImpression) throws
            Exception {
        PromoImpressionDB.updateAdResponseSummary(promoImpression);
    }
}
