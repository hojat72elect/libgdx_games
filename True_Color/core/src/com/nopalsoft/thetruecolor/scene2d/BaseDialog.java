package com.nopalsoft.thetruecolor.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.thetruecolor.Assets;
import com.nopalsoft.thetruecolor.MainTheTrueColor;
import com.nopalsoft.thetruecolor.screens.BaseScreen;

public class BaseDialog extends Group {
    public static final float ANIMATION_DURATION = .3f;

    private final Image dimImage;
    protected BaseScreen screen;
    protected I18NBundle languages;
    protected MainTheTrueColor game;

    public BaseDialog(BaseScreen currentScreen, float width, float height, float positionY) {
        screen = currentScreen;
        game = currentScreen.game;
        languages = Assets.languagesBundle;
        setSize(width, height);
        setY(positionY);

        dimImage = new Image(Assets.blackPixelDrawable);
        dimImage.setSize(BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT);

        setBackGround(Assets.dialogDrawable);
    }

    protected void setCloseButton(float positionY) {
        Button btClose = new Button(Assets.buttonFalseDrawable);
        btClose.setSize(50F, 50F);
        btClose.setPosition(400F, positionY);
        screen.addPressEffect(btClose);
        btClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        addActor(btClose);
    }

    private void setBackGround(NinePatchDrawable imageBackground) {
        Image img = new Image(imageBackground);
        img.setSize(getWidth(), getHeight());
        addActor(img);
    }

    public void show(Stage stage) {

        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setX(BaseScreen.SCREEN_WIDTH / 2f - getWidth() / 2f);

        setScale(.5f);
        addAction(Actions.scaleTo(1, 1, ANIMATION_DURATION));

        dimImage.getColor().a = 0;
        dimImage.addAction(Actions.alpha(.7f, ANIMATION_DURATION));

        stage.addActor(dimImage);
        stage.addActor(this);

    }

    public void hide() {
        addAction(Actions.sequence(Actions.scaleTo(.5f, .5f, ANIMATION_DURATION), Actions.removeActor()));
        dimImage.addAction(Actions.sequence(Actions.alpha(0, ANIMATION_DURATION), Actions.removeActor()));
    }
}
