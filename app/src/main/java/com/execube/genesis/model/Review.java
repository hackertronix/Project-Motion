package com.execube.genesis.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prateek Phoenix on 5/22/2016.
 */
public class Review implements Parcelable{


    private String mId;
    private String mAuthor;
    private String mContent;



    public Review() {
    }

    private Review(Parcel source)
    {
        mId=source.readString();
        mAuthor=source.readString();
        mContent=source.readString();

    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }


    public static Parcelable.Creator<Review> CREATOR= new Parcelable.Creator<Review>()
    {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
