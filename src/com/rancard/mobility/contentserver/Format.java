package com.rancard.mobility.contentserver;

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
public class Format {

    //variables
    private int id;
    private String fileExt;
    private String pushBearer;
    private String mimeType;

    //constructor
    public Format() {
        id = 0;
        fileExt = "";
        pushBearer = "";
        mimeType = "";
    }

    //constructor
    public Format(int id, String fileExt, String push, String mimeType) {
        this.id = id;
        this.fileExt = fileExt;
        this.pushBearer = push;
        this.mimeType = mimeType;
    }

    //Mutator methods
    public void setId(int id) {
        this.id = id;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public void setPushBearer(String push) {
        this.pushBearer = push;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    //Accessor methods
    public int getId() {
        return this.id;
    }

    public String getFileExt() {
        return this.fileExt;
    }

    public String getPushBearer() {
        return this.pushBearer;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    //DB Access methods
   public static Format viewFormat(int referenceCode) throws Exception {
       return FormatDB.viewFormat(referenceCode);
   }

   public void updateFormat() throws Exception {
       FormatDB.updateFormat(this.id, this.fileExt, this.pushBearer, this.mimeType);
   }

   public void removeFormat() throws Exception {
       FormatDB.deleteFormat(this.id);
   }

   public void insertFormat() throws Exception {
       FormatDB.insertFormat(this.fileExt, this.pushBearer, this.mimeType);
   }

   public static java.util.ArrayList getAllFormats() throws Exception {
       return FormatDB.viewFormats();
   }

}
