

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentsPane;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WndHeroInfo extends WndTabbed {

    private final HeroInfoTab heroInfo;
    private final TalentInfoTab talentInfo;
    private SubclassInfoTab subclassInfo;
    private ArmorAbilityInfoTab abilityInfo;

    private static final int WIDTH = 120;
    private static final int MIN_HEIGHT = 125;
    private static final int MARGIN = 2;

    public WndHeroInfo(HeroClass cl) {

        Image tabIcon;
        switch (cl) {
            case WARRIOR:
            default:
                tabIcon = new ItemSprite(ItemSpriteSheet.SEAL, null);
                break;
            case MAGE:
                tabIcon = new ItemSprite(ItemSpriteSheet.MAGES_STAFF, null);
                break;
            case ROGUE:
                tabIcon = new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK, null);
                break;
            case HUNTRESS:
                tabIcon = new ItemSprite(ItemSpriteSheet.SPIRIT_BOW, null);
                break;
            case DUELIST:
                tabIcon = new ItemSprite(ItemSpriteSheet.RAPIER, null);
                break;
            case CLERIC:
                tabIcon = new ItemSprite(ItemSpriteSheet.ARTIFACT_TOME, null);
                break;
        }

        int finalHeight = MIN_HEIGHT;

        heroInfo = new HeroInfoTab(cl);
        add(heroInfo);
        heroInfo.setSize(WIDTH, MIN_HEIGHT);
        finalHeight = (int) Math.max(finalHeight, heroInfo.height());

        add(new IconTab(tabIcon) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                heroInfo.visible = heroInfo.active = value;
            }
        });

        talentInfo = new TalentInfoTab(cl);
        add(talentInfo);
        talentInfo.setSize(WIDTH, MIN_HEIGHT);
        finalHeight = (int) Math.max(finalHeight, talentInfo.height());

        add(new IconTab(Icons.get(Icons.TALENT)) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                talentInfo.visible = talentInfo.active = value;
            }
        });

        if (Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_2) || DeviceCompat.isDebug()) {
            subclassInfo = new SubclassInfoTab(cl);
            add(subclassInfo);
            subclassInfo.setSize(WIDTH, MIN_HEIGHT);
            finalHeight = (int) Math.max(finalHeight, subclassInfo.height());

            add(new IconTab(new ItemSprite(ItemSpriteSheet.MASK, null)) {
                @Override
                protected void select(boolean value) {
                    super.select(value);
                    subclassInfo.visible = subclassInfo.active = value;
                }
            });
        }

        if (Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_4) || DeviceCompat.isDebug()) {
            abilityInfo = new ArmorAbilityInfoTab(cl);
            add(abilityInfo);
            abilityInfo.setSize(WIDTH, MIN_HEIGHT);
            finalHeight = (int) Math.max(finalHeight, abilityInfo.height());

            add(new IconTab(new ItemSprite(ItemSpriteSheet.CROWN, null)) {
                @Override
                protected void select(boolean value) {
                    super.select(value);
                    abilityInfo.visible = abilityInfo.active = value;
                }
            });
        }

        resize(WIDTH, finalHeight);

        layoutTabs();
        talentInfo.layout();

        select(0);
    }

    @Override
    public void offset(int xOffset, int yOffset) {
        super.offset(xOffset, yOffset);
        talentInfo.layout();
    }

    private static class HeroInfoTab extends Component {

        private final RenderedTextBlock title;
        private final RenderedTextBlock[] info;
        private final Image[] icons;

        public HeroInfoTab(HeroClass cls) {
            super();
            title = PixelScene.renderTextBlock(Messages.titleCase(cls.title()), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            String[] desc_entries = cls.desc().split("\n\n");

            info = new RenderedTextBlock[desc_entries.length];

            for (int i = 0; i < desc_entries.length; i++) {
                info[i] = PixelScene.renderTextBlock(desc_entries[i], 6);
                add(info[i]);
            }

            switch (cls) {
                case WARRIOR:
                default:
                    icons = new Image[]{new ItemSprite(ItemSpriteSheet.SEAL),
                            new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD),
                            new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
                    break;
                case MAGE:
                    icons = new Image[]{new ItemSprite(ItemSpriteSheet.MAGES_STAFF),
                            new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE),
                            new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
                    break;
                case ROGUE:
                    icons = new Image[]{new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK),
                            Icons.get(Icons.STAIRS),
                            new ItemSprite(ItemSpriteSheet.DAGGER),
                            new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
                    break;
                case HUNTRESS:
                    icons = new Image[]{new ItemSprite(ItemSpriteSheet.SPIRIT_BOW),
                            Icons.GRASS.get(),
                            new ItemSprite(ItemSpriteSheet.GLOVES),
                            new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
                    break;
                case DUELIST:
                    icons = new Image[]{new ItemSprite(ItemSpriteSheet.RAPIER),
                            new ItemSprite(ItemSpriteSheet.WAR_HAMMER),
                            new ItemSprite(ItemSpriteSheet.THROWING_SPIKE),
                            new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
                    break;
                case CLERIC:
                    icons = new Image[]{new ItemSprite(ItemSpriteSheet.ARTIFACT_TOME),
                            Icons.TALENT.get(),
                            new ItemSprite(ItemSpriteSheet.CUDGEL),
                            new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
                    break;
            }
            for (Image im : icons) {
                add(im);
            }
        }

        @Override
        protected void layout() {
            super.layout();

            title.setPos((width - title.width()) / 2, MARGIN);

            float pos = title.bottom() + 4 * MARGIN;

            for (int i = 0; i < info.length; i++) {
                info[i].maxWidth((int) width - 20);
                info[i].setPos(20, pos);

                icons[i].x = (20 - icons[i].width()) / 2;
                icons[i].y = info[i].top() + (info[i].height() - icons[i].height()) / 2;
                PixelScene.align(icons[i]);

                pos = info[i].bottom() + 4 * MARGIN;
            }

            height = Math.max(height, pos - 4 * MARGIN);
        }
    }

    private static class TalentInfoTab extends Component {

        private final RenderedTextBlock title;
        private final RenderedTextBlock message;
        private final TalentsPane talentPane;

        public TalentInfoTab(HeroClass cls) {
            super();
            title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(WndHeroInfo.class, "talents")), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            message = PixelScene.renderTextBlock(Messages.get(WndHeroInfo.class, "talents_msg"), 6);
            add(message);

            ArrayList<LinkedHashMap<Talent, Integer>> talents = new ArrayList<>();
            Talent.initClassTalents(cls, talents);
            talents.get(2).clear(); //we show T3 talents with subclasses

            talentPane = new TalentsPane(TalentButton.Mode.INFO, talents);
            add(talentPane);
        }

        @Override
        protected void layout() {
            super.layout();

            title.setPos((width - title.width()) / 2, MARGIN);
            message.maxWidth((int) width);
            message.setPos(0, title.bottom() + 4 * MARGIN);

            talentPane.setRect(0, message.bottom() + 3 * MARGIN, width, 85);

            height = Math.max(height, talentPane.bottom());
        }
    }

    private static class SubclassInfoTab extends Component {

        private final RenderedTextBlock title;
        private final RenderedTextBlock message;
        private final RenderedTextBlock[] subClsDescs;
        private final IconButton[] subClsInfos;

        public SubclassInfoTab(HeroClass cls) {
            super();
            title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(WndHeroInfo.class, "subclasses")), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            message = PixelScene.renderTextBlock(Messages.get(WndHeroInfo.class, "subclasses_msg"), 6);
            add(message);

            HeroSubClass[] subClasses = cls.subClasses();

            subClsDescs = new RenderedTextBlock[subClasses.length];
            subClsInfos = new IconButton[subClasses.length];

            for (int i = 0; i < subClasses.length; i++) {
                subClsDescs[i] = PixelScene.renderTextBlock(subClasses[i].shortDesc(), 6);
                int finalI = i;
                subClsInfos[i] = new IconButton(Icons.get(Icons.INFO)) {
                    @Override
                    protected void onClick() {
                        Game.scene().addToFront(new WndInfoSubclass(cls, subClasses[finalI]));
                    }
                };
                add(subClsDescs[i]);
                add(subClsInfos[i]);
            }
        }

        @Override
        protected void layout() {
            super.layout();

            title.setPos((width - title.width()) / 2, MARGIN);
            message.maxWidth((int) width);
            message.setPos(0, title.bottom() + 4 * MARGIN);

            float pos = message.bottom() + 4 * MARGIN;

            for (int i = 0; i < subClsDescs.length; i++) {
                subClsDescs[i].maxWidth((int) width - 20);
                subClsDescs[i].setPos(0, pos);

                subClsInfos[i].setRect(width - 20, subClsDescs[i].top() + (subClsDescs[i].height() - 20) / 2, 20, 20);

                pos = subClsDescs[i].bottom() + 4 * MARGIN;
            }

            height = Math.max(height, pos - 4 * MARGIN);
        }
    }

    private static class ArmorAbilityInfoTab extends Component {

        private final RenderedTextBlock title;
        private final RenderedTextBlock message;
        private final RenderedTextBlock[] abilityDescs;
        private final IconButton[] abilityInfos;

        public ArmorAbilityInfoTab(HeroClass cls) {
            super();
            title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(WndHeroInfo.class, "abilities")), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            message = PixelScene.renderTextBlock(Messages.get(WndHeroInfo.class, "abilities_msg"), 6);
            add(message);

            ArmorAbility[] abilities = cls.armorAbilities();

            abilityDescs = new RenderedTextBlock[abilities.length];
            abilityInfos = new IconButton[abilities.length];

            for (int i = 0; i < abilities.length; i++) {
                abilityDescs[i] = PixelScene.renderTextBlock(abilities[i].shortDesc(), 6);
                int finalI = i;
                abilityInfos[i] = new IconButton(Icons.get(Icons.INFO)) {
                    @Override
                    protected void onClick() {
                        Game.scene().addToFront(new WndInfoArmorAbility(cls, abilities[finalI]));
                    }
                };
                add(abilityDescs[i]);
                add(abilityInfos[i]);
            }
        }

        @Override
        protected void layout() {
            super.layout();

            title.setPos((width - title.width()) / 2, MARGIN);
            message.maxWidth((int) width);
            message.setPos(0, title.bottom() + 4 * MARGIN);

            float pos = message.bottom() + 4 * MARGIN;

            for (int i = 0; i < abilityDescs.length; i++) {
                abilityDescs[i].maxWidth((int) width - 20);
                abilityDescs[i].setPos(0, pos);

                abilityInfos[i].setRect(width - 20, abilityDescs[i].top() + (abilityDescs[i].height() - 20) / 2, 20, 20);

                pos = abilityDescs[i].bottom() + 4 * MARGIN;
            }

            height = Math.max(height, pos - 4 * MARGIN);
        }
    }
}
