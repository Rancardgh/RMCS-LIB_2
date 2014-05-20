package com.rancard.util;

public abstract interface ProcessListener
{
  public abstract void start();
  
  public abstract void processRead(int paramInt);
  
  public abstract void error(String paramString);
  
  public abstract void done();
}


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.ProcessListener
 * JD-Core Version:    0.7.0.1
 */