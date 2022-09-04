package com.example.musicplayer.presentation.usecases

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.exoplayer.*
import com.example.musicplayer.other.Constants.MEDIA_ROOT_ID
import com.example.musicplayer.other.Resources
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MusicUseCase @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) {
    val curPlayingSong = musicServiceConnection.curPlayingSong
    val playbackState = musicServiceConnection.playbackState

    val timePassed = flow {
        while (true) {
            val duration = playbackState.value?.currentPlayingPosition
                ?: 0
            emit(duration)
            delay(1000L)
        }
    }

    suspend fun subscribeToService(): Resources<List<MediaBrowserCompat.MediaItem>> =
        suspendCoroutine {
            musicServiceConnection.subscribe(
                MEDIA_ROOT_ID,
                object : MediaBrowserCompat.SubscriptionCallback() {
                    override fun onChildrenLoaded(
                        parentId: String,
                        children: MutableList<MediaBrowserCompat.MediaItem>
                    ) {
                        super.onChildrenLoaded(parentId, children)
                        it.resume(Resources.Success(children))
                    }

                    override fun onError(parentId: String) {
                        super.onError(parentId)
                        it.resume(Resources.Error(message = "Failed to subscribe"))
                    }
                }
            )
        }

    fun unsubscribeToService() {
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID)
    }

    fun skipToNextTrack() = musicServiceConnection.skipToNextTrack()

    fun skipToPrevTrack() = musicServiceConnection.skipToPrev()

    fun seekTo(pos: Long) = musicServiceConnection.seekTo(pos)

    fun fastForward() = musicServiceConnection.fastForward()

    fun rewind() = musicServiceConnection.rewind()

    fun stopPlaying() = musicServiceConnection.stopPlaying()

    fun playFromMediaId(
        mediaId: String
    ) = musicServiceConnection.playFromMediaId(mediaId)

    fun isMusicPlayingOrPaused() = musicServiceConnection.playbackState.value?.let {
        return@let it.isPlaying || it.isPlayEnabled
    } ?: false

    fun playPause(musicId: String, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if(isPrepared && musicId == curPlayingSong.value?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID))
            playPauseCurrentSong(toggle)
        else playFromMediaId(musicId)
    }

    private fun playPauseCurrentSong(toggle: Boolean) {
        playbackState.value?.let {
            when {
                it.isPlaying -> if (toggle) musicServiceConnection.pause()
                it.isPlayEnabled -> musicServiceConnection.play()
                else -> Unit
            }
        }
    }

    fun playOrToggleSong(
        mediaItem: Song,
        toggle: Boolean = false
    ){
        val isPrepared = playbackState.value?.isPrepared ?: false
        if(isPrepared && mediaItem.mediaId == curPlayingSong.value?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)){
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> if (toggle) musicServiceConnection.transportControls.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId,null)
        }
    }
}