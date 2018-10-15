package com.execube.genesis.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
  @SerializedName("overview")
  var overview: String = "",
  @SerializedName("original_language")
  var originalLanguage: String = "",
  @SerializedName("original_title")
  var originalTitle: String = "",
  @SerializedName("video")
  var video: Boolean = false,
  @SerializedName("title")
  var title: String = "",
  @SerializedName("poster_path")
  var posterPath: String = "",
  @SerializedName("backdrop_path")
  var backdropPath: String = "",
  @SerializedName("release_date")
  var releaseDate: String = "",
  @SerializedName("vote_average")
  var voteAverage: Double = 0.0,
  @SerializedName("popularity")
  var popularity: Double = 0.0,
  @SerializedName("id")
  var id: Int = 0,
  @SerializedName("adult")
  var adult: Boolean = false,
  @SerializedName("vote_count")
  var voteCount: Int = 0,
  var isFavourite: Boolean = false
) : Parcelable