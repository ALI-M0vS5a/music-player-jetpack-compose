package com.example.musicplayer.data.remote

import com.example.musicplayer.data.entities.Song
import com.example.musicplayer.other.Constants.SONG_COLLECTION
import com.example.musicplayer.other.Resources
import com.example.musicplayer.other.safeCall
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class MusicDatabase {

    private val fireStore = FirebaseFirestore.getInstance()
    private val songCollection = fireStore.collection(SONG_COLLECTION)

    suspend fun getAllSongs(): Resources<List<Song>> = safeCall {
        songCollection.get().await().toObjects(Song::class.java)
    }
}
