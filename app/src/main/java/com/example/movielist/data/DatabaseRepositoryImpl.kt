package com.example.movielist.data

import com.example.movielist.data.database.MovieDao
import com.example.movielist.domain.DatabaseRepository

class DatabaseRepositoryImpl(private val dao: MovieDao): DatabaseRepository {
    override fun getAllMovies(): List<Int> {
        val list: MutableList<Int> = emptyList<Int>().toMutableList()
        dao.getAllMovies().map { list.add(it.id) }
        return list.toList()
    }

    override fun insertMovie(id: Int) {
        dao.insertMovie(com.example.movielist.data.database.Movie(id))
    }

    override fun deleteMovies() {
        dao.deleteMovies()
    }
}