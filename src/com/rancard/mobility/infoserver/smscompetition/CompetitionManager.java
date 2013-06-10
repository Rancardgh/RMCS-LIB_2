package com.rancard.mobility.infoserver.smscompetition;

import java.util.ArrayList;

public abstract class CompetitionManager {
    
    //filter
    public static final int RELAXED_FILTER = 0;
    public static final int STRICT_FILTER = 1;
    
    //competition type
    public static final int ENDLESS_ODDS = 0;
    public static final int FIXED_ODDS = 1;
    public static final int ENDLESS_ODDS_QUESTION_BACK = 2;
    
    //participation
    public static final String INFINITE_PARTICIPATION = "*";
    
    //create competition
    public static void createCompetition (Competition competition) throws Exception {
        CompetitionDB.createCompetition (competition);
    }
    
    //delete one competition for provider
    public static void deleteThisCompetition (String competitionId) throws Exception {
        CompetitionDB.deleteThisCompetition (competitionId);
    }
    
    //delete all competitions for provider
    public static void deleteAllCompetitions (String accountId) throws Exception {
        CompetitionDB.deleteAllCompetitions (accountId);
    }
    
    //delete all competitions with a particular keyword for provider
    public static void deleteAllCompetitions (String accountId, String keyword) throws Exception {
        CompetitionDB.deleteAllCompetitions (accountId, keyword);
    }
    
    //update competition
    public static void updateCompetition (Competition oldcompetition, Competition newcompetition) throws Exception {
        CompetitionDB.updateCompetition (oldcompetition, newcompetition);
    }
    
    //view one competition bad method
    public static Competition viewCompetition (String keyword, String accountId) throws Exception {
        return CompetitionDB.viewCompetition (keyword, accountId);
    }
    
    public static Competition viewCompetition (String keyword, String accountId, java.util.Date date) throws
            Exception {
        return CompetitionDB.viewCompetition (keyword, accountId, date);
    }
    
    public static ArrayList viewEntries (String competitionId) throws Exception {
        return com.rancard.mobility.infoserver.smscompetition.CompetitionDB.viewEntries (competitionId);
    }
    
    public static ArrayList viewEntries ( String competitionId,  java.util.Date date) throws Exception {
        return com.rancard.mobility.infoserver.smscompetition.CompetitionDB.viewEntries (competitionId, date);
    }
    
    //view one competition
    public static Competition viewCompetition ( String competitionId) throws Exception {
        return CompetitionDB.viewCompetition (competitionId);
    }
    //view all competitions for a provider
    public static ArrayList viewAllCompetitions (String accountId) throws Exception {
        return CompetitionDB.viewAllCompetitions (accountId);
    }

        //view all competitions for a provider for a particular keyword
    public static ArrayList viewAllCompetitions (String accountId , String keyword) throws Exception {
        return CompetitionDB.viewAllCompetitions (accountId ,keyword);
    }
    public static ArrayList viewAllActiveCompetitions (String accountId, String month) throws Exception {
        return CompetitionDB.viewAllActiveCompetitions (accountId, month);
    }
    
    public static ArrayList viewAllActiveCompetitions (String accountId, String beginMonth, String endMonth) throws Exception {
        return CompetitionDB.viewAllActiveCompetitions (accountId, beginMonth, endMonth);
    }
    
    //generate list of winners based on a stored answer and filter intensity
    public static ArrayList viewWinners_predefined (String keyword, String accountId, int filterIntensity) throws Exception {
        return com.rancard.mobility.infoserver.common.inbox.InboxDB.viewWinners_predefined (
                keyword, accountId, filterIntensity);
    }
    
    //generate list of winners based on a given array of possible answers and filter intensity
    public static ArrayList viewWinners_userdefined (String keyword, String accountId, String[] answer, int filterIntensity) throws
            Exception {
        return com.rancard.mobility.infoserver.common.inbox.InboxDB.viewWinners_userdefined (
                keyword, accountId, answer, filterIntensity);
    }
    
    //generate list of winners based on a stored answer and filter intensity
    public static ArrayList viewWinners_predefined (String competitionId, int filterIntensity) throws Exception {
        return com.rancard.mobility.infoserver.common.inbox.InboxDB.viewWinners_predefined (competitionId, filterIntensity);
    }
    
    //generate list of winners based on a given array of possible answers and filter intensity
    public static ArrayList viewWinners_userdefined (String competitionId, String[] answer, int filterIntensity) throws
            Exception {
        return com.rancard.mobility.infoserver.common.inbox.InboxDB.viewWinners_userdefined (competitionId, answer, filterIntensity);
    }
    
    //execute an auto draw on a given competition using a predefined answer
    //and a given filter intensity level - strict or relaxed
    public static void autoDraw_predefined (String competitionId, int filterIntensity) throws
            Exception {
        Competition oldComp = CompetitionManager.viewCompetition (competitionId);
        ArrayList winners = CompetitionManager.viewWinners_predefined (oldComp.
                getKeyword (), oldComp.getAccountId (), filterIntensity);
        
        //check if there were any winners
        if(winners.size () > 0){
            //check if competition has ended
            if (oldComp.isEnded ()) {
                
                //check if competition has not been drawn
                Prize temp = (Prize) oldComp.getPrizes ().get (0);
                if (temp.getWinnerMSISDN ().equals ("") ||
                        temp.getWinnerMSISDN () == null) {
                    for (int i = 0; i < oldComp.getPrizes ().size (); i++) {
                        Prize prize = ((Prize) oldComp.getPrizes ().get (i));
                        int index = new Double ((Math.random () * winners.size ())).
                                intValue ();
                        prize.setWinnerMSISDN (winners.get (index).toString ());
                        winners.remove (index);
                        if (winners.size () == 0) {
                            break;
                        }
                    }
                    CompetitionManager.updateCompetition (oldComp, oldComp);
                } else {
                    throw new Exception (com.rancard.common.Feedback.
                            WINNERS_ALREADY_DRAWN);
                }
            } else {
                throw new Exception (com.rancard.common.Feedback.
                        COMPETITION_NOT_OVER);
            }
        } else {
            throw new Exception (com.rancard.common.Feedback.WINNERS_NOT_FOUND);
        }
    }
    
    //execute an auto draw on a given competition using a predefined answer
    //and a given filter intensity level - strict or relaxed
    public static void autoDraw_userdefined (String competitionId, String[] answers, int filterIntensity) throws
            Exception {
        
        Competition oldComp = CompetitionManager.viewCompetition (competitionId);
        ArrayList winners = CompetitionManager.viewWinners_userdefined (oldComp.getKeyword (),
                oldComp.getAccountId (), answers, filterIntensity);
        
        
        //check if there were any winners
        
        //check if competition has ended
        if (oldComp.isEnded ()) {
            
            //check if competition has not been drawn
            Prize temp = (Prize) oldComp.getPrizes ().get (0);
            if (temp.getWinnerMSISDN ().equals ("") ||
                    temp.getWinnerMSISDN () == null) {
                if(winners.size () > 0){
                    for (int i = 0; i < oldComp.getPrizes ().size (); i++) {
                        Prize prize = ((Prize) oldComp.getPrizes ().get (i));
                        int index = new Double ((Math.random () * winners.size ())).
                                intValue ();
                        prize.setWinnerMSISDN (winners.get (index).toString ());
                        winners.remove (index);
                        if (winners.size () == 0) {
                            break;
                        }
                    }
                    CompetitionManager.updateCompetition (oldComp, oldComp);
                } else {
                    throw new Exception (com.rancard.common.Feedback.WINNERS_NOT_FOUND);
                }
            } else {
                throw new Exception (com.rancard.common.Feedback.
                        WINNERS_ALREADY_DRAWN);
            }
        } else {
            throw new Exception (com.rancard.common.Feedback.
                    COMPETITION_NOT_OVER);
        }
    }
    
    
    
    public static void main (String[] args) {
        Option optiona = new Option ();
        optiona.setDescription ("Manchester Utd");
        optiona.setOptionId ("a");
        optiona.setPercVoted ("0");
        optiona.setQuestionId ("3");
        optiona.setAccountId ("test");
        optiona.setKeyword ("nb");
        
        Option optionb = new Option ();
        optionb.setDescription ("Barcalona FC");
        optionb.setOptionId ("b");
        optionb.setPercVoted ("0");
        optionb.setQuestionId ("3");
        optionb.setAccountId ("test");
        optionb.setKeyword ("nb");
        
        Option optionc = new Option ();
        optionc.setDescription ("Real Madrid");
        optionc.setOptionId ("c");
        optionc.setPercVoted ("0");
        optionc.setQuestionId ("3");
        optionc.setAccountId ("test");
        optionc.setKeyword ("nb");
        
        ArrayList options = new ArrayList ();
        options.add (optiona);
        options.add (optionb);
        options.add (optionc);
        
        Answer ans = new Answer ();/*("3", "a", "Manchester Utd", "nb", "test");*/
        ans.setAccountId ("test");
        ans.setKeyword ("nb");
        ans.setDescription ("Manchester Utd");
        ans.setOptionId ("a");
        ans.setQuestionId ("3");
        
        Question ques = new Question ("3", "Which football team is in the English Premiership", "nb", "test");
        
        Prize prize1 = new Prize ("a", "prize--1", "", "", "nb", "test", "3");
        Prize prize2 = new Prize ("b", "prize--2", "", "", "nb", "test", "3");
        Prize prize3 = new Prize ("c", "prize--3", "", "", "nb", "test", "3");
        ArrayList prizes = new ArrayList ();
        prizes.add (prize1);
        prizes.add (prize2);
        prizes.add (prize3);
        
        Competition comp = new EndlessOddsCompetition ();
        //comp.setAnswer(ans);
        comp.setKeyword ("nb");
        comp.setCurrentParticipation (0);
        comp.setDescription ("Sports Competition");
        comp.setAccountId ("test");
        //comp.setQuestion(ques);
        comp.setPrizes (prizes);
        comp.setOptions (options);
        
        try {
            String[] answers = {"a", "A", "option"};
            //CompetitionManager.autoDraw_userdefined("nb", "test", answers, CompetitionManager.RELAXED_FILTER);
            //CompetitionManager.createCompetition (comp);
        } catch (Exception e) {
            System.out.println (e.getMessage ());
        }
    }
}
