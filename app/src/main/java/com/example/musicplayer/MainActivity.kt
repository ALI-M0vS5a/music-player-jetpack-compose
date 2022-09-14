package com.example.musicplayer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bumptech.glide.RequestManager
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    var backPressed = 0L
    @Inject
    lateinit var glide: RequestManager

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        setContent {
            viewModel.apply {}
            Navigation(finish = finish)
        }
    }

    val finish: () -> Unit = {
        if(backPressed + 3000 > System.currentTimeMillis()){
            finish()
        }else {
            Toast.makeText(this, "Again to exit", Toast.LENGTH_SHORT).show()
        }
        backPressed = System.currentTimeMillis()
    }
}