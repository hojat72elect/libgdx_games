package com.nopalsoft.zombiedash.shop

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.MainZombieDash
import com.nopalsoft.zombiedash.Settings

class GetGemsSubMenu(game: MainZombieDash, contenedor: Table) {
    var monedasLikeFacebook: Int = 1500

    // Comun
    var btLikeFacebook: TextButton
    var btInviteFacebook: TextButton?

    // iOS
    var btBuy5milCoins: TextButton?
    var btBuy15MilCoins: TextButton?
    var btBuy30MilCoins: TextButton?
    var btBuy50MilCoins: TextButton?

    var contenedor: Table?
    var game: MainZombieDash?
    var idiomas: I18NBundle
    var textBuy: String?

    init {
        this.game = game
        this.contenedor = contenedor
        idiomas = game.idiomas
        contenedor.clear()

        textBuy = idiomas.get("buy")

        btLikeFacebook = TextButton(idiomas.get("like_us"), Assets.styleTextButtonBuy)
        if (Settings.didLikeFacebook) btLikeFacebook = TextButton(idiomas.get("visit_us"), Assets.styleTextButtonPurchased)
        addEfectoPress(btLikeFacebook)
        btLikeFacebook.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (!Settings.didLikeFacebook) {
                    Settings.didLikeFacebook = true
                    game.stage.addAction(Actions.sequence(Actions.delay(1f), Actions.run(object : Runnable {
                        override fun run() {
                            Settings.gemsTotal += monedasLikeFacebook
                            btLikeFacebook.setText(idiomas.get("visit_us"))
                            btLikeFacebook.setStyle(Assets.styleTextButtonBuy)
                        }
                    })))
                }
            }
        })

        btInviteFacebook = TextButton(idiomas.get("invite"), Assets.styleTextButtonBuy)
        addEfectoPress(btInviteFacebook!!)


        btBuy5milCoins = TextButton(textBuy, Assets.styleTextButtonBuy)
        addEfectoPress(btBuy5milCoins!!)

        btBuy15MilCoins = TextButton(textBuy, Assets.styleTextButtonBuy)
        addEfectoPress(btBuy15MilCoins!!)

        btBuy30MilCoins = TextButton(textBuy, Assets.styleTextButtonBuy)
        addEfectoPress(btBuy30MilCoins!!)

        btBuy50MilCoins = TextButton(textBuy, Assets.styleTextButtonBuy)
        addEfectoPress(btBuy50MilCoins!!)

        // Facebook Like
        val faceLikeDescription = idiomas.format("facebook_like_description", monedasLikeFacebook)
        val faceInviteDescription = idiomas.format("facebook_invite_description", Settings.NUM_GEMS_INVITE_FACEBOOK)


        contenedor.add<Table?>(agregarPersonajeTabla(monedasLikeFacebook, Assets.btFacebook, faceLikeDescription, btLikeFacebook)).expandX().fill()
        contenedor.row()

        contenedor.add<Table?>(agregarPersonajeTabla(Settings.NUM_GEMS_INVITE_FACEBOOK, Assets.btFacebook, faceInviteDescription, btInviteFacebook))
            .expandX().fill()
        contenedor.row()


        val moneda = TextureRegionDrawable(Assets.itemGem)

        // Venta de monedas

        // Comprar 5mil
        contenedor.add<Table?>(agregarPersonajeTabla(5000, moneda, idiomas.get("gem_simple_pack"), btBuy5milCoins)).expandX().fill()
        contenedor.row()

        // Comprar 15mil
        contenedor.add<Table?>(agregarPersonajeTabla(15000, moneda, idiomas.get("gem_super_pack"), btBuy15MilCoins)).expandX().fill()
        contenedor.row()

        contenedor.add<Table?>(agregarPersonajeTabla(30000, moneda, idiomas.get("gem_mega_pack"), btBuy30MilCoins)).expandX().fill()
        contenedor.row()

        contenedor.add<Table?>(agregarPersonajeTabla(50000, moneda, idiomas.get("gem_super_mega_pack"), btBuy50MilCoins)).expandX().fill()
        contenedor.row()
    }

    private fun agregarPersonajeTabla(numMonedasToGet: Int, imagen: TextureRegionDrawable?, descripcion: String?, boton: TextButton?): Table {
        val moneda = Image(Assets.itemGem)
        val imgPersonaje = Image(imagen)

        val tbBarraTitulo = Table()
        tbBarraTitulo.add<Label?>(Label(idiomas.format("get_num", numMonedasToGet), Assets.labelStyleChico)).left().padLeft(5f)
        tbBarraTitulo.add<Image?>(moneda).left().expandX().padLeft(5f).size(20f)

        val tbDescrip = Table()
        tbDescrip.add<Image?>(imgPersonaje).left().pad(10f).size(55f, 45f)
        val lblDescripcion = Label(descripcion, Assets.labelStyleChico)
        lblDescripcion.setWrap(true)
        lblDescripcion.setFontScale(.9f)
        tbDescrip.add<Label?>(lblDescripcion).expand().fill().padLeft(5f)

        val tbContent = Table()
        tbContent.defaults().padLeft(20f).padRight(20f)
        tbContent.setBackground(Assets.storeTableBackground)
        tbContent.add<Table?>(tbBarraTitulo).expandX().fill().colspan(2).padTop(20f)
        tbContent.row().colspan(2)
        tbContent.add<Table?>(tbDescrip).expandX().fill()
        tbContent.row().colspan(2)

        tbContent.add<TextButton?>(boton).right().padBottom(20f).size(120f, 45f)

        return tbContent
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
}
