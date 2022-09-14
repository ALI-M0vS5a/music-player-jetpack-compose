package com.example.musicplayer.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("execute_time")
    val execute_time: Double,
    @SerializedName("status_code")
    val status_code: Int
)