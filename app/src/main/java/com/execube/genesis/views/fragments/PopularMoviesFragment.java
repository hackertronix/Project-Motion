package com.execube.genesis.views.fragments;

import android.app.ActivityOptions;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.execube.genesis.R;
import com.execube.genesis.adapters.GenericAdapter;
import com.execube.genesis.adapters.PopularMoviesAdapter;
import com.execube.genesis.model.Movie;
import com.execube.genesis.model.TMDBResponse;
import com.execube.genesis.network.API;
import com.execube.genesis.utils.AppConstants;
import com.execube.genesis.utils.EndlessScrollListener;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Prateek Phoenix on 4/24/2016.
 */
public class PopularMoviesFragment extends Fragment {

    private static final String TAG = "TAG";
    private static final String POPULAR_MOVIES_ARRAY ="popular_movies" ;
    private ArrayList<Movie> mMovies;
    private RecyclerView popularMoviesList = null;

    private View progressBarPopular = null;
    private SwipeRefreshLayout layout;
    private boolean deviceIsTablet;


    // FIXME: 28/04/16 make it a field
    private GenericAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;

    public PopularMoviesFragment() {
        //empty constructor required
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View content = inflater.inflate(R.layout.fragment_popular_movies,container,false);

        popularMoviesList = content.findViewById(R.id.popular_recyclerView);
        progressBarPopular = content.findViewById(R.id.progressBar_popular);
        layout = content.findViewById(R.id.activity_main_swipe_refresh_layout);
        deviceIsTablet= getResources().getBoolean(R.bool.is_tablet);

        mAdapter = new GenericAdapter(mMovies, getActivity());

        layout.setEnabled(false);

        layout.setColorSchemeColors(getResources().getColor(R.color.accent));
        if(savedInstanceState!=null&&savedInstanceState.containsKey(POPULAR_MOVIES_ARRAY))
        {
            mMovies=savedInstanceState.getParcelableArrayList(POPULAR_MOVIES_ARRAY);
            mAdapter.setMovies(mMovies);
            Log.d(TAG, "Popular onCreate: restoring " + mMovies.size());

        }


        if ( mMovies == null ) {

            fetchData(1);

        }

        if (savedInstanceState != null) {
            progressBarPopular.setVisibility(View.GONE);
        }



        setupRecyclerView();
        return content;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(POPULAR_MOVIES_ARRAY,mMovies);//Saving state of the ArrayList to avoid the network calls.
        Log.v(TAG,"Popular Saving State");

    }
    private void setupRecyclerView() {

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            gridLayoutManager = new GridLayoutManager(getActivity(),2);
        }
        else{
            gridLayoutManager = new GridLayoutManager(getActivity(),3);
        }
        popularMoviesList.setLayoutManager(gridLayoutManager);
        popularMoviesList.addOnScrollListener(new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchData(page);
            }
        });
        mAdapter = new GenericAdapter(mMovies,getActivity());
        popularMoviesList.setAdapter(mAdapter);

    }

    private void fetchData(final int page) {

        if(page == 1)
        {
            progressBarPopular.setVisibility(View.VISIBLE);

        }else{
            layout.setEnabled(true);
            layout.setRefreshing(true);
        }

        API moviesAPI = API.retrofit.create(API.class);

        Call<TMDBResponse> call = moviesAPI.fetchPopularMovies(AppConstants.API_KEY,AppConstants.SORT_POPULARITY,page);

        call.enqueue(new Callback<TMDBResponse>() {
            @Override
            public void onResponse(Call<TMDBResponse> call, Response<TMDBResponse> response) {

                if(page>1)
                {
                    TMDBResponse result = response.body();
                    mMovies.addAll(result.getResults());
                    mAdapter.setMovies(mMovies);
                    layout.setRefreshing(false);
                    layout.setEnabled(false);
                }
                else{

                    TMDBResponse result = response.body();
                    mMovies = (ArrayList<Movie>) result.getResults();
                    progressBarPopular.setVisibility(View.GONE);
                    setupRecyclerView();
                    mAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<TMDBResponse> call, Throwable t) {
                progressBarPopular.setVisibility(View.GONE);
                layout.setEnabled(false);
                layout.setRefreshing(false);
            }
        });
    }





    public interface openDetailsListener{
        void openDetails(Movie movie,ActivityOptions options);
    }
}