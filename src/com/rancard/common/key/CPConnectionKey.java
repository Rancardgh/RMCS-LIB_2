package com.rancard.common.key;

import com.rancard.common.CPUser;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Mustee on 3/29/2014.
 */
@Embeddable
public class CPConnectionKey implements Serializable {

    @Column(length = 50, name = "conn_id", nullable = false)
    private String connID;

    @Column(name = "list_id", nullable = false, length = 20)
    private String listID;

    protected CPConnectionKey() {
    }

    public CPConnectionKey(String connID, String listID) {
        this.connID = connID;
        this.listID = listID;
    }

    public String getConnID() {
        return connID;
    }

    public void setConnID(String connID) {
        this.connID = connID;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
