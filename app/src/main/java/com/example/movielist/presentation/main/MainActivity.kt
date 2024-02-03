package com.example.movielist.presentation.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.example.movielist.data.api.MovieApiInstance
import com.example.movielist.data.api.MovieResponse
import com.example.movielist.presentation.adapters.MovieAdapter
import com.example.movielist.data.database.MovieDao
import com.example.movielist.data.database.MoviesDatabase
import com.example.movielist.databinding.ActivityMainBinding
import com.example.movielist.domain.models.Movie
import com.example.movielist.presentation.movie_list.MovieListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        movieList = binding.recycle
        movieAdapter = MovieAdapter(emptyList(), this@MainActivity)
        movieList.adapter = movieAdapter
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            movieList.layoutManager = GridLayoutManager(this@MainActivity, 5)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieList.layoutManager = GridLayoutManager(this@MainActivity, 3)
        }

        viewModel.fetchMovies()

        // Получение списка фильмов с API
        viewModel.movieList.observe(this) { list ->
            list?.let {
                displayMovies(it)
            }
        }

        viewModel.fetchMovies()

        // Обработчик поискового запроса
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Выполнение запроса на основе введенного текста
                if (query.isEmpty()) {
                    viewModel.fetchMovies()
                } else {
                    viewModel.searchMovie(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Обновление списка фильмов при изменении текста поиска
                if (newText.isEmpty()) {
                    viewModel.fetchMovies()
                }
                return true
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            movieList.layoutManager = GridLayoutManager(this@MainActivity, 5)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieList.layoutManager = GridLayoutManager(this@MainActivity, 3)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Инициализация меню
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.movie_list_btn -> {
                // Переход к активности со списком фильмов
                val intent = Intent(this, MovieListActivity::class.java)
                this.startActivity(intent)
            }
            R.id.clear_movie_list -> {
                // Очистка списка фильмов
                GlobalScope.launch {
                    viewModel.deleteAllMovies()
                }
                Toast.makeText(this,
                    "Вы успешно очистили список планируемых фильмов",
                    Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun displayMovies(movies: List<Movie>) {
        movieAdapter = MovieAdapter(movies, this@MainActivity)
        movieList.adapter = movieAdapter
    }

}
