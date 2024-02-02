package com.example.movielist.domain

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movielist.domain.models.Movie

interface DatabaseRepository {
    fun getAllMovies(): List<Movie>

    fun insertMovie(movie: Movie)

    fun deleteMovies()
}