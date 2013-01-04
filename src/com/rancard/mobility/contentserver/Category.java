package com.rancard.mobility.contentserver;

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
public class Category {

    //variables
    private int id;
    private String categoryDesc;
    private int contentType;

    //Default constructor
    public Category() {
        id = 0;
        categoryDesc = "";
        contentType = 0;
    }

    //constructor
    public Category(int id, String desc, int contentType) {
        this.id = id;
        this.categoryDesc = desc;
        this.contentType = contentType;
    }

    //Mutator methods
    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryDesc(String desc) {
        this.categoryDesc = desc;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    //Accessor methods
    public int getId() {
        return this.id;
    }

    public String getCategoryDesc() {
        return this.categoryDesc;
    }

    public int getContentType() {
        return this.contentType;
    }

    //DB Access methods
    public static Category viewCategory(int referenceCode) throws Exception {
        return CategoryDB.viewCategory(referenceCode);
    }

    public void updateCategory() throws Exception {
        CategoryDB.updateCategory(this.id, this.categoryDesc,
                                      this.contentType);
    }

    public void removeCategory() throws Exception {
        CategoryDB.deleteCategory(this.id);
    }

    public void insertCategory() throws Exception {
        CategoryDB.insertCategory(this.categoryDesc, this.contentType);
    }


    public static java.util.List getCategories(String contentType) throws Exception {
        java.util.List categoryIDs = new java.util.ArrayList();
        categoryIDs = CategoryDB.getDistinctCategories(contentType);
        return categoryIDs;
    }
    
    /*public static java.util.List getPopulatedCategories(String contentType) throws Exception {
        java.util.List categoryIDs = new java.util.ArrayList();
        categoryIDs = CategoryDB.getPopulatedCategories(contentType);
        return categoryIDs;
    }*/
    
    public static java.util.List getPopulatedCategories(String contentType, String siteId) throws Exception {
        java.util.List categoryIDs = new java.util.ArrayList();
        categoryIDs = CategoryDB.getPopulatedCategories(contentType, siteId);
        return categoryIDs;
    }
    
    public static HashMap getPopulatedCategories (String siteId) throws Exception {
        return CategoryDB.getPopulatedCategories(siteId);
    }
}
