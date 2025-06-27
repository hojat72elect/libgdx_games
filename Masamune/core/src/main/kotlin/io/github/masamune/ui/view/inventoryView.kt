package io.github.masamune.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import io.github.masamune.tiledmap.ItemCategory
import io.github.masamune.tiledmap.ItemType
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.InventoryViewModel
import io.github.masamune.ui.model.ItemModel
import io.github.masamune.ui.model.UIStats
import io.github.masamune.ui.widget.EquipmentStatsTable
import io.github.masamune.ui.widget.ItemInfoTable
import io.github.masamune.ui.widget.ItemInventoryTable
import io.github.masamune.ui.widget.OptionTable
import io.github.masamune.ui.widget.equipmentStatsTable
import io.github.masamune.ui.widget.itemInfoTable
import io.github.masamune.ui.widget.itemInventoryTable
import io.github.masamune.ui.widget.optionTable
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.table

private enum class InventoryUiState {
    SELECT_OPTION, ITEMS, SELECT_EQUIPMENT, EQUIPMENT
}

@Scene2dDsl
class InventoryView(
    model: InventoryViewModel,
    skin: Skin,
) : View<InventoryViewModel>(skin, model), KTable {

    private val equipmentStatsTable: EquipmentStatsTable
    private val optionTable: OptionTable
    private val itemTable: ItemInventoryTable
    private val itemInfoTable: ItemInfoTable

    private var state = InventoryUiState.SELECT_OPTION
    private var playerEquipment: Map<ItemCategory, ItemModel> = emptyMap()
    private var equipmentItems: List<ItemModel> = emptyList()
    private var inventoryItems: List<ItemModel> = emptyList()
    private var activeCategory: ItemCategory = ItemCategory.OTHER

    private val activeEquipmentItems: List<ItemModel>
        // type UNDEFINED = special unequip item
        get() = equipmentItems.filter { it.category == activeCategory }

    init {
        background = skin.getDrawable("dialog_frame")
        setFillParent(true)

        val uiStatsLabels = mapOf(
            UIStats.LIFE to i18nTxt(I18NKey.STATS_LIFE),
            UIStats.MANA to i18nTxt(I18NKey.STATS_MANA),
            UIStats.STRENGTH to i18nTxt(I18NKey.STATS_STRENGTH),
            UIStats.AGILITY to i18nTxt(I18NKey.STATS_AGILITY),
            UIStats.CONSTITUTION to i18nTxt(I18NKey.STATS_CONSTITUTION),
            UIStats.INTELLIGENCE to i18nTxt(I18NKey.STATS_INTELLIGENCE),
            UIStats.DAMAGE to i18nTxt(I18NKey.STATS_ATTACK),
            UIStats.ARMOR to i18nTxt(I18NKey.STATS_ARMOR),
            UIStats.RESISTANCE to i18nTxt(I18NKey.STATS_RESISTANCE),
        )
        val uiEquipmentLabels = mapOf(
            ItemCategory.WEAPON to i18nTxt(I18NKey.ITEM_CATEGORY_WEAPON),
            ItemCategory.ARMOR to i18nTxt(I18NKey.ITEM_CATEGORY_ARMOR),
            ItemCategory.HELMET to i18nTxt(I18NKey.ITEM_CATEGORY_HELMET),
            ItemCategory.BOOTS to i18nTxt(I18NKey.ITEM_CATEGORY_BOOTS),
            ItemCategory.ACCESSORY to i18nTxt(I18NKey.ITEM_CATEGORY_ACCESSORY),
        )
        val options = listOf(
            i18nTxt(I18NKey.MENU_OPTION_ITEM),
            i18nTxt(I18NKey.MENU_OPTION_EQUIPMENT),
            i18nTxt(I18NKey.MENU_OPTION_QUIT),
        )

        // top left -> title, equipment and stats
        equipmentStatsTable = equipmentStatsTable(skin, "", uiEquipmentLabels, uiStatsLabels) {
            it.growX().align(Align.topLeft)
        }

        // top right -> inventory options
        table(skin) { tblCell ->
            background = skin.getDrawable("dialog_frame")

            this@InventoryView.optionTable = optionTable(skin, options)

            tblCell.top().fillX().width(250f).row()
        }

        table(skin) { outerTblCell ->
            this.align(Align.topLeft)

            // bottom left -> scrollable items + talons
            table(skin) { tblCell ->
                background = skin.getDrawable("dialog_frame")

                this@InventoryView.itemTable = itemInventoryTable(skin) {
                    isVisible = false
                    it.grow()
                }

                tblCell.growY().left().top().minWidth(450f)
            }

            // bottom right -> item info
            this@InventoryView.itemInfoTable = itemInfoTable(skin) { tblCell ->
                isVisible = false
                tblCell.expandX().top().right().width(370f)
            }

            outerTblCell.colspan(2).pad(20f, 10f, 10f, 10f).grow().top().left()
        }

        registerOnPropertyChanges()
    }

    override fun registerOnPropertyChanges() {
        viewModel.onPropertyChange(InventoryViewModel::playerName) { name ->
            equipmentStatsTable.playerName(name)
            isVisible = name.isNotBlank()
            optionTable.firstOption()
        }
        viewModel.onPropertyChange(InventoryViewModel::playerStats) { stats ->
            val life = stats.zeroIfMissing(UIStats.LIFE)
            val lifeMax = stats.zeroIfMissing(UIStats.LIFE_MAX)
            equipmentStatsTable.statsValue(UIStats.LIFE, "$life / $lifeMax")

            val mana = stats.zeroIfMissing(UIStats.MANA)
            val manaMax = stats.zeroIfMissing(UIStats.MANA_MAX)
            equipmentStatsTable.statsValue(UIStats.MANA, "$mana / $manaMax")

            equipmentStatsTable.statsValue(UIStats.STRENGTH, stats.zeroIfMissing(UIStats.STRENGTH))
            equipmentStatsTable.statsValue(UIStats.AGILITY, stats.zeroIfMissing(UIStats.AGILITY))
            equipmentStatsTable.statsValue(UIStats.CONSTITUTION, stats.zeroIfMissing(UIStats.CONSTITUTION))
            equipmentStatsTable.statsValue(UIStats.INTELLIGENCE, stats.zeroIfMissing(UIStats.INTELLIGENCE))
            equipmentStatsTable.statsValue(UIStats.DAMAGE, stats.zeroIfMissing(UIStats.DAMAGE))
            equipmentStatsTable.statsValue(UIStats.ARMOR, stats.zeroIfMissing(UIStats.ARMOR))
            equipmentStatsTable.statsValue(UIStats.RESISTANCE, stats.zeroIfMissing(UIStats.RESISTANCE))
        }
        viewModel.onPropertyChange(InventoryViewModel::playerEquipment) {
            playerEquipment = it
            equipmentStatsTable.clearEquipment()
            playerEquipment.forEach { (category, item) ->
                equipmentStatsTable.equipmentName(category, item.name)
            }
        }
        viewModel.onPropertyChange(InventoryViewModel::equipmentItems) { newItems ->
            equipmentItems = newItems
            if (state == InventoryUiState.EQUIPMENT) {
                val items = activeEquipmentItems
                updateItemTableAndInfo(items, itemTable.selectedEntryIdx)
                equipmentStatsTable.clearDiff()
                if (itemTable.numEntries == 0) {
                    return@onPropertyChange
                }

                val item = items[itemTable.selectedEntryIdx]
                val diff: Map<UIStats, Int> = viewModel.calcDiff(item)
                diff.forEach { (uiStat, diffValue) ->
                    equipmentStatsTable.diffValue(uiStat, diffValue)
                }
            }
        }
        viewModel.onPropertyChange(InventoryViewModel::inventoryItems) {
            inventoryItems = it
            if (state == InventoryUiState.ITEMS) {
                updateItemTableAndInfo(inventoryItems, itemTable.selectedEntryIdx)
            }
        }

    }

    override fun onUpPressed() {
        when (state) {
            InventoryUiState.SELECT_OPTION -> {
                if (optionTable.prevOption()) {
                    viewModel.playSndMenuClick()
                }
            }

            InventoryUiState.ITEMS -> {
                if (itemTable.prevEntry()) {
                    val item = inventoryItems[itemTable.selectedEntryIdx]
                    itemInfoTable.item(item.name, item.description, item.image)
                    viewModel.playSndMenuClick()
                }
            }

            InventoryUiState.SELECT_EQUIPMENT -> {
                if (equipmentStatsTable.prevEquipment()) {
                    selectEquipment(equipmentStatsTable.selectedCategory())
                }
            }

            InventoryUiState.EQUIPMENT -> {
                if (itemTable.prevEntry()) {
                    updateEquipment()
                }
            }
        }
    }

    override fun onDownPressed() {
        when (state) {
            InventoryUiState.SELECT_OPTION -> {
                if (optionTable.nextOption()) {
                    viewModel.playSndMenuClick()
                }
            }

            InventoryUiState.ITEMS -> {
                if (itemTable.nextEntry()) {
                    val item = inventoryItems[itemTable.selectedEntryIdx]
                    itemInfoTable.item(item.name, item.description, item.image)
                    viewModel.playSndMenuClick()
                }
            }

            InventoryUiState.SELECT_EQUIPMENT -> {
                if (equipmentStatsTable.nextEquipment()) {
                    selectEquipment(equipmentStatsTable.selectedCategory())
                }
            }

            InventoryUiState.EQUIPMENT -> {
                if (itemTable.nextEntry()) {
                    updateEquipment()
                }
            }
        }
    }

    private fun updateEquipment() {
        viewModel.playSndMenuClick()
        equipmentStatsTable.clearDiff()
        val item = activeEquipmentItems[itemTable.selectedEntryIdx]
        // special clear equipment item -> don't show it in info table and don't calculate diff
        itemInfoTable.isVisible = item.type != ItemType.UNDEFINED
        itemInfoTable.item(item.name, item.description, item.image)
        val diff: Map<UIStats, Int> = viewModel.calcDiff(item)
        diff.forEach { (uiStat, diffValue) ->
            equipmentStatsTable.diffValue(uiStat, diffValue)
        }
    }

    override fun onSelectPressed() {
        when (state) {
            InventoryUiState.SELECT_OPTION -> {
                when (optionTable.selectedOption) {
                    0 -> onSelectItems()
                    1 -> onSelectEquipment()
                    else -> viewModel.quit()
                }
            }

            InventoryUiState.ITEMS -> {
                if (itemTable.hasEntries()) {
                    viewModel.consume(inventoryItems[itemTable.selectedEntryIdx])
                }
            }
            InventoryUiState.EQUIPMENT -> {
                viewModel.equip(activeEquipmentItems[itemTable.selectedEntryIdx])
                viewModel.playSndMenuAccept()
            }

            InventoryUiState.SELECT_EQUIPMENT -> {
                onSelectEquipment(equipmentStatsTable.selectedCategory())
            }
        }
    }

    private fun onSelectItems() {
        state = InventoryUiState.ITEMS
        optionTable.stopSelectAnimation()

        equipmentStatsTable.title(i18nTxt(I18NKey.MENU_OPTION_ITEM))
        // update item table and item info table
        updateItemTableAndInfo(inventoryItems)

        viewModel.playSndMenuAccept()
    }

    private fun onSelectEquipment() {
        state = InventoryUiState.SELECT_EQUIPMENT
        equipmentStatsTable.title(i18nTxt(I18NKey.MENU_OPTION_EQUIPMENT))
        // update equipment info
        equipmentStatsTable.showEquipment(true)
        equipmentStatsTable.selectEquipment(ItemCategory.WEAPON)
        selectEquipment(ItemCategory.WEAPON, playSound = false)
        viewModel.playSndMenuAccept()
    }

    private fun selectEquipment(category: ItemCategory, playSound: Boolean = true) {
        val equippedItem = playerEquipment[category]
        itemInfoTable.isVisible = equippedItem != null
        if (equippedItem != null) {
            itemInfoTable.item(equippedItem.name, equippedItem.description, equippedItem.image)
        }
        if (playSound) {
            viewModel.playSndMenuClick()
        }
    }


    private fun onSelectEquipment(category: ItemCategory) {
        state = InventoryUiState.EQUIPMENT
        equipmentStatsTable.stopEquipmentSelectAnimation()

        // update item table and item info table
        activeCategory = category
        updateItemTableAndInfo(activeEquipmentItems)

        // update diff
        updateEquipment()

        viewModel.playSndMenuAccept()
    }

    private fun updateItemTableAndInfo(items: List<ItemModel>, idx: Int = -1) {
        // update item table
        itemTable.clearEntries()
        items.forEach { item ->
            val invItem = itemTable.item(item.name, item.amount)
            if (item.type == ItemType.UNDEFINED) {
                // don't show amount for special unequip item
                invItem.hideAmount()
            }
        }
        if (idx == -1) {
            itemTable.selectFirstEntry()
        } else {
            itemTable.selectEntry(idx)
        }
        itemTable.isVisible = true

        // update item info table
        if (itemTable.hasEntries()) {
            val item = items[itemTable.selectedEntryIdx]
            if (item.type == ItemType.UNDEFINED) {
                // ignore special clear equipment item
                itemInfoTable.isVisible = false
                return
            }

            itemInfoTable.isVisible = true
            itemInfoTable.clearItem()
            itemInfoTable.item(item.name, item.description, item.image)
        } else {
            itemInfoTable.clearItem()
            itemInfoTable.isVisible = false
        }
    }

    override fun onBackPressed() {
        if (state == InventoryUiState.EQUIPMENT) {
            state = InventoryUiState.SELECT_EQUIPMENT
            equipmentStatsTable.resumeEquipmentSelectAnimation()
            itemTable.clearEntries()
            itemTable.isVisible = false
            itemInfoTable.isVisible = false
            selectEquipment(equipmentStatsTable.selectedCategory())
            viewModel.playSndMenuAbort()
            equipmentStatsTable.clearDiff()
            return
        }

        if (state == InventoryUiState.SELECT_EQUIPMENT || state == InventoryUiState.ITEMS) {
            itemTable.clearEntries()
            itemInfoTable.clearItem()
            itemTable.isVisible = false
            itemInfoTable.isVisible = false
            state = InventoryUiState.SELECT_OPTION
            viewModel.playSndMenuAbort()
            optionTable.resumeSelectAnimation()
            equipmentStatsTable.clearDiff()
            equipmentStatsTable.title("")
            equipmentStatsTable.showEquipment(false)
            return
        }

        if (optionTable.lastOption()) {
            viewModel.playSndMenuAbort()
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.inventoryView(
    model: InventoryViewModel,
    skin: Skin,
    init: (@Scene2dDsl InventoryView).(S) -> Unit = {},
): InventoryView = actor(InventoryView(model, skin), init)
