package com.example.movielist.domain.interactors

import com.example.movielist.domain.DatabaseRepository

class GetFavoritesMoviesUseCase(private val repository: DatabaseRepository) {
    fun execute(): List<Int> {
        return repository.getAllMovies()
    }
}