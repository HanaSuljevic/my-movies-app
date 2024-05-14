package com.example.mymoviesapplication.data

import android.content.Context
import com.example.mymoviesapplication.data.repositories.MoviesDatabaseRepository
import com.example.mymoviesapplication.data.repositories.MoviesRepository
import com.example.mymoviesapplication.data.repositories.NetworkMoviesRepository
import com.example.mymoviesapplication.data.repositories.OfflineMoviesRepository
import com.example.mymoviesapplication.data.services.MovieApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit


/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val moviesRepository: MoviesDatabaseRepository
    val moviesServiceRepository: MoviesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val moviesRepository: MoviesDatabaseRepository by lazy {
        OfflineMoviesRepository(MoviesDatabase.getDatabase(context).movieDao())
    }
    override val moviesServiceRepository: MoviesRepository by lazy {
        NetworkMoviesRepository(context)
    }
}
