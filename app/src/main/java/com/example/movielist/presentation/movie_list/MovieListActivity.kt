package com.example.movielist.presentation.movie_list

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.presentation.adapters.MovieAdapter
import com.example.movielist.databinding.ActivityMovieListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListActivity : AppCompatActivity() {
    lateinit var binding: ActivityMovieListBinding
    private lateinit var movieAdapter: MovieAdapter
    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var movieList: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }
    private fun init() {
        movieList = binding.recycle
        movieAdapter = MovieAdapter(emptyList(), this)
        movieList.adapter = movieAdapter
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            movieList.layoutManager = GridLayoutManager(this, 5)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieList.layoutManager = GridLayoutManager(this, 3)
        }

        // Получение списка фильмов с API
        viewModel.movieList.observe(this) { list ->
            list?.let {
                displayMovies(it)
            }
        }

        viewModel.getFavoritesMovies()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            movieList.layoutManager = GridLayoutManager(this@MovieListActivity, 5)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieList.layoutManager = GridLayoutManager(this@MovieListActivity, 3)
        }
    }

    private fun displayMovies(movies: List<com.example.movielist.domain.models.Movie>) {
        movieAdapter = MovieAdapter(movies, this@MovieListActivity)
        movieList.adapter = movieAdapter
    }
}


