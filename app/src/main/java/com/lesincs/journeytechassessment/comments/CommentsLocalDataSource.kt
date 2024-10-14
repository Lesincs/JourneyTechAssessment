package com.lesincs.journeytechassessment.comments

import com.lesincs.journeytechassessment.comments.data.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CommentsLocalDataSource @Inject constructor() {

    fun getCommentsFlow(postId: String): Flow<List<Comment>> {
        return flowOf()
    }

    suspend fun updateComments(postId: String, comments: List<Comment>) {

    }
}