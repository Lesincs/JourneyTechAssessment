package com.lesincs.journeytechassessment.posts

import io.kotest.matchers.shouldBe
import org.junit.Test

class PostTest {
    @Test
    fun `should match search query given query is empty`() {
        val post = Post(id = "ea", title = "fastidii", body = "consul")

        post.matchesSearchQuery("") shouldBe true
    }

    @Test
    fun `should match search query given title contains search query`() {
        val post = Post(id = "risus", title = "odio", body = "equidem")

        post.matchesSearchQuery("dio") shouldBe true
    }

    @Test
    fun `should match search query given title contains search query regardless case`() {
        val post = Post(id = "risus", title = "odio", body = "equidem")

        post.matchesSearchQuery("DIO") shouldBe true
    }

    @Test
    fun `should match search query given body contains search query`() {
        val post = Post(id = "risus", title = "odio", body = "equidem")

        post.matchesSearchQuery("equidem") shouldBe true
    }

    @Test
    fun `should match search query given body contains search query regardless case`() {
        val post = Post(id = "risus", title = "odio", body = "equidem")

        post.matchesSearchQuery("EQUIDEM") shouldBe true
    }
}