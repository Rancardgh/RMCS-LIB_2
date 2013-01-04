package com.rancard.mobility.contentserver;

import java.sql.*;
import java.util.*;
import com.rancard.common.DConnect;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public abstract class DownloadLogDB {
    public DownloadLogDB() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     insert record
    */
    public static void createLogItem(String ticketID, String id, String listID,
                              String subscriberMSISDN, int phoneId, java.util.Date date,
                              boolean downloadCompleted, String pin) throws Exception {

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try{
            con = DConnect.getConnection();
            query = "INSERT into download_log(ticketId,id,list_id,subscriberMSISDN,phone_id," +
                    "date_of_download,status,pin) values(?,?,?,?,?,?,?,?)";

            prepstat = con.prepareStatement(query);
            prepstat.setString(1,ticketID);
            prepstat.setString(2,id);
            prepstat.setString(3,listID);
            prepstat.setString(4,subscriberMSISDN);
            prepstat.setInt(5,phoneId);
            prepstat.setDate(6,new java.sql.Date(date.getTime()));
            if(downloadCompleted)
                prepstat.setInt(7,1);
            else
                prepstat.setInt(7,0);
            prepstat.setString(8,pin);

            prepstat.execute();
        }catch (Exception ex) {
            if(con != null)
                con.close();
            throw new Exception(ex.getMessage());
        }
        if(con != null)
            con.close();
    }

    /*
    Update log entry - usually to set time and status of download
    */
    public static void updateLogItem(String ticketID, java.util.Date date,
                              boolean downloadCompleted) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try{
            int status = downloadCompleted ? 1 : 0;
            con = DConnect.getConnection();
            query = "UPDATE download_log SET date_of_download=?,status=? WHERE" +
                   " ticketId='" + ticketID+"'";
            prepstat=con.prepareStatement(query);
            prepstat.setDate(1,new java.sql.Date(date.getTime()));
            prepstat.setInt(2,status);

            prepstat.execute();
        }
        catch (Exception ex){
           if(con != null)
               con.close();
           throw new Exception(ex.getMessage());
        }
        if(con != null)
            con.close();
    }

    /*
    Deletes a lod entry
    */
    public static void deleteLogItem(String ticketID) throws Exception {
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "DELETE from download_log WHERE ticketId=?";
            prepstat=con.prepareStatement(query);
            prepstat.setString(1,ticketID);
            prepstat.execute();
        }catch (Exception ex){
            if(con != null)
                con.close();
            throw new Exception(ex.getMessage());
        }
        if(con != null)
            con.close();
    }

    /*
    Retrieves a log item as a bean
    */
    public static Transaction viewLogItem(String ticketID) throws Exception {
        Transaction newBean = new Transaction();
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        try {
            con = DConnect.getConnection();
            query = "SELECT * from download_log WHERE ticketId=?";
            prepstat=con.prepareStatement(query);
            prepstat.setString(1,ticketID);
            rs = prepstat.executeQuery();

            while (rs.next()){
                newBean.setTicketID(rs.getString("ticketid"));
                newBean.setID(rs.getString("id"));
                newBean.setListID(rs.getString("list_id"));
                newBean.setSubscriberMSISDN(rs.getString("subscriberMSISDN"));
                newBean.setPhoneId(rs.getString("phone_id"));
                //newBean.setDate((java.util.Date)rs.getDate("date_of_download"));
                if(rs.getInt("status") == 1)
                    newBean.setDownloadCompleted(true);
                else
                    newBean.setDownloadCompleted(false);
            }
        }catch (Exception ex){
           if(con != null)
               con.close();
           throw new Exception(ex.getMessage());
       }
       if(con != null)
          con.close();
       return newBean;
   }
   /*
   Retrrieves information on download numbers of content items
   */
   public static java.util.ArrayList getMostDownloaded(int typeId) throws Exception {
       java.util.ArrayList itemList;
       ContentItem newBean = new ContentItem();
       String query;
       ResultSet rs = null;
       Connection con = null;
       PreparedStatement prepstat = null;

       try {
           con = DConnect.getConnection();
           query = "SELECT id, list_id from download_log";
           prepstat = con.prepareStatement(query);
           rs = prepstat.executeQuery();

           int fetchSize = 0;
           while (rs.next()){
               fetchSize++;
           }
           rs.beforeFirst();
           itemList = new ArrayList(0);
           itemList.add(0, "");
           int[] itemCount = new int[fetchSize];
           for(int i = 0; i < itemCount.length; i++) {
               itemCount[i] = 0;
           }

           while (rs.next()){
               newBean = newBean.viewContentItem(rs.getString("id"), rs.getString("list_id"));
               for(int i = 0; i < fetchSize; i++) {
                   if((itemList.get(i).equals("")) || ((!((ContentItem)itemList.get(i)).isEqualTo(newBean)) && (i == fetchSize - 1))) {
                       itemList.add(i, newBean);
                       itemCount[itemList.indexOf(newBean)] += 1;
                       break;
                   }
                   if(((ContentItem)itemList.get(i)).isEqualTo(newBean)) {
                       itemCount[i] += 1;
                       break;
                   }
               }
           }
           itemList.remove("");
           java.util.ArrayList items = new java.util.ArrayList();
           double mean = 0.0;
           double sum = 0;
           for(int i = 0; i < itemList.size(); i++){
               if(((ContentItem)itemList.get(i)).gettype().intValue() == typeId){
                   StatStruct struct = new StatStruct((ContentItem)itemList.get(i), itemCount[i]);
                   items.add(struct);
                   sum += struct.count;
               }
           }
           if(sum != 0)
               mean = sum/items.size();
           else
               mean = 0.0;
           itemList = new ArrayList();
           for(int i = 0; i < items.size(); i++) {
               int downloadCounter = ((StatStruct)items.get(i)).count;
               if(downloadCounter >= mean) {
                   itemList.add(((StatStruct)items.get(i)).clb);
               }
           }
       } catch(Exception e) {
           throw new Exception();
       }
       return itemList;
   }

   /*
   holds, as a data structure, a content item and the number of times
   it has been downloaded
   */
   static class StatStruct {
       ContentItem clb;
       int count;

       public StatStruct(ContentItem clb_new, int count_new) {
           this.clb = clb_new;
           this.count = count_new;
       }
   }

   /*
   test
   */
   public static void main( String args[]) throws Exception {
       /*Transaction dlb = new  Transaction();
       Transaction dlb2 = new  Transaction();
       dlb.setTicketID("00101");
       dlb.setID("01");
       dlb.setListID("001");
       dlb.setSubscriberMSISDN("233276768443");
       dlb.setDownloadCompleted(false);

       dlb.logMe();

       dlb2.viewMyLog("00101");
       System.out.println("dlb2's Subscriber's MSISDN" + dlb2.getSubscriberMSISDN());

       dlb2.setDownloadCompleted(true);
       System.out.println("dlb2's status " + dlb2.getTicketID());
       dlb2.updateMyLog();

       dlb.viewMyLog("00101");
       System.out.println("dlb's" + dlb.downloadCompleted());

       dlb2.removeMyLog();*/

       //try update
       DownloadLogDB.updateLogItem("34h1ju88d0",new Transaction().now(),true);
   }

    private void jbInit() throws Exception {
    }


}
