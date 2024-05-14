package com.example.mymoviesbaza.ui.screens.my_movies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymoviesapplication.MoviesTopAppBar
import com.example.mymoviesapplication.R
import com.example.mymoviesapplication.ui.AppViewModelProvider
import com.example.mymoviesapplication.ui.screens.components.ErrorComponent
import com.example.mymoviesapplication.ui.screens.components.LoadingComponent
import com.example.mymoviesapplication.ui.screens.my_movies.MyMovieDetailsScreen
import com.example.mymoviesapplication.ui.screens.my_movies.components.MyMoviesListComponent
import com.example.mymoviesbaza.ui.navigation.NavigationDestination

object MyMoviesDestination : NavigationDestination {
    override val route = "my-movies"
    override val titleRes = R.string.my_movies_screen
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMoviesScreen(
    navigateBack: () -> Unit,
    onNavigateToScreen: (route: String) -> Unit,
    isReviewUpdated: Boolean? = null,
    modifier: Modifier = Modifier,
    viewModel: MyMoviesViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(isReviewUpdated) {
        if (isReviewUpdated == true) {
            uiState.currentMovie?.let { viewModel.getMoviesById(it.id) }
        }
    }

    //COMPONENT FUNCTIONS
    @Composable
    fun renderLoading(){
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
                ErrorComponent(retryAction = { viewModel.getMoviesFromDb() })
            }
        }
    }
    @Composable
    fun renderMovies(innerPadding: PaddingValues) {
        if (!uiState.loading) {
            if(uiState.isShowingListPage) {
                MyMoviesListComponent(
                    movies = uiState.myMoviesList,
                    onClick = {
                        viewModel.updateCurrentMovie(it)
                        viewModel.navigateToDetailPage()
                    },
                    onSearch = {
                            search ->
                        viewModel.searchMovies(search)
                    },
                    onNavigateToScreen = onNavigateToScreen,
                    modifier = modifier
                        .padding(innerPadding)
                )
            } else {
                uiState.currentMovie?.let {
                    MyMovieDetailsScreen(
                        movie = it,
                        onBackPressed = { viewModel.navigateToListPage() },
                        onNavigateToScreen = onNavigateToScreen,
                        removeMovie = {
                            viewModel.removeMovie()
                            viewModel.navigateToListPage()
                        },
                        contentPadding = innerPadding,
                    )
                }
            }
        }
        
    }
    //COMPONENT FUNCTIONS END

    //BODY
    Scaffold(
        modifier = modifier,
        topBar = {
            if (uiState.isShowingListPage) {
                MoviesTopAppBar(
                    title = stringResource(R.string.my_movies_screen),
                    canNavigateBack = true,
                    navigateUp = navigateBack
                )
            }
        },
    ) { innerPadding ->
        renderLoading()
        renderError()
        renderMovies(innerPadding = innerPadding)

    }
}