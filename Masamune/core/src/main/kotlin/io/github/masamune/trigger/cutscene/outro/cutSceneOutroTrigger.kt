package io.github.masamune.trigger.cutscene.outro

import com.badlogic.gdx.utils.Align
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import io.github.masamune.asset.MusicAsset
import io.github.masamune.screen.FadeTransitionType
import io.github.masamune.screen.MainMenuScreen
import io.github.masamune.trigger.TriggerScript
import io.github.masamune.trigger.trigger
import io.github.masamune.ui.model.I18NKey

fun World.cutSceneOutroTrigger(
    name: String,
    scriptEntity: Entity,
): TriggerScript = trigger(name, this, Entity.NONE) {
    actionRemove(scriptEntity)
    actionPlayMusic(MusicAsset.FOREST)
    actionDelay(1f)
    actionCutSceneText(I18NKey.CUT_SCENE_OUTRO_TEXT1, Align.center, duration = 9f)
    actionCutSceneText(I18NKey.CUT_SCENE_OUTRO_TEXT2, Align.center, duration = 7f)
    actionCutSceneText(I18NKey.CUT_SCENE_OUTRO_TEXT3, Align.center, duration = 7f)
    actionChangeScreen {
        transitionScreen<MainMenuScreen>(
            FadeTransitionType(1f, 0f, 2f),
            FadeTransitionType(0f, 1f, 0.25f, delayInSeconds = 2f),
        )
    }
}
