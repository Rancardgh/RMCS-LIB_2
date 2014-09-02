

package com.rancard.mobility.infoserver.livescore;

import com.rancard.common.DConnect;
import com.rancard.common.uidGen;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.util.PaginatedList;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public abstract class LiveScoreServiceDB {
    public static void createLiveScoreService(LiveScoreService service)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);


            String SQL = "select * from service_definition where keyword='" + service.getKeyword() + "' and account_id='" + service.getAccountId() + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (!rs.next()) {
                SQL = "insert into service_definition(service_type,keyword,account_id,service_name,default_message,is_basic,last_updated,command) values(?,?,?,?,?,?,?,?)";

                prepstat = con.prepareStatement(SQL);

                prepstat.setString(1, service.getServiceType());
                prepstat.setString(2, service.getKeyword());
                prepstat.setString(3, service.getAccountId());
                prepstat.setString(4, service.getServiceName());
                prepstat.setString(5, service.getDefaultMessage());
                if (service.isBasic()) {
                    prepstat.setInt(6, 1);
                } else {
                    prepstat.setInt(6, 0);
                }
                prepstat.setString(7, service.getLastUpdated());
                prepstat.setString(8, service.getCommand());
                prepstat.execute();
            }
            SQL = "select * from keyword_mapping where keyword='" + service.getKeyword() + "' and account_id='" + service.getAccountId() + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (!rs.next()) {
                SQL = "insert into keyword_mapping (keyword,account_id,mapping) values(?,?,?)";
                prepstat = con.prepareStatement(SQL);
                prepstat.clearBatch();

                String[] serviceIds = service.getLiveScoreServiceIds();
                for (int i = 0; i < serviceIds.length; i++) {
                    prepstat.setString(1, service.getKeyword());
                    prepstat.setString(2, service.getAccountId());
                    prepstat.setString(3, serviceIds[i]);
                    prepstat.addBatch();
                }
                prepstat.executeBatch();
            }
        } catch (Exception ex) {
            bError = true;
            try {
                deleteLiveScoreService(service.getKeyword(), service.getAccountId());
            } catch (Exception ee) {
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (bError) {
                con.rollback();
            } else {
                con.commit();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public static void updateLiveScoreServiceName(String keyword, String account_id, String serviceName)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);


            String SQL = "update service_definition set service_name=?,last_updated=? where keyword=? and account_id=?";

            prepstat = con.prepareStatement(SQL);


            prepstat.setString(1, serviceName);
            prepstat.setTimestamp(2, new Timestamp(Calendar.getInstance().getTime().getTime()));
            prepstat.setString(3, keyword);
            prepstat.setString(4, account_id);

            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            bError = true;
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static void updateLiveScoreServiceMessage(String keyword, String account_id, String defaultMessage)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean bError = false;
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);


            String SQL = "update service_definition set default_message=?,last_updated=? where keyword=? and account_id=?";

            prepstat = con.prepareStatement(SQL);


            prepstat.setString(1, defaultMessage);
            prepstat.setTimestamp(2, new Timestamp(Calendar.getInstance().getTime().getTime()));
            prepstat.setString(3, keyword);
            prepstat.setString(4, account_id);

            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            bError = true;
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static LiveScoreService viewLiveScoreService(String keyword, String accountId)
            throws Exception {
        LiveScoreService service = new LiveScoreService();


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String SQL = "SELECT * FROM service_definition s inner join keyword_mapping k on s.keyword=k.keyword and s.account_id=k.account_id where s.keyword='" + keyword + "' and s.account_id='" + accountId + "'";


            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();


            rs.last();
            int numResults = rs.getRow();
            rs.beforeFirst();

            int i = 0;
            String[] mappings = new String[numResults];
            while (rs.next()) {
                service.setAccountId(rs.getString("s.account_id"));
                service.setDefaultMessage(rs.getString("s.default_message"));
                service.setKeyword(rs.getString("s.keyword"));
                service.setLastUpdated(rs.getString("s.last_updated"));
                mappings[i] = rs.getString("k.mapping");
                service.setServiceName(rs.getString("s.service_name"));
                service.setServiceType(rs.getString("s.service_type"));
                i++;
            }
            service.setLiveScoreServiceIds(mappings);
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return service;
    }

    public static ArrayList viewAllLiveScoreLeagues(String accountId)
            throws Exception {
        ArrayList services = new ArrayList();


        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat2 = null;
        try {
            con = DConnect.getConnection();

            String SQL = "select keyword from service_definition where account_id='" + accountId + "' and service_type=15 and command=2";

            PreparedStatement prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                LiveScoreService service = new LiveScoreService();

                SQL = "SELECT * FROM service_definition s inner join keyword_mapping k on s.keyword=k.keyword and s.account_id=k.account_id where s.keyword='" + rs.getString("keyword") + "' and s.account_id='" + accountId + "'";

                prepstat = con.prepareStatement(SQL);
                rs2 = prepstat.executeQuery();


                rs2.last();
                int numResults = rs2.getRow();
                rs2.beforeFirst();

                int i = 0;
                String[] mappings = new String[numResults];
                while (rs2.next()) {
                    service.setAccountId(rs2.getString("s.account_id"));
                    service.setDefaultMessage(rs2.getString("s.default_message"));
                    service.setKeyword(rs2.getString("s.keyword"));
                    service.setLastUpdated(rs2.getString("s.last_updated"));
                    mappings[i] = rs2.getString("k.mapping");
                    service.setServiceName(rs2.getString("s.service_name"));
                    service.setServiceType(rs2.getString("s.service_type"));
                    i++;
                }
                service.setLiveScoreServiceIds(mappings);

                services.add(service);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return services;
    }

    public static void deleteLiveScoreService(String keyword, String account_id)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();


            String SQL = "delete from service_definition, keyword_mapping, service_subscription where keyword='" + keyword + "' and account_id='" + account_id + "'";
            prepstat = con.prepareStatement(SQL);
            prepstat.execute();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static ArrayList getAccount_KeywordPairForService(String liveScoreServiceID)
            throws Exception {
        ArrayList account_keyword_pairs = new ArrayList();


        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat2 = null;
        try {
            con = DConnect.getConnection();

            String SQL = "SELECT s.account_id,s.keyword FROM service_definition s inner join keyword_mapping k on s.keyword=k.keyword and s.account_id=k.account_id where k.mapping='" + liveScoreServiceID + "'";


            PreparedStatement prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                String account_keyword_pair = new String();
                account_keyword_pair = rs.getString("s.account_id") + "-" + rs.getString("s.keyword");
                account_keyword_pairs.add(account_keyword_pair);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return account_keyword_pairs;
    }

    public static String getKeywordForService(String liveScoreServiceID, String accountId)
            throws Exception {
        ArrayList account_keyword_pairs = new ArrayList();


        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat2 = null;
        String keyword = new String();
        try {
            con = DConnect.getConnection();

            String SQL = "SELECT s.keyword FROM service_definition s inner join keyword_mapping k on s.keyword=k.keyword and s.account_id=k.account_id where k.mapping='" + liveScoreServiceID + "' and s.account_id='" + accountId + "'";


            PreparedStatement prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                keyword = rs.getString("s.keyword");
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return keyword;
    }

    public static String[][] getAvailableLiveScoreServices()
            throws Exception {
        String[][] struct = (String[][]) null;

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String query = "SELECT * FROM livescore_services";
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            struct = new String[rowCount][2];
            rs.beforeFirst();
            rowCount = 0;
            while (rs.next()) {
                struct[rowCount][0] = rs.getString("league_id");
                struct[rowCount][1] = rs.getString("league_name");
                rowCount++;
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return struct;
    }

    public static int getBillingTypeForSubscriber(String msisdn, String accountId)
            throws Exception {
        int billingType = 0;


        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "select billing_type from service_subscription where account_id='" + accountId + "' and msisdn='" + msisdn + "'" + " and keyword in(select keyword from service_definition where account_id='" + accountId + "' and command='2' and is_basic='1') " + "and status='1'";


            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (rs.next()) {
            }
            return rs.getInt("billing_type");
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
            return billingType;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
    }

    public static HashMap viewSubscribersForGame(String accountId, String keyword, int status)
            throws Exception {
        HashMap groupedSubscribers = new HashMap();


        ResultSet networks = null;
        ResultSet subs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();


            String query = "select allowed_networks from cp_connections where list_id='" + accountId + "'";
            prepstat = con.prepareStatement(query);
            networks = prepstat.executeQuery();
            while (networks.next()) {
                String prefix = new String();
                prefix = networks.getString("allowed_networks");


                query = "select ab.msisdn from address_book ab inner join service_subscription ss on ab.msisdn=ss.msisdn and ab.account_id=ss.account_id where ab.account_id='" + accountId + "' and ss.keyword='" + keyword + "' and ss.status=" + status + " and ss.msisdn like '+" + prefix + "%'";

                prepstat = con.prepareStatement(query);
                subs = prepstat.executeQuery();


                subs.last();
                int numResults = subs.getRow();
                String[] subscribers = new String[numResults];
                subs.beforeFirst();


                int count = 0;
                while (subs.next()) {
                    subscribers[count] = subs.getString("ab.msisdn");
                    count++;
                }
                groupedSubscribers.put(prefix, subscribers);
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return groupedSubscribers;
    }

    public static ArrayList viewSubscribersForGame(String accountId, String game_id, String alias)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        LiveScoreFixtureSubscriber lfs = null;
        ArrayList subs = new ArrayList();
        try {
            con = DConnect.getConnection();


            String query = "select * from service_subscription where account_id='" + accountId + "' and keyword='" + game_id + "' " + "order by subscription_date desc";


            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                lfs = new LiveScoreFixtureSubscriber();
                lfs.setMsisdn(rs.getString("msisdn"));
                lfs.setDate(rs.getTimestamp("subscription_date").toString());
                lfs.setStatus("" + rs.getInt("status") + "");

                subs.add(lfs);
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return subs;
    }

    public static PaginatedList viewSubscribersForGame(String accountId, String game_id, String alias, int[] pagingParams, String[] sortParams)
            throws Exception {
        String sort_order = sortParams[1];
        String sort_criterion = sortParams[0];
        int totalNumResults = 0;
        int currPage = pagingParams[0];
        int pageSize = pagingParams[1];
        PaginatedList pl = null;

        String orderByCondition = " order by msisdn ";

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        LiveScoreFixtureSubscriber lfs = null;
        ArrayList subs = new ArrayList();
        try {
            con = DConnect.getConnection();


            String query = "select * from service_subscription where account_id=? and keyword=?";

            String queryCount = "select count(*) as totalResults from service_subscription where account_id=? and keyword=?";
            if ((alias != null) && (alias != "")) {
                query = query + " or keyword='" + alias + "'";
                queryCount = queryCount + " or keyword='" + alias + "'";
            }
            if (!"".equals(sort_criterion)) {
                orderByCondition = " order by " + sort_criterion;
            }
            query = query + orderByCondition + " " + sort_order;


            prepstat = con.prepareStatement(queryCount);

            prepstat.setString(1, accountId);
            prepstat.setString(2, game_id);


            rs = prepstat.executeQuery();
            while (rs.next()) {
                totalNumResults = rs.getInt("totalResults");
            }
            prepstat = con.prepareStatement(query);

            prepstat.setString(1, accountId);
            prepstat.setString(2, game_id);


            rs = prepstat.executeQuery();


            int start = PaginatedList.getStartOfNextPage(currPage, pageSize);
            int i = 0;
            int totalNumPages = 0;
            while ((i < start + pageSize) && (rs.next())) {
                if (i == 0) {
                    totalNumPages = totalNumResults / pageSize;
                    if (totalNumResults % pageSize > 0) {
                        totalNumPages++;
                    }
                }
                if (i >= start) {
                    lfs = new LiveScoreFixtureSubscriber();
                    lfs.setMsisdn(rs.getString("msisdn"));
                    lfs.setDate(rs.getTimestamp("subscription_date").toString());
                    lfs.setStatus("" + rs.getInt("status") + "");

                    subs.add(lfs);
                }
                i++;
            }
            pl = new PaginatedList(subs, totalNumResults, pageSize, currPage, sort_order, sort_criterion);
            if (pl == null) {
                pl = (PaginatedList) PaginatedList.EMPTY_PAGE;
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return pl;
    }

    public static int viewNoSubscribersForGame(String accountId, String game_id)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        int no_subs = 0;
        try {
            con = DConnect.getConnection();


            String query = "select * from livescore_fixture_mgt where account_id=? and game_id=?";


            prepstat = con.prepareStatement(query);

            prepstat.setString(1, accountId);
            prepstat.setString(2, game_id);


            rs = prepstat.executeQuery();
            while (rs.next()) {
                no_subs = rs.getInt("no_subscribers");
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return no_subs;
    }

    public static String[][] getAvailableLiveScoreServices(String accountId)
            throws Exception {
        String[][] struct = (String[][]) null;

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String query = "select * from livescore_services ls inner join keyword_mapping km on ls.league_id=km.mapping inner join service_definition sd on km.account_id=sd.account_id and km.keyword=sd.keyword where km.account_id='" + accountId + "'";

            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            struct = new String[rowCount][2];
            rs.beforeFirst();
            rowCount = 0;
            while (rs.next()) {
                struct[rowCount][0] = rs.getString("keyword");
                struct[rowCount][1] = rs.getString("service_name");
                rowCount++;
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        }
        if (con != null) {
            con.close();
        }
        return struct;
    }

    public static boolean canAccommodateSubscriber(String accountId, String msisdn, LiveScoreFixture game)
            throws Exception {
        boolean canAccommodateSubscriber = false;

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String query = "select max_subs_count from keyword_mapping where account_id='" + accountId + "' and mapping='" + game.getLeagueId() + "'";
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            String value = "";
            if (rs.next()) {
                value = rs.getString("max_subs_count");
            }
            if ((value != null) && (!value.equals(""))) {
                int limit = Integer.parseInt(value);
                String keywordVals = "";

                query = "select count(*) as subs_count from service_subscription where account_id='" + accountId + "' and (keyword='" + game.getGameId() + "' or keyword='" + game.getAlias() + "')";

                prepstat = con.prepareStatement(query);
                rs = prepstat.executeQuery();

                int curr_count = 0;
                if (rs.next()) {
                    curr_count = rs.getInt("subs_count");
                }
                if (curr_count + 1 <= limit) {
                    canAccommodateSubscriber = true;
                } else {
                    canAccommodateSubscriber = false;
                }
            }
            return true;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            return canAccommodateSubscriber;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static boolean isRegisteredToLiveScoreServices(String accountId, String msisdn)
            throws Exception {
        boolean isRegisteredToLiveScoreServices = false;

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String query = "select * from service_subscription s where s.keyword in (select d.keyword from service_definition d where d.is_basic=1 and d.account_id='" + accountId + "' and d.service_type=15 and command=2) and msisdn='" + msisdn + "' and s.account_id='" + accountId + "'";

            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            String value = "";
            if (rs.next()) {
                isRegisteredToLiveScoreServices = true;
            }
            return false;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            return isRegisteredToLiveScoreServices;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static boolean isRegisteredToLiveScoreServices(String accountId, String msisdn, int status)
            throws Exception {
        boolean isRegisteredToLiveScoreServices = false;

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String query = "select * from service_subscription s where s.keyword in (select d.keyword from service_definition d where d.is_basic=1 and d.account_id='" + accountId + "' and d.service_type=15 and command=2) and s.msisdn='" + msisdn + "' and s.account_id='" + accountId + "' and s.status='" + status + "'";

            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            String value = "";
            if (rs.next()) {
                isRegisteredToLiveScoreServices = true;
            }
            return false;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            return isRegisteredToLiveScoreServices;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static String[] subscribeToLiveScoreServices(String accountId, String msisdn, int numOfDays, int billingType)
            throws Exception {
        String[] regId = new String[2];
        int error = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        boolean failedCheck = false;

        Calendar c = Calendar.getInstance();
        c.add(5, numOfDays);
        String nextSubDate = new java.sql.Date(c.getTimeInMillis()).toString();


        nextSubDate = billingType == 1 ? "" : nextSubDate;
        try {
            con = DConnect.getConnection();
            String SQL = "select * from address_book where account_id='" + accountId + "' and msisdn='" + msisdn + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            if (!rs.next()) {
                SQL = "Insert into address_book (account_id,msisdn,registration_id) values(?,?,?)";
                prepstat = con.prepareStatement(SQL);
                regId[0] = uidGen.generateNumberID(6);
                prepstat.setString(1, accountId);
                prepstat.setString(2, msisdn);
                prepstat.setString(3, regId[0]);
                prepstat.execute();
            } else {
                regId[0] = rs.getString("registration_id");
            }
            SQL = "delete from service_subscription where keyword in (select d.keyword from service_definition d where d.is_basic=1 and d.account_id='" + accountId + "' and d.service_type=15 and d.command=2) and account_id='" + accountId + "' and msisdn='" + msisdn + "'";

            prepstat = con.prepareStatement(SQL);
            prepstat.execute();

            SQL = "select d.keyword from service_definition d where d.is_basic=1 and d.account_id='" + accountId + "' and d.service_type=15 and command=2";

            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                SQL = "Insert into service_subscription (subscription_date,msisdn,keyword,account_id,status,next_subscription_date,billing_type) values(?,?,?,?,?,?,?)";
                prepstat = con.prepareStatement(SQL);
                prepstat.setTimestamp(1, new Timestamp(Calendar.getInstance().getTime().getTime()));
                prepstat.setString(2, msisdn);
                prepstat.setString(3, rs.getString("d.keyword"));
                prepstat.setString(4, accountId);
                prepstat.setInt(5, 1);
                prepstat.setString(6, nextSubDate);
                prepstat.setInt(7, billingType);

                prepstat.execute();
                regId[1] = nextSubDate;
            }
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
                con = null;
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
                con = null;
            }
        }
        return regId;
    }

    public static boolean isMultiShortCodeLivesScore(String accountId)
            throws Exception {
        boolean isTrue = false;

        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();

            String query = "select * from service_definition where account_id='" + accountId + "' and command in('01','02','03')";
            prepstat = con.prepareStatement(query);
            rs = prepstat.executeQuery();

            String value = "";
            if (rs.next()) {
                isTrue = true;
            }
            return false;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            return isTrue;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static String manageNextLivescoreSubscription(java.util.Date today)
            throws Exception {
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.livescore.LiveScoreServiceDB:manageNextLivescoreSubscription");


        Connection con = null;
        PreparedStatement prepstat = null;
        ResultSet serviceLevelRS = null;
        ResultSet subscriptionLevelRS = null;

        String processId = uidGen.getUId();
        String nextSubscriptionDate = new java.sql.Date(today.getTime()).toString();
        String keyword = "";
        String accountId = "";
        try {
            con = DConnect.getConnection();

            String SQL = "select keyword, account_id from service_definition where service_type=15 and command=9";
            prepstat = con.prepareStatement(SQL);
            serviceLevelRS = prepstat.executeQuery();
            while (serviceLevelRS.next()) {
                keyword = serviceLevelRS.getString("keyword");
                accountId = serviceLevelRS.getString("account_id");

                SQL = "select distinct(msisdn) from service_subscription where keyword in (select d.keyword from service_definition d where d.is_basic=1 and d.account_id='" + accountId + "' and d.service_type=15 and d.command=2) and account_id='" + accountId + "' and next_subscription_date='" + nextSubscriptionDate + "'" + " and billing_type='" + 2 + "'";


                prepstat = con.prepareStatement(SQL);
                subscriptionLevelRS = prepstat.executeQuery();

                SQL = "delete from service_subscription where keyword in (select d.keyword from service_definition d where d.is_basic=1 and d.account_id='" + accountId + "' and d.service_type=15 and d.command=2) and account_id='" + accountId + "' and next_subscription_date='" + nextSubscriptionDate + "'" + " and billing_type='" + 2 + "'";


                prepstat = con.prepareStatement(SQL);
                prepstat.execute();
                while (subscriptionLevelRS.next()) {
                    SQL = "insert into temp_service_subscription (msisdn,keyword,account_id,process_id,date,billing_type) values (?,?,?,?,?,?)";
                    prepstat = con.prepareStatement(SQL);

                    prepstat.setString(1, subscriptionLevelRS.getString("msisdn"));
                    prepstat.setString(2, keyword);
                    prepstat.setString(3, accountId);
                    prepstat.setString(4, processId);
                    prepstat.setString(5, nextSubscriptionDate);
                    prepstat.setInt(6, 2);

                    prepstat.execute();
                }
            }
            System.out.println(new java.util.Date() + ":process manageNextLivescoreSubscription completed successfully!");
            return processId;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            System.out.println(new java.util.Date() + ":Error in method manageNextLivescoreSubscription:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static int manageNextLivescoreSubscription(java.util.Date today, String accountId)
            throws Exception {
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.livescore.LiveScoreServiceDB:manageNextLivescoreSubscription for account:" + accountId);


        Connection con = null;
        PreparedStatement prepstat = null;
        int status = 0;


        String nextSubscriptionDate = new java.sql.Date(today.getTime()).toString();
        try {
            con = DConnect.getConnection();
            String SQL = "update service_subscription set status='0' where keyword in (select d.keyword from service_definition d where d.is_basic=1 and d.account_id='" + accountId + "' and d.service_type=15 and d.command=2) and account_id='" + accountId + "' and next_subscription_date='" + nextSubscriptionDate + "'" + " and billing_type='" + 2 + "' and status='1'";
            prepstat = con.prepareStatement(SQL);
            status = prepstat.executeUpdate();
            System.out.println(new java.util.Date() + ":process manageNextLivescoreSubscription completed successfully:\n No. of records updated:" + status);

            return status;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            System.out.println(new java.util.Date() + ":Error in method manageNextLivescoreSubscription:" + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static UserService viewLiveScoreSubscriptionService(String accountId)
            throws Exception {
        Connection con = null;
        PreparedStatement prepstat = null;
        ResultSet rs = null;
        UserService service = new UserService();
        try {
            con = DConnect.getConnection();

            String SQL = "select * from service_definition where service_type='15' and command='9' and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                service.setKeyword(rs.getString("keyword"));
                service.setServiceType(rs.getString("service_type"));
                service.setAccountId(rs.getString("account_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setDefaultMessage(rs.getString("default_message"));
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));

                service.setPricing(rs.getString("pricing"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
            }
            return service;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public static UserService viewHeadLiveScoreService(String accountId)
            throws Exception {
        Connection con = null;
        PreparedStatement prepstat = null;
        ResultSet rs = null;
        UserService service = new UserService();
        try {
            con = DConnect.getConnection();

            String SQL = "select * from service_definition where service_type='15' and command='0' and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                service.setKeyword(rs.getString("keyword"));
                service.setServiceType(rs.getString("service_type"));
                service.setAccountId(rs.getString("account_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setDefaultMessage(rs.getString("default_message"));
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                service.setLastUpdated(publishTime);
                service.setCommand(rs.getString("command"));
                service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                service.setAllowedSiteTypes(rs.getString("allowed_site_types"));

                service.setPricing(rs.getString("pricing"));
                if (rs.getInt("is_basic") == 1) {
                    service.setIsBasic(true);
                } else {
                    service.setIsBasic(false);
                }
            }
            return service;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }
}
