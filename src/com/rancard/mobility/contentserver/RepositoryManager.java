package com.rancard.mobility.contentserver;

import org.apache.commons.fileupload.*;
import javax.naming.Context;
import javax.naming.NamingException;
import com.rancard.common.Config;
import com.rancard.util.DataImportListener;
import javax.naming.InitialContext;
import java.io.File;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpException;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * <p>Title: Rancard Content Server</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Rancard Solutions Ltd</p>
 * @author not attributable
 * @version 1.0
 */
public class RepositoryManager {
    
    private String tmpFolder; // temporary folder for performing transformations
    
    // Bean Properties
    private String contentId;
    private java.lang.String id;
    private java.lang.String listId;
    private java.lang.String title;
    private java.lang.Integer type;
    private String formats;
    private String price;
    private java.lang.Integer category;
    private String author;
    private java.sql.Timestamp date_added;
    
    
    //pagination variables
    private int start;
    private int count = 10;
    
    
    public RepositoryManager(String tempFolder) {
        tmpFolder = tempFolder;
    }
    
    public RepositoryManager() throws Exception {
        if (this.tmpFolder == null) {
            try {
                this.tmpFolder = getTempDirectory();
            } catch (Exception ex) {
                throw new Exception("Temporary directory not set");
            }
        }
    }
    
    public void addItem(ContentItem item) {
        
        com.rancard.mobility.contentserver.ContentListDB db = new ContentListDB();
        
    }
    
    public void updateItem(String listId, String id, String author,
            String title, byte[] previewFile) {
        
        com.rancard.mobility.contentserver.ContentListDB db = new ContentListDB();
        
    }
    
    public void updateItem(ContentItem item) {
        
        com.rancard.mobility.contentserver.ContentListDB db = new ContentListDB();
        
    }
    
    public void viewItem() {}
    
    public void deleteItem(String id, String listId) throws Exception {
        
        com.rancard.mobility.contentserver.ContentListDB cldb = new
                ContentListDB();
        try {
            cldb.deleteContentListItem(id, listId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        //cldb.deleteContentList(li);
        
    }
    
    public void deleteItem(String[] id, String listId) throws Exception {
        
        com.rancard.mobility.contentserver.ContentListDB cldb = new
                ContentListDB();
        try {
            cldb.deleteContentListItem(id, listId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        //cldb.deleteContentList(li);
        
    }
    
    public void FindItem() {}
    
    public java.util.ArrayList createPreviews(String[] contentIds, String listId) throws Exception {
        // version 2
        com.rancard.mobility.contentserver.ContentListDB cdb = new com.rancard.
                mobility.contentserver.ContentListDB();
        // content adapter is responsible for dynamically adapting content to different formats
        ContentAdapter ca = new ContentAdapter();
        // for each id
        java.util.ArrayList validFiles = new java.util.ArrayList();
        // get path to work directory
        String path = this.getTmpFolder();
        // check if directory exists for listId
        File workDirectory = new File(path);
        if (!(workDirectory.exists() && workDirectory.isDirectory())) {
            
            if (!workDirectory.mkdir()) {
                throw new Exception("Unable to find work directory. Please check work directory settings in properties file");
            }
        }
        // so work directory exists lets create a directory for the author
        // create directory for listId
        File userWorkDirectory = new File(workDirectory.getAbsolutePath() +
                File.separator + listId);
        if (!(userWorkDirectory.exists() && userWorkDirectory.isDirectory())) {
            if (!userWorkDirectory.mkdirs()) {
                throw new Exception(
                        "Unable to create work directory for content provider with listId" +
                        listId + " . Check security permissions");
            }
            
        }
        for (int i = 0; i < contentIds.length; i++) {
            java.io.File tmp = null;
            com.rancard.mobility.contentserver.uploadsBean contentItem = new com.rancard.mobility.contentserver.uploadsBean();
            // fetch content file from DB
            //contentItem = cdb.fetchContentItemData(listId, contentIds[i]);
            contentItem = fetchFile(listId, contentIds[i]);
            // generate preview
            contentItem = ca.generatePreview(contentItem);
            // write to temp file
            tmp = ca.writeBytesToFile(contentItem.getDataStream(),
                    userWorkDirectory.getAbsolutePath() +
                    File.separator + contentIds[i]);
            // batch update files back to db.
            if (tmp.exists()) {
                tmp.getAbsolutePath();
                // return hashmap of sucessful updates
                validFiles.add(contentIds[i]);
                
            }
        }
        
        cdb.loadPreviewFiles(validFiles, listId,
                userWorkDirectory.getAbsolutePath());
        
        return validFiles;
    }
    
    public void ImportContent(FileItem fi, String contentProviderId ,Integer type) throws Exception {
        if (!fi.isFormField()) {
            String filename = fi.getName();
            int executeStatus;
            // check file type
            // if filetype is zip
            if (filename != null && filename.endsWith(".zip")) {
                // store to tempory location
                // unzip to db
                // instantiate archive manager with connection to repository
                com.rancard.mobility.contentserver.util.ArchiveManager
                        zipmanager = new com.rancard.mobility.contentserver.
                        util.ArchiveManager(this.getTmpFolder());
                zipmanager.unZipToContentRepository(fi.getInputStream(),
                        fi.getName(), contentProviderId, type);
                // delete temp file
                // update content list
            } else {
                throw new Exception(
                        "Invalid file Archive. File must be a zip file");
                
            }
            
        }
        
    }
    
    public void ImportContent(FileItem fi, String contentProviderId ,Integer type, String supplierId) throws Exception {
        if (!fi.isFormField()) {
            String filename = fi.getName();
            int executeStatus;
            // check file type
            // if filetype is zip
            if (filename != null && filename.endsWith(".zip")) {
                // store to tempory location
                // unzip to db
                // instantiate archive manager with connection to repository
                com.rancard.mobility.contentserver.util.ArchiveManager
                        zipmanager = new com.rancard.mobility.contentserver.
                        util.ArchiveManager(this.getTmpFolder());
                zipmanager.unZipToContentRepository(fi.getInputStream(),
                        fi.getName(), contentProviderId, type, supplierId);
                // delete temp file
                // update content list
            } else {
                throw new Exception(
                        "Invalid file Archive. File must be a zip file");
                
            }
            
        }
        
    }
    
    public void ImportContent(FileItem fi, String contentProviderId ,Integer type, String supplierId, String keyword) throws Exception {
        if (!fi.isFormField()) {
            String filename = fi.getName();
            int executeStatus;
            // check file type
            // if filetype is zip
            if (filename != null && filename.endsWith(".zip")) {
                // store to tempory location
                // unzip to db
                // instantiate archive manager with connection to repository
                com.rancard.mobility.contentserver.util.ArchiveManager
                        zipmanager = new com.rancard.mobility.contentserver.
                        util.ArchiveManager(this.getTmpFolder());
                zipmanager.unZipToContentRepository(fi.getInputStream(),
                        fi.getName(), contentProviderId, type, supplierId, keyword);
                // delete temp file
                // update content list
            } else {
                throw new Exception(
                        "Invalid file Archive. File must be a zip file");
                
            }
            
        }
        
    }   
    
    public void ImportContent(FileItem fi, String contentProviderId) throws Exception {
        if (!fi.isFormField()) {
            String filename = fi.getName();
            int executeStatus;
            // check file type
            // if filetype is zip
            if (filename != null && filename.endsWith(".zip")) {
                // store to tempory location
                // unzip to db
                // instantiate archive manager with connection to repository
                com.rancard.mobility.contentserver.util.ArchiveManager
                        zipmanager = new com.rancard.mobility.contentserver.
                        util.ArchiveManager(this.getTmpFolder());
                zipmanager.unZipToContentRepository(fi.getInputStream(),
                        fi.getName(), contentProviderId);
                // delete temp file
                // update content list
            } else {
                throw new Exception(
                        "Invalid file Archive. File must be a zip file");
                
            }
            
        }
        
    }
    
    public com.rancard.util.Page ViewContentList() throws Exception {
        /**
         * @todo
         * add the neccessary parameters (if any)
         **/
        ContentListDB content_list = new ContentListDB();
        
        com.rancard.util.Page pg = com.rancard.util.Page.EMPTY_PAGE;
        return content_list.viewContentList(listId, title, formats,
                type, start, count);
    }
    
    public com.rancard.util.Page ViewContentList(String listId, String title, String formats, Integer type, int start, int count) throws Exception {
        /**
         * @todo
         * add the neccessary parameters (if any)
         **/
        ContentListDB content_list = new ContentListDB();
        
        com.rancard.util.Page pg = com.rancard.util.Page.EMPTY_PAGE;
        return content_list.viewContentList(listId, title, formats, type, start,
                count);
    }
    
    public com.rancard.util.Page SearchContentList(String listId, String title, String format, Integer type, String author, String price,
            Integer category, int isFree, int start, int count) throws Exception {
        
        ContentListDB content_list = new ContentListDB();
        
        com.rancard.util.Page pg = com.rancard.util.Page.EMPTY_PAGE;
        return content_list.searchContentList(listId, title, format, type, author, price, category, isFree, start, count);
    }
    
    public static com.rancard.util.Page searchContentListByPhone(String ownerId, String title, String formats, Integer type, String author, String price,
            Integer category, String phoneId, int isFree, String shortItemRef, String supplierId,  int start, int count) throws Exception {
        return new ContentListDB().searchContentListByPhone(ownerId, title, formats, type, author, price, category, phoneId, isFree, shortItemRef, supplierId, start, count);
    }
    
    public static com.rancard.util.Page searchContentListBySite(String ownerId, String title, String formats, Integer type, String author,
            String price, Integer category, int isFree, String shortItemRef, String supplierId, int start, int count) throws Exception {
        return new ContentListDB().searchContentListBySite(ownerId, title, formats, type, author, price, category, isFree, shortItemRef, supplierId, start, count);
    }
    
    public static com.rancard.util.Page searchContentListByContentSubscriber(String csid, String listId, String title, String formats, Integer type, String author,
            String price, Integer category, int isFree, int start, int count) throws Exception {
        return new ContentListDB().searchContentListByContentSubscriber(csid, listId, title, formats, type, author, price, category, isFree, start, count);
    }
    
    public static com.rancard.util.Page searchContentList(String ownerId, String title, String formats, Integer type, String author,
            String price, Integer category, int isFree, String shortItemRef, String supplierId, int start, int count) throws Exception {
        return new ContentListDB().searchContentList (ownerId, title, formats, type, author, price, category, isFree, shortItemRef, supplierId, start, count);
    }
    
    public java.util.List getDistinctListIDs(String queryEnding) {
        java.util.List listIDs = new java.util.ArrayList();
        try {
            listIDs = new com.rancard.mobility.contentserver.ContentListDB().
                    getDistinctListIDs(queryEnding);
        } catch (Exception e) {
            System.out.println("Exception at getDistinctListIDs(): " +
                    e.getMessage());
        }
        return listIDs;
    }
    
    public com.rancard.mobility.contentserver.ContentItem viewContentItem(String id, String listId) throws Exception {
        return new ContentListDB().viewcontent_list(id, listId);
    }
    
    public java.util.ArrayList viewContentItem(String [] id, String listId) throws Exception {
        return new ContentListDB().viewContentList(id, listId);
    }
    
    public java.util.ArrayList viewContentList(java.lang.String[] id, java.lang.String[] listId) throws Exception {
        return new ContentListDB().viewContentList(id, listId);
    }
    
    public com.rancard.mobility.contentserver.ContentItem viewContentItem(String contentId) throws Exception {
        return new ContentListDB().viewcontent_list(contentId);
    }
    
    public String viewContentList() {
        return "";
    }
    
    public String[][] getCPsForTypes() throws Exception {
        return ContentListDB.getCPsForTypes();
    }
    
    public static HashMap getMyProviders (String siteId) throws Exception {
        return ContentListDB.getMyProviders(siteId);
    }
    
    public java.util.ArrayList getRecentlyAdded(int typeId, int count) throws Exception {
        return new ContentListDB().getRecentlyAdded(typeId, count);
    }
    
    public void updateContentItem(java.lang.String id, java.lang.String price, java.lang.Integer category, java.lang.String author,
            java.lang.String listId) throws Exception {
        
        ContentListDB db = new ContentListDB();
        db.updateContentItem(id, price, category, author, listId);
    }
    
    public void updateContentItem(java.lang.String id, java.lang.String price, String title, java.lang.Integer category, java.lang.String author,
            boolean isFree, String shortItemRef, java.lang.String listId) throws Exception {
        
        ContentListDB db = new ContentListDB();
        db.updateContentItem(id, price, title, category, author, isFree, shortItemRef, listId);
    }
    
    public void updateContentItemsPrice(java.lang.String[] ids, java.lang.String newprice, java.lang.String listId) throws Exception {
        
        ContentListDB db = new ContentListDB();
        db.updateContentList(ids, newprice, listId);
    }
    
    public void updateContentItemsCategory(java.lang.String[] ids, java.lang.Integer newcategory, java.lang.String listId) throws
            Exception {
        
        ContentListDB db = new ContentListDB();
        db.updateContentList(ids, newcategory, listId);
    }
    
    public void updateContentItemsAuthor(java.lang.String[] ids, java.lang.String newauthor, java.lang.String listId) throws Exception {
        ContentListDB db = new ContentListDB();
        db.updateContentListAuthor(ids, newauthor, listId);
    }
    
    public void updateContentListFreebie(java.lang.String[] id, boolean isFree, java.lang.String listId) throws Exception {
        ContentListDB db = new ContentListDB();
        db.updateContentListFreebie(id, isFree, listId);
    }
    
    public String getTmpFolder() {
        return tmpFolder;
    }
    
    private String getTempDirectory() throws Exception {
        
        String tmpDirectory = "WEB-INF/work";
        try {
            Context ctx = new InitialContext();
            Config appConfig = (Config) ctx.lookup("java:comp/env/bean/rmcsConfig");
            
            if(appConfig!=null &&  appConfig.valueOf("TMPDIR")!=null){
                tmpDirectory = appConfig.valueOf("TMPDIR");
            }
            
            
        } catch (NamingException e) {
            //   Log.write(e);
            throw new Exception(e.getMessage());
        }
        
        return tmpDirectory;
        
    }
    
    public static com.rancard.mobility.contentserver.uploadsBean fetchFile(java.lang.String list_id, java.lang.String id) throws Exception {
        return new com.rancard.mobility.contentserver.uploadsDB().viewuploads(list_id, id);
    }
    
    //generate text for external files
    public static byte[] getByteArray(String urlstr) throws Exception {
        
        InputStream in = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        HttpClient httpclient = new HttpClient();
        GetMethod httpget = new GetMethod(urlstr);
        
        byte[] buff = null;
        try {
            int statusCode = httpclient.executeMethod(httpget);
            if (statusCode != HttpStatus.SC_OK) {
                throw new HttpException(new Integer(statusCode).toString());
            }
            
            long resplength = httpget.getResponseContentLength();
            Long size = new Long(resplength);
            int length = size.intValue();
            
            // Use Buffered Stream for reading/writing.
            in = httpget.getResponseBodyAsStream();
            bis = new BufferedInputStream(in);
            
            buff = new byte[length];
            int bytesRead;
            
            // Simple read loop.
            while ( -1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            }
            
        } catch (Exception e) {
            throw new Exception();
        } finally {
            httpget.releaseConnection();
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return buff;
    }
    
    public static String getHexString(byte[] item) throws Exception {
        String ErrorStr = null;
        String msg = new String();
        
        byte[] b = item;
        
        String hex = bytesToHex(b);
        System.out.println(hex);
        
        //System.out.println(b);
        return hex;
    }
    
    private static String bytesToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            buf.append(byteToHex(data[i]));
        }
        return (buf.toString());
    }
    
    
    private static String byteToHex(byte data) {
        StringBuffer buf = new StringBuffer();
        buf.append(toHexChar((data >>> 4) & 0x0F));
        buf.append(toHexChar(data & 0x0F));
        return buf.toString();
    }
    
    
    private static char toHexChar(int i) {
        if ((0 <= i) && (i <= 9)) {
            return (char) ('0' + i);
        } else {
            return (char) ('A' + (i - 10));
        }
    }
    
    public static String getUD(String format, ContentItem item) throws
            Exception {
        String ud = null;
        byte[] byteStream = null;
        
        if (!item.islocal()) {
            byteStream = RepositoryManager.getByteArray(item.getDownloadUrl());
        } else {
            com.rancard.mobility.contentserver.uploadsBean upload = new RepositoryManager().fetchFile(item.getListId(), item.getid());
            byteStream = upload.getDataStream();
        }
        
        if (format.equalsIgnoreCase("noktxt") || format.equalsIgnoreCase("ems")) {
            ud = new String(byteStream);
        } else if (format.equalsIgnoreCase("imy") || format.equalsIgnoreCase("ott")
        || format.equalsIgnoreCase("10.imy") || format.equalsIgnoreCase("12.imy")) {
            ud = getHexString(byteStream);
        }else if (format.equalsIgnoreCase("bmp")) {
            try{
                ud = getHexString(new ContentAdapter().generateOTB(byteStream, new String(item.getid() + "-" + item.getListId())));
            }catch(Exception e){
                ud = "";
            }finally{
                byteStream = null;
            }
        }
        if (ud.indexOf("%") != -1) {
            ud = ud.substring(0, ud.indexOf("%"));
        }
        if (ud.indexOf("\r") != -1) {
            ud = ud.substring(0, ud.indexOf("\r"));
        }
        if (ud.indexOf("\n") != -1) {
            ud = ud.substring(0, ud.indexOf("\n"));
        }
        
        return ud;
    }
    
    //returns total storage space consumed by actual files and preview files in
    //the database
    public static long totalDiskSpaceUsed(String list_id) throws Exception {
        return com.rancard.mobility.contentserver.uploadsDB.totalDiskSpaceUsed(list_id);
    }
    
    //returns a hashmap of the count of all available items for a given provider
    //grouped by type -- mono ringers, poly ringers, wallpapers, etc. It neglects
    //content types that are not represented within the content list
    public static java.util.HashMap itemCountByType(String listId) throws Exception {
        return ContentListDB.itemCountByType(listId);
    }
    
    //returns a count of all available items for a given provider.
    //Selection is based on an array of type IDs
    public static int itemCountByType(String listId, int[] typeIDs) throws Exception {
        return ContentListDB.itemCountByType(listId, typeIDs);
    }
    
    public static void uploadPreview(DataImportListener listener, List fileItems, String listId) throws Exception {
        uploadsDB.uploadPreview(listener, fileItems, listId);
    }
    
    public static void createCustomList(String cpId, String customListId, String customListName) throws Exception {
        CustomListDB.createCustomList(cpId, customListId, customListName);
    }
    
    public static void updateCustomList(String cpId, String customListId, String customListName) throws Exception {
        CustomListDB.updateCustomList(cpId, customListId, customListName);
    }
    
    public static void deleteCustomList(String customListId) throws Exception {
        CustomListDB.deleteCustomList(customListId);
    }
    
    public static void deleteCustomLists(String[] customListId) throws Exception {
        CustomListDB.deleteCustomLists(customListId);
    }
    
    public static void deleteCustomLists(String cpId) throws Exception {
        CustomListDB.deleteCustomLists(cpId);
    }
    
    public static CustomList viewCustomList(String cpId, String customListId) throws Exception {
        return CustomListDB.viewCustomList(cpId, customListId);
    }
    
    public static ArrayList viewCustomLists(String cpId, String[] customListIds) throws Exception {
        return CustomListDB.viewCustomLists(cpId, customListIds);
    }
    
    public static ArrayList viewCustomLists(String cpId) throws Exception {
        return CustomListDB.viewCustomLists(cpId);
    }
    
    public static ArrayList viewCustomListDefinitions(String cpId) throws Exception {
        return CustomListDB.viewCustomListDefinitions(cpId);
    }
    
    public static void addItemsToList(String customListId, String[] ids, String[] listIds) throws Exception {
        CustomListDB.addItemsToList(customListId, ids, listIds);
    }
    
    public static void removeItemsFromList(String customListId, String[] ids, String[] listIds) throws Exception {
        CustomListDB.removeItemsFromList(customListId, ids, listIds);
    }
    
    public static void exportRecentAdditionsToCustomList(String cpid, int limit) throws Exception {
        ContentListDB.exportRecentAdditionsToCustomList(cpid, limit);
    }
    
    public static ArrayList fetchRecentAdditions(String cpid, int typeId) throws Exception {
        return ContentListDB.fetchRecentAdditions(cpid, typeId);
    }
    
    public static String getCpIdForSite(String siteId) throws Exception {
        return CPSiteDB.getCpIdForSite(siteId);
    }
    
    public static String validateSupplier (String ownerId, String supplierKey) throws Exception {
        return ContentListDB.validateSupplier (ownerId, supplierKey);
    }
    
    public static String resolveShortItemReference (String shortItemRef, String accountId, String keyword) throws Exception {
        return ContentListDB.resolveShortItemReference (shortItemRef, accountId, keyword);
    }
    
    public static ArrayList getDistinctTypes (int parentServiceType, String accountId) throws Exception {
        return ContentTypeDB.getDistinctTypes (parentServiceType, accountId);
    }
    
}
