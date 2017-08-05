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
import android.widget.ImageView;

import com.execube.genesis.R;
import com.execube.genesis.model.Movie;
import com.execube.genesis.model.TMDBResponse;
import com.execube.genesis.network.API;
import com.execube.genesis.utils.AppConstants;
import com.squareup.picasso.Picasso;

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
    private TopRatedMoviesAdapter mAdapter;



    public TopRatedMoviesFragment() {
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(TAG,"TopRated OnCreate");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG,"TopRated OnPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG,"TopRated OnResume");
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


        // restore from savedInstanceState if  state was saved
        if(savedInstanceState!=null&&savedInstanceState.containsKey(TOP_RATED_MOVIES_ARRAY))
        {
            mMovies=savedInstanceState.getParcelableArrayList(TOP_RATED_MOVIES_ARRAY);
            Log.d(TAG, "onCreate: restoring " + mMovies.size());
        }

        //Do a fresh network fetch if savedInstanceState == null
        else{
            Log.d(TAG, "onCreate: network call");

            fetchData();


        }



        if(savedInstanceState!=null)
        {
            progressBarTopRated.setVisibility(GONE);
        }

        // CHECKING FOR DEVICE ORIENTATION TO SET NUMBER OF GRID VIEW COLUMNS
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            topRatedMoviesList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        else{
            topRatedMoviesList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }


        mAdapter=new TopRatedMoviesAdapter();
        topRatedMoviesList.setAdapter(mAdapter);

        return content;
    }

    private void fetchData() {

        API moviesAPI = API.retrofit.create(API.class);
        Call<TMDBResponse> call = moviesAPI.fetchTopRatedMovies(AppConstants.API_KEY,AppConstants.SORT_R_RATED,1);

        call.enqueue(new Callback<TMDBResponse>() {
            @Override
            public void onResponse(Call<TMDBResponse> call, Response<TMDBResponse> response) {
                TMDBResponse result = response.body();
                mMovies = (ArrayList<Movie>) result.getResults();
                progressBarTopRated.setVisibility(GONE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TMDBResponse> call, Throwable t) {

            }
        });

    }


    private class TopRatedMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mPosterImage;
        private Movie mMovie;

        public TopRatedMoviesViewHolder(View itemView) {
            super(itemView);

            mPosterImage = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            mMovie=movie;
            Picasso.with(getActivity()).load(AppConstants.IMAGE_URL + AppConstants.IMAGE_SIZE_500 + movie.getPosterPath())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(mPosterImage);
        }

        @Override
        public void onClick(View v) {


            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(getActivity(),mPosterImage,"posterImage");
            ((openDetailsListener)getActivity()).openDetails(mMovie,options);
        }


    }

    private class TopRatedMoviesAdapter extends RecyclerView.Adapter<TopRatedMoviesViewHolder> {


        @Override
        public TopRatedMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.movie_item, parent, false);

            return new TopRatedMoviesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TopRatedMoviesViewHolder holder, int position) {
            Movie movie = mMovies.get(position);


            holder.bind(movie);

        }

        @Override
        public int getItemCount() {
            if(mMovies==null)
                return 0;
            else
                return mMovies.size();
        }
    }


    public interface openDetailsListener{
        void openDetails(Movie movie,ActivityOptions options);
    }
}