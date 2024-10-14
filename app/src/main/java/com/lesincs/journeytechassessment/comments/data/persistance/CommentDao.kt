package com.lesincs.journeytechassessment.comments.data.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Query("SELECT * from comments where postId = :postId")
    fun getAllComments(postId: String): Flow<List<CommentEntity>>

    @Transaction
    suspend fun replaceAllComments(postId: String, comments: List<CommentEntity>) {
        deleteAllComments(postId)
        insertComments(comments)
    }

    @Query("DELETE FROM comments where postId = :postId")
    suspend fun deleteAllComments(postId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)
}