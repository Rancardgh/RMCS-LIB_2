package com.rancard.mobility.contentserver;

import java.sql.PreparedStatement;
import com.rancard.common.DConnect;
import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class RelationshipDB {

    public static void insertRelationship(String cs_id, String listId) throws
            Exception {

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query =
                    "INSERT into cs_cp_relationship(cs_id,list_id) values(?,?)";

            prepstat = con.prepareStatement(query);
            prepstat.setString(1, cs_id);
            prepstat.setString(2, listId);

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

    /*
         Deletes an entry
     */
    public static void deleteRelationshipForProvider(String id) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "DELETE from cs_cp_relationship WHERE list_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, id);
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

    public static void deleteRelationshipForContentSubscriber(String id) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "DELETE from cs_cp_relationship WHERE cs_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, id);
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

    public static void deleteRelationship(String csid, String cpid) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "DELETE from cs_cp_relationship WHERE cs_id=? and list_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, csid);
            prepstat.setString(2, cpid);
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


        public static void deleteRelationship(String[] csid, String cpid) throws
            Exception {
        Connection con = null;
        Statement prepstat = null;
        int[] aiupdateCounts = null;
        boolean bError = false;
        String SQL1, SQL2;

        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            con.createStatement();
            prepstat = con.createStatement();

            for(int i = 0; i < csid.length; i++){
                
                SQL1 = "DELETE from cs_cp_relationship WHERE cs_id='" + csid[i] +
                       "' and list_id ='" + cpid + "'";
                 bError = false;
                //prepstat.clearBatch();

                // add SQL statements
                prepstat.addBatch(SQL1);
              
            }
            // execute the statements

            aiupdateCounts = prepstat.executeBatch();
            prepstat.clearBatch();
            // end try
        }
// catch blocks
        catch (BatchUpdateException bue) {
            bError = true;
            aiupdateCounts = bue.getUpdateCounts();

            SQLException SQLe = bue;
            while (SQLe != null) {
                // do exception stuff
                SQLe = SQLe.getNextException();
            }
        } catch (SQLException SQLe) { // end BatchUpdateException catch
            //
            System.out.println("there was an error during the update");
        } finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 ||
                    iProcessed == -2
                        ) {
                    // statement was successful
                    System.out.println("Delete sucessful");
                } else {
                    // error on statement
                    bError = true;
                    break;
                }
            } // end for

            if (bError) {
                con.rollback();
            } else {
                con.commit();
            }
            if (con != null) {
                con.close();
            }

        } // end finally

    }

public static void deleteProviderRelationship(String[] cpid, String csid) throws
            Exception {
        Connection con = null;
        Statement prepstat = null;
        int[] aiupdateCounts = null;
        boolean bError = false;
        String SQL1, SQL2;

        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            con.createStatement();
            prepstat = con.createStatement();

            for(int i = 0; i < cpid.length; i++){
                
                SQL1 = "DELETE from cs_cp_relationship WHERE list_id='" + cpid[i] +
                       "' and cs_id ='" + csid + "'";
                 bError = false;
                //prepstat.clearBatch();

                // add SQL statements
                prepstat.addBatch(SQL1);
              
            }
            // execute the statements

            aiupdateCounts = prepstat.executeBatch();
            prepstat.clearBatch();
            // end try
        }
// catch blocks
        catch (BatchUpdateException bue) {
            bError = true;
            aiupdateCounts = bue.getUpdateCounts();

            SQLException SQLe = bue;
            while (SQLe != null) {
                // do exception stuff
                SQLe = SQLe.getNextException();
            }
        } catch (SQLException SQLe) { // end BatchUpdateException catch
            //
            System.out.println("there was an error during the update");
        } finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 ||
                    iProcessed == -2
                        ) {
                    // statement was successful
                    System.out.println("Delete sucessful");
                } else {
                    // error on statement
                    bError = true;
                    break;
                }
            } // end for

            if (bError) {
                con.rollback();
            } else {
                con.commit();
            }
            if (con != null) {
                con.close();
            }

        } // end finally

    }
 
    
    /*
         Retrieves an item as a bean
     */
    public static ArrayList viewProvidersForContentSubscriber(String id) throws
            Exception {
        ArrayList providers = new ArrayList();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "SELECT * from cs_cp_relationship   WHERE cs_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, id);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                  providers.add(rs.getString("list_id"));
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
        return providers;
    }

    public static ArrayList viewProvidersDetailsForContentSubscriber(String id) throws
            Exception {
        ArrayList providers = new ArrayList();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "SELECT * from cs_cp_relationship  csr inner join cp_user cpu on csr.list_id = cpu.id  WHERE cs_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, id);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                com.rancard.mobility.contentprovider.User cp = new  com.rancard.mobility.contentprovider.User();
              
                cp.setId(rs.getString("id"));
                cp.setName(rs.getString("name"));
                cp.setLogoUrl(rs.getString("logo_url"));
                cp.setUsername(rs.getString("username"));
                 providers.add(cp);
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
        return providers;
    }
    public static ArrayList viewContentSubscribersForProvider(String id) throws
            Exception {
        ArrayList providers = new ArrayList();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "SELECT * from cs_cp_relationship WHERE list_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, id);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                providers.add(rs.getString("cs_id"));
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
        return providers;
    }


  public static ArrayList viewContentSubscriberDetailsForProvider(String id) throws
            Exception {
        ArrayList providers = new ArrayList();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "SELECT * from cs_cp_relationship  csr inner join cp_user cpu on csr.cs_id = cpu.id  WHERE list_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, id);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                com.rancard.mobility.contentprovider.User cp = new  com.rancard.mobility.contentprovider.User();
              
                cp.setId(rs.getString("id"));
                cp.setName(rs.getString("name"));
                cp.setLogoUrl(rs.getString("logo_url"));
                cp.setUsername(rs.getString("username"));
                 providers.add(cp);
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
        return providers;
    }

}
