package com.lesincs.journeytechassessment.comments.data

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

class CommentsRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockCommentsLocalDataSource: CommentsLocalDataSource = mockk()
    private val mockCommentsRemoteDataSource: CommentsRemoteDataSource = mockk()

    private val sut: CommentsRepository =
        CommentsRepository(mockCommentsLocalDataSource, mockCommentsRemoteDataSource)

    @Test
    fun `should get commentsFlow from local data source`() {
        val commentsFlow = flowOf(
            listOf(
                Comment(
                    id = "nihil",
                    postId = "curae",
                    name = "Kent Green",
                    email = "sondra.vaughan@example.com",
                    body = "senectus"
                )
            )
        )
        every { mockCommentsLocalDataSource.getCommentsFlow("1") } returns commentsFlow

        sut.getCommentsFlow("1") shouldBe commentsFlow
    }

    @Test
    fun `verify update comments successfully case`() = runTest {
        val comments = listOf(
            Comment(
                id = "nihil",
                postId = "curae",
                name = "Kent Green",
                email = "sondra.vaughan@example.com",
                body = "senectus"
            )
        )
        coEvery { mockCommentsRemoteDataSource.getComments("1") } returns comments
        coEvery { mockCommentsLocalDataSource.updateComments("1", comments) } just runs

        val updateSuccessfully = sut.updateComments("1")

        updateSuccessfully shouldBe true
        coVerify(exactly = 1) {
            mockCommentsRemoteDataSource.getComments("1")
            mockCommentsLocalDataSource.updateComments("1", comments)
        }
    }

    @Test
    fun `verify update comments failed case`() = runTest {
        coEvery { mockCommentsRemoteDataSource.getComments("1") } returns null

        val updateSuccessfully = sut.updateComments("1")

        updateSuccessfully shouldBe false
        coVerify(exactly = 0) {
            mockCommentsLocalDataSource.updateComments(any(), any())
        }
    }
}