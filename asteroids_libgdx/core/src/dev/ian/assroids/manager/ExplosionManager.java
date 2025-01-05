package dev.ian.assroids.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dev.ian.assroids.entity.Explosion;

/**
 * Created by: Ian Parcon
 * Date created: Sep 09, 2018
 * Time created: 9:49 PM
 */
public class ExplosionManager {
    private List<Explosion> explosions;

    public ExplosionManager() {
        explosions = new ArrayList<Explosion>();

    }

    public void add(Explosion explosion) {
        explosions.add(explosion);
    }

    public void draw(SpriteBatch batch) {
        Iterator<Explosion> explosionIter = explosions.iterator();
        while (explosionIter.hasNext()) {
            Explosion explosion = explosionIter.next();
            explosion.update(Gdx.graphics.getDeltaTime());
            explosion.draw(batch);
            if (explosion.isAnimationFinished()) explosionIter.remove();
        }
    }
}
