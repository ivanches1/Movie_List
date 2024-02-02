package com.example.movielist.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

//сущность базы данных с ее моделью данных
@Entity(tableName = "movies")
data class Movie(//объявляем модель данных сущности, в ней хранится только идентификатор фильма чтобы обратиться к API за данными о нем
    @PrimaryKey val id: Int
)
