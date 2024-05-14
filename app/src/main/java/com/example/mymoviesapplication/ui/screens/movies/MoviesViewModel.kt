package com.example.mymoviesapplication.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoviesapplication.data.repositories.MoviesDatabaseRepository
import com.example.mymoviesapplication.data.repositories.MoviesRepository
import com.example.mymoviesapplication.data.repositories.NetworkMoviesRepository
import com.example.mymoviesapplication.entities.Movie
import com.example.mymoviesapplication.models.MovieGenre
import com.example.mymoviesapplication.models.MovieModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


data class MoviesUiState(
    val moviesList: List<MovieModel> = emptyList(),
    val currentMovie: MovieModel? = null,
    val isShowingListPage: Boolean = true,
    val error: String? = null,
    val loading: Boolean = false,

    val isFavourite: Boolean = false,
    val movieDbObj: Movie? = null,
    val selectedGenre: String = MovieGenre.action.toString()
)

class MoviesViewModel(
    private val moviesDatabaseRepository: MoviesDatabaseRepository,
    private val moviesServiceRepository: MoviesRepository
)  : ViewModel() {
    private val _uiState = MutableStateFlow(
        MoviesUiState(
            moviesList = emptyList(),
            currentMovie = null,
            error = null,
            loading = false
        )
    )
    val uiState: StateFlow<MoviesUiState> = _uiState

    init {
        getMovies(MovieGenre.action.toString())
    }

    fun updateCurrentMovie(selectedMovie: MovieModel) {
        _uiState.update {
            it.copy(currentMovie = selectedMovie)
        }
        viewModelScope.launch {
            getMovieByTitleFromDb(selectedMovie.title)
        }
    }
    fun navigateToDetailPage() {
        _uiState.update {
            it.copy(isShowingListPage = false)
        }
    }
    fun navigateToListPage(){
        _uiState.update {
            it.copy(isShowingListPage = true)
        }
    }
    fun getMovies(genre: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(loading = true, selectedGenre = genre)
            }
            try {
                val response = moviesServiceRepository.getMovies(genre)
                _uiState.update {
                    it.copy(
                        moviesList = response.movies,
                        loading = false
                    )
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        error = "Došlo je do greške",
                        loading = false
                    )
                }
            } catch (e: HttpException) {
                _uiState.update {
                    it.copy(
                        error = "Došlo je do greške",
                        loading = false
                    )
                }
            }
        }
    }

    private suspend fun getMovieByTitleFromDb(title: String) {
        val currentMovie = uiState.value.currentMovie
        if (currentMovie != null) {
            var dbObj = moviesDatabaseRepository.getMovieByTitle(title).firstOrNull()
            if (dbObj != null) {
                _uiState.update {
                    it.copy(
                        isFavourite = true,
                        movieDbObj = dbObj
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isFavourite = false,
                        movieDbObj = null,
                    )
                }
            }
        }
    }

    fun saveItem() {
        val currentMovie = uiState.value.currentMovie
        val movie = currentMovie?.let {
            Movie(
                title = currentMovie.title,
                year = currentMovie.year,
                timeline = currentMovie.timeline,
                rating = currentMovie.rating,
                image = currentMovie.image,
                description = currentMovie.description,
                review = null,
                myRating =null,
            )
        }
        viewModelScope.launch {
            if (!uiState.value.isFavourite) {
                movie?.let { moviesDatabaseRepository.insertMovie(movie) }
            } else {
                uiState.value.movieDbObj?.let { moviesDatabaseRepository.deleteMovie(it) }
            }
        }
    }
}