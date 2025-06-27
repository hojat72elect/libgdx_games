package io.github.masamune.dialog.village

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.dialog.ActionExit
import io.github.masamune.dialog.Dialog
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.dialog.dialog
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.get

fun DialogConfigurator.villageExitDialog(name: String): Dialog = dialog(name) {
    page(bundle[I18NKey.DIALOG_VILLAGE_EXIT_PAGE1]) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.villageIntroDialog(name: String, world: World, triggeringEntity: Entity): Dialog = dialog(name) {
    val playerName = entityName(world, triggeringEntity)

    page(bundle[I18NKey.DIALOG_VILLAGE_INTRO_PAGE1], "hero", playerName) {
        option(dialogOptionOk, ActionExit)
    }
}
