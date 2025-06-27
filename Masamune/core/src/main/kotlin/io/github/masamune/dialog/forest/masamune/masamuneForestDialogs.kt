package io.github.masamune.dialog.forest.masamune

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.dialog.ActionExit
import io.github.masamune.dialog.ActionNext
import io.github.masamune.dialog.Dialog
import io.github.masamune.dialog.DialogConfigurator
import io.github.masamune.dialog.dialog
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.get

fun DialogConfigurator.masamuneForest00Dialog(name: String, world: World, triggeringEntity: Entity) = dialog(name) {
    val playerName = entityName(world, triggeringEntity)

    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_00_PAGE1], "hero", playerName) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.masamuneForest10Dialog(name: String, world: World, triggeringEntity: Entity) = dialog(name) {
    val playerName = entityName(world, triggeringEntity)
    val demonFireTitle = bundle[I18NKey.NPC_DEMON_FIRE_TITLE]
    val demonSpiritTitle = bundle[I18NKey.NPC_DEMON_SPIRIT_TITLE]

    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_10_PAGE1], "demon_fire", demonFireTitle) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_10_PAGE2], "hero", playerName) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_10_PAGE3, demonSpiritTitle], "demon_fire", demonFireTitle) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.masamuneForest20Dialog(name: String, world: World, triggeringEntity: Entity) = dialog(name) {
    val playerName = entityName(world, triggeringEntity)
    val demonFireTitle = bundle[I18NKey.NPC_DEMON_FIRE_TITLE]
    val demonSpiritTitle = bundle[I18NKey.NPC_DEMON_SPIRIT_TITLE]

    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_20_PAGE1, demonFireTitle], "demon_spirit", demonSpiritTitle) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_20_PAGE2], "demon_spirit", demonSpiritTitle) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_20_PAGE3], "hero", playerName) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_20_PAGE4, demonFireTitle], "demon_spirit", demonSpiritTitle) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_20_PAGE5], "demon_fire", demonFireTitle) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_20_PAGE6], "demon_spirit", demonSpiritTitle) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_20_PAGE7], "hero", playerName) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.masamuneForest30Dialog(name: String): Dialog = dialog(name) {
    val demonFireTitle = bundle[I18NKey.NPC_DEMON_FIRE_TITLE]
    val demonSpiritTitle = bundle[I18NKey.NPC_DEMON_SPIRIT_TITLE]

    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_30_PAGE1], "demon_fire", demonFireTitle) {
        option(dialogOptionNext, ActionNext)
    }
    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_30_PAGE2], "demon_spirit", demonSpiritTitle) {
        option(dialogOptionOk, ActionExit)
    }
}

fun DialogConfigurator.masamuneForest40Dialog(name: String, world: World, triggeringEntity: Entity) = dialog(name) {
    val playerName = entityName(world, triggeringEntity)

    page(bundle[I18NKey.DIALOG_MASAMUNE_FOREST_40_PAGE1], "hero", playerName) {
        option(dialogOptionOk, ActionExit)
    }
}
