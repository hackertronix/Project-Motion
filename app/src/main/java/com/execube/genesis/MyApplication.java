package com.execube.genesis;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.orm.SugarContext;
import com.squareup.otto.Bus;

/**
 * Created by Prateek Phoenix on 6/5/2016.
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
