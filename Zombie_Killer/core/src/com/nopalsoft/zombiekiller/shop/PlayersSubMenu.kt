package com.nopalsoft.zombiekiller.shop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.zombiekiller.AnimationSprite
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.MainZombie
import com.nopalsoft.zombiekiller.Settings
import com.nopalsoft.zombiekiller.game_objects.Hero
import com.nopalsoft.zombiekiller.scene2d.AnimatedSpriteActor

class PlayersSubMenu(containerTable: Table, game: MainZombie) {
    val PRICE_HERO_RAMBO: Int = 1000
    val PRICE_HERO_SOLDIER: Int = 1500
    val PRICE_HERO_ELITE: Int = 2000
    val PRICE_HERO_VADER: Int = 2500

    var didBuyRambo: Boolean = false
    var didBuySoldier: Boolean = false
    var didBuyElite: Boolean = false
    var didBuyVader: Boolean = false

    var labelPriceRambo: Label? = null
    var labelPriceSoldier: Label? = null
    var labelPriceElite: Label? = null
    var labelPriceVader: Label? = null
    var buttonBuySWAT: TextButton? = null
    var buttonBuyRambo: TextButton? = null
    var buttonBuySoldier: TextButton? = null
    var buttonBuyElite: TextButton? = null
    var buttonBuyVader: TextButton? = null
    var buttons: Array<TextButton>? = null
    var containerTable: Table?
    var languagesBundle: I18NBundle
    var textBuy: String?
    var textSelect: String?

    init {
        languagesBundle = game.idiomas!!
        this.containerTable = containerTable
        containerTable.clear()
        loadPurchases()

        textBuy = languagesBundle.get("buy")
        textSelect = languagesBundle.get("select")

        if (!didBuyRambo) labelPriceRambo = Label(PRICE_HERO_RAMBO.toString() + "", Assets.labelStyleChico)

        if (!didBuySoldier) labelPriceSoldier = Label(PRICE_HERO_SOLDIER.toString() + "", Assets.labelStyleChico)

        if (!didBuyElite) labelPriceElite = Label(PRICE_HERO_ELITE.toString() + "", Assets.labelStyleChico)

        if (!didBuyVader) labelPriceVader = Label(PRICE_HERO_VADER.toString() + "", Assets.labelStyleChico)

        createButtons()

        containerTable.add<Table?>(createPlayerTable(languagesBundle.get("swat"), null, Assets.heroSwatWalk!!, languagesBundle.get("swat_description"), buttonBuySWAT)).expandX().fill()
        containerTable.row()

        containerTable.add<Table?>(createPlayerTable(languagesBundle.get("guerrilla"), labelPriceRambo, Assets.heroRamboWalk!!, languagesBundle.get("guerrilla_description"), buttonBuyRambo)).expandX()
            .fill()
        containerTable.row()

        containerTable.add<Table?>(createPlayerTable(languagesBundle.get("soldier"), labelPriceSoldier, Assets.heroSoldierWalk!!, languagesBundle.get("soldier_description"), buttonBuySoldier))
            .expandX()
            .fill()
        containerTable.row()

        containerTable.add<Table?>(createPlayerTable(languagesBundle.get("elite_force"), labelPriceElite, Assets.heroForceWalk!!, languagesBundle.get("elite_force_description"), buttonBuyElite))
            .expandX().fill()
        containerTable.row()

        containerTable.add<Table?>(createPlayerTable(languagesBundle.get("ghost"), labelPriceVader, Assets.heroVaderWalk!!, languagesBundle.get("ghost_description"), buttonBuyVader)).expandX().fill()
        containerTable.row()
    }

    private fun createPlayerTable(title: String?, priceLabel: Label?, playerAnimation: AnimationSprite, description: String?, button: TextButton?): Table {
        val coinImage = Image(Assets.itemGem)
        val playerSpriteActor = AnimatedSpriteActor(playerAnimation)

        if (priceLabel == null) coinImage.isVisible = false

        val titleBarTable = Table()
        titleBarTable.add<Label?>(Label(title, Assets.labelStyleChico)).expandX().left()
        titleBarTable.add<Image?>(coinImage).right().size(20f)
        titleBarTable.add<Label?>(priceLabel).right().padRight(10f)

        val tbContent = Table()
        tbContent.pad(0f)
        tbContent.setBackground(Assets.storeTableBackground)

        tbContent.defaults().padLeft(20f).padRight(20f)
        tbContent.add<Table?>(titleBarTable).expandX().fill().colspan(2).padTop(20f)
        tbContent.row()
        tbContent.add<AnimatedSpriteActor?>(playerSpriteActor).left().size(70f, 70f)

        val descriptionLabel = Label(description, Assets.labelStyleChico)
        descriptionLabel.setWrap(true)
        descriptionLabel.setFontScale(.9f)
        tbContent.add<Label?>(descriptionLabel).expand().fill().padLeft(5f)

        tbContent.row().colspan(2)
        tbContent.add<TextButton?>(button).expandX().right().padBottom(20f).size(120f, 45f)
        tbContent.row().colspan(2)

        return tbContent
    }

    private fun createButtons() {
        buttons = Array<TextButton>()

        buttonBuySWAT = TextButton(textSelect, Assets.styleTextButtonPurchased)
        if (Settings.skinSeleccionada == Hero.TYPE_SWAT) buttonBuySWAT!!.isVisible = false

        addEfectoPress(buttonBuySWAT!!)
        buttonBuySWAT!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.skinSeleccionada = Hero.TYPE_SWAT
                setSelected(buttonBuySWAT!!)
            }
        })

        if (didBuyRambo) buttonBuyRambo = TextButton(textSelect, Assets.styleTextButtonPurchased)
        else buttonBuyRambo = TextButton(textBuy, Assets.styleTextButtonBuy)

        if (Settings.skinSeleccionada == Hero.TYPE_RAMBO) buttonBuyRambo!!.isVisible = false

        addEfectoPress(buttonBuyRambo!!)
        buttonBuyRambo!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (didBuyRambo) {
                    Settings.skinSeleccionada = Hero.TYPE_RAMBO
                    setSelected(buttonBuyRambo!!)
                } else if (Settings.gemsTotal >= PRICE_HERO_RAMBO) {
                    Settings.gemsTotal -= PRICE_HERO_RAMBO
                    setButtonStylePurchased(buttonBuyRambo!!)
                    labelPriceRambo!!.remove()
                    didBuyRambo = true
                }
                savePurchases()
            }
        })

        if (didBuySoldier) buttonBuySoldier = TextButton(textSelect, Assets.styleTextButtonPurchased)
        else buttonBuySoldier = TextButton(textBuy, Assets.styleTextButtonBuy)

        if (Settings.skinSeleccionada == Hero.TYPE_SOLDIER) buttonBuySoldier!!.isVisible = false

        addEfectoPress(buttonBuySoldier!!)
        buttonBuySoldier!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (didBuySoldier) {
                    Settings.skinSeleccionada = Hero.TYPE_SOLDIER
                    setSelected(buttonBuySoldier!!)
                } else if (Settings.gemsTotal >= PRICE_HERO_SOLDIER) {
                    Settings.gemsTotal -= PRICE_HERO_SOLDIER
                    setButtonStylePurchased(buttonBuySoldier!!)
                    labelPriceSoldier!!.remove()
                    didBuySoldier = true
                }
                savePurchases()
            }
        })

        if (didBuyElite) buttonBuyElite = TextButton(textSelect, Assets.styleTextButtonPurchased)
        else buttonBuyElite = TextButton(textBuy, Assets.styleTextButtonBuy)

        if (Settings.skinSeleccionada == Hero.TYPE_FORCE) buttonBuyElite!!.isVisible = false

        addEfectoPress(buttonBuyElite!!)
        buttonBuyElite!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (didBuyElite) {
                    Settings.skinSeleccionada = Hero.TYPE_FORCE
                    setSelected(buttonBuyElite!!)
                } else if (Settings.gemsTotal >= PRICE_HERO_ELITE) {
                    Settings.gemsTotal -= PRICE_HERO_ELITE
                    setButtonStylePurchased(buttonBuyElite!!)
                    labelPriceElite!!.remove()
                    didBuyElite = true
                }
                savePurchases()
            }
        })

        if (didBuyVader) buttonBuyVader = TextButton(textSelect, Assets.styleTextButtonPurchased)
        else buttonBuyVader = TextButton(textBuy, Assets.styleTextButtonBuy)

        if (Settings.skinSeleccionada == Hero.TYPE_VADER) buttonBuyVader!!.isVisible = false

        addEfectoPress(buttonBuyVader!!)
        buttonBuyVader!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (didBuyVader) {
                    Settings.skinSeleccionada = Hero.TYPE_VADER
                    setSelected(buttonBuyVader!!)
                } else if (Settings.gemsTotal >= PRICE_HERO_VADER) {
                    Settings.gemsTotal -= PRICE_HERO_VADER
                    setButtonStylePurchased(buttonBuyVader!!)
                    labelPriceVader!!.remove()
                    didBuyVader = true
                }
                savePurchases()
            }
        })

        buttons!!.add(buttonBuySWAT)
        buttons!!.add(buttonBuyRambo)
        buttons!!.add(buttonBuySoldier)
        buttons!!.add(buttonBuyElite)
        buttons!!.add(buttonBuyVader)
    }

    private fun loadPurchases() {
        didBuyRambo = preferences.getBoolean("didBuyRambo", false)
        didBuySoldier = preferences.getBoolean("didBuySoldier", false)
        didBuyElite = preferences.getBoolean("didBuyElite", false)
        didBuyVader = preferences.getBoolean("didBuyVader", false)
    }

    private fun savePurchases() {
        preferences.putBoolean("didBuyRambo", didBuyRambo)
        preferences.putBoolean("didBuySoldier", didBuySoldier)
        preferences.putBoolean("didBuyElite", didBuyElite)
        preferences.putBoolean("didBuyVader", didBuyVader)
        preferences.flush()
        Settings.save()
    }

    private fun setButtonStylePurchased(boton: TextButton) {
        boton.setStyle(Assets.styleTextButtonPurchased)
        boton.setText(textSelect)
    }

    private fun setSelected(button: TextButton) {
        // I make all visible and at the end the selected button invisible.
        for (arrBotone in buttons!!) {
            arrBotone.isVisible = true
        }
        button.isVisible = false
    }

    protected fun addEfectoPress(actor: Actor) {
        actor.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                actor.setPosition(actor.getX(), actor.getY() - 3)
                event.stop()
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                actor.setPosition(actor.getX(), actor.getY() + 3)
            }
        })
    }

    companion object {
        private val preferences: Preferences = Gdx.app.getPreferences("com.nopalsoft.zombiekiller.shop")
    }
}
