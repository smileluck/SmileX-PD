package top.zsmile.core.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceConfig {
    private static HikariDataSource dataSource = null;
    private static HikariConfig config = null;

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    public static HikariConfig getConfig() {
        return config;
    }

    public synchronized static void createDataSource(HikariConfig hikariConfig) {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
        dataSource = new HikariDataSource(hikariConfig);
        config = hikariConfig;
    }
}
