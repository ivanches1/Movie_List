package com.example.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movielist.API.MovieApiInstance
import com.example.movielist.API.MovieResponse
import com.example.movielist.Adapters.MovieAdapter
import com.example.movielist.databinding.ActivityMovieInfoBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityMovieInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("id", 0)
        val service = MovieApiInstance.api
        val call = service.getMovieById(id.toString())
        fetchMovie(call)
    }

    private fun fetchMovie(call: Call<MovieResponse>) {
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {

                    val movieResponse = response.body()
                    val movie = movieResponse?.docs?.get(0)
                    var genres = ""
                    movie?.genres?.forEach { genre -> genres = genres + genre.name + ", " }
                    genres = genres.substring(0, genres.length-2)
                    binding.name.text = movie?.name
                    binding.year.text = movie?.year.toString()
                    binding.rating.text = movie?.rating?.kp.toString()
                    binding.description.text = movie?.description
                    binding.genres.text = genres
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