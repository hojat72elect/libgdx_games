package com.nopalsoft.zombiewars

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.Array

object Assets {
    var fontChico: BitmapFont? = null
    @JvmField
    var fontGrande: BitmapFont? = null

    @JvmField
    var map: TiledMap? = null

    /**
     * Hero
     */
    @JvmField
    var heroFarmerDie: AnimationSprite? = null
    @JvmField
    var heroFarmerHurt: Sprite? = null
    @JvmField
    var heroFarmerShoot: AnimationSprite? = null
    @JvmField
    var heroFarmerWalk: AnimationSprite? = null

    @JvmField
    var heroLumberDie: AnimationSprite? = null
    @JvmField
    var heroLumberHurt: Sprite? = null
    @JvmField
    var heroLumberShoot: AnimationSprite? = null
    @JvmField
    var heroLumberWalk: AnimationSprite? = null

    @JvmField
    var heroForceDie: AnimationSprite? = null
    @JvmField
    var heroForceHurt: Sprite? = null
    @JvmField
    var heroForceShoot: AnimationSprite? = null
    @JvmField
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
    @JvmField
    var bullet1: AnimationSprite? = null
    var bullet2: AnimationSprite? = null
    var bullet3: AnimationSprite? = null
    var bullet4: AnimationSprite? = null
    var bullet5: AnimationSprite? = null
    @JvmField
    var muzzle: AnimationSprite? = null

    /**
     * Zombies
     */
    @JvmField
    var zombieKidWalk: AnimationSprite? = null
    @JvmField
    var zombieKidAttack: AnimationSprite? = null
    @JvmField
    var zombieKidDie: AnimationSprite? = null
    @JvmField
    var zombieKidHurt: Sprite? = null

    @JvmField
    var zombiePanWalk: AnimationSprite? = null
    @JvmField
    var zombiePanAttack: AnimationSprite? = null
    @JvmField
    var zombiePanDie: AnimationSprite? = null
    @JvmField
    var zombiePanHurt: Sprite? = null

    @JvmField
    var zombieCuasyWalk: AnimationSprite? = null
    @JvmField
    var zombieCuasyAttack: AnimationSprite? = null
    @JvmField
    var zombieCuasyDie: AnimationSprite? = null
    @JvmField
    var zombieCuasyHurt: Sprite? = null

    @JvmField
    var zombieFrankWalk: AnimationSprite? = null
    @JvmField
    var zombieFrankAttack: AnimationSprite? = null
    @JvmField
    var zombieFrankDie: AnimationSprite? = null
    @JvmField
    var zombieFrankHurt: Sprite? = null

    @JvmField
    var zombieMummyWalk: AnimationSprite? = null
    @JvmField
    var zombieMummyAttack: AnimationSprite? = null
    @JvmField
    var zombieMummyDie: AnimationSprite? = null
    @JvmField
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

    @JvmStatic
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
         * Items
         */

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
    }

    private fun loadAnimationWalk(atlas: TextureAtlas, ruta: String): AnimationSprite {
        val arrSprites = Array<Sprite?>()

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

    private fun loadAnimationAttack(atlas: TextureAtlas, ruta: String): AnimationSprite {
        val arrSprites = Array<Sprite?>()

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

    private fun loadAnimationDie(atlas: TextureAtlas, ruta: String): AnimationSprite {
        val arrSprites = Array<Sprite?>()

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

    private fun loadAnimationShoot(atlas: TextureAtlas, ruta: String): AnimationSprite {
        val arrSprites = Array<Sprite?>()

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
        val arrSprites = Array<Sprite?>()

        var i = 1
        var obj: Sprite?
        do {
            obj = atlas.createSprite("Bullet/muzzle$i")
            i++
            if (obj != null) arrSprites.add(obj)
        } while (obj != null)

        val time = .009f * arrSprites.size
        return AnimationSprite(time, arrSprites)
    }

    private fun loadAnimationBullet(atlas: TextureAtlas, ruta: String): AnimationSprite {
        val arrSprites = Array<Sprite?>()

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

        map = TmxMapLoader().load("data/MapsTest/suelo.tmx")
    }
}
