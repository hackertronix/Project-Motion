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
import com.execube.genesis.views.fragments.FavouritesFragment;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by hackertronix on 06/08/17.
 */

public class FavouritesAdapter
    extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

  private ArrayList<Movie> mMovies;

  private Activity mActivity;

  public FavouritesAdapter(ArrayList<Movie> mMovies, Activity mActivity) {
    this.mMovies = mMovies;
    this.mActivity = mActivity;
  }

  @Override public FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
    return new FavouritesViewHolder(view);
  }

  @Override public void onBindViewHolder(FavouritesViewHolder holder, int position) {

    Movie movie = mMovies.get(position);
    holder.bind(movie);
  }

  @Override public int getItemCount() {
    return mMovies.size();
  }

  public class FavouritesViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {
    private ImageView mPosterImageView;
    private Movie mMovie;

    public FavouritesViewHolder(View itemView) {
      super(itemView);
      mPosterImageView = itemView.findViewById(R.id.poster);
      itemView.setOnClickListener(this);
    }

    public void bind(Movie movie) {
      mMovie = movie;
      Picasso.with(mActivity)
          .load(AppConstants.IMAGE_URL + AppConstants.IMAGE_SIZE_500 + mMovie.getPosterPath())
          .placeholder(R.drawable.placeholder)
          .error(R.drawable.error)
          .into(mPosterImageView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) @Override public void onClick(View v) {

      ActivityOptions options =
          ActivityOptions.makeSceneTransitionAnimation(mActivity, mPosterImageView, "posterImage");
      ((FavouritesFragment.openDetailsListener) mActivity).openDetails(mMovie, options);
    }
  }
}
