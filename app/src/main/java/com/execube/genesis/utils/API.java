package com.execube.genesis.utils;

/**
 * Created by Prateek Phoenix on 4/24/2016.
 */
public class API {



    public static String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    public static String API_KEY = "api_key=YOUR_API_KEY_GOES_HERE";
    public static String SORT_POPULARITY = "&sort_by=popularity.desc";
    public static String SORT_R_RATED = "&certification_country=US&certification=R&sort_by=vote_average.desc&vote_count.gte=250";
    public static String IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static String IMAGE_SIZE_500 = "w500/"; //Because nobody likes blurry images

   }
