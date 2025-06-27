package io.github.masamune.ui.model

import com.badlogic.gdx.utils.I18NBundle
import io.github.masamune.audio.AudioService
import io.github.masamune.event.CutSceneTextEvent
import io.github.masamune.event.Event

class CutSceneViewModel(
    bundle: I18NBundle,
    audioService: AudioService,
) : ViewModel(bundle, audioService) {

    var textModel: CutSceneTextModel by propertyNotify(CutSceneTextModel.EMPTY)

    override fun onEvent(event: Event) {
        when (event) {
            is CutSceneTextEvent -> {
                textModel = CutSceneTextModel(i18nTxt(event.i18NKey), event.align, event.duration)
            }

            else -> Unit
        }
    }

}
