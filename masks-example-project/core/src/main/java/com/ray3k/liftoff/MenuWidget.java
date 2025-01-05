package com.ray3k.liftoff;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

import static com.ray3k.liftoff.Core.*;

public class MenuWidget extends Table {
    private boolean activated;
    private static final float LEFT_PADDING = 5f;
    public TextButton textButton;
    public List<Mode> list;
    
    public MenuWidget(Array<Mode> values) {
        defaults().growX();
        textButton = new TextButton(mode.toString(), skin);
        add(textButton).minWidth(200);

        row();
        list = new List(skin);
        list.setItems(values);
        list.setSelected(mode);
        add(list);

        pack();

        setPosition(LEFT_PADDING, -list.getHeight());
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!activated) {
                    addAction(Actions.sequence(Actions.moveTo(LEFT_PADDING, 0, .75f, Interpolation.smoother), Actions.run(() -> activated = !activated)));
                } else {
                    addAction(Actions.sequence(Actions.moveTo(LEFT_PADDING, -list.getHeight(), .75f, Interpolation.smoother), Actions.run(() -> activated = !activated)));
                }
            }
        });

        list.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textButton.setText(list.getSelected().toString());
                pack();
                setX(LEFT_PADDING);
                addAction(Actions.sequence(Actions.moveTo(LEFT_PADDING, -list.getHeight(), .75f, Interpolation.smoother), Actions.run(() -> {
                    activated = !activated;
                    core.dispose();
                    mode = list.getSelected();
                    core.create();
                })));
            }
        });
    }
}
