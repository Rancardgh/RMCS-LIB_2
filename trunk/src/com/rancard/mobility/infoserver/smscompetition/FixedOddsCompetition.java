package com.rancard.mobility.infoserver.smscompetition;

import com.rancard.mobility.infoserver.common.inbox.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class FixedOddsCompetition extends Competition {
    public FixedOddsCompetition() {
        super();
        this.setParticipationLimit("0");
        this.setType(CompetitionManager.FIXED_ODDS);
    }

    public FixedOddsCompetition(String serviceType, String serviceName, String defaultMessage, String keyword, String desc, String thumburl, 
            String limit, String accountId, int type, int partLevel, Timestamp start,  Timestamp end, ArrayList prizes, String qstn, ArrayList opt, 
            String ans, ArrayList altAns) {
        super(serviceType, serviceName, defaultMessage, keyword, desc, thumburl, limit, accountId, type, partLevel, start, end,
              prizes, qstn, opt, ans, altAns);
        this.setParticipationLimit("0");
        this.setType(CompetitionManager.FIXED_ODDS);
    }

    //checks whether a participant can be added by checking the participation
    //limit.
    public boolean canAddParticipant() throws Exception {
        boolean canRegister = false;
        if (this.getParticipationLimit().equals(CompetitionManager.
                                                INFINITE_PARTICIPATION)) {
            canRegister = true;
        } else {
            if ((this.getCurrentParticipation() + 1) >
                Integer.parseInt(this.getParticipationLimit())) {
                canRegister = false;
            } else {
                canRegister = true;
            }
        }
        return canRegister;
    }

    //checks whether the given msisdn has been registered
    public boolean alreadyRegistered(String mobileno) throws Exception {
        return CompetitionDB.alreadyRegistered(mobileno, getKeyword(), getAccountId());
    }

    //checks whether the given msisdn has been registered
    public boolean alreadyVoted(String mobileno) throws Exception {
        return CompetitionDB.alreadyVoted(mobileno, getKeyword(), getAccountId());
    }

    //checks whether the given msisdn has been registered
    public String register(InboxEntry entry) throws Exception {
        //write into inbox
        String response = InboxManager.write(entry);
        return response;
    }

    public void vote(InboxEntry entry) throws Exception {
        InboxManager.updateVote(entry);
    }

}
