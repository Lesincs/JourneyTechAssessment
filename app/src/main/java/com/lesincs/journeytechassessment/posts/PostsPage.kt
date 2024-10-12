package com.lesincs.journeytechassessment.posts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.lesincs.journeytechassessment.R
import com.lesincs.journeytechassessment.common.ui.theme.JourneyTechAssessmentTheme

@Composable
fun PostsPage(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    PostsScaffold(
        isRefreshing = false,
        posts = listOf(),
        onRefresh = {},
        modifier = modifier,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PostsScaffold(
    isRefreshing: Boolean,
    posts: List<Post>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.posts_page_title)) }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            modifier = Modifier.padding(paddingValues),
            onRefresh = onRefresh,
        ) {
            if (posts.isEmpty()) {
                EmptyPostsHint()
                return@PullToRefreshBox
            }
            LazyColumn {
                items(posts) { post ->
                    HorizontalDivider()
                    PostItem(post)
                }
            }
        }
    }
}

@Composable
private fun EmptyPostsHint() {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(),
        text = stringResource(R.string.no_posts_available_hint),
    )
}

@Composable
private fun PostItem(post: Post) {
    ListItem(
        headlineContent = {
            Text(post.title)
        },
        supportingContent = {
            Text(post.body)
        }
    )
}

@Preview
@Composable
private fun PostsScaffoldPreview_With_Posts() {
    JourneyTechAssessmentTheme {
        PostsScaffold(
            isRefreshing = false,
            posts = listOf(
                Post(
                    id = "1",
                    title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                    body = "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
                ),
                Post(
                    id = "2",
                    title = "dolorem eum magni eos aperiam quia",
                    body = "ut aspernatur corporis harum nihil quis provident sequi\\nmollitia nobis aliquid molestiae\\nperspiciatis et ea nemo ab reprehenderit accusantium quas\\nvoluptate dolores velit et doloremque molestiae"
                ),
            ),
            onRefresh = {},
        )
    }
}

@Preview
@Composable
private fun PostsScaffoldPreview_Empty_Posts() {
    JourneyTechAssessmentTheme {
        PostsScaffold(
            isRefreshing = false,
            posts = listOf(),
            onRefresh = {},
        )
    }
}