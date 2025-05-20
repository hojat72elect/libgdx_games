package com.nopalsoft.ninjarunner.leaderboard;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.ninjarunner.Assets;


public class NextGoalFrame extends Group {

    public static final float WIDTH = 170;
    public static final float HEIGHT = 80;
    public Person oPersona;

    /**
     * uso un image button porque puede tener fondo y una imagen
     */
    private ImageButton imagenPersona;
    private Image imagenCuenta;

    Label lbNombre;
    Label lbScorePersona;
    Label lbPuntosRestantesParaSuperar;


    public NextGoalFrame(float x, float y) {
        setBounds(x, y, WIDTH, HEIGHT);

        lbNombre = new Label("", Assets.labelStyleChico);
        lbNombre.setFontScale(.5f);
        lbNombre.setPosition(60, 60);

        lbScorePersona = new Label("", Assets.labelStyleChico);
        lbScorePersona.setFontScale(.5f);
        lbScorePersona.setPosition(60, 40);

        lbPuntosRestantesParaSuperar = new Label("", Assets.labelStyleChico);
        lbPuntosRestantesParaSuperar.setFontScale(.5f);
        lbPuntosRestantesParaSuperar.setPosition(60, 20);

        addActor(lbNombre);
        addActor(lbScorePersona);
        addActor(lbPuntosRestantesParaSuperar);


        debug();
    }

    /**
     * Pone una persona nueva en el frame
     *
     * @param persona
     */
    public void updatePersona(Person persona) {
        this.oPersona = persona;

        lbNombre.setText(oPersona.name);
        lbScorePersona.setText(oPersona.getScoreWithFormat());

//        if (oPersona.imagen != null)
//            setPicture(oPersona.imagen);
//        else {
//            //A veces la imagen de la persona anterior si existe y de la siguiente no
//            // asi que la quito para que no salga la imagen del a persona anterior
//            if (imagenPersona != null)
//                imagenPersona.remove();
//            oPersona.setDownloadImageCompleteListener(new Person.DownloadImageCompleteListener() {
//                @Override
//                public void downloaded() {
//                    setPicture(oPersona.imagen);
//                }
//            });
//        }


        if (oPersona.imagen != null)
            setPicture(oPersona.imagen);
        else {
            oPersona.downloadImage(new Person.DownloadImageCompleteListener() {
                @Override
                public void imageDownloaded() {
                    setPicture(oPersona.imagen);
                }

                @Override
                public void imageDownloadFail() {
                    setPicture(Assets.photoNA);
                }
            });
        }

    }

    private void setPicture(TextureRegionDrawable drawable) {
        imagenPersona = new ImageButton(new ImageButton.ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null));
        imagenPersona.setSize(50, 50);
        imagenPersona.getImageCell().size(50);
        imagenPersona.setPosition(5, HEIGHT / 2f - imagenPersona.getHeight() / 2f);
        addActor(imagenPersona);
    }

    public void updatePuntuacion(long puntuacion) {
        lbPuntosRestantesParaSuperar.setText("Pa Ganar" + (oPersona.score - puntuacion));
    }
}
