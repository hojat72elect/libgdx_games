package com.nopalsoft.ninjarunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.handlers.FacebookHandler;
import com.nopalsoft.ninjarunner.handlers.GameServicesHandler;
import com.nopalsoft.ninjarunner.handlers.RequestHandler;
import com.nopalsoft.ninjarunner.leaderboard.LeaderboardScreen;
import com.nopalsoft.ninjarunner.leaderboard.Person;
import com.nopalsoft.ninjarunner.screens.Screens;

import java.util.Iterator;

public class MainGame extends Game {

    public Array<Person> arrPerson = new Array<>();

    public final GameServicesHandler gameServiceHandler;
    public final RequestHandler reqHandler;
    public final FacebookHandler facebookHandler;


    public Stage stage;
    public SpriteBatch batcher;
    public I18NBundle idiomas;

    public MainGame(RequestHandler reqHandler, GameServicesHandler gameServiceHandler, FacebookHandler facebookHandler) {
        this.reqHandler = reqHandler;
        this.gameServiceHandler = gameServiceHandler;
        this.facebookHandler = facebookHandler;
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

        //Si estoy en la tabla pues actalizo la tabla
        if (getScreen() instanceof LeaderboardScreen) {
            LeaderboardScreen oScreen = (LeaderboardScreen) getScreen();
            oScreen.updateLeaderboard();
        }
    }

    /**
     * p
     * Se manda llamar cuando se cierra la sesion en facebook, quita de la tabla todos los usuario de facebook
     */
    public void removeFromArray(Person.TipoCuenta cuenta) {
        if (arrPerson == null)
            return;

        Iterator<Person> i = arrPerson.iterator();
        while (i.hasNext()) {
            Person obj = i.next();
            if (obj.tipoCuenta == cuenta)
                i.remove();
        }

        arrPerson.sort();// Acomoda de mayor a menor

        //Si estoy en la tabla pues actalizo la tabla
        if (getScreen() instanceof LeaderboardScreen) {
            LeaderboardScreen oScreen = (LeaderboardScreen) getScreen();
            oScreen.updateLeaderboard();
        }
    }
}
