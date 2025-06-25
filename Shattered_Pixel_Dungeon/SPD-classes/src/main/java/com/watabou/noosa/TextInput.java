

package com.watabou.noosa;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.watabou.glscripts.Script;
import com.watabou.glwrap.Blending;
import com.watabou.glwrap.Quad;
import com.watabou.glwrap.Texture;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Point;

//essentially contains a libGDX text input field, plus a PD-rendered background
public class TextInput extends Component {

    private final Stage stage;
    private final Container container;
    private final TextField textField;

    private final Skin skin;

    private final NinePatch bg;

    public TextInput(NinePatch bg, boolean multiline, int size) {
        super();
        this.bg = bg;
        add(bg);

        //use a custom viewport here to ensure stage camera matches game camera
        Viewport viewport = new Viewport() {
        };
        viewport.setWorldSize(Game.width, Game.height);
        viewport.setScreenBounds(0, Game.bottomInset, Game.width, Game.height);
        viewport.setCamera(new OrthographicCamera());
        stage = new Stage(viewport);
        Game.inputHandler.addInputProcessor(stage);

        container = new Container<TextField>();
        stage.addActor(container);
        container.setTransform(true);

        skin = new Skin(FileUtils.getFileHandle(Files.FileType.Internal, "gdx/textfield.json"));

        TextField.TextFieldStyle style = skin.get(TextField.TextFieldStyle.class);
        style.font = Game.platform.getFont(size, "", false, false);
        style.background = null;
        if (multiline) {
            textField = new TextArea("", style) {
                @Override
                public void cut() {
                    super.cut();
                    onClipBoardUpdate();
                }

                @Override
                public void copy() {
                    super.copy();
                    onClipBoardUpdate();
                }
            };
        } else {
            textField = new TextField("", style) {
                @Override
                public void cut() {
                    super.cut();
                    onClipBoardUpdate();
                }

                @Override
                public void copy() {
                    super.copy();
                    onClipBoardUpdate();
                }
            };
        }
        textField.setProgrammaticChangeEvents(true);

        if (!multiline) textField.setAlignment(Align.center);

        textField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BitmapFont f = Game.platform.getFont(size, textField.getText(), false, false);
                TextField.TextFieldStyle style = textField.getStyle();
                if (f != style.font) {
                    style.font = f;
                    textField.setStyle(style);
                }
                onChanged();
            }
        });

        if (!multiline) {
            textField.setTextFieldListener(new TextField.TextFieldListener() {
                public void keyTyped(TextField textField, char c) {
                    if (c == '\r' || c == '\n') {
                        enterPressed();
                    }
                }
            });
        }

        textField.setOnscreenKeyboard(new TextField.OnscreenKeyboard() {
            @Override
            public void show(boolean visible) {
                Game.platform.setOnscreenKeyboardVisible(visible);
            }
        });

        container.setActor(textField);
        stage.setKeyboardFocus(textField);
        Game.platform.setOnscreenKeyboardVisible(true);
    }

    public void enterPressed() {
        //fires any time enter is pressed, do nothing by default
    }

    public void onChanged() {
        //fires any time the text box is changed, do nothing by default
    }

    public void onClipBoardUpdate() {
        //fires any time the clipboard is updated via cut or copy, do nothing by default
    }

    public void setText(String text) {
        textField.setText(text);
        textField.setCursorPosition(textField.getText().length());
    }

    public void setMaxLength(int maxLength) {
        textField.setMaxLength(maxLength);
    }

    public String getText() {
        return textField.getText();
    }

    public void copyToClipboard() {
        if (textField.getSelection().isEmpty()) {
            textField.selectAll();
        }

        textField.copy();
    }

    public void pasteFromClipboard() {
        String contents = Gdx.app.getClipboard().getContents();
        if (contents == null) return;

        if (!textField.getSelection().isEmpty()) {
            //just use cut, but override clipboard
            textField.cut();
            Gdx.app.getClipboard().setContents(contents);
        }

        String existing = textField.getText();
        int cursorIdx = textField.getCursorPosition();

        textField.setText(existing.substring(0, cursorIdx) + contents + existing.substring(cursorIdx));
        textField.setCursorPosition(cursorIdx + contents.length());
    }

    @Override
    protected void layout() {
        super.layout();

        float contX = x;
        float contY = y;
        float contW = width;
        float contH = height;

        if (bg != null) {
            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            contX += bg.marginLeft();
            contY += bg.marginTop();
            contW -= bg.marginHor();
            contH -= bg.marginVer();
        }

        float zoom = Camera.main.zoom;
        Camera c = camera();
        if (c != null) {
            zoom = c.zoom;
            Point p = c.cameraToScreen(contX, contY);
            contX = p.x / zoom;
            contY = p.y / zoom;
        }

        container.align(Align.topLeft);
        container.setPosition(contX * zoom, (Game.height - (contY * zoom)));
        container.size(contW * zoom, contH * zoom);
    }

    @Override
    public void update() {
        super.update();
        stage.act(Game.elapsed);
    }

    @Override
    public void draw() {
        super.draw();
        Quad.releaseIndices();
        Script.unuse();
        Texture.clear();
        stage.draw();
        Quad.bindIndices();
        Blending.useDefault();
    }

    @Override
    public synchronized void destroy() {
        super.destroy();
        if (stage != null) {
            stage.dispose();
            skin.dispose();
            Game.inputHandler.removeInputProcessor(stage);
            Game.platform.setOnscreenKeyboardVisible(false);
            if (!DeviceCompat.isDesktop()) Game.platform.updateSystemUI();
        }
    }
}
