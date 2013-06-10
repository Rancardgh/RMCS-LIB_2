package com.rancard.mobility.contentserver;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import com.mullassery.imaging.Imaging;
import com.unwiredtec.rtcreator.*;
import java.io.IOException;
import java.io.*;

import javax.naming.Context;
import javax.naming.NamingException;
import com.rancard.common.Config;

import javax.naming.InitialContext;
import com.Ostermiller.util.ExecHelper;


import com.mullassery.imaging.util.Util;
import com.mullassery.imaging.ImagingFactory;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import com.jswiff.SWFWriter;

public class ContentAdapter {
    
    //protected Imaging imaging = null;
    protected BufferedImage original = null;
    protected byte[] watermark = null;
    String workDirectory = null;
    String pluginLocation = null;
    String watermarkPath = null;
    String otbLocation = null;
    String midiconvlocation = null;
    String mp3ConvName = null;
    String mp3ConvPath = null;
    
    public ContentAdapter() throws Exception {
        this.workDirectory = getWorkDirectory();
        this.pluginLocation = getPluginLocation();
        this.watermarkPath = getWaterMarkLocation();
        this.otbLocation = getOTBConvertorLocation();
        this.midiconvlocation = getMIDIConvertorLocation();
        this.mp3ConvName = getMP3ConvName();
        this.mp3ConvPath = getMP3ConvPath();
    }
    
    // generate preview
    public static byte[] generateImagePreview(byte[] background, byte[] top, int x, int y, String extension) throws IOException {
        Imaging imaging = ImagingFactory.createImagingInstance(
                ImagingFactory.AWT_LOADER,
                ImagingFactory.JAVA2D_TRANSFORMER);
        
        java.io.InputStream in = new java.io.ByteArrayInputStream(background);
        java.io.ByteArrayOutputStream byteOut = new java.io.
                ByteArrayOutputStream();
        java.io.InputStream inTop = new java.io.ByteArrayInputStream(top);
        //InputStream ini = java.io.ByteArrayInputStream(by);
        
        String type = Util.correctImageType(extension);
        
        BufferedImage backgroundImg = javax.imageio.ImageIO.read(in);
        
        // BufferedImage backgroundImg = imaging.read(in);
        BufferedImage overlayImg = javax.imageio.ImageIO.read(inTop);
        
        backgroundImg = imaging.resize(backgroundImg, 150, 150, true);
        
        int a = (int) Util.convertToWhole("60%", backgroundImg.getWidth());
        int b = (int) Util.convertToWhole("60%", backgroundImg.getHeight());
        
        //BufferedImage im2 = ImageIO.read(url2);
        Graphics2D g = backgroundImg.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g.drawImage(overlayImg,
                (backgroundImg.getWidth() - overlayImg.getWidth()) / 2,
                (backgroundImg.getHeight() - overlayImg.getHeight()) / 2, null);
        g.dispose();
        /*BufferedImage newImg = Util.duplicate(backgroundImg);
                   newImg.createGraphics().drawImage(overlayImg, null, x, y);
         */
        
        //BufferedImage newImg =  imaging.overlay( backgroundImg,imaging.rotate(overlayImg, Util.degrees2Radians(-30)),5000,5000);
        //BufferedImage newImg = imaging.overlay(backgroundImg,overlayImg, 100, 100);
        javax.imageio.ImageIO.write(backgroundImg, "jpg", byteOut);
        // imaging.write(backgroundImg,"jpg", byteOut, type, 1);
        return byteOut.toByteArray();
    }
    
    // generate preview
    public static byte[] resizeImage(byte[] original, int x, int y, String extension) throws IOException {
        Imaging imaging = ImagingFactory.createImagingInstance(
                ImagingFactory.AWT_LOADER,
                ImagingFactory.JAVA2D_TRANSFORMER);
        
        java.io.InputStream in = new java.io.ByteArrayInputStream(original);
        java.io.ByteArrayOutputStream byteOut = new java.io.
                ByteArrayOutputStream();
        //InputStream ini = java.io.ByteArrayInputStream(by);
        
        String type = Util.correctImageType(extension);
        
        BufferedImage backgroundImg = javax.imageio.ImageIO.read(in);
        
        backgroundImg = imaging.resize(backgroundImg, x, y, true);
        
        //int a = (int) Util.convertToWhole("60%", backgroundImg.getWidth());
        //int b = (int) Util.convertToWhole("60%", backgroundImg.getHeight());
        
        /* //BufferedImage im2 = ImageIO.read(url2);
         Graphics2D g = backgroundImg.createGraphics();
         g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
         g.drawImage(overlayImg,
                     (backgroundImg.getWidth() - overlayImg.getWidth()) / 2,
         (backgroundImg.getHeight() - overlayImg.getHeight()) / 2, null);
         g.dispose();
         BufferedImage newImg = Util.duplicate(backgroundImg);
                    newImg.createGraphics().drawImage(overlayImg, null, x, y);
         */
        
        //BufferedImage newImg =  imaging.overlay( backgroundImg,imaging.rotate(overlayImg, Util.degrees2Radians(-30)),5000,5000);
        //BufferedImage newImg = imaging.overlay(backgroundImg,overlayImg, 100, 100);
        javax.imageio.ImageIO.write(backgroundImg, extension, byteOut);
        // imaging.write(backgroundImg,"jpg", byteOut, type, 1);
        return byteOut.toByteArray();
    }
    
    public void setWatermark(byte[] watermark) {
        this.watermark = watermark;
    }
    
    public byte[] generatePreview(byte[] original, String fileName) throws Exception {
        byte[] previewFile = null;
        int type = com.rancard.util.ExtManager.getType(fileName);
        String extension = com.rancard.util.ExtManager.getFileExtension(
                fileName);
        if (type == 1 || type == 2 || type == 3) {
            // find last index og original file name and change extension to wav
            previewFile = generateSoundPreview(original, fileName);
            
        } else if (type == 4 || type == 5) {
            if (watermark == null) {
                this.watermark = loadWaterMark();
            }
            previewFile = generateImagePreview(original, watermark, 100, 100,
                    extension);
        } else {
            throw new Exception("Preview cannot be generated for content type");
        }
        return previewFile;
    }
    
    public com.rancard.mobility.contentserver.uploadsBean generatePreview(com.rancard.mobility.contentserver.uploadsBean contentFile) throws
            Exception {
        
        byte[] original = contentFile.getDataStream();
        String fileName = contentFile.getfilename();
        com.rancard.mobility.contentserver.uploadsBean previewFile = new com.rancard.mobility.contentserver.uploadsBean();
        int type = com.rancard.util.ExtManager.getType(fileName);
        String extension = com.rancard.util.ExtManager.getFileExtension(
                fileName);
        if (type == 1 || type == 2 || type == 3) {
            // find last index og original file name and change extension to wav
            fileName = getWorkDirectory() + File.separator + contentFile.getid() + "." +
                    extension;
            
            previewFile.setDataStream(generateSoundPreview(original, fileName));
            
        } else if (type == 4 || type == 5) {
            if (watermark == null) {
                this.watermark = loadWaterMark();
            }
            previewFile.setDataStream(generateImagePreview(original, watermark,
                    100, 100,
                    extension));
            previewFile.setfilename(fileName);
        } else {
            throw new Exception("Preview cannot be generated for content type");
        }
        return previewFile;
    }
    
    // generate preview
    protected byte[] generateImagePreview(byte[] background, String top, String color, String font, int x, int y, String extension) {
        Imaging imaging = ImagingFactory.createImagingInstance(
                ImagingFactory.AWT_LOADER,
                ImagingFactory.JAVA2D_TRANSFORMER);
        
        java.io.InputStream in = new java.io.ByteArrayInputStream(background);
        java.io.ByteArrayOutputStream byteOut = new java.io.
                ByteArrayOutputStream();
        //java.io.InputStream inTop = new java.io.ByteArrayInputStream(top);
        //InputStream ini = java.io.ByteArrayInputStream(by);
        String type = Util.getExtentionType(extension);
        BufferedImage backgroundImg = imaging.read(in);
        //BufferedImage overlayImg = imaging.read(inTop);
        BufferedImage newImg = imaging.textOverlay(backgroundImg, top, x, y,
                Font.getFont(font), Color.getColor(color));
        imaging.write(newImg, byteOut, type, 0.75f);
        return byteOut.toByteArray();
    }
    
    public static byte[] generateImagePreview(java.io.InputStream in, String top, String color, String font, int x, int y, String extension) {
        Imaging imaging = ImagingFactory.createImagingInstance(
                ImagingFactory.AWT_LOADER,
                ImagingFactory.JAVA2D_TRANSFORMER);
        
        //java.io.InputStream in = new java.io.ByteArrayInputStream(background);
        java.io.ByteArrayOutputStream byteOut = new java.io.
                ByteArrayOutputStream();
        //java.io.InputStream inTop = new java.io.ByteArrayInputStream(top);
        //InputStream ini = java.io.ByteArrayInputStream(by);
        String type = Util.getExtentionType(extension);
        BufferedImage backgroundImg = imaging.read(in);
        
        //BufferedImage overlayImg = imaging.read(inTop);
        BufferedImage newImg = imaging.textOverlay(backgroundImg, top, x, y,
                Font.getFont(font), Color.getColor(color));
        imaging.write(newImg, byteOut, type, 0.75f);
        return byteOut.toByteArray();
    }
    
    // generate preview
    /**
     * This method generates a sound preview file in midi format which is 10 seconds long
     *
     **/
    public byte[] generateSoundPreview(byte[] soundFile) {
        
//
// open sound file
// convert a melody and put the results
// into a byte array named nokia
        Ringtone tone = Ringtone.openRingtone(soundFile);
        byte[] previewFile = tone.getRingtoneBytes(RingtoneConstants.
                FORMAT_FLASH);
        return previewFile;
// return soundFile;
    }
    
    public byte[] generateSoundPreview(byte[] soundFileBytes, String soundFileName) throws java.io.
            IOException, Exception, FileNotFoundException {
        // validate sound file
        // get path to tmp folder
        java.io.File soundFile = writeBytesToFile(soundFileBytes, soundFileName);
        String previewFileName = com.rancard.util.ExtManager.changeFileExtension(soundFileName, "noktxt");
        byte[] previewFileBytes = null;
        
        // create temp file
        String originalFilePath = soundFile.getAbsolutePath();
        String ext = com.rancard.util.ExtManager.getFileExtension(soundFileName);
        
        try {
            if (!ext.equalsIgnoreCase("mp3")) {
                //set system variables
                System.setProperty("rtc.parser.mp3.name", this.mp3ConvName);
                System.setProperty("rtc.parser.mp3.path", this.mp3ConvPath);
                System.setProperty("rtc.convertor.mp3.name", this.mp3ConvName);
                System.setProperty("rtc.convertor.mp3.path", this.mp3ConvPath);
                
                
                //convert using ringtone creator
                try{
                    Ringtone tone = Ringtone.openRingtone(soundFileBytes);
                    previewFileBytes = tone.getRingtoneBytes(RingtoneConstants.FORMAT_NOKIATXT);
                    java.io.File prevFile = writeBytesToFile(previewFileBytes, previewFileName);
                }catch(RingtoneConvertException rce){
                    System.out.println(rce.getMessage());
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
                soundFile.delete();
            }else{
                previewFileBytes = soundFileBytes;
            }
            if(previewFileBytes != null){
                java.io.ByteArrayInputStream bin = new ByteArrayInputStream(previewFileBytes);
                java.io.ByteArrayOutputStream bout = new ByteArrayOutputStream();
                
                com.jswiff.SWFDocument prevswf = com.jswiff.MediaImporter.importSound(bin, com.jswiff.MediaImporter.TYPE_MP3);
                SWFWriter writer = new SWFWriter(prevswf, bout);
                writer.write();
                previewFileBytes = bout.toByteArray();
            }else{
                throw new java.io.FileNotFoundException("Unable to generated preview file ");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception generalException) {
            System.out.println(generalException.getMessage());
        }
        
        return previewFileBytes;
    }
    
    private String getPluginLocation() throws Exception {
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            return appConfig.valueOf("RINGTONETOOLS");
        } catch (NamingException e) {
            //   Log.write(e);
            throw new Exception(e.getMessage());
        }
        
        // return "S:\\developer\\project\\rcs\\contentmanager\\rmscontentserver\\WEB-INF\\contentadaptation\\soundtools\\ringtonetools.exe";
        
    }
    
    private String getOTBConvertorLocation() throws Exception {
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            return appConfig.valueOf("OTA_BITMAP_TOOL");
        } catch (NamingException e) {
            //   Log.write(e);
            throw new Exception(e.getMessage());
        }
        
        // return "S:\\developer\\project\\rcs\\contentmanager\\rmscontentserver\\WEB-INF\\contentadaptation\\soundtools\\ringtonetools.exe";
        
    }
    
    private String getMIDIConvertorLocation() throws Exception {
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            return appConfig.valueOf("MIDI_CONV_TOOL");
        } catch (NamingException e) {
            //   Log.write(e);
            throw new Exception(e.getMessage());
        }
        
        // return "S:\\developer\\project\\rcs\\contentmanager\\rmscontentserver\\WEB-INF\\contentadaptation\\soundtools\\ringtonetools.exe";
        
    }
    
    private String getWaterMarkLocation() throws Exception {
        String watermarkfilepath = null;
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            
            watermarkfilepath = appConfig.valueOf("WATERMARK_PATH");
            if (watermarkfilepath != null && new File(watermarkfilepath).exists()) {
                System.out.println("Found watermark path as: " + watermarkfilepath);
                return watermarkfilepath;
            } else {
                System.out.println("Could not find watermark path. Currently set as: " + watermarkfilepath);
                throw new FileNotFoundException(
                        "Unable to find work directoru or work directory not set for content adapter");
            }
            
        } catch (NamingException e) {
            //   Log.write(e);
            throw new Exception(e.getMessage());
        }
        
        //return "S:\\developer\\project\\rcs\\contentmanager\\rmscontentserver\\WEB-INF\\contentadaptation\\imagetools\\preview_black.gif";
    }
    
    private byte[] loadWaterMark() throws FileNotFoundException, Exception {
        byte[] watermark = null;
        
        InputStream in = null;
        ByteArrayOutputStream out = null;
        if (this.watermarkPath == null) {
            this.watermarkPath = getWaterMarkLocation();
        }
        try {
            in = new FileInputStream(this.watermarkPath);
            out = new ByteArrayOutputStream();
            
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            watermark = out.toByteArray();
            in.close();
            out.close();
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException(
                    "Unable to locate watermark file or watermark file not set in properties file");
        } catch (IOException ex) {
        }
        
        return watermark;
    }
    
    private String getWorkDirectory() throws Exception {
        
        try {
            Context ctx = new InitialContext();
            // get refference to application server context
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            String workDir = appConfig.valueOf("WORKDIR");
            if (workDir != null && new File(workDir).exists()) {
                return workDir;
            } else {
                throw new FileNotFoundException(
                        "Unable to find work directoru or work directory not set for content adapter");
            }
        } catch (NamingException e) {
            //   Log.write(e);
            throw new Exception(e.getMessage());
        }
        
        
        //return "S:\\developer\\work\\";
    }
    
    private String getMP3ConvName() throws Exception {
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            return appConfig.valueOf("MP3_CONV_NAME");
        } catch (NamingException e) {
            //   Log.write(e);
            throw new Exception(e.getMessage());
        }
    }
    
    private String getMP3ConvPath() throws Exception {
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            return appConfig.valueOf("MP3_CONV_PATH");
        } catch (NamingException e) {
            //   Log.write(e);
            throw new Exception(e.getMessage());
        }
        
        // return "S:\\developer\\project\\rcs\\contentmanager\\rmscontentserver\\WEB-INF\\contentadaptation\\soundtools\\ringtonetools.exe";
        
    }
    
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        
        // Get the size of the file
        long length = file.length();
        
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];
        
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                &&
                (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " +
                    file.getName());
        }
        
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    
    public static File writeBytesToFile(byte[] src, String dst) throws IOException {
        InputStream in = new ByteArrayInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        return new File(dst);
        
    }
    
    public byte[] generateOTB(byte[] imageBytes, String imageFileName) throws java.io.
            IOException, Exception, FileNotFoundException {
        String path = this.workDirectory;
        // check if directory exists for listId
        File workDirectory = new File(path);
        if (!(workDirectory.exists() && workDirectory.isDirectory())) {
            if (!workDirectory.mkdir()) {
                throw new Exception("Unable to find work directory. Please check work directory settings in properties file");
            }
        }
        // create directory for listId
        File otbWorkDirectory = new File(workDirectory.getAbsolutePath() + File.separator + "otb");
        if (!(otbWorkDirectory.exists() && otbWorkDirectory.isDirectory())) {
            if (!otbWorkDirectory.mkdirs()) {
                throw new Exception(
                        "Unable to create work directory for OTB files. Check security permissions");
            }
        }
        String absPath = otbWorkDirectory.getAbsolutePath() + File.separator + imageFileName;
        java.io.File imageFile = writeBytesToFile(imageBytes, absPath);
        String otbFileName = com.rancard.util.ExtManager.changeFileExtension(absPath, "otb");
        
        byte[] otbFile = null;
        File otbImageFile = new File(otbFileName);
        if(otbImageFile.exists()){
            otbFile = getBytesFromFile(otbImageFile);
        }else{
            // create temp file
            path = imageFile.getAbsolutePath();
            String ext = com.rancard.util.ExtManager.getFileExtension(imageFileName);
            
            try {
                // get runtime exec to conversiontool
                String _otbtools = getOTBConvertorLocation();
                
                // Execute a command with an argument that contains a space
                String[] commands = new String[] {_otbtools, path, otbFileName};
                // commands
                ExecHelper child = ExecHelper.exec(commands);
                // if command is sucessful
                if (0 == child.getStatus()) {
                    otbImageFile = new File(otbFileName);
                    boolean exists = otbImageFile.exists();
                    if (exists) {
                        // File or directory exists
                        //read previewfile
                        otbFile = getBytesFromFile(otbImageFile);
                        // delete prev file and temp
                        //otbImageFile.delete();
                        imageFile.delete();
                    } else {
                        throw new java.io.FileNotFoundException("Unable to generated OTB file ");
                    }
                    // destroy tmp file
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new Exception ("Could not create otb file");
            } catch (Exception generalException) {
                System.out.println(generalException.getMessage());
                throw new Exception ("Could not create otb file");
            }
        }
        
        return otbFile;
    }
    
    public void testWAV() throws java.io.
            IOException, Exception, FileNotFoundException {
        
        try {
            // get runtime exec to conversiontool
            String _ringtonetools = "C:\\rmcsresources\\contentadaptation\\soundtools\\timidity\\timidity-con.exe";
            
            // Execute a command with an argument that contains a space
            String[] commands = new String[] {_ringtonetools, "-Ow", "-s 44100", "-o", "c:\\test1.wav", "c:\\greatest_love_of_all.mid"};
            // commands
            ExecHelper child = ExecHelper.exec(commands);
            // if command is sucessful
            if (0 == child.getStatus()) {
                File prevFile = new File("c:\\test1.wav");
                boolean exists = prevFile.exists();
                if (exists) {
                    // delete prev file and temp
                    //prevFile.delete ();
                } else {
                    throw new java.io.FileNotFoundException(
                            "Unable to generated preview file ");
                }
                // destroy tmp file
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception generalException) {
            System.out.println(generalException.getMessage());
        }
    }
    
    public void testAU() throws java.io.
            IOException, Exception, FileNotFoundException {
        
        try {
            // get runtime exec to conversiontool
            String _ringtonetools = midiconvlocation;
            
            // Execute a command with an argument that contains a space
            String[] commands = new String[] {_ringtonetools, "-Ou", /*"-s 44100",*/ "-o", "c:\\test1.au", "c:\\greatest_love_of_all.mid"};
            // commands
            ExecHelper child = ExecHelper.exec(commands);
            // if command is sucessful
            if (0 == child.getStatus()) {
                File prevFile = new File("c:\\test1.au");
                boolean exists = prevFile.exists();
                if (exists) {
                    // delete prev file and temp
                    //prevFile.delete ();
                } else {
                    throw new java.io.FileNotFoundException(
                            "Unable to generated preview file ");
                }
                // destroy tmp file
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception generalException) {
            System.out.println(generalException.getMessage());
        }
    }
    
    /**
     * This method generates a sound preview file in wav format which is 10 seconds long.
     *  it requires the file extension to be specificed in the filename
     *
     *
     * public byte[] generateSoundPreview(byte[] soundFileBytes,
     * String soundFileName
     * ) throws java.io.
     * IOException, Exception, FileNotFoundException {
     * // validate sound file
     * // get path to tmp folder
     * java.io.File soundFile = writeBytesToFile(soundFileBytes, soundFileName);
     * String previewFileName = com.rancard.util.ExtManager.
     * changeFileExtension(soundFileName, "wav");
     *
     * // sound preview files are best viewed as swf or wav
     * byte[] previewFile = null;
     * // create temp file
     * String path = soundFile.getAbsolutePath();
     * try {
     * // get runtime exec to conversiontool
     * String _ringtonetools = getPluginLocation();
     *
     * // Execute a command with an argument that contains a space
     * String[] commands = new String[] {_ringtonetools, path,
     * previewFileName};
     * // commands
     * ExecHelper child = ExecHelper.exec(commands);
     * // if command is sucessful
     * if (0 == child.getStatus()) {
     * File prevFile = new File(previewFileName);
     * boolean exists = prevFile.exists();
     * if (exists) {
     * // File or directory exists
     * //read previewfile
     * previewFile = getBytesFromFile(prevFile);
     * // delete prev file and temp
     * prevFile.delete();
     * soundFile.delete();
     * java.io.ByteArrayInputStream bin = new ByteArrayInputStream(
     * previewFile);
     * java.io.ByteArrayOutputStream bout = new
     * ByteArrayOutputStream();
     *
     * com.jswiff.SWFDocument prevswf = com.jswiff.MediaImporter.
     * importSound(bin, com.jswiff.MediaImporter.TYPE_WAV);
     * SWFWriter writer = new SWFWriter(prevswf, bout);
     * writer.write();
     * previewFile = bout.toByteArray();
     *
     * } else {
     * throw new java.io.FileNotFoundException(
     * "Unable to generated preview file ");
     * }
     * // destroy tmp file
     * }
     * } catch (IOException e) {
     * System.out.println(e.getMessage());
     * } catch (Exception generalException) {
     * System.out.println(generalException.getMessage());
     * }
     *
     * return previewFile;
     * }*/
    
    /*public byte[] generateSoundPreview (byte[] soundFileBytes, String soundFileName) throws java.io.
            IOException, Exception, FileNotFoundException {
        // validate sound file
        // get path to tmp folder
        java.io.File soundFile = writeBytesToFile (soundFileBytes, soundFileName);
        String previewFileName = com.rancard.util.ExtManager.
                changeFileExtension (soundFileName, "au");
     
        // sound preview files are best viewed as swf or wav
        byte[] previewFile = null;
        // create temp file
        String path = soundFile.getAbsolutePath ();
        String ext = com.rancard.util.ExtManager.getFileExtension (soundFileName);
     
        try {
            if (!ext.equalsIgnoreCase ("mp3")) {
                String _ringtonetools = new String ();
                // get runtime exec to conversiontool
                if(ext.equalsIgnoreCase ("mid")){
                    _ringtonetools = getMIDIConvertorLocation ();
                } else {
                    _ringtonetools = getPluginLocation ();
                }
     
                // Execute a command with an argument that contains a space
                String[] commands = null;
                if(ext.equalsIgnoreCase ("mid")){
                    commands = new String[] {_ringtonetools, "-Ou", "-o", previewFileName, path};
                } else {
                    commands = new String[] {_ringtonetools, path, previewFileName};
                }
                // commands
                ExecHelper child = ExecHelper.exec (commands);
                // if command is sucessful
                if (0 == child.getStatus ()) {
                    File prevFile = new File (previewFileName);
                    boolean exists = prevFile.exists ();
                    if (exists) {
                        // File or directory exists
                        //read previewfile
                        previewFile = getBytesFromFile (prevFile);
                        // delete prev file and temp
                        prevFile.delete ();
                        soundFile.delete ();
                    } else {
                        throw new java.io.FileNotFoundException (
                                "Unable to generated preview file ");
                    }
                    // destroy tmp file
                }
            }else{
                previewFile = soundFileBytes;
            }
            if(previewFile != null){
                java.io.ByteArrayInputStream bin = new
                        ByteArrayInputStream (
                        previewFile);
                java.io.ByteArrayOutputStream bout = new
                        ByteArrayOutputStream ();
     
                com.jswiff.SWFDocument prevswf = com.jswiff.
                        MediaImporter.
                        importSound (bin,
                        com.jswiff.MediaImporter.TYPE_WAV);
                SWFWriter writer = new SWFWriter (prevswf, bout);
                writer.write ();
                previewFile = bout.toByteArray ();
            }else{
                throw new java.io.FileNotFoundException (
                        "Unable to generated preview file ");
            }
        } catch (IOException e) {
            System.out.println (e.getMessage ());
        } catch (Exception generalException) {
            System.out.println (generalException.getMessage ());
        }
     
        return previewFile;
    }*/
    
    /*/ generate preview
    public byte[] convertImage(byte[] InputFile, String outputformat,String color, String font, int x,
                        int y, String extension) {
     
        java.io.InputStream in = new java.io.ByteArrayInputStream(background);
        java.io.ByteArrayOutputStream byteOut = new java.io.
                                 ByteArrayOutputStream();
        //java.io.InputStream inTop = new java.io.ByteArrayInputStream(top);
        //InputStream ini = java.io.ByteArrayInputStream(by);
        String type = Util.getExtentionType(extension);
        BufferedImage backgroundImg = imaging.read(in);
        //BufferedImage overlayImg = imaging.read(inTop);
        BufferedImage newImg = imaging.textOverlay(backgroundImg,top, x, y,Font.getFont(font), Color.getColor(color));
        imaging.write(newImg, byteOut, type, 0.75f);
        return byteOut.toByteArray();
    }*/
    
}
