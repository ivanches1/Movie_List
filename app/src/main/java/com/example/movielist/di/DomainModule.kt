package com.example.todolist.di

import com.example.movielist.domain.ApiRepository
import com.example.movielist.domain.DatabaseRepository
import com.example.movielist.domain.interactors.AddToFavoriteUseCase
import com.example.movielist.domain.interactors.DeleteAllFavoriteMoviesUseCase
import com.example.movielist.domain.interactors.FetchMovieUseCase
import com.example.movielist.domain.interactors.GetFavoritesMoviesUseCase
import com.example.movielist.domain.interactors.GetMovieByIdUseCase
import com.example.movielist.domain.interactors.SearchMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideAddToFavoriteUseCase(repository: DatabaseRepository) : AddToFavoriteUseCase{
        return AddToFavoriteUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteAllFavoriteMoviesUseCase(repository: DatabaseRepository) : DeleteAllFavoriteMoviesUseCase {
        return DeleteAllFavoriteMoviesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetFavoritesMoviesUseCase(repository: DatabaseRepository): GetFavoritesMoviesUseCase {
        return GetFavoritesMoviesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideFetchMovieUSeCase(repository: ApiRepository): FetchMovieUseCase {
        return FetchMovieUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetMovieByIdUseCase(repository: ApiRepository): GetMovieByIdUseCase {
        return GetMovieByIdUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSearchMoviesUseCase(repository: ApiRepository): SearchMoviesUseCase {
        return SearchMoviesUseCase(repository)
    }


}
