package com.bitfire.uracer.game.actors;

import com.badlogic.gdx.math.Vector2;
import com.bitfire.uracer.game.world.GameWorld;
import com.bitfire.uracer.utils.AlgebraMath;
import com.bitfire.uracer.utils.NewConvert;

public final class CarState {

    public final Car car;
    private final float CarMaxSpeedSquared;
    private final float CarMaxForce;
    public int currTileX = -1, currTileY = -1;
    public Vector2 tilePosition = new Vector2();
    public float currVelocityLenSquared = 0;
    public float currThrottle = 0;
    public float currSpeedFactor = 0;
    public float currForceFactor = 0;
    private final GameWorld world;

    public CarState(GameWorld world, Car car) {
        this.world = world;
        this.car = car;

        if (car != null) {
            CarMaxSpeedSquared = car.getCarModel().max_speed * car.getCarModel().max_speed;
            CarMaxForce = car.getCarModel().max_force;
        } else {
            CarMaxSpeedSquared = 1;
            CarMaxForce = 1;
        }
    }

    public void dispose() {
    }

    public void reset() {
        currTileX = -1;
        currTileY = -1;
    }

    public void update(CarDescriptor carDescriptor) {
        if (carDescriptor != null) {
            updateFactors(carDescriptor);
        }

        updateTilePosition();
    }

    private void updateFactors(CarDescriptor carDescriptor) {
        currVelocityLenSquared = carDescriptor.velocityWorldCoordinates.len2();
        currThrottle = carDescriptor.throttle;
        currSpeedFactor = AlgebraMath.fixup(AlgebraMath.clamp(currVelocityLenSquared / CarMaxSpeedSquared, 0f, 1f));
        currForceFactor = AlgebraMath.fixup(AlgebraMath.clamp(currThrottle / CarMaxForce, 0f, 1f));
    }

    /*
     * Keeps track of the car's tile position and triggers a TileChanged event whenever the car's world position translates to a
     * tile index that is different from the previous one.
     */
    private void updateTilePosition() {
        tilePosition.set(world.pxToTile(NewConvert.INSTANCE.mt2px(car.getBody().getPosition().x), NewConvert.INSTANCE.mt2px(car.getBody().getPosition().y)));

        currTileX = (int) tilePosition.x;
        currTileY = (int) tilePosition.y;
    }
}
