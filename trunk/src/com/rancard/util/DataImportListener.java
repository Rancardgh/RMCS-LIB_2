/* Licence:
*   Use this however/wherever you like, just don't blame me if it breaks anything.
*
* Credit:
*   If you're nice, you'll leave this bit:
*
*   Class by Pierre-Alexandre Losson -- http://www.telio.be/blog
*   email : plosson@users.sourceforge.net
*/
package com.rancard.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Original : plosson on 06-janv.-2006 15:05:44 - Last modified  by $Author: Administrator $ on $Date: 2006/04/24 18:47:33 $
 * @version 1.0 - Rev. $Revision: 1.1.1.1 $
 */
public class DataImportListener implements ProcessListener
{
    private HttpServletRequest request;
    private long delay = 0;
    private long startTime = 0;
    private int totalProcessSteps = 0;
    private int totalProcessStepsCompleted = 0;
    private int totalFiles = -1;

    public DataImportListener(HttpServletRequest request, long debugDelay, int numberOfSteps)
    {
        this.request = request;
        this.delay = debugDelay;
        /// data import here is a series of processes which involve uploading file,
        // verifying file etc
        totalProcessSteps = numberOfSteps;// request.getContentLength();
        this.startTime = System.currentTimeMillis();
    }

    public void start()
    {
        totalFiles ++;
        updateUploadInfo("start");
    }

    public void processRead(int stepsRead)
    {
        totalProcessStepsCompleted = totalProcessStepsCompleted + stepsRead;
        updateUploadInfo("progress");

        try
        {
            Thread.sleep(delay);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void error(String message)
    {
        updateUploadInfo("error");
    }

    public void done()
    {
        updateUploadInfo("done");
    }

    private long getDelta()
    {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    private void updateUploadInfo(String status)
    {
        long delta = (System.currentTimeMillis() - startTime) / 1000;
        request.getSession().setAttribute("uploadInfo", new UploadInfo(totalFiles, totalProcessSteps, totalProcessStepsCompleted,delta,status));
    }

}
