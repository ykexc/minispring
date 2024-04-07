package com.minis.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mqz
 */
public interface ResultSetExtractor <T>{
    T extractData(ResultSet rs) throws SQLException;
}
