package com.rancard.common.key;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mustee on 4/8/2014.
 */
@Embeddable
public class SystemSMSQueueKey implements Serializable {

    @Column(name = "publish_date", nullable = false)
    private Date publishDate;

    @Column(name = "keyword", length = 32)
    private String keyword;

    @Column(name = "account_id", nullable = false)
    private String accountID;

    @Column(name = "msg_id", nullable = false)
    private Integer messageID;

    protected SystemSMSQueueKey() {
    }

    public SystemSMSQueueKey(Date publishDate, String keyword, String accountID, Integer messageID) {
        this.publishDate = publishDate;
        this.keyword = keyword;
        this.accountID = accountID;
        this.messageID = messageID;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public Integer getMessageID() {
        return messageID;
    }

    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
