package com.example.musicplayer.presentation.details

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.other.formatDuration


data class SongScreenState(
    val image: String = "",
    val currentPlayingSong: Song? = null,
    val isPlaying: Boolean = false,
    val totalDuration: Long = 0L,
    val sliderValue: Float = 0f,
    val curPlayingPosition: Long? = null,
    val musicSliderState: MusicSliderState = MusicSliderState(),
    val dominantColor: Color = Color.White
)

data class MusicSliderState(
    val timePassed: Long = 0L,
    val timeLeft: Long = 0L
){
    val timePassedFormatted
        get() = timePassed.formatDuration()

    val timeLeftFormatted
        get() = timeLeft.formatDuration()
}
