package com.example.mymoviesapplication

import android.app.Application
import com.example.mymoviesapplication.data.AppContainer
import com.example.mymoviesapplication.data.AppDataContainer
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class MoviesApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
