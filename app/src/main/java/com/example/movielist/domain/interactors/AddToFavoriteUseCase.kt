package com.example.movielist.domain.interactors

import com.example.movielist.domain.DatabaseRepository

class AddToFavoriteUseCase(private val repository: DatabaseRepository) {
    suspend fun execute(id: Int) {
        repository.insertMovie(id)
    }
}