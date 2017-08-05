package com.execube.genesis.database;

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


        int movie_id = Integer.parseInt(id);
        Movie movie = realm.where(Movie.class)
                .equalTo("id",movie_id)
                .findFirst();

        return movie;
    }

    public void InsertMovieToDB(final Movie movie){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(movie);
                Log.d(TAG, movie.getMovieId()+" was inserted");
            }
        });
    }

    public void deleteMovieFromDB(Movie movie){

        realm.beginTransaction();
        movie.deleteFromRealm();
        realm.commitTransaction();
    }


}
