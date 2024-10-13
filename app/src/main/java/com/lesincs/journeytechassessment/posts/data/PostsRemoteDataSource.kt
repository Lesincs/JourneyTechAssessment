package com.lesincs.journeytechassessment.posts.data

import com.lesincs.journeytechassessment.posts.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PostsRemoteDataSource @Inject constructor() {

    suspend fun getPosts(): List<Post>? {
        return null
    }
}