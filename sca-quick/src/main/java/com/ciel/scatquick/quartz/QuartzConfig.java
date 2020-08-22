package com.ciel.scatquick.quartz;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

/**
 * quartz
 */
@Configuration
public class QuartzConfig {

    @Autowired
    protected RestTemplate restTemplate;

    @Bean(name = "fuck-job")
    public JobDetail printTimeJobDetail(){
        return JobBuilder.newJob(new QuartzJobBean(){

            @Override
            protected void executeInternal(JobExecutionContext jobExecutionContext)  {

                String ali = restTemplate.getForObject("http://120.27.69.29:3000/", String.class);
                System.out.println(ali.length());
                System.out.println(Thread.currentThread().getName());

            }
        }.getClass())//PrintTimeJob我们的业务类

                .withIdentity("fuck-job-id")//可以给该JobDetail起一个id
                //每个JobDetail内都有一个Map，包含了关联到这个Job的数据，在Job类中可以通过context获取
                .usingJobData("msg", "Hello Quartz")//关联键值对
                .storeDurably()//即使没有Trigger关联时，也不需要删除该JobDetail
                .build();
    }

    @Bean
    public Trigger printTimeJobTrigger(@Qualifier("fuck-job") JobDetail jobDetail) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * 1 * ?");
        return TriggerBuilder.newTrigger()
                .forJob(printTimeJobDetail())//关联上述的JobDetail
                .withIdentity("fuck-job-tri")//给Trigger起个名字
                .withSchedule(cronScheduleBuilder)
                .build();
    }
}