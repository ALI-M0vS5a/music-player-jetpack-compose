package com.example.musicplayer.presentation.details

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicplayer.data.entities.Song

@Composable
fun SongViewPagerList(
    modifier: Modifier = Modifier,
    song: Song
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SongViewPagerImage(
            song = song,
            modifier = modifier
        )
        Spacer(modifier = modifier.height(7.dp))
        SongTextColumn(
            song = song,
            modifier = modifier)

    }
}

@Composable
fun SongViewPagerImage(
    modifier: Modifier = Modifier,
    song: Song
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(song.imageUrl)
            .build(),
        contentDescription = null,
        modifier = modifier
            .size(231.dp)
            .border(
                width = 1.dp,
                shape = CircleShape,
                color = Color.LightGray
            )
            .clip(shape = CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun SongTextColumn(
    modifier: Modifier = Modifier,
    song: Song
) {
    Column(modifier = modifier) {
        Text(
            text = song.title,
            fontSize = 24.sp,
            fontWeight = FontWeight(700),
            color = Color(android.graphics.Color.parseColor("#000000")),
            lineHeight = 36.sp,
            modifier = modifier

        )
        Spacer(modifier = modifier.height(1.dp))
        Text(
            text = song.subtitle,
            fontSize = 14.sp,
            fontWeight = FontWeight(500),
            color = Color(android.graphics.Color.parseColor("#676565")),
            lineHeight = 21.sp,
            modifier = modifier

        )
    }
}