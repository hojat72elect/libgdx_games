package com.salvai.centrum.screens;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.salvai.centrum.utils.Constants;
import com.salvai.centrum.utils.RandomUtil;

public class Background {

    private Array<Star> stars;
    private Array<Integer> starsCreationDelay;
    private Texture starTexture;

    public Background(Texture starTexture) {
        this.starTexture = starTexture;
        stars = new Array<Star>();
        starsCreationDelay = new Array<Integer>();

        for (int i = 0; i < 10; i++) {
            starsCreationDelay.add(10 * i);
        }

        //create first stars
        for (int i = 0; i < Constants.MAX_STARS - starsCreationDelay.size; i++)
            stars.addAll(new Star(starTexture,true));
    }

    private void update(float delta) {
        checkDelays(delta);
        updateStars();
        createStars();
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

    private void createStars() {
        for (int i = 0; i < Constants.MAX_STARS - stars.size - starsCreationDelay.size; i++)
            stars.add(new Star(starTexture,false));
    }

    private void updateStars() {
        for (Star star : stars)
            if (star.move()) {
                stars.removeValue(star, false);
                starsCreationDelay.add(40); // new delay
            }

    }

    public void draw(float delta, Batch batch) {
        update(delta);
        for (Star star : stars)
            star.sprite.draw(batch);
    }

    public void drawPause( Batch batch) {
        for (Star star : stars)
            star.sprite.draw(batch);
    }

    private class Star {
        public Sprite sprite;
        Vector2 position;
        int speed;

        Star(Texture starTexture, boolean initial) {
            if (initial)
                position = new Vector2(RandomUtil.getRandomBackgroundStarXCoordinate(), RandomUtil.getRandomBackgroundStarYCoordinate());
            else
                position = new Vector2(Constants.SCREEN_WIDTH, RandomUtil.getRandomBackgroundStarYCoordinate());
            sprite = new Sprite(starTexture);
            sprite.setBounds(position.x, position.y, Constants.STAR_SIZE, Constants.STAR_SIZE);
            speed = RandomUtil.getRandomStarSpeed();
//            sprite.setColor(RandomUtil.getRandomColor());
            sprite.setColor(RandomUtil.getRandomColor());
            sprite.setAlpha(RandomUtil.getRandomAlpha());
        }


        boolean move() {
            position.x -= speed;
            sprite.setX(position.x);
            return position.x <= 0;
        }
    }
}
