/*
 * processsubscriberequest.java
 *
 * Created on October 28, 2006, 1:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore.serviceinterfaces;

import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.mobility.infoserver.livescore.LiveScoreFixture;
import com.rancard.mobility.infoserver.livescore.LiveScoreQuartzHelper;
import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
import com.rancard.mobility.infoserver.livescore.LiveScoreSubscriptionHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;

/**
 *
 * @author Messenger
 */
public class setuptriggers extends HttpServlet implements RequestDispatcher {
    
    //public static final int DEFAULT_ALLOWANCE = 4;
    //public static final int OUTLOOK_PERIOD = 24;
    
    //Initialize global variables
    public void init () throws ServletException {
    }
    
    //Process the HTTP Get request
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int DEFAULT_ALLOWANCE = 0;
        int OUTLOOK_PERIOD = 0;
        int REREG_START_HOUR_OF_DAY =0;
        
        //log statement
         System.out.println (new java.util.Date()+"@com.rancard.mobility.infoserver.livescore.serviceinterfaces");
        
        
        try{
            String value = com.rancard.util.PropertyHolder.getPropsValue ("LS_TRIGGER_ALLOWANCE");
            if(value != null && !value.equals ("")){
                DEFAULT_ALLOWANCE = Integer.parseInt (value);
            }else{
                DEFAULT_ALLOWANCE = 6;
            }
        }catch(Exception e){
            DEFAULT_ALLOWANCE = 6;
        }
        
        try{
            String value = com.rancard.util.PropertyHolder.getPropsValue ("LS_GAME_OUTLOOK");
            if(value != null && !value.equals ("")){
                OUTLOOK_PERIOD = Integer.parseInt (value);
            }else{
                OUTLOOK_PERIOD = 24;
            }
        }catch(Exception e){
            OUTLOOK_PERIOD = 24;
        }
         
         //get LS re-registration trigger start time (HOUR_OF_DAY)
        try{
            String value = com.rancard.util.PropertyHolder.getPropsValue ("REREG_TRIGGER_START_HOUR_OF_DAY");
            if(value != null && !value.equals ("")){
                REREG_START_HOUR_OF_DAY = Integer.parseInt (value);
            }else{
                REREG_START_HOUR_OF_DAY = 7;
            }
        }catch(Exception e){
            REREG_START_HOUR_OF_DAY = 7;
        }
        
        //get reeponse writer
        PrintWriter out = response.getWriter ();
        Calendar calendar = java.util.Calendar.getInstance ();
        java.sql.Date today = new java.sql.Date (calendar.getTime ().getTime ());
        //String dateString = today.toString ();
        Scheduler scheduler = null;
        String url = this.getServletContext ().getInitParameter ("contentServerUrl");
        String todayString = new java.sql.Timestamp (new java.util.Date ().getTime ()).toString ();
        
        try {
            scheduler = (Scheduler) this.getServletContext ().getAttribute ("scheduler");
            if (scheduler != null) {
                System.out.println (new java.util.Date()+":Setting up triggers on " + todayString);
                System.out.println (new java.util.Date()+":Looking for game times...");
                ArrayList dates = LiveScoreServiceManager.viewDistinctGameTimesForDate (today, LiveScoreFixture.NOT_PLAYED);
                String message = "";
                
                if (dates.size () > 0) {
                    System.out.println (new java.util.Date()+":Found " + dates.size () + " game time(s) for.");
                    for (int i = 0; i < dates.size (); i++) {
                        //set times
                        String dateString = (String)dates.get (i);
                        System.out.println (new java.util.Date()+":Game time " + (i + 1) + ": " + dateString);
                        java.sql.Timestamp gameTime = java.sql.Timestamp.valueOf (dateString);
                        Calendar cal = Calendar.getInstance ();
                        //create actual game time
                        cal.setTime (new java.util.Date (gameTime.getTime ()));
                        //set trigger for 4 hours before game time
                        cal.add (cal.HOUR_OF_DAY, -(DEFAULT_ALLOWANCE));
                        
                        Calendar now = Calendar.getInstance ();
                        //check if the 4-hour allowance is feasible
                        String allowance = "";
                        boolean createTrigger = false;
                        if(cal.before (now)) {
                            int halfOfDefaultAllowance = DEFAULT_ALLOWANCE / 2;
                            cal.setTime (new java.util.Date (gameTime.getTime ()));
                            cal.add (cal.HOUR_OF_DAY, -(halfOfDefaultAllowance));
                            //check if allowance is feasible
                            allowance = "";
                            if(cal.before (now)) {
                                createTrigger = false;
                            }else{
                                allowance = "" + halfOfDefaultAllowance;
                                createTrigger = true;
                            }
                        }else{
                            allowance = "" + DEFAULT_ALLOWANCE;
                            createTrigger = true;
                        }
                        
                        if(createTrigger/* && LiveScoreServiceManager.createTriggerSchedule (new java.sql.Timestamp (cal.getTimeInMillis ())) == true*/){
                            System.out.println (new java.util.Date()+":Trigger time " + (i + 1) + ": " + new java.sql.Timestamp (cal.getTimeInMillis ()).toString ());
                            
                            //create jobs
                            String jobName = new String ("livescore_fixture_today_" + (i +1));
                            String groupName = new String ("todays_games_" + (i +1));
                            SimpleTrigger trigger = new SimpleTrigger (jobName, groupName, cal.getTime ());
                            JobDetail jobDetail = new JobDetail (jobName, groupName, LiveScoreQuartzHelper.class);
                            
                            org.quartz.JobDataMap dataMap = new org.quartz.JobDataMap ();
                            dataMap.put ("allowance", allowance);
                            dataMap.put ("url", url);
                            jobDetail.setJobDataMap (dataMap);
                            
                            JobDetail temp = scheduler.getJobDetail (jobName, groupName);
                            if(temp != null && (temp.getName () != null || !temp.getName ().equals (""))){
                                scheduler.deleteJob (jobName, groupName);
                            }
                            
                            //jobDetail.setJobDataMap (dataMap);
                            scheduler.scheduleJob (jobDetail, trigger);
                            this.getServletConfig ().getServletContext ().setAttribute ("scheduler", scheduler);
                        }
                    }
                } else {
                    System.out.println (new java.util.Date()+":No game times found");
                }
                
                //set up trigger for re-subscription for livescore service
                java.util.Calendar now = java.util.Calendar.getInstance ();
                java.util.Calendar preferredTriggerTime = java.util.Calendar.getInstance ();
                java.util.Calendar triggerTime = java.util.Calendar.getInstance ();
                
                preferredTriggerTime.set (preferredTriggerTime.HOUR_OF_DAY, REREG_START_HOUR_OF_DAY);
                preferredTriggerTime.set (preferredTriggerTime.MINUTE, 0);
                preferredTriggerTime.set (preferredTriggerTime.SECOND, 0);
                preferredTriggerTime.set (preferredTriggerTime.MILLISECOND, 0);
                
                if(now.before (preferredTriggerTime)){
                    triggerTime = preferredTriggerTime;
                }else{
                    now.add(Calendar.MINUTE, 5); //set to trigger five mins after server restart
                    triggerTime =now; 
                }
                
                
                //Trigger setup 4 LiveScore  auto All-League Monthly  re-subscription---------------------------------------------
                    //log statement
                System.out.println (new java.util.Date()+":Trigger time for re-subscription process: " + new java.sql.Timestamp (triggerTime.getTimeInMillis ()).toString ());
                
                //create jobs
                String jobName = new String ("livescore_re-subscription_today");
                String groupName = new String ("livescore_re-subscription");
                SimpleTrigger trigger = new SimpleTrigger (jobName, groupName, triggerTime.getTime ());
                JobDetail jobDetail = new JobDetail (jobName, groupName, LiveScoreSubscriptionHelper.class);
                
                org.quartz.JobDataMap dataMap = new org.quartz.JobDataMap ();
                dataMap.put ("url", url);
                jobDetail.setJobDataMap (dataMap);
                
                JobDetail temp = scheduler.getJobDetail (jobName, groupName);
                if(temp != null && (temp.getName () != null || !temp.getName ().equals (""))){
                    scheduler.deleteJob (jobName, groupName);
                }
                
                //jobDetail.setJobDataMap (dataMap);
                scheduler.scheduleJob (jobDetail, trigger);
                this.getServletConfig ().getServletContext ().setAttribute ("scheduler", scheduler);
            }else {
                System.out.println ("scheduler is null");
            }
        } catch (Exception e) {
            System.out.println (new java.util.Date()+"error setting up LiveScore triggers:"+e.getMessage ());
        } finally {
        }
    }
    
    //Process the HTTP Post request
    public void doPost (HttpServletRequest request,
            HttpServletResponse response) throws
            ServletException, IOException {
        doGet (request, response);
    }
    
    //Clean up resources
    public void destroy () {
    }
    
    public void forward (ServletRequest request,
            ServletResponse response) throws ServletException, IOException{
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        doGet (req, resp);
    }
    
    public void include (ServletRequest request,
            ServletResponse response)  throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        doGet (req, resp);
    }
    
}
