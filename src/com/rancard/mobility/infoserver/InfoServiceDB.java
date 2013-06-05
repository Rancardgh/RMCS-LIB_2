package com.rancard.mobility.infoserver;

import com.rancard.common.DConnect;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.util.URLUTF8Encoder;
import java.io.*;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class InfoServiceDB extends com.rancard.mobility.infoserver.common.services.UserServiceDB {

    //constructor
    public InfoServiceDB () {
    }

    //insert record
    public void createInfoServiceEntry (Date date, String keyword, String message, String accountId, String ownerId, String imageURL, String articleTitle) throws Exception {
        // check if keyword is registered.
        com.rancard.mobility.infoserver.common.services.UserService serv = viewService (keyword, accountId);
        int test = 0;
        if (serv.getAccountId () != null && serv.getAccountId ().equals (accountId) && serv.getKeyword () != null && serv.getKeyword ().equals (keyword)) {
            test = 1;
        } else {
            test = 0;
        }
        // if keyword is registered create entry for keyword for particular date
        if (test == 1) {
            // serv.exists();
            String SQL, SQL_label;
            ResultSet rs = null;
            ResultSet rs_label = null;
            Connection con = null;
            PreparedStatement prepstat = null;
            PreparedStatement prepstat_label = null;

            try {
                con = DConnect.getConnection ();
                java.sql.Date sqlDate_date = new java.sql.Date (date.getTime ());

                int test_exists = 0;

                // first test if record exists
                SQL = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? order by msg_id desc";

                prepstat = con.prepareStatement (SQL);
                prepstat.setDate (1, sqlDate_date);
                prepstat.setString (2, keyword);
                prepstat.setString (3, accountId);

                rs = prepstat.executeQuery ();
                int lastMsgId = 0;
                while (rs.next ()) {
                    test_exists = 1;
                    lastMsgId = rs.getInt ("msg_id");
                    break;
                }

                // retrieve header and footer information from service_labels
                SQL_label = "select * from service_labels where account_id = ? and keyword = ?";
                prepstat_label = con.prepareStatement (SQL_label);
                prepstat_label.setString (1, accountId);
                prepstat_label.setString (2, keyword);

                rs_label = prepstat_label.executeQuery ();
                String header = "";
                String footer = "";
                while (rs_label.next ()) {
                    header = rs_label.getString ("header");
                    footer = rs_label.getString ("footer");
                }

                // If footer exists, enter new line character before adding footer
                String final_message = header + message + ((footer.equals ("")) ? "" : "\n" + footer);

                java.util.Date currentDate = new java.util.Date ();
                if (test_exists == 1) {

                    addInfoService (final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
                    // uppdate service entry for particular day
                    //updateInfoService(date, keyword, message, accountId);

                    // update default message on service definition via inherited method from user service
                    if (!date.after (currentDate)) {
                        updateService (final_message, keyword, accountId);
                    }

                } else if (test_exists == 0) {
                    // create new message in table
                    addInfoService (final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
                    // poor code this could be optimized to go into a transaction
                    // update default message on service definition via inherited method from user service
                    if (!date.after (currentDate)) {
                        updateService (final_message, keyword, accountId);
                    }
                } else {
                    throw new Exception ("unable to determine insert basis for service item: " + keyword);
                }

            } catch (Exception ex) {
                if (con != null) {
                    con.close ();
                }
                throw new Exception (ex.getMessage ());
            } finally {
                if (prepstat != null) {
                    prepstat.close ();
                }
                if (prepstat_label != null) {
                    prepstat_label.close ();
                }
                if (rs != null) {
                    rs.close ();
                }
                if (rs_label != null) {
                    rs_label.close ();
                }
                if (con != null) {
                    con.close ();
                }
            }
        } else {
            System.out.println ("Cannot create news item. Service does not exist");
        }
    }


    /* This version of createInfoServiceEntry does not add the message to system_sms_queue
     * if the message meets any of the following criteria:
     * 1. content lenth exceeds number of characters specified in argument maxContentLenth.
     * 2. duplicate message exists with the same publish_date, account_id, keyword and msg_hash
     *
     * While it might seems simpler to do a character count on the message string before
     * passing it here, the motivation for this version of the function is to take into account
     * the service label information and reject the entry if the resulting length is too long
     *
     * returns 1 if final content length exceeds maxContentLength.
     * returns 2 if duplicate item already exists with same publish_date, account_id, keyword and msg_hash
     */
    public int createInfoServiceEntry (java.util.Date date, java.lang.String keyword, java.lang.String message, java.lang.String accountId, String ownerId, String imageURL, String articleTitle, int maxContentLength) throws Exception {
        // check if keyword is registered.
        com.rancard.mobility.infoserver.common.services.UserService serv = viewService (keyword, accountId);
        int test = 0;
        if (serv.getAccountId () != null && serv.getAccountId ().equals (accountId) && serv.getKeyword () != null && serv.getKeyword ().equals (keyword)) {
            test = 1;
        } else {
            test = 0;
        }
        // if keyword is registered create entry for keyword for particular date
        if (test == 1) {
            // serv.exists();
            String SQL, SQL_label, SQL_duplicateTest;
            ResultSet rs = null;
            ResultSet rs_label = null;
            ResultSet rs_duplicateTest = null;
            Connection con = null;
            PreparedStatement prepstat = null;
            PreparedStatement prepstat_label = null;
            PreparedStatement prepstat_duplicateTest = null;

            try {
                con = DConnect.getConnection ();
                java.sql.Date sqlDate_date = new java.sql.Date (date.getTime ());

                int test_exists = 0;

                // first test if record exists
                SQL = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? order by msg_id desc";

                prepstat = con.prepareStatement (SQL);
                prepstat.setDate (1, sqlDate_date);
                prepstat.setString (2, keyword);
                prepstat.setString (3, accountId);

                rs = prepstat.executeQuery ();
                int lastMsgId = 0;
                while (rs.next ()) {
                    test_exists = 1;
                    lastMsgId = rs.getInt ("msg_id");
                    break;
                }

                // retrieve header and footer information from service_labels
                SQL_label = "select * from service_labels where account_id = ? and keyword = ?";
                prepstat_label = con.prepareStatement (SQL_label);
                prepstat_label.setString (1, accountId);
                prepstat_label.setString (2, keyword);

                rs_label = prepstat_label.executeQuery ();
                String header = "";
                String footer = "";
                while (rs_label.next ()) {
                    header = rs_label.getString ("header");
                    footer = rs_label.getString ("footer");
                }

                // If footer exists, enter new line character before adding footer -- only of content + footer is <= maxContentLength
                String final_message = header + message;
                if (new String (final_message + ((footer.equals ("")) ? "" : "\n" + footer)).length () <= maxContentLength) {
                    final_message = final_message + ((footer.equals ("")) ? "" : "\n" + footer);
                } else {
                    System.out.println ("Ignoring footer: \"" + footer + "\" from actual message. Stored message: " + final_message);
                }

                // Test length of final message
                if (final_message.length () > maxContentLength) {
                    return 1;
                }

                // test if duplicate message exists
                SQL_duplicateTest = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? and msg_hash = ?";

                prepstat_duplicateTest = con.prepareStatement (SQL_duplicateTest);
                prepstat_duplicateTest.setDate (1, sqlDate_date);
                prepstat_duplicateTest.setString (2, keyword);
                prepstat_duplicateTest.setString (3, accountId);
                prepstat_duplicateTest.setInt (4, final_message.hashCode ());

                rs_duplicateTest = prepstat_duplicateTest.executeQuery ();
                int duplicate_exists = 0;

                while (rs_duplicateTest.next ()) {
                    duplicate_exists = 1;
                    break;
                }

                if (duplicate_exists == 1) {
                    return 2;
                }

                java.util.Date currentDate = new java.util.Date ();
                if (test_exists == 1) {

                    addInfoService (final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
                    // uppdate service entry for particular day
                    //updateInfoService(date, keyword, message, accountId);

                    // update default message on service definition via inherited method from user service
                    if (!date.after (currentDate)) {
                        updateService (final_message, keyword, accountId);
                    }

                } else if (test_exists == 0) {
                    // create new message in table
                    addInfoService (final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle);
                    // poor code this could be optimized to go into a transaction
                    // update default message on service definition via inherited method from user service
                    if (!date.after (currentDate)) {
                        updateService (final_message, keyword, accountId);
                    }
                } else {
                    throw new Exception ("unable to determine insert basis for service item: " + keyword);
                }

            } catch (Exception ex) {
                if (con != null) {
                    con.close ();
                }
                throw new Exception (ex.getMessage ());
            } finally {
                if (prepstat != null) {
                    prepstat.close ();
                }
                if (prepstat_label != null) {
                    prepstat_label.close ();
                }
                if (rs != null) {
                    rs.close ();
                }
                if (rs_label != null) {
                    rs_label.close ();
                }
                if (con != null) {
                    con.close ();
                }
            }
        } else {
            System.out.println ("Cannot create news item. Service does not exist");
        }

        return 0;
    }

    public int createInfoServiceEntryWithTags (java.util.Date date, java.lang.String keyword, java.lang.String message, java.lang.String accountId, String ownerId, String imageURL, String articleTitle,
            String contentURL,String author, String tags, String messageRef, int maxContentLength) throws Exception {
        // check if keyword is registered.
        com.rancard.mobility.infoserver.common.services.UserService serv = viewService (keyword, accountId);
        int test = 0;
        if (serv.getAccountId () != null && serv.getAccountId ().equals (accountId) && serv.getKeyword () != null && serv.getKeyword ().equals (keyword)) {
            test = 1;
        } else {
            test = 0;
        }
        // if keyword is registered create entry for keyword for particular date
        if (test == 1) {
            // serv.exists();
            String SQL, SQL_label, SQL_duplicateTest;
            ResultSet rs = null;
            ResultSet rs_label = null;
            ResultSet rs_duplicateTest = null;
            Connection con = null;
            PreparedStatement prepstat = null;
            PreparedStatement prepstat_label = null;
            PreparedStatement prepstat_duplicateTest = null;

            try {
                con = DConnect.getConnection ();
                java.sql.Date sqlDate_date = new java.sql.Date (date.getTime ());

                int test_exists = 0;

                // first test if record exists
                SQL = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? order by msg_id desc";

                prepstat = con.prepareStatement (SQL);
                prepstat.setDate (1, sqlDate_date);
                prepstat.setString (2, keyword);
                prepstat.setString (3, accountId);

                rs = prepstat.executeQuery ();
                int lastMsgId = 0;
                while (rs.next ()) {
                    test_exists = 1;
                    lastMsgId = rs.getInt ("msg_id");
                    break;
                }

                // retrieve header and footer information from service_labels
                SQL_label = "select * from service_labels where account_id = ? and keyword = ?";
                prepstat_label = con.prepareStatement (SQL_label);
                prepstat_label.setString (1, accountId);
                prepstat_label.setString (2, keyword);

                rs_label = prepstat_label.executeQuery ();
                String header = "";
                String footer = "";
                while (rs_label.next ()) {
                    header = rs_label.getString ("header");
                    footer = rs_label.getString ("footer");
                }

                // If footer exists, enter new line character before adding footer -- only of content + footer is <= maxContentLength
                String final_message = header + message;
                if (new String (final_message + ((footer.equals ("")) ? "" : "\n" + footer)).length () <= maxContentLength) {
                    final_message = final_message + ((footer.equals ("")) ? "" : "\n" + footer);
                } else {
                    System.out.println ("Ignoring footer: \"" + footer + "\" from actual message. Stored message: " + final_message);
                }

                // Test length of final message
                if (final_message.length () > maxContentLength) {
                    return 1;
                }

                // test if duplicate message exists
                SQL_duplicateTest = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? and msg_hash = ?";

                prepstat_duplicateTest = con.prepareStatement (SQL_duplicateTest);
                prepstat_duplicateTest.setDate (1, sqlDate_date);
                prepstat_duplicateTest.setString (2, keyword);
                prepstat_duplicateTest.setString (3, accountId);
                prepstat_duplicateTest.setInt (4, final_message.hashCode ());

                rs_duplicateTest = prepstat_duplicateTest.executeQuery ();
                int duplicate_exists = 0;

                while (rs_duplicateTest.next ()) {
                    duplicate_exists = 1;
                    break;
                }

                if (duplicate_exists == 1) {
                    return 2;
                }

                java.util.Date currentDate = new java.util.Date ();

                if (test_exists == 1) {
//String message, String keyword, String accountId, int msg_id, java.sql.Date sqlDate,String ownerId, String imageURL, String articleTitle, String contentURL, String author,String tags, String messageRef
                    
                    addInfoServiceWithTags (final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle,contentURL, author,tags, messageRef);
                    // uppdate service entry for particular day
                    //updateInfoService(date, keyword, message, accountId);

                    // update default message on service definition via inherited method from user service
                    if (!date.after (currentDate)) {
                        updateService (final_message, keyword, accountId);
                    }

                } else if (test_exists == 0) {
                    // create new message in table
                    addInfoServiceWithTags (final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle,contentURL, author,tags, messageRef);
                    // poor code this could be optimized to go into a transaction
                    // update default message on service definition via inherited method from user service
                    if (!date.after (currentDate)) {
                        updateService (final_message, keyword, accountId);
                    }
                } else {
                    throw new Exception ("unable to determine insert basis for service item: " + keyword);
                }

            } catch (Exception ex) {
                if (con != null) {
                    con.close ();
                }
                throw new Exception (ex.getMessage ());
            } finally {
                if (prepstat != null) {
                    prepstat.close ();
                }
                if (prepstat_label != null) {
                    prepstat_label.close ();
                }
                if (rs != null) {
                    rs.close ();
                }
                if (rs_label != null) {
                    rs_label.close ();
                }
                if (con != null) {
                    con.close ();
                }
            }
        } else {
            System.out.println ("Cannot create news item. Service does not exist");
        }

        return 0;
    }

    /*
    public void createInfoServiceEntries(java.util.ArrayList<InfoService> infoServArray) throws Exception {
    // check if keyword is registered.
    com.rancard.mobility.infoserver.common.services.UserService serv =  viewService(keyword,accountId);
    int test = 0;
    if (serv.getAccountId() != null && serv.getAccountId().equals(accountId) && serv.getKeyword() != null && serv.getKeyword().equals(keyword)) {
    test = 1;
    } else {
    test  = 0;
    }
    // if keyword is registered create entry for keyword for particular date
    if (test == 1) {
    // serv.exists();
    String SQL;
    ResultSet rs=null;
    Connection con=null;
    PreparedStatement prepstat=null;
    
    try {
    con=DConnect.getConnection();
    java.sql.Date sqlDate_date= new java.sql.Date(date.getTime());
    
    int test_exists = 0;
    
    // first test if record exists
    SQL = "select * from system_sms_queue where publish_date = ? and keyword = ?  and account_id= ? order by msg_id desc";
    
    prepstat=con.prepareStatement(SQL);
    prepstat.setDate(1,sqlDate_date);
    prepstat.setString(2,keyword);
    prepstat.setString(3,accountId);
    
    rs=prepstat.executeQuery();
    int lastMsgId = 0;
    while (rs.next()) {
    test_exists = 1;
    lastMsgId = rs.getInt("msg_id");
    break;
    }
    
    java.util.Date currentDate = new java.util.Date();
    if ( test_exists == 1 ) {
    
    addInfoService(message, keyword, accountId, lastMsgId+1, sqlDate_date);
    // uppdate service entry for particular day
    //updateInfoService(date, keyword, message, accountId);
    
    // update default message on service definition via inherited method from user service
    if (!date.after(currentDate))
    updateService(message,keyword,accountId);
    
    } else if  ( test_exists == 0 ) {
    // create new message in table
    addInfoService(message, keyword, accountId, lastMsgId+1, sqlDate_date);
    // poor code this could be optimized to go into a transaction
    // update default message on service definition via inherited method from user service
    if (!date.after(currentDate))
    updateService(message,keyword,accountId);
    } else {
    throw new Exception("unable to determine insert basis for service item: " + keyword);
    }
    
    }catch (Exception ex){
    if(con !=null)
    con.close();
    throw new Exception(ex.getMessage());
    }
    if(con !=null)
    con.close();
    } else {
    System.out.println("Cannot create news item. Service does not exist");
    }
    }
     */
    //String message, String keyword, String accountId, int msg_id, java.sql.Date sqlDate,String ownerId, String imageURL, String articleTitle, String contentURL, String author,String tags, String messageRef
    public void addInfoService (String message, String keyword, String accountId, int msg_id, java.sql.Date sqlDate, String ownerId, String imageURL, String articleTitle) throws Exception {
        String SQL = "insert into system_sms_queue (publish_date,Publish_time ,keyword,message,account_id,msg_id,msg_hash, owner_id, image_url, article_title) values";
        SQL = SQL + "(?,?, ?, ?, ?, ?,?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            prepstat = prepstat = con.prepareStatement (SQL);

            prepstat.setDate (1, sqlDate);
            prepstat.setTimestamp (2, new java.sql.Timestamp (new java.util.Date ().getTime ()));
            prepstat.setString (3, keyword);
            prepstat.setString (4, message);
            prepstat.setString (5, accountId);
            prepstat.setInt (6, msg_id);
            prepstat.setInt (7, message.hashCode ());
            prepstat.setString (8, ownerId);
            prepstat.setString (9, imageURL);
            prepstat.setString (10, articleTitle);
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }

        //cleanup counter db vars
        if (prepstat != null) {
            prepstat.close ();
        }
        if (con != null) {
            con.close ();
        }

    }
//final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle,contentURL, author,tags, messageRef
    //final_message, keyword, accountId, lastMsgId + 1, sqlDate_date, ownerId, imageURL, articleTitle,contentURL, author,tags, messageRef
    public void addInfoServiceWithTags (String message, String keyword, String accountId, int msg_id, java.sql.Date sqlDate,
            String ownerId, String imageURL, String articleTitle, String contentURL, String author,String tags, String messageRef) throws Exception {

        //commented out until two new columns (author, content_url) are added to DB
        /*String SQL = "insert into system_sms_queue (publish_date,Publish_time ,keyword,message,account_id,msg_id,msg_hash, owner_id, image_url, article_title,tags,msg_ref,author,content_url)"
                + " values (?,?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?,?,?)"; */
        String SQL = "insert into system_sms_queue (publish_date,Publish_time ,keyword,message,account_id,msg_id,msg_hash, owner_id, image_url, article_title,tags,msg_ref)"
                + " values (?,?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            prepstat = prepstat = con.prepareStatement (SQL);

            prepstat.setDate (1, sqlDate);
            prepstat.setTimestamp (2, new java.sql.Timestamp (new java.util.Date ().getTime ()));
            prepstat.setString (3, keyword);
            prepstat.setString (4, message);
            prepstat.setString (5, accountId);
            prepstat.setInt (6, msg_id);
            prepstat.setInt (7, message.hashCode ());
            prepstat.setString (8, ownerId);
            prepstat.setString (9, imageURL);
            prepstat.setString (10, articleTitle);
            prepstat.setString (11, tags);
            prepstat.setString (12, messageRef);
            //prepstat.setString(13, author); //commented out until column (author) is added to DB
            //prepstat.setString(14, contentURL); //commented out until column (content_url) is added to DB
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }

        //cleanup counter db vars
        if (prepstat != null) {
            prepstat.close ();
        }
        if (con != null) {
            con.close ();
        }

    }

    public void updateInfoService (java.util.Date date,
            java.lang.String keyword, java.lang.String message, java.lang.String accountId, int msg_id, String imageURL, String articleTitle) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            java.sql.Date sqlDate_date = new java.sql.Date (date.getTime ());

            SQL = "update system_sms_queue set Publish_time= ?, message =?, msg_hash=?, image_url=?, article_title=? where keyword =? and publish_date=? and  account_id= ? and msg_id = ?";

            prepstat = con.prepareStatement (SQL);
            prepstat.setTimestamp (1, new java.sql.Timestamp (new java.util.Date ().getTime ()));
            prepstat.setString (2, message);
            prepstat.setInt (3, message.hashCode ());
            prepstat.setString (4, imageURL);
            prepstat.setString (5, articleTitle);
            prepstat.setString (6, keyword);
            prepstat.setDate (7, sqlDate_date);
            prepstat.setString (8, accountId);
            prepstat.setInt (9, msg_id);
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (prepstat != null) {
            prepstat.close ();
        }
        if (rs != null) {
            rs.close ();
        }
        if (con != null) {
            con.close ();
        }
    }

    public void updateInfoService (java.util.Date date,
            java.lang.String keyword, java.lang.String message, java.lang.String accountId, int msg_id, String imageURL, String articleTitle, String author, String contentURL) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            java.sql.Date sqlDate_date = new java.sql.Date (date.getTime ());

            //---------------Uncomment this portion when author and content_url columns are added to DB----------------
            //SQL = "update system_sms_queue set Publish_time= ?, message =?, msg_hash=?, image_url=?, article_title=?, content_url=?, author=? where keyword =? and publish_date=? and  account_id= ? and msg_id = ?";
            //-------------------------------------------------------------------------------------------------------------------------------------
            
            //---------------Remove this portion when author and content_url columns are added to DB ---------------------
            SQL = "update system_sms_queue set Publish_time= ?, message =?, msg_hash=?, image_url=?, article_title=? where keyword =? and publish_date=? and  account_id= ? and msg_id = ?";
            //-------------------------------------------------------------------------------------------------------------------------------------
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setTimestamp (1, new java.sql.Timestamp (new java.util.Date ().getTime ()));
            prepstat.setString (2, message);
            prepstat.setInt (3, message.hashCode ());
            prepstat.setString (4, imageURL);
            prepstat.setString (5, articleTitle);
            
            //---------------Uncomment this portion when author and content_url columns are added to DB----------------
            /*prepstat.setString (6, contentURL);
            prepstat.setString (7, author);
            prepstat.setString (8, keyword);
            prepstat.setDate (9, sqlDate_date);
            prepstat.setString (10, accountId);
            prepstat.setInt (11, msg_id);*/
            //-------------------------------------------------------------------------------------------------------------------------------------
            
            //---------------Remove this portion when author and content_url columns are added to DB ---------------------
            prepstat.setString (6, keyword);
            prepstat.setDate (7, sqlDate_date);
            prepstat.setString (8, accountId);
            prepstat.setInt (9, msg_id);
            //-------------------------------------------------------------------------------------------------------------------------------------
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (prepstat != null) {
            prepstat.close ();
        }
        if (rs != null) {
            rs.close ();
        }
        if (con != null) {
            con.close ();
        }
    }

    public void deleteInfoService (java.lang.Integer id) throws Exception {

        String SQL;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            SQL = "delete from system_sms_queue where id=?";
            prepstat = con.prepareStatement (SQL);

            prepstat.setInt (1, id.intValue ());
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (prepstat != null) {
            prepstat.close ();
        }
        if (con != null) {
            con.close ();
        }
    }

    public void deleteInfoService (String keyword, String accountId) throws Exception {

        String SQL;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            SQL = "delete from system_sms_queue where keyword=? and account_id=?";
            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, keyword);
            prepstat.setString (2, accountId);
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (prepstat != null) {
            prepstat.close ();
        }
        if (con != null) {
            con.close ();
        }
    }

    public void deleteInfoService (java.util.Date date, java.lang.String keyword, java.lang.String accountId, int msg_id) throws Exception {
        // IMPORTANT NOTE: All records occuring after deleted record MUST have their
        // msg_id fields decremented by 1, else this will create holes in the databases
        // and cause potential problems!!!
        String SQL;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            java.sql.Date sqlDate_date = new java.sql.Date (date.getTime ());

            SQL = "delete from system_sms_queue where  keyword =? and publish_date=? and  account_id= ? and msg_id = ?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, keyword);
            prepstat.setDate (2, sqlDate_date);
            prepstat.setString (3, accountId);
            prepstat.setInt (4, msg_id);
            prepstat.execute ();

        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (prepstat != null) {
            prepstat.close ();
        }
        if (con != null) {
            con.close ();
        }
    }

    /*
     * Retrieves a message from the message queue by account and keyword. if a message reference is available, it will be used
     * in the query. otherwise the most recent item is fetched.
     */
    public static InfoService retrieveOutboundMessage (UserService service, java.util.Date date, String messageRef) throws Exception {
        InfoService outboundMessage = new InfoService ();

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        java.sql.Date sqlDate = new java.sql.Date (date.getTime ());
        //------log Statement
        System.out.println (new java.util.Date () + ":@com.rancard.mobility.infoserver.InfoServiceDB..");
        System.out.println (new java.util.Date () + ": retrieving outbound message from message queue...");


        try {

            if (messageRef != null && !messageRef.equals ("")) {

                SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl "
                        + "on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword "
                        + "where ssq.keyword=? and ssq.account_id = ? and publish_date =? and msg_ref=? "
                        + "order by publish_time desc, msg_id desc limit 10";

                con = DConnect.getConnection ();
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, service.getKeyword ());
                prepstat.setString (2, service.getAccountId ());
                prepstat.setDate (3, sqlDate);
                prepstat.setString (4, messageRef);
            } else {

                SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl "
                        + "on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword "
                        + "where ssq.keyword=? and ssq.account_id = ? and publish_date =? "
                        + "order by publish_time desc, msg_id desc limit 10";

                con = DConnect.getConnection ();
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, service.getKeyword ());
                prepstat.setString (2, service.getAccountId ());
                prepstat.setDate (3, sqlDate);
            }
            
            rs = prepstat.executeQuery ();

            if (rs.next ()) {
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat ("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format (new java.util.Date (rs.getTimestamp ("Publish_time").getTime ()));

                outboundMessage.setPublishTime (publishTime);
                outboundMessage.setKeyword (rs.getString ("keyword"));
                
                Clob clob = rs.getClob ("message");
                String tmpMsg = "";
                String temp = "";

                BufferedReader br = new BufferedReader (clob.getCharacterStream ());
                while ((temp = br.readLine ()) != null) {
                    tmpMsg = tmpMsg + temp;
                }
                
                tmpMsg = URLUTF8Encoder.removeMalformedChars (tmpMsg);
                System.out.println ("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
                outboundMessage.setMessage (tmpMsg);
                
                outboundMessage.setAccountId (rs.getString ("account_id"));
                outboundMessage.setMsgId (rs.getInt ("msg_id"));
                outboundMessage.setPublishDate (new java.text.SimpleDateFormat ("dd-MMM-yyyy").format ((java.util.Date) rs.getDate ("publish_date")));
                outboundMessage.setOwnerId (rs.getString ("owner_id"));
                outboundMessage.setImageURL (rs.getString ("image_url"));
                outboundMessage.setArticleTitle (rs.getString ("article_title"));
                outboundMessage.setTags (rs.getString ("tags"));
                outboundMessage.setMessageRef (rs.getString ("msg_ref"));

                String header = rs.getString ("header");
                String footer = rs.getString ("footer");
                outboundMessage.setHeader ((header == null) ? "" : header);
                outboundMessage.setFooter ((footer == null) ? "" : footer);

                outboundMessage.setDefaultMessage (service.getDefaultMessage ());
                outboundMessage.setServiceName (service.getServiceName ());
                outboundMessage.setServiceType (service.getServiceType ());
                outboundMessage.setPricing (service.getPricing ());
                outboundMessage.setLastUpdated (service.getLastUpdated ());
                outboundMessage.setAllowedShortcodes (service.getAllowedShortcodes ());
                outboundMessage.setAllowedSiteTypes (service.getAllowedSiteTypes ());
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }

            System.out.println (new java.util.Date () + ": error retrieving info for " + service.getKeyword () + "/" + date.toString () + "/"
                    + service.getAccountId () + " combination: " + ex.getMessage ());

            throw new Exception (ex.getMessage ());
        } finally {
            if (prepstat != null) {
                prepstat.close ();
            }
            if (rs != null) {
                rs.close ();
            }
            if (con != null) {
                con.close ();
            }
        }
        return outboundMessage;
    }

    public InfoService viewInfoService (String keyword, String accountId, java.util.Date date) throws Exception {
        InfoService system_sms_queue = new InfoService ();
        java.util.ArrayList<InfoService> recordList = new java.util.ArrayList ();

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        java.sql.Date sqlDate = new java.sql.Date (date.getTime ());
        //------log Statement
        System.out.println (new java.util.Date () + ":@com.rancard.mobility.infoserver.InfoServiceDB..");
        System.out.println (new java.util.Date () + ":viewing info service...");


        try {
            UserService service = com.rancard.mobility.infoserver.common.services.ServiceManager.viewService (keyword, accountId);
            system_sms_queue.setAccountId (service.getAccountId ());
            system_sms_queue.setKeyword (service.getKeyword ());
            system_sms_queue.setDefaultMessage (service.getDefaultMessage ());
            system_sms_queue.setServiceName (service.getServiceName ());
            system_sms_queue.setServiceType (service.getServiceType ());
            system_sms_queue.setPricing (service.getPricing ());
            system_sms_queue.setLastUpdated (service.getLastUpdated ());
            system_sms_queue.setAllowedShortcodes (service.getAllowedShortcodes ());
            system_sms_queue.setAllowedSiteTypes (service.getAllowedSiteTypes ());

            SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl "
                    + "on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword "
                    + "where ssq.keyword=? and ssq.account_id = ? and publish_date =? "
                    + "order by publish_time desc, msg_id limit 10";

            con = DConnect.getConnection ();
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, keyword);
            prepstat.setString (2, accountId);
            prepstat.setDate (3, sqlDate);
            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat ("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format (new java.util.Date (rs.getTimestamp ("Publish_time").getTime ()));
                system_sms_queue.setPublishTime (publishTime);
                system_sms_queue.setKeyword (rs.getString ("keyword"));
                
                Clob clob = rs.getClob ("message");
                String tmpMsg = "";
                String temp = "";

                BufferedReader br = new BufferedReader (clob.getCharacterStream ());
                while ((temp = br.readLine ()) != null) {
                    tmpMsg = tmpMsg + temp;
                }
                
                tmpMsg = URLUTF8Encoder.removeMalformedChars (tmpMsg);
                
                System.out.println ("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
                system_sms_queue.setMessage (tmpMsg);
                
                system_sms_queue.setAccountId (rs.getString ("account_id"));
                system_sms_queue.setMsgId (rs.getInt ("msg_id"));
                system_sms_queue.setPublishDate (new java.text.SimpleDateFormat ("dd-MMM-yyyy").format ((java.util.Date) rs.getDate ("publish_date")));
                system_sms_queue.setOwnerId (rs.getString ("owner_id"));
                system_sms_queue.setImageURL (rs.getString ("image_url"));
                system_sms_queue.setArticleTitle (rs.getString ("article_title"));

                String header = rs.getString ("header");
                String footer = rs.getString ("footer");
                system_sms_queue.setHeader ((header == null) ? "" : header);
                system_sms_queue.setFooter ((footer == null) ? "" : footer);

                system_sms_queue.setDefaultMessage (service.getDefaultMessage ());
                system_sms_queue.setServiceName (service.getServiceName ());
                system_sms_queue.setServiceType (service.getServiceType ());
                system_sms_queue.setPricing (service.getPricing ());
                system_sms_queue.setLastUpdated (service.getLastUpdated ());
                system_sms_queue.setAllowedShortcodes (service.getAllowedShortcodes ());
                system_sms_queue.setAllowedSiteTypes (service.getAllowedSiteTypes ());

                recordList.add (system_sms_queue);
                system_sms_queue = new InfoService ();
            }

            //get random message from retrieved messages
            int recordCount = recordList.size ();
            System.out.println (new java.util.Date () + ":" + recordCount + " records retrieved from db for " + keyword + "/" + date.toString () + "/" + accountId + " combination");
            int msg_id = 0;
            int msg_base = 0;
            if (recordCount > 0) {
                msg_base = recordList.get (0).getMsgId ();
                msg_id = selectMessage (recordCount, msg_base);
                System.out.println (new java.util.Date () + ":" + msg_id + " selected as random msg_id");
            }

            if (!recordList.isEmpty ()) {
                system_sms_queue = recordList.get (msg_id - msg_base);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }

            System.out.println (new java.util.Date () + ": error retrieving info for " + keyword + "/" + date.toString () + "/" + accountId + " combination: " + ex.getMessage ());

            throw new Exception (ex.getMessage ());
        } finally {
            if (prepstat != null) {
                prepstat.close ();
            }
            if (rs != null) {
                rs.close ();
            }
            if (con != null) {
                con.close ();
            }
        }
        return system_sms_queue;
    }

    public static InfoService viewInfoService (String keyword, String accountId, java.util.Date date, int msg_id) throws Exception {
        InfoService system_sms_queue = new InfoService ();

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        java.sql.Date sqlDate = new java.sql.Date (date.getTime ());

        try {
            UserService service = com.rancard.mobility.infoserver.common.services.ServiceManager.viewService (keyword, accountId);

            system_sms_queue.setAccountId (service.getAccountId ());
            system_sms_queue.setKeyword (service.getKeyword ());
            system_sms_queue.setDefaultMessage (service.getDefaultMessage ());
            system_sms_queue.setServiceName (service.getServiceName ());
            system_sms_queue.setServiceType (service.getServiceType ());
            system_sms_queue.setPricing (service.getPricing ());
            system_sms_queue.setLastUpdated (service.getLastUpdated ());
            system_sms_queue.setAllowedShortcodes (service.getAllowedShortcodes ());
            system_sms_queue.setAllowedSiteTypes (service.getAllowedSiteTypes ());

            con = DConnect.getConnection ();

            SQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl "
                    + "on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword "
                    + "where ssq.keyword=? and ssq.account_id = ? and publish_date =? "
                    + "and msg_id = ?";
            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, keyword);
            prepstat.setString (2, accountId);
            prepstat.setDate (3, sqlDate);
            prepstat.setInt (4, msg_id);
            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat ("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format (new java.util.Date (rs.getTimestamp ("Publish_time").getTime ()));
                system_sms_queue.setPublishTime (publishTime);
                system_sms_queue.setKeyword (rs.getString ("keyword"));
                
                Clob clob = rs.getClob ("message");
                String tmpMsg = "";
                String temp = "";

                BufferedReader br = new BufferedReader (clob.getCharacterStream ());
                while ((temp = br.readLine ()) != null) {
                    tmpMsg = tmpMsg + temp;
                }
                
                tmpMsg = URLUTF8Encoder.removeMalformedChars (tmpMsg);
                System.out.println ("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
                system_sms_queue.setMessage (tmpMsg);
                
                system_sms_queue.setAccountId (rs.getString ("account_id"));
                system_sms_queue.setMsgId (rs.getInt ("msg_id"));
                system_sms_queue.setPublishDate (new java.text.SimpleDateFormat ("dd-MMM-yyyy").format ((java.util.Date) rs.getDate ("publish_date")));
                system_sms_queue.setOwnerId (rs.getString ("owner_id"));
                system_sms_queue.setImageURL (rs.getString ("image_url"));
                system_sms_queue.setArticleTitle (rs.getString ("article_title"));

                String header = rs.getString ("header");
                String footer = rs.getString ("footer");
                system_sms_queue.setHeader ((header == null) ? "" : header);
                system_sms_queue.setFooter ((footer == null) ? "" : footer);
            }

        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (prepstat != null) {
            prepstat.close ();
        }
        if (rs != null) {
            rs.close ();
        }
        if (con != null) {
            con.close ();
        }

        return system_sms_queue;
    }

    /*public InfoService viewInfoService(String keyword,String accountId, java.util.Date date) throws Exception {
    //for backward compatibility in handling default case
    int recordCount = getRecordCount(keyword, accountId, new java.sql.Date(date.getTime()));
    int msg_id = selectMessage(recordCount);
    return viewInfoService(keyword, accountId, date, msg_id);
    }*/
    /*private static int getRecordCount(String keyword, String accountId, java.sql.Date date) throws Exception{
    String counterSQL = "SELECT count(*) as counter FROM system_sms_queue WHERE keyword = ? and account_id = ? and publish_date = ? group by keyword";
    Connection con =DConnect.getConnection();
    PreparedStatement counterStat = con.prepareStatement(counterSQL);
    counterStat.setString(1,keyword);
    counterStat.setString(2,accountId);
    counterStat.setDate(3,date);
    ResultSet counterRS = counterStat.executeQuery();
    int recordCount = 0;
    while (counterRS.next()) {
    recordCount = counterRS.getInt("counter");
    }
    return recordCount;
    }*/
    private int selectMessage (int n, int startRange) {
        java.util.Random r = new java.util.Random ();
        r.setSeed (new java.util.Date ().getTime ());
        int ret = r.nextInt ((n > 0) ? n : 1) + startRange;
        return ret;
    }

    /*   public java.util.ArrayList<InfoService> viewInfoServices(String keyword, String accountId, java.util.Date date) throws Exception {

    java.util.ArrayList<InfoService> list = new java.util.ArrayList();
    //get count of records existing for keyword on specific date for a given account_id
    String counterSQL = "SELECT count(1) as counter FROM system_sms_queue WHERE keyword = ? and account_id = ? and publish_date = ? group by keyword";
    Connection con = DConnect.getConnection();
    PreparedStatement counterStat = con.prepareStatement(counterSQL);
    counterStat.setString(1,keyword);
    counterStat.setString(2,accountId);
    counterStat.setDate(3, new java.sql.Date(date.getTime()));
    ResultSet counterRS = counterStat.executeQuery();
    int recordCount = 0;
    while (counterRS.next()) {
    recordCount = counterRS.getInt("counter");
    }

    if (recordCount > 0) {
    for (int i = recordCount; i > ((recordCount-10 > 0) ? recordCount - 10 : 0); i--) {
    list.add(viewInfoService(keyword, accountId, date, i));
    }
    }

    return list;
    }*/
    public java.util.ArrayList<InfoService> viewInfoServices (String keyword, String accountId, java.util.Date date) throws Exception {
        InfoService system_sms_queue = null;

        java.util.ArrayList<InfoService> list = new java.util.ArrayList ();
        //get count of records existing for keyword on specific date for a given account_id
        String counterSQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl "
                + "on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword "
                + "where ssq.keyword=? and ssq.account_id = ? and publish_date =? "
                + "order by msg_id desc";
        Connection con = DConnect.getConnection ();
        PreparedStatement counterStat = con.prepareStatement (counterSQL);
        counterStat.setString (1, keyword);
        counterStat.setString (2, accountId);
        counterStat.setDate (3, new java.sql.Date (date.getTime ()));
        ResultSet rs = counterStat.executeQuery ();

        while (rs.next ()) {
            system_sms_queue = new InfoService ();
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat ("dd-MMM-yyyy HH.mm.ss");
            String publishTime = df.format (new java.util.Date (rs.getTimestamp ("Publish_time").getTime ()));
            system_sms_queue.setPublishTime (publishTime);
            system_sms_queue.setKeyword (rs.getString ("keyword"));
            
            Clob clob = rs.getClob ("message");
            String tmpMsg = "";
            String temp = "";

            BufferedReader br = new BufferedReader (clob.getCharacterStream ());
            while ((temp = br.readLine ()) != null) {
                tmpMsg = tmpMsg + temp;
            }
                
            tmpMsg = URLUTF8Encoder.removeMalformedChars (tmpMsg);
            System.out.println ("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
            system_sms_queue.setMessage (tmpMsg);
            
            system_sms_queue.setAccountId (rs.getString ("account_id"));
            system_sms_queue.setMsgId (rs.getInt ("msg_id"));
            system_sms_queue.setOwnerId (rs.getString ("owner_id"));
            system_sms_queue.setPublishDate (new java.text.SimpleDateFormat ("dd-MMM-yyyy").format ((java.util.Date) rs.getDate ("publish_date")));
            system_sms_queue.setImageURL (rs.getString ("image_url"));
            system_sms_queue.setArticleTitle (rs.getString ("article_title"));

            String header = rs.getString ("header");
            String footer = rs.getString ("footer");
            system_sms_queue.setHeader ((header == null) ? "" : header);
            system_sms_queue.setFooter ((footer == null) ? "" : footer);

            //add to list
            list.add (system_sms_queue);
        }

        return list;
    }

    public java.util.HashMap<String, String> viewHeaderFooter (String keyword, String accountId) throws Exception {
        java.util.HashMap<String, String> hf_map = new java.util.HashMap ();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        String header = "";
        String footer = "";

        try {
            con = DConnect.getConnection ();
            SQL = "select * from service_labels  where keyword = ?  and account_id= ?";

            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, keyword);
            prepstat.setString (2, accountId);

            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                header = rs.getString ("header");
                footer = rs.getString ("footer");
            }

        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        } finally {
            if (prepstat != null) {
                prepstat.close ();
            }
            if (rs != null) {
                rs.close ();
            }
            if (con != null) {
                con.close ();
            }
        }

        hf_map.put ("header", (header == null) ? "" : header);
        hf_map.put ("footer", (footer == null) ? "" : footer);

        return hf_map;
    }

    public java.util.ArrayList<InfoService> viewInfoServices (String keyword, String accountId, java.util.Date date, String ownerId) throws Exception {
        InfoService system_sms_queue = null;

        java.util.ArrayList<InfoService> list = new java.util.ArrayList ();
        //get count of records existing for keyword on specific date for a given account_id
        String counterSQL = "select ssq.*, sl.header, sl.footer from system_sms_queue ssq left outer join service_labels sl "
                + "on ssq.account_id = sl.account_id and ssq.keyword = sl.keyword "
                + "where ssq.keyword=? and ssq.account_id = ? and publish_date =? "
                + "and owner_id =? order by msg_id desc";
        Connection con = DConnect.getConnection ();
        PreparedStatement counterStat = con.prepareStatement (counterSQL);
        counterStat.setString (1, keyword);
        counterStat.setString (2, accountId);
        counterStat.setDate (3, new java.sql.Date (date.getTime ()));
        counterStat.setString (4, ownerId);

        ResultSet rs = counterStat.executeQuery ();

        while (rs.next ()) {
            system_sms_queue = new InfoService ();
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat ("dd-MMM-yyyy HH.mm.ss");
            String publishTime = df.format (new java.util.Date (rs.getTimestamp ("Publish_time").getTime ()));
            system_sms_queue.setPublishTime (publishTime);
            system_sms_queue.setKeyword (rs.getString ("keyword"));
            
            Clob clob = rs.getClob ("message");
            String tmpMsg = "";
            String temp = "";

            BufferedReader br = new BufferedReader (clob.getCharacterStream ());
            while ((temp = br.readLine ()) != null) {
                tmpMsg = tmpMsg + temp;
            }
            
            tmpMsg = URLUTF8Encoder.removeMalformedChars (tmpMsg);
            System.out.println ("Text item from SYSTEM_SMS_QUEUE (WORKING LOCALLY): " + tmpMsg);
            system_sms_queue.setMessage (tmpMsg);
            
            system_sms_queue.setAccountId (rs.getString ("account_id"));
            system_sms_queue.setMsgId (rs.getInt ("msg_id"));
            system_sms_queue.setOwnerId (rs.getString ("owner_id"));
            system_sms_queue.setPublishDate (new java.text.SimpleDateFormat ("dd-MMM-yyyy").format ((java.util.Date) rs.getDate ("publish_date")));
            system_sms_queue.setImageURL (rs.getString ("image_url"));
            system_sms_queue.setArticleTitle (rs.getString ("article_title"));

            String header = rs.getString ("header");
            String footer = rs.getString ("footer");
            system_sms_queue.setHeader ((header == null) ? "" : header);
            system_sms_queue.setFooter ((footer == null) ? "" : footer);

            //add to list
            list.add (system_sms_queue);
        }

        return list;
    }

    public void updatebackupsystem_sms_queue (java.util.Date date, java.lang.String service, java.lang.String message) throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {

            java.sql.Date sqlDate_date = new java.sql.Date (date.getTime ());

            con = DConnect.getConnection ();

            SQL = "Update  backup_system_sms_queue set message= ? , last_update=? where keyword=? ";

            prepstat = con.prepareStatement (SQL);


            prepstat.setString (1, message);
            prepstat.setDate (2, sqlDate_date);
            prepstat.setString (3, service);

            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }

    }

    public InfoService viewbackupsystem_sms_queue (String keyword) throws Exception {
        InfoService system_sms_queue = new InfoService ();

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection ();
            java.sql.Date tempdate = null;

            SQL = "select * from  backup_system_sms_queue where keyword=?";

            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, keyword);
            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                //System.out.println("the message is"+rs.getString("message"));

                system_sms_queue.setMessage (rs.getString ("message"));
            }

            // close connection, return to pool
            prepstat.close ();
            prepstat = null;
            con.close ();
            con = null;
        } catch (Exception ex) {
            ex.printStackTrace ();
            if (con != null) {
                con.close ();
                con = null;
            }
        } finally {
            if (prepstat != null) {
                try {
                    prepstat.close ();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close ();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }
        /*}catch (Exception ex){
        if(con !=null)
        con.close();
        throw new Exception(ex.getMessage());
        }
        if(con !=null)
        con.close();*/

        return system_sms_queue;
    }

    public void update_currency (java.lang.String alt_value, java.lang.String service, java.util.Date date) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            java.sql.Date sqlDate_date = new java.sql.Date (date.getTime ());

            con = DConnect.getConnection ();

            SQL = "Update system_sms_queue set alt_value = ? where service=? and date=?";

            prepstat = con.prepareStatement (SQL);

            prepstat.setString (1, alt_value);
            prepstat.setString (2, service);
            prepstat.setDate (3, sqlDate_date);

            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }

    public static void logInfoRequest (String ownerId, String msg, String accountId, String keyword, String msisdn, String shortcode) throws Exception {

        String SQL;
        Connection con = null;
        PreparedStatement prepstat = null;


        System.out.println (new java.util.Date () + ":logging Info request with ownerId-accountId:" + ownerId + "-" + accountId + "...");

        try {
            con = DConnect.getConnection ();

            SQL = "insert into  system_sms_usage_log(owner_id, msg, account_id, keyword, msisdn, shortcode) "
                    + "values(?,?,?,?,?,?)";

            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, ownerId);
            prepstat.setString (2, msg);
            prepstat.setString (3, accountId);
            prepstat.setString (4, keyword);
            prepstat.setString (5, msisdn);
            prepstat.setString (6, shortcode);
            prepstat.execute ();

            System.out.println (new java.util.Date () + ":Infor Request successfully logged!");
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            System.out.println (new java.util.Date () + ":Error logging InfoRequest:" + ex.getMessage ());

            throw new Exception (ex.getMessage ());
        }
        if (prepstat != null) {
            prepstat.close ();
        }
        if (con != null) {
            con.close ();
        }
    }
}
