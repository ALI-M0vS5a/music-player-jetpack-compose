package com.example.musicplayer.data.repository


import com.example.musicplayer.data.remote.responses.LyricsResponse
import com.example.musicplayer.other.Resources

interface LyricsRepo {
    suspend fun getLyrics(q_track: String, q_artist: String): Resources<LyricsResponse>
}