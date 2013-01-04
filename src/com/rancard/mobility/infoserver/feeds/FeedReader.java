/*
 * FeedReader.java
 *
 * Created on August 6, 2007, 2:24 PM
 */
package com.rancard.mobility.infoserver.feeds;

import com.rancard.mobility.infoserver.InfoService;
import com.rancard.security.AuthCredentialSupplier;
import com.sun.syndication.fetcher.FetcherException;
import java.io.*;
import java.sql.*;
import java.util.Enumeration;
import java.util.regex.*;

import com.rancard.common.DConnect;
import com.rancard.common.uidGen;
import com.rancard.mobility.infoserver.InfoServiceDB;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.sun.org.apache.xml.internal.serializer.ElemDesc;

import com.sun.syndication.io.*;
import com.sun.syndication.feed.synd.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.net.URL;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherEvent;
import com.sun.syndication.fetcher.FetcherListener;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpClientFeedFetcher;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.text.html.HTML.Attribute;

import javax.swing.text.html.parser.Element;
import javax.swing.text.html.parser.AttributeList;

/**
 *
 * @author nii
 * @version
 */
public class FeedReader extends HttpServlet {

    public static final String MSG_PRTY_BHVR_STORE_AND_WAIT = "0";
    public static final String MSG_PRTY_BHVR_MOST_RECENT_IMMEDIATELY = "1";
    public static final String MSG_PRTY_BHVR_ALL_IMMEDIATELY = "2";

    private static String extractCategories (String feedLink, SyndCategory category) {

        String categoryString = "";
        if (feedLink != null && category != null) {
            if (feedLink.equals ("http://m.soccernet.com")) {
                categoryString = category.getName ().replaceAll ("_", ",");
            } else {
                categoryString = category.getName ();
            }
        }

        return categoryString;
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType ("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter ();
        out.println (getFeedUpdates ());
        out.close ();
    }
    //get a list of feeds

    // check for changes
    public static int getFeedUpdates () {
        return getFeedUpdates (25, null);
    }

    // check for changes
    public static int getFeedUpdates (String[] feedIds) {
        return getFeedUpdates (25, feedIds);
    }

    public static int getFeedUpdates (int numEntries, String[] feedIds) {
        // hashmap to hold feeds
        java.util.HashMap feeds = new java.util.HashMap ();
        // get Active feeds
        java.util.HashMap feedRecords = getActiveFeeds (feedIds);
        // get updated  feeds
        feeds = retrieveUpdatedFeeds (feedRecords);
        // feedId // <SndFeed object>
        // update local accounts
        java.util.HashMap feedSubs = getFeedSubscribers (feeds);
        // list of all cp_accounts to update grouped  by feed_id

        // create batch statements

        java.util.Iterator<String> feedIdIter = feedSubs.keySet ().iterator ();
        while (feedIdIter.hasNext ()) {
            String tmpFeedId = feedIdIter.next ();
            java.util.ArrayList fdSubs = (java.util.ArrayList) feedSubs.get (tmpFeedId);
            SyndFeed feed = (SyndFeed) feeds.get (tmpFeedId);
           

            if (feed.getEntries ().size () < numEntries) {
                numEntries = feed.getEntries ().size ();
            }

            ///
            //java.util.Iterator<SyndEntry> entries = feed.getEntries ().iterator ();
            java.util.ListIterator<SyndEntry> entries = feed.getEntries ().listIterator (numEntries);
            int itemCounter = numEntries - 1;

            while (entries.hasPrevious ()) {
                SyndEntry currentEntry = entries.previous ();
                String msg = currentEntry.getDescription ().getValue ();
                java.util.Date pubDate = currentEntry.getPublishedDate ();
                java.util.List categories = currentEntry.getCategories ();
                String author=currentEntry.getAuthor();
                String title=currentEntry.getTitle();
                String con_url=currentEntry.getLink();
                String imageURL="";
                try{
                    ArrayList<Element> foreign=(ArrayList<Element>) currentEntry.getForeignMarkup();
                    Element attrib=foreign.get(1);
                 AttributeList at=((ArrayList<Element>)currentEntry.getForeignMarkup()).get(0).getAttribute("attributes");
                 Vector<?> v=(Vector<?>) at.getValues();
                   imageURL=v.get(v.indexOf("url")).toString();

                   
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    e.printStackTrace(System.out);
                }
                
                System.out.println("this is image url---------------------->"+imageURL);

                String categoryStr = "";
                if (categories != null) {
                    for (int i = 0; i < categories.size (); i++) {
                        SyndCategory cat = (SyndCategory) categories.get (i);
                        if (categoryStr.equals ("")) {
                            categoryStr = categoryStr + extractCategories (feed.getLink (), cat);
                        } else {
                            categoryStr = categoryStr + "," + extractCategories (feed.getLink (), cat);
                        }
                    }
                }

                // Special case: Extract only up to start of HTML tag because of silly ESPN feeds
                int html_start_location = msg.indexOf ("<img width");
                msg = msg.substring (0, (html_start_location == -1) ? msg.length () : html_start_location);
                // Also for ESPN: Remove bold and italics tags
                msg = msg.replaceAll ("<b>", "");
                msg = msg.replaceAll ("</b>", "");
                msg = msg.replaceAll ("<i>", "");
                msg = msg.replaceAll ("</i>", "");
                // For MTV: remove paragraph HTML tags
                msg = msg.replaceAll ("&lt;p&gt;", "");
                msg = msg.replaceAll ("&lt;/p&gt;", "");
                msg = msg.replaceAll ("&lt;b&gt;", "");
                msg = msg.replaceAll ("&lt;/b&gt;", "");
                msg = msg.replaceAll ("&lt;i&gt;", "");
                msg = msg.replaceAll ("&lt;/i&gt;", "");
                
                //For VOA: Remove iffy quotes
                msg = msg.replaceAll("“", "\"");
                msg = msg.replaceAll("”", "\"");

                if (msg.length () > 0) {
                    for (int i = 0; i < fdSubs.size (); i++) {
                        com.rancard.mobility.infoserver.feeds.CPUserFeeds afeed = ((com.rancard.mobility.infoserver.feeds.CPUserFeeds) fdSubs.get (i));

                        String accountId = afeed.getCpUserId ();
                        String keyword = afeed.getKeyword ();
                        String regexReject = (afeed.getRegexReject () == null) ? "" : afeed.getRegexReject ();
                        int allowedAge = afeed.getAllowedAge ();
                        int msgDlrPriority = afeed.getMsgDlrPrority ();

                        UserService service = new UserService ();
                        try {
                            service = com.rancard.mobility.infoserver.common.services.ServiceManager.viewService (keyword, accountId);
                        } catch (Exception e) {
                            System.out.println (new java.util.Date () + ": " + keyword + ": " + accountId
                                    + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: "
                                    + "Did not find matching service. May not be able to transmit updates");
                        }

                        //first check to see if content should be rejected based on regex_reject field
                        String[] regexes = regexReject.split ("::");
                        int rejectFlag = 0;
                        for (int j = 0; j < regexes.length; j++) {
                            String expr = regexes[j];
                            if (!expr.equals ("")) {
                                Pattern pattern = Pattern.compile (expr);
                                Matcher matcher = pattern.matcher (msg);
                                if (matcher.find ()) {
                                    System.out.println (new java.util.Date () + ": " + keyword + ": " + accountId
                                            + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: "
                                            + "regex_reject pattern matched. Reject item: " + msg);
                                    rejectFlag = 1;
                                    break;
                                }
                            }
                        }

                        if (rejectFlag == 0) {
                            InfoServiceDB db = new InfoServiceDB ();
                            java.util.Date date = new java.util.Date ();

                            try {

                                int contentUpdateCheck = 0;
                                String messageRef = uidGen.generateSecureUID ();

                                if (allowedAge > 0) {
                                    java.util.Calendar cal = java.util.Calendar.getInstance ();
                                    cal.add (java.util.Calendar.HOUR, -1 * allowedAge);
                                    java.util.Date allowedStartTime = cal.getTime ();

                                    if (pubDate == null) {
                                        // Silly feed doesn't have publishDate on items so will add entry anyway
                                        System.out.println (new java.util.Date () + ": " + keyword + ": " + accountId
                                                + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: "
                                                + "Can't apply filtering by allowed_age because content doesn't have timestamp. Will add anyway");
                                        //contentUpdateCheck = db.createInfoServiceEntry (date, keyword, msg, accountId, "RSS", "", "", 160);
                                        //String message, String keyword, String accountId, int msg_id, java.sql.Date sqlDate,String ownerId, String imageURL, String articleTitle, String contentURL, String author,String tags, String messageRef
                                        contentUpdateCheck = db.createInfoServiceEntryWithTags (date, keyword, msg, accountId, "RSS", imageURL, title,con_url,author,categoryStr, messageRef, 160);
                                    } else if (pubDate.after (allowedStartTime)) {
                                        // Nice, content matches our timestamp filtering criterion. Add!
                                        //contentUpdateCheck = db.createInfoServiceEntry (date, keyword, msg, accountId, "RSS", "", "", 160);
                                        contentUpdateCheck = db.createInfoServiceEntryWithTags (date, keyword, msg, accountId, "RSS", imageURL, title,con_url,author,categoryStr, messageRef, 160);
                                    } else {
                                        //late feed items -- will not store -- set content update check to negative
                                        contentUpdateCheck = -1;
                                    }
                                } else {
                                    // No restriction on content age. Pull down everything you can!
                                    //contentUpdateCheck = db.createInfoServiceEntry (date, keyword, msg, accountId, "RSS", "", "", 160);
                                    contentUpdateCheck = db.createInfoServiceEntryWithTags (date, keyword, msg, accountId, "RSS", imageURL, title,con_url,author,categoryStr, messageRef, 160);
                                }

                                if (contentUpdateCheck == -1) {
                                    System.out.println (new java.util.Date () + ": " + keyword + ": " + accountId
                                            + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: content is old. feed item rejected");
                                } else if (contentUpdateCheck == 1) {
                                    System.out.println (new java.util.Date () + ": " + keyword + ": " + accountId
                                            + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: content length exceeds 160 chars. feed item rejected");
                                } else if (contentUpdateCheck == 2) {
                                    System.out.println (new java.util.Date () + ": " + keyword + ": " + accountId
                                            + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: duplicate content. feed item rejected. content: " + msg);
                                } else {
                                    //message has been stored. Determine transmission behaviour
                                    if (new String ("" + msgDlrPriority).equals (MSG_PRTY_BHVR_ALL_IMMEDIATELY)) {
                                        //deliver immediately
                                        System.out.println ("delivering content now ... " + messageRef);
                                        // Spawn off new thread to do this while we just carry on with our lives
                                        (new Thread (new ContentDispatcher (date, service, messageRef))).start ();
                                    } else if (new String ("" + msgDlrPriority).equals (MSG_PRTY_BHVR_MOST_RECENT_IMMEDIATELY) && (itemCounter == 0)) {
                                        // this item is at the top of the pile -- the most recent of this batch. Send immediately
                                        System.out.println ("delivering last, most recent content now ... " + messageRef);
                                        (new Thread (new ContentDispatcher (date, service, messageRef))).start ();
                                    } else if (new String ("" + msgDlrPriority).equals (MSG_PRTY_BHVR_STORE_AND_WAIT)) {
                                        // this item has low priority -- store and wait for scheduled pick-up
                                        System.out.println ("content stored! user can pick it up whenever ... " + messageRef);
                                    } else {
                                        // this item has low priority -- store and wait for scheduled pick-up
                                        System.out.println ("content stored! user can pick it up whenever ... " + messageRef);
                                    }
                                }

                            } catch (Exception ex) {
                                System.out.println (new java.util.Date () + " Create Info Service Entry unable to create. : " + "Error Message:" + ex.getMessage ());
                                ex.printStackTrace ();
                            }

                        }
                    }
                } else {
                    System.out.println (new java.util.Date () + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: blank content. feed item rejected for feedId: " + tmpFeedId);
                }

                itemCounter--;
                if (itemCounter < 0) {
                    break;
                }
            }
        }

        return 0;
    }

    private static java.util.HashMap getActiveFeeds (String[] feedIds) {
        // get all feeds setup on our system
        String feedIdString = "";

        if (feedIds != null && feedIds.length > 0) {
            feedIdString = "'" + feedIds[0].trim () + "'";
            for (int i = 1; i < feedIds.length; i++) {
                feedIdString = feedIdString + ",'" + feedIds[i] + "'";
            }
        }

        PreparedStatement prepstat = null;
        ResultSet rs = null;
        Connection con = null;

        java.util.HashMap feeds = new java.util.HashMap ();
// get a list of active feeds
        try {
            String SQL = "select * from feeds where is_active=1";
            if (!feedIdString.equals ("")) {
                SQL = "select * from feeds where is_active=1 and feed_id in (" + feedIdString + ")";
            }
            con = DConnect.getConnection ();
            prepstat = con.prepareStatement (SQL);
            rs = prepstat.executeQuery ();

            while (rs.next ()) {
                URL feedUrl = new URL (rs.getString ("feed_url"));
                String id = rs.getString ("feed_id");
                String name = rs.getString ("feed_name");
                String is_active = rs.getString ("is_active");
                String username = (rs.getString ("username") == null) ? "" : rs.getString ("username");
                String password = (rs.getString ("password") == null) ? "" : rs.getString ("password");

                Feed currentItem = new Feed ();
                currentItem.setFeedId (id);
                currentItem.setFeedName (name);
                currentItem.setFeedURL (feedUrl);
                currentItem.setUsername (username);
                currentItem.setPassword (password);
                if (is_active.equals ("0")) {
                    currentItem.setActive (false);
                } else if (is_active.equals ("1")) {
                    currentItem.setActive (true);
                }

                feeds.put (id, currentItem);
            }
        } catch (Exception ex) {

            System.out.println (new java.util.Date () + " ERROR: " + ex.getMessage ());
            ex.printStackTrace ();
        } finally {
        }
        return feeds;
    }

    private static java.util.HashMap getFeedSubscribers (java.util.HashMap feedIds) {
        java.util.HashMap feedUsers = new java.util.HashMap ();
        PreparedStatement prepstat = null;
        ResultSet rs = null;
        Connection con = null;
        int messagesUpdated = 0;
        // get feeds
        String ids = "(";

        if (!feedIds.keySet ().isEmpty ()) {
            java.util.Iterator<String> feedIdIter = feedIds.keySet ().iterator ();
            while (feedIdIter.hasNext ()) {
                String feedId = feedIdIter.next ();
                ids = ids + feedId;
                if (feedIdIter.hasNext ()) {
                    ids = ids + ",";
                }
                feedUsers.put (feedId, new java.util.ArrayList ());
            }
            ids = ids + ")";

            java.util.ArrayList userfeeds = new java.util.ArrayList ();
            // check for new feed messages
            try {
                String SQL = "select * from cp_user_feeds WHERE feed_id IN " + ids + "  Order by feed_id";
                con = DConnect.getConnection ();
                prepstat = con.prepareStatement (SQL);
                rs = prepstat.executeQuery ();

                while (rs.next ()) {

                    com.rancard.mobility.infoserver.feeds.CPUserFeeds cpfeed = new com.rancard.mobility.infoserver.feeds.CPUserFeeds ();
                    String accountId = rs.getString ("account_id");
                    String keyword = rs.getString ("keyword");
                    String feedId = rs.getString ("feed_id");
                    int allowedAge = rs.getInt ("allowed_age");
                    String regexReject = rs.getString ("regex_reject");
                    int msgDlrPriority = rs.getInt ("msg_dlr_priority");

                    java.util.ArrayList tmpFeeds = (java.util.ArrayList) feedUsers.get (feedId);
                    cpfeed.setFeedId (feedId);
                    cpfeed.setCpUserId (accountId);
                    cpfeed.setKeyword (keyword);
                    cpfeed.setAllowedAge (allowedAge);
                    cpfeed.setRegexReject (regexReject);
                    cpfeed.setMsgDlrPrority (msgDlrPriority);

                    tmpFeeds.add (cpfeed);
                    feedUsers.put (feedId, tmpFeeds);
                    // messagesUpdated += retrieveNewMessages(account_id, keyword, feed_id);
                }
                // sort users into feed groups in readiness for batch updates



            } catch (Exception ex) {
                ex.printStackTrace ();
                System.out.println ("ERROR: " + ex.getMessage ());
            } finally {
                try {
                    if (prepstat != null) {
                        prepstat.close ();
                        prepstat = null;
                    }
                    if (rs != null) {
                        rs.close ();
                        rs = null;
                    }
                    if (con != null) {
                        con.close ();
                        con = null;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace ();
                }
            }
        }
        return feedUsers;
    }

    private static java.util.HashMap retrieveUpdatedFeeds (java.util.HashMap feedRecords) {
        java.util.HashMap feeds = new java.util.HashMap ();

        java.util.Iterator<String> entries = feedRecords.keySet ().iterator ();
        while (entries.hasNext ()) {

            String feedId = entries.next ();
            Feed currentFeed = (Feed) feedRecords.get (feedId);
            URL inputUrl = currentFeed.getFeedURL ();
            String username = currentFeed.getUsername ();
            String password = currentFeed.getPassword ();
            FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance ();
            FeedFetcher fetcher;

            if (username != null && !username.equals ("") && password != null && !password.equals ("")) {
                AuthCredentialSupplier authCredentials = new AuthCredentialSupplier (username, password);
                fetcher = new HttpClientFeedFetcher (feedInfoCache, authCredentials);
            } else {
                fetcher = new HttpClientFeedFetcher (feedInfoCache);
            }


            System.err.println ("Retrieving feed " + inputUrl.toExternalForm ());
            try {
                // Retrieve the feed.
                // We will get a Feed Polled Event and then a
                // Feed Retrieved event (assuming the feed is valid)
                SyndFeed feed = fetcher.retrieveFeed (inputUrl);
                // if feed is retrieved  add to list of feeds which needs to be updated
                System.err.println (new java.util.Date () + " " + inputUrl + " retrieved");
                System.err.println (new java.util.Date () + " " + inputUrl + " has a title: " + feed.getTitle () + " and contains " + feed.getEntries ().size () + " entries.");

                if (feed != null && !feed.getEntries ().isEmpty ()) {
                    feeds.put (feedId, feed);
                }
            } catch (IllegalArgumentException ex) {
                System.err.println (new java.util.Date () + " Illegal Argument . Likely an invalid URL");
                ex.printStackTrace ();

            } catch (FeedException ex) {

                ex.printStackTrace ();
            } catch (FetcherException ex) {
                ex.printStackTrace ();
            } catch (IOException ex) {
                ex.printStackTrace ();
            } catch (Exception ex) {
                System.err.println (new java.util.Date () + " Unknown Host : lets try the next URL");
                ex.printStackTrace ();
            }
        }

        return feeds;
    }
    // update subscribers to those feeds

    static class FetcherEventListenerImpl implements FetcherListener {

        /**
         * @see com.sun.syndication.fetcher.FetcherListener#fetcherEvent(com.sun.syndication.fetcher.FetcherEvent)
         */
        public void fetcherEvent (FetcherEvent event) {
            String eventType = event.getEventType ();
            if (FetcherEvent.EVENT_TYPE_FEED_POLLED.equals (eventType)) {
                System.err.println ("\tEVENT: Feed Polled. URL = " + event.getUrlString ());
            } else if (FetcherEvent.EVENT_TYPE_FEED_RETRIEVED.equals (eventType)) {

                // handle feed changes here
                // update users who have subscribed to this feed

                System.err.println ("\tEVENT: Feed Retrieved. URL = " + event.getUrlString ());
            } else if (FetcherEvent.EVENT_TYPE_FEED_UNCHANGED.equals (eventType)) {
                System.err.println ("\tEVENT: Feed Unchanged. URL = " + event.getUrlString ());
            }
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest (request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest (request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo () {
        return "Short description";
    }
// </editor-fold>
}
