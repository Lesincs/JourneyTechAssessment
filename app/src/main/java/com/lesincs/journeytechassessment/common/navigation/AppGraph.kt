package com.lesincs.journeytechassessment.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lesincs.journeytechassessment.comments.CommentsPage
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
            PostsPage(onPostClicked = { post ->
                navController.navigate(AppRoutes.Comments(post.id))
            })
        }
        composable<AppRoutes.Comments> {
            CommentsPage(onBack = { navController.popBackStack() })
        }
    }
}