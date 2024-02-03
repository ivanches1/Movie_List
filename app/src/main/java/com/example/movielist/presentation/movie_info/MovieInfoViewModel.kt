package com.example.movielist.presentation.movie_info

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielist.domain.interactors.AddToFavoriteUseCase
import com.example.movielist.domain.interactors.GetMovieByIdUseCase
import com.example.movielist.domain.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieInfoViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val addToFavoriteUseCase: AddToFavoriteUseCase
): ViewModel() {

    private val _movie = MutableLiveData<List<Movie>>()
    val movie : LiveData<List<Movie>> get() = _movie

    fun fetchMovieById(id: String) {
        viewModelScope.launch {
            try {
                val res = getMovieByIdUseCase.execute(id)

                _movie.value = res

            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addToFavorite(id: Int) {
        viewModelScope.launch {
            try {
                addToFavoriteUseCase.execute(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}