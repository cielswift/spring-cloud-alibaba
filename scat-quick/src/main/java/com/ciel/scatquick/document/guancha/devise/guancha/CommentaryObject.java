package com.ciel.scatquick.document.guancha.devise.guancha;

import java.util.ArrayList;
import java.util.List;

/**
 * CommentaryObject即是ConcreteSubject，值得注意的是该类对Observer的具体实现类即SMSUser持有一个引用，
 * 因为要通过该引用对象来更新观察者们
 */
public class CommentaryObject implements Subject, Commentary {
    private List<Observer> observerList = new ArrayList<Observer>();
    private String commentary;

    public CommentaryObject(String commentary) {
        this.commentary = commentary;
    }

    @Override
    public void subscribeObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void unSubscribeObserver(Observer observer) {
        observerList.remove(observer);
    }

    //通知所有观察者
    @Override
    public void notifyObservers() {
        for (Observer observer : observerList) {
            observer.update(getCommentary());
        }
    }

    @Override
    public String subjectDetails() {
        return getCommentary();
    }

    @Override
    public void setCommentary(String commentary) {
        this.commentary = commentary;

        notifyObservers(); //通知所有观察者
    }

    @Override
    public String getCommentary() {
        return this.commentary;
    }
}