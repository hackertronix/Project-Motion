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
import com.execube.genesis.adapters.TopRatedMoviesAdapter;
import com.execube.genesis.model.Movie;
import com.execube.genesis.model.TMDBResponse;
import com.execube.genesis.network.API;
import com.execube.genesis.utils.AppConstants;
import com.execube.genesis.utils.EndlessScrollListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;


/**
 * Created by Prateek Phoenix on 4/24/2016.
 */
public class TopRatedMoviesFragment extends Fragment {

    private static final String TAG = "TAG";
    public static final String TOP_RATED_MOVIES_ARRAY="top_rated_movies";

    private ArrayList<Movie> mMovies;
    private RecyclerView topRatedMoviesList = null;
    private View progressBarTopRated = null;
    private SwipeRefreshLayout layout;
    private TopRatedMoviesAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;



    public TopRatedMoviesFragment() {
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.d(TAG, "onSaveInstanceState: called");
        outState.putParcelableArrayList(TOP_RATED_MOVIES_ARRAY,mMovies);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_top_rated_movies, container, false);
        topRatedMoviesList = content.findViewById(R.id.top_rated_recyclerView);
        progressBarTopRated = content.findViewById(R.id.progressBar_top_rated);
        layout = content.findViewById(R.id.top_rated_swipeRefreshLayout);

        layout.setEnabled(false);



        mAdapter = new TopRatedMoviesAdapter(mMovies,getActivity());

        // restore from savedInstanceState if  state was saved
        if(savedInstanceState!=null&&savedInstanceState.containsKey(TOP_RATED_MOVIES_ARRAY))
        {
            mMovies=savedInstanceState.getParcelableArrayList(TOP_RATED_MOVIES_ARRAY);
            Log.d(TAG, "onCreate: restoring " + mMovies.size());
        }

        //Do a fresh network fetch if savedInstanceState == null
        if( mMovies == null){
            Log.d(TAG, "onCreate: network call");

            fetchData(1);
        }



        if(savedInstanceState!=null)
        {
            progressBarTopRated.setVisibility(GONE);
        }

        // CHECKING FOR DEVICE ORIENTATION TO SET NUMBER OF GRID VIEW COLUMNS



        setupRecyclerView();
        return content;

    }

    private void setupRecyclerView() {

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            gridLayoutManager = new GridLayoutManager(getActivity(),2);

        }
        else{
            gridLayoutManager = new GridLayoutManager(getActivity(),3);
        }

        topRatedMoviesList.setLayoutManager(gridLayoutManager);
        topRatedMoviesList.addOnScrollListener(new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchData(page);
            }
        });
        mAdapter=new TopRatedMoviesAdapter(mMovies,getActivity());
        topRatedMoviesList.setAdapter(mAdapter);

    }

    private void fetchData(final int page) {

        if(page == 1)
        {
            progressBarTopRated.setVisibility(View.VISIBLE);

        }else {
            layout.setEnabled(true);
            layout.setRefreshing(true);
        }

        API moviesAPI = API.retrofit.create(API.class);
        Call<TMDBResponse> call = moviesAPI.fetchTopRatedMovies(AppConstants.API_KEY,AppConstants.SORT_R_RATED,page);

        call.enqueue(new Callback<TMDBResponse>() {
            @Override
            public void onResponse(Call<TMDBResponse> call, Response<TMDBResponse> response) {
                if(page > 1)
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
                    progressBarTopRated.setVisibility(GONE);
                    setupRecyclerView();
                    mAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<TMDBResponse> call, Throwable t) {

                progressBarTopRated.setVisibility(View.GONE);
                layout.setEnabled(false);
                layout.setRefreshing(false);
            }
        });

    }

    public interface openDetailsListener{
        void openDetails(Movie movie,ActivityOptions options);
    }
}