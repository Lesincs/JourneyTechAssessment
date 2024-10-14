package com.lesincs.journeytechassessment.comments

import app.cash.turbine.test
import com.lesincs.journeytechassessment.comments.data.Comment
import com.lesincs.journeytechassessment.comments.data.persistance.CommentDao
import com.lesincs.journeytechassessment.comments.data.persistance.CommentEntity
import com.lesincs.journeytechassessment.common.db.AppDatabase
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CommentsLocalDataSourceTest {
    private val mockCommentDao: CommentDao = mockk(relaxed = true)
    private val mockRoomDatabase: AppDatabase = mockk() {
        every { commentDao() } returns mockCommentDao
    }

    private val sut: CommentsLocalDataSource = CommentsLocalDataSource(mockRoomDatabase)

    @Test
    fun `should update comments through CommentDao`() = runTest {
        sut.updateComments(
            postId = "1",
            comments = listOf(
                Comment(
                    id = "vituperata",
                    postId = "montes",
                    name = "Forest Emerson",
                    email = "lilly.schroeder@example.com",
                    body = "dicit"
                ),
                Comment(
                    id = "postea",
                    postId = "salutatus",
                    name = "Mitch Wilkerson",
                    email = "cleo.burton@example.com",
                    body = "arcu"
                ),
            )
        )

        coVerify(exactly = 1) {
            mockCommentDao.replaceAllComments(
                "1",
                comments = listOf(
                    CommentEntity(
                        id = "vituperata",
                        postId = "montes",
                        name = "Forest Emerson",
                        email = "lilly.schroeder@example.com",
                        body = "dicit"
                    ),
                    CommentEntity(
                        id = "postea",
                        postId = "salutatus",
                        name = "Mitch Wilkerson",
                        email = "cleo.burton@example.com",
                        body = "arcu"
                    ),
                )
            )
        }
    }

    @Test
    fun `should get comments flow through CommentDao`() = runTest {
        every { mockCommentDao.getAllComments("1") } returns flowOf(
            listOf(
                CommentEntity(
                    id = "posidonium",
                    postId = "rhoncus",
                    name = "Johnnie Jensen",
                    email = "eric.dalton@example.com",
                    body = "idque"

                )
            )
        )
        val commentsFlow = sut.getCommentsFlow("1")

        commentsFlow.test {
            awaitItem() shouldBe listOf(
                Comment(
                    id = "posidonium",
                    postId = "rhoncus",
                    name = "Johnnie Jensen",
                    email = "eric.dalton@example.com",
                    body = "idque"
                )
            )
            awaitComplete()
        }
    }

}