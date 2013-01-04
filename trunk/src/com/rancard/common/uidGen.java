package com.rancard.common;

import java.text.*;
import java.util.*;

/**
 * <p>Title: UID generator</p>
 * <p>Description: This is a  class to provide a uinque Id for various entity types in the CHARJ application</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rancard  Solutions</p>
 * @author Ehizogie Binitie
 * @version 1.0
 **/

public class uidGen {
    /*private variables*/
    private String id = null;
    
    private Calendar cal = new GregorianCalendar ();
    
    /*Constructor*/
    public uidGen () {}
    
    /*Get a unique id*/
    /*public String getUId () {
        
        Random generator = new Random ();
        String id = String.valueOf (generator.nextInt (1000000000));
        generator = null;
        return id;
        //generateID(14);
    }*/
    
    public static synchronized String generateID (int stringLength) {
        StringBuffer id = new StringBuffer ();
        char[] characters = {'Q', 'U', 'o', 'a', 'G', 'y', '2', 'M', '4', '0',
        'v', '9', 'T', 'c', 'k', 'l', 'N', 'm', 'g', 'q', '5', 'r', '8', 's', 'w', 'z',
        'Z', 'x', 'A', 'f', 'i', 'u', 'B', '1', 'E', 'H', 'I', 'p', 'J', 'X', 'j', 'L',
        '7', 'e', 'W', 'C', 'O', '3', 'n', 'P', 'd', 'R', 'F', '6', 'S', 't',
        'D', 'V', 'b', 'K', 'Y', 'h'};
        
        for (int i = 0; i < stringLength; i++) {
            double index = (Math.random () * characters.length);
            id.append (characters[new Double (index).intValue ()]);
        }
        
        String id_string = id.toString ();
        
        //cleaning up
        id = null;
        characters = null;
        
        return id_string;
    }
    
    public static synchronized String generateNumberID (int stringLength) {
        StringBuffer id = new StringBuffer ();
        char[] characters = {'1', '0', '2', '4', '3', '5', '8', '6', '9', '0',
        '7', '9', '3', '1', '9', '7', '6', '8', '1', '4', '2', '3', '9', '3', '1', '8',
        '5', '5', '3', '8', '6', '0', '2', '1', '6', '8', '8'};
        
        for (int i = 0; i < stringLength; i++) {
            double index = (Math.random () * characters.length);
            id.append (characters[new Double (index).intValue ()]);
        }
        
        String id_string = id.toString ();
        
        //cleaning up
        id = null;
        characters = null;
        
        return id_string;
    }
    
    public static synchronized String getUId () {
        Calendar cal = new GregorianCalendar ();
        Date currentDate = cal.getTime ();
        DateFormat df = new SimpleDateFormat ("yyMMddhhmmssss");
        String header = new Integer (100+(int) ((Math.random () * 100) + (Math.random () * 100) - (Math.random () * 20) )).toString ();
        if(header.length () < 3){
            header = header + generateNumberID (3 - header.length ());
        }
        String tail = new Integer (100+(int) ((Math.random ()* 100) + (Math.random ()* 100) - (Math.random ()* 20) )).toString ();
        if(tail.length () < 3){
            tail = tail + generateNumberID (3 - tail.length ());
        }
        return header + df.format (currentDate) + tail;
    }

    public static synchronized String genUID(String prefix, int numRandomDigits) {
	java.security.SecureRandom wheel = new java.security.SecureRandom();
        int base = (int) Math.pow(10, numRandomDigits);
	int myInt = wheel.nextInt(1000000) + base;
        String str = Integer.toString(myInt);

        Calendar cal = new GregorianCalendar();
        java.util.Date currentDate = cal.getTime();
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        return prefix + df.format(currentDate) + "" + str.substring(0, numRandomDigits);
    }

    public static synchronized String generateSecureUID () {
        UUID id = UUID.randomUUID();
        return id.toString ();
    }
    
}
