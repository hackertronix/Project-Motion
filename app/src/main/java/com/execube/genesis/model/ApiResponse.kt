package com.execube.genesis.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(@SerializedName("page")
                    val page: Int = 0,
                    @SerializedName("total_pages")
                    val totalPages: Int = 0,
                    @SerializedName("results")
                    val results: ArrayList<Movie> = ArrayList(),
                    @SerializedName("total_results")
                    val totalResults: Int = 0)