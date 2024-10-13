package com.lesincs.journeytechassessment.posts.data

import com.lesincs.journeytechassessment.common.db.AppDatabase
import com.lesincs.journeytechassessment.posts.data.persistance.PostDao
import com.lesincs.journeytechassessment.posts.data.persistance.PostEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostsLocalDataSource @Inject constructor(
    appDatabase: AppDatabase,
) {

    private val postsDao: PostDao = appDatabase.postDao()

    fun getPostsFlow(): Flow<List<Post>> {
        return postsDao.getAllPosts().map { entities ->
            entities.map { entity ->
                Post(
                    id = entity.id,
                    title = entity.title,
                    body = entity.body,
                )
            }
        }
    }

    suspend fun updatePosts(posts: List<Post>) {
        postsDao.replaceAllPosts(
            posts = posts.map { post ->
                PostEntity(
                    id = post.id,
                    title = post.title,
                    body = post.body,
                )
            }
        )
    }
}

