package io.github.masamune.util.web

import kotlinx.serialization.Serializable

@Serializable
data class EnemyInfo(
    val imageUrl: String,
    val name: String,
    val xp: Int,
    val talons: Int,
    val stats: Map<String, Float>,
    val level: Int,
    val combatActions: List<String>,
)
