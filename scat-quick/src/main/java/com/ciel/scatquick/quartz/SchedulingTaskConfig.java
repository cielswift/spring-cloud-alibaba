package com.ciel.scatquick.quartz;

import com.ciel.scatquick.beanload.AppEventPush;
import com.ciel.scatquick.beanload.AppEvn;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.*;

@EnableScheduling //开启定时任务
@Configuration
public class SchedulingTaskConfig {

    /**
     * 字段	允许值	允许的特殊字符
     * 秒（Seconds）	0~59的整数	, - * /    四个字符
     * 分（Minutes）	0~59的整数	, - * /    四个字符
     * 小时（Hours）	0~23的整数	, - * /    四个字符
     * 日期（DayofMonth）	1~31的整数（但是你需要考虑你月的天数）	,- * ? / L W C     八个字符
     * 月份（Month）	1~12的整数或者 JAN-DEC	, - * /    四个字符
     * 星期（DayofWeek）	1~7的整数或者 SUN-SAT （1=SUN）	, - * ? / L C #     八个字符
     * 年(可选，留空)（Year）	1970~2099	, - * /    四个字符
     *
     * 　　每一个域都使用数字，但还可以出现如下特殊字符，它们的含义是：
     *
     * 　　（1）*：表示匹配该域的任意值。假如在Minutes域使用*, 即表示每分钟都会触发事件。
     *
     * 　　（2）?：只能用在DayofMonth和DayofWeek两个域。它也匹配域的任意值，但实际不会。因为DayofMonth和DayofWeek会相互影响。例如想在每月的20日触发调度，不管20日到底是星期几，则只能使用如下写法： 13 13 15 20 * ?, 其中最后一位只能用？，而不能使用*，如果使用*表示不管星期几都会触发，实际上并不是这样。
     *
     * 　　（3）-：表示范围。例如在Minutes域使用5-20，表示从5分到20分钟每分钟触发一次
     *
     * 　　（4）/：表示起始时间开始触发，然后每隔固定时间触发一次。例如在Minutes域使用5/20,则意味着5分钟触发一次，而25，45等分别触发一次.
     *
     * 　　（5）,：表示列出枚举值。例如：在Minutes域使用5,20，则意味着在5和20分每分钟触发一次。
     *
     * 　　（6）L：表示最后，只能出现在DayofWeek和DayofMonth域。如果在DayofWeek域使用5L,意味着在最后的一个星期四触发。
     *
     * 　  （7）W:表示有效工作日(周一到周五),只能出现在DayofMonth域，系统将在离指定日期的最近的有效工作日触发事件。例如：在 DayofMonth使用5W，如果5日是星期六，则将在最近的工作日：星期五，即4日触发。如果5日是星期天，则在6日(周一)触发；如果5日在星期一到星期五中的一天，则就在5日触发。另外一点，W的最近寻找不会跨过月份 。
     *
     * 　　（8）LW:这两个字符可以连用，表示在某个月最后一个工作日，即最后一个星期五。
     *
     * 　（9）#:用于确定每个月第几个星期几，只能出现在DayofMonth域。例如在4#2，表示某月的第二个星期三。
     */

    @Autowired
    private JavaMailSender javaMailSender;

    //, 枚举; -区间 ; 0/4 从0秒启动,每4秒执行一次 ;  1 1 1 LW * ? 每个月最后一个工作日
    // (星期写法)1-7,1周日,7 周六 ; 4#2 第二个星期4; 7L每个月最后一个周六
    //, 枚举; -区间 ; 0/4 从0秒启动,每4秒执行一次 ;  1 1 1 LW * ? 每个月最后一个工作日
    // (星期写法)1-7,1周日,7 周六 ; 4#2 第二个星期4; 7L每个月最后一个周六
    @Scheduled(cron = "1 * * 14,15 * 1-7" )
    public void run2() throws FileNotFoundException, MessagingException {

        //普通邮件
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("测试邮件主题");
        simpleMailMessage.setText("测试邮件内容");

        simpleMailMessage.setTo("1018866480@qq.com");
        //simpleMailMessage.setCc(); //抄送
        simpleMailMessage.setFrom("15966504931@163.com"); //自己, 设置邮件发送者
        javaMailSender.send(simpleMailMessage);


        //文件邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);

        mimeMessageHelper.setSubject("邮件主题");
        mimeMessageHelper.setText("<h6 style='color:red'>内容</h6>",true);
        mimeMessageHelper.setTo("1018866480@qq.com");
        mimeMessageHelper.setFrom("15966504931@163.com"); //自己, 设置邮件发送者

        mimeMessageHelper.addAttachment("code.yml",ResourceUtils.getFile("classpath:bootstrap.yml"));
        javaMailSender.send(mimeMessage);
    }


    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    protected ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    protected ThreadPoolTaskExecutor taskExecutor;

    @Scheduled(cron = "1/35 * * * * ?" )
    public void redisWrite(){

        threadPoolExecutor.submit(() -> {

            System.out.println(String.format("START: CURRENT THREAD NAME: %s",Thread.currentThread().getName()));

            redisTemplate.opsForValue()
                    .set(String.valueOf(System.currentTimeMillis()),"Fuck is the my life",6,TimeUnit.HOURS);

            System.out.println(String.format("END: CURRENT THREAD NAME: %s",Thread.currentThread().getName()));

        });
    }

    /**
     * 事件
     */

    @Autowired
    protected AppEventPush appEventPush;

    @Autowired
    protected ApplicationContext applicationContext;
    /**
     * 发布事件
     */
    @Scheduled(cron = "1/20 * * * * ?")
    public void tes(){

        applicationContext.publishEvent(new AppEvn(applicationContext,"app发布事件"));

        appEventPush.sendEmail("japan tokyo");
    }

    /**
     * 代替事件监听器,不用写ApplicationListener
     */
    @EventListener
    public void listenHello(AppEvn event) {
        System.out.println(String.format("@EventListener 收到事件: 名称%s ,源%s ",event.getName(),event.getSource().getClass().getName()));
    }



}
