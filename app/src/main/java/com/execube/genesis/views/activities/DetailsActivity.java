package com.execube.genesis.views.activities;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.transition.Slide;
import android.view.Gravity;

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
            Bundle arguments = new Bundle();
            arguments.putParcelable("PARCEL",
                    getIntent().getParcelableExtra("PARCEL"));

            fragment = new DetailsFragment();
            fragment.setArguments(arguments);
            fragmentManager.beginTransaction()
                    .add(R.id.details_container,fragment)
                    .commit();

        }

    }
}