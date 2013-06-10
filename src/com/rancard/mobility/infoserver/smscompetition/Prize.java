package com.rancard.mobility.infoserver.smscompetition;

public class Prize {
    
    private String prizeId, description, winnerMSISDN, thumbnailURL,
            keyword, accountId;
    private String questionId;

    public Prize() {
        this.prizeId = "";
        this.description = "";
        this.winnerMSISDN = "";
        this.thumbnailURL = "";
        this.keyword = "";
        this.accountId = "";
        this.questionId = "";
    }

    public Prize(String id, String desc, String winnerMSISDN,
                 String thumbnailURL, String keyword, String accountId,
                 String questionid) {
        this.prizeId = id;
        this.description = desc;
        this.winnerMSISDN = winnerMSISDN;
        this.thumbnailURL = thumbnailURL;
        this.keyword = keyword;
        this.accountId = accountId;
        this.questionId = questionid;
    }

    //mutator methods
    public void setPrizeId(String id) {
        this.prizeId = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWinnerMSISDN(String mobNumber) {
        this.winnerMSISDN = mobNumber;
    }

    public void setThumbnailURL(String url) {
        this.thumbnailURL = url;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setAccountId(String id) {
       this.accountId = id;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }


    //accessor methods
    public String getPrizeId() {
        return this.prizeId;
    }

    public String getDescription() {
        return this.description;
    }

    public String getWinnerMSISDN() {
        return this.winnerMSISDN;
    }

    public String getThumbnailURL() {
        return this.thumbnailURL;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getQuestionId() {
        return questionId;
    }
}
