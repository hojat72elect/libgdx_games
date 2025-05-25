package com.nopalsoft.ninjarunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.handlers.RequestHandler;
import com.nopalsoft.ninjarunner.leaderboard.Person;
import com.nopalsoft.ninjarunner.screens.Screens;

public class MainGame extends Game {

    public Array<Person> arrPerson = new Array<>();

    public final RequestHandler reqHandler;

    public Stage stage;
    public SpriteBatch batcher;
    public I18NBundle idiomas;

    public MainGame(RequestHandler reqHandler) {
        this.reqHandler = reqHandler;
    }

    @Override
    public void create() {
        idiomas = I18NBundle.createBundle(Gdx.files.internal("strings/strings"));
        batcher = new SpriteBatch();
        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));

        Settings.load();
        Assets.load();
        // Achievements.init(this);
        setScreen(new GameScreen(this, true));
    }

    public void setArrayPerson(Array<Person> _arrPerson) {
        if (arrPerson == null) {
            arrPerson = _arrPerson;
        } else {
            for (Person oPerson : _arrPerson) {
                if (!arrPerson.contains(oPerson, false))// false para que compare por equals que ya sobreescribi
                    arrPerson.add(oPerson);
                else {
                    arrPerson.get(arrPerson.indexOf(oPerson, false)).updateDatos(oPerson.name, oPerson.score);
                }
            }
        }

        for (Person oPerson : arrPerson) {
            //Antes lo tenia en el constructor de la clase persona pero lo que pasaba era que, Cada vez que se creaba
            // el objeto persona ya fuera en la clase de Android, iOS o desktop siempre descargaba las imagenes otra vez
            //Por ejemplo se descargaban todas las imagenes de _arrPerson aunque ya existieran en arrPerson
            oPerson.downloadImage(null);
        }

        arrPerson.sort();// Acomoda de mayor a menor

    }

}
