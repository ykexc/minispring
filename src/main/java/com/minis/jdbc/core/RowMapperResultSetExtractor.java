package com.minis.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mqz
 */
public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

    private final RowMapper<T> rowMapper;

    public RowMapperResultSetExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> extractData(ResultSet rs) throws SQLException {

        List<T> res = new ArrayList<T>();
        int rowNum = 0;
        while (rs.next()) {
            res.add(rowMapper.mapRow(rs, rowNum++));
        }

        return res;
    }
}
