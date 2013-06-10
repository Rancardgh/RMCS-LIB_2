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
public class PhoneMake {

    //variables
    private int id;
    private String make;

    public PhoneMake() {
        id = 0;
        make = "";
    }

    public PhoneMake(int id, String make) {
        this.id = id;
        this.make = make;
    }

    //Mutator methods
    public void setId(int id) {
        this.id = id;
    }

    public void setMake(String make) {
        this.make = make;
    }

    //Accessor methods
    public int getId() {
        return this.id;
    }

    public String getMake() {
        return this.make;
    }

    //DB Access methods
   public static PhoneMake viewPhoneMake(int referenceCode) throws Exception {
       return PhoneMakeDB.viewPhoneMake(referenceCode);
   }

   public void updatePhoneMake() throws Exception {
       PhoneMakeDB.updatePhoneMake(this.id, this.make);
   }

   public void removePhoneMake() throws Exception {
       PhoneMakeDB.deletePhoneMake(this.id);
   }

   public void insertPhoneMake() throws Exception {
       PhoneMakeDB.insertPhoneMake(this.make);
   }

   public static java.util.List getPhoneMakes() throws Exception {
        java.util.List makeIDs = new java.util.ArrayList();
        makeIDs = PhoneMakeDB.getPhoneMakes();
        return makeIDs;
    }
}
