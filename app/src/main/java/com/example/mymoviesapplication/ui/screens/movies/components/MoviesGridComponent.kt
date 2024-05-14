package com.example.mymoviesapplication.ui.screens.movies.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymoviesapplication.models.MovieModel
import com.example.mymoviesapplication.ui.AppViewModelProvider
import com.example.mymoviesapplication.ui.screens.movies.MoviesViewModel

@Composable
fun MoviesGridComponent(
    movies: List<MovieModel>,
    onClick: (MovieModel) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(modifier = Modifier.padding(top = contentPadding.calculateTopPadding())) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(180.dp),
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(bottom = 10.dp),
        ) {
            items(items = movies, key = { movie -> movie.title }) { movie ->
                MoviePhotoCardItem(
                    movie,
                    onClick
                )
            }
        }
    }
}