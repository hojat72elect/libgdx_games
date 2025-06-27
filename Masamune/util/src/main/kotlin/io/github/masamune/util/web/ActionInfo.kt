package io.github.masamune.util.web

import kotlinx.serialization.Serializable

@Serializable
data class ActionInfo(
    val name: String,
    val description: String,
    val type: String,
    val targetType: String,
    val manaCost: Int,
)
