package com.lesincs.journeytechassessment.comments.data

import com.lesincs.journeytechassessment.comments.data.persistance.CommentDao
import com.lesincs.journeytechassessment.comments.data.persistance.CommentEntity
import com.lesincs.journeytechassessment.common.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentsLocalDataSource @Inject constructor(
    appDatabase: AppDatabase,
) {

    private val commentDao: CommentDao = appDatabase.commentDao()

    fun getCommentsFlow(postId: String): Flow<List<Comment>> =
        commentDao.getAllComments(postId).map {
            it.map { commentEntity ->
                Comment(
                    id = commentEntity.id,
                    postId = commentEntity.postId,
                    name = commentEntity.name,
                    email = commentEntity.email,
                    body = commentEntity.body
                )
            }
        }

    suspend fun updateComments(postId: String, comments: List<Comment>) {
        commentDao.replaceAllComments(postId, comments.map { comment ->
            CommentEntity(
                id = comment.id,
                postId = comment.postId,
                name = comment.name,
                email = comment.email,
                body = comment.body
            )
        })
    }
}