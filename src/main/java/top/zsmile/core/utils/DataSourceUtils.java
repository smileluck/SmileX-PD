package top.zsmile.core.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import top.zsmile.core.config.DataSourceConfig;
import top.zsmile.core.exception.BaseException;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceUtils {
    public static void initDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/cloud_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai");
        config.setUsername("root");
        config.setPassword("root");
        DataSourceConfig.createDataSource(config);
    }

    public static Connection getConnection() {

        HikariDataSource dataSource = DataSourceConfig.getDataSource();
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

    public static void closeConnection(){
        
    }
}
