package com.nopalsoft.dragracer.shop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Array
import com.nopalsoft.dragracer.Assets
import com.nopalsoft.dragracer.MainStreet
import com.nopalsoft.dragracer.Settings
import com.nopalsoft.dragracer.Settings.save

class CharactersSubMenu(var game: MainStreet, container: Table) {


    var didBuyBanshee  = false
    var didBuyTornado  = false
    var didBuyTurismo  = false
    var didBuyAudiS5  = false
    var didBuyBmwX6  = false
    var didBuyBullet  = false
    var didBuyCrossfire  = false
    var didBuyCitroenC4  = false
    var didBuyDodgeCharger  = false
    var didBuyFiat500 = false
    var didBuyHondaCRV = false
    var didBuyMazda6 = false
    var didBuyMazdaRX8 = false
    var didBuySeatIbiza = false
    var didBuyVolkswagenScirocco = false
    var buttonBuyDiablo: TextButton? = null
    var buttonBuyBanshee: TextButton? = null
    var buttonBuyTornado: TextButton? = null
    var buttonBuyTurismo: TextButton? = null
    var buttonBuyAudiS5: TextButton? = null
    var buttonBuyBmwX6: TextButton? = null
    var buttonBuyBullet: TextButton? = null
    var buttonBuyCrossfire: TextButton? = null
    var buttonBuyCitroenC4: TextButton? = null
    var buttonBuyDodgeCharger: TextButton? = null
    var buttonBuyFiat500Lounge: TextButton? = null
    var buttonBuyHondaCRV: TextButton? = null
    var buttonBuyMazda6: TextButton? = null
    var buttonBuyMazdaRX8: TextButton? = null
    var buttonBuySeatIbiza: TextButton? = null
    var buttonBuyVolkswagenScirocco: TextButton? = null
    private var arrayButtons: Array<TextButton>? = null

    init {
        loadPurchases()

        container.clear()

        var labelPriceBanshee: Label? = null
        var labelPriceTornado: Label? = null
        var labelPriceTurismo: Label? = null
        var labelPriceAudiS5: Label? = null
        var labelPriceBmwX6: Label? = null
        var labelPriceCamaro: Label? = null
        var labelPriceCrossfire: Label? = null
        var labelPriceCitroenC4: Label? = null
        var labelPriceDodgeCharger: Label? = null
        var labelPriceFiat500Lounge: Label? = null
        var labelPriceHondeCRV: Label? = null
        var labelPriceMazda6: Label? = null
        var labelPriceMazdaRX8: Label? = null
        var labelPriceSeatIbiza: Label? = null
        var labelPriceVolkswagenScirocco: Label? = null

        if (!didBuyBanshee) labelPriceBanshee =
            Label(PRICE_BANSHEE.toString() + "", Assets.labelStyleSmall)
        if (!didBuyTornado) labelPriceTornado =
            Label(PRICE_TORNADO.toString() + "", Assets.labelStyleSmall)

        if (!didBuyTurismo) labelPriceTurismo =
            Label(PRICE_TURISM.toString() + "", Assets.labelStyleSmall)

        if (!didBuyAudiS5) labelPriceAudiS5 =
            Label(PRICE_CAR_AUDI_S5.toString() + "", Assets.labelStyleSmall)

        if (!didBuyBmwX6) labelPriceBmwX6 =
            Label(PRICE_CAR_BMW_X6.toString() + "", Assets.labelStyleSmall)

        if (!didBuyBullet) labelPriceCamaro =
            Label(PRICE_BULLET.toString() + "", Assets.labelStyleSmall)

        if (!didBuyCrossfire) labelPriceCrossfire =
            Label(PRICE_CAR_CHEVROLET_CROSSFIRE.toString() + "", Assets.labelStyleSmall)

        if (!didBuyCitroenC4) labelPriceCitroenC4 =
            Label(PRICE_CAR_CITROEN_C4.toString() + "", Assets.labelStyleSmall)

        if (!didBuyDodgeCharger) labelPriceDodgeCharger =
            Label(PRICE_CAR_DODGE_CHARGER.toString() + "", Assets.labelStyleSmall)

        if (!didBuyFiat500) labelPriceFiat500Lounge =
            Label(PRICE_CAR_FIAT_500_LOUNGE.toString() + "", Assets.labelStyleSmall)

        if (!didBuyHondaCRV) labelPriceHondeCRV =
            Label(PRICE_CAR_HONDA_CRV.toString() + "", Assets.labelStyleSmall)

        if (!didBuyMazda6) labelPriceMazda6 =
            Label(PRICE_CAR_MAZDA_6.toString() + "", Assets.labelStyleSmall)

        if (!didBuyMazdaRX8) labelPriceMazdaRX8 =
            Label(PRICE_CAR_MAZDA_RX8.toString() + "", Assets.labelStyleSmall)

        if (!didBuySeatIbiza) labelPriceSeatIbiza =
            Label(PRICE_CAR_SEAT_IBIZA.toString() + "", Assets.labelStyleSmall)

        if (!didBuyVolkswagenScirocco) labelPriceVolkswagenScirocco =
            Label(PRICE_CAR_VOLKSWAGEN_SCIROCCO.toString() + "", Assets.labelStyleSmall)

        initializeButtons()

        container.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f)
        container.row()

        // User Default
        container
            .add(
                agregarPersonajeTabla(
                    "Diablo",
                    null,
                    Assets.carDevil,
                    "Good car. It's not the fastest, but it's got great handling although maybe a little too twitchy for some.",
                    buttonBuyDiablo
                )
            ).expandX().fill()
        container.row()

        // SKIN_CARRO_BANSHEE
        container
            .add(
                agregarPersonajeTabla(
                    "Banshee",
                    labelPriceBanshee,
                    Assets.carBanshee,
                    "Looks great and drives even better. Awesome acceleration and slight oversteer make this a thrilling ride.",
                    buttonBuyBanshee
                )
            ).expandX().fill()
        container.row()

        // SKIN_CARRO_TORNADO
        container
            .add(
                agregarPersonajeTabla(
                    "Tornado",
                    labelPriceTornado,
                    Assets.carTornado,
                    "Pretty speedy. Nothing too hot about this car, it looks ok and is ok to drive.",
                    buttonBuyTornado
                )
            ).expandX().fill()
        container.row()

        // SKIN_CARRO_TURISMO
        container
            .add(
                agregarPersonajeTabla(
                    "Turismo",
                    labelPriceTurismo,
                    Assets.carTourism,
                    "If you can get this rare sport car, you'll be rewarded with a superbly fast drive. If you get it, take care of it.",
                    buttonBuyTurismo
                )
            ).expandX().fill()
        container.row()

        // SKIN_CARRO_AUDI_S5
        container
            .add(
                agregarPersonajeTabla(
                    "Ventura", labelPriceAudiS5,
                    Assets.carAudiS5, "No description", buttonBuyAudiS5
                )
            )
            .expandX().fill()
        container.row()

        // SKIN_CARRO_BMW_X6
        container
            .add(
                agregarPersonajeTabla(
                    "XMW", labelPriceBmwX6, Assets.carBmwX6,
                    "No description", buttonBuyBmwX6
                )
            ).expandX().fill()
        container.row()

        // PRECIO_BULLET
        container
            .add(
                agregarPersonajeTabla(
                    "Bullet",
                    labelPriceCamaro,
                    Assets.carBullet,
                    "Probably the best sporty hatchback. It's quick and sticks to road really well. Acceleration is great too.",
                    buttonBuyBullet
                )
            ).expandX().fill()
        container.row()

        // SKIN_CARRO_CHEVRLOTE_CROSSFIRE
        container
            .add(
                agregarPersonajeTabla(
                    "Crosstown", labelPriceCrossfire,
                    Assets.carChevroletCrossfire, "No description",
                    buttonBuyCrossfire
                )
            ).expandX().fill()
        container.row()

        // SKIN_CARRO_CITROEN_C4
        container
            .add(
                agregarPersonajeTabla(
                    "Omega X", labelPriceCitroenC4,
                    Assets.carCitroenC4, "No description", buttonBuyCitroenC4
                )
            )
            .expandX().fill()
        container.row()

        // SKIN_CARRO_DODGE_CHARGER
        container
            .add(
                agregarPersonajeTabla(
                    "Vulcano", labelPriceDodgeCharger,
                    Assets.carDodgeCharger, "No description",
                    buttonBuyDodgeCharger
                )
            ).expandX().fill()
        container.row()

        // SKIN_CARRO_FIAT_500_LOUNGE
        container
            .add(
                agregarPersonajeTabla(
                    "Fiesta", labelPriceFiat500Lounge,
                    Assets.carFiat500Lounge, "No description",
                    buttonBuyFiat500Lounge
                )
            ).expandX().fill()
        container.row()

        // SKIN_CARRO_HONDA_CRV
        container
            .add(
                agregarPersonajeTabla(
                    "Comander", labelPriceHondeCRV,
                    Assets.carHondaCRV, "No description", buttonBuyHondaCRV
                )
            )
            .expandX().fill()
        container.row()

        // SKIN_CARRO_MAZDA_6
        container
            .add(
                agregarPersonajeTabla(
                    "Orion", labelPriceMazda6,
                    Assets.carMazda6, "No description", buttonBuyMazda6
                )
            )
            .expandX().fill()
        container.row()

        // SKIN_CARRO_MAZDA_RX8
        container
            .add(
                agregarPersonajeTabla(
                    "Colorado", labelPriceMazdaRX8,
                    Assets.carMazdaRX8, "No description", buttonBuyMazdaRX8
                )
            )
            .expandX().fill()
        container.row()

        // SKIN_CARRO_SEAT_IBIZA
        container
            .add(
                agregarPersonajeTabla(
                    "Formosa", labelPriceSeatIbiza,
                    Assets.carSeatIbiza, "No description", buttonBuySeatIbiza
                )
            )
            .expandX().fill()
        container.row()

        // SKIN_CARRO_VOLKSWAGEN_SCIROCCO
        container
            .add(
                agregarPersonajeTabla(
                    "SHU", labelPriceVolkswagenScirocco,
                    Assets.carVolkswagenScirocco, "No description",
                    buttonBuyVolkswagenScirocco
                )
            ).expandX().fill()
        container.row()
    }

    private fun agregarPersonajeTabla(
        titulo: String, lblPrecio: Label?,
        imagen: AtlasRegion, descripcion: String, boton: TextButton?
    ): Table {
        val moneda = Image(Assets.coinFront)
        val imgPersonaje = Image(imagen)

        if (lblPrecio == null) moneda.isVisible = false

        val tbBarraTitulo = Table()
        tbBarraTitulo.add(Label(titulo, Assets.labelStyleSmall)).expandX()
            .left()
        tbBarraTitulo.add(moneda).right()
        tbBarraTitulo.add(lblPrecio).right().padRight(10f)

        val tbContent = Table()
        // tbContent.debug();
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(8f)
        tbContent.row()
        tbContent.add(imgPersonaje).left().pad(10f).size(40f, 90f)

        val lblDescripcion = Label(descripcion, Assets.labelStyleSmall)
        lblDescripcion.wrap = true
        lblDescripcion.setFontScale(.85f)
        tbContent.add(lblDescripcion).expand().fill().padLeft(5f)

        tbContent.row().colspan(2)
        tbContent.add(boton).expandX().right().padRight(10f).size(120f, 45f)
        tbContent.row().colspan(2)
        tbContent.add(Image(Assets.horizontalSeparator)).expandX().fill()
            .height(5f).padTop(15f)

        return tbContent
    }

    private fun initializeButtons() {
        arrayButtons = Array()

        // DEFAULT
        buttonBuyDiablo = TextButton("Select", Assets.styleTextButtonPurchased)
        if (Settings.selectedSkin == SKIN_CAR_DEVIL) buttonBuyDiablo!!.isVisible = false

        addPressEffect(buttonBuyDiablo!!)
        buttonBuyDiablo!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.selectedSkin = SKIN_CAR_DEVIL
                setSelected(buttonBuyDiablo!!)
            }
        })

        // SKIN_CAR_BANSHEE
        buttonBuyBanshee = if (didBuyBanshee) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_BANSHEE) buttonBuyBanshee!!.isVisible = false

        addPressEffect(buttonBuyBanshee!!)
        buttonBuyBanshee!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyBanshee) {
                    Settings.selectedSkin = SKIN_CAR_BANSHEE
                    setSelected(buttonBuyBanshee!!)
                } else if (Settings.coinsTotal >= PRICE_BANSHEE) {
                    Settings.coinsTotal -= PRICE_BANSHEE
                    setButtonStylePurchased(buttonBuyBanshee!!)
                    didBuyBanshee = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_TORNADO
        buttonBuyTornado = if (didBuyTornado) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_TORNADO) buttonBuyTornado!!.isVisible = false

        addPressEffect(buttonBuyTornado!!)
        buttonBuyTornado!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyTornado) {
                    Settings.selectedSkin = SKIN_CAR_TORNADO
                    setSelected(buttonBuyTornado!!)
                } else if (Settings.coinsTotal >= PRICE_TORNADO) {
                    Settings.coinsTotal -= PRICE_TORNADO
                    setButtonStylePurchased(buttonBuyTornado!!)
                    didBuyTornado = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_TURISMO
        buttonBuyTurismo = if (didBuyTurismo) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_TURISM) buttonBuyTurismo!!.isVisible = false

        addPressEffect(buttonBuyTurismo!!)
        buttonBuyTurismo!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyTurismo) {
                    Settings.selectedSkin = SKIN_CAR_TURISM
                    setSelected(buttonBuyTurismo!!)
                } else if (Settings.coinsTotal >= PRICE_TURISM) {
                    Settings.coinsTotal -= PRICE_TURISM
                    setButtonStylePurchased(buttonBuyTurismo!!)
                    didBuyTurismo = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_AUDI_S5
        buttonBuyAudiS5 = if (didBuyAudiS5) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_AUDI_S5) buttonBuyAudiS5!!.isVisible = false

        addPressEffect(buttonBuyAudiS5!!)
        buttonBuyAudiS5!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyAudiS5) {
                    Settings.selectedSkin = SKIN_CAR_AUDI_S5
                    setSelected(buttonBuyAudiS5!!)
                } else if (Settings.coinsTotal >= PRICE_CAR_AUDI_S5) {
                    Settings.coinsTotal -= PRICE_CAR_AUDI_S5
                    setButtonStylePurchased(buttonBuyAudiS5!!)
                    didBuyAudiS5 = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_BMW_X6
        buttonBuyBmwX6 = if (didBuyBmwX6) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_BMW_X6) buttonBuyBmwX6!!.isVisible = false

        addPressEffect(buttonBuyBmwX6!!)
        buttonBuyBmwX6!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyBmwX6) {
                    Settings.selectedSkin = SKIN_CAR_BMW_X6
                    setSelected(buttonBuyBmwX6!!)
                } else if (Settings.coinsTotal >= PRICE_CAR_BMW_X6) {
                    Settings.coinsTotal -= PRICE_CAR_BMW_X6
                    setButtonStylePurchased(buttonBuyBmwX6!!)
                    didBuyBmwX6 = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_BULLET
        buttonBuyBullet = if (didBuyBullet) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_BULLET) buttonBuyBullet!!.isVisible = false

        addPressEffect(buttonBuyBullet!!)
        buttonBuyBullet!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyBullet) {
                    Settings.selectedSkin = SKIN_CAR_BULLET
                    setSelected(buttonBuyBullet!!)
                } else if (Settings.coinsTotal >= PRICE_BULLET) {
                    Settings.coinsTotal -= PRICE_BULLET
                    setButtonStylePurchased(buttonBuyBullet!!)
                    didBuyBullet = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_CHEVROLET_CROSSFIRE
        buttonBuyCrossfire = if (didBuyCrossfire) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_CHEVROLET_CROSSFIRE) buttonBuyCrossfire!!.isVisible =
            false

        addPressEffect(buttonBuyCrossfire!!)
        buttonBuyCrossfire!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyCrossfire) {
                    Settings.selectedSkin = SKIN_CAR_CHEVROLET_CROSSFIRE
                    setSelected(buttonBuyCrossfire!!)
                } else if (Settings.coinsTotal >= PRICE_CAR_CHEVROLET_CROSSFIRE) {
                    Settings.coinsTotal -= PRICE_CAR_CHEVROLET_CROSSFIRE
                    setButtonStylePurchased(buttonBuyCrossfire!!)
                    didBuyCrossfire = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_CITROEN_C4
        buttonBuyCitroenC4 = if (didBuyCitroenC4) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_CITROEN_C4) buttonBuyCitroenC4!!.isVisible = false

        addPressEffect(buttonBuyCitroenC4!!)
        buttonBuyCitroenC4!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyCitroenC4) {
                    Settings.selectedSkin = SKIN_CAR_CITROEN_C4
                    setSelected(buttonBuyCitroenC4!!)
                } else if (Settings.coinsTotal >= PRICE_CAR_CITROEN_C4) {
                    Settings.coinsTotal -= PRICE_CAR_CITROEN_C4
                    setButtonStylePurchased(buttonBuyCitroenC4!!)
                    didBuyCitroenC4 = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_DODGE_CHARGER
        buttonBuyDodgeCharger = if (didBuyDodgeCharger) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_DODGE_CHARGER) buttonBuyDodgeCharger!!.isVisible =
            false

        addPressEffect(buttonBuyDodgeCharger!!)
        buttonBuyDodgeCharger!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyDodgeCharger) {
                    Settings.selectedSkin = SKIN_CAR_DODGE_CHARGER
                    setSelected(buttonBuyDodgeCharger!!)
                } else if (Settings.coinsTotal >= PRICE_CAR_DODGE_CHARGER) {
                    Settings.coinsTotal -= PRICE_CAR_DODGE_CHARGER
                    setButtonStylePurchased(buttonBuyDodgeCharger!!)
                    didBuyDodgeCharger = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_FIAT_500_LOUNGE
        buttonBuyFiat500Lounge = if (didBuyFiat500) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton(
            "Buy",
            Assets.styleTextButtonBuy
        )

        if (Settings.selectedSkin == SKIN_CAR_FIAT_500_LOUNGE) buttonBuyFiat500Lounge!!.isVisible =
            false

        addPressEffect(buttonBuyFiat500Lounge!!)
        buttonBuyFiat500Lounge!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyFiat500) {
                    Settings.selectedSkin = SKIN_CAR_FIAT_500_LOUNGE
                    setSelected(buttonBuyFiat500Lounge!!)
                } else if (Settings.coinsTotal >= PRICE_CAR_FIAT_500_LOUNGE) {
                    Settings.coinsTotal -= PRICE_CAR_FIAT_500_LOUNGE
                    setButtonStylePurchased(buttonBuyFiat500Lounge!!)
                    didBuyFiat500 = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_HONDA_CRV
        buttonBuyHondaCRV = if (didBuyHondaCRV) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_HONDA_CRV) buttonBuyHondaCRV!!.isVisible = false

        addPressEffect(buttonBuyHondaCRV!!)
        buttonBuyHondaCRV!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyHondaCRV) {
                    Settings.selectedSkin = SKIN_CAR_HONDA_CRV
                    setSelected(buttonBuyHondaCRV!!)
                } else if (Settings.coinsTotal >= PRICE_CAR_HONDA_CRV) {
                    Settings.coinsTotal -= PRICE_CAR_HONDA_CRV
                    setButtonStylePurchased(buttonBuyHondaCRV!!)
                    didBuyHondaCRV = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_MAZDA_6
        buttonBuyMazda6 = if (didBuyMazda6) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_MAZDA_6) buttonBuyMazda6!!.isVisible = false

        addPressEffect(buttonBuyMazda6!!)
        buttonBuyMazda6!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyMazda6) {
                    Settings.selectedSkin = SKIN_CAR_MAZDA_6
                    setSelected(buttonBuyMazda6!!)
                } else if (Settings.coinsTotal >= PRICE_CAR_MAZDA_6) {
                    Settings.coinsTotal -= PRICE_CAR_MAZDA_6
                    setButtonStylePurchased(buttonBuyMazda6!!)
                    didBuyMazda6 = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_MAZDA_RX8
        buttonBuyMazdaRX8 = if (didBuyMazdaRX8) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_MAZDA_RX8) buttonBuyMazdaRX8!!.isVisible = false

        addPressEffect(buttonBuyMazdaRX8!!)
        buttonBuyMazdaRX8!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyMazdaRX8) {
                    Settings.selectedSkin = SKIN_CAR_MAZDA_RX8
                    setSelected(buttonBuyMazdaRX8!!)
                } else if (Settings.coinsTotal >= PRICE_CAR_MAZDA_RX8) {
                    Settings.coinsTotal -= PRICE_CAR_MAZDA_RX8
                    setButtonStylePurchased(buttonBuyMazdaRX8!!)
                    didBuyMazdaRX8 = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_SEAT_IBIZA
        buttonBuySeatIbiza = if (didBuySeatIbiza) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAR_SEAT_IBIZA) buttonBuySeatIbiza!!.isVisible = false

        addPressEffect(buttonBuySeatIbiza!!)
        buttonBuySeatIbiza!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuySeatIbiza) {
                    Settings.selectedSkin = SKIN_CAR_SEAT_IBIZA
                    setSelected(buttonBuySeatIbiza!!)
                } else if (Settings.coinsTotal >= PRICE_CAR_SEAT_IBIZA) {
                    Settings.coinsTotal -= PRICE_CAR_SEAT_IBIZA
                    setButtonStylePurchased(buttonBuySeatIbiza!!)
                    didBuySeatIbiza = true
                }
                savePurchases()
            }
        })

        // SKIN_CAR_VOLKSWAGEN_SCIROCCO
        buttonBuyVolkswagenScirocco = if (didBuyVolkswagenScirocco) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton(
            "Buy",
            Assets.styleTextButtonBuy
        )

        if (Settings.selectedSkin == SKIN_CAR_VOLKSWAGEN_SCIROCCO) buttonBuyVolkswagenScirocco!!.isVisible =
            false

        addPressEffect(buttonBuyVolkswagenScirocco!!)
        buttonBuyVolkswagenScirocco!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyVolkswagenScirocco) {
                    Settings.selectedSkin = SKIN_CAR_VOLKSWAGEN_SCIROCCO
                    setSelected(buttonBuyVolkswagenScirocco!!)
                } else if (Settings.coinsTotal >= PRICE_CAR_VOLKSWAGEN_SCIROCCO) {
                    Settings.coinsTotal -= PRICE_CAR_VOLKSWAGEN_SCIROCCO
                    setButtonStylePurchased(buttonBuyVolkswagenScirocco!!)
                    didBuyVolkswagenScirocco = true
                }
                savePurchases()
            }
        })

        arrayButtons!!.add(buttonBuyDiablo)
        arrayButtons!!.add(buttonBuyBanshee)
        arrayButtons!!.add(buttonBuyTornado)
        arrayButtons!!.add(buttonBuyTurismo)
        arrayButtons!!.add(buttonBuyAudiS5)
        arrayButtons!!.add(buttonBuyBmwX6)
        arrayButtons!!.add(buttonBuyBullet)
        arrayButtons!!.add(buttonBuyCrossfire)
        arrayButtons!!.add(buttonBuyCitroenC4)
        arrayButtons!!.add(buttonBuyDodgeCharger)
        arrayButtons!!.add(buttonBuyFiat500Lounge)
        arrayButtons!!.add(buttonBuyHondaCRV)
        arrayButtons!!.add(buttonBuyMazda6)
        arrayButtons!!.add(buttonBuyMazdaRX8)
        arrayButtons!!.add(buttonBuySeatIbiza)
        arrayButtons!!.add(buttonBuyVolkswagenScirocco)
    }

    private fun loadPurchases() {
        didBuyBanshee = pref.getBoolean("didBuyBanshee", false)
        didBuyTornado = pref.getBoolean("didBuyTornado", false)
        didBuyTurismo = pref.getBoolean("didBuyTurismo", false)
        didBuyAudiS5 = pref.getBoolean("didBuyAudiS5", false)
        didBuyBmwX6 = pref.getBoolean("didBuyBmwX6", false)
        didBuyBullet = pref.getBoolean("didBuyBullet", false)
        didBuyCrossfire = pref.getBoolean("didBuyCrossfire", false)
        didBuyCitroenC4 = pref.getBoolean("didBuyCitroenC4", false)
        didBuyDodgeCharger = pref.getBoolean("didBuyDodgeCharger", false)
        didBuyFiat500 = pref.getBoolean("didBuyFiat500", false)
        didBuyHondaCRV = pref.getBoolean("didBuyHondaCRV", false)
        didBuyMazda6 = pref.getBoolean("didBuyMazda6", false)
        didBuyMazdaRX8 = pref.getBoolean("didBuyMazdaRX8", false)
        didBuySeatIbiza = pref.getBoolean("didBuySeatIbiza", false)
        didBuyVolkswagenScirocco = pref.getBoolean("didBuyVolkswagenScirocco", false)
    }

    private fun savePurchases() {
        pref.putBoolean("didBuyBanshee", didBuyBanshee)
        pref.putBoolean("didBuyTornado", didBuyTornado)
        pref.putBoolean("didBuyTurismo", didBuyTurismo)
        pref.putBoolean("didBuyAudiS5", didBuyAudiS5)
        pref.putBoolean("didBuyBmwX6", didBuyBmwX6)
        pref.putBoolean("didBuyBullet", didBuyBullet)
        pref.putBoolean("didBuyCrossfire", didBuyCrossfire)
        pref.putBoolean("didBuyCitroenC4", didBuyCitroenC4)
        pref.putBoolean("didBuyDodgeCharger", didBuyDodgeCharger)
        pref.putBoolean("didBuyFiat500", didBuyFiat500)
        pref.putBoolean("didBuyHondaCRV", didBuyHondaCRV)
        pref.putBoolean("didBuyMazda6", didBuyMazda6)
        pref.putBoolean("didBuyMazdaRX8", didBuyMazdaRX8)
        pref.putBoolean("didBuySeatIbiza", didBuySeatIbiza)
        pref.putBoolean("didBuyVolkswagenScirocco", didBuyVolkswagenScirocco)
        pref.flush()
        save()
    }

    private fun setButtonStylePurchased(button: TextButton) {
        button.style = Assets.styleTextButtonPurchased
        button.setText("Select")
    }

    private fun setSelected(button: TextButton) {
        // All buttons are visible in the beginning  and at the end the selected button will be invisible.
        for (arrayButton in arrayButtons!!) {
            arrayButton.isVisible = true
        }
        button.isVisible = false
    }

    protected fun addPressEffect(actor: Actor) {
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
        // cars
        const val SKIN_CAR_DEVIL = 0
        const val SKIN_CAR_BANSHEE = 1
        const val SKIN_CAR_TURISM = 3
        const val SKIN_CAR_BULLET = 6
        const val SKIN_CAR_TORNADO = 2
        const val SKIN_CAR_AUDI_S5 = 4
        const val SKIN_CAR_BMW_X6 = 5
        const val SKIN_CAR_CHEVROLET_CROSSFIRE = 7
        const val SKIN_CAR_CITROEN_C4 = 8
        const val SKIN_CAR_DODGE_CHARGER = 9
        const val SKIN_CAR_FIAT_500_LOUNGE = 10
        const val SKIN_CAR_HONDA_CRV = 11
        const val SKIN_CAR_MAZDA_6 = 12
        const val SKIN_CAR_MAZDA_RX8 = 13
        const val SKIN_CAR_SEAT_IBIZA = 14
        const val SKIN_CAR_VOLKSWAGEN_SCIROCCO = 15

        private val pref: Preferences = Gdx.app.getPreferences("com.tiar.dragrace.shop")

        // prices
        const val PRICE_BANSHEE = 50
        const val PRICE_BULLET = 175
        const val PRICE_TURISM = 100
        const val PRICE_TORNADO = 75
        const val PRICE_CAR_AUDI_S5 = 125
        const val PRICE_CAR_BMW_X6 = 150
        const val PRICE_CAR_CHEVROLET_CROSSFIRE = 200
        const val PRICE_CAR_CITROEN_C4 = 225
        const val PRICE_CAR_DODGE_CHARGER = 250
        const val PRICE_CAR_FIAT_500_LOUNGE = 275
        const val PRICE_CAR_HONDA_CRV = 300
        const val PRICE_CAR_MAZDA_6 = 325
        const val PRICE_CAR_MAZDA_RX8 = 350
        const val PRICE_CAR_SEAT_IBIZA = 375
        const val PRICE_CAR_VOLKSWAGEN_SCIROCCO = 400
    }
}
