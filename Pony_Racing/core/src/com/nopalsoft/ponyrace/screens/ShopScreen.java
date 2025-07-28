package com.nopalsoft.ponyrace.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.ponyrace.PonyRacingGame;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.menuobjetos.BotonNube;
import com.nopalsoft.ponyrace.shopSubMenus.PonysSubMenu;

public class ShopScreen extends BaseScreen {

    final int PRECIO_10_BOMBS = 250;
    final int PRECIO_20_BOMBS = 400;
    final int PRECIO_50_BOMBS = 800;
    final int PRECIO_100_BOMBS = 1450;

    final int PRECIO_10_WOOD = 250;
    final int PRECIO_20_WOOD = 400;
    final int PRECIO_50_WOOD = 800;
    final int PRECIO_100_WOOD = 1450;

    final int UPGRADE_PRICE_BOMBS_LEVEL1 = 600;
    final int UPGRADE_PRICE_BOMBS_LEVEL2 = 1100;
    final int UPGRADE_PRICE_BOMBS_LEVEL3 = 1600;
    final int UPGRADE_PRICE_BOMBS_LEVEL4 = 3500;
    final int UPGRADE_PRICE_BOMBS_LEVEL5 = 5000;

    final int UPGRADE_PRICE_WOOD_LEVEL1 = 600;
    final int UPGRADE_PRICE_WOOD_LEVEL2 = 850;
    final int UPGRADE_PRICE_WOOD_LEVEL3 = 1100;
    final int UPGRADE_PRICE_WOOD_LEVEL4 = 2500;
    final int UPGRADE_PRICE_WOOD_LEVEL5 = 4000;

    final int UPGRADE_PRICE_CHOCOLATE_LEVEL1 = 600;
    final int UPGRADE_PRICE_CHOCOLATE_LEVEL2 = 850;
    final int UPGRADE_PRICE_CHOCOLATE_LEVEL3 = 1500;
    final int UPGRADE_PRICE_CHOCOLATE_LEVEL4 = 3500;
    final int UPGRADE_PRICE_CHOCOLATE_LEVEL5 = 5000;

    final int UPGRADE_PRICE_BALLON_LEVEL1 = 600;
    final int UPGRADE_PRICE_BALLON_LEVEL2 = 850;
    final int UPGRADE_PRICE_BALLON_LEVEL3 = 1100;
    final int UPGRADE_PRICE_BALLON_LEVEL4 = 2500;
    final int UPGRADE_PRICE_BALLON_LEVEL5 = 4000;

    final int UPGRADE_PRICE_CHILI_LEVEL1 = 600;
    final int UPGRADE_PRICE_CHILI_LEVEL2 = 950;
    final int UPGRADE_PRICE_CHILI_LEVEL3 = 1500;
    final int UPGRADE_PRICE_CHILI_LEVEL4 = 3000;
    final int UPGRADE_PRICE_CHILI_LEVEL5 = 6000;

    final int UPGRADE_PRICE_COIN_LEVEL1 = 1500;
    final int UPGRADE_PRICE_COIN_LEVEL2 = 2500;
    final int UPGRADE_PRICE_COIN_LEVEL3 = 4500;
    final int UPGRADE_PRICE_COIN_LEVEL4 = 7000;
    final int UPGRADE_PRICE_COIN_LEVEL5 = 10000;

    final int UPGRADE_PRICE_TIME_LEVEL1 = 700;
    final int UPGRADE_PRICE_TIME_LEVEL2 = 1000;
    final int UPGRADE_PRICE_TIME_LEVEL3 = 2000;
    final int UPGRADE_PRICE_TIME_LEVEL4 = 3500;
    final int UPGRADE_PRICE_TIME_LEVEL5 = 7000;

    BotonNube btBack;
    Table contenedor;
    Table menuLateral;

    // Opciones de compra
    TextButton btBuy10Bomb, btBuy20Bomb, btBuy50Bomb, btBuy100Bomb;
    TextButton btBuy10Wood, btBuy20Wood, btBuy50Wood, btBuy100Wood;

    // Opciones de upgrades
    TextButton btUpgradeBomb, btUpgradeWood, btUpgradeChocolate,
            btUpgradeBallon, btUpgradeChili, btUpgradeCoin, btUpgradeTime;
    Label lblPrecioUpBomb, lblPrecioUpWood, lblPrecioUpChocolate,
            lblPrecioBallon, lblPrecioUpChili, lblPrecioCoin, lblPrecioTime;
    Table dentroUpBombas, dentroUpWood, dentroUpChocolate, dentroUpBallon,
            dentroUpChili, dentroUpCoin, dentroUpTime;
    int costUpBomb, costUpWood, costUpChocolate, costUpBallon, costUpChili,
            costUpCoin, costUpTime;

    TextButton btLikeUsFacebook;
    TextButton items, upgrades, coins, ponys, itemsInt2;

    public ShopScreen(PonyRacingGame game) {
        super(game);

        costUpBomb = checkPriceBomb();
        costUpWood = checkPriceWood();
        costUpChocolate = checkPriceChocolate();
        costUpBallon = checkPriceBallon();
        costUpChili = checkPriceChili();
        costUpCoin = checkPriceCoin();
        costUpTime = checkPriceTime();

        btBack = new BotonNube(assetsHandler.nube, "Back", assetsHandler.fontGde);
        btBack.setSize(150, 100);
        btBack.setPosition(645, 5);

        btBack.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                btBack.wasSelected = true;
                btBack.addAction(Actions.sequence(Actions.delay(.2f),
                        btBack.accionInicial, Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                ShopScreen.this.game
                                        .setScreen(new LoadingScreen(
                                                ShopScreen.this.game,
                                                WorldMapTiledScreen.class));
                            }
                        })));
            }
        });

        // Crear table principal (contiene el submenu de la izq y el scrollPanel de la derecha.
        contenedor = new Table();

        menuLateral = new Table();
        menuLateral.setSize(162, 425);
        menuLateral.setPosition(5, 5);

        ScrollPane scroll = new ScrollPane(contenedor);
        scroll.setSize(463, 420);
        scroll.setPosition(169, 10);
        scroll.setScrollingDisabled(true, false);

        stage.addActor(scroll);
        stage.addActor(btBack);
        stage.addActor(menuLateral);

        crearTodosLosBotones();

        setItems();
        items.setChecked(true);

        assetsHandler.skeletonTiendaTitle.setX(400);
        assetsHandler.skeletonTiendaTitle.setY(450);
    }

    private void crearTodosLosBotones() {
        /*
         * BOTONES DEL MENU LATERAL
         */
        TextButtonStyle btStyleMenu = new TextButtonStyle(assetsHandler.btMenuLeftUp,
                assetsHandler.btMenuLeftDown, assetsHandler.btMenuLeftDown, assetsHandler.fontGde);

        items = new TextButton("Items", btStyleMenu);
        items.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                unCheckOtherMenuButtons(items);
                setItems();
                super.clicked(event, x, y);
            }
        });

        upgrades = new TextButton("Upgrades", btStyleMenu);
        upgrades.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                unCheckOtherMenuButtons(upgrades);
                setUpdrades();
                super.clicked(event, x, y);
            }
        });

        coins = new TextButton("Bits", btStyleMenu);
        coins.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                unCheckOtherMenuButtons(coins);
                setCoins();
                super.clicked(event, x, y);
            }
        });

        ponys = new TextButton("Ponys", btStyleMenu);
        ponys.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                unCheckOtherMenuButtons(ponys);
                new PonysSubMenu(ShopScreen.this, contenedor);
                super.clicked(event, x, y);
            }
        });
        itemsInt2 = new TextButton("?", btStyleMenu);
        itemsInt2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

                itemsInt2.setChecked(false);
            }
        });

        menuLateral.add(items).fillX().expandX();
        menuLateral.row();
        menuLateral.add(upgrades).fillX().expandX();
        menuLateral.row();
        menuLateral.add(coins).fillX().expandX();
        menuLateral.row();

        menuLateral.add(ponys).fillX().expandX();
        menuLateral.row();
        menuLateral.add(itemsInt2).fillX().expandX();

        menuLateral.top();

        /*
         * BOTONES DE LAS OPCIONES DE COMPRA
         */
        TextButtonStyle btStyle = new TextButtonStyle(assetsHandler.btNubeUpTienda,
                assetsHandler.btNubeDownTienda, null, assetsHandler.fontChco);

        btBuy10Bomb = new TextButton("Buy", btStyle);
        btBuy10Bomb.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - PRECIO_10_BOMBS >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_10_BOMBS;
                    Settings.numeroBombas += 10;
                }
            }
        });

        btBuy20Bomb = new TextButton("Buy", btStyle);
        btBuy20Bomb.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - PRECIO_20_BOMBS >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_20_BOMBS;
                    Settings.numeroBombas += 20;
                }
            }
        });

        btBuy50Bomb = new TextButton("Buy", btStyle);
        btBuy50Bomb.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - PRECIO_50_BOMBS >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_50_BOMBS;
                    Settings.numeroBombas += 50;
                }
            }
        });

        btBuy100Bomb = new TextButton("Buy", btStyle);
        btBuy100Bomb.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - PRECIO_100_BOMBS >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_100_BOMBS;
                    Settings.numeroBombas += 100;
                }
            }
        });

        btBuy10Wood = new TextButton("Buy", btStyle);
        btBuy10Wood.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - PRECIO_10_WOOD >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_10_WOOD;
                    Settings.numeroWoods += 10;
                }
            }
        });

        btBuy20Wood = new TextButton("Buy", btStyle);
        btBuy20Wood.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - PRECIO_20_WOOD >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_20_WOOD;
                    Settings.numeroWoods += 20;
                }
            }
        });

        btBuy50Wood = new TextButton("Buy", btStyle);
        btBuy50Wood.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - PRECIO_50_WOOD >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_50_WOOD;
                    Settings.numeroWoods += 50;
                }
            }
        });

        btBuy100Wood = new TextButton("Buy", btStyle);
        btBuy100Wood.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - PRECIO_100_WOOD >= 0) {
                    Settings.numeroMonedasActual -= PRECIO_100_WOOD;
                    Settings.numeroWoods += 100;
                }
            }
        });

        /*
         * BOTONES DE LAS OPCIONES DE UPGRADES
         */
        btUpgradeBomb = new TextButton("Upgrade", btStyle);
        btUpgradeBomb.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - costUpBomb >= 0) {
                    Settings.numeroMonedasActual -= costUpBomb;
                    Settings.bombLevel++;
                    if (Settings.bombLevel >= 5)
                        deleteTableDentroDentroUp(dentroUpBombas);
                    costUpBomb = checkPriceBomb();
                    setLabelPrices();
                }
            }
        });

        btUpgradeWood = new TextButton("Upgrade", btStyle);
        btUpgradeWood.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - costUpWood >= 0) {
                    Settings.numeroMonedasActual -= costUpWood;
                    Settings.woodLevel++;
                    if (Settings.woodLevel >= 5)
                        deleteTableDentroDentroUp(dentroUpWood);
                    costUpWood = checkPriceWood();
                    setLabelPrices();
                }
            }
        });

        btUpgradeBallon = new TextButton("Upgrade", btStyle);
        btUpgradeBallon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - costUpBallon >= 0) {
                    Settings.numeroMonedasActual -= costUpBallon;
                    Settings.balloonLevel++;
                    if (Settings.balloonLevel >= 5)
                        deleteTableDentroDentroUp(dentroUpBallon);
                    costUpBallon = checkPriceBallon();
                    setLabelPrices();
                }
            }
        });

        btUpgradeChocolate = new TextButton("Upgrade", btStyle);
        btUpgradeChocolate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - costUpChocolate >= 0) {
                    Settings.numeroMonedasActual -= costUpChocolate;
                    Settings.chocolateLevel++;
                    if (Settings.chocolateLevel >= 5)
                        deleteTableDentroDentroUp(dentroUpChocolate);
                    costUpChocolate = checkPriceChocolate();
                    setLabelPrices();
                }
            }
        });

        btUpgradeChili = new TextButton("Upgrade", btStyle);
        btUpgradeChili.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - costUpChili >= 0) {
                    Settings.numeroMonedasActual -= costUpChili;
                    Settings.chiliLevel++;
                    if (Settings.chiliLevel >= 5)
                        deleteTableDentroDentroUp(dentroUpChili);
                    costUpChili = checkPriceChili();
                    setLabelPrices();
                }
            }
        });

        btUpgradeCoin = new TextButton("Upgrade", btStyle);
        btUpgradeCoin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - costUpCoin >= 0) {
                    Settings.numeroMonedasActual -= costUpCoin;
                    Settings.coinLevel++;
                    if (Settings.coinLevel >= 5)
                        deleteTableDentroDentroUp(dentroUpCoin);
                    costUpCoin = checkPriceCoin();
                    setLabelPrices();
                }
            }
        });

        btUpgradeTime = new TextButton("Upgrade", btStyle);
        btUpgradeTime.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.numeroMonedasActual - costUpTime >= 0) {
                    Settings.numeroMonedasActual -= costUpTime;
                    Settings.timeLevel++;
                    if (Settings.timeLevel >= 5)
                        deleteTableDentroDentroUp(dentroUpTime);
                    costUpTime = checkPriceTime();
                    setLabelPrices();
                }
            }
        });

        /*
         * BOTONES OPCIONES COINS
         */
        final String btFaceText;
        if (Settings.wasAppLiked)
            btFaceText = "Visit us";
        else
            btFaceText = "Like us";

        btLikeUsFacebook = new TextButton(btFaceText, btStyle);
        btLikeUsFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                btLikeUsFacebook.addAction(Actions.sequence(Actions.delay(2f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                if (!Settings.wasAppLiked) {
                                    Settings.wasAppLiked = true;
                                    Settings.sumarMonedas(Settings.MONEDAS_REGALO_FACEBOOK);
                                    Settings.guardar();
                                    btLikeUsFacebook.setText("Visit us");
                                }
                            }
                        })));
            }
        });

        LabelStyle lblStyle = new LabelStyle(assetsHandler.fontChco, Color.WHITE);

        lblPrecioUpBomb = new Label("", lblStyle);
        lblPrecioUpWood = new Label("", lblStyle);
        lblPrecioUpChocolate = new Label("", lblStyle);
        lblPrecioBallon = new Label("", lblStyle);
        lblPrecioUpChili = new Label("", lblStyle);
        lblPrecioCoin = new Label("", lblStyle);
        lblPrecioTime = new Label("", lblStyle);

        lblPrecioUpBomb.setAlignment(Align.left);
        setLabelPrices();
    }

    private void deleteTableDentroDentroUp(Table tabDelete) {
        for (Actor actor : contenedor.getChildren()) {
            Table obj = (Table) actor;

            obj.removeActor(tabDelete);
        }
    }

    private void setLabelPrices() {
        // Bomb
        if (Settings.bombLevel >= 5) {
            lblPrecioUpBomb.setText("");
        } else {
            lblPrecioUpBomb.setText(costUpBomb + "");
        }

        // WOOD
        if (Settings.woodLevel >= 5) {
            lblPrecioUpWood.setText("");
        } else {
            lblPrecioUpWood.setText(costUpWood + "");
        }

        // Chocolate
        if (Settings.chocolateLevel >= 5) {
            lblPrecioUpChocolate.setText("");
        } else {
            lblPrecioUpChocolate.setText(costUpChocolate + "");
        }

        // Ballon
        if (Settings.balloonLevel >= 5) {
            lblPrecioBallon.setText("");
        } else {
            lblPrecioBallon.setText(costUpBallon + "");
        }

        // Chili
        if (Settings.chiliLevel >= 5) {
            lblPrecioUpChili.setText("");
        } else {
            lblPrecioUpChili.setText(costUpChili + "");
        }

        // Coin
        if (Settings.coinLevel >= 5) {
            lblPrecioCoin.setText("");
        } else {
            lblPrecioCoin.setText(costUpCoin + "");
        }

        // time
        if (Settings.timeLevel >= 5) {
            lblPrecioTime.setText("");
        } else {
            lblPrecioTime.setText(costUpTime + "");
        }
    }

    private void setItems() {
        contenedor.clear();

        LabelStyle lblStyle = new LabelStyle(assetsHandler.fontChco, Color.WHITE);

        Table tbDentro = new Table();

        // 10 Bombas
        tbDentro.add(new Image(assetsHandler.bombaTienda)).size(35, 38);
        Label cantidad = new Label("x10", lblStyle);
        tbDentro.add(cantidad).width(80);
        tbDentro.add().width(30);
        tbDentro.add(new Image(assetsHandler.monedaTienda));
        Label precio = new Label(PRECIO_10_BOMBS + "", lblStyle);
        tbDentro.add(precio).width(80);
        tbDentro.add().width(30);
        tbDentro.add(btBuy10Bomb).size(85, 65);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // 20 Bombas
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.bombaTienda)).size(35, 38);
        cantidad = new Label("x20", lblStyle);
        tbDentro.add(cantidad).width(80);
        tbDentro.add().width(30);
        tbDentro.add(new Image(assetsHandler.monedaTienda));
        precio = new Label(PRECIO_20_BOMBS + "", lblStyle);
        tbDentro.add(precio).width(80);
        tbDentro.add().width(30);
        tbDentro.add(btBuy20Bomb).size(85, 65);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // 50 Bombas
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.bombaTienda)).size(35, 38);
        cantidad = new Label("x50", lblStyle);
        tbDentro.add(cantidad).width(80);
        tbDentro.add().width(30);
        tbDentro.add(new Image(assetsHandler.monedaTienda));
        precio = new Label(PRECIO_50_BOMBS + "", lblStyle);
        tbDentro.add(precio).width(80);
        tbDentro.add().width(30);
        tbDentro.add(btBuy50Bomb).size(85, 65);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // 100 Bombas
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.bombaTienda)).size(35, 38);
        cantidad = new Label("x100", lblStyle);
        tbDentro.add(cantidad).width(80);
        tbDentro.add().width(30);
        tbDentro.add(new Image(assetsHandler.monedaTienda));
        precio = new Label(PRECIO_100_BOMBS + "", lblStyle);
        tbDentro.add(precio).width(80);
        tbDentro.add().width(30);
        tbDentro.add(btBuy100Bomb).size(85, 65);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // 10 Madera
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.bananaSpikeTienda)).size(35, 38);
        cantidad = new Label("x10", lblStyle);
        tbDentro.add(cantidad).width(80);
        tbDentro.add().width(30);
        tbDentro.add(new Image(assetsHandler.monedaTienda));
        precio = new Label(PRECIO_10_WOOD + "", lblStyle);
        tbDentro.add(precio).width(80);
        tbDentro.add().width(30);
        tbDentro.add(btBuy10Wood).size(85, 65);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // 20 Madera
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.bananaSpikeTienda)).size(35, 38);
        cantidad = new Label("x20", lblStyle);
        tbDentro.add(cantidad).width(80);
        tbDentro.add().width(30);
        tbDentro.add(new Image(assetsHandler.monedaTienda));
        precio = new Label(PRECIO_20_WOOD + "", lblStyle);
        tbDentro.add(precio).width(80);
        tbDentro.add().width(30);
        tbDentro.add(btBuy20Wood).size(85, 65);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // 50 Madera
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.bananaSpikeTienda)).size(35, 38);
        cantidad = new Label("x50", lblStyle);
        tbDentro.add(cantidad).width(80);
        tbDentro.add().width(30);
        tbDentro.add(new Image(assetsHandler.monedaTienda));
        precio = new Label(PRECIO_50_WOOD + "", lblStyle);
        tbDentro.add(precio).width(80);
        tbDentro.add().width(30);
        tbDentro.add(btBuy50Wood).size(85, 65);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // 100 Bombas
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.bananaSpikeTienda)).size(35, 38);
        cantidad = new Label("x100", lblStyle);
        tbDentro.add(cantidad).width(80);
        tbDentro.add().width(30);
        tbDentro.add(new Image(assetsHandler.monedaTienda));
        precio = new Label(PRECIO_100_WOOD + "", lblStyle);
        tbDentro.add(precio).width(80);
        tbDentro.add().width(30);
        tbDentro.add(btBuy100Wood).size(85, 65);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);
    }

    private void setUpdrades() {
        contenedor.clear();
        LabelStyle lblStyle = new LabelStyle(assetsHandler.fontChco, Color.WHITE);

        // Upgrade Bombas
        Table tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.bombaTienda)).size(35, 38).padLeft(10)
                .padRight(10);
        Label descripcion = new Label("Bomb Efect Last Longer", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroUpBombas = new Table();
        dentroUpBombas.add(new Image(assetsHandler.monedaTienda));
        dentroUpBombas.add(lblPrecioUpBomb).left();
        dentroUpBombas.row().colspan(2);
        dentroUpBombas.add(btUpgradeBomb).size(120, 70);
        if (Settings.bombLevel < 5)
            tbDentro.add(dentroUpBombas);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // Upgrade Wood
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.bananaSpikeTienda)).size(35, 38)
                .padLeft(10).padRight(10);
        descripcion = new Label("Bananas and spikes effects last longer",
                lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroUpWood = new Table();
        dentroUpWood.add(new Image(assetsHandler.monedaTienda));
        dentroUpWood.add(lblPrecioUpWood).left();
        dentroUpWood.row().colspan(2);
        dentroUpWood.add(btUpgradeWood).size(120, 70);
        if (Settings.woodLevel < 5)
            tbDentro.add(dentroUpWood);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // Upgrade Chocolate
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.chocolateTienda)).size(35, 38)
                .padLeft(10).padRight(10);
        descripcion = new Label("Chocolate efect last longer", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroUpChocolate = new Table();
        dentroUpChocolate.add(new Image(assetsHandler.monedaTienda));
        dentroUpChocolate.add(lblPrecioUpChocolate).left();
        dentroUpChocolate.row().colspan(2);
        dentroUpChocolate.add(btUpgradeChocolate).size(120, 70);
        if (Settings.chocolateLevel < 5)
            tbDentro.add(dentroUpChocolate);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // Upgrade Ballon
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.globoTienda)).size(35, 45).padLeft(10)
                .padRight(10);
        descripcion = new Label("Ballons reward extra time", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroUpBallon = new Table();
        dentroUpBallon.add(new Image(assetsHandler.monedaTienda));
        dentroUpBallon.add(lblPrecioBallon).left();
        dentroUpBallon.row().colspan(2);
        dentroUpBallon.add(btUpgradeBallon).size(120, 70);
        if (Settings.balloonLevel < 5)
            tbDentro.add(dentroUpBallon);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // Upgrade Chili
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.chileTienda)).size(35, 38).padLeft(10)
                .padRight(10);
        descripcion = new Label("Chili pepper effect last longer", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroUpChili = new Table();
        dentroUpChili.add(new Image(assetsHandler.monedaTienda));
        dentroUpChili.add(lblPrecioUpChili).left();
        dentroUpChili.row().colspan(2);
        dentroUpChili.add(btUpgradeChili).size(120, 70);
        if (Settings.chiliLevel < 5)
            tbDentro.add(dentroUpChili);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // UPGRADE COIN
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.monedaTienda)).size(35, 38).padLeft(10)
                .padRight(10);
        descripcion = new Label("Bitcoins earn extra bits", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroUpCoin = new Table();
        dentroUpCoin.add(new Image(assetsHandler.monedaTienda));
        dentroUpCoin.add(lblPrecioCoin).left();
        dentroUpCoin.row().colspan(2);
        dentroUpCoin.add(btUpgradeCoin).size(120, 70);
        if (Settings.coinLevel < 5)
            tbDentro.add(dentroUpCoin);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // UPGRADE TIME
        tbDentro = new Table();
        tbDentro.add(new Image(assetsHandler.cronometroTienda)).size(35, 38)
                .padLeft(10).padRight(10);
        descripcion = new Label("Time left in stopwatch earn extra bits",
                lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroUpTime = new Table();
        dentroUpTime.add(new Image(assetsHandler.monedaTienda));
        dentroUpTime.add(lblPrecioTime).left();
        dentroUpTime.row().colspan(2);
        dentroUpTime.add(btUpgradeTime).size(120, 70);
        if (Settings.timeLevel < 5)
            tbDentro.add(dentroUpTime);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);
    }

    private void setCoins() {
        contenedor.clear();

        contenedor.clear();
        LabelStyle lblStyle = new LabelStyle(assetsHandler.fontChco, Color.WHITE);

        // Upgrade Bombas
        Table tbDentro = new Table();


        tbDentro.add(new Image(assetsHandler.btnFacebook)).size(40, 40)
                .padLeft(10).padRight(10);
        Label descripcion = new Label(
                "Like us on facebook and get 3500 bit coins", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        tbDentro.add(btLikeUsFacebook).size(120, 70);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub

    }

    private void unCheckOtherMenuButtons(TextButton checkThisButton) {
        items.setChecked(false);
        upgrades.setChecked(false);
        coins.setChecked(false);
        ponys.setChecked(false);
        itemsInt2.setChecked(false);
        checkThisButton.setChecked(true);
    }

    @Override
    public void draw(float delta) {

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.disableBlending();
        batch.begin();
        batch.draw(assetsHandler.fondoTienda, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        batch.enableBlending();
        batch.draw(assetsHandler.monedaTienda, 5, 440, 35, 35);
        assetsHandler.fontGde.draw(batch, Settings.numeroMonedasActual + "", 45,
                470);

        renderTitle(delta);

        batch.draw(assetsHandler.bombaTienda, 635, 405, 30, 30);
        assetsHandler.fontChco.draw(batch, Settings.numeroBombas + " lvl "
                + Settings.bombLevel + "/5", 660, 425);

        batch.draw(assetsHandler.bananaSpikeTienda, 635, 355, 30, 30);
        assetsHandler.fontChco.draw(batch, Settings.numeroWoods + " lvl "
                + Settings.woodLevel + "/5", 660, 375);

        batch.draw(assetsHandler.chocolateTienda, 635, 305, 30, 30);
        assetsHandler.fontChco.draw(batch, "lvl " + Settings.chocolateLevel + "/5",
                670, 325);
        // //
        batch.draw(assetsHandler.globoTienda, 635, 255, 30, 30);
        assetsHandler.fontChco.draw(batch, "lvl " + Settings.balloonLevel + "/5",
                670, 275);
        //
        batch.draw(assetsHandler.chileTienda, 635, 205, 30, 30);
        assetsHandler.fontChco.draw(batch, "lvl " + Settings.chiliLevel + "/5",
                670, 225);

        batch.draw(assetsHandler.monedaTienda, 635, 155, 30, 30);
        assetsHandler.fontChco.draw(batch, "lvl " + Settings.coinLevel + "/5", 670,
                175);

        batch.draw(assetsHandler.cronometroTienda, 635, 105, 30, 30);
        assetsHandler.fontChco.draw(batch, "lvl " + Settings.timeLevel + "/5", 670,
                125);

        batch.end();

        stage.act(delta);
        stage.draw();

    }

    private void renderTitle(float delta) {
        assetsHandler.animationTiendaTitle.apply(assetsHandler.skeletonTiendaTitle,
                screenLastStateTime, ScreenStateTime, true, null);
        assetsHandler.skeletonTiendaTitle.updateWorldTransform();
        assetsHandler.skeletonTiendaTitle.update(delta);
        skeletonRenderer.draw(batch, assetsHandler.skeletonTiendaTitle);
    }

    private int checkPriceBomb() {
        switch (Settings.bombLevel + 1) {
            case 1:
                return UPGRADE_PRICE_BOMBS_LEVEL1;
            case 2:
                return UPGRADE_PRICE_BOMBS_LEVEL2;

            case 3:
                return UPGRADE_PRICE_BOMBS_LEVEL3;

            case 4:
                return UPGRADE_PRICE_BOMBS_LEVEL4;
            default:
            case 5:
                return UPGRADE_PRICE_BOMBS_LEVEL5;
        }
    }

    private int checkPriceWood() {
        switch (Settings.woodLevel + 1) {
            case 1:
                return UPGRADE_PRICE_WOOD_LEVEL1;
            case 2:
                return UPGRADE_PRICE_WOOD_LEVEL2;

            case 3:
                return UPGRADE_PRICE_WOOD_LEVEL3;

            case 4:
                return UPGRADE_PRICE_WOOD_LEVEL4;
            default:
            case 5:
                return UPGRADE_PRICE_WOOD_LEVEL5;
        }
    }

    private int checkPriceChocolate() {
        switch (Settings.chocolateLevel + 1) {
            case 1:
                return UPGRADE_PRICE_CHOCOLATE_LEVEL1;
            case 2:
                return UPGRADE_PRICE_CHOCOLATE_LEVEL2;

            case 3:
                return UPGRADE_PRICE_CHOCOLATE_LEVEL3;

            case 4:
                return UPGRADE_PRICE_CHOCOLATE_LEVEL4;
            default:
            case 5:
                return UPGRADE_PRICE_CHOCOLATE_LEVEL5;
        }
    }

    private int checkPriceBallon() {
        switch (Settings.balloonLevel + 1) {
            case 1:
                return UPGRADE_PRICE_BALLON_LEVEL1;
            case 2:
                return UPGRADE_PRICE_BALLON_LEVEL2;

            case 3:
                return UPGRADE_PRICE_BALLON_LEVEL3;

            case 4:
                return UPGRADE_PRICE_BALLON_LEVEL4;
            default:
            case 5:
                return UPGRADE_PRICE_BALLON_LEVEL5;
        }
    }

    private int checkPriceChili() {
        switch (Settings.chiliLevel + 1) {
            case 1:
                return UPGRADE_PRICE_CHILI_LEVEL1;
            case 2:
                return UPGRADE_PRICE_CHILI_LEVEL2;

            case 3:
                return UPGRADE_PRICE_CHILI_LEVEL3;

            case 4:
                return UPGRADE_PRICE_CHILI_LEVEL4;
            default:
            case 5:
                return UPGRADE_PRICE_CHILI_LEVEL5;
        }
    }

    private int checkPriceCoin() {
        switch (Settings.coinLevel + 1) {
            case 1:
                return UPGRADE_PRICE_COIN_LEVEL1;
            case 2:
                return UPGRADE_PRICE_COIN_LEVEL2;

            case 3:
                return UPGRADE_PRICE_COIN_LEVEL3;

            case 4:
                return UPGRADE_PRICE_COIN_LEVEL4;
            default:
            case 5:
                return UPGRADE_PRICE_COIN_LEVEL5;
        }
    }

    private int checkPriceTime() {
        switch (Settings.timeLevel + 1) {
            case 1:
                return UPGRADE_PRICE_TIME_LEVEL1;
            case 2:
                return UPGRADE_PRICE_TIME_LEVEL2;

            case 3:
                return UPGRADE_PRICE_TIME_LEVEL3;

            case 4:
                return UPGRADE_PRICE_TIME_LEVEL4;
            default:
            case 5:
                return UPGRADE_PRICE_TIME_LEVEL5;
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
            ShopScreen.this.game.setScreen(new LoadingScreen(
                    ShopScreen.this.game, WorldMapTiledScreen.class));
            return true;
        }
        return false;
    }
}
