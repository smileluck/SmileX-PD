package top.zsmile.core.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import top.zsmile.core.exception.BaseException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataSourceUtils {
    private static HikariDataSource dataSource = null;
    private static HikariConfig config = null;



    static {
        try {
            InputStream is = DataSourceUtils.class.getClassLoader().getResourceAsStream("hikaricp.properties");
            Properties props = new Properties();
            props.load(is);
            config = new HikariConfig(props);
            dataSource = new HikariDataSource(config);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void createDataSource(HikariConfig hikariConfig) {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
        dataSource = new HikariDataSource(hikariConfig);
        config = hikariConfig;
    }

    public synchronized static void initDataSource(HikariConfig config) {
        createDataSource(config);
    }

    public static Connection getConnection() {
        if (dataSource == null) {
            throw new BaseException("数据库未连接");
        }

        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new BaseException("数据库连接异常");
        }

        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
