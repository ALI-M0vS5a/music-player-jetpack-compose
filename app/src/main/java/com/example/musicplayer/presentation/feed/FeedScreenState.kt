package com.example.musicplayer.presentation.feed

import com.example.musicplayer.data.entities.Song


data class FeedScreenState(
    val songList: List<Song> = emptyList(),
    val currentPlayingSong: Song? = null,
    val searchBarText: String = ""
)
