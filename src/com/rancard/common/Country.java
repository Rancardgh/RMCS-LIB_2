package com.rancard.common;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Mustee on 3/29/2014.
 */
@Entity
@Table(name = "country_timezone", schema = "", catalog = "rmcs")
@NamedQueries({
        @NamedQuery(name = "Country.findAll", query = "SELECT c FROM Country c")
})
public class Country implements Serializable {

    @Id
    @Column(nullable = false, name = "country_alias", length = 50)
    private String countryAlias;

    @Column(name = "country_name", length = 100)
    private String countryName;

    @Column(name = "utc_offset")
    private int utcOffset;

    @Column(name = "language", length = 20)
    private String language;

    protected Country() {
    }

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

    public static List<Country> getAll(EntityManager em){
        return em.createNamedQuery("Country.findAll", Country.class).getResultList();
    }
}
