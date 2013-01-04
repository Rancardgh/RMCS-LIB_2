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
public class Phone {

    //variables
    private int id;
    private String phoneModel;
    private int phoneMake;
    private String supportedFormats;

    public Phone() {
        id = 0;
        phoneModel = "";
        phoneMake = 0;
        supportedFormats = "";
    }

    public Phone(int id, String phoneModel, int phoneMake, String supportedFormats) {
        this.id = id;
        this.phoneModel = phoneModel;
        this.phoneMake = phoneMake;
        this.supportedFormats = supportedFormats;
    }

    //Mutator methods
    public void setId(int id) {
        this.id = id;
    }

    public void setModel(String model) {
        this.phoneModel = model;
    }

    public void setMake(int make) {
        this.phoneMake = make;
    }

    public void setSupportedFormats(String formats) {
        if(this.supportedFormats == null)
            this.supportedFormats = "";
        this.supportedFormats = formats;
    }

    //Accessor methods
    public int getId() {
        return this.id;
    }

    public String getModel() {
        return this.phoneModel;
    }

    public int getMake() {
        return this.phoneMake;
    }

    public String getSupportedFormats() {
        if(this.supportedFormats == null)
            this.supportedFormats = "";
        return this.supportedFormats;
    }

    public java.util.ArrayList getSupportedFormatsList() {
        java.util.ArrayList formats = new java.util.ArrayList();
        if(supportedFormats != null) {
            java.util.StringTokenizer st = new java.util.StringTokenizer(this.
                    supportedFormats, ",");
            while (st.hasMoreTokens()) {
                formats.add(st.nextToken());
            }
        } else
            formats = null;
        return formats;
    }

    //DB Access methods
   public static Phone viewPhone(int referenceCode) throws Exception {
       return PhoneDB.viewPhone(referenceCode);
   }

   public void updatePhone() throws Exception {
       PhoneDB.updatePhone(this.id, this.phoneModel, this.phoneMake, this.supportedFormats);
   }

   public void removePhone() throws Exception {
       PhoneDB.deletePhone(this.id);
   }

   public void insertPhone() throws Exception {
       PhoneDB.insertPhone(this.phoneModel, this.phoneMake, this.supportedFormats);
   }

   public static java.util.List getPhoneModels(String queryEnding) throws Exception {
       return PhoneDB.getDistinctMakes(queryEnding);
   }
}
