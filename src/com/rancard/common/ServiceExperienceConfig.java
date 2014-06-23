package com.rancard.common;

import com.rancard.common.key.ServiceExperienceConfigKey;

import javax.persistence.*;

/**
 * Created by Mustee on 3/31/2014.
 */
@Entity
@Table(name = "service_experience_config", schema = "", catalog = "rmcs")
public class ServiceExperienceConfig {

    @EmbeddedId
    private ServiceExperienceConfigKey serviceExperienceConfigKey;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_id", referencedColumnName = "promo_id")
    private PromotionalCampaign promotionalCampaign;

    @Column(name = "welcome_msg", length = 255)
    private String welcomeMessage;

    @Column(name = "already_subscribed_msg", length = 255)
    private String alreadySubscribedMessage;

    @Column(name = "unsubscription_conf_msg", length = 255)
    private String unsubscriptionMessage;

    @Column(name = "promo_msg_sender", length = 11)
    private String promoMessageSender;

    @Column(name = "welcome_msg_sender", length = 11)
    private String welcomeMessageSender;

    @Column(name = "already_subscribed_msg_sender", length = 11)
    private String alreadySubscribedMessageSender;

    @Column(name = "unsubscription_conf_msg_sender", length = 11)
    private String unsubscriptionMessageSender;

    @Column(name = "push_msg_wait_time")
    private Integer pushMessageWaitTime;

    @Column(name = "subscription_interval")
    private Integer subscriptionInterval;

    @Column(name = "url", length = 1536)
    private String url;

    @Column(name = "url_timeout")
    private String urlTimeout;

    @Column(name = "meta_data", length = 1536)
    private String metaData;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "keyword", referencedColumnName = "keyword", nullable = false, insertable = false, updatable = false)
    })
    private ServiceDefinition serviceDefinition;

    protected ServiceExperienceConfig() {
    }

    public ServiceExperienceConfig(ServiceExperienceConfigKey serviceExperienceConfigKey, PromotionalCampaign promotionalCampaign, String welcomeMessage, String alreadySubscribedMessage, String unsubscriptionMessage, String promoMessageSender, String welcomeMessageSender, String alreadySubscribedMessageSender, String unsubscriptionMessageSender, Integer pushMessageWaitTime, Integer subscriptionInterval, String url, String urlTimeout, String metaData) {
        this.serviceExperienceConfigKey = serviceExperienceConfigKey;
        this.promotionalCampaign = promotionalCampaign;
        this.welcomeMessage = welcomeMessage;
        this.alreadySubscribedMessage = alreadySubscribedMessage;
        this.unsubscriptionMessage = unsubscriptionMessage;
        this.promoMessageSender = promoMessageSender;
        this.welcomeMessageSender = welcomeMessageSender;
        this.alreadySubscribedMessageSender = alreadySubscribedMessageSender;
        this.unsubscriptionMessageSender = unsubscriptionMessageSender;
        this.pushMessageWaitTime = pushMessageWaitTime;
        this.subscriptionInterval = subscriptionInterval;
        this.url = url;
        this.urlTimeout = urlTimeout;
        this.metaData = metaData;
    }

    public ServiceExperienceConfigKey getServiceExperienceConfigKey() {
        return serviceExperienceConfigKey;
    }

    public void setServiceExperienceConfigKey(ServiceExperienceConfigKey serviceExperienceConfigKey) {
        this.serviceExperienceConfigKey = serviceExperienceConfigKey;
    }

    public PromotionalCampaign getPromotionalCampaign() {
        return promotionalCampaign;
    }

    public void setPromotionalCampaign(PromotionalCampaign promotionalCampaign) {
        this.promotionalCampaign = promotionalCampaign;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getAlreadySubscribedMessage() {
        return alreadySubscribedMessage;
    }

    public void setAlreadySubscribedMessage(String alreadySubscribedMessage) {
        this.alreadySubscribedMessage = alreadySubscribedMessage;
    }

    public String getUnsubscriptionMessage() {
        return unsubscriptionMessage;
    }

    public void setUnsubscriptionMessage(String unsubscriptionMessage) {
        this.unsubscriptionMessage = unsubscriptionMessage;
    }

    public String getPromoMessageSender() {
        return promoMessageSender;
    }

    public void setPromoMessageSender(String promoMessageSender) {
        this.promoMessageSender = promoMessageSender;
    }

    public String getWelcomeMessageSender() {
        return welcomeMessageSender;
    }

    public void setWelcomeMessageSender(String welcomeMessageSender) {
        this.welcomeMessageSender = welcomeMessageSender;
    }

    public String getAlreadySubscribedMessageSender() {
        return alreadySubscribedMessageSender;
    }

    public void setAlreadySubscribedMessageSender(String alreadySubscribedMessageSender) {
        this.alreadySubscribedMessageSender = alreadySubscribedMessageSender;
    }

    public String getUnsubscriptionMessageSender() {
        return unsubscriptionMessageSender;
    }

    public void setUnsubscriptionMessageSender(String unsubscriptionMessageSender) {
        this.unsubscriptionMessageSender = unsubscriptionMessageSender;
    }

    public Integer getPushMessageWaitTime() {
        return pushMessageWaitTime;
    }

    public void setPushMessageWaitTime(Integer pushMessageWaitTime) {
        this.pushMessageWaitTime = pushMessageWaitTime;
    }

    public Integer getSubscriptionInterval() {
        return subscriptionInterval;
    }

    public void setSubscriptionInterval(Integer subscriptionInterval) {
        this.subscriptionInterval = subscriptionInterval;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlTimeout() {
        return urlTimeout;
    }

    public void setUrlTimeout(String urlTimeout) {
        this.urlTimeout = urlTimeout;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    public void setServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }
}
