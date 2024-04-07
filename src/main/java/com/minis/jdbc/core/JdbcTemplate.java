package com.minis.jdbc.core;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;
import java.util.List;

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
                ArgumentPreparedStatementSetter argumentPreparedStatementSetter = new ArgumentPreparedStatementSetter(args);
                argumentPreparedStatementSetter.setValues(preparedStatement);
                return preparedStatementCallback.doInPreparedStatement(preparedStatement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
        try (Connection conn = dataSource.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            if (null != args) {
                RowMapperResultSetExtractor<T> rowMapperResultSetExtractor = new RowMapperResultSetExtractor<>(rowMapper);
                ArgumentPreparedStatementSetter argumentPreparedStatementSetter = new ArgumentPreparedStatementSetter(args);
                argumentPreparedStatementSetter.setValues(preparedStatement);
                return rowMapperResultSetExtractor.extractData(preparedStatement.executeQuery());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
