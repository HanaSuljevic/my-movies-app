package com.example.mymoviesapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mymoviesapplication.data.DAOs.MovieDao
import com.example.mymoviesapplication.entities.Movie

/**
* Database class with a singleton Instance object.
*/
@Database(entities = [Movie::class], version = 2, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var Instance: MoviesDatabase? = null

        fun getDatabase(context: Context): MoviesDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                val migration1to2 = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        // Add the necessary SQL commands to alter the table and add the new column
                        database.execSQL("ALTER TABLE movies ADD COLUMN description TEXT")
                    }
                }
                Room.databaseBuilder(context, MoviesDatabase::class.java, "movies_database")
                    .addMigrations(migration1to2)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}