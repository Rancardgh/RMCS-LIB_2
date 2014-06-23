/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.DConnect;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.util.DateUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 *
 * @author rancard
 */
public class KeywordMatcher {

    private static double compareStrings(String str1, String str2) {
        List<String> pairs1 = wordLetterPairs(str1.toUpperCase());
        List<String> pairs2 = wordLetterPairs(str2.toUpperCase());
        int intersection = 0;
        int union = pairs1.size() + pairs2.size();
        for (String pair1: pairs1) {
            for (String pair2: pairs2) {
                if (pair1.equals(pair2)) {
                    intersection++;
                    pairs2.remove(pair2);
                    break;
                }
            }
        }
        return (2.0 * intersection) / union;
    }

    /**
     * @return an ArrayList of 2-character Strings.
     */
    private static List<String> wordLetterPairs(String str) {
        List<String> allPairs = new ArrayList<String>();
        // Tokenize the string and put the tokens/words into an array
        String[] words = str.split("s");

        // For each synonym
        for (String word: words) {
            // Find the pairs of characters
            String[] pairsInWord = letterPairs(word);
            allPairs.addAll(Arrays.asList(pairsInWord));
        }
        return allPairs;
    }

    /**
     * @return an array of adjacent letter pairs contained in the input string
     */
    private static String[] letterPairs(String str) {
        int numPairs = str.length() - 1;
        String[] pairs = new String[numPairs];
        for (int i = 0; i < numPairs; i++) {
            pairs[i] = str.substring(i, i + 2);
        }
        return pairs;
    }

    public static String search(String keyword, List<String> list, double matchLevel){
        double bestGuess = 0;
        String synonym = null;

        for(String s: list){
           if(s == null || s.equals("")){
               continue;
           }

            double result = compareStrings(keyword, s);
            if (result > bestGuess && result > matchLevel) {
                bestGuess = result;
                synonym = s;
            }
        }

        return synonym;
    }

    //loop through a list containing words that match the keyword a user supplied. 
    private static Map<String, String> search(String keyword, List<Map<String, String>> list, String key, double matchLevel) {
        double bestGuess = 0;
        Map<String, String> synonym = null;
        for (Map<String, String> s: list) {

            if (key.equalsIgnoreCase("tags")) {
                if (s.get(key) == null || s.get(key).equals("")) {
                    continue;
                }

                for (String tag : s.get(key).split(",")) {
                    double result = compareStrings(keyword, tag);
                    if (result > bestGuess && result > matchLevel) {
                        bestGuess = result;
                        synonym = s;
                    }
                }
            } else {
                double result = compareStrings(keyword, s.get(key));
                if (result > bestGuess && result > matchLevel) {
                    bestGuess = result;
                    synonym = s;
                }
            }
        }
        return synonym;
    }
    
    public static UserService wordMatch(String keyword, String smsc, String shortCode) throws Exception {
        return wordMatch(keyword, smsc, shortCode, 0.7);
    }

    //Returns a keyword if a match is found.
    public static UserService wordMatch(String keyword, String smsc, String shortCode, double matchLevel) throws Exception {
        Map<String, String> synonym = null;
        List<Map<String, String>> services = new ArrayList<Map<String, String>>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DConnect.getConnection();
            //select keyword, service_name, tags from service_definition sd 

            if(smsc.endsWith("2")){
                smsc = smsc.substring(0, smsc.length() - 1);
            }

            String query = "select * from service_definition sd "
                    + "INNER JOIN cp_connections cp ON sd.account_id = cp.list_id "
                    + "where sd.service_type='14' AND cp.conn_id like '" + smsc + "%' "
                    + "and (sd.allowed_shortcodes='' or  sd.allowed_shortcodes like '%" + shortCode + "%')";
            System.out.println(new Date() + ": " + KeywordMatcher.class + ": Will run query: " + query);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            //load the various column results into their various arrayLists
            //for example all results under keyword in the select query will be dumped into the keywords ArrayList

            System.out.println(new Date() + ": " + KeywordMatcher.class + ": Sucessfully run query. Will add result to list");
            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("keyword", rs.getString("keyword"));
                map.put("service_type", rs.getString("service_type"));
                map.put("account_id", rs.getString("account_id"));
                map.put("service_name", rs.getString("service_name"));
                map.put("tags", rs.getString("tags"));
                map.put("default_message", rs.getString("default_message"));
                map.put("last_updated", rs.getString("last_updated"));
                map.put("command", rs.getString("command"));
                map.put("allowed_shortcodes", rs.getString("allowed_shortcodes"));
                map.put("allowed_site_types", rs.getString("allowed_site_types"));
                map.put("pricing", rs.getString("pricing"));
                map.put("service_response_sender", rs.getString("service_response_sender"));
                map.put("is_basic", rs.getString("is_basic"));
                map.put("is_subscription", rs.getString("is_subscription"));

                services.add(map);
            }

            System.out.println(new Date() + ": " + KeywordMatcher.class + ": Will search keywords for match");
            synonym = search(keyword, services, "keyword", matchLevel);

            if (synonym == null) {
                System.out.println(new Date() + ": " + KeywordMatcher.class + ": Match not found will now search service name");
                search(keyword, services, "service_name", matchLevel);
            }

            if (synonym == null) {
                System.out.println(new Date() + ": " + KeywordMatcher.class + ": Match not found will now search tags");
                search(keyword, services, "tags", matchLevel);
            }

        } catch (Exception e) {
            System.out.println(new java.util.Date() + ": [keyMatcher[ERROR]]" + e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        }

        if (synonym != null) {
            return new UserService(synonym.get("service_type"), synonym.get("keyword"), synonym.get("account_id"),
                    synonym.get("service_name"), synonym.get("default_message"), synonym.get("command"),
                    Arrays.asList(synonym.get("allowed_shortcodes").split(",")), Arrays.asList(synonym.get("allowed_site_types").split(",")),
                    synonym.get("pricing"), Boolean.valueOf(synonym.get("is_basic")), Boolean.valueOf(synonym.get("is_subscription")),
                    synonym.get("service_response_sender"), DateUtil.convertFromMySQLTimeStamp(synonym.get("last_updated")));
        } else {
            return null;
        }

    }
}
