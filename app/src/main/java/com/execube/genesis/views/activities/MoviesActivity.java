package com.execube.genesis.views.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;


import com.execube.genesis.R;
import com.execube.genesis.model.Movie;
import com.execube.genesis.utils.EventBus;
import com.execube.genesis.views.fragments.DetailsFragment;
import com.execube.genesis.views.fragments.FavouritesFragment;
import com.execube.genesis.views.fragments.PopularMoviesFragment;
import com.execube.genesis.views.fragments.TopRatedMoviesFragment;
import com.execube.genesis.views.fragments.ViewPagerFragment;


public class MoviesActivity extends AppCompatActivity implements PopularMoviesFragment.openDetailsListener,
        TopRatedMoviesFragment.openDetailsListener,FavouritesFragment.openDetailsListener {


    boolean isTablet;
    EventBus bus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            getWindow().setStatusBarColor(Color.WHITE);
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


  /*  @Override
    public void OnFabTapped() {
        Fragment fragment= new FavouritesFragment();
        FragmentManager fragmentManager= getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.viewpager_container,fragment)
                .addToBackStack(null)
                .commit();
    }*/
}