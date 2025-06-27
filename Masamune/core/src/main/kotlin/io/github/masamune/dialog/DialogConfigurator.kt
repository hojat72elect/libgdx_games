package io.github.masamune.dialog

import com.badlogic.gdx.utils.I18NBundle
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.component.Name
import io.github.masamune.dialog.forest.entrance.spiritForest00Dialog
import io.github.masamune.dialog.forest.entrance.spiritForest10Dialog
import io.github.masamune.dialog.forest.entrance.spiritForest20Dialog
import io.github.masamune.dialog.forest.entrance.spiritForest30Dialog
import io.github.masamune.dialog.forest.masamune.masamuneForest00Dialog
import io.github.masamune.dialog.forest.masamune.masamuneForest10Dialog
import io.github.masamune.dialog.forest.masamune.masamuneForest20Dialog
import io.github.masamune.dialog.forest.masamune.masamuneForest30Dialog
import io.github.masamune.dialog.forest.masamune.masamuneForest40Dialog
import io.github.masamune.dialog.forest.path.manGreen00Dialog
import io.github.masamune.dialog.forest.path.manGreen10Dialog
import io.github.masamune.dialog.forest.path.manGreen20Dialog
import io.github.masamune.dialog.forest.path.manGreen30Dialog
import io.github.masamune.dialog.forest.path.snowyDialog
import io.github.masamune.dialog.village.elder00Dialog
import io.github.masamune.dialog.village.elder10Dialog
import io.github.masamune.dialog.village.flowerGirl00Dialog
import io.github.masamune.dialog.village.flowerGirl10Dialog
import io.github.masamune.dialog.village.flowerGirl20Dialog
import io.github.masamune.dialog.village.flowerGirl30Dialog
import io.github.masamune.dialog.village.merchantDialog
import io.github.masamune.dialog.village.smithDialog
import io.github.masamune.dialog.village.villageExitDialog
import io.github.masamune.dialog.village.villageIntroDialog
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.get
import ktx.app.gdxError
import ktx.log.logger

class DialogConfigurator(val bundle: I18NBundle) {

    val dialogOptionNext: String = bundle[I18NKey.DIALOG_OPTION_NEXT]
    val dialogOptionOk: String = bundle[I18NKey.DIALOG_OPTION_OK]
    val dialogOptionExit: String = bundle[I18NKey.DIALOG_OPTION_EXIT]
    val dialogOptionBuy: String = bundle[I18NKey.DIALOG_OPTION_BUY]
    val dialogOptionNo: String = bundle[I18NKey.GENERAL_NO]

    operator fun get(name: String, world: World, triggeringEntity: Entity): Dialog {
        log.debug { "Creating new dialog $name" }

        return when (name) {
            "elder_00" -> elder00Dialog(name, world, triggeringEntity)
            "elder_10" -> elder10Dialog(name)
            "merchant_00" -> merchantDialog(name)
            "smith_00" -> smithDialog(name, world, triggeringEntity)
            "flower_girl_00" -> flowerGirl00Dialog(name, world, triggeringEntity)
            "flower_girl_10" -> flowerGirl10Dialog(name, world, triggeringEntity)
            "flower_girl_20" -> flowerGirl20Dialog(name, world, triggeringEntity)
            "flower_girl_30" -> flowerGirl30Dialog(name, world, triggeringEntity)
            "villageExit" -> villageExitDialog(name)
            "man_green_00" -> manGreen00Dialog(name)
            "man_green_10" -> manGreen10Dialog(name)
            "man_green_20" -> manGreen20Dialog(name)
            "man_green_30" -> manGreen30Dialog(name)
            "village_intro" -> villageIntroDialog(name, world, triggeringEntity)
            "masamune_forest_00" -> masamuneForest00Dialog(name, world, triggeringEntity)
            "masamune_forest_10" -> masamuneForest10Dialog(name, world, triggeringEntity)
            "masamune_forest_20" -> masamuneForest20Dialog(name, world, triggeringEntity)
            "masamune_forest_30" -> masamuneForest30Dialog(name)
            "masamune_forest_40" -> masamuneForest40Dialog(name, world, triggeringEntity)
            "combat_defeat" -> combatDefeatDialog(name, world, triggeringEntity)
            "snowy" -> snowyDialog(name)
            "spiritForest_00" -> spiritForest00Dialog(name)
            "spiritForest_10" -> spiritForest10Dialog(name)
            "spiritForest_20" -> spiritForest20Dialog(name)
            "spiritForest_30" -> spiritForest30Dialog(name)

            else -> gdxError("There is no dialog configured for name $name")
        }
    }

    private fun combatDefeatDialog(name: String, world: World, triggeringEntity: Entity): Dialog = dialog(name) {
        val playerName = entityName(world, triggeringEntity)
        page(bundle[I18NKey.DIALOG_COMBAT_DEFEAT, playerName]) {
            option(dialogOptionOk, ActionExit)
        }
    }

    fun entityName(world: World, entity: Entity): String = with(world) {
        entity[Name].name
    }

    companion object {
        private val log = logger<DialogConfigurator>()
    }
}
