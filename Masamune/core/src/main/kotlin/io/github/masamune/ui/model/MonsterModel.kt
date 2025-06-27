package io.github.masamune.ui.model

import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.scenes.scene2d.utils.Drawable

data class MonsterModel(
    val name: String,
    val description: String,
    val drawable: Drawable,
    val stats: Map<UIStats, String>,
) {
    companion object {
        val UNKNOWN = MonsterModel("???", "???", BaseDrawable(), emptyMap())
    }
}
