package com.rancard.common;

import com.rancard.common.key.ServiceDefinitionKey;
import com.rancard.mobility.contentserver.EMF;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mustee on 3/29/2014.
 */
@Entity
@Table(name = "service_definition", schema = "", catalog = "rmcs")
@NamedQueries({
        @NamedQuery(name = "ServiceDefinition.findAll", query = "SELECT s FROM ServiceDefinition s")
})
public class ServiceDefinition implements Serializable {

    @EmbeddedId
    private ServiceDefinitionKey serviceDefinitionKey;

    @Column(length = 10, nullable = true, name = "service_type")
    private String serviceType;

    @Column(length = 255, nullable = true, name = "service_name")
    private String serviceName;

    @Column(nullable = false, name = "is_basic")
    private boolean isBasic;

    @Lob
    @Column(name = "default_message", nullable = false)
    private String defaultMessage;

    @Column(name = "last_updated", nullable = false)
    private Date lastUpdated;

    @Column(name = "allowed_site_types", length = 255, nullable = false)
    private String allowedSiteTypes;

    @Column(name = "allowed_shortcodes", nullable = false, length = 255)
    private String allowedShortcodes;

    @Column(nullable = false, length = 255)
    private String pricing;

    @Column(nullable = false, length = 100)
    private String command;

    @Column(nullable = false, name = "is_subscription")
    private boolean isSubscription;

    @Column(nullable = false, length = 255, name = "service_response_sender")
    private String serviceResponseSender;

    @Lob
    private String tags;

    protected ServiceDefinition() {
    }

    public ServiceDefinition(ServiceDefinitionKey serviceDefinitionKey, String serviceType, String serviceName, boolean isBasic, String defaultMessage, Date lastUpdated, String allowedSiteTypes, String allowedShortcodes, String pricing, String command, boolean isSubscription, String serviceResponseSender, String tags) {
        this.serviceDefinitionKey = serviceDefinitionKey;
        this.serviceType = serviceType;
        this.serviceName = serviceName;
        this.isBasic = isBasic;
        this.defaultMessage = defaultMessage;
        this.lastUpdated = lastUpdated;
        this.allowedSiteTypes = allowedSiteTypes;
        this.allowedShortcodes = allowedShortcodes;
        this.pricing = pricing;
        this.command = command;
        this.isSubscription = isSubscription;
        this.serviceResponseSender = serviceResponseSender;
        this.tags = tags;
    }

    public ServiceDefinitionKey getServiceDefinitionKey() {
        return serviceDefinitionKey;
    }

    public void setServiceDefinitionKey(ServiceDefinitionKey serviceDefinitionKey) {
        this.serviceDefinitionKey = serviceDefinitionKey;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isBasic() {
        return isBasic;
    }

    public void setBasic(boolean isBasic) {
        this.isBasic = isBasic;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getAllowedSiteTypes() {
        return allowedSiteTypes;
    }

    public void setAllowedSiteTypes(String allowedSiteTypes) {
        this.allowedSiteTypes = allowedSiteTypes;
    }

    public String getAllowedShortcodes() {
        return allowedShortcodes;
    }

    public void setAllowedShortcodes(String allowedShortcodes) {
        this.allowedShortcodes = allowedShortcodes;
    }

    public String getPricing() {
        return pricing;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isSubscription() {
        return isSubscription;
    }

    public void setSubscription(boolean isSubscription) {
        this.isSubscription = isSubscription;
    }

    public String getServiceResponseSender() {
        return serviceResponseSender;
    }

    public void setServiceResponseSender(String serviceResponseSender) {
        this.serviceResponseSender = serviceResponseSender;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public static List<ServiceDefinition> getAll(EntityManager em) {
        List<ServiceDefinition> services = em.createNamedQuery("ServiceDefinition.findAll", ServiceDefinition.class).getResultList();

        return services;
    }

    public static List<ServiceDefinition> findBySMSC(EntityManager em , String smsc) {
        List<ServiceDefinition> services = em.createQuery("SELECT sd FROM ServiceDefinition as sd, CPConnection as cp " +
                "WHERE sd.serviceDefinitionKey.accountID = cp.cpConnectionKey.listID AND cp.cpConnectionKey.connID LIKE '%" + smsc + "%'").getResultList();

        return services;
    }

    public static List<ServiceDefinition> findBySMSCAndShortCode(EntityManager em, String smsc, String shortCode) {
        List<ServiceDefinition> services = em.createQuery("SELECT sd FROM ServiceDefinition as sd, CPConnection as cp " +
                "WHERE sd.serviceDefinitionKey.accountID = cp.cpConnectionKey.listID " +
                "AND sd.serviceResponseSender = '" + shortCode + "' AND cp.cpConnectionKey.connID LIKE '%" + Utils.getBaseSMSC(smsc) + "%'").getResultList();

        return services;
    }

}
