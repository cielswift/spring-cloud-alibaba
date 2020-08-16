package com.ciel.scatquick.document.guancha.devise.guancha;

/**
 * SMSUser类即是ConcreteObserver，值得注意的是该类也持有了Subject接口的具体实现类即CommentaryObjec的引用，
 * 要通过该引用来注册自身来实现订阅某个主题
 */
public class SMSUser implements Observer {
    private Subject subject;
    private String commentary;
    private String userInfo;
 
    public SMSUser(Subject subject, String userInfo) {
        if(subject==null){
            throw new IllegalArgumentException("No Publisher found.");
        }
        this.subject = subject;
        this.userInfo = userInfo;
    }
    @Override
    public void update(String commentary) {
        this.commentary = commentary;
        // 这里只要subject一发布事件，那么这里订阅者就会马上知道消息
        display();
    }
    @Override
    public void subscribe() {
        System.out.println("Subscribing "+userInfo+" to "+subject.subjectDetails() + " ...");
        subject.subscribeObserver(this); //添加观察者
    }
    @Override
    public void unSubcribe() {
        System.out.println("Unsubscribing "+userInfo+" to "+subject.subjectDetails()+" ...");
        subject.unSubscribeObserver(this); //移除观察者
    }
 
    public void display() {
        System.out.println("["+userInfo+"]: " + this.commentary);
    }
}