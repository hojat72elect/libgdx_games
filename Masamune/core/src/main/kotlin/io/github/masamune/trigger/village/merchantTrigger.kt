package io.github.masamune.trigger.village

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.tiledmap.ItemType
import io.github.masamune.trigger.trigger
import io.github.masamune.ui.model.I18NKey

fun World.merchantTrigger(name: String, triggeringEntity: Entity, scriptEntity: Entity) =
    trigger(name, this, triggeringEntity) {
        actionDialog("merchant_00") { selectedOptionIdx ->
            if (selectedOptionIdx == 0) {
                actionShop(
                    triggeringEntity, scriptEntity, I18NKey.NPC_MERCHANT_TITLE, listOf(
                        ItemType.SMALL_MANA_POTION,
                        ItemType.SMALL_HEALTH_POTION,
                        ItemType.SCROLL_INFERNO,
                    )
                )
            }
        }
    }
