package com.rancard.util;

/**
 * <p>Title: National Identification authority website and cms</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: Rancard Soltuions Ltd</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Display {
  public Display() {
  }

  public static String jspPrint(String str) {
    String ret = "";
    if ( (str != null) && (!str.equals("null"))) {
      ret = str;
    }
    return ret;
  }
}
