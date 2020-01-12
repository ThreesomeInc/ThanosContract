package com.thanos.mockserver;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

@Slf4j
public class ScheduleHelper {

    private static Scheduler scheduler;

    public static synchronized void initScheduler() {
        if (scheduler == null) {
            try {
                scheduler = new StdSchedulerFactory().getScheduler();
                scheduler.start();
            } catch (SchedulerException e) {
                log.error("Fail to create scheduler");
            }
        }
    }

    public static void addScheduler(Class<? extends Job> jobClass, String scheduleName, String cron, String index) {

        final String identityName = getIdentityName(scheduleName, index);

        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(identityName + "Job").build();
        job.getJobDataMap().put("index", identityName);
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(identityName + "Trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
            log.info("{} added with cron {}", identityName, cron);
        } catch (SchedulerException e) {
            log.error("Fail to add scheduler for {}:{}", jobClass.getName(), scheduleName);
        }

    }

    static String getIdentityName(String scheduleName, String index) {
        if (index != null) {
            return scheduleName + '-' + index;
        } else {
            return scheduleName;
        }
    }
}
