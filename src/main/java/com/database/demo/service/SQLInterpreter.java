package com.database.demo.service;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SQLInterpreter {
    private DBUtil dbUtil;

    public SQLInterpreter() {
    }

    public SQLInterpreter(String type) {
        if (type.equals("SqlServer")) {
            dbUtil = new SqlServerInterpreter();
        } else if (type.equals("BigQuery")) {
            dbUtil = new BigQueryInterpreter();
        }
    }

    public Map<String, Object> getData(String query) {
        return dbUtil.getData(query);
    }
}
