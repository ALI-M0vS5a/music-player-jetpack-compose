package com.example.musicplayer.presentation.lyrics

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.musicplayer.R
import com.example.musicplayer.Screen
import com.example.musicplayer.presentation.components.PlayPause
import com.example.musicplayer.presentation.details.SongScreenState
import com.example.musicplayer.presentation.details.SongViewModel
import com.example.musicplayer.ui.theme.Primary80
import com.example.musicplayer.ui.theme.Secondary
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Composable
fun LyricsScreen(
    modifier: Modifier = Modifier,
    viewModel: SongViewModel = hiltViewModel(),
    dominantColor: Color,
    navController: NavController
) {
    val uiState = viewModel.uiState.value

//    LaunchedEffect(key1 = Screen.LyricsScreen.route) {
//        Handler(Looper.getMainLooper()).postDelayed({
//            navController.navigate(Screen.SongScreen.route)
//        }, uiState.musicSliderState.timeLeft)
//    }
    val finish: () -> Unit = {
        navController.navigate(Screen.SongScreen.route)
    }
    val scope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val showReportSheet: () -> Unit = {
        scope.launch {
            if(modalBottomSheetState.isVisible) {
                modalBottomSheetState.hide()
            } else {
                modalBottomSheetState.show()
            }
        }
    }
    BackHandler {
        finish()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = dominantColor
            )
    ) {
        ModalBottomSheetLayout(
            sheetBackgroundColor = Secondary,
            scrimColor = Primary80,
            sheetState = modalBottomSheetState,
            modifier = modifier
                .fillMaxWidth(),
            sheetContent = {
                SheetContent(
                    showReportSheet = {
                        showReportSheet()
                    }
                )
            },
            sheetShape = CircleShape.copy(
                topStart = CornerSize(10),
                topEnd = CornerSize(10),
                bottomStart = CornerSize(0),
                bottomEnd = CornerSize(0)
            )
        ) {

            Column(
                modifier = modifier
                    .padding(bottom = 80.dp)
            ) {
                Spacer(modifier = modifier.height(48.dp))
                LyricsScreenTopView(
                    navController = navController,
                    dominantColor = dominantColor,
                    showReportSheet = {
                        showReportSheet()
                    }
                )
                Spacer(modifier = modifier.height(10.dp))
                Text(
                    text = uiState.lyrics,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState(), true),
                    fontSize = 22.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight(400),
                    color = dominantColor.copy(
                        alpha = 1f,
                        red = 0.63f,
                        green = 0.55f,
                        blue = 0.55f
                    )
                )

            }
            BottomLyricsSection(
                modifier = modifier
                    .align(Alignment.BottomCenter),
                uiState = uiState,
                viewModel = viewModel,
                dominantColor = dominantColor
            )
        }

    }
}

@Composable
private fun BottomLyricsSection(
    modifier: Modifier,
    uiState: SongScreenState,
    viewModel: SongViewModel,
    dominantColor: Color
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(dominantColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                ),
            progress = uiState.sliderValue,
            color = Color.White.copy(
                alpha = 1f,
                red = 0.63f,
                green = 0.55f,
                blue = 0.55f
            )
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = uiState.musicSliderState.timePassedFormatted,
                color = Color.Black.copy(
                    alpha = 1f,
                    red = 0.63f,
                    green = 0.55f,
                    blue = 0.55f
                )
            )
            Text(
                text = uiState.musicSliderState.timeLeftFormatted,
                color = Color.Black.copy(
                    alpha = 1f,
                    red = 0.63f,
                    green = 0.55f,
                    blue = 0.55f
                )
            )
        }
        PlayPause(
            modifier = modifier,
            onPlayPauseButtonPressed = viewModel::onPlayPauseButtonPressed,
            song = uiState.currentPlayingSong!!,
            isPlaying = uiState.isPlaying,
            backgroundColor = "#ffffff",
            contentColor = Color.Black
        )
    }
}

@Composable
fun LyricsScreenTopView(
    modifier: Modifier = Modifier,
    viewModel: SongViewModel = hiltViewModel(),
    navController: NavController,
    dominantColor: Color,
    showReportSheet: () -> Unit
) {
    val uiState = viewModel.uiState.value
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = modifier
                .background(
                    color = Color.DarkGray,
                    shape = CircleShape
                )
                .clickable {
                    navController.navigate(Screen.SongScreen.route)
                },
            painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24),
            contentDescription = null,
            tint = Color.White
        )

        Column(
            modifier = modifier,
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = uiState.currentPlayingSong!!.title,
                color = dominantColor.copy(
                    alpha = 1f,
                    red = 0.63f,
                    green = 0.55f,
                    blue = 0.55f
                ),
                fontSize = 18.sp
            )
            Text(
                text = uiState.currentPlayingSong.subtitle,
                color = dominantColor.copy(
                    alpha = 1f,
                    red = 0.63f,
                    green = 0.55f,
                    blue = 0.55f
                ),
                fontSize = 12.sp
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_outlined_flag_24),
            contentDescription = null,
            tint = dominantColor.copy(
                alpha = 1f,
                red = 0.63f,
                green = 0.55f,
                blue = 0.55f
            ),
            modifier = modifier
                .clickable {
                    showReportSheet()
                }
        )
    }
}

@Composable
fun SheetContent(
    showReportSheet: () -> Unit
) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(7.dp))
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(30.dp)
                    .align(CenterHorizontally)
                    .background(
                        color = Color.Gray,
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Report a problem",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(
                        color = Color.Gray
                    )
                    .align(CenterHorizontally)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(45.dp))
                    Text(
                        text = "Some lyrics are wrong",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(45.dp))
                    Text(
                        text = "All lyrics are wrong",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(45.dp))
                    Text(
                        text = "lyrics not synced properly",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(55.dp))
                }
            }
            Text(
                text = "Cancel",
                color = Color.White,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(bottom = 20.dp)
                    .clickable {
                        showReportSheet()
                    },
                fontWeight = FontWeight.Bold
            )
        }
}