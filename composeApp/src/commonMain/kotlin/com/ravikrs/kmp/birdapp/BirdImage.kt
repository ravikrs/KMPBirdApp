package com.ravikrs.kmp.birdapp

import kotlinx.serialization.Serializable

@Serializable
data class BirdImage(
    val author: String,
    val category: String,
    val path: String
)