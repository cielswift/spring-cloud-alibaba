package com.ciel.scatquick.document.guancha.devise.guancha;

/**
 * 主题
 */
public interface Subject {

    public void subscribeObserver(Observer observer);

    public void unSubscribeObserver(Observer observer);
 

    public void notifyObservers();
 
    String subjectDetails();
}