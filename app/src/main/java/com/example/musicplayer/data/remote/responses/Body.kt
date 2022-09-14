package com.example.musicplayer.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("lyrics")
    val lyrics: Lyrics
)