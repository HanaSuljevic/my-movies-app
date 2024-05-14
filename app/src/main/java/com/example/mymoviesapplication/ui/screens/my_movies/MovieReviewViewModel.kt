package com.example.mymoviesapplication.ui.screens.my_movies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoviesapplication.data.repositories.MoviesDatabaseRepository
import com.example.mymoviesapplication.entities.Movie
import com.example.mymoviesbaza.ui.screens.my_movies.MyMoviesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class MovieReviewUiState(
    var currentMovie: Movie? = null,
    val error: String? = null,
    val loading: Boolean = false,
)

class MovieReviewViewModel(
    private val moviesDatabaseRepository: MoviesDatabaseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        MovieReviewUiState(
            currentMovie = null,
            error = null,
            loading = false
        )
    )
    val uiState: StateFlow<MovieReviewUiState> = _uiState

    fun getMoviewById(id: Int) {
        _uiState.update {
            it.copy(
                loading = true
            )
        }
        viewModelScope.launch {
            var dbObj = moviesDatabaseRepository.getMovieStream(id).firstOrNull()
            if (dbObj != null) {
                _uiState.update {
                    it.copy(
                        currentMovie = dbObj,
                        loading = false,
                    )
                }
            }
        }
    }

    fun updateReview(review: String) {
        var movie = uiState.value.currentMovie
        if (movie != null) {
            movie.review = review
            _uiState.update {
                it.copy(currentMovie = movie, loading = true)
            }
            viewModelScope.launch {
                moviesDatabaseRepository.updateMovie(movie)
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }
}
