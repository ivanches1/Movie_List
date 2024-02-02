package com.example.movielist.data

import com.example.movielist.domain.ApiRepository
import com.example.movielist.domain.models.Movie
import retrofit2.Call

class ApiRepositoryImpl: ApiRepository {
    override fun getMovies(): Call<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun searchMovies(query: String): Call<List<Movie>> {
        TODO("Not yet implemented")
    }

    override fun getMovieById(query: String): Call<List<Movie>> {
        TODO("Not yet implemented")
    }
}