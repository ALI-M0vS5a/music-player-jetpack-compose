package com.example.musicplayer.presentation.greetings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.musicplayer.R
import com.example.musicplayer.Screen


@Composable
fun GreetingsScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            TopSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 25.dp, top = 43.dp),
            )
            Spacer(modifier = Modifier.height(99.dp))
            TitleSection()
            Spacer(modifier = Modifier.height(38.dp))
            GirlEnjoyingMusic()
            Spacer(modifier = Modifier.height(61.dp))
            Button(navController)
        }
    }
}

@Composable
fun TopSection(modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_img),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(57.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun TitleSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .padding(start = 64.dp)
        ) {
            Text(
                text = "Add Your",
                color = Color(android.graphics.Color.parseColor("#BCBCBC")),
                lineHeight = 36.sp,
                fontSize = 24.sp,
                fontWeight = FontWeight(500),

                )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Music",
                color = Color(android.graphics.Color.parseColor("#000000")),
                lineHeight = 36.sp,
                fontSize = 24.sp,
                fontWeight = FontWeight(700),

                )
        }
    }
}

@Composable
fun GirlEnjoyingMusic(

) {
    Image(
        painter = painterResource(id = R.drawable.girl_enjoying_music),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .size(432.dp)
    )
}

@Composable
fun Button(
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 55.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = {
                      navController.navigate(Screen.MusicFeedScreen.route)
            },
            shape = CircleShape,
            modifier = Modifier
                .size(width = 193.dp, height = 62.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                backgroundColor = Color(android.graphics.Color.parseColor("#F5B501"))
            )
        ) {
            Text(
                text = "Let's go",
                fontWeight = FontWeight(700),
                fontSize = 24.sp,
                lineHeight = 36.sp,
                color = Color.White
            )
        }
    }
}
