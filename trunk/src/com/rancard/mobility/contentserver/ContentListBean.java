package com.rancard.mobility.contentserver;

import java.util.*;


public class ContentListBean {

    // Bean Properties
    private String contentId;// global unique ID
    private java.lang.String id; // file Id
    private java.lang.String listId; // provider Id
    private java.lang.String title; // title of item
    private java.lang.Integer type; // content type ringtone, logo etc
    private java.lang.String downloadUrl; // Url for fetching  the file
    private java.lang.String previewUrl;  //url for previewing the file
    private java.lang.String formats;    // file format
    private String price; //item cost
    private java.lang.Integer category; // genre of the type
    private String author;   // creator
    private String other_details;  // extra field for the parameter
    private java.sql.Date date_added; //
    private Long size; //size of the data
    private java.io.File contentFile;  // binary file which holds the actual content Item
    private java.io.File contentPreviewFile; // file for previewing


    private ArrayList availableFormats;

    //pagination variables
    private int start;
    private int count = 5;

    //constructor
    public ContentListBean() {
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

    public void setformats(java.lang.String formats) {
        java.util.ArrayList tempFmtLst = new ArrayList();
        this.formats = formats;
        StringTokenizer st = new StringTokenizer(this.formats, ",");
        while (st.hasMoreTokens()) {
            tempFmtLst.add(st.nextToken());
        }
        this.availableFormats = tempFmtLst;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStart(int start) {
        this.start = start;
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

    public void setDate_Added(java.sql.Date date_added) {
        this.date_added = date_added;
    }

    public void setOther_Details(String other_details) {
        this.other_details = other_details;
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

    public java.lang.String getformats() {
        return this.formats;
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

    public java.sql.Date getDate_Added() {
        return this.date_added;
    }

    public String getOther_Details() {
        return this.other_details;
    }

    public com.rancard.util.Page ViewContentList() throws Exception {
        /**
         * @todo
         add the neccessary parameters (if any)
         **/
        com.rancard.mobility.contentserver.ContentListDB content_list = new ContentListDB();

        com.rancard.util.Page pg = com.rancard.util.Page.EMPTY_PAGE;
        return content_list.viewContentList(listId, title, formats,
                                            type, start, count);
    }

    public com.rancard.util.Page ViewContentList(String listId, String title,
                                                 Integer format, Integer type,
                                                 int start, int count
            ) throws Exception {
        /**
         * @todo
         add the neccessary parameters (if any)
         **/
        ContentListDB content_list = new ContentListDB();

        com.rancard.util.Page pg = com.rancard.util.Page.EMPTY_PAGE;
        return content_list.viewContentList(listId, title, formats, type, start,
                                            count);
    }

    public com.rancard.util.Page SearchContentList(String listId, String title,
            String format, Integer type, String author, String price,
            Integer category, int isFree, int start, int count
            ) throws Exception {
        /**
         * @todo
         add the neccessary parameters (if any)
         **/
        ContentListDB content_list = new ContentListDB();

        com.rancard.util.Page pg = com.rancard.util.Page.EMPTY_PAGE;
        return content_list.searchContentList(listId, title, format, type, author, price, category, isFree, start, count);
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

    public com.rancard.mobility.contentserver.ContentItem getBean(String id,
            String listId) throws Exception {
        return new ContentListDB().viewcontent_list(id, listId);
    }

    public com.rancard.mobility.contentserver.ContentItem getBean(String contentId) throws Exception {
        return new ContentListDB().viewcontent_list(contentId);
    }


    public boolean isEqualTo(ContentListBean clb2) {
        boolean flag = false;
        if ((this.getListId()).equals(clb2.getListId()) &&
            (this.getid().equals(clb2.getid()))) {
            flag = true;
        }
        return flag;
    }

    /*
         returns a list of recently added content by type and limited by count of items to display
     */
    public java.util.ArrayList getRecentlyAdded(int typeId, int count) throws
            Exception {
        return new ContentListDB().getRecentlyAdded(typeId, count);
    }

public static void main(String [] args){

com.rancard.mobility.contentserver.ContentListBean cb = new ContentListBean();

try {
    //cb.SearchContentList("001", "*", "*", new Integer(0), "*", "*",
                         //new Integer(0), 0, 10);
} catch (Exception ex) {
}

}

}
