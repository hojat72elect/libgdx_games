package io.github.masamune.trigger

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.trigger.cutscene.intro.cutSceneIntroTrigger
import io.github.masamune.trigger.cutscene.outro.cutSceneOutroTrigger
import io.github.masamune.trigger.forest.entrance.forestEntranceTrigger
import io.github.masamune.trigger.forest.entrance.healthPotForest1Trigger
import io.github.masamune.trigger.forest.entrance.healthPotForest2Trigger
import io.github.masamune.trigger.forest.entrance.manaPotForest1Trigger
import io.github.masamune.trigger.forest.entrance.spiritForestTrigger
import io.github.masamune.trigger.forest.entrance.terealisFlowerTrigger
import io.github.masamune.trigger.forest.masamune.forestMasamuneMapTrigger
import io.github.masamune.trigger.forest.masamune.masamuneForestTrigger
import io.github.masamune.trigger.forest.path.manGreenTrigger
import io.github.masamune.trigger.forest.path.snowyTrigger
import io.github.masamune.trigger.village.elderTrigger
import io.github.masamune.trigger.village.flowerGirlTrigger
import io.github.masamune.trigger.village.merchantTrigger
import io.github.masamune.trigger.village.smithTrigger
import io.github.masamune.trigger.village.villageExitTrigger
import io.github.masamune.trigger.village.villageIntroTrigger
import ktx.app.gdxError
import ktx.log.logger

class TriggerConfigurator {

    operator fun get(name: String, world: World, scriptEntity: Entity, triggeringEntity: Entity): TriggerScript {
        log.debug { "Creating new trigger $name" }

        return when (name) {
            "villageExit" -> world.villageExitTrigger(name, scriptEntity, triggeringEntity)
            "elder" -> world.elderTrigger(name, triggeringEntity)
            "merchant" -> world.merchantTrigger(name, triggeringEntity, scriptEntity)
            "smith" -> world.smithTrigger(name, triggeringEntity, scriptEntity)
            "flower_girl" -> world.flowerGirlTrigger(name, triggeringEntity)
            "terealis_flower" -> world.terealisFlowerTrigger(name, scriptEntity, triggeringEntity)
            "man_green" -> world.manGreenTrigger(name, scriptEntity, triggeringEntity)
            "cut_scene_intro" -> world.cutSceneIntroTrigger(name, scriptEntity)
            "cut_scene_outro" -> world.cutSceneOutroTrigger(name, scriptEntity)
            "masamune_forest" -> world.masamuneForestTrigger(name, scriptEntity, triggeringEntity)
            "health_pot_forest1" -> world.healthPotForest1Trigger(name, scriptEntity, triggeringEntity)
            "health_pot_forest2" -> world.healthPotForest2Trigger(name, scriptEntity, triggeringEntity)
            "mana_pot_forest1" -> world.manaPotForest1Trigger(name, scriptEntity, triggeringEntity)
            "snowy" -> world.snowyTrigger(name, triggeringEntity)
            "spirit_forest" -> world.spiritForestTrigger(name, scriptEntity, triggeringEntity)

            else -> gdxError("There is no trigger configured for name $name")
        }
    }

    operator fun get(name: String, world: World): TriggerScript? {
        log.debug { "Creating new trigger $name" }
        return when (name) {
            "forest_entrance" -> world.forestEntranceTrigger(name)
            "village_intro" -> world.villageIntroTrigger(name)
            "forest_masamune" -> world.forestMasamuneMapTrigger(name)
            else -> gdxError("There is no trigger configured for name $name")
        }
    }

    companion object {
        private val log = logger<TriggerConfigurator>()
    }
}

