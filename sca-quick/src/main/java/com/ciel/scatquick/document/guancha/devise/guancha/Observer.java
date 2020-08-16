package com.ciel.scatquick.document.guancha.devise.guancha;

/**
 * 观察者
 */
public interface Observer {

    void update(String commentary);
 
    /**
     * subscribe(), method is used to subscribe itself with the subject.
     */
    void subscribe();
 
    /**
     * unsubscribe(), method is used to unsubscribe itself with the subject.
     */
    void unSubcribe();
}