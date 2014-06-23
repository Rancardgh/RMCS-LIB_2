package com.rancard.common;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Mustee on 4/11/2014.
 */
@Entity
@Table(name = "transactions", schema = "", catalog = "rmcs")
public class Transaction {

    @Id
    @Column(name = "trans_id", length = 100, nullable = false)
    private String transactionID;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "keyword", referencedColumnName = "keyword", nullable = false, insertable = false, updatable = false)
    })
    private ServiceDefinition serviceDefinition;

    @Column(name = "msisdn", length = 100, nullable = false)
    private String msisdn;

    @Column(nullable = false)
    private Date date;

    @Column(name = "msg", nullable = false)
    private String message;

    @Column(name = "callback_url", nullable = false)
    private String callbackURL;

    @Column(name = "is_billed", nullable = false)
    private Boolean isBilled;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "price_point_id", referencedColumnName = "price_point_id")
    private PricePoint pricePoint;

    protected Transaction() {
    }

    public Transaction(String transactionID, ServiceDefinition serviceDefinition, String msisdn, Date date, String message, String callbackURL, Boolean isBilled, Boolean isCompleted, PricePoint pricePoint) {
        this.transactionID = transactionID;
        this.serviceDefinition = serviceDefinition;
        this.msisdn = msisdn;
        this.date = date;
        this.message = message;
        this.callbackURL = callbackURL;
        this.isBilled = isBilled;
        this.isCompleted = isCompleted;
        this.pricePoint = pricePoint;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    public void setServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

    public Boolean getIsBilled() {
        return isBilled;
    }

    public void setIsBilled(Boolean isBilled) {
        this.isBilled = isBilled;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public PricePoint getPricePoint() {
        return pricePoint;
    }

    public void setPricePoint(PricePoint pricePoint) {
        this.pricePoint = pricePoint;
    }

    public static void createTransaction(EntityManager em, Transaction transaction){
        em.getTransaction().begin();
        em.persist(transaction);
        em.getTransaction().commit();
    }
}
