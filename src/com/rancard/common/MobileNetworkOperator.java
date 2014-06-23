package com.rancard.common;

import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Mustee on 3/29/2014.
 */
@Entity
@Table(name = "mobile_network_operator", schema = "", catalog = "rmcs")
@NamedQuery(name = "MobileNetworkOperator.findAll", query = "SELECT m FROM MobileNetworkOperator m")
public class MobileNetworkOperator implements Serializable {

    @Id
    @Column(name = "mno_id", length = 10, nullable = false)
    private String mnoID;

    @Column(length = 100)
    private String brand;

    @Column(length = 100)
    private String operator;

    @Column(length = 100)
    private String country;

    @Column(length = 45, name = "default_smsc")
    private String defaultSMSC;

    @Column(columnDefinition = "ENUM('n', 'y',)", name = "mnp_active")
    private String mnpActive;

    @Column(length = 100, name = "msisdn_regex")
    private String msisdnRegex;

    protected MobileNetworkOperator() {
    }

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

    public static List<MobileNetworkOperator> getAll(EntityManager em){
        List<MobileNetworkOperator> mnos = em.createNamedQuery("MobileNetworkOperator.findAll", MobileNetworkOperator.class).getResultList();

        return mnos;
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
