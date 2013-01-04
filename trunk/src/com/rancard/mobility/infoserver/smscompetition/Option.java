package com.rancard.mobility.infoserver.smscompetition;

public class Option {

    private String keyword;
    private String accountId;
    private String description;
    private String optionId;
    private String questionId;
    private String percVoted;

    public Option() {
        keyword = "";
        accountId = "";
        description = "";
        optionId = "";
        questionId = "";
        percVoted = "";
    }

    public Option(String keyword, String accountId, String desc, String optId, String qusId, String percVote) {
        keyword = keyword;
        accountId = accountId;
        description = desc;
        optionId = optId;
        questionId = qusId;
        percVoted = percVote;
    }


    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setPercVoted(String percVoted) {
        this.percVoted = percVoted;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getDescription() {
        return description;
    }

    public String getOptionId() {
        return optionId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getPercVoted() {
        return percVoted;
    }
}
