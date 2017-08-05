package com.execube.genesis.views.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.execube.genesis.R;
import com.execube.genesis.model.Event;
import com.execube.genesis.model.Movie;
import com.execube.genesis.model.Review;
import com.execube.genesis.model.TMBDReviews;
import com.execube.genesis.model.TMDBTrailers;
import com.execube.genesis.model.Trailer;
import com.execube.genesis.network.API;
import com.execube.genesis.utils.AppConstants;
import com.execube.genesis.utils.EventBus;
import com.execube.genesis.database.MoviesDataSource;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;


/**
 * Created by Prateek Phoenix on 4/30/2016.
 */
public class DetailsFragment extends Fragment {
    private static final String TAG = "DETAILS";
    private static final int DEFAULT_NUM_COLORS = 5;

    private Movie mMovie;
    private Movie entry,tempMovie;
    public Intent intent;

    private TextView mDetailTitle;
    private TextView mReleaseDate;
    private TextView mOverview;
    private TextView mOverviewHeader;
    private TextView mReviesHeader;
    private TextView mTrailersHeader;

    private ImageView mBackdrop;
    private Toolbar mToolbar;

    private RatingBar mRatingBar;

    private ArrayList<Review> mReviews=new ArrayList<>();
    private ArrayList<Trailer> mTrailers=new ArrayList<>();
    private List<Movie> updatedFavsList= new ArrayList<>();

    public static final String MOVIE_REVIEWS_ARRAY ="movie_details";
    private static final String MOVIE_TRAILERS_ARRAY = "movie_reviews";
    private Typeface fontBold;
    private Typeface fontMediumLight;
    private Typeface fontMedium;

    private RecyclerView mReviewRecyclerView;
    private RecyclerView mTrailerRecyclerView;

    private ProgressBar mReviewsProgressbar;
    private ProgressBar mTrailersProgressbar;
    private CoordinatorLayout mCoordinatorLayout;
    private CardView mReviewsCardView;
    private FloatingActionButton mFloatingActionButton;

    private ReviewsAdapter mReviewAdapter;
    private int NumOfReviews;
    private TrailersAdapter mTrailerAdapter;
    private MoviesDataSource dataSource;

    private int id;
    private boolean isFav;
    private Movie movie;

    public DetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new MoviesDataSource();
        dataSource.open();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG,"Saving state in onSaveInstanceState");
        outState.putParcelableArrayList(MOVIE_REVIEWS_ARRAY,mReviews);
        outState.putParcelableArrayList(MOVIE_TRAILERS_ARRAY,mTrailers);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onStop() {
        super.onStop();
        dataSource.close();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);


        mBackdrop = view.findViewById(R.id.details_poster);

        mDetailTitle = view.findViewById(R.id.detail_title_text);
        mReleaseDate = view.findViewById(R.id.release_date);
        mOverview = view.findViewById(R.id.overview);
        mOverviewHeader = view.findViewById(R.id.overview_header);
        mReviesHeader= view.findViewById(R.id.review_header);
        mTrailersHeader= view.findViewById(R.id.trailer_header);

        mRatingBar = view.findViewById(R.id.movie_rating);
        mCoordinatorLayout= view.findViewById(R.id.coordinator_layout);
        mReviewRecyclerView= view.findViewById(R.id.review_recycler_view);
        mTrailerRecyclerView= view.findViewById(R.id.trailer_recycler_view);

        mReviewsProgressbar= view.findViewById(R.id.reviews_progressbar);
        mTrailersProgressbar= view.findViewById(R.id.trailers_progressbar);
        mFloatingActionButton= view.findViewById(R.id.fab);

        mReviewsCardView= view.findViewById(R.id.reviews_card);


        intent = getActivity().getIntent();
        Bundle bundle=getArguments();
        mMovie=bundle.getParcelable("PARCEL");
        tempMovie=mMovie;
        id = mMovie.getMovieId();


        checkFav();
        mFloatingActionButton.show();

        assert mMovie != null;

        //PREPPING THE URL FOR QUERY



        mDetailTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mRatingBar.setProgress((int) mMovie.getVoteAverage());
        mOverview.setText(mMovie.getOverview());


        if (Build.VERSION.SDK_INT != 21) {
            fontBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Gotham-Rounded-Bold.ttf");
            fontMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Gotham-Rounded-Medium.ttf");
            fontMediumLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Gotham-Rounded-Book_.ttf");


            mDetailTitle.setTypeface(fontBold);
            mReleaseDate.setTypeface(fontMedium);
            mOverview.setTypeface(fontMediumLight);
            mOverviewHeader.setTypeface(fontBold);
            mReviesHeader.setTypeface(fontBold);
            mTrailersHeader.setTypeface(fontBold);
        }


        if(savedInstanceState!=null&&savedInstanceState.containsKey(MOVIE_REVIEWS_ARRAY))
        {
            Log.v(TAG,"Restoring from bundle");
            mReviews=savedInstanceState.getParcelableArrayList(MOVIE_REVIEWS_ARRAY);
            mTrailers=savedInstanceState.getParcelableArrayList(MOVIE_TRAILERS_ARRAY);

            mReviewsProgressbar.setVisibility(GONE);
            mTrailersProgressbar.setVisibility(GONE);

        }

        else {

            fetchTrailers();
            fetchReviews();
        }

        Picasso.with(getActivity()).load(AppConstants.IMAGE_URL + AppConstants.IMAGE_SIZE_500 + mMovie.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(mBackdrop);
        getActivity().startPostponedEnterTransition();





        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mReviewAdapter= new ReviewsAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        LinearLayoutManager layoutmanager= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mTrailerRecyclerView.setLayoutManager(layoutmanager);
        mTrailerAdapter= new TrailersAdapter();
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        return view;
    }

    private void fetchReviews() {

        API reviewsAPI = API.retrofit.create(API.class);

        retrofit2.Call<TMBDReviews> call = reviewsAPI.fetchReviews(id,AppConstants.API_KEY);
        call.enqueue(new Callback<TMBDReviews>() {
            @Override
            public void onResponse(Call<TMBDReviews> call, Response<TMBDReviews> response) {
                TMBDReviews result = response.body();
                mReviews = (ArrayList<Review>) result.getResults();
                NumOfReviews = result.getTotal_results();

                if(NumOfReviews == 0)
                {
                    mReviewsCardView.setVisibility(GONE);
                }

                mReviewsProgressbar.setVisibility(GONE);
                mReviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TMBDReviews> call, Throwable t) {

            }
        });


    }

    private void fetchTrailers() {


        API trailersAPI = API.retrofit.create(API.class);
        Call<TMDBTrailers> call = trailersAPI.fetchTrailers(id,AppConstants.API_KEY);
        call.enqueue(new Callback<TMDBTrailers>() {
            @Override
            public void onResponse(Call<TMDBTrailers> call, Response<TMDBTrailers> response) {
                TMDBTrailers result = response.body();
                mTrailers = (ArrayList<Trailer>) result.getResults();

                mTrailersProgressbar.setVisibility(GONE);
                mTrailerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TMDBTrailers> call, Throwable t) {

            }
        });

    }

    private void checkFav() {

        //TODO 4: Fix with Realm

        movie = new Movie();
        movie = dataSource.findMovieByid(String.valueOf(id));

        if(movie==null)
        {
            Log.v(TAG,"Null");

            mFloatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        else {
            Log.v(TAG,"NOT Null");

            mFloatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO 5: Fix with Realm
                movie = dataSource.findMovieByid(String.valueOf(id));
                if(movie!=null)
                {
                    dataSource.deleteMovieFromDB(movie);

                    Event event = new Event("Database has been modified!!");
                    EventBus.getBus().post(event);

                    mFloatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Snackbar snackbar = Snackbar.make(mCoordinatorLayout,"Movie removed from Favourites!!",Snackbar.LENGTH_SHORT);
                    View view= snackbar.getView();
                    TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
                else
                {
                    entry = tempMovie;

                    ////TODO 6: Inspect Behaviour

                    dataSource.InsertMovieToDB(entry);
                    Event event = new Event("Database has been modified!!");
                    EventBus.getBus().post(event);
                    mFloatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                    Snackbar snackbar = Snackbar.make(mCoordinatorLayout,"Movie added to Favourites!!",Snackbar.LENGTH_SHORT);
                    View view= snackbar.getView();
                    TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }

            }
        });
    }



    private class ReviewViewHolder extends RecyclerView.ViewHolder{
        private TextView mAuthorText;
        private TextView mReviewText;
        private Review mReview;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorText= itemView.findViewById(R.id.author_textview);
            mReviewText= itemView.findViewById(R.id.review_textview);

        }

        public void bind(Review review)
        {
            mReview= review;

            mAuthorText.setText(mReview.getAuthor());
            mReviewText.setText(mReview.getContent());

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            {
                mAuthorText.setTypeface(fontBold);
                mReviewText.setTypeface(fontMediumLight);
            }


        }
    }

    private class ReviewsAdapter extends RecyclerView.Adapter<ReviewViewHolder>{

        @Override
        public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getActivity()).inflate(R.layout.review_item,parent,false);
            return new ReviewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ReviewViewHolder holder, int position) {
            Review review= mReviews.get(position);
            holder.bind(review);
        }

        @Override
        public int getItemCount() {
            if(mReviews==null)
            { return 0;}
            else
            {return mReviews.size();}
        }
    }

    private class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mTrailerThumbnail;
        private Trailer mTrailer;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            mTrailerThumbnail= itemView.findViewById(R.id.trailer_thumbnail);
            itemView.setOnClickListener(this);
        }

        public void bind(Trailer trailer)
        {
            mTrailer=trailer;

            Picasso.with(getActivity()).load(AppConstants.YOUTUBE_THUMBNAIL_URL+mTrailer.getKey()+ AppConstants.THUMBNAIL_QUALITY)
                    .placeholder(R.drawable.trailer_thumbnail_placeholder).into(mTrailerThumbnail);

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.YOUTUBE_TRAILER_URL+mTrailer.getKey()));
            startActivity(intent);

        }
    }

    private class TrailersAdapter extends RecyclerView.Adapter<TrailerViewHolder>
    {

        @Override
        public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getActivity()).inflate(R.layout.trailer_item,parent,false);
            return new TrailerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TrailerViewHolder holder, int position) {
            Trailer trailer= mTrailers.get(position);
            holder.bind(trailer);
        }

        @Override
        public int getItemCount() {
            return mTrailers.size();
        }
    }

}

