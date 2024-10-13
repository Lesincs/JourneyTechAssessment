package com.lesincs.journeytechassessment.posts.data

import com.lesincs.journeytechassessment.posts.Post
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PostsRemoteDataSource @Inject constructor(
    private val httpClient: HttpClient,
) {

    suspend fun getPosts(): List<Post>? = try {
        httpClient.get("/posts").body<List<Post>>()
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        exception.printStackTrace()
        null
    }
}