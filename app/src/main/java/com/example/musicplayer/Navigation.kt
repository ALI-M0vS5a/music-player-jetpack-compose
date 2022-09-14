package com.example.musicplayer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.musicplayer.presentation.allsongs.AllSongsScreen
import com.example.musicplayer.presentation.details.SongScreen
import com.example.musicplayer.presentation.feed.MusicFeedScreen
import com.example.musicplayer.presentation.greetings.GreetingsScreen
import com.example.musicplayer.presentation.lyrics.LyricsScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun Navigation(
    finish: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.GreetingsScreen.route){
        composable(route = Screen.GreetingsScreen.route) {
            GreetingsScreen(
                navController = navController,
                finish = {
                    finish()
                }
            )
        }
        composable(route = Screen.MusicFeedScreen.route){
            MusicFeedScreen(
                navController = navController,
               )
        }
        composable(route = Screen.AllSongsScreen.route){
            AllSongsScreen(
                navController = navController
            )
        }
        composable(route = Screen.SongScreen.route){
            SongScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.LyricsScreen.route,
            arguments = listOf(
                navArgument("dominantColor"){
                    type = NavType.IntType
                }
            )
        ){
            val dominantColor = remember {
                val color = it.arguments?.getInt("dominantColor")
                color?.let { Color(it) } ?: Color.White
            }
            LyricsScreen(
                dominantColor = dominantColor,
                navController = navController
            )
        }
    }
}