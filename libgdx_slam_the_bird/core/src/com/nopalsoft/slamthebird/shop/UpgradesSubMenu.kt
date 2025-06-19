package com.nopalsoft.slamthebird.shop

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.slamthebird.Assets
import com.nopalsoft.slamthebird.Settings
import com.nopalsoft.slamthebird.SlamTheBirdGame

class UpgradesSubMenu(var game: SlamTheBirdGame, tableContainer: Table) {


    private var priceLevel1: Int = 500
    private var priceLevel2: Int = 1000
    private var priceLevel3: Int = 2500
    private var priceLevel4: Int = 4000
    private var priceLevel5: Int = 5000
    private var priceLevel6: Int = 6000

    var buttonBoostTime: TextButton? = null
    var buttonFreeze: TextButton? = null
    var buttonCoins: TextButton? = null
    var buttonSuperJump: TextButton? = null
    var buttonInvincible: TextButton? = null
    var labelPriceTime: Label? = null
    var labelPriceFreeze: Label? = null
    var labelPriceCoins: Label? = null
    var labelPriceSuperJump: Label? = null
    var labelPriceInvincible: Label? = null

    private var arrayBoostTime: Array<Image?>
    private var arrayBoostFreeze: Array<Image?>
    private var arrayBoostCoins: Array<Image?>
    private var arrayBoostSuperJump: Array<Image?>
    private var arrayBoostInvincible: Array<Image?>

    init {
        tableContainer.clear()

        arrayBoostTime = arrayOfNulls(MAX_LEVEL)
        arrayBoostFreeze = arrayOfNulls(MAX_LEVEL)
        arrayBoostCoins = arrayOfNulls(MAX_LEVEL)
        arrayBoostSuperJump = arrayOfNulls(MAX_LEVEL)
        arrayBoostInvincible = arrayOfNulls(MAX_LEVEL)

        if (Settings.BOOST_DURATION < MAX_LEVEL) labelPriceTime = Label(
            getPriceForLevel(Settings.BOOST_DURATION).toString() + "",
            Assets.smallLabelStyle
        )

        if (Settings.BOOST_FREEZE < MAX_LEVEL) labelPriceFreeze = Label(
            getPriceForLevel(Settings.BOOST_FREEZE)
                .toString() + "", Assets.smallLabelStyle
        )

        if (Settings.BOOST_COINS < MAX_LEVEL) labelPriceCoins = Label(
            getPriceForLevel(Settings.BOOST_COINS).toString() + "",
            Assets.smallLabelStyle
        )

        if (Settings.BOOST_SUPER_JUMP < MAX_LEVEL) labelPriceSuperJump = Label(
            getPriceForLevel(Settings.BOOST_SUPER_JUMP).toString() + "",
            Assets.smallLabelStyle
        )

        if (Settings.BOOST_INVINCIBLE < MAX_LEVEL) labelPriceInvincible = Label(
            getPriceForLevel(Settings.BOOST_INVINCIBLE).toString() + "",
            Assets.smallLabelStyle
        )

        initializeButtons()

        tableContainer.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f)
        tableContainer.row()

        // Upgrade BoostTime
        tableContainer
            .add(
                addPlayerTable(
                    "More power-ups",
                    labelPriceTime, Assets.boosts!!,
                    "Power-ups will appear more often in the game",
                    arrayBoostTime, buttonBoostTime
                )
            ).expandX().fill()
        tableContainer.row()

        // Upgrade Super Jump
        tableContainer
            .add(
                addPlayerTable(
                    "Super jump", labelPriceSuperJump,
                    Assets.superJumpBoost!!,
                    "Super jump power up will last more time",
                    arrayBoostSuperJump, buttonSuperJump
                )
            ).expandX()
            .fill()
        tableContainer.row()

        // Upgrade Ice
        tableContainer
            .add(
                addPlayerTable(
                    "Freeze enemies", labelPriceFreeze,
                    Assets.freezeBoost!!, "Enemies will last more time frozen",
                    arrayBoostFreeze, buttonFreeze
                )
            ).expandX().fill()
        tableContainer.row()

        // Upgrade Invincibility
        tableContainer
            .add(
                addPlayerTable(
                    "Invencible", labelPriceInvincible,
                    Assets.invincibilityBoost!!,
                    "The invencible power-up will last more time",
                    arrayBoostInvincible, buttonInvincible
                )
            ).expandX()
            .fill()
        tableContainer.row()

        // Upgrade Coins
        tableContainer
            .add(
                addPlayerTable(
                    "Coin rain",
                    labelPriceCoins,
                    Assets.coinRainBoost!!,
                    "More coins will fall down when the coin rain power-up is taken",
                    arrayBoostCoins, buttonCoins
                )
            ).expandX().fill()
        tableContainer.row()

        setArrays()
    }

    private fun addPlayerTable(
        title: String, labelPrice: Label?,
        imageAtlasRegion: AtlasRegion, description: String, levels: Array<Image?>,
        button: TextButton?
    ): Table {
        val imageCoin = Image(Assets.coinsRegion)
        val imagePlayer = Image(imageAtlasRegion)

        if (labelPrice == null) imageCoin.isVisible = false

        val tableTitleBar = Table()
        tableTitleBar.add(Label(title, Assets.smallLabelStyle)).expandX()
            .left().padLeft(5f)
        tableTitleBar.add(imageCoin).right()
        tableTitleBar.add(labelPrice).right().padRight(10f)

        val tableDescription = Table()
        tableDescription.add(imagePlayer).left().pad(10f).size(55f, 45f)
        val labelDescription = Label(description, Assets.smallLabelStyle)
        labelDescription.wrap = true
        tableDescription.add(labelDescription).expand().fill().padLeft(5f)

        val tableContent = Table()
        tableContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8f)
        tableContent.row().colspan(2)
        tableContent.add(tableDescription).expandX().fill()
        tableContent.row()

        val auxiliaryTable = Table()
        auxiliaryTable.defaults().padLeft(5f)
        for (i in 0..<MAX_LEVEL) {
            levels[i] = Image(Assets.upgradeOff)
            auxiliaryTable.add(levels[i]).width(18f).height(25f)
        }

        tableContent.add(auxiliaryTable).center().expand()
        tableContent.add(button).right().padRight(10f).size(120f, 45f)

        tableContent.row().colspan(2)
        tableContent.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f).padTop(15f)

        return tableContent
    }

    private fun initializeButtons() {
        buttonBoostTime = TextButton(
            "Upgrade",
            Assets.styleTextButtonSelected
        )
        if (Settings.BOOST_DURATION == MAX_LEVEL) buttonBoostTime!!.isVisible = false
        addPressEffect(buttonBoostTime!!)
        buttonBoostTime!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= getPriceForLevel(Settings.BOOST_DURATION)) {
                    Settings.currentCoins -= getPriceForLevel(Settings.BOOST_DURATION)
                    Settings.BOOST_DURATION++
                    updateLabelPriceAndButton(
                        Settings.BOOST_DURATION,
                        labelPriceTime!!, buttonBoostTime!!
                    )
                    setArrays()
                }
            }
        })

        buttonSuperJump = TextButton(
            "Upgrade",
            Assets.styleTextButtonSelected
        )
        if (Settings.BOOST_SUPER_JUMP == MAX_LEVEL) buttonSuperJump!!.isVisible = false
        addPressEffect(buttonSuperJump!!)
        buttonSuperJump!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= getPriceForLevel(Settings.BOOST_SUPER_JUMP)) {
                    Settings.currentCoins -= getPriceForLevel(Settings.BOOST_SUPER_JUMP)
                    Settings.BOOST_SUPER_JUMP++
                    updateLabelPriceAndButton(
                        Settings.BOOST_SUPER_JUMP,
                        labelPriceSuperJump!!, buttonSuperJump!!
                    )
                    setArrays()
                }
            }
        })

        buttonFreeze = TextButton("Upgrade", Assets.styleTextButtonSelected)
        if (Settings.BOOST_FREEZE == MAX_LEVEL) buttonFreeze!!.isVisible = false

        addPressEffect(buttonFreeze!!)
        buttonFreeze!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= getPriceForLevel(Settings.BOOST_FREEZE)) {
                    Settings.currentCoins -= getPriceForLevel(Settings.BOOST_FREEZE)
                    Settings.BOOST_FREEZE++
                    updateLabelPriceAndButton(
                        Settings.BOOST_FREEZE,
                        labelPriceFreeze!!, buttonFreeze!!
                    )
                    setArrays()
                }
            }
        })

        buttonInvincible = TextButton(
            "Upgrade",
            Assets.styleTextButtonSelected
        )
        if (Settings.BOOST_INVINCIBLE == MAX_LEVEL) buttonInvincible!!.isVisible = false

        addPressEffect(buttonInvincible!!)
        buttonInvincible!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= getPriceForLevel(Settings.BOOST_INVINCIBLE)) {
                    Settings.currentCoins -= getPriceForLevel(Settings.BOOST_INVINCIBLE)
                    Settings.BOOST_INVINCIBLE++
                    updateLabelPriceAndButton(
                        Settings.BOOST_INVINCIBLE,
                        labelPriceInvincible!!, buttonInvincible!!
                    )
                    setArrays()
                }
            }
        })

        buttonCoins = TextButton(
            "Upgrade",
            Assets.styleTextButtonSelected
        )
        if (Settings.BOOST_COINS == MAX_LEVEL) buttonCoins!!.isVisible = false

        addPressEffect(buttonCoins!!)
        buttonCoins!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (Settings.currentCoins >= getPriceForLevel(Settings.BOOST_COINS)) {
                    Settings.currentCoins -= getPriceForLevel(Settings.BOOST_COINS)
                    Settings.BOOST_COINS++
                    updateLabelPriceAndButton(
                        Settings.BOOST_COINS,
                        labelPriceCoins!!, buttonCoins!!
                    )
                    setArrays()
                }
            }
        })
    }

    private fun setArrays() {
        for (i in 0..<Settings.BOOST_DURATION) {
            arrayBoostTime[i]!!.drawable = TextureRegionDrawable(
                Assets.upgradeOn
            )
        }

        for (i in 0..<Settings.BOOST_FREEZE) {
            arrayBoostFreeze[i]!!.drawable = TextureRegionDrawable(
                Assets.upgradeOn
            )
        }

        for (i in 0..<Settings.BOOST_INVINCIBLE) {
            arrayBoostInvincible[i]!!.drawable = TextureRegionDrawable(
                Assets.upgradeOn
            )
        }

        for (i in 0..<Settings.BOOST_SUPER_JUMP) {
            arrayBoostSuperJump[i]!!.drawable = TextureRegionDrawable(
                Assets.upgradeOn
            )
        }

        for (i in 0..<Settings.BOOST_COINS) {
            arrayBoostCoins[i]!!.drawable = TextureRegionDrawable(
                Assets.upgradeOn
            )
        }
    }

    private fun getPriceForLevel(level: Int): Int {
        return when (level) {
            0 -> priceLevel1
            1 -> priceLevel2
            2 -> priceLevel3
            3 -> priceLevel4
            4 -> priceLevel5
            else -> priceLevel6
        }
    }

    private fun updateLabelPriceAndButton(
        nivel: Int, label: Label,
        boton: TextButton
    ) {
        if (nivel < MAX_LEVEL) {
            label.setText(getPriceForLevel(nivel).toString() + "")
        } else {
            label.isVisible = false
            boton.isVisible = false
        }
    }

    private fun addPressEffect(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ): Boolean {
                actor.setPosition(actor.x, actor.y - 3)
                event.stop()
                return true
            }

            override fun touchUp(
                event: InputEvent, x: Float, y: Float,
                pointer: Int, button: Int
            ) {
                actor.setPosition(actor.x, actor.y + 3)
            }
        })
    }

    companion object {
        private const val MAX_LEVEL = 6
    }
}
