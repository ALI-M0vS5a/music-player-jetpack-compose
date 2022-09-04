package com.example.musicplayer.other

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.compose.runtime.remember
import com.example.musicplayer.data.entities.Song

fun MediaBrowserCompat.MediaItem.getSong(): Song = Song(
    mediaId = description.mediaId!!,
    title = description.title.toString(),
    subtitle = description.subtitle.toString(),
    songUrl = description.mediaUri.toString(),
    imageUrl = description.iconUri.toString()

)
fun MediaMetadataCompat.getSong(): Song = Song(
    mediaId = description.mediaId!!,
    title = description.title.toString(),
    subtitle = description.subtitle.toString(),
    songUrl = description.mediaUri.toString(),
    imageUrl = description.iconUri.toString()
)

fun Long.formatDuration(): String {
    var seconds = this/ 1000
    val minutes = seconds / 60
    seconds %= 60
    return String.format("%02d:%02d", minutes, seconds)
}
