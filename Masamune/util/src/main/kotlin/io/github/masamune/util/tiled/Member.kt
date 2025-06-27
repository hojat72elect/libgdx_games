package io.github.masamune.util.tiled

import io.github.masamune.util.toMasamuneClassname
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class Member(
    val name: String,
    val type: String,
    val value: JsonElement,
    val propertyType: String = "",
) {
    val kotlinType: String = when (type) {
        "string", "color" -> "String"
        "bool" -> "Boolean"
        "float" -> "Float"
        "int" -> "Int"
        "class" -> name.toMasamuneClassname()
        else -> error("Unsupported type: $type")
    }

    val kotlinValue: String = when (type) {
        "string", "color" -> "\"${(value as JsonPrimitive).content}\""
        "float" -> "${(value as JsonPrimitive).content}f"
        "class" -> ""
        else -> (value as JsonPrimitive).content
    }
}
