package com.execube.genesis.utils;

import android.util.Log;

import com.execube.genesis.model.Movie;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by hackertronix on 03/08/17.
 */

public class MoviesDataSource {

    Realm realm;
    public static final String TAG = "DATA_TAG";

    public MoviesDataSource() {
    }



    public void open(){
        realm= Realm.getDefaultInstance();
        Log.d(TAG,"Realm opened");
    }


    public void close(){
        realm.close();
        Log.d(TAG, "Realm closed");
    }

    public ArrayList<Movie> getAllMovies(){

        RealmResults<Movie> movies = realm.where(Movie.class).findAll();
        ArrayList<Movie> moviesList = new ArrayList<>();
        moviesList.addAll(realm.copyFromRealm(movies));

        return moviesList;

    }

    public Movie findMovieByid(String id) {

        Movie movie = realm.where(Movie.class)
                .equalTo("mId",id)
                .findFirst();

        return movie;
    }
}
