package com.database.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SqlServerInterpreter implements DBUtil{

    public Map<String, Object> getData(String query) {

        HashMap<String, Object> response = new HashMap<>();
        double start, end;
        start = System.currentTimeMillis();
        try {
            Connection conn = SqlServerUtil.getConnection();
            Statement stmt = conn.createStatement();
            boolean hasResultSet = stmt.execute(query);
            if (hasResultSet) {
                ResultSet rs = stmt.getResultSet();
                end = System.currentTimeMillis();
                ResultSetMetaData rsmd = rs.getMetaData();
                int count = rsmd.getColumnCount();
                response.put("status", "OK");
                List<List<String>> TotalResult = new ArrayList<>();
                List<String> NameCol = new ArrayList<>();
                for (int i = 1; i <= count; i++) {
                    String name = rsmd.getColumnName(i);
                    NameCol.add(name);
                }
                TotalResult.add(NameCol);
                while (rs.next()) {
                    List<String> DataCol = new ArrayList<>();
                    for (int i = 1; i <= count; i++) {
                        String name = rsmd.getColumnName(i);
                        String cur = rs.getString(name);
                        DataCol.add(cur);
                    }
                    TotalResult.add(DataCol);
                }
                response.put("result", TotalResult);
                response.put("time", (end - start) / 1000);
            } else {
                response.put("status", "ERROR");
                end = System.currentTimeMillis();
                response.put("result", "Query: '" + query + "' has been executed");
            }

            response.put("time", (end - start) / 1000);

        } catch (SQLException e) {
            end = System.currentTimeMillis();
            String errorMessage = SqlServerUtil.processException(e);
            response.put("status", "ERROR");
            response.put("result", errorMessage);
            response.put("time", (end - start) / 1000);
        }
        return response;
    }
}
