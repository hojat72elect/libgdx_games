package io.github.masamune.dialog.village

import io.github.masamune.dialog.ActionExit
import io.github.masamune.dialog.Dialog
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.dialog.dialog
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.get

fun DialogConfigurator.merchantDialog(name: String): Dialog = dialog(name) {
    page(bundle[I18NKey.DIALOG_MERCHANT_00_PAGE1], "merchant", bundle[I18NKey.NPC_MERCHANT_TITLE]) {
        option(dialogOptionBuy, ActionExit)
        option(dialogOptionExit, ActionExit)
    }
}
