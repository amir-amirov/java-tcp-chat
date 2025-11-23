package kz.shift.chat.server.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kz.shift.chat.common.config.Config;

import javax.sql.DataSource;

public final class DatabaseManager {
    private static final HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Config.getString("datasource.url"));
        config.setUsername(Config.getString("datasource.username"));
        config.setPassword(Config.getString("datasource.password"));

        config.setMaximumPoolSize(Config.getInteger("datasource.pool.size.max"));
        config.setMinimumIdle(Config.getInteger("datasource.pool.size.min"));

        ds = new HikariDataSource(config);
    }

    private DatabaseManager() {}

    public static DataSource getDataSource() {
        return ds;
    }

    public static void closePool() {
        if (ds != null) ds.close();
    }
}
