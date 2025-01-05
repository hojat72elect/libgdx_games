package com.nGame.utils.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

public abstract class ButtonImage extends Image {

    private Color hoverColor = Color.DARK_GRAY;//GameNG.I.themeManager.getCurrentTheme().menuColor;
    private Color baseColor;

    private float duration = .5f;
    private Interpolation interpolIn = Interpolation.exp5In;
    private Interpolation interpolOut = Interpolation.exp5Out;

    public boolean isSelectable=false;
    private boolean selected=false;

    public ButtonImage(Drawable image) {
        super(image, Scaling.fit, Align.center);


        baseColor = Color.WHITE;

        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer,
                              Actor fromActor) {
                addAction(Actions.color(hoverColor, duration, interpolOut));
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor toActor) {

                addAction(Actions.color(baseColor, duration, interpolIn));
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
    public void setHeight(float height) {
        super.setHeight(height);
        getDrawable().setTopHeight(height);
    }

    @Override
    public void act(float delta) {

        super.act(delta);
    }

    abstract public void onAction();

    public void select(){
        baseColor= Color.DARK_GRAY;
        selected=true;
        addAction(Actions.color(baseColor, duration, interpolIn));
    }

    public void deselect(){
        baseColor= Color.WHITE;
        selected=false;
        addAction(Actions.color(baseColor, duration, interpolIn));
    }

    public void setHoverColor(Color nHoverColor) {
        hoverColor = nHoverColor;
    }
}
