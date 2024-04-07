package com.minis.jdbc.datasource.pooled;

import com.minis.jdbc.conn.PooledConnection;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author mqz
 *
 *
 * this is a pooled datasource
 * Mybatis has implemented a more perfect pooled datasource, which can be referred to below
 */
public class PooledDatasource implements DataSource {

    private List<PooledConnection> connections;

    private String driverName;

    private String url;

    private String username;

    private String password;

    private int initialSize = 2;

    private Properties connectionProperties;


    public PooledDatasource() {}

    private void initPool() {
        this.connections = new ArrayList<>(initialSize);

        try {
            for (int i = 0; i < initialSize; i++) {
                Connection connection = DriverManager.getConnection(url, username, password);
                PooledConnection pooledConnection = new PooledConnection(connection, false);
                this.connections.add(pooledConnection);
                System.out.println("********add connection pool*********");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionFromDriver(getUsername(), getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnectionFromDriver(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    protected Connection getConnectionFromDriverManager(String url, Properties connectionProperties) throws SQLException {
        return DriverManager.getConnection(url, connectionProperties);
    }

    protected Connection getConnectionFromDriver(String username, String password) throws SQLException {
        Properties mergedProps = new Properties();
        Properties connProps = getConnectionProperties();
        if (null != connProps) {
            mergedProps.putAll(connProps);
        }
        if (null != username && null != password) {
            mergedProps.setProperty("user", username);
            mergedProps.setProperty("password", password);
        }
        if (null == this.connections) {
            initPool();
        }
        PooledConnection pooledConnection = getAvailableConnection();
        while (pooledConnection == null) {  //如果所有都在使用的话，那就只能等，这里的实现比较简单，比较好的实现是mybatis的PolledDatasource
            pooledConnection = getAvailableConnection();
            if (null == pooledConnection) {
                try {
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return pooledConnection;
    }

    private PooledConnection getAvailableConnection() throws SQLException {
        for (PooledConnection connection : connections) {
            if (!connection.isActive()) {
                connection.setActive(true);
                return connection;
            }
        }
        return null;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverName + "]", e);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }
}
