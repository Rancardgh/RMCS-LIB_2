package com.rancard.mobility.rndvu;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author padmore
 */
public class ServiceRndvuDetails {
    private String accountId;
    private String keyword;
    private String clientId;
    private String storeId;
    private String itemId;
    private String serviceBundleId;

    public ServiceRndvuDetails(String accountId, String keyword, String clientId, String storeId, String itemId, String serviceBundleId) {
        this.accountId = accountId;
        this.keyword = keyword;
        this.clientId = clientId;
        this.storeId = storeId;
        this.itemId = itemId;
        this.serviceBundleId = serviceBundleId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getServiceBundleId() {
        return serviceBundleId;
    }

    public void setServiceBundleId(String serviceBundleId) {
        this.serviceBundleId = serviceBundleId;
    }
    
    public static ServiceRndvuDetails viewDetails(String accountId, String keyword){
        ServiceRndvuDetails details = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            String selectQuery = "SELECT * FROM service_rndvu_details WHERE account_id = ? AND keyword = ?;";
            conn = DConnect.getConnection();
            stmt = conn.prepareStatement(selectQuery);
            stmt.setString(1, accountId);
            stmt.setString(2, keyword);
            System.out.println(new Date()+"\tINFO\t[ServiceRndvuDetails:viewDetails]\tView Service Details Query: "+stmt.toString());
            rs = stmt.executeQuery();
            
            if (rs.next()){
                System.out.println(new Date()+"\tINFO\t[ServiceRndvuDetails:viewDetails]\tService Details Found!");
                details = new ServiceRndvuDetails(rs.getString("account_id"), rs.getString("keyword"), rs.getString("client_id"), 
                        rs.getString("store_id"), rs.getString("item_id"), rs.getString("service_bundle_id"));
            }
        } catch (Exception ex){
            System.out.println(new Date()+"\tERROR\t[ServiceRndvuDetails:viewDetails]\tException occurred: "+ex.getMessage());
        } finally{
            if (rs != null){
                try{
                    rs.close();
                } catch (SQLException sqx){
                    
                }
            }
            if (stmt != null){
                try{
                    stmt.close();
                } catch (SQLException sqx){
                    
                }
            }
            if (conn != null){
                try{
                    conn.close();
                } catch (SQLException sqx){
                    
                }
            }
        }
        return details;
    }
}
