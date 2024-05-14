package com.example.mymoviesapplication.data.repositories

import android.content.Context
import com.example.mymoviesapplication.data.services.MovieApi
import com.example.mymoviesapplication.data.services.MovieApiService
import com.example.mymoviesapplication.models.GetMoviesResponseModel

interface MoviesRepository {
    suspend fun getMovies(genre: String): GetMoviesResponseModel
}

class NetworkMoviesRepository(context: Context) : MoviesRepository {
    private val movieApiService: MovieApiService = MovieApi.create(context)

    override suspend fun getMovies(genre: String): GetMoviesResponseModel = movieApiService.getMovies(genre)
}