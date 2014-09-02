package com.rancard.mobility.infoserver.common.services;

import com.rancard.mobility.infoserver.feeds.CPUserFeeds;
import com.rancard.mobility.infoserver.feeds.Feed;
import com.rancard.util.Page;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ServiceManager {
    public static void createService(UserService service)
            throws Exception {
        UserServiceDB.createService(service);
    }

    public static void createServiceExperience(UserServiceExperience serviceExperience)
            throws Exception {
        UserServiceExperienceDB.createServiceExperience(serviceExperience);
    }

    public static void updateService(String serviceType, String defaultMessage, String serviceName, String keyword, String accountId)
            throws Exception {
        UserServiceDB.updateService(serviceType, defaultMessage, serviceName, keyword, accountId);
    }

    public static void deleteService(String keyword, String accountId)
            throws Exception {
        UserServiceDB.deleteService(keyword, accountId);
    }

    public static void deleteService(ArrayList keywords, String accountId)
            throws Exception {
        UserServiceDB.deleteService(keywords, accountId);
    }

    public static UserService viewService(String keyword, String accountId)
            throws Exception {
        return UserServiceDB.viewService(keyword, accountId);
    }

    public static UserServiceExperience viewServiceExperience(String accountId, String siteId, String keyword)
            throws Exception {
        return UserServiceExperienceDB.viewServiceExperience(accountId, siteId, keyword);
    }

    public static void deleteServiceExperience(String accountId, String keyword)
            throws Exception {
        UserServiceExperienceDB.deleteServiceExperience(accountId, keyword);
    }

    public static void deleteServiceExperience(String accountId, ArrayList keywords)
            throws Exception {
        UserServiceExperienceDB.deleteServiceExperience(accountId, keywords);
    }

    public static void updateServiceExperience(String accountId, String keyword, UserServiceExperience serviceExperience)
            throws Exception {
        UserServiceExperienceDB.updateServiceExperience(accountId, keyword, serviceExperience);
    }

    public static void createFeed(Feed feed)
            throws Exception {
        UserServiceDB.createFeed(feed);
    }

    public static Feed viewFeed(String feedId)
            throws Exception {
        return UserServiceDB.viewFeed(feedId);
    }

    public static void deleteFeed(String feedId)
            throws Exception {
        UserServiceDB.deleteFeed(feedId);
    }

    public static void deleteFeed(ArrayList feedIds)
            throws Exception {
        UserServiceDB.deleteFeed(feedIds);
    }

    public static void updateFeed(String update_feed_id, Feed feedItem)
            throws Exception {
        UserServiceDB.updateFeed(update_feed_id, feedItem);
    }

    public static void createCPUserFeedAssociation(CPUserFeeds cpUserFeed)
            throws Exception {
        UserServiceDB.createCPUserFeedAssociation(cpUserFeed);
    }

    public static CPUserFeeds viewCPUserFeed(String account_id, String keyword)
            throws Exception {
        return UserServiceDB.viewCPUserFeed(account_id, keyword);
    }

    public static void deleteCPUserFeed(String account_id, String keyword)
            throws Exception {
        UserServiceDB.deleteCPUserFeed(account_id, keyword);
    }

    public static void deleteCPUserFeed(String account_id, ArrayList keywords)
            throws Exception {
        UserServiceDB.deleteCPUserFeed(account_id, keywords);
    }

    public static void updateCPUserFeed(String update_account_id, String update_keyword, CPUserFeeds cpUserFeed)
            throws Exception {
        UserServiceDB.updateCPUserFeed(update_account_id, update_keyword, cpUserFeed);
    }

    public static void createServiceLabel(String account_id, String keyword, String header, String footer)
            throws Exception {
        UserServiceDB.createServiceLabel(account_id, keyword, header, footer);
    }

    public static HashMap viewServiceLabel(String account_id, String keyword)
            throws Exception {
        return UserServiceDB.viewServiceLabel(account_id, keyword);
    }

    public static void deleteServiceLabel(String account_id, String keyword)
            throws Exception {
        UserServiceDB.deleteServiceLabel(account_id, keyword);
    }

    public static void deleteServiceLabel(String account_id, ArrayList keywords)
            throws Exception {
        UserServiceDB.deleteServiceLabel(account_id, keywords);
    }

    public static void updateServiceLabel(String update_account_id, String update_keyword, String new_account_id, String new_keyword, String new_header, String new_footer)
            throws Exception {
        UserServiceDB.updateServiceLabel(update_account_id, update_keyword, new_account_id, new_keyword, new_header, new_footer);
    }

    public static void createServiceForwarding(String account_id, String keyword, String url, long timeout, int listen_status)
            throws Exception {
        UserServiceDB.createServiceForwarding(account_id, keyword, url, timeout, listen_status);
    }

    public static Map<String, String> viewServiceForwarding(String account_id, String keyword)
            throws Exception {
        return UserServiceDB.viewServiceForwarding(account_id, keyword);
    }

    public static void deleteServiceForwarding(String account_id, String keyword)
            throws Exception {
        UserServiceDB.deleteServiceForwarding(account_id, keyword);
    }

    public static void deleteServiceForwarding(String account_id, ArrayList keywords)
            throws Exception {
        UserServiceDB.deleteServiceForwarding(account_id, keywords);
    }

    public static void updateServiceForwarding(String update_account_id, String update_keyword, String new_account_id, String new_keyword, String new_url, String new_timeout, String new_listen_status)
            throws Exception {
        UserServiceDB.updateServiceForwarding(update_account_id, update_keyword, new_account_id, new_keyword, new_url, new_timeout, new_listen_status);
    }

    public static UserService viewServiceByAlias(String alias, String accountId)
            throws Exception {
        return UserServiceDB.viewServiceByAlias(alias, accountId);
    }

    public static String viewLastRequestedKeyword(String msisdn, String accountId, String siteId)
            throws Exception {
        return UserServiceDB.viewLastRequestedKeyword(msisdn, accountId, siteId);
    }

    public static int getLastAccessCount(String msisdn, String accountId, String keyword)
            throws Exception {
        return UserServiceDB.getLastAccessCount(msisdn, accountId, keyword);
    }

    public static String viewLastSubscribedKeyword(String msisdn, String accountId)
            throws Exception {
        return UserServiceDB.viewLastSubscribedKeyword(msisdn, accountId);
    }

    public static List viewAllServices(String accountId)
            throws Exception {
        return UserServiceDB.viewAllServices(accountId);
    }

    public static List viewAllServices(String accountId, String serviceType)
            throws Exception {
        return UserServiceDB.viewAllServices(accountId, serviceType);
    }

    public static List<UserService> viewAllServices(String accountId, String serviceType, String command)
            throws Exception {
        return UserServiceDB.viewAllServices(accountId, serviceType, command);
    }

    public static List viewAllServicesOfParentType(String accountId, String parentType)
            throws Exception {
        return UserServiceDB.viewAllServicesOfParentType(accountId, parentType);
    }

    public static List viewAllServicesForType(String serviceType)
            throws Exception {
        return UserServiceDB.viewAllServicesForType(serviceType);
    }

    public static Map populateRoutingTable()
            throws Exception {
        return UserServiceDB.populateRoutingTable();
    }

    public static Map getServiceTable()
            throws Exception {
        return UserServiceDB.getServiceTable();
    }

    public static List<String> getCPIDsForServiceType(String serviceType)
            throws Exception {
        return UserServiceDB.getCPIDsForServiceType(serviceType);
    }

    public static Map<String, String> getCPIDsForServiceType(String serviceType, String command)
            throws Exception {
        return UserServiceDB.getCPIDsForServiceType(serviceType, command);
    }

    public static void updateSubscriptionStatus(String msisdn, String keyword, String accountId, int status)
            throws Exception {
        UserServiceDB.updateSubscriptionStatus(msisdn, keyword, accountId, status);
    }

    public static void subscribeToService(String msisdn, String keyword, String accountId)
            throws Exception {
        UserServiceDB.subscribeToService(msisdn, keyword, accountId);
    }

    public static void subscribeToService(String msisdn, ArrayList keywords, String accountId)
            throws Exception {
        UserServiceDB.subscribeToService(msisdn, keywords, accountId);
    }

    public static void subscribeToService(String msisdn, ArrayList keywords, String accountId, int numOfDays)
            throws Exception {
        UserServiceDB.subscribeToService(msisdn, keywords, accountId, numOfDays);
    }

    public static String[] subscribeToService(String msisdn, ArrayList keywords, String accountId, int numOfDays, int status, int billingType)
            throws Exception {
        return UserServiceDB.subscribeToService(msisdn, keywords, accountId, numOfDays, status, billingType);
    }

    public static void resumeSubscription(String msisdn, ArrayList keywords, String accountId, int status, Date subscriptionDate, Date nextSubscriptionDate)
            throws Exception {
        UserServiceDB.resumeSubscription(msisdn, keywords, accountId, status, subscriptionDate, nextSubscriptionDate);
    }

    public static void unsubscribeToService(String msisdn, String keyword, String accountId)
            throws Exception {
        UserServiceDB.unsubscribeToService(msisdn, keyword, accountId);
    }

    public static void forceUnsubscribe(String keyword, String accountId)
            throws Exception {
        UserServiceDB.forceUnsubscribe(keyword, accountId);
    }

    public static void forceUnsubscribe(String msisdn, ArrayList keywords, String accountId)
            throws Exception {
        UserServiceDB.forceUnsubscribe(msisdn, keywords, accountId);
    }

    public static boolean verifyUser(String msisdn, String regId, String acctId, String keyword)
            throws Exception {
        return UserServiceDB.verifyUser(msisdn, regId, acctId, keyword);
    }

    public static boolean isRegistered(String msisdn, String accountId)
            throws Exception {
        return UserServiceDB.isRegistered(msisdn, accountId);
    }

    public static boolean isRegistered(String msisdn, String accountId, String keyword)
            throws Exception {
        return UserServiceDB.isRegistered(msisdn, accountId, keyword);
    }

    public static boolean isSubscribed(String msisdn, String accountId, String keyword)
            throws Exception {
        return UserServiceDB.isSubscribed(msisdn, accountId, keyword);
    }

    public static HashMap getSubscription(String msisdn, String accountId, String keyword, String alternativeKeyword)
            throws Exception {
        return UserServiceDB.getSubscription(msisdn, accountId, keyword, alternativeKeyword);
    }

    public static HashMap getRecentUnsubscription(String msisdn, String keyword, String accountID, Date now)
            throws Exception {
        return UserServiceDB.getRecentUnsubscription(msisdn, keyword, accountID, now);
    }

    public static boolean hasRecentUnsubscription(String msisdn, String keyword, String accountID)
            throws Exception {
        return UserServiceDB.hasRecentUnsubscription(msisdn, keyword, accountID);
    }

    public static boolean isMonthlySubscriber(String msisdn, String accountId)
            throws Exception {
        return UserServiceDB.isMonthlySubscriber(msisdn, accountId);
    }

    public static int[] updateServicesRSSFeeds(String accountId, String keyword, ArrayList feedIds)
            throws Exception {
        return UserServiceDB.updateServiceRSSFeeds(accountId, keyword, feedIds);
    }

    public static ArrayList viewServiceRSSFeeds(String accountId, String keyword)
            throws Exception {
        return UserServiceDB.viewServiceRSSFeeds(accountId, keyword);
    }

    public static ArrayList getKeywordsOfBasicServices(String accountId)
            throws Exception {
        return UserServiceDB.getKeywordsOfBasicServices(accountId);
    }

    public static ArrayList getKeywordsUserSubscribedTo(String msisdn, String accountId)
            throws Exception {
        return UserServiceDB.getKeywordsUserSubscribedTo(msisdn, accountId);
    }

    public static ArrayList getKeywordsUserSubscribedTo(String msisdn, String accountId, ArrayList<String> keywordList)
            throws Exception {
        return UserServiceDB.getKeywordsUserSubscribedTo(msisdn, accountId, keywordList);
    }

    public static Page viewSubscribers(String csid, String[] keywords, Timestamp startdate, Timestamp enddate, int start, int count)
            throws Exception {
        return UserServiceDB.viewSubscribers(csid, keywords, startdate, enddate, start, count);
    }

    public static String[] viewAllSubscribers(String csid, String keyword)
            throws Exception {
        return UserServiceDB.viewAllSubscribers(csid, keyword);
    }

    public static String[] viewAllSubscribersWithStatus(String csid, String keyword, int status)
            throws Exception {
        return UserServiceDB.viewAllSubscribersWithStatus(csid, keyword, status);
    }

    public static HashMap viewSubscribersGroupByNetworkPrefix(String accountid, String keyword, int status)
            throws Exception {
        return UserServiceDB.viewSubscribersGroupByNetworkPrefix(accountid, keyword, status);
    }

    public static HashMap viewSubscribersGroupByNetworkPrefix(String accountid, String keyword, String alias, int status)
            throws Exception {
        return UserServiceDB.viewSubscribersGroupByNetworkPrefix(accountid, keyword, alias, status);
    }

    public static HashMap viewSubscribersGroupByNetworkPrefix(String accountid, String keyword, int start, int count)
            throws Exception {
        return UserServiceDB.viewSubscribersGroupByNetworkPrefix(accountid, keyword, start, count);
    }

    public static ArrayList viewTempSubscribersGroupByNetworkPrefix(String accountid, String keyword, String processId)
            throws Exception {
        return UserServiceDB.viewTempSubscribersGroupByNetworkPrefix(accountid, keyword, processId);
    }

    public static ArrayList viewTempSubscribersGroupByNetworkPrefix(String accountid, String processId)
            throws Exception {
        return UserServiceDB.viewTempSubscribersGroupByNetworkPrefix(accountid, processId);
    }

    public static HashMap<String, String[]> viewTempSubscribersGroupByNetworkPrefix(String accountId, int status, Date today)
            throws Exception {
        return UserServiceDB.viewTempSubscribersGroupByNetworkPrefix(accountId, status, today);
    }

    public static String findKeywordForMapping(String mapping, String accountId)
            throws Exception {
        return UserServiceDB.findKeywordForMapping(mapping, accountId);
    }

    public static boolean stoppedB4SubscriptionEnded(String msisdn, String keyword, String accountID, Date now)
            throws Exception {
        return UserServiceDB.stoppedB4SubscriptionEnded(msisdn, keyword, accountID, now);
    }

    public static void createTransaction(UserServiceTransaction trans)
            throws Exception {
        UserServiceTransactionDB.createTransaction(trans);
    }

    public static void updateTransaction(String transId, int isCompleted, int isBilled)
            throws Exception {
        UserServiceTransactionDB.updateTransaction(transId, isCompleted, isBilled);
    }

    public static void deleteTransaction(String transId)
            throws Exception {
        UserServiceTransactionDB.deleteTransaction(transId);
    }

    public static UserServiceTransaction viewTransaction(String transId)
            throws Exception {
        return UserServiceTransactionDB.viewTransaction(transId);
    }

    public static void deleteTempSubscriptionRecord(String msisdn, String accountId, String processId)
            throws Exception {
        UserServiceDB.deleteTempSubscriptionRecord(msisdn, accountId, processId);
    }

    public static String viewNextSubscriptionDate(String msisdn, String accountId, String keyword)
            throws Exception {
        return UserServiceDB.viewNextSubscriptionDate(msisdn, accountId, keyword);
    }
}
