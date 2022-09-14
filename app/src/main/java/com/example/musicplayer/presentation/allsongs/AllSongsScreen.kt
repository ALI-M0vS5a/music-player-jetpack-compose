package com.example.musicplayer.presentation.allsongs


import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import com.example.musicplayer.R
import com.example.musicplayer.Screen
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.presentation.feed.MusicListItem
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun AllSongsScreen(
    allSongsViewModel: AllSongsViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState = allSongsViewModel.uiState.value
    val defaultDominantColor = MaterialTheme.colors.surface
    val context = LocalContext.current
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }
    val finish: () -> Unit = {
        navController.navigate(Screen.MusicFeedScreen.route)
    }
    BackHandler {
        finish()
    }
    LaunchedEffect(key1 = uiState.image) {
        setColorFromImage(
            url = uiState.image,
            context = context,
            viewModel = allSongsViewModel
        ) {
            dominantColor = it
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            ),
        contentAlignment = Alignment.BottomStart
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(contentPadding = PaddingValues(
                bottom = 300.dp,
                top = 50.dp
            ),
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                items(
                    items = uiState.songList
                ) { song ->
                    MusicListItem(
                        song = song,
                        onClick = {
                            allSongsViewModel.playOrToggleSong(it)
                        }
                    )

                }

            }
        }
        uiState.currentPlayingSong?.let {
            CurrentlyPlayingItem(
                song = it,
                isPlaying = uiState.isPlaying,
                progress = uiState.sliderValue,
                onPlayPauseButtonPress = allSongsViewModel::onPlayPauseButtonPressed,
                onSliderValueChange = allSongsViewModel::onSliderValueChange,
                modifier = Modifier
                    .height(88.dp),
                defaultDominantColor = defaultDominantColor,
                dominantColor = dominantColor
            )
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun CurrentlyPlayingItem(
    song: Song,
    isPlaying: Boolean,
    progress: Float,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    onPlayPauseButtonPress: (Song) -> Unit,
    onSliderValueChange: (Float) -> Unit,
    defaultDominantColor: Color,
    dominantColor: Color

) {
    Box(
        modifier = modifier.wrapContentHeight()

    ) {
        Surface(
            onClick = {},
            shape = shape,
            modifier = modifier,
            color = MaterialTheme.colors.surface,
            elevation = 4.dp
        ) {
            Row(
                modifier = modifier
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                defaultDominantColor,
                                dominantColor
                            )
                        )
                    )
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(song.imageUrl),
                    contentDescription = "Song cover",
                    modifier = modifier
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = song.title,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = song.subtitle,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                PlayPauseButton(
                    song = song,
                    isPlaying = isPlaying,
                    onPlayPauseButtonPress = onPlayPauseButtonPress,
                    modifier = modifier
                )
            }
        }
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(top = 58.dp)
                .align(Alignment.BottomCenter),
            progress = progress,
            color = Color.White
        )
    }
}

@Composable
fun PlayPauseButton(
    song: Song,
    isPlaying: Boolean,
    onPlayPauseButtonPress: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    RoundImageButton(
        image = getPlayPauseIcon(isPlaying),
        iconTint = Color.Black,
        backgroundColor = Color.Transparent,
        contentDescription = if (isPlaying) PAUSE_MUSIC_CD else PLAY_MUSIC_CD,
        onClick = {
            onPlayPauseButtonPress(song)
        },
        modifier = modifier
    )
}

@Composable
fun RoundImageButton(
    image: Int,
    iconTint: Color,
    backgroundColor: Color,
    contentDescription: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(CircleShape)
            .background(color = backgroundColor),
        enabled = isEnabled,
    ) {
        Icon(
            painter = painterResource(id = image),
            contentDescription = contentDescription,
            tint = iconTint.copy(alpha = if (isEnabled) 1f else 0.5f),
            modifier = modifier
                .size(50.dp)
        )
    }
}

fun getPlayPauseIcon(isPlaying: Boolean) =
    if (isPlaying) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24

const val PLAY_MUSIC_CD = "Play music"
const val PAUSE_MUSIC_CD = "Pause music"

private suspend fun setColorFromImage(
    url: String,
    context: Context,
    viewModel: AllSongsViewModel,
    onImageLoaded: (Color) -> Unit
) {
    val request = ImageRequest.Builder(context).data(url).build()
    request.context.imageLoader.execute(request).drawable?.let {
        viewModel.calcDominantColor(it) { color ->
            onImageLoaded.invoke(color)
        }
    }
}

