package kz.shift.chat.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE_NAME = "application.properties";

    static {
        load();
    }

    private Config() {}

    private static void load() {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load config file: ", e);
            throw new RuntimeException("Failed to load config file");
        }
    }

    public static String getString(String key) {
        String property = properties.getProperty(key);
        if (property == null) {
            throw new RuntimeException("Property for %s does exist".formatted(key));
        }
        return property;
    }

    public static Integer getInteger(String key) {
        String property = properties.getProperty(key);
        if (property == null) {
            throw new RuntimeException("Property for %s does exist".formatted(key));
        }
        return Integer.parseInt(property);
    }
}
