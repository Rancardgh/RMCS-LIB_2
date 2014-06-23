package com.rancard.common;

import com.rancard.common.key.VMTransactionKey;
import com.rancard.mobility.contentserver.EMF;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Mustee on 4/5/2014.
 */
@Entity
@Table(name = "vm_transactions", schema = "", catalog = "rmcs")
public class VMTransaction {

    @EmbeddedId
    private VMTransactionKey vmTransactionKey;

    @Column(name = "trans_date", nullable = false)
    private Date transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private VMTransactionStatus status;

    @Column(name = "item_id", length = 50)
    private String itemID;

    @Column(name = "category", nullable = true, length = 50)
    private String category;

    @Column(name = "short_url", nullable = true, length = 160)
    private String shortURL;

    protected VMTransaction() {
    }

    public VMTransaction(VMTransactionKey vmTransactionKey, Date transactionDate, VMTransactionStatus status, String itemID, String category, String shortURL) {
        this.vmTransactionKey = vmTransactionKey;
        this.transactionDate = transactionDate;
        this.status = status;
        this.itemID = itemID;
        this.category = category;
        this.shortURL = shortURL;
    }

    public VMTransactionKey getVmTransactionKey() {
        return vmTransactionKey;
    }

    public void setVmTransactionKey(VMTransactionKey vmTransactionKey) {
        this.vmTransactionKey = vmTransactionKey;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public VMTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(VMTransactionStatus status) {
        this.status = status;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public static void createVMTransaction(EntityManager em , VMTransaction vmTransaction) {
        em.getTransaction().begin();
        em.persist(vmTransaction);
        em.getTransaction().commit();

    }

    public static boolean vmTransactionsExists(EntityManager em, String campaignID, Set<String> recipientMSISDNs) {
        for (String recipient : recipientMSISDNs) {
            if (getVMTransaction(em, campaignID, recipient) == null) {
                return false;
            }
        }
        return true;
    }

    public static VMTransaction getVMTransaction(EntityManager em, String campaignID, String recipientMSISDN) {
        Query query = em.createQuery("SELECT vmt FROM VMTransaction vmt WHERE vmt.vmTransactionKey.campaignID = :campaignID AND vmt.vmTransactionKey.recipientMSISDN = :recipientMSISDN");
        query.setParameter("campaignID", campaignID).setParameter("recipientMSISDN", recipientMSISDN);

        List<VMTransaction> vmTransactions = query.getResultList();

        return (vmTransactions.size() == 0) ? null : vmTransactions.get(0);
    }
}
