package com.lesincs.journeytechassessment.common.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lesincs.journeytechassessment.posts.PostsPage

@Composable
fun AppGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppRoutes.Posts,
    ) {
        composable<AppRoutes.Posts> {
            PostsPage(navController = navController)
        }
        composable<AppRoutes.Comments> {
            Text(
                text = "Comments page",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
            )
        }
    }
}