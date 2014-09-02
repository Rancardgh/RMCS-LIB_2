package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

import java.util.List;

public abstract interface FreemiumDataSourceProvider
{
  public abstract List<String> getList()
    throws Exception;
  
  public abstract boolean inProvider(String paramString)
    throws Exception;
  
  public abstract FreemiumDataSourceType getProviderType();
  
  public abstract String getProviderSource();
}


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.freemium.FreemiumDataSourceProvider
 * JD-Core Version:    0.7.0.1
 */