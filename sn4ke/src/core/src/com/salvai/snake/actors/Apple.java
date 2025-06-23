package com.salvai.snake.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.salvai.snake.utils.Colors;
import com.salvai.snake.utils.Constants;
import com.salvai.snake.utils.WorldUtils;

public class Apple extends GameObject {


    public Apple(Vector2 worldPosition, Texture texture, WorldUtils worldUtils, int index) {
        super(worldPosition, texture, worldUtils);
        setOrigin(getWidth() * 0.5f, getHeight() * 0.5f);
        setColor(Colors.getBodyColor(index));
        addAction(Actions.forever(Actions.sequence(Actions.scaleBy(Constants.SCALE, Constants.SCALE, Constants.DURATION), Actions.delay(0.2f), Actions.scaleBy(-Constants.SCALE, -Constants.SCALE, Constants.DURATION))));
    }

    public void reset(Vector2 worldPosition) {
        this.worldPosition = worldPosition;
        screenPosition = worldUtils.worldToScreen(worldPosition);
        addAction(Actions.sequence(Actions.fadeOut(Constants.DURATION * 0.5f), Actions.run(new Runnable() {
            @Override
            public void run() {
                setPosition(screenPosition.x, screenPosition.y);
            }
        }), Actions.delay(0.2f), Actions.fadeIn(Constants.DURATION)));
    }
}
