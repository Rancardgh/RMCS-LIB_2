
package com.rancard.mobility.infoserver.feeds;

import com.rancard.common.DConnect;
import com.rancard.common.uidGen;
import com.rancard.mobility.infoserver.InfoServiceDB;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.security.AuthCredentialSupplier;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherEvent;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.FetcherListener;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpClientFeedFetcher;
import com.sun.syndication.io.FeedException;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.AttributeList;
import javax.swing.text.html.parser.Element;

public class FeedReader extends HttpServlet {
    public static final String MSG_PRTY_BHVR_STORE_AND_WAIT = "0";
    public static final String MSG_PRTY_BHVR_MOST_RECENT_IMMEDIATELY = "1";
    public static final String MSG_PRTY_BHVR_ALL_IMMEDIATELY = "2";

    private static String extractCategories(String feedLink, SyndCategory category) {
        String categoryString = "";
        if ((feedLink != null) && (category != null)) {
            if (feedLink.equals("http://m.soccernet.com")) {
                categoryString = category.getName().replaceAll("_", ",");
            } else {
                categoryString = category.getName();
            }
        }
        return categoryString;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(getFeedUpdates());
        out.close();
    }

    public static int getFeedUpdates() {
        return getFeedUpdates(25, null);
    }

    public static int getFeedUpdates(String[] feedIds) {
        return getFeedUpdates(25, feedIds);
    }

    public static int getFeedUpdates(int numEntries, String[] feedIds) {
        HashMap feeds = new HashMap();

        HashMap feedRecords = getActiveFeeds(feedIds);

        feeds = retrieveUpdatedFeeds(feedRecords);


        HashMap feedSubs = getFeedSubscribers(feeds);


        Iterator<String> feedIdIter = feedSubs.keySet().iterator();
        while (feedIdIter.hasNext()) {
            String tmpFeedId = (String) feedIdIter.next();
            ArrayList fdSubs = (ArrayList) feedSubs.get(tmpFeedId);
            SyndFeed feed = (SyndFeed) feeds.get(tmpFeedId);
            if (feed.getEntries().size() < numEntries) {
                numEntries = feed.getEntries().size();
            }
            ListIterator<SyndEntry> entries = feed.getEntries().listIterator(numEntries);
            int itemCounter = numEntries - 1;
            while (entries.hasPrevious()) {
                SyndEntry currentEntry = (SyndEntry) entries.previous();
                String msg = currentEntry.getDescription().getValue();
                Date pubDate = currentEntry.getPublishedDate();
                List categories = currentEntry.getCategories();
                String author = currentEntry.getAuthor();
                String title = currentEntry.getTitle();
                String con_url = currentEntry.getLink();
                String imageURL = "";
                try {
                    ArrayList<Element> foreign = (ArrayList) currentEntry.getForeignMarkup();
                    Element attrib = (Element) foreign.get(1);
                    AttributeList at = ((Element) ((ArrayList) currentEntry.getForeignMarkup()).get(0)).getAttribute("attributes");
                    Vector<?> v = (Vector) at.getValues();
                    imageURL = v.get(v.indexOf("url")).toString();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace(System.out);
                }
                System.out.println("this is image url---------------------->" + imageURL);

                String categoryStr = "";
                if (categories != null) {
                    for (int i = 0; i < categories.size(); i++) {
                        SyndCategory cat = (SyndCategory) categories.get(i);
                        if (categoryStr.equals("")) {
                            categoryStr = categoryStr + extractCategories(feed.getLink(), cat);
                        } else {
                            categoryStr = categoryStr + "," + extractCategories(feed.getLink(), cat);
                        }
                    }
                }
                int html_start_location = msg.indexOf("<img width");
                msg = msg.substring(0, html_start_location == -1 ? msg.length() : html_start_location);

                msg = msg.replaceAll("<b>", "");
                msg = msg.replaceAll("</b>", "");
                msg = msg.replaceAll("<i>", "");
                msg = msg.replaceAll("</i>", "");

                msg = msg.replaceAll("&lt;p&gt;", "");
                msg = msg.replaceAll("&lt;/p&gt;", "");
                msg = msg.replaceAll("&lt;b&gt;", "");
                msg = msg.replaceAll("&lt;/b&gt;", "");
                msg = msg.replaceAll("&lt;i&gt;", "");
                msg = msg.replaceAll("&lt;/i&gt;", "");


                msg = msg.replaceAll("“", "\"");
                msg = msg.replaceAll("”", "\"");
                if (msg.length() > 0) {
                    for (int i = 0; i < fdSubs.size(); i++) {
                        CPUserFeeds afeed = (CPUserFeeds) fdSubs.get(i);

                        String accountId = afeed.getCpUserId();
                        String keyword = afeed.getKeyword();
                        String regexReject = afeed.getRegexReject() == null ? "" : afeed.getRegexReject();
                        int allowedAge = afeed.getAllowedAge();
                        int msgDlrPriority = afeed.getMsgDlrPrority();

                        UserService service = new UserService();
                        try {
                            service = ServiceManager.viewService(keyword, accountId);
                        } catch (Exception e) {
                            System.out.println(new Date() + ": " + keyword + ": " + accountId + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: " + "Did not find matching service. May not be able to transmit updates");
                        }
                        String[] regexes = regexReject.split("::");
                        int rejectFlag = 0;
                        for (int j = 0; j < regexes.length; j++) {
                            String expr = regexes[j];
                            if (!expr.equals("")) {
                                Pattern pattern = Pattern.compile(expr);
                                Matcher matcher = pattern.matcher(msg);
                                if (matcher.find()) {
                                    System.out.println(new Date() + ": " + keyword + ": " + accountId + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: " + "regex_reject pattern matched. Reject item: " + msg);
                                    rejectFlag = 1;
                                    break;
                                }
                            }
                        }
                        if (rejectFlag == 0) {
                            InfoServiceDB db = new InfoServiceDB();
                            Date date = new Date();
                            try {
                                int contentUpdateCheck = 0;
                                String messageRef = uidGen.generateSecureUID();
                                if (allowedAge > 0) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.add(10, -1 * allowedAge);
                                    Date allowedStartTime = cal.getTime();
                                    if (pubDate == null) {
                                        System.out.println(new Date() + ": " + keyword + ": " + accountId + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: " + "Can't apply filtering by allowed_age because content doesn't have timestamp. Will add anyway");
                                        contentUpdateCheck = db.createInfoServiceEntryWithTags(date, keyword, msg, accountId, "RSS", imageURL, title, con_url, author, categoryStr, messageRef, 160);
                                    } else if (pubDate.after(allowedStartTime)) {
                                        contentUpdateCheck = db.createInfoServiceEntryWithTags(date, keyword, msg, accountId, "RSS", imageURL, title, con_url, author, categoryStr, messageRef, 160);
                                    } else {
                                        contentUpdateCheck = -1;
                                    }
                                } else {
                                    contentUpdateCheck = db.createInfoServiceEntryWithTags(date, keyword, msg, accountId, "RSS", imageURL, title, con_url, author, categoryStr, messageRef, 160);
                                }
                                if (contentUpdateCheck == -1) {
                                    System.out.println(new Date() + ": " + keyword + ": " + accountId + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: content is old. feed item rejected");
                                } else if (contentUpdateCheck == 1) {
                                    System.out.println(new Date() + ": " + keyword + ": " + accountId + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: content length exceeds 160 chars. feed item rejected");
                                } else if (contentUpdateCheck == 2) {
                                    System.out.println(new Date() + ": " + keyword + ": " + accountId + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: duplicate content. feed item rejected. content: " + msg);
                                } else if (new String("" + msgDlrPriority).equals("2")) {
                                    System.out.println("delivering content now ... " + messageRef);
                                    new Thread(new ContentDispatcher(date, service, messageRef)).start();
                                } else if ((new String("" + msgDlrPriority).equals("1")) && (itemCounter == 0)) {
                                    System.out.println("delivering last, most recent content now ... " + messageRef);
                                    new Thread(new ContentDispatcher(date, service, messageRef)).start();
                                } else if (new String("" + msgDlrPriority).equals("0")) {
                                    System.out.println("content stored! user can pick it up whenever ... " + messageRef);
                                } else {
                                    System.out.println("content stored! user can pick it up whenever ... " + messageRef);
                                }
                            } catch (Exception ex) {
                                System.out.println(new Date() + " Create Info Service Entry unable to create. : " + "Error Message:" + ex.getMessage());
                                ex.printStackTrace();
                            }
                        }
                    }
                } else {
                    System.out.println(new Date() + ": @ com.rancard.mobility.infoserver.feeds.FeedReader: blank content. feed item rejected for feedId: " + tmpFeedId);
                }
                itemCounter--;
                if (itemCounter < 0) {
                    break;
                }
            }
        }
        return 0;
    }

    private static HashMap getActiveFeeds(String[] feedIds) {
        String feedIdString = "";
        if ((feedIds != null) && (feedIds.length > 0)) {
            feedIdString = "'" + feedIds[0].trim() + "'";
            for (int i = 1; i < feedIds.length; i++) {
                feedIdString = feedIdString + ",'" + feedIds[i] + "'";
            }
        }
        PreparedStatement prepstat = null;
        ResultSet rs = null;
        Connection con = null;

        HashMap feeds = new HashMap();
        try {
            String SQL = "select * from feeds where is_active=1";
            if (!feedIdString.equals("")) {
                SQL = "select * from feeds where is_active=1 and feed_id in (" + feedIdString + ")";
            }
            con = DConnect.getConnection();
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                URL feedUrl = new URL(rs.getString("feed_url"));
                String id = rs.getString("feed_id");
                String name = rs.getString("feed_name");
                String is_active = rs.getString("is_active");
                String username = rs.getString("username") == null ? "" : rs.getString("username");
                String password = rs.getString("password") == null ? "" : rs.getString("password");

                Feed currentItem = new Feed();
                currentItem.setFeedId(id);
                currentItem.setFeedName(name);
                currentItem.setFeedURL(feedUrl);
                currentItem.setUsername(username);
                currentItem.setPassword(password);
                if (is_active.equals("0")) {
                    currentItem.setActive(false);
                } else if (is_active.equals("1")) {
                    currentItem.setActive(true);
                }
                feeds.put(id, currentItem);
            }
        } catch (Exception ex) {
            System.out.println(new Date() + " ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
        return feeds;
    }

    private static HashMap getFeedSubscribers(HashMap feedIds) {
        HashMap feedUsers = new HashMap();
        PreparedStatement prepstat = null;
        ResultSet rs = null;
        Connection con = null;
        int messagesUpdated = 0;

        String ids = "(";
        if (!feedIds.keySet().isEmpty()) {
            Iterator<String> feedIdIter = feedIds.keySet().iterator();
            while (feedIdIter.hasNext()) {
                String feedId = (String) feedIdIter.next();
                ids = ids + feedId;
                if (feedIdIter.hasNext()) {
                    ids = ids + ",";
                }
                feedUsers.put(feedId, new ArrayList());
            }
            ids = ids + ")";

            ArrayList userfeeds = new ArrayList();
            try {
                String SQL = "select * from cp_user_feeds WHERE feed_id IN " + ids + "  Order by feed_id";
                con = DConnect.getConnection();
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();
                while (rs.next()) {
                    CPUserFeeds cpfeed = new CPUserFeeds();
                    String accountId = rs.getString("account_id");
                    String keyword = rs.getString("keyword");
                    String feedId = rs.getString("feed_id");
                    int allowedAge = rs.getInt("allowed_age");
                    String regexReject = rs.getString("regex_reject");
                    int msgDlrPriority = rs.getInt("msg_dlr_priority");

                    ArrayList tmpFeeds = (ArrayList) feedUsers.get(feedId);
                    cpfeed.setFeedId(feedId);
                    cpfeed.setCpUserId(accountId);
                    cpfeed.setKeyword(keyword);
                    cpfeed.setAllowedAge(allowedAge);
                    cpfeed.setRegexReject(regexReject);
                    cpfeed.setMsgDlrPrority(msgDlrPriority);

                    tmpFeeds.add(cpfeed);
                    feedUsers.put(feedId, tmpFeeds);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR: " + ex.getMessage());
            } finally {
                try {
                    if (prepstat != null) {
                        prepstat.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return feedUsers;
    }

    private static HashMap retrieveUpdatedFeeds(HashMap feedRecords) {
        HashMap feeds = new HashMap();

        Iterator<String> entries = feedRecords.keySet().iterator();
        while (entries.hasNext()) {
            String feedId = (String) entries.next();
            Feed currentFeed = (Feed) feedRecords.get(feedId);
            URL inputUrl = currentFeed.getFeedURL();
            String username = currentFeed.getUsername();
            String password = currentFeed.getPassword();
            FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.getInstance();
            FeedFetcher fetcher;

            if ((username != null) && (!username.equals("")) && (password != null) && (!password.equals(""))) {
                AuthCredentialSupplier authCredentials = new AuthCredentialSupplier(username, password);
                fetcher = new HttpClientFeedFetcher(feedInfoCache, authCredentials);
            } else {
                fetcher = new HttpClientFeedFetcher(feedInfoCache);
            }
            System.err.println("Retrieving feed " + inputUrl.toExternalForm());
            try {
                SyndFeed feed = fetcher.retrieveFeed(inputUrl);

                System.err.println(new Date() + " " + inputUrl + " retrieved");
                System.err.println(new Date() + " " + inputUrl + " has a title: " + feed.getTitle() + " and contains " + feed.getEntries().size() + " entries.");
                if ((feed != null) && (!feed.getEntries().isEmpty())) {
                    if (StringUtils.isNotBlank(feed.getTitle()) || feed.getTitle().startsWith("VIDEO:")) {
                        feeds.put(feedId, feed);
                    }
                }
            } catch (IllegalArgumentException ex) {
                System.err.println(new Date() + " Illegal Argument . Likely an invalid URL");
                ex.printStackTrace();
            } catch (FeedException ex) {
                ex.printStackTrace();
            } catch (FetcherException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                System.err.println(new Date() + " Unknown Host : lets try the next URL");
                ex.printStackTrace();
            }
        }
        return feeds;
    }

    static class FetcherEventListenerImpl
            implements FetcherListener {
        public void fetcherEvent(FetcherEvent event) {
            String eventType = event.getEventType();
            if ("FEED_POLLED".equals(eventType)) {
                System.err.println("\tEVENT: Feed Polled. URL = " + event.getUrlString());
            } else if ("FEED_RETRIEVED".equals(eventType)) {
                System.err.println("\tEVENT: Feed Retrieved. URL = " + event.getUrlString());
            } else if ("FEED_UNCHANGED".equals(eventType)) {
                System.err.println("\tEVENT: Feed Unchanged. URL = " + event.getUrlString());
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Short description";
    }
}


 





  


























 