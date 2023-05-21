package com.example.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movielist.API.MovieApiInstance
import com.example.movielist.API.MovieResponse
import com.example.movielist.Adapters.MovieAdapter
import com.example.movielist.databinding.ActivityMainBinding

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var photoAdapter: MovieAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val context = this
        val service = MovieApiInstance.api
        val call = service.getPhotos()
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val photoResponse = response.body()
                    val photos = photoResponse?.docs
                    photoAdapter = photos?.let { MovieAdapter(it) }!!
                    binding.recycle.adapter = photoAdapter
                    binding.recycle.layoutManager = GridLayoutManager(context, 3)
                } else {
                    Toast.makeText(applicationContext, "something wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
            }
        })

    }
}