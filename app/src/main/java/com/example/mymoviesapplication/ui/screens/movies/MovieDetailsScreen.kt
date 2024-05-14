package com.example.mymoviesapplication.ui.screens.movies

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymoviesapplication.R
import com.example.mymoviesapplication.models.MovieModel
import com.example.mymoviesapplication.models.OrientationEnum
import com.example.mymoviesapplication.ui.AppViewModelProvider

@Composable
fun MovieDetailsScreen(
    movie: MovieModel,
    onBackPressed: () -> Unit,
    addMovie: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    BackHandler(enabled = true, onBackPressed)
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val configuration = LocalConfiguration.current.orientation
    var orientation by remember { mutableStateOf(OrientationEnum.Portrait.toString()) }
    LaunchedEffect(configuration) {
        if (configuration == Configuration.ORIENTATION_LANDSCAPE) {
            orientation = OrientationEnum.LandScape.toString()
        } else {
            orientation = OrientationEnum.Portrait.toString()
        }
    }

    // COMPONENT FUNCTIONS
    @Composable
    fun renderFloatingButton() {
        FloatingActionButton(
            onClick = {
                addMovie()
                onBackPressed()
            },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))

        ) {
            Icon(
                imageVector = if (uiState.isFavourite) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = "dadaj u favorite",
            )
        }
    }

    @Composable
    fun renderHeader() {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "On go Back"
                    )
                }
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    @Composable
    fun renderImage() {
        Box {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current).data(movie.image)
                        .crossfade(true).build(),
                    error = painterResource(R.drawable.ic_broken_image),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = stringResource(R.string.movie_photo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, MaterialTheme.colorScheme.scrim),
                            0f,
                            200f
                        )
                    )
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                        .padding(bottom = 10.dp)
                )
            }
        }
    }

    @Composable
    fun renderInfo() {
        Column {
            Row(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = movie.year,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = movie.rating,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = movie.timeline,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Column(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .padding(bottom = 80.dp)
            ) {
                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
    // COMPONENT FUNCTION END

    // BODY
    Scaffold(
        floatingActionButton = {
            renderFloatingButton()
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .verticalScroll(state = scrollState)
                .padding(
                    bottom = innerPadding.calculateTopPadding(),
                )
        ) {
            renderHeader()
            if (orientation == OrientationEnum.LandScape.toString()) {
                Row(
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f / 3f)
                    ) {
                        renderImage()
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f / 3f)
                    ) {
                        renderInfo()
                    }
                }
            } else {
                Column(
                ) {
                    renderImage()
                    renderInfo()
                }
            }
        }
    }
}