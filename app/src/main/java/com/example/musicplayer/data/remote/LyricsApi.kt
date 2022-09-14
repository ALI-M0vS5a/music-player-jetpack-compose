package com.example.musicplayer.data.remote

import com.example.musicplayer.data.remote.responses.LyricsResponse
import com.example.musicplayer.other.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface LyricsApi {

    @GET("matcher.lyrics.get")
    suspend fun getLyrics(
        @Query("apikey") apikey: String = API_KEY,
        @Query("q_track") q_track: String,
        @Query("q_artist") q_artist: String
    ): LyricsResponse

}