package com.rancard.common;

import com.rancard.mobility.contentserver.EMF;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Mustee on 3/29/2014.
 */
@Entity
@Table(name = "cp_sites", schema = "", catalog = "rmcs")
@NamedQueries({
        @NamedQuery(name = "CPSite.findAll", query = "SELECT c FROM CPSite c")
})
public class CPSite implements Serializable {

    @Id
    @Column(name = "site_id", length = 50)
    private String siteID;

    @JoinColumn(name = "cp_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private CPUser cpUser;

    @Column(name = "site_name", length = 255)
    private String siteName;

    @Column(name = "check_user", nullable = false)
    private boolean checkUser;

    @Column(name = "site_type", nullable = false, length = 10)
    private String siteType;


    protected CPSite() {
    }

    public CPSite(String siteID, CPUser cpUser, String siteName, boolean checkUser, String siteType) {
        this.siteID = siteID;
        this.cpUser = cpUser;
        this.siteName = siteName;
        this.checkUser = checkUser;
        this.siteType = siteType;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public CPUser getCpUser() {
        return cpUser;
    }

    public void setCpUser(CPUser cpUser) {
        this.cpUser = cpUser;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public boolean isCheckUser() {
        return checkUser;
    }

    public void setCheckUser(boolean checkUser) {
        this.checkUser = checkUser;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public static List<CPSite> getAll(EntityManager em) {
        return em.createNamedQuery("CPSite.findAll", CPSite.class).getResultList();
    }

    public static CPSite getSMSSite(EntityManager em, String accountID) {
        Query query = em.createQuery("SELECT cp FROM CPSite cp WHERE cp.siteType = '2' AND cp.cpUser.id = :accountID");
        query.setParameter("accountID", accountID);

        List<CPSite> cpSites = query.getResultList();

        return (cpSites.size() == 0) ? null : cpSites.get(0);
    }
}


