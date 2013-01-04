package com.rancard.mobility.contentserver;

import java.util.*;


public class ContentItem implements java.io.Serializable {

    // Bean Properties
    private String contentId;
    private java.lang.String id;
    private java.lang.String listId;
    private java.lang.String title;
    private java.lang.Integer type;
    private java.lang.String downloadUrl;
    private java.lang.String previewUrl;
    //private java.lang.String formats;
    private String price;
    private java.lang.Integer category;
    private String author;
    private String other_details;
    private java.sql.Timestamp date_added;
    private Long size;
    private boolean canList;
    private boolean isLocal;
    private boolean free;
    private String supplierId;
    private String shortItemRef;

    //private byte [] contentFile;
    //private byte [] previewFile;

    private ContentType contentTypeDetails;
    private com.rancard.mobility.contentprovider.User providerDetails;
    private Format format;

    private ArrayList availableFormats;

    //pagination variables
    private int start;
    private int count = 5;
    private Boolean previewExists;

    //constructor
    public ContentItem() {
    }

    //mutator methods

    public void setContentId(String id) {
        this.contentId = id;
    }

    public void setid(java.lang.String id) {
        this.id = id;
    }

    public void settitle(java.lang.String title) {
        this.title = title;
    }

    public void settype(java.lang.Integer type) {
        this.type = type;
    }

    public void setdownload_url(java.lang.String download_url) {
        this.downloadUrl = download_url;
    }

    public void setFormat(com.rancard.mobility.contentserver.Format format) {
        this.format = format;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setPreviewExists(Boolean previewExists) {
        this.previewExists = previewExists;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCategory(java.lang.Integer category) {
        this.category = category;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setAvailableFormats(ArrayList availableFormats) {
        this.availableFormats = availableFormats;
    }

    public void setDate_Added(java.sql.Timestamp date_added) {
        this.date_added = date_added;
    }

    public void setOther_Details(String other_details) {
        this.other_details = other_details;
    }

    public void setIsLocal(boolean flag) {
        this.isLocal = flag;
    }

    public void setCanList(boolean flag) {
        this.canList = flag;
    }

    //accessor methods
    public String getContentId() {
        return this.contentId;
    }

    public java.lang.String getid() {
        return this.id;
    }

    public java.lang.String gettitle() {
        return this.title;
    }

    public java.lang.Integer gettype() {
        return this.type;
    }

    public java.lang.String getdownload_url() {
        return this.downloadUrl;
    }

    public com.rancard.mobility.contentserver.Format getFormat() {
        return this.format;
    }

    public java.util.ArrayList getAvailableFormats() {
        return this.availableFormats;
    }

    public int getCount() {
        return count;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getListId() {
        return listId;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public int getStart() {
        return start;
    }

    public String getPrice() {
        return price;
    }

    public Integer getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public Long getSize() {
        return this.size;
    }

    public Boolean getPreviewExists() {
        return previewExists;
    }

    public java.sql.Timestamp getDate_Added() {
        return this.date_added;
    }

    public String getOther_Details() {
        return this.other_details;
    }

    public ContentType getContentTypeDetails() {
        return this.contentTypeDetails;
    }

    public boolean islocal() {
        return this.isLocal;
    }

    public boolean canList() {
        return this.canList;
    }

    public void setContentTypeDetails(ContentType type) {
        this.contentTypeDetails = type;
    }

    public com.rancard.mobility.contentprovider.User getProviderDetails() {
        return this.providerDetails;
    }

    public void setProviderDetails(com.rancard.mobility.contentprovider.User cp) {
        this.providerDetails = cp;
    }
    
    public boolean isFree () {
        return free;
    }

    public void setFree (boolean free) {
        this.free = free;
    }

    public com.rancard.util.Page ViewContentList() throws Exception {
        /**
         * @todo
         add the neccessary parameters (if any)
         **/
        ContentListDB content_list = new ContentListDB();

        com.rancard.util.Page pg = com.rancard.util.Page.EMPTY_PAGE;
        return content_list.viewContentList(listId, title, "" + format.getId(),
                                            type, start, count);
    }

    public com.rancard.util.Page ViewContentList(String listId, String title, String format, Integer type, int start, int count
            ) throws Exception {
        /**
         * @todo
         add the neccessary parameters (if any)
         **/
        ContentListDB content_list = new ContentListDB();

        com.rancard.util.Page pg = com.rancard.util.Page.EMPTY_PAGE;
        return content_list.viewContentList(listId, title, format, type, start,
                                            count);
    }

    public com.rancard.util.Page SearchContentList(String listId, String title, String format, Integer type, String author, String price,
            Integer category, int isFree, int start, int count ) throws Exception {
        /**
         * @todo
         add the neccessary parameters (if any)
         **/
        ContentListDB content_list = new ContentListDB();

        com.rancard.util.Page pg = com.rancard.util.Page.EMPTY_PAGE;
        return content_list.searchContentList(listId, title, format, type, author, price, category, isFree, start, count);
    }

    public com.rancard.util.Page searchContentListByPhone(String ownerId, String title, String format, Integer type, String author,
            String price, Integer category, String phoneId, int isFree, String shortItemRef, String supplierId, int start, int count ) throws Exception {
        /**
         * @todo
         add the neccessary parameters (if any)
         **/
        ContentListDB content_list = new ContentListDB();

        com.rancard.util.Page pg = com.rancard.util.Page.EMPTY_PAGE;
        return content_list.searchContentListByPhone(ownerId, title, format, type, author, price, category, phoneId, isFree, shortItemRef, supplierId, start, count);
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
    
    public static String resolveShortItemReference (String shortItemRef, String accountId, String keyword) throws Exception {
        return ContentListDB.resolveShortItemReference (shortItemRef, accountId, keyword);
    }

    public com.rancard.mobility.contentserver.ContentItem viewContentItem(String contentId) throws Exception {
        return new ContentListDB().viewcontent_list(contentId);
    }

    public boolean isEqualTo(ContentItem clb2) {
        boolean flag = false;
        if ((this.getListId()).equals(clb2.getListId()) &&
            (this.getid().equals(clb2.getid()))) {
            flag = true;
        }
        return flag;
    }

    public String[][] getCPsForTypes() throws Exception {
        return ContentListDB.getCPsForTypes();
    }

    /*
         returns a list of recently added content by type and limited by count of items to display
     */
    public java.util.ArrayList getRecentlyAdded(int typeId, int count) throws
            Exception {
        return new ContentListDB().getRecentlyAdded(typeId, count);
    }
    
    public java.util.ArrayList getRecentlyAdded(String cpid, int typeId) throws
            Exception {
        return new ContentListDB().getRecentlyAdded(cpid, typeId);
    }

    public void updateContentItem(java.lang.String id,
                                  java.lang.String price,
                                  java.lang.Integer category,
                                  java.lang.String author,
                                  java.lang.String listId) throws Exception {

        ContentListDB db = new ContentListDB();
        db.updateContentItem(id, price, category, author, listId);
    }

    public void updateContentItem(java.lang.String id,
                                  java.lang.String price, String title,
                                  java.lang.Integer category,
                                  java.lang.String author, boolean isFree,
                                  java.lang.String shortItemRef,
                                  java.lang.String listId) throws Exception {

        ContentListDB db = new ContentListDB();
        db.updateContentItem(id, price, title, category, author, isFree, shortItemRef, listId);
    }

    public void updateContentItemsPrice(java.lang.String[] ids,
                                        java.lang.String newprice,
                                        java.lang.String listId) throws
            Exception {

        ContentListDB db = new ContentListDB();
        db.updateContentList(ids, newprice, listId);
    }
    
    public void updateContentItemsPrice (java.lang.String[] ids, java.lang.String[] newprice, java.lang.String listId) throws Exception {
        ContentListDB db = new ContentListDB ();
        db.updateContentList (ids, newprice, listId);
    }

    public void updateContentItemsCategory(java.lang.String[] ids,
                                           java.lang.Integer newcategory,
                                           java.lang.String listId) throws
            Exception {

        ContentListDB db = new ContentListDB();
        db.updateContentList(ids, newcategory, listId);
    }

    public void updateContentItemsAuthor(java.lang.String[] ids,
                                         java.lang.String newauthor,
                                         java.lang.String listId) throws
            Exception {
        ContentListDB db = new ContentListDB();
        db.updateContentListAuthor(ids, newauthor, listId);
    }
    
    public void updateContentListFreebie (java.lang.String[] id, boolean isFree, java.lang.String listId) throws Exception {
        ContentListDB db = new ContentListDB();
        db.updateContentListFreebie(id, isFree, listId);
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

    public String getSupplierId () {
        return supplierId;
    }

    public void setSupplierId (String supplierId) {
        this.supplierId = supplierId;
    }

    public String getShortItemRef () {
        return shortItemRef;
    }

    public void setShortItemRef (String shortItemRef) {
        this.shortItemRef = shortItemRef;
    }
}
