package kz.shift.chat.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private static final Properties PROPERTIES = new Properties();

    static {
        load();
    }

    private Config() {}

    private static void load() {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("Failed to load config file");
            throw new RuntimeException("Failed to load config file");
        }
    }

    public static String getString(String key) {
        String property = PROPERTIES.getProperty(key);
        if (property == null) {
            throw new RuntimeException("Property for %s does exist".formatted(key));
        }
        return property;
    }

    public static Integer getInteger(String key) {
        String property = PROPERTIES.getProperty(key);
        if (property == null) {
            throw new RuntimeException("Property for %s does exist".formatted(key));
        }
        return Integer.parseInt(property);
    }
}
