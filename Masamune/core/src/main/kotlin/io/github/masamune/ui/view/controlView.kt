package io.github.masamune.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.Scaling
import io.github.masamune.ui.model.I18NKey
import io.github.masamune.ui.model.get
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.table


@Scene2dDsl
class ControlView(
    bundle: I18NBundle,
    skin: Skin,
) : KTable, Table(skin) {

    init {
        setFillParent(true)
        top()

        label(bundle[I18NKey.GENERAL_CONTROLS], "default_huge", skin) {
            setAlignment(Align.center)
            it.growX().height(150f).colspan(2).row()
        }

        table(skin) { tblCell ->
            label(bundle[I18NKey.GENERAL_CONTROLS_MOVE], "default", skin) {
                it.padBottom(10f).row()
            }

            table(skin) {
                image(skin.getDrawable("key_w")) {
                    setScaling(Scaling.fit)
                    it.size(64f, 64f).colspan(3).row()
                }
                image(skin.getDrawable("key_a")) {
                    setScaling(Scaling.fit)
                    it.size(64f, 64f)
                }
                image(skin.getDrawable("key_s")) {
                    setScaling(Scaling.fit)
                    it.size(64f, 64f)
                }
                image(skin.getDrawable("key_d")) {
                    setScaling(Scaling.fit)
                    it.size(64f, 64f)
                }
            }

            tblCell.padBottom(100f)
        }

        table(skin) { tblCell ->
            label(bundle[I18NKey.GENERAL_CONTROLS_INTERACT], "default", skin) {
                it.padBottom(10f).row()
            }

            table(skin) {
                image(skin.getDrawable("key_space")) {
                    setScaling(Scaling.fit)
                    it.size(64f * 3, 64f)
                }
            }

            tblCell.padBottom(100f).top().row()
        }

        table(skin) {
            label(bundle[I18NKey.GENERAL_CONTROLS_OPEN_CLOSE], "default", skin) {
                it.padBottom(10f).row()
            }

            table(skin) {
                image(skin.getDrawable("key_ctrl")) {
                    setScaling(Scaling.fit)
                    it.size(64f * 2, 64f)
                }
            }
        }

        table(skin) {
            label(bundle[I18NKey.GENERAL_CONTROLS_CANCEL], "default", skin) {
                it.padBottom(10f).row()
            }

            table(skin) {
                image(skin.getDrawable("key_esc")) {
                    setScaling(Scaling.fit)
                    it.size(64f, 64f)
                }
            }
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.controlView(
    bundle: I18NBundle,
    skin: Skin,
    init: (@Scene2dDsl ControlView).(S) -> Unit = {},
): ControlView = actor(ControlView(bundle, skin), init)
