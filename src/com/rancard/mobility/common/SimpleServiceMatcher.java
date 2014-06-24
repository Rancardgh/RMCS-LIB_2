package com.rancard.mobility.common;

import com.rancard.common.ServiceDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Mustee on 3/30/2014.
 */
public class SimpleServiceMatcher implements ServiceMatcher {
    private Logger logger = Logger.getLogger(SimpleServiceMatcher.class.getName());

    private double compareStrings(String str1, String str2) {
        List<String> pairs1 = wordLetterPairs(str1.toUpperCase());
        List<String> pairs2 = wordLetterPairs(str2.toUpperCase());
        int intersection = 0;
        int union = pairs1.size() + pairs2.size();
        for (String pair1 : pairs1) {
            for (String pair2 : pairs2) {
                if (pair1.equals(pair2)) {
                    intersection++;
                    pairs2.remove(pair2);
                    break;
                }
            }
        }
        return (2.0 * intersection) / union;
    }

    private List<String> wordLetterPairs(String str) {
        List<String> allPairs = new ArrayList<String>();
        // Tokenize the string and put the tokens/words into an array
        String[] words = str.split("s");

        // For each synonym
        for (String word : words) {
            // Find the pairs of characters
            String[] pairsInWord = letterPairs(word);
            allPairs.addAll(Arrays.asList(pairsInWord));
        }
        return allPairs;
    }

    private String[] letterPairs(String str) {
        int numPairs = str.length() - 1;
        String[] pairs = new String[numPairs];
        for (int i = 0; i < numPairs; i++) {
            pairs[i] = str.substring(i, i + 2);
        }
        return pairs;
    }


    @Override
    public ServiceDefinition matchService(String keyword, List<ServiceDefinition> services, double matchLevel) {
        double bestGuess = 0;
        ServiceDefinition synonym = null;

        for (ServiceDefinition service : services) {
            double result = getBestResult(keyword, service);
            if (result > bestGuess && result > matchLevel) {
                bestGuess = result;
                synonym = service;
            }
        }

        return synonym;
    }

    private double getBestResult(String keyword, ServiceDefinition service) {
        double bestGuess = 0;
        List<String> matches = new ArrayList<String>();
        matches.add(service.getKeyword());
        matches.add(service.getServiceName());
        if (!StringUtils.isEmpty(service.getTags())) {
            matches.addAll(Arrays.asList(service.getTags().split(",")));
        }

        for (String match : matches) {
            double result = compareStrings(keyword, match);
            if (result > bestGuess) {
                bestGuess = result;
            }
        }

        return bestGuess;
    }
}
