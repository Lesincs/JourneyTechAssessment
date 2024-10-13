package com.lesincs.journeytechassessment.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesincs.journeytechassessment.posts.data.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.lesincs.journeytechassessment.R
import com.lesincs.journeytechassessment.posts.data.Post

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
) : ViewModel() {
    private val _isRefreshingPostsStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshingPostsStateFlow: StateFlow<Boolean> = _isRefreshingPostsStateFlow.asStateFlow()

    private val _searchQueryStateFlow: MutableStateFlow<String> = MutableStateFlow("")
    val searchQueryStateFlow: StateFlow<String> = _searchQueryStateFlow.asStateFlow()

    private val _updatePostsFailedErrorMessageResIdStateFlow:MutableStateFlow<Int?> = MutableStateFlow(null)
    val updatePostsFailedErrorMessageResIdStateFlow= _updatePostsFailedErrorMessageResIdStateFlow.asStateFlow()

    val posts: StateFlow<List<Post>> =
        combine(postsRepository.getPostsFlow(), _searchQueryStateFlow) { posts, searchQuery ->
            if (searchQuery.isEmpty()) {
                posts
            } else {
                posts.filter { post -> post.matchesSearchQuery(searchQuery) }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    init {
        updatePosts()
    }

    fun updateSearchQuery(query: String) {
        _searchQueryStateFlow.value = query
    }

     fun updatePosts() {
        _isRefreshingPostsStateFlow.value = true
        viewModelScope.launch {
            val updatedSuccessfully = postsRepository.updatePosts()
            if (!updatedSuccessfully) {
                _updatePostsFailedErrorMessageResIdStateFlow.value = R.string.update_posts_failed_error_message
            }
            _isRefreshingPostsStateFlow.value = false
        }
    }

    fun onUpdatePostsFailedErrorMessageShown() {
        _updatePostsFailedErrorMessageResIdStateFlow.value = null
    }
}