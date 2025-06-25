

package com.shatteredpixel.shatteredpixeldungeon.items.journal;

import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RegionLorePage {

    public static DocumentPage pageForDoc(Document doc) {
        switch (doc) {
            case SEWERS_GUARD:
            default:
                return new RegionLorePage.Sewers();
            case PRISON_WARDEN:
                return new RegionLorePage.Prison();
            case CAVES_EXPLORER:
                return new RegionLorePage.Caves();
            case CITY_WARLOCK:
                return new RegionLorePage.City();
            case HALLS_KING:
                return new RegionLorePage.Halls();
        }
    }

    public static class Sewers extends DocumentPage {
        {
            image = ItemSpriteSheet.SEWER_PAGE;
        }

        @Override
        public Document document() {
            return Document.SEWERS_GUARD;
        }
    }

    public static class Prison extends DocumentPage {
        {
            image = ItemSpriteSheet.PRISON_PAGE;
        }

        @Override
        public Document document() {
            return Document.PRISON_WARDEN;
        }
    }

    public static class Caves extends DocumentPage {
        {
            image = ItemSpriteSheet.CAVES_PAGE;
        }

        @Override
        public Document document() {
            return Document.CAVES_EXPLORER;
        }
    }

    public static class City extends DocumentPage {
        {
            image = ItemSpriteSheet.CITY_PAGE;
        }

        @Override
        public Document document() {
            return Document.CITY_WARLOCK;
        }
    }

    public static class Halls extends DocumentPage {
        {
            image = ItemSpriteSheet.HALLS_PAGE;
        }

        @Override
        public Document document() {
            return Document.HALLS_KING;
        }
    }
}
