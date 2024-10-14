package com.lesincs.journeytechassessment.comments

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lesincs.journeytechassessment.R
import com.lesincs.journeytechassessment.comments.data.Comment
import com.lesincs.journeytechassessment.common.ui.component.EmptyListHintText
import com.lesincs.journeytechassessment.common.ui.component.SearchTopAppBar
import com.lesincs.journeytechassessment.common.ui.theme.JourneyTechAssessmentTheme

@Composable
fun CommentsPage(onBack: () -> Unit) {
    val commentsViewModel = hiltViewModel<CommentsViewModel>()
    val isRefreshing: Boolean =
        commentsViewModel.isRefreshingCommentsStateFlow.collectAsStateWithLifecycle().value
    val comments: List<Comment> = commentsViewModel.comments.collectAsStateWithLifecycle().value
    val errorMessageResId: Int? =
        commentsViewModel.updateCommentsFailedErrorMessageResIdStateFlow.collectAsStateWithLifecycle().value
    val searchQuery: String =
        commentsViewModel.searchQueryStateFlow.collectAsStateWithLifecycle().value

    CommentsScaffold(
        errorMessageResId = errorMessageResId,
        isRefreshing = isRefreshing,
        comments = comments,
        onRefresh = commentsViewModel::updateComments,
        onErrorMessageShown = commentsViewModel::onUpdateCommentsFailedErrorMessageShown,
        searchQuery = searchQuery,
        onSearchQueryChange = commentsViewModel::updateSearchQuery,
        onBack = onBack,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CommentsScaffold(
    @StringRes errorMessageResId: Int?,
    isRefreshing: Boolean,
    comments: List<Comment>,
    onRefresh: () -> Unit,
    onErrorMessageShown: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(errorMessageResId) {
        if (errorMessageResId != null) {
            snackbarHostState.showSnackbar(context.getString(errorMessageResId))
            onErrorMessageShown()
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CommentsTopAppBar(
                onSearchQueryChange = onSearchQueryChange,
                searchQuery = searchQuery,
                onBack = onBack,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        contentWindowInsets = WindowInsets.safeContent,
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            modifier = Modifier.padding(paddingValues),
            onRefresh = onRefresh,
        ) {
            if (comments.isEmpty()) {
                EmptyListHintText(
                    textResId = R.string.no_comments_available_hint,
                )
                return@PullToRefreshBox
            }
            LazyColumn {
                items(comments) { comment ->
                    HorizontalDivider()
                    CommentItem(comment)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentsTopAppBar(
    onSearchQueryChange: (String) -> Unit,
    searchQuery: String,
    onBack: () -> Unit,
) {
    var showSearchBar by rememberSaveable {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = showSearchBar,
            label = "PostsTopAppBar",
            contentAlignment = Alignment.Center,
        ) { shouldShowSearchBar ->
            if (shouldShowSearchBar) {
                SearchTopAppBar(
                    onSearchQueryChange = onSearchQueryChange,
                    onHideSearchBar = { showSearchBar = false },
                    searchQuery = searchQuery,
                    placeHolderText = stringResource(R.string.search_comments_place_holder),
                )
            } else {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.comments_page_title)) },
                    actions = {
                        IconButton(onClick = { showSearchBar = true }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = stringResource(R.string.search_posts_icon_content_description),
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )

            }
        }
    }
}

@Composable
private fun CommentItem(comment: Comment, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(comment.body)
        },
        supportingContent = {
            Text(comment.email)
        },
        overlineContent = {
            Text(comment.name)
        }
    )
}

@Preview
@Composable
private fun CommentsScaffoldPreview_With_Comments() {
    JourneyTechAssessmentTheme {
        CommentsScaffold(
            errorMessageResId = null,
            isRefreshing = false,
            comments = listOf(
                Comment(
                    id = "eros",
                    postId = "natum",
                    name = "Socorro Kramer",
                    email = "christopher.fitzpatrick@example.com",
                    body = "laudantium enim quasi est quidem magnam voluptate ipsam eos\\ntempora quo necessitatibus\\ndolor quam autem quasi\\nreiciendis et nam sapiente accusantium"
                ), Comment(
                    id = "bibendum",
                    postId = "dictum",
                    name = "Rodolfo Evans",
                    email = "annie.fitzgerald@example.com",
                    body = "est natus enim nihil est dolore omnis voluptatem numquam\\net omnis occaecati quod ullam at\\nvoluptatem error expedita pariatur\\nnihil sint nostrum voluptatem reiciendis et"
                )
            ),
            onRefresh = {},
            onErrorMessageShown = {},
            searchQuery = "fermentum",
            onSearchQueryChange = {},
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun CommentsScaffoldPreview_Empty_Comments() {
    JourneyTechAssessmentTheme {
        CommentsScaffold(
            errorMessageResId = null,
            isRefreshing = false,
            comments = listOf(),
            onRefresh = {},
            onErrorMessageShown = {},
            searchQuery = "fermentum",
            onSearchQueryChange = {},
            onBack = {},
        )
    }
}
