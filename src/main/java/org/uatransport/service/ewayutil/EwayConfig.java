package org.uatransport.service.ewayutil;

import lombok.extern.slf4j.Slf4j;
import org.uatransport.config.ConfigurationUtils;

import java.io.IOException;
import java.util.Properties;

@Slf4j
class EwayConfig {

    private static ClassLoader classLoader = ConfigurationUtils.class.getClassLoader();
    private static final String EWAY_PROPERTIES = "eway.properties";

    static Properties getProperty() {

        Properties appProps = new Properties();
        try {
            appProps.load(classLoader.getResourceAsStream(EWAY_PROPERTIES));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return appProps;
    }

    public static String getPropertyValue(String key) {
        return String.valueOf(getProperty().getProperty(key));
    }
}
