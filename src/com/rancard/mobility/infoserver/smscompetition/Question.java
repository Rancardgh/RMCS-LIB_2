package com.rancard.mobility.infoserver.smscompetition;

public class Question {

    private String question;
    private String questionId;
    private String keyword;
    private String accountId;

    public Question() {
        this.question = "";
        this.questionId = "";
        this.keyword = "";
        this.accountId = "";
    }

    public Question(String questionId, String question, String keyword, String accountId) {
        this.question = question;
        this.questionId = questionId;
        this.keyword = keyword;
        this.accountId = accountId;
    }

    public String getQuestionId() {
        return this.questionId;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
