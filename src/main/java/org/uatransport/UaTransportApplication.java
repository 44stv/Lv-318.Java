package org.uatransport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.uatransport.service.ewayutil.EwayRoutesListSaver;

@SpringBootApplication
@EnableCaching
@Slf4j
public class UaTransportApplication {
    @Autowired
    private static  ConfigurableApplicationContext appContext;

    public static void main(String[] args) {
        SpringApplication.run(UaTransportApplication.class, args);
        log.debug("--Application Started--");
         EwayRoutesListSaver saver = appContext.getBean(EwayRoutesListSaver.class);
         saver.updateRoutes();
    }
}
