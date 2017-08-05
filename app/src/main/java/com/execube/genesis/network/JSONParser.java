package com.execube.genesis.network;

import com.execube.genesis.model.Movie;
import com.execube.genesis.model.Review;
import com.execube.genesis.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Prateek Phoenix on 5/31/2016.
 */
public class JSONParser {

    public ArrayList<Movie> parseMovies(String jsonResponse) throws JSONException {

        JSONObject jsonData= new JSONObject(jsonResponse);
        JSONArray moviesJSONArray= jsonData.getJSONArray("results");
        ArrayList<Movie> Movies= new ArrayList<>();
        for (int i = 0; i <moviesJSONArray.length() ; i++) {

            Movie movie= new Movie();
            JSONObject movieJson= moviesJSONArray.getJSONObject(i);

            movie.setMovieId(movieJson.getInt("id"));
            movie.setOriginalTitle(movieJson.getString("original_title"));
            movie.setTitle(movieJson.getString("title"));
            movie.setPosterPath(movieJson.getString("poster_path"));
            movie.setOverview(movieJson.getString("overview"));
            movie.setVoteAverage((float) movieJson.getDouble("vote_average"));
            movie.setBackdropPath(movieJson.getString("backdrop_path"));
            movie.setReleaseDate(movieJson.getString("release_date"));


            Movies.add(movie);
        }

        return  Movies;
    }



    public ArrayList<Review> parseReviews(String JSONData) throws JSONException {


        JSONObject jsonData= new JSONObject(JSONData);
        JSONArray reviewsJSONArray=jsonData.getJSONArray("results");
        ArrayList<Review> Reviews=new ArrayList<>();
        for (int i = 0; i < reviewsJSONArray.length(); i++) {

            Review review = new Review();
            JSONObject reviewJson = reviewsJSONArray.getJSONObject(i);

            review.setId(reviewJson.getString("id"));
            review.setAuthor(reviewJson.getString("author"));
            review.setContent(reviewJson.getString("content"));


            Reviews.add(review);
        }

        return Reviews;
    }


    public ArrayList<Trailer> parseTrailers(String json1) throws JSONException {

        JSONObject jsonData= new JSONObject(json1);
        JSONArray videosJsonArray= jsonData.getJSONArray("results");
        ArrayList<Trailer> Trailer= new ArrayList<>();
        for (int i = 0; i < videosJsonArray.length(); i++) {
            Trailer trailer= new Trailer();

            JSONObject trailerJson= videosJsonArray.getJSONObject(i);

            trailer.setId(trailerJson.getString("id"));
            trailer.setName(trailerJson.getString("name"));
            trailer.setKey(trailerJson.getString("key"));

            Trailer.add(trailer);
        }

        return Trailer;
    }

}
