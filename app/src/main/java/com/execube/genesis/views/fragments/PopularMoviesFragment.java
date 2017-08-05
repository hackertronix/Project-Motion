package com.execube.genesis.views.fragments;

import android.app.ActivityOptions;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.execube.genesis.R;
import com.execube.genesis.adapters.PopularMoviesAdapter;
import com.execube.genesis.model.Movie;
import com.execube.genesis.model.TMDBResponse;
import com.execube.genesis.network.API;
import com.execube.genesis.utils.AppConstants;
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
    private boolean deviceIsTablet;


    // FIXME: 28/04/16 make it a field
    private PopularMoviesAdapter mAdapter;

    public PopularMoviesFragment() {
        //empty constructor required
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View content = inflater.inflate(R.layout.fragment_popular_movies,container,false);


        popularMoviesList = content.findViewById(R.id.popular_recyclerView);
        progressBarPopular = content.findViewById(R.id.progressBar_popular);
        deviceIsTablet= getResources().getBoolean(R.bool.is_tablet);


        if(savedInstanceState!=null&&savedInstanceState.containsKey(POPULAR_MOVIES_ARRAY))
        {
            mMovies=savedInstanceState.getParcelableArrayList(POPULAR_MOVIES_ARRAY);
         Log.d(TAG, "Popular onCreate: restoring " + mMovies.size());

        }


        if ( mMovies == null ) {

            fetchData();

        } else {

            setupRecyclerView();
        }

        if (savedInstanceState != null) {
            progressBarPopular.setVisibility(View.GONE);
        }

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            popularMoviesList.setLayoutManager(new
                    GridLayoutManager(getActivity(), 2));
        }
        else{
            popularMoviesList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }

        setupRecyclerView();
        return content;
    }

    private void setupRecyclerView() {

        mAdapter = new PopularMoviesAdapter(mMovies,getActivity());
        popularMoviesList.setAdapter(mAdapter);
    }

    private void fetchData() {

        progressBarPopular.setVisibility(View.VISIBLE);

        API moviesAPI = API.retrofit.create(API.class);

        Call<TMDBResponse> call = moviesAPI.fetchPopularMovies(AppConstants.API_KEY,AppConstants.SORT_POPULARITY,1);

        call.enqueue(new Callback<TMDBResponse>() {
            @Override
            public void onResponse(Call<TMDBResponse> call, Response<TMDBResponse> response) {
                TMDBResponse result = response.body();
                mMovies = (ArrayList<Movie>) result.getResults();
                progressBarPopular.setVisibility(View.GONE);
                setupRecyclerView();
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<TMDBResponse> call, Throwable t) {
                progressBarPopular.setVisibility(View.GONE);

            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(POPULAR_MOVIES_ARRAY,mMovies);//Saving state of the ArrayList to avoid the network calls.
        super.onSaveInstanceState(outState);
        Log.v(TAG,"Popular Saving State");

    }


    public interface openDetailsListener{
        void openDetails(Movie movie,ActivityOptions options);
    }
}