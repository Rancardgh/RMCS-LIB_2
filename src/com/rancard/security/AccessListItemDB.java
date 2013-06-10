package com.rancard.security;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.rancard.common.DConnect;
import java.util.ArrayList;

public class AccessListItemDB {
    public AccessListItemDB() {
    }

    public void SaveToWhiteList(String ipAddress, String owner_id) throws
            Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        AccessListItem userIP = new AccessListItem();

        try {
            con = DConnect.getConnection();
            java.sql.Date tempdate = null;

            SQL = "insert into whitelist  (allowed_IP, owner_id) values (?,?)";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, ipAddress);
            prepstat.setString(2, owner_id);
             prepstat.execute();
             prepstat.close();
            prepstat = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

    }


    public void SaveToBlackList(String ipAddress, String owner_id) throws
            Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        AccessListItem userIP = new AccessListItem();

        try {
            con = DConnect.getConnection();
            java.sql.Date tempdate = null;

            SQL = "insert into blacklist  (denied_IP, owner_id) values (?,?)";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, ipAddress);
            prepstat.setString(2, owner_id);
            prepstat.execute();
            //rs.close();
            //rs = null;
            prepstat.close();
            prepstat = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

    }


    public void RemoveFromBlackList(String ipAddress) throws
            Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        AccessListItem userIP = new AccessListItem();

        try {
            con = DConnect.getConnection();
            java.sql.Date tempdate = null;

            SQL = "delete from blacklist  where denied_IP =?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, ipAddress);
            prepstat.execute();
            //rs.close();
            //rs = null;
            prepstat.close();
            prepstat = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

    }

    public void RemoveFromWhiteList(String ipAddress) throws
          Exception {
      String SQL;
      ResultSet rs = null;
      Connection con = null;
      PreparedStatement prepstat = null;
      AccessListItem userIP = new AccessListItem();

      try {
          con = DConnect.getConnection();
          java.sql.Date tempdate = null;

          SQL = "delete from whitelist  where allowed_IP =?";
          prepstat = con.prepareStatement(SQL);
          prepstat.setString(1, ipAddress);
          prepstat.execute();
          //rs.close();
          //rs = null;
          prepstat.close();
          prepstat = null;
          con.close();
          con = null;
      } catch (Exception ex) {
          if (con != null) {
              con.close();
              con = null;
          }
          throw new Exception(ex.getMessage());
      } finally {
          if (rs != null) {
              try {
                  rs.close();
              } catch (SQLException e) {
                  ;
              }
              rs = null;
          }
          if (prepstat != null) {
              try {
                  prepstat.close();
              } catch (SQLException e) {
                  ;
              }
              prepstat = null;
          }
          if (con != null) {
              try {
                  con.close();
              } catch (SQLException e) {
                  ;
              }
              con = null;
          }
      }

  }


    public AccessListItem viewWhiteListItem(String ipAddress) throws Exception {
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        AccessListItem userIP = new AccessListItem();

        try {
            con = DConnect.getConnection();
            java.sql.Date tempdate = null;

            SQL = "select * from whitelist where allowed_IP = ?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1, ipAddress);
            rs = prepstat.executeQuery();
            while (rs.next()) {

                userIP.setIpAddress(rs.getString("allowed_IP"));
                userIP.setOwner(rs.getString("owner_id"));

            }

            rs.close();
            rs = null;
            prepstat.close();
            prepstat = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

        return userIP;
    }

    public AccessListItem viewBlackListItem(String ipAddress) throws Exception {
            String SQL;
            ResultSet rs = null;
            Connection con = null;
            PreparedStatement prepstat = null;

            AccessListItem userIP = new AccessListItem();

            try {
                con = DConnect.getConnection();
                java.sql.Date tempdate = null;

                SQL = "select * from blacklist where denied_IP = ?";
                prepstat = con.prepareStatement(SQL);
                prepstat.setString(1, ipAddress);
                rs = prepstat.executeQuery();
                while (rs.next()) {

                    userIP.setIpAddress(rs.getString("denied_IP"));
                    userIP.setOwner(rs.getString("owner_id"));

                }

                rs.close();
                rs = null;
                prepstat.close();
                prepstat = null;
                con.close();
                con = null;
            } catch (Exception ex) {
                if (con != null) {
                    con.close();
                    con = null;
                }
                throw new Exception(ex.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        ;
                    }
                    rs = null;
                }
                if (prepstat != null) {
                    try {
                        prepstat.close();
                    } catch (SQLException e) {
                        ;
                    }
                    prepstat = null;
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        ;
                    }
                    con = null;
                }
            }

            return userIP;
    }


    public java.util.ArrayList viewWhiteList() throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        java.util.ArrayList whiteList = new ArrayList();
        try {
            con = DConnect.getConnection();
            java.sql.Date tempdate = null;

            SQL = "select * from whitelist";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                AccessListItem userIP = new AccessListItem();
                userIP.setIpAddress(rs.getString("allowed_IP"));
                userIP.setOwner(rs.getString("owner_id"));
                whiteList.add(userIP);
            }

            rs.close();
            rs = null;
            prepstat.close();
            prepstat = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

        return whiteList;

    }

    public java.util.ArrayList viewWhiteList(String ownerId) throws Exception {

       String SQL;
       ResultSet rs = null;
       Connection con = null;
       PreparedStatement prepstat = null;
       java.util.ArrayList whiteList = new ArrayList();
       try {
           con = DConnect.getConnection();
           java.sql.Date tempdate = null;

           SQL = "select * from whitelist where owner_id = ?";
           prepstat = con.prepareStatement(SQL);
           prepstat.setString(1,ownerId);
           rs = prepstat.executeQuery();
           while (rs.next()) {
               AccessListItem userIP = new AccessListItem();
               userIP.setIpAddress(rs.getString("allowed_IP"));
               userIP.setOwner(rs.getString("owner_id"));
               whiteList.add(userIP);
           }

           rs.close();
           rs = null;
           prepstat.close();
           prepstat = null;
           con.close();
           con = null;
       } catch (Exception ex) {
           if (con != null) {
               con.close();
               con = null;
           }
           throw new Exception(ex.getMessage());
       } finally {
           if (rs != null) {
               try {
                   rs.close();
               } catch (SQLException e) {
                   ;
               }
               rs = null;
           }
           if (prepstat != null) {
               try {
                   prepstat.close();
               } catch (SQLException e) {
                   ;
               }
               prepstat = null;
           }
           if (con != null) {
               try {
                   con.close();
               } catch (SQLException e) {
                   ;
               }
               con = null;
           }
       }

       return whiteList;

   }



    public java.util.ArrayList viewBlackList() throws Exception {

        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        java.util.ArrayList whiteList = new ArrayList();
        try {
            con = DConnect.getConnection();
            java.sql.Date tempdate = null;

            SQL = "select * from blacklist";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                AccessListItem userIP = new AccessListItem();
                userIP.setIpAddress(rs.getString("denied_IP"));
                userIP.setOwner(rs.getString("owner_id"));
                whiteList.add(userIP);
            }

            rs.close();
            rs = null;
            prepstat.close();
            prepstat = null;
            con.close();
            con = null;
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ;
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (SQLException e) {
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    ;
                }
                con = null;
            }
        }

        return whiteList;

    }



    public java.util.ArrayList viewBlackList(String ownerId) throws Exception {

          String SQL;
          ResultSet rs = null;
          Connection con = null;
          PreparedStatement prepstat = null;
          java.util.ArrayList whiteList = new ArrayList();
          try {
              con = DConnect.getConnection();
              java.sql.Date tempdate = null;

              SQL = "select * from blacklist where owner_id =?";
              prepstat = con.prepareStatement(SQL);
              prepstat.setString(1,ownerId);
              rs = prepstat.executeQuery();
              while (rs.next()) {
                  AccessListItem userIP = new AccessListItem();
                  userIP.setIpAddress(rs.getString("denied_IP"));
                  userIP.setOwner(rs.getString("owner_id"));
                  whiteList.add(userIP);
              }

              rs.close();
              rs = null;
              prepstat.close();
              prepstat = null;
              con.close();
              con = null;
          } catch (Exception ex) {
              if (con != null) {
                  con.close();
                  con = null;
              }
              throw new Exception(ex.getMessage());
          } finally {
              if (rs != null) {
                  try {
                      rs.close();
                  } catch (SQLException e) {
                      ;
                  }
                  rs = null;
              }
              if (prepstat != null) {
                  try {
                      prepstat.close();
                  } catch (SQLException e) {
                      ;
                  }
                  prepstat = null;
              }
              if (con != null) {
                  try {
                      con.close();
                  } catch (SQLException e) {
                      ;
                  }
                  con = null;
              }
          }

          return whiteList;

      }


}
