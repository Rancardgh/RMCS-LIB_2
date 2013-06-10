package com.rancard.common;

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
    
    public Feedback () throws Exception {
        Locale locale = new Locale ("en");
        rbundle = ResourceBundle.getBundle ("feedback", locale);
        language = locale.getLanguage ();
    }
    
    public Feedback (String languageCode) throws Exception {
        Locale locale = new Locale (languageCode);
        rbundle = ResourceBundle.getBundle ("feedback", locale);
        language = locale.getLanguage ();
    }
    
    public Feedback (String languageCode, String countryCode) throws Exception {
        Locale locale = new Locale (languageCode, countryCode);
        rbundle = ResourceBundle.getBundle ("feedback", locale);
    }
    
    public String getLanguage () {
        return language;
    }
    
    //internal messages
    public String getCode (String key) {
        String out = null;
        try{
            out = this.rbundle.getString (key + "_CODE");
        }catch (Exception e){
            out = "";
        }
        return out;
    }
    
    public String getType (String key) {        
        String out = null;
        try{
            out = this.rbundle.getString (key + "_TYPE");
        }catch (Exception e){
            out = "";
        }
        return out;
    }
    
    public String getTechnicalDescription (String key) {
        String out = null;
        try{
            out = this.rbundle.getString (key + "_TECH_DESC");
        }catch (Exception e){
            out = "";
        }
        return out;
    }
    
    public String getUserFriendlyDescription (String key) {
        String out = null;
        try{
            out = this.rbundle.getString (key + "_USER_DESC");
        }catch (Exception e){
            out = "";
        }
        return out;
    }
    
    public String getValue (String key) {
        String out = null;
        try{
            out = this.rbundle.getString (key);
        }catch (Exception e){
            out = "";
        }
        return out;
    }
    
    public String formDefaultMessage (String key) {
        String out = null;
        try{
            out = getType (key) + " " + getCode (key) + ":" + getTechnicalDescription (key);
        }catch (Exception e){
            out = "";
        }
        return out;
    }
    
    //livescore-specific messages
   public String getLiveScoreResultType (String key) {
        String out = null;
        try{
            out = this.rbundle.getString ("LS_RT_" + key);
        }catch (Exception e){
            out = "";
        }
        return out;
    }
    
    public String getLiveScoreStatusCode (String key) {
        String out = null;
        try{
            out = this.rbundle.getString ("LS_SC_" + key);
        }catch (Exception e){
            out = "";
        }
        return out;
    }
    
    public String getLiveScorePeriodCode (String key) {
        String out = null;
        try{
            out = this.rbundle.getString ("LS_PC_" + key);
        }catch (Exception e){
            out = "";
        }
        return out;
    }
    
    /*
    OKs
    public static final String REGISTRATION_COMPLETED = "Ok:";
    public static final String VOTES_RECEIVED = "Ok:";
    public static final String DOWNLOAD_PROCESSED = "Ok:";
    
    //content item messages - 1000s
    public static final String MISSING_INVALID_ITEM_REF = "ERROR 1000:Missing or invalid item reference";
    public static final String NO_CONTENT_AT_LOCATION = "ERROR 1001:Content does not exist at the URL";
    public static final String MISSING_INVALID_CONTENT_TYPE = "ERROR 1002:Missing or Invalid service type";
    public static final String PREVIEW_CREATION_ERROR = "ERROR 1003:Preview cannot be generated for content type";

    //Phone/subscriber messsages - 2000s
    public static final String MISSING_INVALID_MSISDN = "ERROR 2000:Missing or Invalid MSISDN";
    public static final String MISSING_INVALID_PHONE_ID = "ERROR 2001:Missing or invalid phoneId";
    public static final String PHONE_FORMAT_INCOMPATIBILITY = "ERROR 2002:Phone may not support format";
    public static final String PHONE_MATRIX_INIT_ERROR = "ERROR 2003:Could not initialize mobile device matrix";
    public static final String PUSH_PROTOCOL_NOT_SUPPORTED = "ERROR 2004:Phone does not support push protocol";

    //download messages - 3000s
    public static final String MISSING_INVALID_TICKET_ID = "ERROR 3000:Missing or invalid ticket ID";
    public static final String EXPIRED_TICKET_ID = "ERROR 3001:Download ticket has expired";

    //authentication - 4000s
    public static final String INVALID_PASSWORD = "ERROR 4000:Invalid password";
    public static final String INVALID_REQUEST_CREDENTIALS = "ERROR 4001:Could not authenticate request";
    public static final String MISSING_INVALID_PIN = "ERROR 4002:Missing or Invalid PIN";
    public static final String INSUFFICIENT_CREDIT_ON_PIN = "ERROR 4003:PIN has insufficient credit";
    public static final String BANDWIDTH_EXCEEDED = "ERROR 4004:Bandwidth allocated to user has been exceeded";
    public static final String INBOX_EXCEEDED = "ERROR 4005:Inbox limit allocated to user has been exceeded";
    public static final String OUTBOX_EXCEEDED = "ERROR 4006:Outbox limit allocated to user has been exceeded";
    public static final String MISSING_INVALID_REGISTRATION_ID = "ERROR 4007:Missing registration ID";

    //transport messages - 5000s
    public static final String DATA_STREAM_ERROR = "ERROR 5000:Data-streaming failed";
    public static final String PROTOCOL_ERROR = "ERROR 5001:Fatal protocol violation";
    public static final String TRANSPORT_ERROR = "ERROR 5002:Fatal transport error";
    public static final String CONNECTION_ERROR = "ERROR 5003:Could not connect to location";
    
    //Unspecific Errors - 6000
    public static final String MALFORMED_REQUEST = "ERROR 6000:Malformed request";
    public static final String GENERIC_ERROR = "ERROR 6001:Process could not be completed";

    //XML processing - 7000s
    public static final String XML_PARSER_ERROR = "ERROR 7000:Could not parse XML document";

    //Notification - 8000s
    public static final String ROUTE_NOTIFICATION_FAILED = "ERROR 8000:Could not route notification";
    public static final String NO_CONNECTIONS_FOUND = "ERROR 8001:No connections found for provider";
    public static final String UNSUPPORTED_NETWORK = "ERROR 8002:Notification cannot be sent to this network";

    //Competitions - 9000s
    public static final String NO_SUCH_COMPETITION = "ERROR 9000:Competition does not exist";
    public static final String COMPETITION_NOT_OVER = "ERROR 9001:Competition is not over";
    public static final String COMPETITION_ENDED = "ERROR 9002:Competition has ended";
    public static final String WINNERS_ALREADY_DRAWN = "ERROR 9003:Winners have already been drawn";
    public static final String WINNERS_NOT_FOUND = "ERROR 9004:No winners were found";
    public static final String MISSING_COMPETITION_ID = "ERROR 9005:Missing Competition ID";
    public static final String PARTICIPATION_LIMIT_EXCEEDED = "ERROR 9006:Participation limit has been exceeded";

    //Services - 10000s
    public static final String NO_SUCH_SERVICE = "ERROR 10001:Service does not exist";
    public static final String NO_URL_FOR_SERVICE = "ERROR 10002:No URL defined for service";
    public static final String INVALID_SERVICE_REQUEST = "ERROR 10003:Invalid request for service";
    public static final String MISSING_INVALID_PROV_ID = "ERROR 10004:Missing or Invalid Provider ID";
    public static final String MISSING_INVALID_KEYWORD = "ERROR 10005:Missing or Invalid Keyword";
    public static final String REGISTRATION_FAILED = "ERROR 10006:Registration could not be completed";
    public static final String ALREADY_REGISTERED = "ERROR 10007:The subscriber has already been registered";
    public static final String ALREADY_VOTED = "ERROR 10008:The subscriber has already voted";
    public static final String EMPTY_VOTE = "ERROR 10009:Vote is empty";
    public static final String NOT_REGISTERED = "ERROR 10012:The subscriber has not subscribed to this service";
    
    //Billing - 11000s
    public static final String BILLING_MECH_FAILURE = "ERROR 11000:Could not complete billing process";
    public static final String NO_BILLING_URL = "ERROR 11001:No billing URL was found";
    */
}
