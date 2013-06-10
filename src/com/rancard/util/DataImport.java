package com.rancard.util;


import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.rancard.common.*;
import jxl.*;

public class DataImport {
    public DataImport() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private HttpServletRequest request = null;

    // column names of the MySQL table
    // which should be in the same order as the excel columns
    private String[] columnNames = null;
    private java.util.HashMap fixedColumnValues = new java.util.HashMap();
    private int[] requiredColumns = null;


    // table name to import to
    private String tableName = null;

    static Connection conn = null;

    static PreparedStatement pst = null;

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setRequiredColumns(int[] requiredColumns) {
        this.requiredColumns = requiredColumns;
    }

    public void setFixedColumnValues(HashMap fixedColumnValues) {
        this.fixedColumnValues = fixedColumnValues;
    }

    // import data from excel to mysql
    public Message importdata() {
        Message reply = new Message();
        try {
            ServletInputStream is = request.getInputStream();
            byte[] junk = new byte[1024];
            int bytesRead = 0;

            // strip off the HTTP information from input stream
            // the first four lines are request junk
            bytesRead = is.readLine(junk, 0, junk.length);
            bytesRead = is.readLine(junk, 0, junk.length);
            bytesRead = is.readLine(junk, 0, junk.length);
            bytesRead = is.readLine(junk, 0, junk.length);

            // create the workbook object from the ServletInputStream
            Workbook workbook = Workbook.getWorkbook(is);
            Sheet[] sheets = workbook.getSheets();
            for(int index = 0; index < sheets.length; index++){
                Sheet sheet = workbook.getSheet(index);
                System.out.println(sheet.getName());
                Cell cell = null;

                // prepare the insert sql statement
                StringBuffer sql = new StringBuffer("INSERT INTO ").append(
                        tableName).append("(");
                StringBuffer params = new StringBuffer("VALUES(");
                int cols = columnNames.length;
                for (int i = 0; i < cols; i++) {
                    sql.append(columnNames[i]).append(",");
                    params.append("?,");
                }
                sql = sql.deleteCharAt(sql.length() -
                                       1).append(")").append(params.
                        deleteCharAt(
                                params.length() - 1)).
                      append(")");

                // get database connection
                conn = DConnect.getConnection();
                pst = conn.prepareStatement(sql.toString());

                // read data from the excel spreadsheet
                // the code here assumes that the data begin in row 2 [A2]
                int rows = sheet.getRows();
                for (int i = 1; i < rows; i++) {
                    boolean isValid = true;
                    // validate row for required columns
                    for (int k = 0; k < requiredColumns.length; k++) {
                        if (sheet.getCell(requiredColumns[k], i) != null &&
                            sheet.getCell(requiredColumns[k], i).getType() ==
                            CellType.EMPTY) {
                            isValid = false;
                            reply.setStatus(isValid);

                            String row = new Integer(i).toString();
                            reply.setMessage(columnNames[requiredColumns[k]] +
                                             " is a required column. Row " +
                                             row +
                                             " of this column is empty");
                            return reply;
                        }
                    }

                    if (isValid) {
                        // column 1 is unique id
                        pst.setString(1,
                                      com.rancard.common.uidGen.generateID(14));
                        // if a column is specified as fixed then the values for that column are not read from the excel dodument but are specified
                        int fixedColumnCount = 0;
                        for (int j = 0; j < cols - 1; j++) {
                            if (fixedColumnValues.containsKey(new Integer(j + 2))) {
                                if (fixedColumnValues.get(new Integer(j + 2)).
                                    getClass().getName().equalsIgnoreCase(
                                            "java.lang.String")) {
                                    pst.setString(j + 2,
                                                  (String) fixedColumnValues.
                                                  get(new Integer(j + 2)));
                                } else if (fixedColumnValues.get(new Integer(j +
                                        2)).
                                           getClass().getName().
                                           equalsIgnoreCase(
                                        "java.util.Date")) {
                                    pst.setDate(j + 2,
                                                new java.sql.Date(((java.util.
                                            Date)
                                            fixedColumnValues.
                                            get(new Integer(j + 2))).getTime()));
                                }

                                fixedColumnCount = fixedColumnCount + 1;
                            } else {

                                cell = sheet.getCell(j - fixedColumnCount, i);
                                pst.setString(j + 2, cell.getContents());
                            }
                        }

                    }
                    pst.addBatch();
                }
                pst.executeBatch();
            }

            // close the workbook and free up memory
            reply.setMessage("Upload Successful");
            reply.setStatus(true);
            workbook.close();

        } catch (Exception e) {
            reply.setStatus(false);
            reply.setMessage(e.getMessage());

        } finally {
            closeDB();
        }
        //reply.setMessage("Upload successful");
        return reply;
    }

    private static void closeDB() {
        try {
            if (pst != null) {
                pst.close();
                pst = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (Exception e) {}
    }

    private void jbInit() throws Exception {
    }
}
