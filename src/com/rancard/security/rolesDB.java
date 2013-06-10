package com.rancard.security;

/**
 * <p>Title: National Identification authority website and cms</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Rancard Soltuions Ltd</p>
 * @author unascribed
 * @version 1.0
 */


import java.sql.*;
import com.rancard.common.DConnect;

public class rolesDB {

  public rolesDB() {
  }

  public String getroleName(String id) throws Exception {
    String roleName = "";

    String SQL;
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement prepstat = null;

    try {
      con = DConnect.getConnection();
      java.sql.Date tempdate = null;

      SQL = "Select r.role from  roles r " +
          "where  r.role_ID=?";

      //System.out.println("SQL = " + SQL);

      prepstat = con.prepareStatement(SQL);
      prepstat.setString(1, id);
      rs = prepstat.executeQuery();
      while (rs.next()) {
        roleName = rs.getString("role");

      }

      rs.close();
      rs = null;
      prepstat.close();
      prepstat = null;
      con.close();
      con = null;
    }
    catch (Exception ex) {
      if (con != null) {
        con.close();
        con = null;
      }
      throw new Exception(ex.getMessage());
    }
    finally {
      if (rs != null) {
        try {
          rs.close();
        }
        catch (SQLException e) {
          ;
        }
        rs = null;
      }
      if (prepstat != null) {
        try {
          prepstat.close();
        }
        catch (SQLException e) {
          ;
        }
        prepstat = null;
      }
      if (con != null) {
        try {
          con.close();
        }
        catch (SQLException e) {
          ;
        }
        con = null;
      }
    }

    return roleName;
  }

  public roles viewRoles(String role_id) throws Exception {
    roles roleType = new roles();

    String SQL;
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement prepstat = null;

    try {
      con = DConnect.getConnection();
      java.sql.Date tempdate = null;

      SQL = "Select r.* from  roles r " +
          "where  r.role_ID=?";

      //System.out.println("SQL = " + SQL);

      prepstat = con.prepareStatement(SQL);
      prepstat.setString(1, role_id);
      rs = prepstat.executeQuery();
      while (rs.next()) {
        roleType.setRole_id(rs.getString("role_ID"));
        roleType.setRole_name(rs.getString("role"));
        roleType.setManageUser(rs.getString("canManageUsers"));
        roleType.setMenu1(rs.getString("canViewMenu1"));
        roleType.setMenu2(rs.getString("canViewMenu2"));
        roleType.setCreateFolder(rs.getString("canCreateFolder"));
        roleType.setDeleteFolder(rs.getString("canDeleteFolder"));
        roleType.setManageTemplate(rs.getString("canManageTemplate"));
        roleType.setMenu3(rs.getString("canViewMenu3"));
        roleType.setMenu4(rs.getString("canViewMenu4"));
        roleType.setMenu5(rs.getString("canViewMenu5"));
        roleType.setMenu6(rs.getString("canViewMenu6"));
      }

      rs.close();
      rs = null;
      prepstat.close();
      prepstat = null;
      con.close();
      con = null;
    }
    catch (Exception ex) {
      if (con != null) {
        con.close();
        con = null;
      }
      throw new Exception(ex.getMessage());
    }
    finally {
      if (rs != null) {
        try {
          rs.close();
        }
        catch (SQLException e) {
          ;
        }
        rs = null;
      }
      if (prepstat != null) {
        try {
          prepstat.close();
        }
        catch (SQLException e) {
          ;
        }
        prepstat = null;
      }
      if (con != null) {
        try {
          con.close();
        }
        catch (SQLException e) {
          ;
        }
        con = null;
      }
    }

    return roleType;
  }

  public java.util.HashMap getAllRoles() throws Exception { /**
     * @todo
                     add the neccessary parameters (if any)
     **/
    java.util.HashMap results = new java.util.HashMap();
     String SQL;
     ResultSet rs = null;
     Connection con = null;
     PreparedStatement prepstat = null;
     try {
       con = DConnect.getConnection();
       SQL = "select * from roles ";
       prepstat = con.prepareStatement(SQL); /**
       * @todo If the query had parameters set the PreparedStatement's parameters

       **/
      rs = prepstat.executeQuery();
      while (rs.next()) {
         roles roleType = new roles();

         roleType.setRole_id(rs.getString("role_ID"));

         results.put(roleType.getRole_id(), roleType);
       }
     }
     catch (Exception ex) {
       if (con != null) {
         con.close();
       }
       throw new Exception(ex.getMessage());
     }
     if (con != null) {
       con.close();
     }
     return results;
   }
}
