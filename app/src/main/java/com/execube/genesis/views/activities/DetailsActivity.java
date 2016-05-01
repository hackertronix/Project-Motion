package com.execube.genesis.views.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;

import com.execube.genesis.R;
import com.execube.genesis.views.fragments.DetailsFragment;

/**
 * Created by Prateek Phoenix on 5/1/2016.
 */
public class DetailsActivity extends FragmentActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        if (Build.VERSION.SDK_INT >= 21) {
           getWindow().setStatusBarColor(getResources().getColor(R.color.details_status_bar));
           Slide slide=new Slide(Gravity.BOTTOM);
            slide.excludeTarget(android.R.id.statusBarBackground,true);
            slide.excludeTarget(android.R.id.navigationBarBackground,true);
            getWindow().setEnterTransition(slide);
            postponeEnterTransition();
        }

        FragmentManager fragmentManager= getSupportFragmentManager();
        Fragment fragment= fragmentManager.findFragmentById(R.id.details_container);

        if(fragment==null)
        {
            fragment= new DetailsFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.details_container,fragment)
                    .commit();

        }

    }
}
