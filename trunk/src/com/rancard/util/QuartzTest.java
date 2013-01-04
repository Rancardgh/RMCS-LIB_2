/*
 * QuartzTest.java
 *
 * Created on January 25, 2007, 12:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.util;

import com.rancard.mobility.infoserver.livescore.LiveScoreTriggerSetup;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.helpers.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTest {
    
    public static void main (String[] args) {
        
        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler ();
            
            // and start it off
            scheduler.start ();
            
            JobDetail jobDetail = new JobDetail ("myJob", "group1", LiveScoreTriggerSetup.class);
            
            Trigger trigger = TriggerUtils.makeSecondlyTrigger ();
            trigger.setStartTime (TriggerUtils.getEvenSecondDate (new java.util.Date ()));  // start on the next even hour
            trigger.setName ("livescore_fixtures");
            trigger.setGroup ("group1");
            
            scheduler.scheduleJob (jobDetail, trigger);
            
            try {
                // wait 90 seconds to show jobs
                Thread.sleep (30L * 1000L);
                // executing...
            } catch (Exception e) {
            }
            
            scheduler.shutdown ();
            
        } catch (SchedulerException se) {
            se.printStackTrace ();
        }
    }
}
