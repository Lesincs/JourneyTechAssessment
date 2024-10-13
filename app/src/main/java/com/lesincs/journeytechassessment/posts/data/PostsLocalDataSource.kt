package com.lesincs.journeytechassessment.posts.data

import com.lesincs.journeytechassessment.posts.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PostsLocalDataSource @Inject constructor() {

    fun getPostsFlow(): Flow<List<Post>> {
        return flowOf()
    }

    suspend fun updatePosts(posts: List<Post>) {

    }
}