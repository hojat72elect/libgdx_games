package com.nopalsoft.ponyrace.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.ponyrace.PonyRacingGame
import com.nopalsoft.ponyrace.Settings
import com.nopalsoft.ponyrace.menuobjetos.BotonNube
import com.nopalsoft.ponyrace.shopSubMenus.PonysSubMenu

class ShopScreen(game: PonyRacingGame) : BaseScreen(game) {
    val PRECIO_10_BOMBS: Int = 250
    val PRECIO_20_BOMBS: Int = 400
    val PRECIO_50_BOMBS: Int = 800
    val PRECIO_100_BOMBS: Int = 1450

    val PRECIO_10_WOOD: Int = 250
    val PRECIO_20_WOOD: Int = 400
    val PRECIO_50_WOOD: Int = 800
    val PRECIO_100_WOOD: Int = 1450

    val UPGRADE_PRICE_BOMBS_LEVEL1: Int = 600
    val UPGRADE_PRICE_BOMBS_LEVEL2: Int = 1100
    val UPGRADE_PRICE_BOMBS_LEVEL3: Int = 1600
    val UPGRADE_PRICE_BOMBS_LEVEL4: Int = 3500
    val UPGRADE_PRICE_BOMBS_LEVEL5: Int = 5000

    val UPGRADE_PRICE_WOOD_LEVEL1: Int = 600
    val UPGRADE_PRICE_WOOD_LEVEL2: Int = 850
    val UPGRADE_PRICE_WOOD_LEVEL3: Int = 1100
    val UPGRADE_PRICE_WOOD_LEVEL4: Int = 2500
    val UPGRADE_PRICE_WOOD_LEVEL5: Int = 4000

    val UPGRADE_PRICE_CHOCOLATE_LEVEL1: Int = 600
    val UPGRADE_PRICE_CHOCOLATE_LEVEL2: Int = 850
    val UPGRADE_PRICE_CHOCOLATE_LEVEL3: Int = 1500
    val UPGRADE_PRICE_CHOCOLATE_LEVEL4: Int = 3500
    val UPGRADE_PRICE_CHOCOLATE_LEVEL5: Int = 5000

    val UPGRADE_PRICE_BALLON_LEVEL1: Int = 600
    val UPGRADE_PRICE_BALLON_LEVEL2: Int = 850
    val UPGRADE_PRICE_BALLON_LEVEL3: Int = 1100
    val UPGRADE_PRICE_BALLON_LEVEL4: Int = 2500
    val UPGRADE_PRICE_BALLON_LEVEL5: Int = 4000

    val UPGRADE_PRICE_CHILI_LEVEL1: Int = 600
    val UPGRADE_PRICE_CHILI_LEVEL2: Int = 950
    val UPGRADE_PRICE_CHILI_LEVEL3: Int = 1500
    val UPGRADE_PRICE_CHILI_LEVEL4: Int = 3000
    val UPGRADE_PRICE_CHILI_LEVEL5: Int = 6000

    val UPGRADE_PRICE_COIN_LEVEL1: Int = 1500
    val UPGRADE_PRICE_COIN_LEVEL2: Int = 2500
    val UPGRADE_PRICE_COIN_LEVEL3: Int = 4500
    val UPGRADE_PRICE_COIN_LEVEL4: Int = 7000
    val UPGRADE_PRICE_COIN_LEVEL5: Int = 10000

    val UPGRADE_PRICE_TIME_LEVEL1: Int = 700
    val UPGRADE_PRICE_TIME_LEVEL2: Int = 1000
    val UPGRADE_PRICE_TIME_LEVEL3: Int = 2000
    val UPGRADE_PRICE_TIME_LEVEL4: Int = 3500
    val UPGRADE_PRICE_TIME_LEVEL5: Int = 7000

    var btBack: BotonNube
    var contenedor: Table
    var menuLateral: Table

    // Opciones de compra
    var btBuy10Bomb: TextButton? = null
    var btBuy20Bomb: TextButton? = null
    var btBuy50Bomb: TextButton? = null
    var btBuy100Bomb: TextButton? = null
    var btBuy10Wood: TextButton? = null
    var btBuy20Wood: TextButton? = null
    var btBuy50Wood: TextButton? = null
    var btBuy100Wood: TextButton? = null

    // Opciones de upgrades
    var btUpgradeBomb: TextButton? = null
    var btUpgradeWood: TextButton? = null
    var btUpgradeChocolate: TextButton? = null
    var btUpgradeBallon: TextButton? = null
    var btUpgradeChili: TextButton? = null
    var btUpgradeCoin: TextButton? = null
    var btUpgradeTime: TextButton? = null
    var lblPrecioUpBomb: Label? = null
    var lblPrecioUpWood: Label? = null
    var lblPrecioUpChocolate: Label? = null
    var lblPrecioBallon: Label? = null
    var lblPrecioUpChili: Label? = null
    var lblPrecioCoin: Label? = null
    var lblPrecioTime: Label? = null
    var dentroUpBombas: Table? = null
    var dentroUpWood: Table? = null
    var dentroUpChocolate: Table? = null
    var dentroUpBallon: Table? = null
    var dentroUpChili: Table? = null
    var dentroUpCoin: Table? = null
    var dentroUpTime: Table? = null
    var costUpBomb: Int
    var costUpWood: Int
    var costUpChocolate: Int
    var costUpBallon: Int
    var costUpChili: Int
    var costUpCoin: Int
    var costUpTime: Int

    var btLikeUsFacebook: TextButton? = null
    var items: TextButton? = null
    var upgrades: TextButton? = null
    var coins: TextButton? = null
    var ponys: TextButton? = null
    var itemsInt2: TextButton? = null

    init {
        costUpBomb = checkPriceBomb()
        costUpWood = checkPriceWood()
        costUpChocolate = checkPriceChocolate()
        costUpBallon = checkPriceBallon()
        costUpChili = checkPriceChili()
        costUpCoin = checkPriceCoin()
        costUpTime = checkPriceTime()

        btBack = BotonNube(assetsHandler.nube, "Back", assetsHandler.fontGde)
        btBack.setSize(150f, 100f)
        btBack.setPosition(645f, 5f)

        btBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btBack.wasSelected = true
                btBack.addAction(
                    Actions.sequence(
                        Actions.delay(.2f),
                        btBack.accionInicial, Actions.run(object : Runnable {
                            override fun run() {
                                this@ShopScreen.game!!
                                    .setScreen(
                                        LoadingScreen(
                                            this@ShopScreen.game!!,
                                            WorldMapTiledScreen::class.java
                                        )
                                    )
                            }
                        })
                    )
                )
            }
        })

        // Crear table principal (contiene el submenu de la izq y el scrollPanel de la derecha.
        contenedor = Table()

        menuLateral = Table()
        menuLateral.setSize(162f, 425f)
        menuLateral.setPosition(5f, 5f)

        val scroll = ScrollPane(contenedor)
        scroll.setSize(463f, 420f)
        scroll.setPosition(169f, 10f)
        scroll.setScrollingDisabled(true, false)

        stage.addActor(scroll)
        stage.addActor(btBack)
        stage.addActor(menuLateral)

        crearTodosLosBotones()

        setItems()
        items!!.setChecked(true)

        assetsHandler.skeletonTiendaTitle.setX(400f)
        assetsHandler.skeletonTiendaTitle.setY(450f)
    }

    private fun crearTodosLosBotones() {
        /*
         * BOTONES DEL MENU LATERAL
         */
        val btStyleMenu = TextButtonStyle(
            assetsHandler.btMenuLeftUp,
            assetsHandler.btMenuLeftDown, assetsHandler.btMenuLeftDown, assetsHandler.fontGde
        )

        items = TextButton("Items", btStyleMenu)
        items!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                unCheckOtherMenuButtons(items!!)
                setItems()
                super.clicked(event, x, y)
            }
        })

        upgrades = TextButton("Upgrades", btStyleMenu)
        upgrades!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                unCheckOtherMenuButtons(upgrades!!)
                setUpdrades()
                super.clicked(event, x, y)
            }
        })

        coins = TextButton("Bits", btStyleMenu)
        coins!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                unCheckOtherMenuButtons(coins!!)
                setCoins()
                super.clicked(event, x, y)
            }
        })

        ponys = TextButton("Ponys", btStyleMenu)
        ponys!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                unCheckOtherMenuButtons(ponys!!)
                PonysSubMenu(this@ShopScreen, contenedor)
                super.clicked(event, x, y)
            }
        })
        itemsInt2 = TextButton("?", btStyleMenu)
        itemsInt2!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                itemsInt2!!.setChecked(false)
            }
        })

        menuLateral.add<TextButton?>(items).fillX().expandX()
        menuLateral.row()
        menuLateral.add<TextButton?>(upgrades).fillX().expandX()
        menuLateral.row()
        menuLateral.add<TextButton?>(coins).fillX().expandX()
        menuLateral.row()

        menuLateral.add<TextButton?>(ponys).fillX().expandX()
        menuLateral.row()
        menuLateral.add<TextButton?>(itemsInt2).fillX().expandX()

        menuLateral.top()

        /*
         * BOTONES DE LAS OPCIONES DE COMPRA
         */
        val btStyle = TextButtonStyle(
            assetsHandler.btNubeUpTienda,
            assetsHandler.btNubeDownTienda, null, assetsHandler.fontChco
        )

        btBuy10Bomb = TextButton("Buy", btStyle)
        btBuy10Bomb!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - PRECIO_10_BOMBS >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_10_BOMBS
                    Settings.numeroBombas += 10
                }
            }
        })

        btBuy20Bomb = TextButton("Buy", btStyle)
        btBuy20Bomb!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - PRECIO_20_BOMBS >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_20_BOMBS
                    Settings.numeroBombas += 20
                }
            }
        })

        btBuy50Bomb = TextButton("Buy", btStyle)
        btBuy50Bomb!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - PRECIO_50_BOMBS >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_50_BOMBS
                    Settings.numeroBombas += 50
                }
            }
        })

        btBuy100Bomb = TextButton("Buy", btStyle)
        btBuy100Bomb!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - PRECIO_100_BOMBS >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_100_BOMBS
                    Settings.numeroBombas += 100
                }
            }
        })

        btBuy10Wood = TextButton("Buy", btStyle)
        btBuy10Wood!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - PRECIO_10_WOOD >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_10_WOOD
                    Settings.numeroWoods += 10
                }
            }
        })

        btBuy20Wood = TextButton("Buy", btStyle)
        btBuy20Wood!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - PRECIO_20_WOOD >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_20_WOOD
                    Settings.numeroWoods += 20
                }
            }
        })

        btBuy50Wood = TextButton("Buy", btStyle)
        btBuy50Wood!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - PRECIO_50_WOOD >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_50_WOOD
                    Settings.numeroWoods += 50
                }
            }
        })

        btBuy100Wood = TextButton("Buy", btStyle)
        btBuy100Wood!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - PRECIO_100_WOOD >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_100_WOOD
                    Settings.numeroWoods += 100
                }
            }
        })

        /*
         * BOTONES DE LAS OPCIONES DE UPGRADES
         */
        btUpgradeBomb = TextButton("Upgrade", btStyle)
        btUpgradeBomb!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - costUpBomb >= 0) {
                    Settings.numeroMonedasActual -= costUpBomb
                    Settings.bombLevel++
                    if (Settings.bombLevel >= 5) deleteTableDentroDentroUp(dentroUpBombas)
                    costUpBomb = checkPriceBomb()
                    setLabelPrices()
                }
            }
        })

        btUpgradeWood = TextButton("Upgrade", btStyle)
        btUpgradeWood!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - costUpWood >= 0) {
                    Settings.numeroMonedasActual -= costUpWood
                    Settings.woodLevel++
                    if (Settings.woodLevel >= 5) deleteTableDentroDentroUp(dentroUpWood)
                    costUpWood = checkPriceWood()
                    setLabelPrices()
                }
            }
        })

        btUpgradeBallon = TextButton("Upgrade", btStyle)
        btUpgradeBallon!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - costUpBallon >= 0) {
                    Settings.numeroMonedasActual -= costUpBallon
                    Settings.balloonLevel++
                    if (Settings.balloonLevel >= 5) deleteTableDentroDentroUp(dentroUpBallon)
                    costUpBallon = checkPriceBallon()
                    setLabelPrices()
                }
            }
        })

        btUpgradeChocolate = TextButton("Upgrade", btStyle)
        btUpgradeChocolate!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - costUpChocolate >= 0) {
                    Settings.numeroMonedasActual -= costUpChocolate
                    Settings.chocolateLevel++
                    if (Settings.chocolateLevel >= 5) deleteTableDentroDentroUp(dentroUpChocolate)
                    costUpChocolate = checkPriceChocolate()
                    setLabelPrices()
                }
            }
        })

        btUpgradeChili = TextButton("Upgrade", btStyle)
        btUpgradeChili!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - costUpChili >= 0) {
                    Settings.numeroMonedasActual -= costUpChili
                    Settings.chiliLevel++
                    if (Settings.chiliLevel >= 5) deleteTableDentroDentroUp(dentroUpChili)
                    costUpChili = checkPriceChili()
                    setLabelPrices()
                }
            }
        })

        btUpgradeCoin = TextButton("Upgrade", btStyle)
        btUpgradeCoin!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - costUpCoin >= 0) {
                    Settings.numeroMonedasActual -= costUpCoin
                    Settings.coinLevel++
                    if (Settings.coinLevel >= 5) deleteTableDentroDentroUp(dentroUpCoin)
                    costUpCoin = checkPriceCoin()
                    setLabelPrices()
                }
            }
        })

        btUpgradeTime = TextButton("Upgrade", btStyle)
        btUpgradeTime!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (Settings.numeroMonedasActual - costUpTime >= 0) {
                    Settings.numeroMonedasActual -= costUpTime
                    Settings.timeLevel++
                    if (Settings.timeLevel >= 5) deleteTableDentroDentroUp(dentroUpTime)
                    costUpTime = checkPriceTime()
                    setLabelPrices()
                }
            }
        })

        /*
         * BOTONES OPCIONES COINS
         */
        val btFaceText: String
        if (Settings.wasAppLiked) btFaceText = "Visit us"
        else btFaceText = "Like us"

        btLikeUsFacebook = TextButton(btFaceText, btStyle)
        btLikeUsFacebook!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btLikeUsFacebook!!.addAction(
                    Actions.sequence(
                        Actions.delay(2f),
                        Actions.run(object : Runnable {
                            override fun run() {
                                if (!Settings.wasAppLiked) {
                                    Settings.wasAppLiked = true
                                    Settings.sumarMonedas(Settings.MONEDAS_REGALO_FACEBOOK)
                                    Settings.guardar()
                                    btLikeUsFacebook!!.setText("Visit us")
                                }
                            }
                        })
                    )
                )
            }
        })

        val lblStyle = LabelStyle(assetsHandler.fontChco, Color.WHITE)

        lblPrecioUpBomb = Label("", lblStyle)
        lblPrecioUpWood = Label("", lblStyle)
        lblPrecioUpChocolate = Label("", lblStyle)
        lblPrecioBallon = Label("", lblStyle)
        lblPrecioUpChili = Label("", lblStyle)
        lblPrecioCoin = Label("", lblStyle)
        lblPrecioTime = Label("", lblStyle)

        lblPrecioUpBomb!!.setAlignment(Align.left)
        setLabelPrices()
    }

    private fun deleteTableDentroDentroUp(tabDelete: Table?) {
        for (actor in contenedor.getChildren()) {
            val obj = actor as Table

            obj.removeActor(tabDelete)
        }
    }

    private fun setLabelPrices() {
        // Bomb
        if (Settings.bombLevel >= 5) {
            lblPrecioUpBomb!!.setText("")
        } else {
            lblPrecioUpBomb!!.setText(costUpBomb.toString() + "")
        }

        // WOOD
        if (Settings.woodLevel >= 5) {
            lblPrecioUpWood!!.setText("")
        } else {
            lblPrecioUpWood!!.setText(costUpWood.toString() + "")
        }

        // Chocolate
        if (Settings.chocolateLevel >= 5) {
            lblPrecioUpChocolate!!.setText("")
        } else {
            lblPrecioUpChocolate!!.setText(costUpChocolate.toString() + "")
        }

        // Ballon
        if (Settings.balloonLevel >= 5) {
            lblPrecioBallon!!.setText("")
        } else {
            lblPrecioBallon!!.setText(costUpBallon.toString() + "")
        }

        // Chili
        if (Settings.chiliLevel >= 5) {
            lblPrecioUpChili!!.setText("")
        } else {
            lblPrecioUpChili!!.setText(costUpChili.toString() + "")
        }

        // Coin
        if (Settings.coinLevel >= 5) {
            lblPrecioCoin!!.setText("")
        } else {
            lblPrecioCoin!!.setText(costUpCoin.toString() + "")
        }

        // time
        if (Settings.timeLevel >= 5) {
            lblPrecioTime!!.setText("")
        } else {
            lblPrecioTime!!.setText(costUpTime.toString() + "")
        }
    }

    private fun setItems() {
        contenedor.clear()

        val lblStyle = LabelStyle(assetsHandler.fontChco, Color.WHITE)

        var tbDentro = Table()

        // 10 Bombas
        tbDentro.add<Image?>(Image(assetsHandler.bombaTienda)).size(35f, 38f)
        var cantidad = Label("x10", lblStyle)
        tbDentro.add<Label?>(cantidad).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<Image?>(Image(assetsHandler.monedaTienda))
        var precio = Label(PRECIO_10_BOMBS.toString() + "", lblStyle)
        tbDentro.add<Label?>(precio).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<TextButton?>(btBuy10Bomb).size(85f, 65f)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // 20 Bombas
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.bombaTienda)).size(35f, 38f)
        cantidad = Label("x20", lblStyle)
        tbDentro.add<Label?>(cantidad).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<Image?>(Image(assetsHandler.monedaTienda))
        precio = Label(PRECIO_20_BOMBS.toString() + "", lblStyle)
        tbDentro.add<Label?>(precio).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<TextButton?>(btBuy20Bomb).size(85f, 65f)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // 50 Bombas
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.bombaTienda)).size(35f, 38f)
        cantidad = Label("x50", lblStyle)
        tbDentro.add<Label?>(cantidad).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<Image?>(Image(assetsHandler.monedaTienda))
        precio = Label(PRECIO_50_BOMBS.toString() + "", lblStyle)
        tbDentro.add<Label?>(precio).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<TextButton?>(btBuy50Bomb).size(85f, 65f)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // 100 Bombas
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.bombaTienda)).size(35f, 38f)
        cantidad = Label("x100", lblStyle)
        tbDentro.add<Label?>(cantidad).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<Image?>(Image(assetsHandler.monedaTienda))
        precio = Label(PRECIO_100_BOMBS.toString() + "", lblStyle)
        tbDentro.add<Label?>(precio).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<TextButton?>(btBuy100Bomb).size(85f, 65f)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // 10 Madera
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.bananaSpikeTienda)).size(35f, 38f)
        cantidad = Label("x10", lblStyle)
        tbDentro.add<Label?>(cantidad).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<Image?>(Image(assetsHandler.monedaTienda))
        precio = Label(PRECIO_10_WOOD.toString() + "", lblStyle)
        tbDentro.add<Label?>(precio).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<TextButton?>(btBuy10Wood).size(85f, 65f)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // 20 Madera
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.bananaSpikeTienda)).size(35f, 38f)
        cantidad = Label("x20", lblStyle)
        tbDentro.add<Label?>(cantidad).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<Image?>(Image(assetsHandler.monedaTienda))
        precio = Label(PRECIO_20_WOOD.toString() + "", lblStyle)
        tbDentro.add<Label?>(precio).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<TextButton?>(btBuy20Wood).size(85f, 65f)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // 50 Madera
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.bananaSpikeTienda)).size(35f, 38f)
        cantidad = Label("x50", lblStyle)
        tbDentro.add<Label?>(cantidad).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<Image?>(Image(assetsHandler.monedaTienda))
        precio = Label(PRECIO_50_WOOD.toString() + "", lblStyle)
        tbDentro.add<Label?>(precio).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<TextButton?>(btBuy50Wood).size(85f, 65f)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // 100 Bombas
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.bananaSpikeTienda)).size(35f, 38f)
        cantidad = Label("x100", lblStyle)
        tbDentro.add<Label?>(cantidad).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<Image?>(Image(assetsHandler.monedaTienda))
        precio = Label(PRECIO_100_WOOD.toString() + "", lblStyle)
        tbDentro.add<Label?>(precio).width(80f)
        tbDentro.add().width(30f)
        tbDentro.add<TextButton?>(btBuy100Wood).size(85f, 65f)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)
    }

    private fun setUpdrades() {
        contenedor.clear()
        val lblStyle = LabelStyle(assetsHandler.fontChco, Color.WHITE)

        // Upgrade Bombas
        var tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.bombaTienda)).size(35f, 38f).padLeft(10f)
            .padRight(10f)
        var descripcion = Label("Bomb Efect Last Longer", lblStyle)
        descripcion.setWrap(true)
        tbDentro.add<Label?>(descripcion).expandX().fill()
        dentroUpBombas = Table()
        dentroUpBombas!!.add<Image?>(Image(assetsHandler.monedaTienda))
        dentroUpBombas!!.add<Label?>(lblPrecioUpBomb).left()
        dentroUpBombas!!.row().colspan(2)
        dentroUpBombas!!.add<TextButton?>(btUpgradeBomb).size(120f, 70f)
        if (Settings.bombLevel < 5) tbDentro.add<Table?>(dentroUpBombas)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // Upgrade Wood
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.bananaSpikeTienda)).size(35f, 38f)
            .padLeft(10f).padRight(10f)
        descripcion = Label(
            "Bananas and spikes effects last longer",
            lblStyle
        )
        descripcion.setWrap(true)
        tbDentro.add<Label?>(descripcion).expandX().fill()
        dentroUpWood = Table()
        dentroUpWood!!.add<Image?>(Image(assetsHandler.monedaTienda))
        dentroUpWood!!.add<Label?>(lblPrecioUpWood).left()
        dentroUpWood!!.row().colspan(2)
        dentroUpWood!!.add<TextButton?>(btUpgradeWood).size(120f, 70f)
        if (Settings.woodLevel < 5) tbDentro.add<Table?>(dentroUpWood)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // Upgrade Chocolate
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.chocolateTienda)).size(35f, 38f)
            .padLeft(10f).padRight(10f)
        descripcion = Label("Chocolate efect last longer", lblStyle)
        descripcion.setWrap(true)
        tbDentro.add<Label?>(descripcion).expandX().fill()
        dentroUpChocolate = Table()
        dentroUpChocolate!!.add<Image?>(Image(assetsHandler.monedaTienda))
        dentroUpChocolate!!.add<Label?>(lblPrecioUpChocolate).left()
        dentroUpChocolate!!.row().colspan(2)
        dentroUpChocolate!!.add<TextButton?>(btUpgradeChocolate).size(120f, 70f)
        if (Settings.chocolateLevel < 5) tbDentro.add<Table?>(dentroUpChocolate)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // Upgrade Ballon
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.globoTienda)).size(35f, 45f).padLeft(10f)
            .padRight(10f)
        descripcion = Label("Ballons reward extra time", lblStyle)
        descripcion.setWrap(true)
        tbDentro.add<Label?>(descripcion).expandX().fill()
        dentroUpBallon = Table()
        dentroUpBallon!!.add<Image?>(Image(assetsHandler.monedaTienda))
        dentroUpBallon!!.add<Label?>(lblPrecioBallon).left()
        dentroUpBallon!!.row().colspan(2)
        dentroUpBallon!!.add<TextButton?>(btUpgradeBallon).size(120f, 70f)
        if (Settings.balloonLevel < 5) tbDentro.add<Table?>(dentroUpBallon)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // Upgrade Chili
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.chileTienda)).size(35f, 38f).padLeft(10f)
            .padRight(10f)
        descripcion = Label("Chili pepper effect last longer", lblStyle)
        descripcion.setWrap(true)
        tbDentro.add<Label?>(descripcion).expandX().fill()
        dentroUpChili = Table()
        dentroUpChili!!.add<Image?>(Image(assetsHandler.monedaTienda))
        dentroUpChili!!.add<Label?>(lblPrecioUpChili).left()
        dentroUpChili!!.row().colspan(2)
        dentroUpChili!!.add<TextButton?>(btUpgradeChili).size(120f, 70f)
        if (Settings.chiliLevel < 5) tbDentro.add<Table?>(dentroUpChili)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // UPGRADE COIN
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.monedaTienda)).size(35f, 38f).padLeft(10f)
            .padRight(10f)
        descripcion = Label("Bitcoins earn extra bits", lblStyle)
        descripcion.setWrap(true)
        tbDentro.add<Label?>(descripcion).expandX().fill()
        dentroUpCoin = Table()
        dentroUpCoin!!.add<Image?>(Image(assetsHandler.monedaTienda))
        dentroUpCoin!!.add<Label?>(lblPrecioCoin).left()
        dentroUpCoin!!.row().colspan(2)
        dentroUpCoin!!.add<TextButton?>(btUpgradeCoin).size(120f, 70f)
        if (Settings.coinLevel < 5) tbDentro.add<Table?>(dentroUpCoin)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)

        // UPGRADE TIME
        tbDentro = Table()
        tbDentro.add<Image?>(Image(assetsHandler.cronometroTienda)).size(35f, 38f)
            .padLeft(10f).padRight(10f)
        descripcion = Label(
            "Time left in stopwatch earn extra bits",
            lblStyle
        )
        descripcion.setWrap(true)
        tbDentro.add<Label?>(descripcion).expandX().fill()
        dentroUpTime = Table()
        dentroUpTime!!.add<Image?>(Image(assetsHandler.monedaTienda))
        dentroUpTime!!.add<Label?>(lblPrecioTime).left()
        dentroUpTime!!.row().colspan(2)
        dentroUpTime!!.add<TextButton?>(btUpgradeTime).size(120f, 70f)
        if (Settings.timeLevel < 5) tbDentro.add<Table?>(dentroUpTime)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)
    }

    private fun setCoins() {
        contenedor.clear()

        contenedor.clear()
        val lblStyle = LabelStyle(assetsHandler.fontChco, Color.WHITE)

        // Upgrade Bombas
        val tbDentro = Table()


        tbDentro.add<Image?>(Image(assetsHandler.btnFacebook)).size(40f, 40f)
            .padLeft(10f).padRight(10f)
        val descripcion = Label(
            "Like us on facebook and get 3500 bit coins", lblStyle
        )
        descripcion.setWrap(true)
        tbDentro.add<Label?>(descripcion).expandX().fill()
        tbDentro.add<TextButton?>(btLikeUsFacebook).size(120f, 70f)
        contenedor.add<Table?>(tbDentro).expandX().fill()
        contenedor.row().padTop(15f)
    }

    override fun update(delta: Float) {
        // TODO Auto-generated method stub
    }

    private fun unCheckOtherMenuButtons(checkThisButton: TextButton) {
        items!!.setChecked(false)
        upgrades!!.setChecked(false)
        coins!!.setChecked(false)
        ponys!!.setChecked(false)
        itemsInt2!!.setChecked(false)
        checkThisButton.setChecked(true)
    }

    override fun draw(delta: Float) {
        camera!!.update()
        batch!!.setProjectionMatrix(camera!!.combined)

        batch!!.disableBlending()
        batch!!.begin()
        batch!!.draw(assetsHandler.fondoTienda, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())

        batch!!.enableBlending()
        batch!!.draw(assetsHandler.monedaTienda, 5f, 440f, 35f, 35f)
        assetsHandler.fontGde.draw(
            batch, Settings.numeroMonedasActual.toString() + "", 45f,
            470f
        )

        renderTitle(delta)

        batch!!.draw(assetsHandler.bombaTienda, 635f, 405f, 30f, 30f)
        assetsHandler.fontChco.draw(
            batch, (Settings.numeroBombas.toString() + " lvl "
                    + Settings.bombLevel + "/5"), 660f, 425f
        )

        batch!!.draw(assetsHandler.bananaSpikeTienda, 635f, 355f, 30f, 30f)
        assetsHandler.fontChco.draw(
            batch, (Settings.numeroWoods.toString() + " lvl "
                    + Settings.woodLevel + "/5"), 660f, 375f
        )

        batch!!.draw(assetsHandler.chocolateTienda, 635f, 305f, 30f, 30f)
        assetsHandler.fontChco.draw(
            batch, "lvl " + Settings.chocolateLevel + "/5",
            670f, 325f
        )
        // //
        batch!!.draw(assetsHandler.globoTienda, 635f, 255f, 30f, 30f)
        assetsHandler.fontChco.draw(
            batch, "lvl " + Settings.balloonLevel + "/5",
            670f, 275f
        )
        //
        batch!!.draw(assetsHandler.chileTienda, 635f, 205f, 30f, 30f)
        assetsHandler.fontChco.draw(
            batch, "lvl " + Settings.chiliLevel + "/5",
            670f, 225f
        )

        batch!!.draw(assetsHandler.monedaTienda, 635f, 155f, 30f, 30f)
        assetsHandler.fontChco.draw(
            batch, "lvl " + Settings.coinLevel + "/5", 670f,
            175f
        )

        batch!!.draw(assetsHandler.cronometroTienda, 635f, 105f, 30f, 30f)
        assetsHandler.fontChco.draw(
            batch, "lvl " + Settings.timeLevel + "/5", 670f,
            125f
        )

        batch!!.end()

        stage.act(delta)
        stage.draw()
    }

    private fun renderTitle(delta: Float) {
        assetsHandler.animationTiendaTitle.apply(
            assetsHandler.skeletonTiendaTitle,
            screenLastStateTime, ScreenStateTime, true, null
        )
        assetsHandler.skeletonTiendaTitle.updateWorldTransform()
        assetsHandler.skeletonTiendaTitle.update(delta)
        skeletonRenderer!!.draw(batch, assetsHandler.skeletonTiendaTitle)
    }

    private fun checkPriceBomb(): Int {
        when (Settings.bombLevel + 1) {
            1 -> return UPGRADE_PRICE_BOMBS_LEVEL1
            2 -> return UPGRADE_PRICE_BOMBS_LEVEL2

            3 -> return UPGRADE_PRICE_BOMBS_LEVEL3

            4 -> return UPGRADE_PRICE_BOMBS_LEVEL4

            5 -> return UPGRADE_PRICE_BOMBS_LEVEL5
            else -> return UPGRADE_PRICE_BOMBS_LEVEL5
        }
    }

    private fun checkPriceWood(): Int {
        when (Settings.woodLevel + 1) {
            1 -> return UPGRADE_PRICE_WOOD_LEVEL1
            2 -> return UPGRADE_PRICE_WOOD_LEVEL2

            3 -> return UPGRADE_PRICE_WOOD_LEVEL3

            4 -> return UPGRADE_PRICE_WOOD_LEVEL4

            5 -> return UPGRADE_PRICE_WOOD_LEVEL5
            else -> return UPGRADE_PRICE_WOOD_LEVEL5
        }
    }

    private fun checkPriceChocolate(): Int {
        when (Settings.chocolateLevel + 1) {
            1 -> return UPGRADE_PRICE_CHOCOLATE_LEVEL1
            2 -> return UPGRADE_PRICE_CHOCOLATE_LEVEL2

            3 -> return UPGRADE_PRICE_CHOCOLATE_LEVEL3

            4 -> return UPGRADE_PRICE_CHOCOLATE_LEVEL4

            5 -> return UPGRADE_PRICE_CHOCOLATE_LEVEL5
            else -> return UPGRADE_PRICE_CHOCOLATE_LEVEL5
        }
    }

    private fun checkPriceBallon(): Int {
        when (Settings.balloonLevel + 1) {
            1 -> return UPGRADE_PRICE_BALLON_LEVEL1
            2 -> return UPGRADE_PRICE_BALLON_LEVEL2

            3 -> return UPGRADE_PRICE_BALLON_LEVEL3

            4 -> return UPGRADE_PRICE_BALLON_LEVEL4

            5 -> return UPGRADE_PRICE_BALLON_LEVEL5
            else -> return UPGRADE_PRICE_BALLON_LEVEL5
        }
    }

    private fun checkPriceChili(): Int {
        when (Settings.chiliLevel + 1) {
            1 -> return UPGRADE_PRICE_CHILI_LEVEL1
            2 -> return UPGRADE_PRICE_CHILI_LEVEL2

            3 -> return UPGRADE_PRICE_CHILI_LEVEL3

            4 -> return UPGRADE_PRICE_CHILI_LEVEL4

            5 -> return UPGRADE_PRICE_CHILI_LEVEL5
            else -> return UPGRADE_PRICE_CHILI_LEVEL5
        }
    }

    private fun checkPriceCoin(): Int {
        when (Settings.coinLevel + 1) {
            1 -> return UPGRADE_PRICE_COIN_LEVEL1
            2 -> return UPGRADE_PRICE_COIN_LEVEL2

            3 -> return UPGRADE_PRICE_COIN_LEVEL3

            4 -> return UPGRADE_PRICE_COIN_LEVEL4

            5 -> return UPGRADE_PRICE_COIN_LEVEL5
            else -> return UPGRADE_PRICE_COIN_LEVEL5
        }
    }

    private fun checkPriceTime(): Int {
        when (Settings.timeLevel + 1) {
            1 -> return UPGRADE_PRICE_TIME_LEVEL1
            2 -> return UPGRADE_PRICE_TIME_LEVEL2

            3 -> return UPGRADE_PRICE_TIME_LEVEL3

            4 -> return UPGRADE_PRICE_TIME_LEVEL4

            5 -> return UPGRADE_PRICE_TIME_LEVEL5
            else -> return UPGRADE_PRICE_TIME_LEVEL5
        }
    }

    override fun show() {
    }

    override fun hide() {
        super.hide()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            this@ShopScreen.game!!.setScreen(
                LoadingScreen(
                    this@ShopScreen.game!!, WorldMapTiledScreen::class.java
                )
            )
            return true
        }
        return false
    }
}
