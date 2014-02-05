package com.rancard.util;

import java.util.*;

public class Validator {
    private static ArrayList validCharSet = new ArrayList();
    public Validator() {
    }

    /** @todo add stripping for leading zeros in cases that a number is specified in the format: 00233276176997 */

    public static String stripLeadingPlus(String in)throws Exception{
        //System.out.println("stripLeadingPlus got "+in);
        int len = in.length();
        char[] in_tokens = in.toCharArray();

        if( (in_tokens[0] == '+') || (in_tokens[0] == ' ') ){
            //System.out.println("There exists a + in the number");
            int lastPlusIndex = in.lastIndexOf("+");
            in = in.substring(lastPlusIndex+1,in.length());
            in = new StringBuffer(in).deleteCharAt(in.indexOf(' ')).toString();
            System.out.println("The parsed number is: "+in);
            return in;
        }
        //System.out.println("There exists NO + in the number");
        return in;
    }

    public static boolean isNumber(String n) throws Exception {
        try {
            char[] tokens = n.toCharArray();
            //validate each of the characters for invalid characters
            for (int i = 0; i < tokens.length; i++) {
                if (!Validator.isCharValid(new Character(tokens[i]).toString(),validCharSet)) {

                    return false;
                }
            }
            //now validate for whether there exists a patch of +s with numbers mixed
            //find the last position of a +
            int lastPlusPosition = n.lastIndexOf("+");
            //we then see if there are any numbers between the first and last plus positions
            if( !(lastPlusPosition == 0) || !(lastPlusPosition == 1) ){//position where + usually is
                for (int i = 0; i < lastPlusPosition; i++) {
                    if (tokens[i] != '+') {
                        return false;
                        //break; //we break if a !+ is found. we go to the if below
                    }
                }
            }
            //System.out.println("successfully parsed string input");
            return true;
        } catch (NumberFormatException e) {
            //System.out.println("caught exception on isNumber: " + e.getMessage());
            return false;
        }
    }

    /** @todo mod this to be allowedPrefixes and validCharSet checker */
    public static boolean isCharValid(String in,ArrayList validTokens) throws Exception {

        if (validTokens.contains(in)) {
            System.out.println("Tokens size " + validTokens.size());
            System.out.println(in + " is a valid number");
            return true;
        } else {
            System.out.println("Tokens size " + validTokens.size());
            System.out.println(in + " is NOT a valid number");
            return false;
        }
    }

    public static boolean isLengthValid(String number, int length) throws
            Exception {
        boolean state = false;
        if (number.length() <= length) {
            //System.out.println("isLengthValid for " + number + " succeeded");
            state = true;
        } else {
            //System.out.println("isLengthValid for " + number + " failed");
            state = false;
        }
        return state;
    }

    public static ArrayList validateNumbers(String[] numbers,int numLen,String validTokenStr)throws Exception{
        ArrayList all = new ArrayList();
        String[] valids = new String[numbers.length];
        String[] invalids = new String[numbers.length];
        ArrayList valids_arrlist = new ArrayList();
        String[] validTokens = Validator.buildValidCharsArray(validTokenStr);

        //populate valid tokens arraylist
        for(int i = 0; i < validTokens.length; i++)
            valids_arrlist.add(validTokens[i]);
        Validator.setValidCharSet(valids_arrlist);
        //System.out.println("Populated the valid chars array");

        for(int j = 0; j < numbers.length; j++){
            String number = numbers[j];
            //if the length of the number is valid: if(isLengthValid)
            if (Validator.isLengthValid(number,numLen)){
              //if(isNumberValid)
              if(Validator.isNumber(number)){
                  number = Validator.stripLeadingPlus(number);
                  valids[j] = number;
                  //System.out.println("Got valid number "+number);
              }else{
                  invalids[j] = number;
                  //System.out.println("Got level 1 invalid number "+number);
              }
            }else{
                invalids[j] = number;
                //System.out.println("Got level 2 invalid number " + number);
            }
        }
        String reply = buildMultipleSendResultMessage(valids, invalids);
        //System.out.println("built reply message");
        all.add(valids);
        all.add(invalids);
        all.add(reply);

        return all;
    }

    public static String buildMultipleSendResultMessage(String[] valids,
            String[] invalids) throws Exception {
        /** @todo optimize message building to iterate only over non-null  */
        String reply = "";
        if (valids.length != 0) {
            for (int i = 0; i < valids.length; i++) {
                if ( /*!("".equals(valids[i])) || */((valids[i] != null))) {
                    reply += "OK: " + valids[i] + "\n";
                }
            }
        }
        if (invalids.length != 0) {
            for (int i = 0; i < invalids.length; i++) {
                if ( /*!("".equals(invalids[i])) || */((invalids[i] != null))) {
                    reply += "ERROR: 101t to " + invalids[i] + "\n";
                }
            }
        }
        //System.out.println("reply Message = \n" + reply);
        return reply;
    }
    public static String[] buildValidCharsArray(String in)throws Exception{
        String[] out = new String[in.length()];
        char[] tmp = in.toCharArray();
        for(int i = 0; i < tmp.length; i++){
            out[i] = new Character(tmp[i]).toString();
        }
        //System.out.println("Built valid char array");
        return out;
    }

    public static boolean isSpacefonNumber(String n) {
        boolean state = false;
        try {
            if (n.length() > 10) {
                state = false;
                throw new Exception("The number is invalid: It is longer than 10 characters! Enter an Areeba number of 10 characters only!");
            } else if (n.length() == 10) {
                String firstThree = n.substring(0, 3);
                System.out.println("Number: " + firstThree);
                if ((firstThree.equals("024"))) {
                    state = true;
                } else {
                    state = false;
                    throw new Exception(
                            "The number is invalid. Enter an Areeba number of the form 0244954413 or 0243954413");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return state;
    }

    public static String formatMoney(double value) {
        /*DecimalFormat currency = new DecimalFormat("#######################");
             return currency.format(value);*/

        Money amt1 = new Money(value);
        return amt1.toString();
    }


    public static void setValidCharSet(ArrayList validCharset) {
        validCharSet = validCharset;
    }

    public ArrayList getValidCharSet() {
        return validCharSet;
    }

    public static boolean isNumberNoValidChars(String in)throws NumberFormatException{
	  try{
	  	long tmp = Long.parseLong(in);
		return true;
	  }catch(NumberFormatException e){
	  	return false;
	  }

    }

    

}
