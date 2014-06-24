package com.rancard.mobility.common;


import com.rancard.common.CPConnection;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public abstract class PushDriver {
    private final Logger logger = Logger.getLogger(PushDriver.class.getName());
    private final Map<String, String> params;
    private final CPConnection cnxn;

    public static final String RMS = "rms";
    public static final String KANNEL = "kannel";



    public PushDriver(CPConnection cnxn, Map<String, String> params){
        this.cnxn = cnxn;
        this.params = params;
    }

    public abstract String sendSMSTextMessage() throws IOException;

    public static PushDriver getDriver(CPConnection cnxn, Map<String, String> params) {
        if (cnxn.getDriverType() == null) {
            return null;
        }

        if (cnxn.getDriverType().equalsIgnoreCase(KANNEL)) {
            return new KannelDriver(cnxn, params);
        } else if (cnxn.getDriverType().equals(RMS)) {
            return new RMSDriver(cnxn, params);
        } else {
            return null;
        }

    }

    protected Map<String, String> getParams(){
        return params;
    }

    protected CPConnection getCPConnection(){
        return cnxn;
    }


    protected String getParameterValue(Map<String, String> params, String param) throws IllegalArgumentException{
        String value = params.get(param);

        if(value == null){
            logger.severe(param + "is null.");
            throw new IllegalArgumentException(param +" is null");
        }

        return value;
    }



}
