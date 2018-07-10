package org.uatransport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.uatransport.controller.UserController;
import org.uatransport.entity.dto.UserDTO;
import org.uatransport.service.implementation.ExpirationCheckService;

//@EntityScan(
//    basePackageClasses = {UaTransportApplication.class, Jsr310JpaConverters.class}
//)
@SpringBootApplication
@EnableCaching
@Slf4j
public class UaTransportApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context=SpringApplication.run(UaTransportApplication.class, args);
        log.debug("--Application Started--");

        /*UserDTO userDTO= new UserDTO("asd","asd", "ira_ih@ukr.net","123456","123456");
        UserDTO userDTO2= new UserDTO("asd","asd", "ih.iryna@gmail.com","123456","123456");
        context.getBean(UserController.class).signUp(userDTO);
        context.getBean(UserController.class).signUp(userDTO2);*/

    //  context.getBean(ExpirationCheckService.class).scheduleCleanTaskByExpirationTime();

    }
}
