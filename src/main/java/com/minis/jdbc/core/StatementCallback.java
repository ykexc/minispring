package com.minis.jdbc.core;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author mqz
 */
@FunctionalInterface
public interface StatementCallback {


    Object doInStatement(Statement stmt) throws SQLException;

}
