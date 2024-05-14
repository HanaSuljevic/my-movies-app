package com.example.mymoviesapplication.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val year: String,
    val timeline: String,
    var rating: String,
    val image: String? = null,
    var review: String?,
    var myRating: Int?,
    val description: String?
)