package com.salvai.snake.utils;

import com.badlogic.gdx.Gdx;

public class UrlController {

    public static void openRate() {
        Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.salvai.snake");
    }

    public static void openWebsite() {
        Gdx.net.openURI("https://simondalvai.com");
    }
}
