package com.rancard.util;

import java.text.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DisplayMethods {

  public DisplayMethods() {
  }

  public String clearNull(String strValue) throws Exception {
    if (strValue == null || strValue.equals("null")) {
      strValue = "";
    }

    return strValue;
  }

  // this version returns a blank if strValue is null or "null"
  public String clearNullRHS(String strValue) throws Exception {
    if (strValue == null || strValue.equals("null")) {
      strValue = "&nbsp;";
    }

    return strValue;
  }

  public double clearNull_dbl(Double dblValue) throws Exception {
    if (dblValue == null) {
      dblValue = new Double(0.00);
    }

    return dblValue.doubleValue();
  }

  public int clearNull_int(Integer intValue) throws Exception {
    if (intValue == null) {
      intValue = new Integer(0);
    }

    return intValue.intValue();
  }

  public String dispMoney(Double dblValue) throws Exception {
    NumberFormat numberFormatter = NumberFormat.getNumberInstance();
    NumberFormat nFormat = new DecimalFormat("#,##0.00");

    String strValue = "";

    if (dblValue != null) {
      strValue = nFormat.format(dblValue);
    }
    else {
      strValue = "__________";
    }

    return strValue;
  }

  public String reduceString(String description, Integer display) {
    int i_display = display.intValue();
    int string_len = description.length();

    if (i_display > description.length()) {
      i_display = description.length();
    }

    char description_20[] = new char[i_display];
    description.getChars(0, (i_display), description_20, 0);

    description = "";

    for (int i = 0; i < i_display; i++) {
      description += description_20[i];
    }

    if (display.intValue() < string_len) {
      description = description + "...";
    }

    return description;
  }

}