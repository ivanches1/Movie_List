package com.example.movielist.domain.interactors

import com.example.movielist.domain.ApiRepository
import com.example.movielist.domain.models.Movie

class FetchMovieUseCase(private val repository: ApiRepository) {
    fun execute(): List<Movie> {
        return repository.getMovies()
    }
}