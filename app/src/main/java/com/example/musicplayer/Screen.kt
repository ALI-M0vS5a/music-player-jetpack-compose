package com.example.musicplayer


sealed class Screen(val route: String){
    object GreetingsScreen : Screen("greetings_screen")
    object MusicFeedScreen : Screen("music_feed_screen")
    object AllSongsScreen : Screen("all_songs_screen")
    object SongScreen : Screen("song_screen")
    object LyricsScreen : Screen("lyrics_screen/{dominantColor}")
}
