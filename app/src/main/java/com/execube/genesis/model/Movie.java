package com.execube.genesis.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prateek Phoenix on 4/24/2016.
 */
public class Movie implements Parcelable {

    private int mId;
    private String mOriginalTitle;
    private String mTitle;
    private  String mPosterPath;
    private  String mOverview;
    private  float mVoteAverage;


    private Movie(Parcel source) {

        mId=source.readInt();
        mOriginalTitle=source.readString();
        mTitle=source.readString();
        mPosterPath=source.readString();
        mOverview=source.readString();
        mVoteAverage=source.readFloat();
    }

    public Movie() {

    }



    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public float getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        mVoteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeFloat(mVoteAverage);
    }

    public static final Parcelable.Creator<Movie> CREATOR= new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            // FIXME: 28/04/16 it was creating empty array before
            return new Movie[size];
        }
    };


}