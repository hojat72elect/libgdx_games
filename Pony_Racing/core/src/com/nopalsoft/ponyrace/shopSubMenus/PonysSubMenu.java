package com.nopalsoft.ponyrace.shopSubMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ponyrace.Assets;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.screens.ShopScreen;

import java.util.Iterator;

public class PonysSubMenu {
    final String NOMBRE_SKIN_CLOUD = "Cloud";
    final String NOMBRE_SKIN_CIENTIFICO = "cientifico";
    final String NOMBRE_SKIN_ENEMIGO = "enemigo";
    final String NOMBRE_SKIN_NATYLOL = "Natylol";
    final String NOMBRE_SKIN_IGNIS = "Ignis";
    final String NOMBRE_SKIN_LALBA = "LAlba";
    final int PRECIO_DESBLOQUEAR_PONY = 5000;
    final String BUY = "Buy";
    final String USE = "Use";
    private final String prefName = "com.nopalsoft.ponyRace.ponysSubMenu";
    private final Preferences prefAchiv = Gdx.app.getPreferences(prefName);
    Table contenedor;
    Assets oAssets;
    TextButton btUnlockCloud, btUnlockCientifico, btUnlockEnemigo, btUnlockNatylol, btUnlockIgnis, btUnlockLalba;
    Table dentroCloud, dentroCientifico, dentroEnemigo, dentroNatylol, dentroIgnis, dentroLalba;
    private boolean isCientificoUnlocked;
    private boolean isEnemigoUnlocked;
    private boolean isNatylolUnlocked;
    private boolean isIgnisUnlocked;
    private boolean isLalbaUnlocked;

    public PonysSubMenu(ShopScreen shop, Table contenedor) {
        this.contenedor = contenedor;
        oAssets = shop.game.oAssets;

        isCientificoUnlocked = prefAchiv.getBoolean("isCientificoUnlocked", false);
        isEnemigoUnlocked = prefAchiv.getBoolean("isEnemigoUnlocked", false);
        isNatylolUnlocked = prefAchiv.getBoolean("isNatylolUnlocked", false);
        isIgnisUnlocked = prefAchiv.getBoolean("isIgnisUnlocked", false);
        isLalbaUnlocked = prefAchiv.getBoolean("isLalbaUnlocked", false);

        contenedor.clear();

        inicializarBotones();
        setPonys();
    }

    public void guardar() {
        prefAchiv.putBoolean("isCientificoUnlocked", isCientificoUnlocked);
        prefAchiv.putBoolean("isEnemigoUnlocked", isEnemigoUnlocked);
        prefAchiv.putBoolean("isNatylolUnlocked", isNatylolUnlocked);
        prefAchiv.putBoolean("isIgnisUnlocked", isIgnisUnlocked);
        prefAchiv.putBoolean("isLalbaUnlocked", isLalbaUnlocked);
        prefAchiv.flush();
    }

    private void inicializarBotones() {
        TextButtonStyle btStyle = new TextButtonStyle(oAssets.btNubeUpTienda, oAssets.btNubeDownTienda, null, oAssets.fontChco);

        btUnlockCloud = new TextButton(USE, btStyle);
        if (Settings.selectedSkin.equals(NOMBRE_SKIN_CLOUD))
            btUnlockCloud.setVisible(false);
        btUnlockCloud.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.selectedSkin = NOMBRE_SKIN_CLOUD;
                setVisibleButtonsBut(btUnlockCloud);
            }
        });

        // Cientifico
        String textBoton = BUY;
        if (isCientificoUnlocked)
            textBoton = USE;
        btUnlockCientifico = new TextButton(textBoton, btStyle);
        if (Settings.selectedSkin.equals(NOMBRE_SKIN_CIENTIFICO))
            btUnlockCientifico.setVisible(false);
        btUnlockCientifico.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isCientificoUnlocked) {
                    Settings.selectedSkin = NOMBRE_SKIN_CIENTIFICO;
                    setVisibleButtonsBut(btUnlockCientifico);
                } else if (Settings.numeroMonedasActual - PRECIO_DESBLOQUEAR_PONY > 0) {
                    Settings.numeroMonedasActual -= PRECIO_DESBLOQUEAR_PONY;
                    isCientificoUnlocked = true;
                    btUnlockCientifico.setText(USE);
                    removePrecio(dentroCientifico);
                    guardar();
                }
            }
        });

        // Enemigo
        textBoton = BUY;
        if (isEnemigoUnlocked)
            textBoton = USE;
        btUnlockEnemigo = new TextButton(textBoton, btStyle);
        if (Settings.selectedSkin.equals(NOMBRE_SKIN_ENEMIGO))
            btUnlockEnemigo.setVisible(false);
        btUnlockEnemigo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isEnemigoUnlocked) {
                    Settings.selectedSkin = NOMBRE_SKIN_ENEMIGO;
                    setVisibleButtonsBut(btUnlockEnemigo);
                } else if (Settings.numeroMonedasActual - PRECIO_DESBLOQUEAR_PONY > 0) {
                    Settings.numeroMonedasActual -= PRECIO_DESBLOQUEAR_PONY;
                    isEnemigoUnlocked = true;
                    btUnlockEnemigo.setText(USE);
                    removePrecio(dentroEnemigo);
                    guardar();
                }
            }
        });

        // Natylol
        textBoton = BUY;
        if (isNatylolUnlocked)
            textBoton = USE;
        btUnlockNatylol = new TextButton(textBoton, btStyle);
        if (Settings.selectedSkin.equals(NOMBRE_SKIN_NATYLOL))
            btUnlockNatylol.setVisible(false);
        btUnlockNatylol.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isNatylolUnlocked) {
                    Settings.selectedSkin = NOMBRE_SKIN_NATYLOL;
                    setVisibleButtonsBut(btUnlockNatylol);
                } else if (Settings.numeroMonedasActual - PRECIO_DESBLOQUEAR_PONY > 0) {
                    Settings.numeroMonedasActual -= PRECIO_DESBLOQUEAR_PONY;
                    isNatylolUnlocked = true;
                    btUnlockNatylol.setText(USE);
                    removePrecio(dentroNatylol);
                    guardar();
                }
            }
        });

        // Ignis
        textBoton = BUY;
        if (isIgnisUnlocked)
            textBoton = USE;
        btUnlockIgnis = new TextButton(textBoton, btStyle);
        if (Settings.selectedSkin.equals(NOMBRE_SKIN_IGNIS))
            btUnlockIgnis.setVisible(false);
        btUnlockIgnis.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isIgnisUnlocked) {
                    Settings.selectedSkin = NOMBRE_SKIN_IGNIS;
                    setVisibleButtonsBut(btUnlockIgnis);
                } else if (Settings.numeroMonedasActual - PRECIO_DESBLOQUEAR_PONY > 0) {
                    Settings.numeroMonedasActual -= PRECIO_DESBLOQUEAR_PONY;
                    isIgnisUnlocked = true;
                    btUnlockIgnis.setText(USE);
                    removePrecio(dentroIgnis);
                    guardar();
                }
            }
        });

        // Ignis
        textBoton = BUY;
        if (isLalbaUnlocked)
            textBoton = USE;
        btUnlockLalba = new TextButton(textBoton, btStyle);
        if (Settings.selectedSkin.equals(NOMBRE_SKIN_LALBA))
            btUnlockLalba.setVisible(false);
        btUnlockLalba.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isLalbaUnlocked) {
                    Settings.selectedSkin = NOMBRE_SKIN_LALBA;
                    setVisibleButtonsBut(btUnlockLalba);
                } else if (Settings.numeroMonedasActual - PRECIO_DESBLOQUEAR_PONY > 0) {
                    Settings.numeroMonedasActual -= PRECIO_DESBLOQUEAR_PONY;
                    isLalbaUnlocked = true;
                    btUnlockLalba.setText(USE);
                    removePrecio(dentroLalba);
                    guardar();
                }
            }
        });
    }

    private void removePrecio(Table tableDentro) {
        Iterator<Actor> ite = tableDentro.getChildren().iterator();
        while (ite.hasNext()) {
            Actor obj = ite.next();
            if (obj instanceof Image || obj instanceof Label)
                ite.remove();
        }
    }

    /**
     * Pone todos los botones en visible y un boton en invisible
     *
     * @param setInvisible el boton que no se va ver
     */
    private void setVisibleButtonsBut(TextButton setInvisible) {

        // Voy a iterar a dentro del contenedor, luego dentro de tablaDentro y luego de dentro cloud para poner los botones visibles
        for (Actor actor : contenedor.getChildren()) {
            Table tbDentro = (Table) actor;

            for (Actor dentroTbdentro : tbDentro.getChildren()) {
                if (dentroTbdentro instanceof Table) {

                    for (Actor obj : ((Table) dentroTbdentro).getChildren()) {
                        if (obj instanceof TextButton) {
                            obj.setVisible(true);
                        }
                    }
                }
            }
        }
        setInvisible.setVisible(false);
    }

    private void setPonys() {
        LabelStyle lblStyle = new LabelStyle(oAssets.fontChco, Color.WHITE);

        // Cloud
        Table tbDentro = new Table();
        tbDentro.add(new Image(oAssets.perfilCloud)).size(65, 60).padLeft(10).padRight(10);
        Label descripcion = new Label("Play using cloud pony", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expand().fill();
        dentroCloud = new Table();
        dentroCloud.add(btUnlockCloud).size(120, 70);

        tbDentro.add(dentroCloud);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // Cientifico
        tbDentro = new Table();
        tbDentro.add(new Image(oAssets.perfilcientifico)).size(65, 60).padLeft(10).padRight(10);
        descripcion = new Label("Play using scientisg pony", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroCientifico = new Table();
        if (!isCientificoUnlocked) {
            dentroCientifico.add(new Image(oAssets.monedaTienda));
            Label precio = new Label(PRECIO_DESBLOQUEAR_PONY + "", lblStyle);
            dentroCientifico.add(precio).left();
            dentroCientifico.row().colspan(2);
        }
        dentroCientifico.add(btUnlockCientifico).size(120, 70);
        tbDentro.add(dentroCientifico);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // Enemigo
        tbDentro = new Table();
        tbDentro.add(new Image(oAssets.perfilenemigo)).size(65, 60).padLeft(10).padRight(10);
        descripcion = new Label("Play using Enemy pony", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroEnemigo = new Table();
        if (!isEnemigoUnlocked) {
            dentroEnemigo.add(new Image(oAssets.monedaTienda));
            Label precio = new Label(PRECIO_DESBLOQUEAR_PONY + "", lblStyle);
            dentroEnemigo.add(precio).left();
            dentroEnemigo.row().colspan(2);
        }
        dentroEnemigo.add(btUnlockEnemigo).size(120, 70);
        tbDentro.add(dentroEnemigo);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // Natylol
        tbDentro = new Table();
        tbDentro.add(new Image(oAssets.perfilNatylol)).size(65, 60).padLeft(10).padRight(10);
        descripcion = new Label("Play using Natylol pony", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroNatylol = new Table();
        if (!isNatylolUnlocked) {
            dentroNatylol.add(new Image(oAssets.monedaTienda));
            Label precio = new Label(PRECIO_DESBLOQUEAR_PONY + "", lblStyle);
            dentroNatylol.add(precio).left();
            dentroNatylol.row().colspan(2);
        }
        dentroNatylol.add(btUnlockNatylol).size(120, 70);
        tbDentro.add(dentroNatylol);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // Ignis
        tbDentro = new Table();
        tbDentro.add(new Image(oAssets.perfilIgnis)).size(65, 60).padLeft(10).padRight(10);
        descripcion = new Label("Play using Ignis pony", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroIgnis = new Table();
        if (!isIgnisUnlocked) {
            dentroIgnis.add(new Image(oAssets.monedaTienda));
            Label precio = new Label(PRECIO_DESBLOQUEAR_PONY + "", lblStyle);
            dentroIgnis.add(precio).left();
            dentroIgnis.row().colspan(2);
        }
        dentroIgnis.add(btUnlockIgnis).size(120, 70);
        tbDentro.add(dentroIgnis);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);

        // Lalba
        tbDentro = new Table();
        tbDentro.add(new Image(oAssets.perfilLAlba)).size(65, 60).padLeft(10).padRight(10);
        descripcion = new Label("Play using Lalba pony", lblStyle);
        descripcion.setWrap(true);
        tbDentro.add(descripcion).expandX().fill();
        dentroLalba = new Table();
        if (!isLalbaUnlocked) {
            dentroLalba.add(new Image(oAssets.monedaTienda));
            Label precio = new Label(PRECIO_DESBLOQUEAR_PONY + "", lblStyle);
            dentroLalba.add(precio).left();
            dentroLalba.row().colspan(2);
        }
        dentroLalba.add(btUnlockLalba).size(120, 70);
        tbDentro.add(dentroLalba);
        contenedor.add(tbDentro).expandX().fill();
        contenedor.row().padTop(15);
    }
}
