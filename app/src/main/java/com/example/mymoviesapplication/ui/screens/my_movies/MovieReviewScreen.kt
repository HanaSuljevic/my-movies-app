package com.example.mymoviesapplication.ui.screens.my_movies

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mymoviesapplication.MoviesTopAppBar
import com.example.mymoviesapplication.R
import com.example.mymoviesapplication.ui.AppViewModelProvider
import com.example.mymoviesapplication.ui.screens.components.LoadingComponent
import com.example.mymoviesbaza.ui.navigation.NavigationDestination
import com.example.mymoviesbaza.ui.screens.my_movies.MyMoviesViewModel

object MovieReviewDestination : NavigationDestination {
    override val route = "review/{id}"
    override val titleRes = R.string.movies_screen
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieReviewScreen(
    // id: passed from navigation
    id: String?,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: MovieReviewViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    var reviewText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(id) {
        if (id != null) {
            viewModel.getMoviewById(id.toInt())
        }
    }

    LaunchedEffect(uiState) {
        if (reviewText.isEmpty()) {
            reviewText = uiState.currentMovie?.review ?: ""
        }
    }

    val submitForm: () -> Unit = {
        viewModel.updateReview(reviewText)
        navController.previousBackStackEntry?.savedStateHandle?.set("resultStatus", true)
        navController.popBackStack()
    }

    Scaffold(
         topBar = {
                MoviesTopAppBar(
                    title = "Review",
                    canNavigateBack = true,
                    navigateUp = {
                        navController.navigateUp()
                    }
                )
        },
    ) { innerPadding ->
        if (uiState.loading) {
            LoadingComponent()
        }
        if (!uiState.loading){
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                uiState.currentMovie?.let {
                    Text(
                        text = "Write a review for '${it.title}'",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(bottom = 10.dp, top = 10.dp)
                            .fillMaxWidth()
                    )
                }
                TextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    placeholder = { Text("Write your review...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .height(100.dp)
                )
                Button(
                    onClick =  {
                        submitForm()
                    },
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Submit")
                }
            }
        }
    }
}