/*
 * FeedJob.java
 *
 * Created on August 14, 2007, 4:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.feeds;
import org.quartz.*;
/**
 *
 * @author nii
 */
public class FeedJob implements Job {
    
    /**
     * Creates a new instance of FeedJob
     */
    public FeedJob() {
    }
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("TriggerSetup is executing for FeedReader on " + new java.sql.Timestamp(new java.util.Date().getTime()).toString());
        System.out.println(FeedReader.getFeedUpdates());
        
        //Info Service Alert Job
       /* try{
            new com.rancard.mobility.infoserver.alert.InfoServiceAlertJob().execute(context);
        }catch(org.quartz.JobExecutionException ex){
        
        }*/
    }
}
