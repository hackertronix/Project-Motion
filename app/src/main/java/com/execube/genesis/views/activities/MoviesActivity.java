package com.execube.genesis.views.activities;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.execube.genesis.R;
import com.execube.genesis.views.fragments.ViewPagerFragment;
import io.fabric.sdk.android.Fabric;

public class MoviesActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_movies);

        View view= findViewById(R.id.viewpager_container);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        FragmentManager fragmentManager= getSupportFragmentManager();
        Fragment fragment;
        fragmentManager.findFragmentById(R.id.viewpager_container);

        if(savedInstanceState==null)
        {
            fragment= new ViewPagerFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.viewpager_container,fragment)
                    .commit();

        }


    }
}
