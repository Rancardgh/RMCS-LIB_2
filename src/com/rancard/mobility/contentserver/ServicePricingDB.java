

package com.rancard.mobility.contentserver;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Kweku Tandoh
 */
public abstract class ServicePricingDB {
    
    public static void create(String cpId, String keyword, String networkPrefix, String price, String serviceType, String shortCode) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            
            con = DConnect.getConnection();
            
            query = "select * from service_definition where keyword=? and account_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, keyword);
            prepstat.setString(2, cpId);
            rs = prepstat.executeQuery();
            if (rs.next()) {
                query = "INSERT into service_pricing(account_id,,keyword,network_prefix,price,service_type,short_code) values(?,?,?,?,?,?)";
                
                prepstat = con.prepareStatement(query);
                
                prepstat.setString(1, cpId);
                prepstat.setString(2, keyword);
                prepstat.setString(3, networkPrefix);
                prepstat.setString(4, price);
                prepstat.setString(5, serviceType);
                prepstat.setString(6, shortCode);
                
                prepstat.execute();
            }
            
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
    }
    
    public static void updatePrice(String price, String cpId, String serviceType, String networkCode) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String keyword = "";
        
        try {
            con = DConnect.getConnection();
            query = "select keyword from service_pricing where account_id=? and service_type=? and network_prefix=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, cpId);
            prepstat.setString(2, serviceType);
            prepstat.setString(3, networkCode);
            rs = prepstat.executeQuery();
            while(rs.next()){
                keyword = rs.getString("keyword");
            }
            
            query = "select * from service_definition where keyword=? and account_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, keyword);
            prepstat.setString(2, cpId);
            rs = prepstat.executeQuery();
            if (rs.next()) {
                query = "update service_pricing set price=? where account_id=? and service_type=? and network_prefix=?";
                
                prepstat = con.prepareStatement(query);
                
                prepstat.setString(1, price);
                prepstat.setString(2, cpId);
                prepstat.setString(3, serviceType);
                prepstat.setString(4, networkCode);
                prepstat.execute();
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
    }
    
    public static void delete(String cpId, String serviceType, String networkCode) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "DELETE from service_pricing WHERE account_id=? and service_type=? and network_prefix=?";
            prepstat = con.prepareStatement(query);
            
            prepstat.setString(1, cpId);
            prepstat.setString(2, serviceType);
            prepstat.setString(3, networkCode);
            
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
    }
    
    public static void deleteAllForCP(String cpId) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "DELETE from service_pricing WHERE account_id=?";
            prepstat = con.prepareStatement(query);
            
            prepstat.setString(1, cpId);
            
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
    }
    
    public static void deleteAllForCPonNetwork(String cpId, String networkCode) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "DELETE from service_pricing WHERE account_id=? and network_prefix=?";
            prepstat = con.prepareStatement(query);
            
            prepstat.setString(1, cpId);
            prepstat.setString(2, networkCode);
            
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
    }
    
    public static ServicePricingBean viewPrice(String accountId, String serviceType, String networkPrefix) throws Exception {
        ServicePricingBean bean = new ServicePricingBean();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String keyword = "";
        
        try {
            con = DConnect.getConnection();
            
            query = "select keyword from service_pricing where account_id=? and service_type=? and network_prefix=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, accountId);
            prepstat.setString(2, serviceType);
            prepstat.setString(3, networkPrefix);
            rs = prepstat.executeQuery();
            while(rs.next()){
                keyword = rs.getString("keyword");
            }
            
            query = "select * from service_definition where keyword=? and account_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);
            rs = prepstat.executeQuery();
            if (rs.next()) {
                query = "select * from service_pricing where account_id=? and service_type=? and network_prefix=?";
                prepstat = con.prepareStatement(query);
                
                prepstat.setString(1, accountId);
                prepstat.setString(2, serviceType);
                prepstat.setString(3, networkPrefix);
                
                rs = prepstat.executeQuery();
                
                while (rs.next()) {
                    bean.setAccountId(rs.getString("account_id"));
                    bean.setKeyword(rs.getString("keyword"));
                    bean.setNetworkPrefix(rs.getString("network_prefix"));
                    bean.setPrice(rs.getString("price"));
                    bean.setServiceType(rs.getString("service_type"));
                    bean.setShortCode(rs.getString("short_code"));
                }
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return bean;
    }
    
    public static ServicePricingBean viewPriceWithUrl(String accountId, String serviceType, String networkPrefix) throws Exception {
        ServicePricingBean bean = new ServicePricingBean();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String keyword = "";
        
        try {
            con = DConnect.getConnection();
            
            query = "select keyword from service_pricing where account_id=? and service_type=? and network_prefix=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, accountId);
            prepstat.setString(2, serviceType);
            prepstat.setString(3, networkPrefix);
            rs = prepstat.executeQuery();
            while(rs.next()){
                keyword = rs.getString("keyword");
            }
            
            query = "select * from service_definition where keyword=? and account_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);
            rs = prepstat.executeQuery();
            if (rs.next()) {
                query = "select * from service_pricing where account_id=? and service_type=? and network_prefix=?";
                prepstat = con.prepareStatement(query);
                
                prepstat.setString(1, accountId);
                prepstat.setString(2, serviceType);
                prepstat.setString(3, networkPrefix);
                
                rs = prepstat.executeQuery();
                
                while (rs.next()) {
                    bean.setAccountId(rs.getString("account_id"));
                    bean.setKeyword(rs.getString("keyword"));
                    bean.setNetworkPrefix(rs.getString("network_prefix"));
                    bean.setPrice(rs.getString("price"));
                    bean.setServiceType(rs.getString("service_type"));
                    bean.setShortCode(rs.getString("short_code"));
                    bean.setUrl (rs.getString ("billing_url"));
                }
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return bean;
    }
    
    public static ArrayList viewServicePricesForNetwork(String accountId, String serviceType) throws Exception {
        java.util.ArrayList sites = new java.util.ArrayList();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        ArrayList keywords = new ArrayList();
        
        try {
            con = DConnect.getConnection();
            
            query = "select keyword from service_definition where account_id=? and service_type=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, accountId);
            prepstat.setString(2, serviceType);
            rs = prepstat.executeQuery();
            while(rs.next()){
                keywords.add(rs.getString("keyword"));
            }
            if(keywords.size() > 0){
                query = "select * from service_pricing where account_id=? and service_type=? and (";
                for(int i = 0; i < keywords.size(); i++){
                    query = query + "keyword='" + keywords.get(i).toString() + "'";
                    if(i == keywords.size() - 1){
                        query = query + ")";
                    }else{
                        query = query + " or ";
                    }
                }
                prepstat = con.prepareStatement(query);
                prepstat.setString(1, accountId);
                prepstat.setString(2, serviceType);
                rs = prepstat.executeQuery();
                
                while (rs.next()) {
                    ServicePricingBean bean = new ServicePricingBean();
                    
                    bean.setAccountId(rs.getString("account_id"));
                    bean.setKeyword(rs.getString("keyword"));
                    bean.setNetworkPrefix(rs.getString("network_prefix"));
                    bean.setPrice(rs.getString("price"));
                    bean.setServiceType(rs.getString("service_type"));
                    bean.setShortCode(rs.getString("short_code"));
                    
                    sites.add(bean);
                }
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return sites;
    }
    
    public static ArrayList viewAllForCP(String accountId) throws Exception {
        java.util.ArrayList sites = new java.util.ArrayList();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        ArrayList keywords = new ArrayList();
        
        try {
            con = DConnect.getConnection();
            
            query = "select keyword from service_definition where account_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, accountId);
            rs = prepstat.executeQuery();
            while(rs.next()){
                keywords.add(rs.getString("keyword"));
            }
            if(keywords.size() > 0){
                query = "select * from service_pricing where account_id=? and (";
                for(int i = 0; i < keywords.size(); i++){
                    query = query + "keyword='" + keywords.get(i).toString() + "'";
                    if(i == keywords.size() - 1){
                        query = query + ")";
                    }else{
                        query = query + " or ";
                    }
                }
                prepstat = con.prepareStatement(query);
                prepstat.setString(1, accountId);
                rs = prepstat.executeQuery();
                
                while (rs.next()) {
                    ServicePricingBean bean = new ServicePricingBean();
                    
                    bean.setAccountId(rs.getString("account_id"));
                    bean.setKeyword(rs.getString("keyword"));
                    bean.setNetworkPrefix(rs.getString("network_prefix"));
                    bean.setPrice(rs.getString("price"));
                    bean.setServiceType(rs.getString("service_type"));
                    bean.setShortCode(rs.getString("short_code"));
                    
                    sites.add(bean);
                }
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return sites;
    }
    
    public static HashMap viewPricesForCP(String accountId) throws Exception {
        java.util.HashMap grpPrices = new java.util.HashMap();
        java.util.ArrayList prices = new java.util.ArrayList();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        ArrayList keywords = new ArrayList();
        
        try {
            con = DConnect.getConnection();
            
            query = "select keyword from service_definition where account_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, accountId);
            rs = prepstat.executeQuery();
            while(rs.next()){
                keywords.add(rs.getString("keyword"));
            }
            if(keywords.size() > 0){
                query = "select * from service_pricing where account_id=? and (";
                for(int i = 0; i < keywords.size(); i++){
                    query = query + "keyword='" + keywords.get(i).toString() + "'";
                    if(i == keywords.size() - 1){
                        query = query + ")";
                    }else{
                        query = query + " or ";
                    }
                }
                prepstat = con.prepareStatement(query);
                prepstat.setString(1, accountId);
                rs = prepstat.executeQuery();
                
                String conTypeMem = "1";
                while (rs.next()) {
                    ServicePricingBean bean = new ServicePricingBean();
                    bean.setAccountId(rs.getString("account_id"));
                    bean.setKeyword(rs.getString("keyword"));
                    bean.setNetworkPrefix(rs.getString("network_prefix"));
                    bean.setPrice(rs.getString("price"));
                    bean.setServiceType(rs.getString("service_type"));
                    bean.setShortCode(rs.getString("short_code"));
                    
                    if (bean.getServiceType().equals(conTypeMem)){
                        prices.add(bean);
                    } else {
                        grpPrices.put(new String(conTypeMem), prices);
                        conTypeMem = bean.getServiceType();
                        prices = new ArrayList();
                        prices.add(bean);
                    }
                }
                grpPrices.put(conTypeMem, prices);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return grpPrices;
    }
    
    public static HashMap viewAllPrices() throws Exception {
        HashMap prices = new HashMap(); //first level of grouping - service types
        
        String query;
        ResultSet rs = null;
        ResultSet types = null;
        ResultSet cps = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        ArrayList keywords = new ArrayList();
        
        try {
            con = DConnect.getConnection();
            
            query = "select service_type from service_route where parent_service_type=1";
            prepstat = con.prepareStatement(query);
            types = prepstat.executeQuery();
            
            while(types.next()){
                String srvType = types.getString("service_type");
                query = "select account_id from service_definition where service_type='" + srvType + "'";
                prepstat = con.prepareStatement(query);
                cps = prepstat.executeQuery();
                
                HashMap cpPrices = new HashMap (); //second level of grouping - providers
                while(cps.next()) {
                    String accountId = cps.getString("account_id");
                    query = "select keyword from service_definition where account_id='" + accountId + "' and service_type='" + srvType + "'";
                    prepstat = con.prepareStatement(query);
                    rs = prepstat.executeQuery();
                    while(rs.next()){
                        keywords.add(rs.getString("keyword"));
                    }
                    ArrayList indivPrices = new ArrayList (); //last level of grouping - prices themselves
                    if(keywords.size() > 0){
                        query = "select * from service_pricing where service_type='" + srvType + "' and account_id='" + accountId + "' and (";
                        for(int i = 0; i < keywords.size(); i++){
                            query = query + "keyword='" + keywords.get(i).toString() + "'";
                            if(i == keywords.size() - 1){
                                query = query + ")";
                            }else{
                                query = query + " or ";
                            }
                        }
                        prepstat = con.prepareStatement(query);
                        rs = prepstat.executeQuery();
                        
                        while (rs.next()) {
                            ServicePricingBean bean = new ServicePricingBean();
                            bean.setAccountId(rs.getString("account_id"));
                            bean.setKeyword(rs.getString("keyword"));
                            bean.setNetworkPrefix(rs.getString("network_prefix"));
                            bean.setPrice(rs.getString("price"));
                            bean.setServiceType(rs.getString("service_type"));
                            bean.setShortCode(rs.getString("short_code"));
                            
                            indivPrices.add(bean);
                        }
                    }
                    cpPrices.put(accountId, indivPrices);
                }
                prices.put(srvType, cpPrices);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        
        return prices;
    }
    
    /*public static void updateNetworkPrefix(String network, String cpId, String serviceType, String networkCode) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
     
        try {
            con = DConnect.getConnection();
            query = "update service_pricing set network_prefix=? where account_id=? and keyword=?";
     
            prepstat = con.prepareStatement(query);
     
            prepstat.setString(1, network);
            prepstat.setString(2, cpId);
            prepstat.setString(3, serviceType);
            prepstat.setString(4, networkCode);
            prepstat.execute();
     
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
    }*/
    
    /*public static void update(String price, String network, String cpId, String keyword) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
     
        try {
            con = DConnect.getConnection();
            query = "update service_pricing set price=?, network_prefix=? where account_id=? and keyword=?";
     
            prepstat = con.prepareStatement(query);
     
            prepstat.setString(1, price);
            prepstat.setString(2, network);
            prepstat.setString(3, cpId);
            prepstat.setString(4, keyword);
            prepstat.execute();
     
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
    }*/
    
    /*public static ServicePricingBean view (String accountId, String keyword, String networkPrefix) throws Exception {
        ServicePricingBean bean = new ServicePricingBean ();
     
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
     
        try {
            con = DConnect.getConnection ();
            query = "select * from service_definition where keyword=? and account_id=?";
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, keyword);
            prepstat.setString (2, accountId);
            // check if service is already registered on either the service_definition table if not insert it
            rs = prepstat.executeQuery ();
            if (!rs.next ()) {
                query = "select * from service_pricing where account_id=? and keyword=? and network_prefix=?";
                prepstat = con.prepareStatement (query);
     
                prepstat.setString (1, accountId);
                prepstat.setString (2, keyword);
                prepstat.setString (3, networkPrefix);
     
                rs = prepstat.executeQuery ();
     
                while (rs.next ()) {
                    bean.setAccountId (rs.getString ("account_id"));
                    bean.setKeyword (rs.getString ("keyword"));
                    bean.setNetworkPrefix (rs.getString ("network_prefix"));
                    bean.setPrice (rs.getString ("price"));
                    bean.setServiceType (rs.getString ("service_type"));
                    bean.setShortCode (rs.getString ("short_code"));
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
     
        return bean;
    }*/
}