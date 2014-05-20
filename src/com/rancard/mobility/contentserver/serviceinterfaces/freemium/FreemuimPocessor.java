package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

import java.util.List;

public abstract interface FreemuimPocessor
{
  public abstract boolean freemuimExists(String paramString)
    throws Exception;
  
  public abstract boolean freemuimExists(String paramString1, String paramString2)
    throws Exception;
  
  public abstract List<Freemium> getFreemiums(String paramString1, String paramString2)
    throws Exception;
  
  public abstract List<Freemium> getActiveFreemiums(String paramString1, String paramString2)
    throws Exception;
  
  public abstract Freemium getFreemium(String paramString)
    throws Exception;
  
  public abstract boolean process(String paramString1, String paramString2, String paramString3)
    throws Exception;
}


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.FreemuimPocessor
 * JD-Core Version:    0.7.0.1
 */