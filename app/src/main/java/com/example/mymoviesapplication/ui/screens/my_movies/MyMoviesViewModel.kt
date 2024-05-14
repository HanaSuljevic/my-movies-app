package com.example.mymoviesbaza.ui.screens.my_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoviesapplication.data.repositories.MoviesDatabaseRepository
import com.example.mymoviesapplication.data.repositories.NetworkMoviesRepository
import com.example.mymoviesapplication.entities.Movie
import com.example.mymoviesapplication.models.MovieModel
import com.example.mymoviesapplication.ui.screens.movies.MoviesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


data class MyMoviesUiState(
    val myMoviesList: List<Movie> = emptyList(),
    var currentMovie: Movie? = null,
    val isShowingListPage: Boolean = true,
    val error: String? = null,
    val loading: Boolean = false,

    var search: String = ""
)

class MyMoviesViewModel(
    private val moviesDatabaseRepository: MoviesDatabaseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        MyMoviesUiState(
            myMoviesList = emptyList(),
            currentMovie = null,
            error = null,
            loading = false
        )
    )
    val uiState: StateFlow<MyMoviesUiState> = _uiState

    init {
        getMoviesFromDb()
    }

    fun updateCurrentMovie(selectedMovie: Movie) {
        _uiState.update {
            it.copy(currentMovie = selectedMovie)
        }
    }

    fun navigateToDetailPage() {
        _uiState.update {
            it.copy(isShowingListPage = false)
        }
    }

    fun navigateToListPage() {
        _uiState.update {
            it.copy(isShowingListPage = true)
        }
    }

    fun getMoviesFromDb() {
        _uiState.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
            moviesDatabaseRepository.getAllMoviesStream().collect { movies ->
                _uiState.update {
                    it.copy(myMoviesList = movies, loading = false)
                }
            }
        }
    }

    fun getMoviesById(id: Int) {
        val currentMovie = uiState.value.currentMovie
        _uiState.update {
            it.copy(
                loading = true
            )
        }
        if (currentMovie != null) {
            viewModelScope.launch {
                var dbObj = moviesDatabaseRepository.getMovieStream(id).firstOrNull()
                if (dbObj != null) {
                    _uiState.update {
                        it.copy(
                            currentMovie = dbObj,
                            loading = false
                        )
                    }
                }
            }

        }
    }

    fun searchMovies(search: String) {
        _uiState.update {
            it.copy(search = search)
        }

        viewModelScope.launch {
            moviesDatabaseRepository.searchMovies(search).collect { movies ->
                _uiState.update {
                    it.copy(myMoviesList = movies)
                }
            }
        }
    }

    fun updateRating(rating: Int) {
        var movie = uiState.value.currentMovie
        if (movie != null) {
            movie.myRating = rating
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

     fun removeMovie() {
       viewModelScope.launch {
           uiState.value.currentMovie?.let { moviesDatabaseRepository.deleteMovie(it) };
       }
    }
}