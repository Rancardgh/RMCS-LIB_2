package com.rancard.mobility.infoserver.common.services;

import java.util.*;

public class UserServiceExperience {

    private String accountId;
    private String siteId;
    private String keyword;
    private String promoMsg;
    private String promoId;
    private String promoRespCode;
    private String welcomeMsg;
    private String alreadySubscribedMsg;
    private String unsubscriptionConfirmationMsg;
    private String promoMsgSender;
    private String welcomeMsgSender;
    private String alreadySubscribedMsgSender;
    private String unsubscriptionConfirmationMsgSender;
    private int pushMsgWaitTime;
    private int subscriptionInterval;
    private String url;
    private int urlTimeout;
    private ServiceExperienceMetaDataOptions metaData;
    public static final int DEFAULT_URL_CALL_TIMEOUT = 5000;

    public UserServiceExperience(String accountID, String siteID, String keyword, String promoMsg, String promoID, String promoRespCode,
            String welcomeMsg, String alreadySubscribedMsg, String unsubscriptionConfirmationMsg, String promoMsgSender,
            String welcomeMsgSender, String alreadySubscribedMsgSender, String unsubscriptionConfirmationMsgSender,
            int pushMsgWaitTime, int subscriptionInterval, String url, int urlTimeout, ServiceExperienceMetaDataOptions metaData) {
        this.accountId = accountID;
        this.siteId = siteID;
        this.keyword = keyword;
        this.promoMsg = promoMsg;
        this.promoId = promoID;
        this.promoRespCode = promoRespCode;
        this.welcomeMsg = welcomeMsg;
        this.alreadySubscribedMsg = alreadySubscribedMsg;
        this.unsubscriptionConfirmationMsg = unsubscriptionConfirmationMsg;
        this.promoMsgSender = promoMsgSender;
        this.welcomeMsgSender = welcomeMsgSender;
        this.alreadySubscribedMsgSender = alreadySubscribedMsgSender;
        this.unsubscriptionConfirmationMsgSender = unsubscriptionConfirmationMsgSender;
        this.pushMsgWaitTime = pushMsgWaitTime;
        this.subscriptionInterval = subscriptionInterval;
        this.url = url;
        this.urlTimeout = urlTimeout;
        this.metaData = metaData;
    }
    
   /* public UserServiceExperience(String accountID, String siteID, String keyword, String promoMsg, String promoID, String promoRespCode,
            String welcomeMsg, String alreadySubscribedMsg, String unsubscriptionConfirmationMsg, String promoMsgSender,
            String welcomeMsgSender, String alreadySubscribedMsgSender, String unsubscriptionConfirmationMsgSender,
            int pushMsgWaitTime, int subscriptionInterval, String url, int urlTimeout, ServiceExperienceMetaData metaData) {
        this.accountId = accountID;
        this.siteId = siteID;
        this.keyword = keyword;
        this.promoMsg = promoMsg;
        this.promoId = promoID;
        this.promoRespCode = promoRespCode;
        this.welcomeMsg = welcomeMsg;
        this.alreadySubscribedMsg = alreadySubscribedMsg;
        this.unsubscriptionConfirmationMsg = unsubscriptionConfirmationMsg;
        this.promoMsgSender = promoMsgSender;
        this.welcomeMsgSender = welcomeMsgSender;
        this.alreadySubscribedMsgSender = alreadySubscribedMsgSender;
        this.unsubscriptionConfirmationMsgSender = unsubscriptionConfirmationMsgSender;
        this.pushMsgWaitTime = pushMsgWaitTime;
        this.subscriptionInterval = subscriptionInterval;
        this.url = url;
        this.urlTimeout = urlTimeout;
        this.metaData = metaData;
    }
*/

    public String getAccountId() {
        return accountId;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getPromoMsg() {
        return promoMsg;
    }

    public String getPromoId() {
        return promoId;
    }

    public String getPromoRespCode() {
        return promoRespCode;
    }

    public String getWelcomeMsg() {
        return welcomeMsg;
    }

    public String getAlreadySubscribedMsg() {
        return alreadySubscribedMsg;
    }

    public String getUnsubscriptionConfirmationMsg() {
        return unsubscriptionConfirmationMsg;
    }

    public String getPromoMsgSender() {
        return promoMsgSender;
    }

    public String getWelcomeMsgSender() {
        return welcomeMsgSender;
    }

    public String getAlreadySubscribedMsgSender() {
        return alreadySubscribedMsgSender;
    }

    public String getUnsubscriptionConfirmationMsgSender() {
        return unsubscriptionConfirmationMsgSender;
    }

    public int getPushMsgWaitTime() {
        return pushMsgWaitTime;
    }

    public int getSubscriptionInterval() {
        return subscriptionInterval;
    }

    public String getUrl() {
        return url;
    }

    public int getUrlTimeout() {
        return urlTimeout;
    }

    public ServiceExperienceMetaDataOptions getMetaData() {
        return metaData;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setPromoMsg(String promoMsg) {
        this.promoMsg = promoMsg;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    public void setPromoRespCode(String promoRespCode) {
        this.promoRespCode = promoRespCode;
    }

    public void setWelcomeMsg(String welcomeMsg) {
        this.welcomeMsg = welcomeMsg;
    }

    public void setAlreadySubscribedMsg(String alreadySubscribedMsg) {
        this.alreadySubscribedMsg = alreadySubscribedMsg;
    }

    public void setUnsubscriptionConfirmationMsg(String unsubscriptionConfirmationMsg) {
        this.unsubscriptionConfirmationMsg = unsubscriptionConfirmationMsg;
    }

    public void setPromoMsgSender(String promoMsgSender) {
        this.promoMsgSender = promoMsgSender;
    }

    public void setWelcomeMsgSender(String welcomeMsgSender) {
        this.welcomeMsgSender = welcomeMsgSender;
    }

    public void setAlreadySubscribedMsgSender(String alreadySubscribedMsgSender) {
        this.alreadySubscribedMsgSender = alreadySubscribedMsgSender;
    }

    public void setUnsubscriptionConfirmationMsgSender(String unsubscriptionConfirmationMsgSender) {
        this.unsubscriptionConfirmationMsgSender = unsubscriptionConfirmationMsgSender;
    }

    public void setPushMsgWaitTime(int pushMsgWaitTime) {
        this.pushMsgWaitTime = pushMsgWaitTime;
    }

    public void setSubscriptionInterval(int subscriptionInterval) {
        this.subscriptionInterval = subscriptionInterval;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlTimeout(int urlTimeout) {
        this.urlTimeout = urlTimeout;
    }

    public void setMetaData(ServiceExperienceMetaDataOptions metaData) {
        this.metaData = metaData;
    }


    /*private Map<String, String> getMetaDataMap(String metaData) {
        Map<String, String> map = new HashMap<String, String>();
        
        if (metaData == null || metaData.equals("")) {
            return map;
        }

        List<String> mDataPairs = new ArrayList();
        mDataPairs.addAll(Arrays.asList(metaData.split("&")));
        for (String pair : mDataPairs) {
            String[] parts = pair.split("=");
            map.put(parts[0].toUpperCase(), parts[1]);
        }

        return map;
    }*/
}
