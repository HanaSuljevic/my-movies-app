package com.example.mymoviesapplication.ui.screens.movies.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mymoviesapplication.models.MovieGenre

@Composable
fun MovieGenreFilterComponent(
    onGenreSelected: (String) -> Unit,
    selectedGenre: String = MovieGenre.action.toString(),
    disabled: Boolean = false
) {
    val genres = MovieGenre.values()

    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(bottom = 10.dp, top = 10.dp)
    ) {
        genres.forEach { genre ->
            Button(
                onClick = { onGenreSelected(genre.name) },
                modifier = Modifier.padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    if (selectedGenre == genre.toString()) MaterialTheme.colorScheme.primary else Color.Gray,
                    contentColor = Color.White
                ),
                enabled = !disabled
            ) {
                Text(text = genre.name.capitalize())
            }
        }
    }
}