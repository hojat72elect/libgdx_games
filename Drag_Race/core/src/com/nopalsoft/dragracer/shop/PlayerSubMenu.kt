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

class PlayerSubMenu(var game: MainStreet, containerTable: Table) {


    var didBuyBanshee: Boolean = false
    var didBuyTornado: Boolean = false
    var didBuyTurismo: Boolean = false
    var didBuyAudiS5: Boolean = false
    var didBuyBmwX6: Boolean = false
    var didBuyCamaro: Boolean = false
    var didBuyCrossfire: Boolean = false
    var didBuyCitroenC4: Boolean = false
    var didBuyDodgeCharger: Boolean = false
    var didBuyFiat500: Boolean = false
    var didBuyHondaCRV: Boolean = false
    var didBuyMazda6: Boolean = false
    var didBuyMazdaRX8: Boolean = false
    var didBuySeatIbiza: Boolean = false
    var didBuyVolkswagenScirocco: Boolean = false

    var btBuyDiablo: TextButton? = null
    var btBuyBanshee: TextButton? = null
    var btBuyTornado: TextButton? = null
    var btBuyTurismo: TextButton? = null
    var btBuyAudiS5: TextButton? = null
    var btBuyBmwX6: TextButton? = null
    var btBuyBullet: TextButton? = null
    var btBuyCrossfire: TextButton? = null
    var btBuyCitroenC4: TextButton? = null
    var btBuyDodgeCharger: TextButton? = null
    var btBuyFiat500Lounge: TextButton? = null
    var btBuyHondaCRV: TextButton? = null
    var btBuyMazda6: TextButton? = null
    var btBuyMazdaRX8: TextButton? = null
    var btBuySeatIbiza: TextButton? = null
    var btBuyVolkswagenScirocco: TextButton? = null
    private var arrayButtons: Array<TextButton>? = null

    init {
        loadPurchases()

        containerTable.clear()

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
        var labelPriceHondaCRV: Label? = null
        var labelPriceMazda6: Label? = null
        var labelPriceMazdaRX8: Label? = null
        var labelPriceSeatIbiza: Label? = null
        var labelPriceVolkswagenScirocco: Label? = null

        if (!didBuyBanshee) labelPriceBanshee = Label(
            PRICE_BANSHEE.toString() + "",
            Assets.labelStyleSmall
        )
        if (!didBuyTornado) labelPriceTornado = Label(
            PRICE_TORNADO.toString() + "",
            Assets.labelStyleSmall
        )

        if (!didBuyTurismo) labelPriceTurismo = Label(
            PRICE_TURISMO.toString() + "",
            Assets.labelStyleSmall
        )

        if (!didBuyAudiS5) labelPriceAudiS5 = Label(
            PRICE_AUDI_S5.toString() + "",
            Assets.labelStyleSmall
        )

        if (!didBuyBmwX6) labelPriceBmwX6 = Label(
            PRICE_BMW_X6.toString() + "",
            Assets.labelStyleSmall
        )

        if (!didBuyCamaro) labelPriceCamaro = Label(
            PRICE_CAMARO.toString() + "",
            Assets.labelStyleSmall
        )

        if (!didBuyCrossfire) labelPriceCrossfire = Label(
            PRICE_CHEVROLET_CROSSFIRE
                .toString() + "", Assets.labelStyleSmall
        )

        if (!didBuyCitroenC4) labelPriceCitroenC4 = Label(
            PRICE_CITROEN_C4.toString() + "",
            Assets.labelStyleSmall
        )

        if (!didBuyDodgeCharger) labelPriceDodgeCharger = Label(
            PRICE_DODGE_CHARGER.toString() + "",
            Assets.labelStyleSmall
        )

        if (!didBuyFiat500) labelPriceFiat500Lounge = Label(
            PRICE_FIAT_500_LOUNGE
                .toString() + "", Assets.labelStyleSmall
        )

        if (!didBuyHondaCRV) labelPriceHondaCRV = Label(
            PRICE_HONDA_CRV.toString() + "",
            Assets.labelStyleSmall
        )

        if (!didBuyMazda6) labelPriceMazda6 = Label(
            PRICE_MAZDA_6.toString() + "",
            Assets.labelStyleSmall
        )

        if (!didBuyMazdaRX8) labelPriceMazdaRX8 = Label(
            PRICE_MAZDA_RX8.toString() + "",
            Assets.labelStyleSmall
        )

        if (!didBuySeatIbiza) labelPriceSeatIbiza = Label(
            PRICE_SEAT_IBIZA.toString() + "",
            Assets.labelStyleSmall
        )

        if (!didBuyVolkswagenScirocco) labelPriceVolkswagenScirocco = Label(
            PRICE_VOLKSWAGEN_SCIROCCO.toString() + "",
            Assets.labelStyleSmall
        )

        initializeButtons()

        containerTable.add(Image(Assets.horizontalSeparatorDrawable)).expandX().fill()
            .height(5f)
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Diablo",
                    null,
                    Assets.carDiablo!!,
                    "Good car. It's not the fastest, but it's got great handling although maybe a little too twitchy for some.",
                    btBuyDiablo
                )
            ).expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Banshee",
                    labelPriceBanshee,
                    Assets.carBanshee!!,
                    "Looks great and drives even better. Awesome acceleration and slight over-steer make this a thrilling ride.",
                    btBuyBanshee
                )
            ).expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Tornado",
                    labelPriceTornado,
                    Assets.carTornado!!,
                    "Pretty speedy. Nothing too hot about this car, it looks ok and is ok to drive.",
                    btBuyTornado
                )
            ).expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Turismo",
                    labelPriceTurismo,
                    Assets.carTurismo!!,
                    "If you can get this rare sport car, you'll be rewarded with a superbly fast drive. If you get it, take care of it.",
                    btBuyTurismo
                )
            ).expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Ventura", labelPriceAudiS5,
                    Assets.carAudiS5!!, "No description", btBuyAudiS5
                )
            )
            .expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "XMW", labelPriceBmwX6, Assets.carBmwX6!!,
                    "No description", btBuyBmwX6
                )
            ).expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Bullet",
                    labelPriceCamaro,
                    Assets.carCamaro!!,
                    "Probably the best sporty hatchback. It's quick and sticks to road really well. Acceleration is great too.",
                    btBuyBullet
                )
            ).expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Crosstown", labelPriceCrossfire,
                    Assets.carChevroletCrossfire!!, "No description",
                    btBuyCrossfire
                )
            ).expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Omega X", labelPriceCitroenC4,
                    Assets.carCitroenC4!!, "No description", btBuyCitroenC4
                )
            )
            .expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Vulcano", labelPriceDodgeCharger,
                    Assets.carDodgeCharger!!, "No description",
                    btBuyDodgeCharger
                )
            ).expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Fiesta", labelPriceFiat500Lounge,
                    Assets.carFiat500Lounge!!, "No description",
                    btBuyFiat500Lounge
                )
            ).expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Comander", labelPriceHondaCRV,
                    Assets.carHondaCRV!!, "No description", btBuyHondaCRV
                )
            )
            .expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Orion", labelPriceMazda6,
                    Assets.carMazda6!!, "No description", btBuyMazda6
                )
            )
            .expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Colorado", labelPriceMazdaRX8,
                    Assets.carMazdaRx8!!, "No description", btBuyMazdaRX8
                )
            )
            .expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "Formosa", labelPriceSeatIbiza,
                    Assets.carSeatIbiza!!, "No description", btBuySeatIbiza
                )
            )
            .expandX().fill()
        containerTable.row()

        containerTable
            .add(
                addPlayerTable(
                    "SHU", labelPriceVolkswagenScirocco,
                    Assets.carVolkswagenScirocco!!, "No description",
                    btBuyVolkswagenScirocco
                )
            ).expandX().fill()
        containerTable.row()
    }

    private fun addPlayerTable(
        title: String, priceLabel: Label?,
        image: AtlasRegion, description: String, button: TextButton?
    ): Table {
        val coinImage = Image(Assets.coinFront)
        val playerImage = Image(image)

        if (priceLabel == null) coinImage.isVisible = false

        val titleBarTable = Table()
        titleBarTable.add(Label(title, Assets.labelStyleSmall)).expandX()
            .left()
        titleBarTable.add(coinImage).right()
        titleBarTable.add(priceLabel).right().padRight(10f)

        val tableContent = Table()
        tableContent.add(titleBarTable).expandX().fill().colspan(2).padTop(8f)
        tableContent.row()
        tableContent.add(playerImage).left().pad(10f).size(40f, 90f)

        val labelDescription = Label(description, Assets.labelStyleSmall)
        labelDescription.wrap = true
        labelDescription.setFontScale(.85f)
        tableContent.add(labelDescription).expand().fill().padLeft(5f)

        tableContent.row().colspan(2)
        tableContent.add(button).expandX().right().padRight(10f).size(120f, 45f)
        tableContent.row().colspan(2)
        tableContent.add(Image(Assets.horizontalSeparatorDrawable)).expandX().fill()
            .height(5f).padTop(15f)

        return tableContent
    }

    private fun initializeButtons() {
        arrayButtons = Array()

        // DEFAULT
        btBuyDiablo = TextButton("Select", Assets.styleTextButtonPurchased)
        if (Settings.selectedSkin == SKIN_DEVIL) btBuyDiablo!!.isVisible = false

        addPressEffect(btBuyDiablo!!)
        btBuyDiablo!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.selectedSkin = SKIN_DEVIL
                setSelected(btBuyDiablo!!)
            }
        })

        btBuyBanshee = if (didBuyBanshee) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_BANSHEE) btBuyBanshee!!.isVisible = false

        addPressEffect(btBuyBanshee!!)
        btBuyBanshee!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyBanshee) {
                    Settings.selectedSkin = SKIN_BANSHEE
                    setSelected(btBuyBanshee!!)
                } else if (Settings.coinsTotal >= PRICE_BANSHEE) {
                    Settings.coinsTotal -= PRICE_BANSHEE
                    setButtonStylePurchased(btBuyBanshee!!)
                    didBuyBanshee = true
                }
                savePurchases()
            }
        })

        btBuyTornado = if (didBuyTornado) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_TORNADO) btBuyTornado!!.isVisible = false

        addPressEffect(btBuyTornado!!)
        btBuyTornado!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyTornado) {
                    Settings.selectedSkin = SKIN_TORNADO
                    setSelected(btBuyTornado!!)
                } else if (Settings.coinsTotal >= PRICE_TORNADO) {
                    Settings.coinsTotal -= PRICE_TORNADO
                    setButtonStylePurchased(btBuyTornado!!)
                    didBuyTornado = true
                }
                savePurchases()
            }
        })

        btBuyTurismo = if (didBuyTurismo) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_TURISMO) btBuyTurismo!!.isVisible = false

        addPressEffect(btBuyTurismo!!)
        btBuyTurismo!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyTurismo) {
                    Settings.selectedSkin = SKIN_TURISMO
                    setSelected(btBuyTurismo!!)
                } else if (Settings.coinsTotal >= PRICE_TURISMO) {
                    Settings.coinsTotal -= PRICE_TURISMO
                    setButtonStylePurchased(btBuyTurismo!!)
                    didBuyTurismo = true
                }
                savePurchases()
            }
        })

        btBuyAudiS5 = if (didBuyAudiS5) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_AUDI_S5) btBuyAudiS5!!.isVisible = false

        addPressEffect(btBuyAudiS5!!)
        btBuyAudiS5!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyAudiS5) {
                    Settings.selectedSkin = SKIN_AUDI_S5
                    setSelected(btBuyAudiS5!!)
                } else if (Settings.coinsTotal >= PRICE_AUDI_S5) {
                    Settings.coinsTotal -= PRICE_AUDI_S5
                    setButtonStylePurchased(btBuyAudiS5!!)
                    didBuyAudiS5 = true
                }
                savePurchases()
            }
        })

        btBuyBmwX6 = if (didBuyBmwX6) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_BMW_X6) btBuyBmwX6!!.isVisible = false

        addPressEffect(btBuyBmwX6!!)
        btBuyBmwX6!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyBmwX6) {
                    Settings.selectedSkin = SKIN_BMW_X6
                    setSelected(btBuyBmwX6!!)
                } else if (Settings.coinsTotal >= PRICE_BMW_X6) {
                    Settings.coinsTotal -= PRICE_BMW_X6
                    setButtonStylePurchased(btBuyBmwX6!!)
                    didBuyBmwX6 = true
                }
                savePurchases()
            }
        })

        btBuyBullet = if (didBuyCamaro) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CAMARO) btBuyBullet!!.isVisible = false

        addPressEffect(btBuyBullet!!)
        btBuyBullet!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyCamaro) {
                    Settings.selectedSkin = SKIN_CAMARO
                    setSelected(btBuyBullet!!)
                } else if (Settings.coinsTotal >= PRICE_CAMARO) {
                    Settings.coinsTotal -= PRICE_CAMARO
                    setButtonStylePurchased(btBuyBullet!!)
                    didBuyCamaro = true
                }
                savePurchases()
            }
        })

        btBuyCrossfire = if (didBuyCrossfire) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CHEVROLET_CROSSFIRE) btBuyCrossfire!!.isVisible = false

        addPressEffect(btBuyCrossfire!!)
        btBuyCrossfire!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyCrossfire) {
                    Settings.selectedSkin = SKIN_CHEVROLET_CROSSFIRE
                    setSelected(btBuyCrossfire!!)
                } else if (Settings.coinsTotal >= PRICE_CHEVROLET_CROSSFIRE) {
                    Settings.coinsTotal -= PRICE_CHEVROLET_CROSSFIRE
                    setButtonStylePurchased(btBuyCrossfire!!)
                    didBuyCrossfire = true
                }
                savePurchases()
            }
        })

        btBuyCitroenC4 = if (didBuyCitroenC4) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_CITROEN_C4) btBuyCitroenC4!!.isVisible = false

        addPressEffect(btBuyCitroenC4!!)
        btBuyCitroenC4!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyCitroenC4) {
                    Settings.selectedSkin = SKIN_CITROEN_C4
                    setSelected(btBuyCitroenC4!!)
                } else if (Settings.coinsTotal >= PRICE_CITROEN_C4) {
                    Settings.coinsTotal -= PRICE_CITROEN_C4
                    setButtonStylePurchased(btBuyCitroenC4!!)
                    didBuyCitroenC4 = true
                }
                savePurchases()
            }
        })

        btBuyDodgeCharger = if (didBuyDodgeCharger) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_DODGE_CHARGER) btBuyDodgeCharger!!.isVisible = false

        addPressEffect(btBuyDodgeCharger!!)
        btBuyDodgeCharger!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyDodgeCharger) {
                    Settings.selectedSkin = SKIN_DODGE_CHARGER
                    setSelected(btBuyDodgeCharger!!)
                } else if (Settings.coinsTotal >= PRICE_DODGE_CHARGER) {
                    Settings.coinsTotal -= PRICE_DODGE_CHARGER
                    setButtonStylePurchased(btBuyDodgeCharger!!)
                    didBuyDodgeCharger = true
                }
                savePurchases()
            }
        })

        btBuyFiat500Lounge = if (didBuyFiat500) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton(
            "Buy",
            Assets.styleTextButtonBuy
        )

        if (Settings.selectedSkin == SKIN_FIAT_500_LOUNGE) btBuyFiat500Lounge!!.isVisible = false

        addPressEffect(btBuyFiat500Lounge!!)
        btBuyFiat500Lounge!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyFiat500) {
                    Settings.selectedSkin = SKIN_FIAT_500_LOUNGE
                    setSelected(btBuyFiat500Lounge!!)
                } else if (Settings.coinsTotal >= PRICE_FIAT_500_LOUNGE) {
                    Settings.coinsTotal -= PRICE_FIAT_500_LOUNGE
                    setButtonStylePurchased(btBuyFiat500Lounge!!)
                    didBuyFiat500 = true
                }
                savePurchases()
            }
        })

        btBuyHondaCRV = if (didBuyHondaCRV) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_HONDA_CRV) btBuyHondaCRV!!.isVisible = false

        addPressEffect(btBuyHondaCRV!!)
        btBuyHondaCRV!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyHondaCRV) {
                    Settings.selectedSkin = SKIN_HONDA_CRV
                    setSelected(btBuyHondaCRV!!)
                } else if (Settings.coinsTotal >= PRICE_HONDA_CRV) {
                    Settings.coinsTotal -= PRICE_HONDA_CRV
                    setButtonStylePurchased(btBuyHondaCRV!!)
                    didBuyHondaCRV = true
                }
                savePurchases()
            }
        })

        btBuyMazda6 = if (didBuyMazda6) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_MAZDA_6) btBuyMazda6!!.isVisible = false

        addPressEffect(btBuyMazda6!!)
        btBuyMazda6!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyMazda6) {
                    Settings.selectedSkin = SKIN_MAZDA_6
                    setSelected(btBuyMazda6!!)
                } else if (Settings.coinsTotal >= PRICE_MAZDA_6) {
                    Settings.coinsTotal -= PRICE_MAZDA_6
                    setButtonStylePurchased(btBuyMazda6!!)
                    didBuyMazda6 = true
                }
                savePurchases()
            }
        })

        btBuyMazdaRX8 = if (didBuyMazdaRX8) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_MAZDA_RX8) btBuyMazdaRX8!!.isVisible = false

        addPressEffect(btBuyMazdaRX8!!)
        btBuyMazdaRX8!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyMazdaRX8) {
                    Settings.selectedSkin = SKIN_MAZDA_RX8
                    setSelected(btBuyMazdaRX8!!)
                } else if (Settings.coinsTotal >= PRICE_MAZDA_RX8) {
                    Settings.coinsTotal -= PRICE_MAZDA_RX8
                    setButtonStylePurchased(btBuyMazdaRX8!!)
                    didBuyMazdaRX8 = true
                }
                savePurchases()
            }
        })

        btBuySeatIbiza = if (didBuySeatIbiza) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton("Buy", Assets.styleTextButtonBuy)

        if (Settings.selectedSkin == SKIN_SEAT_IBIZA) btBuySeatIbiza!!.isVisible = false

        addPressEffect(btBuySeatIbiza!!)
        btBuySeatIbiza!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuySeatIbiza) {
                    Settings.selectedSkin = SKIN_SEAT_IBIZA
                    setSelected(btBuySeatIbiza!!)
                } else if (Settings.coinsTotal >= PRICE_SEAT_IBIZA) {
                    Settings.coinsTotal -= PRICE_SEAT_IBIZA
                    setButtonStylePurchased(btBuySeatIbiza!!)
                    didBuySeatIbiza = true
                }
                savePurchases()
            }
        })

        btBuyVolkswagenScirocco = if (didBuyVolkswagenScirocco) TextButton(
            "Select",
            Assets.styleTextButtonPurchased
        )
        else TextButton(
            "Buy",
            Assets.styleTextButtonBuy
        )

        if (Settings.selectedSkin == SKIN_VOLKSWAGEN_SCIROCCO) btBuyVolkswagenScirocco!!.isVisible = false

        addPressEffect(btBuyVolkswagenScirocco!!)
        btBuyVolkswagenScirocco!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                if (didBuyVolkswagenScirocco) {
                    Settings.selectedSkin = SKIN_VOLKSWAGEN_SCIROCCO
                    setSelected(btBuyVolkswagenScirocco!!)
                } else if (Settings.coinsTotal >= PRICE_VOLKSWAGEN_SCIROCCO) {
                    Settings.coinsTotal -= PRICE_VOLKSWAGEN_SCIROCCO
                    setButtonStylePurchased(btBuyVolkswagenScirocco!!)
                    didBuyVolkswagenScirocco = true
                }
                savePurchases()
            }
        })

        arrayButtons!!.add(btBuyDiablo)
        arrayButtons!!.add(btBuyBanshee)
        arrayButtons!!.add(btBuyTornado)
        arrayButtons!!.add(btBuyTurismo)
        arrayButtons!!.add(btBuyAudiS5)
        arrayButtons!!.add(btBuyBmwX6)
        arrayButtons!!.add(btBuyBullet)
        arrayButtons!!.add(btBuyCrossfire)
        arrayButtons!!.add(btBuyCitroenC4)
        arrayButtons!!.add(btBuyDodgeCharger)
        arrayButtons!!.add(btBuyFiat500Lounge)
        arrayButtons!!.add(btBuyHondaCRV)
        arrayButtons!!.add(btBuyMazda6)
        arrayButtons!!.add(btBuyMazdaRX8)
        arrayButtons!!.add(btBuySeatIbiza)
        arrayButtons!!.add(btBuyVolkswagenScirocco)
    }

    private fun loadPurchases() {
        didBuyBanshee = pref.getBoolean("didBuyBanshee", false)
        didBuyTornado = pref.getBoolean("didBuyTornado", false)
        didBuyTurismo = pref.getBoolean("didBuyTurismo", false)
        didBuyAudiS5 = pref.getBoolean("didBuyAudiS5", false)
        didBuyBmwX6 = pref.getBoolean("didBuyBmwX6", false)
        didBuyCamaro = pref.getBoolean("didBuyBullet", false)
        didBuyCrossfire = pref.getBoolean("didBuyCrossfire", false)
        didBuyCitroenC4 = pref.getBoolean("didBuyCitroenC4", false)
        didBuyDodgeCharger = pref.getBoolean("didBuyDodgeCharger", false)
        didBuyFiat500 = pref.getBoolean("didBuyFiat500", false)
        didBuyHondaCRV = pref.getBoolean("didBuyHondaCRV", false)
        didBuyMazda6 = pref.getBoolean("didBuyMazda6", false)
        didBuyMazdaRX8 = pref.getBoolean("didBuyMazdaRX8", false)
        didBuySeatIbiza = pref.getBoolean("didBuySeatIbiza", false)
        didBuyVolkswagenScirocco = pref.getBoolean(
            "didBuyVolkswagenScirocco",
            false
        )
    }

    private fun savePurchases() {
        pref.putBoolean("didBuyBanshee", didBuyBanshee)
        pref.putBoolean("didBuyTornado", didBuyTornado)
        pref.putBoolean("didBuyTurismo", didBuyTurismo)
        pref.putBoolean("didBuyAudiS5", didBuyAudiS5)
        pref.putBoolean("didBuyBmwX6", didBuyBmwX6)
        pref.putBoolean("didBuyBullet", didBuyCamaro)
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
        Settings.save()
    }

    private fun setButtonStylePurchased(button: TextButton) {
        button.style = Assets.styleTextButtonPurchased
        button.setText("Select")
    }

    private fun setSelected(selectedButton: TextButton) {
        // I make all visible and at the end the selected button invisible
        for (button in arrayButtons!!) {
            button.isVisible = true
        }
        selectedButton.isVisible = false
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
        // cars
        const val SKIN_DEVIL: Int = 0
        const val SKIN_BANSHEE: Int = 1
        const val SKIN_TURISMO: Int = 3
        const val SKIN_CAMARO: Int = 6
        const val SKIN_TORNADO: Int = 2
        const val SKIN_AUDI_S5: Int = 4
        const val SKIN_BMW_X6: Int = 5
        const val SKIN_CHEVROLET_CROSSFIRE: Int = 7
        const val SKIN_CITROEN_C4: Int = 8
        const val SKIN_DODGE_CHARGER: Int = 9
        const val SKIN_FIAT_500_LOUNGE: Int = 10
        const val SKIN_HONDA_CRV: Int = 11
        const val SKIN_MAZDA_6: Int = 12
        const val SKIN_MAZDA_RX8: Int = 13
        const val SKIN_SEAT_IBIZA: Int = 14
        const val SKIN_VOLKSWAGEN_SCIROCCO: Int = 15

        private val pref: Preferences = Gdx.app
            .getPreferences("com.tiar.dragrace.shop")

        // car prices
        const val PRICE_BANSHEE: Int = 50
        const val PRICE_CAMARO: Int = 175
        const val PRICE_TURISMO: Int = 100
        const val PRICE_TORNADO: Int = 75
        const val PRICE_AUDI_S5: Int = 125
        const val PRICE_BMW_X6: Int = 150
        const val PRICE_CHEVROLET_CROSSFIRE: Int = 200
        const val PRICE_CITROEN_C4: Int = 225
        const val PRICE_DODGE_CHARGER: Int = 250
        const val PRICE_FIAT_500_LOUNGE: Int = 275
        const val PRICE_HONDA_CRV: Int = 300
        const val PRICE_MAZDA_6: Int = 325
        const val PRICE_MAZDA_RX8: Int = 350
        const val PRICE_SEAT_IBIZA: Int = 375
        const val PRICE_VOLKSWAGEN_SCIROCCO: Int = 400
    }
}
