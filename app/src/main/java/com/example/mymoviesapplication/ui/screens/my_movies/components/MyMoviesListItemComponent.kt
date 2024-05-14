package com.example.mymoviesapplication.ui.screens.my_movies.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mymoviesapplication.R
import com.example.mymoviesapplication.entities.Movie


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMoviesListItemComponent(
    movie: Movie,
    onItemClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        onClick = { onItemClick(movie) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp)
        ) {
            MyMoviesItemImage(
                movie = movie,
                modifier = Modifier.size(150.dp)
            )
            Column(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(R.dimen.padding_small),
                        horizontal = dimensionResource(R.dimen.padding_medium)
                    )
                    .weight(1f)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.card_text_vertical_space))
                )
                Text(
                    text = movie.year,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
                if (movie.myRating != null) {
                    Row(
                        modifier = modifier.padding(top = 10.dp)
                    ) {
                        Text(
                            text = movie.myRating.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Rating Star"
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Row {
                    Text(
                        text = movie.timeline,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = movie.rating,
                        style = MaterialTheme.typography.bodyLarge
                    )

                }
                movie.review?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3,
                    )
                }
            }
        }
    }
}