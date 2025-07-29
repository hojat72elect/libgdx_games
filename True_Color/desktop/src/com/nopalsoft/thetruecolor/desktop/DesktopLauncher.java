package com.nopalsoft.thetruecolor.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.nopalsoft.thetruecolor.MainTheTrueColor;
import com.nopalsoft.thetruecolor.handlers.HandlerGWT;


public class DesktopLauncher {
    public static MainTheTrueColor game;

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 480;
        config.height = 800;
        game = new MainTheTrueColor( handlerGWT);
        new LwjglApplication(game, config);
    }

    static HandlerGWT handlerGWT = new HandlerGWT() {
        @Override
        public void getTextureFromFacebook(String url, final OnTextureLoaded onTextureLoaded) {
            Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
            request.setUrl(url);
            Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    final byte[] bytes = httpResponse.getResult();
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
                            Texture texture = new Texture(new PixmapTextureData(pixmap, pixmap.getFormat(), false, false, true));
                            pixmap.dispose();
                            onTextureLoaded.onTextureLoaded(texture);
                        }
                    });
                }

                @Override
                public void failed(Throwable t) {
                    Gdx.app.log("EmptyDownloadTest", "Failed", t);
                }

                @Override
                public void cancelled() {
                    Gdx.app.log("EmptyDownloadTest", "Cancelled");
                }
            });
        }
    };
}
