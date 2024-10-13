package com.lesincs.journeytechassessment.posts.data

import com.lesincs.journeytechassessment.getMockHttpClient
import com.lesincs.journeytechassessment.posts.Post
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PostsRemoteDataSourceTest {

    @Test
    fun `should get posts given http client get posts successfully`() = runTest {
        val posts = listOf(Post(id = "gravida", title = "tation", body = "iuvaret"))
        val sut = PostsRemoteDataSource(
            httpClient = getMockHttpClient(
                path = "/posts",
                success = true,
                responseBody = posts
            )
        )
        sut.getPosts() shouldBe posts
    }

    @Test
    fun `should get null given http client get posts failed`() = runTest {
        val sut = PostsRemoteDataSource(
            httpClient = getMockHttpClient<List<Post>>(path = "/posts", success = false)
        )
        sut.getPosts() shouldBe null
    }

}