package com.execube.genesis.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.execube.genesis.R;
import com.execube.genesis.model.Trailer;
import com.execube.genesis.utils.AppConstants;
import com.execube.genesis.views.fragments.DetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by hackertronix on 06/08/17.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private ArrayList<Trailer> mTrailers;
    private Activity mActivity;

    public TrailersAdapter(ArrayList<Trailer> mTrailers, Activity mActivity) {
        this.mTrailers = mTrailers;
        this.mActivity = mActivity;
    }


    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item,parent,false);
        return new TrailersViewHolder(view);    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        Trailer trailer= mTrailers.get(position);
        holder.bind(trailer);
    }

    @Override
    public int getItemCount() {
        if( mTrailers.size() == 0)
            return 0;
        else
            return mTrailers.size();

    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mTrailerThumbnail;
        private Trailer mTrailer;

        public TrailersViewHolder(View itemView) {
            super(itemView);

            mTrailerThumbnail = itemView.findViewById(R.id.trailer_thumbnail);
            itemView.setOnClickListener(this);
        }

        public void bind(Trailer trailer) {
            mTrailer = trailer;

            Picasso.with(mActivity).load(AppConstants.YOUTUBE_THUMBNAIL_URL + mTrailer.getKey() + AppConstants.THUMBNAIL_QUALITY)
                    .placeholder(R.drawable.trailer_thumbnail_placeholder).into(mTrailerThumbnail);

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.YOUTUBE_TRAILER_URL + mTrailer.getKey()));
            mActivity.startActivity(intent);

        }
    }

}
