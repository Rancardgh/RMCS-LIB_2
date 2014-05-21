package com.rancard.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class Config {

    private String location = "WEB-INF/conf/rmcs.properties";
    private Properties defaultProps = new Properties();

    public void setLocation(String location)
            throws Exception {
        this.location = location;
        try {
            load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void init()
            throws Exception {
        if (this.location == null) {
            throw new Exception("'location'  of config file must be set");
        }
        load();
    }

    public Properties load()
            throws IOException {
        FileInputStream in = new FileInputStream(this.location);
        this.defaultProps.load(in);
        in.close();

        return this.defaultProps;
    }

    public void Store()
            throws IOException {
        FileOutputStream out = new FileOutputStream(this.location);

        this.defaultProps.store(out, "Properties modified on" + new Date().toString());

        out.close();
    }

    public String valueOf(String key) {
        return this.defaultProps.getProperty(key);
    }

    public void changeValueOf(String key, String newValue) {
        this.defaultProps.put(key, newValue);
    }
}
