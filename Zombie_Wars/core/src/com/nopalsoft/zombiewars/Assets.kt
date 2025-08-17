package com.nopalsoft.zombiewars

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.Array


object Assets {
    var fontChico: BitmapFont? = null
    var fontGrande: BitmapFont? = null
    var map: TiledMap? = null

    /**
     * Hero
     */
    var heroFarmerDie: AnimationSprite? = null
    var heroFarmerHurt: Sprite? = null
    var heroFarmerShoot: AnimationSprite? = null
    var heroFarmerWalk: AnimationSprite? = null

    var heroLumberDie: AnimationSprite? = null
    var heroLumberHurt: Sprite? = null
    var heroLumberShoot: AnimationSprite? = null
    var heroLumberWalk: AnimationSprite? = null

    var heroForceDie: AnimationSprite? = null
    var heroForceHurt: Sprite? = null
    var heroForceShoot: AnimationSprite? = null
    var heroForceWalk: AnimationSprite? = null

    var heroRamboDie: AnimationSprite? = null
    var heroRamboHurt: Sprite? = null
    var heroRamboShoot: AnimationSprite? = null
    var heroRamboWalk: AnimationSprite? = null

    var heroSoldierDie: AnimationSprite? = null
    var heroSoldierHurt: Sprite? = null
    var heroSoldierShoot: AnimationSprite? = null
    var heroSoldierWalk: AnimationSprite? = null

    var heroSwatDie: AnimationSprite? = null
    var heroSwatHurt: Sprite? = null
    var heroSwatShoot: AnimationSprite? = null
    var heroSwatWalk: AnimationSprite? = null

    var heroVaderDie: AnimationSprite? = null
    var heroVaderHurt: Sprite? = null
    var heroVaderShoot: AnimationSprite? = null
    var heroVaderWalk: AnimationSprite? = null

    /**
     * Bullet
     */
    var bullet1: AnimationSprite? = null
    var bullet2: AnimationSprite? = null
    var bullet3: AnimationSprite? = null
    var bullet4: AnimationSprite? = null
    var bullet5: AnimationSprite? = null
    var muzzle: AnimationSprite? = null

    /**
     * Zombies
     */
    var zombieKidWalk: AnimationSprite? = null
    var zombieKidAttack: AnimationSprite? = null
    var zombieKidDie: AnimationSprite? = null
    var zombieKidHurt: Sprite? = null

    var zombiePanWalk: AnimationSprite? = null
    var zombiePanAttack: AnimationSprite? = null
    var zombiePanDie: AnimationSprite? = null
    var zombiePanHurt: Sprite? = null

    var zombieCuasyWalk: AnimationSprite? = null
    var zombieCuasyAttack: AnimationSprite? = null
    var zombieCuasyDie: AnimationSprite? = null
    var zombieCuasyHurt: Sprite? = null

    var zombieFrankWalk: AnimationSprite? = null
    var zombieFrankAttack: AnimationSprite? = null
    var zombieFrankDie: AnimationSprite? = null
    var zombieFrankHurt: Sprite? = null

    var zombieMummyWalk: AnimationSprite? = null
    var zombieMummyAttack: AnimationSprite? = null
    var zombieMummyDie: AnimationSprite? = null
    var zombieMummyHurt: Sprite? = null

    var labelStyleChico: LabelStyle? = null
    var labelStyleGrande: LabelStyle? = null

    var pixelNegro: NinePatchDrawable? = null

    fun loadStyles(atlas: TextureAtlas) {
        // Label Style
        labelStyleChico = LabelStyle(fontChico, Color.WHITE)
        labelStyleGrande = LabelStyle(fontGrande, Color.WHITE)

        pixelNegro = NinePatchDrawable(NinePatch(atlas.findRegion("UI/pixelNegro"), 1, 1, 0, 0))
    }

    fun load() {
        val atlas = TextureAtlas(Gdx.files.internal("data/atlasMap.txt"))

        fontChico = BitmapFont(Gdx.files.internal("data/fontChico.fnt"), atlas.findRegion("fontChico"))
        fontGrande = BitmapFont(Gdx.files.internal("data/fontGrande.fnt"), atlas.findRegion("fontGrande"))

        loadStyles(atlas)

        /*
         * Bullets
         */
        bullet1 = loadAnimationBullet(atlas, "Bullet/bullet1")
        bullet2 = loadAnimationBullet(atlas, "Bullet/bullet2")
        bullet3 = loadAnimationBullet(atlas, "Bullet/bullet3")
        bullet4 = loadAnimationBullet(atlas, "Bullet/bullet4")
        bullet5 = loadAnimationBullet(atlas, "Bullet/bullet5")
        muzzle = loadAnimationMuzzle(atlas)


        /*
         * HeroFarmer
         */
        heroFarmerDie = loadAnimationDie(atlas, "HeroFarmer/")
        heroFarmerHurt = atlas.createSprite("HeroFarmer/hurt")
        heroFarmerShoot = loadAnimationShoot(atlas, "HeroFarmer/")
        heroFarmerWalk = loadAnimationWalk(atlas, "HeroFarmer/")

        /*
         * HeroLumber
         */
        heroLumberDie = loadAnimationDie(atlas, "HeroLumber/")
        heroLumberHurt = atlas.createSprite("HeroLumber/hurt")
        heroLumberShoot = loadAnimationAttack(atlas, "HeroLumber/")
        heroLumberWalk = loadAnimationWalk(atlas, "HeroLumber/")

        /*
         * HeroForce
         */
        heroForceDie = loadAnimationDie(atlas, "HeroForce/")
        heroForceHurt = atlas.createSprite("HeroForce/hurt")
        heroForceShoot = loadAnimationShoot(atlas, "HeroForce/")
        heroForceWalk = loadAnimationWalk(atlas, "HeroForce/")

        /*
         * HeroRambo
         */
        heroRamboDie = loadAnimationDie(atlas, "HeroRambo/")
        heroRamboHurt = atlas.createSprite("HeroRambo/hurt")
        heroRamboShoot = loadAnimationShoot(atlas, "HeroRambo/")
        heroRamboWalk = loadAnimationWalk(atlas, "HeroRambo/")

        /*
         * HeroSoldier
         */
        heroSoldierDie = loadAnimationDie(atlas, "HeroSoldier/")
        heroSoldierHurt = atlas.createSprite("HeroSoldier/hurt")
        heroSoldierShoot = loadAnimationShoot(atlas, "HeroSoldier/")
        heroSoldierWalk = loadAnimationWalk(atlas, "HeroSoldier/")

        /*
         * HeroSwat
         */
        heroSwatDie = loadAnimationDie(atlas, "HeroSwat/")
        heroSwatHurt = atlas.createSprite("HeroSwat/hurt")
        heroSwatShoot = loadAnimationShoot(atlas, "HeroSwat/")
        heroSwatWalk = loadAnimationWalk(atlas, "HeroSwat/")

        /*
         * HeroVader
         */
        heroVaderDie = loadAnimationDie(atlas, "HeroVader/")
        heroVaderHurt = atlas.createSprite("HeroVader/hurt")
        heroVaderShoot = loadAnimationShoot(atlas, "HeroVader/")
        heroVaderWalk = loadAnimationWalk(atlas, "HeroVader/")

        /*
         * Zombie kid
         */
        zombieKidWalk = loadAnimationWalk(atlas, "ZombieKid/")
        zombieKidAttack = loadAnimationAttack(atlas, "ZombieKid/")
        zombieKidDie = loadAnimationDie(atlas, "ZombieKid/")
        zombieKidHurt = atlas.createSprite("ZombieKid/die1")

        /*
         * Zombie pan
         */
        zombiePanWalk = loadAnimationWalk(atlas, "ZombiePan/")
        zombiePanAttack = loadAnimationAttack(atlas, "ZombiePan/")
        zombiePanDie = loadAnimationDie(atlas, "ZombiePan/")
        zombiePanHurt = atlas.createSprite("ZombiePan/die1")

        /*
         * Zombie Cuasy
         */
        zombieCuasyWalk = loadAnimationWalk(atlas, "ZombieCuasy/")
        zombieCuasyAttack = loadAnimationAttack(atlas, "ZombieCuasy/")
        zombieCuasyDie = loadAnimationDie(atlas, "ZombieCuasy/")
        zombieCuasyHurt = atlas.createSprite("ZombieCuasy/die1")

        /*
         * Zombie Frank
         */
        zombieFrankWalk = loadAnimationWalk(atlas, "ZombieFrank/")
        zombieFrankAttack = loadAnimationAttack(atlas, "ZombieFrank/")
        zombieFrankDie = loadAnimationDie(atlas, "ZombieFrank/")
        zombieFrankHurt = atlas.createSprite("ZombieFrank/die1")

        /*
         * Zombie mummy
         */
        zombieMummyWalk = loadAnimationWalk(atlas, "ZombieMummy/")
        zombieMummyAttack = loadAnimationAttack(atlas, "ZombieMummy/")
        zombieMummyDie = loadAnimationDie(atlas, "ZombieMummy/")
        zombieMummyHurt = atlas.createSprite("ZombieMummy/die1")

        Settings.load()
    }

    private fun loadAnimationWalk(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "walk" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .009f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationAttack(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "attack" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .01875f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationDie(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "die" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .03f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationShoot(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + "shoot" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .0095f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationMuzzle(atlas: TextureAtlas): AnimationSprite {
        val arrSprites = Array<Sprite>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite("Bullet/" + "muzzle" + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .009f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationBullet(atlas: TextureAtlas, ruta: String?): AnimationSprite {
        val arrSprites = Array<Sprite>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite(ruta + i)
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .03f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    fun loadTiledMap() {
        if (map != null) {
            map!!.dispose()
            map = null
        }

        if (Settings.isTest) {
            map = TmxMapLoader().load("data/MapsTest/suelo.tmx")
        } else {
            map = AtlasTmxMapLoader().load("data/Maps/suelo.tmx")
        }
    }
}
