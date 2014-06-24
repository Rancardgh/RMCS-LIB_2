package com.rancard.common;

import com.rancard.common.db.DConnect;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by Mustee on 3/29/2014.
 */
public class MobileNetworkOperator implements Serializable {
    private static Logger logger = Logger.getLogger(CPConnection.class.getName());
    private String mnoID;
    private String brand;
    private String operator;
    private String country;
    private String defaultSMSC;
    private String mnpActive;
    private String msisdnRegex;

    public MobileNetworkOperator(String mnoID, String brand, String operator, String country, String defaultSMSC, String mnpActive, String msisdnRegex) {
        this.mnoID = mnoID;
        this.brand = brand;
        this.operator = operator;
        this.country = country;
        this.defaultSMSC = defaultSMSC;
        this.mnpActive = mnpActive;
        this.msisdnRegex = msisdnRegex;
    }

    public String getMnoID() {
        return mnoID;
    }

    public void setMnoID(String mnoID) {
        this.mnoID = mnoID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDefaultSMSC() {
        return defaultSMSC;
    }

    public void setDefaultSMSC(String defaultSMSC) {
        this.defaultSMSC = defaultSMSC;
    }

    public String getMnpActive() {
        return mnpActive;
    }

    public void setMnpActive(String mnpActive) {
        this.mnpActive = mnpActive;
    }

    public String getMsisdnRegex() {
        return msisdnRegex;
    }

    public void setMsisdnRegex(String msisdnRegex) {
        this.msisdnRegex = msisdnRegex;
    }

    public static List<MobileNetworkOperator> getAll() throws Exception {
        List<MobileNetworkOperator> mnos = new ArrayList<MobileNetworkOperator>();
        final String query = "SELECT * FROM mobile_network_operator";
        Connection conn = null;
        ResultSet rs = null;

        try {
            logger.info("Getting connections: "+query);
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(query);

            while (rs.next()){
                mnos.add(new MobileNetworkOperator(rs.getString("mno_id"), rs.getString("brand"), rs.getString("operator"), rs.getString("country"),
                        rs.getString("default_smsc"), rs.getString("mnp_active"), rs.getString("msisdn_regex")));
            }

            return mnos;
        } catch (Exception e) {
            logger.severe("Problem getting connections: " + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static boolean isValidMSISDN(List<MobileNetworkOperator> operators, String smsc, String msisdn){
        MobileNetworkOperator mobileNetworkOperator = findByDefaultSMSC(operators, smsc);
        if(mobileNetworkOperator == null){
            return false;
        }

        Pattern pattern = Pattern.compile(mobileNetworkOperator.msisdnRegex);
        return pattern.matcher(msisdn).matches();
    }

    public static String formatToInternationalFormat(List<MobileNetworkOperator> operators, String smsc, String msisdn){
        MobileNetworkOperator mobileNetworkOperator = findByDefaultSMSC(operators, smsc);
        if(mobileNetworkOperator == null){
            throw new IllegalArgumentException("Mobile network could not be found.");
        }

        if(mobileNetworkOperator.country.equalsIgnoreCase("Ghana")){
            return Utils.formatToInternationalFormatGH(msisdn);
        }else if(mobileNetworkOperator.country.equalsIgnoreCase("Nigeria")){
            return Utils.formatToInternationalFormatNG(msisdn);
        }else{
            throw new UnsupportedOperationException("Sorry msisdn cannot be converted.");
        }
    }

    public static MobileNetworkOperator findByDefaultSMSC(List<MobileNetworkOperator> operators, String smsc){
        MobileNetworkOperator mobileNetworkOperator = null;
        for(MobileNetworkOperator operator: operators){
            if(StringUtils.containsIgnoreCase(smsc, Utils.getBaseSMSC(operator.defaultSMSC) )){
                mobileNetworkOperator = operator;
                break;
            }
        }

        return mobileNetworkOperator;
    }


    public static boolean hasMSISDN(List<MobileNetworkOperator> operators, String s){
        for(MobileNetworkOperator operator: operators){
            Pattern pattern = Pattern.compile(operator.msisdnRegex);
            if(pattern.matcher(s).matches()){
                return true;
            }
        }

        return false;
    }



}
