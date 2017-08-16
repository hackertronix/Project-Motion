package com.execube.genesis.utils;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by hackertronix on 16/08/17.
 */

public class RxEventBus {

    private static RxEventBus instance;

    private PublishSubject<String> subject = PublishSubject.create();


    public static RxEventBus getInstance()
    {
        if(instance == null)
        {
            instance = new RxEventBus();
        }
         return instance;
    }

    public void setMessage(String message)
    {
        subject.onNext(message);
    }

    public Observable<String> getMessage()
    {
        return subject;
    }


}
