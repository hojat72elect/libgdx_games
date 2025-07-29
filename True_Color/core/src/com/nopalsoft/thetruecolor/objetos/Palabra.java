package com.nopalsoft.thetruecolor.objetos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.nopalsoft.thetruecolor.Assets;
import com.nopalsoft.thetruecolor.Settings;
import com.nopalsoft.thetruecolor.scene2d.VentanaHelpSettings.Languages;
import com.nopalsoft.thetruecolor.screens.Screens;

public class Palabra {
    public static final int COLOR_BLUE = 0;
    public static final int COLOR_CYAN = 1;
    public static final int COLOR_GREEN = 2;
    public static final int COLOR_YELLOW = 3;

    /**
     * El color de la palabra
     */
    public int color;

    /**
     * Lo que dice la palabra comparar con la tabla de arriba
     */
    public int texto;

    public Label imagen;

    public Palabra() {
        imagen = new Label("", new LabelStyle(Assets.fontExtraGrande, Color.WHITE));
    }

    public void init() {
        color = MathUtils.random(0, 7);

        // 35% posibilidades que lo que dice la pabrar y su color son iguales
        if (MathUtils.randomBoolean(.35f)) {
            texto = color;
        } else {
            texto = MathUtils.random(0, 7);
        }

        String textColor;
        switch (texto) {
            case COLOR_BLUE:
                textColor = "blue";
                break;
            case COLOR_CYAN:
                textColor = "cyan";
                break;
            case COLOR_GREEN:
                textColor = "green";
                break;
            case COLOR_YELLOW:
                textColor = "yellow";
                break;
            case 4:
                textColor = "pink";
                break;
            case 5:
                textColor = "brown";
                break;
            case 6:
                textColor = "purple";
                break;
            case 7:
            default:
                textColor = "red";
                break;
        }

        imagen.remove();
        imagen.setText(Assets.idiomas.get(textColor));
        imagen.setColor(getColorActualPalabra());
        if (Settings.selectedLanguage == Languages.RUSSIAN)
            imagen.setFontScale(.68f);
        else
            imagen.setFontScale(1);
        imagen.pack();
        imagen.setPosition(Screens.SCREEN_WIDTH / 2f - imagen.getWidth() / 2f, 450);
    }

    /**
     * Es el color de la palabra
     */
    public Color getColorActualPalabra() {
        return getColor(color);
    }

    public static Color getColor(int colorPalabra) {
        Color color;
        switch (colorPalabra) {
            case 0:
                color = Color.BLUE;
                break;
            case 1:
                color = Color.CYAN;
                break;
            case 2:
                color = Color.GREEN;
                break;
            case 3:
                color = Color.YELLOW;
                break;
            case 4:
                color = Color.PINK;
                break;
            case 5:
                color = new Color(.6f, .3f, 0, 1);// Cafe
                break;
            case 6:
                color = Color.PURPLE;
                break;
            case 7:
            default:
                color = Color.RED;
                break;
        }
        return color;
    }

    public static Color getRandomColor() {
        return getColor(MathUtils.random(7));
    }
}
