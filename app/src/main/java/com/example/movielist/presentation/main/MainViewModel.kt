package com.example.movielist.presentation.main

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.domain.interactors.DeleteAllFavoriteMoviesUseCase
import com.example.movielist.domain.interactors.FetchMovieUseCase
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
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fetchMovieUseCase: FetchMovieUseCase,
    private val deleteAllFavoriteMoviesUseCase: DeleteAllFavoriteMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
): ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList : LiveData<List<Movie>> get() =_movieList

    fun fetchMovies() {
        viewModelScope.launch {
            try {
                val res = fetchMovieUseCase.execute()

                _movieList.value = res

            }catch (e: Exception) {
                e.printStackTrace()

                _movieList.value = emptyList()

            }
        }
    }

    fun deleteAllMovies() {
        viewModelScope.launch {
            try {
                deleteAllFavoriteMoviesUseCase.execute()
                fetchMovies()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Favorites books list successfully cleared", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchMovie(query: String) {
        viewModelScope.launch {
            try {
                val res = searchMoviesUseCase.execute(query)
                withContext(Dispatchers.Main) {
                    _movieList.value = res
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}