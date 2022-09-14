package com.example.musicplayer.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicplayer.R
import com.example.musicplayer.data.entities.Song


@Composable
fun PlayPause(
    modifier: Modifier,
    onPlayPauseButtonPressed: (Song) -> Unit,
    song: Song,
    isPlaying: Boolean,
    backgroundColor: String,
    contentColor: Color
) {
    Button(
        modifier = modifier
            .size(74.dp),
        onClick = {
            onPlayPauseButtonPressed(song)
        },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(android.graphics.Color.parseColor(backgroundColor)),
            contentColor = contentColor
        )
    ) {
        if (isPlaying) {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_baseline_pause_24
                ),
                contentDescription = "play_pause",
                modifier = modifier
                    .size(50.dp)
            )
        } else {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_baseline_play_arrow_24
                ),
                contentDescription = "play_pause",
                modifier = modifier
                    .size(50.dp)
            )
        }

    }
}