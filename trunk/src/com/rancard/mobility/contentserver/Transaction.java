package com.rancard.mobility.contentserver;

import java.util.ArrayList;

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
public class Transaction {

    //properties of bean
    private String ticketID; //unique ID for download
    private String id; //ID of content
    private String listID; //ID of list containing item
    private String subscriberMSISDN; //subsciber's ID
    private String phoneId; //phone id that is being used for the download
    private java.sql.Timestamp date; //date of download
    private boolean downloadCompleted; //status of download
    private String voucher; //eVoucher no.
    private String siteId; //ID of requesting website/wapsite/smsc
    private boolean isBilled; 
    private boolean notifSent; 
    private String keyword; //keyword used in requesting the download
    private String pricePoint;
    //composite items
    private ContentItem contentItem;
    private Format format;
    private CPSite site;

    /*
     Creates and instance of the bean. Instance properties have default values
     */
    public Transaction() {
        this.ticketID = null;
        this.id = null;
        this.listID = null;
        this.subscriberMSISDN = null;
        this.phoneId = "";
        this.date = now();
        this.downloadCompleted = false;
        this.voucher = null;
        this.siteId = null;
        this.pricePoint = null;
        this.isBilled = false;
        this.notifSent = false;
        
        this.keyword = "";
        
        this.contentItem = new ContentItem ();
        this.format = new Format ();
        this.site = new CPSite ();
    }

    /*
     Creates and instance of the bean. Instance properties have values provided
         on instantiation
     */
    public Transaction(String newTicketID, String newID, String newListID, String newSubscriberMSISDN, String phoneId,
                       boolean flag, String voucherNo, String siteId, boolean isBilled, String kw, boolean notifSent, String pricePoint) {
        this.ticketID = newTicketID;
        this.id = newID;
        this.listID = newListID;
        this.subscriberMSISDN = newSubscriberMSISDN;
        this.phoneId = phoneId;
        this.date = now();
        this.downloadCompleted = flag;
        this.voucher = voucherNo;
        this.siteId = siteId;
        this.isBilled = isBilled;
        this.notifSent = notifSent;
        this.keyword = kw;
        this.pricePoint = pricePoint;
        this.contentItem = new ContentItem();
        this.format = new Format();
        this.site = new CPSite ();
    }

    /*
        returns the exact time and date when the method was invoked
     */
    public java.sql.Timestamp now() {
        java.sql.Timestamp datetime = new java.sql.Timestamp(java.util.Calendar.
                getInstance().getTimeInMillis());
        return datetime;
    }

    /*
        Sets bean's ticketID property
     */
    public void setTicketID(String newTicketID) {
        this.ticketID = newTicketID;
    }


    /*
         Sets bean's id property
     */
    public void setID(String newID) {
        this.id = newID;
    }

    /*
         Sets bean's listID property
     */
    public void setListID(String newListID) {
        this.listID = newListID;
    }


    /*
            Sets bean's subscriberMSISDN property
     */
    public void setSubscriberMSISDN(String newSubscriberMSISDN) {
        this.subscriberMSISDN = newSubscriberMSISDN;
    }

    /*
        Sets bean's phoneId property
     */
    public void setPhoneId(String id) {
        this.phoneId = id;
    }

    /*
        Sets bean's date_time property
     */
    public void setDate(java.sql.Timestamp newDate) {
        this.date = newDate;
    }

    /*
        Sets bean's getDownloadCompleted property
     */
    public void setDownloadCompleted(boolean flag) {
        this.downloadCompleted = flag;
    }

    public void setPin(String pin) throws Exception {
        this.voucher = pin;
    }
    
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    
    public void setIsBilled (boolean isBilled) {
        this.isBilled = isBilled;
    }
    
    public void setKeyword (String keyword) {
        this.keyword = keyword;
    }

    /*
        Gets bean's ticketID value
     */
    public String getTicketID() {
        return this.ticketID;
    }

    /*
        Gets bean's id value
     */
    public String getID() {
        return this.id;
    }

    /*
      Gets bean's listID value
     */
    public String getListID() {
        return this.listID;
    }

    public ContentItem getContentItem() {
        return this.contentItem;
    }

    public void setContentItem(ContentItem item) {
        this.contentItem = item;
    }

    public Format getFormat() {
        return this.format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    /*
        Gets bean's subscriberMSISDN value
     */
    public String getSubscriberMSISDN() {
        return this.subscriberMSISDN;
    }

    /*
        Gets bean's phoneId value
     */
    public String getPhoneId() {
        return this.phoneId;
    }

    /*
        Gets bean's date_time value
     */
    public java.util.Date getDate() {
        return this.date;
    }

    /*
     Returns true if bean's download was successful and false if unsuccessful
     */
    public boolean getDownloadCompleted() {
        return this.downloadCompleted;
    }

    public String getPin() {
        return this.voucher;
    }
    
    public String getSiteId() {
        return this.siteId;
    }
    
    public boolean isIsBilled () {
        return isBilled;
    }
    
    public String getKeyword () {
        return keyword;
    }
    
    public CPSite getSite() {
        return site;
    }

    public void setSite(CPSite site) {
        this.site = site;
    }

    
    /*
        Updates properties of this bean with values from the log given a unique
        identifier
     */
    public static Transaction viewTransaction(String myTicketID) throws Exception {
        return TransactionDB.viewTransaction(myTicketID);
    }
    
    public static ArrayList viewTransactions (String ownerId, String msisdn, String downloadStatus, java.sql.Timestamp startdate,
            java.sql.Timestamp enddate, int typeId, String supplierId, String pin, String siteId, String billedStatus, String notifSent) throws Exception {
        return TransactionDB.viewTransactions (ownerId, msisdn, downloadStatus, startdate, enddate, typeId, supplierId, pin, siteId, billedStatus, notifSent);
    }

    /*public static com.rancard.util.Page viewTransactions(String csid, String msisdn, String status, java.sql.Timestamp startdate,
            java.sql.Timestamp enddate, int typeId, String listId, String pin, String siteId, String billed, int start, int count) throws Exception {
        return TransactionDB.viewTransactions(csid, msisdn, status, startdate, enddate, typeId, listId, pin, siteId, billed, start, count);
    }*/


    /*
        Updates attributes of log item with current property values
     */
    /*public static void updateTransaction(String ticketID, boolean status, boolean billed) throws Exception {
        TransactionDB.updateTransaction(ticketID, status, billed);
    }*/
    
    public static void updateDownloadStatus (String ticketID, boolean downloadCompleted) throws Exception {
        TransactionDB.updateDownloadStatus (ticketID, downloadCompleted);
    }
    
    /*public static void updateTransactionWithBilling (String ticketID, boolean billed) throws Exception {
        TransactionDB.updateTransactionWithBilling(ticketID, billed);
    }*/

    /*
        Deletes this log item
     */
    public static void removeTransaction(String ticketID) throws Exception {
        TransactionDB.deleteTransaction(ticketID);
    }

    /*
        Adds a log item
     */
    /*public static void logTransaction(String ticketID, String id, String listID, String subscriberMSISDN, String phoneId, boolean downloadCompleted,
                                      String pin, String siteId, boolean billed, String keyword) throws Exception {
        TransactionDB.createTransaction(ticketID, id, listID, subscriberMSISDN, phoneId, downloadCompleted, pin, siteId, billed, keyword);
    }*/
    
    public static void logTransaction(String ticketID, String id, String listID, String subscriberMSISDN, String phoneId, boolean downloadCompleted,
                                      String pin, String siteId, String keyword) throws Exception {
        TransactionDB.createTransaction(ticketID, id, listID, subscriberMSISDN, phoneId, downloadCompleted, pin, siteId, keyword);
    }

    /*
        Generates statistic on content usage
     */
    public static java.util.ArrayList getMostDownloadedThisMonth(int contentType) throws Exception {
        return TransactionDB.getMostDownloadedThisMonth(contentType);
    }

    public static java.util.ArrayList getMostDownloadedLastMonth(int contentType) throws Exception {
        return TransactionDB.getMostDownloadedLastMonth(contentType);
    }

    //returns number of completed downloads for the current date for a given provider
    public static int totalDownloadsToday(String listId) throws Exception {
        return TransactionDB.totalDownloadsToday(listId);
    }

    //returns number of completed downloads for a given date for a given provider
    public static int totalDownloadsForDate(String listId, java.util.Date date) throws Exception {
        return TransactionDB.totalDownloadsForDate(listId, date);
    }

    //returns number of completed downloads for the current month for a given provider
    public static int totalDownloadsThisMonth(String list_id) throws Exception {
        return TransactionDB.totalDownloadsThisMonth(list_id);
    }

    //returns number of completed downloads for the previous month for a given provider
    public static int totalDownloadsLastMonth(String list_id) throws Exception {
        return TransactionDB.totalDownloadsLastMonth(list_id);
    }

    //returns number of uncompleted downloads for a given provider
    public static int totalIncompleteDownloads(String list_id) throws Exception {
        return TransactionDB.totalIncompleteDownloads(list_id);
    }

    //returns filesize of completed downloads for a given provider
    public static long totalBandwidthUsed(String list_id) throws Exception {
        return TransactionDB.totalBandwidthUsed(list_id);
    }
    
    public static ArrayList fetchPopularDownloads (String cpid, int typeId) throws Exception {
        return TransactionDB.fetchPopularDownloads (cpid, typeId);
    }
        
    public static void exportPopularDownloadsToCustomList (String cpid, int limit) throws Exception {
        TransactionDB.exportPopularDownloadsToCustomList (cpid, limit);
    }

    /*
       public static void main(String[] args) {
           try{
               java.util.ArrayList list = new com.rancard.mobility.contentserver.DownloadLogBean().getMostDownloaded(1);


           } catch(Exception e){
               System.out.println("Exception: " + e.getMessage());
           }
       }
     */

    public boolean getNotifSent () {
        return notifSent;
    }

    public void setNotifSent (boolean notifSent) {
        this.notifSent = notifSent;
    }

    public String getPricePoint () {
        return pricePoint;
    }

    public void setPricePoint (String pricePoint) {
        this.pricePoint = pricePoint;
    }
}
