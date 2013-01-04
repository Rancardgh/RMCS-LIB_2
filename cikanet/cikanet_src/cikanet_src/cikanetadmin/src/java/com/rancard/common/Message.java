package com.rancard.common;
/*
 This is a utility class to carry both the status of an action as well as the cause.
 
 
 
 */
public class Message {
    private boolean status;
    private String message;
    public Message () {
    }
    
    /**
     * toString
     */
    public String toString () {
        return this.getMessage ();
        
    }
    
    public Message (String message,boolean status) {
        this.message = message;
        this.status =status;
    }
    public void setStatus (boolean status) {
        this.status = status;
    }
    
    public void setMessage (String message) {
        this.message = message;
    }
    
    public boolean isStatus () {
        return status;
    }
    
    public String getMessage () {
        return message;
    }
}
