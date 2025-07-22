package dev.lonami.klooni.interfaces;

import com.badlogic.gdx.math.Vector2;

import dev.lonami.klooni.game.Cell;

/**
 * IEffectFactory interface has to be implemented for each effect.
 * <p>
 * It tells the name and the price of the effect and will create it, when needed.
 *
 * @see IEffect
 */
public interface IEffectFactory {
    String getName();

    String getDisplay();

    int getPrice();

    IEffect create(final Cell deadCell, final Vector2 culprit);
}
