package com.nopalsoft.ninjarunner.shop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.MainGame;
import com.nopalsoft.ninjarunner.Settings;

public class UpgradesSubMenu {

    public final int MAX_LEVEL = 6;

    final int PRECIO_NIVEL_1 = 500;
    final int PRECIO_NIVEL_2 = 1000;
    final int PRECIO_NIVEL_3 = 1750;
    final int PRECIO_NIVEL_4 = 2500;
    final int PRECIO_NIVEL_5 = 3000;

    Button btUpgradeMagnet;
    Button btUpgradeLife;
    Button btUpgradeEnergy;
    Button btUpgradeCoins;
    Button btUpgradeTreasureChest;

    Label lbPrecioMagnet;
    Label lbPrecioLife;
    Label lbPrecioEnergy;
    Label lbPrecioCoins;
    Label lbPrecioTreasureChest;

    Image[] arrMagnet;
    Image[] arrLife;
    Image[] arrEnergy;
    Image[] arrCoins;
    Image[] arrTreasureChest;

    Table contenedor;
    I18NBundle idiomas;

    public UpgradesSubMenu(Table contenedor, MainGame game) {
        this.contenedor = contenedor;
        idiomas = game.idiomas;
        contenedor.clear();

        arrMagnet = new Image[MAX_LEVEL];
        arrLife = new Image[MAX_LEVEL];
        arrEnergy = new Image[MAX_LEVEL];
        arrCoins = new Image[MAX_LEVEL];
        arrTreasureChest = new Image[MAX_LEVEL];

        if (Settings.LEVEL_MAGNET < MAX_LEVEL)
            lbPrecioMagnet = new Label(calcularPrecio(Settings.LEVEL_MAGNET) + "", Assets.labelStyleChico);

        if (Settings.LEVEL_LIFE < MAX_LEVEL)
            lbPrecioLife = new Label(calcularPrecio(Settings.LEVEL_LIFE) + "", Assets.labelStyleChico);

        if (Settings.LEVEL_ENERGY < MAX_LEVEL)
            lbPrecioEnergy = new Label(calcularPrecio(Settings.LEVEL_ENERGY) + "", Assets.labelStyleChico);

        if (Settings.LEVEL_COINS < MAX_LEVEL)
            lbPrecioCoins = new Label(calcularPrecio(Settings.LEVEL_COINS) + "", Assets.labelStyleChico);

        if (Settings.LEVEL_TREASURE_CHEST < MAX_LEVEL)
            lbPrecioTreasureChest = new Label(calcularPrecio(Settings.LEVEL_TREASURE_CHEST) + "", Assets.labelStyleChico);

        inicializarBotones();

        contenedor.defaults().expand().fill().padLeft(10).padRight(20).padBottom(10);

        // Upgrade MAGNET
        contenedor.add(
                agregarPersonajeTabla(idiomas.get("upgradeMagnet"), lbPrecioMagnet, Assets.magnet, 35, 35, idiomas.get("magnetDescription"),
                        arrMagnet, btUpgradeMagnet)).row();
        contenedor.add(
                agregarPersonajeTabla("Upgrade Life", lbPrecioLife, Assets.hearth, 38, 29, idiomas.get("bombDescription"), arrLife, btUpgradeLife))
                .row();
        contenedor.add(
                agregarPersonajeTabla("Upgrade Eneergy", lbPrecioEnergy, Assets.energy, 25, 35, idiomas.get("bombDescription"), arrEnergy,
                        btUpgradeEnergy)).row();
        contenedor.add(
                agregarPersonajeTabla("Upgrade coins", lbPrecioCoins, Assets.moneda.getKeyFrame(0), 35, 35, idiomas.get("bombDescription"), arrCoins,
                        btUpgradeCoins)).row();
        contenedor.add(
                agregarPersonajeTabla(idiomas.get("upgradeTreasureChest"), lbPrecioTreasureChest, Assets.magnet, 35, 35,
                        idiomas.get("treasureChestDescription"), arrTreasureChest, btUpgradeTreasureChest)).row();

        setArrays();

    }

    private Table agregarPersonajeTabla(String titulo, Label lblPrecio, Sprite imagen, float imagenWidth, float imagenHeight, String descripcion,
                                        Image[] arrLevel, Button btUpgrade) {

        Image moneda = new Image(Assets.moneda.getKeyFrame(0));
        Image imgPersonaje = new Image(imagen);

        if (lblPrecio == null)
            moneda.setVisible(false);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo.add(new Label(titulo, Assets.labelStyleChico)).expandX().left();
        tbBarraTitulo.add(moneda).right().size(20);
        tbBarraTitulo.add(lblPrecio).right().padRight(10);

        Table tbContent = new Table();
        tbContent.setBackground(Assets.backgroundItemShop);
        tbContent.pad(5);

        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2);
        tbContent.row();

        tbContent.add(imgPersonaje).size(imagenWidth, imagenHeight);
        Label lblDescripcion = new Label(descripcion, Assets.labelStyleChico);
        lblDescripcion.setWrap(true);
        tbContent.add(lblDescripcion).expand().fill();

        Table auxTab = new Table();
        auxTab.setBackground(Assets.backgroundUpgradeBar);
        auxTab.pad(5);
        auxTab.defaults().padLeft(5);
        for (int i = 0; i < MAX_LEVEL; i++) {
            arrLevel[i] = new Image();
            auxTab.add(arrLevel[i]).size(15);
        }

        tbContent.row();
        tbContent.add(auxTab);
        tbContent.add(btUpgrade).left().size(40);

        return tbContent;

    }

    private void inicializarBotones() {
        btUpgradeMagnet = new Button(Assets.styleButtonUpgrade);
        btUpgradeMagnet.setUserObject(Settings.LEVEL_MAGNET);
        initButton(btUpgradeMagnet, lbPrecioMagnet);

        btUpgradeLife = new Button(Assets.styleButtonUpgrade);
        btUpgradeLife.setUserObject(Settings.LEVEL_LIFE);
        initButton(btUpgradeLife, lbPrecioLife);

        btUpgradeEnergy = new Button(Assets.styleButtonUpgrade);
        btUpgradeEnergy.setUserObject(Settings.LEVEL_ENERGY);
        initButton(btUpgradeEnergy, lbPrecioEnergy);

        btUpgradeCoins = new Button(Assets.styleButtonUpgrade);
        btUpgradeCoins.setUserObject(Settings.LEVEL_COINS);
        initButton(btUpgradeCoins, lbPrecioCoins);

        btUpgradeTreasureChest = new Button(Assets.styleButtonUpgrade);
        btUpgradeTreasureChest.setUserObject(Settings.LEVEL_TREASURE_CHEST);
        initButton(btUpgradeTreasureChest, lbPrecioTreasureChest);

    }

    private void initButton(final Button btn, final Label lblPrecio) {
        if ((Integer) btn.getUserObject() == MAX_LEVEL)
            btn.setVisible(false);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int levelActual = (Integer) btn.getUserObject();

                if (Settings.monedasTotal >= calcularPrecio(levelActual)) {
                    Settings.monedasTotal -= calcularPrecio(levelActual);

                    if (btn == btUpgradeMagnet) {
                        Settings.LEVEL_MAGNET++;
                    } else if (btn == btUpgradeLife) {
                        Settings.LEVEL_LIFE++;
                    } else if (btn == btUpgradeEnergy) {
                        Settings.LEVEL_ENERGY++;
                    } else if (btn == btUpgradeCoins) {
                        Settings.LEVEL_COINS++;
                    } else if (btn == btUpgradeTreasureChest) {
                        Settings.LEVEL_TREASURE_CHEST++;
                    }

                    levelActual++;
                    btn.setUserObject(levelActual);

                    updateLabelPriceAndButton(levelActual, lblPrecio, btn);
                    setArrays();
                }
            }
        });
    }

    private int calcularPrecio(int nivel) {
        switch (nivel) {
            case 0:
                return PRECIO_NIVEL_1;

            case 1:
                return PRECIO_NIVEL_2;

            case 2:
                return PRECIO_NIVEL_3;

            case 3:
                return PRECIO_NIVEL_4;

            default:
            case 4:
                return PRECIO_NIVEL_5;

        }

    }

    private void updateLabelPriceAndButton(int nivel, Label label, Button boton) {
        if (nivel < MAX_LEVEL) {
            label.setText(calcularPrecio(nivel) + "");

        } else {
            label.setVisible(false);
            boton.setVisible(false);
        }
    }

    private void setArrays() {
        for (int i = 0; i < Settings.LEVEL_MAGNET; i++) {
            arrMagnet[i].setDrawable(new TextureRegionDrawable(Assets.btShare));
        }

        for (int i = 0; i < Settings.LEVEL_LIFE; i++) {
            arrLife[i].setDrawable(new TextureRegionDrawable(Assets.btShare));
        }

        for (int i = 0; i < Settings.LEVEL_ENERGY; i++) {
            arrEnergy[i].setDrawable(new TextureRegionDrawable(Assets.btShare));
        }

        for (int i = 0; i < Settings.LEVEL_COINS; i++) {
            arrCoins[i].setDrawable(new TextureRegionDrawable(Assets.btShare));
        }

        for (int i = 0; i < Settings.LEVEL_TREASURE_CHEST; i++) {
            arrTreasureChest[i].setDrawable(new TextureRegionDrawable(Assets.btShare));
        }

    }

}
