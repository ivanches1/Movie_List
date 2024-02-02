package com.example.movielist.domain.models

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
    val logo: String,
)

data class Name(
    val name: String,
    val language: String?,
    val type: String?
)

data class Rating(
    val kp: Double,
    val imdb: Double,
    val filmCritics: Double,
    val russianFilmCritics: Double,
    val await: Int?
)