package io.github.masamune.util.tiled

import kotlinx.serialization.Serializable

@Serializable
data class PropertyType(
    val id: Int,
    val name: String,
    val type: String,
    val values: List<String> = emptyList(),
    val members: List<Member> = emptyList(),
)
