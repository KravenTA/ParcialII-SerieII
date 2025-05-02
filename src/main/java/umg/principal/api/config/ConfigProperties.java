package umg.principal.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigProperties {
    private static final Logger logger = Logger.getLogger(ConfigProperties.class.getName());
    private static final Properties properties = new Properties();
    private static boolean loaded = false;

    private ConfigProperties() {
        // Constructor privado para evitar instanciación
    }

    public static void load() {
        if (!loaded) {
            try (InputStream input = ConfigProperties.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (input != null) {
                    properties.load(input);
                    loaded = true;
                } else {
                    logger.severe("❌ The application.properties file was not found");
                }
            } catch (IOException e) {
                logger.severe("❌ Error loading the properties file: " + e.getMessage());
            }
        }
    }

    public static String getProperty(String key) {
        if (!loaded) {
            load();
        }
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (!loaded) {
            load();
        }
        return properties.getProperty(key, defaultValue);
    }

    public static LocalDate getReportDate() {
        String dateStr = getProperty("covid.report.date", LocalDate.now().toString());
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
    }
    public static String getExampleCountry() {
        return getProperty("covid.example.country", "CHN");
    }


    public static int getProcessingDelay() {
        String delayStr = getProperty("covid.processing.delay", "15");
        try {
            return Integer.parseInt(delayStr);
        } catch (NumberFormatException e) {
            logger.warning("Invalid value for covid.processing.delay: " + delayStr + ". Using default value: 15");
            return 15;
        }
    }
}
