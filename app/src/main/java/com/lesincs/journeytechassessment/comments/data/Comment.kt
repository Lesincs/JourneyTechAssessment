package com.lesincs.journeytechassessment.comments.data

data class Comment(
    val id: String,
    val postId: String,
    val name: String,
    val email: String,
    val body: String,
)