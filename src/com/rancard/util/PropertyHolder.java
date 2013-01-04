package com.rancard.util;

import com.rancard.common.Config;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*import java.io.InputStream;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 */

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class PropertyHolder {

  private static Properties props;
  private static String filePath;

  public static void setProperties(Properties value) {
    props = value;
  }

  public static boolean initialize(String propertyFile) {
    filePath = propertyFile;
    return initialize();
  }

  public static boolean initialize() {
    props = new Properties();
    try {
      props.load(new java.io.FileInputStream(filePath));
    }
    catch (Exception e) {
      return false;
    }
    return true;
  }

  public static boolean save() {
    try {
      java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath);
      props.store(fos, "CMS 3.0 PROPERTIES FILE");
      fos.close();
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static String get(final String key) {
    return props.getProperty(key);
  }

  public static void set(final String key, final String value) {
    props.setProperty(key, value);
  }
  
  public static String getPropsValue (String key) throws Exception {
        String value = null;
        try {
            Context ctx = new InitialContext ();
            Context envCtx = (Context) ctx.lookup ("java:comp/env");
            Config appConfig = (Config) envCtx.lookup ("bean/rmcsConfig");
            
            value = appConfig.valueOf (key);
            
        } catch (NamingException e) {
            value = "";
        }
        
        return value;
    }

  public static void main(String[] s) {
    filePath =
        "C:\\dev\\project\\rancardCms3.0\\CMS3.0\\nia\\WEB-INF\\cms.properties";
    initialize();
    while (props.elements().hasMoreElements()) {
      System.out.println( (String) props.elements().nextElement());

    }

    System.out.println(save());
  }

}
