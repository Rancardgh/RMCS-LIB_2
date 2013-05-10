package com.rancard.mobility.contentserver.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
/**
 * <p>Title: Rancard Content Server</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Rancard Solutions Ltd</p>
 *
 * @author Ehizogie Binitie
 * @version 1.0
 *
 *  contains 3rd Party Code from Ian Darwin please see lcredits below
 *  modified code to support reading and writing file to database
 */

/* Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002.
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: LICENSE,v 1.8 2004/02/09 03:33:38 ian Exp $
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java
 * language and environment is gratefully acknowledged.
 *
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.List;
import java.util.Iterator;
import java.sql.*;
import com.rancard.util.ExtManager;
import javax.naming.Context;
import javax.naming.NamingException;
import com.rancard.common.Config;
import javax.naming.InitialContext;

/**
 * zipManager -- print or unzip a JAR or PKZIP file using java.util.zip. Command-line
 * version: extracts files.
 *
 * @author Ian Darwin, Ian@DarwinSys.com $Id: ArchiveManager.java,v 1.7 2004/03/07
 *         17:40:35 ian Exp $
 */
public class ArchiveManager {


    /** Constants for mode listing or mode extracting. */
    public static final int LIST = 0, EXTRACT = 1;

    /** Whether we are extracting or just printing TOC */
    protected int mode = LIST;

    /** The ZipFile that is used to read an archive */
    protected ZipFile zippy;
    /** Tmpfolder for image manipulatoins using external tools   */
    protected String tmpFolder;

    /** The Zip input stream that is used to read an archive */
    protected ZipInputStream zipStream;
    /** The buffer for reading/writing the ZipFile data */
    protected byte[] b;

    protected boolean warnedMkDir = false;

    public void unZipToContentRepository(InputStream in, String fileName,
                                         String contentProviderId) {
        ArchiveManager u = new ArchiveManager();
        u.setMode(EXTRACT);
        String candidate = fileName;
        // System.err.println("Trying path " + candidate);


        try {
            java.sql.Connection conn = com.rancard.common.DConnect.
                                       getConnection();
            if (candidate.endsWith(".zip") || candidate.endsWith(".jar")) {
                u.unZip(in, conn, contentProviderId);
            } else {
                System.err.println("Not a zip file? " + candidate);
            }
            System.err.println("All done!");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public void unZipToContentRepository(InputStream in, String fileName,
                                         String contentProviderId, Integer type) {
        ArchiveManager u = new ArchiveManager();
        u.setMode(EXTRACT);
        String candidate = fileName;
        // System.err.println("Trying path " + candidate);

        java.sql.Connection conn = null;
        try {
            conn = com.rancard.common.DConnect.getConnection();
            if (candidate.endsWith(".zip") || candidate.endsWith(".jar")) {
                u.unZip(in, conn, contentProviderId, type);
            } else {
                System.err.println("Not a zip file? " + candidate);
            }
            System.err.println("All done!");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (conn != null) {
                try{
                    conn.close();
                }catch (Exception e) {
                    conn = null;
                }
            }
        } finally {
            //close DB connection
            if (conn != null) {
                try{
                    conn.close();
                }catch (Exception e) {
                    conn = null;
                }
            }
        }
    }
    
    public void unZipToContentRepository(InputStream in, String fileName,
                                         String contentProviderId, Integer type, String supplierId) {
        ArchiveManager u = new ArchiveManager();
        u.setMode(EXTRACT);
        String candidate = fileName;
        // System.err.println("Trying path " + candidate);

        java.sql.Connection conn = null;
        try {
            conn = com.rancard.common.DConnect.getConnection();
            if (candidate.endsWith(".zip") || candidate.endsWith(".jar")) {
                u.unZip(in, conn, contentProviderId, type, supplierId);
            } else {
                System.err.println("Not a zip file? " + candidate);
            }
            System.err.println("All done!");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (conn != null) {
                try{
                    conn.close();
                }catch (Exception e) {
                    conn = null;
                }
            }
        } finally {
            //close DB connection
            if (conn != null) {
                try{
                    conn.close();
                }catch (Exception e) {
                    conn = null;
                }
            }
        }
    }

    public void unZipToContentRepository(InputStream in, String fileName,
                                         String contentProviderId, Integer type, String supplierId, String keyword) throws Exception {
        ArchiveManager u = new ArchiveManager();
        u.setMode(EXTRACT);
        String candidate = fileName;
        // System.err.println("Trying path " + candidate);
        java.sql.Connection conn = null;
        try {
            conn = com.rancard.common.DConnect.getConnection();
            if (candidate.endsWith(".zip") || candidate.endsWith(".jar")) {
                u.unZip(in, conn, contentProviderId, type, supplierId, keyword);
                System.err.println("All done!");
            } else {
                System.err.println("Not a zip file? " + candidate);
                throw new Exception("Invalid file type");
            }            
        } catch (Exception ex) {
            throw ex;
        } finally {
            //close DB connection
            if (conn != null) {
                try{
                    conn.close();
                }catch (Exception e) {
                    conn = null;
                }
            }
        }
    }    

    /** Construct an ArchiveManager object. Just allocate the buffer */
    public ArchiveManager() {
        b = new byte[8092];
    }

    /** Construct an ArchiveManager object. Just allocate the buffer . Specify tmp folder for manipulation*/
    public ArchiveManager(String tmpFolder) {
        b = new byte[8092];
        this.tmpFolder = tmpFolder;
    }

    /** Set the Mode (list, extract). */
    protected void setMode(int m) {
        if (m == LIST || m == EXTRACT) {
            mode = m;
        }
    }

    /** Cache of paths we've mkdir()ed. */
    protected SortedSet dirsMade;


    public void unZip(InputStream in) {
        dirsMade = new TreeSet();
        try {

            zipStream = new ZipInputStream(in);
            // Enumeration all = zippy.entries();
            // get connection to temporary table
            // extract each file
            // determine file properties
            // write to table
            // update permanent table.
            while (0 != zipStream.available()) {
                getFile(zipStream.getNextEntry());
                zipStream.closeEntry();
                // add to batch
            }
            zipStream.close();

        } catch (IOException err) {
            System.err.println("IO Error: " + err);
            return;
        } catch (SQLException ex) {
            //System.out.println("SQL Error "+ex.getMessage());
        }
    }

    public String createTempTable(java.sql.Connection conn) throws Exception {
        String tablename = new com.rancard.common.uidGen().getUId();

        String SQL = "CREATE TEMPORARY TABLE UPLOADS (" +
                     "UPLOADID INT NOT NULL, " +
                     "FILENAME VARCHAR(255), " +
                     "BINARYFILE LONGBLOB, " +
                     "PRIMARY KEY (UPLOADID) " +
                     "); ";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(SQL);
        } catch (Exception ex) {
            if (conn != null) {
                conn.close();
            }
            //todo: create logger here
            tablename = null;
            //     throw new Exception(ex.getMessage());
        }
        if (conn != null) {
            conn.close();
        }

        return tablename;
    }

    protected void unZip(InputStream in, java.sql.Connection conn,
                         String contenProviderId) throws
            Exception {
        dirsMade = new TreeSet();
        try {
            zipStream = new ZipInputStream(in);
            // Enumeration all = zippy.entries();
            String tablename = contenProviderId;
            // create temp table

            // get connection to temporary table
            // extract each file
            //while (0 != zipStream.available()) {
            ZipEntry z;

            while ((z = zipStream.getNextEntry()) != null) {
                byte[] buf = new byte[1024];
                int len;
                java.io.ByteArrayOutputStream bos = new java.io.
                        ByteArrayOutputStream();
                while ((len = zipStream.read(buf)) > 0) {
                    bos.write(buf, 0, len);
                }
                byte[] unzippedFile = bos.toByteArray();
                bos.close();
                // write validation class to ensure that only valid file types are written
                write(z, unzippedFile, conn, tablename);
                //zipStream.closeEntry();
                // add to batch

            }
            //}
            zipStream.close();

        } catch (IOException err) {
            System.err.println("IO Error: " + err);
            return;
        } catch (SQLException ex) {
            //System.out.println("SQL Error "+ex.getMessage());
        }
    }


    protected void unZip(InputStream in, java.sql.Connection conn,
                         String contenProviderId, Integer type) throws
            Exception {
        dirsMade = new TreeSet();
        try {
            zipStream = new ZipInputStream(in);
            // Enumeration all = zippy.entries();
            String tablename = contenProviderId;
            // create temp table

            // get connection to temporary table
            // extract each file
            //while (0 != zipStream.available()) {
            ZipEntry z;

            while ((z = zipStream.getNextEntry()) != null) {
                byte[] buf = new byte[1024];
                int len;
                java.io.ByteArrayOutputStream bos = new java.io.
                        ByteArrayOutputStream();
                while ((len = zipStream.read(buf)) > 0) {
                    bos.write(buf, 0, len);
                }
                byte[] unzippedFile = bos.toByteArray();
                bos.close();
                // write validation class to ensure that only valid file types are written

                write(z, unzippedFile, conn, tablename, type.intValue ());
                //zipStream.closeEntry();
                // add to batch

            }
            //}
            zipStream.close();

        } catch (IOException err) {
            System.err.println("IO Error: " + err);
            return;
        } catch (SQLException ex) {
            //System.out.println("SQL Error "+ex.getMessage());
        }
    }
    
    protected void unZip(InputStream in, java.sql.Connection conn,
                         String contenProviderId, Integer type, String supplierId) throws
            Exception {
        dirsMade = new TreeSet();
        try {
            zipStream = new ZipInputStream(in);
            // Enumeration all = zippy.entries();
            String tablename = contenProviderId;
            // create temp table

            // get connection to temporary table
            // extract each file
            //while (0 != zipStream.available()) {
            ZipEntry z;

            while ((z = zipStream.getNextEntry()) != null) {
                byte[] buf = new byte[1024];
                int len;
                java.io.ByteArrayOutputStream bos = new java.io.
                        ByteArrayOutputStream();
                while ((len = zipStream.read(buf)) > 0) {
                    bos.write(buf, 0, len);
                }
                byte[] unzippedFile = bos.toByteArray();
                bos.close();
                // write validation class to ensure that only valid file types are written

                write(z, unzippedFile, conn, tablename, type.intValue (), supplierId);
                //zipStream.closeEntry();
                // add to batch

            }
            //}
            zipStream.close();

        } catch (IOException err) {
            System.err.println("IO Error: " + err);
            throw new Exception("Unzipping content failed");
        } catch (SQLException ex) {
            //System.out.println("SQL Error "+ex.getMessage());
        }
    }

    protected void unZip(InputStream in, java.sql.Connection conn,
                     String contenProviderId, Integer type, String supplierId, String keyword) throws
            Exception {
        dirsMade = new TreeSet();
        zipStream = new ZipInputStream(in);
        // Enumeration all = zippy.entries();
        String tablename = contenProviderId;
        // create temp table
        // get connection to temporary table
        // extract each file
        //while (0 != zipStream.available()) {
        ZipEntry z;

        while ((z = zipStream.getNextEntry()) != null) {
            byte[] buf = new byte[1024];
            int len;
            java.io.ByteArrayOutputStream bos = new java.io.
                    ByteArrayOutputStream();
            while ((len = zipStream.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            byte[] unzippedFile = bos.toByteArray();
            bos.close();
            // write validation class to ensure that only valid file types are written

            write(z, unzippedFile, conn, tablename, type.intValue (), supplierId, keyword);
            //zipStream.closeEntry();
            // add to batch
        }
        zipStream.close();
    }

    /**
     * Process one file from the zip, given its name. Either print the name, or
     * create the file on disk.
     */
    protected void getFile(ZipEntry e) throws IOException,
            java.sql.SQLException {
        String zipName = e.getName();
        switch (mode) {
        case EXTRACT:
            if (zipName.startsWith("/")) {
                if (!warnedMkDir) {
                    System.out.println("Ignoring absolute paths");
                }
                warnedMkDir = true;
                zipName = zipName.substring(1);
            }

            // if a directory, just return. We mkdir for every file,
            // since some widely-used Zip creators don't put out
            // any directory entries, or put them in the wrong place.
            if (zipName.endsWith("/")) {
                return;
            }

            // Else must be a file; open the file for output
            // Get the directory part.
            int ix = zipName.lastIndexOf('/');
            if (ix > 0) {
                String dirName = zipName.substring(0, ix);
                if (!dirsMade.contains(dirName)) {
                    File d = new File(dirName);
                    // If it already exists as a dir, don't do anything
                    if (!(d.exists() && d.isDirectory())) {
                        // Try to create the directory, warn if it fails
                        System.out.println("Creating Directory: " + dirName);
                        if (!d.mkdirs()) {
                            System.err.println("Warning: unable to mkdir "
                                               + dirName);
                        }
                        dirsMade.add(dirName);
                    }
                }
            }
            System.err.println("Creating " + zipName);

            //FileOutputStream os = new FileOutputStream(zipName);
            String filename = e.getName();

            // size
            long size = e.getSize();

            // extenstion
            String extension = null;
            int x = filename.lastIndexOf(".");
            if (x != 0) {
                extension = filename.substring(x + 1, filename.length());
            }

            // type
            int type = new com.rancard.mobility.contentserver.ContentType().
                       getTypeCode(extension);

            InputStream is = zippy.getInputStream(e);
            java.io.ByteArrayOutputStream bos = new java.io.
                                                ByteArrayOutputStream();

            byte[] buff = new byte[32768];
            int bytesRead;

            // determine type
            // Simple read/write loop.
            while ( -1 != (bytesRead = is.read(buff, 0, 32768))) {

                bos.write(buff, 0, bytesRead);
            }

            byte[] zipedfile = bos.toByteArray();

            is.close();

            // hhighly inefficient all files can be written in a single connection
            try {
                Connection conn = com.rancard.common.DConnect.getConnection(); //  DriverManager.getConnection("jdbc:mysql://localhost:3306/rcs","admin", "admin");
                //check if extenasion is valid
                int executeStatus;
                String ID = new com.rancard.common.uidGen().getUId();
                String listId = "001";
                //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/rcs", "admin", "admin");
                PreparedStatement pstmt = conn.prepareStatement(
                        "insert into uploads	(id, filename,binaryfile,list_id) 	values 	(?, ?,?, ? ,?)");
                pstmt.setString(1, ID);
                executeStatus = pstmt.EXECUTE_FAILED;
                //File uploadedFile = new File("uploadedfile.dat");
                //InputStream is = fi.getInputStream(); ;
                pstmt.setString(2, zipName);

                pstmt.setBytes(3, zipedfile);
                //  pstmt.setBinaryStream(4, is, is.available());
                pstmt.setString(4, listId);
                executeStatus = pstmt.executeUpdate();
                // if the status is not failed
                if (executeStatus != pstmt.EXECUTE_FAILED) {
                    // update the content list table with a refference
                    //pstmt = conn.prepareStatement("insert into content_list	(id, filename,binaryfile,list_id) 	values 	(?, ?, ? ,?)");
                    String qry = "insert into content_list (id, content_id, title,size,  price, list_id, date_added  )" +
                                 " values " +
                                 "(?, ?, ?, ?, ?,?, ?)";
                    pstmt = conn.prepareStatement(qry);
                    pstmt.setString(1, ID);
                    pstmt.setString(2, new com.rancard.common.uidGen().getUId());
                    pstmt.setString(3, zipName);
                    pstmt.setInt(4, 3000);
                    pstmt.setString(5, "1500");
                    pstmt.setString(6, listId);
                    pstmt.setDate(7,
                                  new java.sql.Date(new java.util.Date().
                            getTime()));
                    pstmt.execute();

                }
            } catch (Exception ex) {

            }

            is.close();

            //os.close();
            break;
        case LIST:

            // Not extracting, just list
            if (e.isDirectory()) {
                System.out.println("Directory " + zipName);
            } else {
                System.out.println("File " + zipName);
            }
            break;
        default:
            throw new IllegalStateException("mode value (" + mode + ") bad");
        }
    }


    /**
     * Process one file from the zip, given its name. Unzip the file to temporary table and extract pre-processing infomration.
     */
    protected void write(ZipEntry e, byte[] zipedfile, java.sql.Connection conn,
                         String tableName) throws
            IOException, java.sql.SQLException, Exception {
        String zipName = e.getName();

        if (zipName.startsWith("/")) {
            if (!warnedMkDir) {
                System.out.println("Ignoring absolute paths");
            }
            warnedMkDir = true;
            zipName = zipName.substring(1);
        }

        // if a directory, just return. We mkdir for every file,
        // since some widely-used Zip creators don't put out
        // any directory entries, or put them in the wrong place.
        if (zipName.endsWith("/")) {
            return;
        }
        // extract information
        System.out.println("Creating " + zipName);
        // file name
        String filename = e.getName();
        filename = zipName.substring(zipName.lastIndexOf("/") + 1, zipName.length());

        // // strip away folders as an optional feature
        // size
        long size = e.getSize();
        // extenstion
        String extension = null;
        int x = filename.lastIndexOf(".");
        if (x != 0) {
            extension = filename.substring(x + 1, filename.length());
        }
        // type
        int type = ExtManager.getTypeForFormat(extension);
        String formatid = ExtManager.getFormatFor(extension);
        // time
        long time = e.getTime();
        // preview file
        // slightly better performance use single datbase connection all files can be written in a single connection
        if (type != 0) {
            try {
                //check if extenasion is valid
                int executeStatus;
                String ID = new com.rancard.common.uidGen().getUId();
                String listId = tableName;
                //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/rcs", "admin", "admin");
                PreparedStatement pstmt = conn.prepareStatement(
                        "insert into uploads  (id, filename,binaryfile,list_id) 	values 	(?, ?, ? ,?)");
                pstmt.setString(1, ID);
                executeStatus = pstmt.EXECUTE_FAILED;
                pstmt.setString(2, filename);
                pstmt.setBytes(3, zipedfile);
                pstmt.setString(4, listId);
                executeStatus = pstmt.executeUpdate();
                // if the status is not failed
                if (executeStatus != pstmt.EXECUTE_FAILED) {
                    // update the content list table with a refference
                    String qry = "insert into content_list (id, content_id, title,size,  price, list_id, date_added ,content_type,formats,isLocal )" +
                                 " values " +
                                 "(?, ?, ?, ?, ?,?, ?,?,?,?)";
                    pstmt = conn.prepareStatement(qry);
                    pstmt.setString(1, ID);
                    pstmt.setString(2, new com.rancard.common.uidGen().getUId());
                    pstmt.setString(3, zipName);
                    pstmt.setLong(4, size);
                    pstmt.setString(5, "0");
                    pstmt.setString(6, listId);
                    pstmt.setTimestamp(7, new java.sql.Timestamp(time));
                    pstmt.setInt(8, type);
                    pstmt.setString(9, formatid);
                    pstmt.setInt(10, 1);
                    pstmt.execute();

                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Error importing ringtone into database");
            }
        }
        //os.close();
    }


    /**
     * Process one file from the zip, given its name. Unzip the file to temporary table and extract pre-processing infomration.
     */
    protected void write(ZipEntry e, byte[] zipedfile, java.sql.Connection conn,
                         String tableName, int type) throws
            IOException, java.sql.SQLException, Exception {
        String zipName = e.getName();

        if (zipName.startsWith("/")) {
            if (!warnedMkDir) {
                System.out.println("Ignoring absolute paths");
            }
            warnedMkDir = true;
            zipName = zipName.substring(1);
        }

        // if a directory, just return. We mkdir for every file,
        // since some widely-used Zip creators don't put out
        // any directory entries, or put them in the wrong place.
        if (zipName.endsWith("/")) {
            return;
        }
        // extract information
        System.out.println("Creating " + zipName);
        // file name
        String filename = e.getName();
        filename = zipName.substring(zipName.lastIndexOf("/") + 1, zipName.length());

        // // strip away folders as an optional feature
        // size
        long size = e.getSize();
        // extenstion
        String extension = null;
        int x = filename.lastIndexOf(".");
        if (x != 0) {
            extension = filename.substring(x + 1, filename.length());
        }
        String typeId = "" + type;
        String formatid = ExtManager.getFormatFor(extension);
        if(!ExtManager.match (typeId, formatid)){
            type = ExtManager.getTypeForFormat(extension);
        }
        // time
        long time = e.getTime();
        // preview file
        // slightly better performance use single datbase connection all files can be written in a single connection
        if (type != 0) {
            try {
                //check if extenasion is valid
                int executeStatus;
                String ID = new com.rancard.common.uidGen().generateNumberID (10);
                String listId = tableName;
                //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/rcs", "admin", "admin");
                PreparedStatement pstmt = conn.prepareStatement(
                        "insert into uploads  (id, filename,binaryfile,list_id) 	values 	(?, ?, ? ,?)");
                pstmt.setString(1, ID);
                executeStatus = pstmt.EXECUTE_FAILED;
                pstmt.setString(2, filename);
                pstmt.setBytes(3, zipedfile);
                pstmt.setString(4, listId);
                executeStatus = pstmt.executeUpdate();
                // if the status is not failed
                if (executeStatus != pstmt.EXECUTE_FAILED) {
                    // update the content list table with a refference
                    String qry = "insert into content_list (id, content_id, title,size,  price, list_id, date_added ,content_type,formats,isLocal )" +
                                 " values (?, ?, ?, ?, ?,?, ?,?,?,?)";
                    pstmt = conn.prepareStatement(qry);
                    pstmt.setString(1, ID);
                    pstmt.setString(2, new com.rancard.common.uidGen().getUId());
                    pstmt.setString(3, zipName);
                    pstmt.setLong(4, size);
                    pstmt.setString(5, "0");
                    pstmt.setString(6, listId);
                    pstmt.setTimestamp(7, new java.sql.Timestamp(time));
                    pstmt.setInt(8, type);
                    pstmt.setString(9, formatid);
                    pstmt.setInt(10, 1);
                    pstmt.execute();

                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Error importing ringtone into database");
            }
        }
        //os.close();
    }   
    
    protected void write(ZipEntry e, byte[] zipedfile, java.sql.Connection conn,
                         String tableName, int type, String supplierId) throws
            IOException, java.sql.SQLException, Exception {
        String zipName = e.getName();

        if (zipName.startsWith("/")) {
            if (!warnedMkDir) {
                System.out.println("Ignoring absolute paths");
            }
            warnedMkDir = true;
            zipName = zipName.substring(1);
        }

        // if a directory, just return. We mkdir for every file,
        // since some widely-used Zip creators don't put out
        // any directory entries, or put them in the wrong place.
        if (zipName.endsWith("/")) {
            return;
        }
        // extract information
        System.out.println("Creating " + zipName);
        // file name
        String filename = e.getName();
        filename = zipName.substring(zipName.lastIndexOf("/") + 1, zipName.length());

        // // strip away folders as an optional feature
        // size
        long size = e.getSize();
        // extenstion
        String extension = null;
        int x = filename.lastIndexOf(".");
        if (x != 0) {
            extension = filename.substring(x + 1, filename.length());
        }
        String typeId = "" + type;
        String formatid = ExtManager.getFormatFor(extension);
        if(!ExtManager.match (typeId, formatid)){
            type = ExtManager.getTypeForFormat(extension);
        }
        // time
        long time = e.getTime();
        // preview file
        // slightly better performance use single datbase connection all files can be written in a single connection
        if (type != 0) {
            try {
                //check if extenasion is valid
                int executeStatus;
                String ID = new com.rancard.common.uidGen().generateNumberID (10);
                String listId = tableName;
                //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/rcs", "admin", "admin");
                PreparedStatement pstmt = conn.prepareStatement(
                        "insert into uploads  (id, filename,binaryfile,list_id) 	values 	(?, ?, ? ,?)");
                pstmt.setString(1, ID);
                executeStatus = pstmt.EXECUTE_FAILED;
                pstmt.setString(2, filename);
                pstmt.setBytes(3, zipedfile);
                pstmt.setString(4, listId);
                executeStatus = pstmt.executeUpdate();
                // if the status is not failed
                if (executeStatus != pstmt.EXECUTE_FAILED) {
                    // update the content list table with a refference
                    String qry = "insert into content_list (id, content_id, title,size,  price, list_id, date_added ,content_type,formats,isLocal,supplier_id )" +
                                 " values (?, ?, ?, ?, ?,?, ?,?,?,?,?)";
                    pstmt = conn.prepareStatement(qry);
                    pstmt.setString(1, ID);
                    pstmt.setString(2, new com.rancard.common.uidGen().getUId());
                    pstmt.setString(3, zipName);
                    pstmt.setLong(4, size);
                    pstmt.setString(5, "0");
                    pstmt.setString(6, listId);
                    pstmt.setTimestamp(7, new java.sql.Timestamp(time));
                    pstmt.setInt(8, type);
                    pstmt.setString(9, formatid);
                    pstmt.setInt(10, 1);
                    pstmt.setString(11, supplierId);
                    pstmt.execute();

                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Error importing ringtone into database");
            }
        }
        //os.close();
    }
    
    protected void write(ZipEntry e, byte[] zipedfile, java.sql.Connection conn,
                         String tableName, int type, String supplierId, String keyword) throws 
            IOException, java.sql.SQLException, Exception {
        String zipName = e.getName();

        if (zipName.startsWith("/")) {
            if (!warnedMkDir) {
                System.out.println("Ignoring absolute paths");
            }
            warnedMkDir = true;
            zipName = zipName.substring(1);
        }

        // if a directory, just return. We mkdir for every file,
        // since some widely-used Zip creators don't put out
        // any directory entries, or put them in the wrong place.
        if (zipName.endsWith("/")) {
            return;
        }
        // extract information
        System.out.println("Creating " + zipName);
        // file name
        String filename = e.getName();
        filename = zipName.substring(zipName.lastIndexOf("/") + 1, zipName.length());

        // // strip away folders as an optional feature
        // size
        long size = e.getSize();
        // extenstion
        String extension = null;
        int x = filename.lastIndexOf(".");
        if (x != 0) {
            extension = filename.substring(x + 1, filename.length());
        }
        String typeId = "" + type;
        String formatid = ExtManager.getFormatFor(extension);
        if(!ExtManager.match (typeId, formatid)){
            type = ExtManager.getTypeForFormat(extension);
        }
        // time
        long time = e.getTime();
        // preview file
        // slightly better performance use single datbase connection all files can be written in a single connection
        if (type != 0) {
            try {
                //check if extenasion is valid
                int executeStatus;
                String ID = new com.rancard.common.uidGen().generateNumberID (10);
                String listId = tableName;
                
                PreparedStatement pstmt = null;
                // update the content list table with a refference
                String qry = "insert into content_list (id, content_id, title,size,  price, list_id, date_added ,content_type,formats,isLocal,supplier_id, keyword)" +
                             " values (?, ?, ?, ?, ?,?, ?,?,?,?,?,?)";
                pstmt = conn.prepareStatement(qry);
                pstmt.setString(1, ID);
                pstmt.setString(2, new com.rancard.common.uidGen().getUId());
                pstmt.setString(3, zipName);
                pstmt.setLong(4, size);
                pstmt.setString(5, "0");
                pstmt.setString(6, listId);
                pstmt.setTimestamp(7, new java.sql.Timestamp(time));
                pstmt.setInt(8, type);
                pstmt.setString(9, formatid);
                pstmt.setInt(10, 1);
                pstmt.setString(11, supplierId);
                pstmt.setString(12, keyword);
                executeStatus = pstmt.executeUpdate();
                // if the status is not failed
                if (executeStatus != PreparedStatement.EXECUTE_FAILED) {
                    pstmt = conn.prepareStatement(
                        "insert into uploads  (id, filename,binaryfile,list_id) 	values 	(?, ?, ? ,?)");
                    pstmt.setString(1, ID);
                    pstmt.setString(2, filename);
                    pstmt.setBytes(3, zipedfile);
                    pstmt.setString(4, listId);
                } else {
                    // remove content list
                    pstmt = conn.prepareStatement(String.format("delete from content_list where id='%s' and keyword='%s'", ID, keyword));
                    pstmt.executeUpdate();
                }
            } catch (Exception ex) {
                System.out.println("Error importing ringtone into database");
                throw ex;
            }
        } 
    }    

    private String getTempDirectory() throws Exception {
        /*  try {
              Context ctx = new InitialContext();
              Config appConfig = (Config) ctx.lookup("config/Application");
              return appConfig.valueOf("TMPDIR");
          } catch (NamingException e) {
              //   Log.write(e);
              throw new Exception(e.getMessage());
          }*/
        return
                "S:\\developer\\work\\";
    }


}
