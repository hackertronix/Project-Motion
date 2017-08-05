package com.execube.genesis.views.fragments;

import android.app.ActivityOptions;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.execube.genesis.R;
import com.execube.genesis.adapters.FavouritesAdapter;
import com.execube.genesis.model.Event;
import com.execube.genesis.model.Movie;
import com.execube.genesis.utils.EventBus;
import com.execube.genesis.database.MoviesDataSource;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prateek Phoenix on 6/7/2016.
 */
public class FavouritesFragment extends Fragment {

    public static final String TAG= "TAG";
    private static final String FAVOURITE_MOVIES_ARRAY = "favourite_movies";
    private List<Movie> mMovies=new ArrayList<>();
    private RecyclerView mFavouritesRecyclerView;
    ArrayList<Movie> moviesArrayList;
    private FavouritesAdapter mAdapter;
    private TabLayout mTabLayout;
    private MoviesDataSource dataSource;

    public FavouritesFragment() {
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"Favourite OnCreate");

    }


    @Override
    public void onResume() {


     /*   Sugar ORM does not have a Loader
          so to refresh the recyclerview adapter I am reinitializing it*/
        super.onResume();
        EventBus.getBus().register(this);
        Log.v(TAG,"Favourite OnResume");

        //TODO 1: Fix with Realm


        mMovies=dataSource.getAllMovies();
        mAdapter=new FavouritesAdapter((ArrayList<Movie>) mMovies,getActivity());
        mFavouritesRecyclerView.setAdapter(mAdapter);
        mFavouritesRecyclerView.invalidate();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataSource.close();

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.v(TAG,"Favourite OnPause");
        EventBus.getBus().unregister(this);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG,"In OnCreateView()");
        View view = inflater.inflate(R.layout.fragment_favourites,container,false);
        mFavouritesRecyclerView=(RecyclerView)view.findViewById(R.id.favourites_recyclerview);


        dataSource = new MoviesDataSource();
        dataSource.open();

        if(savedInstanceState!=null&&savedInstanceState.containsKey(FAVOURITE_MOVIES_ARRAY))
        {
            Log.v(TAG,"Restoring State");
            mMovies=savedInstanceState.getParcelableArrayList(FAVOURITE_MOVIES_ARRAY);
        }

        else {
            //TODO 2: Fix with Realm
            mMovies=  dataSource.getAllMovies();
        }



        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            mFavouritesRecyclerView.setLayoutManager(new
                    GridLayoutManager(getActivity(), 2));
        }
        else{
            mFavouritesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }
        mAdapter= new FavouritesAdapter((ArrayList<Movie>) mMovies,getActivity());
        mFavouritesRecyclerView.setAdapter(mAdapter);
        mFavouritesRecyclerView.invalidate();
        return view;
    }







    @Override
    public void onSaveInstanceState(Bundle outState) {
        moviesArrayList=new ArrayList<>(mMovies);
        Log.v(TAG,"Saving State");
        outState.putParcelableArrayList(FAVOURITE_MOVIES_ARRAY,moviesArrayList);
        super.onSaveInstanceState(outState);
    }




    public interface openDetailsListener{
        void openDetails(Movie movie,ActivityOptions options);
    }


    @Subscribe

    //WorkAround to achieve Database Synchronisation with the Favourites RecyclerView

    public void onEventReceived(Event event)
    {
      String message= event.getMessage();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

        //TODO 3: Fix with Realm
        mMovies=dataSource.getAllMovies();
        mAdapter=new FavouritesAdapter((ArrayList<Movie>) mMovies,getActivity());
        mFavouritesRecyclerView.setAdapter(mAdapter);
        mFavouritesRecyclerView.invalidateItemDecorations();
    }
}

