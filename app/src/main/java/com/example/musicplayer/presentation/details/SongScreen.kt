package com.example.musicplayer.presentation.details

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.Coil
import coil.imageLoader
import coil.request.ImageRequest
import com.example.musicplayer.R
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.presentation.greetings.TopSection

@Composable
fun SongScreen(
    modifier: Modifier = Modifier,
    songViewModel: SongViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState = songViewModel.uiState.value
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }

    LaunchedEffect(key1 = uiState.image) {
        setColorFromImage(
            url = uiState.image,
            context = context,
            viewModel = songViewModel
        ) {
            dominantColor = it
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = modifier) {
            TopSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 25.dp, top = 43.dp),
            )
            Spacer(modifier = modifier.height(70.dp))
            TopView(
                modifier = modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = modifier.height(50.dp))
            SongView(
                modifier = modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )
            Spacer(modifier = modifier.height(50.dp))
            PausePlaySection(
                modifier = modifier,
                isPlaying = uiState.isPlaying,
                song = uiState.currentPlayingSong!!,
                onPlayPauseButtonPressed = songViewModel::onPlayPauseButtonPressed
            )
            Spacer(modifier = modifier.height(20.dp))
            Slider(
                progress = songViewModel.uiState.value.sliderValue,
                onSliderValueChange = songViewModel::onSliderValueChange,
                state = songViewModel.uiState.value.musicSliderState
            )
        }
    }
}


@Composable
fun TopView(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ImageInTopView(
            painter = painterResource(id = R.drawable.ic_baseline_list_24)
        )
        ImageInTopView(
            painter = painterResource(id = R.drawable.ic_baseline_compare_arrows_24)
        )
        ImageInTopView(
            painter = painterResource(id = R.drawable.ic_baseline_heart_broken_24)
        )
        ImageInTopView(
            painter = painterResource(id = R.drawable.ic_baseline_campaign_24)
        )
    }
}

@Composable
fun ImageInTopView(
    modifier: Modifier = Modifier,
    painter: Painter
) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
    )
}


@Composable
fun SongView(
    modifier: Modifier = Modifier,
    songViewModel: SongViewModel = hiltViewModel()
) {
    val uiState = songViewModel.uiState.value
    LazyColumn(
        modifier = modifier
    ) {
        item {
            uiState.currentPlayingSong?.let {
                SongViewPagerList(song = it)
            }

        }
    }
}

@Composable
fun PausePlaySection(
    modifier: Modifier = Modifier,
    song: Song,
    onPlayPauseButtonPressed: (Song) -> Unit,
    isPlaying: Boolean = true,
    songViewModel: SongViewModel = hiltViewModel()
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_skip_previous_24),
            contentDescription = "skip_previous",
            modifier = modifier
                .clickable {
                    songViewModel.skipToPreviousSong()
                }
        )
        Spacer(modifier = modifier.width(35.43.dp))
        Button(
            modifier = modifier
                .size(74.dp),
            onClick = {
                onPlayPauseButtonPressed(song)
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(android.graphics.Color.parseColor("#F5B501")),
                contentColor = Color.White
            )
        ) {
            if (isPlaying) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_baseline_pause_24
                    ),
                    contentDescription = "play_pause",
                    modifier = modifier
                        .size(50.dp)
                )
            } else {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_baseline_play_arrow_24
                    ),
                    contentDescription = "play_pause",
                    modifier = modifier
                        .size(50.dp)
                )
            }

        }
        Spacer(modifier = modifier.width(37.5.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_skip_next_24),
            contentDescription = "skip_next",
            modifier = modifier
                .clickable {
                    songViewModel.skipToNextSong()
                }
        )
    }
}

@Composable
fun Slider(
    modifier: Modifier = Modifier,
    progress: Float,
    onSliderValueChange: (Float) -> Unit,
    state: MusicSliderState
) {
    Slider(
        value = progress,
        onValueChange = onSliderValueChange,
        colors = SliderDefaults.colors(
            activeTrackColor = Color.Black,
            inactiveTrackColor = Color.LightGray,
            thumbColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 46.dp, end = 10.dp)
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = state.timePassedFormatted,
            color = Color.Black
        )
        Text(
            text = state.timeLeftFormatted,
            color = Color.Black
        )
    }
}

private suspend fun setColorFromImage(
    url: String,
    context: Context,
    viewModel: SongViewModel,
    onImageLoaded: (Color) -> Unit
) {
    val request = ImageRequest.Builder(context).data(url).build()
    request.context.imageLoader.execute(request).drawable?.let {
        viewModel.calcDominantColor(it){ color ->
            onImageLoaded.invoke(color)
        }
    }
}

