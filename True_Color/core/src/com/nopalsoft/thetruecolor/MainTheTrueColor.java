package com.nopalsoft.thetruecolor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.thetruecolor.leaderboard.Person;
import com.nopalsoft.thetruecolor.leaderboard.Person.TipoCuenta;
import com.nopalsoft.thetruecolor.screens.MainMenuScreen;
import com.nopalsoft.thetruecolor.screens.Screens;

import java.util.Iterator;

public class MainTheTrueColor extends Game {
    public Array<Person> arrPerson;

    public MainTheTrueColor() {

    }

    public Stage stage;
    public SpriteBatch batcher;

    @Override
    public void create() {

        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));
        batcher = new SpriteBatch();

        Settings.load();
        Assets.load();
        com.nopalsoft.thetruecolor.Achievements.init();
        setScreen(new MainMenuScreen(this));
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
            getPersonPhoto(oPerson);
        }

        // Si no estoy en el menu principal ps no actualizo
        if (getScreen() instanceof MainMenuScreen) {
            MainMenuScreen oScreen = (MainMenuScreen) getScreen();
            oScreen.updateLeaderboard();
        }
    }

    private void getPersonPhoto(final Person oPerson) {


    }

    /**
     * Se manda llamar cuando se cierra la sesion en facebook, quita de la tabla todos los usuario de facebook
     */
    public void removeFromArray(TipoCuenta cuenta) {
        if (arrPerson == null)
            return;

        Iterator<Person> i = arrPerson.iterator();
        while (i.hasNext()) {
            Person obj = i.next();
            if (obj.tipoCuenta == cuenta)
                i.remove();
        }

        // Si no estoy en el menu principal ps no actualizo
        if (getScreen() instanceof MainMenuScreen) {
            MainMenuScreen oScreen = (MainMenuScreen) getScreen();
            oScreen.updateLeaderboard();
        }
    }
}
