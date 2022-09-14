package com.example.musicplayer.presentation.feed


import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.exoplayer.MusicServiceConnection
import com.example.musicplayer.other.Constants
import com.example.musicplayer.other.getSong
import com.example.musicplayer.presentation.usecases.MusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeedViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val musicUseCase: MusicUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf(FeedScreenState())
    val uiState: State<FeedScreenState> = _uiState

    private val _navigateToSongScreen = MutableSharedFlow<Boolean>()
    val navigateToSongScreen = _navigateToSongScreen.asSharedFlow()

    private val searchQuery = MutableStateFlow("")

    private val curPlayingSong = musicUseCase.curPlayingSong
    private val playBackState = musicUseCase.playbackState

    init {
        collectSongs()
        collectCurrentSong()
    }

    private fun collectSongs(){
        musicServiceConnection.subscribe(Constants.MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback(){
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                val items = children.map {
                    Song(
                        it.mediaId!!,
                        it.description.title.toString(),
                        it.description.subtitle.toString(),
                        it.description.subtitle.toString(),
                        it.description.mediaUri.toString(),
                        it.description.iconUri.toString()
                    )
                }
                _uiState.value = uiState.value.copy(songList = items)
            }
        })
    }

    private fun collectCurrentSong() = viewModelScope.launch(Dispatchers.Main) {
        curPlayingSong.asFlow().collectLatest {
            val song = it?.getSong()
            _uiState.value = uiState.value.copy(currentPlayingSong = song)
        }
    }

     fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) = viewModelScope.launch(Dispatchers.Main) {
        musicUseCase.playOrToggleSong(mediaItem, toggle)
    }

    fun onSearchQueryChanged(query: String) = viewModelScope.launch(Dispatchers.Main){
        _uiState.value = uiState.value.copy(searchBarText = query)
        searchQuery.emit(query)
    }
}