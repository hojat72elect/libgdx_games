package com.salvai.snake.screens.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.salvai.snake.utils.Colors;

import static com.salvai.snake.utils.Constants.DURATION;
import static com.salvai.snake.utils.Constants.INTERPOLATION;
import static com.salvai.snake.utils.Constants.SCALE;


public class PreviewSnake {
    public Array<Image> previews;
    int style;
    int amount;
    boolean active;

    public PreviewSnake(int style, int amount, Texture texture, boolean active) {
        this.style = style;
        this.amount = amount;
        this.active = active;

        previews = new Array<Image>();

        for (int i = 0; i < amount - 1; i++) {
            Image body = new Image(texture);
            body.setOrigin(body.getWidth() * 0.5f, body.getWidth() * 0.5f);
            body.setColor(Colors.getBodyColor(style));
            previews.add(body);
        }

        Image head = new Image(texture);
        head.setOrigin(head.getWidth() * 0.5f, head.getWidth() * 0.5f);
        head.setColor(Colors.getHeadColor(style));
        previews.add(head);

        if (active)
            for (Image preview : previews)
                preview.addAction(Actions.forever(Actions.sequence(Actions.scaleBy(SCALE, SCALE, DURATION, INTERPOLATION), Actions.scaleBy(-SCALE, -SCALE, DURATION, INTERPOLATION))));

    }

    public void startAnimation() {
        if (!active) {
            active = true;
            for (Image preview : previews) {
                preview.clearActions();
                preview.addAction(Actions.forever(Actions.sequence(Actions.scaleBy(SCALE, SCALE, DURATION, INTERPOLATION), Actions.scaleBy(-SCALE, -SCALE, DURATION, INTERPOLATION))));
            }
        }
    }

    public void stopAnimation() {
        if (active) {
            active = false;
            for (Image preview : previews) {
                if (preview.hasActions()) {
                    preview.clearActions();
                    preview.addAction(Actions.scaleTo(1, 1, DURATION, INTERPOLATION));
                }
            }
        }
    }
}
