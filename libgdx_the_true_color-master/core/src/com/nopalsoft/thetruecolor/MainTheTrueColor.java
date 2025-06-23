package com.nopalsoft.thetruecolor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.thetruecolor.handlers.FacebookHandler;
import com.nopalsoft.thetruecolor.handlers.GameServicesHandler;
import com.nopalsoft.thetruecolor.handlers.HandlerGWT;
import com.nopalsoft.thetruecolor.handlers.HandlerGWT.OnTextureLoaded;
import com.nopalsoft.thetruecolor.handlers.RequestHandler;
import com.nopalsoft.thetruecolor.leaderboard.Person;
import com.nopalsoft.thetruecolor.leaderboard.Person.TipoCuenta;
import com.nopalsoft.thetruecolor.screens.MainMenuScreen;
import com.nopalsoft.thetruecolor.screens.Screens;

import java.util.Iterator;

public class MainTheTrueColor extends Game {
    public Array<Person> arrPerson;

    public final GameServicesHandler gameServiceHandler;
    public final RequestHandler reqHandler;
    public final FacebookHandler facebookHandler;
    public final HandlerGWT handlerGWT;

    public MainTheTrueColor(RequestHandler reqHandler,
                            GameServicesHandler gameServiceHandler,
                            FacebookHandler facebookHandler,
                            HandlerGWT handlerGWT) {
        this.reqHandler = reqHandler;
        this.gameServiceHandler = gameServiceHandler;
        this.facebookHandler = facebookHandler;
        this.handlerGWT = handlerGWT;
    }

    public Stage stage;
    public SpriteBatch batcher;

    @Override
    public void create() {

        stage = new Stage(new StretchViewport(Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT));
        batcher = new SpriteBatch();

        Settings.load();
        Assets.load();
        com.nopalsoft.thetruecolor.Achievements.init(this);
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
        // (FIXME: Profile images should not be hardcoded here)
        handlerGWT.getTextureFromFacebook("https://picsum.photos/200", new OnTextureLoaded() {
            @Override
            public void onTextureLoaded(Texture texture) {
                oPerson.setPicture(new TextureRegionDrawable(new TextureRegion(texture)));
            }
        });
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
