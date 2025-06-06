package com.nopalsoft.ninjarunner.leaderboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Person implements Comparable<Person> {
    public enum AccountType {
        GOOGLE_PLAY, AMAZON, FACEBOOK
    }

    public interface DownloadImageCompleteListener {
        void imageDownloaded();

        void imageDownloadFail();
    }

    public AccountType accountType;
    final public String id;
    public String name;
    public long score;
    public String urlImage;
    public TextureRegionDrawable image;

    public boolean isMe; // Indicates that this person is the user

    public Person(AccountType accountType, String id, String name, long oScore, String imageURL) {
        this.accountType = accountType;
        this.id = id;
        this.name = name;
        this.score = oScore;
        this.urlImage = imageURL;
    }

    public void downloadImage(final DownloadImageCompleteListener listener) {
        if (image != null) //Why download it again?
            return;
        HttpRequest request = new HttpRequest(HttpMethods.GET);
        request.setUrl(urlImage);
        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                final byte[] bytes = httpResponse.getResult();
                Gdx.app.postRunnable(() -> {
                    Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
                    Texture texture = new Texture(new PixmapTextureData(pixmap, pixmap.getFormat(), false, false, true));
                    pixmap.dispose();
                    image = new TextureRegionDrawable(new TextureRegion(texture));
                    if (listener != null)
                        listener.imageDownloaded();
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
        int floatPos = str.contains(".") ? str.length() - str.indexOf(".") : 0;
        int nGroups = (str.length() - floatPos - 1 - (str.contains("-") ? 1 : 0)) / 3;
        for (int i = 0; i < nGroups; i++) {
            int commaPos = str.length() - i * 4 - 3 - floatPos;
            str = str.substring(0, commaPos) + "," + str.substring(commaPos);
        }
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person objPerson) {
            return id.equals(objPerson.id) && accountType == objPerson.accountType;
        } else
            return false;
    }

    @Override
    public int compareTo(Person otherPerson) {
        return Long.compare(otherPerson.score, score);
    }
}
