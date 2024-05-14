package com.example.mymoviesapplication.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieModel(
   val title: String,
   val year: String,
   val timeline: String,
   @SerialName(value = "imdbRating")
   val rating: String,
   @SerialName(value = "posterImage")
   val image: String? = null,
   val description: String = ""
)