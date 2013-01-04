package com.rancard.util;

import java.util.*;

public abstract class ExtManager {
    
    private static HashMap formatLookUp = new HashMap();
    private static HashMap typeLookUp = new HashMap();
    private static HashMap typeMatch = new HashMap();
    
    public static int getTypeForFormat(String fileExt) throws Exception {
        initTypeLookUp();
        if (fileExt != null) {
            fileExt = fileExt.toLowerCase();
            try {
                if (typeLookUp.containsKey(fileExt)) {
                    return new Integer(typeLookUp.get(fileExt).toString()).
                            intValue();
                } else {
                    return 0;
                }
            } catch (Exception e) {
                throw new Exception("Key not found.");
            }
        } else {
            throw new Exception("extension not specified.");
        }
    }
    
    public static boolean isSupportedFormat(String fileExt) throws Exception {
        initTypeLookUp();
        if (fileExt != null) {
            fileExt = fileExt.toLowerCase();
            try {
                if (typeLookUp.containsKey(fileExt)) {
                    return true; //new Integer(typeLookUp.get(fileExt).toString()).intValue();
                } else {
                    return false;
                }
            } catch (Exception e) {
                throw new Exception("Key not found.");
            }
        } else {
            throw new Exception("extension not specified.");
        }
    }
    
    public static String getFormatFor(String fileExt) throws Exception {
        initFormatLookUp();
        if (fileExt != null) {
            fileExt = fileExt.toLowerCase();
            try {
                return formatLookUp.get(fileExt).toString();
            } catch (Exception e) {
                throw new Exception("Key not found.");
            }
        } else {
            throw new Exception("extension not specified.");
        }
    }
    
    private static void initTypeLookUp() {
        typeLookUp.put("jpg", "5");
        typeLookUp.put("gif", "5");
        typeLookUp.put("png", "5");
        typeLookUp.put("wbmp", "4");
        typeLookUp.put("tiff", "5");
        typeLookUp.put("jpeg", "5");
        typeLookUp.put("bmp", "4");
        typeLookUp.put("noktxt", "1");
        typeLookUp.put("emy", "1");
        typeLookUp.put("imy", "1");
        typeLookUp.put("ems", "1");
        typeLookUp.put("amr", "3");
        typeLookUp.put("midi", "2");
        typeLookUp.put("mid", "2");
        typeLookUp.put("m4a", "3");
        typeLookUp.put("aac", "3");
        typeLookUp.put("mp3", "3");
        typeLookUp.put("qcp", "3");
        typeLookUp.put("awb", "3");
        typeLookUp.put("wav", "3");
        typeLookUp.put("wma", "3");
        typeLookUp.put("pmd", "3");
        typeLookUp.put("jar", "7");
        typeLookUp.put("jad", "7");
        typeLookUp.put("m4v", "6");
        typeLookUp.put("3gpp", "6");
        typeLookUp.put("3gp", "6");
        typeLookUp.put("avi", "6");
        typeLookUp.put("mpg", "6");
        typeLookUp.put("mpeg", "6");
        typeLookUp.put("txt", "8");
        typeLookUp.put("cab", "7");
        typeLookUp.put("gcd", "8");
        typeLookUp.put("ott", "8");
        typeLookUp.put("sis", "7");
        typeLookUp.put("mp4", "6");
        typeLookUp.put("mmf", "2");
        typeLookUp.put("m4v", "6");
        typeLookUp.put("0.mid", "2");
        typeLookUp.put("1.mid", "2");
        typeLookUp.put("ma1.mmf", "2");
        typeLookUp.put("ma2.mmf", "2");
        typeLookUp.put("ma3.mmf", "2");
        typeLookUp.put("10.imy", "1");
        typeLookUp.put("12.imy", "1");
        typeLookUp.put("rtttl", "1");
        typeLookUp.put("rtx", "1");
        typeLookUp.put("nokia", "1");
        typeLookUp.put("bin", "1");
        typeLookUp.put("ott", "1");
        typeLookUp.put("7bit.nokia", "1");
        typeLookUp.put("8bit.nokia", "1");
        typeLookUp.put("10.sagem", "1");
        typeLookUp.put("emy.extended", "1");
        typeLookUp.put("motbin", "1");
        typeLookUp.put("mot", "1");
        typeLookUp.put("sagem", "1");
        typeLookUp.put("au", "3");
        typeLookUp.put("aiff", "3");
        typeLookUp.put("swf", "3");
        typeLookUp.put("mfi", "3");
        typeLookUp.put("apk", "7");
        typeLookUp.put("flv", "6");
    }
    
    private static void initFormatLookUp() {
        formatLookUp.put("jpg", "1");
        formatLookUp.put("gif", "2");
        formatLookUp.put("png", "3");
        formatLookUp.put("wbmp", "4");
        formatLookUp.put("tiff", "35");
        formatLookUp.put("jpeg", "22");
        formatLookUp.put("bmp", "26");
        formatLookUp.put("noktxt", "27");
        formatLookUp.put("emy", "38");
        formatLookUp.put("imy", "36");
        formatLookUp.put("ems", "37");
        formatLookUp.put("amr", "13");
        formatLookUp.put("midi", "21");
        formatLookUp.put("mid", "7");
        formatLookUp.put("m4a", "10");
        formatLookUp.put("aac", "14");
        formatLookUp.put("mp3", "9");
        formatLookUp.put("qcp", "8");
        //formatLookUp.put("awb", "3");
        formatLookUp.put("wav", "17");
        formatLookUp.put("wma", "18");
        formatLookUp.put("pmd", "5");
        formatLookUp.put("jar", "15");
        formatLookUp.put("jad", "16");
        formatLookUp.put("m4v", "19");
        formatLookUp.put("3gpp", "20");
        formatLookUp.put("3gp", "50");
        formatLookUp.put("avi", "23");
        formatLookUp.put("mpg", "24");
        formatLookUp.put("mpeg", "25");
        formatLookUp.put("txt", "30");
        formatLookUp.put("cab", "31");
        formatLookUp.put("gcd", "32");
        formatLookUp.put("ott", "33");
        formatLookUp.put("sis", "34");
        formatLookUp.put("mp4", "6");
        formatLookUp.put("mmf", "11");
        formatLookUp.put("0.mid", "7");
        formatLookUp.put("1.mid", "7");
        formatLookUp.put("ma1.mmf", "11");
        formatLookUp.put("ma2.mmf", "11");
        formatLookUp.put("ma3.mmf", "11");
        formatLookUp.put("10.imy", "45");
        formatLookUp.put("12.imy", "46");
        formatLookUp.put("rtttl", "39");
        formatLookUp.put("rtx", "40");
        formatLookUp.put("nokia", "41");
        //formatLookUp.put("bin", "");
        formatLookUp.put("7bit.nokia", "42");
        formatLookUp.put("8bit.nokia", "43");
        formatLookUp.put("10.sagem", "44");
        formatLookUp.put("emy.extended", "47");
        formatLookUp.put("motbin", "48");
        formatLookUp.put("apk", "52");
        formatLookUp.put("flv", "51");
        //formatLookUp.put("au", "");
        //formatLookUp.put("aiff", "3");
        //formatLookUp.put("swf", "3");
        //formatLookUp.put("mfi", "3");
    }
    
    private static void initTypeMatch(){
        ArrayList wallpaperFmts = new ArrayList();
        ArrayList logoFmts = new ArrayList();
        ArrayList videoFmts = new ArrayList();
        ArrayList monophoneFmts = new ArrayList();
        ArrayList polyphoneFmts = new ArrayList();
        ArrayList realtoneFmts = new ArrayList();
        ArrayList gameFmts = new ArrayList();
        ArrayList themeFmts = new ArrayList();
        ArrayList aniWallpaperFmts = new ArrayList();
        
        int[] wallarr = {1,2,3,4,35,22,};
        for(int i = 0; i < wallarr.length; i++){
            wallpaperFmts.add(wallarr[i]);
        }
        
        int[] logoarr = {4,26};
        for(int i = 0; i < logoarr.length; i++){
            logoFmts.add(logoarr[i]);
        }
        
        int[] vidarr = {19,20,50,23,24,25,6,51};
        for(int i = 0; i < vidarr.length; i++){
            videoFmts.add(vidarr[i]);
        }
        
        int[] monoarr = {27,38,36,37,45,46,39,40,41,42,43,44,45,46,47,48};
        for(int i = 0; i < monoarr.length; i++){
            monophoneFmts.add(monoarr[i]);
        }
        
        int[] polyarr = {21,7,11};
        for(int i = 0; i < polyarr.length; i++){
            polyphoneFmts.add(polyarr[i]);
        }
        
        int[] realarr = {13,10,14,9,8,17,18,6};
        for(int i = 0; i < realarr.length; i++){
            realtoneFmts.add(realarr[i]);
        }
        
        int[] thmarr = {15,16,31,34,52};
        for(int i = 0; i < thmarr.length; i++){
            themeFmts.add(thmarr[i]);
            gameFmts.add(thmarr[i]);
        }
        
        int[] aniarr = {2};
        for(int i = 0; i < aniarr.length; i++){
            aniWallpaperFmts.add(aniarr[i]);
        }
        
        typeMatch.put("1", monophoneFmts);
        typeMatch.put("2", polyphoneFmts);
        typeMatch.put("3", realtoneFmts);
        typeMatch.put("4", logoFmts);
        typeMatch.put("5", wallpaperFmts);
        typeMatch.put("6", videoFmts);
        typeMatch.put("7", gameFmts);
        typeMatch.put("9", aniWallpaperFmts);
        typeMatch.put("10", themeFmts);
    }
    
    public static  boolean match(String typeId, String formatId) throws Exception {
        initTypeMatch();
        ArrayList formats = (ArrayList) typeMatch.get(typeId);
        try{
            if(formats.contains(new Integer(formatId))){
                return true;
            }else{
                return false;
            }
        }catch(NumberFormatException e){
            return false;
        }
    }
    
    public static String stripAwayFileExtension(String fileName) {
        
        int x = fileName.lastIndexOf(".");
        if (x != 0) {
            fileName = fileName.substring(0, x);
        }
        
        return fileName;
    }
    
    public static String changeFileExtension(String fileName, String newExt) {
        
        int x = fileName.lastIndexOf(".");
        if (x > 0) {
            fileName = fileName.substring(0, x);
        }
        
        return fileName + "." + newExt;
    }
    
    public static String getFileExtension(String fileName) {
        String ext = "";
        int x = fileName.lastIndexOf(".");
        if (x != 0) {
            ext = fileName.substring(x + 1, fileName.length());
        }
        
        return ext;
    }
    
    
    public static int getType(String fileName) throws Exception {
        
        String ext = getFileExtension(fileName);
        int type = getTypeForFormat(ext);
        return type;
    }
    
    public static void main(String[] args) {
        try {
            System.out.println(ExtManager.getTypeForFormat("motbin"));
        } catch (Exception ex) {
        }
        try {
            System.out.println(ExtManager.getTypeForFormat("aiff"));
        } catch (Exception ex1) {
        }
    }
    
}
