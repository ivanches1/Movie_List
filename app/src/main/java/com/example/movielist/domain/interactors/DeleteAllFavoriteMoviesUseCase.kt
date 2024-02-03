package com.example.movielist.domain.interactors

import com.example.movielist.domain.DatabaseRepository

class DeleteAllFavoriteMoviesUseCase(private val repository: DatabaseRepository) {
    suspend fun execute() {
        repository.deleteMovies()
    }
}