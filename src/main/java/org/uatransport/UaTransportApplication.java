package org.uatransport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

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
    }

   /* List<Feedback> feedbacks= context.getBean(FeedbackService.class).getByUserId(1);
    List<Transit> transits = new ArrayList<>();
for (int i = 0; i < feedbacks.size(); i++) {
        transits.add(feedbacks.get(i).getTransit());
    }*/
   /*Optional<Transit> transit =  context.getBean(TransitRepository.class).findById(35);
context.getBean(ModelMapper.class).map(transit,TransitDTO.class);*/
}
