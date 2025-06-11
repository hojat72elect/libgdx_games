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

import java.util.Iterator;

public class PersonajesSubMenu {

    public static final int SKIN_CARRO_DIABLO = 0;
    public static final int SKIN_CARRO_BANSHEE = 1;
    public static final int SKIN_CARRO_TURISMO = 3;
    public static final int SKIN_CARRO_BULLET = 6;
    public static final int SKIN_CARRO_TORNADO = 2;

    public static final int SKIN_CARRO_AUDI_S5 = 4;
    public static final int SKIN_CARRO_BMW_X6 = 5;
    public static final int SKIN_CARRO_CHEVRLOTE_CROSSFIRE = 7;
    public static final int SKIN_CARRO_CITROEN_C4 = 8;
    public static final int SKIN_CARRO_DODGE_CHARGER = 9;
    public static final int SKIN_CARRO_FIAT_500_LOUNGE = 10;
    public static final int SKIN_CARRO_HONDA_CRV = 11;
    public static final int SKIN_CARRO_MAZDA_6 = 12;
    public static final int SKIN_CARRO_MAZDA_RX8 = 13;
    public static final int SKIN_CARRO_SEAT_IBIZA = 14;
    public static final int SKIN_CARRO_VOLKSWAGEN_SCIROCCO = 15;

    final int PRECIO_BANSHEE = 50;
    final int PRECIO_BULLET = 175;
    final int PRECIO_TURISMO = 100;
    final int PRECIO_TORNADO = 75;

    final int PRECIO_CARRO_AUDI_S5 = 125;
    final int PRECIO_CARRO_BMW_X6 = 150;
    final int PRECIO_CARRO_CHEVRLOTE_CROSSFIRE = 200;
    final int PRECIO_CARRO_CITROEN_C4 = 225;
    final int PRECIO_CARRO_DODGE_CHARGER = 250;
    final int PRECIO_CARRO_FIAT_500_LOUNGE = 275;
    final int PRECIO_CARRO_HONDA_CRV = 300;
    final int PRECIO_CARRO_MAZDA_6 = 325;
    final int PRECIO_CARRO_MAZDA_RX8 = 350;
    final int PRECIO_CARRO_SEAT_IBIZA = 375;
    final int PRECIO_CARRO_VOLKSWAGEN_SCIROCCO = 400;

    boolean didBuyBanshee, didBuyTornado, didBuyTurismo, didBuyAudiS5,
            didBuyBmwX6, didBuyBullet, didBuyCrossfire, didBuyCitroenC4,
            didBuyDodgeCharger, didBuyFiat500, didBuyHondaCRV, didBuyMazda6,
            didBuyMazdaRX8, didBuySeatIbiza, didBuyVolkswagenScirocco;

    TextButton btBuyDiablo, btBuyBanshee, btBuyTornado, btBuyTurismo,
            btBuyAudiS5, btBuyBmwX6, btBuyBullet, btBuyCrossfire,
            btBuyCitroenC4, btBuyDodgeCharger, btBuyFiat500Lounge,
            btBuyHondaCRV, btBuyMazda6, btBuyMazdaRX8, btBuySeatIbiza,
            btBuyVolkswagenScirocco;
    Array<TextButton> arrBotones;

    Table contenedor;
    MainStreet game;

    private final static Preferences pref = Gdx.app
            .getPreferences("com.tiar.dragrace.shop");

    public PersonajesSubMenu(MainStreet game, Table contenedor) {
        this.game = game;
        this.contenedor = contenedor;
        loadPurchases();

        contenedor.clear();

        Label lblPrecioBanshee = null;
        Label lblPrecioTornado = null;
        Label lblPrecioTurismo = null;
        Label lblPrecioAudiS5 = null;
        Label lblPrecioBmwX6 = null;
        Label lblPrecioCamaro = null;
        Label lblPrecioCrossfire = null;
        Label lblPrecioCitroenC4 = null;
        Label lblPrecioDodgeCharger = null;
        Label lblPrecioFiat500Lounge = null;
        Label lblPrecioHondeCRV = null;
        Label lblPrecioMazda6 = null;
        Label lblPrecioMazdaRX8 = null;
        Label lblPrecioSeatIbiza = null;
        Label lblPrecioVOlkswagenScirocco = null;

        if (!didBuyBanshee)
            lblPrecioBanshee = new Label(PRECIO_BANSHEE + "",
                    Assets.labelStyleChico);
        if (!didBuyTornado)
            lblPrecioTornado = new Label(PRECIO_TORNADO + "",
                    Assets.labelStyleChico);

        if (!didBuyTurismo)
            lblPrecioTurismo = new Label(PRECIO_TURISMO + "",
                    Assets.labelStyleChico);

        if (!didBuyAudiS5)
            lblPrecioAudiS5 = new Label(PRECIO_CARRO_AUDI_S5 + "",
                    Assets.labelStyleChico);

        if (!didBuyBmwX6)
            lblPrecioBmwX6 = new Label(PRECIO_CARRO_BMW_X6 + "",
                    Assets.labelStyleChico);

        if (!didBuyBullet)
            lblPrecioCamaro = new Label(PRECIO_BULLET + "",
                    Assets.labelStyleChico);

        if (!didBuyCrossfire)
            lblPrecioCrossfire = new Label(PRECIO_CARRO_CHEVRLOTE_CROSSFIRE
                    + "", Assets.labelStyleChico);

        if (!didBuyCitroenC4)
            lblPrecioCitroenC4 = new Label(PRECIO_CARRO_CITROEN_C4 + "",
                    Assets.labelStyleChico);

        if (!didBuyDodgeCharger)
            lblPrecioDodgeCharger = new Label(PRECIO_CARRO_DODGE_CHARGER + "",
                    Assets.labelStyleChico);

        if (!didBuyFiat500)
            lblPrecioFiat500Lounge = new Label(PRECIO_CARRO_FIAT_500_LOUNGE
                    + "", Assets.labelStyleChico);

        if (!didBuyHondaCRV)
            lblPrecioHondeCRV = new Label(PRECIO_CARRO_HONDA_CRV + "",
                    Assets.labelStyleChico);

        if (!didBuyMazda6)
            lblPrecioMazda6 = new Label(PRECIO_CARRO_MAZDA_6 + "",
                    Assets.labelStyleChico);

        if (!didBuyMazdaRX8)
            lblPrecioMazdaRX8 = new Label(PRECIO_CARRO_MAZDA_RX8 + "",
                    Assets.labelStyleChico);

        if (!didBuySeatIbiza)
            lblPrecioSeatIbiza = new Label(PRECIO_CARRO_SEAT_IBIZA + "",
                    Assets.labelStyleChico);

        if (!didBuyVolkswagenScirocco)
            lblPrecioVOlkswagenScirocco = new Label(
                    PRECIO_CARRO_VOLKSWAGEN_SCIROCCO + "",
                    Assets.labelStyleChico);

        inicializarBotones();

        contenedor.add(new Image(Assets.separadorHorizontal)).expandX().fill()
                .height(5);
        contenedor.row();

        // Usar Default
        contenedor
                .add(agregarPersonajeTabla(
                        "Diablo",
                        null,
                        Assets.carroDiablo,
                        "Good car. It's not the fastest, but it's got great handling although maybe a little too twitchy for some.",
                        btBuyDiablo)).expandX().fill();
        contenedor.row();

        // SKIN_CARRO_BANSHEE
        contenedor
                .add(agregarPersonajeTabla(
                        "Banshee",
                        lblPrecioBanshee,
                        Assets.carroBanshee,
                        "Looks great and drives even better. Awesome acceleration and slight oversteer make this a thrilling ride.",
                        btBuyBanshee)).expandX().fill();
        contenedor.row();

        // SKIN_CARRO_TORNADO
        contenedor
                .add(agregarPersonajeTabla(
                        "Tornado",
                        lblPrecioTornado,
                        Assets.carroTornado,
                        "Pretty speedy. Nothing too hot about this car, it looks ok and is ok to drive.",
                        btBuyTornado)).expandX().fill();
        contenedor.row();

        // SKIN_CARRO_TURISMO
        contenedor
                .add(agregarPersonajeTabla(
                        "Turismo",
                        lblPrecioTurismo,
                        Assets.carroTurismo,
                        "If you can get this rare sport car, you'll be rewarded with a superbly fast drive. If you get it, take care of it.",
                        btBuyTurismo)).expandX().fill();
        contenedor.row();

        // SKIN_CARRO_AUDI_S5
        contenedor
                .add(agregarPersonajeTabla("Ventura", lblPrecioAudiS5,
                        Assets.audiS5, "No description", btBuyAudiS5))
                .expandX().fill();
        contenedor.row();

        // SKIN_CARRO_BMW_X6
        contenedor
                .add(agregarPersonajeTabla("XMW", lblPrecioBmwX6, Assets.bmwX6,
                        "No description", btBuyBmwX6)).expandX().fill();
        contenedor.row();

        // PRECIO_BULLET
        contenedor
                .add(agregarPersonajeTabla(
                        "Bullet",
                        lblPrecioCamaro,
                        Assets.carroBullet,
                        "Probably the best sporty hatchback. It's quick and sticks to road really well. Acceleration is great too.",
                        btBuyBullet)).expandX().fill();
        contenedor.row();

        // SKIN_CARRO_CHEVRLOTE_CROSSFIRE
        contenedor
                .add(agregarPersonajeTabla("Crosstown", lblPrecioCrossfire,
                        Assets.chevroletCrossfire, "No description",
                        btBuyCrossfire)).expandX().fill();
        contenedor.row();

        // SKIN_CARRO_CITROEN_C4
        contenedor
                .add(agregarPersonajeTabla("Omega X", lblPrecioCitroenC4,
                        Assets.citroenC4, "No description", btBuyCitroenC4))
                .expandX().fill();
        contenedor.row();

        // SKIN_CARRO_DODGE_CHARGER
        contenedor
                .add(agregarPersonajeTabla("Vulcano", lblPrecioDodgeCharger,
                        Assets.dodgeCharger, "No description",
                        btBuyDodgeCharger)).expandX().fill();
        contenedor.row();

        // SKIN_CARRO_FIAT_500_LOUNGE
        contenedor
                .add(agregarPersonajeTabla("Fiesta", lblPrecioFiat500Lounge,
                        Assets.fiat500Lounge, "No description",
                        btBuyFiat500Lounge)).expandX().fill();
        contenedor.row();

        // SKIN_CARRO_HONDA_CRV
        contenedor
                .add(agregarPersonajeTabla("Comander", lblPrecioHondeCRV,
                        Assets.hondaCRV, "No description", btBuyHondaCRV))
                .expandX().fill();
        contenedor.row();

        // SKIN_CARRO_MAZDA_6
        contenedor
                .add(agregarPersonajeTabla("Orion", lblPrecioMazda6,
                        Assets.mazda6, "No description", btBuyMazda6))
                .expandX().fill();
        contenedor.row();

        // SKIN_CARRO_MAZDA_RX8
        contenedor
                .add(agregarPersonajeTabla("Colorado", lblPrecioMazdaRX8,
                        Assets.mazdaRX8, "No description", btBuyMazdaRX8))
                .expandX().fill();
        contenedor.row();

        // SKIN_CARRO_SEAT_IBIZA
        contenedor
                .add(agregarPersonajeTabla("Formosa", lblPrecioSeatIbiza,
                        Assets.seatIbiza, "No description", btBuySeatIbiza))
                .expandX().fill();
        contenedor.row();

        // SKIN_CARRO_VOLKSWAGEN_SCIROCCO
        contenedor
                .add(agregarPersonajeTabla("SHU", lblPrecioVOlkswagenScirocco,
                        Assets.volkswagenScirocco, "No description",
                        btBuyVolkswagenScirocco)).expandX().fill();
        contenedor.row();
    }

    private Table agregarPersonajeTabla(String titulo, Label lblPrecio,
                                        AtlasRegion imagen, String descripcion, TextButton boton) {

        Image moneda = new Image(Assets.coinFrente);
        Image imgPersonaje = new Image(imagen);

        if (lblPrecio == null)
            moneda.setVisible(false);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo.add(new Label(titulo, Assets.labelStyleChico)).expandX()
                .left();
        tbBarraTitulo.add(moneda).right();
        tbBarraTitulo.add(lblPrecio).right().padRight(10);

        Table tbContent = new Table();
        // tbContent.debug();
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(8);
        tbContent.row();
        tbContent.add(imgPersonaje).left().pad(10).size(40, 90);

        Label lblDescripcion = new Label(descripcion, Assets.labelStyleChico);
        lblDescripcion.setWrap(true);
        lblDescripcion.setFontScale(.85f);
        tbContent.add(lblDescripcion).expand().fill().padLeft(5);

        tbContent.row().colspan(2);
        tbContent.add(boton).expandX().right().padRight(10).size(120, 45);
        tbContent.row().colspan(2);
        tbContent.add(new Image(Assets.separadorHorizontal)).expandX().fill()
                .height(5).padTop(15);

        return tbContent;
    }

    private void inicializarBotones() {
        arrBotones = new Array<TextButton>();

        // DEFAULT
        btBuyDiablo = new TextButton("Select", Assets.styleTextButtonPurchased);
        if (Settings.skinSeleccionada == SKIN_CARRO_DIABLO)
            btBuyDiablo.setVisible(false);

        addEfectoPress(btBuyDiablo);
        btBuyDiablo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.skinSeleccionada = SKIN_CARRO_DIABLO;
                setSelected(btBuyDiablo);
            }
        });

        // SKIN_CARRO_BANSHEE
        if (didBuyBanshee)
            btBuyBanshee = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyBanshee = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_BANSHEE)
            btBuyBanshee.setVisible(false);

        addEfectoPress(btBuyBanshee);
        btBuyBanshee.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyBanshee) {
                    Settings.skinSeleccionada = SKIN_CARRO_BANSHEE;
                    setSelected(btBuyBanshee);
                } else if (Settings.coinsTotal >= PRECIO_BANSHEE) {
                    Settings.coinsTotal -= PRECIO_BANSHEE;
                    setButtonStylePurchased(btBuyBanshee);
                    didBuyBanshee = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_TORNADO
        if (didBuyTornado)
            btBuyTornado = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyTornado = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_TORNADO)
            btBuyTornado.setVisible(false);

        addEfectoPress(btBuyTornado);
        btBuyTornado.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyTornado) {
                    Settings.skinSeleccionada = SKIN_CARRO_TORNADO;
                    setSelected(btBuyTornado);
                } else if (Settings.coinsTotal >= PRECIO_TORNADO) {
                    Settings.coinsTotal -= PRECIO_TORNADO;
                    setButtonStylePurchased(btBuyTornado);
                    didBuyTornado = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_TURISMO
        if (didBuyTurismo)
            btBuyTurismo = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyTurismo = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_TURISMO)
            btBuyTurismo.setVisible(false);

        addEfectoPress(btBuyTurismo);
        btBuyTurismo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyTurismo) {
                    Settings.skinSeleccionada = SKIN_CARRO_TURISMO;
                    setSelected(btBuyTurismo);
                } else if (Settings.coinsTotal >= PRECIO_TURISMO) {
                    Settings.coinsTotal -= PRECIO_TURISMO;
                    setButtonStylePurchased(btBuyTurismo);
                    didBuyTurismo = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_AUDI_S5
        if (didBuyAudiS5)
            btBuyAudiS5 = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyAudiS5 = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_AUDI_S5)
            btBuyAudiS5.setVisible(false);

        addEfectoPress(btBuyAudiS5);
        btBuyAudiS5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyAudiS5) {
                    Settings.skinSeleccionada = SKIN_CARRO_AUDI_S5;
                    setSelected(btBuyAudiS5);
                } else if (Settings.coinsTotal >= PRECIO_CARRO_AUDI_S5) {
                    Settings.coinsTotal -= PRECIO_CARRO_AUDI_S5;
                    setButtonStylePurchased(btBuyAudiS5);
                    didBuyAudiS5 = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_BMW_X6
        if (didBuyBmwX6)
            btBuyBmwX6 = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyBmwX6 = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_BMW_X6)
            btBuyBmwX6.setVisible(false);

        addEfectoPress(btBuyBmwX6);
        btBuyBmwX6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyBmwX6) {
                    Settings.skinSeleccionada = SKIN_CARRO_BMW_X6;
                    setSelected(btBuyBmwX6);
                } else if (Settings.coinsTotal >= PRECIO_CARRO_BMW_X6) {
                    Settings.coinsTotal -= PRECIO_CARRO_BMW_X6;
                    setButtonStylePurchased(btBuyBmwX6);
                    didBuyBmwX6 = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_BULLET
        if (didBuyBullet)
            btBuyBullet = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyBullet = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_BULLET)
            btBuyBullet.setVisible(false);

        addEfectoPress(btBuyBullet);
        btBuyBullet.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyBullet) {
                    Settings.skinSeleccionada = SKIN_CARRO_BULLET;
                    setSelected(btBuyBullet);
                } else if (Settings.coinsTotal >= PRECIO_BULLET) {
                    Settings.coinsTotal -= PRECIO_BULLET;
                    setButtonStylePurchased(btBuyBullet);
                    didBuyBullet = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_CHEVRLOTE_CROSSFIRE
        if (didBuyCrossfire)
            btBuyCrossfire = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyCrossfire = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_CHEVRLOTE_CROSSFIRE)
            btBuyCrossfire.setVisible(false);

        addEfectoPress(btBuyCrossfire);
        btBuyCrossfire.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyCrossfire) {
                    Settings.skinSeleccionada = SKIN_CARRO_CHEVRLOTE_CROSSFIRE;
                    setSelected(btBuyCrossfire);
                } else if (Settings.coinsTotal >= PRECIO_CARRO_CHEVRLOTE_CROSSFIRE) {
                    Settings.coinsTotal -= PRECIO_CARRO_CHEVRLOTE_CROSSFIRE;
                    setButtonStylePurchased(btBuyCrossfire);
                    didBuyCrossfire = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_CITROEN_C4
        if (didBuyCitroenC4)
            btBuyCitroenC4 = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyCitroenC4 = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_CITROEN_C4)
            btBuyCitroenC4.setVisible(false);

        addEfectoPress(btBuyCitroenC4);
        btBuyCitroenC4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyCitroenC4) {
                    Settings.skinSeleccionada = SKIN_CARRO_CITROEN_C4;
                    setSelected(btBuyCitroenC4);
                } else if (Settings.coinsTotal >= PRECIO_CARRO_CITROEN_C4) {
                    Settings.coinsTotal -= PRECIO_CARRO_CITROEN_C4;
                    setButtonStylePurchased(btBuyCitroenC4);
                    didBuyCitroenC4 = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_DODGE_CHARGER
        if (didBuyDodgeCharger)
            btBuyDodgeCharger = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyDodgeCharger = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_DODGE_CHARGER)
            btBuyDodgeCharger.setVisible(false);

        addEfectoPress(btBuyDodgeCharger);
        btBuyDodgeCharger.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyDodgeCharger) {
                    Settings.skinSeleccionada = SKIN_CARRO_DODGE_CHARGER;
                    setSelected(btBuyDodgeCharger);
                } else if (Settings.coinsTotal >= PRECIO_CARRO_DODGE_CHARGER) {
                    Settings.coinsTotal -= PRECIO_CARRO_DODGE_CHARGER;
                    setButtonStylePurchased(btBuyDodgeCharger);
                    didBuyDodgeCharger = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_FIAT_500_LOUNGE
        if (didBuyFiat500)
            btBuyFiat500Lounge = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyFiat500Lounge = new TextButton("Buy",
                    Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_FIAT_500_LOUNGE)
            btBuyFiat500Lounge.setVisible(false);

        addEfectoPress(btBuyFiat500Lounge);
        btBuyFiat500Lounge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyFiat500) {
                    Settings.skinSeleccionada = SKIN_CARRO_FIAT_500_LOUNGE;
                    setSelected(btBuyFiat500Lounge);
                } else if (Settings.coinsTotal >= PRECIO_CARRO_FIAT_500_LOUNGE) {
                    Settings.coinsTotal -= PRECIO_CARRO_FIAT_500_LOUNGE;
                    setButtonStylePurchased(btBuyFiat500Lounge);
                    didBuyFiat500 = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_HONDA_CRV
        if (didBuyHondaCRV)
            btBuyHondaCRV = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyHondaCRV = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_HONDA_CRV)
            btBuyHondaCRV.setVisible(false);

        addEfectoPress(btBuyHondaCRV);
        btBuyHondaCRV.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyHondaCRV) {
                    Settings.skinSeleccionada = SKIN_CARRO_HONDA_CRV;
                    setSelected(btBuyHondaCRV);
                } else if (Settings.coinsTotal >= PRECIO_CARRO_HONDA_CRV) {
                    Settings.coinsTotal -= PRECIO_CARRO_HONDA_CRV;
                    setButtonStylePurchased(btBuyHondaCRV);
                    didBuyHondaCRV = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_MAZDA_6
        if (didBuyMazda6)
            btBuyMazda6 = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyMazda6 = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_MAZDA_6)
            btBuyMazda6.setVisible(false);

        addEfectoPress(btBuyMazda6);
        btBuyMazda6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyMazda6) {
                    Settings.skinSeleccionada = SKIN_CARRO_MAZDA_6;
                    setSelected(btBuyMazda6);
                } else if (Settings.coinsTotal >= PRECIO_CARRO_MAZDA_6) {
                    Settings.coinsTotal -= PRECIO_CARRO_MAZDA_6;
                    setButtonStylePurchased(btBuyMazda6);
                    didBuyMazda6 = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_MAZDA_RX8
        if (didBuyMazdaRX8)
            btBuyMazdaRX8 = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyMazdaRX8 = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_MAZDA_RX8)
            btBuyMazdaRX8.setVisible(false);

        addEfectoPress(btBuyMazdaRX8);
        btBuyMazdaRX8.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyMazdaRX8) {
                    Settings.skinSeleccionada = SKIN_CARRO_MAZDA_RX8;
                    setSelected(btBuyMazdaRX8);
                } else if (Settings.coinsTotal >= PRECIO_CARRO_MAZDA_RX8) {
                    Settings.coinsTotal -= PRECIO_CARRO_MAZDA_RX8;
                    setButtonStylePurchased(btBuyMazdaRX8);
                    didBuyMazdaRX8 = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_SEAT_IBIZA
        if (didBuySeatIbiza)
            btBuySeatIbiza = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuySeatIbiza = new TextButton("Buy", Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_SEAT_IBIZA)
            btBuySeatIbiza.setVisible(false);

        addEfectoPress(btBuySeatIbiza);
        btBuySeatIbiza.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuySeatIbiza) {
                    Settings.skinSeleccionada = SKIN_CARRO_SEAT_IBIZA;
                    setSelected(btBuySeatIbiza);
                } else if (Settings.coinsTotal >= PRECIO_CARRO_SEAT_IBIZA) {
                    Settings.coinsTotal -= PRECIO_CARRO_SEAT_IBIZA;
                    setButtonStylePurchased(btBuySeatIbiza);
                    didBuySeatIbiza = true;
                }
                savePurchases();
            }
        });

        // SKIN_CARRO_VOLKSWAGEN_SCIROCCO
        if (didBuyVolkswagenScirocco)
            btBuyVolkswagenScirocco = new TextButton("Select",
                    Assets.styleTextButtonPurchased);
        else
            btBuyVolkswagenScirocco = new TextButton("Buy",
                    Assets.styleTextButtonBuy);

        if (Settings.skinSeleccionada == SKIN_CARRO_VOLKSWAGEN_SCIROCCO)
            btBuyVolkswagenScirocco.setVisible(false);

        addEfectoPress(btBuyVolkswagenScirocco);
        btBuyVolkswagenScirocco.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (didBuyVolkswagenScirocco) {
                    Settings.skinSeleccionada = SKIN_CARRO_VOLKSWAGEN_SCIROCCO;
                    setSelected(btBuyVolkswagenScirocco);
                } else if (Settings.coinsTotal >= PRECIO_CARRO_VOLKSWAGEN_SCIROCCO) {
                    Settings.coinsTotal -= PRECIO_CARRO_VOLKSWAGEN_SCIROCCO;
                    setButtonStylePurchased(btBuyVolkswagenScirocco);
                    didBuyVolkswagenScirocco = true;
                }
                savePurchases();
            }
        });

        arrBotones.add(btBuyDiablo);
        arrBotones.add(btBuyBanshee);
        arrBotones.add(btBuyTornado);
        arrBotones.add(btBuyTurismo);
        arrBotones.add(btBuyAudiS5);
        arrBotones.add(btBuyBmwX6);
        arrBotones.add(btBuyBullet);
        arrBotones.add(btBuyCrossfire);
        arrBotones.add(btBuyCitroenC4);
        arrBotones.add(btBuyDodgeCharger);
        arrBotones.add(btBuyFiat500Lounge);
        arrBotones.add(btBuyHondaCRV);
        arrBotones.add(btBuyMazda6);
        arrBotones.add(btBuyMazdaRX8);
        arrBotones.add(btBuySeatIbiza);
        arrBotones.add(btBuyVolkswagenScirocco);
    }

    private void loadPurchases() {
        didBuyBanshee = pref.getBoolean("didBuyBanshee", false);
        didBuyTornado = pref.getBoolean("didBuyTornado", false);
        didBuyTurismo = pref.getBoolean("didBuyTurismo", false);
        didBuyAudiS5 = pref.getBoolean("didBuyAudiS5", false);
        didBuyBmwX6 = pref.getBoolean("didBuyBmwX6", false);
        didBuyBullet = pref.getBoolean("didBuyBullet", false);
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
        pref.putBoolean("didBuyBullet", didBuyBullet);
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

    private void setButtonStylePurchased(TextButton boton) {
        boton.setStyle(Assets.styleTextButtonPurchased);
        boton.setText("Select");
    }

    private void setSelected(TextButton boton) {
        // Pongo todos visibles y al final el boton seleccionado en invisible
        Iterator<TextButton> i = arrBotones.iterator();
        while (i.hasNext()) {
            i.next().setVisible(true);
        }
        boton.setVisible(false);
    }

    protected void addEfectoPress(final Actor actor) {
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
