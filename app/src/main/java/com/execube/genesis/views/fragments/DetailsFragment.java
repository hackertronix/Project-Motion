package com.execube.genesis.views.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.execube.genesis.R;
import com.execube.genesis.model.Movie;
import com.execube.genesis.utils.API;
import com.mikepenz.materialize.color.Material;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Prateek Phoenix on 4/30/2016.
 */
public class DetailsFragment extends Fragment {
    private static final String TAG = "DETAILS";
    private static final int DEFAULT_NUM_COLORS = 5;
    private Movie mMovie;
    public Intent intent;

    private TextView mDetailTitle;
    private TextView mReleaseDate;
    private TextView mOverview;
    private TextView mOverviewHeader;


    private ImageView mBackdrop;
    private Toolbar mToolbar;

    private RatingBar mRatingBar;


    public DetailsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_detail,container,false);

        mBackdrop=(ImageView)view.findViewById(R.id.details_poster);
        mToolbar=(Toolbar)view.findViewById(R.id.toolbar);

        mDetailTitle=(TextView)view.findViewById(R.id.detail_title_text);
        mReleaseDate=(TextView)view.findViewById(R.id.release_date);
        mOverview=(TextView)view.findViewById(R.id.overview);
        mOverviewHeader=(TextView)view.findViewById(R.id.overview_header);

        mRatingBar=(RatingBar)view.findViewById(R.id.movie_rating);

        intent= getActivity().getIntent();
        mMovie=intent.getExtras().getParcelable("PARCEL");

        Typeface fontBold= Typeface.createFromAsset(getActivity().getAssets(),"fonts/Gotham-Rounded-Bold.ttf");
        Typeface fontMedium= Typeface.createFromAsset(getActivity().getAssets(),"fonts/Gotham-Rounded-Medium.ttf");

        Typeface fontBold2= Typeface.createFromAsset(getActivity().getAssets(),"fonts/RobotoMono-Medium.ttf");
        Typeface fontMedium2= Typeface.createFromAsset(getActivity().getAssets(),"fonts/RobotoMono-Regular.ttf");
        Typeface fontMedium3= Typeface.createFromAsset(getActivity().getAssets(),"fonts/Gotham-Rounded-Book_.ttf");



        Picasso.with(getActivity()).load(API.IMAGE_URL+API.IMAGE_SIZE_500 +mMovie.getPosterPath())
                .into(mBackdrop);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        mDetailTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mRatingBar.setProgress((int)mMovie.getVoteAverage());
        mOverview.setText(mMovie.getOverview());

        mDetailTitle.setTypeface(fontBold);
        mReleaseDate.setTypeface(fontMedium);
        mOverview.setTypeface(fontMedium3);
        mOverviewHeader.setTypeface(fontBold);




        return view;
    }


}
