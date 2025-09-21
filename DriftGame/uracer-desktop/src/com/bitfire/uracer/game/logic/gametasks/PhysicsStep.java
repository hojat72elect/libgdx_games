package com.bitfire.uracer.game.logic.gametasks;

import com.badlogic.gdx.physics.box2d.World;
import com.bitfire.uracer.configuration.PhysicsUtils;
import com.bitfire.uracer.game.GameEvents;
import com.bitfire.uracer.game.events.PhysicsStepEvent.Type;
import com.bitfire.uracer.game.events.TaskManagerEvent;

public class PhysicsStep extends GameTask {
    private final World world;

    public PhysicsStep(World world, TaskManagerEvent.Order order) {
        super(order);
        this.world = world;
    }

    @Override
    public void dispose() {
        super.dispose();
        GameEvents.physicsStep.removeAllListeners();
    }

    @Override
    protected void onTick() {
        GameEvents.physicsStep.trigger(this, Type.OnBeforeTimestep);
        world.step(PhysicsUtils.Dt, 10, 10);
        GameEvents.physicsStep.trigger(this, Type.OnAfterTimestep);
    }

    @Override
    protected void onTickCompleted() {
        world.clearForces();
        GameEvents.physicsStep.trigger(this, Type.OnSubstepCompleted);
    }

    @Override
    public void onGameReset() {
        world.clearForces();
    }

    @Override
    public void onGameRestart() {
        onGameReset();
    }
}
