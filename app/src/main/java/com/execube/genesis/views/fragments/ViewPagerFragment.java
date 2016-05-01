package com.execube.genesis.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.execube.genesis.R;

/**
 * Created by Prateek Phoenix on 4/24/2016.
 */
public class ViewPagerFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_viewpager,container,false);

        ViewPager viewPager= (ViewPager)view.findViewById(R.id.viewPager);
        TabLayout tabLayout=(TabLayout)view.findViewById(R.id.tabLayout);

        final PopularMoviesFragment fragment1= new PopularMoviesFragment();
        final TopRatedMoviesFragment fragment2= new TopRatedMoviesFragment();



        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return position==0?fragment1:fragment2;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
//                return position==0?"POPULAR":"TOP RATED";
                return  null;

            }
        });




        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.popular_movies_tab_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.top_rated_movies_tab_icon);
        return view;

    }
}
