package com.example.musicplayer.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.musicplayer.R

@ExperimentalMaterialApi
@Composable
fun LyricsTopView(
    modifier: Modifier,
    navController: NavController,
    dominantColor: Color,
    booleanIsFrom: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Lyrics",
            color = dominantColor.copy(
                alpha = 1f,
                red = 0.63f,
                green = 0.55f,
                blue = 0.55f
            ),
            fontSize = 18.sp
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_open_in_full_24),
            contentDescription = "viewInFull",
            modifier = modifier
                .size(25.dp)
                .clickable {
                    if(booleanIsFrom) {
                        navController.navigateUp()
                    } else {
                        navController.navigate("lyrics_screen/${dominantColor.toArgb()}")
                    }
                },
            tint = dominantColor.copy(
                alpha = 1f,
                red = 0.63f,
                green = 0.55f,
                blue = 0.55f
            )
        )
    }
}