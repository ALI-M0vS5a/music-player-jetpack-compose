package com.example.musicplayer.presentation.allsongs

import com.example.musicplayer.data.entities.Song


data class AllSongsScreenState(
    val image: String = "",
    val songList: List<Song> = emptyList(),
    val currentPlayingSong: Song? = null,
    val sliderValue: Float = 0f,
    val isPlaying: Boolean = false,
)
