package com.nopalsoft.ninjarunner.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.ninjarunner.AnimationSprite;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.MainGame;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.objetos.Mascota;
import com.nopalsoft.ninjarunner.scene2d.AnimatedSpriteActor;

import java.util.Iterator;

public class MascotasSubMenu {

    final int PRECIO_BOMB = 5000;

    boolean didBuyBomb;

    Label lblPrecioBird, lblPrecioBomb;

    TextButton btBuySelectBird, btBuyBomb;
    Array<TextButton> arrBotones;

    public final int MAX_LEVEL = 6;
    final int PRECIO_NIVEL_1 = 350;
    final int PRECIO_NIVEL_2 = 1000;
    final int PRECIO_NIVEL_3 = 3000;
    final int PRECIO_NIVEL_4 = 4500;
    final int PRECIO_NIVEL_5 = 5000;
    final int PRECIO_NIVEL_6 = 7500;

    Button btUpgradeBird, btUpgradeBomb;

    Image[] arrBird;
    Image[] arrBomb;

    Table contenedor;
    I18NBundle idiomas;

    String textBuy;
    String textSelect;

    private final static Preferences pref = Gdx.app.getPreferences("com.tiar.shantirunner.shop");

    public MascotasSubMenu(Table contenedor, MainGame game) {
        idiomas = game.idiomas;
        this.contenedor = contenedor;
        contenedor.clear();

        loadPurchases();

        textBuy = idiomas.get("buy");
        textSelect = idiomas.get("select");

        arrBird = new Image[MAX_LEVEL];
        arrBomb = new Image[MAX_LEVEL];

        if (Settings.LEVEL_MASCOTA_BIRD < MAX_LEVEL) {
            lblPrecioBird = new Label(calcularPrecio(Settings.LEVEL_MASCOTA_BIRD) + "", Assets.labelStyleChico);
        }

        if (!didBuyBomb) {
            lblPrecioBomb = new Label(PRECIO_BOMB + "", Assets.labelStyleChico);
        } else if (Settings.LEVEL_MASCOTA_BOMB < MAX_LEVEL) {
            lblPrecioBomb = new Label(calcularPrecio(Settings.LEVEL_MASCOTA_BOMB) + "", Assets.labelStyleChico);
        }

        inicializarBotones();

        contenedor.defaults().expand().fill().padLeft(10).padRight(20).padBottom(10);

        contenedor.add(
                agregarMascota("Chicken", lblPrecioBird, Assets.Mascota1Fly, 60, 54, idiomas.get("pinkChikenDescription"), btBuySelectBird, arrBird,
                        btUpgradeBird)).row();
        contenedor.add(
                agregarMascota("Bomb", lblPrecioBomb, Assets.MascotaBombFly, 53, 64, idiomas.get("bombDescription"), btBuyBomb, arrBomb, btUpgradeBomb))
                .row();

        setArrays();
    }

    public Table agregarMascota(String titulo, Label lblPrecio, AnimationSprite imagen, float imagenWidth, float imagenHeight, String descripcion,
                                TextButton btBuy, Image[] arrLevel, Button btUpgrade) {
        Image moneda = new Image(Assets.moneda.getKeyFrame(0));
        AnimatedSpriteActor imgPersonaje = new AnimatedSpriteActor(imagen);

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

        tbContent.row().colspan(2);
        tbContent.add(btBuy).expandX().right().size(120, 45);
        tbContent.row().colspan(2);

        return tbContent;

    }

    private void inicializarBotones() {
        arrBotones = new Array<TextButton>();

        {// DEFAULT
            {// BUY
                btBuySelectBird = new TextButton(textSelect, Assets.styleTextButtonPurchased);
                if (Settings.skinMascotaSeleccionada == Mascota.Tipo.GALLINA_ROSA)
                    btBuySelectBird.setVisible(false);
                btBuySelectBird.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Settings.skinMascotaSeleccionada = Mascota.Tipo.GALLINA_ROSA;
                        setSelected(btBuySelectBird);
                    }
                });
            }
            {// UPGRADE
                btUpgradeBird = new Button(Assets.styleButtonUpgrade);
                if (Settings.LEVEL_MASCOTA_BIRD == MAX_LEVEL)
                    btUpgradeBird.setVisible(false);
                btUpgradeBird.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (Settings.monedasTotal >= calcularPrecio(Settings.LEVEL_MASCOTA_BIRD)) {
                            Settings.monedasTotal -= calcularPrecio(Settings.LEVEL_MASCOTA_BIRD);
                            Settings.LEVEL_MASCOTA_BIRD++;
                            updateLabelPriceAndButton(Settings.LEVEL_MASCOTA_BIRD, lblPrecioBird, btUpgradeBird);
                            setArrays();
                        }
                    }
                });
            }
        }

        {// MASCOTA BOM{
            {// BUY
                if (didBuyBomb)
                    btBuyBomb = new TextButton(textSelect, Assets.styleTextButtonPurchased);
                else
                    btBuyBomb = new TextButton(textBuy, Assets.styleTextButtonBuy);

                if (Settings.skinMascotaSeleccionada == Mascota.Tipo.BOMBA)
                    btBuyBomb.setVisible(false);

                btBuyBomb.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (didBuyBomb) {
                            Settings.skinMascotaSeleccionada = Mascota.Tipo.BOMBA;
                            setSelected(btBuyBomb);
                        } else if (Settings.monedasTotal >= PRECIO_BOMB) {
                            Settings.monedasTotal -= PRECIO_BOMB;
                            setButtonStylePurchased(btBuyBomb);
                            didBuyBomb = true;
                            btUpgradeBomb.setVisible(true);
                            updateLabelPriceAndButton(Settings.LEVEL_MASCOTA_BOMB, lblPrecioBomb, btUpgradeBomb);
                        }
                        savePurchases();
                    }
                });
            }
            {// UPGRADE
                btUpgradeBomb = new Button(Assets.styleButtonUpgrade);
                if (Settings.LEVEL_MASCOTA_BOMB == MAX_LEVEL || !didBuyBomb)
                    btUpgradeBomb.setVisible(false);
                btUpgradeBomb.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (Settings.monedasTotal >= calcularPrecio(Settings.LEVEL_MASCOTA_BOMB)) {
                            Settings.monedasTotal -= calcularPrecio(Settings.LEVEL_MASCOTA_BOMB);
                            Settings.LEVEL_MASCOTA_BOMB++;
                            updateLabelPriceAndButton(Settings.LEVEL_MASCOTA_BOMB, lblPrecioBomb, btUpgradeBomb);
                            setArrays();
                        }
                    }
                });
            }
        }

        arrBotones.add(btBuySelectBird);
        arrBotones.add(btBuyBomb);

    }

    private void loadPurchases() {
        didBuyBomb = pref.getBoolean("didBuyBomb", false);
    }

    private void savePurchases() {
        pref.putBoolean("didBuyBomb", didBuyBomb);
        pref.flush();
        Settings.save();

    }

    private void setButtonStylePurchased(TextButton boton) {
        boton.setStyle(Assets.styleTextButtonPurchased);
        boton.setText(textSelect);

    }

    private void setSelected(TextButton boton) {
        // Pongo todos visibles y al final el boton seleccionado en invisible
        Iterator<TextButton> i = arrBotones.iterator();
        while (i.hasNext()) {
            i.next().setVisible(true);
        }
        boton.setVisible(false);
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

            case 4:
                return PRECIO_NIVEL_5;
            default:
            case 5:
                return PRECIO_NIVEL_6;

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
        for (int i = 0; i < Settings.LEVEL_MASCOTA_BIRD; i++) {
            arrBird[i].setDrawable(new TextureRegionDrawable(Assets.btShare));
        }

        for (int i = 0; i < Settings.LEVEL_MASCOTA_BOMB; i++) {
            arrBomb[i].setDrawable(new TextureRegionDrawable(Assets.btShare));
        }

    }

}
