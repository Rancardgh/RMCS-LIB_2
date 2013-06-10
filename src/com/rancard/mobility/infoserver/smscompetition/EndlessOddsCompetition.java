package com.rancard.mobility.infoserver.smscompetition;

import java.sql.Timestamp;
import java.util.ArrayList;
import com.rancard.mobility.infoserver.common.inbox.InboxManager;
import com.rancard.mobility.infoserver.common.inbox.InboxEntry;

public class EndlessOddsCompetition extends Competition {
    public EndlessOddsCompetition() {
        super();
        this.setParticipationLimit(CompetitionManager.INFINITE_PARTICIPATION);
        this.setType(CompetitionManager.ENDLESS_ODDS);
    }

    public EndlessOddsCompetition(String serviceType, String serviceName, String defaultMessage, String keyword, String desc, String thumburl,
                                  String limit, String accountId, int type, int partLevel, Timestamp start, Timestamp end, ArrayList prizes, String qstn,ArrayList opt,
                                  String ans, ArrayList altAns) {
        super(serviceType, serviceName, defaultMessage, keyword, desc, thumburl, limit, accountId, type, partLevel, start, end,
              prizes, qstn, opt, ans, altAns);
        this.setParticipationLimit(CompetitionManager.INFINITE_PARTICIPATION);
        this.setType(CompetitionManager.ENDLESS_ODDS);
    }

    public String vote(InboxEntry entry) throws Exception {
        //write into inbox
        String response = InboxManager.write(entry);

        return response;
    }


}
