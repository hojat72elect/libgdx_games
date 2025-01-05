package com.nGame.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.nGame.utils.ng.Assets;

/**
 * Helper Class
 */
public class Utils {
    public static Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };


    public static class Ui {

        public static Table createTable(boolean fillParent) {
            Table table = new Table(Assets.I.skin);
            //table.debug();
            table.defaults();
            table.setFillParent(fillParent);
            return table;
        }

        public static Label createLabel(String s,Label.LabelStyle style) {
            Label label = new Label(s, style);
            label.setAlignment(Align.left, Align.center);
            return label;
        }

        public static Label createLabel(String s) {
            Label label = new Label(s, Assets.I.defaultLabelStyle);
            label.setAlignment(Align.left, Align.center);
            return label;
        }
    }
}
