package com.lesincs.journeytechassessment.posts.data

import com.lesincs.journeytechassessment.MainDispatcherRule
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class PostsRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockPostsLocalDataSource: PostsLocalDataSource = mockk()
    private val mockPostsRemoteDataSource: PostsRemoteDataSource = mockk()

    private val sut: PostsRepository =
        PostsRepository(mockPostsLocalDataSource, mockPostsRemoteDataSource)

    @Test
    fun `should get postsFlow from local data source`() {
        val postsFlow = flowOf(listOf(Post(id = "eam", title = "eius", body = "expetendis")))
        every { mockPostsLocalDataSource.getPostsFlow() } returns postsFlow

        sut.getPostsFlow() shouldBe postsFlow
    }

    @Test
    fun `verify update posts successfully case`() = runTest {
        val posts = listOf(Post(id = "eam", title = "eius", body = "expetendis"))
        coEvery { mockPostsRemoteDataSource.getPosts() } returns posts
        coEvery { mockPostsLocalDataSource.updatePosts(posts) } just runs

        val updateSuccessfully = sut.updatePosts()

        updateSuccessfully shouldBe true
        coVerify(exactly = 1) {
            mockPostsRemoteDataSource.getPosts()
            mockPostsLocalDataSource.updatePosts(posts)
        }
    }
}