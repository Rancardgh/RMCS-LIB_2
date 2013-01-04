package com.rancard.mobility.contentprovider;

import com.rancard.common.DConnect;
import java.util.ArrayList;
import java.sql.*;


public class UserDB {
    
    
    public void createDealer(String name, String id, double accountBalance, String username, String password,
            double bandwidthBalance, double inboxBalance, double outboxBalance) throws Exception {
        String SQL;
        
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            SQL = "select * from cp_user where username=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, username);
            rs = prepstat.executeQuery();
            if (rs.next()) {
                
                throw new Exception(
                        "This username already exists .please use a different username");
                
            } else {
                
                SQL =
                        "Insert into cp_user(accountBalance,id,name,username,password,bandwidth_balance,inbox_balance,outbox_balance) values(?,?,?,?,?,?,?,?)";
                
                prepstat = con.prepareStatement(SQL);
                prepstat.setDouble(1, accountBalance);
                prepstat.setString(2, id);
                prepstat.setString(3, name);
                prepstat.setString(4, username);
                prepstat.setString(5, password);
                prepstat.setDouble(6, bandwidthBalance);
                prepstat.setDouble(7, inboxBalance);
                prepstat.setDouble(8, outboxBalance);
                
                prepstat.execute();
                
            }
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                }
                con = null;
            }
            
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
    }
    
    public void updateDealer(String name, String id, double accountBalance,
            String username, String password) throws Exception {
        String SQL;
        
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            SQL = "Update cp_user set accountBalance=?,id=?,name=?,username=?,password=? where username='" +
                    username + "'";
            
            prepstat = con.prepareStatement(SQL);
            prepstat.setDouble(1, accountBalance);
            prepstat.setString(2, id);
            prepstat.setString(3, name);
            prepstat.setString(4, username);
            prepstat.setString(5, password);
            
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                }
                con = null;
            }
            //throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
    }
    
    public void updateDealer(String username, String name, String password,
            String defaultSmsc, String defaultService) throws
            Exception {
        String SQL;
        
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            SQL = "Update cp_user set name=?,password=?, default_service=?,default_smsc=? where username=?";
            
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, name);
            prepstat.setString(2, password);
            prepstat.setString(3, defaultService);
            prepstat.setString(4, defaultSmsc);
            prepstat.setString(5, username);
            
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                }
                con = null;
            }
            //throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
    }
    
    public void updateDealer(String username, String name, String defaultSmsc,
            String defaultService) throws Exception {
        String SQL;
        
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            SQL =
                    "Update cp_user set name=?, default_service=?,default_smsc=? where username=?";
            
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, name);
            prepstat.setString(2, defaultService);
            prepstat.setString(3, defaultSmsc);
            prepstat.setString(4, username);
            
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                }
                con = null;
            }
            //throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
    }
    
    public void updateDealerInfo(String username, String name, String password,
            String defaultSmsc, String defaultService,String logoUrl) throws
            Exception {
        String SQL;
        
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            SQL = "Update cp_user set name=?,password=?, default_service=?,default_smsc=?,logo_url =? where username=?";
            
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, name);
            prepstat.setString(2, password);
            prepstat.setString(3, defaultService);
            prepstat.setString(4, defaultSmsc);
            prepstat.setString(5, logoUrl);
            prepstat.setString(6, username);
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                }
                con = null;
            }
            //throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
    }
    

    public void updateDealerInfo(String username, String name, String defaultSmsc,
            String defaultService,String logoUrl) throws Exception {
        String SQL;
        
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            SQL =
                    "Update cp_user set name=?, default_service=?,default_smsc=?,logo_url =? where username=?";
            
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, name);
            prepstat.setString(2, defaultService);
            prepstat.setString(3, defaultSmsc);
            prepstat.setString(4, logoUrl);
            prepstat.setString(5, username);
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                }
                con = null;
            }
            //throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
    }
    
    public void updateDealer(String username, String language) throws Exception {
        String SQL;
        
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            SQL =
                    "Update cp_user set default_language=? where username=?";
            
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, language);
            prepstat.setString(2, username);
            
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                }
                con = null;
            }
            //throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
    }
    
    public void updateDealer(String id, double bandwidth, double inbox, double outbox) throws Exception {
        String SQL;
        
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            SQL =
                    "Update cp_user set bandwidth_balance=?, inbox_balance=?, outbox_balance=? where id=?";
            
            prepstat = con.prepareStatement(SQL);
            prepstat.setDouble(1, bandwidth);
            prepstat.setDouble(2, inbox);
            prepstat.setDouble(3, outbox);
            prepstat.setString(4, id);
            
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                }
                con = null;
            }
            //throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
    }
    
    public void changePassword(String id, String password) throws Exception {
        String SQL;
        
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            SQL = "Update dealer set password=? where id=?";
            
            prepstat = con.prepareStatement(SQL);
            
            prepstat.setString(1, password);
            prepstat.setString(2, id);
            
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                }
                con = null;
            }
            //throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
    }
    
    public void deleteDealer(String id) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            SQL = "delete from cp_user where id ='" + id + "'";
            prepstat = con.prepareStatement(SQL);
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                }
                
                con = null;
            }
            //throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
        
    }
    
    public void deleteDealer(String[] ids) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        String deleteList = "";
        for (int i = 0; i < ids.length; i++) {
            deleteList = deleteList + "'" + ids[i] + "'" +
                    ((ids.length - 1 == i) ? "" : ",");
        }
        
        deleteList = "(" + deleteList + ")";
        
        try {
            con = DConnect.getConnection("rmcs");
            SQL = "delete from cp_user where id  IN " + deleteList;
            prepstat = con.prepareStatement(SQL);
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                }
                
                con = null;
            }
            //throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
        
    }
    
    public User viewDealer(String id) throws Exception {
        User dealer = new User();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        //------log Statement
        System.out.println(new java.util.Date()+ ":@com.rancard.mobility.contentprovider.UserDB...");
        System.out.println(new java.util.Date()+ ": viewing cp_user/dealer ("+ id +")...");
        
        try {
            con = DConnect.getConnection("rmcs");
            
            SQL = "SELECT * FROM cp_user  where cp_user.id='" +
                    id + "'";
            prepstat = con.prepareStatement(SQL);
            
            rs = prepstat.executeQuery();
            java.util.ArrayList services = new java.util.ArrayList();
            while (rs.next()) {
                dealer.setId(rs.getString("id"));
                dealer.setName(rs.getString("name"));
                dealer.setPassword(rs.getString("password"));
                dealer.setUsername(rs.getString("username"));
                dealer.setDefaultSmsc(rs.getString("default_smsc"));
                dealer.setDefaultService(rs.getString("default_service"));
                dealer.setLogoUrl(rs.getString("logo_url"));
                dealer.setDefaultLanguage(rs.getString("default_language"));
                dealer.setBandwidthBalance(rs.getDouble("bandwidth_balance"));
                dealer.setInboxBalance(rs.getDouble("inbox_balance"));
                dealer.setOutboxBalance(rs.getDouble("outbox_balance"));
                
                System.out.println(new java.util.Date()+ ": cp_user ("+ id +") found!");
            }
            //log
            if(dealer.getId()==null || "".equals(dealer.getId()) ){
                System.out.println(new java.util.Date()+ ": cp_user ("+ id +") not found!");
            }
            
            if(dealer.getId() != null && !dealer.getId().equals("")){
                SQL = "SELECT * FROM cp_user_profile up inner join service_route sr on up.service_type_id=sr.service_type where up.account_id='" + id + "'";
                prepstat = con.prepareStatement(SQL);
                
                rs = prepstat.executeQuery();
                
                while (rs.next()) {
                    com.rancard.mobility.contentserver.ContentType service = new com.rancard.mobility.contentserver.ContentType();
                    service.setParentServiceType(rs.getInt("sr.parent_service_type"));
                    service.setServiceName(rs.getString("sr.service_name"));
                    service.setServiceType(rs.getInt("sr.service_type"));
                    
                    services.add(service);
                }
                dealer.setServices(services);
            }
            rs.close();
            rs = null;
            prepstat.close();
            prepstat = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            //error log
            System.out.println(new java.util.Date()+ ": error viewing cp_user ("+ id +"): " + ex.getMessage() );
            
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
        
        return dealer;
    }
    
    public User view(String username, String password) throws Exception {
        User dealer = new User();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            
            SQL = "SELECT * FROM cp_user where cp_user.username='" +
                    username + "' and cp_user.password = '" + password + "'";
            prepstat = con.prepareStatement(SQL);
            
            rs = prepstat.executeQuery();
            java.util.ArrayList services = new java.util.ArrayList();
            while (rs.next()) {
//        transactions.Device tmpDevice = new transactions.Device();
                
                // dealer.setAccountBalance(rs.getDouble("accountBalance"));
                dealer.setId(rs.getString("id"));
                dealer.setName(rs.getString("name"));
                dealer.setPassword(rs.getString("password"));
                dealer.setUsername(rs.getString("username"));
                dealer.setDefaultSmsc(rs.getString("default_smsc"));
                dealer.setDefaultService(rs.getString("default_service"));
                dealer.setLogoUrl(rs.getString("logo_url"));
                dealer.setDefaultLanguage(rs.getString("default_language"));
                dealer.setBandwidthBalance(rs.getDouble("bandwidth_balance"));
                dealer.setInboxBalance(rs.getDouble("inbox_balance"));
                dealer.setOutboxBalance(rs.getDouble("outbox_balance"));
                
                /*if (rs.getString("device_number") != null) {
                  tmpDevice.setDealerId(rs.getString("id"));
                 
                  tmpDevice.setBalance(rs.getDouble("balance"));
                  tmpDevice.setDeviceNumber(rs.getString("device_number"));
                  tmpDevice.setDeviceType(rs.getString("device_type"));
                  devicesList.add(tmpDevice);
                         }*/
            }
            
            if(dealer.getId() != null && !dealer.getId().equals("")){
                SQL = "SELECT * FROM cp_user_profile up inner join service_route sr on up.service_type_id=sr.service_type where up.account_id='" + dealer.getId() + "'";
                prepstat = con.prepareStatement(SQL);
                
                rs = prepstat.executeQuery();
                
                while (rs.next()) {
                    com.rancard.mobility.contentserver.ContentType service = new com.rancard.mobility.contentserver.ContentType();
                    service.setParentServiceType(rs.getInt("sr.parent_service_type"));
                    service.setServiceName(rs.getString("sr.service_name"));
                    service.setServiceType(rs.getInt("sr.service_type"));
                    
                    services.add(service);
                }
                dealer.setServices(services);
            }
            //dealer.setDevices(devicesList);
            rs.close();
            rs = null;
            prepstat.close();
            prepstat = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
        
        return dealer;
    }
    
    //utility method to view all dealers
    public java.util.ArrayList getDealers() throws Exception {
        java.util.ArrayList dealers = new java.util.ArrayList();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection("rmcs");
            
            SQL = "Select * from cp_user";
            prepstat = con.prepareStatement(SQL);
            
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                User dealer = new User();
                
                // dealer.setAccountBalance(rs.getDouble("accountBalance"));
                
                dealer.setId(rs.getString("id"));
                dealer.setName(rs.getString("name"));
                dealer.setPassword(rs.getString("password"));
                dealer.setUsername(rs.getString("username"));
                
                dealers.add(dealer);
            }
            rs.close();
            rs = null;
            prepstat.close();
            prepstat = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                   System.out.println(e.getMessage());
                }
                con = null;
            }
        }
        return dealers;
    }
    
    
    /*
     public boolean addToDealerCreditBalance(transactions.CreditBalanceItem item,
                                              String dealerId) {
     
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        transactions.CreditBalanceItem creditItem = null;
     
        try {
          int newQuantity = 0;
          con = new com.rancard.common.DConnect().getConnection("rmcs");
          SQL =
              "SELECT * FROM  credit_balances inner join air_time_products on credit_balances.productId = air_time_products.id  where productId =? and dealerId =? ";
          prepstat = con.prepareStatement(SQL);
          prepstat.setString(1, item.getProduct().getProductId());
          prepstat.setString(2, dealerId);
          rs = prepstat.executeQuery();
     
          if (rs.next()) {
            creditItem = new transactions.CreditBalanceItem(new Product(rs.
                getString("name"), rs.getString("id")),
                new Integer(rs.getInt("quantity")));
     
            newQuantity = (new Integer(creditItem.getQuantity().intValue() +
     item.getQuantity().intValue())).intValue();
     
            SQL =
     "UPDATE credit_balances SET quantity= ?  where productId = ?  and dealerid = ?";
//            "UPDATE credit_balances  set quantity  =? where productId=? and dealerId=? ";
            prepstat = con.prepareStatement(SQL);
            prepstat.setInt(1, newQuantity);
            prepstat.setString(2, creditItem.getProduct().getProductId());
            prepstat.setString(3, dealerId);
            prepstat.execute();
     
          }
          else {
     
     newQuantity = (new Integer(item.getQuantity().intValue())).intValue();
     
            SQL =
     " INSERT INTO credit_balances (productId,dealerId,quantity ) VALUES(?,?,?)";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, item.getProduct().getProductId());
            prepstat.setString(2, dealerId);
            prepstat.setInt(3, newQuantity);
            prepstat.execute();
     
          }
        }
        catch (Exception ex) {
          if (con != null) {
            try {
              con.close();
            }
            catch (SQLException ex1) {
            }
            con = null;
          }
     
        }
        finally {
          if (rs != null) {
            try {
              rs.close();
            }
            catch (SQLException e) {
              ;
            }
            rs = null;
          }
          if (prepstat != null) {
            try {
              prepstat.close();
            }
            catch (SQLException e) {
              ;
            }
            prepstat = null;
          }
          if (con != null) {
            try {
              con.close();
            }
            catch (SQLException e) {
              ;
            }
            con = null;
          }
        }
        return false;
      }
     
     public boolean debitDealerCreditBalance(transactions.CreditBalanceItem item,
     String dealerId) throws Exception {
     
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        transactions.CreditBalanceItem creditItem = null;
     
        try {
          int newQuantity = 0;
          con = new com.rancard.common.DConnect().getConnection("rmcs");
          SQL =
              "SELECT * FROM  credit_balances inner join air_time_products on credit_balances.productId = air_time_products.id  where productId =? and dealerId =? ";
          prepstat = con.prepareStatement(SQL);
          prepstat.setString(1, item.getProduct().getProductId());
          prepstat.setString(2, dealerId);
          rs = prepstat.executeQuery();
     
          if (rs.next()) {
            creditItem = new transactions.CreditBalanceItem(new Product(rs.
                getString("name"), rs.getString("id"),
                new Double(rs.getDouble("cash_value"))),
                     new Integer(rs.getInt("quantity")));
     
     if (creditItem.getQuantity().intValue() < item.getQuantity().intValue()) {
              throw new Exception("Insufficient credit to execute Action");
     
            }
            newQuantity = (new Integer(creditItem.getQuantity().intValue() -
     item.getQuantity().intValue())).intValue();
     
            SQL =
     "UPDATE credit_balances  set quantity  =? where productId=? and dealerId=? ";
            prepstat = con.prepareStatement(SQL);
            prepstat.setInt(1, newQuantity);
            prepstat.setString(2, creditItem.getProduct().getProductId());
            prepstat.setString(3, dealerId);
            prepstat.execute();
     
          }
          else {
     
            SQL =
     " INSERT INTO credit_balances (productId,dealerId,quantity]) VALUES(?,?,?)";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, creditItem.getProduct().getProductId());
            prepstat.setString(2, dealerId);
            prepstat.setInt(3, newQuantity);
            prepstat.execute();
     
          }
        }
        catch (Exception ex) {
          if (con != null) {
            try {
              con.close();
            }
            catch (SQLException ex1) {
            }
            con = null;
          }
     
        }
        finally {
          if (rs != null) {
            try {
              rs.close();
            }
            catch (SQLException e) {
              ;
            }
            rs = null;
          }
          if (prepstat != null) {
            try {
              prepstat.close();
            }
            catch (SQLException e) {
              ;
            }
            prepstat = null;
          }
          if (con != null) {
            try {
              con.close();
            }
            catch (SQLException e) {
              ;
            }
            con = null;
          }
        }
        return false;
      }
     */
    
    
    public static void main(String[] args) throws Exception {
        //Dealer dealer = new DealerDB().viewDealer("dealer");
        //dealer.updateDealer(dealer.getName(),dealer.getId(),dealer.getDevices(),10.0,dealer.getUsername(),dealer.getPassword());
        
        UserDB d = new UserDB();
        /*java.util.ArrayList de = d.getDealers();
         System.out.println("There are: "+de.size() + " dealers");*/
        // d.deleteDealer("test");
    }
}
