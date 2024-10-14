package com.lesincs.journeytechassessment.comments.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class CommentsRemoteDataSource @Inject constructor(
    private val httpClient: HttpClient,
) {

    suspend fun getComments(postId: String): List<Comment>? {
        return try {
            httpClient.get("/posts/$postId/comments").body<List<Comment>>()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }
}