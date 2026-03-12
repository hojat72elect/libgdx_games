package com.nopalsoft.ninjarunner.shop

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.NinjaRunnerGame
import com.nopalsoft.ninjarunner.Settings

class UpgradesSubMenu(tableContainer: Table, game: NinjaRunnerGame) {
    val MAX_LEVEL: Int = 6

    val PRICE_LEVEL_1: Int = 500
    val PRICE_LEVEL_2: Int = 1000
    val PRICE_LEVEL_3: Int = 1750
    val PRICE_LEVEL_4: Int = 2500
    val PRICE_LEVEL_5: Int = 3000

    var buttonUpgradeMagnet: Button? = null
    var buttonUpgradeLife: Button? = null
    var buttonUpgradeEnergy: Button? = null
    var buttonUpgradeCoins: Button? = null
    var buttonUpgradeTreasureChest: Button? = null

    var labelPriceMagnet: Label? = null
    var labelPriceLife: Label? = null
    var labelPriceEnergy: Label? = null
    var labelPriceCoins: Label? = null
    var labelPriceTreasureChest: Label? = null

    var arrayMagnet: Array<Image?>
    var arrayLife: Array<Image?>
    var arrayEnergy: Array<Image?>
    var arrayCoin: Array<Image?>
    var arrayTreasureChest: Array<Image?>

    var languages = game.languages

    init {
        tableContainer.clear()

        arrayMagnet = arrayOfNulls<Image>(MAX_LEVEL)
        arrayLife = arrayOfNulls<Image>(MAX_LEVEL)
        arrayEnergy = arrayOfNulls<Image>(MAX_LEVEL)
        arrayCoin = arrayOfNulls<Image>(MAX_LEVEL)
        arrayTreasureChest = arrayOfNulls<Image>(MAX_LEVEL)

        if (Settings.LEVEL_MAGNET < MAX_LEVEL) labelPriceMagnet = Label(calculatePrice(Settings.LEVEL_MAGNET).toString() + "", Assets.labelStyleSmall)

        if (Settings.LEVEL_LIFE < MAX_LEVEL) labelPriceLife = Label(calculatePrice(Settings.LEVEL_LIFE).toString() + "", Assets.labelStyleSmall)

        if (Settings.LEVEL_ENERGY < MAX_LEVEL) labelPriceEnergy = Label(calculatePrice(Settings.LEVEL_ENERGY).toString() + "", Assets.labelStyleSmall)

        if (Settings.LEVEL_COINS < MAX_LEVEL) labelPriceCoins = Label(calculatePrice(Settings.LEVEL_COINS).toString() + "", Assets.labelStyleSmall)

        if (Settings.LEVEL_TREASURE_CHEST < MAX_LEVEL) labelPriceTreasureChest = Label(calculatePrice(Settings.LEVEL_TREASURE_CHEST).toString() + "", Assets.labelStyleSmall)

        initializeButtons()

        tableContainer.defaults().expand().fill().padLeft(10f).padRight(20f).padBottom(10f)

        // Upgrade MAGNET
        tableContainer.add<Table?>(
            addPlayerTable(
                languages!!.get("upgradeMagnet"), labelPriceMagnet, Assets.magnet, 35f, 35f, languages!!.get("magnetDescription"),
                arrayMagnet, buttonUpgradeMagnet
            )
        ).row()
        tableContainer.add<Table?>(
            addPlayerTable("Upgrade Life", labelPriceLife, Assets.hearth, 38f, 29f, languages!!.get("bombDescription"), arrayLife, buttonUpgradeLife)
        )
            .row()
        tableContainer.add<Table?>(
            addPlayerTable(
                "Upgrade Eneergy", labelPriceEnergy, Assets.energy, 25f, 35f, languages!!.get("bombDescription"), arrayEnergy,
                buttonUpgradeEnergy
            )
        ).row()
        tableContainer.add<Table?>(
            addPlayerTable(
                "Upgrade coins", labelPriceCoins, Assets.coinAnimation!!.getKeyFrame(0f), 35f, 35f, languages!!.get("bombDescription"), arrayCoin,
                buttonUpgradeCoins
            )
        ).row()
        tableContainer.add<Table?>(
            addPlayerTable(
                languages!!.get("upgradeTreasureChest"), labelPriceTreasureChest, Assets.magnet, 35f, 35f,
                languages!!.get("treasureChestDescription"), arrayTreasureChest, buttonUpgradeTreasureChest
            )
        ).row()

        setArrays()
    }

    private fun addPlayerTable(
        title: String?, labelPrice: Label?, image: Sprite?, imageWidth: Float, imageHeight: Float, description: String?,
        arrayLevel: Array<Image?>, buttonUpgrade: Button?
    ): Table {
        val imageCoin = Image(Assets.coinAnimation!!.getKeyFrame(0f))
        val imagePlayer = Image(image)

        if (labelPrice == null) imageCoin.isVisible = false

        val tableTitleBar = Table()
        tableTitleBar.add<Label?>(Label(title, Assets.labelStyleSmall)).expandX().left()
        tableTitleBar.add<Image?>(imageCoin).right().size(20f)
        tableTitleBar.add<Label?>(labelPrice).right().padRight(10f)

        val tableContent = Table()
        tableContent.setBackground(Assets.backgroundItemShop)
        tableContent.pad(5f)

        tableContent.add<Table?>(tableTitleBar).expandX().fill().colspan(2)
        tableContent.row()

        tableContent.add<Image?>(imagePlayer).size(imageWidth, imageHeight)
        val labelDescription = Label(description, Assets.labelStyleSmall)
        labelDescription.setWrap(true)
        tableContent.add<Label?>(labelDescription).expand().fill()

        val auxiliaryTable = Table()
        auxiliaryTable.setBackground(Assets.backgroundUpgradeBar)
        auxiliaryTable.pad(5f)
        auxiliaryTable.defaults().padLeft(5f)
        for (i in 0..<MAX_LEVEL) {
            arrayLevel[i] = Image()
            auxiliaryTable.add<Image?>(arrayLevel[i]).size(15f)
        }

        tableContent.row()
        tableContent.add<Table?>(auxiliaryTable)
        tableContent.add<Button?>(buttonUpgrade).left().size(40f)

        return tableContent
    }

    private fun initializeButtons() {
        buttonUpgradeMagnet = Button(Assets.styleButtonUpgrade)
        buttonUpgradeMagnet!!.userObject = Settings.LEVEL_MAGNET
        initializeButton(buttonUpgradeMagnet!!, labelPriceMagnet!!)

        buttonUpgradeLife = Button(Assets.styleButtonUpgrade)
        buttonUpgradeLife!!.userObject = Settings.LEVEL_LIFE
        initializeButton(buttonUpgradeLife!!, labelPriceLife!!)

        buttonUpgradeEnergy = Button(Assets.styleButtonUpgrade)
        buttonUpgradeEnergy!!.userObject = Settings.LEVEL_ENERGY
        initializeButton(buttonUpgradeEnergy!!, labelPriceEnergy!!)

        buttonUpgradeCoins = Button(Assets.styleButtonUpgrade)
        buttonUpgradeCoins!!.userObject = Settings.LEVEL_COINS
        initializeButton(buttonUpgradeCoins!!, labelPriceCoins!!)

        buttonUpgradeTreasureChest = Button(Assets.styleButtonUpgrade)
        buttonUpgradeTreasureChest!!.userObject = Settings.LEVEL_TREASURE_CHEST
        initializeButton(buttonUpgradeTreasureChest!!, labelPriceTreasureChest!!)
    }

    private fun initializeButton(button: Button, labelPrice: Label) {
        if (button.userObject as Int? == MAX_LEVEL) button.isVisible = false
        button.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                var levelActual = button.userObject as Int

                if (Settings.totalCoins >= calculatePrice(levelActual)) {
                    Settings.totalCoins -= calculatePrice(levelActual)

                    if (button === buttonUpgradeMagnet) {
                        Settings.LEVEL_MAGNET++
                    } else if (button === buttonUpgradeLife) {
                        Settings.LEVEL_LIFE++
                    } else if (button === buttonUpgradeEnergy) {
                        Settings.LEVEL_ENERGY++
                    } else if (button === buttonUpgradeCoins) {
                        Settings.LEVEL_COINS++
                    } else if (button === buttonUpgradeTreasureChest) {
                        Settings.LEVEL_TREASURE_CHEST++
                    }

                    levelActual++
                    button.userObject = levelActual

                    updateLabelPriceAndButton(levelActual, labelPrice, button)
                    setArrays()
                }
            }
        })
    }

    private fun calculatePrice(level: Int): Int {
        return when (level) {
            0 -> PRICE_LEVEL_1
            1 -> PRICE_LEVEL_2
            2 -> PRICE_LEVEL_3
            3 -> PRICE_LEVEL_4
            else -> PRICE_LEVEL_5
        }
    }

    private fun updateLabelPriceAndButton(level: Int, label: Label, button: Button) {
        if (level < MAX_LEVEL) {
            label.setText(calculatePrice(level).toString() + "")
        } else {
            label.isVisible = false
            button.isVisible = false
        }
    }

    private fun setArrays() {
        for (i in 0..<Settings.LEVEL_MAGNET) {
            arrayMagnet[i]!!.setDrawable(TextureRegionDrawable(Assets.buttonShare))
        }

        for (i in 0..<Settings.LEVEL_LIFE) {
            arrayLife[i]!!.setDrawable(TextureRegionDrawable(Assets.buttonShare))
        }

        for (i in 0..<Settings.LEVEL_ENERGY) {
            arrayEnergy[i]!!.setDrawable(TextureRegionDrawable(Assets.buttonShare))
        }

        for (i in 0..<Settings.LEVEL_COINS) {
            arrayCoin[i]!!.setDrawable(TextureRegionDrawable(Assets.buttonShare))
        }

        for (i in 0..<Settings.LEVEL_TREASURE_CHEST) {
            arrayTreasureChest[i]!!.setDrawable(TextureRegionDrawable(Assets.buttonShare))
        }
    }
}
