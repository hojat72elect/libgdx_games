package com.nopalsoft.ninjarunner.leaderboard;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.ninjarunner.Assets;


public class NextGoalFrame extends Group {

    public static final float WIDTH = 170;
    public static final float HEIGHT = 80;
    public Person person;

    /**
     * I use an image button because it can have a background and an image.
     */
    private ImageButton personImage;

    Label labelName;
    Label labelPlayerScore;
    Label labelRemainingPointsToOvercome;


    public NextGoalFrame(float x, float y) {
        setBounds(x, y, WIDTH, HEIGHT);

        labelName = new Label("", Assets.labelStyleSmall);
        labelName.setFontScale(.5f);
        labelName.setPosition(60, 60);

        labelPlayerScore = new Label("", Assets.labelStyleSmall);
        labelPlayerScore.setFontScale(.5f);
        labelPlayerScore.setPosition(60, 40);

        labelRemainingPointsToOvercome = new Label("", Assets.labelStyleSmall);
        labelRemainingPointsToOvercome.setFontScale(.5f);
        labelRemainingPointsToOvercome.setPosition(60, 20);

        addActor(labelName);
        addActor(labelPlayerScore);
        addActor(labelRemainingPointsToOvercome);


        debug();
    }

    /**
     * Puts a new person in the frame.
     */
    public void updatePersona(Person person) {
        this.person = person;

        labelName.setText(this.person.name);
        labelPlayerScore.setText(this.person.getScoreWithFormat());


        if (this.person.image != null)
            setPicture(this.person.image);
        else {
            this.person.downloadImage(new Person.DownloadImageCompleteListener() {
                @Override
                public void imageDownloaded() {
                    setPicture(NextGoalFrame.this.person.image);
                }

                @Override
                public void imageDownloadFail() {
                    setPicture(Assets.photoNA);
                }
            });
        }
    }

    private void setPicture(TextureRegionDrawable drawable) {
        personImage = new ImageButton(new ImageButton.ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null));
        personImage.setSize(50, 50);
        personImage.getImageCell().size(50);
        personImage.setPosition(5, HEIGHT / 2f - personImage.getHeight() / 2f);
        addActor(personImage);
    }
}
