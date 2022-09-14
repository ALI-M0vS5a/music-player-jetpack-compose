package com.example.musicplayer.domain

import com.example.musicplayer.data.repository.LyricsRepo
import com.example.musicplayer.data.repository.LyricsRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModule {
    @Singleton
    @Binds
    abstract fun bindsLyricsRepo(
        lyricsRepoImpl: LyricsRepoImpl
    ): LyricsRepo
}