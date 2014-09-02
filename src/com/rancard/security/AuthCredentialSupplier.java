package com.rancard.security;

import com.sun.syndication.fetcher.impl.HttpClientFeedFetcher.CredentialSupplier;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;

public class AuthCredentialSupplier implements CredentialSupplier
{
    private String username = null;

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return this.username;
    }

    private String password = null;

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return this.password;
    }

    public AuthCredentialSupplier() {}

    public AuthCredentialSupplier(String username, String password)
    {
        setUsername(username);
        setPassword(password);
    }

    public Credentials getCredentials(String realm, String host)
    {
        String username = getUsername();
        String password = getPassword();
        return new UsernamePasswordCredentials(username, password);
    }
}
