package io.github.masamune.trigger.cutscene.intro

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.utils.Align
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.asset.MusicAsset
import io.github.masamune.asset.TiledMapAsset
import io.github.masamune.component.Player
import io.github.masamune.screen.FadeTransitionType
import io.github.masamune.screen.GameScreen
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger
import io.github.masamune.ui.model.I18NKey

fun World.cutSceneIntroTrigger(
    name: String,
    scriptEntity: Entity,
): TriggerScript = trigger(name, this, Entity.NONE) {
    actionRemove(scriptEntity)
    actionPlayMusic(MusicAsset.INTRO)
    actionLoadMap(TiledMapAsset.FOREST_MASAMUNE, withBoundaries = false, withTriggers = false, withPortals = false)
    val playerSelector = selectEntity { family { all(Player) }.single() }
    actionHideEntity(playerSelector)
    actionEntitySpeed(playerSelector, 0.2f)
    actionFollowPath(playerSelector, pathId = 36, removeAtEnd = true, waitForEnd = false)
    actionDelay(2f)
    actionCutSceneText(I18NKey.CUT_SCENE_INTRO_TEXT1, Align.center, duration = 11f)
    actionCutSceneText(I18NKey.CUT_SCENE_INTRO_TEXT2, Align.center, duration = 11f)
    actionCutSceneText(I18NKey.CUT_SCENE_INTRO_TEXT3, Align.center, duration = 9f)
    actionCutSceneText(I18NKey.CUT_SCENE_INTRO_TEXT4, Align.center, duration = 9f)
    actionCutSceneText(I18NKey.CUT_SCENE_INTRO_TEXT5, Align.center, duration = 8f)
    actionFadeOutMusic(3.5f, wait = false)
    actionChangeScreen {
        transitionScreen<GameScreen>(
            FadeTransitionType(1f, 0f, 4f, Interpolation.fastSlow),
            FadeTransitionType(0.33f, 1f, 0.25f, Interpolation.slowFast, delayInSeconds = 3f),
        ) {
            it.startNewGame()
        }
    }
}
