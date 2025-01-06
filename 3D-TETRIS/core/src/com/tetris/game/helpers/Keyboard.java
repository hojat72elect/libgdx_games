package com.tetris.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.tetris.game.gameobjects.Pieces;
import com.tetris.game.gameobjects.PiecesEasy;

/**
 * Created by mkabi_000 on 8/2/2015.
 */
public class Keyboard {
    public void interact(Button sound, Button clock, Button play, AssetLoader assets, Pieces piece, boolean[][][]fixed){
        if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_UP)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.up(fixed);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_DOWN)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.down(fixed);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_LEFT)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.left(fixed);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_RIGHT)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.right(fixed);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.rotateX(fixed, !clock.isChecked());
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.rotateY(fixed, !clock.isChecked());
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.rotateZ(fixed, !clock.isChecked());
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.descend(fixed);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            clock.toggle();
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            play.toggle();
        }
    }
    public void interactEasy(Button sound, Button clock, Button play, AssetLoader assets, PiecesEasy piece, boolean[][][]fixed){
        if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_UP)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.up(fixed);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_DOWN)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.down(fixed);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_LEFT)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.left(fixed);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.DPAD_RIGHT)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.right(fixed);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.rotateX(fixed, !clock.isChecked());
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.rotateY(fixed, !clock.isChecked());
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.rotateZ(fixed, !clock.isChecked());
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            piece.descend(fixed);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            clock.toggle();
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            if (!sound.isChecked()) {
                assets.interact.play();
            }
            play.toggle();
        }
    }
}
