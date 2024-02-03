package com.example.movielist.presentation.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.presentation.movie_info.MovieInfoActivity
import com.example.movielist.databinding.ItemMovieBinding
import com.example.movielist.domain.models.Movie
import com.squareup.picasso.Picasso


class MovieAdapter(private val movies: List<Movie>, private val context: Context) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    // Создание нового ViewHolder и связывание с макетом элемента списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Создаем экземпляр привязки макета элемента списка
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Привязка данных фильма к ViewHolder и установка слушателя клика
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

        // Установка слушателя клика на элемент списка
        holder.itemView.setOnClickListener {
            // Создание интента для открытия активности с информацией о фильме
            val intent = Intent(context, MovieInfoActivity::class.java)
            intent.putExtra("id", movie.id)
            context.startActivity(intent)
        }
    }

    // Возвращает количество элементов в списке
    override fun getItemCount(): Int {
        return movies.size
    }

    // Внутренний класс, представляющий элемент списка
    inner class ViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        // Связывание данных фильма с соответствующими представлениями внутри элемента списка
        fun bind(movie: Movie) {
            binding.cameraTextView.text = movie.name
            binding.dateTextView.text = movie.rating.kp.toString()
            Picasso.get()
                .load(movie.poster)
                .resize(400, 600) // Укажите требуемые размеры изображения
                .centerCrop() // Центрировать и обрезать изображение, чтобы соответствовать указанным размерам
                .into(binding.photoImageView)
        }
    }
}

