package io.github.masamune.util.tiled

import kotlinx.serialization.Serializable

@Serializable
data class TiledProject(
    val propertyTypes: List<PropertyType>,
)
