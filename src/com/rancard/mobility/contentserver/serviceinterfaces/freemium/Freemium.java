/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mustee
 */
public class Freemium {
    private String id;
    private String accountID;
    private String keyword;
    private Date startDate;
    private Date endDate;
    private boolean active;
    private FreemiumType type; 
    private int length;
    private FreemiumRolloverOption rolloverOption;
    
    public Freemium(String id, String accountID, String keyword, int length, Date startDate, Date endDate, boolean active, FreemiumType type, FreemiumRolloverOption rolloverOption){
        this.id = id;
        this.accountID =accountID;
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.type = type;
        this.length = length;
        this.rolloverOption = rolloverOption;
    }

    public String getID() {
        return id;
    }

    public String getAccountID() {
        return accountID;
    }


    public String getKeyword() {
        return keyword;
    }

    public int getLength(){
        return length;
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return active;
    }

    public FreemiumType getType() {
        return type;
    }
    
    public List<FreemiumDataSource> getDataSource() throws SQLException, Exception{
        return FreemiumDB.getFreemuimDataSource(this);
    }
    
    public List<FreemiumDataSource> getDataSource(Connection conn) throws SQLException{
        return FreemiumDB.getFreemuimDataSource(this, conn);
    }

    /**
     * @return the rolloverOption
     */
    public FreemiumRolloverOption getRolloverOption() {
        return rolloverOption;
    }
}
