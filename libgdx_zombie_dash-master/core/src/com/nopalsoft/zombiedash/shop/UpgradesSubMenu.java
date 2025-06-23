package com.nopalsoft.zombiedash.shop;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.zombiedash.AnimationSprite;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.MainZombieDash;
import com.nopalsoft.zombiedash.Settings;
import com.nopalsoft.zombiedash.scene2d.AnimatedSpriteActor;

public class UpgradesSubMenu {
    public final int MAX_LEVEL = 6;

    int precioNivel1 = 350;
    int precioNivel2 = 1250;
    int precioNivel3 = 2500;
    int precioNivel4 = 3750;
    int precioNivel5 = 4750;
    int precioNivel6 = 5750;

    int precio100Bullets = 250;
    int precio250Bullets = 500;
    int precio500Bullets = 750;
    int precio1000Bullets = 1000;

    TextButton btBuy100Bullets;
    TextButton btBuy250Bullets;
    TextButton btBuy500Bullets;
    TextButton btBuy1000Bullets;

    Label lbPrecio100Bullets;
    Label lbPrecio250Bullets;
    Label lbPrecio500Bullets;
    Label lbPrecio1000Bullets;

    TextButton btUpgradeWeapon;
    TextButton btUpgradeSecondJump;
    TextButton btUpgradeLife;
    TextButton btUpgradeShield;

    Label lbPrecioWeapon;
    Label lbPrecioSecondJump;
    Label lbPrecioLife;
    Label lbPrecioShield;

    Image[] arrWeapon;
    Image[] arrSecondJump;
    Image[] arrLife;
    Image[] arrShield;

    Table contenedor;
    I18NBundle idiomas;

    String textUpgrade, textBuy;

    public UpgradesSubMenu(Table contenedor, MainZombieDash game) {
        this.contenedor = contenedor;
        idiomas = game.idiomas;
        contenedor.clear();

        textUpgrade = idiomas.get("upgrade");
        textBuy = idiomas.get("buy");

        arrWeapon = new Image[MAX_LEVEL];
        arrSecondJump = new Image[MAX_LEVEL];
        arrLife = new Image[MAX_LEVEL];
        arrShield = new Image[MAX_LEVEL];

        lbPrecio100Bullets = new Label(precio100Bullets + "", Assets.labelStyleChico);
        lbPrecio250Bullets = new Label(precio250Bullets + "", Assets.labelStyleChico);
        lbPrecio500Bullets = new Label(precio500Bullets + "", Assets.labelStyleChico);
        lbPrecio1000Bullets = new Label(precio1000Bullets + "", Assets.labelStyleChico);

        if (Settings.LEVEL_WEAPON < MAX_LEVEL)
            lbPrecioWeapon = new Label(calcularPrecio(Settings.LEVEL_WEAPON) + "", Assets.labelStyleChico);

        if (Settings.LEVEL_SECOND_JUMP < MAX_LEVEL)
            lbPrecioSecondJump = new Label(calcularPrecio(Settings.LEVEL_SECOND_JUMP) + "", Assets.labelStyleChico);

        if (Settings.LEVEL_LIFE < MAX_LEVEL)
            lbPrecioLife = new Label(calcularPrecio(Settings.LEVEL_LIFE) + "", Assets.labelStyleChico);

        if (Settings.LEVEL_SHIELD < MAX_LEVEL)
            lbPrecioShield = new Label(calcularPrecio(Settings.LEVEL_SHIELD) + "", Assets.labelStyleChico);

        inicializarBotones();

        contenedor
                .add(agregarCompra(idiomas.format("bulles_x_num", 100), lbPrecio100Bullets, Assets.bullet1, idiomas.get("buy_100_bullets"),
                        btBuy100Bullets)).expandX().fill();
        contenedor.row();

        contenedor
                .add(agregarCompra(idiomas.format("bulles_x_num", 250), lbPrecio250Bullets, Assets.bullet1, idiomas.get("buy_250_bullets"),
                        btBuy250Bullets)).expandX().fill();
        contenedor.row();

        contenedor
                .add(agregarCompra(idiomas.format("bulles_x_num", 500), lbPrecio500Bullets, Assets.bullet1, idiomas.get("buy_500_bullets"),
                        btBuy500Bullets)).expandX().fill();
        contenedor.row();

        contenedor
                .add(agregarCompra(idiomas.format("bulles_x_num", 1000), lbPrecio1000Bullets, Assets.bullet1, idiomas.get("buy_1000_bullets"),
                        btBuy1000Bullets)).expandX().fill();
        contenedor.row();

        // Upgrade weapon
        contenedor
                .add(agregarUpgrades(idiomas.get("upgrade_your_weapon"), lbPrecioWeapon, Assets.weapon,
                        idiomas.get("upgrade_your_weapon_description"), arrWeapon, btUpgradeWeapon)).expandX().fill();
        contenedor.row();

        // Life
        contenedor
                .add(agregarUpgrades(idiomas.get("upgrade_your_life"), lbPrecioLife, Assets.itemHeart, idiomas.get("upgrade_your_life_description"),
                        arrLife, btUpgradeLife)).expandX().fill();
        contenedor.row();

        // Shield
        contenedor
                .add(agregarUpgrades(idiomas.get("upgrade_your_shield"), lbPrecioShield, Assets.itemShield,
                        idiomas.get("upgrade_your_shield_description"), arrShield, btUpgradeShield)).expandX().fill();
        contenedor.row();

        // Drop chance
        contenedor
                .add(agregarUpgrades(idiomas.get("upgrade_second_jump"), lbPrecioSecondJump, Assets.itemJump,
                        idiomas.get("upgrade_second_jump_description"), arrSecondJump, btUpgradeSecondJump)).expandX().fill();
        contenedor.row();

        setArrays();

    }

    private Table agregarCompra(String titulo, Label lblPrecio, AnimationSprite imagen, String descripcion, TextButton boton) {

        Image moneda = new Image(Assets.itemGem);
        AnimatedSpriteActor imgPersonaje = new AnimatedSpriteActor(imagen);

        if (lblPrecio == null)
            moneda.setVisible(false);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo.add(new Label(titulo, Assets.labelStyleChico)).expandX().left();
        tbBarraTitulo.add(moneda).right().size(20);
        tbBarraTitulo.add(lblPrecio).right().padRight(10);

        Table tbDescrip = new Table();
        tbDescrip.add(imgPersonaje).center().pad(5).size(30, 30);
        Label lblDescripcion = new Label(descripcion, Assets.labelStyleChico);
        lblDescripcion.setWrap(true);
        lblDescripcion.setFontScale(.9f);
        tbDescrip.add(lblDescripcion).expand().fill().padLeft(5);

        Table tbContent = new Table();
        // tbContent.debug();
        tbContent.setBackground(Assets.storeTableBackground);
        tbContent.defaults().padLeft(20).padRight(20);

        tbContent.add(tbBarraTitulo).expandX().fill().colspan(1).padTop(20);

        tbContent.row().colspan(1);
        tbContent.add(tbDescrip).expandX().fill();

        tbContent.row().padBottom(20);
        tbContent.add(boton).expandX().right().size(120, 45);

        return tbContent;

    }

    /**
     * @param titulo
     * @param lblPrecio
     * @param imagen
     * @param descripcion
     * @param boton
     * @return
     */
    private Table agregarUpgrades(String titulo, Label lblPrecio, AtlasRegion imagen, String descripcion, Image[] arrLevel, TextButton boton) {

        Image moneda = new Image(Assets.itemGem);
        Image imgPersonaje = new Image(imagen);

        if (lblPrecio == null)
            moneda.setVisible(false);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo.add(new Label(titulo, Assets.labelStyleChico)).expandX().left();
        tbBarraTitulo.add(moneda).right().size(20);
        tbBarraTitulo.add(lblPrecio).right().padRight(10);

        Table tbDescrip = new Table();
        tbDescrip.add(imgPersonaje).left().pad(5).size(55, 48);
        Label lblDescripcion = new Label(descripcion, Assets.labelStyleChico);
        lblDescripcion.setWrap(true);
        lblDescripcion.setFontScale(.9f);
        tbDescrip.add(lblDescripcion).expand().fill().padLeft(5);

        Table tbContent = new Table();
        // tbContent.debug();
        tbContent.setBackground(Assets.storeTableBackground);
        tbContent.defaults().padLeft(20).padRight(20);

        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(20);
        tbContent.row().colspan(2);
        tbContent.add(tbDescrip).expandX().fill();
        tbContent.row().padBottom(20);

        Table auxTab = new Table();
        auxTab.defaults().padLeft(5);
        for (int i = 0; i < MAX_LEVEL; i++) {
            arrLevel[i] = new Image(Assets.upgradeOff);
            auxTab.add(arrLevel[i]).width(25).height(25);
        }

        tbContent.add(auxTab).left().expand().padRight(0);
        tbContent.add(boton).left().size(120, 45).padLeft(0);

        return tbContent;

    }

    private void inicializarBotones() {
        btUpgradeWeapon = new TextButton(textUpgrade, Assets.styleTextButtonPurchased);
        if (Settings.LEVEL_WEAPON == MAX_LEVEL)
            btUpgradeWeapon.setVisible(false);
        addEfectoPress(btUpgradeWeapon);
        btUpgradeWeapon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.gemsTotal >= calcularPrecio(Settings.LEVEL_WEAPON)) {
                    Settings.gemsTotal -= calcularPrecio(Settings.LEVEL_WEAPON);
                    Settings.LEVEL_WEAPON++;
                    updateLabelPriceAndButton(Settings.LEVEL_WEAPON, lbPrecioWeapon, btUpgradeWeapon);
                    setArrays();
                }
            }
        });

        // Chance life
        btUpgradeLife = new TextButton(textUpgrade, Assets.styleTextButtonPurchased);
        if (Settings.LEVEL_LIFE == MAX_LEVEL)
            btUpgradeLife.setVisible(false);
        addEfectoPress(btUpgradeLife);
        btUpgradeLife.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.gemsTotal >= calcularPrecio(Settings.LEVEL_LIFE)) {
                    Settings.gemsTotal -= calcularPrecio(Settings.LEVEL_LIFE);
                    Settings.LEVEL_LIFE++;
                    updateLabelPriceAndButton(Settings.LEVEL_LIFE, lbPrecioLife, btUpgradeLife);
                    setArrays();
                }
            }
        });

        // Chance shield
        btUpgradeShield = new TextButton(textUpgrade, Assets.styleTextButtonPurchased);
        if (Settings.LEVEL_SHIELD == MAX_LEVEL)
            btUpgradeShield.setVisible(false);
        addEfectoPress(btUpgradeShield);
        btUpgradeShield.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.gemsTotal >= calcularPrecio(Settings.LEVEL_SHIELD)) {
                    Settings.gemsTotal -= calcularPrecio(Settings.LEVEL_SHIELD);
                    Settings.LEVEL_SHIELD++;
                    updateLabelPriceAndButton(Settings.LEVEL_SHIELD, lbPrecioShield, btUpgradeShield);
                    setArrays();
                }
            }
        });

        // Chance drop
        btUpgradeSecondJump = new TextButton(textUpgrade, Assets.styleTextButtonPurchased);
        if (Settings.LEVEL_SECOND_JUMP == MAX_LEVEL)
            btUpgradeSecondJump.setVisible(false);
        addEfectoPress(btUpgradeSecondJump);
        btUpgradeSecondJump.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.gemsTotal >= calcularPrecio(Settings.LEVEL_SECOND_JUMP)) {
                    Settings.gemsTotal -= calcularPrecio(Settings.LEVEL_SECOND_JUMP);
                    Settings.LEVEL_SECOND_JUMP++;
                    updateLabelPriceAndButton(Settings.LEVEL_SECOND_JUMP, lbPrecioSecondJump, btUpgradeSecondJump);
                    setArrays();
                }
            }
        });

        // Comprar 100
        btBuy100Bullets = new TextButton(textBuy, Assets.styleTextButtonPurchased);
        addEfectoPress(btBuy100Bullets);
        btBuy100Bullets.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.gemsTotal >= precio100Bullets) {
                    Settings.gemsTotal -= precio100Bullets;
                    Settings.numBullets += 100;
                }
            }
        });

        // Comprar 250
        btBuy250Bullets = new TextButton(textBuy, Assets.styleTextButtonPurchased);
        addEfectoPress(btBuy250Bullets);
        btBuy250Bullets.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.gemsTotal >= precio250Bullets) {
                    Settings.gemsTotal -= precio250Bullets;
                    Settings.numBullets += 250;
                }
            }
        });

        // Comprar 500
        btBuy500Bullets = new TextButton(textBuy, Assets.styleTextButtonPurchased);
        addEfectoPress(btBuy500Bullets);
        btBuy500Bullets.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.gemsTotal >= precio500Bullets) {
                    Settings.gemsTotal -= precio500Bullets;
                    Settings.numBullets += 500;
                }
            }
        });

        // Comprar 1000
        btBuy1000Bullets = new TextButton(textBuy, Assets.styleTextButtonPurchased);
        addEfectoPress(btBuy1000Bullets);
        btBuy1000Bullets.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.gemsTotal >= precio1000Bullets) {
                    Settings.gemsTotal -= precio1000Bullets;
                    Settings.numBullets += 1000;
                }
            }
        });

    }

    private void setArrays() {
        for (int i = 0; i < Settings.LEVEL_WEAPON; i++) {
            arrWeapon[i].setDrawable(new TextureRegionDrawable(Assets.itemSkull));
        }

        for (int i = 0; i < Settings.LEVEL_SECOND_JUMP; i++) {
            arrSecondJump[i].setDrawable(new TextureRegionDrawable(Assets.itemSkull));
        }

        for (int i = 0; i < Settings.LEVEL_LIFE; i++) {
            arrLife[i].setDrawable(new TextureRegionDrawable(Assets.itemSkull));
        }

        for (int i = 0; i < Settings.LEVEL_SHIELD; i++) {
            arrShield[i].setDrawable(new TextureRegionDrawable(Assets.itemSkull));
        }

    }

    private int calcularPrecio(int nivel) {
        switch (nivel) {
            case 0:
                return precioNivel1;

            case 1:
                return precioNivel2;

            case 2:
                return precioNivel3;

            case 3:
                return precioNivel4;

            case 4:
                return precioNivel5;
            default:
            case 5:
                return precioNivel6;

        }

    }

    private void updateLabelPriceAndButton(int nivel, Label label, TextButton boton) {
        if (nivel < MAX_LEVEL) {
            label.setText(calcularPrecio(nivel) + "");

        } else {
            label.setVisible(false);
            boton.setVisible(false);
        }
    }

    protected void addEfectoPress(final Actor actor) {
        actor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() - 3);
                event.stop();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() + 3);
            }
        });
    }
}
