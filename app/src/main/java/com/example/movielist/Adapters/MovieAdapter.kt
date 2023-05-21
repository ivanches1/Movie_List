package com.example.movielist.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.API.Movie
import com.example.movielist.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso


class MovieAdapter(private val photos: List<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photos[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    inner class ViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(photo: Movie) {
            binding.cameraTextView.text = photo.name
            binding.dateTextView.text = photo.rating.kp.toString()
            Picasso.get()
                .load(photo.poster.url)
                .into(binding.photoImageView)
        }
    }
}
