package com.rancard.mobility.infoserver.smscompetition;

import java.sql.Timestamp;
import java.util.ArrayList;
import com.rancard.mobility.infoserver.common.services.UserService;

public /*abstract*/ class Competition extends UserService {

    private String description;
    private String thumbnailURL;
    private String participationLimit;
    private String competitionId;
    private String question;
    private String answer;
    
    private int type;
    private long currentParticipation;
    
    private Timestamp startDate;
    private Timestamp endDate;
    
    private ArrayList prizes;
    private ArrayList options;
    private ArrayList alternativeAnswers;
    
    
    public Competition() {
        super();
        
        this.description = "";
        this.thumbnailURL = "";
        this.participationLimit = "";
        this.competitionId = "";
        this.question = "";
        this.answer = "";
        
        this.type = -1;
        this.currentParticipation = 0;
        
        this.startDate = this.now();
        this.endDate = this.now();
        
        this.prizes = new ArrayList();
        this.options = new ArrayList();
        this.alternativeAnswers = new ArrayList();
    }

    public Competition(String serviceType, String serviceName, String defaultMessage, String keyword, String desc, String thumburl, String limit,
                       String accountId, int type, int partLevel, Timestamp start, Timestamp end, ArrayList prizes, String qstn, ArrayList opt,
                       String ans, ArrayList altAns) {
        
        super(serviceType, keyword, accountId, serviceName, defaultMessage);
        
        this.description = desc;
        this.thumbnailURL = thumburl;
        this.participationLimit = limit;
        this.type = type;
        this.currentParticipation = partLevel;
        this.startDate = start;
        this.endDate = end;
        this.prizes = prizes;
        this.question = qstn;
        this.options = opt;
        this.answer = ans;
        this.alternativeAnswers = altAns;
    }


    private Timestamp now() {
        return new Timestamp(java.util.Calendar.getInstance().getTime().getTime());
    }

    //accessor methods
    /*public String getKeyword() {
        return this.keyword;
    }*/

    public String getDescription() {
        return this.description;
    }

    public String getThumbnailURL() {
        return this.thumbnailURL;
    }

    public String getParticipationLimit() {
        return this.participationLimit;
    }

    /*public String getAccountId() {
        return this.accountId;
    }*/

    public int getType() {
        return this.type;
    }

    public long getCurrentParticipation() {
        return this.currentParticipation;
    }

    public Timestamp getStartDate() {
        return this.startDate;
    }

    public Timestamp getEndDate() {
        return this.endDate;
    }

    public ArrayList getPrizes() {
        return this.prizes;
    }

    public String getQuestion() {
        return this.question;
    }

    public ArrayList getOptions() {
        return this.options;
    }

    public String getAnswer() {
        return this.answer;
    }
    
    public ArrayList getAlternativeAnswers() {
        return this.alternativeAnswers;
    }
    
    public String getAlternativeAnswersToString() {
        String altAns = "";
        
        for(int i=0; i < this.alternativeAnswers.size (); i++){
            altAns = altAns + this.alternativeAnswers.get (i).toString () + ",";
        }
        altAns =("".equals(altAns)) ? "" : altAns.substring (0, altAns.length () - 1);
        return altAns;
    }

    public boolean isEnded() {
        if (this.now().getTime() >= endDate.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    //mutator methods
    /*public void setKeyword(String keyword) {
        this.keyword = keyword;
    }*/

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setThumbnailURL(String url) {
        this.thumbnailURL = url;
    }

    public void setParticipationLimit(String limit) {
        this.participationLimit = limit;
    }

    /*public void setAccountId(String id) {
        this.accountId = id;
    }*/

    public void setType(int type) {
        this.type = type;
    }

    public void setCurrentParticipation(long number) {
        this.currentParticipation = number;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public void setPrizes(ArrayList prizes) {
        this.prizes = prizes;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(ArrayList optns) {
        this.options = optns;
    }

    public void setAnswer(String ans) {
        this.answer = ans;
    }
    
    public void setAlternativeAnswers(String ans) {
        
        if(ans!=null){
        java.util.StringTokenizer st = new java.util.StringTokenizer(ans, ",");
        while (st.hasMoreTokens()) {
            String answer = st.nextToken().trim ();
            this.alternativeAnswers.add(answer);
        }
        
      }
    }
    
    public void setAlternativeAnswers(ArrayList ans) {
        this.alternativeAnswers = ans;
    }

    /**
     * Getter for property competitionId.
     * @return Value of property competitionId.
     */
    public String getCompetitionId() {
        return this.competitionId;
    }

    /**
     * Setter for property competitionId.
     * @param competitionId New value of property competitionId.
     */
    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }
}
