package com.rancard.mobility.common;

public interface PushDriver {

    // content type
    public static final String SEVENBIT = "0";
    public static final String EIGHTBIT = "1";
    public static final String UCS2 = "2";
    public static final String SENDSMSTEXTRESPONSE = "";
    public static final String SENDSMSBINARYRESPONSE = "";
    public static final String SENDPUSHRESPONSE = "";
    public static final String SENDSMSTEXTMULTIRESPONSE = "";

    public String sendSMSTextMessage(String to, String from, String text, String username, String password, String conn, String service, String price);

    public String sendSMSTextMessage(String to, String from, String text, String username, String password, String conn, String meta_data);

    public String sendSMSTextMessage(String to[], String from, String text, String username, String password, String conn, String service, String price);

    public String sendSMSBinaryMessage(String to, String from, String ud, String udh, String codingScheme, String format, String username,
                                       String password, String conn, String serviceID, String price);

    public String sendWAPPushMessage(String to, String from, String text, String url, String username, String password, String conn, String service,
                                     String price);

    public String processResponse(String reply) throws Exception;
}
