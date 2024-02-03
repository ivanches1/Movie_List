package com.example.movielist.presentation.movie_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.movielist.data.api.MovieApiInstance
import com.example.movielist.data.api.MovieResponse
import com.example.movielist.data.database.Movie
import com.example.movielist.data.database.MovieDao
import com.example.movielist.data.database.MoviesDatabase
import com.example.movielist.databinding.ActivityMovieInfoBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieInfoActivity : AppCompatActivity() {
    private lateinit var movieDao: MovieDao
    lateinit var binding: ActivityMovieInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получение идентификатора фильма из переданных другой активностью данных
        val id = intent.getIntExtra("id", 0)

        // Инициализация Retrofit сервиса
        val service = MovieApiInstance.api

        // Получение информации о фильме по его идентификатору
        val call = service.getMovieById(id.toString())
//        fetchMovie(call)

        // Обработчик кнопки "Добавить в список планируемых фильмов"
        binding.addToWatchList.setOnClickListener {
            // Инициализация базы данных и Dao
            val appDatabase = MoviesDatabase.getDatabase(applicationContext)
            movieDao = appDatabase.movieDao()

            // Вставка фильма в базу данных в асинхронном режиме чтобы БД не блокировала основной поток приложения
            GlobalScope.launch {
                movieDao.insertMovie(Movie(id))
            }

            Toast.makeText(this, "Фильм добавлен в избранное", Toast.LENGTH_SHORT).show()
        }
    }

    // Получение информации о фильме из API
    private fun fetchMovie(call: Call<MovieResponse>) {
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    val movie = movieResponse?.docs?.get(0)

                    // Формирование строки с жанрами фильма (тк изначально они приходят массивом)
                    var genres = ""
                    movie?.genres?.forEach { genre -> genres = genres + genre.name + ", " }
                    genres = genres.substring(0, genres.length - 2)

                    // Заполнение полей информации о фильме в пользовательском интерфейсе
                    binding.name.text = movie?.name
                    binding.year.text = movie?.year.toString()
                    binding.rating.text = movie?.rating?.kp.toString()
                    binding.description.text = movie?.description
                    binding.genres.text = genres

                    // Загрузка постера фильма с помощью библиотеки Picasso
                    Picasso.get().load(movie?.poster?.url).into(binding.poster)
                } else {
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }
}
