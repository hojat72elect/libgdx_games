package com.nopalsoft.dragracer.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.nopalsoft.dragracer.Assets;
import com.nopalsoft.dragracer.MainStreet;
import com.nopalsoft.dragracer.Settings;

public class PlayerSubMenu {

    // cars
    public static final int SKIN_DEVIL = 0;
    public static final int SKIN_BANSHEE = 1;
    public static final int SKIN_TURISMO = 3;
    public static final int SKIN_CAMARO = 6;
    public static final int SKIN_TORNADO = 2;
    public static final int SKIN_AUDI_S5 = 4;
    public static final int SKIN_BMW_X6 = 5;
    public static final int SKIN_CHEVROLET_CROSSFIRE = 7;
    public static final int SKIN_CITROEN_C4 = 8;
    public static final int SKIN_DODGE_CHARGER = 9;
    public static final int SKIN_FIAT_500_LOUNGE = 10;
    public static final int SKIN_HONDA_CRV = 11;
    public static final int SKIN_MAZDA_6 = 12;
    public static final int SKIN_MAZDA_RX8 = 13;
    public static final int SKIN_SEAT_IBIZA = 14;
    public static final int SKIN_VOLKSWAGEN_SCIROCCO = 15;

    // car prices
    final int PRICE_BANSHEE = 50;
    final int PRICE_CAMARO = 175;
    final int PRICE_TURISMO = 100;
    final int PRICE_TORNADO = 75;
    final int PRICE_AUDI_S5 = 125;
    final int PRICE_BMW_X6 = 150;
    final int PRICE_CHEVROLET_CROSSFIRE = 200;
    final int PRICE_CITROEN_C4 = 225;
    final int PRICE_DODGE_CHARGER = 250;
    final int PRICE_FIAT_500_LOUNGE = 275;
    final int PRICE_HONDA_CRV = 300;
    final int PRICE_MAZDA_6 = 325;
    final int PRICE_MAZDA_RX8 = 350;
    final int PRICE_SEAT_IBIZA = 375;
    final int PRICE_VOLKSWAGEN_SCIROCCO = 400;

    boolean didBuyBanshee, didBuyTornado, didBuyTurismo, didBuyAudiS5,
            didBuyBmwX6, didBuyCamaro, didBuyCrossfire, didBuyCitroenC4,
            didBuyDodgeCharger, didBuyFiat500, didBuyHondaCRV, didBuyMazda6,
            didBuyMazdaRX8, didBuySeatIbiza, didBuyVolkswagenScirocco;

    TextButton btBuyDiablo, btBuyBanshee, btBuyTornado, btBuyTurismo,
            btBuyAudiS5, btBuyBmwX6, btBuyBullet, btBuyCrossfire,
            btBuyCitroenC4, btBuyDodgeCharger, btBuyFiat500Lounge,
            btBuyHondaCRV, btBuyMazda6, btBuyMazdaRX8, btBuySeatIbiza,
            btBuyVolkswagenScirocco;
    Array<TextButton> arrayButtons;

    Table containerTable;
    MainStreet game;

    private final static Preferences pref = Gdx.app
            .getPreferences("com.tiar.dragrace.shop");

    public PlayerSubMenu(MainStreet game, Table containerTable) {
        this.game = game;
        this.containerTable = containerTable;
        loadPurchases();

        containerTable.clear();

        Label labelPriceBanshee = null;
        Label labelPriceTornado = null;
        Label labelPriceTurismo = null;
        Label labelPriceAudiS5 = null;
        Label labelPriceBmwX6 = null;
        Label labelPriceCamaro = null;
        Label labelPriceCrossfire = null;
        Label labelPriceCitroenC4 = null;
        Label labelPriceDodgeCharger = null;
        Label labelPriceFiat500Lounge = null;
        Label labelPriceHondaCRV = null;
        Label labelPriceMazda6 = null;
        Label labelPriceMazdaRX8 = null;
        Label labelPriceSeatIbiza = null;
        Label labelPriceVolkswagenScirocco = null;

        if (!didBuyBanshee)
            labelPriceBanshee = new Label(PRICE_BANSHEE + "",
                    Assets.labelStyleSmall);
        if (!didBuyTornado)
            labelPriceTornado = new Label(PRICE_TORNADO + "",
                    Assets.labelStyleSmall);

        if (!didBuyTurismo)
            labelPriceTurismo = new Label(PRICE_TURISMO + "",
                    Assets.labelStyleSmall);

        if (!didBuyAudiS5)
            labelPriceAudiS5 = new Label(PRICE_AUDI_S5 + "",
                    Assets.labelStyleSmall);

        if (!didBuyBmwX6)
            labelPriceBmwX6 = new Label(PRICE_BMW_X6 + "",
                    Assets.labelStyleSmall);

        if (!didBuyCamaro)
            labelPriceCamaro = new Label(PRICE_CAMARO + "",
                    Assets.labelStyleSmall);

        if (!didBuyCrossfire)
            labelPriceCrossfire = new Label(PRICE_CHEVROLET_CROSSFIRE
                    + "", Assets.labelStyleSmall);

        if (!didBuyCitroenC4)
            labelPriceCitroenC4 = new Label(PRICE_CITROEN_C4 + "",
                    Assets.labelStyleSmall);

        if (!didBuyDodgeCharger)
            labelPriceDodgeCharger = new Label(PRICE_DODGE_CHARGER + "",
                    Assets.labelStyleSmall);

        if (!didBuyFiat500)
            labelPriceFiat500Lounge = new Label(PRICE_FIAT_500_LOUNGE
                    + "", Assets.labelStyleSmall);

        if (!didBuyHondaCRV)
            labelPriceHondaCRV = new Label(PRICE_HONDA_CRV + "",
                    Assets.labelStyleSmall);

        if (!didBuyMazda6)
            labelPriceMazda6 = new Label(PRICE_MAZDA_6 + "",
                    Assets.labelStyleSmall);

        if (!didBuyMazdaRX8)
            labelPriceMazdaRX8 = new Label(PRICE_MAZDA_RX8 + "",
                    Assets.labelStyleSmall);

        if (!didBuySeatIbiza)
            labelPriceSeatIbiza = new Label(PRICE_SEAT_IBIZA + "",
                    Assets.labelStyleSmall);

        if (!didBuyVolkswagenScirocco)
            labelPriceVolkswagenScirocco = new Label(
                    PRICE_VOLKSWAGEN_SCIROCCO + "",
                    Assets.labelStyleSmall);

        initializeButtons();

        containerTable.add(new Image(Assets.horizontalSeparatorDrawable)).expandX().fill()
                .height(5);
        containerTable.row();

        containerTable
                .add(addPlayerTable(
                        "Diablo",
                        null,
                        Assets.carDiablo,
                        "Good car. It's not the fastest, but it's got great handling although maybe a little too twitchy for some.",
                        btBuyDiablo)).expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable(
                        "Banshee",
                        labelPriceBanshee,
                        Assets.carBanshee,
                        "Looks great and drives even better. Awesome acceleration and slight over-steer make this a thrilling ride.",
                        btBuyBanshee)).expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable(
                        "Tornado",
                        labelPriceTornado,
                        Assets.carTornado,
                        "Pretty speedy. Nothing too hot about this car, it looks ok and is ok to drive.",
                        btBuyTornado)).expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable(
                        "Turismo",
                        labelPriceTurismo,
                        Assets.carTurismo,
                        "If you can get this rare sport car, you'll be rewarded with a superbly fast drive. If you get it, take care of it.",
                        btBuyTurismo)).expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable("Ventura", labelPriceAudiS5,
                        Assets.carAudiS5, "No description", btBuyAudiS5))
                .expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable("XMW", labelPriceBmwX6, Assets.carBmwX6,
                        "No description", btBuyBmwX6)).expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable(
                        "Bullet",
                        labelPriceCamaro,
                        Assets.carCamaro,
                        "Probably the best sporty hatchback. It's quick and sticks to road really well. Acceleration is great too.",
                        btBuyBullet)).expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable("Crosstown", labelPriceCrossfire,
                        Assets.carChevroletCrossfire, "No description",
                        btBuyCrossfire)).expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable("Omega X", labelPriceCitroenC4,
                        Assets.carCitroenC4, "No description", btBuyCitroenC4))
                .expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable("Vulcano", labelPriceDodgeCharger,
                        Assets.carDodgeCharger, "No description",
                        btBuyDodgeCharger)).expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable("Fiesta", labelPriceFiat500Lounge,
                        Assets.carFiat500Lounge, "No description",
                        btBuyFiat500Lounge)).expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable("Comander", labelPriceHondaCRV,
                        Assets.carHondaCRV, "No description", btBuyHondaCRV))
                .expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable("Orion", labelPriceMazda6,
                        Assets.carMazda6, "No description", btBuyMazda6))
                .expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable("Colorado", labelPriceMazdaRX8,
                        Assets.carMazdaRx8, "No description", btBuyMazdaRX8))
                .expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable("Formosa", labelPriceSeatIbiza,
                        Assets.carSeatIbiza, "No description", btBuySeatIbiza))
                .expandX().fill();
        containerTable.row();

        containerTable
                .add(addPlayerTable("SHU", labelPriceVolkswagenScirocco,
                        Assets.carVolkswagenScirocco, "No description",
                        btBuyVolkswagenScirocco)).expandX().fill();
        containerTable.row();
    }

    private Table addPlayerTable(String title, Label priceLabel,
                                 AtlasRegion image, String description, TextButton button) {

        Image coinImage = new Image(Assets.coinFront);
        Image playerImage = new Image(image);

        if (priceLabel == null)
            coinImage.setVisible(false);

        Table titleBarTable = new Table();
        titleBarTable.add(new Label(title, Assets.labelStyleSmall)).expandX()
                .left();
        titleBarTable.add(coinImage).right();
        titleBarTable.add(priceLabel).right().padRight(10);

        Table tableContent = new Table();
        tableContent.add(titleBarTable).expandX().fill().colspan(2).padTop(8);
        tableContent.row();
        tableContent.add(playerImage).left().pad(10).size(40, 90);

        Label labelDescription = new Label(description, Assets.labelStyleSmall);
        labelDescription.setWrap(true);
        labelDescription.setFontScale(.85f);
        tableContent.add(labelDescription).expand().fill().padLeft(5);

        tableContent.row().colspan(2);
        tableContent.add(button).expandX().right().padRight(10).size(120, 45);
        tableContent.row().colspan(2);
        tableContent.add(new Image(Assets.horizontalSeparatorDrawable)).expandX().fill()
                .height(5).padTop(15);

        return tableContent;
    }

    private void initializeButtons() {
        arrayButtons = new Array<>();

        // DEFAULT
        btBuyDiablo = new TextButton("Select", Assets.styleTextButtonPurchased);
        if (Settings.selectedSkin == SKIN_DEVIL)
            btBuyDiablo.setVisible(false);

        addPressEffect(btBuyDiablo);
        btBuyDiablo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.selectedSkin = SKIN_DEVIL;
                setSelected(btBuyDiablo);
            }
        });

        if (didBuyBanshee)
            btBuyBanshee = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyBanshee = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_BANSHEE)
            btBuyBanshee.setVisible(false);

        addPressEffect(btBuyBanshee);
        btBuyBanshee.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyBanshee) {
                    Settings.selectedSkin = SKIN_BANSHEE;
                    setSelected(btBuyBanshee);
                } else if (Settings.coinsTotal >= PRICE_BANSHEE) {
                    Settings.coinsTotal -= PRICE_BANSHEE;
                    setButtonStylePurchased(btBuyBanshee);
                    didBuyBanshee = true;
                }
                savePurchases();
            }
        });

        if (didBuyTornado)
            btBuyTornado = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyTornado = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_TORNADO)
            btBuyTornado.setVisible(false);

        addPressEffect(btBuyTornado);
        btBuyTornado.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyTornado) {
                    Settings.selectedSkin = SKIN_TORNADO;
                    setSelected(btBuyTornado);
                } else if (Settings.coinsTotal >= PRICE_TORNADO) {
                    Settings.coinsTotal -= PRICE_TORNADO;
                    setButtonStylePurchased(btBuyTornado);
                    didBuyTornado = true;
                }
                savePurchases();
            }
        });

        if (didBuyTurismo)
            btBuyTurismo = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyTurismo = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_TURISMO)
            btBuyTurismo.setVisible(false);

        addPressEffect(btBuyTurismo);
        btBuyTurismo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyTurismo) {
                    Settings.selectedSkin = SKIN_TURISMO;
                    setSelected(btBuyTurismo);
                } else if (Settings.coinsTotal >= PRICE_TURISMO) {
                    Settings.coinsTotal -= PRICE_TURISMO;
                    setButtonStylePurchased(btBuyTurismo);
                    didBuyTurismo = true;
                }
                savePurchases();
            }
        });

        if (didBuyAudiS5)
            btBuyAudiS5 = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyAudiS5 = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_AUDI_S5)
            btBuyAudiS5.setVisible(false);

        addPressEffect(btBuyAudiS5);
        btBuyAudiS5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyAudiS5) {
                    Settings.selectedSkin = SKIN_AUDI_S5;
                    setSelected(btBuyAudiS5);
                } else if (Settings.coinsTotal >= PRICE_AUDI_S5) {
                    Settings.coinsTotal -= PRICE_AUDI_S5;
                    setButtonStylePurchased(btBuyAudiS5);
                    didBuyAudiS5 = true;
                }
                savePurchases();
            }
        });

        if (didBuyBmwX6)
            btBuyBmwX6 = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyBmwX6 = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_BMW_X6)
            btBuyBmwX6.setVisible(false);

        addPressEffect(btBuyBmwX6);
        btBuyBmwX6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyBmwX6) {
                    Settings.selectedSkin = SKIN_BMW_X6;
                    setSelected(btBuyBmwX6);
                } else if (Settings.coinsTotal >= PRICE_BMW_X6) {
                    Settings.coinsTotal -= PRICE_BMW_X6;
                    setButtonStylePurchased(btBuyBmwX6);
                    didBuyBmwX6 = true;
                }
                savePurchases();
            }
        });

        if (didBuyCamaro)
            btBuyBullet = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyBullet = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_CAMARO)
            btBuyBullet.setVisible(false);

        addPressEffect(btBuyBullet);
        btBuyBullet.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyCamaro) {
                    Settings.selectedSkin = SKIN_CAMARO;
                    setSelected(btBuyBullet);
                } else if (Settings.coinsTotal >= PRICE_CAMARO) {
                    Settings.coinsTotal -= PRICE_CAMARO;
                    setButtonStylePurchased(btBuyBullet);
                    didBuyCamaro = true;
                }
                savePurchases();
            }
        });

        if (didBuyCrossfire)
            btBuyCrossfire = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyCrossfire = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_CHEVROLET_CROSSFIRE)
            btBuyCrossfire.setVisible(false);

        addPressEffect(btBuyCrossfire);
        btBuyCrossfire.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyCrossfire) {
                    Settings.selectedSkin = SKIN_CHEVROLET_CROSSFIRE;
                    setSelected(btBuyCrossfire);
                } else if (Settings.coinsTotal >= PRICE_CHEVROLET_CROSSFIRE) {
                    Settings.coinsTotal -= PRICE_CHEVROLET_CROSSFIRE;
                    setButtonStylePurchased(btBuyCrossfire);
                    didBuyCrossfire = true;
                }
                savePurchases();
            }
        });

        if (didBuyCitroenC4)
            btBuyCitroenC4 = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyCitroenC4 = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_CITROEN_C4)
            btBuyCitroenC4.setVisible(false);

        addPressEffect(btBuyCitroenC4);
        btBuyCitroenC4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyCitroenC4) {
                    Settings.selectedSkin = SKIN_CITROEN_C4;
                    setSelected(btBuyCitroenC4);
                } else if (Settings.coinsTotal >= PRICE_CITROEN_C4) {
                    Settings.coinsTotal -= PRICE_CITROEN_C4;
                    setButtonStylePurchased(btBuyCitroenC4);
                    didBuyCitroenC4 = true;
                }
                savePurchases();
            }
        });

        if (didBuyDodgeCharger)
            btBuyDodgeCharger = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyDodgeCharger = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_DODGE_CHARGER)
            btBuyDodgeCharger.setVisible(false);

        addPressEffect(btBuyDodgeCharger);
        btBuyDodgeCharger.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyDodgeCharger) {
                    Settings.selectedSkin = SKIN_DODGE_CHARGER;
                    setSelected(btBuyDodgeCharger);
                } else if (Settings.coinsTotal >= PRICE_DODGE_CHARGER) {
                    Settings.coinsTotal -= PRICE_DODGE_CHARGER;
                    setButtonStylePurchased(btBuyDodgeCharger);
                    didBuyDodgeCharger = true;
                }
                savePurchases();
            }
        });

        if (didBuyFiat500)
            btBuyFiat500Lounge = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyFiat500Lounge = new TextButton("Buy",
                    Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_FIAT_500_LOUNGE)
            btBuyFiat500Lounge.setVisible(false);

        addPressEffect(btBuyFiat500Lounge);
        btBuyFiat500Lounge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyFiat500) {
                    Settings.selectedSkin = SKIN_FIAT_500_LOUNGE;
                    setSelected(btBuyFiat500Lounge);
                } else if (Settings.coinsTotal >= PRICE_FIAT_500_LOUNGE) {
                    Settings.coinsTotal -= PRICE_FIAT_500_LOUNGE;
                    setButtonStylePurchased(btBuyFiat500Lounge);
                    didBuyFiat500 = true;
                }
                savePurchases();
            }
        });

        if (didBuyHondaCRV)
            btBuyHondaCRV = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyHondaCRV = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_HONDA_CRV)
            btBuyHondaCRV.setVisible(false);

        addPressEffect(btBuyHondaCRV);
        btBuyHondaCRV.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyHondaCRV) {
                    Settings.selectedSkin = SKIN_HONDA_CRV;
                    setSelected(btBuyHondaCRV);
                } else if (Settings.coinsTotal >= PRICE_HONDA_CRV) {
                    Settings.coinsTotal -= PRICE_HONDA_CRV;
                    setButtonStylePurchased(btBuyHondaCRV);
                    didBuyHondaCRV = true;
                }
                savePurchases();
            }
        });

        if (didBuyMazda6)
            btBuyMazda6 = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyMazda6 = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_MAZDA_6)
            btBuyMazda6.setVisible(false);

        addPressEffect(btBuyMazda6);
        btBuyMazda6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyMazda6) {
                    Settings.selectedSkin = SKIN_MAZDA_6;
                    setSelected(btBuyMazda6);
                } else if (Settings.coinsTotal >= PRICE_MAZDA_6) {
                    Settings.coinsTotal -= PRICE_MAZDA_6;
                    setButtonStylePurchased(btBuyMazda6);
                    didBuyMazda6 = true;
                }
                savePurchases();
            }
        });

        if (didBuyMazdaRX8)
            btBuyMazdaRX8 = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyMazdaRX8 = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_MAZDA_RX8)
            btBuyMazdaRX8.setVisible(false);

        addPressEffect(btBuyMazdaRX8);
        btBuyMazdaRX8.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyMazdaRX8) {
                    Settings.selectedSkin = SKIN_MAZDA_RX8;
                    setSelected(btBuyMazdaRX8);
                } else if (Settings.coinsTotal >= PRICE_MAZDA_RX8) {
                    Settings.coinsTotal -= PRICE_MAZDA_RX8;
                    setButtonStylePurchased(btBuyMazdaRX8);
                    didBuyMazdaRX8 = true;
                }
                savePurchases();
            }
        });

        if (didBuySeatIbiza)
            btBuySeatIbiza = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuySeatIbiza = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_SEAT_IBIZA)
            btBuySeatIbiza.setVisible(false);

        addPressEffect(btBuySeatIbiza);
        btBuySeatIbiza.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuySeatIbiza) {
                    Settings.selectedSkin = SKIN_SEAT_IBIZA;
                    setSelected(btBuySeatIbiza);
                } else if (Settings.coinsTotal >= PRICE_SEAT_IBIZA) {
                    Settings.coinsTotal -= PRICE_SEAT_IBIZA;
                    setButtonStylePurchased(btBuySeatIbiza);
                    didBuySeatIbiza = true;
                }
                savePurchases();
            }
        });

        if (didBuyVolkswagenScirocco)
            btBuyVolkswagenScirocco = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyVolkswagenScirocco = new TextButton("Buy",
                    Assets.styleTextButtonBuy);

        if (Settings.selectedSkin == SKIN_VOLKSWAGEN_SCIROCCO)
            btBuyVolkswagenScirocco.setVisible(false);

        addPressEffect(btBuyVolkswagenScirocco);
        btBuyVolkswagenScirocco.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyVolkswagenScirocco) {
                    Settings.selectedSkin = SKIN_VOLKSWAGEN_SCIROCCO;
                    setSelected(btBuyVolkswagenScirocco);
                } else if (Settings.coinsTotal >= PRICE_VOLKSWAGEN_SCIROCCO) {
                    Settings.coinsTotal -= PRICE_VOLKSWAGEN_SCIROCCO;
                    setButtonStylePurchased(btBuyVolkswagenScirocco);
                    didBuyVolkswagenScirocco = true;
                }
                savePurchases();
            }
        });

        arrayButtons.add(btBuyDiablo);
        arrayButtons.add(btBuyBanshee);
        arrayButtons.add(btBuyTornado);
        arrayButtons.add(btBuyTurismo);
        arrayButtons.add(btBuyAudiS5);
        arrayButtons.add(btBuyBmwX6);
        arrayButtons.add(btBuyBullet);
        arrayButtons.add(btBuyCrossfire);
        arrayButtons.add(btBuyCitroenC4);
        arrayButtons.add(btBuyDodgeCharger);
        arrayButtons.add(btBuyFiat500Lounge);
        arrayButtons.add(btBuyHondaCRV);
        arrayButtons.add(btBuyMazda6);
        arrayButtons.add(btBuyMazdaRX8);
        arrayButtons.add(btBuySeatIbiza);
        arrayButtons.add(btBuyVolkswagenScirocco);
    }

    private void loadPurchases() {
        didBuyBanshee = pref.getBoolean("didBuyBanshee", false);
        didBuyTornado = pref.getBoolean("didBuyTornado", false);
        didBuyTurismo = pref.getBoolean("didBuyTurismo", false);
        didBuyAudiS5 = pref.getBoolean("didBuyAudiS5", false);
        didBuyBmwX6 = pref.getBoolean("didBuyBmwX6", false);
        didBuyCamaro = pref.getBoolean("didBuyBullet", false);
        didBuyCrossfire = pref.getBoolean("didBuyCrossfire", false);
        didBuyCitroenC4 = pref.getBoolean("didBuyCitroenC4", false);
        didBuyDodgeCharger = pref.getBoolean("didBuyDodgeCharger", false);
        didBuyFiat500 = pref.getBoolean("didBuyFiat500", false);
        didBuyHondaCRV = pref.getBoolean("didBuyHondaCRV", false);
        didBuyMazda6 = pref.getBoolean("didBuyMazda6", false);
        didBuyMazdaRX8 = pref.getBoolean("didBuyMazdaRX8", false);
        didBuySeatIbiza = pref.getBoolean("didBuySeatIbiza", false);
        didBuyVolkswagenScirocco = pref.getBoolean("didBuyVolkswagenScirocco",
                false);
    }

    private void savePurchases() {
        pref.putBoolean("didBuyBanshee", didBuyBanshee);
        pref.putBoolean("didBuyTornado", didBuyTornado);
        pref.putBoolean("didBuyTurismo", didBuyTurismo);
        pref.putBoolean("didBuyAudiS5", didBuyAudiS5);
        pref.putBoolean("didBuyBmwX6", didBuyBmwX6);
        pref.putBoolean("didBuyBullet", didBuyCamaro);
        pref.putBoolean("didBuyCrossfire", didBuyCrossfire);
        pref.putBoolean("didBuyCitroenC4", didBuyCitroenC4);
        pref.putBoolean("didBuyDodgeCharger", didBuyDodgeCharger);
        pref.putBoolean("didBuyFiat500", didBuyFiat500);
        pref.putBoolean("didBuyHondaCRV", didBuyHondaCRV);
        pref.putBoolean("didBuyMazda6", didBuyMazda6);
        pref.putBoolean("didBuyMazdaRX8", didBuyMazdaRX8);
        pref.putBoolean("didBuySeatIbiza", didBuySeatIbiza);
        pref.putBoolean("didBuyVolkswagenScirocco", didBuyVolkswagenScirocco);
        pref.flush();
        Settings.save();
    }

    private void setButtonStylePurchased(TextButton button) {
        button.setStyle(Assets.styleTextButtonPurchased);
        button.setText("Select");
    }

    private void setSelected(TextButton selectedButton) {
        // I make all visible and at the end the selected button invisible
        for (TextButton button : arrayButtons) {
            button.setVisible(true);
        }
        selectedButton.setVisible(false);
    }

    protected void addPressEffect(final Actor actor) {
        actor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() - 3);
                event.stop();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() + 3);
            }
        });
    }
}
