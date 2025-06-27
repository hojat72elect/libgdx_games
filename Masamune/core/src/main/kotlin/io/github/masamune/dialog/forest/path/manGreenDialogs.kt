package io.github.masamune.dialog.forest.path

import io.github.masamune.dialog.ActionExit
import io.github.masamune.dialog.Dialog
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.dialog.dialog
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.get

fun DialogConfigurator.manGreen00Dialog(name: String): Dialog = dialog(name) {
    page(bundle[I18NKey.DIALOG_MAN_GREEN_00_PAGE1], "man_green", bundle[I18NKey.NPC_MAN_GREEN_TITLE]) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.manGreen10Dialog(name: String): Dialog = dialog(name) {
    page(bundle[I18NKey.DIALOG_MAN_GREEN_10_PAGE1], "man_green", bundle[I18NKey.NPC_MAN_GREEN_TITLE]) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.manGreen20Dialog(name: String): Dialog = dialog(name) {
    page(bundle[I18NKey.DIALOG_MAN_GREEN_20_PAGE1], "man_green", bundle[I18NKey.NPC_MAN_GREEN_TITLE]) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.manGreen30Dialog(name: String): Dialog = dialog(name) {
    page(bundle[I18NKey.DIALOG_MAN_GREEN_30_PAGE1], "man_green", bundle[I18NKey.NPC_MAN_GREEN_TITLE]) {
        option(dialogOptionOk, ActionExit)
    }
}
