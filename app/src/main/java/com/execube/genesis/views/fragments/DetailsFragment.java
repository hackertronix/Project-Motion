package com.execube.genesis.views.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.execube.genesis.R;
import com.execube.genesis.model.Movie;
import com.execube.genesis.model.Review;
import com.execube.genesis.utils.API;
import com.execube.genesis.utils.OkHttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;


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

    private ArrayList<Review> mReviews;


    public DetailsFragment() {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);


        mBackdrop = (ImageView) view.findViewById(R.id.details_poster);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        mDetailTitle = (TextView) view.findViewById(R.id.detail_title_text);
        mReleaseDate = (TextView) view.findViewById(R.id.release_date);
        mOverview = (TextView) view.findViewById(R.id.overview);
        mOverviewHeader = (TextView) view.findViewById(R.id.overview_header);

        mRatingBar = (RatingBar) view.findViewById(R.id.movie_rating);


        Bundle bundle=getArguments();
        mMovie=bundle.getParcelable("PARCEL");


        String id = String.valueOf(mMovie.getId());
        String reviewQueryUrl = API.MOVIES_BASE_URL + id + "/reviews" + API.API_KEY;
        String trailerQueryUrl = API.MOVIES_BASE_URL+id+"/videos"+API.API_KEY;

        mDetailTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mRatingBar.setProgress((int) mMovie.getVoteAverage());
        mOverview.setText(mMovie.getOverview());


        if (Build.VERSION.SDK_INT != 21) {
            Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Gotham-Rounded-Bold.ttf");
            Typeface fontMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Gotham-Rounded-Medium.ttf");
            Typeface fontMediumLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Gotham-Rounded-Book_.ttf");


            mDetailTitle.setTypeface(fontBold);
            mReleaseDate.setTypeface(fontMedium);
            mOverview.setTypeface(fontMediumLight);
            mOverviewHeader.setTypeface(fontBold);
        }


        OkHttpHandler handler = new OkHttpHandler(reviewQueryUrl, mCallback);
        handler.fetchData();

        Picasso.with(getActivity()).load(API.IMAGE_URL + API.IMAGE_SIZE_500 + mMovie.getPosterPath())
                .into(mBackdrop);
        getActivity().startPostponedEnterTransition();


        return view;
    }

    private okhttp3.Callback mCallback = new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //TODO handle failure on UI thread
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException,IllegalStateException{

            try {
                String jsonResponse= response.body().string();
                Log.v(TAG,jsonResponse );
                JSONObject jsonObject = new JSONObject(jsonResponse);
                int resultCount = jsonObject.getInt("total_results");
                if (resultCount != 0) {
                    mReviews = parseReviews(jsonObject);
                } else
                    mReviews = null;

            } catch (JSONException e) {

            }
            catch (IllegalStateException e){}
        }
    };

    private ArrayList<Review> parseReviews(JSONObject jsonObject) throws JSONException {


        ArrayList<Review> Reviews = new ArrayList<>();
        JSONArray reviewsJSONArray = jsonObject.getJSONArray("results");

        for (int i = 0; i < reviewsJSONArray.length(); i++) {

            Review review = new Review();
            JSONObject reviewJson = reviewsJSONArray.getJSONObject(i);

            review.setId(reviewJson.getInt("id"));
            review.setAuthor(reviewJson.getString("author"));
            review.setContent(reviewJson.getString("content"));
            review.setTotalResults(reviewJson.getInt("total_results"));

            Reviews.add(review);
        }

        return Reviews;
    }


}





