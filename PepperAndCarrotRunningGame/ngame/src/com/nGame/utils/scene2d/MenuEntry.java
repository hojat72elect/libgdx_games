package com.nGame.utils.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nGame.utils.ng.Assets;
import com.nGame.utils.ng.Log;

public abstract class MenuEntry extends Label {

    private Color hoverColor = Color.RED;//SameBreaker.I.themeManager.getCurrentTheme().menuColor;
    private Color baseColor;

    private float duration = .5f;
    private Interpolation interpolIn = Interpolation.exp5In;
    private Interpolation interpolOut = Interpolation.exp5Out;

    private boolean selected=false;

    public MenuEntry(CharSequence text) {
        super(text, Assets.I.defaultLabelStyle);

       // setAlignment(Align.right);


        baseColor = Color.WHITE;

        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer,
                              Actor fromActor) {
                select();
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor toActor) {
                deselect();
                super.exit(event, x, y, pointer, toActor);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                onAction();
            }
        });

    }

    @Override
    public void act(float delta) {

        super.act(delta);
    }

    abstract public void onAction();

    public void select(){
        Log.d("select");
        addAction(Actions.color(hoverColor, duration, interpolOut));
        selected=true;
    }

    public void deselect(){
        Log.d("deselect");
        addAction(Actions.color(baseColor, duration, interpolIn));
        selected=false;
    }

    public void setHoverColor(Color nHoverColor) {
        hoverColor = nHoverColor;
    }
}
