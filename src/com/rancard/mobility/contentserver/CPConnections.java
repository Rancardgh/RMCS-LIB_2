package com.rancard.mobility.contentserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class CPConnections {

    private String password = null;
    private String username = null;
    private String connection = null;
    private String gatewayURL = null;
    private String httpMethod = null;
    private String driverType = null;
    private List allowedNetworks = null;
    private String providerId = null;
    private String billingMech = null;
    private String countryName = null;
    private String countryAlias = null;
    private int utcOffset = 0;
    private String language = null;

    public CPConnections(String password, String username, String connection, String gatewayURL, String httpMethod, 
            String driverType, List allowedNetworks, String providerId, String billingMech, String countryName,
            String countryAlias, int utcOffset, String language){
        this.password =password;
        this.username = username;
        this.connection = connection;
        this.gatewayURL = gatewayURL;
        this.httpMethod = httpMethod;
        this.driverType = driverType;
        this.allowedNetworks = allowedNetworks;
        this.providerId = providerId;
        this.billingMech = billingMech;
        this.countryName = countryName;
        this.countryAlias =countryAlias;
        this.utcOffset = utcOffset;
        this.language = language;
    }
    //constructor
    public CPConnections() {
        password = new String();
        username = new String();
        connection = new String();
        gatewayURL = new String();
        httpMethod = new String();
        driverType = new String();
        allowedNetworks = new ArrayList();
        providerId = new String();
        billingMech = new String();
        countryName = new String();
        countryAlias = new String();
        utcOffset = 0;
        language = new String();
    }

    //accessor (get) methods
    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getConnection() {
        return this.connection;
    }

    public String getGatewayURL() {
        return this.gatewayURL;
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

    public String getDriverType() {
        return this.driverType;
    }

    public List getAllowedNetworks() {
        return this.allowedNetworks;
    }

    public String getProviderId() {
        return this.providerId;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public String getCountryAlias() {
        return this.countryAlias;
    }

    public int getUTCOffset() {
        return this.utcOffset;
    }

    public String getLanguage() {
        return this.language;
    }
    //mutator (set) methods

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public void setGatewayURL(String gatewayURL) {
        this.gatewayURL = gatewayURL;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public void setAllowedNetworks(ArrayList allowedNetworks) {
        this.allowedNetworks = allowedNetworks;
    }

    public void setAllowedNetworks(String allowedNetworks) {
        StringTokenizer st = new StringTokenizer(allowedNetworks, ",");
        while (st.hasMoreTokens()) {
            this.allowedNetworks.add(st.nextToken());
        }
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getBillingMech() {
        return billingMech;
    }

    public void setBillingMech(String billingMech) {
        this.billingMech = billingMech;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCountryAlias(String countryAlias) {
        this.countryAlias = countryAlias;
    }

    public void setUTCOffset(int utcOffset) {
        this.utcOffset = utcOffset;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static CPConnections getConnection(String providerID) throws Exception{
        try {
            List<CPConnections> connections = CPConnDB.viewConnections(providerID);
            if (connections == null || connections.isEmpty()) {
                return null;
            } else{
                return connections.get(0);
            }
        } catch (Exception e) {
            System.out.println(new Date() + ": " + CPConnections.class + ":ERROR " + e.getMessage());
            throw new Exception(com.rancard.common.Feedback.NO_CONNECTIONS_FOUND);
        }
    }

    public static CPConnections getConnection(String providerID, String msisdn) throws
            Exception {
        CPConnections cnxn = null;
        List connections;

        //get list of connections
        try {
            connections = CPConnDB.viewConnections(providerID);
            if (connections == null || connections.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println(new Date() + ": " + CPConnections.class + ":ERROR " + e.getMessage());
            throw new Exception(com.rancard.common.Feedback.NO_CONNECTIONS_FOUND);
        }

        //check for supported networks for each of the selected connections
        try {
            for (int i = 0; i < connections.size(); i++) {
                List networks = ((CPConnections) connections.get(i)).
                        getAllowedNetworks();
                for (int j = 0; j < networks.size(); j++) {
                    if (msisdn.substring(msisdn.indexOf("+") + 1,
                            networks.get(j).toString().length()
                            + msisdn.indexOf("+") + 1).
                            equals(networks.get(j).toString())) {
                        cnxn = (CPConnections) connections.get(i);
                        break;
                    }
                }
                if (cnxn != null) {
                    break;
                }
            }
            if (cnxn == null) {
                throw new Exception();
            }

        } catch (Exception e) {
            throw new Exception(com.rancard.common.Feedback.UNSUPPORTED_NETWORK);
        }
        return cnxn;
    }

    public static CPConnections getConnection(String providerID, String msisdn, String driverType) throws
            Exception {
        CPConnections cnxn = null;
        List connections = null;

        //get list of connections
        try {
            connections = CPConnDB.viewConnections(providerID, driverType);
            if (connections == null || connections.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception(com.rancard.common.Feedback.NO_CONNECTIONS_FOUND);
        }

        //check for supported networks for each of the selected connections
        try {

            for (int i = 0; i < connections.size(); i++) {
                List networks = ((CPConnections) connections.get(i)).getAllowedNetworks();
                for (int j = 0; j < networks.size(); j++) {
                    if (msisdn.substring(msisdn.indexOf("+") + 1,
                            networks.get(j).toString().length() + msisdn.indexOf("+") + 1)
                            .equals(networks.get(j).toString())) {
                        cnxn = (CPConnections) connections.get(i);
                        break;
                    }
                }
                if (cnxn != null) {
                    break;
                }
            }
            if (cnxn == null) {
                throw new Exception();
            }

        } catch (Exception e) {
            throw new Exception(com.rancard.common.Feedback.UNSUPPORTED_NETWORK);
        }
        return cnxn;
    }

    public void updateConnection() throws Exception {
    }

    public void removeConnection() throws Exception {
    }

    public void insertConnection() throws Exception {
    }
}
