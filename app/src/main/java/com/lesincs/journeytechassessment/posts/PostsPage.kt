package com.lesincs.journeytechassessment.posts

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.lesincs.journeytechassessment.R
import com.lesincs.journeytechassessment.common.ui.theme.JourneyTechAssessmentTheme
import com.lesincs.journeytechassessment.posts.data.Post

@Composable
fun PostsPage(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val postsViewModel = hiltViewModel<PostsViewModel>()
    val isRefreshing: Boolean =
        postsViewModel.isRefreshingPostsStateFlow.collectAsStateWithLifecycle().value
    val posts: List<Post> = postsViewModel.posts.collectAsStateWithLifecycle().value
    val errorMessageResId: Int? =
        postsViewModel.updatePostsFailedErrorMessageResIdStateFlow.collectAsStateWithLifecycle().value
    val searchQuery: String =
        postsViewModel.searchQueryStateFlow.collectAsStateWithLifecycle().value
    PostsScaffold(
        errorMessageResId = errorMessageResId,
        isRefreshing = isRefreshing,
        posts = posts,
        onRefresh = postsViewModel::updatePosts,
        onErrorMessageShown = postsViewModel::onUpdatePostsFailedErrorMessageShown,
        searchQuery = searchQuery,
        onSearchQueryChange = postsViewModel::updateSearchQuery,
        modifier = modifier,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PostsScaffold(
    @StringRes errorMessageResId: Int?,
    isRefreshing: Boolean,
    posts: List<Post>,
    onRefresh: () -> Unit,
    onErrorMessageShown: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
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
            PostsTopAppBar(
                onSearchQueryChange = onSearchQueryChange,
                searchQuery = searchQuery,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
private fun PostsTopAppBar(
    onSearchQueryChange: (String) -> Unit,
    searchQuery: String,
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
                PostsSearchBar(
                    onSearchQueryChange = onSearchQueryChange,
                    onHideSearchBar = { showSearchBar = false },
                    searchQuery = searchQuery,
                )
            } else {
                PostsCenterAlignedTopAppBar(
                    onShowSearchBar = { showSearchBar = true },
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PostsCenterAlignedTopAppBar(onShowSearchBar: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.posts_page_title)) },
        actions = {
            IconButton(onClick = onShowSearchBar) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_posts_icon_content_description),
                )
            }
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PostsSearchBar(
    onSearchQueryChange: (String) -> Unit,
    onHideSearchBar: () -> Unit,
    searchQuery: String,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focusRequester) { focusRequester.requestFocus() }
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                modifier = Modifier.focusRequester(focusRequester),
                query = searchQuery,
                onQueryChange = { onSearchQueryChange(it) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.isNotEmpty()) {
                            onSearchQueryChange("")
                        } else {
                            onHideSearchBar()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.search_close_button_content_description),
                        )
                    }
                },
                placeholder = { Text(stringResource(R.string.search_posts_place_holder)) },
                onSearch = {},
                expanded = false,
                onExpandedChange = {},
            )
        },
        expanded = false,
        onExpandedChange = {},
        content = {},
    )
}

@Composable
private fun EmptyPostsHint() {
    // using verticalScroll so even posts are empty
    // the scroll event can still be propagated to PullToRefreshBox
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(),
            text = stringResource(R.string.no_posts_available_hint),
        )
    }
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
            errorMessageResId = null,
            onErrorMessageShown = {},
            searchQuery = "",
            onSearchQueryChange = {},
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
            errorMessageResId = null,
            onErrorMessageShown = {},
            searchQuery = "",
            onSearchQueryChange = {},
        )
    }
}