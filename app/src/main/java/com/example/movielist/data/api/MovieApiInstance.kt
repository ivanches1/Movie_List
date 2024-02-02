package com.example.movielist.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieApiInstance {
    // Создаем экземпляр Retrofit с помощью ленивой инициализации (экземпляры создаются при обращении к ним в коде)
    private val retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl("https://api.kinopoisk.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Создаем экземпляр сервиса API с помощью ленивой инициализации
    val api: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }
}
