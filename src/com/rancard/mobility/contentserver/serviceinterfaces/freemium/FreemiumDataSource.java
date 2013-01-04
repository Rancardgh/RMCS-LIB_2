/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

/**
 *
 * @author Mustee
 */
public class FreemiumDataSource {
    private Freemium freemium;
    private FreemiumDataSourceType sourceType;
    private String source;
    private String column;
    private String delimiter;
    private boolean notIn;
    
    public FreemiumDataSource(Freemium freemium, FreemiumDataSourceType sourceType, String source, String column, String delimiter, boolean notIn){
        this.freemium = freemium;
        this.column = column;
        this.delimiter = delimiter;
        this.sourceType = sourceType;
        this.source = source;
        this.notIn = notIn;
    }

    public Freemium getFreemium() {
        return freemium;
    }

    public FreemiumDataSourceType getSourceType() {
        return sourceType;
    }

    public String getSource() {
        return source;
    }

    public String getColumn() {
        return column;
    }

    public String getDelimiter() {
        return delimiter;
    }


    public boolean isNotIn() {
        return notIn;
    }
    
    
}
