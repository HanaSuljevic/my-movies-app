package com.example.mymoviesapplication.ui.screens.my_movies.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymoviesapplication.R
import com.example.mymoviesapplication.entities.Movie
import com.example.mymoviesapplication.ui.AppViewModelProvider
import com.example.mymoviesapplication.ui.screens.components.EmptyListComponent
import com.example.mymoviesapplication.ui.screens.components.LoadingComponent
import com.example.mymoviesapplication.ui.screens.movies.MoviesDestination
import com.example.mymoviesbaza.ui.screens.my_movies.MyMoviesViewModel

@Composable
fun MyMoviesListComponent(
    movies: List<Movie>,
    onClick: (Movie) -> Unit,
    onSearch: (String) -> Unit,
    onNavigateToScreen: (route: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: MyMoviesViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = modifier
    ) {
        TextField(
            value = uiState.search,
            onValueChange = { onSearch(it) },
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(16.dp)
        )
        if (movies.isEmpty()) {
            EmptyListComponent(
                buttonTitle = "Add movies",
                retryAction = { onNavigateToScreen(MoviesDestination.route) }
            )
        } else {
            LazyColumn(
                contentPadding = contentPadding,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            ) {
                items(movies, key = { movie -> movie.id }) { movie ->
                    MyMoviesListItemComponent(
                        movie = movie,
                        onItemClick = onClick
                    )
                }
            }

        }
    }
}