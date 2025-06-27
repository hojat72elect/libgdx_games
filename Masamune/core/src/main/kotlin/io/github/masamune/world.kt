package io.github.masamune

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.Masamune.Companion.UNIT_SCALE
import io.github.masamune.asset.AtlasAsset
import io.github.masamune.asset.CachingAtlas
import io.github.masamune.asset.SoundAsset
import io.github.masamune.audio.AudioService
import io.github.masamune.combat.ActionExecutorService
import io.github.masamune.combat.action.Action
import io.github.masamune.component.Animation
import io.github.masamune.component.CharacterStats
import io.github.masamune.component.Equipment
import io.github.masamune.component.Graphic
import io.github.masamune.component.Inventory
import io.github.masamune.component.Item
import io.github.masamune.component.ItemStats
import io.github.masamune.component.Move
import io.github.masamune.component.Physic
import io.github.masamune.component.Player
import io.github.masamune.component.QuestLog
import io.github.masamune.component.Remove
import io.github.masamune.component.Selector
import io.github.masamune.component.Tiled
import io.github.masamune.component.Transform
import io.github.masamune.quest.Quest
import io.github.masamune.tiledmap.AnimationType
import io.github.masamune.tiledmap.ItemCategory
import io.github.masamune.tiledmap.ItemType
import ktx.app.gdxError
import ktx.log.Logger
import ktx.math.vec2
import ktx.math.vec3
import kotlin.math.max
import kotlin.math.min

private val log = Logger("World")

const val SELECTOR_SPEED = 1.5f
const val SELECTOR_SCALE = 1.2f

fun World.selectorEntity(target: Entity, confirmed: Boolean) = this.entity {
    // position and size don't matter because they get updated in the SelectorSystem
    val atlas = inject<CachingAtlas>(AtlasAsset.CHARS_AND_PROPS.name)
    it += Transform(vec3(), vec2(), SELECTOR_SCALE)
    val animationCmp = Animation.ofAtlas(atlas, "select", AnimationType.IDLE, speed = SELECTOR_SPEED)
    it += animationCmp
    it += Graphic(animationCmp.gdxAnimation.getKeyFrame(0f))
    it += Selector(target, confirmed)
}

fun World.addItem(
    itemEntity: Entity,
    to: Entity,
    equipItem: Boolean,
) {
    val itemCmp = itemEntity[Item]
    val items = to[Inventory].items
    val existingItem = items.firstOrNull { it[Item].type == itemCmp.type }
    if (existingItem != null) {
        // item already in inventory -> increase amount
        log.debug { "Increasing amount of item ${itemCmp.type} by ${itemCmp.amount}" }
        existingItem[Item].amount += itemCmp.amount
        return
    }

    // if item is an equipment and 'to' entity is a player then equip the item instead of
    // adding it to the inventory, if no item of that type is equipped yet
    if (equipItem && itemCmp.category.isEquipment && to has Player) {
        val equipmentItems = to[Equipment].items
        if (equipmentItems.none { it[Item].category == itemCmp.category }) {
            equipItem(itemEntity, to)
            return
        }
    }

    // item not yet in inventory -> add it
    log.debug { "Adding new item of type ${itemCmp.type} to inventory" }
    // in case equipment is removed and added to inventory, it will have an amount of 0 -> increase it to 1
    itemCmp.amount = max(1, itemCmp.amount)
    items += itemEntity
}

fun World.equipItem(itemEntity: Entity, to: Entity) {
    val itemCmp = itemEntity[Item]
    val equipmentCmp = to[Equipment]
    val equipmentItems = equipmentCmp.items
    val toStats = to[CharacterStats]
    log.debug { "Equipping item ${itemCmp.type}" }

    // remove currently equipped item, if there is any
    removeEquipment(itemCmp.category, to)

    // equip item (= add to equipment and modify stats)
    equipmentItems += itemEntity
    toStats += itemEntity[ItemStats]
    to.getOrNull(Move)?.let { toMove ->
        toMove.speed += itemEntity.getOrNull(Move)?.speed ?: 0f
    }
}

fun World.removeEquipment(category: ItemCategory, from: Entity) {
    val equipmentCmp = from[Equipment]
    val equipmentItems = equipmentCmp.items
    log.debug { "Removing equipment $category from $from" }

    // remove currently equipped item, if there is any (=remove from equipment and modify stats)
    equipmentItems.singleOrNull { it[Item].category == category }?.let { existingItem ->
        equipmentItems -= existingItem
        from[CharacterStats] -= existingItem[ItemStats]
        from.getOrNull(Move)?.let { fromMove ->
            fromMove.speed -= existingItem.getOrNull(Move)?.speed ?: 0f
        }

        // move item to inventory
        addItem(existingItem, from, equipItem = false)
    }
}

// gets called in inventory view when a consumable item is consumed
fun World.consumeItem(item: Entity, consumer: Entity) {
    val itemCmp = item[Item]
    val itemStats = item[ItemStats]
    val consumerStats = consumer[CharacterStats]

    consumerStats += itemStats
    if (itemStats.life > 0f || itemStats.mana > 0f) {
        inject<AudioService>().play(SoundAsset.HEAL1)
    } else {
        inject<AudioService>().play(SoundAsset.CONSUME)
    }

    removeItem(itemCmp.type, 1, consumer, removeEntity = true)
}

fun World.removeItem(type: ItemType, amount: Int, from: Entity, removeEntity: Boolean = true): Boolean {
    val items = from[Inventory].items
    val existingItem = items.firstOrNull { it[Item].type == type }
    if (existingItem == null) {
        gdxError("Cannot remove a non-existing item of type $type")
    }

    log.debug { "Decreasing amount of item $type by $amount" }
    existingItem[Item].amount -= amount
    if (existingItem[Item].amount <= 0) {
        log.debug { "Removing item of type $type from inventory" }
        items -= existingItem
        if (removeEntity) {
            existingItem.remove()
        }
        return true
    }

    return false
}

fun World.removeItem(item: Entity, from: Entity) {
    val items = from[Inventory].items
    items -= item
    item.remove()
}

fun World.isEntityDead(entity: Entity): Boolean = with(this) {
    return entity !in this || entity[CharacterStats].life < 1f
}

fun World.isEntityAlive(entity: Entity): Boolean = with(this) {
    return entity in this && entity[CharacterStats].life >= 1f
}

fun World.isEntityHurt(entity: Entity): Boolean {
    return this.isEntityAlive(entity) && entity[CharacterStats].life < (entity[CharacterStats].lifeMax - 1f)
}

fun World.canPerformAction(entity: Entity, action: Action): Boolean {
    val actionExecutorService = inject<ActionExecutorService>()
    return action.run { actionExecutorService.canPerform(entity) }
}

inline fun <reified T : Quest> World.hasQuest(entity: Entity): Boolean {
    return entity[QuestLog].getOrNull<T>() != null
}

fun World.hasItem(entity: Entity, itemType: ItemType): Boolean {
    return entity[Inventory].items.any { item -> item[Item].type == itemType }
}

fun World.spawnSfx(target: Entity, sfxAtlasKey: String, duration: Float, scale: Float = 1f) {
    val (toPos, toSize, toScale) = target[Transform]
    val sfxAtlas = inject<CachingAtlas>(AtlasAsset.SFX.name)

    val maxSize = 1.5f
    val diffMaxX = toSize.x - maxSize
    val diffMaxY = toSize.y - maxSize

    entity {
        val sfxPosition = toPos.cpy()
        sfxPosition.add(max(0f, diffMaxX) * 0.5f, max(0f, diffMaxY) * 0.5f, 3f)
        val sfxSize = vec2(min(maxSize, toSize.x), min(maxSize, toSize.y))
        it += Transform(sfxPosition, sfxSize, toScale * scale)

        val animation = Animation.ofAtlas(sfxAtlas, sfxAtlasKey, AnimationType.IDLE)
        animation.speed = 1f / (duration / animation.gdxAnimation.animationDuration)
        animation.playMode = com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL
        it += animation
        it += Graphic(animation.gdxAnimation.getKeyFrame(0f))
        it += Remove(duration)
    }
}

fun World.spawnSfx(sfxAtlasKey: String, location: Vector2, duration: Float, scale: Float = 1f) {
    val sfxAtlas = inject<CachingAtlas>(AtlasAsset.SFX.name)
    val animation = Animation.ofAtlas(sfxAtlas, sfxAtlasKey, AnimationType.IDLE)
    val keyFrame = animation.gdxAnimation.getKeyFrame(0f, false)
    val size = vec2(keyFrame.regionWidth.toFloat(), keyFrame.regionHeight.toFloat()).scl(UNIT_SCALE)

    entity {
        it += Transform(vec3(location, z = 3f), size, scale)
        animation.speed = 1f / (duration / animation.gdxAnimation.animationDuration)
        animation.playMode = com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL
        it += animation
        it += Graphic(animation.gdxAnimation.getKeyFrame(0f))
        it += Remove(duration)
    }
}

fun World.getEntityByTiledId(tiledId: Int): Entity {
    return this.family { all(Tiled) }.firstOrNull { it[Tiled].id == tiledId } ?: Entity.NONE
}

fun World.getEntityByTiledIdOrNull(tiledId: Int): Entity? {
    return this.family { all(Tiled) }.firstOrNull { it[Tiled].id == tiledId }
}

fun World.teleportEntity(entity: Entity, to: Vector2) {
    entity.getOrNull(Physic)?.let { physic ->
        physic.body.setTransform(to.x, to.y, physic.body.angle)
        physic.prevPosition.set(to)
    }
    entity[Transform].position.run {
        this.x = to.x
        this.y = to.y
    }
}
