

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoMob;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;

public class BossHealthBar extends Component {

    private Image bar;

    private Image rawShielding;
    private Image shieldedHP;
    private Image hp;
    private BitmapText hpText;

    private Button bossInfo;
    private BuffIndicator buffs;

    private static Mob boss;

    private Image skull;
    private Emitter blood;

    private static final String asset = Assets.Interfaces.BOSSHP;

    private static BossHealthBar instance;
    private static boolean bleeding;

    public BossHealthBar() {
        super();
        visible = active = (boss != null);
        instance = this;
    }

    @Override
    public synchronized void destroy() {
        super.destroy();
        if (instance == this) instance = null;
        if (buffs != null) BuffIndicator.setBossInstance(null);
    }

    @Override
    protected void createChildren() {
        bar = new Image(asset, 0, 0, 64, 16);
        add(bar);

        width = bar.width;
        height = bar.height;

        rawShielding = new Image(asset, 15, 25, 47, 4);
        rawShielding.alpha(0.5f);
        add(rawShielding);

        shieldedHP = new Image(asset, 15, 25, 47, 4);
        add(shieldedHP);

        hp = new Image(asset, 15, 19, 47, 4);
        add(hp);

        hpText = new BitmapText(PixelScene.pixelFont);
        hpText.alpha(0.6f);
        add(hpText);

        bossInfo = new Button() {
            @Override
            protected void onClick() {
                super.onClick();
                if (boss != null) {
                    GameScene.show(new WndInfoMob(boss));
                }
            }

            @Override
            protected String hoverText() {
                if (boss != null) {
                    return boss.name();
                }
                return super.hoverText();
            }
        };
        add(bossInfo);

        if (boss != null) {
            buffs = new BuffIndicator(boss, false);
            BuffIndicator.setBossInstance(buffs);
            add(buffs);
        }

        skull = new Image(asset, 5, 18, 6, 6);
        add(skull);

        blood = new Emitter();
        blood.pos(skull);
        blood.pour(BloodParticle.FACTORY, 0.3f);
        blood.autoKill = false;
        blood.on = false;
        add(blood);
    }

    @Override
    protected void layout() {
        bar.x = x;
        bar.y = y;

        hp.x = shieldedHP.x = rawShielding.x = bar.x + 15;
        hp.y = shieldedHP.y = rawShielding.y = bar.y + 3;

        hpText.scale.set(PixelScene.align(0.5f));
        hpText.x = hp.x + 1;
        hpText.y = hp.y + (hp.height - (hpText.baseLine() + hpText.scale.y)) / 2f;
        hpText.y -= 0.001f; //prefer to be slightly higher
        PixelScene.align(hpText);

        bossInfo.setRect(x, y, bar.width, bar.height);

        if (buffs != null) {
            buffs.setRect(hp.x, hp.y + 5, 47, 8);
        }

        skull.x = bar.x + 5;
        skull.y = bar.y + 5;
    }

    @Override
    public void update() {
        super.update();
        if (boss != null) {
            if (!boss.isAlive() || !Dungeon.level.mobs.contains(boss)) {
                boss = null;
                visible = active = false;
                if (buffs != null) {
                    BuffIndicator.setBossInstance(null);
                    remove(buffs);
                    buffs.destroy();
                    buffs = null;
                }
            } else {

                int health = boss.HP;
                int shield = boss.shielding();
                int max = boss.HT;

                hp.scale.x = Math.max(0, (health - shield) / (float) max);
                shieldedHP.scale.x = health / (float) max;
                rawShielding.scale.x = shield / (float) max;

                if (bleeding != blood.on) {
                    if (bleeding) skull.tint(0xcc0000, 0.6f);
                    else skull.resetColor();
                    blood.on = bleeding;
                }

                if (shield <= 0) {
                    hpText.text(health + "/" + max);
                } else {
                    hpText.text(health + "+" + shield + "/" + max);
                }
            }
        }
    }

    public static void assignBoss(Mob boss) {
        if (BossHealthBar.boss == boss) {
            return;
        }
        BossHealthBar.boss = boss;
        bleed(false);
        if (instance != null) {
            instance.visible = instance.active = true;
            if (boss != null) {
                if (instance.buffs != null) {
                    instance.remove(instance.buffs);
                    instance.buffs.destroy();
                }
                instance.buffs = new BuffIndicator(boss, false);
                BuffIndicator.setBossInstance(instance.buffs);
                instance.add(instance.buffs);
                instance.layout();
            }
        }
    }

    public static boolean isAssigned() {
        return boss != null && boss.isAlive() && Dungeon.level.mobs.contains(boss);
    }

    public static void bleed(boolean value) {
        bleeding = value;
    }

    public static boolean isBleeding() {
        return isAssigned() && bleeding;
    }
}
