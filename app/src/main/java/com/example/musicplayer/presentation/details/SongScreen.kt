package com.example.musicplayer.presentation.details

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.imageLoader
import coil.request.ImageRequest
import com.example.musicplayer.R
import com.example.musicplayer.Screen
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.presentation.components.LyricsTopView
import com.example.musicplayer.presentation.components.PlayPause
import com.example.musicplayer.presentation.components.SongFullPlayerMidSection
import com.example.musicplayer.presentation.greetings.TopSection


@ExperimentalMaterialApi
@Composable
fun SongScreen(
    modifier: Modifier = Modifier,
    songViewModel: SongViewModel = hiltViewModel(),
    navController: NavController
) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val uiState = songViewModel.uiState.value
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }
    val finish: () -> Unit = {
        navController.navigate(Screen.MusicFeedScreen.route)
    }

    LaunchedEffect(key1 = uiState.image) {
        setColorFromImage(
            url = uiState.image,
            context = context,
            viewModel = songViewModel
        ) {
            dominantColor = it
        }
    }
    BackHandler {
        finish()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor,
                        dominantColor
                    )
                )
            )
            .verticalScroll(
                state = scrollState
            )
    ) {

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
        uiState.currentPlayingSong?.let {
            SongFullPlayerMidSection(song = it)
        }


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

        Spacer(modifier = modifier.height(20.dp))
        LyricsBox(
            dominantColor = dominantColor,
            navController = navController
        )

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
        PlayPause(
            modifier = modifier,
            onPlayPauseButtonPressed = onPlayPauseButtonPressed,
            song = song,
            isPlaying = isPlaying,
            backgroundColor = "#F5B501",
            contentColor = Color.White
        )
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
            .padding(
                start = 10.dp,
                end = 10.dp
            ),
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
        viewModel.calcDominantColor(it) { color ->
            onImageLoaded.invoke(color)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun LyricsBox(
    modifier: Modifier = Modifier,
    dominantColor: Color,
    viewModel: SongViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState = viewModel.uiState.value
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 70.dp, start = 20.dp, end = 20.dp)
            .background(
                color = dominantColor,
                shape = CircleShape.copy(
                    all = CornerSize(2)
                )
            )
            .clickable {
                navController.navigate("lyrics_screen/${dominantColor.toArgb()}")
            }
    ) {
        LyricsTopView(
            modifier = modifier,
            dominantColor = dominantColor,
            navController = navController,
            booleanIsFrom = false
        )
        Text(
            text = uiState.lyrics,
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            fontSize = 22.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight(500),
            overflow = TextOverflow.Ellipsis,
            maxLines = 11,
            color = dominantColor.copy(
                alpha = 1f,
                red = 0.63f,
                green = 0.55f,
                blue = 0.55f
            )
        )
    }
}

