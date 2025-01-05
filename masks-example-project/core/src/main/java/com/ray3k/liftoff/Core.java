package com.ray3k.liftoff;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.tenpatch.TenPatchDrawable;

public class Core extends ApplicationAdapter {
    public static Core core;
    public static Skin skin;
    public static Stage stage;
    public static SpriteBatch spriteBatch;
    public static TenPatchDrawable background;
    public enum Mode {
        NONE("None"), GL_SCISSOR("glScissor"), SCISSOR_STACK("ScissorStack"), DEPTH_BUFFER("Depth Buffer"), DEPTH_BUFFER_SHAPE_DRAWER("Depth Buffer w/ ShapeDrawer"), BLENDING_FUNCTION("Blending Function"), PIXMAPS("Pixmaps"), SHADER("Shader"), SHADER_VIDEO("Shader w/ Video"), BLEND_FUNC_SEPARATE("BlendFuncSeparate"), BLENDING_FUNCTION_TINTING("Blending Function Tinting");
        private String name;
    
        Mode(String name) {
            this.name = name;
        }
    
        @Override
        public String toString() {
            return name;
        }
    }
    public static Mode mode = Mode.NONE;
    public static Sound chomp;
    private Test test;
    
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        
        core = this;
        skin = new Skin(Gdx.files.internal("skin.json"));
        spriteBatch = new SpriteBatch();
        stage = new Stage(new ScreenViewport(), spriteBatch);
        Gdx.input.setInputProcessor(stage);
        chomp = Gdx.audio.newSound(Gdx.files.internal("chomp.mp3"));
    
        Table root = new Table();
        root.setFillParent(true);
        root.pad(10);
        stage.addActor(root);
    
        ImageButton imageButton = new ImageButton(skin);
        root.add(imageButton).expand().top().right();
        imageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                create();
            }
        });
    
        Array<Mode> modes = new Array<>();
        modes.addAll(Mode.NONE, Mode.GL_SCISSOR, Mode.SCISSOR_STACK, Mode.DEPTH_BUFFER, Mode.DEPTH_BUFFER_SHAPE_DRAWER,
                Mode.BLENDING_FUNCTION, Mode.PIXMAPS, Mode.SHADER, Mode.SHADER_VIDEO, Mode.BLEND_FUNC_SEPARATE,
                Mode.BLENDING_FUNCTION_TINTING);
        MenuWidget menuWidget = new MenuWidget(modes);
        stage.addActor(menuWidget);
        
        TenPatchDrawable oldBackground = background;
        background = skin.get("tile-repeatable-10", TenPatchDrawable.class);
        if (oldBackground != null) {
            background.setOffsetX(oldBackground.getOffsetX());
            background.setOffsetY(oldBackground.getOffsetY());
        }
    
        switch(mode) {
            case NONE:
                test = new TestNone();
                break;
            case GL_SCISSOR:
                test = new TestGlScissor();
                break;
            case SCISSOR_STACK:
                test = new TestScissorStack();
                break;
            case DEPTH_BUFFER:
                test = new TestDepthBuffer();
                break;
            case DEPTH_BUFFER_SHAPE_DRAWER:
                test = new TestDepthBufferShapeDrawer();
                break;
            case BLENDING_FUNCTION:
                test = new TestBlendingFunction();
                break;
            case PIXMAPS:
                test = new TestPixmaps();
                break;
            case SHADER:
                test = new TestShader();
                break;
            case SHADER_VIDEO:
                test = new TestShaderVideo();
                break;
            case BLEND_FUNC_SEPARATE:
                test = new TestBlendFuncSeparate();
                break;
            case BLENDING_FUNCTION_TINTING:
                test = new TestBlendingFunctionTinting();
                break;
        }
        test.prep();
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    
    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        test.act(delta);
        test.draw();
    }
    
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}