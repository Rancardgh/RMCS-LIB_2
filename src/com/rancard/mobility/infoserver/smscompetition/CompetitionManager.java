package com.rancard.mobility.infoserver.smscompetition;

import com.rancard.mobility.infoserver.common.inbox.InboxDB;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;

public abstract class CompetitionManager
{
    public static final int RELAXED_FILTER = 0;
    public static final int STRICT_FILTER = 1;
    public static final int ENDLESS_ODDS = 0;
    public static final int FIXED_ODDS = 1;
    public static final int ENDLESS_ODDS_QUESTION_BACK = 2;
    public static final String INFINITE_PARTICIPATION = "*";

    public static void createCompetition(Competition competition)
            throws Exception
    {
        CompetitionDB.createCompetition(competition);
    }

    public static void deleteThisCompetition(String competitionId)
            throws Exception
    {
        CompetitionDB.deleteThisCompetition(competitionId);
    }

    public static void deleteAllCompetitions(String accountId)
            throws Exception
    {
        CompetitionDB.deleteAllCompetitions(accountId);
    }

    public static void deleteAllCompetitions(String accountId, String keyword)
            throws Exception
    {
        CompetitionDB.deleteAllCompetitions(accountId, keyword);
    }

    public static void updateCompetition(Competition oldcompetition, Competition newcompetition)
            throws Exception
    {
        CompetitionDB.updateCompetition(oldcompetition, newcompetition);
    }

    public static Competition viewCompetition(String keyword, String accountId)
            throws Exception
    {
        return CompetitionDB.viewCompetition(keyword, accountId);
    }

    public static Competition viewCompetition(String keyword, String accountId, Date date)
            throws Exception
    {
        return CompetitionDB.viewCompetition(keyword, accountId, date);
    }

    public static ArrayList viewEntries(String competitionId)
            throws Exception
    {
        return CompetitionDB.viewEntries(competitionId);
    }

    public static ArrayList viewEntries(String competitionId, Date date)
            throws Exception
    {
        return CompetitionDB.viewEntries(competitionId, date);
    }

    public static Competition viewCompetition(String competitionId)
            throws Exception
    {
        return CompetitionDB.viewCompetition(competitionId);
    }

    public static ArrayList viewAllCompetitions(String accountId)
            throws Exception
    {
        return CompetitionDB.viewAllCompetitions(accountId);
    }

    public static ArrayList viewAllCompetitions(String accountId, String keyword)
            throws Exception
    {
        return CompetitionDB.viewAllCompetitions(accountId, keyword);
    }

    public static ArrayList viewAllActiveCompetitions(String accountId, String month)
            throws Exception
    {
        return CompetitionDB.viewAllActiveCompetitions(accountId, month);
    }

    public static ArrayList viewAllActiveCompetitions(String accountId, String beginMonth, String endMonth)
            throws Exception
    {
        return CompetitionDB.viewAllActiveCompetitions(accountId, beginMonth, endMonth);
    }

    public static ArrayList viewWinners_predefined(String keyword, String accountId, int filterIntensity)
            throws Exception
    {
        return InboxDB.viewWinners_predefined(keyword, accountId, filterIntensity);
    }

    public static ArrayList viewWinners_userdefined(String keyword, String accountId, String[] answer, int filterIntensity)
            throws Exception
    {
        return InboxDB.viewWinners_userdefined(keyword, accountId, answer, filterIntensity);
    }

    public static ArrayList viewWinners_predefined(String competitionId, int filterIntensity)
            throws Exception
    {
        return InboxDB.viewWinners_predefined(competitionId, filterIntensity);
    }

    public static ArrayList viewWinners_userdefined(String competitionId, String[] answer, int filterIntensity)
            throws Exception
    {
        return InboxDB.viewWinners_userdefined(competitionId, answer, filterIntensity);
    }

    public static void autoDraw_predefined(String competitionId, int filterIntensity)
            throws Exception
    {
        Competition oldComp = viewCompetition(competitionId);
        ArrayList winners = viewWinners_predefined(oldComp.getKeyword(), oldComp.getAccountId(), filterIntensity);
        if (winners.size() > 0)
        {
            if (oldComp.isEnded())
            {
                Prize temp = (Prize)oldComp.getPrizes().get(0);
                if ((temp.getWinnerMSISDN().equals("")) || (temp.getWinnerMSISDN() == null))
                {
                    for (int i = 0; i < oldComp.getPrizes().size(); i++)
                    {
                        Prize prize = (Prize)oldComp.getPrizes().get(i);
                        int index = new Double(Math.random() * winners.size()).intValue();

                        prize.setWinnerMSISDN(winners.get(index).toString());
                        winners.remove(index);
                        if (winners.size() == 0) {
                            break;
                        }
                    }
                    updateCompetition(oldComp, oldComp);
                }
                else
                {
                    throw new Exception("9003");
                }
            }
            else
            {
                throw new Exception("9001");
            }
        }
        else {
            throw new Exception("9004");
        }
    }

    public static void autoDraw_userdefined(String competitionId, String[] answers, int filterIntensity)
            throws Exception
    {
        Competition oldComp = viewCompetition(competitionId);
        ArrayList winners = viewWinners_userdefined(oldComp.getKeyword(), oldComp.getAccountId(), answers, filterIntensity);
        if (oldComp.isEnded())
        {
            Prize temp = (Prize)oldComp.getPrizes().get(0);
            if ((temp.getWinnerMSISDN().equals("")) || (temp.getWinnerMSISDN() == null))
            {
                if (winners.size() > 0)
                {
                    for (int i = 0; i < oldComp.getPrizes().size(); i++)
                    {
                        Prize prize = (Prize)oldComp.getPrizes().get(i);
                        int index = new Double(Math.random() * winners.size()).intValue();

                        prize.setWinnerMSISDN(winners.get(index).toString());
                        winners.remove(index);
                        if (winners.size() == 0) {
                            break;
                        }
                    }
                    updateCompetition(oldComp, oldComp);
                }
                else
                {
                    throw new Exception("9004");
                }
            }
            else {
                throw new Exception("9003");
            }
        }
        else
        {
            throw new Exception("9001");
        }
    }

    public static void main(String[] args)
    {
        Option optiona = new Option();
        optiona.setDescription("Manchester Utd");
        optiona.setOptionId("a");
        optiona.setPercVoted("0");
        optiona.setQuestionId("3");
        optiona.setAccountId("test");
        optiona.setKeyword("nb");

        Option optionb = new Option();
        optionb.setDescription("Barcalona FC");
        optionb.setOptionId("b");
        optionb.setPercVoted("0");
        optionb.setQuestionId("3");
        optionb.setAccountId("test");
        optionb.setKeyword("nb");

        Option optionc = new Option();
        optionc.setDescription("Real Madrid");
        optionc.setOptionId("c");
        optionc.setPercVoted("0");
        optionc.setQuestionId("3");
        optionc.setAccountId("test");
        optionc.setKeyword("nb");

        ArrayList options = new ArrayList();
        options.add(optiona);
        options.add(optionb);
        options.add(optionc);

        Answer ans = new Answer();
        ans.setAccountId("test");
        ans.setKeyword("nb");
        ans.setDescription("Manchester Utd");
        ans.setOptionId("a");
        ans.setQuestionId("3");

        Question ques = new Question("3", "Which football team is in the English Premiership", "nb", "test");

        Prize prize1 = new Prize("a", "prize--1", "", "", "nb", "test", "3");
        Prize prize2 = new Prize("b", "prize--2", "", "", "nb", "test", "3");
        Prize prize3 = new Prize("c", "prize--3", "", "", "nb", "test", "3");
        ArrayList prizes = new ArrayList();
        prizes.add(prize1);
        prizes.add(prize2);
        prizes.add(prize3);

        Competition comp = new EndlessOddsCompetition();

        comp.setKeyword("nb");
        comp.setCurrentParticipation(0L);
        comp.setDescription("Sports Competition");
        comp.setAccountId("test");

        comp.setPrizes(prizes);
        comp.setOptions(options);
        try
        {
            String[] answers = new String[] { "a", "A", "option" };
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
