package com.example.musicplayer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.presentation.allsongs.AllSongsScreen
import com.example.musicplayer.presentation.details.SongScreen
import com.example.musicplayer.presentation.feed.MusicFeedScreen
import com.example.musicplayer.presentation.greetings.GreetingsScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.GreetingsScreen.route){
        composable(route = Screen.GreetingsScreen.route) {
            GreetingsScreen(navController = navController)
        }
        composable(route = Screen.MusicFeedScreen.route){
            MusicFeedScreen(navController = navController)
        }
        composable(route = Screen.AllSongsScreen.route){
            AllSongsScreen()
        }
        composable(route = Screen.SongScreen.route){
            SongScreen()
        }
    }
}