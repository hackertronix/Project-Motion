package com.execube.genesis.views;

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

    private static final String TAG = "HELLO WORLD";
    private ArrayList<Movie> mMovies;//where are you using this ?
    private RecyclerView topRatedMoviesList = null;
    private View progressBarTopRated = null;

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

            Movies.add(movie);
        }

        return Movies;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_top_rated_movies, container, false);
        topRatedMoviesList = (RecyclerView) content.findViewById(R.id.top_rated_recyclerView);
        progressBarTopRated = content.findViewById(R.id.progressBar_top_rated);

        String url = API.BASE_URL + API.API_KEY + API.SORT_R_RATED;
        OkHttpHandler handler = new OkHttpHandler(url, apiCallback);
        handler.fetchData();//TODO use http caching

        PopularMoviesAdapter mAdapter = new PopularMoviesAdapter();

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            topRatedMoviesList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        else{
            topRatedMoviesList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }


        topRatedMoviesList.setAdapter(mAdapter);

        return content;
    }


    private Callback apiCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //TODO show error message from here but in UI thread
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
                    topRatedMoviesList.setVisibility(View.VISIBLE);
                    //TODO add adapter
                }
            });
        }
    };

    private class PopularMoviesViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPosterImage;

        public PopularMoviesViewHolder(View itemView) {
            super(itemView);

            mPosterImage = (ImageView) itemView.findViewById(R.id.poster);
        }
    }

    private class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesViewHolder> {


        @Override
        public PopularMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.movie_item, parent, false);

            return new PopularMoviesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PopularMoviesViewHolder holder, int position) {
            final Movie movie = mMovies.get(position);

            Picasso.with(getActivity()).load(API.IMAGE_URL + API.IMAGE_SIZE_185 + movie.getPosterPath())
                    .into(holder.mPosterImage);
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }
    }
}

