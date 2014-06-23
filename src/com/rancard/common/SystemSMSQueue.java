package com.rancard.common;

import com.rancard.common.db.DConnect;
import com.rancard.common.key.SystemSMSQueueKey;
import com.rancard.util.DateUtil;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

/**
 * Created by Mustee on 4/8/2014.
 */
@Entity
@Table(name = "system_sms_queue", schema = "", catalog = "rmcs")
public class SystemSMSQueue {

    @EmbeddedId
    private SystemSMSQueueKey systemSMSQueueKey;

    @Column(name = "publish_time", nullable = false)
    private Date publishTime;

    @Lob
    private String message;

    @Column(name = "msg_hash")
    private Integer messageHash;

    @Column(name = "owner_id", length = 100)
    private String ownerID;

    @Column(name = "image_url", length = 255)
    private String imageURL;

    @Column(name = "article_title", length = 100)
    private String articleTitle;

    @Lob
    @Column(length = 100000)
    private byte[] tags;

    @Column(name = "msg_ref")
    private String messageRef;

    protected SystemSMSQueue() {
    }

    public SystemSMSQueue(SystemSMSQueueKey systemSMSQueueKey, Date publishTime, String message, Integer messageHash, String ownerID, String imageURL, String articleTitle, byte[] tags, String messageRef) {
        this.systemSMSQueueKey = systemSMSQueueKey;
        this.publishTime = publishTime;
        this.message = message;
        this.messageHash = messageHash;
        this.ownerID = ownerID;
        this.imageURL = imageURL;
        this.articleTitle = articleTitle;
        this.tags = tags;
        this.messageRef = messageRef;
    }

    public SystemSMSQueueKey getSystemSMSQueueKey() {
        return systemSMSQueueKey;
    }

    public void setSystemSMSQueueKey(SystemSMSQueueKey systemSMSQueueKey) {
        this.systemSMSQueueKey = systemSMSQueueKey;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMessageHash() {
        return messageHash;
    }

    public void setMessageHash(Integer messageHash) {
        this.messageHash = messageHash;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public byte[] getTags() {
        return tags;
    }

    public void setTags(byte[] tags) {
        this.tags = tags;
    }

    public String getMessageRef() {
        return messageRef;
    }

    public void setMessageRef(String messageRef) {
        this.messageRef = messageRef;
    }

    public static SystemSMSQueue find(EntityManager em, Date publishDate, String accountID, String keyword, Integer messageID) {
        Query query = em.createQuery("SELECT ssq FROM SystemSMSQueue ssq WHERE systemSMSQueueKey.publishDate = '" + DateUtil.formatToShort(publishDate) + "' " +
                "AND systemSMSQueueKey.accountID = :accountID AND systemSMSQueueKey.keyword = :keyword AND systemSMSQueueKey.messageID = :messageID");
        query.setParameter("accountID", accountID).setParameter("keyword", keyword).setParameter("messageID", messageID);
        List<SystemSMSQueue> queues = query.getResultList();

        return (queues.size() == 0) ? null : queues.get(0);
    }

    public static SystemSMSQueue find(EntityManager em, String accountID, String keyword) {
        Query query = em.createQuery("SELECT ssq FROM SystemSMSQueue ssq WHERE systemSMSQueueKey.accountID = :accountID AND systemSMSQueueKey.keyword = :keyword " +
                "ORDER BY systemSMSQueueKey.publishDate desc, systemSMSQueueKey.messageID desc");
        query.setParameter("accountID", accountID).setParameter("keyword", keyword);
        List<SystemSMSQueue> queues = query.getResultList();

        return (queues.size() == 0) ? null : queues.get(0);
    }

    public static SystemSMSQueue find(EntityManager em, String accountID, String keyword, Integer messageID) {
        Query query = em.createQuery("SELECT ssq FROM SystemSMSQueue ssq WHERE " +
                "systemSMSQueueKey.accountID = :accountID AND systemSMSQueueKey.keyword = :keyword AND systemSMSQueueKey.messageID = :messageID");
        query.setParameter("accountID", accountID).setParameter("keyword", keyword).setParameter("messageID", messageID);
        List<SystemSMSQueue> queues = query.getResultList();

        return (queues.size() == 0) ? null : queues.get(0);
    }

}
