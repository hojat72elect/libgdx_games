package com.nopalsoft.zombiedash.shop

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
import com.nopalsoft.zombiedash.AnimationSprite
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.MainZombieDash
import com.nopalsoft.zombiedash.Settings
import com.nopalsoft.zombiedash.objects.Hero
import com.nopalsoft.zombiedash.scene2d.AnimatedSpriteActor

class PersonajesSubMenu(contenedor: Table, game: MainZombieDash) {
    val PRECIO_HERO_RAMBO: Int = 1000
    val PRECIO_HERO_SOLDIER: Int = 1500
    val PRECIO_HERO_ELITE: Int = 2000
    val PRECIO_HERO_VADER: Int = 2500
    var didBuyRambo: Boolean = false
    var didBuySoldier: Boolean = false
    var didBuyElite: Boolean = false
    var didBuyVader: Boolean = false
    var lbPrecioRambo: Label? = null
    var lbPrecioSoldier: Label? = null
    var lbPrecioElite: Label? = null
    var lbPrecioVader: Label? = null
    var btBuySWAT: TextButton? = null
    var btBuyRambo: TextButton? = null
    var btBuySoldier: TextButton? = null
    var btBuyElite: TextButton? = null
    var btBuyVader: TextButton? = null
    var arrBotones: Array<TextButton>? = null
    var idiomas: I18NBundle = game.idiomas!!
    var textBuy: String?
    var textSelect: String?

    init {
        contenedor.clear()
        loadPurchases()

        textBuy = idiomas.get("buy")
        textSelect = idiomas.get("select")

        if (!didBuyRambo) lbPrecioRambo = Label(PRECIO_HERO_RAMBO.toString() + "", Assets.labelStyleChico)
        if (!didBuySoldier) lbPrecioSoldier = Label(PRECIO_HERO_SOLDIER.toString() + "", Assets.labelStyleChico)

        if (!didBuyElite) lbPrecioElite = Label(PRECIO_HERO_ELITE.toString() + "", Assets.labelStyleChico)

        if (!didBuyVader) lbPrecioVader = Label(PRECIO_HERO_VADER.toString() + "", Assets.labelStyleChico)

        inicializarBotones()

        // Usar Default
        contenedor.add<Table?>(agregarPersonajeTabla(idiomas.get("swat"), null, Assets.heroSwatRun!!, idiomas.get("swat_description"), btBuySWAT)).expandX()
            .fill()
        contenedor.row()

        // SKIN_HERO_RAMBO
        contenedor
            .add<Table?>(
                agregarPersonajeTabla(
                    idiomas.get("guerrilla"), lbPrecioRambo, Assets.heroRamboRun!!, idiomas.get("guerrilla_description"),
                    btBuyRambo
                )
            ).expandX().fill()
        contenedor.row()

        // SKIN_HERO_SOLDIER
        contenedor.add<Table?>(
            agregarPersonajeTabla(
                idiomas.get("soldier"),  //
                lbPrecioSoldier,  //
                Assets.heroSoldierRun!!,  //
                idiomas.get("soldier_description"),  //
                btBuySoldier
            )
        ).expandX().fill()
        contenedor.row()

        // SKIN_HERO_SWAT
        contenedor.add<Table?>(
            agregarPersonajeTabla(
                idiomas.get("elite_force"),  //
                lbPrecioElite,  //
                Assets.heroForceRun!!,  //
                idiomas.get("elite_force_description"),  //
                btBuyElite
            )
        ).expandX().fill()
        contenedor.row()

        // SKIN_HERO_VADER
        contenedor.add<Table?>(
            agregarPersonajeTabla(
                idiomas.get("ghost"),  //
                lbPrecioVader,  //
                Assets.heroVaderRun!!,  //
                idiomas.get("ghost_description"),  //
                btBuyVader
            )
        ).expandX().fill()
        contenedor.row()
    }

    private fun agregarPersonajeTabla(titulo: String?, lblPrecio: Label?, imagen: AnimationSprite, descripcion: String?, boton: TextButton?): Table {
        val moneda = Image(Assets.itemGem)
        val imgPersonaje = AnimatedSpriteActor(imagen)

        if (lblPrecio == null) moneda.isVisible = false

        val tbBarraTitulo = Table()
        tbBarraTitulo.add(Label(titulo, Assets.labelStyleChico)).expandX().left()
        tbBarraTitulo.add(moneda).right().size(20f)
        tbBarraTitulo.add<Label?>(lblPrecio).right().padRight(10f)

        val tbContent = Table()
        tbContent.setBackground(Assets.storeTableBackground)

        tbContent.defaults().padLeft(20f).padRight(20f)
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(20f)
        tbContent.row()
        tbContent.add(imgPersonaje).left().size(70f, 70f)

        val lblDescripcion = Label(descripcion, Assets.labelStyleChico)
        lblDescripcion.setWrap(true)
        lblDescripcion.setFontScale(.9f)
        tbContent.add(lblDescripcion).expand().fill().padLeft(5f)

        tbContent.row().colspan(2)
        tbContent.add<TextButton?>(boton).expandX().right().padBottom(20f).size(120f, 45f)
        tbContent.row().colspan(2)

        return tbContent
    }

    private fun inicializarBotones() {
        arrBotones = Array<TextButton>()

        // DEFAULT
        btBuySWAT = TextButton(textSelect, Assets.styleTextButtonPurchased)
        if (Settings.skinSeleccionada == Hero.TIPO_SWAT) btBuySWAT!!.isVisible = false

        addEfectoPress(btBuySWAT!!)
        btBuySWAT!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.skinSeleccionada = Hero.TIPO_SWAT
                setSelected(btBuySWAT!!)
            }
        })

        // SKIN_HERO_RAMBO
        btBuyRambo = if (didBuyRambo) TextButton(textSelect, Assets.styleTextButtonPurchased)
        else TextButton(textBuy, Assets.styleTextButtonBuy)

        if (Settings.skinSeleccionada == Hero.TIPO_RAMBO) btBuyRambo!!.isVisible = false

        addEfectoPress(btBuyRambo!!)
        btBuyRambo!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (didBuyRambo) {
                    Settings.skinSeleccionada = Hero.TIPO_RAMBO
                    setSelected(btBuyRambo!!)
                } else if (Settings.gemsTotal >= PRECIO_HERO_RAMBO) {
                    Settings.gemsTotal -= PRECIO_HERO_RAMBO
                    setButtonStylePurchased(btBuyRambo!!)
                    lbPrecioRambo!!.remove()
                    didBuyRambo = true
                }
                savePurchases()
            }
        })

        // SKIN_HERO_SOLDIER
        btBuySoldier = if (didBuySoldier) TextButton(textSelect, Assets.styleTextButtonPurchased)
        else TextButton(textBuy, Assets.styleTextButtonBuy)

        if (Settings.skinSeleccionada == Hero.TIPO_SOLDIER) btBuySoldier!!.isVisible = false

        addEfectoPress(btBuySoldier!!)
        btBuySoldier!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (didBuySoldier) {
                    Settings.skinSeleccionada = Hero.TIPO_SOLDIER
                    setSelected(btBuySoldier!!)
                } else if (Settings.gemsTotal >= PRECIO_HERO_SOLDIER) {
                    Settings.gemsTotal -= PRECIO_HERO_SOLDIER
                    setButtonStylePurchased(btBuySoldier!!)
                    lbPrecioSoldier!!.remove()
                    didBuySoldier = true
                }
                savePurchases()
            }
        })

        // SKIN_HERO_SWAT
        btBuyElite = if (didBuyElite) TextButton(textSelect, Assets.styleTextButtonPurchased)
        else TextButton(textBuy, Assets.styleTextButtonBuy)

        if (Settings.skinSeleccionada == Hero.TIPO_FORCE) btBuyElite!!.isVisible = false

        addEfectoPress(btBuyElite!!)
        btBuyElite!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (didBuyElite) {
                    Settings.skinSeleccionada = Hero.TIPO_FORCE
                    setSelected(btBuyElite!!)
                } else if (Settings.gemsTotal >= PRECIO_HERO_ELITE) {
                    Settings.gemsTotal -= PRECIO_HERO_ELITE
                    setButtonStylePurchased(btBuyElite!!)
                    lbPrecioElite!!.remove()
                    didBuyElite = true
                }
                savePurchases()
            }
        })

        // SKIN_HERO_VADER
        btBuyVader = if (didBuyVader) TextButton(textSelect, Assets.styleTextButtonPurchased)
        else TextButton(textBuy, Assets.styleTextButtonBuy)

        if (Settings.skinSeleccionada == Hero.TIPO_VADER) btBuyVader!!.isVisible = false

        addEfectoPress(btBuyVader!!)
        btBuyVader!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (didBuyVader) {
                    Settings.skinSeleccionada = Hero.TIPO_VADER
                    setSelected(btBuyVader!!)
                } else if (Settings.gemsTotal >= PRECIO_HERO_VADER) {
                    Settings.gemsTotal -= PRECIO_HERO_VADER
                    setButtonStylePurchased(btBuyVader!!)
                    lbPrecioVader!!.remove()
                    didBuyVader = true
                }
                savePurchases()
            }
        })

        arrBotones!!.add(btBuySWAT)
        arrBotones!!.add(btBuyRambo)
        arrBotones!!.add(btBuySoldier)
        arrBotones!!.add(btBuyElite)
        arrBotones!!.add(btBuyVader)
    }

    private fun loadPurchases() {
        didBuyRambo = pref.getBoolean("didBuyRambo", false)
        didBuySoldier = pref.getBoolean("didBuySoldier", false)
        didBuyElite = pref.getBoolean("didBuyElite", false)
        didBuyVader = pref.getBoolean("didBuyVader", false)
    }

    private fun savePurchases() {
        pref.putBoolean("didBuyRambo", didBuyRambo)
        pref.putBoolean("didBuySoldier", didBuySoldier)
        pref.putBoolean("didBuyElite", didBuyElite)
        pref.putBoolean("didBuyVader", didBuyVader)
        pref.flush()
        Settings.save()
    }

    private fun setButtonStylePurchased(boton: TextButton) {
        boton.setStyle(Assets.styleTextButtonPurchased)
        boton.setText(textSelect)
    }

    private fun setSelected(boton: TextButton) {
        // Pongo todos visibles y al final el boton seleccionado en invisible
        for (arrBotone in arrBotones!!) {
            arrBotone.isVisible = true
        }
        boton.isVisible = false
    }

    private fun addEfectoPress(actor: Actor) {
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
        private val pref: Preferences = Gdx.app.getPreferences("com.nopalsoft.zombiedash.shop")
    }
}
