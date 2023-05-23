package com.example.movielist.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    // Получить все фильмы из базы данных
    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<Movie>

    // Вставить фильм в базу данных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    // Удалить все фильмы из базы данных
    @Query("DELETE FROM movies")
    fun deleteMovies()
}

