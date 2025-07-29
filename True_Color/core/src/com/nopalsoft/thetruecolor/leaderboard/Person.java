package com.nopalsoft.thetruecolor.leaderboard;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.thetruecolor.Assets;

public class Person extends Group implements Comparable<Person> {
    final float WIDTH = DialogRanking.WIDTH - 5;
    final float HEIGHT = 75;

    public enum TipoCuenta {
        GOOGLE_PLAY, FACEBOOK, AMAZON
    }

    public TipoCuenta tipoCuenta;

    final public String id;
    public String name;
    public long score;

    Label lbNombre;
    Label lbScore;

    public Person(TipoCuenta tipoCuenta, String id, String name, long oScore) {
        setBounds(0, 0, WIDTH, HEIGHT);

        this.tipoCuenta = tipoCuenta;
        this.id = id;
        this.name = name;
        this.score = oScore;

        TextureRegionDrawable keyCuenta;
        switch (tipoCuenta) {
            case AMAZON:
                keyCuenta = Assets.btAmazon;
                break;
            case FACEBOOK:
                keyCuenta = Assets.btFacebook;
                break;
            case GOOGLE_PLAY:
            default:
                keyCuenta = Assets.btGoogle;
                break;
        }

        Image imagenCuenta = new Image(keyCuenta);
        imagenCuenta.setSize(30, 30);
        imagenCuenta.setPosition(10, HEIGHT / 2f - imagenCuenta.getHeight() / 2f);

        lbNombre = new Label(name, new LabelStyle(Assets.fontChico, Color.BLACK));
        lbNombre.setFontScale(.7f);
        lbNombre.setPosition(140, 36);

        lbScore = new Label(formatScore(), new LabelStyle(Assets.fontChico, Color.RED));
        lbScore.setPosition(140, 5);

        addActor(imagenCuenta);
        addActor(lbNombre);
        addActor(lbScore);

        // Separador
        Image img = new Image(Assets.header);
        img.setPosition(0, 0);
        img.setSize(WIDTH, 5);
        addActor(img);
    }

    // Sacado de http://stackoverflow.com/a/15329259/3479489
    public String formatScore() {
        String str = String.valueOf(score);
        int floatPos = str.contains(".") ? str.length() - str.indexOf(".") : 0;
        int nGroups = (str.length() - floatPos - 1 - (str.contains("-") ? 1 : 0)) / 3;
        for (int i = 0; i < nGroups; i++) {
            int commaPos = str.length() - i * 4 - 3 - floatPos;
            str = str.substring(0, commaPos) + "," + str.substring(commaPos);
        }
        return str;
    }

    @Override
    public int compareTo(Person o) {
        return Long.compare(o.score, score);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            Person objPerson = (Person) obj;
            return id.equals(objPerson.id) && tipoCuenta == objPerson.tipoCuenta;
        } else
            return false;
    }

    public void updateDatos(String _name, long _score) {
        name = _name;
        score = _score;
        lbNombre.setText(name);
        lbScore.setText(formatScore());
    }
}
