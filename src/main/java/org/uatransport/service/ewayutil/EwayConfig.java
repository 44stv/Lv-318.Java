package org.uatransport.service.ewayutil;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

@Slf4j
class EwayConfig {
    static String getProperty(String property) {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "eway.properties";
        Properties appProps = new Properties();
        try {
            appProps.load(new InputStreamReader(new FileInputStream(appConfigPath), Charset.forName("UTF-8")));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        return appProps.getProperty(property);
    }
}
