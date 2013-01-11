package com.rancard.util;

import java.io.*;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class Config {
    
    
    private String location = "WEB-INF/conf/rmcs.properties";
    private Properties defaultProps = new Properties();
    /**
     * Set the base for subsequent call's to openConfigFileRead()
     * and openConfigFileWrite()
     *
     * @param location a file path or url
     */
    public void setLocation(String location) throws Exception {
  
        this.location = location;
        try {

            load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void init() throws Exception {
        if (this.location == null) {
            throw new Exception("'location'  of config file must be set");
        }else{
        load();
        }
    }

    public java.util.Properties load() throws IOException {

        // create and load default properties

        FileInputStream in = new FileInputStream(this.location);
        defaultProps.load(in);
        in.close();

        return defaultProps;
    }

    public void Store() throws IOException {

        // create and load default properties
        FileOutputStream out = new FileOutputStream(this.location);

        defaultProps.store(out,
                           "Properties modified on" +
                           new java.util.Date().toString());
        out.close();

    }

    public String valueOf(String key) {
        return (String)this.defaultProps.getProperty(key);
    }

    public void changeValueOf(String key, String newValue) {
        this.defaultProps.put(key, newValue);
    }
    
    //note that configName is as defined in the context.xml
    public String getValue (String configName, String key) throws Exception {

        try {
            Context ctx = new InitialContext ();
            Config tempConfig = (Config) ctx.lookup ("java:comp/env/bean/" + configName);

            if (tempConfig != null) {
                return tempConfig.valueOf (key);
            } else {
                return null;
            }
        } catch (NamingException e) {
            throw new Exception (e.getMessage ());
        }
    }

}
