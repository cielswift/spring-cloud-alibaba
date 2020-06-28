package com.ciel.scatquick.beanload;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 事件发布器
 */
@Component
public class AppEventPush implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    private List<String> blackList;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

        this.publisher = applicationEventPublisher;
    }

    public void sendEmail(String address) {

        AppEvn event = new AppEvn(this, address);
        publisher.publishEvent(event);
        System.out.println(event);

    }

    public List<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<String> blackList) {
        this.blackList = blackList;
    }


}
