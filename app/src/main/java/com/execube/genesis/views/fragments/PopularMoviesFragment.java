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
public class PopularMoviesFragment extends Fragment {

    private static final String TAG = "HELLO WORLD";
    private static final String POPULAR_MOVIES_ARRAY ="popular_movies" ;
    private ArrayList<Movie> mMovies;
    private RecyclerView popularMoviesList = null;
    private View progressBarPopular = null;

    // FIXME: 28/04/16 make it a field
    private PopularMoviesAdapter mAdapter;

    public PopularMoviesFragment() {
        //empty constructor required
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if(savedInstanceState!=null&&savedInstanceState.containsKey(POPULAR_MOVIES_ARRAY))
        {
            mMovies=savedInstanceState.getParcelableArrayList(POPULAR_MOVIES_ARRAY);
            Log.d(TAG, "onCreate: restoring " + mMovies.size());
        }
        else {
            Log.d(TAG, "onCreate: network call");

            String url = API.BASE_URL+API.API_KEY+API.SORT_POPULARITY;
            OkHttpHandler handler= new OkHttpHandler(url, apiCallback);
            handler.fetchData();
        }*/
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: called");
        outState.putParcelableArrayList(POPULAR_MOVIES_ARRAY,mMovies);//Saving state of the ArrayList to avoid the network calls.
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_popular_movies,container,false);
        popularMoviesList = (RecyclerView) content.findViewById(R.id.popular_recyclerView);
        progressBarPopular = content.findViewById(R.id.progressBar_popular);


        if(savedInstanceState!=null&&savedInstanceState.containsKey(POPULAR_MOVIES_ARRAY))
        {
            mMovies=savedInstanceState.getParcelableArrayList(POPULAR_MOVIES_ARRAY);
            Log.d(TAG, "onCreate: restoring " + mMovies.size());
        }
        else {
            Log.d(TAG, "onCreate: network call");

            String url = API.BASE_URL+API.API_KEY+API.SORT_POPULARITY;
            OkHttpHandler handler= new OkHttpHandler(url, apiCallback);
            handler.fetchData();
        }




        // FIXME: 28/04/16 hide the progress bar
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


        mAdapter = new PopularMoviesAdapter();
        popularMoviesList.setAdapter(mAdapter);
        return content;
    }



    private Callback apiCallback =  new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //TODO show error message from here but in UI thread
            Log.e(TAG, "onFailure: " + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                Log.d(TAG, "onResponse: parsing movies");
                mMovies= parseItems(response.body().string());
            } catch (Exception e) {
                Log.v(TAG,"Exception caught: ",e);
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: progress bar to visible");
                    progressBarPopular.setVisibility(View.GONE);
                    if(mAdapter!=null)
                    {

                        mAdapter.notifyDataSetChanged();

                    }
                }
            });
        }
    };

    private class PopularMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mPosterImage;
        private Movie mMovie;

        public PopularMoviesViewHolder(View itemView) {
            super(itemView);

            mPosterImage= (ImageView)itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }


        public void bind(Movie movie) {

            mMovie=movie;
            Picasso.with(getActivity()).load(API.IMAGE_URL+API.IMAGE_SIZE_185+movie.getPosterPath())
                    .into(mPosterImage);
            Log.v(TAG,"Done loading images");
        }
        @Override
        public void onClick(View v) {
            Intent intent= new Intent(getActivity(),DetailsActivity.class);
            intent.putExtra("PARCEL",mMovie);
            startActivity(intent);
        }


    }

    private class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesViewHolder>
    {


        @Override
        public PopularMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getActivity()).inflate(R.layout.movie_item,parent,false);

            return new PopularMoviesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PopularMoviesViewHolder holder, int position) {
             Movie movie=mMovies.get(position);
             holder.bind(movie);

        }

        @Override
        public int getItemCount() {
            // FIXME: 28/04/16
            if (mMovies == null) {
                return 0;
            } else {
                return mMovies.size();
            }
        }
    }


    private ArrayList<Movie> parseItems( String jsonResponse) throws JSONException{

        JSONObject jsonData= new JSONObject(jsonResponse);
        JSONArray moviesJSONArray= jsonData.getJSONArray("results");
        ArrayList<Movie> Movies= new ArrayList<>();
        for (int i = 0; i <moviesJSONArray.length() ; i++) {

            Movie movie= new Movie();
            JSONObject movieJson= moviesJSONArray.getJSONObject(i);

            movie.setId(movieJson.getInt("id"));
            movie.setOriginalTitle(movieJson.getString("original_title"));
            movie.setTitle(movieJson.getString("title"));
            movie.setPosterPath(movieJson.getString("poster_path"));
            movie.setOverview(movieJson.getString("overview"));
            movie.setVoteAverage((float) movieJson.getDouble("vote_average"));


            Movies.add(movie);
        }

        return  Movies;
    }
}