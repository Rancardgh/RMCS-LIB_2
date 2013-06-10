package com.rancard.util;

public interface MonitoredProcess {
 public void start();
 public void processRead(int stepsInProcess);
 public void error(String Message);
 public void done();

}


