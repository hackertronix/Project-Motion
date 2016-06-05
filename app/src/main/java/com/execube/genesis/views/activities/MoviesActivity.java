package com.execube.genesis.views.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;


import com.crashlytics.android.Crashlytics;
import com.execube.genesis.R;
import com.execube.genesis.model.Movie;
import com.execube.genesis.views.fragments.DetailsFragment;
import com.execube.genesis.views.fragments.PopularMoviesFragment;
import com.execube.genesis.views.fragments.TopRatedMoviesFragment;
import com.execube.genesis.views.fragments.ViewPagerFragment;
import io.fabric.sdk.android.Fabric;


public class MoviesActivity extends AppCompatActivity implements PopularMoviesFragment.openDetailsListener,
        TopRatedMoviesFragment.openDetailsListener{


    public boolean isTablet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_movies);


        if (findViewById(R.id.details_container) == null)//CHECKING FOR TABLET CONFIGURATION
        {
            isTablet=false;
        }
        else{
            isTablet=true;
        }
        View view= findViewById(R.id.viewpager_container);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setExitTransition(new Explode());
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

    @Override
    public void openDetails(Movie movie,ActivityOptions options) {

        //options parameter is for the transition

        if(isTablet)
        {
           //TODO Retain the transitions.

          Bundle bundle=new Bundle();
            bundle.putParcelable("PARCEL",movie);

            DetailsFragment fragment= new DetailsFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_container,fragment)
                    .commit();
}

        else{
            Intent intent= new Intent(this,DetailsActivity.class);
            intent.putExtra("PARCEL",movie);
            startActivity(intent,options.toBundle());
        }

    }
}