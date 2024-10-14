package com.lesincs.journeytechassessment.comments.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentsRepository @Inject constructor(
    private val localDataSource: CommentsLocalDataSource,
    private val remoteDataSource: CommentsRemoteDataSource,
) {

    fun getCommentsFlow(postId: String): Flow<List<Comment>> {
        return localDataSource.getCommentsFlow(postId = postId)
    }

    suspend fun updateComments(postId: String): Boolean {
        val comments = remoteDataSource.getComments(postId = postId) ?: return false
        localDataSource.updateComments(postId = postId, comments = comments)
        return true
    }
}