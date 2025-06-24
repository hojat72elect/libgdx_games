package com.nopalsoft.thetruecolor.leaderboard;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
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
    public String urlImagen;

    /**
     * uso un image button porque puede tener fondo y una imagen
     */
    private ImageButton imagenPersona;
    private final Image imagenCuenta;

    Label lbNombre;
    Label lbScore;

    boolean isMe;// Indica que esta persona es el usuario

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
            default:
            case GOOGLE_PLAY:
                keyCuenta = Assets.btGoogle;
                break;
        }

        imagenCuenta = new Image(keyCuenta);
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

    public void setPicture(TextureRegionDrawable drawable) {
        imagenPersona = new ImageButton(new ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null));
        imagenPersona.setSize(60, 60);
        imagenPersona.getImageCell().size(60);
        imagenPersona.setPosition(60, HEIGHT / 2f - imagenPersona.getHeight() / 2f);
        addActor(imagenPersona);
    }

    // Sacado de http://stackoverflow.com/a/15329259/3479489
    public String formatScore() {
        String str = String.valueOf(score);
        int floatPos = str.indexOf(".") > -1 ? str.length() - str.indexOf(".") : 0;
        int nGroups = (str.length() - floatPos - 1 - (str.indexOf("-") > -1 ? 1 : 0)) / 3;
        for (int i = 0; i < nGroups; i++) {
            int commaPos = str.length() - i * 4 - 3 - floatPos;
            str = str.substring(0, commaPos) + "," + str.substring(commaPos);
        }
        return str;
    }

    @Override
    public int compareTo(Person o) {
        if (score > o.score)
            return -1;
        else if (score < o.score)
            return 1;
        else
            return 0;
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

    public void setImagenURL(String scoreHolderIconImageUrl) {
        urlImagen = scoreHolderIconImageUrl;
    }
}
