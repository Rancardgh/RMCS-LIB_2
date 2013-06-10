package com.rancard.mobility.contentserver;

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
public class ContentType {
    
    private String serviceName;
    private int serviceType;
    private int parentServiceType;
    
    //constructor
    public ContentType () {
        this.serviceName = "";
        this.serviceType = 0;
        this.parentServiceType = 0;
    }
    
    //constructor
    public ContentType (String serviceName, int serviceType, int parentServiceType) {
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.parentServiceType = parentServiceType;
    }
    
    //mutator methods
    public void setServiceName (String serviceName) {
        this.serviceName = serviceName;
    }
    
    public void setServiceType (int serviceType) {
        this.serviceType = serviceType;
    }
    
    public void setParentServiceType (int parentServiceType) {
        this.parentServiceType = parentServiceType;
    }
    
    //accessor methods
    public String getServiceName () {
        return this.serviceName;
    }
    
    public int getServiceType () {
        return this.serviceType;
    }
    
    public int getTypeCode (String fileExtension) {
        if("mid".equals (fileExtension)){
            serviceType = 2;
        }
        
        if("jpg".equals (fileExtension)){
            serviceType = 4;
        }
        
        return serviceType;
    }
    
    public int getParentServiceType () {
        return this.parentServiceType;
    }
    
    //DB Access methods
    public static void insertServiceType(String serviceName, String serviceUrl, int serviceType,  int parentServiceType) throws Exception {
        ContentTypeDB.insertServiceType (serviceName, serviceUrl, serviceType,  parentServiceType);
    }
    
    public static void updateServiceType(int oldServiceType, String serviceName, int parentServiceType, String serviceUrl)  throws Exception {
        ContentTypeDB.updateServiceType (oldServiceType, serviceName, parentServiceType, serviceUrl);
    }
    
    public static void deleteServicType(int serviceType) throws Exception {
        ContentTypeDB.deleteServicType (serviceType);
    }
    
    public static ContentType viewServiceType (int serviceType) throws Exception {
        return ContentTypeDB.viewServiceType (serviceType);
    }
    
    public static java.util.List getDistinctTypes () throws Exception {
        java.util.List types = new java.util.ArrayList ();
        types = com.rancard.mobility.contentserver.ContentTypeDB.getDistinctTypes ();
        return types;
    }
    
    public static java.util.List getDistinctTypes (int parentServiceType) throws Exception {
        java.util.List types = new java.util.ArrayList ();
        types = com.rancard.mobility.contentserver.ContentTypeDB.getDistinctTypes (parentServiceType);
        return types;
    }
    
    public static java.util.ArrayList getDistinctTypes (int parentServiceType, String accountId) throws Exception {
        return ContentTypeDB.getDistinctTypes (parentServiceType, accountId);
    }
    
    public static boolean isRegisteredWithProfile (int serviceType, String accountId) throws Exception {
        return com.rancard.mobility.contentserver.ContentTypeDB.isRegisteredWithProfile (serviceType, accountId);
    }
}
