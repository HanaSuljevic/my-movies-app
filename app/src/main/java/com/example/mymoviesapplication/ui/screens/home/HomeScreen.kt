package com.example.mymoviesbaza.ui.screens.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.fonts.FontStyle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mymoviesapplication.MoviesTopAppBar
import com.example.mymoviesapplication.R
import com.example.mymoviesapplication.models.OrientationEnum
import com.example.mymoviesapplication.ui.screens.movies.MoviesDestination
import com.example.mymoviesbaza.ui.navigation.NavigationDestination
import com.example.mymoviesbaza.ui.screens.my_movies.MyMoviesDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    onNavigateToScreen: (route: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current.orientation
    var orientation by remember { mutableStateOf(OrientationEnum.Portrait.toString()) }
    LaunchedEffect(configuration) {
        if (configuration == Configuration.ORIENTATION_LANDSCAPE) {
            orientation = OrientationEnum.LandScape.toString()
        } else {
            orientation = OrientationEnum.Portrait.toString()
        }
    }

    // COMPONENT FUNCTION
    @Composable
    fun renderMenuItem(
        route: String = MyMoviesDestination.route,
        icon: Int = R.drawable.popcorn_icon,
        title: Int = R.string.my_movies_screen,
        description: String = stringResource(R.string.my_movies_button_desc),
        cardColors: CardColors = CardDefaults.cardColors(Color.White),
    ) {
        Card(
            onClick = { onNavigateToScreen(route) },
            colors = cardColors,
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "Menu icon",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 10.dp)
                    )
                    Column {
                        Text(
                            text = stringResource(title),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
    // COMPONENT FUNCTION END

    // BODY
    Scaffold(
        modifier = modifier,
        topBar = {
            MoviesTopAppBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = false,
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = stringResource(R.string.welcome))
            
        }
        if (orientation == OrientationEnum.LandScape.toString()) {
            Column {
                Box(modifier = Modifier.weight(1f))
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                ) {
                    Column(Modifier.weight(1f)) {
                        renderMenuItem(
                            route = MoviesDestination.route,
                            icon = R.drawable.movie_icon,
                            title = R.string.movies_screen,
                            description = stringResource(R.string.movies_button_desc),
                            cardColors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        renderMenuItem()
                    }
                }
            }
        }
        else {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.welcome),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                    )
                }
                renderMenuItem(
                    route = MoviesDestination.route,
                    icon = R.drawable.movie_icon,
                    title = R.string.movies_screen,
                    description = stringResource(R.string.movies_button_desc),
                    cardColors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                )
                renderMenuItem()
            }
        }
    }
}