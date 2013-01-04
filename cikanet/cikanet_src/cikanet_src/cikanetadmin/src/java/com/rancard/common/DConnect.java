package com.rancard.common;
import java.sql.*;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.naming.InitialContext;

public class DConnect {
    private static Connection con = null;
    
    public static Connection getConnection () throws Exception {
        
        try {
            // Class.forName("com.mysql.jdbc.Driver").newInstance();
            //use jdbc:mysql://remoteip/test eg
            //    jdbc://mysql://192.168.20.165/CellPhoneDB
            con = getConnection ("rmcs");
            //DriverManager.getConnection("jdbc:mysql://localhost:3306/rmcs","admin", "admin");
            // = DriverManager.getConnection("jdbc:mysql://localhost/rms?user=admin&password=admin");
            
            //return con;
        } catch(Exception e) {
            //System.out.println("Exception: Couldn't connect to the database" + e.getMessage());
            throw new Exception ("Exception: Couldn't connect to the database " + e.getMessage ());
        }
        return con;
    }
    
    
    public static Connection getConnection (String datasource) throws Exception {
        Context ctx = null;
        DataSource ds = null;
        
        try {
            ctx = new InitialContext ();
            ds = (DataSource) ctx.lookup ("java:comp/env/jdbc/"+datasource);
            return ds.getConnection ();
        } catch (NamingException e) {
            //   Log.write(e);
            throw new Exception (e.getMessage ());
        } catch (SQLException e) {
            throw new Exception (e.getMessage ());
        } finally {
            ctx = null;
            ds = null;
        }
    }
}
