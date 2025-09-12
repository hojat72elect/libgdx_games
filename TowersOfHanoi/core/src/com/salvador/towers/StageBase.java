package com.salvador.towers;

import static com.salvador.towers.Tool.Values.DISC_HEIGHT;
import static com.salvador.towers.Tool.Values.SCREEN_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.salvador.towers.Objects.Disc;
import com.salvador.towers.Objects.RectBase;

import java.util.ArrayList;
import java.util.Stack;

public class StageBase {

    Stack<Disc> stackOne;
    Stack<Disc> stackTwo;
    Stack<Disc> stackThree;
    private final Stage stage;
    private final RectBase rectBase;
    private final RectBase rectUp1;
    private final RectBase rectUp2;
    private final RectBase rectUp3;
    private final int Disc = 12;
    private final int baseHeight = 15;
    private final ArrayList<Disc> listDiscs;
    private final float posRectOne;
    private final float posRectTwo;
    private final float posRectThree;
    private final float posY;
    private final Main main;

    public StageBase(Main main, Stage stage) {
        this.stage = stage;
        this.main = main;

        posY = (SCREEN_WIDTH / 2) - 280;
        posRectOne = (SCREEN_WIDTH / 2) - (600 / 4);
        posRectTwo = (SCREEN_WIDTH / 2);
        posRectThree = (SCREEN_WIDTH / 2) + (600 / 4);

        rectBase = new RectBase(posY, posY, 600, baseHeight);
        rectUp1 = new RectBase(posRectOne - (Disc / 2), posY, Disc, 130);
        rectUp2 = new RectBase(posRectTwo - (Disc / 2), posY, Disc, 130);
        rectUp3 = new RectBase(posRectThree - (Disc / 2), posY, Disc, 130);

        stage.addActor(rectBase);
        stage.addActor(rectUp1);
        stage.addActor(rectUp2);
        stage.addActor(rectUp3);

        listDiscs = new ArrayList<>();

        stackOne = new Stack<>();
        stackTwo = new Stack<>();
        stackThree = new Stack<>();
    }

    public void createDiscs(int num) {

        for (int i = 0; i < num; i++) {
            float width = 100 - (10 * i);
            Disc disc = new Disc(posRectOne - (width / 2), posY + baseHeight + (DISC_HEIGHT * i), width);

            String path = "colors/color" + (i + 1) + ".png";
            disc.setTexture(new Texture(Gdx.files.internal(path)));
            putDisc(disc, 1);
        }
    }

    public void removeDiscs() {
        while (stackOne.size() > 0) {
            stackOne.pop().remove();
        }

        while (stackTwo.size() > 0) {
            stackTwo.pop().remove();
        }
        while (stackThree.size() > 0) {
            stackThree.pop().remove();
        }
    }

    public void makeMove(int f, int t) {

        Disc from = null;

        switch (f) {
            case 1:
                from = stackOne.pop();
                break;
            case 2:
                from = stackTwo.pop();
                break;
            case 3:
                from = stackThree.pop();
                break;
        }
        SequenceAction sequenceAction;
        switch (t) {
            case 1:
                stackOne.push(from);

                sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.moveTo(from.getX(), posY + 150, 0.5f));
                sequenceAction.addAction(Actions.moveTo(posRectOne - (from.getWidth() / 2), posY + 150, 0.5f));
                sequenceAction.addAction(Actions.moveTo(posRectOne - (from.getWidth() / 2), posY + baseHeight + (DISC_HEIGHT * (stackOne.size() - 1)), 0.5f));
                sequenceAction.addAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        main.nextMovePlay();
                        return true;
                    }
                });
                from.addAction(sequenceAction);

                break;
            case 2:
                stackTwo.push(from);

                sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.moveTo(from.getX(), posY + 150, 0.5f));
                sequenceAction.addAction(Actions.moveTo(posRectTwo - (from.getWidth() / 2), posY + 150, 0.5f));
                sequenceAction.addAction(Actions.moveTo(posRectTwo - (from.getWidth() / 2), posY + baseHeight + (DISC_HEIGHT * (stackTwo.size() - 1)), 0.5f));
                sequenceAction.addAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        main.nextMovePlay();
                        return true;
                    }
                });

                from.addAction(sequenceAction);
                break;
            case 3:
                stackThree.push(from);
                sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.moveTo(from.getX(), posY + 150, 0.5f));
                sequenceAction.addAction(Actions.moveTo(posRectThree - (from.getWidth() / 2), posY + 150, 0.5f));
                sequenceAction.addAction(Actions.moveTo(posRectThree - (from.getWidth() / 2), posY + baseHeight + (DISC_HEIGHT * (stackThree.size() - 1)), 0.5f));
                sequenceAction.addAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        main.nextMovePlay();
                        return true;
                    }
                });

                from.addAction(sequenceAction);
                break;
        }
    }

    public void putDisc(Disc disc, int t) {
        switch (t) {
            case 1:
                stackOne.push(disc);
                break;
            case 2:
                stackTwo.push(disc);
                break;
            case 3:
                stackThree.push(disc);
                break;
        }
        stage.addActor(disc);
    }
}
