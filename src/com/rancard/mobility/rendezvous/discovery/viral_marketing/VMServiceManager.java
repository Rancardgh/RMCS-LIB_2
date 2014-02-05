/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.rendezvous.discovery.viral_marketing;

import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.util.SubscriberManager;
import java.util.*;

/**
 *
 * @author nii
 */
public class VMServiceManager {
    public static final String VM_SHARELINK_TAG = "#SHARELINK";
    public static final String VM_CONTENT_INSERT_TAG = "#SHARECONTENT";

    public static void createUser (String msisdn, String accountId, String keyword, String username, int points) throws Exception {
        VMUser user = new VMUser (msisdn, accountId, keyword, username, points);
        VMUserDB.createUser (user);
    }

    public static VMUser viewUser (String keyword, String accountId, String msisdn) throws
            Exception {
        return VMUserDB.viewUser (keyword, accountId, msisdn);
    }

    public synchronized static void addPoints (String keyword, String accountId, String msisdn, int point) throws
            Exception {
        VMUserDB.addPoints (keyword, accountId, msisdn, point);
    }

    public static boolean createTransaction (String campaignId, String recruiterMsisdn, String recruiterNetworkId,
            String recipientMsisdn, String recipientNetworkId, String status, String itemId, String category, String shortUrl) throws Exception {
        VMTransaction transaction = new VMTransaction (campaignId, recruiterMsisdn, recruiterNetworkId, recipientMsisdn, recipientNetworkId, status,
                itemId, category, shortUrl);
        return VMTransactionDB.createTransaction (transaction);
    }

    public static ArrayList<VMTransaction> viewTransactions (String campaignId, String recipientMsisdn) throws
            Exception {
        return VMTransactionDB.viewTransactions (campaignId, recipientMsisdn);
    }

    public static VMTransaction viewTransaction (String campaignId, String recipientMsisdn, String recruiterMsisdn) throws
            Exception {
        return VMTransactionDB.viewTransaction (campaignId, recipientMsisdn, recruiterMsisdn);
    }

    public static VMTransaction viewMostRecentTransaction (String accountId, String keyword, String recipientMsisdn) throws
            Exception {
        return VMTransactionDB.viewMostRecentTransaction (accountId, keyword, recipientMsisdn);
    }

    public static VMTransaction viewTransaction (String accountId, String keyword, String recipientMsisdn, String recruiterMsisdn) throws
            Exception {
        return VMTransactionDB.viewTransaction (accountId, keyword, recipientMsisdn, recruiterMsisdn);
    }

    public static void updateTransactionStatus (String campaignId, String recipientMsisdn, String status) throws
            Exception {
        VMTransactionDB.updateTransactionStatus (campaignId, recipientMsisdn, status);
    }

    public static void updateTransactionStatus (String campaignId, String recipientMsisdn, String recruiterMsisdn, String status,
            int points) throws Exception {
        VMTransactionDB.updateTransactionStatus (campaignId, recipientMsisdn, recruiterMsisdn, status, points);
    }

    public static void createCampaign (String campaignId, String accountId, String keyword,
            String messageSender, String message, String networkId, String howTo, String followUpMsgSuccess, String followUpMsgError) throws Exception {
        VMCampaign campaign = new VMCampaign (campaignId, accountId, keyword, messageSender, message,
                networkId, howTo, followUpMsgSuccess, followUpMsgError);
        VMCampaignDB.createCampaign (campaign);
    }

    public static VMCampaign viewCampaignByAccount (String campaignId, String accountId) throws
            Exception {
        return VMCampaignDB.viewCampaignByAccount (campaignId, accountId);
    }

    public static VMCampaign viewCampaignByNetwork (String campaignId, String networkId) throws
            Exception {
        return VMCampaignDB.viewCampaignByNetwork (campaignId, networkId);
    }

    public static VMCampaign viewCampaignByService (String accountId, String keyword) throws
            Exception {
        return VMCampaignDB.viewCampaignByService (accountId, keyword);
    }
    
    public static VMCampaign viewCampaignByServiceAndNetwork (String accountId, String keyword, String networkId) throws
            Exception {
        return VMCampaignDB.viewCampaignByServiceAndNetwork (accountId, keyword, networkId);
    }

    public static void updateCampaignMessage (String campaignId, String message) throws
            Exception {
        VMCampaignDB.updateCampaignMessage (campaignId, message);
    }

    public static VMConfig getVmConfig (String accountId) throws Exception {
        return VMConfigDB.getVmConfig (accountId);
    }
    
    public static String createHowToMessage (UserService srvc, VMCampaign campaign, String msisdn) throws Exception {
        String shareMessage = "";
        if (campaign != null && campaign.getVmHowTo () != null && !campaign.getVmHowTo ().equals ("")) {
            if (campaign.getVmHowTo ().toUpperCase ().contains (VM_SHARELINK_TAG)) { // placeholder for share link found. replace with SHARE link from VM Config for service provider
                String shortLink = generateShareLink (srvc, campaign, msisdn);
                shareMessage = campaign.getVmHowTo ().replaceAll (VM_SHARELINK_TAG, shortLink);
            } else {
                shareMessage = campaign.getVmHowTo ();
            }
        }
        
        return shareMessage;
    }
    
    /*
    public static String embedShareLink (String accountId, String keyword, String content, String msisdn) throws Exception {
        String outgoingContent = "";
        UserService srvc = ServiceManager.viewService (keyword, accountId);
        VMCampaign campaign = VMServiceManager.viewCampaignByService (accountId, keyword);
        String footer = InfoServiceDB.getServiceFooter (accountId, keyword);
        
        if (footer != null && content != null && !content.equals ("") && !footer.equals ("")) {
            if (footer.toUpperCase ().contains (VM_SHARELINK_TAG)) { // placeholder for share link found. replace with SHARE link from VM Config for service provider
                String shortLink = generateShareLink (srvc, campaign, msisdn, content);
                footer = footer.replaceAll (VM_SHARELINK_TAG, shortLink);
                outgoingContent = ((content + " " + footer).length () > 160) ? content : (content + " " + footer);
            } else {
                outgoingContent = content;
            }
        }
        
        return outgoingContent;
    }*/
    
    public static String generateShareLink (UserService srvc, VMCampaign campaign, String msisdn) throws Exception {
        String shareUrl = "";
        if (srvc != null) {
            try {
                VMConfig vmConfig = com.rancard.mobility.rendezvous.discovery.viral_marketing.VMServiceManager.getVmConfig (srvc.getAccountId ());
                if (vmConfig.getShareUrl () != null && !vmConfig.getShareUrl ().equals ("")) {
                    String outgoingMsg = srvc.getDefaultMessage ().replaceAll ("\r\n", " ");
                    outgoingMsg = outgoingMsg.replaceAll ("\r", "");
                    outgoingMsg = outgoingMsg.replaceAll ("\n", "");
                    
                    //generate content snippet
                    if (campaign.getMessage () != null && campaign.getMessage ().toUpperCase ().contains (VM_CONTENT_INSERT_TAG)) {
                        int contentSnippetLength = 0;
                        String lengthStr = campaign.getMessage ().substring (campaign.getMessage ().indexOf ("<") + 1, campaign.getMessage ().indexOf (">"));
                        String lengthConfig = campaign.getMessage ().substring (campaign.getMessage ().indexOf ("<"), campaign.getMessage ().indexOf (">") + 1);
                        try {
                            contentSnippetLength = Integer.parseInt (lengthStr);
                        } catch (Exception e) {
                        }
                        String truncatedStr = (outgoingMsg.length () >= contentSnippetLength) ? outgoingMsg.substring (0, contentSnippetLength) : outgoingMsg;
                        if (Character.isLetter (outgoingMsg.charAt (contentSnippetLength))) {
                            truncatedStr = truncatedStr.substring (0, truncatedStr.lastIndexOf (" ")) + "...";
                        }
                        
                        //embed content snippet into template
                        outgoingMsg = campaign.getMessage ().replaceAll (VM_CONTENT_INSERT_TAG, truncatedStr);
                        outgoingMsg = outgoingMsg.replaceAll (lengthConfig, "");
                    }
                    
                    String urlInsertions = "msisdn=" + java.net.URLEncoder.encode (msisdn, "UTF-8") + "&shareMessage=" + java.net.URLEncoder.encode (outgoingMsg, "UTF-8") 
                            + "&serviceName=" + java.net.URLEncoder.encode (srvc.getServiceName ().toUpperCase (), "UTF-8");
                    String resultingUrl = com.rancard.util.URLUTF8Encoder.doMessageEscaping (urlInsertions, vmConfig.getShareUrl ());
                    try {
                        shareUrl = com.rancard.util.URL.shortenUrlWithGoogle (resultingUrl);
                    } catch (Exception e) {
                        throw new Exception ("Error encountered shortening URL: " + resultingUrl + ": " + e.getMessage ());
                    }
                } else {
                    throw new Exception ("VM Config object does not have a SHARE URL defined.");
                }
            } catch (Exception e) {
            }
        }
        return shareUrl;
    }
    
    public static String generateShareLink (UserService srvc, VMCampaign campaign, String msisdn, String content) throws Exception {
        String shareUrl = "";
        if (srvc != null) {
            try {
                VMConfig vmConfig = com.rancard.mobility.rendezvous.discovery.viral_marketing.VMServiceManager.getVmConfig (srvc.getAccountId ());
                if (vmConfig.getShareUrl () != null && !vmConfig.getShareUrl ().equals ("")) {
                    String outgoingMsg = content.replaceAll ("\r\n", " ");
                    outgoingMsg = outgoingMsg.replaceAll ("\r", "");
                    outgoingMsg = outgoingMsg.replaceAll ("\n", "");
                    
                    //generate content snippet
                    if (campaign.getMessage () != null && campaign.getMessage ().toUpperCase ().contains (VM_CONTENT_INSERT_TAG)) {
                        int contentSnippetLength = 0;
                        String lengthStr = campaign.getMessage ().substring (campaign.getMessage ().indexOf ("<") + 1, campaign.getMessage ().indexOf (">"));
                        String lengthConfig = campaign.getMessage ().substring (campaign.getMessage ().indexOf ("<"), campaign.getMessage ().indexOf (">") + 1);
                        try {
                            contentSnippetLength = Integer.parseInt (lengthStr);
                        } catch (Exception e) {
                        }
                        String truncatedStr = (outgoingMsg.length () >= contentSnippetLength) ? outgoingMsg.substring (0, contentSnippetLength) : outgoingMsg;
                        if (Character.isLetter (outgoingMsg.charAt (contentSnippetLength))) {
                            truncatedStr = truncatedStr.substring (0, truncatedStr.lastIndexOf (" ")) + "...";
                        }
                        
                        //embed content snippet into template
                        outgoingMsg = campaign.getMessage ().replaceAll (VM_CONTENT_INSERT_TAG, truncatedStr);
                        outgoingMsg = outgoingMsg.replaceAll (lengthConfig, "");
                    }
                    
                    String urlInsertions = "msisdn=" + java.net.URLEncoder.encode (msisdn, "UTF-8") + "&shareMessage=" + java.net.URLEncoder.encode (outgoingMsg, "UTF-8") 
                            + "&serviceName=" + java.net.URLEncoder.encode (srvc.getServiceName ().toUpperCase (), "UTF-8");
                    String resultingUrl = com.rancard.util.URLUTF8Encoder.doMessageEscaping (urlInsertions, vmConfig.getShareUrl ());
                    try {
                        shareUrl = com.rancard.util.URL.shortenUrlWithGoogle (resultingUrl);
                    } catch (Exception e) {
                        throw new Exception ("Error encountered shortening URL: " + resultingUrl + ": " + e.getMessage ());
                    }
                } else {
                    throw new Exception ("VM Config object does not have a SHARE URL defined.");
                }
            } catch (Exception e) {
            }
        }
        return shareUrl;
    }

    public static HashMap processInvitations (String accountId, String keyword, String recipList, String recipUnifiedPrefix,
            String senderUnifiedPrefix, int numSignificantDigits, String msisdn, boolean useMsisdnAsSender, SubscriberManager subMngr) throws Exception {

        //String status = "NOK";

        HashMap responsePkg = new HashMap ();
        HashMap<VMCampaign, ArrayList<String>> campaignsAndInvitees = new HashMap<VMCampaign, ArrayList<String>> ();//storage for campaigns and users to be invited
        HashMap<String, VMCampaign> campaignHolder = new HashMap<String, VMCampaign> ();//temporary storage for campaign  versions for various networks

        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Processing VM Invitation ... ");
        
        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Looking up network ID for " + msisdn + "  ... ");
        String networkId = subMngr.getNetworkIdFromMsisdn (msisdn);
            
        //VMCampaign campaign = VMServiceManager.viewCampaignByServiceAndNetwork (accountId, keyword, networkId);
        VMCampaign campaign = VMServiceManager.viewCampaignByService (accountId, keyword);
        String campaignId = campaign.getCampaignId ();
        String outgoingOkMsg = campaign.getFollowUpMsgSuccess ();
        String outgoingErrorMsg = campaign.getFollowUpMsgError ();
        String msisdnNetworkId = "";//campaign.getNetworkId ();
        System.out.println (new java.util.Date () + ": [vm-invite:INFO]: VM Campaign details: Account ID: " + campaign.getAccountId () + " :: Campaign ID: "
                + campaignId + " :: Outgoing OK Msg: " + outgoingOkMsg + " :: Outgoing Error Msg: " + outgoingErrorMsg
                + " :: Campaign Network ID: "/* + msisdnNetworkId*/);

        //campaignHolder.put (campaign.getNetworkId (), campaign);
        campaignsAndInvitees.put (campaign, new ArrayList<String> ());
        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Campaign with ID " + campaignId + " added to store ... ");

        String[] rawNumbers = recipList.split (" ");
        for (int i = 0; i < rawNumbers.length; i++) {
            String recipient = rawNumbers[i];
            if (recipient == null) {
                recipient = "";
            }
            recipient = recipient.trim ();
            
            String digit_regex = "\\d+";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(digit_regex);
            java.util.regex.Matcher matcher = pattern.matcher(recipient.replace("+", ""));
            
            if (matcher.matches()) {
                System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Looking up network ID for " + recipient + "  ... ");
                networkId = subMngr.getNetworkIdFromMsisdn (recipient);
                System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Network ID for " + recipient + " is " + networkId);

                if (networkId != null && !networkId.equals ("")) {
                    recipient = normalizeNumber (recipient, recipUnifiedPrefix, numSignificantDigits);
                    System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Recipient normalized to " + recipient);

                    // Check to make sure subscriber isn't being silly by sending to himself
                    if (!msisdn.equalsIgnoreCase (recipient)) {
                        if (!campaignHolder.keySet ().contains (networkId)) { //new network, new campaign, new set of invitees
                            campaign = VMServiceManager.viewCampaignByNetwork (campaignId, networkId); // retrieve new campaign
                            campaignHolder.put (networkId, campaign);
                            ArrayList<String> invitees = new ArrayList<String> ();
                            //invitees.add (recipient);
                            campaignsAndInvitees.put (campaign, invitees);
                            System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Added campaign with ID " + campaign.getCampaignId ()
                                    + " to store ");

                            // Register use in vm_users table
                            VMServiceManager.createUser (msisdn, campaign.getAccountId (), campaign.getKeyword (), "", 0);
                        } else {//existing campaign, possibly with invitees -- add new invitee
                            //campaignsAndInvitees.get (campaignHolder.get (networkId)).add (recipient);
                            System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Campaign with ID " + campaign.getCampaignId ()
                                    + " already in store.");
                        }

                        if (VMServiceManager.viewTransaction (campaignId, recipient, msisdn).isEmptyTransaction ()) {
                            // All checks passed at this point
                            if (VMServiceManager.createTransaction (campaignId, msisdn, msisdnNetworkId, recipient, networkId, "inv_sent", "", "", "")) {
                                campaignsAndInvitees.get (campaignHolder.get (networkId)).add (recipient);
                                System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Adding recipient " + recipient + " to Campaign with ID "
                                        + campaign.getCampaignId ());

                                System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: VM transaction created for campaign ID " + campaign.getCampaignId ()
                                        + " and recipient " + recipient + " from recruiter " + msisdn);
                            } else {
                                System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: VM transaction could NOT be created for campaign ID " + campaign.getCampaignId ()
                                        + " and recipient " + recipient + " from recruiter " + msisdn);
                            }
                        } else {
                            System.out.println (new java.util.Date () + ": [vm-invite:WARNING]: VM transaction already exists for campaign ID " + campaign.getCampaignId ()
                                    + " and recipient " + recipient + " from recruiter " + msisdn);
                        }
                    }
                }
            } else {
                System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Excluding " +  recipient + " from recipients list from recruiter " + msisdn + "for campaign ID " 
                        + campaign.getCampaignId ());
            }
        }

        responsePkg.put ("ok_msg_to_recruiter", outgoingOkMsg);
        responsePkg.put ("error_msg_to_recruiter", outgoingErrorMsg);
        responsePkg.put ("vm_package", campaignsAndInvitees);

        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Finished processing VM invitation.");

        // Produce output
        /*if (campaignsAndInvitees.size () > 0) {
        //status = "OK";
        responsePkg.put ("msg_to_recruiter", outgoingOkMsg);
        responsePkg.put ("msg_to_recruiter", outgoingErrorMsg);
        
        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Finished processing VM invitation.");
        } else {
        //status = "NOK";
        responsePkg.put ("msg_to_recruiter", outgoingErrorMsg);
        
        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Finished processing VM invitation. VM invitation status is NOK");
        }*/

        
        //responsePkg.put ("process_status", status);

        //clean up
        campaign = null;
        campaignHolder.clear ();
        campaignHolder = null;

        return responsePkg;
    }

    public static HashMap processInvitations (String accountId, String keyword, String recipList, String recipUnifiedPrefix,
            String senderUnifiedPrefix, int numSignificantDigits, String msisdn, boolean useMsisdnAsSender, String usersName,
            SubscriberManager subMngr) throws Exception {

        //String status = "NOK";

        HashMap responsePkg = new HashMap ();
        HashMap<VMCampaign, ArrayList<String>> campaignsAndInvitees = new HashMap<VMCampaign, ArrayList<String>> ();//storage for campaigns and users to be invited
        HashMap<String, VMCampaign> campaignHolder = new HashMap<String, VMCampaign> ();//temporary storage for campaign  versions for various networks

        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Processing VM Invitation ... ");
        
        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Looking up network ID for " + msisdn + "  ... ");
        String networkId = subMngr.getNetworkIdFromMsisdn (msisdn);
            
        VMCampaign campaign = VMServiceManager.viewCampaignByServiceAndNetwork (accountId, keyword, networkId);
        String campaignId = campaign.getCampaignId ();
        String outgoingOkMsg = campaign.getFollowUpMsgSuccess ();
        String outgoingErrorMsg = campaign.getFollowUpMsgError ();
        String msisdnNetworkId = "";//campaign.getNetworkId ();
        System.out.println (new java.util.Date () + ": [vm-invite:INFO]: VM Campaign details: Account ID: " + campaign.getAccountId () + " :: Campaign ID: "
                + campaignId + " :: Outgoing OK Msg: " + outgoingOkMsg + " :: Outgoing Error Msg: " + outgoingErrorMsg
                + " :: Campaign Network ID: " + msisdnNetworkId);

        //campaignHolder.put (campaign.getNetworkId (), campaign);
        campaignsAndInvitees.put (campaign, new ArrayList<String> ());
        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Campaign with ID " + campaignId + " added to store ... ");

        String[] rawNumbers = recipList.split (" ");
        for (int i = 0; i < rawNumbers.length; i++) {
            String recipient = rawNumbers[i];
            if (recipient == null) {
                recipient = "";
            }
            recipient = recipient.trim ();
            
            System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Looking up network ID for " + recipient + "  ... ");
            networkId = subMngr.getNetworkIdFromMsisdn (recipient);
            System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Network ID for " + recipient + " is " + networkId);

            if (networkId != null && !networkId.equals ("")) {
                recipient = normalizeNumber (recipient, recipUnifiedPrefix, numSignificantDigits);
                System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Recipient normalized to " + recipient);

                // Check to make sure subscriber isn't being silly by sending to himself
                if (!msisdn.equalsIgnoreCase (recipient)) {
                    if (!campaignHolder.keySet ().contains (networkId)) { //new network, new campaign, new set of invitees
                        campaign = VMServiceManager.viewCampaignByNetwork (campaignId, networkId); // retrieve new campaign
                        campaignHolder.put (networkId, campaign);
                        ArrayList<String> invitees = new ArrayList<String> ();
                        //invitees.add (recipient);
                        campaignsAndInvitees.put (campaign, invitees);
                        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Added campaign with ID " + campaign.getCampaignId ()
                                + " to store ");

                        // Register use in vm_users table
                        VMServiceManager.createUser (msisdn, campaign.getAccountId (), campaign.getKeyword (), usersName, 0);
                    } else {//existing campaign, possibly with invitees -- add new invitee
                        //campaignsAndInvitees.get (campaignHolder.get (networkId)).add (recipient);
                        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Campaign with ID " + campaign.getCampaignId ()
                                + " already in store.");
                    }

                    if (VMServiceManager.viewTransaction (campaignId, recipient, msisdn).isEmptyTransaction ()) {
                        // All checks passed at this point
                        if (VMServiceManager.createTransaction (campaignId, msisdn, msisdnNetworkId, recipient, networkId, "inv_sent", "", "", "")) {
                            campaignsAndInvitees.get (campaignHolder.get (networkId)).add (recipient);
                            System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Adding recipient " + recipient + " to Campaign with ID "
                                    + campaign.getCampaignId ());

                            System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: VM transaction created for campaign ID " + campaign.getCampaignId ()
                                    + " and recipient " + recipient + " from recruiter " + msisdn);
                        } else {
                            System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: VM transaction could NOT be created for campaign ID " + campaign.getCampaignId ()
                                    + " and recipient " + recipient + " from recruiter " + msisdn);
                        }
                    } else {
                        System.out.println (new java.util.Date () + ": [vm-invite:WARNING]: VM transaction already exists for campaign ID " + campaign.getCampaignId ()
                                + " and recipient " + recipient + " from recruiter " + msisdn);
                    }
                }
            }
        }

        responsePkg.put ("ok_msg_to_recruiter", outgoingOkMsg);
        responsePkg.put ("error_msg_to_recruiter", outgoingErrorMsg);
        responsePkg.put ("vm_package", campaignsAndInvitees);

        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Finished processing VM invitation.");

        // Produce output
        /*if (campaignsAndInvitees.size () > 0) {
        //status = "OK";
        responsePkg.put ("msg_to_recruiter", outgoingOkMsg);
        responsePkg.put ("msg_to_recruiter", outgoingErrorMsg);
        
        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Finished processing VM invitation.");
        } else {
        //status = "NOK";
        responsePkg.put ("msg_to_recruiter", outgoingErrorMsg);
        
        System.out.println (new java.util.Date () + ": [vm-invite:DEBUG]: Finished processing VM invitation. VM invitation status is NOK");
        }*/

        
        //responsePkg.put ("process_status", status);

        //clean up
        campaign = null;
        campaignHolder.clear ();
        campaignHolder = null;

        return responsePkg;
    }

    private static String normalizeNumber (String msisdn, String unifiedPrefix, int numDigits) throws Exception {

        if (numDigits > msisdn.length ()) {
            throw new Exception ("Cannot normalize number. Invalid num_digits parameter passed");
        }

        return unifiedPrefix + msisdn.substring (msisdn.length () - numDigits, msisdn.length ());
    }
    /*public static HashMap sendInvitation (String campaignId, String recipList, String recipRegex,
    String recipUnifiedPrefix, String senderUnifiedPrefix, int numSignificantDigits,
    String msisdn, boolean useMsisdnAsSender, String accountId, SubscriberManager subMngr) throws Exception {
    
    String status = "NOK";
    HashMap responsePkg = new HashMap ();
    HashMap<VMCampaign, ArrayList<String>> campaignsAndInvitees = new HashMap<VMCampaign, ArrayList<String>> ();
    ArrayList<String> networkIdHolder = new ArrayList<String> ();//to remember networks already encountered
    VMCampaign campaign = null;
    
    String[] rawNumbers = recipList.split (" ");
    for (int i = 0; i < rawNumbers.length; i++) {
    String recipient = rawNumbers[i];
    String networkId = subMngr.getNetworkId (recipient);
    
    if (!networkIdHolder.contains (networkId)) { //new network, new campaign, new set of invitees
    networkIdHolder.add (networkId);
    campaign = VMServiceManager.viewCampaignByNetwork (campaignId, networkId); // retrieve new campaign
    ArrayList<String> invitees = new ArrayList<String> ();
    invitees.add (recipient);
    campaignsAndInvitees.put (campaign, invitees);
    } else {//existing
    
    }
    }
    
    VMCampaign campaign = VMServiceManager.viewCampaignByAccount (campaignId, accountId);
    String campaign_message = campaign.getMessage ();
    String campaign_keyword = campaign.getKeyword ();
    String campaign_account_id = campaign.getAccountId ();
    String campaign_message_sender = "";
    if (useMsisdnAsSender) {
    campaign_message_sender = normalizeNumber (msisdn, senderUnifiedPrefix, numSignificantDigits);
    } else {
    campaign_message_sender = campaign.getMessageSender ();
    }
    
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile (recipRegex);
    ArrayList<String> formattedNumbers = new ArrayList<String> ();
    
    // Register use in vm_users table
    VMServiceManager.createUser (msisdn, campaign_account_id, campaign_keyword, "", 0);
    
    String[] rawNumbers = recipList.split (" ");
    for (int i = 0; i < rawNumbers.length; i++) {
    String recipient = rawNumbers[i];
    java.util.regex.Matcher matcher = pattern.matcher (recipient);
    if (matcher.matches ()) {
    String formattedRecipient = normalizeNumber (recipient, recipUnifiedPrefix, numSignificantDigits);
    
    // Check to make sure subscriber isn't being silly by sending to himself
    if (!msisdn.equalsIgnoreCase (formattedRecipient)) {
    
    if (VMServiceManager.viewTransaction (campaignId, formattedRecipient).isEmptyTransaction ()) {
    // All checks passed at this point, so add recipient
    formattedNumbers.add (formattedRecipient);
    VMServiceManager.createTransaction (campaignId, msisdn, formattedRecipient, "inv_sent", "", "", "");
    }
    
    }
    
    } else {
    //Badly formatted number. Do nothing
    }
    }
    
    Iterator<String> subList = formattedNumbers.iterator ();
    
    // Produce output
    if (formattedNumbers.size () > 0) {
    status = "OK";
    } else {
    status = "NOK";
    }
    
    responsePkg.put ("recipients", subList);
    responsePkg.put ("campaign", campaign);
    responsePkg.put ("transactionStatus", status);
    
    return responsePkg;
    }*/
}
