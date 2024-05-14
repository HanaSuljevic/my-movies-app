package com.example.mymoviesapplication.data.repositories

import com.example.mymoviesapplication.data.DAOs.MovieDao
import com.example.mymoviesapplication.entities.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesDatabaseRepository {
    fun getAllMoviesStream(): Flow<List<Movie>>

    fun getMovieStream(id: Int): Flow<Movie?>

    suspend fun getMovieByTitle(title: String): Flow<Movie?>

    suspend fun searchMovies(search: String): Flow<List<Movie>>

    suspend fun insertMovie(item: Movie)

    suspend fun deleteMovie(item: Movie)

    suspend fun updateMovie(item: Movie)
}

class OfflineMoviesRepository(private val movieDao: MovieDao) : MoviesDatabaseRepository {
    override fun getAllMoviesStream(): Flow<List<Movie>> = movieDao.getAllMovies()

    override fun getMovieStream(id: Int): Flow<Movie?> = movieDao.getMovie(id)

    override suspend fun getMovieByTitle(title: String): Flow<Movie?> = movieDao.getMovieByTitle(title)

    override suspend fun searchMovies(search: String): Flow<List<Movie>> = movieDao.searchMovies(search)

    override suspend fun insertMovie(movie: Movie) = movieDao.insert(movie)

    override suspend fun deleteMovie(movie: Movie) = movieDao.delete(movie)

    override suspend fun updateMovie(movie: Movie) = movieDao.update(movie)
}