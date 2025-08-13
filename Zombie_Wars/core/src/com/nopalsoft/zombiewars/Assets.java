package com.nopalsoft.zombiewars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;

public class Assets {

    public static BitmapFont fontChico;
    public static BitmapFont fontGrande;

    public static TiledMap map;

    /**
     * Hero
     */
    public static AnimationSprite heroFarmerDie;
    public static Sprite heroFarmerHurt;
    public static AnimationSprite heroFarmerShoot;
    public static AnimationSprite heroFarmerWalk;

    public static AnimationSprite heroLumberDie;
    public static Sprite heroLumberHurt;
    public static AnimationSprite heroLumberShoot;
    public static AnimationSprite heroLumberWalk;

    public static AnimationSprite heroForceDie;
    public static Sprite heroForceHurt;
    public static AnimationSprite heroForceShoot;
    public static AnimationSprite heroForceWalk;

    public static AnimationSprite heroRamboDie;
    public static Sprite heroRamboHurt;
    public static AnimationSprite heroRamboShoot;
    public static AnimationSprite heroRamboWalk;

    public static AnimationSprite heroSoldierDie;
    public static Sprite heroSoldierHurt;
    public static AnimationSprite heroSoldierShoot;
    public static AnimationSprite heroSoldierWalk;

    public static AnimationSprite heroSwatDie;
    public static Sprite heroSwatHurt;
    public static AnimationSprite heroSwatShoot;
    public static AnimationSprite heroSwatWalk;

    public static AnimationSprite heroVaderDie;
    public static Sprite heroVaderHurt;
    public static AnimationSprite heroVaderShoot;
    public static AnimationSprite heroVaderWalk;

    /**
     * Bullet
     */
    public static AnimationSprite bullet1;
    public static AnimationSprite bullet2;
    public static AnimationSprite bullet3;
    public static AnimationSprite bullet4;
    public static AnimationSprite bullet5;
    public static AnimationSprite muzzle;

    /**
     * Zombies
     */
    public static AnimationSprite zombieKidWalk;
    public static AnimationSprite zombieKidAttack;
    public static AnimationSprite zombieKidDie;
    public static Sprite zombieKidHurt;

    public static AnimationSprite zombiePanWalk;
    public static AnimationSprite zombiePanAttack;
    public static AnimationSprite zombiePanDie;
    public static Sprite zombiePanHurt;

    public static AnimationSprite zombieCuasyWalk;
    public static AnimationSprite zombieCuasyAttack;
    public static AnimationSprite zombieCuasyDie;
    public static Sprite zombieCuasyHurt;

    public static AnimationSprite zombieFrankWalk;
    public static AnimationSprite zombieFrankAttack;
    public static AnimationSprite zombieFrankDie;
    public static Sprite zombieFrankHurt;

    public static AnimationSprite zombieMummyWalk;
    public static AnimationSprite zombieMummyAttack;
    public static AnimationSprite zombieMummyDie;
    public static Sprite zombieMummyHurt;

    public static LabelStyle labelStyleChico;
    public static LabelStyle labelStyleGrande;

    public static NinePatchDrawable pixelNegro;

    public static Sound shoot1;
    public static Sound zombiePan;
    public static Sound zombieKid;
    public static Sound zombieCuasy;
    public static Sound zombieMummy;
    public static Sound zombieFrank;

    public static Sound hurt1;
    public static Sound hurt2;
    public static Sound hurt3;
    public static Sound gem;
    public static Sound skull;
    public static Sound jump;
    public static Sound shield;
    public static Sound hearth;

    public static void loadStyles(TextureAtlas atlas) {
        // Label Style
        labelStyleChico = new LabelStyle(fontChico, Color.WHITE);
        labelStyleGrande = new LabelStyle(fontGrande, Color.WHITE);

        pixelNegro = new NinePatchDrawable(new NinePatch(atlas.findRegion("UI/pixelNegro"), 1, 1, 0, 0));
    }

    public static void load() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        fontChico = new BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("fontChico"));
        fontGrande = new BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"));

        loadStyles(atlas);

        /**
         * Bullets
         */
        bullet1 = loadAnimationBullet(atlas, "Bullet/bullet1");
        bullet2 = loadAnimationBullet(atlas, "Bullet/bullet2");
        bullet3 = loadAnimationBullet(atlas, "Bullet/bullet3");
        bullet4 = loadAnimationBullet(atlas, "Bullet/bullet4");
        bullet5 = loadAnimationBullet(atlas, "Bullet/bullet5");
        muzzle = loadAnimationMuzzle(atlas, "Bullet/");
        /**
         * Items
         */

        /**
         * HeroFarmer
         */
        heroFarmerDie = loadAnimationDie(atlas, "HeroFarmer/");
        heroFarmerHurt = atlas.createSprite("HeroFarmer/hurt");
        heroFarmerShoot = loadAnimationShoot(atlas, "HeroFarmer/");
        heroFarmerWalk = loadAnimationWalk(atlas, "HeroFarmer/");

        /**
         * HeroLumber
         */
        heroLumberDie = loadAnimationDie(atlas, "HeroLumber/");
        heroLumberHurt = atlas.createSprite("HeroLumber/hurt");
        heroLumberShoot = loadAnimationAttack(atlas, "HeroLumber/");
        heroLumberWalk = loadAnimationWalk(atlas, "HeroLumber/");

        /**
         * HeroForce
         */
        heroForceDie = loadAnimationDie(atlas, "HeroForce/");
        heroForceHurt = atlas.createSprite("HeroForce/hurt");
        heroForceShoot = loadAnimationShoot(atlas, "HeroForce/");
        heroForceWalk = loadAnimationWalk(atlas, "HeroForce/");

        /**
         * HeroRambo
         */
        heroRamboDie = loadAnimationDie(atlas, "HeroRambo/");
        heroRamboHurt = atlas.createSprite("HeroRambo/hurt");
        heroRamboShoot = loadAnimationShoot(atlas, "HeroRambo/");
        heroRamboWalk = loadAnimationWalk(atlas, "HeroRambo/");

        /**
         * HeroSoldier
         */
        heroSoldierDie = loadAnimationDie(atlas, "HeroSoldier/");
        heroSoldierHurt = atlas.createSprite("HeroSoldier/hurt");
        heroSoldierShoot = loadAnimationShoot(atlas, "HeroSoldier/");
        heroSoldierWalk = loadAnimationWalk(atlas, "HeroSoldier/");

        /**
         * HeroSwat
         */
        heroSwatDie = loadAnimationDie(atlas, "HeroSwat/");
        heroSwatHurt = atlas.createSprite("HeroSwat/hurt");
        heroSwatShoot = loadAnimationShoot(atlas, "HeroSwat/");
        heroSwatWalk = loadAnimationWalk(atlas, "HeroSwat/");

        /**
         * HeroVader
         */
        heroVaderDie = loadAnimationDie(atlas, "HeroVader/");
        heroVaderHurt = atlas.createSprite("HeroVader/hurt");
        heroVaderShoot = loadAnimationShoot(atlas, "HeroVader/");
        heroVaderWalk = loadAnimationWalk(atlas, "HeroVader/");

        /**
         * Zombie kid
         */
        zombieKidWalk = loadAnimationWalk(atlas, "ZombieKid/");
        zombieKidAttack = loadAnimationAttack(atlas, "ZombieKid/");
        zombieKidDie = loadAnimationDie(atlas, "ZombieKid/");
        zombieKidHurt = atlas.createSprite("ZombieKid/die1");

        /**
         * Zombie pan
         */
        zombiePanWalk = loadAnimationWalk(atlas, "ZombiePan/");
        zombiePanAttack = loadAnimationAttack(atlas, "ZombiePan/");
        zombiePanDie = loadAnimationDie(atlas, "ZombiePan/");
        zombiePanHurt = atlas.createSprite("ZombiePan/die1");

        /**
         * Zombie Cuasy
         */
        zombieCuasyWalk = loadAnimationWalk(atlas, "ZombieCuasy/");
        zombieCuasyAttack = loadAnimationAttack(atlas, "ZombieCuasy/");
        zombieCuasyDie = loadAnimationDie(atlas, "ZombieCuasy/");
        zombieCuasyHurt = atlas.createSprite("ZombieCuasy/die1");

        /**
         * Zombie Frank
         */
        zombieFrankWalk = loadAnimationWalk(atlas, "ZombieFrank/");
        zombieFrankAttack = loadAnimationAttack(atlas, "ZombieFrank/");
        zombieFrankDie = loadAnimationDie(atlas, "ZombieFrank/");
        zombieFrankHurt = atlas.createSprite("ZombieFrank/die1");

        /**
         * Zombie mummy
         */
        zombieMummyWalk = loadAnimationWalk(atlas, "ZombieMummy/");
        zombieMummyAttack = loadAnimationAttack(atlas, "ZombieMummy/");
        zombieMummyDie = loadAnimationDie(atlas, "ZombieMummy/");
        zombieMummyHurt = atlas.createSprite("ZombieMummy/die1");

        Settings.load();

        // shoot1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/shoot2.mp3"));
        // zombiePan = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombiePan.mp3"));
        // zombieKid = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieKid.mp3"));
        // zombieCuasy = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieCuasy.mp3"));
        // zombieMummy = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieMummy.mp3"));
        // zombieFrank = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/zombieFrank.mp3"));
        //
        // hurt1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hurt.mp3"));
        // hurt2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hurt2.mp3"));
        // hurt3 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hurt3.mp3"));
        //
        // gem = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/gem.mp3"));
        // skull = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/skull.mp3"));
        // jump = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/jump.mp3"));
        // shield = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/pick.mp3"));
        // hearth = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hearth.mp3"));

        // 2

    }

    private static AnimationSprite loadAnimationWalk(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + "walk" + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .009f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    private static AnimationSprite loadAnimationAttack(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + "attack" + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .01875f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    private static AnimationSprite loadAnimationDie(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + "die" + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .03f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    private static AnimationSprite loadAnimationShoot(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + "shoot" + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .0095f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    private static AnimationSprite loadAnimationMuzzle(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + "muzzle" + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .009f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    private static AnimationSprite loadAnimationBullet(TextureAtlas atlas, String ruta) {
        Array<Sprite> arrSprites = new Array<Sprite>();

        int i = 1;
        Sprite obj = null;
        do {
            obj = atlas.createSprite(ruta + i);
            i++;
            if (obj != null)
                arrSprites.add(obj);
        } while (obj != null);

        float time = .03f * arrSprites.size;
        return new AnimationSprite(time, arrSprites);
    }

    public static void loadTiledMap(int numMap) {
        if (map != null) {
            map.dispose();
            map = null;
        }

        if (Settings.isTest) {
            map = new TmxMapLoader().load("data/MapsTest/suelo.tmx");
        } else {
            map = new AtlasTmxMapLoader().load("data/Maps/suelo.tmx");
        }
    }

    public static void playSound(Sound sonido, float volume) {
        if (Settings.isSoundOn) {
            sonido.play(volume);
        }
    }
}
