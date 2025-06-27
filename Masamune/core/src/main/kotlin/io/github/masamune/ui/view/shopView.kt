package io.github.masamune.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import io.github.masamune.tiledmap.ItemCategory
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.ItemModel
import io.github.masamune.ui.model.ShopOption
import io.github.masamune.ui.model.ShopViewModel
import io.github.masamune.ui.model.UIStats
import io.github.masamune.ui.widget.ItemInfoTable
import io.github.masamune.ui.widget.ItemShopTable
import io.github.masamune.ui.widget.OptionTable
import io.github.masamune.ui.widget.PopupTable
import io.github.masamune.ui.widget.ShopStatsTable
import io.github.masamune.ui.widget.itemInfoTable
import io.github.masamune.ui.widget.itemShopTable
import io.github.masamune.ui.widget.optionTable
import io.github.masamune.ui.widget.popupTable
import io.github.masamune.ui.widget.shopStatsTable
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table


private enum class ShopViewFocus {
    OPTIONS, ITEMS, CONFIRM
}

/**
 * ShopView is a 2x2 table where:
 * - top left show the name of the shop + the player stats
 * - top right shows the possible options like weapon, sell or quit
 * - bottom left shows a scroll pane with all the items of a specific option
 * - bottom right shows the details of an item including its image
 */
@Scene2dDsl
class ShopView(
    model: ShopViewModel,
    skin: Skin,
) : View<ShopViewModel>(skin, model), KTable {

    private val talonLabel: Label
    private val totalLabel: Label

    private val shopStatsTable: ShopStatsTable
    private val itemShopTable: ItemShopTable
    private val optionTable: OptionTable
    private val itemInfoTable: ItemInfoTable
    private val popupTable: PopupTable

    private val talonsPostfix = "[#FFFFFF77]${i18nTxt(I18NKey.STATS_TALONS).first()}[]"
    private var focus = ShopViewFocus.OPTIONS
    private var activeItems: List<ItemModel> = emptyList()

    init {
        background = skin.getDrawable("dialog_frame")
        setFillParent(true)

        // top left -> shop title and stats
        val uiStatsLabels = mapOf(
            UIStats.STRENGTH to i18nTxt(I18NKey.STATS_STRENGTH),
            UIStats.AGILITY to i18nTxt(I18NKey.STATS_AGILITY),
            UIStats.CONSTITUTION to i18nTxt(I18NKey.STATS_CONSTITUTION),
            UIStats.INTELLIGENCE to i18nTxt(I18NKey.STATS_INTELLIGENCE),
            UIStats.DAMAGE to i18nTxt(I18NKey.STATS_ATTACK),
            UIStats.ARMOR to i18nTxt(I18NKey.STATS_ARMOR),
            UIStats.RESISTANCE to i18nTxt(I18NKey.STATS_RESISTANCE),
        )
        shopStatsTable = shopStatsTable(skin, "", uiStatsLabels) {
            it.padLeft(10.0f).padTop(10f).top().left()
        }
        // top right -> shop options
        table(skin) { tblCell ->
            background = skin.getDrawable("dialog_frame")

            this@ShopView.optionTable = optionTable(skin) { optionTableCell ->
                optionTableCell.fill().align(Align.left)
            }

            tblCell.top().right().padTop(10f).padRight(10f).width(250f).row()
        }
        // bottom left -> scrollable items + talons
        table(skin) { tblCell ->
            background = skin.getDrawable("dialog_frame")

            table(skin) { innerTblCell ->
                val talonsLabel = this@ShopView.i18nTxt(I18NKey.STATS_TALONS)
                this@ShopView.talonLabel = label(talonsLabel, "dialog_option", skin) { lblCell ->
                    userObject = talonsLabel
                    setAlignment(Align.left)
                    lblCell.padTop(2f).padBottom(5f).expandX().left().minWidth(220f)
                }
                val totalLabel = this@ShopView.i18nTxt(I18NKey.GENERAL_TOTAL)
                this@ShopView.totalLabel = label(totalLabel, "dialog_option", skin) { lblCell ->
                    userObject = totalLabel
                    setAlignment(Align.left)
                    lblCell.padTop(2f).padBottom(5f).minWidth(220f)
                }
                innerTblCell.growX().row()
            }

            this@ShopView.itemShopTable = itemShopTable(this@ShopView.talonsPostfix, skin) { itCell ->
                isVisible = false
                itCell.grow()
            }

            tblCell.pad(10f, 10f, 10f, 20f).grow().minWidth(450f)
        }
        // bottom right -> item info
        itemInfoTable = itemInfoTable(skin) { tblCell ->
            isVisible = false
            tblCell.pad(10f, 0f, 10f, 10f).growX().top().minWidth(370f)
        }
        // confirm buy popup
        popupTable = scene2d.popupTable("", emptyList(), skin)

        registerOnPropertyChanges()
    }

    override fun registerOnPropertyChanges() {
        viewModel.onPropertyChange(ShopViewModel::playerStats) { stats ->
            val missingValue = "0"

            shopStatsTable.statsValue(UIStats.STRENGTH, stats[UIStats.STRENGTH] ?: missingValue)
            shopStatsTable.statsValue(UIStats.AGILITY, stats[UIStats.AGILITY] ?: missingValue)
            shopStatsTable.statsValue(UIStats.INTELLIGENCE, stats[UIStats.INTELLIGENCE] ?: missingValue)
            shopStatsTable.statsValue(UIStats.CONSTITUTION, stats[UIStats.CONSTITUTION] ?: missingValue)
            shopStatsTable.statsValue(UIStats.DAMAGE, stats[UIStats.DAMAGE] ?: missingValue)
            shopStatsTable.statsValue(UIStats.ARMOR, stats[UIStats.ARMOR] ?: missingValue)
            shopStatsTable.statsValue(UIStats.RESISTANCE, stats[UIStats.RESISTANCE] ?: missingValue)
        }

        viewModel.onPropertyChange(ShopViewModel::playerTalons) { value ->
            talonLabel.setText(" ${talonLabel.userObject}: $value$talonsPostfix ")
        }

        viewModel.onPropertyChange(ShopViewModel::options) { optionNames ->
            optionTable.clearOptions()
            optionNames.forEach { optionTable.option(it.second, it.first) }
        }

        viewModel.onPropertyChange(ShopViewModel::totalCost) { value ->
            totalLabel.setText(" ${totalLabel.userObject}: ${value}$talonsPostfix ")
        }

        viewModel.onPropertyChange(ShopViewModel::shopName) {
            shopStatsTable.shopName(it)
            isVisible = true
            focus = ShopViewFocus.OPTIONS
        }
    }

    private fun updateActiveItem() {
        if (activeItems.isEmpty()) {
            return
        }

        val selectedItem = activeItems[itemShopTable.selectedEntryIdx]
        itemInfoTable.item(selectedItem.name, selectedItem.description, selectedItem.image)

        val diff: Map<UIStats, Int> = viewModel.calcDiff(selectedItem)
        shopStatsTable.clearDiff()
        diff.forEach { (uiStat, diffValue) ->
            shopStatsTable.diffValue(uiStat, diffValue)
        }
    }

    override fun onUpPressed() {
        when (focus) {
            ShopViewFocus.OPTIONS -> {
                if (optionTable.prevOption()) {
                    viewModel.playSndMenuClick()
                }
            }

            ShopViewFocus.ITEMS -> {
                if (itemShopTable.prevEntry()) {
                    viewModel.playSndMenuClick()
                    updateActiveItem()
                }
            }

            ShopViewFocus.CONFIRM -> {
                if (popupTable.prevOption()) {
                    viewModel.playSndMenuClick()
                }
            }
        }
    }

    override fun onDownPressed() {
        when (focus) {
            ShopViewFocus.OPTIONS -> {
                if (optionTable.nextOption()) {
                    viewModel.playSndMenuClick()
                }
            }

            ShopViewFocus.ITEMS -> {
                if (itemShopTable.nextEntry()) {
                    viewModel.playSndMenuClick()
                    updateActiveItem()
                }
            }

            ShopViewFocus.CONFIRM -> {
                if (popupTable.nextOption()) {
                    viewModel.playSndMenuClick()
                }
            }
        }
    }

    override fun onRightPressed() {
        if (focus != ShopViewFocus.ITEMS || itemShopTable.hasNoEntries()) {
            return
        }

        val amount = viewModel.incItemAmount(itemShopTable.selectedEntryIdx)
        itemShopTable.amount(amount)
    }

    override fun onLeftPressed() {
        if (focus != ShopViewFocus.ITEMS || itemShopTable.hasNoEntries()) {
            return
        }

        val amount = viewModel.decItemAmount(itemShopTable.selectedEntryIdx)
        itemShopTable.amount(amount)
    }

    private fun selectOption() {
        val shopOption: ShopOption = optionTable.selectedUserObject()
        activeItems = when (shopOption) {
            ShopOption.WEAPON -> viewModel.shopItemsOf(ItemCategory.WEAPON)
            ShopOption.ARMOR -> viewModel.shopItemsOf(ItemCategory.ARMOR, ItemCategory.HELMET, ItemCategory.BOOTS)
            ShopOption.ACCESSORY -> viewModel.shopItemsOf(ItemCategory.ACCESSORY)
            ShopOption.OTHER -> viewModel.shopItemsOf(ItemCategory.OTHER)
            ShopOption.SELL -> viewModel.itemsToSell()
            ShopOption.QUIT -> {
                isVisible = false
                viewModel.quit()
                return
            }
        }

        focus = ShopViewFocus.ITEMS
        updateActiveItems()
    }

    private fun updateActiveItems() {
        optionTable.stopSelectAnimation()
        itemShopTable.isVisible = true
        itemInfoTable.isVisible = activeItems.isNotEmpty()
        itemShopTable.clearEntries()
        itemInfoTable.clearItem()
        activeItems.forEach { itemShopTable.item(it.name, it.cost) }
        updateActiveItem()
    }

    private fun onBuyItems() {
        if (viewModel.totalCost == 0) {
            return
        }

        viewModel.playSndMenuAccept()
        focus = ShopViewFocus.CONFIRM
        itemShopTable.stopSelectAnimation()

        if (viewModel.sellMode) {
            popupTable.update(
                i18nTxt(I18NKey.SHOP_CONFIRM_SELL),
                listOf(i18nTxt(I18NKey.GENERAL_YES), i18nTxt(I18NKey.GENERAL_NO))
            )
        } else {
            popupTable.update(
                i18nTxt(I18NKey.SHOP_CONFIRM_BUY),
                listOf(i18nTxt(I18NKey.GENERAL_YES), i18nTxt(I18NKey.GENERAL_NO))
            )
        }

        popupTable.firstOption()
        stage.addActor(popupTable)
    }

    override fun onSelectPressed() {
        when (focus) {
            ShopViewFocus.OPTIONS -> {
                selectOption()
                viewModel.playSndMenuAccept()
            }

            ShopViewFocus.ITEMS -> onBuyItems()
            ShopViewFocus.CONFIRM -> {
                closeConfirmPopup()
                if (popupTable.selectedOption == 0) {
                    // item buy/sell confirmed
                    viewModel.playSndMenuAccept()
                    viewModel.buyOrSellItems()
                    itemShopTable.clearAmounts()
                    if (viewModel.sellMode) {
                        activeItems = viewModel.itemsToSell()
                        updateActiveItems()
                    }
                    return
                }
                viewModel.playSndMenuAbort()
            }
        }
    }

    override fun onBackPressed() {
        when (focus) {
            ShopViewFocus.OPTIONS -> {
                // select 'Quit' option
                if (optionTable.lastOption()) {
                    viewModel.playSndMenuClick()
                }
            }

            ShopViewFocus.ITEMS -> {
                focus = ShopViewFocus.OPTIONS
                itemShopTable.clearEntries()
                itemShopTable.isVisible = false
                itemInfoTable.isVisible = false
                optionTable.resumeSelectAnimation()
                viewModel.playSndMenuAbort()
                shopStatsTable.clearDiff()
            }

            ShopViewFocus.CONFIRM -> {
                closeConfirmPopup()
                viewModel.playSndMenuAbort()
            }
        }
    }

    private fun closeConfirmPopup() {
        focus = ShopViewFocus.ITEMS
        popupTable.remove()
        itemShopTable.resumeSelectAnimation()
    }
}

@Scene2dDsl
fun <S> KWidget<S>.shopView(
    model: ShopViewModel,
    skin: Skin,
    init: (@Scene2dDsl ShopView).(S) -> Unit = {},
): ShopView = actor(ShopView(model, skin), init)
