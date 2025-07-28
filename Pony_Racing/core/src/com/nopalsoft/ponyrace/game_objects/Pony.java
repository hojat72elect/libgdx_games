package com.nopalsoft.ponyrace.game_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationState.AnimationStateListener;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game.TileMapHandler;

import java.util.Random;

public class Pony {
    static public final int STATE_DEAD = 0;
    static public final int STATE_WALK_LEFT = 1;
    static public final int STATE_WALK_RIGHT = 2;
    static public final int STATE_STAND = 3;
    public static final float VEL_JUMP = 3.5f;
    public static final float VEL_RUN = 2.0f;
    public static float TIEMPO_IS_HURT = 0f;
    public final float TIEMPO_IS_CHILE;
    public final float TIEMPO_IS_DULCE;
    public final Vector3 position;
    public final Vector3 aceleration;
    public int state;
    public float angleRad;
    public boolean canRotate;// Solo cuando toca el piso inclinado puede rotar
    public AnimationState animState;
    public Skeleton ponySkel;
    public boolean isJumping;
    public boolean isDoubleJump;
    public boolean isHurt;
    public boolean isChile; // Es true cuando toca un chile
    public boolean isDulce;// Es true cuando toca un dulce

    public Vector2 regresoHoyo;
    public boolean cayoEnHoyo;
    public boolean tocoElPisoDespuesCaerHoyo;

    public float stateTime;
    public float chileTime;
    public float dulceTime;
    public Random oRan;

    public String nombreSkin;

    public int monedasRecolectadas;
    public int chilesRecolectados;
    public int globosRecolectados;
    public int dulcesRecolectados;

    public int lugarEnLaCarrera; // Variable que dice si vas en primer, segundo, tercer, et. Lugar en la carrera

    public boolean pasoLaMeta;

    // Variables para saber si disparo o no
    public boolean fireBomb;
    public boolean fireWood;

    public Pony(float x, float y, String nombreSkin, TileMapHandler oWorld) {
        this.oRan = oWorld.random;

        switch (Settings.chiliLevel) {
            default:
            case 0:
                TIEMPO_IS_CHILE = 3;
                break;
            case 1:
                TIEMPO_IS_CHILE = 3.5f;
                break;
            case 2:
                TIEMPO_IS_CHILE = 5f;
                break;
            case 3:
                TIEMPO_IS_CHILE = 7f;
                break;
            case 4:
                TIEMPO_IS_CHILE = 9;
                break;
            case 5:
                TIEMPO_IS_CHILE = 11f;
                break;
        }

        switch (Settings.chocolateLevel) {
            default:
            case 0:
                TIEMPO_IS_DULCE = 3;
                break;
            case 1:
                TIEMPO_IS_DULCE = 3.5f;
                break;
            case 2:
                TIEMPO_IS_DULCE = 4f;
                break;
            case 3:
                TIEMPO_IS_DULCE = 5f;
                break;
            case 4:
                TIEMPO_IS_DULCE = 7;
                break;
            case 5:
                TIEMPO_IS_DULCE = 9f;
                break;
        }

        this.nombreSkin = nombreSkin;

        state = STATE_WALK_RIGHT;

        position = new Vector3(x, y, 0);
        aceleration = new Vector3();
        regresoHoyo = new Vector2();
        monedasRecolectadas = 0;
        stateTime = 0;
        isJumping = false;
        isDoubleJump = false;
        isHurt = false;
        pasoLaMeta = false;
        tocoElPisoDespuesCaerHoyo = true;
        ponySkel = new Skeleton(oWorld.game.assetsHandler.ponySkeletonData);
        ponySkel.setSkin(nombreSkin);
        ponySkel.setToSetupPose();

        AnimationStateData stateData = new AnimationStateData(ponySkel.getData());
        stateData.setMix("Jump", "Running", .45f);
        stateData.setMix("Jump", "standing", .45f);
        stateData.setMix("Jump2", "standing", .45f);
        stateData.setMix("Jump2", "Running", .45f);
        stateData.setMix("Jump", "Jump2", -.45f);
        stateData.setMix("hurt", "Running", .3f);
        stateData.setMix("hurt", "standing", .3f);
        // stateData.setMix("hurt", "death", .3f);
        stateData.setMix("hurt", "hurt", .3f);

        animState = new AnimationState(stateData);
        animState.addListener(new AnimationStateListener() {
            @Override
            public void event(int trackIndex, Event event) {
                // TODO Auto-generated method stub

            }

            @Override
            public void complete(int trackIndex, int loopCount) {
                // TODO Auto-generated method stub

            }

            @Override
            public void start(int trackIndex) {
                // TODO Auto-generated method stub

            }

            @Override
            public void end(int trackIndex) {
                ponySkel.setToSetupPose();
            }
        });
        animState.setAnimation(0, "standing", true);
    }

    public void update(float delta, Body obj, float accelX) {

        stateTime += delta;
        position.x = obj.getPosition().x;
        position.y = obj.getPosition().y;

        int MAX_ANGLE_DEGREES = 18;
        float angleLimitRad = (float) Math.toRadians(MAX_ANGLE_DEGREES);
        angleRad = obj.getAngle();

        if (!canRotate || isJumping)
            angleRad = 0;
        else if (angleRad > angleLimitRad)
            angleRad = angleLimitRad;
        else if (angleRad < -angleLimitRad)
            angleRad = -angleLimitRad;

        obj.setTransform(position.x, position.y, angleRad);

        aceleration.set(accelX, 0, 0);

        if (state == STATE_DEAD)
            return;

        if (isChile) {
            chileTime += delta;
            if (chileTime >= TIEMPO_IS_CHILE) {
                chileTime = 0;
                isChile = false;
            }
        }

        if (isDulce) {
            dulceTime += delta;
            if (dulceTime >= TIEMPO_IS_DULCE) {
                dulceTime = 0;
                isDulce = false;
            }
        }

        if (accelX < 0)
            state = STATE_WALK_LEFT;
        else if (accelX > 0)
            state = STATE_WALK_RIGHT;

        if (isHurt && stateTime < TIEMPO_IS_HURT)
            return;
        else if (isHurt) {
            isHurt = false;
            stateTime = 0;
        }

        if (isChile && animState.getCurrent(0).getAnimation().getName().equals("Running"))
            animState.setAnimation(0, "Runningchile", true);
        else if (isDulce && animState.getCurrent(0).getAnimation().getName().equals("Running"))
            animState.setAnimation(0, "Runningchocolate", true);
        else if (!isDulce && !isChile && (animState.getCurrent(0).getAnimation().getName().equals("Runningchile") || animState.getCurrent(0).getAnimation().getName().equals("Runningchocolate")))
            animState.setAnimation(0, "Running", true);

        if ((animState.getCurrent(0).getAnimation().getName().equals("standingchocolate") || animState.getCurrent(0).getAnimation().getName().equals("standingchile") || animState.getCurrent(0).getAnimation().getName().equals("standing") || animState.getCurrent(0).getAnimation().getName().equals("hurt")) && aceleration.x != 0) {
            if (isDulce)
                animState.setAnimation(0, "Runningchocolate", true);
            else if (isChile)
                animState.setAnimation(0, "Runningchile", true);
            else
                animState.setAnimation(0, "Running", true);
        }

        // Si no se esta moviendo a la izq o derecha, y si ya toco el suelo lo
        // pongo en stanging
        if ((animState.getCurrent(0).getAnimation().getName().equals("Runningchocolate") || animState.getCurrent(0).getAnimation().getName().equals("Running") || animState.getCurrent(0).getAnimation().getName().equals("Runningchile") || animState.getCurrent(0).getAnimation().getName().equals("hurt")) && aceleration.x == 0 && !isJumping) {
            if (isDulce)
                animState.setAnimation(0, "standingchocolate", true);
            else if (isChile)
                animState.setAnimation(0, "standingchile", true);
            else
                animState.setAnimation(0, "standing", true);
        }
    }

    public void jump() {
        // isJumping=false; //salto infinito
        if (isDoubleJump || isHurt)
            return;

        if (isJumping) {
            if (isDulce)
                animState.setAnimation(0, "Jump2chocolate", false);
            else if (isChile)
                animState.setAnimation(0, "Jump2chile", false);
            else
                animState.setAnimation(0, "Jump2", false);
        } else {
            if (isDulce)
                animState.setAnimation(0, "Jumpchocolate", false);
            else if (isChile)
                animState.setAnimation(0, "Jumpchile", false);
            else
                animState.setAnimation(0, "Jump", false);
        }
        animState.addAnimation(0, "Running", true, 0);

        if (!isJumping)
            isJumping = true;
        else if (!isDoubleJump)
            isDoubleJump = true;
    }

    public void getHurt(float tiempoIsHurt) {
        if (isHurt || isChile)
            return;

        TIEMPO_IS_HURT = tiempoIsHurt;
        stateTime = 0;
        isHurt = true;

        // Gdx.app.log("Tiempo Hurt", TIEMPO_IS_HURT+"");

        animState.setAnimation(0, "hurt", true);
    }

    public void die() {
        stateTime = 0;
        state = STATE_DEAD;
        animState.setAnimation(0, "death", false);
    }

    public void tocoElPiso() {
        isJumping = false;
        isDoubleJump = false;
        canRotate = false;
        tocoElPisoDespuesCaerHoyo = true;
    }

    public void tocoPisoInclinado() {
        tocoElPiso();
        canRotate = true;
    }

    public void cayoEnHoyo() {
        tocoElPisoDespuesCaerHoyo = false;
        cayoEnHoyo = true;
    }

    public void tocoChile() {
        chileTime = 0;
        isChile = true;
    }

    public void tocoDulce() {
        dulceTime = 0;
        isDulce = true;
    }
}
