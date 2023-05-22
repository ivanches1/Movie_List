package com.example.movielist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movielist.API.MovieApiInstance
import com.example.movielist.API.MovieResponse
import com.example.movielist.Adapters.MovieAdapter
import com.example.movielist.Database.MovieDao
import com.example.movielist.Database.MoviesDatabase
import com.example.movielist.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var movieDao: MovieDao
    private lateinit var movieAdapter: MovieAdapter
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
                    binding.recycle.layoutManager = GridLayoutManager(this@MainActivity, 3)
                } else {
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
            }
        })
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


}