package com.database.demo.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public interface DBUtil {
    public Map<String, Object> getData(String query);
}
