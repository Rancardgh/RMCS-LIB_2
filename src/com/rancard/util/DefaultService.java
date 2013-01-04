/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rancard.util;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import java.util.*;
/**
 * This class implements the routing logic to the default service of a CP user. 
 * 
 * @author nii
 */
public class DefaultService {
        
    private static List<String> tempSubscriptionKeywords = Arrays.asList("in","ln","jo",
            "mn","an","gf","fb","qo","bt","CN","is","bn","qt","gh","ifn","hb","fn","epl","spd",
            "isa","voa","voi","bl","qu","sr");
    private static ArrayList<String> subscriptionKeywords = new ArrayList<String>(tempSubscriptionKeywords);
    
    public static String getHelp(String accountId, String msisdn, String dest, String keyword) {
        String helpMessage = "";
        if (accountId.equals("005")) {
            helpMessage = handleTigoHelp(msisdn);
        } else if (accountId.equals("085")) {
            helpMessage = handleGloNigEspnHelp();
        }
        return helpMessage;
    }
    
    private static String handleTigoHelp(String msisdn) {
        String finalMessage = "Sorry! We couldn't find the service you wanted. Please reply to this message with HELP.";
                
        ArrayList<String> currentSubscriptions = new ArrayList<String>();
        try {
            currentSubscriptions = ServiceManager.getKeywordsUserSubscribedTo(msisdn, "005",subscriptionKeywords);
            
            if (currentSubscriptions.size() == 0) {
                finalMessage = "SMS Plus: To receive content, select an option " 
                        + "(eg: African News) from the Tigo SMS plus menu on your phone. " 
                        + "To unsubscribe, send STOP + keyword to 801";
            } else if (currentSubscriptions.size() == 1) {
                String keywordList = expandKeywordList(currentSubscriptions);
                finalMessage = "You are currently subscribed to the following service: " + keywordList + ". "
                        + "If you wish to cancel your subscription, send STOP to 801";
            } else {
                // User subscribed to multiple services
                String keywordList = expandKeywordList(currentSubscriptions);
                String singleKeyword = (keywordList.split(", "))[0];
                finalMessage = "You are currently subscribed to the following services: " + keywordList + ". "
                        + "If you wish to cancel one of the services, send (example) STOP " + singleKeyword 
                        + " to 801";
            }
        } catch (Exception e) {
            // Do some error handling 
        }
        return finalMessage;
    }

    private static String handleGloNigEspnHelp() {
        return "Sorry we cannot identify your keyword. To get the hottest sports " +
                "news from around the world simply send ESPN to 32929";
    }
    
    private static String expandKeywordList(ArrayList<String> keywordList) {
        StringBuffer keyListBuffer = new StringBuffer();
        Iterator<String> keywordIterator = keywordList.iterator();
        while (keywordIterator.hasNext()) {
            keyListBuffer.append(keywordIterator.next().toUpperCase());
            if (keywordIterator.hasNext())
                keyListBuffer.append(", ");
        }
        return keyListBuffer.toString();                
    }
}
