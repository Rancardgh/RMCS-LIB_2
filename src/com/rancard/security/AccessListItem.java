package com.rancard.security;

public class AccessListItem {
    private String ipAddress;
    private String owner;
    public AccessListItem() {
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getOwner() {
        return owner;
    }

    public String getOwner(String ipAddress) {
        return owner;
    }

}
