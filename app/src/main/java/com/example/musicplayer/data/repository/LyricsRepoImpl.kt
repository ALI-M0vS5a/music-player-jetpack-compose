package com.example.musicplayer.data.repository

import com.example.musicplayer.data.remote.LyricsApi
import com.example.musicplayer.data.remote.responses.LyricsResponse
import com.example.musicplayer.other.ErrorType
import com.example.musicplayer.other.Resources
import javax.inject.Inject

class LyricsRepoImpl @Inject constructor(
   private val api: LyricsApi
) : LyricsRepo {
    override suspend fun getLyrics(q_track: String, q_artist: String): Resources<LyricsResponse> {
        val response = try {
            api.getLyrics(
                q_track = q_track,
                q_artist = q_artist
            )
        }catch (e: Exception){
            return Resources.Error(ErrorType.UNKNOWN,"An unknown error occurred")
        }
        return Resources.Success(response)
    }

}