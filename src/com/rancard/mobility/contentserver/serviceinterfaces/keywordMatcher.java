/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.DConnect;
import com.rancard.mobility.infoserver.common.services.UserService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rancard
 */
public class KeywordMatcher {

    String SQL, synonym;
    Connection con = null;
    PreparedStatement prepstat = null;
    ArrayList<String> keywords = new ArrayList();
    double bestGuess = 0;

    private static double compareStrings(String str1, String str2) {
        ArrayList pairs1 = wordLetterPairs(str1.toUpperCase());
        ArrayList pairs2 = wordLetterPairs(str2.toUpperCase());
        int intersection = 0;
        int union = pairs1.size() + pairs2.size();
        for (int i = 0; i < pairs1.size(); i++) {
            Object pair1 = pairs1.get(i);
            for (int j = 0; j < pairs2.size(); j++) {
                Object pair2 = pairs2.get(j);
                if (pair1.equals(pair2)) {
                    intersection++;
                    pairs2.remove(j);
                    break;
                }
            }
        }
        return (2.0 * intersection) / union;
    }

    /**
     * @return an ArrayList of 2-character Strings.
     */
    private static ArrayList wordLetterPairs(String str) {
        ArrayList allPairs = new ArrayList();
        // Tokenize the string and put the tokens/words into an array
        String[] words = str.split("s");
        // For each synonym
        for (int w = 0; w < words.length; w++) {
            // Find the pairs of characters
            String[] pairsInWord = letterPairs(words[w]);
            allPairs.addAll(Arrays.asList(pairsInWord));
            /* for (int p = 0; p < pairsInWord.length; p++) {
             allPairs.add(pairsInWord[p]);
             }*/
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

    //loop through a list containing words that match the keyword a user supplied. 
    private static Map search(String keyword, List<Map> list, String key) {
        double bestGuess = 0;
        Map synonym = null;
        for (int i = 0; i < list.size(); i++) {

            if (key.equalsIgnoreCase("tags")) {
                if (list.get(i).get(key) == null || list.get(i).get(key).toString().equals("")) {
                    continue;
                }

                for (String tag : list.get(i).get(key).toString().split(",")) {
                    double result = compareStrings(keyword, tag);
                    if (result > bestGuess && result > 0.4) {
                        bestGuess = result;
                        synonym = list.get(i);
                    }
                }
            } else {
                double result = compareStrings(keyword, list.get(i).get(key).toString());
                if (result > bestGuess && result > 0.4) {
                    bestGuess = result;
                    synonym = list.get(i);
                }
            }
        }
        return synonym;
    }

    //Returns a keyword if a match is found.
    public static UserService wordMatch(String keyword, String smsc, String shortCode) throws Exception {
        Map synonym = null;
        List<Map> services = new ArrayList<Map>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DConnect.getConnection();
            //select keyword, service_name, tags from service_definition sd 
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
                Map map = new HashMap();
                map.put("keyword", rs.getString("keyword"));
                map.put("service_type", rs.getString("service_type"));
                map.put("account_id", rs.getString("account_id"));
                map.put("service_name", rs.getString("service_name"));
                map.put("tags", rs.getString("tags"));
                map.put("default_message", rs.getString("default_message"));
                map.put("last_updated", rs.getDate("last_updated"));
                map.put("command", rs.getString("command"));
                map.put("allowed_shortcodes", rs.getString("allowed_shortcodes"));
                map.put("allowed_site_types", rs.getString("allowed_site_types"));
                map.put("pricing", rs.getString("pricing"));
                map.put("service_response_sender", rs.getString("service_response_sender"));

                if (rs.getInt("is_basic") == 1) {
                    map.put("is_basic", true);
                } else {
                    map.put("is_basic", false);
                }

                if (rs.getInt("is_subscription") == 1 || rs.getString("is_subscription").equalsIgnoreCase("true")) {
                    map.put("is_subscription", true);
                } else {
                    map.put("is_subscription", false);
                }

                services.add(map);
            }

            System.out.println(new Date() + ": " + KeywordMatcher.class + ": Will search keywords for match");
            synonym = search(keyword, services, "keyword");

            if (synonym == null) {
                System.out.println(new Date() + ": " + KeywordMatcher.class + ": Match not found will now search service name");
                search(keyword, services, "service_name");
            }

            if (synonym == null) {
                System.out.println(new Date() + ": " + KeywordMatcher.class + ": Match not found will now search tags");
                search(keyword, services, "tags");
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
            return new UserService(synonym.get("service_type").toString(), synonym.get("keyword").toString(), synonym.get("account_id").toString(),
                    synonym.get("service_name").toString(), synonym.get("default_message").toString(), synonym.get("command").toString(),
                    synonym.get("allowed_shortcodes").toString(), synonym.get("allowed_site_types").toString(), synonym.get("pricing").toString(),
                    Boolean.getBoolean(synonym.get("is_basic").toString()), Boolean.getBoolean(synonym.get("is_subscription").toString()),
                    synonym.get("service_response_sender").toString());
        } else {
            return null;
        }

    }
}
