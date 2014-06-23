package com.rancard.common;

import com.rancard.common.key.ServiceSubscriptionKey;
import com.rancard.mobility.contentserver.EMF;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Mustee on 3/29/2014.
 */
@Entity
@Table(name = "service_subscription", schema = "", catalog = "rmcs")
@SecondaryTable(name = "service_subscription_ext", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "account_id"),
        @PrimaryKeyJoinColumn(name = "keyword", referencedColumnName = "keyword"), @PrimaryKeyJoinColumn(name = "msisdn", referencedColumnName = "msisdn")})
@NamedQueries({
        @NamedQuery(name = "ServiceSubscription.findAll", query = "SELECT s FROM ServiceSubscription s")
})
public class ServiceSubscription {

    @EmbeddedId
    private ServiceSubscriptionKey serviceSubscriptionKey;

    @Column(name = "subscription_date", nullable = false)
    private Date subscriptionDate;

    @Column(name = "next_subscription_date")
    private String nextSubscriptionDate;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false, name = "billing_type")
    private Integer billingType;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = true, name = "channel", length = 100, table = "service_subscription_ext")
    private Channel channel;

    @Lob
    @Column(nullable = true, name = "meta_data", table = "service_subscription_ext")
    private String metaData;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "keyword", referencedColumnName = "keyword", nullable = false, insertable = false, updatable = false)
    })
    private ServiceDefinition serviceDefinition;

    protected ServiceSubscription() {
    }

    public ServiceSubscription(ServiceSubscriptionKey serviceSubscriptionKey, Date subscriptionDate, String nextSubscriptionDate, Integer status, Integer billingType, Channel channel, String metaData) {
        this.serviceSubscriptionKey = serviceSubscriptionKey;
        this.subscriptionDate = subscriptionDate;
        this.nextSubscriptionDate = nextSubscriptionDate;
        this.status = status;
        this.billingType = billingType;
        this.channel = channel;
        this.metaData = metaData;
    }

    public ServiceSubscriptionKey getServiceSubscriptionKey() {
        return serviceSubscriptionKey;
    }

    public void setServiceSubscriptionKey(ServiceSubscriptionKey serviceSubscriptionKey) {
        this.serviceSubscriptionKey = serviceSubscriptionKey;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public String getNextSubscriptionDate() {
        return nextSubscriptionDate;
    }

    public void setNextSubscriptionDate(String nextSubscriptionDate) {
        this.nextSubscriptionDate = nextSubscriptionDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBillingType() {
        return billingType;
    }

    public void setBillingType(Integer billingType) {
        this.billingType = billingType;
    }

    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    public void setServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public static void createSubscription(EntityManager em, ServiceSubscription subscription){
        em.getTransaction().begin();
        em.persist(subscription);
        em.getTransaction().commit();
    }
    public static List<ServiceSubscription> getAll(EntityManager em) {
        return em.createNamedQuery("ServiceSubscription.findAll", ServiceSubscription.class).getResultList();
    }

    public static List<ServiceSubscription> getAll(EntityManager em, int limit) {
        return em.createNamedQuery("ServiceSubscription.findAll", ServiceSubscription.class).setMaxResults(limit).getResultList();
    }

    public static ServiceSubscription getLastSubscription(EntityManager em, String msisdn) {
        Query query = em.createQuery("SELECT ss FROM ServiceSubscription ss WHERE ss.serviceSubscriptionKey.msisdn = :msisdn ORDER BY ss.subscriptionDate desc ");
        query.setParameter("msisdn", msisdn);
        List<ServiceSubscription> subscriptions = (List<ServiceSubscription>)query.getResultList();

        return (subscriptions.size() == 0) ? null : subscriptions.get(0);
    }

    public static void deleteSubscription(EntityManager em, ServiceSubscriptionKey key) {
        em.getTransaction().begin();
        em.remove(em.getReference(ServiceSubscription.class, key));
        em.getTransaction().commit();

    }

    public static List<ServiceSubscription> getSubscriptions(EntityManager em, String smsc, String shortCode, String msisdn) {
        List<ServiceDefinition> services = ServiceDefinition.findBySMSCAndShortCode(em, smsc, shortCode);

        Set<String> accountIDs = new HashSet<String>();
        for (ServiceDefinition service : services) {
            accountIDs.add(service.getServiceDefinitionKey().getAccountID());
        }

        Query query = em.createQuery("SELECT ss FROM ServiceSubscription ss WHERE ss.serviceSubscriptionKey.accountID IN :services AND ss.serviceSubscriptionKey.msisdn = :msisdn");
        query.setParameter("services", accountIDs).setParameter("msisdn", msisdn);

        List<ServiceSubscription> subscriptions = query.getResultList();
        return subscriptions;
    }


    public static void deleteSubscriptions(EntityManager em, String smsc, String shortCode, String msisdn) {
        List<ServiceSubscription> subscriptions = ServiceSubscription.getSubscriptions(em, smsc, shortCode, msisdn);

        em.getTransaction().begin();
        for (ServiceSubscription subs : subscriptions) {
            em.remove(subs);
        }

        em.getTransaction().commit();


    }

}
