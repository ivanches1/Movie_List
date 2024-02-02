package com.example.movielist.domain.interactors

import com.example.movielist.domain.DatabaseRepository

class AddToFavoriteUseCase(private val repository: DatabaseRepository) {
    fun execute(id: Int) {
        repository.insertMovie(id)
    }
}