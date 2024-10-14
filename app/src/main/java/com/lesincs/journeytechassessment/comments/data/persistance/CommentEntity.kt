package com.lesincs.journeytechassessment.comments.data.persistance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey
    val id: String,
    val postId: String,
    val name: String,
    val email: String,
    val body: String,
)