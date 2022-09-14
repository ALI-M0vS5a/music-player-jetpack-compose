package com.example.musicplayer.data.entities

import androidx.annotation.DrawableRes

data class BottomMenuContent(
    @DrawableRes val iconId : Int,
    val route: String ?= null
)
