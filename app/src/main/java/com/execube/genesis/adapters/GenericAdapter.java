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
 * Created by hackertronix on 02/09/17.
 */

public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.GenericViewHolder>{

  private ArrayList<Movie> mMovies;
  private Activity mActivity;

  public GenericAdapter(ArrayList<Movie> mMovies, Activity mActivity) {
    this.mMovies = mMovies;
    this.mActivity = mActivity;
  }

  @Override public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);

    return new GenericViewHolder(view);
  }

  @Override public void onBindViewHolder(GenericViewHolder holder, int position) {

    Movie movie = mMovies.get(position);
    holder.bind(movie);
  }

  @Override public int getItemCount() {

    if (mMovies == null) {
      return 0;
    } else {
      return mMovies.size();
    }

  }

  public void setMovies(ArrayList<Movie> listOfMovies) {
    mMovies = listOfMovies;
    if(listOfMovies!=null)
    {
      notifyItemRangeChanged(0, listOfMovies.size());
      notifyDataSetChanged();
    }



  }

  public class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView mPosterImageView;
    private Movie mMovie;

    public GenericViewHolder(View itemView) {
      super(itemView);

      mPosterImageView = itemView.findViewById(R.id.poster);
      mPosterImageView.setOnClickListener(this);
    }

    public void bind(Movie movie) {
      mMovie = movie;
      Picasso.with(mActivity)
          .load(AppConstants.IMAGE_URL + AppConstants.IMAGE_SIZE_500 + movie.getPosterPath())
          .placeholder(R.drawable.placeholder)
          .error(R.drawable.error)
          .into(mPosterImageView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override public void onClick(View view) {

      ActivityOptions options =
          ActivityOptions.makeSceneTransitionAnimation(mActivity, mPosterImageView, "posterImage");
      ((TopRatedMoviesFragment.openDetailsListener) mActivity).openDetails(mMovie, options);
    }
  }
}
