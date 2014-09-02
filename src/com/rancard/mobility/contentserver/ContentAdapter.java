package com.rancard.mobility.contentserver;

import com.Ostermiller.util.ExecHelper;
import com.jswiff.MediaImporter;
import com.jswiff.SWFDocument;
import com.jswiff.SWFWriter;
import com.mullassery.imaging.Imaging;
import com.mullassery.imaging.ImagingFactory;
import com.mullassery.imaging.util.Util;
import com.rancard.common.Config;
import com.rancard.util.ExtManager;
import com.unwiredtec.rtcreator.Ringtone;
import com.unwiredtec.rtcreator.RingtoneConvertException;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContentAdapter {
    protected BufferedImage original = null;
    protected byte[] watermark = null;
    String workDirectory = null;
    String pluginLocation = null;
    String watermarkPath = null;
    String otbLocation = null;
    String midiconvlocation = null;
    String mp3ConvName = null;
    String mp3ConvPath = null;

    public ContentAdapter()
            throws Exception {
        this.workDirectory = getWorkDirectory();
        this.pluginLocation = getPluginLocation();
        this.watermarkPath = getWaterMarkLocation();
        this.otbLocation = getOTBConvertorLocation();
        this.midiconvlocation = getMIDIConvertorLocation();
        this.mp3ConvName = getMP3ConvName();
        this.mp3ConvPath = getMP3ConvPath();
    }

    public static byte[] generateImagePreview(byte[] background, byte[] top, int x, int y, String extension)
            throws IOException {
        Imaging imaging = ImagingFactory.createImagingInstance(4, 3);


        InputStream in = new ByteArrayInputStream(background);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        InputStream inTop = new ByteArrayInputStream(top);


        String type = Util.correctImageType(extension);

        BufferedImage backgroundImg = ImageIO.read(in);


        BufferedImage overlayImg = ImageIO.read(inTop);

        backgroundImg = imaging.resize(backgroundImg, 150, 150, true);

        int a = (int) Util.convertToWhole("60%", backgroundImg.getWidth());
        int b = (int) Util.convertToWhole("60%", backgroundImg.getHeight());


        Graphics2D g = backgroundImg.createGraphics();
        g.setComposite(AlphaComposite.getInstance(3, 0.4F));
        g.drawImage(overlayImg, (backgroundImg.getWidth() - overlayImg.getWidth()) / 2, (backgroundImg.getHeight() - overlayImg.getHeight()) / 2, null);


        g.dispose();


        ImageIO.write(backgroundImg, "jpg", byteOut);

        return byteOut.toByteArray();
    }

    public static byte[] resizeImage(byte[] original, int x, int y, String extension)
            throws IOException {
        Imaging imaging = ImagingFactory.createImagingInstance(4, 3);


        InputStream in = new ByteArrayInputStream(original);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();


        String type = Util.correctImageType(extension);

        BufferedImage backgroundImg = ImageIO.read(in);

        backgroundImg = imaging.resize(backgroundImg, x, y, true);


        ImageIO.write(backgroundImg, extension, byteOut);

        return byteOut.toByteArray();
    }

    public void setWatermark(byte[] watermark) {
        this.watermark = watermark;
    }

    public byte[] generatePreview(byte[] original, String fileName)
            throws Exception {
        byte[] previewFile = null;
        int type = ExtManager.getType(fileName);
        String extension = ExtManager.getFileExtension(fileName);
        if ((type == 1) || (type == 2) || (type == 3)) {
            previewFile = generateSoundPreview(original, fileName);
        } else if ((type == 4) || (type == 5)) {
            if (this.watermark == null) {
                this.watermark = loadWaterMark();
            }
            previewFile = generateImagePreview(original, this.watermark, 100, 100, extension);
        } else {
            throw new Exception("Preview cannot be generated for content type");
        }
        return previewFile;
    }

    public uploadsBean generatePreview(uploadsBean contentFile)
            throws Exception {
        byte[] original = contentFile.getDataStream();
        String fileName = contentFile.getfilename();
        uploadsBean previewFile = new uploadsBean();
        int type = ExtManager.getType(fileName);
        String extension = ExtManager.getFileExtension(fileName);
        if ((type == 1) || (type == 2) || (type == 3)) {
            fileName = getWorkDirectory() + File.separator + contentFile.getid() + "." + extension;


            previewFile.setDataStream(generateSoundPreview(original, fileName));
        } else if ((type == 4) || (type == 5)) {
            if (this.watermark == null) {
                this.watermark = loadWaterMark();
            }
            previewFile.setDataStream(generateImagePreview(original, this.watermark, 100, 100, extension));


            previewFile.setfilename(fileName);
        } else {
            throw new Exception("Preview cannot be generated for content type");
        }
        return previewFile;
    }

    protected byte[] generateImagePreview(byte[] background, String top, String color, String font, int x, int y, String extension) {
        Imaging imaging = ImagingFactory.createImagingInstance(4, 3);


        InputStream in = new ByteArrayInputStream(background);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();


        String type = Util.getExtentionType(extension);
        BufferedImage backgroundImg = imaging.read(in);

        BufferedImage newImg = imaging.textOverlay(backgroundImg, top, x, y, Font.getFont(font), Color.getColor(color));

        imaging.write(newImg, byteOut, type, 0.75F);
        return byteOut.toByteArray();
    }

    public static byte[] generateImagePreview(InputStream in, String top, String color, String font, int x, int y, String extension) {
        Imaging imaging = ImagingFactory.createImagingInstance(4, 3);


        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();


        String type = Util.getExtentionType(extension);
        BufferedImage backgroundImg = imaging.read(in);


        BufferedImage newImg = imaging.textOverlay(backgroundImg, top, x, y, Font.getFont(font), Color.getColor(color));

        imaging.write(newImg, byteOut, type, 0.75F);
        return byteOut.toByteArray();
    }

    public byte[] generateSoundPreview(byte[] soundFile) {
        Ringtone tone = Ringtone.openRingtone(soundFile);
        byte[] previewFile = tone.getRingtoneBytes("FLASH");

        return previewFile;
    }

    public byte[] generateSoundPreview(byte[] soundFileBytes, String soundFileName)
            throws IOException, Exception, FileNotFoundException {
        File soundFile = writeBytesToFile(soundFileBytes, soundFileName);
        String previewFileName = ExtManager.changeFileExtension(soundFileName, "noktxt");
        byte[] previewFileBytes = null;


        String originalFilePath = soundFile.getAbsolutePath();
        String ext = ExtManager.getFileExtension(soundFileName);
        try {
            if (!ext.equalsIgnoreCase("mp3")) {
                System.setProperty("rtc.parser.mp3.name", this.mp3ConvName);
                System.setProperty("rtc.parser.mp3.path", this.mp3ConvPath);
                System.setProperty("rtc.convertor.mp3.name", this.mp3ConvName);
                System.setProperty("rtc.convertor.mp3.path", this.mp3ConvPath);
                try {
                    Ringtone tone = Ringtone.openRingtone(soundFileBytes);
                    previewFileBytes = tone.getRingtoneBytes("NOKIATXT");
                    File prevFile = writeBytesToFile(previewFileBytes, previewFileName);
                } catch (RingtoneConvertException rce) {
                    System.out.println(rce.getMessage());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                soundFile.delete();
            } else {
                previewFileBytes = soundFileBytes;
            }
            if (previewFileBytes != null) {
                ByteArrayInputStream bin = new ByteArrayInputStream(previewFileBytes);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();

                SWFDocument prevswf = MediaImporter.importSound(bin, 0);
                SWFWriter writer = new SWFWriter(prevswf, bout);
                writer.write();
                previewFileBytes = bout.toByteArray();
            } else {
                throw new FileNotFoundException("Unable to generated preview file ");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception generalException) {
            System.out.println(generalException.getMessage());
        }
        return previewFileBytes;
    }

    private String getPluginLocation()
            throws Exception {
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            return appConfig.valueOf("RINGTONETOOLS");
        } catch (NamingException e) {
            throw new Exception(e.getMessage());
        }
    }

    private String getOTBConvertorLocation()
            throws Exception {
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            return appConfig.valueOf("OTA_BITMAP_TOOL");
        } catch (NamingException e) {
            throw new Exception(e.getMessage());
        }
    }

    private String getMIDIConvertorLocation()
            throws Exception {
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            return appConfig.valueOf("MIDI_CONV_TOOL");
        } catch (NamingException e) {
            throw new Exception(e.getMessage());
        }
    }

    private String getWaterMarkLocation()
            throws Exception {
        String watermarkfilepath = null;
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");

            watermarkfilepath = appConfig.valueOf("WATERMARK_PATH");
            if ((watermarkfilepath != null) && (new File(watermarkfilepath).exists())) {
                System.out.println("Found watermark path as: " + watermarkfilepath);
                return watermarkfilepath;
            }
            System.out.println("Could not find watermark path. Currently set as: " + watermarkfilepath);
            throw new FileNotFoundException("Unable to find work directoru or work directory not set for content adapter");
        } catch (NamingException e) {
            throw new Exception(e.getMessage());
        }
    }

    private byte[] loadWaterMark()
            throws FileNotFoundException, Exception {
        byte[] watermark = null;

        InputStream in = null;
        ByteArrayOutputStream out = null;
        if (this.watermarkPath == null) {
            this.watermarkPath = getWaterMarkLocation();
        }
        try {
            in = new FileInputStream(this.watermarkPath);
            out = new ByteArrayOutputStream();


            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            watermark = out.toByteArray();
            in.close();
            out.close();
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Unable to locate watermark file or watermark file not set in properties file");
        } catch (IOException ex) {
        }
        return watermark;
    }

    private String getWorkDirectory()
            throws Exception {
        try {
            Context ctx = new InitialContext();

            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            String workDir = appConfig.valueOf("WORKDIR");
            if ((workDir != null) && (new File(workDir).exists())) {
                return workDir;
            }
            throw new FileNotFoundException("Unable to find work directoru or work directory not set for content adapter");
        } catch (NamingException e) {
            throw new Exception(e.getMessage());
        }
    }

    private String getMP3ConvName()
            throws Exception {
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            return appConfig.valueOf("MP3_CONV_NAME");
        } catch (NamingException e) {
            throw new Exception(e.getMessage());
        }
    }

    private String getMP3ConvPath()
            throws Exception {
        try {
            Context ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            Config appConfig = (Config) envCtx.lookup("bean/rmcsConfig");
            return appConfig.valueOf("MP3_CONV_PATH");
        } catch (NamingException e) {
            throw new Exception(e.getMessage());
        }
    }

    public static byte[] getBytesFromFile(File file)
            throws IOException {
        InputStream is = new FileInputStream(file);


        long length = file.length();
        if (length > 2147483647L) {
        }
        byte[] bytes = new byte[(int) length];


        int offset = 0;
        int numRead = 0;
        while ((offset < bytes.length) && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

    public static File writeBytesToFile(byte[] src, String dst)
            throws IOException {
        InputStream in = new ByteArrayInputStream(src);
        OutputStream out = new FileOutputStream(dst);


        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        return new File(dst);
    }

    public byte[] generateOTB(byte[] imageBytes, String imageFileName)
            throws IOException, Exception, FileNotFoundException {
        String path = this.workDirectory;

        File workDirectory = new File(path);
        if (((!workDirectory.exists()) || (!workDirectory.isDirectory())) &&
                (!workDirectory.mkdir())) {
            throw new Exception("Unable to find work directory. Please check work directory settings in properties file");
        }
        File otbWorkDirectory = new File(workDirectory.getAbsolutePath() + File.separator + "otb");
        if (((!otbWorkDirectory.exists()) || (!otbWorkDirectory.isDirectory())) &&
                (!otbWorkDirectory.mkdirs())) {
            throw new Exception("Unable to create work directory for OTB files. Check security permissions");
        }
        String absPath = otbWorkDirectory.getAbsolutePath() + File.separator + imageFileName;
        File imageFile = writeBytesToFile(imageBytes, absPath);
        String otbFileName = ExtManager.changeFileExtension(absPath, "otb");

        byte[] otbFile = null;
        File otbImageFile = new File(otbFileName);
        if (otbImageFile.exists()) {
            otbFile = getBytesFromFile(otbImageFile);
        } else {
            path = imageFile.getAbsolutePath();
            String ext = ExtManager.getFileExtension(imageFileName);
            try {
                String _otbtools = getOTBConvertorLocation();


                String[] commands = {_otbtools, path, otbFileName};

                ExecHelper child = ExecHelper.exec(commands);
                if (0 == child.getStatus()) {
                    otbImageFile = new File(otbFileName);
                    boolean exists = otbImageFile.exists();
                    if (exists) {
                        otbFile = getBytesFromFile(otbImageFile);


                        imageFile.delete();
                    } else {
                        throw new FileNotFoundException("Unable to generated OTB file ");
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new Exception("Could not create otb file");
            } catch (Exception generalException) {
                System.out.println(generalException.getMessage());
                throw new Exception("Could not create otb file");
            }
        }
        return otbFile;
    }

    public void testWAV()
            throws IOException, Exception, FileNotFoundException {
        try {
            String _ringtonetools = "C:\\rmcsresources\\contentadaptation\\soundtools\\timidity\\timidity-con.exe";


            String[] commands = {_ringtonetools, "-Ow", "-s 44100", "-o", "c:\\test1.wav", "c:\\greatest_love_of_all.mid"};

            ExecHelper child = ExecHelper.exec(commands);
            if (0 == child.getStatus()) {
                File prevFile = new File("c:\\test1.wav");
                boolean exists = prevFile.exists();
                if (!exists) {
                    throw new FileNotFoundException("Unable to generated preview file ");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception generalException) {
            System.out.println(generalException.getMessage());
        }
    }

    public void testAU()
            throws IOException, Exception, FileNotFoundException {
        try {
            String _ringtonetools = this.midiconvlocation;


            String[] commands = {_ringtonetools, "-Ou", "-o", "c:\\test1.au", "c:\\greatest_love_of_all.mid"};

            ExecHelper child = ExecHelper.exec(commands);
            if (0 == child.getStatus()) {
                File prevFile = new File("c:\\test1.au");
                boolean exists = prevFile.exists();
                if (!exists) {
                    throw new FileNotFoundException("Unable to generated preview file ");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception generalException) {
            System.out.println(generalException.getMessage());
        }
    }
}