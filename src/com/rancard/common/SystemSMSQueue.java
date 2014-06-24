package com.rancard.common;

import com.rancard.common.db.DConnect;
import com.rancard.util.DateUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Mustee on 4/8/2014.
 */
public class SystemSMSQueue {
    private static Logger logger = Logger.getLogger(SystemSMSQueue.class.getName());
    private Date publishDate;
    private String keyword;
    private String accountID;
    private Integer messageID;
    private Date publishTime;
    private String message;
    private Integer messageHash;
    private String ownerID;
    private String imageURL;
    private String articleTitle;
    private byte[] tags;
    private String messageRef;

    public SystemSMSQueue(Date publishDate, String keyword, String accountID, Integer messageID, Date publishTime, String message, Integer messageHash, String ownerID, String imageURL, String articleTitle, byte[] tags, String messageRef) {
        this.publishDate = publishDate;
        this.keyword = keyword;
        this.accountID = accountID;
        this.messageID = messageID;
        this.publishTime = publishTime;
        this.message = message;
        this.messageHash = messageHash;
        this.ownerID = ownerID;
        this.imageURL = imageURL;
        this.articleTitle = articleTitle;
        this.tags = tags;
        this.messageRef = messageRef;
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

    public static SystemSMSQueue find(Date publishDate, String accountID, String keyword, Integer messageID) throws Exception {
        final String query = "SELECT * FROM system_sms_queue ssq WHERE publishDate = '" + DateUtil.formatToShort(publishDate) + "' " +
                "account_id = '"+accountID+"' AND keyword = '"+keyword+"' AND msg_id ='"+messageID+"'";

        Connection conn = null;
        ResultSet rs = null;

        try {
            logger.info("Getting last subscription: "+query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            if(rs.next()){
                return new SystemSMSQueue(rs.getDate("publish_date"), rs.getString("keyword"), rs.getString("account_id"), rs.getInt("msg_id"), rs.getDate("publish_time"),
                        rs.getString("message"), rs.getInt("msg_hash"), rs.getString("owner_id"), rs.getString("image_url"), rs.getString("article_title"), rs.getBytes("tags"),
                        rs.getString("msg_ref"));
            }

            return null;
        } catch (Exception e) {
            logger.severe("Problem getting last subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static SystemSMSQueue find(String accountID, String keyword) throws Exception {
        final String query = "SELECT * FROM system_sms_queue ssq WHERE account_id = '"+accountID+"' AND keyword = '"+keyword+"' ORDER BY publish_date desc, msg_id desc";
        Connection conn = null;
        ResultSet rs = null;

        try {
            logger.info("Getting last subscription: "+query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            if(rs.next()){
                return new SystemSMSQueue(rs.getDate("publish_date"), rs.getString("keyword"), rs.getString("account_id"), rs.getInt("msg_id"), rs.getDate("publish_time"),
                        rs.getString("message"), rs.getInt("msg_hash"), rs.getString("owner_id"), rs.getString("image_url"), rs.getString("article_title"), rs.getBytes("tags"),
                        rs.getString("msg_ref"));
            }

            return null;
        } catch (Exception e) {
            logger.severe("Problem getting last subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static SystemSMSQueue find(String accountID, String keyword, Integer messageID) throws Exception {
        final String query = "SELECT * FROM system_sms_queue ssq WHERE account_id = '"+accountID+"' AND keyword = '"+keyword+"' AND msg_id ='"+messageID+"'";

        Connection conn = null;
        ResultSet rs = null;

        try {
            logger.info("Getting last subscription: "+query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            if(rs.next()){
                return new SystemSMSQueue(rs.getDate("publish_date"), rs.getString("keyword"), rs.getString("account_id"), rs.getInt("msg_id"), rs.getDate("publish_time"),
                        rs.getString("message"), rs.getInt("msg_hash"), rs.getString("owner_id"), rs.getString("image_url"), rs.getString("article_title"), rs.getBytes("tags"),
                        rs.getString("msg_ref"));
            }

            return null;
        } catch (Exception e) {
            logger.severe("Problem getting last subscription: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

}
