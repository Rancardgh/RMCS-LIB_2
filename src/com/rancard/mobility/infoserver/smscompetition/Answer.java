package com.rancard.mobility.infoserver.smscompetition;

public class Answer {
    private String questionId;
    private String optionId;
    private String keyword;
    private String accountId;
    private String description;

    //constructor
    public Answer() {
        questionId = "";
        optionId = "";
        keyword = "";
        accountId = "";
        description = "";
    }

    public Answer(String questionid, String optionid, String desc, String keyword, String accountId) {
        this.questionId = questionid;
        this.optionId = optionid;
        this.keyword = keyword;
        this.accountId = accountId;
        this.description = desc;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
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

    public String getQuestionId() {
        return questionId;
    }

    public String getOptionId() {
        return optionId;
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
}
