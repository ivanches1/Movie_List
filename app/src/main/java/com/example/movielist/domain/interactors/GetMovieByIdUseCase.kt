package com.example.movielist.domain.interactors

import com.example.movielist.domain.ApiRepository
import com.example.movielist.domain.models.Movie

class GetMovieByIdUseCase(private val repository: ApiRepository) {
    fun execute(query: String): List<Movie> {
        return repository.getMovieById(query)
    }
}