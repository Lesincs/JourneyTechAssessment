package com.lesincs.journeytechassessment.common.navigation

import kotlinx.serialization.Serializable

object AppRoutes {

    @Serializable
    data object Posts

    @Serializable
    data class Comments(val postId: String)
}