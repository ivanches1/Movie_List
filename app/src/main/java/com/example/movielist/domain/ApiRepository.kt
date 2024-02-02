package com.example.movielist.domain

import com.example.movielist.data.api.MovieResponse
import com.example.movielist.domain.models.Movie
import retrofit2.Call

interface ApiRepository {

    fun getMovies(): Call<List<Movie>>

    fun searchMovies(query: String): Call<List<Movie>>

    fun getMovieById(query: String): Call<List<Movie>>
}