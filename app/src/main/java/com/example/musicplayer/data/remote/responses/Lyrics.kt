package com.example.musicplayer.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Lyrics(
    @SerializedName("explicit")
    val explicit: Int = 0,
    @SerializedName("lyrics_body")
    val lyrics_body: String = "",
    @SerializedName("lyrics_copyright")
    val lyrics_copyright: String = "",
    @SerializedName("lyrics_id")
    val lyrics_id: Int = 0,
    @SerializedName("pixel_tracking_url")
    val pixel_tracking_url: String = "",
    @SerializedName("script_tracking_url")
    val script_tracking_url: String = "",
    @SerializedName("updated_time")
    val updated_time: String = ""
)