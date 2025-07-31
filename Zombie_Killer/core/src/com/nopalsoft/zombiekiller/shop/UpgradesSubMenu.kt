package com.nopalsoft.zombiekiller.shop

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
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.MainZombie
import com.nopalsoft.zombiekiller.Settings

class UpgradesSubMenu(contenedor: Table, game: MainZombie) {
    val maxLevel: Int = 6

    var precioNivel1: Int = 350
    var precioNivel2: Int = 1250
    var precioNivel3: Int = 2500
    var precioNivel4: Int = 3750
    var precioNivel5: Int = 4750
    var precioNivel6: Int = 5750

    var btUpgradeWeapon: TextButton? = null
    var btUpgradeChanceDrop: TextButton? = null
    var btUpgradeLife: TextButton? = null
    var btUpgradeShield: TextButton? = null

    var lbPrecioWeapon: Label? = null
    var lbPrecioChanceDrop: Label? = null
    var lbPrecioLife: Label? = null
    var lbPrecioShield: Label? = null

    var arrWeapon: Array<Image?>
    var arrChanceDrop: Array<Image?>
    var arrLife: Array<Image?>
    var arrShield: Array<Image?>

    var idiomas: I18NBundle = game.idiomas!!

    var textUpgrade: String?

    init {
        contenedor.clear()

        textUpgrade = idiomas.get("upgrade")

        arrWeapon = arrayOfNulls<Image>(maxLevel)
        arrChanceDrop = arrayOfNulls<Image>(maxLevel)
        arrLife = arrayOfNulls<Image>(maxLevel)
        arrShield = arrayOfNulls<Image>(maxLevel)

        if (Settings.LEVEL_WEAPON < maxLevel) lbPrecioWeapon = Label(calcularPrecio(Settings.LEVEL_WEAPON).toString() + "", Assets.labelStyleChico)

        if (Settings.LEVEL_CHANCE_DROP < maxLevel) lbPrecioChanceDrop = Label(calcularPrecio(Settings.LEVEL_CHANCE_DROP).toString() + "", Assets.labelStyleChico)

        if (Settings.LEVEL_LIFE < maxLevel) lbPrecioLife = Label(calcularPrecio(Settings.LEVEL_LIFE).toString() + "", Assets.labelStyleChico)

        if (Settings.LEVEL_SHIELD < maxLevel) lbPrecioShield = Label(calcularPrecio(Settings.LEVEL_SHIELD).toString() + "", Assets.labelStyleChico)

        inicializarBotones()

        // Upgrade weapon
        contenedor
            .add<Table?>(
                agregarPersonajeTabla(
                    idiomas.get("upgrade_your_weapon"), lbPrecioWeapon, Assets.weapon,
                    idiomas.get("upgrade_your_weapon_description"), arrWeapon, btUpgradeWeapon
                )
            ).expandX().fill()
        contenedor.row()

        // Life
        contenedor
            .add<Table?>(
                agregarPersonajeTabla(
                    idiomas.get("upgrade_your_life"), lbPrecioLife, Assets.itemHeart,
                    idiomas.get("upgrade_your_life_description"), arrLife, btUpgradeLife
                )
            ).expandX().fill()
        contenedor.row()

        // Shield
        contenedor
            .add<Table?>(
                agregarPersonajeTabla(
                    idiomas.get("upgrade_your_shield"), lbPrecioShield, Assets.itemShield,
                    idiomas.get("upgrade_your_shield_description"), arrShield, btUpgradeShield
                )
            ).expandX().fill()
        contenedor.row()

        // Drop chance
        contenedor
            .add<Table?>(
                agregarPersonajeTabla(
                    idiomas.get("drop_chance"), lbPrecioChanceDrop, Assets.itemCollection,
                    idiomas.get("drop_chance_description"), arrChanceDrop, btUpgradeChanceDrop
                )
            ).expandX().fill()
        contenedor.row()

        setArrays()
    }

    private fun agregarPersonajeTabla(titulo: String?, lblPrecio: Label?, imagen: AtlasRegion?, descripcion: String?, arrLevel: Array<Image?>, boton: TextButton?): Table {
        val moneda = Image(Assets.itemGem)
        val imgPersonaje = Image(imagen)

        if (lblPrecio == null) moneda.isVisible = false

        val tbBarraTitulo = Table()
        tbBarraTitulo.add(Label(titulo, Assets.labelStyleChico)).expandX().left()
        tbBarraTitulo.add(moneda).right().size(20f)
        tbBarraTitulo.add<Label?>(lblPrecio).right().padRight(10f)

        val tbDescrip = Table()
        tbDescrip.add(imgPersonaje).left().pad(5f).size(55f, 48f)
        val lblDescripcion = Label(descripcion, Assets.labelStyleChico)
        lblDescripcion.setWrap(true)
        lblDescripcion.setFontScale(.9f)
        tbDescrip.add(lblDescripcion).expand().fill().padLeft(5f)

        val tbContent = Table()
        tbContent.pad(0f)
        tbContent.setBackground(Assets.storeTableBackground)
        tbContent.defaults().padLeft(20f).padRight(20f)

        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(20f)
        tbContent.row().colspan(2)
        tbContent.add(tbDescrip).expandX().fill()
        tbContent.row().padBottom(20f)

        val auxTab = Table()
        auxTab.defaults().padLeft(5f)
        for (i in 0..<maxLevel) {
            arrLevel[i] = Image(Assets.upgradeOff)
            auxTab.add<Image?>(arrLevel[i]).width(25f).height(25f)
        }

        tbContent.add(auxTab).left().expand().padRight(0f)
        tbContent.add<TextButton?>(boton).left().size(120f, 45f).padLeft(0f)

        return tbContent
    }

    private fun inicializarBotones() {
        btUpgradeWeapon = TextButton(textUpgrade, Assets.styleTextButtonPurchased)
        if (Settings.LEVEL_WEAPON == maxLevel) btUpgradeWeapon!!.isVisible = false
        addEfectoPress(btUpgradeWeapon!!)
        btUpgradeWeapon!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.gemsTotal >= calcularPrecio(Settings.LEVEL_WEAPON)) {
                    Settings.gemsTotal -= calcularPrecio(Settings.LEVEL_WEAPON)
                    Settings.LEVEL_WEAPON++
                    updateLabelPriceAndButton(Settings.LEVEL_WEAPON, lbPrecioWeapon!!, btUpgradeWeapon!!)
                    setArrays()
                }
            }
        })

        // Chance life
        btUpgradeLife = TextButton(textUpgrade, Assets.styleTextButtonPurchased)
        if (Settings.LEVEL_LIFE == maxLevel) btUpgradeLife!!.isVisible = false
        addEfectoPress(btUpgradeLife!!)
        btUpgradeLife!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.gemsTotal >= calcularPrecio(Settings.LEVEL_LIFE)) {
                    Settings.gemsTotal -= calcularPrecio(Settings.LEVEL_LIFE)
                    Settings.LEVEL_LIFE++
                    updateLabelPriceAndButton(Settings.LEVEL_LIFE, lbPrecioLife!!, btUpgradeLife!!)
                    setArrays()
                }
            }
        })

        // Chance shield
        btUpgradeShield = TextButton(textUpgrade, Assets.styleTextButtonPurchased)
        if (Settings.LEVEL_SHIELD == maxLevel) btUpgradeShield!!.isVisible = false
        addEfectoPress(btUpgradeShield!!)
        btUpgradeShield!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.gemsTotal >= calcularPrecio(Settings.LEVEL_SHIELD)) {
                    Settings.gemsTotal -= calcularPrecio(Settings.LEVEL_SHIELD)
                    Settings.LEVEL_SHIELD++
                    updateLabelPriceAndButton(Settings.LEVEL_SHIELD, lbPrecioShield!!, btUpgradeShield!!)
                    setArrays()
                }
            }
        })

        // Chance drop
        btUpgradeChanceDrop = TextButton(textUpgrade, Assets.styleTextButtonPurchased)
        if (Settings.LEVEL_CHANCE_DROP == maxLevel) btUpgradeChanceDrop!!.isVisible = false
        addEfectoPress(btUpgradeChanceDrop!!)
        btUpgradeChanceDrop!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.gemsTotal >= calcularPrecio(Settings.LEVEL_CHANCE_DROP)) {
                    Settings.gemsTotal -= calcularPrecio(Settings.LEVEL_CHANCE_DROP)
                    Settings.LEVEL_CHANCE_DROP++
                    updateLabelPriceAndButton(Settings.LEVEL_CHANCE_DROP, lbPrecioChanceDrop!!, btUpgradeChanceDrop!!)
                    setArrays()
                }
            }
        })
    }

    private fun setArrays() {
        for (i in 0..<Settings.LEVEL_WEAPON) {
            arrWeapon[i]!!.setDrawable(TextureRegionDrawable(Assets.itemSkull))
        }

        for (i in 0..<Settings.LEVEL_CHANCE_DROP) {
            arrChanceDrop[i]!!.setDrawable(TextureRegionDrawable(Assets.itemSkull))
        }

        for (i in 0..<Settings.LEVEL_LIFE) {
            arrLife[i]!!.setDrawable(TextureRegionDrawable(Assets.itemSkull))
        }

        for (i in 0..<Settings.LEVEL_SHIELD) {
            arrShield[i]!!.setDrawable(TextureRegionDrawable(Assets.itemSkull))
        }
    }

    private fun calcularPrecio(nivel: Int): Int {
        return when (nivel) {
            0 -> precioNivel1

            1 -> precioNivel2

            2 -> precioNivel3

            3 -> precioNivel4

            4 -> precioNivel5

            5 -> precioNivel6
            else -> precioNivel6
        }
    }

    private fun updateLabelPriceAndButton(level: Int, label: Label, button: TextButton) {
        if (level < maxLevel) {
            label.setText(calcularPrecio(level).toString() + "")
        } else {
            label.isVisible = false
            button.isVisible = false
        }
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
}
