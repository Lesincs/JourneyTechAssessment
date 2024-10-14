package com.lesincs.journeytechassessment.comments

import com.lesincs.journeytechassessment.comments.data.Comment
import javax.inject.Inject

class CommentsRemoteDataSource @Inject constructor() {

    suspend fun getComments(postId: String): List<Comment>? {
        return null
    }
}