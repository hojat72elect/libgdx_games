

package com.shatteredpixel.shatteredpixeldungeon.services.news;

import java.util.ArrayList;

public abstract class NewsService {

    public static abstract class NewsResultCallback {
        public abstract void onArticlesFound(ArrayList<NewsArticle> articles);

        public abstract void onConnectionFailed();
    }

    public abstract void checkForArticles(boolean useMetered, boolean forceHTTPS, NewsResultCallback callback);
}
