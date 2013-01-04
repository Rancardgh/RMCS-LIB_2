/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author rancard
 */
public class keywordMatcher {

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
    private static String search(String keyword, ArrayList<String> list) {
        double bestGuess = 0;
        String synonym = "";
        for (int i = 0; i < list.size(); i++) {
            double result = compareStrings(keyword, list.get(i));
            if (result > bestGuess && result > 0.4) {
                bestGuess = result;
                synonym = list.get(i);
            }
        }
        return synonym;
    }

    //Returns a keyword if a match is found.
    public static String wordMatch(String keyword, String smsc) {
        String query,synonym="";
        String option1,option2,option3,option4;
        ArrayList<String> keywords = new ArrayList();
        ArrayList<String> serviceName = new ArrayList();
        ArrayList<String> tags = new ArrayList();
        ArrayList<String> defaultMessage = new ArrayList();
        ArrayList<String> newTemp = new ArrayList();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DConnect.getConnection();
            query = "select keyword,service_name,tags,default_message from service_definition where account_id in (select list_id from cp_connections where conn_id like '" + smsc + "%') and service_type='14'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            //load the various column results into their various arrayLists
            //for example all results under keyword in the select query will be dumped into the keywords ArrayList
            while (rs.next()){
                keywords.add(rs.getString(1));
                serviceName.add(rs.getString(2));
                tags.add(rs.getString(3));
                defaultMessage.add(rs.getString(4));
            }
            //some of the tags contain null values. Remove null values so as to avoid exceptions
            for (int i = 0; i < tags.size(); i++) {
                String words = tags.get(i);
                if (words == null || words.isEmpty()) {
                    continue;
                }
                String[] array = words.split(",");
                newTemp.addAll(Arrays.asList(array));
            }
            
            /**start search for a keyword match in the keywords ArrayList
             * if nothing is found move unto the next arraylist that contains words from service_name
             * and so on..
             *  if something is found, assign that to synonym variable,
             */
            
            option1 = search(keyword,keywords);
            if(option1==null || option1.isEmpty()){
                option2 = search(keyword,serviceName);
                if(option2==null || option2.isEmpty()){
                    option3 = search(keyword,newTemp);
                    if(option3==null || option3.isEmpty()){
                        option4 = search(keyword,defaultMessage);
                        if(option4==null || option4.isEmpty()){
                            synonym = option4;
                        }else{
                            synonym = option4;
                        }
                    }else{
                        synonym = option3;
                    }
                }else{
                    synonym = option2;
                }
            }else{
                synonym = option1;
            }
        } catch (Exception e) {
            System.out.println(new java.util.Date() + ": [keyMatcher[ERROR]]" + e.getMessage());

        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(new java.util.Date() + ": [keyMatcher[ERROR]]" + e.getMessage());
            }
        }
        return synonym;
    }
}
