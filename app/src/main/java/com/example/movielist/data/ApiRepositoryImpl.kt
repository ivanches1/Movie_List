package com.example.movielist.data

import com.example.movielist.data.api.MovieApiService
import com.example.movielist.domain.ApiRepository
import com.example.movielist.domain.models.Movie
import com.example.movielist.domain.models.Name
import com.example.movielist.domain.models.Rating
import retrofit2.Call
import retrofit2.Response

class ApiRepositoryImpl(private val apiService: MovieApiService): ApiRepository {
    override suspend fun getMovies(): List<Movie> {
        return fetchMovie(apiService.getMovies())
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        return fetchMovie(apiService.searchMovies(query))
    }

    override suspend fun getMovieById(query: String): List<Movie> {
        return fetchMovie(apiService.getMovieById(query))
    }

    private fun fetchMovie(call: com.example.movielist.data.api.MovieResponse): List<Movie> {

        return call.docs.map {
            Movie(
                rating = Rating.fromApi(it.rating),
                alternativeName = it.alternativeName,
                description = it.description,
                enName = it.enName,
                genres = it.genres.map { it.name },
                id = it.id,
                logo = it.logo?.url,
                movieLength = it.movieLength,
                name = it.name,
                names = it.names.map { Name.fromApi(it) },
                poster = it.poster.url,
                shortDescription = it.shortDescription,
                type = it.type,
                year = it.year
            )
        }


    }
}