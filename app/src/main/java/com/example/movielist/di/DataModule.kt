package com.example.todolist.di

import android.content.Context
import com.example.movielist.data.ApiRepositoryImpl
import com.example.movielist.data.DatabaseRepositoryImpl
import com.example.movielist.data.api.MovieApiInstance
import com.example.movielist.data.api.MovieApiService
import com.example.movielist.data.database.MovieDao
import com.example.movielist.data.database.MoviesDatabase
import com.example.movielist.domain.ApiRepository
import com.example.movielist.domain.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideApiRepository(apiService: MovieApiService): ApiRepository {
        return ApiRepositoryImpl(apiService)
    }

    @Singleton
    @Provides
    fun provideDatabaseRepository(dao: MovieDao): DatabaseRepository {
        return DatabaseRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideDao(database: MoviesDatabase): MovieDao {
        return database.movieDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MoviesDatabase {
        return MoviesDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideApiService(): MovieApiService {
        return MovieApiInstance.api
    }

}