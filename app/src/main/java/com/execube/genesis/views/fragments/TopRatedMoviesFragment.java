package com.execube.genesis.views.fragments;

import android.content.Intent;
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
import com.execube.genesis.utils.API;
import com.execube.genesis.utils.OkHttpHandler;
import com.execube.genesis.views.activities.DetailsActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Prateek Phoenix on 4/24/2016.
 */
public class TopRatedMoviesFragment extends Fragment {

    private static final String TAG = "TOP";
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
        topRatedMoviesList = (RecyclerView) content.findViewById(R.id.top_rated_recyclerView);
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


            String url = API.BASE_URL + API.API_KEY + API.SORT_R_RATED;
            OkHttpHandler handler = new OkHttpHandler(url, apiCallback);
            handler.fetchData();//TODO use http caching

        }


        if(savedInstanceState!=null)
        {
            progressBarTopRated.setVisibility(View.GONE);
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


    private Callback apiCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //TODO show error message from here but in UI thread
            Log.e(TAG, "onFailure: " + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                mMovies = parseItems(response.body().string());
            } catch (Exception e) {
                Log.v(TAG, "Exception caught: ", e);
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBarTopRated.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private class TopRatedMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mPosterImage;
        private Movie mMovie;

        public TopRatedMoviesViewHolder(View itemView) {
            super(itemView);

            mPosterImage = (ImageView) itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            mMovie=movie;
            Picasso.with(getActivity()).load(API.IMAGE_URL + API.IMAGE_SIZE_500 + movie.getPosterPath())
                    .into(mPosterImage);
        }

        @Override
        public void onClick(View v) {


            Intent intent= new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("PARCEL",mMovie);
            startActivity(intent);
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

    private ArrayList<Movie> parseItems(String jsonResponse) throws JSONException {
        //TODO better make ti generic and move it to a parser or utils class
        JSONObject jsonData = new JSONObject(jsonResponse);
        JSONArray moviesJSONArray = jsonData.getJSONArray("results");
        ArrayList<Movie> Movies = new ArrayList<>();
        for (int i = 0; i < moviesJSONArray.length(); i++) {

            Movie movie = new Movie();
            JSONObject movieJson = moviesJSONArray.getJSONObject(i);

            movie.setId(movieJson.getInt("id"));
            movie.setOriginalTitle(movieJson.getString("original_title"));//TODO use constants
            movie.setOverview(movieJson.getString("overview"));
            movie.setPosterPath(movieJson.getString("poster_path"));
            movie.setVoteAverage((float) movieJson.getDouble("vote_average"));
            movie.setTitle(movieJson.getString("title"));
            movie.setBackdropPath(movieJson.getString("backdrop_path"));
            movie.setReleaseDate(movieJson.getString("release_date"));
            Movies.add(movie);
        }

        return Movies;
    }
}

