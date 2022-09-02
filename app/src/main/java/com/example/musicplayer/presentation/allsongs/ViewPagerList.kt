package com.example.musicplayer.presentation.allsongs

import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicplayer.R
import com.example.musicplayer.data.entities.Song

@Composable
fun ViewPagerList(
    modifier: Modifier = Modifier,
    song: Song,
    painter: Painter,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = modifier.width(10.dp))
        MusicRowImage(
            song = song,
            modifier = modifier
        )
        Spacer(modifier = modifier.width(25.dp))
        TextOfSong(song = song)
        Spacer(modifier = modifier.width(150.dp))
        PlayPauseIcon(
            painter = painter,
            onClick = {
                onClick.invoke()
            }
        )
    }
}


@Composable
fun MusicRowImage(
    song: Song,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(song.imageUrl)
            .build(),
        contentDescription = null,
        modifier = modifier
            .size(44.dp)
            .border(
                width = 1.dp,
                shape = CircleShape.copy(
                    topStart = CornerSize(30.dp),
                    topEnd = CornerSize(30.dp),
                    bottomEnd = CornerSize(30.dp),
                    bottomStart = CornerSize(30.dp)
                ),
                color = Color.LightGray
            )
            .clip(
                shape = CircleShape.copy(
                    topStart = CornerSize(30.dp),
                    topEnd = CornerSize(30.dp),
                    bottomEnd = CornerSize(30.dp),
                    bottomStart = CornerSize(30.dp)
                ),
            ),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun TextOfSong(song: Song,modifier: Modifier = Modifier) {
    Text(
        text = "${song.title} - ${song.subtitle}",
        fontSize = 16.sp,
        color = Color.Black,
        modifier = modifier
    )
}

@Composable
fun PlayPauseIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    onClick: () -> Unit = {}
) {

    Icon(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .size(44.dp)
            .clickable {
                onClick.invoke()
            }
    )
}

