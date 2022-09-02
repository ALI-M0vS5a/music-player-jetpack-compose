package com.example.musicplayer.data

import androidx.annotation.DrawableRes

data class BottomMenuContent(
    @DrawableRes val iconId : Int,
    val route: String ?= null
)
