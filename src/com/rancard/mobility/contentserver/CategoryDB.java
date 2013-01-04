package com.rancard.mobility.contentserver;

import java.sql.PreparedStatement;
import com.rancard.common.DConnect;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

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
public abstract class CategoryDB {
    
    /*
     insert record
     */
    public static void insertCategory (String categoryDesc, int contentType) throws
            Exception {
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query =
                    "INSERT into category_list(content_desc,content_type_id) values(?,?)";
            
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, categoryDesc);
            prepstat.setInt (2, contentType);
            
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    /*
         Update list entry
     */
    public static void updateCategory (int id, String contentDesc,
            int contentType) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query =
                    "UPDATE category_list SET category_desc=?,content_type_id=? WHERE" +
                    " category_id=" + id;
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, contentDesc);
            prepstat.setInt (2, contentType);
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    /*
         Deletes an entry
     */
    public static void deleteCategory (int id) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "DELETE from category_list WHERE category_id=?";
            prepstat = con.prepareStatement (query);
            prepstat.setInt (1, id);
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    /*
         Retrieves an item as a bean
     */
    public static Category viewCategory (int categoryId) throws
            Exception {
        Category newBean = new Category ();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "SELECT * from category_list WHERE category_id=?";
            prepstat = con.prepareStatement (query);
            prepstat.setInt (1, categoryId);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                newBean.setId (rs.getInt ("category_id"));
                newBean.setCategoryDesc (rs.getString ("category_desc"));
                newBean.setContentType (rs.getInt ("content_type_id"));
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
        return newBean;
    }
    
    /*
      Returns a list of distinct types present in the database
     */
    public static java.util.List getPopulatedCategories (String contentType, String siteId) throws Exception {
        String queryEnding = "";
        
        if (contentType != null || !contentType.equals ("") || !contentType.equals ("0")) {
            queryEnding = "where content_type_id=" + contentType;
        }
        
        java.util.List types = new java.util.ArrayList ();
        
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
            String listIDquery = new String (" (content_list.list_id='"+ acctId + "' or ");
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            while(rs.next ()){
                listIDquery = listIDquery + " content_list.list_id='" + rs.getString ("list_id") + "' or ";
            }
            listIDquery = listIDquery.substring (0, listIDquery.lastIndexOf ("' or ") + 1);
            listIDquery = listIDquery + ") and ";
            
            query = "SELECT distinct content_list.category, category_list.category_id, category_list.category_desc, category_list.content_type_id " +
                    "FROM content_list, category_list where " + listIDquery + "category_list.content_type_id=" + contentType + " and " +
                    "category_list.category_id=content_list.category order by category_list.category_id";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                types.add (new Category (rs.getInt ("category_id"),
                        rs.getString ("category_desc"),
                        rs.getInt ("content_type_id")));
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
        
        return types;
    }
    
    public static HashMap getPopulatedCategories (String siteId) throws Exception {
        HashMap grpCats = new HashMap ();
        
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
            String listIDquery = new String (" (content_list.list_id='"+ acctId + "' or ");
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            while(rs.next ()){
                listIDquery = listIDquery + " content_list.list_id='" + rs.getString ("list_id") + "' or ";
            }
            listIDquery = listIDquery.substring (0, listIDquery.lastIndexOf ("' or ") + 1);
            listIDquery = listIDquery + ") and ";
            
            query = "SELECT distinct content_list.category, category_list.category_id, category_list.category_desc, category_list.content_type_id " +
                    "FROM content_list, category_list where " + listIDquery + "category_list.category_id=content_list.category order by category_list.content_type_id, " +
                    "category_list.category_id";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            int conTypeMem = 1;
            ArrayList cats = new ArrayList ();
            while (rs.next ()) {
                Category cat = new Category (rs.getInt ("category_id"), rs.getString ("category_desc"), rs.getInt ("content_type_id"));
                if (cat.getContentType() == conTypeMem){
                    cats.add(cat);
                } else {
                    grpCats.put(new String("" + conTypeMem), cats);
                    conTypeMem = cat.getContentType();
                    cats = new ArrayList ();
                    cats.add(cat);
                }
            }
            grpCats.put(new String("" + conTypeMem), cats);
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return grpCats;
    }
    
    public static java.util.List getDistinctCategories (String contentType) throws
            Exception {
        String queryEnding = "";
        
        if (contentType != null || !contentType.equals ("") || !contentType.equals ("0")) {
            queryEnding = "where content_type_id=" + contentType;
        }
        
        java.util.List types = new java.util.ArrayList ();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            query = "SELECT * from category_list " + queryEnding;
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                types.add (new Category (rs.getInt ("category_id"),
                        rs.getString ("category_desc"),
                        rs.getInt ("content_type_id")));
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
        
        return types;
    }
    
    
    /*
      Returns a list of distinct types present in the database
     */
    /*public static java.util.List getPopulatedCategories (String contentType) throws
            Exception {
        String queryEnding = "";
        
        if (contentType != null || !contentType.equals ("") || !contentType.equals ("0")) {
            queryEnding = "where content_type_id=" + contentType;
        }
        
        java.util.List types = new java.util.ArrayList ();
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            //query = "SELECT * from category_list " + queryEnding;
            
            query = "SELECT distinct content_list.category, category_list.category_id, category_list.category_desc, category_list.content_type_id " +
                    "FROM content_list, category_list where category_list.content_type_id=" + contentType + " and category_list.category_id=content_list.category " +
                    "order by category_list.category_id";
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                types.add (new Category (rs.getInt ("category_id"),
                        rs.getString ("category_desc"),
                        rs.getInt ("content_type_id")));
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
        
        return types;
    }    */
    
    
    /*
        test
     */
    public static void main (String args[]) throws Exception {
        
    }
}
