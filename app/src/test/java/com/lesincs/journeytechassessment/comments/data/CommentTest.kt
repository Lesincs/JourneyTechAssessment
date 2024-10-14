package com.lesincs.journeytechassessment.comments.data

import io.kotest.matchers.shouldBe
import org.junit.Test

class CommentTest {

    @Test
    fun `should match search query given query is empty`() {
        val comment = Comment(
            id = "verterem",
            postId = "fugit",
            name = "Larry Landry",
            email = "daisy.ewing@example.com",
            body = "prompta"
        )

        comment.matchesSearchQuery("") shouldBe true
    }

    @Test
    fun `should match search query given name contains search query`() {
        val comment = Comment(
            id = "verterem",
            postId = "fugit",
            name = "Larry Landry",
            email = "daisy.ewing@example.com",
            body = "prompta"
        )

        comment.matchesSearchQuery("Landry") shouldBe true
    }

    @Test
    fun `should match search query given name contains search query regardless case`() {
        val comment = Comment(
            id = "verterem",
            postId = "fugit",
            name = "Larry Landry",
            email = "daisy.ewing@example.com",
            body = "prompta"
        )

        comment.matchesSearchQuery("LANDRY") shouldBe true
    }

    @Test
    fun `should match search query given email contains search query`() {
        val comment = Comment(
            id = "verterem",
            postId = "fugit",
            name = "Larry Landry",
            email = "daisy.ewing@example.com",
            body = "prompta"
        )

        comment.matchesSearchQuery("@example.com") shouldBe true
    }

    @Test
    fun `should match search query given email contains search query regardless case`() {
        val comment = Comment(
            id = "verterem",
            postId = "fugit",
            name = "Larry Landry",
            email = "daisy.ewing@example.com",
            body = "prompta"
        )

        comment.matchesSearchQuery("@EXAMPLE.COM") shouldBe true
    }

    @Test
    fun `should match search query given body contains search query`() {
        val comment = Comment(
            id = "verterem",
            postId = "fugit",
            name = "Larry Landry",
            email = "daisy.ewing@example.com",
            body = "prompta test"
        )

        comment.matchesSearchQuery("a te") shouldBe true
    }

    @Test
    fun `should match search query given body contains search query regardless case`() {
        val comment = Comment(
            id = "verterem",
            postId = "fugit",
            name = "Larry Landry",
            email = "daisy.ewing@example.com",
            body = "prompta test"
        )

        comment.matchesSearchQuery("TA TE") shouldBe true
    }

    @Test
    fun `verify not match case`() {
        val comment = Comment(
            id = "verterem",
            postId = "fugit",
            name = "Larry Landry",
            email = "daisy.ewing@example.com",
            body = "prompta test"
        )

        comment.matchesSearchQuery("AAA") shouldBe false
    }
}