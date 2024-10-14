package com.lesincs.journeytechassessment.comments

import com.lesincs.journeytechassessment.comments.data.Comment
import com.lesincs.journeytechassessment.getMockHttpClient
import com.lesincs.journeytechassessment.posts.data.Post
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class CommentsRemoteDataSourceTest {
    @Test
    fun `should get comments given http client get comments successfully`() = runTest {
        val comments = listOf(
            Comment(
                id = "mucius",
                postId = "meliore",
                name = "Juliette Richmond",
                email = "dewayne.rowland@example.com",
                body = "aeque",
            )
        )
        val sut = CommentsRemoteDataSource(
            httpClient = getMockHttpClient(
                path = "/posts/1/comments",
                success = true,
                responseBody = comments
            )
        )
        sut.getComments("1") shouldBe comments
    }

    @Test
    fun `should get null given http client get posts failed`() = runTest {
        val sut = CommentsRemoteDataSource(
            httpClient = getMockHttpClient<List<Comment>>(
                path = "/posts/1/comments",
                success = false,
            )
        )
        sut.getComments("1") shouldBe null
    }
}