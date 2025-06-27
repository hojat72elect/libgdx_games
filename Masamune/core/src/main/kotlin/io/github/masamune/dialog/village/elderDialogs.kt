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

fun DialogConfigurator.elder00Dialog(name: String, world: World, triggeringEntity: Entity): Dialog = dialog(name) {
    val playerName = entityName(world, triggeringEntity)
    val elderTitle = bundle[I18NKey.NPC_ELDER_TITLE]

    page(bundle[I18NKey.DIALOG_ELDER_00_PAGE1, playerName], "elder", elderTitle) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_ELDER_00_PAGE2, playerName], "elder", elderTitle) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_ELDER_00_PAGE3], "elder", elderTitle) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.elder10Dialog(name: String): Dialog = dialog(name) {
    val elderTitle = bundle[I18NKey.NPC_ELDER_TITLE]

    page(bundle[I18NKey.DIALOG_ELDER_10_PAGE1], "elder", elderTitle) {
        option(dialogOptionOk, ActionExit)
    }
}
