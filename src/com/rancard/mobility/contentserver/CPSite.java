/*
 * CPSite.java
 *
 * Created on August 23, 2006, 1:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.contentserver;

import java.util.List;

/**
 *
 * @author Kweku Tandoh
 */
public class CPSite {
    
    public static final String WEB = "0";
    public static final String WAP = "1";
    public static final String SMS = "2";
    public static final String IVR = "3";
    
    private String cpId;
    private String cpSiteName;
    private String cpSiteId;
    private String siteType;
    private boolean checkUser;
    
    public CPSite(){        
    }
    
    public CPSite(String cpID, String siteName, String siteID, String siteType, boolean checkUser){
        this.cpId = cpID;
        this.cpSiteName = siteName;
        this.cpSiteId = siteID;
        this.siteType = siteType;
        this.checkUser = checkUser;
    }
    
   
    public String getCpId () {
        return cpId;
    }
    
    public void setCpId (String cpId) {
        this.cpId = cpId;
    }
    
    public String getCpSiteName () {
        return cpSiteName;
    }
    
    public boolean getCheckUser () {
        return this.checkUser;
    }
    
    public void setCpSiteName (String cpSiteName) {
        this.cpSiteName = cpSiteName;
    }
    
    public String getCpSiteId () {
        return cpSiteId;
    }
    
    public void setCpSiteId (String cpSiteId) {
        this.cpSiteId = cpSiteId;
    }
    
    public void setCheckUser (boolean check) {
        this.checkUser = check;
    }
    
    //management functions
    public static void createSite (String cpId, String siteName, String siteId, boolean check, String type) throws Exception {
        CPSiteDB.createSite (cpId, siteName, siteId, check, type);
    }
    
    public static void updateSiteName (String siteName, String siteId) throws Exception {
        CPSiteDB.updateSiteName (siteName, siteId);
    }
    
    public static void updateSiteAuthentication (boolean check, String siteId) throws Exception {
        CPSiteDB.updateSiteAuthentication (check, siteId);
    }
    
    public static void deleteSite (String siteId) throws Exception {
        CPSiteDB.deleteSite (siteId);
    }
    
    public static void deleteAllSitesForCP (String cpId) throws Exception {
        CPSiteDB.deleteAllSitesForCP (cpId);
    }
    
    public static CPSite viewSite (String siteId) throws Exception {
        return CPSiteDB.viewSite (siteId);
    }
    
    public static List<CPSite> viewAllSitesForCP (String cpId) throws Exception {
        return CPSiteDB.viewAllSitesForCP (cpId);
    }
    
    public static List<CPSite> viewAffiliatedSitesForCP (String cpId) throws Exception {
        return CPSiteDB.viewAffiliatedSitesForCP (cpId);
    }
    
    public static List<String> viewCPsWithSites () throws Exception {
        return CPSiteDB.viewCPsWithSites ();
    }
    
    public static String getCpIdForSite (String siteId) throws Exception {
        return CPSiteDB.getCpIdForSite (siteId);
    }

    public String getSiteType () {
        return siteType;
    }

    public void setSiteType (String siteType) {
        this.siteType = siteType;
    }
}
