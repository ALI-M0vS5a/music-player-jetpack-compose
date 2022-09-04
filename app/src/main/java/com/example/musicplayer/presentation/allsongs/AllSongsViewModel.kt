package com.example.musicplayer.presentation.allsongs

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.exoplayer.MusicService
import com.example.musicplayer.exoplayer.MusicServiceConnection
import com.example.musicplayer.exoplayer.isPlaying
import com.example.musicplayer.other.Constants
import com.example.musicplayer.other.getSong
import com.example.musicplayer.presentation.usecases.MusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllSongsViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val musicUseCase: MusicUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf(AllSongsScreenState())
    val uiState: State<AllSongsScreenState> = _uiState

    private val curPlayingSong = musicUseCase.curPlayingSong
    private val playbackState = musicUseCase.playbackState

    init {
        collectSongs()
        collectCurrentPlayingSong()
        collectPlaybackState()
        collectTimePassed()
    }


    private fun collectSongs(){
        musicServiceConnection.subscribe(Constants.MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback(){
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                val items = children.map {
                    Song(
                        it.mediaId!!,
                        it.description.title.toString(),
                        it.description.subtitle.toString(),
                        it.description.mediaUri.toString(),
                        it.description.iconUri.toString()
                    )
                }
                _uiState.value = uiState.value.copy(songList = items)
            }
        })
    }
    private fun collectPlaybackState() = viewModelScope.launch {
        playbackState.asFlow().collectLatest { state ->
            state?.let {
                _uiState.value = uiState.value.copy(
                    isPlaying = it.isPlaying
                )
            }
        }
    }
    private fun collectCurrentPlayingSong() = viewModelScope.launch(Dispatchers.Main) {
        curPlayingSong.asFlow().collectLatest {
             it?.getSong()?.let { song ->
                 _uiState.value = uiState.value.copy(
                     currentPlayingSong = song,
                     image = song.imageUrl
                 )
             }
        }
    }
    private fun collectTimePassed() = viewModelScope.launch {
        musicUseCase.timePassed.collectLatest {
            updateDurationInUI(it, MusicService.curSongDuration)
        }
    }

    private fun updateDurationInUI(currentTime: Long, totalTime: Long) {
        if (totalTime == 0L) return
        _uiState.value = uiState.value.copy(
            sliderValue = currentTime / totalTime.toFloat()
        )
    }
    fun onSliderValueChange(value: Float) = viewModelScope.launch {
        _uiState.value = uiState.value.copy(
            sliderValue = value
        )
        val totalDuration = MusicService.curSongDuration
        musicUseCase.seekTo((value * totalDuration).toLong())
    }
    fun onPlayPauseButtonPressed(mediaId: Song, toggle: Boolean = true) = viewModelScope.launch {
        musicUseCase.playOrToggleSong(mediaId, toggle)
    }
    fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) = viewModelScope.launch(Dispatchers.Main) {
        musicUseCase.playOrToggleSong(mediaItem, toggle)
    }

    fun calcDominantColor(
        drawable: Drawable,
        onFinish: (Color) -> Unit
    ) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888,true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}
