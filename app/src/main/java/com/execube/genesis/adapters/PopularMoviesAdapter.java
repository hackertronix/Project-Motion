package com.execube.genesis.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.execube.genesis.R;
import com.execube.genesis.model.Movie;
import com.execube.genesis.utils.AppConstants;
import com.execube.genesis.views.fragments.PopularMoviesFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by hackertronix on 06/08/17.
 */

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesViewHolder> {

    private ArrayList<Movie> mMovies;
    private Activity mActivity;

    public PopularMoviesAdapter(ArrayList<Movie> mMovies, Activity activity) {
        this.mMovies = mMovies;
        this.mActivity = activity;
    }

    @Override
    public PopularMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,parent,false);

        return new PopularMoviesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(PopularMoviesViewHolder holder, int position) {

        Movie movie = mMovies.get(position);
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

    public void setMovies(ArrayList<Movie> listOfMovies) {
        mMovies = listOfMovies;
        notifyItemRangeChanged(0, listOfMovies.size());
        notifyDataSetChanged();

    }


    public class PopularMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPosterImage;
        private Movie mMovie;


        public PopularMoviesViewHolder(View itemView) {
            super(itemView);
            mPosterImage= itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }


        public void bind(Movie movie) {

            mMovie=movie;
            Picasso.with(mActivity).load(AppConstants.IMAGE_URL+ AppConstants.IMAGE_SIZE_500 +movie.getPosterPath())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(mPosterImage);
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View view) {

            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(mActivity,mPosterImage,"posterImage");
            ((PopularMoviesFragment.openDetailsListener)mActivity).openDetails(mMovie,options);


        }
    }
}
