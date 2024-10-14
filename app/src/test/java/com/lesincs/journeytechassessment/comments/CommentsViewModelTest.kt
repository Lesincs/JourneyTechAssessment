package com.lesincs.journeytechassessment.comments

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.lesincs.journeytechassessment.MainDispatcherRule
import com.lesincs.journeytechassessment.R
import com.lesincs.journeytechassessment.comments.data.Comment
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CommentsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockCommentsRepository: CommentsRepository = mockk()
    private val mockSavedStateHandle: SavedStateHandle = mockk()

    private lateinit var sut: CommentsViewModel

    private fun constructSut() {
        sut = CommentsViewModel(
            savedStateHandle = mockSavedStateHandle,
            commentsRepository = mockCommentsRepository,
        )
    }

    @Before
    fun setUp() {
        every { mockSavedStateHandle.get<String>("postId") } returns "1"
    }

    @Test
    fun `should search query be empty by default`() {
        every { mockCommentsRepository.getCommentsFlow("1") } returns flowOf()
        coEvery { mockCommentsRepository.updateComments("1") } returns false

        constructSut()

        sut.searchQueryStateFlow.value shouldBe ""
    }

    @Test
    fun `should search query be updated correctly`() {
        every { mockCommentsRepository.getCommentsFlow("1") } returns flowOf()
        coEvery { mockCommentsRepository.updateComments("1") } returns false
        constructSut()

        sut.updateSearchQuery("news")

        sut.searchQueryStateFlow.value shouldBe "news"
    }

    @Test
    fun `should comments be empty by default`() = runTest {
        every { mockCommentsRepository.getCommentsFlow("1") } returns flowOf()
        coEvery { mockCommentsRepository.updateComments("1") } returns false
        constructSut()

        sut.comments.value shouldBe emptyList()
    }

    @Test
    fun `should comments be comments from repository when constructing given comments are available and search query is empty`() =
        runTest {
            val comments = listOf(
                Comment(
                    id = "1",
                    postId = "1",
                    name = "Clarice",
                    email = "boo.cig@eple.com",
                    body = "conclusi"
                ),
                Comment(
                    id = "2",
                    postId = "1",
                    name = "Johb",
                    email = "foo.cig@gmail.com",
                    body = "constituto"
                ),
            )
            every { mockCommentsRepository.getCommentsFlow("1") } returns flowOf(comments)
            coEvery { mockCommentsRepository.updateComments("1") } returns false
            constructSut()

            sut.comments.test {
                awaitItem() shouldBe emptyList()
                awaitItem() shouldBe comments
            }
        }

    @Test
    fun `should comments be filtered by search query given comments are available and search query is not empty`() =
        runTest {
            val comments = listOf(
                Comment(
                    id = "1",
                    postId = "1",
                    name = "Clarice",
                    email = "boo.cig@eple.com",
                    body = "conclusi"
                ),
                Comment(
                    id = "2",
                    postId = "1",
                    name = "Johb",
                    email = "foo.cig@gmail.com",
                    body = "constituto"
                ),
            )
            every { mockCommentsRepository.getCommentsFlow("1") } returns flowOf(comments)
            coEvery { mockCommentsRepository.updateComments("1") } returns false
            constructSut()
            sut.updateSearchQuery("Johb")

            sut.comments.test {
                awaitItem() shouldBe emptyList()
                awaitItem() shouldBe listOf(
                    Comment(
                        id = "2",
                        postId = "1",
                        name = "Johb",
                        email = "foo.cig@gmail.com",
                        body = "constituto"
                    )
                )
            }
        }

    @Test
    fun `should update comments successfully when constructing given comments are updated successfully`() =
        runTest {
            val commentsStateFlow = MutableStateFlow<List<Comment>>(emptyList())
            every { mockCommentsRepository.getCommentsFlow("1") } returns commentsStateFlow

            coEvery { mockCommentsRepository.updateComments("1") } answers {
                // emulate comments being updated
                commentsStateFlow.value =
                    listOf(
                        Comment(
                            id = "dicat",
                            postId = "persius",
                            name = "Rocky Watkins",
                            email = "annabelle.holder@example.com",
                            body = "himenaeos"
                        )
                    )
                true
            }

            constructSut()

            sut.comments.test {
                awaitItem() shouldBe emptyList()
                awaitItem() shouldBe listOf(
                    Comment(
                        id = "dicat",
                        postId = "persius",
                        name = "Rocky Watkins",
                        email = "annabelle.holder@example.com",
                        body = "himenaeos"
                    )
                )
            }
            sut.updateCommentsFailedErrorMessageResIdStateFlow.value shouldBe null
        }

    @Test
    fun `should update comments failed when constructing given comments are updated failed`() =
        runTest {
            val commentsStateFlow = MutableStateFlow<List<Comment>>(emptyList())
            every { mockCommentsRepository.getCommentsFlow("1") } returns commentsStateFlow
            coEvery { mockCommentsRepository.updateComments("1") } returns false

            constructSut()

            sut.comments.test {
                awaitItem() shouldBe emptyList()
            }
            sut.updateCommentsFailedErrorMessageResIdStateFlow.value shouldBe R.string.update_comments_failed_error_message
        }

    @Test
    fun `should update comments successfully when call updateComments given comments are update successfully`() =
        runTest {
            val commentsStateFlow = MutableStateFlow<List<Comment>>(emptyList())
            every { mockCommentsRepository.getCommentsFlow("1") } returns commentsStateFlow
            coEvery { mockCommentsRepository.updateComments("1") } returns true
            constructSut()

            coEvery { mockCommentsRepository.updateComments("1") } answers {
                // emulate comments being updated
                commentsStateFlow.value =
                    listOf(
                        Comment(
                            id = "dicat",
                            postId = "persius",
                            name = "Rocky Watkins",
                            email = "annabelle.holder@example.com",
                            body = "himenaeos"
                        )
                    )
                true
            }
            sut.updateComments()

            sut.comments.test {
                awaitItem() shouldBe emptyList()
                awaitItem() shouldBe listOf(
                    Comment(
                        id = "dicat",
                        postId = "persius",
                        name = "Rocky Watkins",
                        email = "annabelle.holder@example.com",
                        body = "himenaeos"
                    )
                )
            }
            sut.updateCommentsFailedErrorMessageResIdStateFlow.value shouldBe null
        }

    @Test
    fun `should update comments failed when call updateComments given comments are update failed`() =
        runTest {
            val commentsStateFlow = MutableStateFlow<List<Comment>>(emptyList())
            every { mockCommentsRepository.getCommentsFlow("1") } returns commentsStateFlow
            coEvery { mockCommentsRepository.updateComments("1") } returns true
            constructSut()

            coEvery { mockCommentsRepository.updateComments("1") } returns false
            sut.updateComments()

            sut.comments.test {
                awaitItem() shouldBe emptyList()
            }
            sut.updateCommentsFailedErrorMessageResIdStateFlow.value shouldBe R.string.update_comments_failed_error_message
        }

    @Test
    fun `should consume update comments failed error message when call onUpdateCommentsFailedErrorMessageShown`() =
        runTest {
            val commentsStateFlow = MutableStateFlow<List<Comment>>(emptyList())
            every { mockCommentsRepository.getCommentsFlow("1") } returns commentsStateFlow
            coEvery { mockCommentsRepository.updateComments("1") } returns false
            constructSut()
            sut.updateCommentsFailedErrorMessageResIdStateFlow.value shouldBe R.string.update_comments_failed_error_message

            sut.onUpdateCommentsFailedErrorMessageShown()

            sut.updateCommentsFailedErrorMessageResIdStateFlow.value shouldBe null
        }
}