package com.execube.genesis.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prateek Phoenix on 5/22/2016.
 */
public class Trailer implements Parcelable{

    @SerializedName("id")
    public String mId;

    @SerializedName("key")
    public String mKey;

    @SerializedName("name")
    public String mName;

    private Trailer(Parcel source)
    {
        mId=source.readString();
        mKey=source.readString();
        mName=source.readString();

    }

    public Trailer()
    {

    }



    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mId);
        dest.writeString(mKey);
        dest.writeString(mName);

    }


    public static final Parcelable.Creator<Trailer> CREATOR= new Parcelable.Creator<Trailer>(){

        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
