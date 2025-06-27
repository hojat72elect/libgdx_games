package io.github.masamune.util.web

import kotlinx.serialization.Serializable

@Serializable
data class ItemInfo(
    val imageUrl: String,
    val name: String,
    val category: String,
    val stats: Map<String, Float>,
    val speed: Float,
    val action: String,
    val cost: Int,
)
