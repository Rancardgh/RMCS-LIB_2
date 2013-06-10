package com.rancard.mobility.common;

public abstract class Driver {
    public static final String MKS_SUPPORT = "mks";
    public static final String RMS_SUPPORT = "rms";
    public static final String KANNEL_SUPPORT = "kannel";
    
    //instantiate a driver by specifying a getway URL
    public static PushDriver getPushDriver(String driverName) {
        PushDriver driver = null;
        if(driverName.equals(MKS_SUPPORT))
            driver =  new MksDriver("http://mks.alcatel.pt:8004/ghana/sms/xml");
        else if(driverName.equals(RMS_SUPPORT))
            driver =  new RMSDriver("http://212.96.11.19/rms12");
        else if(driverName.equals(KANNEL_SUPPORT))
            driver =  new KannelDriver("http://192.168.1.249:13013/cgi-bin");
        return driver;
    }
    
    //instantiate a driver using gateway URL in storage
    public static PushDriver getDriver(String driverType, String gatewayUrl) {
        PushDriver driver = null;
        if(driverType.equals(MKS_SUPPORT))
            driver =  new MksDriver(gatewayUrl);
        else if(driverType.equals(RMS_SUPPORT))
            driver =  new RMSDriver(gatewayUrl);
        else if(driverType.equals(KANNEL_SUPPORT))
            driver =  new KannelDriver(gatewayUrl);
        return driver;
    }
}
