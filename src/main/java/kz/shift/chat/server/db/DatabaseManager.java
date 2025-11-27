package kz.shift.chat.server.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kz.shift.chat.common.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static final HikariDataSource ds;
    private static final String URL = Config.getString("datasource.url");
    private static final String USER = Config.getString("datasource.username");
    private static final String PASSWORD = Config.getString("datasource.password");
    private static final String SQL_FILE = Config.getString("datasource.scheme.file");

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);

        config.setMaximumPoolSize(Config.getInteger("datasource.pool.size.max"));
        config.setMinimumIdle(Config.getInteger("datasource.pool.size.min"));

        ds = new HikariDataSource(config);

        initializeTables();
    }

    private DatabaseManager() {}

    public static DataSource getDataSource() {
        return ds;
    }

    public static void closePool() {
        if (ds != null) ds.close();
    }

    private static void initializeTables() {
        String sql = getSqlCommandFromFile(SQL_FILE);

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
            logger.info("Database tables are initialized.");
        } catch (SQLException e) {
            logger.error("Failed to initialize DB scheme: ", e);
            throw new RuntimeException(e);
        }
    }

    private static String getSqlCommandFromFile(String file) {
        try (InputStream is = DatabaseManager.class.getClassLoader().getResourceAsStream(file)) {
            if (is == null) {
                logger.error("Scheme file not found: {}", file);
                throw new RuntimeException("Scheme file not found.");
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Failed to read commands from scheme file: {}", file, e);
            throw new RuntimeException("Failed to read scheme file.", e);
        }
    }
}
