/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

import com.rancard.mobility.common.ThreadedMessageSender;
import com.rancard.mobility.contentserver.CPConnections;
import java.util.*;

/**
 *
 * @author nii
 */
public class VMServiceManager {

    public static void createUser(String msisdn, String accountId, String keyword,
            String username, int points) throws Exception {
        VMUser user = new VMUser(msisdn, accountId, keyword, username, points);
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

    public static void createTransaction(String campaignId, String recruiterMsisdn,
            String recipientMsisdn, String status) throws Exception {
        VMTransaction transaction = new VMTransaction(campaignId, recruiterMsisdn, recipientMsisdn, status);
        VMTransactionDB.createTransaction(transaction);
    }

    public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn) throws
            Exception {
        return VMTransactionDB.viewTransaction(campaignId, recipientMsisdn);
    }
    
    public static VMTransaction viewTransaction(String campaignId, String recipientMsisdn, String recruiterMsisdn, boolean sendReminderIfExists) throws
            Exception {
        return VMTransactionDB.viewTransaction(campaignId, recipientMsisdn, recruiterMsisdn, sendReminderIfExists);
    }

    public static VMTransaction viewTransaction(String accountId, String keyword, String recipientMsisdn) throws
            Exception {
        return VMTransactionDB.viewTransaction(accountId, keyword, recipientMsisdn);
    }

    public static void updateTransactionStatus(String campaignId, String recipientMsisdn, String status) throws
            Exception {
        VMTransactionDB.updateTransactionStatus(campaignId, recipientMsisdn, status);
    }

    public static void updateTransactionStatus(String campaignId, String recipientMsisdn, String status,
            int points) throws Exception {
        VMTransactionDB.updateTransactionStatus(campaignId, recipientMsisdn, status, points);
    }

    public static void createCampaign(String campaignId, String accountId, String keyword,
            String messageSender, String message) throws Exception {
        VMCampaign campaign = new VMCampaign(campaignId, accountId, keyword, messageSender, message);
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

    public static String sendInvitation(String campaignId, String recipList, String recipRegex,
            String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits,
            String msisdn, boolean useMsisdnAsSender) throws Exception {

        String message = "";

        VMCampaign campaign = VMServiceManager.viewCampaign(campaignId);
        String campaign_message = campaign.getMessage();
        String campaign_keyword = campaign.getKeyword();
        String campaign_account_id = campaign.getAccountId();
        String campaign_message_sender = "";
        if (useMsisdnAsSender) {
            campaign_message_sender = normalizeNumber(msisdn, senderUnifiedPrefix, numSignificantDigits);
        } else {
            campaign_message_sender = campaign.getMessageSender();
        }

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(recipRegex);
        ArrayList<String> formattedNumbers = new ArrayList<String>();

        // Register use in vm_users table
        VMServiceManager.createUser(msisdn, campaign_account_id, campaign_keyword, "", 0);

        String[] rawNumbers = recipList.split(" ");
        for (int i = 0; i < rawNumbers.length; i++) {
            String recipient = rawNumbers[i];
            java.util.regex.Matcher matcher = pattern.matcher(recipient);
            if (matcher.matches()) {
                String formattedRecipient = normalizeNumber(recipient, recipUnifiedPrefix, numSignificantDigits);

                // Check to make sure subscriber isn't being silly by sending to himself
                if (!msisdn.equalsIgnoreCase(formattedRecipient)) {

                    if (VMServiceManager.viewTransaction(campaignId, formattedRecipient).isEmptyTransaction()) {
                        // All checks passed at this point, so add recipient
                        formattedNumbers.add(formattedRecipient);
                        VMServiceManager.createTransaction(campaignId, msisdn, formattedRecipient, "inv_sent");
                    }

                }

            } else {
                //Badly formatted number. Do nothing
            }
        }

        // Push messages
        CPConnections cnxn = new CPConnections();
        cnxn = CPConnections.getConnection(campaign_account_id, msisdn);

        Iterator<String> subList = formattedNumbers.iterator();

        // Spawn off new threads to carry out sending so we don't wait for gateway response
        while (subList.hasNext()) {
            (new Thread(new ThreadedMessageSender(cnxn, subList.next(), campaign_message_sender, campaign_message, 0))).start();
        }

        // Produce output
        if (formattedNumbers.size() > 0) {
            message = "Your invitation has been sent! Invite another friend and increase your chances of winning 100 cedis in airtime";
        } else {
            message = "Sorry, your invitation couldn't be sent. Your recipients may have already been invited. Also please ensure the numbers you submitted are correctly formatted";
        }

        return message;
    }

    public static String sendInvitation(String accountId, String keyword, String recipList, String recipRegex,
            String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits,
            String msisdn, boolean useMsisdnAsSender) throws Exception {

        String message = "";

        VMCampaign campaign = VMServiceManager.viewCampaign(accountId, keyword);
        String campaign_message = campaign.getMessage();
        String campaign_keyword = campaign.getKeyword();
        String campaign_account_id = campaign.getAccountId();
        String campaign_message_sender = "";
        String campaignId = campaign.getCampaignId();
        if (useMsisdnAsSender) {
            campaign_message_sender = normalizeNumber(msisdn, senderUnifiedPrefix, numSignificantDigits);
        } else {
            campaign_message_sender = campaign.getMessageSender();
        }

        String formattedRecipient = "";
        ArrayList<String> formattedNumbers = new ArrayList<String>();

        if (campaignId != null && !campaignId.equals("")) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(recipRegex);

            // Register use in vm_users table
            VMServiceManager.createUser(msisdn, campaign_account_id, campaign_keyword, "", 0);

            String[] rawNumbers = recipList.split(" ");
            for (int i = 0; i < rawNumbers.length; i++) {
                String recipient = rawNumbers[i];
                java.util.regex.Matcher matcher = pattern.matcher(recipient);
                if (matcher.matches()) {
                    formattedRecipient = normalizeNumber(recipient, recipUnifiedPrefix, numSignificantDigits);

                    // Check to make sure subscriber isn't being silly by sending to himself
                    if (!msisdn.equalsIgnoreCase(formattedRecipient)) {
                        formattedNumbers.add(formattedRecipient);
                        if (VMServiceManager.viewTransaction(campaignId, formattedRecipient, msisdn).isEmptyTransaction()) {
                            // All checks passed at this point, so add recipient
                            VMServiceManager.createTransaction(campaignId, msisdn, formattedRecipient, "inv_sent");
                        }

                    }

                } else {
                    //Badly formatted number. Do nothing
                }
            }
        } else {
            message = "Sorry, your invitation couldn't be sent at this time.";
        }

        // Push messages
        CPConnections cnxn = new CPConnections();
        cnxn = CPConnections.getConnection(campaign_account_id, msisdn);

        Iterator<String> subList = formattedNumbers.iterator();

        // Spawn off new threads to carry out sending so we don't wait for gateway response
        while (subList.hasNext()) {
            (new Thread(new ThreadedMessageSender(cnxn, subList.next(), campaign_message_sender, campaign_message, 0))).start();
        }

        // Produce output
        if (formattedNumbers.size() > 0) {
            message = "Your invitation has been sent! Invite another friend and increase your chances of winning 100 cedis in airtime";
        } else {
            message = "Sorry, your invitation couldn't be sent. Your recipients may have already been invited. Also please ensure the numbers you submitted are correctly formatted";
        }

        return message;
    }

    public static String sendInvitation(String accountId, String keyword, String recipList, String recipRegex,
            String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits,
            String msisdn, boolean useMsisdnAsSender, String usersName) throws Exception {

        String message = "";

        VMCampaign campaign = null;
        String campaign_message = "";
        try {
            campaign = VMServiceManager.viewCampaign(accountId, keyword);
            campaign_message = campaign.getMessage();
        } catch (Exception e) {
            message = "Sorry, your invitation couldn't be sent at this time.";
            System.out.println("Problem getting campaign. Probably related to the database");
            throw new Exception();
        }

        if ((campaign.getMessage() + " -" + usersName).length() > 160) {
            String temp_name = "";
            String[] name = usersName.split(" ");
            if (name != null) {

                for (int i = name.length - 1; i > 0; i--) {
                    temp_name = usersName.substring(0, usersName.indexOf(name[i]));
                    if ((campaign_message + " -" + temp_name).length() < 160 && temp_name.length() > 0) {
                        campaign_message = (campaign_message + " -" + temp_name).trim();
                        break;
                    }
                }
            }
        } else {
            campaign_message = campaign_message + " -" + usersName;
        }
        String campaign_keyword = campaign.getKeyword();
        String campaign_account_id = campaign.getAccountId();
        String campaign_message_sender = "";
        String campaignId = campaign.getCampaignId();
        if (useMsisdnAsSender) {
            campaign_message_sender = normalizeNumber(msisdn, senderUnifiedPrefix, numSignificantDigits);
        } else {
            campaign_message_sender = campaign.getMessageSender();
        }

        String formattedRecipient = "";
        ArrayList<String> formattedNumbers = new ArrayList<String>();

        if (campaignId != null && !campaignId.equals("")) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(recipRegex);

            // Register use in vm_users table
            VMServiceManager.createUser(msisdn, campaign_account_id, campaign_keyword, "", 0);

            String[] rawNumbers = recipList.split(" ");
            for (int i = 0; i < rawNumbers.length; i++) {
                String recipient = rawNumbers[i];
                java.util.regex.Matcher matcher = pattern.matcher(recipient);
                if (matcher.matches()) {
                    formattedRecipient = normalizeNumber(recipient, recipUnifiedPrefix, numSignificantDigits);

                    // Check to make sure subscriber isn't being silly by sending to himself
                    if (!msisdn.equalsIgnoreCase(formattedRecipient)) {

                        if (VMServiceManager.viewTransaction(campaignId, formattedRecipient).isEmptyTransaction()) {
                            // All checks passed at this point, so add recipient
                            formattedNumbers.add(formattedRecipient);
                            VMServiceManager.createTransaction(campaignId, msisdn, formattedRecipient, "inv_sent");
                        }

                    }

                } else {
                    //Badly formatted number. Do nothing
                }
            }
        } else {
            System.out.println("Campaign ID is null or empty");
            message = "Sorry, your invitation couldn't be sent at this time.";
        }

        // Push messages
        //CPConnections cnxn = new CPConnections();
        CPConnections cnxn = CPConnections.getConnection(campaign_account_id, msisdn);

        Iterator<String> subList = formattedNumbers.iterator();

        // Spawn off new threads to carry out sending so we don't wait for gateway response
        while (subList.hasNext()) {
            (new Thread(new ThreadedMessageSender(cnxn, subList.next(), campaign_message_sender, campaign_message, 0))).start();
        }

        // Produce output
        if (formattedNumbers.size() > 0) {
            message = "Your invitation has been sent! Invite another friend and increase your chances of winning 100 cedis in airtime";
        } else {
            message = "Sorry, your invitation couldn't be sent. Your recipients may have already been invited. Also please ensure the numbers you submitted are correctly formatted";
        }

        return message;
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
