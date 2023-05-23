package com.example.movielist

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movielist.API.MovieApiInstance
import com.example.movielist.API.MovieResponse
import com.example.movielist.Adapters.MovieAdapter
import com.example.movielist.Database.MovieDao
import com.example.movielist.Database.MoviesDatabase
import com.example.movielist.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var movieDao: MovieDao
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var orientationEventListener: OrientationEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация базы данных и Dao
        val appDatabase = MoviesDatabase.getDatabase(applicationContext)
        movieDao = appDatabase.movieDao()

        // Инициализация Retrofit сервиса для работы с методами получения данных от API
        val service = MovieApiInstance.api

        // Получение списка фильмов с API
        val call = service.getMovies()
        fetchMovies(call)

        // Обработчик поискового запроса
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Выполнение запроса на основе введенного текста
                val call = if (query.isEmpty()) {
                    service.getMovies()
                } else {
                    service.searchMovies(query)
                }
                fetchMovies(call)//вызываем функцию которая устанавливает в список RecycleView полученные фильмы
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Обновление списка фильмов при изменении текста поиска
                if (newText.isEmpty()) {//при очистке запроса устанавливаются стандартные данные как при первом запуске
                    val call = service.getMovies()
                    fetchMovies(call)
                }
                return true
            }
        })
    }

    // Получение списка фильмов из API
    private fun fetchMovies(call: Call<MovieResponse>) {
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    val movies = movieResponse?.docs

                    // Инициализация и привязка адаптера к RecyclerView
                    movieAdapter = movies?.let { MovieAdapter(it, this@MainActivity) }!!
                    binding.recycle.adapter = movieAdapter

                    // Определение ориентации экрана и установка соответствующего количества столбцов в RecyclerView
                    val display = (this@MainActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                    val rotation = display.rotation
                    val config = this@MainActivity.resources.configuration

                    if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
                        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            binding.recycle.layoutManager = GridLayoutManager(this@MainActivity, 3)
                        }
                    } else if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
                        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            binding.recycle.layoutManager = GridLayoutManager(this@MainActivity, 5)
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
            }
        })

        // Инициализация и запуск слушателя событий изменения ориентации экрана
        orientationEventListener = object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }

                // Определение новой ориентации экрана
                val rotation = windowManager.defaultDisplay.rotation
                val newOrientation = when (rotation) {
                    Surface.ROTATION_0 -> Configuration.ORIENTATION_PORTRAIT
                    Surface.ROTATION_90 -> Configuration.ORIENTATION_LANDSCAPE
                    Surface.ROTATION_180 -> Configuration.ORIENTATION_PORTRAIT
                    Surface.ROTATION_270 -> Configuration.ORIENTATION_LANDSCAPE
                    else -> Configuration.ORIENTATION_UNDEFINED
                }

                // Обработка изменения ориентации экрана
                handleOrientationChange(newOrientation)
            }
        }

        orientationEventListener.enable()
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
                    movieDao.deleteMovies()
                }
                Toast.makeText(this,
                    "Вы успешно очистили список планируемых фильмов",
                    Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    // Установка количества столбцов в RecyclerView в зависимости от ориентации экрана
    private fun handleOrientationChange(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.recycle.layoutManager = GridLayoutManager(this@MainActivity, 3)
        } else {
            binding.recycle.layoutManager = GridLayoutManager(this@MainActivity, 5)
        }
    }
}
