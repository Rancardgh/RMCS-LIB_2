
package com.rancard.common;


import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class DConnect {
    private static Connection con = null;


    public static Connection getConnection()
            throws Exception {

        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();


            con = getConnection("rmcs");

        } catch (Exception e) {

            throw new Exception("Exception: Couldn't connect to the database " + e.getMessage());

        }

        return con;

    }


    public static Connection getConnection(String datasource)
            throws Exception {

        Context ctx = null;

        DataSource ds = null;

        try {

            ctx = new InitialContext();

            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/" + datasource);

            return ds.getConnection();

        } catch (NamingException e) {

            throw new Exception(e.getMessage());

        } catch (SQLException e) {

            throw new Exception(e.getMessage());

        } finally {

            ctx = null;

            ds = null;

        }

    }

}



/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar

 * Qualified Name:     com.rancard.common.DConnect

 * JD-Core Version:    0.7.0.1

 */