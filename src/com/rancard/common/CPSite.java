package com.rancard.common;

import com.rancard.common.db.DConnect;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Mustee on 3/29/2014.
 */
public class CPSite implements Serializable {
    private static Logger logger = Logger.getLogger(CPSite.class.getName());

    private String siteId;
    private String cpId;
    private String siteName;
    private boolean checkUser;
    private String siteType;


    public CPSite(String siteId, String cpId, String siteName, boolean checkUser, String siteType) {
        this.siteId = siteId;
        this.cpId = cpId;
        this.siteName = siteName;
        this.checkUser = checkUser;
        this.siteType = siteType;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteID) {
        this.siteId = siteID;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public boolean isCheckUser() {
        return checkUser;
    }

    public void setCheckUser(boolean checkUser) {
        this.checkUser = checkUser;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }


    public static CPSite getSMSSite(String accountID) throws Exception {
        String query = "SELECT * FROM cp_sites WHERE site_type = '2' AND cp_id = ?";
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            conn = DConnect.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, accountID);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return new CPSite(rs.getString("site_id"), rs.getString("cp_id"), rs.getString("site_name"),
                        rs.getBoolean("check_user"), rs.getString("site_type"));
            }

            return null;
        } catch (Exception e) {
            logger.severe("Problem getting cp_site: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if(stmt !=null){
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}


