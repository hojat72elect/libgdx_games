package me.vrekt.oasis.entity.enemy.projectile;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import me.vrekt.oasis.utility.Pooling;

/**
 * Manages projectiles per entity
 */
public final class ProjectileManager {

    private final Array<Projectile> activeProjectiles = new Array<>();


    /**
     * Spawn a projectile and shoot it
     *
     * @param type   type
     * @param origin origin
     * @param target target
     * @param result result
     */
    public void spawnProjectile(ProjectileType type,
                                Vector2 origin,
                                Vector2 target,
                                ProjectileResult result) {
        final Projectile obtained = Pooling.projectile();
        obtained.load(type.data(), result);

        obtained.shoot(origin, target);
        activeProjectiles.add(obtained);
    }

    /**
     * Spawn a projectile that has a 'death' animation
     *
     * @param type      type
     * @param animation animation
     * @param origin    origin
     * @param target    target
     * @param result    result
     */
    public void spawnAnimatedProjectile(ProjectileType type,
                                        Animation<TextureRegion> animation,
                                        Vector2 origin,
                                        Vector2 target,
                                        ProjectileResult result) {
        final Projectile obtained = Pooling.projectile();
        obtained.load(type.data(), result);
        obtained.animate(animation);

        obtained.shoot(origin, target);
        activeProjectiles.add(obtained);
    }

    /**
     * Update all activate projectiles
     *
     * @param delta delta
     */
    public void update(float delta) {
        Projectile projectile;
        for (int i = activeProjectiles.size; --i >= 0; ) {
            projectile = activeProjectiles.get(i);
            if (projectile.isExpired()) {
                activeProjectiles.removeIndex(i);
                Pooling.freeProjectile(projectile);
            } else {
                projectile.update(delta);
            }
        }
    }

    /**
     * Render projectiles
     *
     * @param batch batch
     * @param delta delta
     */
    public void render(SpriteBatch batch, float delta) {
        for (Projectile projectile : activeProjectiles) {
            projectile.render(batch, delta);
        }
    }
}
