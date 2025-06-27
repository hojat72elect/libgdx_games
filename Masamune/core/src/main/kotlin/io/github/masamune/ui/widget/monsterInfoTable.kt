package io.github.masamune.ui.widget

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import io.github.masamune.ui.model.UIStats
import io.github.masamune.ui.view.typingLabel
import ktx.actors.txt
import ktx.scene2d.KTable
import ktx.scene2d.KTableWidget
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.table

@Scene2dDsl
class MonsterInfoTable(
    skin: Skin,
    statsLabels: Map<UIStats, String>,
) : Table(skin), KTable {

    private val image: Image
    private val nameLabel: Label
    private val descriptionLabel: TypingLabel

    private val lifeLabel: Label
    private val manaLabel: Label
    private val agilityLabel: Label
    private val damageLabel: Label
    private val armorLabel: Label
    private val resistanceLabel: Label
    private val xpLabel: Label
    private val talonsLabel: Label

    init {
        background = skin.getDrawable("dialog_frame")

        table(skin) { headerTblCell ->
            this@MonsterInfoTable.image = image { imgCell ->
                setScaling(Scaling.fit)
                imgCell.size(42f, 42f).fill()
            }
            this@MonsterInfoTable.nameLabel = label("", "dialog_content", skin) { lblCell ->
                setAlignment(Align.left)
                color = skin.getColor("highlight_blue")
                lblCell.growX().padLeft(20f)
            }
            headerTblCell.growX().padBottom(20f).row()
        }

        table(skin) { statsTblCell ->
            this@MonsterInfoTable.lifeLabel = statsLabel(statsLabels[UIStats.LIFE], skin, skin.getColor("green"))
            this@MonsterInfoTable.manaLabel = statsLabel(statsLabels[UIStats.MANA], skin, skin.getColor("blue"))
            this@MonsterInfoTable.agilityLabel = statsLabel(statsLabels[UIStats.AGILITY], skin)
            this@MonsterInfoTable.damageLabel = statsLabel(statsLabels[UIStats.DAMAGE], skin)
            this@MonsterInfoTable.armorLabel = statsLabel(statsLabels[UIStats.ARMOR], skin)
            this@MonsterInfoTable.resistanceLabel = statsLabel(statsLabels[UIStats.RESISTANCE], skin)
            this@MonsterInfoTable.xpLabel = statsLabel(statsLabels[UIStats.XP], skin)
            this@MonsterInfoTable.talonsLabel = statsLabel(statsLabels[UIStats.TALONS], skin)

            statsTblCell.growX().padBottom(20f).row()
        }

        descriptionLabel = typingLabel("", "dialog_content", skin) { lblCell ->
            wrap = true
            val defaultTextColor = skin.getColor("dark_grey")
            defaultToken = "{COLOR=#$defaultTextColor}"
            clearColor.set(defaultTextColor)
            setAlignment(Align.topLeft)
            lblCell.padTop(10f).grow()
        }
    }

    fun monster(name: String, description: String, drawable: Drawable?, stats: Map<UIStats, String>) {
        this.nameLabel.txt = name
        this.image.drawable = drawable
        this.descriptionLabel.restart("{FAST}$description")

        // hide stats if there are none (e.g. unknown monster)
        this.lifeLabel.parent.isVisible = stats.isNotEmpty()

        this.lifeLabel.txt = "${stats[UIStats.LIFE_MAX]}"
        this.manaLabel.txt = "${stats[UIStats.MANA_MAX]}"
        this.agilityLabel.txt = "${stats[UIStats.AGILITY]}"
        this.damageLabel.txt = "${stats[UIStats.DAMAGE]}"
        this.armorLabel.txt = "${stats[UIStats.ARMOR]}"
        this.resistanceLabel.txt = "${stats[UIStats.RESISTANCE]}"
        this.xpLabel.txt = "${stats[UIStats.XP]}"
        this.talonsLabel.txt = "${stats[UIStats.TALONS]}"
    }

    fun clearMonster() {
        this.nameLabel.txt = ""
        this.descriptionLabel.txt = ""
        this.lifeLabel.txt = ""
        this.manaLabel.txt = ""
        this.agilityLabel.txt = ""
        this.damageLabel.txt = ""
        this.armorLabel.txt = ""
        this.resistanceLabel.txt = ""
        this.xpLabel.txt = ""
        this.talonsLabel.txt = ""
        this.image.drawable = null
    }

    companion object {
        private fun KTableWidget.statsLabel(
            statsLabel: String?,
            skin: Skin,
            color: Color = skin.getColor("dark_grey")
        ): Label {
            label("${statsLabel}:", "dialog_content", skin) {
                this.color = color
                this.setAlignment(Align.left)
                it.fillX().padLeft(10f)
            }
            return label("", "dialog_content", skin) { lblCell ->
                this.color = skin.getColor("dark_grey")
                lblCell.growX().padLeft(15f).row()
            }
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.monsterInfoTable(
    skin: Skin,
    statsLabels: Map<UIStats, String>,
    init: (@Scene2dDsl MonsterInfoTable).(S) -> Unit = {},
): MonsterInfoTable = actor(MonsterInfoTable(skin, statsLabels), init)
