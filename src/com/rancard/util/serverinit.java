/*
 * serverinit.java
 *
 * Created on October 20, 2006, 2:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.util;

import com.rancard.common.Config;
import com.rancard.common.Feedback;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import com.rancard.mobility.common.FonCapabilityMtrx;
import com.rancard.mobility.infoserver.livescore.LiveScoreTriggerSetup;
import java.util.HashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class serverinit extends HttpServlet {
    
    //static variables
    PrintWriter out = null;
    FonCapabilityMtrx fcm = null;
    HashMap routingTable = null;
    List types = null;
    HashMap prices = null;
    Scheduler scheduler = null;
    Scheduler feedScheduler = null;
    
    Feedback feedback_en = null;
    Feedback feedback_fr = null;
    
    //Initialize global variables
    public void init (ServletConfig config) throws ServletException {
        super.init (config);
        
        //log statement
        System.out.println (new java.util.Date()+":@com.rancard.util.serverint");
    
        String url = (String) config.getServletContext ().getInitParameter ("contentServerUrl");
        String feedUrl = (String) config.getServletContext ().getInitParameter ("feedServerUrl");
        try{
            fcm = new FonCapabilityMtrx ();
            System.out.println (new java.util.Date()+":Capabilities Matrix initialized");
        }catch(Exception e){
            System.out.println (new java.util.Date()+":Could not initialize capabilities matrix.");
        }
        try {
            types = com.rancard.mobility.contentserver.ContentType.getDistinctTypes (1);
            System.out.println (new java.util.Date()+":Service Types initialized");
        } catch (Exception e) {
            System.out.println (new java.util.Date()+":Could not initialize service types.");
        }
        try{
            //prices = com.rancard.mobility.contentserver.ServicePricingBean.viewAllPrices ();
            prices = com.rancard.util.payment.PaymentManager.viewPricePoints_hm ();
            System.out.println (new java.util.Date()+":Pricing Matrix initialized");
        }catch(Exception e){
            System.out.println (new java.util.Date()+":Could not initialize pricing matrix.");
        }
        /*try{
            scheduler = initScheduler (url);
            System.out.println (new java.util.Date()+":Scheduler initialized");
        }catch (Exception e) {
            System.out.println (new java.util.Date()+"Could not initialize Scheduler." + e.getMessage ());
        }*/
        
      
        try{ 
              //feedScheduler = initFeedScheduler(feedUrl);
              //System.out.println (new java.util.Date()+":FeedScheduler initialized");
        }catch (Exception e) {
            System.out.println (new java.util.Date()+":Could not initialize FeedScheduler." + e.getMessage ());
        }
        try{
            feedback_en = new Feedback ("en");
            System.out.println (new java.util.Date()+":English feedback initialized");
        }catch (Exception e) {
            System.out.println (new java.util.Date()+":Could not initialize English feedback." + e.getMessage ());
        }
        try{
            feedback_fr = new Feedback ("fr");
            System.out.println (new java.util.Date()+":French feedback initialized");
        }catch (Exception e) {
            System.out.println (new java.util.Date()+":Could not initialize French feedback." + e.getMessage ());
        }
        
        /*
        resultTypes.put ("50", "Games Played");
        resultTypes.put ("100", "Win");
        resultTypes.put ("110", "Draw");
        resultTypes.put ("120", "Lost");
        resultTypes.put ("200", "Goals scored by home team");
        resultTypes.put ("210", "Goals conceded");
        resultTypes.put ("300", "Points");
        resultTypes.put ("400", "Goal scored by player");
        resultTypes.put ("410", "Warning");
        resultTypes.put ("420", "Red card");
        resultTypes.put ("430", "Assist");
        resultTypes.put ("440", "Updated scorer");
        resultTypes.put ("500", "Changed status");
        resultTypes.put ("600", "Final Position");
        resultTypes.put ("610", "Result Time");
        resultTypes.put ("615", "Time in milliseconds");
        resultTypes.put ("620", "Time in hh.mm.ss.SS format");
        resultTypes.put ("630", "Time in hh.mm.ss format");
        resultTypes.put ("640", "Shots");
        resultTypes.put ("650", "Laps behind");
        resultTypes.put ("700", "Has Table");
        resultTypes.put ("710", "Table Position");
        resultTypes.put ("720", "Table Penalty Scores");
        System.out.println ("LiveScore Result Types initialized");
        
        periodCodes.put ("0", "No period");
        periodCodes.put ("100", "1st Period");
        periodCodes.put ("110", "2nd Period");
        periodCodes.put ("120", "3rd Period");
        periodCodes.put ("130", "4th Period");
        periodCodes.put ("140", "5th Period");
        periodCodes.put ("150", "6th Period");
        periodCodes.put ("160", "7th Per");
        periodCodes.put ("170", "8th Period");
        periodCodes.put ("180", "9th Period");
        periodCodes.put ("190", "10th Period");
        periodCodes.put ("300", "Full Time");
        periodCodes.put ("400", "Overtime");
        periodCodes.put ("500", "Penalties");
        periodCodes.put ("90", "Current Period");
        System.out.println ("LiveScore Period Codes initialized");
        
        statusCodes.put ("1000", "Game not started");
        statusCodes.put ("1100", "Game Active");
        statusCodes.put ("1200", "Game Interrupted");
        statusCodes.put ("1300", "Game Cancelled");
        statusCodes.put ("1400", "Game ended and confirmed");
        statusCodes.put ("1420", "Game ended, confirmed by 3 sources");
        statusCodes.put ("1500", "Game Closed");
        statusCodes.put ("1600", "Game Postponed");
        statusCodes.put ("1700", "Game Postponed Indefinitely");
        statusCodes.put ("1800", "Game Undecided");
        statusCodes.put ("1900", "Walk Over");
        System.out.println ("LiveScore status Codes initialized");
         */
        
        config.getServletContext ().setAttribute ("capabilitiesMtrx", fcm);
        config.getServletContext ().setAttribute ("serviceTypes", types);
        config.getServletContext ().setAttribute ("pricingMatrix", prices);
        /*
        config.getServletContext ().setAttribute ("resultTypes", resultTypes);
        config.getServletContext ().setAttribute ("periodCodes", periodCodes);
        config.getServletContext ().setAttribute ("statusCodes", statusCodes);
         */
        config.getServletContext ().setAttribute ("scheduler", scheduler);
        config.getServletContext ().setAttribute ("feedback_en", feedback_en);
        config.getServletContext ().setAttribute ("feedback_fr", feedback_fr);
    }
    
    //Process the HTTP Get request
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //request.setAttribute("capabilitiesMtrx", fcm);
    }
    
    //Process the HTTP Post request
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet (request, response);
    }
    
    //Clean up resources
    public void destroy () {
        /*try{
            Scheduler scheduler = (Scheduler) this.getServletConfig ().getServletContext ().getAttribute ("scheduler");
            scheduler.shutdown ();
        } catch (SchedulerException se) {
            se.getMessage ();
        }*/
    }
    
   /* //initialize scheduler without JDBCStore implementation
    private Scheduler initScheduler (String url) throws Exception {
        try {
            // Initiate a Schedule Factory
            SchedulerFactory schedulerFactory = new StdSchedulerFactory ();
            // Retrieve a scheduler from schedule factory
            Scheduler scheduler = schedulerFactory.getScheduler ();
     
            // current time
            java.util.Calendar c = java.util.Calendar.getInstance ();
            int mins = 0;
            try{
                String minite = com.rancard.util.PropertyHolder.getPropsValue ("LS_SCHED_START_AFTER");
                if(minite != null && !minite.equals ("")){
                    mins = Integer.parseInt (minite);
                }else{
                    mins = 5;
                }
            }catch(Exception e){
                mins = 5;
            }
            
            c.add (c.MINUTE, mins);
     
            // Initiate JobDetail with job name, job group, and executable job class
            JobDetail jobDetail = new JobDetail ("jobDetail2", "jobDetailGroup2", LiveScoreTriggerSetup.class);
            org.quartz.JobDataMap dataMap = new org.quartz.JobDataMap ();
            dataMap.put ("url", url);
            jobDetail.setJobDataMap (dataMap);
            // Initiate CronTrigger with its name and group name
            CronTrigger cronTrigger = new CronTrigger ("cronTrigger", "triggerGroup2");
            try {
                // setup CronExpression
                String cexp = new String ("0 " + c.get (c.MINUTE) + " " + c.get (c.HOUR_OF_DAY) + " * * ? *");
                // Assign the CronExpression to CronTrigger
                cronTrigger.setCronExpression (cexp);
            } catch (Exception e) {
                //e.printStackTrace ();
            }
            // schedule a job with JobDetail and Trigger
            scheduler.scheduleJob (jobDetail, cronTrigger);
     
            // start the scheduler
            scheduler.start ();
     
            return scheduler;
     
        } catch (SchedulerException se) {
            se.getMessage ();
            throw new Exception ();
        }
    }
    */
    
    //initialize scheduler with JDBCStore implementation
    private Scheduler initScheduler (String url) throws Exception {
        try {
            // Initiate a Schedule Factory
            StdSchedulerFactory schedulerFactory = new StdSchedulerFactory ();
            
            //initialize scheduler with properties file
            java.util.Properties quartzProps = getQuartzPropertiesPath ();
            
            //initialize  schedulerFactory with properties loaded properties
            schedulerFactory.initialize (quartzProps);
            
            // Retrieve a scheduler from schedule factory
            Scheduler scheduler = null;
            try{
                scheduler = schedulerFactory.getScheduler ();
            }catch(Exception e) {
                System.out.println (e.getMessage ());
            }
            
            // current time
            java.util.Calendar c = java.util.Calendar.getInstance ();
            int mins = 0;
            try{
                String minite = com.rancard.util.PropertyHolder.getPropsValue ("LS_SCHED_START_AFTER");
                if(minite != null && !minite.equals ("")){
                    mins = Integer.parseInt (minite);
                }else{
                    mins = 5;
                }
            }catch(Exception e){
                mins = 5;
            }
            
            c.add (c.MINUTE, mins);
            
            String jobName = "livescore_job";
            String jobGroup = "livescore_job_group";
            String triggerName = "livescore_trigger";
            String triggerGroup = "livescore_trigger_group";
            
            // Initiate JobDetail with job name, job group, and executable job class
            JobDetail jobDetail = new JobDetail (jobName, jobGroup, LiveScoreTriggerSetup.class);
            org.quartz.JobDataMap dataMap = new org.quartz.JobDataMap ();
            dataMap.put ("url", url);
            jobDetail.setJobDataMap (dataMap);
            // Initiate CronTrigger with its name and group name
            CronTrigger cronTrigger = new CronTrigger (triggerName, triggerGroup);
            try {
                // setup CronExpression
                String cexp = new String ("0 " + c.get (c.MINUTE) + " " + c.get (c.HOUR_OF_DAY) + " * * ? *");
                // Assign the CronExpression to CronTrigger
                cronTrigger.setCronExpression (cexp);
            } catch (Exception e) {
                //e.printStackTrace ();
            }
            
            // schedule a job with JobDetail and Trigger
            JobDetail tempJobDetail = scheduler.getJobDetail (jobName, jobGroup);
            CronTrigger tempCronTrigger = (CronTrigger) scheduler.getTrigger (triggerName, triggerGroup);
            
            if(tempJobDetail != null && (tempJobDetail.getName () != null || !tempJobDetail.getName ().equals (""))){
                //scheduler.deleteJob (jobName, jobGroup);
                //scheduler.scheduleJob (tempJobDetail, tempCronTrigger);
                System.out.println("Scheduler with the following description is already initialized:\r\nJobName: " + tempJobDetail.getName () + " | JobGroup: " +
                        tempJobDetail.getGroup () + "  | TriggerName: " + tempCronTrigger.getName () + " | TriggerGroup: " + tempCronTrigger.getGroup ());
            }else{
                scheduler.deleteJob (jobName, jobGroup);
                scheduler.scheduleJob (jobDetail, cronTrigger);
            }
            
            // start the scheduler
            scheduler.start ();
            
            return scheduler;
            
        } catch (SchedulerException se) {
            System.out.println ("Scheduler Initialization Exception: " + se.getMessage ());
            throw new Exception ();
        }
    }
    
   //diabled because InfoFeed process not ready for production 
    //initialize scheduler with JDBCStore implementation
    private Scheduler initFeedScheduler (String url) throws Exception {
        try {
            // Initiate a Schedule Factory
             //log statement
                System.out.println(new java.util.Date()+":Initiating a Schedule Factory for FeedScheduler...");
            StdSchedulerFactory schedulerFactory = new StdSchedulerFactory ();
             //log statement
                System.out.println(new java.util.Date()+":Schedule Factory for FeedScheduler initiated!");
        
            //initialize scheduler with properties file
                System.out.println(new java.util.Date()+":retrieving PropertiesPath for FeedScheduler...");
               
            java.util.Properties quartzFeedsProps  = getQuartzFeedsPropertiesPath ();
           
            
            //initialize  schedulerFactory with properties loaded properties
                System.out.println(new java.util.Date()+":initializing schedulerFactory with properties file:"+quartzFeedsProps+"...");
            schedulerFactory.initialize (quartzFeedsProps);
                System.out.println(new java.util.Date()+":schedulerFactory successfully inititialized with properties file!");
            
            // Retrieve a scheduler from schedule factory
            Scheduler scheduler = null;
            try{
                    System.out.println(new java.util.Date()+":Retrieving a Scheduler from schedule factory...");
                scheduler = schedulerFactory.getScheduler ();
                    System.out.println(new java.util.Date()+":Scheduler successfully retrieved from schedule factory!");
        
            }catch(Exception e) {
                System.out.println (new java.util.Date()+":error retrieving Sheduler from shedule factory:"+e.getMessage ());
            }

            String jobName = "infoFeeds_job";
            String jobGroup = "infoFeeds_job_group";
            String triggerName = "infoFeeds_trigger";
            String triggerGroup = "infoFeeds_trigger_group";
            
            // Initiate JobDetail with job name, job group, and executable job class
            System.out.println (new java.util.Date()+":Initiate JobDetail with job name ("+jobName+"), job group ("+jobGroup+")" +
                    ", and executable job class (com.rancard.mobility.infoserver.feeds.FeedJob)");
  
            JobDetail jobDetail = new JobDetail (jobName, jobGroup, com.rancard.mobility.infoserver.feeds.FeedJob.class);
            
                System.out.println (new java.util.Date()+":Populating JobDataMap...");
                System.out.println (new java.util.Date()+":JobData1: URL="+url);
                
            org.quartz.JobDataMap dataMap = new org.quartz.JobDataMap ();
            dataMap.put ("url", url);
            jobDetail.setJobDataMap (dataMap);
            
                System.out.println (new java.util.Date()+":JobDataMap pupulated!");
            
            // Initiate CronTrigger with its name and group name
             System.out.println (new java.util.Date()+":Initiating CronTrigger with its name ("+triggerName+")" +
                     "and group name ("+triggerGroup+")...");
               
            CronTrigger cronTrigger = new CronTrigger (triggerName, triggerGroup);
            try {
                // setup CronExpression
                    System.out.println (new java.util.Date()+":seting up CronExpression...");
                String cexp = new String ("0 0/2 * * * ?");
                // Assign the CronExpression to CronTrigger
                
                    System.out.println (new java.util.Date()+":Assigning the CronExpression ("+cexp+")to CronTrigger...");
                cronTrigger.setCronExpression (cexp);
                
                    System.out.println (new java.util.Date()+":CronExpression ("+cexp+") Assigned!");
      
            } catch (Exception e) {
                //e.printStackTrace ();
                System.out.println (new java.util.Date()+": error setting up CronExpression:" + e.getMessage());
            }
            
            // schedule a job with JobDetail and Trigger
            System.out.println (new java.util.Date()+": scheduling job with JobDetail ("+jobName+"|"+jobGroup+") and Trigger ("+triggerName+"|"+triggerGroup);
         
            JobDetail tempJobDetail = scheduler.getJobDetail (jobName, jobGroup);
            CronTrigger tempCronTrigger = (CronTrigger) scheduler.getTrigger (triggerName, triggerGroup);
            
            if(tempJobDetail != null && (tempJobDetail.getName () != null || !tempJobDetail.getName ().equals (""))){
                //scheduler.deleteJob (jobName, jobGroup);
                //scheduler.scheduleJob (tempJobDetail, tempCronTrigger);
                System.out.println(new java.util.Date()+":Scheduler with the following description is already initialized:\r\nJobName: " + tempJobDetail.getName () + " | JobGroup: " +
                        tempJobDetail.getGroup () + "  | TriggerName: " + tempCronTrigger.getName () + " | TriggerGroup: " + tempCronTrigger.getGroup ());
            }else{
                
                scheduler.deleteJob (jobName, jobGroup);
                scheduler.scheduleJob (jobDetail, cronTrigger);
            }
            
            // start the scheduler
            System.out.println (new java.util.Date()+": Starting FeedScheduler...");
          
            scheduler.start ();
            
            return scheduler;
            
        } catch (SchedulerException se) {
            System.out.println (new java.util.Date()+":Feeds Scheduler Initialization Exception: " + se.getMessage ());
            throw new Exception ();
        }
    }
    
    public java.util.Properties getQuartzPropertiesPath () throws Exception {
        
        java.util.Properties path = new java.util.Properties ();
        String value = "";
        try {
            javax.naming.Context ctx = new javax.naming.InitialContext ();
            com.rancard.common.Config appConfig = (com.rancard.common.Config) ctx.lookup ("java:comp/env/bean/quartzConfig");
            
            if(appConfig!=null){
                path = appConfig.load ();
            }
            
        } catch (javax.naming.NamingException e) {
            //   Log.write(e);
            throw new Exception (e.getMessage ());
        }
        
        return path;
        
    }
   //diabled because InfoFeed process not ready for production 
    public java.util.Properties getQuartzFeedsPropertiesPath () throws Exception {
        
        java.util.Properties path = new java.util.Properties ();
        String value = "";
        try {
            javax.naming.Context ctx = new javax.naming.InitialContext ();
            com.rancard.common.Config appConfig = (com.rancard.common.Config) ctx.lookup ("java:comp/env/bean/quartzFeedsConfig");
            
            if(appConfig!=null){
                path = appConfig.load ();
            }
            
        } catch (javax.naming.NamingException e) {
            //   Log.write(e);
            throw new Exception (e.getMessage ());
        }
        
        return path;
   
    }     
}
