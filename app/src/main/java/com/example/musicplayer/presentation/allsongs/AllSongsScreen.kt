package com.example.musicplayer.presentation.allsongs


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asLiveData
import com.example.musicplayer.MainViewModel
import com.example.musicplayer.R
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.exoplayer.isPlaying
import com.example.musicplayer.exoplayer.toSong
import com.example.musicplayer.other.Status
import com.example.musicplayer.presentation.feed.MusicListItem
import com.google.accompanist.pager.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalPagerApi
@Composable
fun AllSongsScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val state = rememberPagerState()
    val scope = rememberCoroutineScope()
    var curPlayingSong: Song? = null
    val playbackState = viewModel.playbackState.observeAsState().value
    val listOfSongs = viewModel.mediaItem.observeAsState(listOf()).value



//    viewModel.mediaItem.observe(
//        lifecycleOwner
//    ) {
//        it?.let {
//            fun switchToCurrentPlayingSong(song: Song) {
//                val newItemIndex = listOfSongs.indexOf(song)
//                if (newItemIndex != -1) {
//                    scope.launch {
//                        state.scrollToPage(newItemIndex)
//                        curPlayingSong = song
//                    }
//                }
//            }
//            switchToCurrentPlayingSong(curPlayingSong ?: return@observe)
//        }
//    }
    viewModel.curPlayingSong.observe(
        lifecycleOwner
    ) {
        if (it == null) return@observe
        curPlayingSong = it.toSong()
        fun switchToCurrentPlayingSong(song: Song) {
            val newItemIndex = listOfSongs.indexOf(song)
            if (newItemIndex != -1) {
                scope.launch {
                    state.scrollToPage(newItemIndex)
                    curPlayingSong = song
                }
            }
        }
        switchToCurrentPlayingSong(curPlayingSong ?: return@observe)
    }


    viewModel.isConnected.observe(
        lifecycleOwner
    ) {
        it?.getContentIfNotHandled()?.let { result ->
            when (result.status) {
                Status.ERROR -> {
                    Toast.makeText(context, "isConnected", Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
    }
    viewModel.networkError.observe(
        lifecycleOwner
    ) {
        it?.getContentIfNotHandled()?.let { result ->
            when (result.status) {
                Status.ERROR -> {
                    Toast.makeText(context, "networkError", Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {


            Spacer(modifier = Modifier.height(50.dp))
            LazyColumn(contentPadding = PaddingValues(3.dp), modifier = Modifier) {
                items(
                    items = listOfSongs
                ) { song ->
                    MusicListItem(
                        song = song,
                        onClick = {
                            viewModel.playOrToggleSong(it)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(320.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(25.dp)
                    )
            ) {
                HorizontalPager(
                    count = listOfSongs.size,
                    state = state
                ) { page ->
                    ViewPagerList(
                        song = listOfSongs[page],
                        modifier = Modifier,
                        painter = painterResource(
                            id = if (playbackState?.isPlaying == true) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24
                        ),
                        onClick = {
                            curPlayingSong?.let {
                                viewModel.playOrToggleSong(it, true)
                            }
                        }
                    )
                }
            }
        }
    }
}

