package framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("config.properties is not exist: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }
}
