package com.lesincs.journeytechassessment.posts

import app.cash.turbine.test
import com.lesincs.journeytechassessment.MainDispatcherRule
import com.lesincs.journeytechassessment.R
import com.lesincs.journeytechassessment.posts.data.Post
import com.lesincs.journeytechassessment.posts.data.PostsRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class PostsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockPostsRepository: PostsRepository = mockk()

    private lateinit var sut: PostsViewModel

    private fun constructSut() {
        sut = PostsViewModel(
            postsRepository = mockPostsRepository,
        )
    }

    @Test
    fun `should search query be empty by default`() {
        every { mockPostsRepository.getPostsFlow() } returns flowOf()
        coEvery { mockPostsRepository.updatePosts() } returns false

        constructSut()

        sut.searchQueryStateFlow.value shouldBe ""
    }

    @Test
    fun `should search query be updated correctly`() {
        every { mockPostsRepository.getPostsFlow() } returns flowOf()
        coEvery { mockPostsRepository.updatePosts() } returns false
        constructSut()

        sut.updateSearchQuery("news")

        sut.searchQueryStateFlow.value shouldBe "news"
    }

    @Test
    fun `should posts be empty by default`() = runTest {
        every { mockPostsRepository.getPostsFlow() } returns flowOf()
        coEvery { mockPostsRepository.updatePosts() } returns false
        constructSut()

        sut.posts.value shouldBe emptyList()
    }

    @Test
    fun `should posts be posts from repository when constructing given posts are available and search query is empty`() =
        runTest {
            val posts = listOf(
                Post(id = "amet", title = "novum", body = "est"),
                Post(id = "finibus", title = "pericula", body = "constituto"),
            )
            every { mockPostsRepository.getPostsFlow() } returns flowOf(posts)
            coEvery { mockPostsRepository.updatePosts() } returns false
            constructSut()

            sut.posts.test {
                awaitItem() shouldBe emptyList()
                awaitItem() shouldBe posts
            }
        }

    @Test
    fun `should posts be filtered by search query given posts are available and search query is not empty`() =
        runTest {
            val posts = listOf(
                Post(id = "amet", title = "novum", body = "est"),
                Post(id = "finibus", title = "pericula", body = "constituto"),
            )
            every { mockPostsRepository.getPostsFlow() } returns flowOf(posts)
            coEvery { mockPostsRepository.updatePosts() } returns false
            constructSut()
            sut.updateSearchQuery("pericula")

            sut.posts.test {
                awaitItem() shouldBe emptyList()
                awaitItem() shouldBe listOf(
                    Post(
                        id = "finibus",
                        title = "pericula",
                        body = "constituto"
                    )
                )
            }
        }

    @Test
    fun `should update posts successfully when constructing given posts are updated successfully`() =
        runTest {
            val postsStateFlow = MutableStateFlow<List<Post>>(emptyList())
            every { mockPostsRepository.getPostsFlow() } returns postsStateFlow
            coEvery { mockPostsRepository.updatePosts() } answers {
                // emulate posts being updated
                postsStateFlow.value =
                    listOf(Post(id = "finibus", title = "pericula", body = "constituto"))
                true
            }

            constructSut()

            sut.posts.test {
                awaitItem() shouldBe emptyList()
                awaitItem() shouldBe listOf(
                    Post(
                        id = "finibus",
                        title = "pericula",
                        body = "constituto"
                    )
                )
            }
            sut.updatePostsFailedErrorMessageResIdStateFlow.value shouldBe null
        }

    @Test
    fun `should update posts failed when constructing given posts are updated failed`() =
        runTest {
            val postsStateFlow = MutableStateFlow<List<Post>>(emptyList())
            every { mockPostsRepository.getPostsFlow() } returns postsStateFlow
            coEvery { mockPostsRepository.updatePosts() } returns false

            constructSut()

            sut.posts.test {
                awaitItem() shouldBe emptyList()
            }
            sut.updatePostsFailedErrorMessageResIdStateFlow.value shouldBe R.string.update_posts_failed_error_message
        }

    @Test
    fun `should update posts successfully when call updatePosts given posts are update successfully`() =
        runTest {
            val postsStateFlow = MutableStateFlow<List<Post>>(emptyList())
            every { mockPostsRepository.getPostsFlow() } returns postsStateFlow
            coEvery { mockPostsRepository.updatePosts() } returns true
            constructSut()

            coEvery { mockPostsRepository.updatePosts() } answers {
                // emulate posts being updated
                postsStateFlow.value =
                    listOf(Post(id = "finibus", title = "pericula", body = "constituto"))
                true
            }
            sut.updatePosts()

            sut.posts.test {
                awaitItem() shouldBe emptyList()
                awaitItem() shouldBe listOf(
                    Post(
                        id = "finibus",
                        title = "pericula",
                        body = "constituto"
                    )
                )
            }
            sut.updatePostsFailedErrorMessageResIdStateFlow.value shouldBe null
        }

    @Test
    fun `should update posts failed when call updatePosts given posts are update failed`() =
        runTest {
            val postsStateFlow = MutableStateFlow<List<Post>>(emptyList())
            every { mockPostsRepository.getPostsFlow() } returns postsStateFlow
            coEvery { mockPostsRepository.updatePosts() } returns true
            constructSut()

            coEvery { mockPostsRepository.updatePosts() } returns false
            sut.updatePosts()

            sut.posts.test {
                awaitItem() shouldBe listOf()
            }
            sut.updatePostsFailedErrorMessageResIdStateFlow.value shouldBe R.string.update_posts_failed_error_message
        }

    @Test
    fun `should consume update posts failed error message when call onUpdatePostsFailedErrorMessageShown`() =
        runTest {
            val postsStateFlow = MutableStateFlow<List<Post>>(emptyList())
            every { mockPostsRepository.getPostsFlow() } returns postsStateFlow
            coEvery { mockPostsRepository.updatePosts() } returns false
            constructSut()
            sut.updatePostsFailedErrorMessageResIdStateFlow.value shouldBe R.string.update_posts_failed_error_message

            sut.onUpdatePostsFailedErrorMessageShown()

            sut.updatePostsFailedErrorMessageResIdStateFlow.value shouldBe null
        }

}