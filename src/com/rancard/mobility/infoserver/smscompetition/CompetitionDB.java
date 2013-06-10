package com.rancard.mobility.infoserver.smscompetition;

import java.sql.*;
import com.rancard.common.*;
import com.rancard.mobility.infoserver.common.inbox.InboxEntry;
import java.util.ArrayList;
import java.util.Calendar;

public abstract class CompetitionDB {
    
    //check if participant has registered
    public static boolean alreadyRegistered (String mobileno,
            String keyword, String accountid) throws Exception {
        boolean registered = false;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = com.rancard.common.DConnect.getConnection ();
            
            SQL =
                    "select * from inbox where mobileno=?" +
                    " and keyword=? and account_id=?";
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, mobileno);
            prepstat.setString (2, keyword);
            prepstat.setString (3, accountid);
            
            rs = prepstat.executeQuery ();
            while (rs.next ()) {
                registered = true;
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
        return registered;
    }
    
    //check if the participant has already voted
    public static boolean alreadyVoted (String mobileno,
            String keyword, String accountid) throws Exception {
        boolean voted = false;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = com.rancard.common.DConnect.getConnection ();
            
            SQL =
                    "select response from inbox where mobileno=? " +
                    "and keyword=? and account_id=?";
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, mobileno);
            prepstat.setString (2, keyword);
            prepstat.setString (3, accountid);
            
            rs = prepstat.executeQuery ();
            while (rs.next ()) {
                if (rs.getString ("response") != null &&
                        !rs.getString ("response").equals ("")) {
                    voted = true;
                }
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
        return voted;
    }
    
    public static void createCompetition (Competition comp) throws
            Exception {
        
        String SQL;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        Calendar start = Calendar.getInstance ();
        Calendar end = Calendar.getInstance ();
        
        try {
            con = DConnect.getConnection ();
            con.setAutoCommit (false);
            
            start.setTimeInMillis (comp.getStartDate ().getTime ());
            end.setTimeInMillis (comp.getEndDate ().getTime ());
            
            if(end.before (start)){
                throw new Exception ("END date cannot be before START date");
            }
            
            // if service already exists ensure that time is duration is not the same on the competitions table
            //for service
            SQL = "select * from service_definition where keyword=? and account_id=? and service_type=8";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, comp.getKeyword ());
            prepstat.setString (2, comp.getAccountId ());
            // check if service is already registered on either the service_definition table if not insert it
            rs = prepstat.executeQuery ();
            if (!rs.next ()) {
                SQL = "insert into service_definition(service_type,keyword,account_id,service_name,default_message) values(?,?,?,?,?)";
                
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1,"8");
                prepstat.setString (2, comp.getKeyword ());
                prepstat.setString (3, comp.getAccountId ());
                prepstat.setString (4, comp.getServiceName ());
                prepstat.setString (5, comp.getDefaultMessage ());
                prepstat.execute ();
            }
            
            //for competition
            // check if the competition exists for the same duration
            /*SQL = "select * from competitions where keyword =? and account_id= ?  "+
                    "AND ( ( ? > startdate and ? <  startdate  )"+
                    " OR (? < startdate and ? > startdate  ) "+
                    " OR (? > enddate   and ? < enddate  ) "+
                    " OR (? < enddate   and ? > enddate  ) " +
                    " OR (? = startdate   and ? = enddate  ) )";*/
            SQL = "select * from competitions where keyword =? and account_id= ? " + 
                    "AND (" +
                    "(? < startdate and ? between startdate and enddate) OR "+
                    "(? between startdate and enddate and ? between startdate and enddate) OR "+
                    "(? between startdate and enddate and ? > enddate) OR "+
                    "(startdate between ? and ? and enddate between ? and ?) OR "+
                    "(startdate = ? and enddate = ?)"+
                    ")";
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, comp.getKeyword ());
            prepstat.setString (2, comp.getAccountId ());
            prepstat.setTimestamp (3, comp.getStartDate ());
            prepstat.setTimestamp (4, comp.getEndDate ());
            prepstat.setTimestamp (5, comp.getStartDate ());
            prepstat.setTimestamp (6, comp.getEndDate ());
            prepstat.setTimestamp (7, comp.getStartDate ());
            prepstat.setTimestamp (8, comp.getEndDate ());
            prepstat.setTimestamp (9, comp.getStartDate ());
            prepstat.setTimestamp (10, comp.getEndDate ());
            prepstat.setTimestamp (11, comp.getStartDate ());
            prepstat.setTimestamp (12, comp.getEndDate ());
            prepstat.setTimestamp (13, comp.getStartDate ());
            prepstat.setTimestamp (14, comp.getEndDate ());
            
            // if not insert new competition
            rs = prepstat.executeQuery ();
            if (!rs.next ()) {
                SQL =   "insert into competitions(keyword,description,type,startdate,enddate,thumbnailurl,currentparticipation," +
                        "participationlimit,account_id,competition_id,question,answer,alternative_answers) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, comp.getKeyword ());
                prepstat.setString (2, comp.getDescription ());
                prepstat.setInt (3, comp.getType ());
                prepstat.setTimestamp (4, comp.getStartDate ());
                prepstat.setTimestamp (5, comp.getEndDate ());
                prepstat.setString (6, comp.getThumbnailURL ());
                prepstat.setLong (7, comp.getCurrentParticipation ());
                prepstat.setString (8, comp.getParticipationLimit ());
                prepstat.setString (9, comp.getAccountId ());
                prepstat.setString (10, comp.getCompetitionId ());
                prepstat.setString (11, comp.getQuestion ());
                prepstat.setString (12, comp.getAnswer ());
                prepstat.setString (13, comp.getAlternativeAnswersToString ());
                prepstat.execute ();
            }else{
                throw new Exception ("A competition  already exists within the same duration with the same keyword");
                //return ;//error message should be that
            }
            //for options
            if(!comp.getOptions ().isEmpty ()){
                SQL = "insert into quickquestions_options(optionid,description,competition_id,keyword,account_id) values(?,?,?,?,?)";
                for (int i = 0; i < comp.getOptions ().size (); i++) {
                    prepstat = con.prepareStatement (SQL);
                    prepstat.setString (1, ((Option) comp.getOptions ().get (i)).getOptionId ());
                    prepstat.setString (2, ((Option) comp.getOptions ().get (i)).getDescription ());
                    prepstat.setString (3, comp.getCompetitionId ());
                    prepstat.setString (4, ((Option) comp.getOptions ().get (i)).getKeyword ());
                    prepstat.setString (5, ((Option) comp.getOptions ().get (i)).getAccountId ());
                    
                    prepstat.execute ();
                }
            }
            
            //for prizes
            if(!comp.getPrizes ().isEmpty ()){
                SQL = "insert into prizes(id,keyword,description," +
                        "winnersmsisdn,thumbnailurl,account_id,competition_id) values(?,?,?,?,?,?,?)";
                for (int i = 0; i < comp.getPrizes ().size (); i++) {
                    prepstat = con.prepareStatement (SQL);
                    prepstat.setString (1, ((Prize) comp.getPrizes ().get (i)).getPrizeId ());
                    prepstat.setString (2, ((Prize) comp.getPrizes ().get (i)).getKeyword ());
                    prepstat.setString (3, ((Prize) comp.getPrizes ().get (i)).getDescription ());
                    prepstat.setString (4, ((Prize) comp.getPrizes ().get (i)).getWinnerMSISDN ());
                    prepstat.setString (5, ((Prize) comp.getPrizes ().get (i)).getThumbnailURL ());
                    prepstat.setString (6, ((Prize) comp.getPrizes ().get (i)).getAccountId ());
                    prepstat.setString (7,  comp.getCompetitionId ());
                    
                    prepstat.execute ();
                    
                }
            }
            // execute the statements
            //prepstat.executeBatch();
            
        } catch (Exception ex) {
            
            bError = true;
            try{
                deleteThisCompetition (comp.getCompetitionId ());
            }catch(Exception ee){}
            throw new Exception (ex.getMessage ());
        } finally {
            
            if (bError) {
                con.rollback ();
            } else {
                con.commit ();
            }
            
            if (con != null) {
                con.close ();
            }
            start = null;
            end = null;
            comp = null;
        }
    }
    
    public static void updateCompetition (Competition oldcomp, Competition newcomp) throws
            Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        Calendar start = Calendar.getInstance ();
        Calendar end = Calendar.getInstance ();
        
        try {
            con = DConnect.getConnection ();
            con.setAutoCommit (false);
            
            start.setTimeInMillis (newcomp.getStartDate ().getTime ());
            end.setTimeInMillis (newcomp.getEndDate ().getTime ());
            
            if(end.before (start)){
                throw new Exception ("END date cannot be before START date");
            }
            
            // check if the competition exists for the same duration
            /*SQL = "select * from competitions where keyword =? and account_id= ?  "+
                    "AND ( ( ? > startdate and ? <  startdate  )"+
                    " OR (? < startdate and ? > startdate  ) "+
                    " OR (? > enddate   and ? < enddate  ) "+
                    " OR (? < enddate   and ? > enddate  ) " +
                    " OR (? = startdate   and ? = enddate  ) )" +
                    "AND competition_id!=?";*/
            SQL = "select * from competitions where keyword =? and account_id= ? " + 
                    "AND (" +
                    "(? < startdate and ? between startdate and enddate) OR "+
                    "(? between startdate and enddate and ? between startdate and enddate) OR "+
                    "(? between startdate and enddate and ? > enddate) OR "+
                    "(startdate between ? and ? and enddate between ? and ?) OR "+
                    "(startdate = ? and enddate = ?)"+
                    ") AND " +
                    "competition_id!=?";
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, oldcomp.getKeyword ());
            prepstat.setString (2, oldcomp.getAccountId ());
            prepstat.setTimestamp (3, newcomp.getStartDate ());
            prepstat.setTimestamp (4, newcomp.getEndDate ());
            prepstat.setTimestamp (5, newcomp.getStartDate ());
            prepstat.setTimestamp (6, newcomp.getEndDate ());
            prepstat.setTimestamp (7, newcomp.getStartDate ());
            prepstat.setTimestamp (8, newcomp.getEndDate ());
            prepstat.setTimestamp (9, newcomp.getStartDate ());
            prepstat.setTimestamp (10, newcomp.getEndDate ());
            prepstat.setTimestamp (11, newcomp.getStartDate ());
            prepstat.setTimestamp (12, newcomp.getEndDate ());
            prepstat.setTimestamp (13, newcomp.getStartDate ());
            prepstat.setTimestamp (14, newcomp.getEndDate ());
            prepstat.setString (15, oldcomp.getCompetitionId ());
            
            // if not insert new competition
            rs = prepstat.executeQuery ();
            if (!rs.next ()) {
                
                //for competition
                SQL = "update service_definition set default_message='" + newcomp.getDefaultMessage () + "',service_name='" + newcomp.getServiceName () +
                        "' where account_id='" + oldcomp.getAccountId () + "' and keyword='" + oldcomp.getKeyword () + "' and service_type='" + oldcomp.getServiceType () + "'";
                
                prepstat = con.prepareStatement (SQL);
                prepstat.execute ();
                
                //for competition
                SQL = "update competitions set description=?,type=?,startdate=?,enddate=?,thumbnailurl=?,currentparticipation=?," +
                        "participationlimit=?,question=?, answer=?, alternative_answers=?, keyword=?, account_id=? where competition_id=?";
                
                prepstat = con.prepareStatement (SQL);
                //prepstat.clearBatch();
                
                prepstat.setString (1, newcomp.getDescription ());
                prepstat.setInt (2, newcomp.getType ());
                prepstat.setTimestamp (3, newcomp.getStartDate ());
                prepstat.setTimestamp (4, newcomp.getEndDate ());
                prepstat.setString (5, newcomp.getThumbnailURL ());
                prepstat.setLong (6, newcomp.getCurrentParticipation ());
                prepstat.setString (7, newcomp.getParticipationLimit ());
                prepstat.setString (8, newcomp.getQuestion ());
                prepstat.setString (9, newcomp.getAnswer ());
                prepstat.setString (10, newcomp.getAlternativeAnswersToString ());
                //check value of alternative answers
                //System.out.println("Alternative ansers: " + newcomp.getAlternativeAnswersToString ());
                prepstat.setString (11, oldcomp.getKeyword ());
                prepstat.setString (12, oldcomp.getAccountId ());
                prepstat.setString (13, oldcomp.getCompetitionId ());
                
                prepstat.execute ();
                
                //for options
                SQL =
                        "update quickquestions_options set description=? where optionid=? and competition_id=?";
                for (int i = 0; i < newcomp.getOptions ().size (); i++) {
                    prepstat = con.prepareStatement (SQL);
                    prepstat.setString (1, ((Option) newcomp.getOptions ().get (i)).getDescription ());
                    
                    prepstat.setString (2, ((Option) oldcomp.getOptions ().get (i)).getOptionId ());
                    prepstat.setString (3, newcomp.getCompetitionId ());
                    //prepstat.setString (4, ((Option) oldcomp.getOptions ().get (i)).getKeyword ());
                    //prepstat.setString (5, ((Option) oldcomp.getOptions ().get (i)).getAccountId ());
                    
                    prepstat.execute ();
                }
                
                //for prizes
                SQL =
                        "update prizes set description=?,winnersmsisdn=?,thumbnailurl=? " +
                        "where id=? and competition_id=?";
                for (int i = 0; i < newcomp.getPrizes ().size (); i++) {
                    prepstat = con.prepareStatement (SQL);
                    prepstat.setString (1, ((Prize) newcomp.getPrizes ().get (i)).getDescription ());
                    prepstat.setString (2, ((Prize) newcomp.getPrizes ().get (i)).getWinnerMSISDN ());
                    prepstat.setString (3, ((Prize) newcomp.getPrizes ().get (i)).getThumbnailURL ());
                    prepstat.setString (4, ((Prize) oldcomp.getPrizes ().get (i)).getPrizeId ());
                    //prepstat.setString (5, ((Prize) oldcomp.getPrizes ().get (i)).getKeyword ());
                    //prepstat.setString (6, ((Prize) oldcomp.getPrizes ().get (i)).getAccountId ());
                    prepstat.setString (5, oldcomp.getCompetitionId ());
                    
                    prepstat.execute ();
                }
                // execute the statements
                //prepstat.executeBatch();
            }else{
                throw new Exception ("Cannot edit competition for this date.");
            }
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
                con = null;
            }
            bError = true;
            throw new Exception (ex.getMessage ());
        } finally {
            
            if (con != null) {
                con.close ();
            }
        }
        
    }
    
    public static Competition viewCompetition (String keyword, String accountId) throws
            Exception {
        
        Competition competition = null;
        ArrayList prizes = new ArrayList ();
        ArrayList options = new ArrayList ();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from service_definition s ,competitions c where c.account_id=? and c.keyword =? and c.keyword=s.keyword and " +
                    "c.account_id=s.account_id and s.service_type=8";
            
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, keyword);
            prepstat.setString (2, accountId);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                if (rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS) {
                    competition = new EndlessOddsCompetition ();
                } else if (rs.getInt ("type") == CompetitionManager.FIXED_ODDS) {
                    competition = new FixedOddsCompetition ();
                } else if(rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS_QUESTION_BACK) {
                    competition = new EndlessOddsWithQuestion ();
                }
                
                competition.setCompetitionId (rs.getString ("competition_id"));
                competition.setCurrentParticipation (rs.getLong ("currentparticipation"));
                competition.setDescription (rs.getString ("description"));
                competition.setEndDate (rs.getTimestamp ("enddate"));
                competition.setKeyword (rs.getString ("keyword"));
                competition.setParticipationLimit (rs.getString ("participationlimit"));
                competition.setAccountId (rs.getString ("account_id"));
                competition.setQuestion (rs.getString ("question"));
                competition.setAnswer (rs.getString ("answer"));
                competition.setStartDate (rs.getTimestamp ("startdate"));
                competition.setThumbnailURL (rs.getString ("thumbnailurl"));
                competition.setAlternativeAnswers (rs.getString ("alternative_answers"));
                competition.setType (rs.getInt ("type"));
                competition.setServiceName (rs.getString ("service_name"));
                competition.setServiceType (rs.getString ("service_type"));
            }
            
            SQL = "select * from  prizes where competition_id=?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, competition.getCompetitionId ());
            rs = prepstat.executeQuery ();
            while (rs.next ()) {
                prizes.add (new Prize (rs.getString ("id"), rs.getString ("description"), rs.getString ("winnersmsisdn"), rs.getString ("thumbnailurl"),
                        rs.getString ("keyword"), rs.getString ("account_id"), rs.getString ("competition_id")));
            }
            competition.setPrizes (prizes);
            
            SQL = "select * from quickquestions_options where competition_id=?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, competition.getCompetitionId ());
            rs = prepstat.executeQuery ();
            while (rs.next ()) {
                Option opt = new Option ();
                opt.setDescription (rs.getString ("description"));
                opt.setOptionId (rs.getString ("optionid"));
                opt.setQuestionId (rs.getString ("competition_id"));
                opt.setKeyword (rs.getString ("keyword"));
                opt.setAccountId (rs.getString ("account_id"));
                
                options.add (opt);
            }
            competition.setOptions (options);
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return competition;
    }
    
    public static Competition viewCompetition (String keyword, String accountId, java.util.Date date) throws
            Exception {
        
        Competition competition = null;
        ArrayList prizes = new ArrayList ();
        ArrayList options = new ArrayList ();
        String dateStr = new java.sql.Timestamp (date.getTime ()).toString ();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean compFound = false;
        
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from service_definition s ,competitions c where c.account_id='" + accountId + "' and c.keyword ='" + keyword + "' and c.startdate<='" + dateStr + "' and c.enddate>='" + dateStr
                    + "' and c.keyword=s.keyword and c.account_id=s.account_id and s.service_type=8";
            
            prepstat = con.prepareStatement (SQL);
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                if (rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS) {
                    competition = new EndlessOddsCompetition ();
                } else if (rs. getInt ("type") == CompetitionManager.FIXED_ODDS) {
                    competition = new FixedOddsCompetition ();
                } else if(rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS_QUESTION_BACK) {
                    competition = new EndlessOddsWithQuestion ();
                }
                
                competition.setCompetitionId (rs.getString ("competition_id"));
                competition.setCurrentParticipation (rs.getLong ("currentparticipation"));
                competition.setDescription (rs.getString ("description"));
                competition.setEndDate (rs.getTimestamp ("enddate"));
                competition.setKeyword (rs.getString ("keyword"));
                competition.setParticipationLimit (rs.getString ("participationlimit"));
                competition.setAccountId (rs.getString ("account_id"));
                competition.setQuestion (rs.getString ("question"));
                competition.setAnswer (rs.getString ("answer"));
                competition.setStartDate (rs.getTimestamp ("startdate"));
                competition.setThumbnailURL (rs.getString ("thumbnailurl"));
                competition.setAlternativeAnswers (rs.getString ("alternative_answers"));
                competition.setType (rs.getInt ("type"));
                competition.setServiceName (rs.getString ("service_name"));
                competition.setServiceType (rs.getString ("service_type"));
                competition.setDefaultMessage (rs.getString ("default_message"));
                
                compFound = true;
            }
            if(compFound!=false) {
                SQL = "select * from  prizes where competition_id=?";
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, competition.getCompetitionId ());
                rs = prepstat.executeQuery ();
                while (rs.next ()) {
                    prizes.add (new Prize (rs.getString ("id"), rs.getString ("description"), rs.getString ("winnersmsisdn"), rs.getString ("thumbnailurl"),
                            rs.getString ("keyword"), rs.getString ("account_id"), rs.getString ("competition_id")));
                }
                competition.setPrizes (prizes);
                
                SQL = "select * from quickquestions_options where competition_id=?";
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, competition.getCompetitionId ());
                rs = prepstat.executeQuery ();
                while (rs.next ()) {
                    Option opt = new Option ();
                    opt.setDescription (rs.getString ("description"));
                    opt.setOptionId (rs.getString ("optionid"));
                    opt.setQuestionId (rs.getString ("competition_id"));
                    opt.setKeyword (rs.getString ("keyword"));
                    opt.setAccountId (rs.getString ("account_id"));
                    
                    options.add (opt);
                }
                competition.setOptions (options);
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
        
        return competition;
    }
    
    public static ArrayList viewEntries ( String competitionId) throws Exception {
        
        ArrayList entries = new ArrayList ();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            SQL =  "select ib.* from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and ib.account_id = cmp.account_id " +
                    "where cmp.competition_id=?  and ib.date_voted BETWEEN cmp.startdate and cmp.enddate order by date_voted desc";
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
    
    public static ArrayList viewEntries ( String competitionId,  java.util.Date date) throws Exception {
        
        ArrayList entries = new ArrayList ();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        String dateStr = new java.sql.Timestamp (date.getTime ()).toString ().split (" ")[0];
        
        try {
            con = DConnect.getConnection ();
            SQL =  "select ib.* from inbox ib inner join competitions cmp on ib.keyword =cmp.keyword and ib.account_id = " +
                    "cmp.account_id where cmp.competition_id='" + competitionId + "'  and ib.date_voted BETWEEN cmp.startdate and cmp.enddate and " +
                    "date(date_voted)='" + dateStr + "'  order by date_voted desc";
            prepstat = con.prepareStatement (SQL);
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
    
    public static Competition viewCompetition ( String competitionId) throws
            Exception {
        
        Competition competition = null;
        ArrayList prizes = new ArrayList ();
        ArrayList options = new ArrayList ();
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from competitions c, service_definition s  where competition_id=? and c.keyword=s.keyword and " +
                    "c.account_id=s.account_id and s.service_type=8";
            
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1,competitionId);
            
            
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                if (rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS) {
                    competition = new EndlessOddsCompetition ();
                } else if (rs.getInt ("type") == CompetitionManager.FIXED_ODDS) {
                    competition = new FixedOddsCompetition ();
                } else if(rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS_QUESTION_BACK) {
                    competition = new EndlessOddsWithQuestion ();
                }
                
                competition.setCompetitionId (rs.getString ("competition_id"));
                competition.setCurrentParticipation (rs.getLong ("currentparticipation"));
                competition.setDescription (rs.getString ("description"));
                competition.setEndDate (rs.getTimestamp ("enddate"));
                competition.setKeyword (rs.getString ("keyword"));
                competition.setParticipationLimit (rs.getString ("participationlimit"));
                competition.setAccountId (rs.getString ("account_id"));
                competition.setQuestion (rs.getString ("question"));
                competition.setAnswer (rs.getString ("answer"));
                competition.setStartDate (rs.getTimestamp ("startdate"));
                competition.setThumbnailURL (rs.getString ("thumbnailurl"));
                competition.setAlternativeAnswers (rs.getString ("alternative_answers"));
                competition.setType (rs.getInt ("type"));
                competition.setServiceName (rs.getString ("service_name"));
                competition.setServiceType (rs.getString ("service_type"));
                competition.setDefaultMessage (rs.getString ("default_message"));
            }
            
            SQL = "select * from  prizes where competition_id=?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, competition.getCompetitionId ());
            rs = prepstat.executeQuery ();
            while (rs.next ()) {
                prizes.add (new Prize (rs.getString ("id"), rs.getString ("description"), rs.getString ("winnersmsisdn"), rs.getString ("thumbnailurl"),
                        rs.getString ("keyword"), rs.getString ("account_id"), rs.getString ("competition_id")));
            }
            competition.setPrizes (prizes);
            
            SQL = "select * from quickquestions_options where competition_id=?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, competition.getCompetitionId ());
            rs = prepstat.executeQuery ();
            while (rs.next ()) {
                Option opt = new Option ();
                opt.setDescription (rs.getString ("description"));
                opt.setOptionId (rs.getString ("optionid"));
                opt.setQuestionId (rs.getString ("competition_id"));
                opt.setKeyword (rs.getString ("keyword"));
                opt.setAccountId (rs.getString ("account_id"));
                
                options.add (opt);
            }
            competition.setOptions (options);
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
        
        return competition;
    }
    
    
    public static ArrayList viewAllCompetitions (String accountId) throws
            Exception {
        
        java.util.ArrayList competitions = new java.util.ArrayList ();
        
        String SQL;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat, prepstat2 = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from service_definition s ,competitions c where c.account_id=? and c.keyword=s.keyword and " +
                    "c.account_id=s.account_id and s.service_type=8";
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, accountId);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                Competition competition = null;
                
                if (rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS) {
                    competition = new EndlessOddsCompetition ();
                } else if (rs.getInt ("type") == CompetitionManager.FIXED_ODDS) {
                    competition = new FixedOddsCompetition ();
                } else if(rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS_QUESTION_BACK) {
                    competition = new EndlessOddsWithQuestion ();
                }
                
                competition.setCompetitionId (rs.getString ("competition_id"));
                competition.setCurrentParticipation (rs.getLong ("currentparticipation"));
                competition.setDescription (rs.getString ("description"));
                competition.setEndDate (rs.getTimestamp ("enddate"));
                competition.setKeyword (rs.getString ("keyword"));
                competition.setParticipationLimit (rs.getString ("participationlimit"));
                competition.setAccountId (rs.getString ("account_id"));
                competition.setQuestion (rs.getString ("question"));
                competition.setAnswer (rs.getString ("answer"));
                competition.setStartDate (rs.getTimestamp ("startdate"));
                competition.setThumbnailURL (rs.getString ("thumbnailurl"));
                competition.setAlternativeAnswers (rs.getString ("alternative_answers"));
                competition.setType (rs.getInt ("type"));
                competition.setServiceName (rs.getString ("service_name"));
                competition.setServiceType (rs.getString ("service_type"));
                
                SQL = "select * from  prizes where competition_id=?";
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, competition.getCompetitionId ());
                rs2 = prepstat.executeQuery ();
                
                ArrayList prizes = new ArrayList ();
                while (rs2.next ()) {
                    prizes.add (new Prize (rs2.getString ("id"), rs2.getString ("description"), rs2.getString ("winnersmsisdn"),
                            rs2.getString ("thumbnailurl"), rs2.getString ("keyword"), rs2.getString ("account_id"), rs2.getString ("competition_id")));
                }
                competition.setPrizes (prizes);
                
                SQL = "select * from quickquestions_options where competition_id=?";
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, competition.getCompetitionId ());
                rs2 = prepstat.executeQuery ();
                
                ArrayList options = new ArrayList ();
                while (rs2.next ()) {
                    Option opt = new Option ();
                    opt.setDescription (rs2.getString ("description"));
                    opt.setOptionId (rs2.getString ("optionid"));
                    opt.setQuestionId (rs2.getString ("competition_id"));
                    opt.setKeyword (rs2.getString ("keyword"));
                    opt.setAccountId (rs2.getString ("account_id"));
                    
                    options.add (opt);
                    
                }
                competition.setOptions (options);
                competitions.add (competition);
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
        
        return competitions;
    }
    
    
    
    public static ArrayList viewAllCompetitions (String accountId , String keyword) throws
            Exception {
        
        java.util.ArrayList competitions = new java.util.ArrayList ();
        
        String SQL;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat, prepstat2 = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from service_definition s ,competitions c where c.account_id=? and c.keyword=s.keyword and " +
                    "c.account_id=s.account_id and s.service_type=8 and s.keyword= ?";
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, accountId);
            prepstat.setString (2, keyword);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                Competition competition = null;
                
                if (rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS) {
                    competition = new EndlessOddsCompetition ();
                } else if (rs.getInt ("type") == CompetitionManager.FIXED_ODDS) {
                    competition = new FixedOddsCompetition ();
                } else if(rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS_QUESTION_BACK) {
                    competition = new EndlessOddsWithQuestion ();
                }
                
                competition.setCompetitionId (rs.getString ("competition_id"));
                competition.setCurrentParticipation (rs.getLong ("currentparticipation"));
                competition.setDescription (rs.getString ("description"));
                competition.setEndDate (rs.getTimestamp ("enddate"));
                competition.setKeyword (rs.getString ("keyword"));
                competition.setParticipationLimit (rs.getString ("participationlimit"));
                competition.setAccountId (rs.getString ("account_id"));
                competition.setQuestion (rs.getString ("question"));
                competition.setAnswer (rs.getString ("answer"));
                competition.setStartDate (rs.getTimestamp ("startdate"));
                competition.setThumbnailURL (rs.getString ("thumbnailurl"));
                competition.setAlternativeAnswers (rs.getString ("alternative_answers"));
                competition.setType (rs.getInt ("type"));
                competition.setServiceName (rs.getString ("service_name"));
                competition.setServiceType (rs.getString ("service_type"));
                
                SQL = "select * from  prizes where competition_id=?";
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, competition.getCompetitionId ());
                rs2 = prepstat.executeQuery ();
                
                ArrayList prizes = new ArrayList ();
                while (rs2.next ()) {
                    prizes.add (new Prize (rs2.getString ("id"), rs2.getString ("description"), rs2.getString ("winnersmsisdn"),
                            rs2.getString ("thumbnailurl"), rs2.getString ("keyword"), rs2.getString ("account_id"), rs2.getString ("competition_id")));
                }
                competition.setPrizes (prizes);
                
                SQL = "select * from quickquestions_options where competition_id=?";
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, competition.getCompetitionId ());
                rs2 = prepstat.executeQuery ();
                
                ArrayList options = new ArrayList ();
                while (rs2.next ()) {
                    Option opt = new Option ();
                    opt.setDescription (rs2.getString ("description"));
                    opt.setOptionId (rs2.getString ("optionid"));
                    opt.setQuestionId (rs2.getString ("competition_id"));
                    opt.setKeyword (rs2.getString ("keyword"));
                    opt.setAccountId (rs2.getString ("account_id"));
                    
                    options.add (opt);
                    
                }
                competition.setOptions (options);
                competitions.add (competition);
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
        
        return competitions;
    }
    
    
    public static ArrayList viewAllActiveCompetitions (String accountId, String month) throws
            Exception {
        
        java.util.ArrayList competitions = new java.util.ArrayList ();
        
        String SQL;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat, prepstat2 = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from service_definition s ,competitions c where c.account_id=? and c.keyword=s.keyword and " +
                    "c.account_id=s.account_id and s.service_type=8 and month(c.startdate) <= ? and month(c.enddate) >= ?";
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, accountId);
            prepstat.setString (2, month);
            prepstat.setString (3, month);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                Competition competition = null;
                
                if (rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS) {
                    competition = new EndlessOddsCompetition ();
                } else if (rs.getInt ("type") == CompetitionManager.FIXED_ODDS) {
                    competition = new FixedOddsCompetition ();
                } else if(rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS_QUESTION_BACK) {
                    competition = new EndlessOddsWithQuestion ();
                }
                
                competition.setCompetitionId (rs.getString ("competition_id"));
                competition.setCurrentParticipation (rs.getLong ("currentparticipation"));
                competition.setDescription (rs.getString ("description"));
                competition.setEndDate (rs.getTimestamp ("enddate"));
                competition.setKeyword (rs.getString ("keyword"));
                competition.setParticipationLimit (rs.getString ("participationlimit"));
                competition.setAccountId (rs.getString ("account_id"));
                competition.setQuestion (rs.getString ("question"));
                competition.setAnswer (rs.getString ("answer"));
                competition.setStartDate (rs.getTimestamp ("startdate"));
                competition.setThumbnailURL (rs.getString ("thumbnailurl"));
                competition.setAlternativeAnswers (rs.getString ("alternative_answers"));
                competition.setType (rs.getInt ("type"));
                competition.setServiceName (rs.getString ("service_name"));
                competition.setServiceType (rs.getString ("service_type"));
                
                SQL = "select * from  prizes where competition_id=?";
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, competition.getCompetitionId ());
                rs2 = prepstat.executeQuery ();
                
                ArrayList prizes = new ArrayList ();
                while (rs2.next ()) {
                    prizes.add (new Prize (rs2.getString ("id"), rs2.getString ("description"), rs2.getString ("winnersmsisdn"),
                            rs2.getString ("thumbnailurl"), rs2.getString ("keyword"), rs2.getString ("account_id"), rs2.getString ("competition_id")));
                }
                competition.setPrizes (prizes);
                
                SQL = "select * from quickquestions_options where competition_id=?";
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, competition.getCompetitionId ());
                rs2 = prepstat.executeQuery ();
                
                ArrayList options = new ArrayList ();
                while (rs2.next ()) {
                    Option opt = new Option ();
                    opt.setDescription (rs2.getString ("description"));
                    opt.setOptionId (rs2.getString ("optionid"));
                    opt.setQuestionId (rs2.getString ("competition_id"));
                    opt.setKeyword (rs2.getString ("keyword"));
                    opt.setAccountId (rs2.getString ("account_id"));
                    
                    options.add (opt);
                    
                }
                competition.setOptions (options);
                competitions.add (competition);
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
        
        return competitions;
    }
    
    public static ArrayList viewAllActiveCompetitions (String accountId, String beginMonth, String endMonth) throws
            Exception {
        
        java.util.ArrayList competitions = new java.util.ArrayList ();
        
        String SQL;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat, prepstat2 = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from competitions c inner join service_definition s on s.keyword=c.keyword where "+
                    "month(c.startdate) <= ? or month(c.enddate) >= ?";
            
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, accountId);
            prepstat.setString (2, endMonth);
            prepstat.setString (3, beginMonth);
            rs = prepstat.executeQuery ();
            
            while (rs.next ()) {
                Competition competition = null;
                
                if (rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS) {
                    competition = new EndlessOddsCompetition ();
                } else if (rs.getInt ("type") == CompetitionManager.FIXED_ODDS) {
                    competition = new FixedOddsCompetition ();
                } else if(rs.getInt ("type") == CompetitionManager.ENDLESS_ODDS_QUESTION_BACK) {
                    competition = new EndlessOddsWithQuestion ();
                }
                
                competition.setCompetitionId (rs.getString ("competition_id"));
                competition.setCurrentParticipation (rs.getLong ("currentparticipation"));
                competition.setDescription (rs.getString ("description"));
                competition.setEndDate (rs.getTimestamp ("enddate"));
                competition.setKeyword (rs.getString ("keyword"));
                competition.setParticipationLimit (rs.getString ("participationlimit"));
                competition.setAccountId (rs.getString ("account_id"));
                competition.setQuestion (rs.getString ("question"));
                competition.setAnswer (rs.getString ("answer"));
                competition.setStartDate (rs.getTimestamp ("startdate"));
                competition.setThumbnailURL (rs.getString ("thumbnailurl"));
                competition.setAlternativeAnswers (rs.getString ("alternative_answers"));
                competition.setType (rs.getInt ("type"));
                competition.setServiceName (rs.getString ("service_name"));
                competition.setServiceType (rs.getString ("service_type"));
                
                SQL = "select * from  prizes where competition_id=?";
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, competition.getCompetitionId ());
                rs2 = prepstat.executeQuery ();
                
                ArrayList prizes = new ArrayList ();
                while (rs2.next ()) {
                    prizes.add (new Prize (rs2.getString ("id"), rs2.getString ("description"), rs2.getString ("winnersmsisdn"),
                            rs2.getString ("thumbnailurl"), rs2.getString ("keyword"), rs2.getString ("account_id"), rs2.getString ("competition_id")));
                }
                competition.setPrizes (prizes);
                
                SQL = "select * from quickquestions_options where competition_id=?";
                prepstat = con.prepareStatement (SQL);
                prepstat.setString (1, competition.getCompetitionId ());
                rs2 = prepstat.executeQuery ();
                
                ArrayList options = new ArrayList ();
                while (rs2.next ()) {
                    Option opt = new Option ();
                    opt.setDescription (rs2.getString ("description"));
                    opt.setOptionId (rs2.getString ("optionid"));
                    opt.setQuestionId (rs2.getString ("competition_id"));
                    opt.setKeyword (rs2.getString ("keyword"));
                    opt.setAccountId (rs2.getString ("account_id"));
                    
                    options.add (opt);
                    
                }
                competition.setOptions (options);
                competitions.add (competition);
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
        
        return competitions;
    }
    
    public static void deleteThisCompetition (String competitionId) throws
            Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        boolean deleted = false;
        
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from competitions where competition_id='" + competitionId + "'";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            String keyword = "";
            String accountId = "";
            String enddate = "";
            String startdate = "";
            
            while(rs.next ()){
                keyword = rs.getString ("keyword");
                accountId = rs.getString ("account_id");
                startdate = rs.getString ("startdate");
                enddate = rs.getString ("enddate");
            }
            
            //cascade delete - from competitions
            SQL = "delete from prizes where competition_id='" + competitionId + "'";
            prepstat = con.prepareStatement (SQL);
            prepstat.execute ();
            
            //cascade delete - from competitions
            SQL = "delete from quickquestions_options where competition_id='" + competitionId + "'";
            prepstat = con.prepareStatement (SQL);
            prepstat.execute ();
            
            //cascade delete - from competitions
            SQL = "delete from inbox where keyword='" + keyword + "' and account_id='" + accountId + "' and date_voted between '" + startdate+ "' and '" + enddate + "'";
            prepstat = con.prepareStatement (SQL);
            prepstat.execute ();
            
            //cascade delete - from competitions
            SQL = "delete from competitions where competition_id='" + competitionId + "'";
            prepstat = con.prepareStatement (SQL);
            prepstat.execute ();
            
            SQL = "select * from competitions where keyword='" + keyword + "' and account_id='" + accountId + "'";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            if (!rs.next ()) {
                SQL = "delete from service_definition where keyword='" + keyword + "' and account_id='" + accountId + "'";
                prepstat = con.prepareStatement (SQL);
                prepstat.execute ();
            }
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            bError = true;
            throw new Exception (ex.getMessage ());
        } finally {
            
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static void deleteAllCompetitions (String account_id) throws
            Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        try {
            con = DConnect.getConnection ();
            con.setAutoCommit (false);
            
            //cascade delete - from competitions
            SQL = "delete from competitions where account_id=?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, account_id);
            
            prepstat.execute ();
            
            //cascade delete - from questions
            /*SQL = "delete from quickquestions where account_id=?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, account_id);
             
            prepstat.execute ();*/
            
            //cascade delete - from options
            SQL = "delete from quickquestions_options where account_id=?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, account_id);
            
            prepstat.execute ();
            
            //cascade delete - from prizes
            SQL = "delete from prizes where account_id=?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, account_id);
            
            prepstat.execute ();
            
            //cascade delete - from answers
            /*SQL = "delete from answers where account_id=?";
            prepstat = con.prepareStatement (SQL);
            prepstat.setString (1, account_id);
             
            prepstat.execute ();*/
            
            SQL = "select * from competitions where account_id=?";
            
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, account_id);
            
            rs = prepstat.executeQuery ();
            
            if (!rs.next ()) {
                SQL = "delete from service_definition where account_id=?";
                prepstat.setString (1, account_id);
                prepstat.execute ();
            }
            
            // execute the statements
            //prepstat.executeBatch();
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            bError = true;
            throw new Exception (ex.getMessage ());
        } finally {
            
            if (con != null) {
                con.close ();
            }
        }
    }
    
    public static void deleteAllCompetitions (String account_id, String keyword) throws
            Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        
        try {
            con = DConnect.getConnection ();
            SQL = "select * from competitions where account_id='" + account_id + "' and keyword='" + keyword + "'";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            String enddate = "";
            String startdate = "";
            String competitionId = "";
            
            while(rs.next ()){
                competitionId = rs.getString ("competition_id");
                startdate = rs.getString ("startdate");
                enddate = rs.getString ("enddate");
                
                //cascade delete - from competitions
                SQL = "delete from prizes where competition_id='" + competitionId + "'";
                prepstat = con.prepareStatement (SQL);
                prepstat.execute ();
                
                //cascade delete - from competitions
                SQL = "delete from quickquestions_options where competition_id='" + competitionId + "'";
                prepstat = con.prepareStatement (SQL);
                prepstat.execute ();
                
                //cascade delete - from competitions
                SQL = "delete from inbox where keyword='" + keyword + "' and account_id='" + account_id + "' and date_voted between '" + startdate+ "' and '" + enddate + "'";
                prepstat = con.prepareStatement (SQL);
                prepstat.execute ();
                
                //cascade delete - from competitions
                SQL = "delete from competitions where competition_id='" + competitionId + "'";
                prepstat = con.prepareStatement (SQL);
                prepstat.execute ();
            }
            
            SQL = "select * from competitions where keyword='" + keyword + "' and account_id='" + account_id + "'";
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();
            
            if (!rs.next ()) {
                SQL = "delete from service_definition where keyword='" + keyword + "' and account_id='" + account_id + "'";
                prepstat = con.prepareStatement (SQL);
                prepstat.execute ();
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            bError = true;
            throw new Exception (ex.getMessage ());
        } finally {
            
            if (con != null) {
                con.close ();
            }
        }
    }
}
