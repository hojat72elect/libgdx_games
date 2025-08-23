package com.bitfire.uracer.game.logic.gametasks;

import com.badlogic.gdx.utils.Array;
import com.bitfire.uracer.game.events.TaskManagerEvent;
import com.bitfire.uracer.game.world.GameWorld;

/**
 * Manages the creation and destruction of the main game tasks.
 */
public final class GameTasksManager {
    private final Array<GameTask> tasks = new Array<>(10);
    /**
     * keeps track of the concrete game tasks (note that they are all publicly accessible for performance reasons)
     */

    // physics step
    public PhysicsStep physicsStep = null;
    // sound
    public SoundManager sound = null;
    // hud
    public Hud hud = null;
    // special effects
    public TrackEffects effects = null;
    private final GameWorld gameWorld;

    public GameTasksManager(GameWorld world) {
        gameWorld = world;
        createTasks();
    }

    private void createTasks() {
        // physics step
        physicsStep = new PhysicsStep(gameWorld.getBox2DWorld(), TaskManagerEvent.Order.MINUS_4);
        add(physicsStep);

        // sound manager
        sound = new SoundManager();
        add(sound);

        // hud manager
        hud = new Hud();
        add(hud);

        // effects manager
        effects = new TrackEffects();
        add(effects);
    }

    private void add(GameTask task) {
        tasks.add(task);
    }

    public void dispose() {
        for (GameTask task : tasks) {
            task.dispose();
        }
    }
}
