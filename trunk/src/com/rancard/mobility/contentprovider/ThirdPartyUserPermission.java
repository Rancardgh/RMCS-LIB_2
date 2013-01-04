/*
 * ThirdPartyUserPermission.java
 *
 * Created on September 12, 2007, 2:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.contentprovider;

/**
 *
 * @author USER
 */
public class ThirdPartyUserPermission {
    private com.rancard.mobility.infoserver.common.services.UserService service = new  com.rancard.mobility.infoserver.common.services.UserService();
    private java.lang.String accountId;
    private java.lang.String keyword;
    private java.lang.String ThirdPartyId;
    private java.lang.Boolean canView;
    private java.lang.Boolean canSubmit;
    private java.lang.Boolean canEdit;
    
    
    
    
    /** Creates a new instance of ThirdPartyUserPermission */
    public ThirdPartyUserPermission() {
    }
    
    public java.lang.String getAccountId() {
        return  this.getService().getAccountId();
        //  return accountId;
    }
    
    public void setAccountId(java.lang.String accountId) {
        this.getService().setAccountId(accountId);
        //  this.accountId = accountId;
    }
    
    public java.lang.String getKeyword() {
        return getService().getKeyword();
    }
    
    public void setKeyword(java.lang.String keyword) {
        this.getService().setKeyword(keyword);
        // this.keyword = keyword;
    }
    
    public String getServiceType(){
    return   (this.service !=null) ?this.service.getServiceType():"";
     
    }
    
    public java.lang.String getThirdPartyId() {
        return ThirdPartyId;
    }
    
    public void setThirdPartyId(java.lang.String ThirdPartyId) {
        this.ThirdPartyId = ThirdPartyId;
    }
    
    public java.lang.Boolean getCanView() {
        return canView;
    }
    
    public void setCanView(java.lang.Boolean canView) {
        this.canView = canView;
    }
    
    public int canView() {
        return canView.booleanValue()?1:0;
    }
    
    public java.lang.Boolean getCanSubmit() {
        return canSubmit;
    }
    
    public int canSubmit() {
        return canSubmit.booleanValue()?1:0;
    }
    
    public void setCanSubmit(java.lang.Boolean canSubmit) {
        this.canSubmit = canSubmit;
    }
    
    public java.lang.Boolean getCanEdit() {
        return canEdit;
    }
    
    public int canEdit() {
        return canEdit.booleanValue()?1:0;
    }
    public void setCanEdit(java.lang.Boolean canEdit) {
        this.canEdit = canEdit;
    }
    
    public com.rancard.mobility.infoserver.common.services.UserService getService() {
        return service;
    }
    
    public void setService(com.rancard.mobility.infoserver.common.services.UserService service) {
        this.service = service;
    }
    
}
