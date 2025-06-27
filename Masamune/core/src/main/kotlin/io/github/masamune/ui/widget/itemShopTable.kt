package io.github.masamune.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.KGroup
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.scene2d

@Scene2dDsl
class ItemShopTable(
    private val talonsPostfix: String,
    skin: Skin,
) : ScrollableSelectionTable<ShopItemWidget>(
    skin,
    entriesPerRow = 1,
), KGroup {

    fun clearAmounts() {
        forEach { it.amount(0) }
    }

    fun item(title: String, cost: Int) {
        val shopItem = scene2d.shopItem(title, cost, talonsPostfix, skin) {
            select(this@ItemShopTable.numEntries == 0)
        }
        addEntry(shopItem)
    }

    fun amount(value: Int) = selectedEntry.amount(value)

    fun stopSelectAnimation() = selectedEntry.stopSelectAnimation()

    fun resumeSelectAnimation() = selectedEntry.resumeSelectAnimation()

}

@Scene2dDsl
fun <S> KWidget<S>.itemShopTable(
    talonsPostfix: String,
    skin: Skin,
    init: (@Scene2dDsl ItemShopTable).(S) -> Unit = {},
): ItemShopTable = actor(ItemShopTable(talonsPostfix, skin), init)
