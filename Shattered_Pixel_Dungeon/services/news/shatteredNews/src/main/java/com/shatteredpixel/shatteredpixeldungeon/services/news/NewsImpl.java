

package com.shatteredpixel.shatteredpixeldungeon.services.news;

public class NewsImpl {

    private static final NewsService newsChecker = new ShatteredNews();

    public static NewsService getNewsService() {
        return newsChecker;
    }

    public static boolean supportsNews() {
        return true;
    }
}