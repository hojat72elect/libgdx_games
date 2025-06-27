package io.github.masamune.ui.model

import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.World
import io.github.masamune.audio.AudioService
import io.github.masamune.component.Equipment
import io.github.masamune.component.Inventory
import io.github.masamune.component.Item
import io.github.masamune.component.Name
import io.github.masamune.component.Player
import io.github.masamune.component.CharacterStats
import io.github.masamune.consumeItem
import io.github.masamune.equipItem
import io.github.masamune.event.Event
import io.github.masamune.event.EventService
import io.github.masamune.event.MenuBeginEvent
import io.github.masamune.event.MenuEndEvent
import io.github.masamune.removeEquipment
import io.github.masamune.removeItem
import io.github.masamune.tiledmap.ItemCategory
import io.github.masamune.tiledmap.ItemType

class InventoryViewModel(
    bundle: I18NBundle,
    audioService: AudioService,
    private val world: World,
    private val eventService: EventService,
) : ViewModel(bundle, audioService) {

    private val playerEntities = world.family { all(Player) }
    private val emptyItems = listOf(
        emptyItemModel(ItemCategory.WEAPON, "<< ${i18nTxt(I18NKey.MENU_OPTION_CLEAR_ITEM)} >>"),
        emptyItemModel(ItemCategory.ARMOR, "<< ${i18nTxt(I18NKey.MENU_OPTION_CLEAR_ITEM)} >>"),
        emptyItemModel(ItemCategory.HELMET, "<< ${i18nTxt(I18NKey.MENU_OPTION_CLEAR_ITEM)} >>"),
        emptyItemModel(ItemCategory.BOOTS, "<< ${i18nTxt(I18NKey.MENU_OPTION_CLEAR_ITEM)} >>"),
        emptyItemModel(ItemCategory.ACCESSORY, "<< ${i18nTxt(I18NKey.MENU_OPTION_CLEAR_ITEM)} >>"),
    )

    // view properties
    var playerName by propertyNotify("")
    var playerStats: Map<UIStats, String> by propertyNotify(emptyMap())
    var playerEquipment: Map<ItemCategory, ItemModel> by propertyNotify(emptyMap())
    var equipmentItems: List<ItemModel> by propertyNotify(emptyList())
    var inventoryItems: List<ItemModel> by propertyNotify(emptyList())

    override fun onEvent(event: Event) {
        if (event !is MenuBeginEvent || event.type != MenuType.INVENTORY) {
            return
        }

        with(world) {
            val player = playerEntities.first()
            playerName = player[Name].name

            // calculate equipment bonus before setting player stats because StatsView reacts to playerStats change
            val equipmentCmp = player[Equipment]
            updatePlayerEquipment(equipmentCmp)
            val itemPartition = player[Inventory].items.partition { it[Item].category.isEquipment }
            inventoryItems = itemPartition.second.map { it.toItemModel(world, withConsumeInfo = true) }
            val playerEquipmentItems = itemPartition.first.map { it.toItemModel(world) }
            equipmentItems = emptyItems + playerEquipmentItems

            playerStats = uiMapOf(player[CharacterStats])
        }
    }

    private fun updatePlayerEquipment(equipmentCmp: Equipment) {
        playerEquipment = equipmentCmp.items.map {
            val itemModel = it.toItemModel(world)
            itemModel.category to itemModel
        }.toMap()
    }

    fun quit() {
        eventService.fire(MenuEndEvent)
        eventService.fire(MenuBeginEvent(MenuType.GAME))
        // setting player name to blank will hide the inventory view again
        playerName = ""
    }

    fun calcDiff(itemModel: ItemModel): Map<UIStats, Int> {
        return playerEntities.first().calcEquipmentDiff(itemModel, world)
    }

    fun consume(itemModel: ItemModel) = with(world) {
        val playerEntity = playerEntities.first()
        if (!itemModel.consumable) {
            return@with
        }

        val inventoryCmp = playerEntity[Inventory]
        val itemEntity = inventoryCmp.items.single { it[Item].type == itemModel.type }
        world.consumeItem(itemEntity, playerEntity)
        playerStats = uiMapOf(playerEntity[CharacterStats])
        inventoryItems = inventoryCmp.items
            .filter { !it[Item].category.isEquipment }
            .map { it.toItemModel(world, withConsumeInfo = true) }
    }

    fun equip(itemModel: ItemModel) = with(world) {
        val playerEntity = playerEntities.first()
        val inventoryCmp = playerEntity[Inventory]
        if (itemModel.type == ItemType.UNDEFINED) {
            // special unequip item
            world.removeEquipment(itemModel.category, playerEntity)
        } else {
            // equip item (move from inventory to equipment component)
            val itemEntity = inventoryCmp.items.single { it[Item].type == itemModel.type }
            world.equipItem(itemEntity, playerEntity)
            // do NOT remove the item entity because it still exists. It just got moved to the equipment component items
            world.removeItem(itemModel.type, 1, playerEntity, removeEntity = false)
        }

        // update equipment ItemModel
        val equipmentCmp = playerEntity[Equipment]
        updatePlayerEquipment(equipmentCmp)
        playerStats = uiMapOf(playerEntity[CharacterStats])
        equipmentItems = emptyItems + inventoryCmp.items
            .filter { it[Item].category.isEquipment }
            .map { it.toItemModel(world) }
    }

}
