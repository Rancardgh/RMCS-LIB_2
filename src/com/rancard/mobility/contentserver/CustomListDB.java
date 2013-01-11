/*
 * CustomListDB.java
 *
 * Created on October 3, 2006, 12:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.contentserver;
import com.rancard.common.DConnect;
import java.sql.Statement;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Kweku Tandoh
 */
public abstract class CustomListDB {
    
    //create new custom list
    public static void createCustomList(String cpId, String customListId, String customListName) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "INSERT into custom_list_definition (cp_id, custom_list_id, custom_list_name) values(?,?,?)";
            
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, cpId);
            prepstat.setString(2, customListId);
            prepstat.setString(3, customListName);
            
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
    
    //edit existing custom list
    public static void updateCustomList(String cpId, String customListId, String customListName) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "UPDATE custom_list_definition SET custom_list_name=? WHERE cp_id=? and custom_list_id=?";
            
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, customListName);
            prepstat.setString(2, cpId);
            prepstat.setString(3, customListId);
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
    
    //delete an existing custom list
    public static void deleteCustomList(String customListId) throws Exception {
        //make sure to delete its items
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "DELETE from custom_list_definition WHERE custom_list_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, customListId);
            prepstat.execute();
            
            query = "DELETE from custom_list WHERE custom_list_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, customListId);
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
    
    //delete specific custom lists
    public static void deleteCustomLists(String[] customListId) throws Exception {
        //make sure to delete its items
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
            
            for(int i = 0 ; i < customListId.length; i++){
                SQL1 = "delete from custom_list_definition where custom_list_id='" + customListId[i] + "'";
                SQL2 = "delete from custom_list where custom_list_id='" + customListId[i] + "'";
                bError = false;
                
                // add SQL statements
                prepstat.addBatch(SQL1);
                prepstat.addBatch(SQL2);
            }
            
            aiupdateCounts = prepstat.executeBatch();
            prepstat.clearBatch();
        } catch (BatchUpdateException bue) {
            bError = true;
            aiupdateCounts = bue.getUpdateCounts();
            
            SQLException SQLe = bue;
            while (SQLe != null) {
                SQLe = SQLe.getNextException();
            }
        } catch (SQLException SQLe) {
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
        }
    }
    
    //delete all custom lists for a given content subscriber
    public static void deleteCustomLists(String cpId) throws Exception {
        //make sure to delete its items
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        Statement stat = null;
        ArrayList ids = new ArrayList<String>();
        
        try {
            con = DConnect.getConnection();
            
            //get custom list IDs
            query = "select custom_list_id from custom_list_definition where cp_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, cpId);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                ids.add(rs.getString("custom_list_id"));
            }
            
            //delete custom list definition
            query = "DELETE from custom_list_definition WHERE cp_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, cpId);
            prepstat.execute();
            
            con.setAutoCommit(false);
            con.createStatement();
            stat = con.createStatement();
            
            //delete its items
            for(int i = 0 ; i < ids.size(); i++){
                query = "delete from custom_list where custom_list_id='" + ids.get(i) + "'";
                stat.addBatch(query);
            }
            stat.executeBatch();
            stat.clearBatch();
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
    
    //view a specific custom list
    public static CustomList viewCustomList(String cpId, String customListId) throws Exception {
        CustomList thisList = new CustomList();
        
        String query;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "select * from custom_list_definition where cp_id=? and custom_list_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, cpId);
            prepstat.setString(2, customListId);
            rs = prepstat.executeQuery();
            
            if (rs.next()) { //first instance of custom list definition
                thisList.setCpId(rs.getString("cp_id"));
                thisList.setCustomListId(rs.getString("custom_list_id"));
                thisList.setCustomListName(rs.getString("custom_list_name"));
                
                query = "select item_id, prov_id from custom_list where custom_list_id=?";
                prepstat = con.prepareStatement(query);
                prepstat.setString(1, customListId);
                rs = prepstat.executeQuery();
                
                ArrayList items = new ArrayList<ContentItem>();
                while (rs.next()) { //possibly, the existence of multiple item references
                    ContentItem item = new ContentItem();
                    Format format = new Format();
                    ContentType type = new ContentType();
                    com.rancard.mobility.contentprovider.User cp = new com.rancard.mobility.contentprovider.User();
                    
                    query = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id=? and content_list.list_id=? and " +
                            "content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id";
                    
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
                thisList.setItems(items);
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
        
        return thisList;
    }
    
    //view a specific custom lists
    public static ArrayList viewCustomLists (String cpId, String[] customListIds) throws Exception {
        ArrayList allLists = new ArrayList();
        
        String query;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            for (int i = 0; i < customListIds.length; i++) {
                query = "select * from custom_list_definition where cp_id='" + cpId + "' and custom_list_id='" + customListIds[i] + "'";
                prepstat = con.prepareStatement(query);
                rs = prepstat.executeQuery();
                
                while (rs.next()) { //first instance of custom list definition
                    CustomList thisList = new CustomList();
                    thisList.setCpId(rs.getString("cp_id"));
                    thisList.setCustomListId(rs.getString("custom_list_id"));
                    thisList.setCustomListName(rs.getString("custom_list_name"));
                    
                    query = "select item_id, prov_id from custom_list where custom_list_id='" + customListIds[i] + "'";
                    prepstat = con.prepareStatement(query);
                    rs = prepstat.executeQuery();
                    
                    ArrayList items = new ArrayList();
                    while (rs.next()) { //possibly, the existence of multiple item references
                        ContentItem item = new ContentItem();
                        Format format = new Format();
                        ContentType type = new ContentType();
                        com.rancard.mobility.contentprovider.User cp = new com.rancard.mobility.contentprovider.User();
                        
                        String id = rs.getString("item_id");
                        String listid = rs.getString("prov_id");
                        
                        query = "SELECT * FROM content_list, service_route, cp_user, format_list where content_list.id='"+ id +"' and content_list.list_id='"+ listid +"' and " +
                                "content_list.content_type=service_route.service_type and content_list.list_id=cp_user.id and content_list.formats=format_list.format_id";
                        
                        prepstat = con.prepareStatement(query);
                                                
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
                    thisList.setItems(items);
                    allLists.add(thisList);
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
        
        return allLists;
    }
        
//view all custom lists for a given content subscriber
    public static ArrayList viewCustomLists(String cpId) throws Exception {
        ArrayList lists = new ArrayList();
        
        String query;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "select * from custom_list_definition where cp_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, cpId);
            rs = prepstat.executeQuery();
            
            while (rs.next()) { //multiple instance of custom list definition
                ArrayList items = new ArrayList();
                CustomList thisList = new CustomList();
                thisList.setCpId(rs.getString("cp_id"));
                thisList.setCustomListId(rs.getString("custom_list_id"));
                thisList.setCustomListName(rs.getString("custom_list_name"));
                
                query = "select * from content_list ctl inner join custom_list cl on ctl.id=cl.item_id and ctl.list_id=cl.prov_id inner join service_route sr on ctl.content_type=sr.service_type " +
                        "inner join cp_user cp on ctl.list_id=cp.id inner join format_list fl on ctl.formats=fl.format_id where cl.custom_list_id='" + thisList.getCustomListId() + "'";
                prepstat = con.prepareStatement(query);
                rs3 = prepstat.executeQuery();
                
                while (rs3.next()) { //many items
                    ContentItem item = new ContentItem();
                    Format format = new Format();
                    ContentType type = new ContentType();
                    com.rancard.mobility.contentprovider.User cp = new com.rancard.mobility.contentprovider.User();
                    
                    //content item
                    item.setContentId(rs3.getString("ctl.content_id"));
                    item.setid(rs3.getString("ctl.id"));
                    item.settitle(rs3.getString("ctl.title"));
                    item.settype(new Integer(rs3.getInt("ctl.content_type")));
                    item.setdownload_url(rs3.getString("ctl.download_url"));
                    item.setPreviewUrl(rs3.getString("ctl.preview_url"));
                    item.setPrice(rs3.getString("ctl.price"));
                    item.setAuthor(rs3.getString("ctl.author"));
                    item.setCategory(new Integer(rs3.getInt("ctl.category")));
                    item.setSize(new Long(rs3.getLong("ctl.size")));
                    item.setListId(rs3.getString("ctl.list_id"));
                    item.setDate_Added(rs3.getTimestamp("ctl.date_added"));
                    item.setOther_Details(rs3.getString("ctl.other_details"));
                    if (rs3.getInt("ctl.isLocal") == 1) {
                        item.setIsLocal(true);
                    } else {
                        item.setIsLocal(false);
                    }
                    if (rs3.getInt("ctl.show") == 1) {
                        item.setCanList(true);
                    } else {
                        item.setCanList(false);
                    }
                    if (rs3.getInt("ctl.is_free") == 1) {
                        item.setFree(true);
                    } else {
                        item.setFree(false);
                    }
                    item.setShortItemRef (rs3.getString ("short_item_ref"));
                    item.setSupplierId (rs3.getString ("supplier_id"));
                    
                    //content type
                    type.setParentServiceType(rs3.getInt("sr.parent_service_type"));
                    type.setServiceName(rs3.getString("sr.service_name"));
                    type.setServiceType(rs3.getInt("sr.service_type"));
                    
                    //content provider
                    cp.setId(rs3.getString("cp.id"));
                    cp.setName(rs3.getString("cp.name"));
                    cp.setUsername(rs3.getString("cp.username"));
                    cp.setPassword(rs3.getString("cp.password"));
                    cp.setDefaultSmsc(rs3.getString("cp.default_smsc"));
                    cp.setLogoUrl(rs3.getString("cp.logo_url"));
                    cp.setDefaultService(rs3.getString("cp.default_service"));
                    
                    //format
                    format.setId(rs3.getInt("fl.format_id"));
                    format.setFileExt(rs3.getString("fl.file_ext"));
                    format.setMimeType(rs3.getString("fl.mime_type"));
                    format.setPushBearer(rs3.getString("fl.push_bearer"));
                    
                    item.setContentTypeDetails(type);
                    item.setProviderDetails(cp);
                    item.setFormat(format);
                    
                    items.add(item);
                }
                thisList.setItems(items);
                lists.add(thisList);
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
        return lists;
    }
    
//view all custom lists for a given content subscriber
    public static ArrayList viewCustomListDefinitions(String cpId) throws Exception {
        ArrayList lists = new ArrayList();
        
        String query;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            query = "select * from custom_list_definition where cp_id=?";
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, cpId);
            rs = prepstat.executeQuery();
            
            while (rs.next()) { //multiple instances of custom list definitions
                CustomList thisList = new CustomList();
                thisList.setCpId(rs.getString("cp_id"));
                thisList.setCustomListId(rs.getString("custom_list_id"));
                thisList.setCustomListName(rs.getString("custom_list_name"));
                
                lists.add(thisList);
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
        return lists;
    }
    
//add items to a list
    public static void addItemsToList(String customListId, String[] ids, String[] listIds) throws Exception {
        Connection con = null;
        PreparedStatement prepstat = null;
        Statement stat = null;
        int[] aiupdateCounts = null;
        boolean bError = false;
        String query;
        ResultSet rs = null;
        
        if(ids.length == listIds.length){
            try {
                con = DConnect.getConnection();
                
                query = "select * from custom_list_definition where custom_list_id=?";
                prepstat = con.prepareStatement(query);
                prepstat.setString(1, customListId);
                rs = prepstat.executeQuery();
                if (rs.next()) {
                    con.setAutoCommit(false);
                    stat = con.createStatement();
                    
                    for(int i = 0 ; i < ids.length; i++){
                        query = "INSERT into custom_list (custom_list_id, item_id, prov_id) values('" + customListId + "','" + ids[i] +"','" + listIds[i] + "')";
                        bError = false;
                        stat.addBatch(query);
                    }
                    aiupdateCounts = stat.executeBatch();
                    stat.clearBatch();
                }
            } catch (BatchUpdateException bue) {
                bError = true;
                aiupdateCounts = bue.getUpdateCounts();
                
                SQLException SQLe = bue;
                while (SQLe != null) {
                    SQLe = SQLe.getNextException();
                }
            } catch (SQLException SQLe) {
            } finally {
                int iProcessed;
                // determine operation result
                for (int i = 0; i < aiupdateCounts.length; i++) {
                    iProcessed = aiupdateCounts[i];
                    if (iProcessed > 0 ||
                            iProcessed == -2
                            ) {
                        // statement was successful
                        System.out.println("Insert sucessful");
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
        }else{
            throw new Exception("unpaired references found");
        }
    }
    
//remove items from a list
    public static void removeItemsFromList(String customListId, String[] ids, String[] listIds) throws Exception {
        Connection con = null;
        PreparedStatement prepstat = null;
        Statement stat = null;
        int[] aiupdateCounts = null;
        boolean bError = false;
        String query;
        ResultSet rs = null;
        
        if(ids.length == listIds.length){
            try {
                con = DConnect.getConnection();
                
                con.setAutoCommit(false);
                stat = con.createStatement();
                
                for(int i = 0 ; i < ids.length; i++){
                    query = "delete from custom_list where custom_list_id='" + customListId + "' and item_id='" + ids[i] +
                            "' and prov_id='" + listIds[i] + "'";
                    bError = false;
                    stat.addBatch(query);
                }
                aiupdateCounts = stat.executeBatch();
                stat.clearBatch();
            } catch (BatchUpdateException bue) {
                bError = true;
                aiupdateCounts = bue.getUpdateCounts();
                
                SQLException SQLe = bue;
                while (SQLe != null) {
                    SQLe = SQLe.getNextException();
                }
            } catch (SQLException SQLe) {
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
            }
        }else{
            throw new Exception("unpaired references found");
        }
    }
}