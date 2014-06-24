package com.rancard.mobility.common;



import com.rancard.common.CPConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Mustee on 2/10/14.
 */
public class ThreadedMessageSender implements Runnable {
    private final Logger logger = Logger.getLogger(ThreadedMessageSender.class.getName());

    private final CPConnection cnxn;
    private final String msisdn;
    private final String shortCode;
    private final String message;
    private final String account;
    private final String accountID;
    private final String keyword;
    private final String product;
    private final String smsc;
    private final double price;
    private final int delay;

    public ThreadedMessageSender(CPConnection cnxn, String msisdn, String shortCode, String message, String account,
                                 String accountID, String keyword, String product, String smsc, double price, int delay) {
        this.cnxn = cnxn;
        this.msisdn = msisdn;
        this.shortCode = shortCode;
        this.message = message;
        this.account = account;
        this.accountID = accountID;
        this.keyword = keyword;
        this.product = product;
        this.smsc = smsc;
        this.price = price;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("to", msisdn);
            params.put("from", shortCode);
            params.put("account", account);
            params.put("account_id", accountID);
            params.put("keyword", keyword);
            params.put("text", message);
            params.put("product", product);
            params.put("price", Double.toString(price));

            PushDriver driver = PushDriver.getDriver(cnxn, params);
            if (driver == null) {
                logger.warning("No driver found for connection of type: " + cnxn.getDriverType());
                return;
            }

            Thread.sleep(delay);
            driver.sendSMSTextMessage();


        } catch (InterruptedException e) {
            logger.severe("Thread error sending notification: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Error sending notification: " + e.getMessage());
        }
    }
}
