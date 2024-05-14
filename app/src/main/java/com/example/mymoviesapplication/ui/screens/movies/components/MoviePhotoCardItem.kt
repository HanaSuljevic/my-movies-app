package com.example.mymoviesapplication.ui.screens.movies.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymoviesapplication.R
import com.example.mymoviesapplication.models.MovieModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviePhotoCardItem(
    movie: MovieModel,
    onItemClick: (MovieModel) -> Unit
) {
    Column(
        modifier = Modifier.padding(5.dp),
    ) {
        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            onClick = { onItemClick(movie) }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current).data(movie.image)
                    .crossfade(true).build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.movie_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(
            text = movie.title,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 5.dp, bottom = 15.dp)
        )
    }
}