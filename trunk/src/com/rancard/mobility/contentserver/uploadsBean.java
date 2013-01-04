package com.rancard.mobility.contentserver;

import com.rancard.util.DataImportListener;
import java.util.List;

public class uploadsBean {

// Bean Properties
    private java.lang.String id;
    private java.lang.String filename;
    private java.lang.String listId;
    private byte[] dataStream;
    private byte[] previewStream;
    private String format;
    private String mimeType;
    //constructor
    public uploadsBean() {
    }


    public void setid(java.lang.String id) {
        this.id = id;
    }

    public java.lang.String getid() {
        return this.id;
    }

    public void setfilename(java.lang.String filename) {
        this.filename = filename;
    }

    public java.lang.String getfilename() {
        return this.filename;
    }


    public void setlist_id(java.lang.String list_id) {
        this.listId = list_id;
    }

    public void setDataStream(byte[] in) {
        this.dataStream = in;
    }

    public void setPreviewStream(byte[] in) {
        this.previewStream = in;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public java.lang.String getlist_id() {
        return this.listId;
    }

    public byte[] getDataStream() {
        return dataStream;
    }

    public byte[] getPreviewStream() {
        return previewStream;
    }

    public String getFormat() {
        return format;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void createuploads() throws Exception {
        uploadsDB uploads = new uploadsDB();

//uploads.createuploads(id,filename,binaryfile,list_id);
    }

    public void updateuploads() throws Exception {
        uploadsDB uploads = new uploadsDB();

//uploads.updateuploads(id,filename,binaryfile,list_id);
    }

    public void deleteuploads() throws Exception {
        uploadsDB uploads = new uploadsDB();

        uploads.deleteuploads(listId, id);
    }

    public void viewuploads() throws Exception {
        uploadsBean bean = new uploadsBean();
        uploadsDB uploads = new uploadsDB();

        bean = uploads.viewuploads(listId, id);

        id = bean.getid();
        filename = bean.getfilename();
        dataStream = bean.getDataStream();
        previewStream = bean.getPreviewStream();
        listId = bean.getlist_id();
        format = bean.getFormat();
        mimeType = bean.getMimeType();
    }

    //returns an Uploads Object which contains the file object of the content item
    public static uploadsBean viewuploads(java.lang.String list_id,
                                          java.lang.String id) throws Exception {
        return new uploadsDB().viewuploads(list_id, id);
    }

    //returns the value of the storage consumed by content hosted by a given provider
    //in the database
    public static long getStorageSpaceUsed(String listId) throws Exception{
        return uploadsDB.totalDiskSpaceUsed(listId);
    }
    
    public static void uploadPreview (DataImportListener listener, List fileItems, String listId) throws Exception {
        uploadsDB.uploadPreview (listener, fileItems, listId);
    }

}
