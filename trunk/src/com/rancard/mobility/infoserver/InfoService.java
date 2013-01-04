package com.rancard.mobility.infoserver;
import com.rancard.mobility.infoserver.common.services.UserService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import com.rancard.util.MaskIP;
public class InfoService extends UserService {
    
// Bean Properties
    private String publishDate;
    private int msgId;
    private java.lang.String publishTime;
    private java.lang.String message;
    private java.util.ArrayList<InfoService> messages;
    private java.lang.String ownerId;
    private java.lang.String imageURL;
    private java.lang.String articleTitle;
    private String header;
    private String footer;
    private String messageRef;
    private String tags;
    public static final String DATEFORMATDISPLAY = "dd-MMM-yyyy";
    public static final String DATEFORMAT = "dd-MM-yyyy";
    public static final String TIMESTAMPFORMAT = "dd-MMM-yyyy HH.mm.ss";
    
    
    
//constructor
    
    
    public InfoService(){
    }
    
    
    public InfoService(String serviceType, String serviceName, String defaultMessage, String keyword, String message, String accountId, String pubDate) {
        
        super(serviceType, keyword, accountId, serviceName, defaultMessage);
        
        this.message = message;
        this.publishDate = pubDate;
        
        //this.participationLimit = limit;
    }
    
    public java.lang.String getPublishDate(){
        return this.publishDate;
    }
    
    public java.lang.String getPublishTime(){
        return this.publishTime;
    }
    
    public java.lang.String getMessage(){
        return this.message;
    }
    
    public java.lang.String getHeader(){
        return this.header;
    }
    
    public java.lang.String getFooter(){
        return this.footer;
    }
    
    public java.util.ArrayList<InfoService> getMessages(){
        return this.messages;
    }
    
    public int getMsgId() {
        return this.msgId;
    }
    public String getOwnerId() {
        return this.ownerId;
    }
    public java.lang.String getImageURL() {
        return imageURL;
    }
    public java.lang.String getArticleTitle() {
        return articleTitle;
    }
    
    public void setPublishDate(java.lang.String date) {
        this.publishDate=date;
    }
    
    public void setPublishTime(java.lang.String time) {
        this.publishTime=time;
    }
    
    public void setMessage(java.lang.String message) {
        this.message=message;
    }
    
    public void setHeader(java.lang.String header) {
        this.header=header;
    }
    
    public void setFooter(java.lang.String footer) {
        this.footer=footer;
    }
    
    public void setMessages(java.util.ArrayList<InfoService> messages) {
        this.messages=messages;
    }
    
    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }
    public void setOwnerId(java.lang.String ownerId){
        this.ownerId = ownerId;
    }
    
    public void setImageURL(java.lang.String imageURL) {
        this.imageURL = imageURL;
    }
    
    public void setArticleTitle(java.lang.String articleTitle) {
        this.articleTitle = articleTitle;
    }

    /**
     * @return the messageRef
     */
    public String getMessageRef () {
        return messageRef;
    }

    /**
     * @param messageRef the messageRef to set
     */
    public void setMessageRef (String messageRef) {
        this.messageRef = messageRef;
    }

    /**
     * @return the tags
     */
    public String getTags () {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags (String tags) {
        this.tags = tags;
    }
    
    public void createInfoServiceEntry() throws Exception{
        InfoServiceDB system_sms_queue=new InfoServiceDB();
        DateFormat formatter = new SimpleDateFormat(this.DATEFORMAT);
        Date date;
        try {
            date = (Date) formatter.parse(this.getPublishDate());
            system_sms_queue.createInfoServiceEntry(date,this.getKeyword(),this.getMessage(),this.getAccountId(), this.getOwnerId(), this.imageURL, this.articleTitle);
        } catch (ParseException ex) {
            //logging statement
            System.out.println(new java.util.Date()+":Error creating InfoServiceEntry:");
            ex.printStackTrace();
        }
        
    }
    
    public void updateInfoServiceEntry() throws Exception {
        InfoServiceDB system_sms_queue=new InfoServiceDB();
        DateFormat formatter = new SimpleDateFormat(this.DATEFORMAT);
        Date date;
        try {
            date = (Date) formatter.parse(this.getPublishDate());
            system_sms_queue.updateInfoService(date,this.getKeyword(),this.getMessage(),this.getAccountId(),this.msgId, this.imageURL, this.articleTitle);
        } catch (ParseException ex) {
            //logging statement
            System.out.println(new java.util.Date()+":Error updating InfoServiceEntry:");
            ex.printStackTrace();
        }
    }
    
    public void deleteInfoServiceEntry() throws Exception {
        InfoServiceDB system_sms_queue=new InfoServiceDB();
        DateFormat formatter = new SimpleDateFormat(this.DATEFORMAT);
        Date date;
        try {
            date = (Date) formatter.parse(this.getPublishDate());
            system_sms_queue.deleteInfoService(date,this.getKeyword(),this.getAccountId(),this.msgId);
        } catch (ParseException ex) {
            //logging statement
            System.out.println(new java.util.Date()+":Error deleting InfoServiceEntry:");
            ex.printStackTrace();
        }
    }
    
    public void viewInfoService()throws Exception {
        InfoService bean=new InfoService();
        InfoServiceDB system_sms_queue=new InfoServiceDB();
        java.text.SimpleDateFormat newDF=new java.text.SimpleDateFormat(this.DATEFORMAT);
        java.util.Date pubDate = newDF.parse(this.getPublishDate());
        bean=system_sms_queue.viewInfoService(this.getKeyword(),this.getAccountId(),pubDate);
        java.text.SimpleDateFormat df=new java.text.SimpleDateFormat(this.DATEFORMAT);
        
        this.setAccountId(bean.getAccountId());
        this.setDefaultMessage(bean.getDefaultMessage());
        this.setKeyword(bean.getKeyword());
        this.setPublishDate(bean.getPublishDate());
        this.setPublishTime(bean.getPublishTime());
        this.setServiceType(bean.getServiceType());
        this.setMessage(bean.getMessage());
        this.setMsgId(bean.getMsgId());
        this.setServiceName(bean.getServiceName());
        this.setPricing(bean.getPricing());
        this.setAllowedShortcodes(bean.getAllowedShortcodes());
        this.setAllowedSiteTypes(bean.getAllowedSiteTypes());
        this.setOwnerId(bean.getOwnerId());
        this.setImageURL(bean.getImageURL());
        this.setArticleTitle(bean.getArticleTitle());
        this.setHeader(bean.getHeader());
        this.setFooter(bean.getFooter());
    }
    public static InfoService viewInfoService(String keyword,String accountId, String date, int msg_id)throws Exception {
        
        InfoServiceDB system_sms_queue=new InfoServiceDB();
        java.text.SimpleDateFormat newDF=new java.text.SimpleDateFormat(DATEFORMAT);
        java.util.Date pubDate = newDF.parse(date);
        return system_sms_queue.viewInfoService(keyword,accountId,pubDate,msg_id);
        
    }
    
    public void viewMessages() throws Exception {
        java.util.ArrayList<InfoService> list = new java.util.ArrayList();
        InfoServiceDB system_sms_queue=new InfoServiceDB();
        java.text.SimpleDateFormat newDF=new java.text.SimpleDateFormat(this.DATEFORMAT);
        java.util.Date pubDate = newDF.parse(this.getPublishDate());
        this.messages = system_sms_queue.viewInfoServices(this.getKeyword(),this.getAccountId(),pubDate);
    }
    public void viewMessagesFor3rdParty() throws Exception {
        java.util.ArrayList<InfoService> list = new java.util.ArrayList();
        InfoServiceDB system_sms_queue=new InfoServiceDB();
        java.text.SimpleDateFormat newDF=new java.text.SimpleDateFormat(this.DATEFORMAT);
        java.util.Date pubDate = newDF.parse(this.getPublishDate());
        this.messages = system_sms_queue.viewInfoServices(this.getKeyword(),this.getAccountId(),pubDate, this.getOwnerId() );
    }
    
    public void updateInfoService() throws Exception {
        InfoServiceDB system_sms_queue=new InfoServiceDB();
        
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat(DATEFORMAT);
        java.util.Date utilDate_date=new java.util.Date();
        String s = java.text.DateFormat.getTimeInstance(DateFormat.SHORT).format(utilDate_date);
        
        try{
            utilDate_date=sdf.parse(publishDate);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
//        system_sms_queue.updateInfoService(id,utilDate_date,publishTime,service,message,user,delivered);
    }
    
    public  void viewHeaderFooter() throws Exception {
        InfoServiceDB system_sms_queue=new InfoServiceDB();
        java.util.HashMap<String, String> headerFooter = new java.util.HashMap();
        headerFooter = system_sms_queue.viewHeaderFooter(this.getKeyword(), this.getAccountId());
        this.header = headerFooter.get("header");
        this.footer = headerFooter.get("footer");
    }
    
    public void deleteInfoService() throws Exception {
        InfoServiceDB system_sms_queue=new InfoServiceDB();
        system_sms_queue.deleteInfoService(this.getKeyword(), this.getAccountId());
    }
    
    public static void logInfoRequest(String ownerId,String msg,String accountId, String keyword, String msisdn, String shortcode) throws Exception {
        
        InfoServiceDB.logInfoRequest(ownerId, msg, accountId, keyword, msisdn, shortcode);
    }

    
}

