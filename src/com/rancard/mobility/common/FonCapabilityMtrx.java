package com.rancard.mobility.common;

import com.rancard.common.Config;
import net.sourceforge.wurfl.wurflapi.*;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FonCapabilityMtrx {

    private UAManager uam = null;
    private CapabilityMatrix cm = null;
    private ListManager lm = null;
    private TreeMap allDevices = null;
    private TreeMap allActualDevices = null;
    private HashMap capabilities = null;
    private String wurflPath;
    private String wurflPatchPath;

    //instatiate the capabilities matrix
    public FonCapabilityMtrx() throws Exception{
        this.wurflPath = getWurflPath ();
        this.wurflPatchPath = getWurflPatchPath ();
        
        ObjectsManager.getWurflInstance(wurflPath);

        uam = ObjectsManager.getUAManagerInstance();
        cm = ObjectsManager.getCapabilityMatrixInstance();
        lm = ObjectsManager.getListManagerInstance();

        allDevices = lm.getDeviceGroupedByBrand();
        allActualDevices = lm.getActualDeviceElementsList();
        
        this.initCapabilities();
    }
    
    //get path to the XML file containing the capabilities matrix
    private String getWurflPath () throws Exception {
        
        String path = "WEB-INF/work";
        try {
            Context ctx = new InitialContext ();
            Config appConfig = (Config) ctx.lookup ("java:comp/env/bean/rmcsConfig");
            
            if(appConfig!=null &&  appConfig.valueOf ("WURFL")!=null){
                path = appConfig.valueOf ("WURFL");
            }
            
            
        } catch (NamingException e) {
            //   Log.write(e);
            throw new Exception (e.getMessage ());
        }
        
        return path;
        
    }
    
    //get the path to tbe XML patch for the WURFL XML
    private String getWurflPatchPath () throws Exception {
        
        String path = "WEB-INF/work";
        try {
            Context ctx = new InitialContext ();
            Config appConfig = (Config) ctx.lookup ("java:comp/env/bean/rmcsConfig");
            
            if(appConfig!=null &&  appConfig.valueOf ("WURFL_PATCH")!=null){
                path = appConfig.valueOf ("WURFL_PATCH");
            }
            
            
        } catch (NamingException e) {
            //   Log.write(e);
            throw new Exception (e.getMessage ());
        }
        
        return path;
        
    }

    //get list of phone brands
    public java.util.ArrayList getBrandNames() throws Exception {
        return this.lm.getDeviceBrandList();
    }

    //get list of phone models given the brand name
    public Object[] getModelNamesFor(String brand) throws Exception {
        TreeMap models = (TreeMap)this.allDevices.get(brand);
        return models.keySet().toArray();
    }

    //create an actual wireless device given the phone brand and model
    public WurflDevice getActualDevice(String brand, String model) throws Exception {

        TreeMap models = (TreeMap)this.allDevices.get(brand);
        return (WurflDevice) models.get(model);
    }

    //create an actual wireless device given tbe device ID
    public WurflDevice getActualDevice(String phoneId) throws Exception {
        if (this.allActualDevices.containsKey(phoneId)) {
            return (WurflDevice) allActualDevices.get(phoneId);
        } else {
            return null;
        }
    }

    //initiate the format-capability tree
    private void initCapabilities(){
        capabilities = new HashMap();
        ArrayList midi_mono, midi_poly, mmf, imy, amr, awb, aac, wav, mp3,
                spmidi, qcelp, wbmp, bmp, jpg, gif, tiff, png, vid3gpp, vid3gpp2,
                mp4, mov, wmv, jar, jad, sis, ems, emy, nokia, sagem1,
                sagem2, motorola, panasonic = null;
        (midi_poly = new ArrayList()).add("mid");
        midi_poly.add("midi");
        midi_poly.add("0.mid");
        midi_poly.add("1.mid");
        (mmf = new ArrayList()).add("mmf");
        mmf.add("ma1.mmf");
        mmf.add("ma2.mmf");
        mmf.add("ma3.mmf");
        (imy = new ArrayList()).add("imy");
        imy.add("10.imy");
        imy.add("12.imy");
        (amr = new ArrayList()).add("amr");
        (awb = new ArrayList()).add("awb");
        (aac = new ArrayList()).add("aac");
        (wav = new ArrayList()).add("wav");
        (mp3 = new ArrayList()).add("mp3");
        (spmidi = new ArrayList()).add("sp.mid");
        (qcelp = new ArrayList()).add("qcp");
        (wbmp = new ArrayList()).add("wbmp");
        (bmp = new ArrayList()).add("bmp");
        (jpg = new ArrayList()).add("jpg");
        jpg.add("jpeg");
        (gif = new ArrayList()).add("gif");
        (tiff = new ArrayList()).add("tiff");
        tiff.add("tif");
        (png = new ArrayList()).add("png");
        (vid3gpp = new ArrayList()).add("3gpp");
        vid3gpp.add("3gp");
        (vid3gpp2 = new ArrayList()).add("3gpp2");
        (mp4 = new ArrayList()).add("mp4");
        mp4.add("m4v");
        (mov = new ArrayList()).add("mov");
        (wmv = new ArrayList()).add("wmv");
        (ems = new ArrayList()).add("ems");
        ems.add("ems.extende");
        ems.add("emy");
        (emy = new ArrayList()).add("emy");
        (jar = new ArrayList()).add("jar");
        (jad = new ArrayList()).add("jad");
        (sis = new ArrayList()).add("sis");
        (nokia = new ArrayList()).add("noktxt");
        nokia.add("nokia");
        nokia.add("7bit.nokia");
        nokia.add("8bit.nokia");
        (motorola = new ArrayList()).add("motbin");
        motorola.add("mot");
        (sagem1 = new ArrayList()).add("10.sagem");
        (sagem2 = new ArrayList()).add("21.sagem");
        panasonic = new ArrayList();
/*
        capabilities.put("ringtone_midi_polyphonic", midi_poly);
        capabilities.put("ems_imelody", imy);
        capabilities.put("ringtone_mmf", mmf);
        capabilities.put("ringtone_amr", amr);
        capabilities.put("ringtone_awb", awb);
        capabilities.put("ringtone_aac", aac);
        capabilities.put("ringtone_wav", wav);
        capabilities.put("ringtone_mp3", mp3);
        capabilities.put("ringtone_spmidi", spmidi);
        capabilities.put("ringtone_qcelp", qcelp);
        capabilities.put("wallpaper_wbmp", wbmp);
        capabilities.put("wallpaper_bmp", bmp);
        capabilities.put("wallpaper_gif", gif);
        capabilities.put("wallpaper_jpg", jpg);
        capabilities.put("wallpaper_png", png);
        capabilities.put("wallpaper_tiff", tiff);
        capabilities.put("video_3gpp", vid3gpp);
        capabilities.put("video_3gpp2", vid3gpp2);
        capabilities.put("video_mp4", mp4);
        capabilities.put("video_wmv", wmv);
        capabilities.put("ems", ems);
        capabilities.put("nokiaring", nokia);
        capabilities.put("mms_symbian_install", sis);
        capabilities.put("mms_jar", jar);
        capabilities.put("mms_jad", jad);
        capabilities.put("gprtf", motorola);
        capabilities.put("sagem_v1", sagem1);
        capabilities.put("sagem_v2", sagem2);
        capabilities.put("panasonic", panasonic);
 */
        capabilities.put("midi_polyphonic", midi_poly);
        capabilities.put("imelody", imy);
        capabilities.put("mmf", mmf);
        capabilities.put("amr", amr);
        capabilities.put("awb", awb);
        capabilities.put("aac", aac);
        capabilities.put("wav", wav);
        capabilities.put("mp3", mp3);
        capabilities.put("sp_midi", spmidi);
        capabilities.put("qcelp", qcelp);
        capabilities.put("wbmp", wbmp);
        capabilities.put("bmp", bmp);
        capabilities.put("gif", gif);
        capabilities.put("jpg", jpg);
        capabilities.put("png", png);
        capabilities.put("tiff", tiff);
        capabilities.put("video_3gpp", vid3gpp);
        capabilities.put("video_3gpp2", vid3gpp2);
        capabilities.put("video_mp4", mp4);
        capabilities.put("video_wmv", wmv);
        capabilities.put("ems", ems);
        capabilities.put("nokiaring", nokia);
        capabilities.put("mms_symbian_install", sis);
        capabilities.put("mms_jar", jar);
        capabilities.put("mms_jad", jad);
        capabilities.put("gprtf", motorola);
        capabilities.put("sagem_v1", sagem1);
        capabilities.put("sagem_v2", sagem2);
        capabilities.put("panasonic", panasonic);
    }

    //get List Manager for the capabilities matrix
    public ListManager getListManager(){
        return this.lm;
    }

    //get Capabilities Manager for the capabilities matrix
    public CapabilityMatrix getCapabilitiesManager(){
        return this.cm;
    }

    //get User Agent Manager for the capabilities matrix
    public UAManager getUAManager(){
        return this.uam;
    }

    //matches a given file extension with a suppoted capability
    public String findSupportedCapability(String format){
        String capability = null;
        Object[] capa = this.capabilities.keySet().toArray();
        for(int i = 0; i < capa.length; i++){
            ArrayList fmts = (ArrayList)capabilities.get(capa[i].toString());
            if(fmts.contains(format)){
                capability = capa[i].toString();
                break;
            }
        }
        return capability;
    }

    /*public FonCapabilityMtrx(String wurflXML) throws Exception {
        ObjectsManager.getWurflInstance(wurflXML);

        uam = ObjectsManager.getUAManagerInstance();
        cm = ObjectsManager.getCapabilityMatrixInstance();
        lm = ObjectsManager.getListManagerInstance();

        allDevices = lm.getDeviceGroupedByBrand();
        allActualDevices = lm.getActualDeviceElementsList();
        this.initCapabilities();
    }

    public FonCapabilityMtrx(String wurflXML, String wurflPatch) throws Exception {
        ObjectsManager.getWurflInstance(wurflXML, wurflPatch);

        uam = ObjectsManager.getUAManagerInstance();
        cm = ObjectsManager.getCapabilityMatrixInstance();
        lm = ObjectsManager.getListManagerInstance();

        allDevices = lm.getDeviceGroupedByBrand();
        allActualDevices = lm.getActualDeviceElementsList();
        this.initCapabilities();
    }*/
    
    public static void main(String[] args) {
        FonCapabilityMtrx fcm = null;
        ArrayList al = null;
            Object[] models = null;
            Object[] capa = null;
            String formats = null;
            HashMap hm = null;
            WurflDevice dev = null;

        try{
            //fcm = new FonCapabilityMtrx("S:\\developer\\project\\mobilityContentServer211\\rmcs\\web\\WEB-INF\\wurfl.xml", "");
        } catch(Exception e){
            System.out.println(com.rancard.common.Feedback.PHONE_MATRIX_INIT_ERROR);
        }

        /*String capaValue = fcm.getCapabilitiesManager().getCapabilityForDevice("nokia_6680_ver1", "resolution_width");
        System.out.println(capaValue);
        capaValue = fcm.getCapabilitiesManager().getCapabilityForDevice("nokia_6680_ver1", "resolution_height");
        System.out.println(capaValue);*/

        try {
            //hm = fcm.lm.getCapabilitiesForDeviceID(dev.getId());
            //capa = hm.keySet().toArray();
            //al = fcm.getListManager().getCapabilitySet();
            //for (int i = 75; i < al.size(); i++) {
              //  System.out.println(al.get(i).toString());
            //}
        } catch (Exception ex) {
        }

        /*try{
            FonCapabilityMtrx fcm = new FonCapabilityMtrx();
            com.rancard.mobility.contentserver.PhoneMake pm = new com.rancard.
                    mobility.contentserver.PhoneMake();
            com.rancard.mobility.contentserver.Phone p = new com.rancard.
                    mobility.
                    contentserver.Phone();
            System.out.println("adding one");
            ArrayList al = null;
            Object[] models = null;
            Object[] capa = null;
            String formats = null;
            HashMap hm = null;
            WurflDevice dev = null;

            al = fcm.getListManager().getCapabilitySet();
            for (int i = 75; i < al.size(); i++) {
                System.out.println(al.get(i).toString());
            }

            try {
                al = fcm.getBrandNames();
                java.io.PrintWriter out = new java.io.PrintWriter(new java.io.FileWriter("c:\\phonelist.csv", true));
        out.println("Brand Name,Model,Device ID");
        out.close();
                //fw.close();
                for (int i = 0; i < al.size(); i++) {
                    models = fcm.getModelNamesFor(al.get(i).toString());
                    for(int j = 0; j < models.length; j++){
             dev = fcm.getActualDevice(al.get(i).toString(), models[j].toString());
             out = new java.io.PrintWriter(new java.io.FileWriter("c:\\phonelist.csv", true));
                        /*hm = fcm.lm.getCapabilitiesForDeviceID(dev.getId());
                        capa = hm.keySet().toArray();
                        formats = new String("30");
                        for(int k = 0; k < capa.length; k++){
                            if (capa[k].toString().equals("wallpaper_wbmp") && hm.get("wallpaper_wbmp").toString().equalsIgnoreCase("true"))
                                formats = formats + ",4";
                            else if(capa[k].toString().equals("wallpaper_bmp") && hm.get("wallpaper_bmp").toString().equalsIgnoreCase("true"))
                                formats = formats + ",26";
                            else if(capa[k].toString().equals("wallpaper_gif") && hm.get("wallpaper_gif").toString().equalsIgnoreCase("true"))
                                formats = formats + ",2";
                            else if(capa[k].toString().equals("wallpaper_jpg") && hm.get("wallpaper_jpg").toString().equalsIgnoreCase("true"))
                                formats = formats + ",1,22";
                            else if(capa[k].toString().equals("wallpaper_png") && hm.get("wallpaper_png").toString().equalsIgnoreCase("true"))
                                formats = formats + ",3";
                            else if(capa[k].toString().equals("wallpaper_tiff") && hm.get("wallpaper_tiff").toString().equalsIgnoreCase("true"))
                                formats = formats + ",35";
                            else if(capa[k].toString().equals("ringtone_midi_polyphonic") && hm.get("ringtone_midi_polyphonic").toString().equalsIgnoreCase("true"))
                                formats = formats + ",7,21";
                            else if(capa[k].toString().equals("ringtone_imelody") && hm.get("ringtone_imelody").toString().equalsIgnoreCase("true"))
                                formats = formats + ",36";
                            else if(capa[k].toString().equals("ringtone_mmf") && hm.get("ringtone_mmf").toString().equalsIgnoreCase("true"))
                                formats = formats + ",11";
                            else if(capa[k].toString().equals("ringtone_amr") && hm.get("ringtone_amr").toString().equalsIgnoreCase("true"))
                                formats = formats + ",13";
                            else if(capa[k].toString().equals("ringtone_aac") && hm.get("ringtone_aac").toString().equalsIgnoreCase("true"))
                                formats = formats + ",14";
                            else if(capa[k].toString().equals("ringtone_wav") && hm.get("ringtone_wav").toString().equalsIgnoreCase("true"))
                                formats = formats + ",17";
                            else if(capa[k].toString().equals("ringtone_mp3") && hm.get("ringtone_mp3").toString().equalsIgnoreCase("true"))
                                formats = formats + ",9";
                            else if(capa[k].toString().equals("video_3gpp") && hm.get("video_3gpp").toString().equalsIgnoreCase("true"))
                                formats = formats + ",12,20";
                            else if(capa[k].toString().equals("video_mp4") && hm.get("video_mp4").toString().equalsIgnoreCase("true"))
                                formats = formats + ",6";
                            else if(capa[k].toString().equals("ems_imelody") && hm.get("ems_imelody").toString().equalsIgnoreCase("true"))
                                formats = formats + ",38";
                            else if(capa[k].toString().equals("ems") && hm.get("ems").toString().equalsIgnoreCase("true"))
                                formats = formats + ",37";
                            else if(capa[k].toString().equals("mms_symbian_install") && hm.get("mms_symbian_install").toString().equalsIgnoreCase("true"))
                                formats = formats + ",34";
                            else if(capa[k].toString().equals("mms_jar") && hm.get("mms_jar").toString().equalsIgnoreCase("true"))
                                formats = formats + ",15";
                            else if(capa[k].toString().equals("mms_jad") && hm.get("mms_jad").toString().equalsIgnoreCase("true"))
                                formats = formats + ",16";
                            else if(capa[k].toString().equals("ringtone_qcelp") && hm.get("ringtone_qcelp").toString().equalsIgnoreCase("true"))
                                formats = formats + ",8";
                            else if(capa[k].toString().equals("mms_nokia_ringingtone") && hm.get("mms_nokia_ringingtone").toString().equalsIgnoreCase("true"))
                                formats = formats + ",27";
                        }
                        p.setMake(i+1);
                        p.setModel(dev.getModelName());
                        p.setSupportedFormats(formats);
                        p.insertPhone();
                        out.println(dev.getBrandName() + "," + dev.getModelName() + "," + dev.getId());
                        out.close();
                    }
                }

                     } catch (Exception ex1) {
                         System.out.println(ex1.getCause());
                     }
            Object[] array = fcm.lm.getListOfGroups().keySet().toArray();
            for (int i = 0; i < array.length; i++) {
                //System.out.println(array[i].toString());
            }

            HashMap hm2 = fcm.lm.getListOfGroups();
            System.out.println(hm2.get("object_download").toString());
        } catch(Exception e){

        }*/
    }
}
