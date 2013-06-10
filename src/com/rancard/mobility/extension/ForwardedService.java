/*
 * ForwardedService.java
 *
 * Created on January 5, 2007, 11:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.extension;

import com.rancard.mobility.infoserver.common.services.UserService;
import java.sql.Timestamp;

/**
 *
 * @author Messenger
 */
public class ForwardedService extends UserService {
    //options for handling HTTP responses
    public static final int REPLY_WITH_DEFAULT = 2;
    public static final int LISTEN_FOR_REPLY = 1;
    public static final int NEVER_LISTEN_FOR_REPLY = 0;    
    
    //class variables
    private String url;
    private int timeout;
    private int listenStatus;
    
    /** Creates a new instance of ForwardedService */
    public ForwardedService () {
        super ();
        this.url = "";
        this.timeout = 0;
        this.listenStatus = this.NEVER_LISTEN_FOR_REPLY;
    }
    
    public ForwardedService (String serviceType, String keyword, String accountId, String serviceName, String defaultMessage, String url, int timeout, int listenStatus) {
        super (serviceType, keyword, accountId, serviceName, defaultMessage);
        this.url = url;
        this.timeout = timeout;
        this.listenStatus = listenStatus;
    }
    
    public Timestamp now () {
        return new Timestamp (java.util.Calendar.getInstance ().getTime ().getTime ());
    }

    public String getUrl () {
        return url;
    }

    public void setUrl (String url) {
        this.url = url;
    }

    public int getTimeout () {
        return timeout;
    }

    public void setTimeout (int timeout) {
        this.timeout = timeout;
    }

    public int getListenStatus () {
        return listenStatus;
    }

    public void setListenStatus (int listenStatus) {
        this.listenStatus = listenStatus;
    }
    
}
