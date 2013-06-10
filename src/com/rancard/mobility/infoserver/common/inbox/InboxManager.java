package com.rancard.mobility.infoserver.common.inbox;

import java.util.ArrayList;
import com.rancard.util.Page;

public abstract class InboxManager {

    //write to inbox
    public static String write(InboxEntry entry) throws
            Exception {
        return InboxDB.write(entry);
    }

    //update entry
    public static void updateVote(InboxEntry entry) throws Exception {
        InboxDB.updateVote(entry);
    }

    //delete particular entry
    public static void deleteVote(String keyword, String msisdn, String accountId) throws Exception {
        InboxDB.deleteVote(keyword, msisdn, accountId);
    }
    
    public static void deleteVote (String entryId) throws Exception {
        InboxDB.deleteVote (entryId);
    }

    //delete all entries for a question on a particular service
    public static void deleteVotes(String keyword,  String accountId) throws Exception {
        InboxDB.deleteVotes(keyword, accountId);
    }

    //view one entry
    public static InboxEntry viewEntry(String keyword, String msisdn, String accountId) throws Exception {
        return InboxDB.viewEntry(keyword, msisdn, accountId);
    }
    
    public static InboxEntry viewEntry (String entryId) throws Exception {
        return InboxDB.viewEntry (entryId);
    }

    //view list of all entries for a question on a particular service
    public static ArrayList viewEntries(String keyword, String accountId) throws Exception {
        return InboxDB.viewEntries(keyword, accountId);
    }
    
    public static ArrayList viewEntries (String competitionId) throws Exception {
        return InboxDB.viewEntries (competitionId);
    }
    
    public static ArrayList viewEntriesOnDate (String competitionId, String date) throws Exception {
        return InboxDB.viewEntriesOnDate (competitionId, date);
    }
    
    public static ArrayList viewCorrectEntries (String competitionId) throws Exception {
        return InboxDB.viewCorrectEntries (competitionId);
    }
    
    public static ArrayList calculateResults (String competitionId) throws Exception {
        return InboxDB.calculateResults(competitionId);
    }

    public static int getParticipationLevel(String keyword, String accountId) throws Exception {
        return InboxDB.getParticipationLevel(keyword, accountId);
    }

    public static Page viewInbox(String keyword, String accountId, java.sql.Timestamp startdate, java.sql.Timestamp enddate, 
            int start, int count) throws Exception {
        return InboxDB.viewInbox(keyword, accountId, startdate, enddate, start,count);
    }


}
