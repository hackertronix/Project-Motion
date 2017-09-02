package com.execube.genesis.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.execube.genesis.R;
import com.execube.genesis.model.Movie;
import com.execube.genesis.utils.AppConstants;
import com.execube.genesis.views.fragments.TopRatedMoviesFragment;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by hackertronix on 06/08/17.
 */

public class TopRatedMoviesAdapter extends RecyclerView.Adapter<TopRatedMoviesAdapter.TopRatedMoviesViewHolder>{

    private ArrayList<Movie> mMovies;
    private Activity mActivity;

    public TopRatedMoviesAdapter(ArrayList<Movie> mMovies, Activity mActivity) {
        this.mMovies = mMovies;
        this.mActivity = mActivity;
    }

    @Override
    public TopRatedMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,parent,false);
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

    public void setMovies(ArrayList<Movie> listMovies) {
        mMovies = listMovies;

        if( listMovies!=null)
        {
            notifyItemRangeChanged(0,listMovies.size());
            notifyDataSetChanged();
        }



    }

    public class TopRatedMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPosterImage;
        private Movie mMovie;

        public TopRatedMoviesViewHolder(View itemView) {
            super(itemView);

            mPosterImage = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            mMovie=movie;
            Picasso.with(mActivity).load(AppConstants.IMAGE_URL + AppConstants.IMAGE_SIZE_500 + movie.getPosterPath())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(mPosterImage);
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {


            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(mActivity,mPosterImage,"posterImage");
            ((TopRatedMoviesFragment.openDetailsListener)mActivity).openDetails(mMovie,options);
        }
    }
}
