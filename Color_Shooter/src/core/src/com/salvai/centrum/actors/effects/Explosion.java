package com.salvai.centrum.actors.effects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.salvai.centrum.utils.Constants;

public class Explosion {
    public ParticleEffect particleEffect;
    public Vector2 position;
    public Sound sound;

    /**
     * for center ball
     *
     * @param color
     */
    public Explosion(Color color, Sound sound, boolean soundOn, ParticleEffect particleEffect) {

        this.sound = sound;
        this.particleEffect = new ParticleEffect(particleEffect);
        position = new Vector2(Constants.WIDTH_CENTER, Constants.HEIGHT_CENTER);
        this.particleEffect.getEmitters().first().setPosition(position.x, position.y);
        float[] colorF = {color.r, color.g, color.b};
        this.particleEffect.getEmitters().first().getTint().setColors(colorF);
        this.particleEffect.scaleEffect(2);
        this.particleEffect.start();
        this.particleEffect.getEmitters().shuffle();
        if (soundOn)
            this.sound.play();
    }

    /**
     * for enemies
     * @param position
     * @param color
     * @param sound
     * @param soundOn
     * @param particleEffect
     */
    public Explosion(Vector2 position, Color color, Sound sound, boolean soundOn,ParticleEffect particleEffect) {
        this.sound = sound;
        this.position = position;
        this.particleEffect = new ParticleEffect(particleEffect);
        this.particleEffect .getEmitters().first().setPosition(position.x, position.y);
        float[] colorF = {color.r, color.g, color.b};
        this.particleEffect.getEmitters().first().getTint().setColors(colorF);
        this.particleEffect.getEmitters().shuffle();
        this.particleEffect.start();
        if (soundOn)
            this.sound.play();

    }

}