package com.execube.genesis.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.execube.genesis.R;
import com.execube.genesis.model.Review;
import com.execube.genesis.views.fragments.DetailsFragment;

import java.util.ArrayList;

/**
 * Created by hackertronix on 06/08/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {


    private ArrayList<Review> mReviews;
    private Activity mActivity;

    public ReviewsAdapter(ArrayList<Review> mReviews, Activity mActivity) {
        this.mReviews = mReviews;
        this.mActivity = mActivity;
    }


    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {


        Review review = mReviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        if( mReviews.size() == 0)
            return 0;
        else
            return mReviews.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        private TextView mAuthorText;
        private TextView mReviewText;
        private Review mReview;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            mAuthorText = itemView.findViewById(R.id.author_textview);
            mReviewText = itemView.findViewById(R.id.review_textview);

        }

        public void bind(Review review) {
            mReview = review;

            mAuthorText.setText(mReview.getAuthor());
            mReviewText.setText(mReview.getContent());

            Typeface fontBold = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Gotham-Rounded-Bold.ttf");
            Typeface fontMediumLight = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Gotham-Rounded-Book_.ttf");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAuthorText.setTypeface(fontBold);
                mReviewText.setTypeface(fontMediumLight);
            }


        }
    }
}


