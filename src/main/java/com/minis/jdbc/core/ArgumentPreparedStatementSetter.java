package com.minis.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author mqz
 */
public class ArgumentPreparedStatementSetter {

    private final Object[] args;

    public ArgumentPreparedStatementSetter(final Object[] args) {
        this.args = args;
    }

    public void setValues(final PreparedStatement ps) throws SQLException {
        if (null != args && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                doSetValue(ps, i + 1, args[i]);
            }
        }
    }

    protected void doSetValue(final PreparedStatement ps, final int index, final Object value) throws SQLException {
        if (value instanceof String) {
            ps.setString(index, (String) value);
        } else if (value instanceof Integer) {
            ps.setInt(index, (Integer) value);
        } else if (value instanceof java.util.Date) {
            ps.setDate(index, new java.sql.Date(((java.util.Date) value).getTime()));
        }
    }

}
