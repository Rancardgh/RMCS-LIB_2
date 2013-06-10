/**
 *
 * @author nii
 * Created On: Thu, Jan 15, 2009
 * This class functions as a filter to register msisdn's as subscribers 
 * under the service_subscription table
 */
package com.rancard.mobility.contentserver.serviceinterfaces;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import com.rancard.common.Feedback;
import com.rancard.mobility.common.ThreadedMessageSender;
import com.rancard.mobility.common.Driver;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.common.services.ServiceManager;

public class processnewsubscription extends HttpServlet implements Filter {

    private FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String keyword = req.getParameter("keyword");
        String dest = req.getParameter("dest");
        String short_code = dest.contains("+") ? dest.substring(1) : dest;
        String msisdn = req.getParameter("msisdn");
        String account_id = (String) req.getAttribute("acctId");

        System.out.println(new java.util.Date() + ": " + keyword + ": " + msisdn + ": Entering processnewsubscription filter...");

        // Handle exceptions for VOA premium keywords
        String alt_keyword = "";
        if (keyword.equalsIgnoreCase("IN") && account_id.equals("005")) {
            alt_keyword = "VOI";
        } else if (keyword.equalsIgnoreCase("AN") && account_id.equals("005")) {
            alt_keyword = "VOA";
        }
        request.setAttribute("override_keyword", alt_keyword);

        com.rancard.mobility.infoserver.common.services.UserService srvc = null;
        try {
            //search for service
            srvc = com.rancard.mobility.infoserver.common.services.ServiceManager.viewService(keyword, account_id);

            if (srvc.isSubscription()) {
                // Subscription service, so continue as planned
                System.out.println(new java.util.Date() + ": " + keyword + ": " + msisdn + ": Keyword is a valid subscription service");

                String service_name = srvc.getServiceName();
                String service_keyword = srvc.getKeyword();

                ArrayList<String> sportsKeywords = new ArrayList<String>();
                sportsKeywords.add("FOOTBALL");
                sportsKeywords.add("FB");
                sportsKeywords.add("GF");
                sportsKeywords.add("SR");
                sportsKeywords.add ("ESPN");

                boolean isSubscribedToOrig = ServiceManager.isSubscribed(msisdn, account_id, service_keyword);
                boolean isSubscribedtoAlt = ServiceManager.isSubscribed(msisdn, account_id, alt_keyword);

                if (isSubscribedToOrig || isSubscribedtoAlt) {

                    if (isSubscribedtoAlt) {
                        service_keyword = alt_keyword;
                        System.out.println(new java.util.Date() + ": " + service_keyword + ": " + msisdn + ": Subscription keyword changed");
                    }

                    // Registration already exists
                    System.out.println(new java.util.Date() + ": " + service_keyword + ": " + msisdn + ": Subscription already exists in DB");
                    String unsubscribe_msg = "";
                    String response_sender = "";

                    if (account_id.equals("005")) {
                        if (short_code.equals("406") || sportsKeywords.contains(service_keyword.toUpperCase())) {
                            if (service_keyword.equalsIgnoreCase("SR")) {
                                unsubscribe_msg = "The latest " + service_name + " summaries for only GHC0.03 daily. To unsubscribe send STOP " + service_keyword.toUpperCase() + " to " + short_code;
                            } else {
                                unsubscribe_msg = "The latest Tigo LiveScore " + service_keyword.toUpperCase() + " news for only GHC0.0795 daily. To unsubscribe send STOP " + service_keyword.toUpperCase() + " to 406.";
                            }
                            response_sender = "406";
                        } else if (short_code.equals("801")) {
                            if (service_keyword.equalsIgnoreCase("VOI")) {
                                unsubscribe_msg = "The latest International News from Voice of America (VOA) for only GHC0.08 daily. To unsubscribe send STOP VOI to 801";
                            } else if (service_keyword.equalsIgnoreCase("VOA")) {
                                unsubscribe_msg = "The latest African News from Voice of America (VOA) for only GHC0.08 daily. To unsubscribe send STOP VOA to 801";
                            } else if (service_keyword.equalsIgnoreCase("BL") || service_keyword.equalsIgnoreCase("QU")) {
                                unsubscribe_msg = "The latest " + service_name + " summaries for only GHC0.03 daily. To unsubscribe send STOP " + service_keyword.toUpperCase() + " to " + short_code;
                            } else {
                                unsubscribe_msg = "The latest " + service_name + " summaries for only GHC0.0636 daily. To unsubscribe send STOP " + service_keyword.toUpperCase() + " to " + short_code;
                            }
                            response_sender = "801";
                        }
                    } else if (account_id.equals("049")) {
                        unsubscribe_msg = "The latest " + service_name + " summaries for only Le 90 daily. To unsubscribe send STOP " + service_keyword.toUpperCase() + " to " + short_code;
                    }
                    request.setAttribute("override_msg", unsubscribe_msg);
                    request.setAttribute("x-kannel-header-from", response_sender);
                } else {
                    // Create new registration
                    System.out.println(new java.util.Date() + ": " + service_keyword + ": " + msisdn + ": Subscription doesn't exist. Creating new subscription record...");

                    if (!alt_keyword.equals("")) {
                        service_keyword = alt_keyword;
                        System.out.println(new java.util.Date() + ": " + service_keyword + ": " + msisdn + ": Subscription keyword changed");
                    }

                    ArrayList<String> keywordList = new ArrayList<String>();
                    keywordList.add(service_keyword);

                    String welcome_msg = "";
                    //welcome/promotional/adverts on services --requires two parameters: keyword | promotional_message (welcome_message)
                    if (account_id.equals("005")) {
                        if (short_code.equals("406") || sportsKeywords.contains(service_keyword.toUpperCase())) {
                            welcome_msg = "You can subscribe to receive daily " + service_keyword.toUpperCase() + " updates from Ghanasoccernet.com at a cost of GHC0.0795 per day. To do so, reply to this message with the word MORE";
                        } else if (short_code.equals("801")) {
                            if (service_keyword.equalsIgnoreCase("VOI")) {
                                welcome_msg = "You can subscribe to receive daily International News updates from VOA at a cost of GHC0.08 per day. To do so, reply to this message with the word MORE";
                            } else if (service_keyword.equalsIgnoreCase("VOA")) {
                                welcome_msg = "You can subscribe to receive daily African News updates from VOA at a cost of GHC0.08 per day. To do so, reply to this message with the word MORE";
                            } else {
                                welcome_msg = "You can subscribe to receive daily " + service_name + " updates at a cost of GHC0.0636 per day. To do so, reply to this message with the word MORE";
                            }
                        }

                        // Update to enable promotion messages on non-subscription services -- adserver function (adserver expected to inject inventory on any message)
                        List<String> tempNonSubscriptionKeywords = Arrays.asList("gtv", "tv3", "welcomemet", "ta", "usd", "tva",
                                "dst", "te", "gia", "gbp", "eur", "aa", "cfa", "nn", "kl", "gse", "ad", "sa", "lh", "az",
                                "kq", "tn", "ttv", "et", "sla", "as", "ms", "af", "naa", "mea", "ai", "be", "ae", "tm",
                                "td", "ba", "em", "va", "we");
                        ArrayList<String> nonSubscriptionKeywords = new ArrayList<String>(tempNonSubscriptionKeywords);

                        if (nonSubscriptionKeywords.contains(service_keyword.toLowerCase())) {
                            welcome_msg = "To get the latest international news from the Voice of America (VOA) simply text INEWS to 801 and get daily news updates for only GHc0.08!";
                            short_code = "801";
                        }

                        // Promote BBC NEWS on USD and LN
                        if (service_keyword.equalsIgnoreCase("USD") || service_keyword.equalsIgnoreCase("LN") ) {
                            welcome_msg = "Get BBC News any where you are on your phone! Simply text NEWS to 1988 and get daily updates for only GHc0.10!";
                            short_code = "1988";
                        }

                        // Promote BIBLE on BT and BL
                        if (service_keyword.equalsIgnoreCase("BT") || service_keyword.equalsIgnoreCase("BL")) {
                            welcome_msg = "Are you downcast and need spiritual motivation? Simply text WORD to 801 and get daily Bible verses for only GHc0.0636!";
                            short_code = "801";
                        }

                        // Promote CELEB on CN
                        if (service_keyword.equalsIgnoreCase("CN")) {
                            welcome_msg = "Do you want to know what your favorite celebrities have been up to? Simply text CELEB  to 801 and get daily updates for only GHc0.0636!";
                            short_code = "801";
                        }

                        // Promote FBALL on GTV,TV3,MET,DST,TE,FB,BA and LS
                        if (service_keyword.equalsIgnoreCase("GTV") || service_keyword.equalsIgnoreCase("TV3") || service_keyword.equalsIgnoreCase("MET") || service_keyword.equalsIgnoreCase("DST") || service_keyword.equalsIgnoreCase("TE") || service_keyword.equalsIgnoreCase("FB") || service_keyword.equalsIgnoreCase("BA") || service_keyword.equalsIgnoreCase("IS")) {
                            welcome_msg = "Get BBC Sports any where you are on your phone! Simply text FBALL to 1988 and get daily updates for only GHc0.10!";
                            short_code = "1988";
                        }

                        // Promote JOKES on JO
                        if (service_keyword.equalsIgnoreCase("JO")) {
                            welcome_msg = "Do you want to hear something funny? Simply text JOKE to 801 and get daily jokes for only GHc0.0636!";
                            short_code = "801";
                        }

                        // Promote MUSIC on MN
                        if (service_keyword.equalsIgnoreCase("MN")) {
                            welcome_msg = "Do you want fresh local and international music news delivered straight to your phone? Simply text MUSIC to 801 and get daily updates for only GHc0.0636!";
                            short_code = "801";
                        }

                        if (service_keyword.equalsIgnoreCase ("ESPN")) {
                            welcome_msg = "You're now subscribed to the ESPN service on Tigo! You'll get sports news from ESPN for only GHp 8 daily! To unsubscribe, send STOP ESPN to 406!";
                            short_code = "406";

                            ServiceManager.subscribeToService(msisdn, keywordList, account_id, 1, 1, 1);
                        }

                        //Actually add record to service_subscription table
                        //Update: Do not add user to table. This is done by sending in MORE
                        //ServiceManager.subscribeToService(msisdn, keywordList, account_id, 1, 1, 1);  
                    } else if (account_id.equals("000")) {
                        //In this case, the welcome_message is actually a promotional message encouraging subscripiton                       
                        welcome_msg = "Get BBC Sports any where you are on your phone! Simply text FBALL to 1988 and get daily updates for only GHc0.10!";
                        short_code = "1988";
                    }

                    // Push notification message
                    CPConnections cnxn = new CPConnections();
                    if (account_id.equals("005")) {
                        // Use driver-specific connection lookup
                        cnxn = CPConnections.getConnection(account_id, msisdn, "kannel");

                        // Spawn off new thread to do this while we just carry on with our lives
                        (new Thread(new ThreadedMessageSender(cnxn, msisdn, short_code, welcome_msg, "", 45000))).start();

                    } else {
                        cnxn = CPConnections.getConnection(account_id, msisdn);

                        // Spawn off new thread to do this while we just carry on with our lives
                        (new Thread(new ThreadedMessageSender(cnxn, msisdn, short_code, welcome_msg, 45000))).start();
                    }

                }

            } else {
                // Not a subscription service. Do nothing   
                System.out.println(new java.util.Date() + ": " + keyword + ": " + msisdn + ": Not a valid subscription service");
            }
        } catch (Exception e) {
            //log statement
            System.out.println(new java.util.Date() + ": " + keyword + ": " + msisdn + ": error @ processnewsubscription: " + e.getMessage());
        }

        System.out.println(new java.util.Date() + ": " + keyword + ": " + msisdn + ": Exiting processnewsubscription filter...");
        filterChain.doFilter(req, res);
    }
}
