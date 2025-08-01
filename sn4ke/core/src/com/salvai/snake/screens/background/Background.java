package com.salvai.snake.screens.background;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.badlogic.gdx.utils.Array;
import com.salvai.snake.enums.MovingDirection;
import com.salvai.snake.utils.Colors;
import com.salvai.snake.utils.Constants;
import com.salvai.snake.utils.RandomUtil;

public class Background {

    private final Array<Integer> starsCreationDelay;
    private final Texture texture;
    private final Stage stage;
    private final int width;
    private final int height;
    private final Image backgroundImage;
    private final int backgroundIndex;
    private boolean colorToReset;
    private int colorIndex;


    public Background(Texture texture, Stage stage, Texture backgroundTexture, int currentBackgroundIndex) {
        this.texture = texture;
        this.stage = stage;
        backgroundIndex = currentBackgroundIndex;

        width = (int) stage.getViewport().getWorldWidth();
        height = (int) stage.getViewport().getWorldHeight();

        starsCreationDelay = new Array<Integer>();

        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setColor(Colors.getBackgroundColor(backgroundIndex));
        backgroundImage.setOrigin(0, 0);
        backgroundImage.setSize(width, height);
        backgroundImage.rotateBy(0);
        backgroundImage.setPosition(0, 0);
        stage.addActor(backgroundImage);

        colorToReset = false;

        for (int i = 0; i < 10; i++)
            starsCreationDelay.add(10 * i);

        //create first stars
        for (int i = 0; i < Constants.MAX_BACKGROUND_OBJECTS - starsCreationDelay.size; i++)
            stage.addActor(new BackgroundObject(texture, true, MovingDirection.DOWN));
    }

    public void changeBackground(final int index) {
        colorIndex = index;
        setBackgroundObjectColor();
        backgroundImage.setColor(Colors.getBackgroundColor(index));
    }

    private void update(float delta, MovingDirection direction, int worldTime, boolean newBest) {
        checkDelays(delta);
        updateStars(direction, 1f / worldTime);
        createStars(direction);
        if (newBest) {
            colorToReset = true;
            randomizeColors();
        } else if (colorToReset) {
            colorToReset = false;
            setBackgroundObjectColor();
        }
    }

    private void setBackgroundObjectColor() {
        for (Actor backgroundObject : stage.getActors())
            if (backgroundObject instanceof BackgroundObject)
                backgroundObject.setColor(Colors.getBackgroundBlockColor(colorIndex));
    }

    private void randomizeColors() {
        for (Actor backgroundObject : stage.getActors())
            if (backgroundObject instanceof BackgroundObject)
                backgroundObject.setColor(RandomUtil.getRandomColor());
    }

    private void checkDelays(float delta) {
        for (int i = 0; i < starsCreationDelay.size; i++) {
            int value = starsCreationDelay.get(i);
            value -= delta;
            starsCreationDelay.set(i, value);
            if (starsCreationDelay.get(i) <= 0)
                starsCreationDelay.removeIndex(i);
        }
    }

    private void createStars(MovingDirection direction) {
        for (int i = 0; i < Constants.MAX_BACKGROUND_OBJECTS - stage.getActors().size - starsCreationDelay.size - 1; i++)
            stage.addActor(new BackgroundObject(texture, false, direction));
    }

    private void updateStars(MovingDirection direction, float worldTime) {
        for (Actor backgroundObject : stage.getActors())
            if (backgroundObject instanceof BackgroundObject && ((BackgroundObject) backgroundObject).move(direction, worldTime)) {
                backgroundObject.remove();
                starsCreationDelay.add(40); // new delay
            }
    }

    public void draw(float delta, MovingDirection direction, int worldTime, boolean newBest) {
        update(delta, direction, worldTime, newBest);
        stage.act();
        stage.getViewport().apply();
        stage.draw();
    }

    private class BackgroundObject extends Image {
        int speed;
        float alpha;

        BackgroundObject(Texture texture, boolean start, MovingDirection direction) {
            super(texture);
            int randomCloudSizeFactor = RandomUtil.getRandomCloudSizeFactor();

            setColor(Colors.getBackgroundBlockColor(colorIndex));

            if (start)
                setPosition(RandomUtil.getRandomBackgroundStarXCoordinate(width), RandomUtil.getRandomBackgroundStarYCoordinate(height));
            else
                switch (direction) {
                    case UP:
                        setPosition(RandomUtil.getRandomBackgroundStarXCoordinate(width), height);
                        break;
                    case DOWN:
                        setPosition(RandomUtil.getRandomBackgroundStarXCoordinate(width), -Constants.BACKGROUND_OBJECT_HEIGHT * randomCloudSizeFactor);
                        break;
                    case RIGHT:
                        setPosition(width, RandomUtil.getRandomBackgroundStarYCoordinate(height));
                        break;
                    default:
                        setPosition(-Constants.BACKGROUND_OBJECT_WIDTH * randomCloudSizeFactor, RandomUtil.getRandomBackgroundStarYCoordinate(height));
                        break;
                }
            setSize(Constants.BACKGROUND_OBJECT_WIDTH * randomCloudSizeFactor, Constants.BACKGROUND_OBJECT_HEIGHT * randomCloudSizeFactor);
            speed = RandomUtil.getRandomStarSpeed();
            alpha = RandomUtil.getRandomAlpha();
        }


        boolean move(MovingDirection direction, float worldTime) {
            switch (direction) {
                case UP:
                    setY(getY() - (speed * worldTime));
                    break;
                case DOWN:
                    setY(getY() + (speed * worldTime));
                    break;
                case RIGHT:
                    setX(getX() - (speed * worldTime));
                    break;
                default:
                    setX(getX() + (speed * worldTime));
                    break;
            }

            return getX() <= -getWidth() || getX() >= stage.getViewport().getWorldWidth() || getY() <= -getHeight() || getY() >= stage.getViewport().getWorldHeight();
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            validate();

            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * alpha);

            float x = getX();
            float y = getY();
            float scaleX = getScaleX();
            float scaleY = getScaleY();

            if (getDrawable() instanceof TransformDrawable) {
                float rotation = getRotation();
                if (scaleX != 1 || scaleY != 1 || rotation != 0) {
                    ((TransformDrawable) getDrawable()).draw(batch, x + getImageX(), y + getImageY(), getOriginX() - getImageX(), getOriginY() - getImageY(),
                            getImageWidth(), getImageHeight(), scaleX, scaleY, rotation);
                    return;
                }
            }
            if (getDrawable() != null)
                getDrawable().draw(batch, x + getImageX(), y + getImageY(), getImageWidth() * scaleX, getImageHeight() * scaleY);
        }
    }
}
