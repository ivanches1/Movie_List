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

        val appDatabase = MoviesDatabase.getDatabase(applicationContext)
        movieDao = appDatabase.movieDao()

        val service = MovieApiInstance.api
        val call = service.getMovies()
        fetchMovies(call)

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val call = if (query.isEmpty()) {
                    service.getMovies()
                } else {
                    service.searchMovies(query)
                }
                fetchMovies(call)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    val call = service.getMovies()
                    fetchMovies(call)
                }
                return true
            }
        })
    }

    private fun fetchMovies(call: Call<MovieResponse>) {
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    val movies = movieResponse?.docs
                    movieAdapter = movies?.let { MovieAdapter(it, this@MainActivity) }!!
                    binding.recycle.adapter = movieAdapter


                    val display =
                        (this@MainActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
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

        orientationEventListener = object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                // Обрабатываем изменение ориентации экрана
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

                // Выполняем действия при изменении ориентации экрана
                handleOrientationChange(newOrientation)
            }
        }

        // Запускаем слушатель событий
        orientationEventListener.enable()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fvButton -> {
                val intent = Intent(this, MovieListActivity::class.java)
                this.startActivity(intent)
            }
        }
        return true
    }

    private fun handleOrientationChange(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.recycle.layoutManager = GridLayoutManager(this@MainActivity, 3)
        } else {
            binding.recycle.layoutManager = GridLayoutManager(this@MainActivity, 5)
        }
    }

}