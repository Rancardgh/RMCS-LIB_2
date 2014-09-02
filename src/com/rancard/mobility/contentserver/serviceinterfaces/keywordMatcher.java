package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.DConnect;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class keywordMatcher {
    String SQL;
    String synonym;
    Connection con = null;
    PreparedStatement prepstat = null;
    ArrayList<String> keywords = new ArrayList();
    double bestGuess = 0.0D;

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
        return 2.0D * intersection / union;
    }

    private static ArrayList wordLetterPairs(String str) {
        ArrayList allPairs = new ArrayList();

        String[] words = str.split("s");
        for (int w = 0; w < words.length; w++) {
            String[] pairsInWord = letterPairs(words[w]);
            allPairs.addAll(Arrays.asList(pairsInWord));
        }
        return allPairs;
    }

    private static String[] letterPairs(String str) {
        int numPairs = str.length() - 1;
        String[] pairs = new String[numPairs];
        for (int i = 0; i < numPairs; i++) {
            pairs[i] = str.substring(i, i + 2);
        }
        return pairs;
    }

    private static String search(String keyword, ArrayList<String> list) {
        double bestGuess = 0.0D;
        String synonym = "";
        for (int i = 0; i < list.size(); i++) {
            double result = compareStrings(keyword, (String) list.get(i));
            if ((result > bestGuess) && (result > 0.4D)) {
                bestGuess = result;
                synonym = (String) list.get(i);
            }
        }
        return synonym;
    }

    public static String wordMatch(String keyword, String smsc) {
        String synonym = "";

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
            String query = "select keyword,service_name,tags,default_message from service_definition where account_id in (select list_id from cp_connections where conn_id like '" + smsc + "%') and service_type='14'";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                keywords.add(rs.getString(1));
                serviceName.add(rs.getString(2));
                tags.add(rs.getString(3));
                defaultMessage.add(rs.getString(4));
            }
            for (int i = 0; i < tags.size(); i++) {
                String words = (String) tags.get(i);
                if ((words != null) && (!words.isEmpty())) {
                    String[] array = words.split(",");
                    newTemp.addAll(Arrays.asList(array));
                }
            }
            String option1 = search(keyword, keywords);
            if ((option1 == null) || (option1.isEmpty())) {
                String option2 = search(keyword, serviceName);
                if ((option2 == null) || (option2.isEmpty())) {
                    String option3 = search(keyword, newTemp);
                    if ((option3 == null) || (option3.isEmpty())) {
                        String option4 = search(keyword, defaultMessage);
                        if ((option4 == null) || (option4.isEmpty())) {
                            synonym = option4;
                        } else {
                            synonym = option4;
                        }
                    } else {
                        synonym = option3;
                    }
                } else {
                    synonym = option2;
                }
            }
            return option1;
        } catch (Exception e) {
            System.out.println(new Date() + ": [keyMatcher[ERROR]]" + e.getMessage());
        } finally {
            try {
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(new Date() + ": [keyMatcher[ERROR]]" + e.getMessage());
            }
        }
        return synonym;
    }
}



/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar

 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.keywordMatcher

 * JD-Core Version:    0.7.0.1

 */