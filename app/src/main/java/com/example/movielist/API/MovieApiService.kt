package com.example.movielist.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface MovieApiService {
    @Headers(
        "X-API-KEY: JAVP002-038MAVB-NSKCDGB-GC3XHT9"
    )
    @GET("movie?page=1&limit=20")
    fun getPhotos(): Call<MovieResponse>
}
