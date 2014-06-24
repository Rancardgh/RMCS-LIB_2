package com.rancard.common;

import java.io.Serializable;

/**
 * Created by Mustee on 3/29/2014.
 */
public class Country implements Serializable {
    private String countryAlias;
    private String countryName;
    private int utcOffset;
    private String language;


    public Country(String countryAlias, String countryName, int utcOffset, String language) {
        this.countryAlias = countryAlias;
        this.countryName = countryName;
        this.utcOffset = utcOffset;
        this.language = language;
    }

    public String getCountryAlias() {
        return countryAlias;
    }

    public void setCountryAlias(String countryAlias) {
        this.countryAlias = countryAlias;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(int utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
