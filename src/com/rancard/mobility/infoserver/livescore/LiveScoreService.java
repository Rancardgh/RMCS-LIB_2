/*
 * LiveScoreService.java
 *
 * Created on January 4, 2007, 12:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore;

import com.rancard.mobility.infoserver.common.services.UserService;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Messenger
 */
public class LiveScoreService extends UserService {
    
    private String[] liveScoreServiceIds;
    
    /** Creates a new instance of LiveScoreService */
    public LiveScoreService () {
        super ();
        this.liveScoreServiceIds = null;
    }
    
    public LiveScoreService (String serviceType, String keyword, String accountId, String serviceName, String defaultMessage, String[] liveScoreServiceIds) {
        super (serviceType, keyword, accountId, serviceName, defaultMessage);
        this.liveScoreServiceIds = liveScoreServiceIds;
    }
    
    public LiveScoreService (String serviceType, String keyword, String accountId, String serviceid, String defaultMessage, String command, String allowedShortcodes, String allowedSiteTypes,
            String pricing, boolean isBasic, String[] liveScoreServiceIds) {
        super (serviceType, keyword, accountId, serviceid, defaultMessage, command, allowedShortcodes, allowedSiteTypes, pricing, isBasic);
        this.liveScoreServiceIds = liveScoreServiceIds;
    }
    
    public Timestamp now () {
        return new Timestamp (java.util.Calendar.getInstance ().getTime ().getTime ());
    }

    public String[] getLiveScoreServiceIds () {
        return liveScoreServiceIds;
    }

    public void setLiveScoreServiceIds (String[] liveScoreServiceIds) {
        this.liveScoreServiceIds = liveScoreServiceIds;
    }
}
