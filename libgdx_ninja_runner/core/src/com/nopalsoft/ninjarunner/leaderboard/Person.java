package com.nopalsoft.ninjarunner.leaderboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Person implements Comparable<Person> {
    public enum TipoCuenta {
        GOOGLE_PLAY, AMAZON, FACEBOOK;
    }

    public interface DownloadImageCompleteListener {
        public void imageDownloaded();

        public void imageDownloadFail();
    }

    DownloadImageCompleteListener listener;
    public TipoCuenta tipoCuenta;
    final public String id;
    public String name;
    public long score;
    public String urlImagen;
    public TextureRegionDrawable imagen;

    public boolean isMe;// Indica que esta persona es el usuario

    public Person(TipoCuenta tipoCuenta, String id, String name, long oScore, String imagenURL) {
        this.tipoCuenta = tipoCuenta;
        this.id = id;
        this.name = name;
        this.score = oScore;
        this.urlImagen = imagenURL;

    }

    public void downloadImage(final DownloadImageCompleteListener listener) {
        if (imagen != null)//Pa que bajarla otra vez
            return;
        HttpRequest request = new HttpRequest(HttpMethods.GET);
        request.setUrl(urlImagen);
        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                final byte[] bytes = httpResponse.getResult();
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
                        Texture texture = new Texture(new PixmapTextureData(pixmap, pixmap.getFormat(), false, false, true));
                        pixmap.dispose();
                        imagen = new TextureRegionDrawable(new TextureRegion(texture));
                        if (listener != null)
                            listener.imageDownloaded();
                    }
                });
            }

            @Override
            public void failed(Throwable t) {
                if (listener != null)
                    listener.imageDownloadFail();
                Gdx.app.log("EmptyDownloadTest", "Failed", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("EmptyDownloadTest", "Cancelled");
            }
        });
    }

    // see: http://stackoverflow.com/a/15329259/3479489
    public String getScoreWithFormat() {
        String str = String.valueOf(score);
        int floatPos = str.indexOf(".") > -1 ? str.length() - str.indexOf(".") : 0;
        int nGroups = (str.length() - floatPos - 1 - (str.indexOf("-") > -1 ? 1 : 0)) / 3;
        for (int i = 0; i < nGroups; i++) {
            int commaPos = str.length() - i * 4 - 3 - floatPos;
            str = str.substring(0, commaPos) + "," + str.substring(commaPos, str.length());
        }
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            Person objPerson = (Person) obj;
            if (id.equals(objPerson.id) && tipoCuenta == objPerson.tipoCuenta)
                return true;
            else
                return false;

        } else
            return false;
    }

    @Override
    public int compareTo(Person o) {
        if (score > o.score)
            return -1;
        else if (score < o.score)
            return 1;
        else
            return 0;
    }

    public void updateDatos(String _name, long _score) {
        name = _name;
        score = _score;

    }

    /**
     * Revisa el bitmap font para ver si contiene todos los chars del nombre de esta persona
     *
     * @param font
     * @return Verdadero si el font contiene todos los chars de lo contrario falso
     */
    public boolean checkIfBitMapFontContainsAllCharsInName(BitmapFont font) {
        boolean contiene = true;
        for (char charAux : name.toCharArray()) {
            if (!font.getData().hasGlyph(charAux)) {
                contiene = false;
                break;
            }
        }
        return contiene;
    }

}
