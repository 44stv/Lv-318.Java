package org.uatransport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
@Slf4j
public class UaTransportApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(UaTransportApplication.class, args);
        log.debug("--Application Started--");
        log.debug("--Application --");

    }

}
