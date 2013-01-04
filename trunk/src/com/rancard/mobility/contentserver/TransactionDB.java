package com.rancard.mobility.contentserver;

import java.sql.*;
import java.util.*;
import com.rancard.common.DConnect;
import com.rancard.util.Page;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public abstract class TransactionDB {
    
    /*
     insert record
     */
    public static void createTransaction (String ticketID, String id, String listID, String subscriberMSISDN, String phoneId,
            boolean downloadCompleted, String pin, String siteId, /*boolean billed, */String keyword) throws Exception {
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query =
                    "INSERT into download_log(ticketId,id,list_id,subscriberMSISDN,phone_id,date_of_download,status,pin,site_id,keyword) values(?,?,?,?,?,?,?,?,?,?)";
            
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, ticketID);
            prepstat.setString (2, id);
            prepstat.setString (3, listID);
            prepstat.setString (4, subscriberMSISDN);
            prepstat.setString (5, phoneId);
            prepstat.setTimestamp (6,
                    new java.sql.Timestamp (java.util.Calendar.
                    getInstance ().
                    getTime ().getTime ()));
            if (downloadCompleted) {
                prepstat.setInt (7, 1);
            } else {
                prepstat.setInt (7, 0);
            }
            prepstat.setString (8, pin);
            prepstat.setString (9, siteId);
            prepstat.setString (10, keyword);
            
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
    
    public static void updateDownloadStatus (String ticketID, boolean downloadCompleted) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            int status = downloadCompleted ? 1 : 0;
            con = DConnect.getConnection ();
            query = "UPDATE download_log SET date_of_download=?,status=? WHERE ticketId='" + ticketID + "'";
            prepstat = con.prepareStatement (query);
            prepstat.setTimestamp (1, new java.sql.Timestamp (java.util.Calendar.getInstance ().getTime ().getTime ()));
            prepstat.setInt (2, status);
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
    
    /*
         Deletes a lod entry
     */
    public static void deleteTransaction (String ticketID) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "DELETE from download_log WHERE ticketId=?";
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, ticketID);
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
    
    /*
         Retrieves a log item as a bean
     */
    public static Transaction viewTransaction (String ticketID) throws Exception {
        Transaction newBean = new Transaction ();
        ContentItem item = new ContentItem ();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        int formatId = Integer.parseInt (ticketID.split ("-")[1]);
        
        try {
            con = DConnect.getConnection ();
            //KB: why does this guy look in the transactions table when nobody else inserts into it??
            query =
                    "select * from download_log d inner join content_list c on d.id=c.id and d.list_id=c.list_id inner join format_list f " +
                    "on c.formats=f.format_id inner join service_route t on c.content_type=t.service_type inner join cp_user u on d.list_id=u.id " +
                    " inner join transactions tr on tr.trans_id=d.ticketId inner join cp_sites cps on d.site_id=cps.site_id WHERE d.ticketId=?";
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, ticketID);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                //for transaction record
                newBean.setTicketID (rs.getString ("d.ticketid"));
                newBean.setSubscriberMSISDN (rs.getString (
                        "d.subscriberMSISDN"));
                newBean.setPhoneId (rs.getString ("d.phone_id"));
                newBean.setDate (rs.getTimestamp ("d.date_of_download"));
                newBean.setPin (rs.getString ("d.pin"));
                if (rs.getInt ("d.status") == 1) {
                    newBean.setDownloadCompleted (true);
                } else {
                    newBean.setDownloadCompleted (false);
                }
                newBean.setSiteId (rs.getString ("d.site_id"));
                if (rs.getInt ("tr.is_billed") == 1) {
                    newBean.setIsBilled (true);
                } else {
                    newBean.setIsBilled (false);
                }
                if (rs.getInt ("tr.is_completed") == 1) {
                    newBean.setNotifSent (true);
                } else {
                    newBean.setNotifSent (false);
                }
                newBean.setPricePoint (rs.getString ("tr.price_point_id"));
                newBean.setKeyword (rs.getString ("d.keyword"));
                
                //for site
                CPSite site = new CPSite ();
                site.setCpId (rs.getString ("cps.cp_id"));
                site.setCpSiteId (rs.getString ("cps.site_id"));
                site.setCpSiteName (rs.getString ("cps.site_name"));
                
                //for format object
                Format format = new Format (rs.getInt ("f.format_id"),
                        rs.getString ("f.file_ext"),
                        rs.getString ("f.push_bearer"),
                        rs.getString ("f.mime_type"));
                
                //for type object
                ContentType type = new ContentType (rs.getString ("t.service_name"), rs.getInt ("t.service_type"),
                        rs.getInt ("t.parent_service_type"));
                
                //for cp_user
                com.rancard.mobility.contentprovider.User cp =
                        new com.rancard.mobility.contentprovider.User ();
                cp.setName (rs.getString ("u.name"));
                cp.setDefaultSmsc (rs.getString ("default_smsc"));
                cp.setId (rs.getString ("u.id"));
                cp.setLogoUrl (rs.getString ("logo_url"));
                cp.setPassword (rs.getString ("password"));
                cp.setUsername (rs.getString ("username"));
                
                //for content item
                item = new ContentItem ();
                item.setAuthor (rs.getString ("c.author"));
                if (rs.getInt ("c.show") == 1) {
                    item.setCanList (true);
                } else {
                    item.setCanList (false);
                }
                item.setCategory (new Integer (rs.getInt (
                        "c.category")));
                item.setContentId (rs.getString ("c.content_id"));
                item.setContentTypeDetails (type);
                item.setDate_Added (rs.getTimestamp ("c.date_added"));
                item.setDownloadUrl (rs.getString ("c.download_url"));
                item.setFormat (format);
                item.setid (rs.getString ("c.id"));
                item.setListId (rs.getString ("c.list_id"));
                item.setOther_Details (rs.getString ("c.other_details"));
                item.setPreviewUrl (rs.getString ("c.preview_url"));
                item.setPrice (rs.getString ("c.price"));
                item.setSize (new Long (rs.getLong ("c.size")));
                if (rs.getInt ("c.isLocal") == 1) {
                    item.setIsLocal (true);
                } else {
                    item.setIsLocal (false);
                }
                if (rs.getInt ("c.isLocal") == 1) {
                    item.setPreviewExists (new Boolean (true));
                } else {
                    item.setPreviewExists (new Boolean (false));
                }
                item.settitle (rs.getString ("c.title"));
                item.setShortItemRef (rs.getString ("c.short_item_ref"));
                item.setSupplierId (rs.getString ("supplier_id"));
                item.setProviderDetails (cp);
                
                newBean.setContentItem (item);
                newBean.setFormat (format);
                newBean.setSite (site);
                
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return newBean;
    }
    
    public static ArrayList viewTransactions (String ownerId, String msisdn, String downloadStatus, java.sql.Timestamp startdate,
            java.sql.Timestamp enddate, int typeId, String supplierId, String pin, String siteId, String billedStatus, String notifSent) throws Exception {
        
        java.util.ArrayList transactions = new java.util.ArrayList ();
        ContentItem item = new ContentItem ();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            
            String siteIDquery = new String ();
            
            //for my sites
            query = "select site_id from cp_sites where cp_id='" + ownerId +"'";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            while(rs.next ()){
                siteIDquery = siteIDquery + "d.site_id='" + rs.getString ("site_id") + "' or ";
            }
            siteIDquery = siteIDquery.substring (0, siteIDquery.lastIndexOf ("' or ") + 1);
            
            //for report
            query = "select * from download_log d inner join content_list c on d.id=c.id and d.list_id=c.list_id inner join format_list f " +
                    "on c.formats=f.format_id inner join service_route t on c.content_type=t.service_type inner join cp_user u on d.list_id=u.id " +
                    " inner join transactions tr on tr.trans_id=d.ticketId where d.list_id='" + ownerId + "' ";
            
            if (msisdn != null && !msisdn.equals ("")) {
                query = query + " and d.subscriberMSISDN like '%" + msisdn + "%'";
            }
            if (downloadStatus != null && (downloadStatus.equals ("1") || downloadStatus.equals ("0"))) {
                query = query + " and d.status=" + Integer.parseInt (downloadStatus);
            }
            if (startdate != null) {
                query = query + " and d.date_of_download >='" + startdate + "'";
            }
            if (enddate != null) {
                query = query + " and d.date_of_download <='" + enddate + "'";
            }
            if (siteId != null && !siteId.equals ("")) {
                query = query + " and d.site_id='" + siteId + "'";
            }
            if (supplierId != null && !supplierId.equals ("*")) {
                query = query + " and c.supplier_id ='" + supplierId + "'";
            }
            if (pin != null && !pin.equals ("")) {
                query = query + " and d.pin='" + pin + "'";
            }
            if (billedStatus != null && (billedStatus.equals ("1") || billedStatus.equals ("0"))) {
                query = query + " and tr.is_billed=" + Integer.parseInt (billedStatus);
            }
            if (typeId != 0) {
                query = query + " and d.id=c.id and d.list_id=c.list_id and c.content_type=" + typeId;
            }
            if (siteId == null || siteId.equals ("")) {
                query = query + " and (" + siteIDquery + ")";
            }
            if (notifSent != null && (notifSent.equals ("1") || notifSent.equals ("0"))) {
                query = query + " and tr.is_completed=" + Integer.parseInt (notifSent);
            }
            query = query + " order by d.date_of_download desc";
            
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                Transaction newBean = new Transaction ();
                //for transaction record
                newBean.setTicketID (rs.getString ("d.ticketid"));
                newBean.setSubscriberMSISDN (rs.getString ("d.subscriberMSISDN"));
                newBean.setPhoneId (rs.getString ("d.phone_id"));
                newBean.setDate (rs.getTimestamp ("d.date_of_download"));
                newBean.setPin (rs.getString ("d.pin"));
                if (rs.getInt ("d.status") == 1) {
                    newBean.setDownloadCompleted (true);
                } else {
                    newBean.setDownloadCompleted (false);
                }
                newBean.setSiteId (rs.getString ("d.site_id"));
                if (rs.getInt ("tr.is_billed") == 1) {
                    newBean.setIsBilled (true);
                } else {
                    newBean.setIsBilled (false);
                }
                if (rs.getInt ("tr.is_completed") == 1) {
                    newBean.setNotifSent (true);
                } else {
                    newBean.setNotifSent (false);
                }
                newBean.setPricePoint (rs.getString ("tr.price_point_id"));
                newBean.setKeyword (rs.getString ("d.keyword"));
                
                //for format object
                Format format = new Format (rs.getInt ("f.format_id"),rs.getString ("f.file_ext"),rs.getString ("f.push_bearer"),rs.getString ("f.mime_type"));
                
                //for type object
                ContentType type = new ContentType (rs.getString ("t.service_name"), rs.getInt ("t.service_type"),rs.getInt ("t.parent_service_type"));
                
                //for cp_user
                com.rancard.mobility.contentprovider.User cp = new com.rancard.mobility.contentprovider.User ();
                cp.setName (rs.getString ("u.name"));
                cp.setDefaultSmsc (rs.getString ("default_smsc"));
                cp.setId (rs.getString ("u.id"));
                cp.setLogoUrl (rs.getString ("logo_url"));
                cp.setPassword (rs.getString ("password"));
                cp.setUsername (rs.getString ("username"));
                
                //for content item
                item = new ContentItem ();
                item.setAuthor (rs.getString ("c.author"));
                if (rs.getInt ("c.show") == 1) {
                    item.setCanList (true);
                } else {
                    item.setCanList (false);
                }
                item.setCategory (new Integer (rs.getInt ("c.category")));
                item.setContentId (rs.getString ("c.content_id"));
                item.setContentTypeDetails (type);
                item.setDate_Added (rs.getTimestamp ("c.date_added"));
                item.setDownloadUrl (rs.getString ("c.download_url"));
                item.setFormat (format);
                item.setid (rs.getString ("c.id"));
                item.setListId (rs.getString ("c.list_id"));
                item.setOther_Details (rs.getString ("c.other_details"));
                item.setPreviewUrl (rs.getString ("c.preview_url"));
                item.setPrice (rs.getString ("c.price"));
                item.setSize (new Long (rs.getLong ("c.size")));
                if (rs.getInt ("c.isLocal") == 1) {
                    item.setIsLocal (true);
                } else {
                    item.setIsLocal (false);
                }
                if (rs.getInt ("c.isLocal") == 1) {
                    item.setPreviewExists (new Boolean (true));
                } else {
                    item.setPreviewExists (new Boolean (false));
                }
                if (rs.getInt ("c.is_free") == 1) {
                    item.setFree (true);
                } else {
                    item.setFree (false);
                }
                item.settitle (rs.getString ("c.title"));
                item.setShortItemRef (rs.getString ("c.short_item_ref"));
                item.setSupplierId (rs.getString ("c.supplier_id"));
                
                item.setProviderDetails (cp);
                
                newBean.setContentItem (item);
                newBean.setFormat (format);
                
                transactions.add (newBean);
            }            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return transactions;
    }
    
    /*
        Retrrieves information on download numbers of content items
     */
    public static java.util.ArrayList getMostDownloadedThisMonth (int typeId) throws Exception {
        
        //get date variables
        java.util.Calendar calendar = java.util.Calendar.getInstance ();
        java.sql.Date today = new java.sql.Date (calendar.getTime ().getTime ());
        String dateString = today.toString ();
        java.util.StringTokenizer st =
                new java.util.StringTokenizer (dateString, "-");
        
        int year = Integer.parseInt (st.nextToken ());
        int month = Integer.parseInt (st.nextToken ());
        
        calendar.set (year, month - 1, 01, 00, 00, 00);
        java.sql.Timestamp firstDay = new java.sql.Timestamp (calendar.
                getTimeInMillis ());
        calendar.set (year, month, 01, 00, 00, 00);
        java.sql.Timestamp lastDay = new java.sql.Timestamp (calendar.
                getTimeInMillis ());
        
        java.util.ArrayList itemList;
        ContentItem item = new ContentItem ();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query =
                    "select * from download_log d inner join content_list c on " +
                    "d.id=c.id and d.list_id=c.list_id inner join format_list f " +
                    "on c.formats=f.format_id inner join service_route t on " +
                    "c.content_type=t.service_type inner join cp_user u on d.list_id=u.id" +
                    " where date_of_download>=? and date_of_download<=?";
            prepstat = con.prepareStatement (query);
            prepstat.setTimestamp (1, firstDay);
            prepstat.setTimestamp (2, lastDay);
            rs = prepstat.executeQuery ();
            
            int fetchSize = 0;
            while (rs.next ()) {
                fetchSize++;
            }
            rs.beforeFirst ();
            itemList = new ArrayList (0);
            itemList.add (0, "");
            int[] itemCount = new int[fetchSize];
            for (int i = 0; i < itemCount.length; i++) {
                itemCount[i] = 0;
            }
            
            while (rs.next ()) {
                //for format object
                Format format = new Format (rs.getInt ("f.format_id"), rs.getString ("f.file_ext"), rs.getString ("f.push_bearer"), rs.getString ("f.mime_type"));
                
                //for type object
                ContentType type = new ContentType (rs.getString ("t.service_name"), rs.getInt ("t.service_type"), rs.getInt ("t.parent_service_type"));
                
                //for cp_user
                com.rancard.mobility.contentprovider.User cp = new com.rancard.mobility.contentprovider.User ();
                cp.setName (rs.getString ("u.name"));
                cp.setDefaultSmsc (rs.getString ("default_smsc"));
                cp.setId (rs.getString ("u.id"));
                cp.setLogoUrl (rs.getString ("logo_url"));
                cp.setPassword (rs.getString ("password"));
                cp.setUsername (rs.getString ("username"));
                
                //for content item
                item = new ContentItem ();
                item.setAuthor (rs.getString ("c.author"));
                if (rs.getInt ("c.show") == 1) {
                    item.setCanList (true);
                } else {
                    item.setCanList (false);
                }
                item.setCategory (new Integer (rs.getInt ("c.category")));
                item.setContentId (rs.getString ("c.content_id"));
                item.setContentTypeDetails (type);
                item.setDate_Added (rs.getTimestamp ("c.date_added"));
                item.setDownloadUrl (rs.getString ("c.download_url"));
                item.setFormat (format);
                item.setid (rs.getString ("c.id"));
                item.setListId (rs.getString ("c.list_id"));
                item.setOther_Details (rs.getString ("c.other_details"));
                item.setPreviewUrl (rs.getString ("c.preview_url"));
                item.setPrice (rs.getString ("c.price"));
                item.setSize (new Long (rs.getLong ("c.size")));
                if (rs.getInt ("c.isLocal") == 1) {
                    item.setIsLocal (true);
                } else {
                    item.setIsLocal (false);
                }
                if (rs.getInt ("c.isLocal") == 1) {
                    item.setPreviewExists (new Boolean (true));
                } else {
                    item.setPreviewExists (new Boolean (false));
                }
                item.settitle (rs.getString ("c.title"));
                item.setShortItemRef (rs.getString ("c.short_item_ref"));
                item.setSupplierId (rs.getString ("c.supplier_id"));
                item.setProviderDetails (cp);
                
                for (int i = 0; i < fetchSize; i++) {
                    if ((itemList.get (i).equals ("")) ||
                            ((!((ContentItem) itemList.get (i)).isEqualTo (item)) &&
                            (i == fetchSize - 1))) {
                        itemList.add (i, item);
                        itemCount[itemList.indexOf (item)] += 1;
                        break;
                    }
                    if (((ContentItem) itemList.get (i)).isEqualTo (item)) {
                        itemCount[i] += 1;
                        break;
                    }
                }
            }
            itemList.remove ("");
            java.util.ArrayList items = new java.util.ArrayList ();
            double mean = 0.0;
            double sum = 0;
            for (int i = 0; i < itemList.size (); i++) {
                if (((ContentItem) itemList.get (i)).gettype ().intValue () ==
                        typeId) {
                    StatStruct struct = new StatStruct ((ContentItem) itemList.
                            get (i), itemCount[i]);
                    items.add (struct);
                    sum += struct.count;
                }
            }
            if (sum != 0) {
                mean = sum / items.size ();
            } else {
                mean = 0.0;
            }
            itemList = new ArrayList ();
            for (int i = 0; i < items.size (); i++) {
                int downloadCounter = ((StatStruct) items.get (i)).count;
                if (downloadCounter >= mean) {
                    itemList.add (((StatStruct) items.get (i)).clb);
                }
            }
        } catch (Exception e) {
            throw new Exception ();
        }
        return itemList;
    }
    
    public static java.util.ArrayList getMostDownloadedLastMonth (int typeId) throws Exception {
        
        //get date variables
        java.util.Calendar calendar = java.util.Calendar.getInstance ();
        java.sql.Date today = new java.sql.Date (calendar.getTime ().getTime ());
        String dateString = today.toString ();
        java.util.StringTokenizer st =
                new java.util.StringTokenizer (dateString, "-");
        
        int year = Integer.parseInt (st.nextToken ());
        int month = Integer.parseInt (st.nextToken ());
        
        calendar.set (year, month - 2, 01, 00, 00, 00);
        java.sql.Timestamp firstDay = new java.sql.Timestamp (calendar.
                getTimeInMillis ());
        calendar.set (year, month - 1, 01, 00, 00, 00);
        java.sql.Timestamp lastDay = new java.sql.Timestamp (calendar.
                getTimeInMillis ());
        
        java.util.ArrayList itemList;
        ContentItem item = new ContentItem ();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query =
                    "select * from download_log d inner join content_list c on " +
                    "d.id=c.id and d.list_id=c.list_id inner join format_list f " +
                    "on c.formats=f.format_id inner join service_route t on " +
                    "c.content_type=t.service_type inner join cp_user u on d.list_id=u.id" +
                    " where date_of_download>=? and date_of_download<=?";
            prepstat = con.prepareStatement (query);
            prepstat.setTimestamp (1, firstDay);
            prepstat.setTimestamp (2, lastDay);
            rs = prepstat.executeQuery ();
            
            int fetchSize = 0;
            while (rs.next ()) {
                fetchSize++;
            }
            rs.beforeFirst ();
            itemList = new ArrayList (0);
            itemList.add (0, "");
            int[] itemCount = new int[fetchSize];
            for (int i = 0; i < itemCount.length; i++) {
                itemCount[i] = 0;
            }
            
            while (rs.next ()) {
                //for format object
                Format format = new Format (rs.getInt ("f.format_id"), rs.getString ("f.file_ext"), rs.getString ("f.push_bearer"), rs.getString ("f.mime_type"));
                
                //for type object
                ContentType type = new ContentType (rs.getString ("t.service_name"), rs.getInt ("t.service_type"), rs.getInt ("t.parent_service_type"));
                
                //for cp_user
                com.rancard.mobility.contentprovider.User cp = new com.rancard.mobility.contentprovider.User ();
                cp.setName (rs.getString ("u.name"));
                cp.setDefaultSmsc (rs.getString ("default_smsc"));
                cp.setId (rs.getString ("u.id"));
                cp.setLogoUrl (rs.getString ("logo_url"));
                cp.setPassword (rs.getString ("password"));
                cp.setUsername (rs.getString ("username"));
                
                //for content item
                item = new ContentItem ();
                item.setAuthor (rs.getString ("c.author"));
                if (rs.getInt ("c.show") == 1) {
                    item.setCanList (true);
                } else {
                    item.setCanList (false);
                }
                item.setCategory (new Integer (rs.getInt (
                        "c.category")));
                item.setContentId (rs.getString ("c.content_id"));
                item.setContentTypeDetails (type);
                item.setDate_Added (rs.getTimestamp ("c.date_added"));
                item.setDownloadUrl (rs.getString ("c.download_url"));
                item.setFormat (format);
                item.setid (rs.getString ("c.id"));
                item.setListId (rs.getString ("c.list_id"));
                item.setOther_Details (rs.getString ("c.other_details"));
                item.setPreviewUrl (rs.getString ("c.preview_url"));
                item.setPrice (rs.getString ("c.price"));
                item.setSize (new Long (rs.getLong ("c.size")));
                if (rs.getInt ("c.isLocal") == 1) {
                    item.setIsLocal (true);
                } else {
                    item.setIsLocal (false);
                }
                if (rs.getInt ("c.isLocal") == 1) {
                    item.setPreviewExists (new Boolean (true));
                } else {
                    item.setPreviewExists (new Boolean (false));
                }
                item.settitle (rs.getString ("c.title"));
                item.setShortItemRef (rs.getString ("c.short_item_ref"));
                item.setSupplierId (rs.getString ("c.supplier_id"));
                item.setProviderDetails (cp);
                
                for (int i = 0; i < fetchSize; i++) {
                    if ((itemList.get (i).equals ("")) ||
                            ((!((ContentItem) itemList.get (i)).isEqualTo (item)) &&
                            (i == fetchSize - 1))) {
                        itemList.add (i, item);
                        itemCount[itemList.indexOf (item)] += 1;
                        break;
                    }
                    if (((ContentItem) itemList.get (i)).isEqualTo (item)) {
                        itemCount[i] += 1;
                        break;
                    }
                }
            }
            itemList.remove ("");
            java.util.ArrayList items = new java.util.ArrayList ();
            double mean = 0.0;
            double sum = 0;
            for (int i = 0; i < itemList.size (); i++) {
                if (((ContentItem) itemList.get (i)).gettype ().intValue () ==
                        typeId) {
                    StatStruct struct = new StatStruct ((ContentItem) itemList.
                            get (i), itemCount[i]);
                    items.add (struct);
                    sum += struct.count;
                }
            }
            if (sum != 0) {
                mean = sum / items.size ();
            } else {
                mean = 0.0;
            }
            itemList = new ArrayList ();
            for (int i = 0; i < items.size (); i++) {
                int downloadCounter = ((StatStruct) items.get (i)).count;
                if (downloadCounter >= mean) {
                    itemList.add (((StatStruct) items.get (i)).clb);
                }
            }
        } catch (Exception e) {
            throw new Exception ();
        }
        return itemList;
    }
    
    public static void exportPopularDownloadsToCustomList (String cpid, int limit) throws Exception {
        //get date variables
        if(limit > 10){limit = 10;}
        if(limit < 0){limit = 0;}
        java.sql.Date today = new java.sql.Date (java.util.Calendar.getInstance ().getTime ().getTime ());
        String dateString = today.toString ();
        java.util.StringTokenizer st = new java.util.StringTokenizer (dateString, "-");
        st.nextToken ();
        int  month = Integer.parseInt (st.nextToken ()) - 1;
        String monthStr = "";
        if(month < 10){
            monthStr = "0" + month;
        }else{
            monthStr = "" + month;
        }
        
        String query;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        Statement stat = null;
        
        try {
            con = DConnect.getConnection ();
            
            String customListId = null;
            
            query = "select custom_list_id from custom_list_definition where cp_id=? and custom_list_name='popular downloads'";
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, cpid);
            rs = prepstat.executeQuery (); //rs now contains list of custom list IDs for a list called 'Popular Downloads''
            while(rs.next ()){
                customListId = rs.getString ("custom_list_id");
            }
            
            if(customListId != null) {
                query = "select service_type from service_route where parent_service_type=1";
                prepstat = con.prepareStatement (query);
                rs = prepstat.executeQuery (); //rs now contains list of service types
                while(rs.next ()){
                    String contentType = rs.getString ("service_type");
                    query = "select dl.id, dl.list_id, COUNT(*) as hits from download_log dl inner join cp_sites cs on dl.site_id=cs.site_id inner join " +
                            "content_list cl on cl.id=dl.id and cl.list_id=dl.list_id where cs.cp_id='" + cpid + "' and cl.content_type=" + contentType +
                            " and month(dl.date_of_download)='" + monthStr + "' group by dl.id order by hits desc limit " + limit;
                    prepstat = con.prepareStatement (query);
                    rs2 = prepstat.executeQuery (); //rs2 contains list of IDs and ListIDs of all popular downloads of the type returned by rs
                    
                    if(rs2.next ()){ //test to see if any results were returned for popular downloads for type returned by rs
                        //if results were returned select all item references in the custom list which are of the same type and for the 'Popular Downloads' list
                        query = "select cu.item_id, cu.prov_id from custom_list cu inner join content_list co on co.id=cu.item_id and co.list_id=cu.prov_id " +
                                "where co.content_type=" + contentType + " and cu.custom_list_id='" + customListId + "'";
                        prepstat = con.prepareStatement (query);
                        rs3 = prepstat.executeQuery (); //rs3 contains list of IDs and ListIDs of all existing popular downloads of the type returned by rs
                        
                        //do a batch delete for all the items returned by rs3
                        int hasMore = 0;
                        con.setAutoCommit (false);
                        stat = con.createStatement ();
                        while(rs3.next ()){
                            query = "delete from custom_list where custom_list_id='" + customListId + "' and item_id='" + rs3.getString ("item_id") +
                                    "' and prov_id='" + rs3.getString ("prov_id") + "'";
                            stat.addBatch (query);
                            hasMore++;
                        }
                        if(hasMore > 0){
                            stat.executeBatch ();
                            stat.clearBatch ();
                        }
                        
                        rs2.beforeFirst ();
                        con.setAutoCommit (false);
                        con.createStatement ();
                        stat = con.createStatement ();
                        while (rs2.next ()) {
                            query = "INSERT into custom_list (custom_list_id, item_id, prov_id) values('" + customListId + "','" + rs2.getString ("id") +
                                    "','" + rs2.getString ("list_id") + "')";
                            stat.addBatch (query);
                        }
                        stat.executeBatch ();
                        stat.clearBatch ();
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception ();
        }
    }
    
    public static ArrayList fetchPopularDownloads (String cpid, int typeId) throws Exception {
        
        String query;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String custimlistid = null;
        ArrayList items = new ArrayList ();
        
        try {
            con = DConnect.getConnection ();
            query = "select custom_list_id from custom_list_definition where cp_id=? and custom_list_name='popular downloads'";
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, cpid);
            rs = prepstat.executeQuery ();
            
            if (rs.next ()) { //first instance of custom list definition
                custimlistid = rs.getString ("custom_list_id");
                
                query = "select item_id, prov_id from custom_list where custom_list_id=?";
                prepstat = con.prepareStatement (query);
                prepstat.setString (1, custimlistid);
                rs = prepstat.executeQuery ();
                
                while (rs.next ()) { //possibly, the existence of multiple item references
                    ContentItem item = new ContentItem ();
                    Format format = new Format ();
                    ContentType type = new ContentType ();
                    com.rancard.mobility.contentprovider.User cp = new com.rancard.mobility.contentprovider.User ();
                    
                    query = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id=? and content_list.list_id=? and " +
                            "content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id " +
                            "and content_list.content_type=" + typeId;
                    
                    prepstat = con.prepareStatement (query);
                    String id = rs.getString ("item_id");
                    String listid = rs.getString ("prov_id");
                    
                    prepstat.setString (1, id);
                    prepstat.setString (2, listid);
                    
                    rs2 = prepstat.executeQuery ();
                    while (rs2.next ()) { //an item
                        //content item
                        item.setContentId (rs2.getString ("content_id"));
                        item.setid (rs2.getString ("id"));
                        item.settitle (rs2.getString ("title"));
                        item.settype (new Integer (rs2.getInt ("content_type")));
                        item.setdownload_url (rs2.getString ("download_url"));
                        item.setPreviewUrl (rs2.getString ("preview_url"));
                        item.setPrice (rs2.getString ("price"));
                        item.setAuthor (rs2.getString ("author"));
                        item.setCategory (new Integer (rs2.getInt ("category")));
                        item.setSize (new Long (rs2.getLong ("size")));
                        item.setListId (rs2.getString ("list_id"));
                        item.setDate_Added (rs2.getTimestamp ("date_added"));
                        item.setOther_Details (rs2.getString ("other_details"));
                        if (rs2.getInt ("isLocal") == 1) {
                            item.setIsLocal (true);
                        } else {
                            item.setIsLocal (false);
                        }
                        if (rs2.getInt ("show") == 1) {
                            item.setCanList (true);
                        } else {
                            item.setCanList (false);
                        }
                        if (rs2.getInt ("is_free") == 1) {
                            item.setFree (true);
                        } else {
                            item.setFree (false);
                        }
                        item.setShortItemRef (rs2.getString ("short_item_ref"));
                        item.setSupplierId (rs2.getString ("supplier_id"));
                        
                        //content type
                        type.setParentServiceType (rs2.getInt ("service_route.parent_service_type"));
                        type.setServiceName (rs2.getString ("service_route.service_name"));
                        type.setServiceType (rs2.getInt ("service_route.service_type"));
                        
                        //content provider
                        cp.setId (rs2.getString ("cp_user.id"));
                        cp.setName (rs2.getString ("cp_user.name"));
                        cp.setUsername (rs2.getString ("cp_user.username"));
                        cp.setPassword (rs2.getString ("cp_user.password"));
                        cp.setDefaultSmsc (rs2.getString ("cp_user.default_smsc"));
                        cp.setLogoUrl (rs2.getString ("cp_user.logo_url"));
                        cp.setDefaultService (rs2.getString ("cp_user.default_service"));
                        
                        //format
                        format.setId (rs2.getInt ("format_list.format_id"));
                        format.setFileExt (rs2.getString ("format_list.file_ext"));
                        format.setMimeType (rs2.getString ("format_list.mime_type"));
                        format.setPushBearer (rs2.getString ("format_list.push_bearer"));
                    }
                    item.setContentTypeDetails (type);
                    item.setProviderDetails (cp);
                    item.setFormat (format);
                    
                    items.add (item);
                }
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return items;
    }
    
    public static int totalDownloadsToday (String list_id) throws Exception {
        java.util.Calendar calendar = java.util.Calendar.getInstance ();
        java.sql.Date today = new java.sql.Date (calendar.getTime ().getTime ());
        
        int number = 0;
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "select count(*) as number from download_log where date(date_of_download) =? and list_id=? and status=1";
            prepstat = con.prepareStatement (query);
            prepstat.setDate (1, today);
            prepstat.setString (2, list_id);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                number = rs.getInt ("number");
            }
        } catch (Exception e) {
            throw new Exception ();
        }
        return number;
    }
    
    public static int totalDownloadsThisMonth (String list_id) throws Exception {
        java.util.Calendar calendar = java.util.Calendar.getInstance ();
        java.sql.Date today = new java.sql.Date (calendar.getTime ().getTime ());
        String dateString = today.toString ();
        
        int number = 0;
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "select count(*) as number from download_log where month(date_of_download)=month(?) and list_id=? and status=1";
            prepstat = con.prepareStatement (query);
            prepstat.setDate (1, today);
            prepstat.setString (2, list_id);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                number = rs.getInt ("number");
            }
        } catch (Exception e) {
            throw new Exception ();
        }
        return number;
    }
    
    public static int totalDownloadsLastMonth (String list_id) throws Exception {
        java.util.Calendar calendar = java.util.Calendar.getInstance ();
        calendar.add (calendar.MONTH, -1);
        java.sql.Date then = new java.sql.Date (calendar.getTime ().getTime ());
        
        int number = 0;
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "select count(*) as number from download_log where month(date_of_download)=month(?) and list_id=? and status=1";
            prepstat = con.prepareStatement (query);
            prepstat.setDate (1, then);
            prepstat.setString (2, list_id);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                number = rs.getInt ("number");
            }
        } catch (Exception e) {
            throw new Exception ();
        }
        return number;
    }
    
    public static int totalDownloadsForDate (String list_id, java.util.Date date) throws Exception {
        java.sql.Date today = new java.sql.Date (date.getTime ());
        
        int number = 0;
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "select count(*) as number from download_log where date(date_of_download)=? and list_id=? and status=1";
            prepstat = con.prepareStatement (query);
            prepstat.setDate (1, today);
            prepstat.setString (2, list_id);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                number = rs.getInt ("number");
            }
        } catch (Exception e) {
            throw new Exception ();
        }
        return number;
    }
    
    public static int totalIncompleteDownloads (String list_id) throws Exception {
        int number = 0;
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "select count(*) as number from download_log where list_id=? and status=0";
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, list_id);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                number = rs.getInt ("number");
            }
        } catch (Exception e) {
            throw new Exception ();
        }
        return number;
    }
    
    public static long totalBandwidthUsed (String list_id) throws Exception {
        long bandwidth = 0;
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query =
                    "select uploads.binaryfile from uploads, download_log where download_log.list_id=? and status=1 and download_log.list_id=uploads.list_id and download_log.id=uploads.id";
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, list_id);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                java.sql.Blob b = rs.getBlob ("binaryfile");
                if (b != null) {
                    bandwidth += b.length ();
                }
            }
        } catch (Exception e) {
            throw new Exception ();
        }
        return bandwidth;
    }
    
    /*public static void updateTransactionWithBilling (String ticketID, boolean billed) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            int status = billed ? 1 : 0;
            con = DConnect.getConnection ();
            query = "UPDATE download_log SET billed=? WHERE ticketId='" + ticketID + "'";
            prepstat = con.prepareStatement (query);
            prepstat.setInt (1, status);
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
    }*/
    
    /*
         Update log entry - usually to set time and status of download
     */
    /*public static void updateTransaction (String ticketID, boolean downloadCompleted, boolean isBilled) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            int status = downloadCompleted ? 1 : 0;
            int billed = isBilled ? 1 : 0;
            con = DConnect.getConnection ();
            query = "UPDATE download_log SET date_of_download=?,status=?,billed=? WHERE ticketId='" + ticketID + "'";
            prepstat = con.prepareStatement (query);
            prepstat.setTimestamp (1,
                    new java.sql.Timestamp (java.util.Calendar.
                    getInstance ().
                    getTime ().getTime ()));
            prepstat.setInt (2, status);
            prepstat.setInt (3, billed);
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
    }*/
    
    /*public static com.rancard.util.Page viewTransactions (String csid, String msisdn, String status, java.sql.Timestamp startdate,
            java.sql.Timestamp enddate, int typeId, String listId, String pin, String siteId, String billed, int start, int count) throws Exception {
        
        java.util.ArrayList transactions = new java.util.ArrayList ();
        ContentItem item = new ContentItem ();
        Page page = null;
        
        int y = 0;
        int i = 0;
        int numResults = 0;
        boolean hasNext = false;
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ("rmcs");
            
            String siteIDquery = new String ();
            String otherAffliliatedSiteIDquery = new String ();
            
            //for my sites
            query = "select site_id from cp_sites where cp_id='" + csid +"'";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            while(rs.next ()){
                siteIDquery = siteIDquery + "d.site_id='" + rs.getString ("site_id") + "' or ";
            }
            siteIDquery = siteIDquery.substring (0, siteIDquery.lastIndexOf ("' or ") + 1);
            
            //for my affiliated sites
            query = "select site_id from cp_sites where cp_id in (select cs_id from cs_cp_relationship where list_id='" + csid + "')";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            while(rs.next ()){
                otherAffliliatedSiteIDquery = otherAffliliatedSiteIDquery + "(d.site_id='" + rs.getString ("site_id") + "' and d.list_id='" + csid + "') or ";
            }
            otherAffliliatedSiteIDquery = otherAffliliatedSiteIDquery.substring (0, otherAffliliatedSiteIDquery.lastIndexOf (" or ") + 1);
            
            if((otherAffliliatedSiteIDquery == null || otherAffliliatedSiteIDquery.equals ("")) && (siteIDquery == null || siteIDquery.equals (""))){
                listId = csid;
            }
            
            //for report
            query = "select * from download_log d inner join content_list c on d.id=c.id and d.list_id=c.list_id inner join format_list f " +
                    "on c.formats=f.format_id inner join service_route t on c.content_type=t.service_type inner join cp_user u on d.list_id=u.id";
            
            if (msisdn != null && !msisdn.equals ("")) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where d.subscriberMSISDN='" + msisdn + "'";
                } else {
                    query = query + " and d.subscriberMSISDN='" + msisdn + "'";
                }
            }
            if (status != null && (status.equals ("1") || status.equals ("0"))) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where d.status=" + Integer.parseInt (status);
                } else {
                    query = query + " and d.status=" + Integer.parseInt (status);
                }
            }
            if (startdate != null) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where d.date_of_download >='" + startdate + "'";
                } else {
                    query = query + " and d.date_of_download >='" + startdate + "'";
                }
            }
            if (enddate != null) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where d.date_of_download <='" + enddate + "'";
                } else {
                    query = query + " and d.date_of_download <='" + enddate + "'";
                }
            }
            if (siteId != null && !siteId.equals ("")) {
                //checking owner of given site ID
                String tempquery = "select cp_id from cp_sites where site_id='" + siteId + "'";
                prepstat = con.prepareStatement (tempquery);
                ResultSet temprs = prepstat.executeQuery ();
                if(temprs.next ()){
                    if(!csid.equals (temprs.getString ("cp_id"))){ //not the owner of the site
                        listId = csid;
                    }
                }
                if (query.indexOf ("where") == -1) {
                    query = query + " where d.site_id='" + siteId + "'";
                } else {
                    query = query + " and d.site_id='" + siteId + "'";
                }
            }
            if (listId != null && !listId.equals ("")) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where d.list_id ='" + listId + "'";
                } else {
                    query = query + " and d.list_id ='" + listId + "'";
                }
            }
            if (pin != null && !pin.equals ("")) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where d.pin='" + pin + "'";
                } else {
                    query = query + " and d.pin='" + pin + "'";
                }
            }
            if (billed != null && (billed.equals ("1") || billed.equals ("0"))) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where d.billed=" + Integer.parseInt (billed);
                } else {
                    query = query + " and d.billed=" + Integer.parseInt (billed);
                }
            }
            if (typeId != 0) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where d.id=c.id and d.list_id=c.list_id " + "and c.content_type=" + typeId;
                } else {
                    query = query + " and d.id=c.id and d.list_id=c.list_id and " + "c.content_type=" + typeId;
                }
            }
            if (siteId == null || siteId.equals ("")) {
                if(siteIDquery != null && !siteIDquery.equals ("")){
                    if (query.indexOf ("where") == -1) {
                        query = query + " where ((" + siteIDquery + ")";
                    } else {
                        query = query + " and ((" + siteIDquery + ")";
                    }
                    if(otherAffliliatedSiteIDquery != null && !otherAffliliatedSiteIDquery.equals ("")){
                        query = query + " or " + otherAffliliatedSiteIDquery;
                    }
                    query = query + ")";
                }else{
                    if(otherAffliliatedSiteIDquery != null && !otherAffliliatedSiteIDquery.equals ("")){
                        query = query + " and (" + otherAffliliatedSiteIDquery + ")";
                    }
                }
                
            }
            
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            // get the total number of records
            rs.last ();
            numResults = rs.getRow ();
            rs.beforeFirst ();
            
            while (i < (start + count) && rs.next ()) {
                if (i == 0) {
                    int x = numResults;
                    y = x / count;
                    if ((x % count) > 0) {
                        y += 1;
                    }
                }
                if (i >= start) {
                    
                    Transaction newBean = new Transaction ();
                    //for transaction record
                    newBean.setTicketID (rs.getString ("d.ticketid"));
                    newBean.setSubscriberMSISDN (rs.getString (
                            "d.subscriberMSISDN"));
                    newBean.setPhoneId (rs.getString ("d.phone_id"));
                    newBean.setDate (rs.getTimestamp ("d.date_of_download"));
                    newBean.setPin (rs.getString ("d.pin"));
                    if (rs.getInt ("d.status") == 1) {
                        newBean.setDownloadCompleted (true);
                    } else {
                        newBean.setDownloadCompleted (false);
                    }
                    newBean.setSiteId (rs.getString ("d.site_id"));
                    if (rs.getInt ("d.billed") == 1) {
                        newBean.setIsBilled (true);
                    } else {
                        newBean.setIsBilled (false);
                    }
                    newBean.setKeyword (rs.getString ("d.keyword"));
                    
                    //for format object
                    Format format = new Format (rs.getInt ("f.format_id"),
                            rs.getString ("f.file_ext"),
                            rs.getString ("f.push_bearer"),
                            rs.getString ("f.mime_type"));
                    
                    //for type object
                    ContentType type = new ContentType (rs.getString ("t.service_name"), rs.getInt ("t.service_type"),
                            rs.getInt ("t.parent_service_type"));
                    
                    //for cp_user
                    com.rancard.mobility.contentprovider.User cp =
                            new com.rancard.mobility.contentprovider.User ();
                    cp.setName (rs.getString ("u.name"));
                    cp.setDefaultSmsc (rs.getString ("default_smsc"));
                    cp.setId (rs.getString ("u.id"));
                    cp.setLogoUrl (rs.getString ("logo_url"));
                    cp.setPassword (rs.getString ("password"));
                    cp.setUsername (rs.getString ("username"));
                    
                    //for content item
                    item = new ContentItem ();
                    item.setAuthor (rs.getString ("c.author"));
                    if (rs.getInt ("c.show") == 1) {
                        item.setCanList (true);
                    } else {
                        item.setCanList (false);
                    }
                    item.setCategory (new Integer (rs.getInt (
                            "c.category")));
                    item.setContentId (rs.getString ("c.content_id"));
                    item.setContentTypeDetails (type);
                    item.setDate_Added (rs.getTimestamp ("c.date_added"));
                    item.setDownloadUrl (rs.getString ("c.download_url"));
                    item.setFormat (format);
                    item.setid (rs.getString ("c.id"));
                    item.setListId (rs.getString ("c.list_id"));
                    item.setOther_Details (rs.getString ("c.other_details"));
                    item.setPreviewUrl (rs.getString ("c.preview_url"));
                    item.setPrice (rs.getString ("c.price"));
                    item.setSize (new Long (rs.getLong ("c.size")));
                    if (rs.getInt ("c.isLocal") == 1) {
                        item.setIsLocal (true);
                    } else {
                        item.setIsLocal (false);
                    }
                    if (rs.getInt ("c.isLocal") == 1) {
                        item.setPreviewExists (new Boolean (true));
                    } else {
                        item.setPreviewExists (new Boolean (false));
                    }
                    item.settitle (rs.getString ("c.title"));
                    item.setProviderDetails (cp);
                    
                    newBean.setContentItem (item);
                    newBean.setFormat (format);
                    
                    transactions.add (newBean);
                }
                i++;
            }
            
            hasNext = rs.next ();
            page = new Page (transactions, start, hasNext, y, numResults);
            
            if (page == null) {
                page = com.rancard.util.Page.EMPTY_PAGE;
            }
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return page;
    }*/
    /*
        holds, as a data structure, a content item and the number of times
        it has been downloaded
     */
    static class StatStruct {
        ContentItem clb;
        int count;
        
        public StatStruct (ContentItem clb_new, int count_new) {
            this.clb = clb_new;
            this.count = count_new;
        }
    }
    
    
    /*
        test
     */
    public static void main (String args[]) throws Exception {
        /*DownloadLogBean dlb = new  DownloadLogBean();
                DownloadLogBean dlb2 = new  DownloadLogBean();
                dlb.setTicketID("00101");
                dlb.setID("01");
                dlb.setListID("001");
                dlb.setSubscriberMSISDN("233276768443");
                dlb.setDownloadCompleted(false);
         
                dlb.logMe();
         
                dlb2.viewMyLog("00101");
         System.out.println("dlb2's Subscriber's MSISDN" + dlb2.getSubscriberMSISDN());
         
                dlb2.setDownloadCompleted(true);
                System.out.println("dlb2's status " + dlb2.getTicketID());
                dlb2.updateMyLog();
         
                dlb.viewMyLog("00101");
                System.out.println("dlb's" + dlb.downloadCompleted());
         
                dlb2.removeMyLog();*/
        
        //try update
        //DownloadLogDB.updateLogItem("34h1ju88d0",new DownloadLogBean().now(),true);
        
        
    }
    
    
}
