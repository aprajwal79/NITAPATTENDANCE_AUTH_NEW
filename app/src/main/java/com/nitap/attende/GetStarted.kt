package com.nitap.attende

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*



import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
//import androidx.viewpager2.compose.HorizontalPager
//import androidx.viewpager2.compose.HorizontalPagerIndicator
//import androidx.viewpager2.compose.rememberPagerState
import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import com.google.accompanist.pager.HorizontalPagerIndicator
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

public class GetStarted : AppCompatActivity() {


    @OptIn(ExperimentalFoundationApi::class)
    @Composable

    fun GetStartedScreen() {
        val context = LocalContext.current
        val pagerState = rememberPagerState(0, 0f) { 3 }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF9F9F9), // Specify your desired color as an Int or Color object
                    shape = RoundedCornerShape(8.dp) // Specify the desired shape as a Shape object
                )
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, bottom = 20.dp)
            ) {

                // TextBelowImage(text = "Welcome to \n nit ap", imageResource = R.drawable.attendance)
                // PagerContent(text = "Welcome to \n nit ap", image = R.drawable.attendance)
                /*TextBelowImage(
                    text = "Hand-pickle high\nquality snacks.",
                    imageResource = R.drawable.checked
                )
                TextBelowImage(
                    text = "Fresh, never fried,\nalways delicious.",
                    imageResource = R.drawable.about_us
                )*/
                // PagerContent(text = "Get ready to\nSnack Smarter.", image = R.drawable.attendance)


            ////

            }



            DotsIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
            )
            Image(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .clickable {
                        context.startActivity(Intent(context, TermsAndConditions::class.java))
                    },
                painter = painterResource(R.drawable.google_signin),
                contentDescription = "Get Started Button",
            )
        }

    }


    /*
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TabIndicator(
        pagerState: PagerState,
        modifier: Modifier = Modifier,
        tabTitles: List<String> = listOf("A", "B", "C")
        activeColor: Color = Color.Blue,
        inactiveColor: Color = Color.Gray,
        selectedTextStyle: TextStyle = TextStyle(fontWeight = FontWeight.Bold),
        unselectedTextStyle: TextStyle = TextStyle(fontWeight = FontWeight.Normal),
    ) {
        val currentPage = pagerState.currentPage
        TabRow(
            modifier = modifier,
            selectedTabIndex = currentPage
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(

                    selected = index == currentPage,
                    onClick = { pagerState.animateScrollToPage(index) },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .background(if (index == currentPage) activeColor else inactiveColor),
                    textStyle = if (index == currentPage) selectedTextStyle else unselectedTextStyle
                )
            }
        }
    }
    */

    /*
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun DynamicTabIndicator(
        pagerState: PagerState,
        modifier: Modifier = Modifier,
        tabContent: @Composable (Int) -> Unit,
        activeColor: Color = Color.Blue,
        inactiveColor: Color = Color.Gray,
        selectedTextStyle: TextStyle = TextStyle(fontWeight = FontWeight.Bold),
        unselectedTextStyle: TextStyle = TextStyle(fontWeight = FontWeight.Normal),
    ) {
        val currentPage = pagerState.currentPage
        val tabCount = {3}// Can be replaced with custom logic for dynamic count

        TabRow(
            modifier = modifier,
            selectedTabIndex = currentPage
        ) {
            repeat(tabCount) { index ->
                Tab(
                    selected = index == currentPage,
                    onClick = { pagerState.animateScrollToPage(index) },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .background(if (index == currentPage) activeColor else inactiveColor),
                    textStyle = if (index == currentPage) selectedTextStyle else unselectedTextStyle
                ) {
                    tabContent(index) // Replace with your custom content creation logic based on the index
                }
            }
        }
    }

    */

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun DotsIndicator(
        pagerState: PagerState,
        modifier: Modifier = Modifier,
        activeDotColor: Color = Color.Blue,
        inactiveDotColor: Color = Color.Gray,
        dotSize: Dp = 8.dp,
        spacing: Dp = 4.dp
    ) {
        val pageCount = pagerState.pageCount
        val currentPage = pagerState.currentPage
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { pageIndex ->
                Spacer(modifier = Modifier.width(spacing))
                MyCircle(
                    modifier = Modifier
                        .size(dotSize)
                        .background(if (pageIndex == currentPage) activeDotColor else inactiveDotColor)
                )
            }
        }
    }

    @Composable
    fun MyCircle(modifier: Modifier) {
        Canvas(modifier = modifier, onDraw = {
            drawCircle(color = Color.Red)
        })
    }
/*
    @Composable
    fun PagerContent(text: String, image: Int) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 28.sp,
                    color = Color(0xFF222222),
                    lineHeight = 36.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                painter = painterResource(image),
                contentDescription = ""
            )
        }
    }
*/
    @Composable
    fun TextBelowImage(text: String, imageResource: Int) {
        var currentPageIndex by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()

        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            Image(
                painter = painterResource(id = imageResource),
                modifier = Modifier
                    .fillMaxWidth(/*0.80f*/)
                    .height(300.dp)
                    .align(Alignment.CenterHorizontally),
                contentDescription = ""
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )

            Text(
                text,
                Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally),
                Color.Blue, 40.sp, FontStyle.Normal, FontWeight.Normal,
                FontFamily.SansSerif, TextUnit.Unspecified, null,
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .weight(1f)
            )
        }
    }

    /*
    @Composable
    fun TextBelowImage(text: String, imageResource: Int) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageResource),
                modifier = Modifier.weight(3f).align(Alignment.BottomCenter),
                contentDescription = ""
            )
            Text(
                text = text,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GetStartedScreen()
        }
    }

    private fun getPageContent(page: Int): String {
        // Implement your logic to get content for specific page
        return when (page) {
            0 -> "Page 1 content"
            1 -> "Page 2 content"
            2 -> "Page 3 content"
            else -> throw IllegalStateException("Invalid page index")
        }
    }

    private fun getPageImageResource(page: Int): Int {
        // Implement your logic to get image resource for specific page
        return when (page) {
            0 -> R.drawable.attendance
            1 -> R.drawable.about_us
            2 -> R.drawable.get_started_button
            else -> throw IllegalStateException("Invalid page index")
        }

    }



}