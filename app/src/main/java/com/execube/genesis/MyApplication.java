package com.execube.genesis;

import android.app.Application;


import com.crashlytics.android.Crashlytics;
import com.squareup.otto.Bus;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Prateek Phoenix on 6/5/2016.
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        Realm.init(this);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("cinematic.realm")
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(configuration);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
