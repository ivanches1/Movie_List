package com.example.movielist.domain.interactors

import com.example.movielist.data.api.MovieApiService

class GetMovieByIdUseCase(private val apiService: MovieApiService) {
    fun execute(query: String) {
        apiService.getMovieById(query)
    }
}