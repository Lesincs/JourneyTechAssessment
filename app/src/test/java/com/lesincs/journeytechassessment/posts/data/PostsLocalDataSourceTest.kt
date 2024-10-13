package com.lesincs.journeytechassessment.posts.data

import app.cash.turbine.test
import com.lesincs.journeytechassessment.common.db.AppDatabase
import com.lesincs.journeytechassessment.posts.data.persistance.PostDao
import com.lesincs.journeytechassessment.posts.data.persistance.PostEntity
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PostsLocalDataSourceTest {
    private val mockPostDao: PostDao = mockk(relaxed = true)
    private val mockRoomDatabase: AppDatabase = mockk() {
        every { postDao() } returns mockPostDao
    }

    private val sut: PostsLocalDataSource = PostsLocalDataSource(mockRoomDatabase)

    @Test
    fun `should update posts through PostDao`() = runTest {
        sut.updatePosts(
            posts = listOf(
                Post(id = "mucius", title = "epicuri", body = "amet"),
                Post(id = "luptatum", title = "urna", body = "constituto"),
            )
        )

        coVerify(exactly = 1) {
            mockPostDao.replaceAllPosts(
                posts = listOf(
                    PostEntity(id = "mucius", title = "epicuri", body = "amet"),
                    PostEntity(id = "luptatum", title = "urna", body = "constituto"),
                )
            )
        }
    }

    @Test
    fun `should get posts flow through PostDao`() = runTest {
        every { mockPostDao.getAllPosts() } returns flowOf(
            listOf(
                PostEntity(
                    id = "mucius",
                    title = "epicuri",
                    body = "amet"
                )
            )
        )
        val postsFlow = sut.getPostsFlow()

        postsFlow.test {
            awaitItem() shouldBe listOf(Post(id = "mucius", title = "epicuri", body = "amet"))
            awaitComplete()
        }
    }
}