package com.example.mymoviesapplication.ui.screens.my_movies

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.mymoviesapplication.MoviesTopAppBar
import com.example.mymoviesapplication.R
import com.example.mymoviesapplication.entities.Movie
import com.example.mymoviesapplication.models.OrientationEnum
import com.example.mymoviesapplication.ui.AppViewModelProvider
import com.example.mymoviesapplication.ui.screens.components.ErrorComponent
import com.example.mymoviesapplication.ui.screens.components.LoadingComponent
import com.example.mymoviesapplication.ui.screens.movies.MovieDetailsScreen
import com.example.mymoviesapplication.ui.screens.movies.MoviesViewModel
import com.example.mymoviesapplication.ui.screens.movies.components.MoviesGridComponent
import com.example.mymoviesbaza.ui.screens.my_movies.MyMoviesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyMovieDetailsScreen(
    movie: Movie,
    onBackPressed: () -> Unit,
    onNavigateToScreen: (route: String) -> Unit,
    removeMovie: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: MyMoviesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    context: Context = LocalContext.current
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val layoutDirection = LocalLayoutDirection.current

    val configuration = LocalConfiguration.current.orientation
    var orientation by remember { mutableStateOf(OrientationEnum.Portrait.toString()) }
    LaunchedEffect(configuration) {
        if (configuration == Configuration.ORIENTATION_LANDSCAPE) {
            orientation = OrientationEnum.LandScape.toString()
        } else {
            orientation = OrientationEnum.Portrait.toString()
        }
    }

    val shareContent: (context: Context) -> Unit = {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "Preproučujem vam film '${movie.title}' koji ima ocjenu ${movie.rating}!")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)

        startActivity(it, shareIntent, null)
    }
    // COMPONENT FUNCTIONS

    @Composable()
    fun renderFloatingActionButton() {
        FloatingActionButton(
            onClick = {
                removeMovie()
                onBackPressed()
            },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "dadaj u favorite",
            )
        }
    }
    @Composable()
    fun renderHeader() {
        Box(
            modifier = modifier
                .padding(top = contentPadding.calculateTopPadding())
        ) {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.padding(8.dp) // Adjust padding as needed
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Add to Favorites"
                    )
                }
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = { shareContent(context) },
                    modifier = Modifier.padding(8.dp) // Adjust padding as needed
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Add to Favorites"
                    )
                }
            }
        }

    }
    @Composable()
    fun renderImage() {
        Box {
            Box {
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
                            400f
                        )
                    )
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }

    @Composable()
    fun renderDetails() {
        Row(
            modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)
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
        Row(
            modifier = Modifier
                .padding(top = 0.dp, bottom = 0.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = movie.timeline,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick =  {
                    onNavigateToScreen("review/"+ movie.id)
                },
                modifier = Modifier.padding(8.dp) // Adjust padding as needed
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Add Review"
                )
            }
        }

        if (uiState.currentMovie?.myRating != null) {
            MovieRating(
                rating = uiState.currentMovie!!.myRating!!,
                onClick = { rating ->
                    viewModel.updateRating(rating)
                }
            )
        } else {
            MovieRating(
                rating = 0,
                onClick = { rating ->
                    viewModel.updateRating(rating)
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .padding(bottom = 80.dp)
        ) {
            uiState.currentMovie?.description?.let { Text(text = it) }
            if (uiState.currentMovie?.review != null && uiState.currentMovie!!.review != "") {
                Text(text = "My Review", modifier = Modifier.padding(top = 10.dp), fontWeight = FontWeight.Bold)
                Text(text = uiState.currentMovie?.review!!)
            }
        }

    }
    // COMPONENT FUNCTIONS END

    Scaffold(floatingActionButton = {
        renderFloatingActionButton()
    },
        modifier = modifier
    ) {innerPadding ->
        Column(
            modifier = modifier
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            renderHeader()
            if(orientation == OrientationEnum.LandScape.toString()){
                //LANDSCAPE
                Row(
                    modifier = modifier
                        .verticalScroll(state = scrollState)
                        .padding(
                            bottom = contentPadding.calculateTopPadding(),
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection)
                        )
                ) {Box(
                    modifier = Modifier
                        .weight(1f / 3f)
                ) {
                    renderImage()
                }
                    Column(
                        modifier = Modifier
                            .weight(2f / 3f)
                    ) {
                        renderDetails()
                    }
                }
            }
            else {
                //PORTRET
                Column(
                    modifier = modifier
                        .verticalScroll(state = scrollState)
                        .padding(
                            bottom = contentPadding.calculateTopPadding(),
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection)
                        )
                ) {
                    renderImage()
                    renderDetails()
                }
            }
            /*Column(
                modifier = modifier
                    .verticalScroll(state = scrollState)
                    .padding(
                        bottom = contentPadding.calculateTopPadding(),
                        start = contentPadding.calculateStartPadding(layoutDirection),
                        end = contentPadding.calculateEndPadding(layoutDirection)
                    )
            ) {
                renderImage()
                renderDetails()
            }*/
        }
    }
}



@Composable
fun MovieRating(
    rating: Int,
    onClick: (value: Int) -> Unit,
) {
    val stars = (1..5).toList()

    Row(modifier = Modifier.padding(top = 0.dp)) {
        stars.forEach { star ->
            IconButton(
                onClick = { onClick(star) },
            ) {
                val icon = if (star <= rating) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                }
                Icon(
                    imageVector = icon,
                    contentDescription = "Rating Star"
                )
            }
        }
    }
}

