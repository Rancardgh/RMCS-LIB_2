package com.rancard.mobility.contentserver;

import java.sql.*;
import java.util.*;

import com.rancard.common.*;
import com.rancard.util.*;
import java.io.File;


public class ContentListDB {
    
//constructor
    public ContentListDB() {}
    
    public void loadPreviewFiles(java.util.ArrayList fileRefrences, String listId, String directoryPath) throws
            Exception, java.io.FileNotFoundException {
        
        String SQL;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        // check that directory is valid
        java.io.File contentDir = new File(directoryPath);
        // if  not valid throw exception
        if (!contentDir.exists()) {
            throw new java.io.FileNotFoundException(
                    "Unable to find work directory . Please set work directory in properties file");
        }
        // else update
        
        int[] aiupdateCounts = null;
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            
            SQL = "update  uploads  set previewfile = ?, preview_exists =1 where id=? and list_id = ?";
            
            prepstat = con.prepareStatement(SQL);
            prepstat.clearBatch();
            for (Iterator iter = fileRefrences.iterator(); iter.hasNext(); ) {
                String id = (String) iter.next();
                
                java.io.File blob = new File(contentDir.getAbsolutePath() +
                        File.separator + id);
                java.io.BufferedInputStream bis = new java.io.
                        BufferedInputStream(new java.
                        io.FileInputStream(blob));
                
                byte[] buff = new byte[new Long(blob.length()).intValue()];
                int bytesRead = 0;
                
                // Simple read loop.
                while ( -1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                }
                //prepstat.setString(1, contentDir.getAbsolutePath() + File.separator + id);
                prepstat.setBytes(1, buff);
                prepstat.setString(2, id);
                prepstat.setString(3, listId);
                prepstat.addBatch();
                
                blob.delete();
            }
            aiupdateCounts = prepstat.executeBatch();
            
            SQL =
                    "update content_list set preview_exists=1 where id =? and list_id =? ";
            
            prepstat = con.prepareStatement(SQL);
            for (Iterator iter = fileRefrences.iterator(); iter.hasNext(); ) {
                String id = (String) iter.next();
                prepstat.setString(1, id);
                prepstat.setString(2, listId);
                prepstat.addBatch();
            }
            // execute the statements
            aiupdateCounts = prepstat.executeBatch();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        
        finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 ||
                        iProcessed == -2
                        ) {
                    // statement was successful
                    System.out.println("Update Sucessful");
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
        }
    }
    
    
//insert record
    public void createcontent_list(java.lang.String id, java.lang.String title, java.lang.Integer type, java.lang.String download_url,
            java.lang.String preview_url, java.lang.Long size, java.lang.Integer formats, java.lang.Long price, java.lang.String list_id,
            java.lang.Integer category, java.sql.Timestamp date_added, java.lang.String other_details, boolean isLocal, boolean canList,
            String author, boolean isFree, String supplierId, String shortItemRef) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "insert into content_list(id,title,content_type,download_url,preview_url,size,formats,price,list_id,category,date_added,other_details," +
                    "author,isLocal,show,is_free,supplier_id,short_item_ref) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            prepstat = con.prepareStatement(SQL);
            
            prepstat.setString(1, id);
            prepstat.setString(2, title);
            prepstat.setInt(3, type.intValue());
            prepstat.setString(4, download_url);
            prepstat.setString(5, preview_url);
            prepstat.setInt(6, size.intValue());
            prepstat.setInt(7, formats.intValue());
            prepstat.setString(8, price.toString());
            prepstat.setString(9, list_id);
            prepstat.setInt(10, category.intValue());
            prepstat.setTimestamp(11, date_added);
            prepstat.setString(12, other_details);
            prepstat.setString(13, author);
            if (isLocal == true) {
                prepstat.setInt(14, 1);
            } else {
                prepstat.setInt(14, 0);
            }
            if (canList == true) {
                prepstat.setInt(15, 1);
            } else {
                prepstat.setInt(15, 0);
            }
            if (isFree == true) {
                prepstat.setInt(16, 1);
            } else {
                prepstat.setInt(16, 0);
            }
            prepstat.setString(17, supplierId);
            prepstat.setString(18, shortItemRef);
            
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
    
    public void updateContentList (java.lang.String[] id, java.lang.Integer category, java.lang.String listId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        int[] aiupdateCounts = null;
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            
            SQL =
                    "update content_list set category=?  where id=? and list_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.clearBatch();
            for (int i = 0; i < id.length; i++) {
                
                prepstat.setInt(1, category.intValue());
                prepstat.setString(2, id[i]);
                prepstat.setString(3, listId);
                prepstat.addBatch();
            }
            // execute the statements
            aiupdateCounts = prepstat.executeBatch();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        
        finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 ||
                        iProcessed == -2
                        ) {
                    // statement was successful
                    System.out.println("Update Sucessful");
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
        }
        
        if (con != null) {
            con.close();
        }
        
    }
    
    public void updateContentList (java.lang.String[] id, java.lang.String price, java.lang.String listId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        int[] aiupdateCounts = null;
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            
            SQL = "update content_list set price=?  where id=? and list_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.clearBatch();
            for (int i = 0; i < id.length; i++) {
                
                prepstat.setString(1, price);
                prepstat.setString(2, id[i]);
                prepstat.setString(3, listId);
                prepstat.addBatch();
            }
            // execute the statements
            aiupdateCounts = prepstat.executeBatch();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        
        finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 ||
                        iProcessed == -2
                        ) {
                    // statement was successful
                    System.out.println("Update Sucessful");
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
        }
        
        if (con != null) {
            con.close();
        }
        
    }
    
    public void updateContentList (java.lang.String[] id, java.lang.String[] price, java.lang.String listId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        String string = "";
        if(price != null || price.length > 0){
            string = price[0] + ",";
            for(int i = 1; i < price.length; i++){
                string = string + price[i] + ",";
            }
        }
        string = string.substring (0, string.lastIndexOf (","));
        
        int[] aiupdateCounts = null;
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            
            SQL = "update content_list set price=?  where id=? and list_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.clearBatch();
            for (int i = 0; i < id.length; i++) {
                
                prepstat.setString(1, string);
                prepstat.setString(2, id[i]);
                prepstat.setString(3, listId);
                prepstat.addBatch();
            }
            // execute the statements
            aiupdateCounts = prepstat.executeBatch();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        
        finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 ||
                        iProcessed == -2
                        ) {
                    // statement was successful
                    System.out.println("Update Sucessful");
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
        }
        
        if (con != null) {
            con.close();
        }
        
    }
    
    public void updateContentListAuthor(java.lang.String[] id, java.lang.String author, java.lang.String listId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        int[] aiupdateCounts = null;
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            //
            
            
            SQL =
                    "update content_list set author=?  where id=? and list_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.clearBatch();
            for (int i = 0; i < id.length; i++) {
                
                prepstat.setString(1, author);
                prepstat.setString(2, id[i]);
                prepstat.setString(3, listId);
                prepstat.addBatch();
            }
            // execute the statements
            aiupdateCounts = prepstat.executeBatch();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        
        finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 ||
                        iProcessed == -2
                        ) {
                    // statement was successful
                    System.out.println("Update Sucessful");
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
        }
        
        if (con != null) {
            con.close();
        }
        
    }
    
    public void updateContentListFreebie(java.lang.String[] id, boolean isFree, java.lang.String listId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        int[] aiupdateCounts = null;
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            
            SQL = "update content_list set is_free=?  where id=? and list_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.clearBatch();
            for (int i = 0; i < id.length; i++) {
                if(isFree == true){
                    prepstat.setInt(1, 1);
                }else{
                    prepstat.setInt(1, 0);
                }
                prepstat.setString(2, id[i]);
                prepstat.setString(3, listId);
                prepstat.addBatch();
            }
            // execute the statements
            aiupdateCounts = prepstat.executeBatch();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        
        finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 ||
                        iProcessed == -2
                        ) {
                    // statement was successful
                    System.out.println("Update Sucessful");
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
        }
        
        if (con != null) {
            con.close();
        }
        
    }
    
    public void updateShortItemReference(String[] id, String listId, String shortItemRef) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        int[] aiupdateCounts = null;
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            
            SQL = "update content_list set short_item_ref=? where id=? and list_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.clearBatch();
            for (int i = 0; i < id.length; i++) {
                prepstat.setString(1, shortItemRef);
                prepstat.setString(2, id[i]);
                prepstat.setString(3, listId);
                prepstat.addBatch();
            }
            // execute the statements
            aiupdateCounts = prepstat.executeBatch();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        
        finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 ||
                        iProcessed == -2
                        ) {
                    // statement was successful
                    System.out.println("Update Sucessful");
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
        }
        
        if (con != null) {
            con.close();
        }
        
    }
    
    public void updateContentList(java.lang.String[] id, java.lang.String price, java.lang.Integer category, java.lang.String author,
            java.lang.String listId, boolean isFree) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        int[] aiupdateCounts = null;
        try {
            
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            
            SQL =
                    "update content_list set price=?,category=?,author=? where id=? and list_id=?";
            
            prepstat = con.prepareStatement(SQL);
            prepstat.clearBatch();
            for (int i = 0; i < id.length; i++) {
                
                prepstat.setString(1, price);
                prepstat.setInt(2, category.intValue());
                prepstat.setString(3, author);
                prepstat.setString(4, id[i]);
                prepstat.setString(5, listId);
                prepstat.addBatch();
            }
            // execute the statements
            aiupdateCounts = prepstat.executeBatch();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        
        finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 ||
                        iProcessed == -2
                        ) {
                    // statement was successful
                    System.out.println("Update Sucessful");
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
        }
        
        if (con != null) {
            con.close();
        }
        
    }
    
    public void updateContentList(java.lang.String[] id, java.lang.String price, java.lang.Integer category, java.lang.String author,
            java.lang.String listId, boolean isFree, String shortItemRef) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        int[] aiupdateCounts = null;
        try {
            
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            
            SQL =
                    "update content_list set price=?,category=?,author=?,short_item_ref=? where id=? and list_id=?";
            
            prepstat = con.prepareStatement(SQL);
            prepstat.clearBatch();
            for (int i = 0; i < id.length; i++) {
                
                prepstat.setString(1, price);
                prepstat.setInt(2, category.intValue());
                prepstat.setString(3, author);
                prepstat.setString(4, shortItemRef);
                prepstat.setString(5, id[i]);
                prepstat.setString(6, listId);
                prepstat.addBatch();
            }
            // execute the statements
            aiupdateCounts = prepstat.executeBatch();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        
        finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 ||
                        iProcessed == -2
                        ) {
                    // statement was successful
                    System.out.println("Update Sucessful");
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
        }
        
        if (con != null) {
            con.close();
        }
        
    }
    
    public void updateContentItem(java.lang.String id, java.lang.String price, String title, java.lang.Integer category, java.lang.String author,
            boolean isFree, String shortItemRef, java.lang.String listId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            
            con = DConnect.getConnection();
            //              con.setAutoCommit(false);
            SQL = "update content_list set price=?,category=?,author=?,title =?, is_free=?,short_item_ref=? where id=? and list_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, price);
            prepstat.setInt(2, category.intValue());
            prepstat.setString(3, author);
            prepstat.setString(4, title);
            if(isFree){
                prepstat.setInt(5, 1);
            }else{
                prepstat.setInt(5, 0);
            }
            prepstat.setString(6, shortItemRef);
            prepstat.setString(7, id);
            prepstat.setString(8, listId);
            // execute the statements
            prepstat.executeUpdate();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            
            if (con != null) {
                con.close();
            }
        }
    }
    
    public void updateContentItem(java.lang.String id, java.lang.String price, java.lang.Integer category, java.lang.String author,
            java.lang.String listId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            
            con = DConnect.getConnection();
            //              con.setAutoCommit(false);
            SQL =
                    "update content_list set price=?,category=?,author=?  where id=? and list_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, price);
            prepstat.setInt(2, category.intValue());
            prepstat.setString(3, author);
            prepstat.setString(4, id);
            prepstat.setString(5, listId);
            // execute the statements
            prepstat.executeUpdate();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            
            if (con != null) {
                con.close();
            }
        }
    }
    
    public void updatecontent_list(java.lang.String id, java.lang.String title, java.lang.Integer type, java.lang.String download_url,
            java.lang.String preview_url, java.lang.Integer size, java.lang.Integer formats, java.lang.Long price, java.lang.Integer category,
            java.sql.Timestamp date_added, java.lang.String other_details, java.lang.String author, boolean isLocal, boolean canList,
            java.lang.String list_id) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            
            SQL = "update content_list set title=?,content_type=?,download_url=?,preview_url=?,size=?,formats=?,price=?,category=?,date_added=?,other_details=?,author=?,id=?,list_id=?,isLocal=? and show?";
            prepstat = con.prepareStatement(SQL);
            
            prepstat.setString(1, title);
            prepstat.setInt(2, type.intValue());
            prepstat.setString(3, download_url);
            prepstat.setString(4, preview_url);
            prepstat.setInt(5, size.intValue());
            prepstat.setInt(6, formats.intValue());
            prepstat.setString(7, price.toString());
            prepstat.setInt(8, category.intValue());
            prepstat.setTimestamp(9, date_added);
            prepstat.setString(10, other_details);
            prepstat.setString(11, author);
            prepstat.setString(12, id);
            prepstat.setString(13, list_id);
            if (isLocal == true) {
                prepstat.setInt(14, 1);
            } else {
                prepstat.setInt(14, 0);
            }
            if (canList == true) {
                prepstat.setInt(15, 1);
            } else {
                prepstat.setInt(15, 0);
            }
            
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
    
    public void deleteContentList(java.lang.String list_id) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "delete  from content_list where list_id=?";
            prepstat = con.prepareStatement(SQL);
            
            prepstat.setString(1, list_id);
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
    
    public void deleteContentListItem(String Id, String listId) throws Exception {
        
        String SQL1, SQL2;
        Connection con = null;
        Statement prepstat = null;
        int[] aiupdateCounts = null;
        boolean bError = false;
        
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            
            SQL1 = "delete  from content_list where id='" + Id +
                    "' and list_id ='" + listId + "'";
            SQL2 = "delete from uploads where id='" + Id + "' and list_id  ='" +
                    listId + "'";
            
            con.createStatement();
            prepstat = con.createStatement();
            
            bError = false;
            prepstat.clearBatch();
            
            // add SQL statements
            prepstat.addBatch(SQL1);
            prepstat.addBatch(SQL2);
            // execute the statements
            
            aiupdateCounts = prepstat.executeBatch();
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
    
    public void deleteContentListItem(String[] Id, String listId) throws Exception {
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
            
            for(int i = 0; i < Id.length; i++){
                SQL1 = "delete  from content_list where id='" + Id[i] +
                        "' and list_id ='" + listId + "'";
                SQL2 = "delete from uploads where id='" + Id[i] + "' and list_id  ='" +
                        listId + "'";
                bError = false;
                //prepstat.clearBatch();
                
                // add SQL statements
                prepstat.addBatch(SQL1);
                prepstat.addBatch(SQL2);
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
    
    public void deleteContentListTmp(java.lang.String list_id) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "delete  from content_list_tmp where list_id=?";
            prepstat = con.prepareStatement(SQL);
            
            prepstat.setString(1, list_id);
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
    
    public ContentItem viewcontent_list(java.lang.String id, java.lang.String list_id) throws Exception {
        
        ContentItem item = new ContentItem();
        Format format = new Format();
        ContentType type = new ContentType();
        com.rancard.mobility.contentprovider.User cp =
                new com.rancard.mobility.contentprovider.User();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            
            //SQL = "select * from  content_list where id=? and list_id=?";
            SQL = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id=? and content_list.list_id=? and " +
                    "content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id";
            
            prepstat = con.prepareStatement(SQL);
            
            prepstat.setString(1, id);
            prepstat.setString(2, list_id);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                //content item
                item.setContentId(rs.getString("content_id"));
                item.setid(rs.getString("id"));
                item.settitle(rs.getString("title"));
                item.settype(new Integer(rs.getInt("content_type")));
                item.setdownload_url(rs.getString("download_url"));
                item.setPreviewUrl(rs.getString("preview_url"));
                //item.setformats(rs.getString("formats"));
                item.setPrice(rs.getString("price"));
                item.setAuthor(rs.getString("author"));
                item.setCategory(new Integer(rs.getInt("category")));
                item.setSize(new Long(rs.getLong("size")));
                item.setListId(rs.getString("list_id"));
                item.setDate_Added(rs.getTimestamp("date_added"));
                item.setOther_Details(rs.getString("other_details"));
                if (rs.getInt("isLocal") == 1) {
                    item.setIsLocal(true);
                } else {
                    item.setIsLocal(false);
                }
                if (rs.getInt("show") == 1) {
                    item.setCanList(true);
                } else {
                    item.setCanList(false);
                }
                if (rs.getInt("is_free") == 1) {
                    item.setFree(true);
                } else {
                    item.setFree(false);
                }
                if (rs.getInt("preview_exists") == 1) {
                    item.setPreviewExists (true);
                } else {
                    item.setPreviewExists(false);
                }
                
                //content type
                type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
                type.setServiceName(rs.getString("service_route.service_name"));
                type.setServiceType(rs.getInt("service_route.service_type"));
                
                //content provider
                cp.setId(rs.getString("cp_user.id"));
                cp.setName(rs.getString("cp_user.name"));
                cp.setUsername(rs.getString("cp_user.username"));
                cp.setPassword(rs.getString("cp_user.password"));
                cp.setDefaultSmsc(rs.getString("cp_user.default_smsc"));
                cp.setLogoUrl(rs.getString("cp_user.logo_url"));
                cp.setDefaultService(rs.getString("cp_user.default_service"));
                
                //format
                format.setId(rs.getInt("format_list.format_id"));
                format.setFileExt(rs.getString("format_list.file_ext"));
                format.setMimeType(rs.getString("format_list.mime_type"));
                format.setPushBearer(rs.getString("format_list.push_bearer"));
            }
            item.setContentTypeDetails(type);
            item.setProviderDetails(cp);
            item.setFormat(format);
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return item;
    }
    
    public ContentItem viewcontent_list(String contentId) throws Exception {
        
        ContentItem item = new ContentItem();
        ContentType type = new ContentType();
        Format format = new Format();
        com.rancard.mobility.contentprovider.User cp =
                new com.rancard.mobility.contentprovider.User();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            
            SQL = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.content_id=? and " +
                    "content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and " +
                    "content_list.formats=format_list.format_id";
            
            prepstat = con.prepareStatement(SQL);
            
            prepstat.setString(1, contentId);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                item.setContentId(rs.getString("content_id"));
                item.setid(rs.getString("id"));
                item.settitle(rs.getString("title"));
                item.settype(new Integer(rs.getInt("content_type")));
                item.setdownload_url(rs.getString("download_url"));
                item.setPreviewUrl(rs.getString("preview_url"));
                //item.setformats(rs.getString("formats"));
                item.setPrice(rs.getString("price"));
                item.setAuthor(rs.getString("author"));
                item.setCategory(new Integer(rs.getInt("category")));
                item.setSize(new Long(rs.getLong("size")));
                item.setListId(rs.getString("list_id"));
                item.setDate_Added(rs.getTimestamp("date_added"));
                item.setOther_Details(rs.getString("other_details"));
                if (rs.getInt("isLocal") == 1) {
                    item.setIsLocal(true);
                } else {
                    item.setIsLocal(false);
                }
                if (rs.getInt("show") == 1) {
                    item.setCanList(true);
                } else {
                    item.setCanList(false);
                }
                if (rs.getInt("is_free") == 1) {
                    item.setFree(true);
                } else {
                    item.setFree(false);
                }
                
                //content type
                type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
                type.setServiceName(rs.getString("service_route.service_name"));
                type.setServiceType(rs.getInt("service_route.service_type"));
                
                //content provider
                cp.setId(rs.getString("cp_user.id"));
                cp.setName(rs.getString("cp_user.name"));
                cp.setUsername(rs.getString("cp_user.username"));
                cp.setPassword(rs.getString("cp_user.password"));
                cp.setDefaultSmsc(rs.getString("cp_user.default_smsc"));
                cp.setLogoUrl(rs.getString("cp_user.logo_url"));
                cp.setDefaultService(rs.getString("cp_user.default_service"));
                
                //format
                format.setId(rs.getInt("format_list.format_id"));
                format.setFileExt(rs.getString("format_list.file_ext"));
                format.setMimeType(rs.getString("format_list.mime_type"));
                format.setPushBearer(rs.getString("format_list.push_bearer"));
                
            }
            item.setContentTypeDetails(type);
            item.setProviderDetails(cp);
            item.setFormat(format);
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return item;
    }
    
    public void ExportContentListToTempTable(String listId) throws Exception {
        /**
         * @todo
         * add the neccessary parameters (if any)
         **/
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "select * from content_list where list_id=? ";
            prepstat = con.prepareStatement(SQL); /**
             * @todo If the query had parameters set the PreparedStatement's parameters
             *
             **/
            prepstat.setString(1, listId);
            rs = prepstat.executeQuery();
            
            SQL = "insert into content_list_tmp(id,content_id,title,content_type,download_url,preview_url,size,formats,price,list_id,category,date_added,other_details,author) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            prepstat = con.prepareStatement(SQL);
            
            while (rs.next()) {
                prepstat.setString(1, rs.getString("id"));
                prepstat.setString(2, rs.getString("content_id"));
                prepstat.setString(3, rs.getString("title"));
                prepstat.setInt(4, rs.getInt(4));
                prepstat.setString(5, rs.getString("download_url"));
                prepstat.setString(6, rs.getString(6));
                prepstat.setLong(7, rs.getLong(7));
                prepstat.setString(8, rs.getString(8));
                prepstat.setString(9, rs.getString(9));
                prepstat.setString(10, rs.getString(10));
                prepstat.setInt(11, rs.getInt(11));
                prepstat.setTimestamp(12, rs.getTimestamp(12));
                prepstat.setString(13, rs.getString(13));
                prepstat.setString(14, rs.getString(14));
                
                prepstat.addBatch();
            }
            prepstat.executeBatch();
            
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
    
    public void ImportContentListFromTempTable(String listId) throws Exception {
        /**
         * @todo
         * add the neccessary parameters (if any)
         **/
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            SQL = "select * from content_list_tmp where list_id=? ";
            prepstat = con.prepareStatement(SQL); /**
             * @todo If the query had parameters set the PreparedStatement's parameters
             *
             **/
            prepstat.setString(1, listId);
            rs = prepstat.executeQuery();
            
            SQL = "insert into content_list(id,content_id,title,content_type,download_url,preview_url,size,formats,price,list_id,category,date_added,other_details,author) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            prepstat = con.prepareStatement(SQL);
            
            while (rs.next()) {
                prepstat.setString(1, rs.getString("id"));
                prepstat.setString(2, rs.getString("content_id"));
                prepstat.setString(3, rs.getString("title"));
                prepstat.setInt(4, rs.getInt(4));
                prepstat.setString(5, rs.getString("download_url"));
                prepstat.setString(6, rs.getString(6));
                prepstat.setLong(7, rs.getLong(7));
                prepstat.setString(8, rs.getString(8));
                prepstat.setString(9, rs.getString(9));
                prepstat.setString(10, rs.getString(10));
                prepstat.setInt(11, rs.getInt(11));
                prepstat.setTimestamp(12, rs.getTimestamp(12));
                prepstat.setString(13, rs.getString(13));
                prepstat.setString(14, rs.getString(14));
                
                prepstat.addBatch();
            }
            prepstat.executeBatch();
            
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
    
    public java.util.ArrayList viewContentList(java.lang.String[] id, java.lang.String listId) throws Exception {
        
        String arrId = "";
        for (int i = 0; i < id.length - 1; i++) {
            arrId = arrId + id[i] + ",";
        }
        arrId = arrId + id[id.length - 1];
        java.util.ArrayList items = new java.util.ArrayList();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            
            //SQL = "select * from  content_list where id=? and list_id=?";
            SQL =
                    "SELECT * FROM content_list, service_route, cp_user, format_list where " +
                    "content_list.id in (" + arrId + ") and content_list.list_id=? and " +
                    "content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id " +
                    "and content_list.formats=format_list.format_id order by date_added desc";
            prepstat = con.prepareStatement(SQL);
            
            prepstat.setString(1, listId);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                
                ContentItem item = new ContentItem();
                ContentType type = new ContentType();
                Format format = new Format();
                com.rancard.mobility.contentprovider.User cp =
                        new com.rancard.mobility.contentprovider.User();
                
                //content item
                item.setContentId(rs.getString("content_id"));
                item.setid(rs.getString("id"));
                item.settitle(rs.getString("title"));
                item.settype(new Integer(rs.getInt("content_type")));
                item.setdownload_url(rs.getString("download_url"));
                item.setPreviewUrl(rs.getString("preview_url"));
//          item.setFormat(rs.getString("formats"));
                item.setPrice(rs.getString("price"));
                item.setAuthor(rs.getString("author"));
                item.setCategory(new Integer(rs.getInt("category")));
                item.setSize(new Long(rs.getLong("size")));
                item.setListId(rs.getString("list_id"));
                item.setDate_Added(rs.getTimestamp("date_added"));
                item.setOther_Details(rs.getString("other_details"));
                if (rs.getInt("preview_exists") == 1) {
                    item.setPreviewExists(new Boolean(true));
                } else {
                    item.setPreviewExists(new Boolean(false));
                }
                if (rs.getInt("is_free") == 1) {
                    item.setFree(true);
                } else {
                    item.setFree(false);
                }
                item.setShortItemRef (rs.getString ("short_item_ref"));
                item.setSupplierId (rs.getString ("supplier_id"));
                
                //content type
                type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
                type.setServiceName(rs.getString("service_route.service_name"));
                type.setServiceType(rs.getInt("service_route.service_type"));
                
                //content provider
                cp.setId(rs.getString("id"));
                cp.setName(rs.getString("name"));
                cp.setUsername(rs.getString("username"));
                cp.setPassword(rs.getString("password"));
                cp.setDefaultSmsc(rs.getString("default_smsc"));
                cp.setLogoUrl(rs.getString("logo_url"));
                cp.setDefaultService(rs.getString("default_service"));
                /*  if (rs.getInt("isLocal") == 1) {
                      cp.setIsLocal(true);
                  } else {
                      cp.setIsLocal(false);
                  }*/
                
                //format
                format.setId(rs.getInt("format_list.format_id"));
                format.setFileExt(rs.getString("format_list.file_ext"));
                format.setMimeType(rs.getString("format_list.mime_type"));
                format.setPushBearer(rs.getString("format_list.push_bearer"));
                
                item.setContentTypeDetails(type);
                item.setProviderDetails(cp);
                item.setFormat(format);
                items.add(item);
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
        return items;
    }
    
    public java.util.ArrayList viewContentList(java.lang.String[] id, java.lang.String[] listId) throws Exception {
        
        java.util.ArrayList items = new java.util.ArrayList();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            
            for(int i = 0; i < id.length; i++){
                //SQL = "select * from  content_list where id=? and list_id=?";
                SQL = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id=? and content_list.list_id=? and " +
                        "content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id " +
                        " order by date_added desc";
                prepstat = con.prepareStatement(SQL);
                
                prepstat.setString(1, id[i]);
                prepstat.setString(2, listId[i]);
                rs = prepstat.executeQuery();
                while (rs.next()) {
                    
                    ContentItem item = new ContentItem();
                    ContentType type = new ContentType();
                    Format format = new Format();
                    com.rancard.mobility.contentprovider.User cp =
                            new com.rancard.mobility.contentprovider.User();
                    
                    //content item
                    item.setContentId(rs.getString("content_id"));
                    item.setid(rs.getString("id"));
                    item.settitle(rs.getString("title"));
                    item.settype(new Integer(rs.getInt("content_type")));
                    item.setdownload_url(rs.getString("download_url"));
                    item.setPreviewUrl(rs.getString("preview_url"));
//          item.setFormat(rs.getString("formats"));
                    item.setPrice(rs.getString("price"));
                    item.setAuthor(rs.getString("author"));
                    item.setCategory(new Integer(rs.getInt("category")));
                    item.setSize(new Long(rs.getLong("size")));
                    item.setListId(rs.getString("list_id"));
                    item.setDate_Added(rs.getTimestamp("date_added"));
                    item.setOther_Details(rs.getString("other_details"));
                    if (rs.getInt("preview_exists") == 1) {
                        item.setPreviewExists(new Boolean(true));
                    } else {
                        item.setPreviewExists(new Boolean(false));
                    }
                    if (rs.getInt("is_free") == 1) {
                        item.setFree(true);
                    } else {
                        item.setFree(false);
                    }
                    item.setShortItemRef (rs.getString ("short_item_ref"));
                    item.setSupplierId (rs.getString ("supplier_id"));
                
                    //content type
                    type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
                    type.setServiceName(rs.getString("service_route.service_name"));
                    type.setServiceType(rs.getInt("service_route.service_type"));
                    
                    //content provider
                    cp.setId(rs.getString("id"));
                    cp.setName(rs.getString("name"));
                    cp.setUsername(rs.getString("username"));
                    cp.setPassword(rs.getString("password"));
                    cp.setDefaultSmsc(rs.getString("default_smsc"));
                    cp.setLogoUrl(rs.getString("logo_url"));
                    cp.setDefaultService(rs.getString("default_service"));
                /*  if (rs.getInt("isLocal") == 1) {
                      cp.setIsLocal(true);
                  } else {
                      cp.setIsLocal(false);
                  }*/
                    
                    //format
                    format.setId(rs.getInt("format_list.format_id"));
                    format.setFileExt(rs.getString("format_list.file_ext"));
                    format.setMimeType(rs.getString("format_list.mime_type"));
                    format.setPushBearer(rs.getString("format_list.push_bearer"));
                    
                    item.setContentTypeDetails(type);
                    item.setProviderDetails(cp);
                    item.setFormat(format);
                    items.add(item);
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
        return items;
    }
    
    public com.rancard.util.Page viewContentList(String listId, String title, String formatId, Integer type, int start,
            int count) throws Exception {
        
        //@todo
        //add the neccessary parameters (if any)
        
        boolean hasNext = false;
        java.util.List logList = new java.util.ArrayList();
        Page ret = null;
        int y = 0;
        int i = 0;
        int numResults = 0;
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        SQL =
                "select * from content_list, service_route, cp_user, format_list where list_id=? ";
        if (!"*".equals(title)) {
            SQL = SQL + " AND title = '" + title + "'";
        }
        if (!"*".equals(formatId)) {
            SQL = SQL + " AND format = '" + formatId + "'";
        }
        if (!"*".equals(type)) {
            SQL = SQL + " AND content_type = '" + type + "'";
        }
        SQL = SQL + " and content_list.content_type=service_route.service_type and " +
                "content_list.list_id=cp_user.id and content_list.formats=format_list.format_id " +
                "order by content_list.date_added desc";
        
        try {
            con = DConnect.getConnection();
            
            prepstat = con.prepareStatement(SQL);
            //* @todo If the query had parameters set the PreparedStatement's parameters
            
            prepstat.setString(1, listId);
            rs = prepstat.executeQuery();
            // get the total number of records
            rs.last();
            numResults = rs.getRow();
            rs.beforeFirst();
            // end of code
            
            while (i < (start + count) && rs.next()) {
                if (i == 0) {
                    int x = numResults;
                    y = x / count;
                    if ((x % count) > 0) {
                        y += 1;
                    }
                }
                if (i >= start) {
                    ContentItem content_list = new ContentItem();
                    ContentType typeBean = new ContentType();
                    Format format = new Format();
                    com.rancard.mobility.contentprovider.User cp =
                            new com.rancard.mobility.contentprovider.User();
                    
                    content_list.setContentId(rs.getString("content_id"));
                    content_list.setid(rs.getString("id"));
                    content_list.settitle(rs.getString("title"));
                    content_list.settype(new Integer(rs.getInt("content_type")));
                    content_list.setdownload_url(rs.getString("download_url"));
                    content_list.setPreviewUrl(rs.getString("preview_url"));
                    content_list.setSize(new Long(rs.getLong("size")));
                    //content_list.setformats(rs.getString("formats"));
                    content_list.setPrice(rs.getString("price"));
                    content_list.setOther_Details(rs.getString("other_details"));
                    content_list.setDate_Added(rs.getTimestamp("date_added"));
                    content_list.setAuthor(rs.getString("author"));
                    content_list.setCategory(new Integer(rs.getInt("category")));
                    content_list.setListId(rs.getString("list_id"));
                    if (rs.getInt("isLocal") == 1) {
                        content_list.setIsLocal(true);
                    } else {
                        content_list.setIsLocal(false);
                    }
                    if (rs.getInt("show") == 1) {
                        content_list.setCanList(true);
                    } else {
                        content_list.setCanList(false);
                    }
                    if (rs.getInt("is_free") == 1) {
                        content_list.setFree(true);
                    } else {
                        content_list.setFree(false);
                    }
                    content_list.setShortItemRef (rs.getString ("short_item_ref"));
                    content_list.setSupplierId (rs.getString ("supplier_id"));
                    
                    //content type
                    typeBean.setParentServiceType(rs.getInt("service_route.parent_service_type"));
                    typeBean.setServiceName(rs.getString("service_route.service_name"));
                    typeBean.setServiceType(rs.getInt("service_route.service_type"));
                    
                    //content provider
                    cp.setId(rs.getString("cp_user.id"));
                    cp.setName(rs.getString("cp_user.name"));
                    cp.setUsername(rs.getString("cp_user.username"));
                    cp.setPassword(rs.getString("cp_user.password"));
                    cp.setDefaultSmsc(rs.getString("cp_user.default_smsc"));
                    cp.setLogoUrl(rs.getString("cp_user.logo_url"));
                    cp.setDefaultService(rs.getString("cp_user.default_service"));
                    
                    //format
                    format.setId(rs.getInt("format_list.format_id"));
                    format.setFileExt(rs.getString("format_list.file_ext"));
                    format.setMimeType(rs.getString("format_list.mime_type"));
                    format.setPushBearer(rs.getString("format_list.push_bearer"));
                    
                    content_list.setContentTypeDetails(typeBean);
                    content_list.setProviderDetails(cp);
                    content_list.setFormat(format);
                    
                    logList.add(content_list);
                }
                i++;
                
            }
            hasNext = rs.next();
            ret = new Page(logList, start, hasNext, y, numResults);
            
            if (ret == null) {
                ret = com.rancard.util.Page.EMPTY_PAGE;
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
        return ret;
    }
    
    public com.rancard.util.Page searchContentList(String listId, String title, String formats, Integer type,String author, String price,
            Integer category, int isFree, int start, int count) throws Exception {
        
        boolean hasNext = false;
        java.util.List logList = new java.util.ArrayList();
        Page ret = null;
        int y = 0;
        int i = 0;
        int numResults = 0;
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        SQL = "select * from content_list cl inner join service_route sr on cl.content_type=sr.service_type inner join cp_user cu on " +
                "cl.list_id=cu.id inner join format_list fl on cl.formats=fl.format_id where content_type =" + type.toString();
        
        if (!"*".equals(title)) {
            SQL = SQL + " AND cl.title like '%" + title + "%'";
        }
        if (!"*".equals(author)) {
            SQL = SQL + " AND cl.author like '%" + author + "%'";
        }
        if (category.intValue() != 0) {
            SQL = SQL + " AND cl.category =" + category;
        }
        if (!"*".equals(formats)) {
            SQL = SQL + " AND cl.formats like '%" + formats + "%'";
        }
        if (!"*".equals(listId)) {
            SQL = SQL + " AND cl.list_id  ='" + listId + "'";
        }
        if (isFree == 0 || isFree == 1) {
            SQL = SQL + " AND cl.is_free = " + isFree;
        }
        SQL = SQL + " and cl.show=1 order by cl.date_added desc";
        
        try {
            con = DConnect.getConnection();
            
            prepstat = con.prepareStatement(SQL); /**
             * @todo If the query had parameters set the PreparedStatement's parameters
             *
             **/
            
            rs = prepstat.executeQuery();
            // get the total number of records
            rs.last();
            numResults = rs.getRow();
            rs.beforeFirst();
            // end of code
            
            while (i < (start + count) && rs.next()) {
                if (i == 0) {
                    int x = numResults;
                    y = x / count;
                    if ((x % count) > 0) {
                        y += 1;
                    }
                }
                if (i >= start) {
                    ContentItem content_list = new ContentItem();
                    ContentType typeBean = new ContentType();
                    Format format = new Format();
                    com.rancard.mobility.contentprovider.User cp =
                            new com.rancard.mobility.contentprovider.User();
                    
                    content_list.setContentId(rs.getString("cl.content_id"));
                    content_list.setid(rs.getString("cl.id"));
                    content_list.settitle(rs.getString("cl.title"));
                    content_list.settype(new Integer(rs.getInt("cl.content_type")));
                    content_list.setdownload_url(rs.getString("cl.download_url"));
                    content_list.setPreviewUrl(rs.getString("cl.preview_url"));
                    content_list.setSize(new Long(rs.getLong("cl.size")));
                    //content_list.setformats(rs.getString("formats"));
                    content_list.setPrice(rs.getString("cl.price"));
                    content_list.setOther_Details(rs.getString("cl.other_details"));
                    // rs.getTimestamp(1).getTime()
                    content_list.setDate_Added(rs.getTimestamp("cl.date_added"));
                    content_list.setAuthor(rs.getString("cl.author"));
                    content_list.setCategory(new Integer(rs.getInt("cl.category")));
                    content_list.setListId(rs.getString("cl.list_id"));
                    if (rs.getInt("cl.preview_exists") == 1) {
                        content_list.setPreviewExists(new Boolean(true));
                    } else {
                        content_list.setPreviewExists(new Boolean(false));
                    }
                    
                    if (rs.getInt("cl.isLocal") == 1) {
                        content_list.setIsLocal(true);
                    } else {
                        content_list.setIsLocal(false);
                    }
                    if (rs.getInt("cl.show") == 1) {
                        content_list.setCanList(true);
                    } else {
                        content_list.setCanList(false);
                    }
                    if (rs.getInt("cl.is_free") == 1) {
                        content_list.setFree(true);
                    } else {
                        content_list.setFree(false);
                    }
                    content_list.setShortItemRef (rs.getString ("short_item_ref"));
                    content_list.setSupplierId (rs.getString ("supplier_id"));
                    
                    //content type
                    typeBean.setParentServiceType(rs.getInt("sr.parent_service_type"));
                    typeBean.setServiceName(rs.getString("sr.service_name"));
                    typeBean.setServiceType(rs.getInt("sr.service_type"));
                    
                    //content provider
                    cp.setId(rs.getString("cu.id"));
                    cp.setName(rs.getString("cu.name"));
                    cp.setUsername(rs.getString("cu.username"));
                    cp.setPassword(rs.getString("cu.password"));
                    cp.setDefaultSmsc(rs.getString("cu.default_smsc"));
                    cp.setLogoUrl(rs.getString("cu.logo_url"));
                    cp.setDefaultService(rs.getString(
                            "cu.default_service"));
                    
                    //format
                    format.setId(rs.getInt("fl.format_id"));
                    format.setFileExt(rs.getString("fl.file_ext"));
                    format.setMimeType(rs.getString("fl.mime_type"));
                    format.setPushBearer(rs.getString(
                            "fl.push_bearer"));
                    
                    content_list.setContentTypeDetails(typeBean);
                    content_list.setProviderDetails(cp);
                    content_list.setFormat(format);
                    
                    logList.add(content_list);
                }
                i++;
                
            }
            hasNext = rs.next();
            ret = new Page(logList, start, hasNext, y, numResults);
            
            if (ret == null) {
                ret = com.rancard.util.Page.EMPTY_PAGE;
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
        return ret;
    }
    
    public com.rancard.util.Page searchContentListByPhone(String ownerId, String title, String formats, Integer type, String author, String price,
            Integer category, String phoneId, int isFree, String shortItemRef, String supplierId, int start, int count) throws Exception {
        
        com.rancard.mobility.common.FonCapabilityMtrx fcm = new com.rancard.mobility.common.FonCapabilityMtrx();
        java.util.ArrayList fonformats = com.rancard.mobility.contentserver.Format.getAllFormats();
        String fmtquery = "(cl.formats=0 or ";
        String capability = new String();
        String capabilityValue = new String();
        net.sourceforge.wurfl.wurflapi.WurflDevice device = fcm.getActualDevice(phoneId);
        com.rancard.mobility.contentserver.Format fmt = null;
        
        if (device != null) {
            for (int j = 0; j < fonformats.size(); j++) {
                fmt = (com.rancard.mobility.contentserver.Format) fonformats.get(j);
                capability = fcm.findSupportedCapability(fmt.getFileExt());
                if(capability != null && !capability.equals("")) {
                    capabilityValue = fcm.getCapabilitiesManager().getCapabilityForDevice(device.getId(),capability);
                    if (capabilityValue.equals("true")) {
                        fmtquery = fmtquery + "cl.formats=" + fmt.getId() + " or ";
                    }
                }
            }
            fmtquery = fmtquery.substring(0, fmtquery.lastIndexOf(" or ") + 1);
            fmtquery = fmtquery + ") and ";
        } else {
            throw new Exception(com.rancard.common.Feedback.MISSING_INVALID_PHONE_ID);
        }
        boolean hasNext = false;
        java.util.List logList = new java.util.ArrayList();
        Page ret = null;
        int y = 0;
        int i = 0;
        int numResults = 0;
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        //String listIDquery = new String(" and (cl.list_id='" +csid + "' or ");
        
        try {
            con = DConnect.getConnection();
            
            /*SQL = "select list_id from cs_cp_relationship where cs_id='" + csid +"'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while(rs.next()){
                listIDquery = listIDquery + " cl.list_id='" + rs.getString("list_id") + "' or ";
            }
            listIDquery = listIDquery.substring(0, listIDquery.lastIndexOf("' or ") + 1);
            listIDquery = listIDquery + ") and ";*/
            
            SQL = "select * from content_list cl inner join service_route sr on cl.content_type=sr.service_type inner join cp_user cu on " +
                    "cl.list_id=cu.id inner join format_list fl on cl.formats=fl.format_id where content_type =" + type.toString();
            
            if (!"*".equals(title)) {
                SQL = SQL + " AND cl.title like '%" + title + "%'";
            }
            if (!"*".equals(author)) {
                SQL = SQL + " AND cl.author like '%" + author + "%'";
            }
            if (category.intValue() != 0) {
                SQL = SQL + " AND cl.category =" + category;
            }
            if (!"*".equals(formats)) {
                SQL = SQL + " AND cl.formats like '%" + formats + "%'";
            }
            if (!"*".equals(ownerId)) {
                SQL = SQL + " AND cl.list_id  ='" + ownerId + "'";
            }
            if (!"*".equals(supplierId)) {
                SQL = SQL + " AND cl.supplier_id  ='" + supplierId + "'";
            }
            if (isFree == 0 || isFree == 1) {
                SQL = SQL + " AND cl.is_free = " + isFree;
            }
            if (!"*".equals(shortItemRef)) {
                SQL = SQL + " AND cl.short_item_ref ='" + shortItemRef + "'";
            }
            SQL = SQL + " and " + fmtquery + " cl.show=1 order by cl.date_added desc";
            
            prepstat = con.prepareStatement(SQL);
            
            rs = prepstat.executeQuery();
            // get the total number of records
            rs.last();
            numResults = rs.getRow();
            rs.beforeFirst();
            // end of code
            
            while (i < (start + count) && rs.next()) {
                if (i == 0) {
                    int x = numResults;
                    y = x / count;
                    if ((x % count) > 0) {
                        y += 1;
                    }
                }
                if (i >= start) {
                    //if (fmts.contains (rs.getString ("formats"))) {
                    ContentItem content_list = new ContentItem();
                    ContentType typeBean = new ContentType();
                    Format format = new Format();
                    com.rancard.mobility.contentprovider.User cp =
                            new com.rancard.mobility.contentprovider.User();
                    
                    content_list.setContentId(rs.getString("cl.content_id"));
                    content_list.setid(rs.getString("cl.id"));
                    content_list.settitle(rs.getString("cl.title"));
                    content_list.settype(new Integer(rs.getInt("cl.content_type")));
                    content_list.setdownload_url(rs.getString("cl.download_url"));
                    content_list.setPreviewUrl(rs.getString("cl.preview_url"));
                    content_list.setSize(new Long(rs.getLong("cl.size")));
                    //content_list.setformats(rs.getString("formats"));
                    content_list.setPrice(rs.getString("cl.price"));
                    content_list.setOther_Details(rs.getString("cl.other_details"));
                    // rs.getTimestamp(1).getTime()
                    content_list.setDate_Added(rs.getTimestamp("cl.date_added"));
                    content_list.setAuthor(rs.getString("cl.author"));
                    content_list.setCategory(new Integer(rs.getInt("cl.category")));
                    content_list.setListId(rs.getString("cl.list_id"));
                    if (rs.getInt("cl.preview_exists") == 1) {
                        content_list.setPreviewExists(new Boolean(true));
                    } else {
                        content_list.setPreviewExists(new Boolean(false));
                    }
                    
                    if (rs.getInt("cl.isLocal") == 1) {
                        content_list.setIsLocal(true);
                    } else {
                        content_list.setIsLocal(false);
                    }
                    if (rs.getInt("cl.show") == 1) {
                        content_list.setCanList(true);
                    } else {
                        content_list.setCanList(false);
                    }
                    if (rs.getInt("cl.is_free") == 1) {
                        content_list.setFree(true);
                    } else {
                        content_list.setFree(false);
                    }
                    content_list.setSupplierId (rs.getString("cl.supplier_id"));
                    content_list.setShortItemRef (rs.getString("cl.short_item_ref"));
                    
                    //content type
                    typeBean.setParentServiceType(rs.getInt("sr.parent_service_type"));
                    typeBean.setServiceName(rs.getString("sr.service_name"));
                    typeBean.setServiceType(rs.getInt("sr.service_type"));
                    
                    //content provider
                    cp.setId(rs.getString("cu.id"));
                    cp.setName(rs.getString("cu.name"));
                    cp.setUsername(rs.getString("cu.username"));
                    cp.setPassword(rs.getString("cu.password"));
                    cp.setDefaultSmsc(rs.getString("cu.default_smsc"));
                    cp.setLogoUrl(rs.getString("cu.logo_url"));
                    cp.setDefaultService(rs.getString(
                            "cu.default_service"));
                    
                    //format
                    format.setId(rs.getInt("fl.format_id"));
                    format.setFileExt(rs.getString("fl.file_ext"));
                    format.setMimeType(rs.getString("fl.mime_type"));
                    format.setPushBearer(rs.getString(
                            "fl.push_bearer"));
                    
                    content_list.setContentTypeDetails(typeBean);
                    content_list.setProviderDetails(cp);
                    content_list.setFormat(format);
                    
                    logList.add(content_list);
                    //}
                }
                i++;
            }
            hasNext = rs.next();
            ret = new Page(logList, start, hasNext, y, numResults);
            
            if (ret == null) {
                ret = com.rancard.util.Page.EMPTY_PAGE;
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
        return ret;
        
    }
    
    public com.rancard.util.Page searchContentListBySite(String siteId, String title, String formats, Integer type, String author,
            String price, Integer category, int isFree, String shortItemRef, String supplierId, int start, int count) throws Exception {
        
        boolean hasNext = false;
        java.util.List logList = new java.util.ArrayList();
        Page ret = null;
        int y = 0;
        int i = 0;
        int numResults = 0;
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        //String listIDquery = new String(" and (cl.list_id='' or ");
        
        try {
            con = DConnect.getConnection();
            
            SQL = "select cp_id from cp_sites where site_id='" + siteId +"'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            String acctId = new String();
            while(rs.next()){
                acctId = rs.getString("cp_id");
            }
            
            /*SQL = "select list_id from cs_cp_relationship where cs_id='" + acctId +"'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while(rs.next()){
                listIDquery = listIDquery + " cl.list_id='" + rs.getString("list_id") + "' or ";
            }
            listIDquery = listIDquery.substring(0, listIDquery.lastIndexOf("' or ") + 1);
            listIDquery = listIDquery + ") and ";*/
            
            SQL = "select * from content_list cl inner join service_route sr on cl.content_type=sr.service_type inner join cp_user cu on " +
                    "cl.list_id=cu.id inner join format_list fl on cl.formats=fl.format_id where content_type =" + type.toString();
            
            if (!"*".equals(title)) {
                SQL = SQL + " AND cl.title like '%" + title + "%'";
            }
            if (!"*".equals(author)) {
                SQL = SQL + " AND cl.author like '%" + author + "%'";
            }
            if (category.intValue() != 0) {
                SQL = SQL + " AND cl.category =" + category;
            }
            if (!"*".equals(formats)) {
                SQL = SQL + " AND cl.formats like '%" + formats + "%'";
            }
            if (!"*".equals(acctId)) {
                SQL = SQL + " AND cl.list_id  ='" + acctId + "'";
            }
            if (!"*".equals(supplierId)) {
                SQL = SQL + " AND cl.supplier_id  ='" + supplierId + "'";
            }
            if (isFree == 0 || isFree == 1) {
                SQL = SQL + " AND cl.is_free = " + isFree;
            }
            if (!"*".equals(shortItemRef)) {
                SQL = SQL + " AND cl.short_item_ref ='" + shortItemRef + "'";
            }
            SQL = SQL  + " and cl.show=1 order by cl.date_added desc";
            
            prepstat = con.prepareStatement(SQL);
            
            rs = prepstat.executeQuery();
            // get the total number of records
            rs.last();
            numResults = rs.getRow();
            rs.beforeFirst();
            // end of code
            
            while (i < (start + count) && rs.next()) {
                if (i == 0) {
                    int x = numResults;
                    y = x / count;
                    if ((x % count) > 0) {
                        y += 1;
                    }
                }
                if (i >= start) {
                    ContentItem content_list = new ContentItem();
                    ContentType typeBean = new ContentType();
                    Format format = new Format();
                    com.rancard.mobility.contentprovider.User cp =
                            new com.rancard.mobility.contentprovider.User();
                    
                    content_list.setContentId(rs.getString("cl.content_id"));
                    content_list.setid(rs.getString("cl.id"));
                    content_list.settitle(rs.getString("cl.title"));
                    content_list.settype(new Integer(rs.getInt("cl.content_type")));
                    content_list.setdownload_url(rs.getString("cl.download_url"));
                    content_list.setPreviewUrl(rs.getString("cl.preview_url"));
                    content_list.setSize(new Long(rs.getLong("cl.size")));
                    //content_list.setformats(rs.getString("formats"));
                    content_list.setPrice(rs.getString("cl.price"));
                    content_list.setOther_Details(rs.getString("cl.other_details"));
                    // rs.getTimestamp(1).getTime()
                    content_list.setDate_Added(rs.getTimestamp("cl.date_added"));
                    content_list.setAuthor(rs.getString("cl.author"));
                    content_list.setCategory(new Integer(rs.getInt("cl.category")));
                    content_list.setListId(rs.getString("cl.list_id"));
                    if (rs.getInt("cl.preview_exists") == 1) {
                        content_list.setPreviewExists(new Boolean(true));
                    } else {
                        content_list.setPreviewExists(new Boolean(false));
                    }
                    content_list.setSupplierId (rs.getString("cl.supplier_id"));
                    content_list.setShortItemRef (rs.getString("cl.short_item_ref"));
                    
                    if (rs.getInt("cl.isLocal") == 1) {
                        content_list.setIsLocal(true);
                    } else {
                        content_list.setIsLocal(false);
                    }
                    if (rs.getInt("cl.show") == 1) {
                        content_list.setCanList(true);
                    } else {
                        content_list.setCanList(false);
                    }
                    if (rs.getInt("cl.is_free") == 1) {
                        content_list.setFree(true);
                    } else {
                        content_list.setFree(false);
                    }
                    
                    //content type
                    typeBean.setParentServiceType(rs.getInt("sr.parent_service_type"));
                    typeBean.setServiceName(rs.getString("sr.service_name"));
                    typeBean.setServiceType(rs.getInt("sr.service_type"));
                    
                    //content provider
                    cp.setId(rs.getString("cu.id"));
                    cp.setName(rs.getString("cu.name"));
                    cp.setUsername(rs.getString("cu.username"));
                    cp.setPassword(rs.getString("cu.password"));
                    cp.setDefaultSmsc(rs.getString("cu.default_smsc"));
                    cp.setLogoUrl(rs.getString("cu.logo_url"));
                    cp.setDefaultService(rs.getString(
                            "cu.default_service"));
                    
                    //format
                    format.setId(rs.getInt("fl.format_id"));
                    format.setFileExt(rs.getString("fl.file_ext"));
                    format.setMimeType(rs.getString("fl.mime_type"));
                    format.setPushBearer(rs.getString(
                            "fl.push_bearer"));
                    
                    content_list.setContentTypeDetails(typeBean);
                    content_list.setProviderDetails(cp);
                    content_list.setFormat(format);
                    
                    logList.add(content_list);
                }
                i++;
                
            }
            hasNext = rs.next();
            ret = new Page(logList, start, hasNext, y, numResults);
            
            if (ret == null) {
                ret = com.rancard.util.Page.EMPTY_PAGE;
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
        return ret;
    }
    
    public com.rancard.util.Page searchContentListByContentSubscriber(String csid, String listId, String title, String formats, Integer type, String author,
            String price, Integer category, int isFree, int start, int count) throws Exception {
        
        boolean hasNext = false;
        java.util.List logList = new java.util.ArrayList();
        Page ret = null;
        int y = 0;
        int i = 0;
        int numResults = 0;
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String listIDquery = new String(" and (cl.list_id='" + csid + "' or ");
        
        try {
            con = DConnect.getConnection();
            
            SQL = "select list_id from cs_cp_relationship where cs_id='" + csid +"'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while(rs.next()){
                listIDquery = listIDquery + " cl.list_id='" + rs.getString("list_id") + "' or ";
            }
            listIDquery = listIDquery.substring(0, listIDquery.lastIndexOf("' or ") + 1);
            listIDquery = listIDquery + ") and ";
            
            SQL = "select * from content_list cl inner join service_route sr on cl.content_type=sr.service_type inner join cp_user cu on " +
                    "cl.list_id=cu.id inner join format_list fl on cl.formats=fl.format_id where cl.content_type =" + type.toString();
            
            if (!"*".equals(title)) {
                SQL = SQL + " AND cl.title like '%" + title + "%'";
            }
            if (!"*".equals(author)) {
                SQL = SQL + " AND cl.author like '%" + author + "%'";
            }
            if (category.intValue() != 0) {
                SQL = SQL + " AND cl.category =" + category;
            }
            if (!"*".equals(formats)) {
                SQL = SQL + " AND cl.formats like '%" + formats + "%'";
            }
            if (!"*".equals(listId)) {
                SQL = SQL + " AND cl.list_id  ='" + listId + "'";
            }
            if (isFree == 0 || isFree == 1) {
                SQL = SQL + " AND cl.is_free = " + isFree;
            }
            SQL = SQL + listIDquery + " cl.show=1 order by cl.date_added desc";
            
            prepstat = con.prepareStatement(SQL);
            
            rs = prepstat.executeQuery();
            // get the total number of records
            rs.last();
            numResults = rs.getRow();
            rs.beforeFirst();
            // end of code
            
            while (i < (start + count) && rs.next()) {
                if (i == 0) {
                    int x = numResults;
                    y = x / count;
                    if ((x % count) > 0) {
                        y += 1;
                    }
                }
                if (i >= start) {
                    ContentItem content_list = new ContentItem();
                    ContentType typeBean = new ContentType();
                    Format format = new Format();
                    com.rancard.mobility.contentprovider.User cp =
                            new com.rancard.mobility.contentprovider.User();
                    
                    content_list.setContentId(rs.getString("cl.content_id"));
                    content_list.setid(rs.getString("cl.id"));
                    content_list.settitle(rs.getString("cl.title"));
                    content_list.settype(new Integer(rs.getInt("cl.content_type")));
                    content_list.setdownload_url(rs.getString("cl.download_url"));
                    content_list.setPreviewUrl(rs.getString("cl.preview_url"));
                    content_list.setSize(new Long(rs.getLong("cl.size")));
                    //content_list.setformats(rs.getString("formats"));
                    content_list.setPrice(rs.getString("cl.price"));
                    content_list.setOther_Details(rs.getString("cl.other_details"));
                    // rs.getTimestamp(1).getTime()
                    content_list.setDate_Added(rs.getTimestamp("cl.date_added"));
                    content_list.setAuthor(rs.getString("cl.author"));
                    content_list.setCategory(new Integer(rs.getInt("cl.category")));
                    content_list.setListId(rs.getString("cl.list_id"));
                    if (rs.getInt("cl.preview_exists") == 1) {
                        content_list.setPreviewExists(new Boolean(true));
                    } else {
                        content_list.setPreviewExists(new Boolean(false));
                    }
                    
                    if (rs.getInt("cl.isLocal") == 1) {
                        content_list.setIsLocal(true);
                    } else {
                        content_list.setIsLocal(false);
                    }
                    if (rs.getInt("cl.show") == 1) {
                        content_list.setCanList(true);
                    } else {
                        content_list.setCanList(false);
                    }
                    if (rs.getInt("cl.is_free") == 1) {
                        content_list.setFree(true);
                    } else {
                        content_list.setFree(false);
                    }
                    
                    //content type
                    typeBean.setParentServiceType(rs.getInt("sr.parent_service_type"));
                    typeBean.setServiceName(rs.getString("sr.service_name"));
                    typeBean.setServiceType(rs.getInt("sr.service_type"));
                    
                    //content provider
                    cp.setId(rs.getString("cu.id"));
                    cp.setName(rs.getString("cu.name"));
                    cp.setUsername(rs.getString("cu.username"));
                    cp.setPassword(rs.getString("cu.password"));
                    cp.setDefaultSmsc(rs.getString("cu.default_smsc"));
                    cp.setLogoUrl(rs.getString("cu.logo_url"));
                    cp.setDefaultService(rs.getString(
                            "cu.default_service"));
                    
                    //format
                    format.setId(rs.getInt("fl.format_id"));
                    format.setFileExt(rs.getString("fl.file_ext"));
                    format.setMimeType(rs.getString("fl.mime_type"));
                    format.setPushBearer(rs.getString(
                            "fl.push_bearer"));
                    
                    content_list.setContentTypeDetails(typeBean);
                    content_list.setProviderDetails(cp);
                    content_list.setFormat(format);
                    
                    logList.add(content_list);
                }
                i++;
                
            }
            hasNext = rs.next();
            ret = new Page(logList, start, hasNext, y, numResults);
            
            if (ret == null) {
                ret = com.rancard.util.Page.EMPTY_PAGE;
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
        return ret;
    }
    
    public com.rancard.util.Page searchContentList(String ownerId, String title, String formats, Integer type, String author,
            String price, Integer category, int isFree, String shortItemRef, String supplierId, int start, int count) throws Exception {
        
        boolean hasNext = false;
        java.util.List logList = new java.util.ArrayList();
        Page ret = null;
        int y = 0;
        int i = 0;
        int numResults = 0;
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        //String listIDquery = new String(" and (cl.list_id='" + csid + "' or ");
        
        try {
            con = DConnect.getConnection();
            
            /*SQL = "select list_id from cs_cp_relationship where cs_id='" + csid +"'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while(rs.next()){
                listIDquery = listIDquery + " cl.list_id='" + rs.getString("list_id") + "' or ";
            }
            listIDquery = listIDquery.substring(0, listIDquery.lastIndexOf("' or ") + 1);
            listIDquery = listIDquery + ") and ";*/
            
            SQL = "select * from content_list cl inner join service_route sr on cl.content_type=sr.service_type inner join cp_user cu on " +
                    "cl.list_id=cu.id inner join format_list fl on cl.formats=fl.format_id where cl.content_type =" + type.toString();
            
            if (!"*".equals(title)) {
                SQL = SQL + " AND cl.title like '%" + title + "%'";
            }
            if (!"*".equals(author)) {
                SQL = SQL + " AND cl.author like '%" + author + "%'";
            }
            if (category.intValue() != 0) {
                SQL = SQL + " AND cl.category =" + category;
            }
            if (!"*".equals(formats)) {
                SQL = SQL + " AND cl.formats like '%" + formats + "%'";
            }
            if (!"*".equals(ownerId)) {
                SQL = SQL + " AND cl.list_id  ='" + ownerId + "'";
            }
            if (!"*".equals(supplierId)) {
                SQL = SQL + " AND cl.supplier_id  ='" + supplierId + "'";
            }
            if (isFree == 0 || isFree == 1) {
                SQL = SQL + " AND cl.is_free = " + isFree;
            }
            if (!"*".equals(shortItemRef)) {
                SQL = SQL + " AND cl.short_item_ref ='" + shortItemRef + "'";
            }
            SQL = SQL + " and cl.show=1 order by cl.date_added desc";
            
            prepstat = con.prepareStatement(SQL);
            
            rs = prepstat.executeQuery();
            // get the total number of records
            rs.last();
            numResults = rs.getRow();
            rs.beforeFirst();
            // end of code
            
            while (i < (start + count) && rs.next()) {
                if (i == 0) {
                    int x = numResults;
                    y = x / count;
                    if ((x % count) > 0) {
                        y += 1;
                    }
                }
                if (i >= start) {
                    ContentItem content_list = new ContentItem();
                    ContentType typeBean = new ContentType();
                    Format format = new Format();
                    com.rancard.mobility.contentprovider.User cp =
                            new com.rancard.mobility.contentprovider.User();
                    
                    content_list.setContentId(rs.getString("cl.content_id"));
                    content_list.setid(rs.getString("cl.id"));
                    content_list.settitle(rs.getString("cl.title"));
                    content_list.settype(new Integer(rs.getInt("cl.content_type")));
                    content_list.setdownload_url(rs.getString("cl.download_url"));
                    content_list.setPreviewUrl(rs.getString("cl.preview_url"));
                    content_list.setSize(new Long(rs.getLong("cl.size")));
                    content_list.setPrice(rs.getString("cl.price"));
                    content_list.setOther_Details(rs.getString("cl.other_details"));
                    // rs.getTimestamp(1).getTime()
                    content_list.setDate_Added(rs.getTimestamp("cl.date_added"));
                    content_list.setAuthor(rs.getString("cl.author"));
                    content_list.setCategory(new Integer(rs.getInt("cl.category")));
                    content_list.setListId(rs.getString("cl.list_id"));
                    if (rs.getInt("cl.preview_exists") == 1) {
                        content_list.setPreviewExists(new Boolean(true));
                    } else {
                        content_list.setPreviewExists(new Boolean(false));
                    }
                    
                    if (rs.getInt("cl.isLocal") == 1) {
                        content_list.setIsLocal(true);
                    } else {
                        content_list.setIsLocal(false);
                    }
                    if (rs.getInt("cl.show") == 1) {
                        content_list.setCanList(true);
                    } else {
                        content_list.setCanList(false);
                    }
                    if (rs.getInt("cl.is_free") == 1) {
                        content_list.setFree(true);
                    } else {
                        content_list.setFree(false);
                    }
                    content_list.setSupplierId (rs.getString("cl.supplier_id"));
                    content_list.setShortItemRef (rs.getString("cl.short_item_ref"));
                    
                    //content type
                    typeBean.setParentServiceType(rs.getInt("sr.parent_service_type"));
                    typeBean.setServiceName(rs.getString("sr.service_name"));
                    typeBean.setServiceType(rs.getInt("sr.service_type"));
                    
                    //content provider
                    cp.setId(rs.getString("cu.id"));
                    cp.setName(rs.getString("cu.name"));
                    cp.setUsername(rs.getString("cu.username"));
                    cp.setPassword(rs.getString("cu.password"));
                    cp.setDefaultSmsc(rs.getString("cu.default_smsc"));
                    cp.setLogoUrl(rs.getString("cu.logo_url"));
                    cp.setDefaultService(rs.getString(
                            "cu.default_service"));
                    
                    //format
                    format.setId(rs.getInt("fl.format_id"));
                    format.setFileExt(rs.getString("fl.file_ext"));
                    format.setMimeType(rs.getString("fl.mime_type"));
                    format.setPushBearer(rs.getString(
                            "fl.push_bearer"));
                    
                    content_list.setContentTypeDetails(typeBean);
                    content_list.setProviderDetails(cp);
                    content_list.setFormat(format);
                    
                    logList.add(content_list);
                }
                i++;
                
            }
            hasNext = rs.next();
            ret = new Page(logList, start, hasNext, y, numResults);
            
            if (ret == null) {
                ret = com.rancard.util.Page.EMPTY_PAGE;
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
        return ret;
    }
    
    public java.util.ArrayList getRecentlyAdded(int typeId, int count) throws Exception {
        java.util.ArrayList recentlyAdded = new ArrayList();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        int counter = 0;
        
        try {
            
            con = DConnect.getConnection();
            
            SQL = "SELECT * FROM content_list, service_route, cp_user, format_list where content_type=? and content_list.content_type=" +
                    "service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id order by date_added desc";
            
            prepstat = con.prepareStatement(SQL);
            
            prepstat.setInt(1, typeId);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                ContentItem item = new ContentItem();
                ContentType type = new ContentType();
                Format format = new Format();
                com.rancard.mobility.contentprovider.User cp = new com.rancard.mobility.contentprovider.User();
                
                //content item
                item.setContentId(rs.getString("content_id"));
                item.setid(rs.getString("id"));
                item.settitle(rs.getString("title"));
                item.settype(new Integer(rs.getInt("content_type")));
                item.setdownload_url(rs.getString("download_url"));
                item.setPreviewUrl(rs.getString("preview_url"));
                //item.setformats(rs.getString("formats"));
                item.setPrice(rs.getString("price"));
                item.setAuthor(rs.getString("author"));
                item.setCategory(new Integer(rs.getInt("category")));
                item.setSize(new Long(rs.getLong("size")));
                item.setListId(rs.getString("list_id"));
                item.setDate_Added(rs.getTimestamp("date_added"));
                item.setOther_Details(rs.getString("other_details"));
                if (rs.getInt("isLocal") == 1) {
                    item.setIsLocal(true);
                } else {
                    item.setIsLocal(false);
                }
                if (rs.getInt("show") == 1) {
                    item.setCanList(true);
                } else {
                    item.setCanList(false);
                }
                if (rs.getInt("is_free") == 1) {
                    item.setFree(true);
                } else {
                    item.setFree(false);
                }
                item.setShortItemRef (rs.getString ("short_item_ref"));
                item.setSupplierId (rs.getString ("supplier_id"));
                
                //content type
                type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
                type.setServiceName(rs.getString("service_route.service_name"));
                type.setServiceType(rs.getInt("service_route.service_type"));
                
                //content provider
                cp.setId(rs.getString("cp_user.id"));
                cp.setName(rs.getString("cp_user.name"));
                cp.setUsername(rs.getString("cp_user.username"));
                cp.setPassword(rs.getString("cp_user.password"));
                cp.setDefaultSmsc(rs.getString("cp_user.default_smsc"));
                cp.setLogoUrl(rs.getString("cp_user.logo_url"));
                cp.setDefaultService(rs.getString("cp_user.default_service"));
                
                //format
                format.setId(rs.getInt("format_list.format_id"));
                format.setFileExt(rs.getString("format_list.file_ext"));
                format.setMimeType(rs.getString("format_list.mime_type"));
                format.setPushBearer(rs.getString("format_list.push_bearer"));
                
                item.setContentTypeDetails(type);
                item.setProviderDetails(cp);
                item.setFormat(format);
                
                if (counter < count) {
                    recentlyAdded.add(item);
                } else {
                    break;
                }
                counter++;
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
        
        return recentlyAdded;
    }
    
    public java.util.ArrayList getRecentlyAdded(String cpid, int typeId) throws Exception {
        java.util.ArrayList recentlyAdded = new ArrayList();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        int counter = 0;
        //String listIDquery = new String(" and (content_list.list_id='" + cpid + "' or ");
        
        try {
            
            con = DConnect.getConnection();
            
            /*SQL = "select list_id from cs_cp_relationship where cs_id='" + cpid +"'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while(rs.next()){
                listIDquery = listIDquery + " content_list.list_id='" + rs.getString("list_id") + "' or ";
            }
            listIDquery = listIDquery.substring(0, listIDquery.lastIndexOf("' or ") + 1);
            listIDquery = listIDquery + ") ";*/
            
            SQL = "SELECT * FROM content_list, service_route, cp_user, format_list where content_type=? and content_list.content_type=" +
                    "service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id and content_list.list_id='" + cpid + "'" +
                    " order by date_added desc limit 10";
            
            prepstat = con.prepareStatement(SQL);
            
            prepstat.setInt(1, typeId);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                ContentItem item = new ContentItem();
                ContentType type = new ContentType();
                Format format = new Format();
                com.rancard.mobility.contentprovider.User cp = new com.rancard.mobility.contentprovider.User();
                
                //content item
                item.setContentId(rs.getString("content_id"));
                item.setid(rs.getString("id"));
                item.settitle(rs.getString("title"));
                item.settype(new Integer(rs.getInt("content_type")));
                item.setdownload_url(rs.getString("download_url"));
                item.setPreviewUrl(rs.getString("preview_url"));
                //item.setformats(rs.getString("formats"));
                item.setPrice(rs.getString("price"));
                item.setAuthor(rs.getString("author"));
                item.setCategory(new Integer(rs.getInt("category")));
                item.setSize(new Long(rs.getLong("size")));
                item.setListId(rs.getString("list_id"));
                item.setDate_Added(rs.getTimestamp("date_added"));
                item.setOther_Details(rs.getString("other_details"));
                if (rs.getInt("isLocal") == 1) {
                    item.setIsLocal(true);
                } else {
                    item.setIsLocal(false);
                }
                if (rs.getInt("show") == 1) {
                    item.setCanList(true);
                } else {
                    item.setCanList(false);
                }
                if (rs.getInt("is_free") == 1) {
                    item.setFree(true);
                } else {
                    item.setFree(false);
                }
                item.setShortItemRef (rs.getString ("short_item_ref"));
                item.setSupplierId (rs.getString ("supplier_id"));
                
                //content type
                type.setParentServiceType(rs.getInt("service_route.parent_service_type"));
                type.setServiceName(rs.getString("service_route.service_name"));
                type.setServiceType(rs.getInt("service_route.service_type"));
                
                //content provider
                cp.setId(rs.getString("cp_user.id"));
                cp.setName(rs.getString("cp_user.name"));
                cp.setUsername(rs.getString("cp_user.username"));
                cp.setPassword(rs.getString("cp_user.password"));
                cp.setDefaultSmsc(rs.getString("cp_user.default_smsc"));
                cp.setLogoUrl(rs.getString("cp_user.logo_url"));
                cp.setDefaultService(rs.getString("cp_user.default_service"));
                
                //format
                format.setId(rs.getInt("format_list.format_id"));
                format.setFileExt(rs.getString("format_list.file_ext"));
                format.setMimeType(rs.getString("format_list.mime_type"));
                format.setPushBearer(rs.getString("format_list.push_bearer"));
                
                item.setContentTypeDetails(type);
                item.setProviderDetails(cp);
                item.setFormat(format);
                
                recentlyAdded.add(item);
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
        
        return recentlyAdded;
    }
    
    public static ArrayList fetchRecentAdditions(String cpid, int typeId) throws Exception {
        
        String query;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String custimlistid = null;
        ArrayList items = new ArrayList();
        
        try {
            con = DConnect.getConnection();
            query = "select custom_list_id from custom_list_definition where cp_id=? and custom_list_name='recent additions'";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, cpid);
            rs = prepstat.executeQuery();
            
            if (rs.next()) { //first instance of custom list definition
                custimlistid = rs.getString("custom_list_id");
                
                query = "select item_id, prov_id from custom_list where custom_list_id=?";
                prepstat = con.prepareStatement(query);
                prepstat.setString(1, custimlistid);
                rs = prepstat.executeQuery();
                
                while (rs.next()) { //possibly, the existence of multiple item references
                    ContentItem item = new ContentItem();
                    Format format = new Format();
                    ContentType type = new ContentType();
                    com.rancard.mobility.contentprovider.User cp = new com.rancard.mobility.contentprovider.User();
                    
                    query = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id=? and content_list.list_id=? and " +
                            "content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id " +
                            "and content_list.content_type=" + typeId;
                    
                    prepstat = con.prepareStatement(query);
                    String id = rs.getString("item_id");
                    String listid = rs.getString("prov_id");
                    
                    prepstat.setString(1, id);
                    prepstat.setString(2, listid);
                    
                    rs2 = prepstat.executeQuery();
                    while (rs2.next()) { //an item
                        //content item
                        item.setContentId(rs2.getString("content_id"));
                        item.setid(rs2.getString("id"));
                        item.settitle(rs2.getString("title"));
                        item.settype(new Integer(rs2.getInt("content_type")));
                        item.setdownload_url(rs2.getString("download_url"));
                        item.setPreviewUrl(rs2.getString("preview_url"));
                        item.setPrice(rs2.getString("price"));
                        item.setAuthor(rs2.getString("author"));
                        item.setCategory(new Integer(rs2.getInt("category")));
                        item.setSize(new Long(rs2.getLong("size")));
                        item.setListId(rs2.getString("list_id"));
                        item.setDate_Added(rs2.getTimestamp("date_added"));
                        item.setOther_Details(rs2.getString("other_details"));
                        if (rs2.getInt("isLocal") == 1) {
                            item.setIsLocal(true);
                        } else {
                            item.setIsLocal(false);
                        }
                        if (rs2.getInt("show") == 1) {
                            item.setCanList(true);
                        } else {
                            item.setCanList(false);
                        }
                        if (rs2.getInt("is_free") == 1) {
                            item.setFree(true);
                        } else {
                            item.setFree(false);
                        }
                        item.setShortItemRef (rs2.getString ("short_item_ref"));
                        item.setSupplierId (rs2.getString ("supplier_id"));
                        
                        //content type
                        type.setParentServiceType(rs2.getInt("service_route.parent_service_type"));
                        type.setServiceName(rs2.getString("service_route.service_name"));
                        type.setServiceType(rs2.getInt("service_route.service_type"));
                        
                        //content provider
                        cp.setId(rs2.getString("cp_user.id"));
                        cp.setName(rs2.getString("cp_user.name"));
                        cp.setUsername(rs2.getString("cp_user.username"));
                        cp.setPassword(rs2.getString("cp_user.password"));
                        cp.setDefaultSmsc(rs2.getString("cp_user.default_smsc"));
                        cp.setLogoUrl(rs2.getString("cp_user.logo_url"));
                        cp.setDefaultService(rs2.getString("cp_user.default_service"));
                        
                        //format
                        format.setId(rs2.getInt("format_list.format_id"));
                        format.setFileExt(rs2.getString("format_list.file_ext"));
                        format.setMimeType(rs2.getString("format_list.mime_type"));
                        format.setPushBearer(rs2.getString("format_list.push_bearer"));
                    }
                    item.setContentTypeDetails(type);
                    item.setProviderDetails(cp);
                    item.setFormat(format);
                    
                    items.add(item);
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
        
        return items;
    }
    
    public static void exportRecentAdditionsToCustomList(String cpid, int limit) throws Exception {
        //get date variables
        if(limit > 10){limit = 10;}
        if(limit < 0){limit = 0;}
        java.sql.Date today = new java.sql.Date(java.util.Calendar.getInstance().getTime().getTime());
        String dateString = today.toString();
        java.util.StringTokenizer st = new java.util.StringTokenizer(dateString, "-");
        st.nextToken();
        int  month = Integer.parseInt(st.nextToken()) - 1;
        String monthStr = "";
        if(month < 10){monthStr = "0" + month;}
        
        String query;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        Statement stat = null;
        
        try {
            con = DConnect.getConnection();
            
            String customListId = null;
            
            query = "select custom_list_id from custom_list_definition where cp_id=? and custom_list_name='recent additions'";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, cpid);
            rs = prepstat.executeQuery(); //rs now contains list of custom list IDs for a list called 'recent additions''
            while(rs.next()){
                customListId = rs.getString("custom_list_id");
            }
            
            if(customListId != null) {
                
                query = "select service_type from service_route where parent_service_type=1";
                prepstat = con.prepareStatement(query);
                rs = prepstat.executeQuery(); //rs now contains list of service types
                while(rs.next()){
                    //String listIDquery = new String("(content_list.list_id='" + cpid + "' or ");
                    String contentType = rs.getString("service_type");
                    /*query = "select list_id from cs_cp_relationship where cs_id='" + cpid +"'";
                    prepstat = con.prepareStatement(query);
                    rs2 = prepstat.executeQuery();
                    while(rs2.next()){
                        listIDquery = listIDquery + " content_list.list_id='" + rs2.getString("list_id") + "' or ";
                    }
                    listIDquery = listIDquery.substring(0, listIDquery.lastIndexOf("' or ") + 1);
                    listIDquery = listIDquery + ") ";*/
                    
                    query = "SELECT content_list.id, content_list.list_id FROM content_list where content_type=" +
                            contentType + " and content_list.list_id='" + cpid + "' order by date_added desc limit " + limit;
                    prepstat = con.prepareStatement(query);
                    rs2 = prepstat.executeQuery(); //rs2 contains list of IDs and ListIDs of all recent additions of the type returned by rs
                    
                    if(rs2.next()){ //test to see if any results were returned for recent additions for type returned by rs
                        //if results were returned select all item references in the custom list which are of the same type and for the 'recent additions' list
                        query = "select cu.item_id, cu.prov_id from custom_list cu inner join content_list co on co.id=cu.item_id and co.list_id=cu.prov_id " +
                                "where co.content_type=" + contentType + " and cu.custom_list_id='" + customListId + "'";
                        prepstat = con.prepareStatement(query);
                        rs3 = prepstat.executeQuery(); //rs3 contains list of IDs and ListIDs of all existing recent additions of the type returned by rs
                        
                        //do a batch delete for all the items returned by rs3
                        int hasMore = 0;
                        con.setAutoCommit(false);
                        stat = con.createStatement();
                        while(rs3.next()){
                            query = "delete from custom_list where custom_list_id='" + customListId + "' and item_id='" + rs3.getString("item_id") +
                                    "' and prov_id='" + rs3.getString("prov_id") + "'";
                            stat.addBatch(query);
                            hasMore++;
                        }
                        if(hasMore > 0){
                            stat.executeBatch();
                            stat.clearBatch();
                        }
                        
                        rs2.beforeFirst();
                        con.setAutoCommit(false);
                        con.createStatement();
                        stat = con.createStatement();
                        while (rs2.next()) {
                            query = "INSERT into custom_list (custom_list_id, item_id, prov_id) values('" + customListId + "','" + rs2.getString("id") +
                                    "','" + rs2.getString("list_id") + "')";
                            stat.addBatch(query);
                        }
                        stat.executeBatch();
                        stat.clearBatch();
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }
    
    /*
     Returns a list of distinct listIDs present in the database
     */
    public java.util.List getDistinctListIDs(String criterion) throws Exception {
        java.util.List listIDs = new java.util.ArrayList();
        
        String queryEnding = "";
        
        if (criterion != null || !criterion.equals("")) {
            queryEnding = "where content_type=" + criterion;
        }
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "SELECT DISTINCT list_Id from content_list " + queryEnding;
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                
                listIDs.add(rs.getString("list_Id"));
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
        return listIDs;
    }
    
    public static String[][] getCPsForTypes() throws Exception {
        String[][] struct = null;
        String query =
                "SELECT distinct content_list.content_type, content_list.list_id, cp_user.name, cp_user.logo_url FROM content_list, cp_user where " +
                "cp_user.id=content_list.list_id order by content_type";
        
        String typeId, listId, name, logo_url = new String();
        
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();
            
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            struct = new String[rowCount][4];
            rs.beforeFirst();
            rowCount = 0;
            
            while (rs.next()) {
                typeId = rs.getString("content_type");
                listId = rs.getString("list_id");
                name = rs.getString("name");
                logo_url = rs.getString("logo_url");
                struct[rowCount][0] = typeId;
                struct[rowCount][1] = listId;
                struct[rowCount][2] = name;
                struct[rowCount][3] = logo_url;
                rowCount++;
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
        
        return struct;
    }
    
    public static HashMap getMyProviders (String siteId) throws Exception {
        HashMap grpPrv = new HashMap ();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "select cp_id from cp_sites where site_id='" + siteId +"'";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            String acctId = new String ();
            while(rs.next ()){
                acctId = rs.getString ("cp_id");
            }
            
            query = "select list_id from cs_cp_relationship where cs_id='" + acctId +"'";
            String listIDquery = new String ("(content_list.list_id='"+ acctId + "' or ");
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            while(rs.next ()){
                listIDquery = listIDquery + " content_list.list_id='" + rs.getString ("list_id") + "' or ";
            }
            listIDquery = listIDquery.substring (0, listIDquery.lastIndexOf ("' or ") + 1);
            listIDquery = listIDquery + ") and ";
            
            query = "SELECT distinct content_list.list_id, cp_user.name, cp_user.logo_url, content_list.content_type " +
                    "FROM content_list, cp_user where " + listIDquery + "cp_user.id=content_list.list_id order by content_list.content_type, " +
                    "cp_user.id";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            int conTypeMem = 1;
            ArrayList provs = new ArrayList ();
            while (rs.next ()) {
                String[] details = new String[3];
                details[0] = rs.getString("list_id");
                details[1] = rs.getString("name");
                details[2] = rs.getString("logo_url");
                int tempStore = rs.getInt("content_type");
                if (tempStore == conTypeMem){
                    provs.add(details);
                } else {
                    grpPrv.put(new String("" + conTypeMem), provs);
                    conTypeMem = tempStore;
                    provs = new ArrayList ();
                    provs.add(details);
                }
            }
            grpPrv.put(new String("" + conTypeMem), provs);
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return grpPrv;
    }
    
    //returns a hashmap of the count of all available items for a given provider
    //grouped by type -- mono ringers, poly ringers, wallpapers, etc. It neglects
    //content types that are not represented within the content list
    public static java.util.HashMap itemCountByType(String listId) throws Exception {
        String query = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        java.util.HashMap table = new java.util.HashMap();
        
        try {
            con = DConnect.getConnection();
            query =
                    "select service_route.service_type, count(content_list.content_id) " +
                    "as number from content_list, service_route where " +
                    "service_route.service_type=content_list.content_type " +
                    "and content_list.list_id=? group by typeCode";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, listId);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                String key = rs.getString("service_type");
                String entry = rs.getString("number");
                table.put(key, entry);
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
        
        return table;
    }
    
    //returns a count of all available items for a given provider.
    //Selection is based on an array of type IDs
    public static int itemCountByType(String listId, int[] typeIDs) throws Exception {
        int number = 0;
        String query = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "select count(content_id) as number from content_list " +
                    "where content_type=" + typeIDs[0];
            
            if (typeIDs.length > 1) {
                for (int i = 1; i < typeIDs.length; i++) {
                    query = query + " or content_type=" + typeIDs[i];
                }
            }
            
            query = query + " and content_list.list_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, listId);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                number = rs.getInt("number");
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
        
        return number;
    }
    
    public static String validateSupplier (String ownerId, String supplierKey) throws Exception {
        String id = null;
        String query = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "select 3rd_party_id from 3rd_party_info where account_id='" + ownerId + "' and 3rd_party_key='" + supplierKey + "'";
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();
            
            if (rs.next()) {
                id = rs.getString("3rd_party_id");
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
        
        return id;
    }
    
    public static String resolveShortItemReference (String shortItemRef, String accountId, String keyword) throws Exception {
        String id = null;
        String query = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "select c.id from content_list c inner join service_definition s on s.service_type=c.content_type and s.account_id=c.list_id where c.list_id='" + 
                    accountId + "' and c.short_item_ref='" + shortItemRef + "' and s.keyword='" + keyword + "'";
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();
            
            if (rs.next()) {
                id = rs.getString("c.id");
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
        
        return id;
    }
    
    /*public void deleteContentListItem(String[] Id, String listId) throws
            Exception {
     
        String SQL1 = "delete  from content_list where list_id='" + listId +
                      "' and id  in (",
                      SQL2 = "delete  from uploads where list_id='" + listId +
                             "' and id  in (";
        Connection con = null;
        Statement prepstat = null;
        int[] aiupdateCounts = null;
        boolean bError = false;
        String qryEnd = "";
        for (int i = 0; i < Id.length - 1; i++) {
            qryEnd = qryEnd + Id[i] + " ,";
        }
        qryEnd = qryEnd + Id[Id.length - 1] + ")";
        SQL1 = SQL1 + qryEnd;
        SQL2 = SQL2 + qryEnd;
     
        /**  try {
             con = DConnect.getConnection();
             SQL = "delete  from content_list where id=? and list_id =?";
             prepstat = con.prepareStatement(SQL);
     
             prepstat.setString(1, Id);
             prepstat.setString(2,listId);
             prepstat.execute();
                      } catch (Exception ex) {
             if (con != null) {
                 con.close();
             }
             throw new Exception(ex.getMessage());
                      }
                      if (con != null) {
             con.close();
                      }**/
    
     /*try {
         con = DConnect.getConnection();
         con.setAutoCommit(false);
      
         SQL1 = "delete  from content_list where id='" + Id +
                "' and list_id ='" + listId + "'";
         SQL2 = "delete from uploads where id='" + Id + "' and list_id  ='" +
                listId + "'";
      
         con.createStatement();
         prepstat = con.createStatement();
      
         bError = false;
         prepstat.clearBatch();
      
         // add SQL statements
         prepstat.addBatch(SQL1);
         prepstat.addBatch(SQL2);
         // execute the statements
      
         aiupdateCounts = prepstat.executeBatch();
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
      
          }*/
    
    /*public void updateContentItem(java.lang.String id, java.lang.String price, String title, java.lang.Integer category, java.lang.String author,
            boolean isFree, java.lang.String listId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            
            con = DConnect.getConnection();
            //              con.setAutoCommit(false);
            SQL = "update content_list set price=?,category=?,author=?,title =?, is_free=? where id=? and list_id=?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, price);
            prepstat.setInt(2, category.intValue());
            prepstat.setString(3, author);
            prepstat.setString(4, title);
            if(isFree){
                prepstat.setInt(5, 1);
            }else{
                prepstat.setInt(5, 0);
            }
            prepstat.setString(6, id);
            prepstat.setString(7, listId);
            // execute the statements
            prepstat.executeUpdate();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            
            if (con != null) {
                con.close();
            }
        }
    }*/
    
    
    /**
     * Main
     */
    public static void main(String args[]) throws Exception {
        int[] ids = {5};
        try {
            System.out.println(ContentListDB.itemCountByType("kf007tg", ids));
        } catch (Exception e) {
            
        }
        
    }
    
    
}
