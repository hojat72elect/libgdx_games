package com.nopalsoft.ponyrace.game_objects;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game.TileMapHandler;

public class OpponentPony extends Pony {

    private final Vector3 pastPosition;
    public boolean didTouchFlag;
    public boolean hasToJump;
    float timeSamePosition;
    private float jumpSuccessProbability = .5f;
    private float timeSinceLastShot;

    public OpponentPony(float x, float y, String skinName, TileMapHandler tileMapHandler) {
        super(x, y, skinName, tileMapHandler);
        hasToJump = false;

        switch (Settings.difficultyLevel) {
            case Settings.DIFFICULTY_EASY:
                jumpSuccessProbability = .35f;
                break;
            case Settings.DIFFICULTY_NORMAL:
                jumpSuccessProbability = .5f;
                break;
            case Settings.DIFFICULTY_HARD:
                jumpSuccessProbability = .7f;
                break;
            case Settings.DIFFICULTY_VERY_HARD:
                jumpSuccessProbability = 1f;
                break;
        }
        pastPosition = new Vector3();
    }

    public void hitSimpleJump(int newPosition) {
        if (didTouchFlag) {
            float pro = random.nextFloat();
            if (pro < jumpSuccessProbability) {
                hasToJump = true;
                state = newPosition;
            }
        } else {
            hasToJump = true;
            state = newPosition;
        }

        didTouchFlag = false;
    }

    public void hitCaminarOtraDireccion(int nuevaDireccion) {
        if (didTouchFlag) {
            float pro = random.nextFloat();
            // Gdx.app.log("PROBABILIDAD", pro + "");
            if (pro < jumpSuccessProbability) {
                state = nuevaDireccion;
            }
        } else {
            state = nuevaDireccion;
        }
        didTouchFlag = false;
    }

    @Override
    public void update(float delta, Body obj, float accelX) {
        timeSinceLastShot += delta;

        // A veces disparan en medio de un salto y la banana queda en el objeto saltar, entonces los otros ponis q tocan el cuadrito tocan la banana y se atoran
        // por eso pongo !hasToJump || !isJumping.. aun asi el problema continuaaa =(
        float TIEMPO_DISPARO = 3f;
        if (timeSinceLastShot >= TIEMPO_DISPARO && (!hasToJump || !isJumping)) {
            timeSinceLastShot -= TIEMPO_DISPARO;
            if (random.nextInt(10) < 2 && !pasoLaMeta) {
                fireWood = true;
            }
        }

        // Checo si a estado mucho tiempo en la misma posicion en caso de que si lo pongo a saltar, esto es porque a veces se atoran
        if (pastPosition.x != position.x)
            pastPosition.x = position.x;
        else {
            timeSamePosition += delta;
            if (timeSamePosition >= 2.5f) {
                timeSamePosition = 0;
                hasToJump = true;
            }
        }

        super.update(delta, obj, accelX);
    }
}
