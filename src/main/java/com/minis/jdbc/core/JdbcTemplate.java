package com.minis.jdbc.core;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;

/**
 * @author mqz
 */
public class JdbcTemplate {


    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }


    public JdbcTemplate() {
    }

    public Object query(StatementCallback statementCallback) {

        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            return statementCallback.doInStatement(stmt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public Object query(String sql, Object[] args, PreparedStatementCallback preparedStatementCallback) {
        try (Connection conn = dataSource.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            if (null != args) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof Integer) {
                        preparedStatement.setInt(i + 1, (int) arg);
                    } else if (arg instanceof String) {
                        preparedStatement.setString(i + 1, (String) arg);
                    } else if (arg instanceof Date) {
                        preparedStatement.setDate(i + 1, new java.sql.Date(((Date) arg).getTime()));
                    }
                }
                return preparedStatementCallback.doInPreparedStatement(preparedStatement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
