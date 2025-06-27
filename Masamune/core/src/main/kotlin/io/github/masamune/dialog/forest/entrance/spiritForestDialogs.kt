package io.github.masamune.dialog.forest.entrance

import io.github.masamune.dialog.ActionExit
import io.github.masamune.dialog.ActionNext
import io.github.masamune.dialog.Dialog
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.dialog.dialog
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.get

fun DialogConfigurator.spiritForest00Dialog(name: String): Dialog = dialog(name) {
    page(bundle[I18NKey.DIALOG_SPIRIT_00_PAGE1]) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.spiritForest10Dialog(name: String): Dialog = dialog(name) {
    page(bundle[I18NKey.DIALOG_SPIRIT_10_PAGE1]) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.spiritForest20Dialog(name: String): Dialog = dialog(name) {
    page(bundle[I18NKey.DIALOG_SPIRIT_20_PAGE1]) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.spiritForest30Dialog(name: String): Dialog = dialog(name) {
    page(bundle[I18NKey.DIALOG_SPIRIT_30_PAGE1]) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_SPIRIT_30_PAGE2]) {
        option(dialogOptionOk, ActionExit)
    }
}
