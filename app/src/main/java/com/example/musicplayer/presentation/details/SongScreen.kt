package com.example.musicplayer.presentation.details

import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicplayer.MainViewModel
import com.example.musicplayer.R
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.exoplayer.isPlaying
import com.example.musicplayer.exoplayer.toSong
import com.example.musicplayer.presentation.greetings.TopSection
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SongScreen(
    modifier: Modifier = Modifier,
    songViewModel: SongViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    var progress by remember {
        mutableStateOf(0f)
    }
    var curPlayerMilliSeconds by remember {
        mutableStateOf(0L)
    }
    var curSongDuration by remember {
        mutableStateOf(0L)
    }

    val state = rememberPagerState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    var curPlayingSong: Song? = null
    var playbackState: PlaybackStateCompat? = null
    val listOfAllSongs = mainViewModel.mediaItem.observeAsState(listOf()).value

    mainViewModel.playbackState.observeAsState().value?.let {
        playbackState = it


    }

    songViewModel.curPlayingPosition.observe(
        lifecycleOwner
    ) {
        curPlayerMilliSeconds = it

    }
    songViewModel.curSongDuration.observe(
        lifecycleOwner
    ){
        curSongDuration = it
    }


    mainViewModel.curPlayingSong.observe(lifecycleOwner) {
        if (it == null) return@observe
        curPlayingSong = it.toSong()
        fun switchToCurrentPlayingSong(song: Song) {
            val newItemIndex = listOfAllSongs.indexOf(song)
            if (newItemIndex != -1) {
                scope.launch {
                    state.scrollToPage(newItemIndex)
                    curPlayingSong = song
                }
            }
        }
        switchToCurrentPlayingSong(curPlayingSong ?: return@observe)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
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
            SongViewPager(
                listOfAllSongs = listOfAllSongs,
                state = state,
                modifier = modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )
            Spacer(modifier = modifier.height(50.dp))
            PausePlaySection(
                modifier = modifier,
                curPlayingSong = curPlayingSong,
                playbackState = playbackState
            )
            Spacer(modifier = modifier.height(20.dp))
            Slider(
                value = progress,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = SliderDefaults.colors(
                    activeTrackColor = Color.Black.copy(alpha = 0.7f),
                    inactiveTrackColor = Color.Black.copy(alpha = 0.4f),
                    thumbColor = Color.Black
                ),
                onValueChange = {
                    progress = it
                },
                enabled = true,
                steps = 5,
                onValueChangeFinished = {

                },
                interactionSource = MutableInteractionSource(),
                valueRange = 0f..100f,
            )
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
                val textCurTime = dateFormat.format(curPlayerMilliSeconds)
                Text(text = textCurTime)
                Text(text = dateFormat.format(curSongDuration))
            }
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


@OptIn(ExperimentalPagerApi::class)
@Composable
fun SongViewPager(
    modifier: Modifier = Modifier,
    listOfAllSongs: List<Song>,
    state: PagerState
) {
    HorizontalPager(
        count = listOfAllSongs.size,
        state = state,
        modifier = modifier
    ) { page ->
        SongViewPagerList(
            song = listOfAllSongs[page]
        )
    }
}

@Composable
fun PausePlaySection(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
    curPlayingSong: Song? = null,
    playbackState: PlaybackStateCompat? = null
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
                    mainViewModel.skipTpPreviousSong()
                }
        )
        Spacer(modifier = modifier.width(35.43.dp))
        Button(
            modifier = modifier
                .size(74.dp),
            onClick = {
                curPlayingSong?.let {
                    mainViewModel.playOrToggleSong(it, true)
                }
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(android.graphics.Color.parseColor("#F5B501")),
                contentColor = Color.White
            )
        ) {
            if (playbackState?.isPlaying == true) {
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
                    mainViewModel.skipToNextSong()
                }
        )
    }
}

