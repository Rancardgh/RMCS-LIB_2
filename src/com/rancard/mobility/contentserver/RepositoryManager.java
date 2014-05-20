package com.rancard.mobility.contentserver;

import com.rancard.common.Config;
import com.rancard.mobility.contentserver.util.ArchiveManager;
import com.rancard.util.DataImportListener;
import com.rancard.util.Page;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class RepositoryManager {
    private String tmpFolder;
    private String contentId;
    private String id;
    private String listId;
    private String title;
    private Integer type;
    private String formats;
    private String price;
    private Integer category;
    private String author;
    private Timestamp date_added;
    private int start;
    private int count = 10;

    public RepositoryManager(String tempFolder) {
        this.tmpFolder = tempFolder;
    }

    public RepositoryManager()
            throws Exception {
        if (this.tmpFolder == null) {
            try {
                this.tmpFolder = getTempDirectory();
            } catch (Exception ex) {
                throw new Exception("Temporary directory not set");
            }
        }
    }

    public void addItem(ContentItem item) {
        ContentListDB db = new ContentListDB();
    }

    public void updateItem(String listId, String id, String author, String title, byte[] previewFile) {
        ContentListDB db = new ContentListDB();
    }

    public void updateItem(ContentItem item) {
        ContentListDB db = new ContentListDB();
    }

    public void viewItem() {
    }

    public void deleteItem(String id, String listId)
            throws Exception {
        ContentListDB cldb = new ContentListDB();
        try {
            cldb.deleteContentListItem(id, listId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteItem(String[] id, String listId)
            throws Exception {
        ContentListDB cldb = new ContentListDB();
        try {
            cldb.deleteContentListItem(id, listId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void FindItem() {
    }

    public ArrayList createPreviews(String[] contentIds, String listId)
            throws Exception {
        ContentListDB cdb = new ContentListDB();


        ContentAdapter ca = new ContentAdapter();

        ArrayList validFiles = new ArrayList();

        String path = getTmpFolder();

        File workDirectory = new File(path);
        if ((!workDirectory.exists()) || (!workDirectory.isDirectory())) {
            if (!workDirectory.mkdir()) {
                throw new Exception("Unable to find work directory. Please check work directory settings in properties file");
            }
        }
        File userWorkDirectory = new File(workDirectory.getAbsolutePath() + File.separator + listId);
        if (((!userWorkDirectory.exists()) || (!userWorkDirectory.isDirectory())) &&
                (!userWorkDirectory.mkdirs())) {
            throw new Exception("Unable to create work directory for content provider with listId" + listId + " . Check security permissions");
        }
        for (int i = 0; i < contentIds.length; i++) {
            File tmp = null;
            uploadsBean contentItem = new uploadsBean();


            contentItem = fetchFile(listId, contentIds[i]);

            contentItem = ca.generatePreview(contentItem);

            tmp = ContentAdapter.writeBytesToFile(contentItem.getDataStream(), userWorkDirectory.getAbsolutePath() + File.separator + contentIds[i]);
            if (tmp.exists()) {
                tmp.getAbsolutePath();

                validFiles.add(contentIds[i]);
            }
        }
        cdb.loadPreviewFiles(validFiles, listId, userWorkDirectory.getAbsolutePath());


        return validFiles;
    }

    public void ImportContent(FileItem fi, String contentProviderId, Integer type)
            throws Exception {
        if (!fi.isFormField()) {
            String filename = fi.getName();
            if ((filename != null) && (filename.endsWith(".zip"))) {
                ArchiveManager zipmanager = new ArchiveManager(getTmpFolder());

                zipmanager.unZipToContentRepository(fi.getInputStream(), fi.getName(), contentProviderId, type);
            } else {
                throw new Exception("Invalid file Archive. File must be a zip file");
            }
        }
    }

    public void ImportContent(FileItem fi, String contentProviderId, Integer type, String supplierId)
            throws Exception {
        if (!fi.isFormField()) {
            String filename = fi.getName();
            if ((filename != null) && (filename.endsWith(".zip"))) {
                ArchiveManager zipmanager = new ArchiveManager(getTmpFolder());

                zipmanager.unZipToContentRepository(fi.getInputStream(), fi.getName(), contentProviderId, type, supplierId);
            } else {
                throw new Exception("Invalid file Archive. File must be a zip file");
            }
        }
    }

    public void ImportContent(FileItem fi, String contentProviderId)
            throws Exception {
        if (!fi.isFormField()) {
            String filename = fi.getName();
            if ((filename != null) && (filename.endsWith(".zip"))) {
                ArchiveManager zipmanager = new ArchiveManager(getTmpFolder());

                zipmanager.unZipToContentRepository(fi.getInputStream(), fi.getName(), contentProviderId);
            } else {
                throw new Exception("Invalid file Archive. File must be a zip file");
            }
        }
    }

    public Page ViewContentList()
            throws Exception {
        ContentListDB content_list = new ContentListDB();

        Page pg = Page.EMPTY_PAGE;
        return content_list.viewContentList(this.listId, this.title, this.formats, this.type, this.start, this.count);
    }

    public Page ViewContentList(String listId, String title, String formats, Integer type, int start, int count)
            throws Exception {
        ContentListDB content_list = new ContentListDB();

        Page pg = Page.EMPTY_PAGE;
        return content_list.viewContentList(listId, title, formats, type, start, count);
    }

    public Page SearchContentList(String listId, String title, String format, Integer type, String author, String price, Integer category, int isFree, int start, int count)
            throws Exception {
        ContentListDB content_list = new ContentListDB();

        Page pg = Page.EMPTY_PAGE;
        return content_list.searchContentList(listId, title, format, type, author, price, category, isFree, start, count);
    }

    public static Page searchContentListByPhone(String ownerId, String title, String formats, Integer type, String author, String price, Integer category, String phoneId, int isFree, String shortItemRef, String supplierId, int start, int count)
            throws Exception {
        return new ContentListDB().searchContentListByPhone(ownerId, title, formats, type, author, price, category, phoneId, isFree, shortItemRef, supplierId, start, count);
    }

    public static Page searchContentListBySite(String ownerId, String title, String formats, Integer type, String author, String price, Integer category, int isFree, String shortItemRef, String supplierId, int start, int count)
            throws Exception {
        return new ContentListDB().searchContentListBySite(ownerId, title, formats, type, author, price, category, isFree, shortItemRef, supplierId, start, count);
    }

    public static Page searchContentListByContentSubscriber(String csid, String listId, String title, String formats, Integer type, String author, String price, Integer category, int isFree, int start, int count)
            throws Exception {
        return new ContentListDB().searchContentListByContentSubscriber(csid, listId, title, formats, type, author, price, category, isFree, start, count);
    }

    public static Page searchContentList(String ownerId, String title, String formats, Integer type, String author, String price, Integer category, int isFree, String shortItemRef, String supplierId, int start, int count)
            throws Exception {
        return new ContentListDB().searchContentList(ownerId, title, formats, type, author, price, category, isFree, shortItemRef, supplierId, start, count);
    }

    public List getDistinctListIDs(String queryEnding) {
        List listIDs = new ArrayList();
        try {
            listIDs = new ContentListDB().getDistinctListIDs(queryEnding);
        } catch (Exception e) {
            System.out.println("Exception at getDistinctListIDs(): " + e.getMessage());
        }
        return listIDs;
    }

    public ContentItem viewContentItem(String id, String listId)
            throws Exception {
        return new ContentListDB().viewcontent_list(id, listId);
    }

    public ArrayList viewContentItem(String[] id, String listId)
            throws Exception {
        return new ContentListDB().viewContentList(id, listId);
    }

    public ArrayList viewContentList(String[] id, String[] listId)
            throws Exception {
        return new ContentListDB().viewContentList(id, listId);
    }

    public ContentItem viewContentItem(String contentId)
            throws Exception {
        return new ContentListDB().viewcontent_list(contentId);
    }

    public String viewContentList() {
        return "";
    }

    public String[][] getCPsForTypes()
            throws Exception {
        return ContentListDB.getCPsForTypes();
    }

    public static HashMap getMyProviders(String siteId)
            throws Exception {
        return ContentListDB.getMyProviders(siteId);
    }

    public ArrayList getRecentlyAdded(int typeId, int count)
            throws Exception {
        return new ContentListDB().getRecentlyAdded(typeId, count);
    }

    public void updateContentItem(String id, String price, Integer category, String author, String listId)
            throws Exception {
        ContentListDB db = new ContentListDB();
        db.updateContentItem(id, price, category, author, listId);
    }

    public void updateContentItem(String id, String price, String title, Integer category, String author, boolean isFree, String shortItemRef, String listId)
            throws Exception {
        ContentListDB db = new ContentListDB();
        db.updateContentItem(id, price, title, category, author, isFree, shortItemRef, listId);
    }

    public void updateContentItemsPrice(String[] ids, String newprice, String listId)
            throws Exception {
        ContentListDB db = new ContentListDB();
        db.updateContentList(ids, newprice, listId);
    }

    public void updateContentItemsCategory(String[] ids, Integer newcategory, String listId)
            throws Exception {
        ContentListDB db = new ContentListDB();
        db.updateContentList(ids, newcategory, listId);
    }

    public void updateContentItemsAuthor(String[] ids, String newauthor, String listId)
            throws Exception {
        ContentListDB db = new ContentListDB();
        db.updateContentListAuthor(ids, newauthor, listId);
    }

    public void updateContentListFreebie(String[] id, boolean isFree, String listId)
            throws Exception {
        ContentListDB db = new ContentListDB();
        db.updateContentListFreebie(id, isFree, listId);
    }

    public String getTmpFolder() {
        return this.tmpFolder;
    }

    private String getTempDirectory()
            throws Exception {
        String tmpDirectory = "WEB-INF/work";
        try {
            Context ctx = new InitialContext();
            Config appConfig = (Config) ctx.lookup("java:comp/env/bean/rmcsConfig");
            if ((appConfig != null) && (appConfig.valueOf("TMPDIR") != null)) {
                tmpDirectory = appConfig.valueOf("TMPDIR");
            }
        } catch (NamingException e) {
            throw new Exception(e.getMessage());
        }
        return tmpDirectory;
    }

    public static uploadsBean fetchFile(String list_id, String id)
            throws Exception {
        return new uploadsDB().viewuploads(list_id, id);
    }

    public static byte[] getByteArray(String urlstr)
            throws Exception {
        InputStream in = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        HttpClient httpclient = new HttpClient();
        GetMethod httpget = new GetMethod(urlstr);

        byte[] buff = null;
        try {
            int statusCode = httpclient.executeMethod(httpget);
            if (statusCode != 200) {
                throw new HttpException(new Integer(statusCode).toString());
            }
            long resplength = httpget.getResponseContentLength();
            Long size = new Long(resplength);
            int length = size.intValue();


            in = httpget.getResponseBodyAsStream();
            bis = new BufferedInputStream(in);

            buff = new byte[length];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            }
            return buff;
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
    }

    public static String getHexString(byte[] item)
            throws Exception {
        String ErrorStr = null;
        String msg = new String();

        byte[] b = item;

        String hex = bytesToHex(b);
        System.out.println(hex);


        return hex;
    }

    private static String bytesToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            buf.append(byteToHex(data[i]));
        }
        return buf.toString();
    }

    private static String byteToHex(byte data) {
        StringBuffer buf = new StringBuffer();
        buf.append(toHexChar(data >>> 4 & 0xF));
        buf.append(toHexChar(data & 0xF));
        return buf.toString();
    }

    private static char toHexChar(int i) {
        if ((0 <= i) && (i <= 9)) {
            return (char) (48 + i);
        }
        return (char) (65 + (i - 10));
    }

    public static String getUD(String format, ContentItem item)
            throws Exception {
        String ud = null;
        byte[] byteStream = null;
        if (!item.islocal()) {
            byteStream = getByteArray(item.getDownloadUrl());
        } else {
            new RepositoryManager();
            uploadsBean upload = fetchFile(item.getListId(), item.getid());
            byteStream = upload.getDataStream();
        }
        if ((format.equalsIgnoreCase("noktxt")) || (format.equalsIgnoreCase("ems"))) {
            ud = new String(byteStream);
        } else if ((format.equalsIgnoreCase("imy")) || (format.equalsIgnoreCase("ott")) || (format.equalsIgnoreCase("10.imy")) || (format.equalsIgnoreCase("12.imy"))) {
            ud = getHexString(byteStream);
        } else if (format.equalsIgnoreCase("bmp")) {
            try {
                ud = getHexString(new ContentAdapter().generateOTB(byteStream, new String(item.getid() + "-" + item.getListId())));
            } catch (Exception e) {
                ud = "";
            } finally {
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

    public static long totalDiskSpaceUsed(String list_id)
            throws Exception {
        return uploadsDB.totalDiskSpaceUsed(list_id);
    }

    public static HashMap itemCountByType(String listId)
            throws Exception {
        return ContentListDB.itemCountByType(listId);
    }

    public static int itemCountByType(String listId, int[] typeIDs)
            throws Exception {
        return ContentListDB.itemCountByType(listId, typeIDs);
    }

    public static void uploadPreview(DataImportListener listener, List fileItems, String listId)
            throws Exception {
        uploadsDB.uploadPreview(listener, fileItems, listId);
    }

    public static void createCustomList(String cpId, String customListId, String customListName)
            throws Exception {
        CustomListDB.createCustomList(cpId, customListId, customListName);
    }

    public static void updateCustomList(String cpId, String customListId, String customListName)
            throws Exception {
        CustomListDB.updateCustomList(cpId, customListId, customListName);
    }

    public static void deleteCustomList(String customListId)
            throws Exception {
        CustomListDB.deleteCustomList(customListId);
    }

    public static void deleteCustomLists(String[] customListId)
            throws Exception {
        CustomListDB.deleteCustomLists(customListId);
    }

    public static void deleteCustomLists(String cpId)
            throws Exception {
        CustomListDB.deleteCustomLists(cpId);
    }

    public static CustomList viewCustomList(String cpId, String customListId)
            throws Exception {
        return CustomListDB.viewCustomList(cpId, customListId);
    }

    public static ArrayList viewCustomLists(String cpId, String[] customListIds)
            throws Exception {
        return CustomListDB.viewCustomLists(cpId, customListIds);
    }

    public static ArrayList viewCustomLists(String cpId)
            throws Exception {
        return CustomListDB.viewCustomLists(cpId);
    }

    public static ArrayList viewCustomListDefinitions(String cpId)
            throws Exception {
        return CustomListDB.viewCustomListDefinitions(cpId);
    }

    public static void addItemsToList(String customListId, String[] ids, String[] listIds)
            throws Exception {
        CustomListDB.addItemsToList(customListId, ids, listIds);
    }

    public static void removeItemsFromList(String customListId, String[] ids, String[] listIds)
            throws Exception {
        CustomListDB.removeItemsFromList(customListId, ids, listIds);
    }

    public static void exportRecentAdditionsToCustomList(String cpid, int limit)
            throws Exception {
        ContentListDB.exportRecentAdditionsToCustomList(cpid, limit);
    }

    public static ArrayList fetchRecentAdditions(String cpid, int typeId)
            throws Exception {
        return ContentListDB.fetchRecentAdditions(cpid, typeId);
    }

    public static String getCpIdForSite(String siteId)
            throws Exception {
        return CPSiteDB.getCpIdForSite(siteId);
    }

    public static String validateSupplier(String ownerId, String supplierKey)
            throws Exception {
        return ContentListDB.validateSupplier(ownerId, supplierKey);
    }

    public static String resolveShortItemReference(String shortItemRef, String accountId, String keyword)
            throws Exception {
        return ContentListDB.resolveShortItemReference(shortItemRef, accountId, keyword);
    }

    public static ArrayList getDistinctTypes(int parentServiceType, String accountId)
            throws Exception {
        return ContentTypeDB.getDistinctTypes(parentServiceType, accountId);
    }
}
