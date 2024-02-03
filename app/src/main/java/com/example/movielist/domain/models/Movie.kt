package com.example.movielist.domain.models

import com.example.movielist.data.api.Movie as ApiMovie
import com.example.movielist.data.api.Rating as ApiRating
import com.example.movielist.data.api.Name as ApiName

data class MovieResponse(
    val docs: List<Movie>
)

data class Movie(
    val rating: Rating,
    val movieLength: Int,
    val id: Int,
    val type: String,
    val name: String,
    val description: String,
    val year: Int,
    val poster: String,
    val genres: List<String>,
    val alternativeName: String?,
    val enName: String?,
    val names: List<Name>,
    val shortDescription: String,
    val logo: String?,
) {
}

data class Name(
    val name: String,
    val language: String?,
    val type: String?
) {
    companion object {
        fun fromApi(apiName: ApiName): Name {
            return Name(
                name = apiName.name,
                language = apiName.language,
                type = apiName.type
            )
        }
    }
}

data class Rating(
    val kp: Double,
    val imdb: Double,
    val filmCritics: Double,
    val russianFilmCritics: Double,
    val await: Int?
) {
    companion object {
        fun fromApi(apiRating: ApiRating): Rating {
            return Rating(
                kp = apiRating.kp,
                imdb = apiRating.imdb,
                filmCritics = apiRating.filmCritics,
                russianFilmCritics = apiRating.russianFilmCritics,
                await = apiRating.await
            )
        }
    }
}
