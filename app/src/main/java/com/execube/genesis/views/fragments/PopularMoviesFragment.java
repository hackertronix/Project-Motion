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
import com.execube.genesis.utils.API;
import com.execube.genesis.utils.JSONParser;
import com.execube.genesis.utils.OkHttpHandler;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
        popularMoviesList = (RecyclerView) content.findViewById(R.id.popular_recyclerView);
        progressBarPopular = content.findViewById(R.id.progressBar_popular);
        deviceIsTablet= getResources().getBoolean(R.bool.is_tablet);

        if(savedInstanceState!=null&&savedInstanceState.containsKey(POPULAR_MOVIES_ARRAY))
        {
            mMovies=savedInstanceState.getParcelableArrayList(POPULAR_MOVIES_ARRAY);
         Log.d(TAG, "Popular onCreate: restoring " + mMovies.size());
        }
        else {

           Log.d(TAG, "Popular onCreate: network call");


            String url = API.BASE_URL+API.POPULAR+API.API_KEY+API.SORT_POPULARITY;
            OkHttpHandler handler= new OkHttpHandler(url, apiCallback);
            handler.fetchData();
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


        mAdapter = new PopularMoviesAdapter();
        popularMoviesList.setAdapter(mAdapter);
        return content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(TAG,"Popular OnCreate");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG,"Popular OnPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG,"Popular OnResume");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(POPULAR_MOVIES_ARRAY,mMovies);//Saving state of the ArrayList to avoid the network calls.
        super.onSaveInstanceState(outState);
        Log.v(TAG,"Popular Saving State");

    }


    private Callback apiCallback =  new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //TODO show error message from here but in UI thread
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                JSONParser parser= new JSONParser();
                String newtworkResponse= response.body().string();
                mMovies= parser.parseMovies(newtworkResponse);

            } catch (Exception e) { Log.v(TAG, "Exception caught: ", e);
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBarPopular.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();


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
            Picasso.with(getActivity()).load(API.IMAGE_URL+API.IMAGE_SIZE_500 +movie.getPosterPath())
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

            if (mMovies == null) {
                return 0;
            } else {
                return mMovies.size();
            }
        }
    }



    public interface openDetailsListener{
        void openDetails(Movie movie,ActivityOptions options);
    }
}