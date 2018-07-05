package org.uatransport.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class ConfigurationUtils {

    private static ClassLoader classLoader = ConfigurationUtils.class.getClassLoader();
    private static final String CROSS_ORIGIN_PROPERTIES = "cross-origin.properties";

    private static Properties getConfigurationPropetries() {
        Properties properties = new Properties();
        try {
            properties.load(classLoader.getResourceAsStream(CROSS_ORIGIN_PROPERTIES));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return properties;
    }

    public static String getPropertyValue(String key) {
        return String.valueOf(getConfigurationPropetries().getProperty(key));
    }
}
