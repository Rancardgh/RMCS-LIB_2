package com.rancard.common.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DConnect {
    private static Logger logger = Logger.getLogger(DConnect.class.getName());

    public static Connection getConnection() throws Exception {
        logger.log(Level.INFO, "Getting a connection");

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            return getConnection("rmcs");

        } catch (InstantiationException e) {
            logger.throwing("DConnect", "getConnection", e);
            throw new Exception(e.getMessage());
        } catch (IllegalAccessException e) {
            logger.throwing("DConnect", "getConnection", e);
            throw new IllegalAccessException(e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.throwing("DConnect", "getConnection", e);
            throw new ClassNotFoundException(e.getMessage());
        } catch (SQLException e) {
            logger.throwing("DConnect", "getConnection", e);
            throw new SQLException(e.getMessage());
        } catch (NamingException e) {
            logger.throwing("DConnect", "getConnection", e);
            throw new NamingException(e.getMessage());
        }

    }


    public static Connection getConnection(String dataSource) throws NamingException, SQLException {

        Context ctx = null;
        DataSource ds;

        try {
            ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/" + dataSource);
            return ds.getConnection();
        } catch (NamingException e) {
            throw new NamingException(e.getMessage());
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            if (ctx != null) {
                ctx.close();
            }

        }
    }
}