package com.bitfire.uracer.utils;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bitfire.uracer.URacer;
import com.bitfire.uracer.resources.Art;

public final class UIUtils {

    public static final boolean debug = false;

    private UIUtils() {
    }

    public static Stage newFittedStage() {
        return new Stage(new FitViewport(ScaleUtils.PlayWidth, ScaleUtils.PlayHeight));
    }

    public static TextButton newTextButton(String text, ClickListener listener) {
        TextButton btn = new TextButton(text, Art.scrSkin);
        btn.addListener(listener);
        return btn;
    }

    public static CheckBox newCheckBox(String text, boolean checked, ChangeListener listener) {
        CheckBox cb = new CheckBox(" " + text, Art.scrSkin);
        cb.setChecked(checked);
        if (listener != null) {
            cb.addListener(listener);
        }
        return cb;
    }

    public static SelectBox<String> newSelectBox(String[] items, ChangeListener listener) {
        SelectBox<String> sb = new SelectBox<>(Art.scrSkin);
        if (listener != null) {
            sb.addListener(listener);
        }

        sb.setItems(items);
        return sb;
    }

    public static List<String> newListBox(String[] items) {
        return newListBox(items, null);
    }

    public static List<String> newListBox(String[] items, ChangeListener listener) {
        List<String> list = new List<>(Art.scrSkin);
        if (listener != null) {
            list.addListener(listener);
        }

        list.setItems(items);
        return list;
    }

    public static Slider newSlider(float min, float max, float step, float value, ChangeListener listener) {
        Slider s = new Slider(min, max, step, false, Art.scrSkin);
        s.setValue(value);
        if (listener != null) {
            s.addListener(listener);
        }
        return s;
    }

    public static Label newLabel(String text, boolean wrap) {
        Label l = new Label(text, Art.scrSkin);
        l.setWrap(wrap);
        return l;
    }

    public static ScrollPane newScrollPane() {
        return new ScrollPane(null, Art.scrSkin);
    }

    public static TextButton newButton(String text, ClickListener listener) {
        TextButton b = new TextButton(text, Art.scrSkin);
        if (listener != null) {
            b.addListener(listener);
        }
        return b;
    }

    public static Table newTable() {
        Table t = new Table();
        if (debug) {
            t.debug();
        }
        return t;
    }

    public static Window newWindow(String title) {
        Window w = new Window(title, Art.scrSkin);
        if (debug) {
            w.debug();
        }
        return w;
    }

    public static Label newVersionInfoLabel() {
        return UIUtils.newLabel("uRacer " + URacer.versionInfo, false);
    }
}
