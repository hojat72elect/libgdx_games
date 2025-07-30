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

    public enum AccountType {
        GOOGLE_PLAY, FACEBOOK, AMAZON
    }

    public AccountType accountType;

    final public String personId;
    public String personName;
    public long personScore;

    Label labelName;
    Label labelScore;

    public Person(AccountType accountType, String personId, String personName, long personScore) {
        setBounds(0, 0, WIDTH, HEIGHT);

        this.accountType = accountType;
        this.personId = personId;
        this.personName = personName;
        this.personScore = personScore;

        TextureRegionDrawable accountIconDrawable;
        switch (accountType) {
            case AMAZON:
                accountIconDrawable = Assets.buttonAmazonDrawable;
                break;
            case FACEBOOK:
                accountIconDrawable = Assets.buttonFacebookDrawable;
                break;
            case GOOGLE_PLAY:
            default:
                accountIconDrawable = Assets.buttonGoogleDrawable;
                break;
        }

        Image imagenCuenta = new Image(accountIconDrawable);
        imagenCuenta.setSize(30, 30);
        imagenCuenta.setPosition(10, HEIGHT / 2f - imagenCuenta.getHeight() / 2f);

        labelName = new Label(personName, new LabelStyle(Assets.fontSmall, Color.BLACK));
        labelName.setFontScale(.7f);
        labelName.setPosition(140, 36);

        labelScore = new Label(formatScore(), new LabelStyle(Assets.fontSmall, Color.RED));
        labelScore.setPosition(140, 5);

        addActor(imagenCuenta);
        addActor(labelName);
        addActor(labelScore);

        // Separator
        Image image = new Image(Assets.header);
        image.setPosition(0, 0);
        image.setSize(WIDTH, 5);
        addActor(image);
    }

    // Taken from http://stackoverflow.com/a/15329259/3479489
    public String formatScore() {
        String scoreString = String.valueOf(personScore);
        int floatPos = scoreString.contains(".") ? scoreString.length() - scoreString.indexOf(".") : 0;
        int nGroups = (scoreString.length() - floatPos - 1 - (scoreString.contains("-") ? 1 : 0)) / 3;
        for (int i = 0; i < nGroups; i++) {
            int commaPos = scoreString.length() - i * 4 - 3 - floatPos;
            scoreString = scoreString.substring(0, commaPos) + "," + scoreString.substring(commaPos);
        }
        return scoreString;
    }

    @Override
    public int compareTo(Person otherPerson) {
        return Long.compare(otherPerson.personScore, personScore);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject instanceof Person) {
            Person objPerson = (Person) otherObject;
            return personId.equals(objPerson.personId) && accountType == objPerson.accountType;
        } else
            return false;
    }
}
