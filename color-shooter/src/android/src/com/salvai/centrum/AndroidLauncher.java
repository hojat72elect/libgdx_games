package com.salvai.centrum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.salvai.centrum.utils.Text;


public class AndroidLauncher extends AndroidApplication {
    private CentrumGameClass game;
    private View gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        FrameLayout layout = new FrameLayout(this);

        View gameView = createGameView(config);
        layout.addView(gameView);
        setContentView(layout);
    }


    private View createGameView(AndroidApplicationConfiguration cfg) {
        game = new CentrumGameClass();
        gameView = initializeForView(game, cfg);
        return gameView;
    }
}
