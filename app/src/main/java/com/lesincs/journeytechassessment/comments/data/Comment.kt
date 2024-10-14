package com.lesincs.journeytechassessment.comments.data

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: String,
    val postId: String,
    val name: String,
    val email: String,
    val body: String,
) {
    fun matchesSearchQuery(query: String): Boolean =
        name.contains(other = query, ignoreCase = true) ||
                email.contains(other = query, ignoreCase = true) ||
                body.contains(other = query, ignoreCase = true)
}