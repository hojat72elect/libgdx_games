package io.github.masamune.ui.model

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.asset.SoundAsset
import io.github.masamune.audio.AudioService
import io.github.masamune.component.CharacterStats
import io.github.masamune.component.Equipment
import io.github.masamune.component.Experience
import io.github.masamune.component.Graphic
import io.github.masamune.component.Inventory
import io.github.masamune.component.Item
import io.github.masamune.component.ItemStats
import io.github.masamune.component.Name
import io.github.masamune.event.EventListener
import io.github.masamune.tiledmap.ConsumableType
import io.github.masamune.tiledmap.ItemType
import ktx.app.gdxError
import kotlin.math.round
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class ViewModel(
    val bundle: I18NBundle,
    val audioService: AudioService,
) : EventListener {

    var sound: Boolean = true

    @PublishedApi
    internal val actionsMap = mutableMapOf<KProperty<*>, MutableList<(Any) -> Unit>>()

    inline fun <reified T : Any> propertyNotify(initialValue: T): ReadWriteProperty<ViewModel, T> =
        Delegates.observable(initialValue) { property, _, newValue -> notify(property, newValue) }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> onPropertyChange(property: KProperty<T>, noinline action: (T) -> Unit) {
        val actions = actionsMap.getOrPut(property) { mutableListOf() } as MutableList<(T) -> Unit>
        actions += action
    }

    fun notify(property: KProperty<*>, value: Any) {
        actionsMap[property]?.forEach { action -> action(value) }
    }

    fun uiMapOf(vararg components: Component<*>): MutableMap<UIStats, String> {
        val result = mutableMapOf<UIStats, String>()

        components.forEach { component ->
            when (component) {
                is CharacterStats -> {
                    result += UIStats.AGILITY to "${component.agility.toInt()}"
                    result += UIStats.ARCANE_STRIKE to "${round((component.arcaneStrike * 100)).toInt()}%"
                    result += UIStats.ARMOR to "${component.armor.toInt()}"
                    result += UIStats.CONSTITUTION to "${component.constitution.toInt()}"
                    result += UIStats.CRITICAL_STRIKE to "${round((component.criticalStrike * 100)).toInt()}%"
                    result += UIStats.DAMAGE to "${component.damage.toInt()}"
                    result += UIStats.INTELLIGENCE to "${component.intelligence.toInt()}"
                    result += UIStats.LIFE to "${component.life.toInt()}"
                    result += UIStats.LIFE_MAX to "${component.lifeMax.toInt()}"
                    result += UIStats.MAGICAL_EVADE to "${round((component.magicalEvade * 100)).toInt()}%"
                    result += UIStats.MANA to "${component.mana.toInt()}"
                    result += UIStats.MANA_MAX to "${component.manaMax.toInt()}"
                    result += UIStats.PHYSICAL_EVADE to "${round((component.physicalEvade * 100)).toInt()}%"
                    result += UIStats.RESISTANCE to "${component.resistance.toInt()}"
                    result += UIStats.STRENGTH to "${component.strength.toInt()}"
                }

                is Experience -> {
                    result += UIStats.LEVEL to "${component.level}"
                    result += UIStats.XP to "${component.current}"
                    result += UIStats.XP_NEEDED to "${component.forLevelUp}"
                }

                is Inventory -> {
                    result += UIStats.TALONS to "${component.talons}"
                }

                else -> gdxError("Unsupported component: $component")
            }
        }

        return result
    }

    fun i18nTxt(key: I18NKey): String = bundle[key]

    // function to get game object descriptions like for items.
    // It detects special strings like %STAT.MANA% and replaces it with the proper entity data
    fun description(key: String, entity: Entity = Entity.NONE, world: World? = null): String {
        // user format method of bundle to correctly replace colors like {{COLOR=ROYAL}}
        val description = bundle.format(key)
        return description.replace(DESCR_TOKEN_REGEX) {
            when (it.groupValues[1]) {
                "STAT" -> replaceStatToken(it.groupValues[2], entity, world!!)
                "I18N" -> bundle[it.groupValues[2]]
                else -> gdxError("Unsupported description token in key $key: ${it.groupValues.first()}")
            }
        }
    }

    private fun replaceStatToken(statName: String, entity: Entity, world: World): String = with(world) {
        val stats = entity[ItemStats]
        when (statName) {
            "AGI" -> "${stats.agility.toInt()}"
            "ARCSTR" -> "${round((stats.arcaneStrike * 100)).toInt()}%"
            "ARMOR" -> "${stats.armor.toInt()}"
            "CONST" -> "${stats.constitution.toInt()}"
            "CRISTR" -> "${round((stats.criticalStrike * 100)).toInt()}%"
            "DAMAGE" -> "${stats.damage.toInt()}"
            "INT" -> "${stats.intelligence.toInt()}"
            "LIFE" -> "${stats.life.toInt()}"
            "MAXLIFE" -> "${stats.lifeMax.toInt()}"
            "MAGEVA" -> "${round((stats.magicalEvade * 100)).toInt()}%"
            "MANA" -> "${stats.mana.toInt()}"
            "MAXMANA" -> "${stats.manaMax.toInt()}"
            "PHYEVA" -> "${round((stats.physicalEvade * 100)).toInt()}%"
            "RES" -> "${stats.resistance.toInt()}"
            "STR" -> "${stats.strength.toInt()}"
            else -> gdxError("Unsupported stat: $statName")
        }
    }

    fun playSndMenuClick() {
        if (!sound) {
            return
        }
        audioService.play(SoundAsset.MENU_CLICK)
    }

    fun playSndMenuAccept() {
        if (!sound) {
            return
        }
        audioService.play(SoundAsset.MENU_ACCEPT)
    }

    fun playSndMenuAbort() {
        if (!sound) {
            return
        }
        audioService.play(SoundAsset.MENU_ABORT)
    }

    fun Vector2.toUiPosition(from: Viewport, to: Viewport): Vector2 {
        from.project(this)
        to.unproject(this)
        this.y = to.worldHeight - this.y
        return this
    }

    fun Equipment.toUiStatsMap(world: World): Map<UIStats, Int> = with(world) {
        val equipmentStats = this@toUiStatsMap.items.map { it[ItemStats] }
        return UIStats.entries.associateWith { uiStat ->
            equipmentStats.sumOf {
                    when (uiStat) {
                        UIStats.AGILITY -> it.agility.toInt()
                        UIStats.ARCANE_STRIKE -> round((it.arcaneStrike * 100)).toInt()
                        UIStats.ARMOR -> it.armor.toInt()
                        UIStats.CONSTITUTION -> it.constitution.toInt()
                        UIStats.CRITICAL_STRIKE -> round((it.criticalStrike * 100)).toInt()
                        UIStats.DAMAGE -> it.damage.toInt()
                        UIStats.INTELLIGENCE -> it.intelligence.toInt()
                        UIStats.LIFE_MAX -> it.life.toInt()
                        UIStats.MAGICAL_EVADE -> round((it.magicalEvade * 100)).toInt()
                        UIStats.MANA_MAX -> it.mana.toInt()
                        UIStats.PHYSICAL_EVADE -> round((it.physicalEvade * 100)).toInt()
                        UIStats.RESISTANCE -> it.resistance.toInt()
                        UIStats.STRENGTH -> it.strength.toInt()
                        else -> 0
                    }
                }
        }
    }

    fun ItemStats.toUiStatsMap(): Map<UIStats, Int> = mapOf(
        UIStats.AGILITY to this@toUiStatsMap.agility.toInt(),
        UIStats.ARCANE_STRIKE to round((this@toUiStatsMap.arcaneStrike * 100)).toInt(),
        UIStats.ARMOR to this@toUiStatsMap.armor.toInt(),
        UIStats.CONSTITUTION to this@toUiStatsMap.constitution.toInt(),
        UIStats.CRITICAL_STRIKE to round((this@toUiStatsMap.criticalStrike * 100)).toInt(),
        UIStats.DAMAGE to this@toUiStatsMap.damage.toInt(),
        UIStats.INTELLIGENCE to this@toUiStatsMap.intelligence.toInt(),
        UIStats.LIFE_MAX to this@toUiStatsMap.lifeMax.toInt(),
        UIStats.MAGICAL_EVADE to round((this@toUiStatsMap.magicalEvade * 100)).toInt(),
        UIStats.MANA_MAX to this@toUiStatsMap.manaMax.toInt(),
        UIStats.PHYSICAL_EVADE to round((this@toUiStatsMap.physicalEvade * 100)).toInt(),
        UIStats.RESISTANCE to this@toUiStatsMap.resistance.toInt(),
        UIStats.STRENGTH to this@toUiStatsMap.strength.toInt(),
    )

    fun CharacterStats.toUiStatsMap(): Map<UIStats, Int> = mapOf(
        UIStats.AGILITY to this@toUiStatsMap.agility.toInt(),
        UIStats.ARCANE_STRIKE to round((this@toUiStatsMap.arcaneStrike * 100)).toInt(),
        UIStats.ARMOR to this@toUiStatsMap.armor.toInt(),
        UIStats.CONSTITUTION to this@toUiStatsMap.constitution.toInt(),
        UIStats.CRITICAL_STRIKE to round((this@toUiStatsMap.criticalStrike * 100)).toInt(),
        UIStats.DAMAGE to this@toUiStatsMap.damage.toInt(),
        UIStats.INTELLIGENCE to this@toUiStatsMap.intelligence.toInt(),
        UIStats.LIFE_MAX to this@toUiStatsMap.lifeMax.toInt(),
        UIStats.MAGICAL_EVADE to round((this@toUiStatsMap.magicalEvade * 100)).toInt(),
        UIStats.MANA_MAX to this@toUiStatsMap.manaMax.toInt(),
        UIStats.PHYSICAL_EVADE to round((this@toUiStatsMap.physicalEvade * 100)).toInt(),
        UIStats.RESISTANCE to this@toUiStatsMap.resistance.toInt(),
        UIStats.STRENGTH to this@toUiStatsMap.strength.toInt(),
    )

    fun Triple<CharacterStats, Int, Int>.toUiStatsMap(): Map<UIStats, String> {
        val stats = this.first
        return mapOf(
            UIStats.AGILITY to "${stats.agility.toInt()}",
            UIStats.ARCANE_STRIKE to "${round((stats.arcaneStrike * 100)).toInt()}",
            UIStats.ARMOR to "${stats.armor.toInt()}",
            UIStats.CONSTITUTION to "${stats.constitution.toInt()}",
            UIStats.CRITICAL_STRIKE to "${round((stats.criticalStrike * 100)).toInt()}",
            UIStats.DAMAGE to "${stats.damage.toInt()}",
            UIStats.INTELLIGENCE to "${stats.intelligence.toInt()}",
            UIStats.LIFE_MAX to "${stats.lifeMax.toInt()}",
            UIStats.MAGICAL_EVADE to "${round((stats.magicalEvade * 100)).toInt()}",
            UIStats.MANA_MAX to "${stats.manaMax.toInt()}",
            UIStats.PHYSICAL_EVADE to "${round((stats.physicalEvade * 100)).toInt()}",
            UIStats.RESISTANCE to "${stats.resistance.toInt()}",
            UIStats.STRENGTH to "${stats.strength.toInt()}",
            UIStats.XP to "${this.second}",
            UIStats.TALONS to "${this.third}",
        )
    }

    fun Entity.toItemModel(world: World, withConsumeInfo: Boolean = false): ItemModel = with(world) {
        val itemEntity = this@toItemModel
        // and transform items into UI ItemModel objects
        val (type, cost, category, descriptionKey, _, consumableType, amount) = itemEntity[Item]
        val itemName = itemEntity[Name].name
        val region: TextureRegion? = itemEntity.getOrNull(Graphic)?.region

        val i18nName = bundle["item.$itemName.name"]
        val isConsumable = consumableType == ConsumableType.INVENTORY_ONLY || consumableType == ConsumableType.COMBAT_AND_INVENTORY
        val i18nDescription = buildString {
            if (withConsumeInfo && isConsumable) {
                appendLine("{BLINK=#695454FF;#69545455;2.5;0.6}${i18nTxt(I18NKey.ITEM_INFO_CONSUMABLE)}{ENDBLINK}")
                appendLine()
            }
            append(description(descriptionKey, itemEntity, world))
        }

        return ItemModel(
            type = type,
            stats = itemEntity.getOrNull(ItemStats)?.toUiStatsMap() ?: emptyMap(),
            name = i18nName,
            cost = cost,
            description = i18nDescription,
            category = category,
            image = TextureRegionDrawable(region),
            amount = amount,
            consumable = isConsumable,
        )
    }

    fun Entity.calcEquipmentDiff(selectedItem: ItemModel, world: World): Map<UIStats, Int> {
        if (!selectedItem.category.isEquipment) {
            return emptyMap()
        }

        with(world) {
            val (items) = this@calcEquipmentDiff[Equipment]
            val itemToCompare = items.firstOrNull { it[Item].category == selectedItem.category }
            val selectedStats = selectedItem.stats

            if (itemToCompare == null) {
                // player has no item of given type selected
                // -> diff is the stats of the selected item
                return mapOf(
                    UIStats.STRENGTH to selectedStats.getOrDefault(UIStats.STRENGTH, 0),
                    UIStats.AGILITY to selectedStats.getOrDefault(UIStats.AGILITY, 0),
                    UIStats.CONSTITUTION to selectedStats.getOrDefault(UIStats.CONSTITUTION, 0),
                    UIStats.INTELLIGENCE to selectedStats.getOrDefault(UIStats.INTELLIGENCE, 0),
                    UIStats.DAMAGE to selectedStats.getOrDefault(UIStats.DAMAGE, 0),
                    UIStats.ARMOR to selectedStats.getOrDefault(UIStats.ARMOR, 0),
                    UIStats.RESISTANCE to selectedStats.getOrDefault(UIStats.RESISTANCE, 0),
                )
            }

            val equipStats = itemToCompare[ItemStats]
            if (selectedItem.type == ItemType.UNDEFINED) {
                // special unequip item -> return currently equipped item stats
                return mapOf(
                    UIStats.STRENGTH to -equipStats.strength.toInt(),
                    UIStats.AGILITY to -equipStats.agility.toInt(),
                    UIStats.CONSTITUTION to -equipStats.constitution.toInt(),
                    UIStats.INTELLIGENCE to -equipStats.intelligence.toInt(),
                    UIStats.DAMAGE to -equipStats.damage.toInt(),
                    UIStats.ARMOR to -equipStats.armor.toInt(),
                    UIStats.RESISTANCE to -equipStats.resistance.toInt(),
                )
            }

            // compare selected item with currently equipped item
            return mapOf(
                UIStats.STRENGTH to (selectedStats.getOrDefault(UIStats.STRENGTH, 0) - equipStats.strength).toInt(),
                UIStats.AGILITY to (selectedStats.getOrDefault(UIStats.AGILITY, 0) - equipStats.agility).toInt(),
                UIStats.CONSTITUTION to (selectedStats.getOrDefault(UIStats.CONSTITUTION, 0) - equipStats.constitution).toInt(),
                UIStats.INTELLIGENCE to (selectedStats.getOrDefault(UIStats.INTELLIGENCE, 0) - equipStats.intelligence).toInt(),
                UIStats.DAMAGE to (selectedStats.getOrDefault(UIStats.DAMAGE, 0) - equipStats.damage).toInt(),
                UIStats.ARMOR to (selectedStats.getOrDefault(UIStats.ARMOR, 0) - equipStats.armor).toInt(),
                UIStats.RESISTANCE to (selectedStats.getOrDefault(UIStats.RESISTANCE, 0) - equipStats.resistance).toInt(),
            )
        }
    }

    companion object {
        // format example: %STAT.MANA%
        // format example: %I18N.magic.double_strike.name%
        private val DESCR_TOKEN_REGEX = "%([A-Z0-9]+)\\.([A-Za-z0-9._]+)%".toRegex()
    }
}
