package io.github.masamune.ui.model

import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import io.github.masamune.tiledmap.ItemCategory
import io.github.masamune.tiledmap.ItemType

data class ItemModel(
    val type: ItemType,
    val stats: Map<UIStats, Int>,
    val name: String,
    val cost: Int,
    val description: String,
    val category: ItemCategory,
    val image: Drawable?,
    var consumable: Boolean,
    var selected: Int = 0, // amount to buy/sell
    var amount: Int = 0, // current player amount
)

fun emptyItemModel(category: ItemCategory, text: String): ItemModel {
    return ItemModel(ItemType.UNDEFINED, emptyMap(), text, 0, "", category, null, false)
}
