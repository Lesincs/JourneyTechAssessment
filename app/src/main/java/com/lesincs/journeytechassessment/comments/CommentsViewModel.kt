package com.lesincs.journeytechassessment.comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.lesincs.journeytechassessment.comments.data.Comment
import com.lesincs.journeytechassessment.comments.data.CommentsRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@HiltViewModel
class CommentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val commentsRepository: CommentsRepository,
) : ViewModel() {
    private val postId: String = savedStateHandle.get<String>("postId").orEmpty()
    
    private val _isRefreshingCommentsStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRefreshingCommentsStateFlow: StateFlow<Boolean> = _isRefreshingCommentsStateFlow.asStateFlow()

    private val _searchQueryStateFlow: MutableStateFlow<String> = MutableStateFlow("")
    val searchQueryStateFlow: StateFlow<String> = _searchQueryStateFlow.asStateFlow()

    private val _updateCommentsFailedErrorMessageResIdStateFlow:MutableStateFlow<Int?> = MutableStateFlow(null)
    val updateCommentsFailedErrorMessageResIdStateFlow= _updateCommentsFailedErrorMessageResIdStateFlow.asStateFlow()

    @OptIn(FlowPreview::class)
    val comments: StateFlow<List<Comment>> =
        combine(
            commentsRepository.getCommentsFlow(postId),
            _searchQueryStateFlow.debounce(SEARCH_QUERY_DEBOUNCE_MILLIS),
        ) { comments, searchQuery ->
            if (searchQuery.isEmpty()) {
                comments
            } else {
                comments.filter { comment -> comment.matchesSearchQuery(searchQuery) }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    init {
        updateComments()
    }

    fun updateSearchQuery(query: String) {
        _searchQueryStateFlow.value = query
    }

     fun updateComments() {
        _isRefreshingCommentsStateFlow.value = true
        viewModelScope.launch {
            val updatedSuccessfully = commentsRepository.updateComments(postId)
            if (!updatedSuccessfully) {
                _updateCommentsFailedErrorMessageResIdStateFlow.value = R.string.update_comments_failed_error_message
            }
            _isRefreshingCommentsStateFlow.value = false
        }
    }

    fun onUpdateCommentsFailedErrorMessageShown() {
        _updateCommentsFailedErrorMessageResIdStateFlow.value = null
    }

    companion object {
        private const val SEARCH_QUERY_DEBOUNCE_MILLIS = 100L
    }
}