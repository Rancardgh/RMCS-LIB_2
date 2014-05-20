package com.rancard.mobility.common;

public abstract interface PushDriver
{
  public static final String SEVENBIT = "0";
  public static final String EIGHTBIT = "1";
  public static final String UCS2 = "2";
  public static final String SENDSMSTEXTRESPONSE = "";
  public static final String SENDSMSBINARYRESPONSE = "";
  public static final String SENDPUSHRESPONSE = "";
  public static final String SENDSMSTEXTMULTIRESPONSE = "";
  
  public abstract String sendSMSTextMessage(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8);
  
  public abstract String sendSMSTextMessage(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7);
  
  public abstract String sendSMSTextMessage(String[] paramArrayOfString, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7);
  
  public abstract String sendSMSBinaryMessage(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11);
  
  public abstract String sendWAPPushMessage(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9);
  
  public abstract String processResponse(String paramString)
    throws Exception;
}


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.common.PushDriver
 * JD-Core Version:    0.7.0.1
 */