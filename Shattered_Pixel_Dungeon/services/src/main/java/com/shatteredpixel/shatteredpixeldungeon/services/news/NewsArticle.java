

package com.shatteredpixel.shatteredpixeldungeon.services.news;

import java.util.Date;

public class NewsArticle {

    public String title;
    public Date date;
    public String summary;

    public String URL;

    //the icon is stored as a string here so it can be decoded to an image later
    //See News.java for supported formats
    public String icon;
}
