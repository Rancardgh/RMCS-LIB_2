/*
 * LiveScoreTriggerScheduleDB.java
 *
 * Created on March 9, 2007, 6:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 *
 * @author Messenger
 */
public abstract class LiveScoreTriggerScheduleDB {
    
    public static boolean createTriggerSchedule (Timestamp triggerTime) throws Exception {
        
        String SQL;
        ResultSet rs=null;
        Connection con=null;
        PreparedStatement prepstat=null;
        boolean created = false;
        
        try {
            con=DConnect.getConnection ();
            
            SQL = "select * from livescore_trigger_schedule where trigger_time=?";
            prepstat=con.prepareStatement (SQL);
            prepstat.setTimestamp (1, triggerTime);
            rs=prepstat.executeQuery ();
            
            if(rs.next ()){
                if(rs.getInt ("sent") == 0){
                    created = true;
                }else{
                    created = false;
                }
            }else{
                SQL="insert into livescore_trigger_schedule (trigger_time,sent) values (?,?)";
                prepstat=con.prepareStatement (SQL);
                prepstat.setTimestamp (1, triggerTime);
                prepstat.setInt (2, 0);
                prepstat.execute ();
                
                created = true;
            }
        }catch (Exception ex){
            if(con !=null)
                con.close ();
            throw new Exception (ex.getMessage ());
        }finally{
            if(con !=null) {
                con.close ();
            }
        }
        return created;
    }
    
    public static void updateTriggerSchedule (Timestamp triggerTime) throws Exception {
        
        String SQL;
        ResultSet rs=null;
        Connection con=null;
        PreparedStatement prepstat=null;
        boolean created = false;
        
        try {
            con=DConnect.getConnection ();
            String timeString = triggerTime.toString ();
            timeString = timeString.substring (0, timeString.lastIndexOf (":"));
            SQL="update livescore_trigger_schedule set sent=1 where trigger_time like '" + timeString + "%'";
            prepstat=con.prepareStatement (SQL);            
            prepstat.execute ();
        }catch (Exception ex){
            if(con !=null)
                con.close ();
            throw new Exception (ex.getMessage ());
        }finally{
            if(con !=null) {
                con.close ();
            }
        }
    }
}
