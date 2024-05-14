package com.example.mymoviesapplication.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mymoviesapplication.MoviesApplication
import com.example.mymoviesapplication.ui.screens.movies.MoviesViewModel
import com.example.mymoviesapplication.ui.screens.my_movies.MovieReviewViewModel
import com.example.mymoviesbaza.ui.screens.my_movies.MyMoviesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MoviesViewModel(
                moviesApplication().container.moviesRepository,
                moviesApplication().container.moviesServiceRepository
            )
        }
        initializer {
            MyMoviesViewModel(moviesApplication().container.moviesRepository)
        }
        initializer {
            MovieReviewViewModel(moviesApplication().container.moviesRepository)
        }
    }
}


fun CreationExtras.moviesApplication(): MoviesApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MoviesApplication)
