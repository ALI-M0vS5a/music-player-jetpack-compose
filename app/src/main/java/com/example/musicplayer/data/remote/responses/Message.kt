package com.example.musicplayer.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("body")
    val body: Body,
    @SerializedName("header")
    val header: Header
)