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
    /*
     * I use an image button because it can have a background and an image.
     */
    private ImageButton personImage;

    Label labelName;
    Label labelScore;

    Table tableAuxiliary;//It is necessary because on the left side there is a photo and on the right side there are several textFields in lines.

    public LeaderBoardFrame(Person persona) {
        setBackground(Assets.backgroundItemShop);
        pad(5);
        this.oPersona = persona;


        labelName = new Label(oPersona.name, Assets.labelStyleSmall);
        labelScore = new Label(oPersona.getScoreWithFormat(), new Label.LabelStyle(Assets.smallFont, Color.RED));

        tableAuxiliary = new Table();
        tableAuxiliary.left();

        tableAuxiliary.defaults().left();
        tableAuxiliary.add(labelName).row();
        tableAuxiliary.add(labelScore).row();

        Image imRedSocial = switch (oPersona.accountType) {
            case GOOGLE_PLAY -> new Image(Assets.imageGoogle);
            case AMAZON -> new Image(Assets.imageAmazon);
            case FACEBOOK -> new Image(Assets.imageFacebook);
        };
        tableAuxiliary.add(imRedSocial).size(25).row();


        if (oPersona.image != null)
            setPicture(oPersona.image);
        else {
            oPersona.downloadImage(new Person.DownloadImageCompleteListener() {
                @Override
                public void imageDownloaded() {
                    setPicture(oPersona.image);
                }

                @Override
                public void imageDownloadFail() {
                    setPicture(Assets.photoNA);
                }
            });
        }
        refresh();//So that it puts the information right away. If I delete it until the photo is put, the information is put in.
    }

    public void setPicture(TextureRegionDrawable drawable) {
        personImage = new ImageButton(new ImageButton.ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null));
        refresh();
    }

    private void refresh() {
        clear();
        float size = 100;
        if (personImage != null) {
            personImage.getImageCell().size(size);
            add(personImage).size(size);
        } else {
            add().size(size);
        }

        add(tableAuxiliary).padLeft(20).expandX().fill();
    }
}
