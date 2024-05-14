package com.example.mymoviesapplication.models

import kotlinx.serialization.Serializable


@Serializable
data class GetMoviesResponseModel(
    val movies: List<MovieModel>
)