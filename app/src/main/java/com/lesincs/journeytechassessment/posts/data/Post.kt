package com.lesincs.journeytechassessment.posts.data

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val title: String,
    val body: String,
) {
    fun matchesSearchQuery(query: String): Boolean =
        title.contains(other = query, ignoreCase = true) ||
                body.contains(other = query, ignoreCase = true)
}