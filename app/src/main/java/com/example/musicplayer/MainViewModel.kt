package com.example.musicplayer

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.other.Resources
import com.example.musicplayer.other.getSong
import com.example.musicplayer.presentation.usecases.MusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicUseCase: MusicUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            _isLoading.value = false
        }
        subscribeToMusic()
    }

    private fun subscribeToMusic() = viewModelScope.launch {
        val resource = musicUseCase.subscribeToService()
        if (resource is Resources.Success)
            handleMusic(resource.data ?: emptyList())
    }

    private fun handleMusic(metaDataList: List<MediaBrowserCompat.MediaItem>) =
        viewModelScope.launch(Dispatchers.Main) {
            metaDataList.map { it.getSong() }
        }

    override fun onCleared() {
        super.onCleared()
        musicUseCase.unsubscribeToService()
    }
}