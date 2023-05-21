package com.example.movielist.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieApiInstance {
    private val retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl("https://api.kinopoisk.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }
}