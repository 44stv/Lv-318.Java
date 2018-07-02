package org.uatransport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.uatransport.entity.NonExtendableCategory;
import org.uatransport.entity.Transit;
import org.uatransport.repository.TransitRepository;
import org.uatransport.service.CategoryService;
import org.uatransport.service.ewayutil.EwayRoutesListSaver;
import org.uatransport.service.implementation.CategoryServiceImpl;
import org.uatransport.service.implementation.TransitServiceImpl;

@SpringBootApplication
@EnableCaching
@Slf4j
public class UaTransportApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(UaTransportApplication.class, args);
        log.debug("--Application Started--");

        EwayRoutesListSaver saver = (EwayRoutesListSaver) context.getBean(EwayRoutesListSaver.class);
        saver.convertAndSaveEwayRoutes();
    }
}
