package com.example.mymoviesapplication.data.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mymoviesapplication.entities.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * from movies WHERE id = :id")
    fun getMovie(id: Int): Flow<Movie>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%' OR year LIKE '%' || :search || '%'")
    fun searchMovies(search: String): Flow<List<Movie>>

    @Query("SELECT * from movies WHERE title = :title")
    fun getMovieByTitle(title: String): Flow<Movie>

    @Query("SELECT * from movies ORDER BY title ASC")
    fun getAllMovies(): Flow<List<Movie>>
}