package com.example.musicplayer.data.remote.responses

import com.google.gson.annotations.SerializedName

data class LyricsResponse(
    @SerializedName("message")
    val message: Message
)