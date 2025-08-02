package com.nopalsoft.ponyrace.game_objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game.TileMapHandler;

public class Wood extends BaseGameObject {
    public static final float TIEMPO_NORMAL = 8f;
    public final float TIEMPO_HURT;
    public float lastStatetime;
    public float stateTime;
    public float angulo;
    public State state;
    public Tipo tipo;
    public Pony ponyTirador;// El pony que tiro este wood

    public Wood(float x, float y, Pony ponyTirador, TileMapHandler oWorld) {
        super(x, y);
        stateTime = 0;
        lastStatetime = stateTime;
        state = State.normal;
        if (oWorld.random.nextBoolean())
            tipo = Tipo.platano;
        else
            tipo = Tipo.tachuela;

        switch (Settings.woodLevel) {

            case 1:
                TIEMPO_HURT = 2.5f;
                break;
            case 2:
                TIEMPO_HURT = 2.7f;
                break;
            case 3:
                TIEMPO_HURT = 3f;
                break;
            case 4:
                TIEMPO_HURT = 3.25f;
                break;
            case 5:
                TIEMPO_HURT = 3.5f;
                break;
            case 0:
            default:
                TIEMPO_HURT = 2;
                break;
        }
        this.ponyTirador = ponyTirador;
    }

    public void update(float delta, Body obj) {
        lastStatetime = stateTime;
        stateTime += delta;

        position.x = obj.getPosition().x;
        position.y = obj.getPosition().y;
        angulo = MathUtils.radiansToDegrees * obj.getAngle();

        if (state == State.normal && stateTime >= TIEMPO_NORMAL) {
            state = State.hit;
            stateTime = 0;
        }
    }

    public void hitByPony(Body obj) {
        if (state == State.normal) {
            state = State.hit;
            stateTime = 0;
            obj.setLinearVelocity(0, 0);
        }
    }

    public enum Tipo {
        platano, tachuela
    }

    public enum State {
        normal, hit
    }
}
