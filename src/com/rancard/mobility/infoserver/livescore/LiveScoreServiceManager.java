/*
 * LiveScoreServiceManager.java
 *
 * Created on January 4, 2007, 4:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore;

import com.rancard.common.Feedback;
import com.rancard.mobility.infoserver.common.services.UserService;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * @author Messenger
 */
public class LiveScoreServiceManager {
    
    //services
    public static void createLiveScoreService(LiveScoreService service) throws Exception {
        LiveScoreServiceDB.createLiveScoreService(service);
    }
    
    public static void updateLiveScoreService(String keyword, String account_id, String serviceName) throws Exception {
        LiveScoreServiceDB.updateLiveScoreServiceName(keyword, account_id, serviceName);
    }
    public static void updateSubscriptionStatus (String gameId, int subStatus, int gameStatus) throws Exception {
    
        LiveScoreFixtureDB.updateSubscriptionStatus(gameId, subStatus, gameStatus);
    }
    
    public static LiveScoreService viewLiveScoreService(String keyword, String accountId) throws Exception {
        return LiveScoreServiceDB.viewLiveScoreService(keyword, accountId);
    }
    
    public static ArrayList viewAllLiveScoreLeagues(String accountId) throws Exception {
        return LiveScoreServiceDB.viewAllLiveScoreLeagues(accountId);
    }
    
    public static void deleteLiveScoreService(String keyword, String account_id) throws Exception {
        LiveScoreServiceDB.deleteLiveScoreService(keyword, account_id);
    }
    
    public static ArrayList getAccount_KeywordPairForService(String liveScoreServiceID) throws Exception {
        return LiveScoreServiceDB.getAccount_KeywordPairForService(liveScoreServiceID);
    }
    
    public static String getKeywordForService(String liveScoreServiceID, String accountId) throws Exception {
        return LiveScoreServiceDB.getKeywordForService(liveScoreServiceID, accountId);
    }
    
    public static String[][] getAvailableLiveScoreServices() throws Exception {
        return LiveScoreServiceDB.getAvailableLiveScoreServices();
    }
    
    public static String[][] getAvailableLiveScoreServices(String accountId) throws Exception {
        return LiveScoreServiceDB.getAvailableLiveScoreServices(accountId);
    }
    
    public static String[] subscribeToLiveScoreServices(String accountId, String msisdn, int numOfDays,int billingType) throws Exception {
        return LiveScoreServiceDB.subscribeToLiveScoreServices(accountId, msisdn, numOfDays, billingType);
    }
    
    public static boolean isRegisteredToLiveScoreServices(String accountId, String msisdn) throws Exception {
        return LiveScoreServiceDB.isRegisteredToLiveScoreServices(accountId, msisdn);
    }
    
     public static boolean isRegisteredToLiveScoreServices(String accountId, String msisdn, int status) throws Exception {
        return LiveScoreServiceDB.isRegisteredToLiveScoreServices(accountId, msisdn, status);
    }
    
      public static boolean isMultiShortCodeLivesScore (String accountId) throws Exception {
        return LiveScoreServiceDB.isMultiShortCodeLivesScore(accountId);
      }
    
    public static String manageNextLivescoreSubscription(Date today) throws Exception {
        return LiveScoreServiceDB.manageNextLivescoreSubscription(today);
    }
     public static int manageNextLivescoreSubscription (java.util.Date today, String accountId) throws Exception {
         return LiveScoreServiceDB.manageNextLivescoreSubscription(today, accountId);
     }
    public static UserService viewLiveScoreSubscriptionService(String accountId) throws Exception {
        return LiveScoreServiceDB.viewLiveScoreSubscriptionService(accountId);
    }
    
    public static UserService viewHeadLiveScoreService(String accountId) throws Exception {
        return LiveScoreServiceDB.viewHeadLiveScoreService(accountId);
    }
    
    //fixtures
    public static void createFixture(LiveScoreFixture game) throws Exception {
        LiveScoreFixtureDB.createFixture(game);
    }
    
    public static HashMap viewAllFixturesForDate(String date, int status) throws Exception {
        return LiveScoreFixtureDB.viewAllFixturesForDate(date, status);
    }
    public static HashMap viewAllFixturesForDateGroupByCP(String date, int status, int notifStatus) throws Exception {
        return LiveScoreFixtureDB.viewAllFixturesForDateGroupByCP(date, status, notifStatus);
    }
    
    public static ArrayList viewDistinctGameTimesForDate(Date date, int status) throws Exception {
        return LiveScoreFixtureDB.viewDistinctGameTimesForDate(date, status);
    }
    
    public static HashMap viewAllActiveFixturesForDate(String date) throws Exception {
        return LiveScoreFixtureDB.viewAllActiveFixturesForDate(date);
    }
    
    public static HashMap viewAllActiveFixturesForDate(String accountId, String date) throws Exception {
        return LiveScoreFixtureDB.viewAllActiveFixturesForDate(accountId, date);
    }
    
    public static ArrayList viewAllActiveFixturesInLeague(String date, String keyword, String accountId) throws Exception {
        return LiveScoreFixtureDB.viewAllActiveFixturesInLeague(date, keyword, accountId);
    }
    public static ArrayList viewAllActiveFixturesForCPInLeague(String date, String keyword, String accountId) throws Exception {
        return LiveScoreFixtureDB.viewAllActiveFixturesForCPInLeague(date, keyword, accountId);
    }
    
    public static ArrayList viewAllActiveFixturesInAllLeagues(String date) throws Exception {
        return LiveScoreFixtureDB.viewAllActiveFixturesInAllLeagues(date);
    }
    
    public static ArrayList viewFixtures(String keyword,String accountId, String date) throws Exception {
        return LiveScoreFixtureDB.viewFixtures(keyword,accountId, date);
    }
    
    
    public static ArrayList viewAllActiveFixturesInLeague(String date, String keyword, String accountId, String team) throws Exception {
        return LiveScoreFixtureDB.viewAllActiveFixturesInLeague(date, keyword, accountId, team);
    }
    public static ArrayList viewAllActiveFixturesForCPInLeague(String date, String keyword, String accountId, String team) throws Exception {
        return LiveScoreFixtureDB.viewAllActiveFixturesForCPInLeague(date, keyword, accountId, team);
    }
    
    public static LiveScoreFixture viewFixture(String gameId) throws Exception {
        return LiveScoreFixtureDB.viewFixture(gameId);
    }
    public static LiveScoreFixture viewFixtureForCP(String gameId, String accountId) throws Exception {
        return LiveScoreFixtureDB.viewFixtureForCP(gameId, accountId);
    }
    public static LiveScoreFixture viewFixture(String gameId, String date) throws Exception {
        return LiveScoreFixtureDB.viewFixture(gameId, date);
    }
    
    public static LiveScoreFixture viewFixtureByAlias(String alias) throws Exception {
        return LiveScoreFixtureDB.viewFixtureByAlias(alias);
    }
    public static LiveScoreFixture viewFixtureForCPByAlias(String alias, String accountId)  throws Exception {
        return LiveScoreFixtureDB.viewFixtureForCPByAlias(alias, accountId);
    }
    public static void deleteFixture(String keyword) throws Exception {
        LiveScoreFixtureDB.deleteFixture(keyword);
    }
    
    public static void updateFixture(LiveScoreFixture update) throws Exception {
        LiveScoreFixtureDB.updateFixture(update);
    }
    
    public static void updateFixture(String match_id, int status, String date) throws Exception {
        LiveScoreFixtureDB.updateFixture(match_id, status, date);
    }
    
    public static void updateFixture(String match_id, String homeScore, String awayScore, String date) throws Exception {
        LiveScoreFixtureDB.updateFixture(match_id, homeScore, awayScore, date);
    }
    
    public static void updateFixture(String match_id, String homeScore, String awayScore, int status, String date) throws Exception {
        LiveScoreFixtureDB.updateFixture(match_id, homeScore, awayScore, status, date);
    }
    
    public synchronized static void subscribeForGame(String accountId, String gameId, String msisdn) throws Exception {
        LiveScoreFixtureDB.subscribeForGame(accountId, gameId, msisdn);
    }
    
    public synchronized  static void unsubscribeFromGame(String msisdn, String gameId, String accountId) throws Exception {
        LiveScoreFixtureDB.unsubscribeFromGame(msisdn, gameId, accountId);
    }
    
    public static String viewGameIdForAlias(String alias) throws Exception {
        return LiveScoreFixtureDB.viewGameIdForAlias(alias);
    }
    
    public static String viewAliasForGameId(String gameId) throws Exception {
        return LiveScoreFixtureDB.viewAliasForGameId(gameId);
    }
    
    public static boolean canAccommodateSubscriber(String accountId, String msisdn, LiveScoreFixture game) throws Exception {
        return LiveScoreServiceDB.canAccommodateSubscriber(accountId, msisdn, game);
    }
    
    //updates
    public static boolean createUpdate(LiveScoreUpdate update) throws Exception {
        return LiveScoreUpdateDB.createUpdate(update);
    }
    
    public static String viewUpdateMessage(String updateId, String language) throws Exception {
        return LiveScoreUpdateDB.viewUpdateMessage(updateId, language);
    }
    
    public static LiveScoreUpdate viewUpdate(String updateId) throws Exception {
        return LiveScoreUpdateDB.viewUpdate(updateId);
    }
    public static LiveScoreUpdate viewUpdate(String updateId, int gameStatus) throws Exception {
        return LiveScoreUpdateDB.viewUpdate(updateId, gameStatus);
    }
    
    public static ArrayList viewAllUpdates(String gameId) throws Exception {
        return LiveScoreUpdateDB.viewAllUpdates(gameId);
    }
    
    public static ArrayList viewAllUpdates(String gameId, String date) throws Exception {
        return LiveScoreUpdateDB.viewAllUpdates(gameId, date);
    }
    
    public static ArrayList viewSubscribersForGame(String accountId, String game_id, String alias) throws Exception {
        return LiveScoreServiceDB.viewSubscribersForGame(accountId, game_id, alias);
    }
    
    public static com.rancard.util.PaginatedList viewSubscribersForGame(String accountId, String game_id, String alias, int [] pagingParams, String [] sortParams) throws Exception {
        return LiveScoreServiceDB.viewSubscribersForGame(accountId, game_id, alias, pagingParams, sortParams);
    }
    
    public static int viewNoSubscribersForGame(String accountId, String game_id)throws Exception {
        return LiveScoreServiceDB.viewNoSubscribersForGame(accountId, game_id);
    }
    
    public static int getBillingTypeForSubscriber(String msisdn, String accountId) throws Exception {
        return LiveScoreServiceDB.getBillingTypeForSubscriber(msisdn,accountId);
    }
    
    
    public static void sendUpdate(String serverUrl, String accountId, String updateId)
    throws HttpException, IOException, Exception{
        
        String url = serverUrl + "sendlivescoreupdate?acctId=" + accountId + "&updateId=" + updateId;
        
        HttpClient client = new HttpClient();
        GetMethod httpGETFORM = new GetMethod(url);
        String resp = "";
        
        try {
            client.executeMethod(httpGETFORM);
        } catch (HttpException e) {
            resp = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage());
            //logging statements
            System.out.println("error response: " + resp);
            //end of logging
        } catch (IOException e) {
            resp = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage());
            //logging statements
            System.out.println("error response: " + resp);
            //end of logging
        } catch (Exception e) {
            //logging statements
            System.out.println("error response: " + e.getMessage());
            //end of logging
        } finally {
            // Release the connection.
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }
    }
    
    //trigger schedules
    public static boolean createTriggerSchedule(Timestamp triggerTime) throws Exception {
        return LiveScoreTriggerScheduleDB.createTriggerSchedule(triggerTime);
    }
    
    public static void updateTriggerSchedule(Timestamp triggerTime) throws Exception {
        LiveScoreTriggerScheduleDB.updateTriggerSchedule(triggerTime);
    }

    
}
