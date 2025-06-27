package io.github.masamune.dialog.forest.path

import io.github.masamune.dialog.ActionExit
import io.github.masamune.dialog.ActionNext
import io.github.masamune.dialog.Dialog
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.dialog.dialog
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.get

fun DialogConfigurator.snowyDialog(name: String): Dialog = dialog(name) {
    page(bundle[I18NKey.DIALOG_SNOWY_PAGE1], "snowy", bundle[I18NKey.NPC_SNOWY_TITLE]) {
        option(dialogOptionOk, ActionNext)
        option(dialogOptionNo, ActionExit)
    }
    page(bundle[I18NKey.DIALOG_SNOWY_PAGE2], "snowy", bundle[I18NKey.NPC_SNOWY_TITLE]) {
        option(dialogOptionOk, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_SNOWY_PAGE3], "snowy", bundle[I18NKey.NPC_SNOWY_TITLE]) {
        option(dialogOptionOk, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_SNOWY_PAGE4], "snowy", bundle[I18NKey.NPC_SNOWY_TITLE]) {
        option(dialogOptionOk, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_SNOWY_PAGE5], "snowy", bundle[I18NKey.NPC_SNOWY_TITLE]) {
        option(dialogOptionOk, ActionExit)
    }
}
