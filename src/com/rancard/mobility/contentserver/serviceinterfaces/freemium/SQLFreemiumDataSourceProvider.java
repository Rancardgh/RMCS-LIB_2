/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

import com.rancard.common.DConnect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mustee
 */
public class SQLFreemiumDataSourceProvider extends FreemiumDataSourceProviderImpl {

    private String column;

    public SQLFreemiumDataSourceProvider(String source, FreemiumDataSourceType type, String column) {
        super(source, type);

        this.column = column;
    }

    public List<String> getList() throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        List<String> items = new ArrayList<String>();
        boolean isColumnIndex = isNumber(column);

        try {
            conn = DConnect.getConnection();
            rs = conn.createStatement().executeQuery(this.getProviderSource());


            while (rs.next()) {
                if (isColumnIndex) {
                    items.add(rs.getString(Integer.parseInt(column)));
                } else {
                    items.add(rs.getString(column));
                }
            }

            return items;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    private boolean isNumber(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public boolean inProvider(String value) throws Exception {
        return getList().contains(value);
    }
}
