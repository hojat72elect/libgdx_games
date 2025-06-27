package io.github.masamune.dialog.village

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.dialog.ActionExit
import io.github.masamune.dialog.Dialog
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.dialog.dialog
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.get

fun DialogConfigurator.smithDialog(name: String, world: World, triggeringEntity: Entity): Dialog = dialog(name) {
    val playerName = entityName(world, triggeringEntity)

    page(bundle[I18NKey.DIALOG_SMITH_00_PAGE1, playerName], "smith", bundle[I18NKey.NPC_SMITH_TITLE]) {
        option(dialogOptionBuy, ActionExit)
        option(dialogOptionExit, ActionExit)
    }
}
