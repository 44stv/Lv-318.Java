package org.uatransport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//@EntityScan(
//    basePackageClasses = {UaTransportApplication.class, Jsr310JpaConverters.class}
//)
@SpringBootApplication
@EnableCaching
@Slf4j
public class UaTransportApplication {

    public static void main(String[] args) {
        SpringApplication.run(UaTransportApplication.class, args);
        log.debug("--Application Started--");
    }
}
