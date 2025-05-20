package com.nopalsoft.ninjarunner.leaderboard;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.ninjarunner.Assets;

public class LeaderBoardFrame extends Table {
    Person oPersona;
    /**
     * uso un image button porque puede tener fondo y una imagen
     */
    private ImageButton imagenPersona;
    private Image imagenCuenta;

    Label lbNombre;
    Label lbScore;

    Table tbAux;//Es necesaria porque del lado izq va una foto y del lado derecho varios textField en renglones

    public LeaderBoardFrame(Person persona) {
        setBackground(Assets.backgroundItemShop);
        pad(5);
        this.oPersona = persona;


        lbNombre = new Label(oPersona.name, Assets.labelStyleChico);
        lbScore = new Label(oPersona.getScoreWithFormat(), new Label.LabelStyle(Assets.fontChico, Color.RED));

        tbAux = new Table();
        tbAux.left();

        tbAux.defaults().left();
        tbAux.add(lbNombre).row();
        tbAux.add(lbScore).row();

        Image imRedSocial = null;
        switch (oPersona.tipoCuenta) {
            case GOOGLE_PLAY:
                imRedSocial = new Image(Assets.imGoogle);
                break;
            case AMAZON:
                imRedSocial = new Image(Assets.imAmazon);
                break;
            case FACEBOOK:
                imRedSocial = new Image(Assets.imFacebook);
                break;
        }
        tbAux.add(imRedSocial).size(25).row();


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
        refresh();//Para que ponga la info luego luego. si lo borro hasta que se ponga la photo se pone la info
    }

    public void setPicture(TextureRegionDrawable drawable) {
        imagenPersona = new ImageButton(new ImageButton.ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null));
        refresh();
    }

    private void refresh() {
        clear();
        float size = 100;
        if (imagenPersona != null) {
            imagenPersona.getImageCell().size(size);
            add(imagenPersona).size(size);
        } else {
            add().size(size);
        }

        add(tbAux).padLeft(20).expandX().fill();


    }

}
