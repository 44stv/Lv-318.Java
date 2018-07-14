package org.uatransport.service.ewayutil;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class EwaySchedule extends QuartzJobBean {
    @Autowired
    private ConfigurableApplicationContext appContext;

    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) {
        EwayRoutesListSaver saver = appContext.getBean(EwayRoutesListSaver.class);
        saver.convertAndSaveEwayRoutes();
    }

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(EwaySchedule.class).storeDurably().withIdentity("jobDetail")
                .withDescription("Update list of transit and stops").build();
    }

    /**
     * For init local DB change "second minutes hours * * ?" to time you wish. for example : "0 30 15 * * ?" - works at
     * 15:30:00 every day. After using set for our application : "0 5 2 ? * SAT" - works at 02:05:00 every Saturday.
     */
    @Bean
    public Trigger routesListSaverJobTrigger() {
        SimpleScheduleBuilder.simpleSchedule().repeatForever();
        return TriggerBuilder.newTrigger().forJob(jobDetail()).withIdentity("jobTrigger")
                .withDescription("Update list of transit and stops")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 ? * SUN")).build();
    }
}
