package com.example.movielist.domain

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movielist.domain.models.Movie

interface DatabaseRepository {
    fun getAllMovies(): List<Int>

    fun insertMovie(id: Int)

    fun deleteMovies()
}