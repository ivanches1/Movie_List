package com.example.movielist.data

import com.example.movielist.data.database.MovieDao
import com.example.movielist.domain.DatabaseRepository

class DatabaseRepositoryImpl(private val dao: MovieDao): DatabaseRepository {
    override fun getAllMovies(): List<Int> {
        return dao.getAllMovies().map { it.id }
    }

    override fun insertMovie(id: Int) {
        dao.insertMovie(com.example.movielist.data.database.Movie(id))
    }

    override fun deleteMovies() {
        dao.deleteMovies()
    }
}