package com.rancard.mobility.contentserver;

import java.sql.*;
import com.rancard.common.DConnect;
import com.rancard.util.DataImportListener;
import org.apache.commons.fileupload.FileItem;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class uploadsDB {
    
//constructor
    public uploadsDB () {}
    
//insert record
    public void createuploads (java.lang.String id, java.lang.String filename,
            java.io.File binaryfile, java.lang.String list_id) throws
            Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL =
                    "insert into uploads(id,filename,binaryfile,list_id) values(?,?,?,?)";
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, id);
            
            prepstat.setString (2, filename);
            
            prepstat.setString (4, list_id);
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public void updateuploads (java.lang.String id, java.lang.String filename,
            java.io.File binaryfile, java.lang.String list_id) throws
            Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL =
                    "update uploads set filename=?,binaryfile=?,list_id=? and id=?";
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, filename);
            
            prepstat.setString (3, list_id);
            
            prepstat.setString (4, id);
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public void updateuploadsWithPreview (java.lang.String id,
            java.lang.String filename,
            java.io.File previewfile,
            java.lang.String list_id) throws
            Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL =
                    "update uploads set filename=?,previewfile=?,list_id=? and id=?";
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, filename);
            
            prepstat.setString (3, list_id);
            
            prepstat.setString (4, id);
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public void deleteuploads (java.lang.String list_id, java.lang.String id) throws
            Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            SQL = "delete from uploads where list_id=? and id=?";
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, list_id);
            
            prepstat.setString (2, id);
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        }
        if (con != null) {
            con.close ();
        }
    }
    
    public com.rancard.mobility.contentserver.uploadsBean viewuploads (java.lang.String list_id,
            java.lang.String id) throws Exception {
        com.rancard.mobility.contentserver.uploadsBean uploads = new com.rancard.mobility.contentserver.uploadsBean ();
        java.io.InputStream filein;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from uploads,content_list,format_list where" +
                    " uploads.list_id=? and uploads.id=? and content_list.id=uploads.id and " +
                    " content_list.list_id=uploads.list_id and format_list.format_id=content_list.formats";
            ;
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, list_id);
            prepstat.setString (2, id);
            rs = prepstat.executeQuery ();
            if (!rs.next ()) {
                throw new Exception ("File not found");
            }
            uploads.setid (id);
            uploads.setlist_id (list_id);
            uploads.setfilename (rs.getString ("uploads.filename"));
            java.sql.Blob b = rs.getBlob ("uploads.binaryfile");
            uploads.setDataStream (b.getBytes (1, new Long (b.length ()).intValue ()));
            java.sql.Blob p = rs.getBlob ("uploads.previewfile");
            if (p != null) {
                uploads.setPreviewStream (p.getBytes (1,
                        new Long (p.length ()).intValue ()));
            }
            uploads.setFormat (rs.getString ("format_list.file_ext"));
            uploads.setMimeType (rs.getString ("format_list.mime_type"));
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        } finally {
            try {
                if (rs != null) {
                    rs.close ();
                }
                if (prepstat != null) {
                    prepstat.close ();
                }
                if (con != null) {
                    con.close ();
                }
            } catch (SQLException sqlee) {}
        }
        
        return uploads;
    }
    
    public uploadsBean viewuploads (String list_id, String id, String keyword) throws Exception {
        
        if (keyword == null || "".equals(keyword.trim())) {
            return viewuploads(list_id, id);
        }
        
        uploadsBean uploads = new uploadsBean ();
        java.io.InputStream filein;
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            SQL = "select * from uploads,content_list,format_list where" +
                    " uploads.list_id=? and uploads.id=? and content_list.keyword=? and content_list.id=uploads.id and " +
                    " content_list.list_id=uploads.list_id and format_list.format_id=content_list.formats";
            prepstat = con.prepareStatement (SQL);
            
            prepstat.setString (1, list_id);
            prepstat.setString (2, id);
            prepstat.setString(3, keyword);
            rs = prepstat.executeQuery ();
            if (!rs.next ()) {
                throw new Exception ("File not found");
            }
            uploads.setid (id);
            uploads.setlist_id (list_id);
            uploads.setfilename (rs.getString ("uploads.filename"));
            java.sql.Blob b = rs.getBlob ("uploads.binaryfile");
            uploads.setDataStream (b.getBytes (1, new Long (b.length ()).intValue ()));
            java.sql.Blob p = rs.getBlob ("uploads.previewfile");
            if (p != null) {
                uploads.setPreviewStream (p.getBytes (1,
                        new Long (p.length ()).intValue ()));
            }
            uploads.setFormat (rs.getString ("format_list.file_ext"));
            uploads.setMimeType (rs.getString ("format_list.mime_type"));
            
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        } finally {
            try {
                if (rs != null) {
                    rs.close ();
                }
                if (prepstat != null) {
                    prepstat.close ();
                }
                if (con != null) {
                    con.close ();
                }
            } catch (SQLException sqlee) {}
        }
        
        return uploads;
    }
    
    //returns size of files in the Uploads table in bytes
    public static long totalDiskSpaceUsed (String list_id) throws Exception {
        long space = 0;
        
        String query = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection ();
            
            query = "select previewfile, binaryfile from uploads where list_id=?";
            
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, list_id);
            rs = prepstat.executeQuery ();
            
            while(rs.next ()){
                java.sql.Blob b = rs.getBlob ("binaryfile");
                if(b != null){
                    space += b.length ();
                }
                java.sql.Blob p = rs.getBlob ("previewfile");
                if (p != null) {
                    space += p.length ();
                }
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        } finally {
            try {
                if (rs != null) {
                    rs.close ();
                }
                if (prepstat != null) {
                    prepstat.close ();
                }
                if (con != null) {
                    con.close ();
                }
            } catch (SQLException sqlee) {}
        }
        
        return space;
    }
    
    public static void uploadPreview (DataImportListener listener, List fileItems, String listId) throws Exception {
        
        String query = null;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        Iterator itr = fileItems.iterator ();
        int i = 0;
        
        try {
            con = DConnect.getConnection ();            
            
            while (itr.hasNext ()) {
                FileItem fi = (FileItem) itr.next ();
                //Check if not form field so as to only handle the file inputs
                //else condition handles the submit button input
                if (!fi.isFormField ()) {
                    
                    System.out.println ("\nNAME: " + fi.getName ());
                    System.out.println ("SIZE: " + fi.getSize ());
                    String filename = fi.getName ();
                    String fileId = fi.getFieldName ();
                    if(filename!=null){
                        PreparedStatement pstmt = con.prepareStatement ("update uploads set previewfile =? where id= ? and list_id =?");
                        
                        InputStream is = fi.getInputStream (); ;
                        //pstmt.setString(1, filename);
                        pstmt.setBinaryStream (1, is, is.available ());
                        pstmt.setString (2,fileId);
                        pstmt.setString (3,listId);
                        pstmt.executeUpdate ();
                        listener.processRead (1);
                        
                        //SQL ="update content_list set preview_exists=1 where id =? and list_id =? ";
                        pstmt = con.prepareStatement ("update content_list set preview_exists=1 where id =? and list_id =? ");
                        
                        pstmt.setString (1, fileId);
                        pstmt.setString (2, listId);
                        pstmt.executeUpdate ();
                        
                    }
                } else {
                    System.out.println ("Field =" + fi.getFieldName ());
                }
                listener.done();
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close ();
            }
            throw new Exception (ex.getMessage ());
        } finally {
            try {
                if (rs != null) {
                    rs.close ();
                }
                if (prepstat != null) {
                    prepstat.close ();
                }
                if (con != null) {
                    con.close ();
                }
            } catch (SQLException sqlee) {}
        }
    }
    
    public static void main (String[] args){
        try{
            System.out.println (uploadsDB.totalDiskSpaceUsed ("kf007tg"));
        }catch(Exception e){
            System.out.println (e.getMessage ());
        }
    }
}
