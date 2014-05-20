package com.rancard.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddressBook
{
    private String accountID;
    private String msisdn;
    private String registrationID;

    public AddressBook(String accountID, String msisdn, String registrationID)
    {
        this.accountID = accountID;
        this.msisdn = msisdn;
        this.registrationID = registrationID;
    }

    public String getAccountID()
    {
        return this.accountID;
    }

    public String getMsisdn()
    {
        return this.msisdn;
    }

    public String getRegistrationID()
    {
        return this.registrationID;
    }

    public boolean exists()
            throws SQLException, Exception
    {
        Connection conn = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM address_book where account_id = '" + this.accountID + "' and msisdn = '" + this.msisdn + "'";
        try
        {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(sql);
            return rs.next();
        }
        catch (SQLException se)
        {
            throw new SQLException(se.getMessage(), se.getSQLState());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally
        {
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean exists(Connection conn)
            throws SQLException, Exception
    {
        ResultSet rs = null;
        String sql = "SELECT * FROM address_book where account_id = '" + this.accountID + "' and msisdn = '" + this.msisdn + "'";
        try
        {
            rs = conn.createStatement().executeQuery(sql);
            return rs.next();
        }
        catch (SQLException se)
        {
            throw new SQLException(se.getMessage(), se.getSQLState());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally
        {
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean save()  throws SQLException, Exception
    {
        Connection conn = null;
        try
        {
            conn = DConnect.getConnection();
            String sql;
            if (!exists(conn))
            {
                sql = "INSERT INTO address_book (account_id, msisdn, registration_id) VALUES ('" + this.accountID + "', '" + this.msisdn + "', '" + this.registrationID + "')";


                conn.createStatement().execute(sql);
                return true;
            }
            return false;
        }
        catch (SQLException se)
        {
            throw new SQLException(se.getMessage(), se.getSQLState());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally
        {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public boolean save(Connection conn)
            throws SQLException, Exception
    {
        try
        {
            if (!exists(conn))
            {
                String sql = "INSERT INTO address_book (account_id, msisdn, registration_id) VALUES ('" + this.accountID + "', '" + this.msisdn + "', '" + this.registrationID + "')";


                conn.createStatement().execute(sql);
                return true;
            }
            return false;
        }
        catch (SQLException se)
        {
            throw new SQLException(se.getMessage(), se.getSQLState());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    public void delete()
            throws SQLException, Exception
    {
        Connection conn = null;
        String sql = "DELETE FROM address_book where account_id = '" + this.accountID + "' and msisdn = '" + this.msisdn + "'";
        try
        {
            conn = DConnect.getConnection();
            conn.createStatement().execute(sql);
        }
        catch (SQLException se)
        {
            throw new SQLException(se.getMessage(), se.getSQLState());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally
        {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void delete(Connection conn)
            throws SQLException, Exception
    {
        String sql = "DELETE FROM address_book where account_id = '" + this.accountID + "' and msisdn = '" + this.msisdn + "'";
        try
        {
            conn.createStatement().execute(sql);
        }
        catch (SQLException se)
        {
            throw new SQLException(se.getMessage(), se.getSQLState());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    public void update()
            throws SQLException, Exception
    {
        Connection conn = null;
        String sql = "UPDATE address_book SET account_id = '" + this.accountID + "', msisdn = '" + this.msisdn + "', registration_id = '" + this.registrationID + "' " + "where account_id = '" + this.accountID + "' and msisdn = '" + this.msisdn + "'";
        try
        {
            conn = DConnect.getConnection();
            conn.createStatement().execute(sql);
        }
        catch (SQLException se)
        {
            throw new SQLException(se.getMessage(), se.getSQLState());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
        finally
        {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void update(Connection conn)
            throws SQLException, Exception
    {
        String sql = "UPDATE address_book SET account_id = '" + this.accountID + "', msisdn = '" + this.msisdn + "', registration_id = '" + this.registrationID + "' " + "where account_id = '" + this.accountID + "' and msisdn = '" + this.msisdn + "'";
        try
        {
            conn.createStatement().execute(sql);
        }
        catch (SQLException se)
        {
            throw new SQLException(se.getMessage(), se.getSQLState());
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
