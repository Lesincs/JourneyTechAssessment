package com.lesincs.journeytechassessment.posts.data.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * from posts")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Transaction
    suspend fun replaceAllPosts(posts: List<PostEntity>) {
        deleteAllPosts()
        insertPosts(posts)
    }

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)
}