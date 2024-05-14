package com.example.mymoviesapplication.data.services

import android.content.Context
import retrofit2.http.Query
import com.example.mymoviesapplication.models.GetMoviesResponseModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit

private const val BASE_URL =
    "https://moviesverse1.p.rapidapi.com"

interface MovieApiService {
    @Headers("Cache-Control: max-age=3600")
    @GET("get-by-genre")
    suspend fun getMovies(@Query("genre") genre: String = "action"): GetMoviesResponseModel
}

object MovieApi {
    fun create(context: Context): MovieApiService {
        val cacheSize = (5 * 1024 * 1024).toLong() // 5 MB cache size, because we like to keep things moderate
        val cache = Cache(context.cacheDir, cacheSize)

        val cachingInterceptor = Interceptor {
                chain ->
            val maxAge = 60
            val originalResponse = chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Cache-Control", "public, max-age=$maxAge")
                    .addHeader("X-RapidAPI-Key", "ff4c5e5304msh20b9f7a20ebed05p17a93ejsn6792aff5f7f0")
                    .addHeader("X-RapidAPI-Host", "moviesverse1.p.rapidapi.com")
                    .build()
            )
            val cacheControl = originalResponse.header("Cache-Control")
            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")
            ) {
                // No cache headers, skip caching
                originalResponse
            } else {
                originalResponse.newBuilder()
                    .addHeader("Cache-Control", "public, max-age=$maxAge")
                    .addHeader("X-RapidAPI-Key", "ff4c5e5304msh20b9f7a20ebed05p17a93ejsn6792aff5f7f0")
                    .addHeader("X-RapidAPI-Host", "moviesverse1.p.rapidapi.com")
                    .build()
            }
        }

        val httpClient = OkHttpClient
            .Builder()
            .cache(cache)
            .addInterceptor(cachingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .client(httpClient)
            .build()

        return retrofit.create(MovieApiService::class.java)
    }
}