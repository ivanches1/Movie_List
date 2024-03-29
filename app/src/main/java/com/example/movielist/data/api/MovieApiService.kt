package com.example.movielist.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MovieApiService {
    // Запрос для получения списка фильмов
    @Headers("X-API-KEY: JAVP002-038MAVB-NSKCDGB-GC3XHT9")
    @GET("v1.3/movie?page=1&limit=40")
    suspend fun getMovies(): MovieResponse

    // Запрос для поиска фильмов по названию
    @Headers("X-API-KEY: JAVP002-038MAVB-NSKCDGB-GC3XHT9")
    @GET("v1.3/movie?page=1&limit=10")
    suspend fun searchMovies(@Query("names.name") query: String): MovieResponse

    // Запрос для получения информации о фильме по его идентификатору
    @Headers("X-API-KEY: JAVP002-038MAVB-NSKCDGB-GC3XHT9")
    @GET("v1.3/movie")
    suspend fun getMovieById(@Query("id") query: String): MovieResponse
}

