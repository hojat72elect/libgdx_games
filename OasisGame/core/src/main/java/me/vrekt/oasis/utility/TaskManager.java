package me.vrekt.oasis.utility;

import com.badlogic.gdx.utils.IntMap;

import java.util.Iterator;

import me.vrekt.oasis.GameManager;
import me.vrekt.oasis.network.utility.GameValidation;

/**
 * Handles scheduling tasks that run on the main game thread.
 */
public final class TaskManager {

    private final IntMap<Task> tasks = new IntMap<>();

    /**
     * Schedule a task
     *
     * @param task  the task
     * @param delay the delay
     */
    public int schedule(Runnable task, float delay) {
        GameValidation.ensureMainThread();

        final int id = tasks.size + 1;
        tasks.put(id, new Task(task, delay));
        return id;
    }

    /**
     * Cancel a task
     *
     * @param id the id
     */
    public void cancel(int id) {
        tasks.remove(id);
    }

    /**
     * Update task manager
     * Ideally invoke after all world update tasks are finished
     */
    public void update() {
        for (Iterator<IntMap.Entry<Task>> it = tasks.iterator(); it.hasNext(); ) {
            final IntMap.Entry<Task> task = it.next();
            if (GameManager.hasTimeElapsed(task.value.timeScheduled, task.value.delay)) {
                task.value.runnable.run();
                it.remove();
            }
        }
    }

    private static final class Task {
        private final Runnable runnable;
        private final float delay;
        private final float timeScheduled;

        public Task(Runnable runnable, float delay) {
            this.runnable = runnable;
            this.delay = delay;
            this.timeScheduled = GameManager.tick() == 0 ? 1 : GameManager.tick();
        }
    }
}
