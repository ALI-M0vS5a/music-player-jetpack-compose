package com.example.musicplayer.presentation.feed


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.musicplayer.MainViewModel
import com.example.musicplayer.R
import com.example.musicplayer.Screen
import com.example.musicplayer.data.BottomMenuContent
import com.example.musicplayer.data.ImageWithText
import com.example.musicplayer.data.WithText
import com.example.musicplayer.data.entities.Song


@Composable
fun MusicFeedScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val songs = viewModel.mediaItem.observeAsState(listOf()).value

    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
    ) {
        Column(modifier = Modifier) {
            Spacer(modifier = Modifier.height(20.dp))
            TopSection(navController)
            Spacer(modifier = Modifier.height(41.dp))
            HelloSection()
            Spacer(modifier = Modifier.height(41.dp))
            TabView(
                withText = listOf(
                    WithText(
                        text = "Recently"
                    ),
                    WithText(
                        text = "Popular"
                    ),
                    WithText(
                        text = "Favourite"
                    ),
                    WithText(
                        text = "Top 10"
                    )
                ),
            ) {
                selectedTabIndex = it
            }
            when (selectedTabIndex) {
                0 -> Recently(
                    listOfAllSong = songs,
                )
                1 -> PopularSection(
                    imageWithText = listOf(
                        ImageWithText(
                            image = painterResource(id = R.drawable.taylor),
                            text = "Taylor"
                        ),
                        ImageWithText(
                            image = painterResource(id = R.drawable.cardi_b),
                            text = "Cardi B"
                        ),
                        ImageWithText(
                            image = painterResource(id = R.drawable.lopez),
                            text = "Lopez"
                        ),
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    listOfAllSong = songs,
                    navController = navController
                )
            }
        }
        BottomMenu(
            items = listOf(
                BottomMenuContent(
                    R.drawable.search
                ),
                BottomMenuContent(
                    R.drawable.circle
                ),
                BottomMenuContent(
                    R.drawable.speaker
                ),
                BottomMenuContent(
                    R.drawable.music,
                    Screen.AllSongsScreen.route
                ),
                BottomMenuContent(
                    R.drawable.heart
                )
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(13.dp),
            navController = navController
        )
    }
}

@Composable
fun TopSection(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ArrowBack(
            Modifier
                .padding(top = 47.64.dp),
            navController
        )
        SearchBar(
            Modifier
                .padding(top = 35.dp)
        )
        com.example.musicplayer.presentation.greetings.TopSection(
            modifier = Modifier
                .padding(top = 27.dp)
        )
    }
}

@Composable
fun ArrowBack(
    modifier: Modifier,
    navController: NavController
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_left_24),
        contentDescription = null,
        modifier = modifier
            .clickable {
                navController.navigateUp()
            }
    )
}

@Composable
fun SearchBar(modifier: Modifier) {

    var text by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = text, onValueChange = {
            text = it
        },
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(color = Color.Black),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_search_24),
                contentDescription = null
            )
        },
        shape = CircleShape,
        modifier = modifier
            .size(width = 258.dp, height = 49.dp),
        placeholder = {
            Text(
                text = "Search",
                fontWeight = FontWeight(400),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = Color(android.graphics.Color.parseColor("#464646"))
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(android.graphics.Color.parseColor("#EFEFEF")),
            cursorColor = Color.Black,
            textColor = Color(android.graphics.Color.parseColor("#464646")),
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color(android.graphics.Color.parseColor("#EFEFEF"))
        )
    )
}

@Composable
fun HelloSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Hello(modifier = Modifier.padding(start = 44.dp))
        FilterButton(modifier = Modifier.padding(end = 32.dp))

    }
}

@Composable
fun Hello(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Hello,",
            fontWeight = FontWeight(400),
            fontSize = 24.sp,
            lineHeight = 36.sp,
            color = Color(android.graphics.Color.parseColor("#AAAAAA"))
        )
        Text(
            text = "Sophia!",
            fontWeight = FontWeight(600),
            fontSize = 26.sp,
            lineHeight = 39.sp,
            color = Color(android.graphics.Color.parseColor("#000000"))
        )
    }
}

@Composable
fun FilterButton(modifier: Modifier) {
    IconButton(
        modifier = modifier
            .size(width = 48.dp, height = 47.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = CircleShape.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp)
                )
            ),
        onClick = {

        }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
            contentDescription = null
        )
    }
}


@Composable
fun TabView(
    modifier: Modifier = Modifier,
    withText: List<WithText>,
    onTabSelected: (selectedIndex: Int) -> Unit
) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    val inactiveColor = Color(android.graphics.Color.parseColor("#202020"))
    val activeColor = Color(android.graphics.Color.parseColor("#F5B501"))
    TabRow(

        selectedTabIndex = selectedTabIndex,
        backgroundColor = Color.Transparent,
        contentColor = Color.LightGray,
        modifier = modifier,
        divider = {

        },
        indicator = {
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(it[selectedTabIndex]),
                color = activeColor,
                height = TabRowDefaults.IndicatorHeight * 0.5F
            )
        }
    ) {
        withText.forEachIndexed { index, item ->

            Tab(
                selected = selectedTabIndex == index,
                selectedContentColor = activeColor,
                unselectedContentColor = inactiveColor,
                onClick = {
                    selectedTabIndex = index
                    onTabSelected(index)

                }) {
                Text(
                    text = item.text,
                    fontWeight = FontWeight(500),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = if (selectedTabIndex == index) activeColor else inactiveColor,
                )
            }
        }
    }
}


@Composable
fun PopularSection(
    modifier: Modifier = Modifier,
    imageWithText: List<ImageWithText>,
    viewModel: MainViewModel = hiltViewModel(),
    listOfAllSong: List<Song>,
    navController: NavController
) {
    Column {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(imageWithText.size) {
                Column(
                    modifier = modifier.padding(top = 41.dp)
                ) {
                    Image(
                        painter = imageWithText[it].image,
                        contentDescription = null,
                        modifier = modifier
                            .border(
                                width = 1.dp,
                                shape = CircleShape.copy(
                                    topStart = CornerSize(30.dp),
                                    topEnd = CornerSize(30.dp),
                                    bottomEnd = CornerSize(30.dp),
                                    bottomStart = CornerSize(30.dp)
                                ),
                                color = Color.LightGray
                            )
                            .size(114.dp)
                            .clip(
                                shape = CircleShape.copy(
                                    topStart = CornerSize(30.dp),
                                    topEnd = CornerSize(30.dp),
                                    bottomEnd = CornerSize(30.dp),
                                    bottomStart = CornerSize(30.dp)
                                ),
                            )
                    )
                    Text(
                        text = imageWithText[it].text,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight(500),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color(android.graphics.Color.parseColor("#6F6F6F")),
                        modifier = modifier
                            .padding(start = 30.dp)
                    )
                }
            }
        }
        Text(
            text = "New",
            fontWeight = FontWeight(600),
            fontSize = 20.sp,
            lineHeight = 30.sp,
            color = Color.Black,
            modifier = modifier.padding(
                start = 32.dp, top = 21.dp
            )
        )
//        var songList: List<Song> = arrayListOf()
//
//         viewModel.mediaItem.observe(
//            LocalLifecycleOwner.current
//         ){
//             when(it.status){
//                 Status.SUCCESS -> {
//                     it.data?.let { songs ->
//                         songList = songs
//                     }
//                 }
//                 else -> Unit
//             }
//         }
        LazyColumn(
            contentPadding = PaddingValues(bottom = 10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .padding(top = 25.dp)
        ) {
            items(
                items = listOfAllSong
            ) { song ->
                MusicListItem(
                    song = song,
                    onClick = {
                        viewModel.playOrToggleSong(it)
                        navController.navigate(Screen.SongScreen.route)
                    }
                )
            }
        }
    }
}


@Composable
fun BottomMenu(
    items: List<BottomMenuContent>,
    modifier: Modifier = Modifier,
    activeHighLightColor: Color = Color.Transparent,
    initialSelectedItemIndex: Int = 0,
    navController: NavController
) {
    var selectedItemIndex by remember {
        mutableStateOf(initialSelectedItemIndex)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                color = Color(android.graphics.Color.parseColor("#F5B501")),
                shape = CircleShape.copy(
                    topStart = CornerSize(35.dp),
                    topEnd = CornerSize(35.dp),
                    bottomEnd = CornerSize(35.dp),
                    bottomStart = CornerSize(35.dp)
                )
            )

    ) {
        items.forEachIndexed { index, item ->
            BottomMenuItem(
                item = item,
                isSelected = index == selectedItemIndex,
                activeHighLightColor = activeHighLightColor
            ) {
                selectedItemIndex = index
                when (selectedItemIndex) {
                    3 -> navController.navigate(Screen.AllSongsScreen.route)
                }
            }
        }
    }
}

@Composable
fun BottomMenuItem(
    item: BottomMenuContent,
    isSelected: Boolean = false,
    activeHighLightColor: Color = Color.Transparent,
    onItemClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) activeHighLightColor else Color.Transparent)
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(id = item.iconId),
            contentDescription = null,
            modifier = Modifier
                .size(57.dp)
                .clickable {
                    onItemClick()
                }
        )
    }
}

@Composable
fun Recently(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    listOfAllSong: List<Song>,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = modifier.height(66.dp))
        ImageInRecently(
            modifier = modifier
                .padding(
                    start = 57.dp,
                    end = 63.dp
                )
        )
        Spacer(modifier = modifier.height(7.dp))
        TextColumnInRecently(
            modifier = modifier
                .padding(
                    start = 153.dp,
                    end = 153.dp
                )
        )


    }
}

@Composable
fun TextColumnInRecently(
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = "Bad guy",
            fontWeight = FontWeight(600),
            fontSize = 24.sp,
            lineHeight = 36.sp,
            color = Color(android.graphics.Color.parseColor("#484646")),
            modifier = modifier
                .size(
                    width = 100.dp,
                    height = 36.dp
                )

        )
        Spacer(modifier = modifier.height(7.dp))
        Text(
            text = "Billie eilish",
            fontWeight = FontWeight(500),
            fontSize = 24.sp,
            lineHeight = 36.sp,
            color = Color(android.graphics.Color.parseColor("#AAAAAA")),
            modifier = modifier
                .size(width = 122.dp, height = 36.dp)

        )
    }

}

@Composable
fun ImageInRecently(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.billie_eilish_bad_guy),
        contentDescription = null,
        modifier = modifier
            .size(
                width = 308.dp, height = 348.dp
            )
            .border(
                width = 1.dp,
                shape = CircleShape.copy(
                    topStart = CornerSize(30.dp),
                    topEnd = CornerSize(30.dp),
                    bottomEnd = CornerSize(30.dp),
                    bottomStart = CornerSize(30.dp)
                ),
                color = Color.LightGray
            )
            .clip(
                shape = CircleShape.copy(
                    topStart = CornerSize(30.dp),
                    topEnd = CornerSize(30.dp),
                    bottomEnd = CornerSize(30.dp),
                    bottomStart = CornerSize(30.dp)
                ),
            ),
        contentScale = ContentScale.Crop
    )

}