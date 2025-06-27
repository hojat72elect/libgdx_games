package io.github.masamune.ui.model

import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.addItem
import io.github.masamune.audio.AudioService
import io.github.masamune.component.Inventory
import io.github.masamune.component.Item
import io.github.masamune.component.Name
import io.github.masamune.component.CharacterStats
import io.github.masamune.event.Event
import io.github.masamune.event.EventService
import io.github.masamune.event.ShopBeginEvent
import io.github.masamune.event.ShopEndEvent
import io.github.masamune.removeItem
import io.github.masamune.tiledmap.ItemCategory
import io.github.masamune.tiledmap.TiledService
import ktx.log.logger

class ShopViewModel(
    bundle: I18NBundle,
    audioService: AudioService,
    private val world: World,
    private val tiledService: TiledService,
    private val eventService: EventService = tiledService.eventService,
) : ViewModel(bundle, audioService) {

    private val weaponOption = ShopOption.WEAPON to bundle["menu.option.weapon"]
    private val armorOption = ShopOption.ARMOR to bundle["menu.option.armor"]
    private val accessoryOption = ShopOption.ACCESSORY to bundle["menu.option.accessory"]
    private val otherOption = ShopOption.OTHER to bundle["menu.option.item"]
    private val sellOption = ShopOption.SELL to bundle["menu.option.sell"]
    private val quitOption = ShopOption.QUIT to bundle["menu.option.quit"]

    // pair-left = localized text for UIStat, pair-right = value
    var playerStats: Map<UIStats, String> by propertyNotify(emptyMap())
    private var shopItems: Map<ItemCategory, List<ItemModel>> by propertyNotify(emptyMap())
    var options: List<Pair<ShopOption, String>> by propertyNotify(emptyList())
    var playerTalons: Int by propertyNotify(0)
    var totalCost: Int by propertyNotify(0)
    var shopName: String by propertyNotify("")
    private var activeItems: List<ItemModel> = emptyList()
    private var playerEntity = Entity.NONE
    private var shopEntity = Entity.NONE
    var sellMode = false
        private set

    override fun onEvent(event: Event) {
        if (event !is ShopBeginEvent) {
            return
        }

        with(event.world) {
            // get player stats (this will update the ShopView)
            playerEntity = event.player
            playerStats = uiMapOf(playerEntity[CharacterStats])
            playerTalons = playerEntity[Inventory].talons

            // get shop items
            shopEntity = event.shop
            shopName = bundle[shopEntity[Name].name]
            shopItems = shopEntity[Inventory].items.groupBy(
                // group by item category
                { itemEntity -> itemEntity[Item].category }
            ) { it.toItemModel(world) }

            // get shop options like weapons, sell, etc. (this will update the ShopView)
            val optWeaponOption = if (shopItems.hasWeapon()) weaponOption else null
            val optArmorOption = if (shopItems.hasArmor()) armorOption else null
            val optAccessoryOption = if (shopItems.hasAccessory()) accessoryOption else null
            val optOtherOption = if (shopItems.hasOther()) otherOption else null
            options = listOfNotNull(
                optWeaponOption,
                optArmorOption,
                optAccessoryOption,
                optOtherOption,
                sellOption,
                quitOption
            )

            // reset total cost
            totalCost = 0
        }
    }

    private fun Map<ItemCategory, List<ItemModel>>.hasWeapon(): Boolean {
        return ItemCategory.WEAPON in this
    }

    private fun Map<ItemCategory, List<ItemModel>>.hasArmor(): Boolean {
        return ItemCategory.ARMOR in this || ItemCategory.HELMET in this || ItemCategory.BOOTS in this
    }

    private fun Map<ItemCategory, List<ItemModel>>.hasAccessory(): Boolean {
        return ItemCategory.ACCESSORY in this
    }

    private fun Map<ItemCategory, List<ItemModel>>.hasOther(): Boolean {
        return ItemCategory.OTHER in this
    }

    fun shopItemsOf(vararg categories: ItemCategory): List<ItemModel> {
        activeItems = shopItems.filter { it.key in categories }.values.flatten()
        activeItems.forEach { it.selected = 0 }
        totalCost = 0
        sellMode = false
        return activeItems
    }

    fun incItemAmount(itemIdx: Int): Int = with(world) {
        val selectedItem = activeItems[itemIdx]
        if (sellMode) {
            if (selectedItem.selected < selectedItem.amount) {
                playSndMenuClick()
                totalCost += selectedItem.cost
                ++selectedItem.selected
            }
            return@with selectedItem.selected
        }

        val talons = playerEntity[Inventory].talons
        if (talons < totalCost + selectedItem.cost) {
            // not enough talons to buy it
            return@with selectedItem.selected
        }

        playSndMenuClick()
        totalCost += selectedItem.cost
        return ++selectedItem.selected
    }

    fun decItemAmount(itemIdx: Int): Int {
        val selectedItem = activeItems[itemIdx]
        if (selectedItem.selected == 0) {
            // cannot go below zero amount
            return 0
        }

        playSndMenuClick()
        totalCost -= selectedItem.cost
        return --selectedItem.selected
    }

    fun itemsToSell(): List<ItemModel> = with(world) {
        activeItems = playerEntity[Inventory].items.map { it.toItemModel(world) }
        activeItems.forEach { it.selected = 0 }
        totalCost = 0
        sellMode = true
        return activeItems
    }

    private fun buyItems() = with(world) {
        // reduce player talons
        val inventoryCmp = playerEntity[Inventory]
        inventoryCmp.talons -= totalCost
        totalCost = 0
        playerTalons = inventoryCmp.talons

        // add items to inventory
        val itemsToBuy = activeItems.filter { it.selected > 0 }
        itemsToBuy.forEach { itemToBuy ->
            log.debug { "Adding item of type: ${itemToBuy.type}" }
            val itemToAdd = tiledService.loadItem(world, itemToBuy.type)
            itemToAdd[Item].amount = itemToBuy.selected
            world.addItem(itemToAdd, playerEntity, false)
        }
        log.debug { "New inventory:\n${inventoryCmp.items.map { it[Item] }.joinToString("\n")}" }

        // reset selected amounts of UI items
        activeItems.forEach { it.selected = 0 }
    }

    private fun sellItems() = with(world) {
        // increase player talons
        val inventoryCmp = playerEntity[Inventory]
        inventoryCmp.talons += totalCost
        totalCost = 0
        playerTalons = inventoryCmp.talons

        // remove items from inventory
        val itemsToSell = activeItems.filter { it.selected > 0 }
        itemsToSell.forEach { itemToSell ->
            log.debug { "Removing item of type: ${itemToSell.type}" }
            world.removeItem(itemToSell.type, itemToSell.selected, playerEntity)
        }
        log.debug { "New inventory:\n${inventoryCmp.items.map { it[Item] }.joinToString("\n")}" }
    }

    fun buyOrSellItems() = with(world) {
        if (sellMode) {
            sellItems()
            return@with
        }

        buyItems()
    }

    fun quit() {
        eventService.fire(ShopEndEvent)
        // cleanup item entities of the shop to free the entities properly
        with(world) {
            val (items) = shopEntity[Inventory]
            items.forEach { it.remove() }
            items.clear()
        }
    }

    fun calcDiff(selectedItem: ItemModel): Map<UIStats, Int> {
        return playerEntity.calcEquipmentDiff(selectedItem, world)
    }

    companion object {
        private val log = logger<ShopViewModel>()
    }
}
