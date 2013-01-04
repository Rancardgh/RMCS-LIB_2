package com.rancard.mobility.infoserver.common.inbox;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.rancard.common.DConnect;
import java.util.ArrayList;
import com.rancard.util.Page;

public abstract class InboxDB {
    
    public static com.rancard.util.Page viewInbox (String keyword, String accountId, java.sql.Timestamp startdate,
            java.sql.Timestamp enddate, int start, int count) throws Exception {
        
        ArrayList responses = new ArrayList ();
        Page page = null;
        
        int y = 0;
        int i = 0;
        int numResults = 0;
        boolean hasNext = false;
        
        String query = "Select * from inbox";
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            if (keyword != null && !keyword.equals ("")) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where keyword='" + keyword + "'";
                } else {
                    query = query + " and keyword='" + keyword + "'";
                }
            }
            if (accountId != null && !accountId.equals ("")) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where account_id='" + accountId + "'";
                } else {
                    query = query + " and account_id='" + accountId + "'";
                }
            }
            if (startdate != null) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where date_voted >='" + startdate +
                            "'";
                } else {
                    query = query + " and date_voted >='" + startdate +
                            "'";
                }
            }
            if (enddate != null) {
                if (query.indexOf ("where") == -1) {
                    query = query + " where date_voted <='" + enddate +
                            "'";
                } else {
                    query = query + " and date_voted <='" + enddate + "'";
                }
            }
            query = query + " order by date_voted desc";
            
            prepstat = con.prepareStatement (query);
            rs = prepstat.executeQuery ();
            
            // get the total number of records
            rs.last ();
            numResults = rs.getRow ();
            rs.beforeFirst ();
            
            while (i < (start + count) && rs.next ()) {
                if (i == 0) {
                    int x = numResults;
                    y = x / count;
                    if ((x % count) > 0) {
                        y += 1;
                    }
                }
                if (i >= start) {
                    
                    InboxEntry newBean = new InboxEntry ();
                    newBean.setAccountId (rs.getString ("account_id"));
                    newBean.setDateReceived (rs.getTimestamp ("date_voted"));
                    newBean.setMessage (rs.getString ("response"));
                    newBean.setMessageId (rs.getString ("voteid"));
                    newBean.setMsisdn (rs.getString ("mobileno"));
                    newBean.setKeyword (rs.getString ("keyword"));
                    newBean.setShortCode (rs.getString ("short_code"));
                    newBean.setViewed (rs.getInt ("is_read"));
                    
                    responses.add (newBean);
                }
                i++;
            }
            
            hasNext = rs.next ();
            page = new Page (responses, start, hasNext, y, numResults);
            
            if (page == null) {
                page = com.rancard.util.Page.EMPTY_PAGE;
            }
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return page;
    }
    
    public static int getParticipationLevel (String keyword, String accountId) throws Exception {
        
        String SQL;
        int level = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "select count(distinct mobileno) as level from  inbox where keyword=? " +
                    "and account_id=?";
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, keyword);
            prepstat.setString (2, accountId);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                level = rs.getInt ("level");
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return level;
    }
    
    public static String write (InboxEntry entry) throws Exception {
        
        //inbox entry fields
        String voteid = entry.getMessageId ();
        String keyword = entry.getKeyword ();
        java.sql.Timestamp dateVoted = entry.getDateReceived ();
        String response = entry.getMessage ();
        //String questionid = entry.getQuestionId();
        String mobileno = entry.getMsisdn ();
        String shortCode = entry.getShortCode ();
        String provId = entry.getAccountId ();
        
        String SQL;
        int level = 0;
        String compId = "";
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            //write into Inbox
            SQL = "insert into inbox(voteid,keyword,date_voted,response," +
                    "mobileno,short_code,account_id) values(?,?,?,?,?,?,?)";
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, voteid);
            prepstat.setString (2, keyword);
            prepstat.setTimestamp (3, dateVoted);
            prepstat.setString (4, response);
            prepstat.setString (5, mobileno);
            prepstat.setString (6, shortCode);
            prepstat.setString (7, provId);
            
            prepstat.execute ();
            
            //get current participation limit
            
            /*SQL = "select competition_id from competitions where keyword='" + keyword + "' and account_id='" + provId + "' and startdate<=? and enddate>=?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setTimestamp (1, dateVoted);
            prepstat.setTimestamp (2, dateVoted);
            
            rs = prepstat.executeQuery ();
            
            if (rs.next ()) {
                compId = rs.getString ("competition_id");
            }
            
            SQL = "select count(distinct q.mobileno) as level from inbox q inner join competitions c on q.keyword=c.keyword and q.account_id=c.account_id  " +
                        "where c.competition_id='" + compId + "' and q.date_voted BETWEEN c.startdate and c.enddate";
            prepstat = con.prepareStatement (SQL);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                level = rs.getInt ("level");
            }
            
            //update the competition with the current participation level
            SQL = "update competitions set currentparticipation=? " +
                    "where keyword=? and account_id=? and competition_id=?";
            
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setLong (1, level);
            prepstat.setString (2, keyword);
            prepstat.setString (3, provId);
            prepstat.setString (4, compId);
            
            prepstat.execute ();*/
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return voteid;
    }
    
    public static void updateVote (InboxEntry entry) throws Exception {
        
        //inbox entry fields
        String voteid = entry.getMessageId ();
        String keyword = entry.getKeyword ();
        java.sql.Timestamp dateVoted = entry.getDateReceived ();
        String response = entry.getMessage ();
        //String questionid = entry.getQuestionId();
        String mobileno = entry.getMsisdn ();
        String shortCode = entry.getShortCode ();
        String provId = entry.getAccountId ();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "update inbox set response=?,date_voted=? " +
                    "where mobileno=? and keyword=? and account_id=?";
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, response);
            prepstat.setTimestamp (2, dateVoted);
            prepstat.setString (3, mobileno);
            //prepstat.setString(4, questionid);
            prepstat.setString (4, keyword);
            prepstat.setString (5, provId);
            
            prepstat.execute ();
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public static void deleteVote (String keyword, String msisdn, String accountId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "delete from inbox where keyword=? and " +
                    "mobileno=? and account_id=?";
            
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, keyword);
            //prepstat.setString(2, questionid);
            prepstat.setString (2, msisdn);
            prepstat.setString (3, accountId);
            
            prepstat.execute ();
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public static void deleteVotes (String keyword, String accountId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL =
                    "delete from inbox where keyword=? and account_id=?";
            
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, keyword);
            //prepstat.setString(2, questionid);
            prepstat.setString (2, accountId);
            
            prepstat.execute ();
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public static void deleteVote (String entryId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "delete from inbox where voteid=?";
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, entryId);
            prepstat.execute ();
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public static InboxEntry viewEntry (String keyword, String msisdn, String accountId) throws Exception {
        
        InboxEntry entry = new InboxEntry ();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from  inbox where keyword=? " +
                    "and mobileno=? and account_id=?";
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, keyword);
            //prepstat.setString(2, questionid);
            prepstat.setString (2, msisdn);
            prepstat.setString (3, accountId);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                entry.setDateReceived (rs.getTimestamp ("date_voted"));
                entry.setMessage (rs.getString ("response"));
                entry.setMessageId (rs.getString ("voteid"));
                entry.setMsisdn (rs.getString ("mobileno"));
                //entry.setQuestionId(rs.getString("questionid"));
                entry.setKeyword (rs.getString ("keyword"));
                entry.setShortCode (rs.getString ("short_code"));
                entry.setAccountId (rs.getString ("account_id"));
                entry.setViewed (rs.getInt ("is_read"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return entry;
    }
    
    public static InboxEntry viewEntry (String entryId) throws Exception {
        
        InboxEntry entry = new InboxEntry ();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from  inbox where voteid=?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, entryId);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                entry.setDateReceived (rs.getTimestamp ("date_voted"));
                entry.setMessage (rs.getString ("response"));
                entry.setMessageId (rs.getString ("voteid"));
                entry.setMsisdn (rs.getString ("mobileno"));
                entry.setKeyword (rs.getString ("keyword"));
                entry.setShortCode (rs.getString ("short_code"));
                entry.setAccountId (rs.getString ("account_id"));
                entry.setViewed (rs.getInt ("is_read"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return entry;
    }
    
    public static ArrayList viewEntries (String keyword, String accountId) throws Exception {
        
        ArrayList entries = new ArrayList ();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL =
                    "select * from  inbox where keyword=? and account_id=?";
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, keyword);
            //prepstat.setString(2, questionid);
            prepstat.setString (2, accountId);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                InboxEntry entry = new InboxEntry ();
                entry.setDateReceived (rs.getTimestamp ("date_voted"));
                entry.setMessage (rs.getString ("response"));
                entry.setMessageId (rs.getString ("voteid"));
                entry.setMsisdn (rs.getString ("mobileno"));
                //entry.setQuestionId(rs.getString("questionid"));
                entry.setKeyword (rs.getString ("keyword"));
                entry.setShortCode (rs.getString ("short_code"));
                entry.setAccountId (rs.getString ("account_id"));
                entry.setViewed (rs.getInt ("is_read"));
                entries.add (entry);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return entries;
    }
    
    public static ArrayList viewEntries (String competitionId) throws Exception {
        
        ArrayList entries = new ArrayList ();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL =  "select ib.* from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and ib.account_id = cmp.account_id " +
                    "where cmp.competition_id=?  and ib.date_voted BETWEEN cmp.startdate and cmp.enddate" ;
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, competitionId);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                InboxEntry entry = new InboxEntry ();
                entry.setDateReceived (rs.getTimestamp ("date_voted"));
                entry.setMessage (rs.getString ("response"));
                entry.setMessageId (rs.getString ("voteid"));
                entry.setMsisdn (rs.getString ("mobileno"));
                entry.setKeyword (rs.getString ("keyword"));
                entry.setShortCode (rs.getString ("short_code"));
                entry.setAccountId (rs.getString ("account_id"));
                entry.setViewed (rs.getInt ("is_read"));
                entries.add (entry);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return entries;
    }
    
    public static ArrayList viewEntriesOnDate (String competitionId, String date) throws Exception {
        
        ArrayList entries = new ArrayList ();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL =  "select ib.* from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and " +
                    "ib.account_id = cmp.account_id where cmp.competition_id=?  and ib.date_voted BETWEEN " +
                    "cmp.startdate and cmp.enddate and ib.date_voted = ?" ;
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, competitionId);
            prepstat.setString (2, date);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                InboxEntry entry = new InboxEntry ();
                entry.setDateReceived (rs.getTimestamp ("date_voted"));
                entry.setMessage (rs.getString ("response"));
                entry.setMessageId (rs.getString ("voteid"));
                entry.setMsisdn (rs.getString ("mobileno"));
                entry.setKeyword (rs.getString ("keyword"));
                entry.setShortCode (rs.getString ("short_code"));
                entry.setAccountId (rs.getString ("account_id"));
                entry.setViewed (rs.getInt ("is_read"));
                entries.add (entry);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return entries;
    }
    
    public static ArrayList viewCorrectEntries (String competitionId) throws Exception {
        
        ArrayList entries = new ArrayList ();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL =  "select answer from competitions where competition_id=?";
            prepstat.setString (1, competitionId);
            rs = prepstat.executeQuery ();
            String ans = new String ();
            while (rs.next ()) {
                ans = rs.getString ("answer");
            }
            
            SQL =  "select ib.* from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and ib.account_id = cmp.account_id " +
                    "where cmp.competition_id=?  and ib.date_voted BETWEEN cmp.startdate and cmp.enddate and ib.response like '%" + ans +"%'" ;
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, competitionId);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                InboxEntry entry = new InboxEntry ();
                entry.setDateReceived (rs.getTimestamp ("date_voted"));
                entry.setMessage (rs.getString ("response"));
                entry.setMessageId (rs.getString ("voteid"));
                entry.setMsisdn (rs.getString ("mobileno"));
                entry.setKeyword (rs.getString ("keyword"));
                entry.setShortCode (rs.getString ("short_code"));
                entry.setAccountId (rs.getString ("account_id"));
                entry.setViewed (rs.getInt ("is_read"));
                entries.add (entry);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return entries;
    }
    
    public static ArrayList viewWinners_predefined (String keyword, String accountId, int filter) throws Exception {
        
        String SQL = new String ();
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        ArrayList winners = new ArrayList ();
        
        try {
            con = DConnect.getConnection ();
            
            if (filter == com.rancard.mobility.infoserver.smscompetition.CompetitionManager.STRICT_FILTER) {
                SQL = "select distinct q.mobileno from inbox q inner join competitions c on q.response=c.answer  " +
                        "where q.keyword=? and q.account_id=? and q.date_voted BETWEEN c.startdate and c.enddate";
            } else if (filter == com.rancard.mobility.infoserver.smscompetition.CompetitionManager.RELAXED_FILTER) {
                SQL = "select distinct q.mobileno from inbox q, competitions a, quickquestions_options o where q.keyword=? and " +
                        "q.account_id=? and o.description=q.response or q.response=a.answer and q.date_voted BETWEEN a.startdate and a.enddate";
            }
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, keyword);
            prepstat.setString (2, accountId);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                winners.add (rs.getString ("mobileno"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return winners;
    }
    
    public static ArrayList viewWinners_userdefined (String keyword, String accountId, String[] answers, int filter) throws
            Exception {
        
        String SQL = new String ();
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        ArrayList winners = new ArrayList ();
        String answerStr = new String ();
        
        if(answers.length > 0){
            if (filter ==
                    com.rancard.mobility.infoserver.smscompetition.CompetitionManager.
                    STRICT_FILTER) {
                String answer = "or q.response='" + answers[0] + "'";
                if (answers.length > 1) {
                    for (int i = 1; i < answers.length; i++) {
                        answer = answer + " or q.response='" + answers[i] + "'";
                    }
                }
                answerStr = answer;
            } else if (filter ==
                    com.rancard.mobility.infoserver.smscompetition.
                    CompetitionManager.
                    RELAXED_FILTER) {
                String answer = "or q.response like \'%" + answers[0] + "%\'";
                if (answers.length > 1) {
                    for (int i = 1; i < answers.length; i++) {
                        answer = answer + " or q.response like \'%" + answers[i] + "%\'";
                    }
                }
                answerStr = answer;
            }
        }
        
        try {
            con = DConnect.getConnection ();
            
            SQL = "select distinct q.mobileno from inbox q inner join competitions a on q.response=a.answer " + answerStr + " where q.keyword='" + keyword +
                    "' and  q.account_id='" + accountId +"' and q.date_voted BETWEEN a.startdate and a.enddate";
            
            prepstat = con.prepareStatement (SQL);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                winners.add (rs.getString ("mobileno"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return winners;
    }
    
    public static ArrayList viewWinners_predefined (String competitionId, int filter) throws Exception {
        
        String SQL = new String ();
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        ArrayList winners = new ArrayList ();
        
        try {
            con = DConnect.getConnection ();
            
            if (filter == com.rancard.mobility.infoserver.smscompetition.CompetitionManager.STRICT_FILTER) {
                SQL = "select distinct q.mobileno from inbox q inner join competitions c on q.response=c.answer  " +
                        "where q.keyword=c.keyword and q.account_id=c.account_id and q.date_voted BETWEEN c.startdate and c.enddate and c.competition_id='" +
                        competitionId + "'";
            } else if (filter == com.rancard.mobility.infoserver.smscompetition.CompetitionManager.RELAXED_FILTER) {
                SQL = "select distinct q.mobileno from inbox q, competitions a, quickquestions_options o where q.keyword=a.keyword and " +
                        "q.account_id=a.account_id and o.description=q.response or q.response=a.answer and q.date_voted BETWEEN a.startdate and a.enddate and " +
                        "a.competition_id='" + competitionId + "'";
            }
            prepstat = con.prepareStatement (SQL);
                        
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                winners.add (rs.getString ("mobileno"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return winners;
    }
    
    public static ArrayList viewWinners_userdefined (String competitionId, String[] answers, int filter) throws
            Exception {
        
        String SQL = new String ();
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        ArrayList winners = new ArrayList ();
        String answerStr = new String ();
        
        if(answers.length > 0){
            if (filter ==
                    com.rancard.mobility.infoserver.smscompetition.CompetitionManager.
                    STRICT_FILTER) {
                String answer = "or q.response='" + answers[0] + "'";
                if (answers.length > 1) {
                    for (int i = 1; i < answers.length; i++) {
                        answer = answer + " or q.response='" + answers[i] + "'";
                    }
                }
                answerStr = answer;
            } else if (filter ==
                    com.rancard.mobility.infoserver.smscompetition.
                    CompetitionManager.
                    RELAXED_FILTER) {
                String answer = "or q.response like \'%" + answers[0] + "%\'";
                if (answers.length > 1) {
                    for (int i = 1; i < answers.length; i++) {
                        answer = answer + " or q.response like \'%" + answers[i] + "%\'";
                    }
                }
                answerStr = answer;
            }
        }
        
        try {
            con = DConnect.getConnection ();
            
            SQL = "select distinct q.mobileno from inbox q inner join competitions a on (q.response=a.answer " + answerStr + ") and q.keyword=a.keyword and  q.account_id=a.account_id" +
                    " and q.date_voted BETWEEN a.startdate and a.enddate and a.competition_id='" + competitionId + "'";
            
            prepstat = con.prepareStatement (SQL);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                winners.add (rs.getString ("mobileno"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return winners;
    }
    
    public static ArrayList calculateResults (String competitionId) throws Exception {
        boolean voted = false;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        int total_votes = 0;
        java.util.ArrayList votePercentages = new java.util.ArrayList ();
        
        try {
            con = com.rancard.common.DConnect.getConnection ();
            
            //get all options
            SQL =  "select * from quickquestions_options where competition_id=?";
            prepstat.setString (1, competitionId);
            rs = prepstat.executeQuery ();
            String ans = new String ();
            while (rs.next ()) {
                com.rancard.mobility.infoserver.smscompetition.Option option = new com.rancard.mobility.infoserver.smscompetition.Option ();
                option.setQuestionId (competitionId);
                option.setAccountId (rs.getString ("account_id"));
                option.setDescription (rs.getString ("description"));
                option.setKeyword (rs.getString ("keyword"));
                option.setOptionId (rs.getString ("optionid"));
                
                votePercentages.add (option);
            }
            
            //get number of entries for the competition
            SQL = "select COUNT(*) from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and ib.account_id = cmp.account_id " +
                    "where cmp.competition_id=?  and ib.date_voted BETWEEN cmp.startdate and cmp.enddate" ;
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, competitionId);
            rs = prepstat.executeQuery ();
            while (rs.next ()) {
                total_votes = rs.getInt ("count");
            }
            
            //get count for each option
            SQL = "select Count(*)as option_total from quickquestions_votes where optionid=?";
            prepstat = con.prepareStatement (SQL);
            int option_total = 0;
            double perc = 0.00;
            for (int k = 0; k < votePercentages.size (); k++) {
                prepstat.setString (1, ((com.rancard.mobility.infoserver.smscompetition.Option)votePercentages.get (k)).getOptionId ());
                rs = prepstat.executeQuery ();
                while (rs.next ()) {
                    option_total = rs.getInt ("option_total");
                }
                
                perc = (double) option_total / total_votes;
                perc *= 100;
                perc = Math.round (perc);
                ((com.rancard.mobility.infoserver.smscompetition.Option)votePercentages.get (k)).
                        setPercVoted (new Double (perc).toString () + "%");
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        return votePercentages;
    }
    
}
