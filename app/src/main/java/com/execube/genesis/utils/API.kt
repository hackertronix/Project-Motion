package com.execube.genesis.utils

import com.execube.genesis.BuildConfig
import com.execube.genesis.model.ApiResponse
import com.execube.genesis.model.Movie
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface API{

  @GET("popular")
  fun getPopularMovies(@Query("api_key") apiKey: String = BuildConfig.API_KEY, @Query("sort_by") sortBy : String="popularity.desc"): Single<ApiResponse>

  companion object {
    fun create(): API{
      val retrofit = Retrofit.Builder()
          .baseUrl(Constants.BASE_URL)
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .build()

      return retrofit.create(API::class.java)
    }
  }
}