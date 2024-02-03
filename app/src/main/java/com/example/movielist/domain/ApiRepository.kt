package com.example.movielist.domain

import com.example.movielist.data.api.MovieResponse
import com.example.movielist.domain.models.Movie
import retrofit2.Call

interface ApiRepository {

    suspend fun getMovies(): List<Movie>

    suspend fun searchMovies(query: String): List<Movie>

    suspend fun getMovieById(query: String): List<Movie>
}