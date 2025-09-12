package com.salvador.towers;

import static com.salvador.towers.Tool.Values.SCREEN_HIGHT;
import static com.salvador.towers.Tool.Values.SCREEN_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.salvador.towers.Objects.Background;
import com.salvador.towers.Objects.ButtonUI;

public class StageUI {

    public Stage stage;

    public Background background;
    public Main main;

    public ButtonUI btnPrev;
    public ButtonUI btnPlay;
    public ButtonUI btnNext;

    public ButtonUI btnAddDisc;
    public ButtonUI btnRemoveDisc;

    private boolean isPlaying = false;

    private final Texture txePlaying;
    private final Texture txePause;

    public StageUI(final Main main, Stage stage) {
        this.stage = stage;
        this.main = main;
        background = new Background(SCREEN_WIDTH * 2, SCREEN_HIGHT);
        background.setTexture(new Texture(Gdx.files.internal("background.png")));
        stage.addActor(background);

        btnPrev = new ButtonUI(((SCREEN_WIDTH) / 2) - (60 / 2) - 150, 20, 60, 60);
        btnPlay = new ButtonUI(((SCREEN_WIDTH) / 2) - (60 / 2), 20, 60, 60);
        btnNext = new ButtonUI(((SCREEN_WIDTH) / 2) - (60 / 2) + 150, 20, 60, 60);

        btnAddDisc = new ButtonUI(100, 270, 50, 50);
        btnRemoveDisc = new ButtonUI(100, 170, 50, 50);

        btnPrev.setTexture(new Texture(Gdx.files.internal("btnPrev.png")));
        btnNext.setTexture(new Texture(Gdx.files.internal("btnNext.png")));

        txePlaying = new Texture(Gdx.files.internal("btnPlay.png"));
        txePause = new Texture(Gdx.files.internal("btnPause.png"));

        btnPlay.setTexture(txePlaying);

        btnAddDisc.setTexture(new Texture(Gdx.files.internal("btnAdd.png")));
        btnRemoveDisc.setTexture(new Texture(Gdx.files.internal("btnRemove.png")));

        stage.addActor(btnPrev);
        stage.addActor(btnPlay);
        stage.addActor(btnNext);

        stage.addActor(btnAddDisc);
        stage.addActor(btnRemoveDisc);

        btnPrev.setOnClickListenner(new ButtonUI.OnClickListenner() {
            @Override
            public void onClick() {
                main.prevMove();
            }
        });

        btnPlay.setOnClickListenner(new ButtonUI.OnClickListenner() {
            @Override
            public void onClick() {
                if (!isPlaying) {
                    main.startPlay();
                    btnPlay.setTexture(txePause);
                } else {
                    btnPlay.setTexture(txePlaying);
                }
                isPlaying = !isPlaying;
            }
        });

        btnNext.setOnClickListenner(new ButtonUI.OnClickListenner() {
            @Override
            public void onClick() {
                main.nextMove();
            }
        });

        btnAddDisc.setOnClickListenner(new ButtonUI.OnClickListenner() {
            @Override
            public void onClick() {
                main.addDisc();
            }
        });

        btnRemoveDisc.setOnClickListenner(new ButtonUI.OnClickListenner() {
            @Override
            public void onClick() {
                main.removeDisc();
            }
        });
    }
}
