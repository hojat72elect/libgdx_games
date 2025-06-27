package io.github.masamune.trigger.village

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.tiledmap.ItemType
import io.github.masamune.trigger.trigger
import io.github.masamune.ui.model.I18NKey

fun World.smithTrigger(name: String, triggeringEntity: Entity, scriptEntity: Entity) =
    trigger(name, this, triggeringEntity) {
        actionDialog("smith_00") { selectedOptionIdx ->
            if (selectedOptionIdx == 0) {
                actionShop(
                    triggeringEntity, scriptEntity, I18NKey.NPC_SMITH_TITLE, listOf(
                        // boots
                        ItemType.BOOTS,
                        ItemType.BOOTS_SPEED,
                        // helmet
                        ItemType.HELMET,
                        // armor
                        ItemType.STUDDED_LEATHER,
                        ItemType.CHAIN_MAIL,
                        ItemType.MAGIC_VEST,
                        // weapon
                        ItemType.SWORD_HEAL,
                        ItemType.SWORD_STRENGTH,
                        // accessory
                        ItemType.RING,
                    )
                )
            }
        }
    }
