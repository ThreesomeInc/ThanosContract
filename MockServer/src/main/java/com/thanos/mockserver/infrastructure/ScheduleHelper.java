package com.thanos.mockserver.infrastructure;

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

        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(scheduleName + "Job").build();
        if (index != null)
            job.getJobDataMap().put("index", index);

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(scheduleName + "Trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("Fail to add scheduler for {}:{}", jobClass.getName(), scheduleName);
        }

    }
}
