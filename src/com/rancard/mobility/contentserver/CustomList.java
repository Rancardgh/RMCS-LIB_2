/*
 * CustomList.java
 *
 * Created on October 3, 2006, 12:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.contentserver;
import java.util.ArrayList;

/**
 *
 * @author Kweku Tandoh
 */
public class CustomList {
    
    private String cpId; //ID of the content subscriber who is creating  the custom list
    private String customListId; //ID of the custome list
    private String customListName; //Name of the custom list
    private ArrayList items; //items contained in this list
    
    /** Creates a new instance of CustomList */
    public CustomList () {
        this.cpId = "";
        this.customListId = "";
        this.customListName = "";
        this.items = new ArrayList ();
    }
    
    /** Creates a new instance of CustomList */
    public CustomList (String cpId, String customListId, String customListName) {
        this.cpId = cpId;
        this.customListId = customListId;
        this.customListName = customListName;
        this.items = new ArrayList ();
    }
    
    /** Creates a new instance of CustomList */
    public CustomList (String cpId, String customListId, String customListName, ArrayList<ContentItem> items) {
        this.cpId = cpId;
        this.customListId = customListId;
        this.customListName = customListName;
        this.items = items;
    }

    public String getCpId () {
        return cpId;
    }

    public void setCpId (String cpId) {
        this.cpId = cpId;
    }

    public String getCustomListId () {
        return customListId;
    }

    public void setCustomListId (String customListId) {
        this.customListId = customListId;
    }

    public String getCustomListName () {
        return customListName;
    }

    public void setCustomListName (String customListName) {
        this.customListName = customListName;
    }
    
    public ArrayList getItems () {
        return items;
    }

    public void setItems (ArrayList<ContentItem> items) {
        this.items = items;
    }
    
    //checks if list is empty
    public boolean isEmpty() {
        return this.items.isEmpty ();
    }   
    
}
