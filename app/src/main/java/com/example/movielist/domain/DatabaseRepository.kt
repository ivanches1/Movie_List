package com.example.movielist.domain

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movielist.domain.models.Movie

interface DatabaseRepository {
    suspend fun getAllMovies(): List<Int>

    suspend fun insertMovie(id: Int)

    suspend fun deleteMovies()
}