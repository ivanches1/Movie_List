package com.example.movielist.API

//создаем модель получаемых данных
data class MovieResponse(
    val docs: List<Movie>
)

data class Movie(
    @Transient val externalId: ExternalId,
    val rating: Rating,
    @Transient val votes: Votes,
    val movieLength: Int,
    val id: Int,
    val type: String,
    val name: String,
    val description: String,
    val year: Int,
    val poster: Poster,
    val genres: List<Genre>,
    @Transient val countries: List<Country>,
    val alternativeName: String?,
    val enName: String?,
    val names: List<Name>,
    val shortDescription: String,
    val logo: Logo,
    @Transient val watchability: Watchability
)

data class ExternalId(
    val kpHD: String,
    val imdb: String,
    val tmdb: Int
)

data class Rating(
    val kp: Double,
    val imdb: Double,
    val filmCritics: Double,
    val russianFilmCritics: Double,
    val await: Int?
)

data class Votes(
    val kp: Int,
    val imdb: Int,
    val filmCritics: Int,
    val russianFilmCritics: Int,
    val await: Int?
)

data class Poster(
    val url: String,
    val previewUrl: String
)

data class Genre(
    val name: String
)

data class Country(
    val name: String
)

data class Name(
    val name: String,
    val language: String?,
    val type: String?
)

data class Logo(
    val url: String
)

data class Watchability(
    val items: List<WatchabilityItem>
)

data class WatchabilityItem(
    val name: String,
    val logo: Logo,
    val url: String
)

