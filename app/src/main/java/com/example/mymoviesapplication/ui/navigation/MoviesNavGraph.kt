package com.example.mymoviesbaza.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymoviesapplication.ui.screens.movies.MoviesDestination
import com.example.mymoviesapplication.ui.screens.movies.MoviesScreen
import com.example.mymoviesapplication.ui.screens.my_movies.MovieReviewDestination
import com.example.mymoviesapplication.ui.screens.my_movies.MovieReviewScreen
import com.example.mymoviesbaza.ui.screens.home.HomeDestination
import com.example.mymoviesbaza.ui.screens.home.HomeScreen
import com.example.mymoviesbaza.ui.screens.my_movies.MyMoviesDestination
import com.example.mymoviesbaza.ui.screens.my_movies.MyMoviesScreen


/**
 * Provides Navigation graph for the application.
 */
@Composable
fun MoviesNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                onNavigateToScreen = { navController.navigate(it) }
            )
        }

        composable(route = MoviesDestination.route) {
            MoviesScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(route = MyMoviesDestination.route) {backStackEntry ->
            val isReviewUpdated = backStackEntry.savedStateHandle.get<Boolean>("resultStatus")
            MyMoviesScreen(
                navigateBack = { navController.navigateUp() },
                onNavigateToScreen = { navController.navigate(it) },
                isReviewUpdated = isReviewUpdated
            )
        }
        composable(route = MovieReviewDestination.route) { backStackEntry ->
            // Extract the ID from the route
            val id = backStackEntry.arguments?.getString("id")

            // Call the composable for the review screen with the ID
            MovieReviewScreen(
                id = id,
                navController = navController
            )
        }
    }
}