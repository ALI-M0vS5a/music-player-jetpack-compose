package com.example.musicplayer.presentation.feed


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.musicplayer.R
import com.example.musicplayer.data.entities.Song


@Composable
fun MusicListItem(
    modifier: Modifier = Modifier,
    song: Song,
    onClick: (music_id: Song) -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onClick.invoke(song)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            val asyncImage = rememberAsyncImagePainter(song.imageUrl)
            if (asyncImage.state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator(
                    modifier = modifier
                        .padding(22.5.dp)
                        .scale(0.5f)
                )
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.imageUrl)
                    .build(),
                contentDescription = null,
                modifier = modifier
                    .size(88.dp)
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
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = modifier.width(2.dp))
            ColumnText(
                song = song,
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            )
            MoreDots()
        }
    }

@Composable
fun ColumnText(
    song: Song,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = song.title,
            fontWeight = FontWeight(600),
            fontSize = 18.sp,
            lineHeight = 27.sp,
            color = Color(android.graphics.Color.parseColor("#000000")),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = song.subtitle,
            fontWeight = FontWeight(600),
            fontSize = 16.sp,
            lineHeight = 27.sp,
            color = Color(android.graphics.Color.parseColor("#AAAAAA")),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun MoreDots(
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = {},
        modifier = modifier
            .background(Color.Transparent)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
            contentDescription = "more",
            modifier = modifier
        )
    }
}


