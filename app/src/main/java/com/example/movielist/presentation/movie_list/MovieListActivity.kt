package com.example.movielist.presentation.movie_list

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.OrientationEventListener
import android.view.Surface
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movielist.data.api.Movie
import com.example.movielist.data.api.MovieApiInstance
import com.example.movielist.data.api.MovieResponse
import com.example.movielist.presentation.adapters.MovieAdapter
import com.example.movielist.data.database.MovieDao
import com.example.movielist.data.database.MoviesDatabase

import com.example.movielist.databinding.ActivityMovieListBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListActivity : AppCompatActivity() {
    lateinit var binding: ActivityMovieListBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieDao: MovieDao
    private lateinit var orientationEventListener: OrientationEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация базы данных и Dao
        val appDatabase = MoviesDatabase.getDatabase(applicationContext)
        movieDao = appDatabase.movieDao()

        // Получение текущей ориентации экрана
        val display = (this@MovieListActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val rotation = display.rotation
        val config = this@MovieListActivity.resources.configuration

        // Стек для хранения фильмов
        var callStack: MutableList<Movie> = emptyList<Movie>().toMutableList()

        GlobalScope.launch {
            // Получение всех фильмов из базы данных
            val movies = movieDao.getAllMovies()

            movies.forEach { elem ->
                val call = MovieApiInstance.api.getMovieById(elem.id.toString())

                // Запрос информации о фильме
                call.enqueue(object : Callback<MovieResponse> {
                    override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                        if (response.isSuccessful) {
                            if (callStack.isEmpty()) {
                                // Если стек пустой, добавляем первый фильм из базы данных
                                callStack = response.body()?.docs?.toMutableList()!!

                                // Инициализация адаптера и привязка его к RecyclerView
                                movieAdapter = MovieAdapter(callStack, this@MovieListActivity)
                                binding.recycle.adapter = movieAdapter

                                // Установка LayoutManager в зависимости от ориентации экрана и вращения
                                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
                                    if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                                        binding.recycle.layoutManager = GridLayoutManager(this@MovieListActivity, 3)
                                    }
                                } else if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
                                    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                        binding.recycle.layoutManager = GridLayoutManager(this@MovieListActivity, 5)
                                    }
                                }

                            } else {
                                // Если стек не пустой, дополняем его новым фильмом (Это необходимо для формирования подходящего под адаптер типа данных)
                                response.body()?.docs?.get(0)?.let { callStack.add(0, it) }

                                // Обновление адаптера и LayoutManager
                                movieAdapter = MovieAdapter(callStack, this@MovieListActivity)
                                binding.recycle.adapter = movieAdapter

                                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
                                    if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                                        binding.recycle.layoutManager = GridLayoutManager(this@MovieListActivity, 3)
                                    }
                                } else if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
                                    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                        binding.recycle.layoutManager = GridLayoutManager(this@MovieListActivity, 5)
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Something went wrong1", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        // Инициализация и запуск слушателя событий ориентации экрана
        orientationEventListener = object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                // Обработка изменения ориентации экрана
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                val rotation = windowManager.defaultDisplay.rotation
                val newOrientation = when (rotation) {
                    Surface.ROTATION_0 -> Configuration.ORIENTATION_PORTRAIT
                    Surface.ROTATION_90 -> Configuration.ORIENTATION_LANDSCAPE
                    Surface.ROTATION_180 -> Configuration.ORIENTATION_PORTRAIT
                    Surface.ROTATION_270 -> Configuration.ORIENTATION_LANDSCAPE
                    else -> Configuration.ORIENTATION_UNDEFINED
                }

                // Выполнение действий при изменении ориентации экрана
                handleOrientationChange(newOrientation)
            }
        }

        // Включение слушателя событий ориентации экрана
        orientationEventListener.enable()
    }

    // Обработка изменения ориентации экрана (при портретной ориентации будет 3 колонки списка а при ландшафтной 5)
    private fun handleOrientationChange(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.recycle.layoutManager = GridLayoutManager(this@MovieListActivity, 3)
        } else {
            binding.recycle.layoutManager = GridLayoutManager(this@MovieListActivity, 5)
        }
    }
}
