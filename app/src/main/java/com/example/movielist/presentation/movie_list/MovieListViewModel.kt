package com.example.movielist.presentation.movie_list

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.domain.interactors.DeleteAllFavoriteMoviesUseCase
import com.example.movielist.domain.interactors.FetchMovieUseCase
import com.example.movielist.domain.interactors.GetFavoritesMoviesUseCase
import com.example.movielist.domain.interactors.GetMovieByIdUseCase
import com.example.movielist.domain.interactors.SearchMoviesUseCase
import com.example.movielist.domain.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val getFavoritesMoviesUseCase: GetFavoritesMoviesUseCase
): ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList : LiveData<List<Movie>> get() =_movieList

    fun getFavoritesMovies() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val moviesIds = getFavoritesMoviesUseCase.execute()
                val movies = moviesIds.map {
                    getMovieByIdUseCase.execute(it.toString())[0]
                }
                withContext(Dispatchers.Main) {
                    _movieList.value = movies
                }
            }
        }
    }

}