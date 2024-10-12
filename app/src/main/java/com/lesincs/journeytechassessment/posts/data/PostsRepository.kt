package com.lesincs.journeytechassessment.posts.data

import com.lesincs.journeytechassessment.posts.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PostsRepository @Inject constructor() {

    fun getPostsFlow(): Flow<List<Post>> {
        return flowOf(emptyList())
    }

    suspend fun updatePosts(): Boolean {
        return false
    }
}