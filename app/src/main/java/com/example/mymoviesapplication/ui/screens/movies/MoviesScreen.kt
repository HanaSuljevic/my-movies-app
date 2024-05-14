package com.example.mymoviesapplication.ui.screens.movies

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymoviesapplication.MoviesTopAppBar
import com.example.mymoviesapplication.R
import com.example.mymoviesapplication.ui.AppViewModelProvider
import com.example.mymoviesapplication.ui.screens.components.ErrorComponent
import com.example.mymoviesapplication.ui.screens.components.LoadingComponent
import com.example.mymoviesapplication.ui.screens.movies.components.MovieGenreFilterComponent
import com.example.mymoviesapplication.ui.screens.movies.components.MoviesGridComponent
import com.example.mymoviesbaza.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object MoviesDestination : NavigationDestination {
    override val route = "movies"
    override val titleRes = R.string.movies_screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()

    // COMPONENT FUNCTIONS
    @Composable
    fun renderLoading() {
        if (uiState.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingComponent()
            }
        }
    }

    @Composable
    fun renderError() {
        if (uiState.error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ErrorComponent(retryAction = { /*TODO*/ })
            }
        }
    }

    @Composable
    fun renderFilter(innerPadding: PaddingValues) {
        if (uiState.isShowingListPage) {
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                MovieGenreFilterComponent(
                    onGenreSelected = {genre -> viewModel.getMovies(genre)},
                    selectedGenre = uiState.selectedGenre,
                    disabled = uiState.loading
                )
            }
        }
    }

    @Composable
    fun renderMovies(innerPadding: PaddingValues) {
        Column (
            modifier = Modifier.padding(innerPadding)
        ) {
            if (!uiState.loading) {
                if (uiState.isShowingListPage) {
                    MoviesGridComponent(
                        movies = uiState.moviesList,
                        contentPadding = innerPadding,
                        onClick = {
                            viewModel.updateCurrentMovie(it)
                            viewModel.navigateToDetailPage()
                        }
                    )
                } else {
                    uiState.currentMovie?.let {
                        MovieDetailsScreen(
                            movie = it,
                            onBackPressed = {
                                viewModel.navigateToListPage()
                            },
                            addMovie = {
                                viewModel.saveItem()
                            }
                        )
                    }
                }
            }
        }
    }
    // COMPONENT FUNCTIONS END

    // BODY
    Scaffold(
        modifier = modifier,
        topBar = {
            if (uiState.isShowingListPage) {
                MoviesTopAppBar(
                    title = stringResource(R.string.movies_screen),
                    canNavigateBack = true,
                    navigateUp = navigateBack
                )
            }
        },
    ) { innerPadding ->
        renderFilter(innerPadding)
        renderLoading()
        renderError()
        renderMovies(innerPadding)
    }
}