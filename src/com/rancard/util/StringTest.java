package com.rancard.util;

public class StringTest {
  public StringTest() {
  }

  String fileName = "some.file.txt.excel.document";

  public int getFileName() {

    return fileName.lastIndexOf(".");

  }

  /**
   * main
   *
   * @param args String[]
   */
  public static void main(String[] args) {
    StringTest thistest = new StringTest();
    System.out.println(thistest.getFileName());
    System.out.println(thistest.fileName.substring(thistest.getFileName()));
  }

}
