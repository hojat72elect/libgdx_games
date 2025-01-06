package com.bms.chemtris.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.bms.chemtris.game.ScoreManager;

/**
 * Class to load labels.
 */
public class LabelLoader {

    //heading label style
    private static final LabelStyle headingStyle = new LabelStyle(FontLoader.font(2.8f), Color.WHITE);
    private static final LabelStyle bodyStyle = new LabelStyle(FontLoader.font(1.2f), Color.WHITE);
    private static final LabelStyle scoreStyle = new LabelStyle(FontLoader.font(.75f),Color.WHITE);

    //method to make heading labels
    public static Label headingLabel(String title){
        Label heading = new Label(title,headingStyle);
        heading.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/4,
                Gdx.graphics.getHeight()/2 + Gdx.graphics.getHeight()/4);
        return heading;
    }

    //method to make score labels
    public static Label levelScore(int level, String tag){
        //get score from tag
        int score = ScoreManager.getScore(tag);
        //make label
        Label label = new Label(" Best-"+score,scoreStyle);
        //set label position
        switch (level){
            case 1:
                label.setPosition(Gdx.graphics.getWidth()/4-Gdx.graphics.getWidth()/30,
                        Gdx.graphics.getHeight()/2+Gdx.graphics.getHeight()/10-Gdx.graphics.getHeight()/14);
                break;

            case 2:
                label.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/15-Gdx.graphics.getWidth()/40,
                        Gdx.graphics.getHeight()/2+Gdx.graphics.getHeight()/10-Gdx.graphics.getHeight()/14);
                break;

            case 3:
                label.setPosition(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/8-Gdx.graphics.getWidth()/30,
                        Gdx.graphics.getHeight()/2+Gdx.graphics.getHeight()/10-Gdx.graphics.getHeight()/14);
                break;

            case 4:
                label.setPosition(Gdx.graphics.getWidth()/4-Gdx.graphics.getWidth()/30,
                        Gdx.graphics.getHeight()/3-Gdx.graphics.getHeight()/14);
                break;

            case 5:
                label.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/15-Gdx.graphics.getWidth()/40,
                        Gdx.graphics.getHeight()/3-Gdx.graphics.getHeight()/14);
                break;

            default:
                label.setPosition(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/8-Gdx.graphics.getWidth()/30,
                        Gdx.graphics.getHeight()/3-Gdx.graphics.getHeight()/14);
                break;
        }

        return label;
    }

    public static Label extraLevel(){
        Label ex = new Label("Score at least one point in any level to play",scoreStyle);
        ex.setWrap(true);
        ex.setWidth(Gdx.graphics.getWidth()/4);
        ex.setPosition(Gdx.graphics.getWidth()/2 + Gdx.graphics.getWidth()/8,
                Gdx.graphics.getHeight()/14);

        return ex;
    }

    public static Label scoreL(){
        Label scoreLabel = new Label("0",headingStyle);
        float h = Gdx.graphics.getHeight()/12;
        scoreLabel.setPosition(Gdx.graphics.getWidth()/2,
                h + 7.4f* h + h);

        return scoreLabel;

    }

    public static Label txtL(){
        Label scoreLabel = new Label("Level 1",bodyStyle);
        float h = Gdx.graphics.getHeight()/12;
        scoreLabel.setPosition(Gdx.graphics.getWidth()/25,
                Gdx.graphics.getHeight()-h-h-h);

        return scoreLabel;
    }

    public static Label gameScore(int score){
        Label scoreLabel = new Label("Scored: "+score,bodyStyle);
        float h = Gdx.graphics.getHeight()/12;
        scoreLabel.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/7,
                Gdx.graphics.getHeight()/2 + Gdx.graphics.getHeight()/8 );

        return scoreLabel;
    }

    public static Label highScore(int score){
        Label scoreLabel = new Label("Best: "+score,bodyStyle);
        float h = Gdx.graphics.getHeight()/12;
        scoreLabel.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/7,
                Gdx.graphics.getHeight()/2);

        return scoreLabel;
    }

}
