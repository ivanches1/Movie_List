package com.example.movielist.presentation.movie_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.movielist.data.api.MovieApiInstance
import com.example.movielist.data.api.MovieResponse
import com.example.movielist.data.database.Movie
import com.example.movielist.data.database.MovieDao
import com.example.movielist.data.database.MoviesDatabase
import com.example.movielist.databinding.ActivityMovieInfoBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MovieInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityMovieInfoBinding
    private val viewModel: MovieInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получение идентификатора фильма из переданных другой активностью данных
        val id = intent.getIntExtra("id", 0)

        viewModel.movie.observe(this) { movie ->
            movie?.let {
                setMovie(it)
            }
        }

        viewModel.fetchMovieById(id.toString())

        // Обработчик кнопки "Добавить в список планируемых фильмов"
        binding.addToWatchList.setOnClickListener {
            viewModel.addToFavorite(id)
            Toast.makeText(this, "Фильм добавлен в избранное", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMovie(movies: List<com.example.movielist.domain.models.Movie>) {
        val movie = movies[0]
        var genres = ""
        movie.genres.forEach { genre -> genres = "$genres$genre, " }
        genres = genres.substring(0, genres.length - 2)

        // Заполнение полей информации о фильме в пользовательском интерфейсе
        binding.name.text = movie.name
        binding.year.text = movie.year.toString()
        binding.rating.text = movie.rating.kp.toString()
        binding.description.text = movie.description
        binding.genres.text = genres

        // Загрузка постера фильма с помощью библиотеки Picasso
        Picasso.get().load(movie.poster).into(binding.poster)
    }

}
