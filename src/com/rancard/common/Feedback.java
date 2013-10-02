package com.rancard.common;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Feedback {

    public static final String MISSING_INVALID_ITEM_REF = "1000";
    public static final String NO_CONTENT_AT_LOCATION = "1001";
    public static final String MISSING_INVALID_CONTENT_TYPE = "1002";
    public static final String PREVIEW_CREATION_ERROR = "1003";
    public static final String MISSING_INVALID_MSISDN = "2000";
    public static final String MISSING_INVALID_PHONE_ID = "\2001";
    public static final String PHONE_FORMAT_INCOMPATIBILITY = "2002";
    public static final String PHONE_MATRIX_INIT_ERROR = "2003";
    public static final String PUSH_PROTOCOL_NOT_SUPPORTED = "2004";
    public static final String MISSING_INVALID_TICKET_ID = "3000";
    public static final String EXPIRED_TICKET_ID = "3001";
    public static final String DOWNLOAD_PROCESSED = "3002";
    //authentication - 4000s
    public static final String INVALID_PASSWORD = "4000";
    public static final String INVALID_REQUEST_CREDENTIALS = "4001";
    public static final String MISSING_INVALID_PIN = "4002";
    public static final String MISSING_INVALID_REGISTRATION_ID = "4002";
    public static final String BANDWIDTH_EXCEEDED = "4004";
    public static final String INBOX_EXCEEDED = "4005";
    public static final String OUTBOX_EXCEEDED = "4006";
    //transport messages - 5000s
    public static final String DATA_STREAM_ERROR = "5000";
    public static final String PROTOCOL_ERROR = "5001";
    public static final String TRANSPORT_ERROR = "5002";
    public static final String CONNECTION_ERROR = "5003";
    //Unspecific Errors - 6000
    public static final String MALFORMED_REQUEST = "6000";
    public static final String GENERIC_ERROR = "6001";
    //XML processing - 7000s
    public static final String XML_PARSER_ERROR = "7000";
    //Notification - 8000s
    public static final String ROUTE_NOTIFICATION_FAILED = "8000";
    public static final String NO_CONNECTIONS_FOUND = "8001";
    public static final String UNSUPPORTED_NETWORK = "8002";
    //Competitions - 9000s
    public static final String NO_SUCH_COMPETITION = "9000";
    public static final String COMPETITION_NOT_OVER = "9001";
    public static final String COMPETITION_ENDED = "9002";
    public static final String WINNERS_ALREADY_DRAWN = "9003";
    public static final String WINNERS_NOT_FOUND = "9004";
    public static final String MISSING_COMPETITION_ID = "9005";
    public static final String PARTICIPATION_LIMIT_EXCEEDED = "9006";
    public static final String EMPTY_VOTE = "9007";
    public static final String ALREADY_VOTED = "9008";
    public static final String VOTES_RECEIVED = "9009";
    //Services - 10000s
    public static final String NOT_REGISTERED = "10000";
    public static final String NO_SUCH_SERVICE = "10001";
    public static final String NO_URL_FOR_SERVICE = "10002";
    public static final String INVALID_SERVICE_REQUEST = "10003";
    public static final String MISSING_INVALID_PROV_ID = "10004";
    public static final String MISSING_INVALID_KEYWORD = "10005";
    public static final String REGISTRATION_FAILED = "10006";
    public static final String ALREADY_REGISTERED = "10007";
    public static final String REGISTRATION_COMPLETED = "10008";
    //Billing - 11000s
    public static final String BILLING_MECH_FAILURE = "11000";
    public static final String NO_BILLING_URL = "11001";
    public static final String INSUFFICIENT_CREDIT_ON_PIN = "11002";
    private ResourceBundle rbundle;
    private String language;

    public Feedback() throws Exception {
        Locale locale = new Locale("en");
        rbundle = ResourceBundle.getBundle("feedback", locale);
        language = locale.getLanguage();
    }

    public Feedback(String languageCode) throws Exception {
        Locale locale = new Locale(languageCode);
        rbundle = ResourceBundle.getBundle("feedback", locale);
        language = locale.getLanguage();
    }

    public Feedback(String languageCode, String countryCode) throws Exception {
        Locale locale = new Locale(languageCode, countryCode);
        rbundle = ResourceBundle.getBundle("feedback", locale);
    }

    public String getLanguage() {
        return language;
    }

    //internal messages
    public String getCode(String key) {

        try {
            return this.rbundle.getString(key + "_CODE");
        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + Feedback.class + "]\tERROR\tGetting code: " + e.getMessage());
        }
        return null;
    }

    public String getType(String key) {

        try {
            return this.rbundle.getString(key + "_TYPE");
        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + Feedback.class + "]\tERROR\tGetting type: " + e.getMessage());
        }
        return null;
    }

    public String getTechnicalDescription(String key) {       
        try {
            return this.rbundle.getString(key + "_TECH_DESC");
        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + Feedback.class + "]\tERROR\tGetting technical description: " + e.getMessage());
        }
        return null;
    }

    public String getUserFriendlyDescription(String key) {
        
        try {
            return this.rbundle.getString(key + "_USER_DESC");
        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + Feedback.class + "]\tERROR\tGetting user friendly description: " + e.getMessage());
        }
        return null;
    }

    public String getValue(String key) {
       
        try {
            return this.rbundle.getString(key);
        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + Feedback.class + "]\tERROR\tGetting value: " + e.getMessage());
        }
        return null;
    }

    public String formDefaultMessage(String key) {
        try {
            return getType(key) + " " + getCode(key) + ":" + getTechnicalDescription(key);
        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + Feedback.class + "]\tERROR\tForming default message: " + e.getMessage());
        }
        return null;
    }

    //livescore-specific messages
    public String getLiveScoreResultType(String key) {
        try {
            return this.rbundle.getString("LS_RT_" + key);
        } catch (Exception e) {
           System.out.println(new Date() + "\t[" + Feedback.class + "]\tERROR\tGetting livescore result type: " + e.getMessage());
        }
        return null;
    }

    public String getLiveScoreStatusCode(String key) {
        try {
            return this.rbundle.getString("LS_SC_" + key);
        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + Feedback.class + "]\tERROR\tGetting livescore status code: " + e.getMessage());
        }
        return null;
    }

    public String getLiveScorePeriodCode(String key) {
       
        try {
            return this.rbundle.getString("LS_PC_" + key);
        } catch (Exception e) {
           System.out.println(new Date() + "\t[" + Feedback.class + "]\tERROR\tGetting livescore period code: " + e.getMessage());
        }
        return null;
    }
}
