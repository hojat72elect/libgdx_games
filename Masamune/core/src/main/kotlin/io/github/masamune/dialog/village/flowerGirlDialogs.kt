package io.github.masamune.dialog.village

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.dialog.ActionExit
import io.github.masamune.dialog.ActionNext
import io.github.masamune.dialog.Dialog
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.dialog.dialog
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.get

fun DialogConfigurator.flowerGirl00Dialog(name: String, world: World, triggeringEntity: Entity): Dialog = dialog(name) {
    val playerName = entityName(world, triggeringEntity)
    val flowerGirlTitle = bundle[I18NKey.NPC_FLOWER_GIRL_TITLE]

    page(bundle[I18NKey.DIALOG_FLOWER_GIRL_00_PAGE1, playerName], "flower_girl", flowerGirlTitle) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_FLOWER_GIRL_00_PAGE2, playerName], "flower_girl", flowerGirlTitle) {
        option(dialogOptionOk, ActionNext)
        option(dialogOptionExit, ActionExit)
    }
    page(bundle[I18NKey.DIALOG_FLOWER_GIRL_00_PAGE3, playerName], "flower_girl", flowerGirlTitle) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_FLOWER_GIRL_00_PAGE4, playerName], "flower_girl", flowerGirlTitle) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.flowerGirl10Dialog(name: String, world: World, triggeringEntity: Entity): Dialog = dialog(name) {
    val playerName = entityName(world, triggeringEntity)

    page(bundle[I18NKey.DIALOG_FLOWER_GIRL_10_PAGE1, playerName], "flower_girl", bundle[I18NKey.NPC_FLOWER_GIRL_TITLE]) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.flowerGirl20Dialog(name: String, world: World, triggeringEntity: Entity): Dialog = dialog(name) {
    val playerName = entityName(world, triggeringEntity)

    page(bundle[I18NKey.DIALOG_FLOWER_GIRL_20_PAGE1, playerName], "flower_girl", bundle[I18NKey.NPC_FLOWER_GIRL_TITLE]) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.flowerGirl30Dialog(name: String, world: World, triggeringEntity: Entity): Dialog = dialog(name) {
    val playerName = entityName(world, triggeringEntity)

    page(bundle[I18NKey.DIALOG_FLOWER_GIRL_30_PAGE1, playerName], "flower_girl", bundle[I18NKey.NPC_FLOWER_GIRL_TITLE]) {
        option(dialogOptionOk, ActionExit)
    }
}
