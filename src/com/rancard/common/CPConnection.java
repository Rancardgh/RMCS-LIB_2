package com.rancard.common;

import com.rancard.common.key.CPConnectionKey;
import com.rancard.mobility.contentserver.EMF;
import com.rancard.util.Utils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Mustee on 3/29/2014.
 */
@Entity
@Table(name = "cp_connections", schema = "", catalog = "rmcs")
@NamedQueries({
        @NamedQuery(name = "CPConnection.findAll", query = "SELECT c FROM CPConnection c")
})
public class CPConnection implements Serializable {

    @EmbeddedId
    private CPConnectionKey cpConnectionKey;

    @Column(length = 50)
    private String username;

    @Column(length = 50)
    private String password;

    @Column(name = "gateway_url")
    private String gatewayURL;

    @Column(length = 50)
    private String method;

    @Column(length = 10, name = "type")
    private String driverType;

    @Column(name = "allowed_networks", length = 500)
    private String allowedNetworks;

    @Column(name = "billing_mech", length = 20)
    private String billingMech;

    @Column(name = "time_zone", length = 20)
    private String timeZone;

    protected CPConnection() {
    }

    public CPConnection(CPConnectionKey cpConnectionKey, String username, String password, String gatewayURL, String method, String driverType, String allowedNetworks, String billingMech, String timeZone) {
        this.cpConnectionKey = cpConnectionKey;
        this.username = username;
        this.password = password;
        this.gatewayURL = gatewayURL;
        this.method = method;
        this.driverType = driverType;
        this.allowedNetworks = allowedNetworks;
        this.billingMech = billingMech;
        this.timeZone = timeZone;
    }

    public CPConnectionKey getCpConnectionKey() {
        return cpConnectionKey;
    }

    public void setCpConnectionKey(CPConnectionKey cpConnectionKey) {
        this.cpConnectionKey = cpConnectionKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGatewayURL() {
        return gatewayURL;
    }

    public void setGatewayURL(String gatewayURL) {
        this.gatewayURL = gatewayURL;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getAllowedNetworks() {
        return allowedNetworks;
    }

    public void setAllowedNetworks(String allowedNetworks) {
        this.allowedNetworks = allowedNetworks;
    }

    public String getBillingMech() {
        return billingMech;
    }

    public void setBillingMech(String billingMech) {
        this.billingMech = billingMech;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public static List<CPConnection> getAll(EntityManager em) {
        List<CPConnection> connections = em.createNamedQuery("CPConnection.findAll", CPConnection.class).getResultList();

        return  connections;
    }

    public static CPConnection getCPConnection(EntityManager em, String accountID, String smsc) {
        Query query = em.createQuery("SELECT cp FROM CPConnection cp WHERE cp.cpConnectionKey.listID = '"
                + accountID + "' AND cp.cpConnectionKey.connID LIKE '%" + Utils.getBaseSMSC(smsc) + "%'");
        List<CPConnection> cpConnections = query.getResultList();


        return (cpConnections.size() == 0) ? null : cpConnections.get(0);
    }
}
