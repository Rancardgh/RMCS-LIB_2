package com.rancard.mobility.contentprovider;

import java.util.*;

//import com.rancard.mobility.server.*;
import com.rancard.security.*;

public class User implements java.io.Serializable {
    //private double balance;
    //private ArrayList devices;

    private String id;
    private String name;
    private String password;
    private String username;
    // information for runtime user of the account.
    // this infromation is used to store who is accessing this account
    private com.rancard.security.ProfilesBean transactionAuthor;
    private ArrayList creditBalances;
    private String defaultSmsc;
    private String defaultService;
    private ArrayList connections;
    private ArrayList services;
    private String logoUrl;
    private String defaultLanguage;
    private double bandwidthBalance;
    private double inboxBalance;
    private double outboxBalance;

    public User() {
        this.id = "";
        this.name = "";
        this.password = "";
        this.username = "";

        this.defaultSmsc = "";
        this.defaultService = "";
        this.connections = new ArrayList();
        this.services = new ArrayList();

        this.logoUrl = "";
        this.defaultLanguage = "";

        this.bandwidthBalance = 0;
        this.inboxBalance = 0;
        this.outboxBalance = 0;
    }

    public User(String username) {
        this.id = "";
        this.name = "";
        this.password = "";
        this.username = username;

        this.defaultSmsc = "";
        this.defaultService = "";
        this.connections = new ArrayList();
        this.services = new ArrayList();

        this.logoUrl = "";
        this.defaultLanguage = "";

        this.bandwidthBalance = 0;
        this.inboxBalance = 0;
        this.outboxBalance = 0;
    }

    public String resetPassword() throws Exception {

        String newPassword = com.rancard.util.PasswordGenerator.generatePassword(6);
        UserDB dealerDao = new UserDB();

        try {
            dealerDao.changePassword(this.getId(), newPassword);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            dealerDao = null;
        }

        return newPassword;
    }

    public com.rancard.common.Message changePassword(String dealerId, String oldPassword, String newPassword) {
        com.rancard.common.Message reply = new com.rancard.common.Message();

        UserDB dealerDao = new UserDB();
        User tmpdealer = null;

        try {
            tmpdealer = dealerDao.viewDealer(dealerId);
        } catch (Exception ex1) {
            reply.setStatus(false);
            reply.setMessage(ex1.getMessage());
            return reply;
        }

        if (tmpdealer != null && oldPassword.equals(tmpdealer.getPassword())) {
            try {
                dealerDao.changePassword(dealerId, newPassword);
            } catch (Exception ex) {
                reply.setStatus(false);
                reply.setMessage(ex.getMessage());
                return reply;
            }
        } else {

            reply.setStatus(false);
            reply.setMessage(
                    "You are not authorized to make this change.please enter a valid password");

        }
        return reply;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDefaultSmsc(String defaultSmsc) {
        this.defaultSmsc = defaultSmsc;
    }

    public String getDefaultSmsc() {
        return this.defaultSmsc;
    }

    public void setDefaultService(String defaultService) {
        this.defaultService = defaultService;
    }

    public String getDefaultService() {
        return this.defaultService;
    }

    public void setConnections(ArrayList connecitons) {
        this.connections = connecitons;
    }

    public java.util.ArrayList getConnections() {
        return this.connections;
    }

    public void setServices(ArrayList services) {
        this.services = services;
    }

    public java.util.ArrayList getServices() {
        return this.services;
    }

    public void setLogoUrl(String url) {
        this.logoUrl = url;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public void setDefaultLanguage(String language) {
        this.defaultLanguage = language;
    }

    public String getDefaultLanguage() {
        return this.defaultLanguage;
    }

    public double getBandwidthBalance() {
        return bandwidthBalance;
    }

    public void setBandwidthBalance(double bandwidthBalance) {
        this.bandwidthBalance = bandwidthBalance;
    }

    public double getInboxBalance() {
        return inboxBalance;
    }

    public void setInboxBalance(double inboxBalance) {
        this.inboxBalance = inboxBalance;
    }

    public double getOutboxBalance() {
        return outboxBalance;
    }

    public void setOutboxBalance(double outboxBalance) {
        this.outboxBalance = outboxBalance;
    }

    public boolean userExists() {
        boolean state = false;
        //java.util.ArrayList delers = new ArrayList(); //dealers collection
        UserDB dealer = new UserDB();
        com.rancard.mobility.contentprovider.User user = new com.rancard.mobility.contentprovider.User();

        try {
            user = dealer.view(this.getUsername(), this.getPassword());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        if (user.getId() != null && !user.getId().equals("")) {
            this.setId(user.getId());
            this.setName(user.getName());
            this.setPassword(user.getPassword());
            this.setDefaultService(user.getDefaultService());
            this.setUsername(user.getUsername());
            this.setDefaultSmsc(user.getDefaultSmsc());
            this.setLogoUrl(user.getLogoUrl());

            state = true;
        }
        return state;
    }

    //db methods
    public void createDealer(String name, String id, double accountBalance, String username, String password,
            double bandwidthBalance, double inboxBalance, double outboxBalance) throws Exception {
        new UserDB().createDealer(name, id, accountBalance, username, password, bandwidthBalance, inboxBalance, outboxBalance);
    }

    public void updateDealer(String name, String id, double accountBalance,
            String username, String password) throws Exception {
        UserDB dealer = new UserDB();
        dealer.updateDealer(name, id, accountBalance, username, password);
    }

    public void updateDealer(String name, String username, String password,
            String defaultSmsc, String defaultService) throws
            Exception {
        UserDB dealer = new UserDB();
        dealer.updateDealer(username, name, password, defaultSmsc,
                defaultService);
    }

    public void updateDealer(String name, String username, String defaultSmsc,
            String defaultService) throws Exception {
        UserDB dealer = new UserDB();
        dealer.updateDealer(username, name, defaultSmsc, defaultService);
    }

    public void updateDealerInfo(String name, String username, String password, String defaultSmsc, String defaultService, String logoUrl) throws Exception {
        UserDB dealer = new UserDB();
        dealer.updateDealerInfo(username, name, password, defaultSmsc, defaultService, logoUrl);
    }

    public void updateDealerInfo(String name, String username, String defaultSmsc, String defaultService, String logoUrl) throws Exception {
        UserDB dealer = new UserDB();
        dealer.updateDealerInfo(username, name, defaultSmsc, defaultService, logoUrl);
    }

    public void updateDealer(String id, double bandwidth, double inbox, double outbox) throws Exception {
        UserDB dealer = new UserDB();
        dealer.updateDealer(id, bandwidth, inbox, outbox);
    }

    public void updateDealer(String username, String language) throws Exception {
        UserDB dealer = new UserDB();
        dealer.updateDealer(username, language);
    }

    public void updateDealer() throws Exception {
        UserDB dealer = new UserDB();
        // dealer.updateDealer(this.getName(), this.getId(), this.getBalance(), this.getUsername(), this.getPassword());
        dealer.updateDealer(this.getName(), this.getId(), 0.00,
                this.getUsername(), this.getPassword());

    }

    public void updateDealerPassWord() throws Exception {
        UserDB dealer = new UserDB();
        dealer.updateDealer(this.getName(), this.getId(), 0.00,
                this.getUsername(), this.getPassword());
    }

    public void deleteDealer(String id) throws Exception {
        UserDB dealer = new UserDB();
        dealer.deleteDealer(id);
    }

    public void deleteDealer(String[] ids) throws Exception {
        UserDB dealer = new UserDB();
        dealer.deleteDealer(ids);
    }

    public User viewDealer(String id) throws Exception {
        UserDB dealer = new UserDB();
        return dealer.viewDealer(id);
    }

    public boolean viewDealerByUserName() throws Exception {
        boolean userExists = false;
        UserDB dealer = new UserDB();
        User tmpDealer = dealer.view(this.username, this.password);
        if (tmpDealer != null && tmpDealer.getId() != null) {
            userExists = true;
            //this.setDevices(tmpDealer.getDevices());
            //this.setBalance(tmpDealer.getAccountBalance());
            this.setId(tmpDealer.getId());
            this.setName(tmpDealer.getName());
            this.setUsername(tmpDealer.getUsername());
            this.setPassword(tmpDealer.getPassword());
            //   this.setDevices(tmpDealer.getDevices());
        }
        return userExists;
    }

    public void viewDealer() throws Exception {
        UserDB dealer = new UserDB();
        User tmpDealer = dealer.viewDealer(this.getId());
        //this.setDevices(tmpDealer.getDevices());
        //this.setBalance(tmpDealer.get.getAccountBalance());
        this.setId(tmpDealer.getId());
        this.setName(tmpDealer.getName());
        this.setUsername(tmpDealer.getUsername());
        this.setPassword(tmpDealer.getPassword());
        this.setDefaultService(tmpDealer.getDefaultService());
        this.setDefaultSmsc(tmpDealer.getDefaultSmsc());
        this.setLogoUrl(tmpDealer.logoUrl);
        this.setDefaultLanguage(tmpDealer.defaultLanguage);
        this.setBandwidthBalance(tmpDealer.getBandwidthBalance());
        this.setInboxBalance(tmpDealer.getInboxBalance());
        this.setConnections(tmpDealer.getConnections());
        this.setOutboxBalance(tmpDealer.getOutboxBalance());
//    this.setDevices(tmpDealer.getDevices());

    }

    public java.util.ArrayList getDealers() throws Exception {
        UserDB dealer = new UserDB();
        return dealer.getDealers();
    }

    public java.util.ArrayList viewUserWhiteList() throws Exception {
        com.rancard.security.AccessListItemDB accessList = new com.rancard.security.AccessListItemDB();
        return accessList.viewWhiteList(this.getId());

    }

    public void addToUserWhiteList(String ip) throws Exception {
        com.rancard.security.AccessListItemDB accessList = new com.rancard.security.AccessListItemDB();
        accessList.SaveToWhiteList(ip, this.getId());
    }

    public void addToUserBlackList(String ip) throws Exception {
        com.rancard.security.AccessListItemDB accessList = new com.rancard.security.AccessListItemDB();
        accessList.SaveToBlackList(ip, this.getId());
    }

    public void removeFromUserBlackList(String ip) throws Exception {
        com.rancard.security.AccessListItemDB accessList = new com.rancard.security.AccessListItemDB();
        accessList.RemoveFromBlackList(ip);
    }

    public void removeFromUserWhiteList(String ip) throws Exception {
        com.rancard.security.AccessListItemDB accessList = new com.rancard.security.AccessListItemDB();
        accessList.RemoveFromWhiteList(ip);
    }

    public java.util.ArrayList viewUserBlackList() throws Exception {
        com.rancard.security.AccessListItemDB accessList = new com.rancard.security.AccessListItemDB();
        return accessList.viewBlackList(this.getId());

    }
}
