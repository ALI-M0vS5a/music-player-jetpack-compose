package com.example.musicplayer.presentation.details

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.data.repository.LyricsRepo
import com.example.musicplayer.exoplayer.MusicService
import com.example.musicplayer.exoplayer.currentPlayingPosition
import com.example.musicplayer.exoplayer.isPlaying
import com.example.musicplayer.other.Resources
import com.example.musicplayer.other.getSong
import com.example.musicplayer.presentation.usecases.MusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val musicUseCase: MusicUseCase,
    private val lyricsRepo: LyricsRepo
) : ViewModel() {


    private val _uiState = mutableStateOf(SongScreenState())
    val uiState: State<SongScreenState> = _uiState

    private val curPlayingSong = musicUseCase.curPlayingSong
    private val playbackState = musicUseCase.playbackState


    init {
        collectPlaybackState()
        collectCurrentSong()
        collectTimePassed()
        collectCurPlayingPosition()
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

    private fun collectCurrentSong() = viewModelScope.launch {
        curPlayingSong.asFlow().collectLatest {
            it?.getSong()?.let { song ->
                fetchLyrics(song)
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
        _uiState.value = uiState.value.copy(
            musicSliderState = uiState.value.musicSliderState.copy(
                timePassed = currentTime,
                timeLeft = totalTime - currentTime,
            )
        )
    }


    fun onSliderValueChange(value: Float) = viewModelScope.launch {
        _uiState.value = uiState.value.copy(
            sliderValue = value
        )
        val totalDuration = MusicService.curSongDuration
        musicUseCase.seekTo((value * totalDuration).toLong())
    }

    private fun collectCurPlayingPosition() = viewModelScope.launch {
        val pos = playbackState.value?.currentPlayingPosition
        _uiState.value = pos?.let {
            uiState.value.copy(
                curPlayingPosition = it
            )
        }!!
    }

    fun onPlayPauseButtonPressed(mediaId: Song, toggle: Boolean = true) = viewModelScope.launch {
        musicUseCase.playOrToggleSong(mediaId, toggle)
    }

    fun skipToNextSong() = viewModelScope.launch {
        musicUseCase.skipToNextTrack()
    }

    fun skipToPreviousSong() = viewModelScope.launch {
        musicUseCase.skipToPrevTrack()
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

    private fun fetchLyrics(song: Song) = viewModelScope.launch {
        val lyrics = lyricsRepo.getLyrics(
            q_track = song.title,
            q_artist = song.artists
        )
        when(lyrics){
            is Resources.Success -> {
                _uiState.value = uiState.value.copy(
                    lyrics = lyrics.data!!.message.body.lyrics.lyrics_body
                )
            }
            else -> Unit
        }
    }
}