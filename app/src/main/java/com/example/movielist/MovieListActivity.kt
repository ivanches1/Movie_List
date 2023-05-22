package com.example.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movielist.API.Movie
import com.example.movielist.API.MovieApiInstance
import com.example.movielist.API.MovieResponse
import com.example.movielist.Adapters.MovieAdapter
import com.example.movielist.Database.MovieDao
import com.example.movielist.Database.MoviesDatabase

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appDatabase = MoviesDatabase.getDatabase(applicationContext)
        movieDao = appDatabase.movieDao()

        var callStack: MutableList<Movie> = emptyList<Movie>().toMutableList()
        GlobalScope.launch {
            val movies = movieDao.getAllMovies()
            movies.forEach { elem ->
                val call = MovieApiInstance.api.getMovieById(elem.id.toString())
                call.enqueue(object : Callback<MovieResponse> {
                    override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                        if (response.isSuccessful) {
                            if (callStack.isEmpty()) {
                                callStack = response.body()?.docs?.toMutableList()!!

                            } else {
                                response.body()?.docs?.get(0)?.let { callStack.add(0, it) }
                                movieAdapter = MovieAdapter(callStack, this@MovieListActivity)
                                binding.recycle.adapter = movieAdapter
                                binding.recycle.layoutManager = GridLayoutManager(this@MovieListActivity, 3)
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
            runOnUiThread {

            }
        }
    }
}