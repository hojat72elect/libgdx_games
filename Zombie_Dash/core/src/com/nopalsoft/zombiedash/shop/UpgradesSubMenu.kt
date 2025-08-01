package com.nopalsoft.zombiedash.shop

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
import com.nopalsoft.zombiedash.AnimationSprite
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.MainZombieDash
import com.nopalsoft.zombiedash.Settings
import com.nopalsoft.zombiedash.scene2d.AnimatedSpriteActor

class UpgradesSubMenu(contenedor: Table, game: MainZombieDash) {
    val MAX_LEVEL: Int = 6

    var precioNivel1: Int = 350
    var precioNivel2: Int = 1250
    var precioNivel3: Int = 2500
    var precioNivel4: Int = 3750
    var precioNivel5: Int = 4750
    var precioNivel6: Int = 5750

    var precio100Bullets: Int = 250
    var precio250Bullets: Int = 500
    var precio500Bullets: Int = 750
    var precio1000Bullets: Int = 1000

    var btBuy100Bullets: TextButton? = null
    var btBuy250Bullets: TextButton? = null
    var btBuy500Bullets: TextButton? = null
    var btBuy1000Bullets: TextButton? = null

    var lbPrecio100Bullets: Label?
    var lbPrecio250Bullets: Label?
    var lbPrecio500Bullets: Label?
    var lbPrecio1000Bullets: Label?

    var btUpgradeWeapon: TextButton? = null
    var btUpgradeSecondJump: TextButton? = null
    var btUpgradeLife: TextButton? = null
    var btUpgradeShield: TextButton? = null

    var lbPrecioWeapon: Label? = null
    var lbPrecioSecondJump: Label? = null
    var lbPrecioLife: Label? = null
    var lbPrecioShield: Label? = null

    var arrWeapon: Array<Image?>
    var arrSecondJump: Array<Image?>
    var arrLife: Array<Image?>
    var arrShield: Array<Image?>

    var idiomas: I18NBundle = game.idiomas!!

    var textUpgrade: String?
    var textBuy: String?

    init {
        contenedor.clear()

        textUpgrade = idiomas.get("upgrade")
        textBuy = idiomas.get("buy")

        arrWeapon = arrayOfNulls<Image>(MAX_LEVEL)
        arrSecondJump = arrayOfNulls<Image>(MAX_LEVEL)
        arrLife = arrayOfNulls<Image>(MAX_LEVEL)
        arrShield = arrayOfNulls<Image>(MAX_LEVEL)

        lbPrecio100Bullets = Label(precio100Bullets.toString() + "", Assets.labelStyleChico)
        lbPrecio250Bullets = Label(precio250Bullets.toString() + "", Assets.labelStyleChico)
        lbPrecio500Bullets = Label(precio500Bullets.toString() + "", Assets.labelStyleChico)
        lbPrecio1000Bullets = Label(precio1000Bullets.toString() + "", Assets.labelStyleChico)

        if (Settings.LEVEL_WEAPON < MAX_LEVEL) lbPrecioWeapon = Label(calcularPrecio(Settings.LEVEL_WEAPON).toString() + "", Assets.labelStyleChico)

        if (Settings.LEVEL_SECOND_JUMP < MAX_LEVEL) lbPrecioSecondJump = Label(calcularPrecio(Settings.LEVEL_SECOND_JUMP).toString() + "", Assets.labelStyleChico)

        if (Settings.LEVEL_LIFE < MAX_LEVEL) lbPrecioLife = Label(calcularPrecio(Settings.LEVEL_LIFE).toString() + "", Assets.labelStyleChico)

        if (Settings.LEVEL_SHIELD < MAX_LEVEL) lbPrecioShield = Label(calcularPrecio(Settings.LEVEL_SHIELD).toString() + "", Assets.labelStyleChico)

        inicializarBotones()

        contenedor
            .add<Table?>(
                agregarCompra(
                    idiomas.format("bulles_x_num", 100), lbPrecio100Bullets, Assets.bullet1!!, idiomas.get("buy_100_bullets"),
                    btBuy100Bullets
                )
            ).expandX().fill()
        contenedor.row()

        contenedor
            .add<Table?>(
                agregarCompra(
                    idiomas.format("bulles_x_num", 250), lbPrecio250Bullets, Assets.bullet1!!, idiomas.get("buy_250_bullets"),
                    btBuy250Bullets
                )
            ).expandX().fill()
        contenedor.row()

        contenedor
            .add<Table?>(
                agregarCompra(
                    idiomas.format("bulles_x_num", 500), lbPrecio500Bullets, Assets.bullet1!!, idiomas.get("buy_500_bullets"),
                    btBuy500Bullets
                )
            ).expandX().fill()
        contenedor.row()

        contenedor
            .add<Table?>(
                agregarCompra(
                    idiomas.format("bulles_x_num", 1000), lbPrecio1000Bullets, Assets.bullet1!!, idiomas.get("buy_1000_bullets"),
                    btBuy1000Bullets
                )
            ).expandX().fill()
        contenedor.row()

        // Upgrade weapon
        contenedor
            .add<Table?>(
                agregarUpgrades(
                    idiomas.get("upgrade_your_weapon"), lbPrecioWeapon, Assets.weapon,
                    idiomas.get("upgrade_your_weapon_description"), arrWeapon, btUpgradeWeapon
                )
            ).expandX().fill()
        contenedor.row()

        // Life
        contenedor
            .add<Table?>(
                agregarUpgrades(
                    idiomas.get("upgrade_your_life"), lbPrecioLife, Assets.itemHeart, idiomas.get("upgrade_your_life_description"),
                    arrLife, btUpgradeLife
                )
            ).expandX().fill()
        contenedor.row()

        // Shield
        contenedor
            .add<Table?>(
                agregarUpgrades(
                    idiomas.get("upgrade_your_shield"), lbPrecioShield, Assets.itemShield,
                    idiomas.get("upgrade_your_shield_description"), arrShield, btUpgradeShield
                )
            ).expandX().fill()
        contenedor.row()

        // Drop chance
        contenedor
            .add<Table?>(
                agregarUpgrades(
                    idiomas.get("upgrade_second_jump"), lbPrecioSecondJump, Assets.itemJump,
                    idiomas.get("upgrade_second_jump_description"), arrSecondJump, btUpgradeSecondJump
                )
            ).expandX().fill()
        contenedor.row()

        setArrays()
    }

    private fun agregarCompra(titulo: String?, lblPrecio: Label?, imagen: AnimationSprite, descripcion: String?, boton: TextButton?): Table {
        val moneda = Image(Assets.itemGem)
        val imgPersonaje = AnimatedSpriteActor(imagen)

        if (lblPrecio == null) moneda.isVisible = false

        val tbBarraTitulo = Table()
        tbBarraTitulo.add(Label(titulo, Assets.labelStyleChico)).expandX().left()
        tbBarraTitulo.add(moneda).right().size(20f)
        tbBarraTitulo.add<Label?>(lblPrecio).right().padRight(10f)

        val tbDescrip = Table()
        tbDescrip.add(imgPersonaje).center().pad(5f).size(30f, 30f)
        val lblDescripcion = Label(descripcion, Assets.labelStyleChico)
        lblDescripcion.setWrap(true)
        lblDescripcion.setFontScale(.9f)
        tbDescrip.add(lblDescripcion).expand().fill().padLeft(5f)

        val tbContent = Table()
        // tbContent.debug();
        tbContent.setBackground(Assets.storeTableBackground)
        tbContent.defaults().padLeft(20f).padRight(20f)

        tbContent.add(tbBarraTitulo).expandX().fill().colspan(1).padTop(20f)

        tbContent.row().colspan(1)
        tbContent.add(tbDescrip).expandX().fill()

        tbContent.row().padBottom(20f)
        tbContent.add<TextButton?>(boton).expandX().right().size(120f, 45f)

        return tbContent
    }

    private fun agregarUpgrades(titulo: String?, lblPrecio: Label?, imagen: AtlasRegion?, descripcion: String?, arrLevel: Array<Image?>, boton: TextButton?): Table {
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
        // tbContent.debug();
        tbContent.setBackground(Assets.storeTableBackground)
        tbContent.defaults().padLeft(20f).padRight(20f)

        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(20f)
        tbContent.row().colspan(2)
        tbContent.add(tbDescrip).expandX().fill()
        tbContent.row().padBottom(20f)

        val auxTab = Table()
        auxTab.defaults().padLeft(5f)
        for (i in 0..<MAX_LEVEL) {
            arrLevel[i] = Image(Assets.upgradeOff)
            auxTab.add<Image?>(arrLevel[i]).width(25f).height(25f)
        }

        tbContent.add(auxTab).left().expand().padRight(0f)
        tbContent.add<TextButton?>(boton).left().size(120f, 45f).padLeft(0f)

        return tbContent
    }

    private fun inicializarBotones() {
        btUpgradeWeapon = TextButton(textUpgrade, Assets.styleTextButtonPurchased)
        if (Settings.LEVEL_WEAPON == MAX_LEVEL) btUpgradeWeapon!!.isVisible = false
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
        if (Settings.LEVEL_LIFE == MAX_LEVEL) btUpgradeLife!!.isVisible = false
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
        if (Settings.LEVEL_SHIELD == MAX_LEVEL) btUpgradeShield!!.isVisible = false
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
        btUpgradeSecondJump = TextButton(textUpgrade, Assets.styleTextButtonPurchased)
        if (Settings.LEVEL_SECOND_JUMP == MAX_LEVEL) btUpgradeSecondJump!!.isVisible = false
        addEfectoPress(btUpgradeSecondJump!!)
        btUpgradeSecondJump!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.gemsTotal >= calcularPrecio(Settings.LEVEL_SECOND_JUMP)) {
                    Settings.gemsTotal -= calcularPrecio(Settings.LEVEL_SECOND_JUMP)
                    Settings.LEVEL_SECOND_JUMP++
                    updateLabelPriceAndButton(Settings.LEVEL_SECOND_JUMP, lbPrecioSecondJump!!, btUpgradeSecondJump!!)
                    setArrays()
                }
            }
        })

        // Comprar 100
        btBuy100Bullets = TextButton(textBuy, Assets.styleTextButtonPurchased)
        addEfectoPress(btBuy100Bullets!!)
        btBuy100Bullets!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.gemsTotal >= precio100Bullets) {
                    Settings.gemsTotal -= precio100Bullets
                    Settings.numBullets += 100
                }
            }
        })

        // Comprar 250
        btBuy250Bullets = TextButton(textBuy, Assets.styleTextButtonPurchased)
        addEfectoPress(btBuy250Bullets!!)
        btBuy250Bullets!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.gemsTotal >= precio250Bullets) {
                    Settings.gemsTotal -= precio250Bullets
                    Settings.numBullets += 250
                }
            }
        })

        // Comprar 500
        btBuy500Bullets = TextButton(textBuy, Assets.styleTextButtonPurchased)
        addEfectoPress(btBuy500Bullets!!)
        btBuy500Bullets!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.gemsTotal >= precio500Bullets) {
                    Settings.gemsTotal -= precio500Bullets
                    Settings.numBullets += 500
                }
            }
        })

        // Comprar 1000
        btBuy1000Bullets = TextButton(textBuy, Assets.styleTextButtonPurchased)
        addEfectoPress(btBuy1000Bullets!!)
        btBuy1000Bullets!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.gemsTotal >= precio1000Bullets) {
                    Settings.gemsTotal -= precio1000Bullets
                    Settings.numBullets += 1000
                }
            }
        })
    }

    private fun setArrays() {
        for (i in 0..<Settings.LEVEL_WEAPON) {
            arrWeapon[i]!!.setDrawable(TextureRegionDrawable(Assets.itemSkull))
        }

        for (i in 0..<Settings.LEVEL_SECOND_JUMP) {
            arrSecondJump[i]!!.setDrawable(TextureRegionDrawable(Assets.itemSkull))
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

    private fun updateLabelPriceAndButton(nivel: Int, label: Label, boton: TextButton) {
        if (nivel < MAX_LEVEL) {
            label.setText(calcularPrecio(nivel).toString() + "")
        } else {
            label.isVisible = false
            boton.isVisible = false
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
