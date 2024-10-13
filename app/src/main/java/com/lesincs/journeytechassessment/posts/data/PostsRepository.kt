package com.lesincs.journeytechassessment.posts.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val localDataSource: PostsLocalDataSource,
    private val remoteDataSource: PostsRemoteDataSource,
) {

    fun getPostsFlow(): Flow<List<Post>> = localDataSource.getPostsFlow()

    suspend fun updatePosts(): Boolean {
        val posts = remoteDataSource.getPosts() ?: return false
        localDataSource.updatePosts(posts)
        return true
    }
}