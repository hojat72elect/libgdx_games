package com.riiablo.engine.client;

import com.artemis.ComponentMapper;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntIntMap;

import com.riiablo.Riiablo;
import com.riiablo.ai.AI;
import com.riiablo.ai.Npc;
import com.riiablo.codec.excel.LvlWarp;
import com.riiablo.codec.excel.Missiles;
import com.riiablo.codec.excel.MonStats;
import com.riiablo.codec.excel.MonStats2;
import com.riiablo.codec.excel.Objects;
import com.riiablo.codec.util.BBox;
import com.riiablo.engine.Engine;
import com.riiablo.engine.client.component.AnimationWrapper;
import com.riiablo.engine.client.component.BBoxWrapper;
import com.riiablo.engine.client.component.CofComponentDescriptors;
import com.riiablo.engine.client.component.Label;
import com.riiablo.engine.client.component.Selectable;
import com.riiablo.engine.server.ServerEntityFactory;
import com.riiablo.engine.server.component.Box2DBody;
import com.riiablo.engine.server.component.Item;
import com.riiablo.engine.server.component.Missile;
import com.riiablo.engine.server.component.Monster;
import com.riiablo.engine.server.component.SoundEmitter;
import com.riiablo.engine.server.component.Warp;
import com.riiablo.map.DT1;
import com.riiablo.map.Map;
import com.riiablo.save.CharData;

public class ClientEntityFactory extends ServerEntityFactory {
  private static final String TAG = "ClientEntityFactory";

  protected ComponentMapper<CofComponentDescriptors> mCofComponentDescriptors;
  protected ComponentMapper<AnimationWrapper> mAnimationWrapper;
  protected ComponentMapper<BBoxWrapper> mBBoxWrapper;
  protected ComponentMapper<Label> mLabel;
  protected ComponentMapper<Selectable> mSelectable;
  protected ComponentMapper<SoundEmitter> mSoundEmitter;
  protected ComponentMapper<Box2DBody> mBox2DBody;

  protected MenuManager menuManager;
  protected DialogManager dialogManager;

  @Override
  public int createPlayer(CharData charData, Vector2 position) {
    int id = super.createPlayer(charData, position);
    mCofComponentDescriptors.create(id);
    mAnimationWrapper.create(id);
    mBBoxWrapper.create(id).box = mAnimationWrapper.get(id).animation.getBox();
    mBox2DBody.create(id);
    return id;
  }

  @Override
  public int createDynamicObject(int act, int monPresetId, float x, float y) {
    return super.createDynamicObject(act, monPresetId, x, y);
  }

  @Override
  public int createStaticObject(int act, int objId, float x, float y) {
    int id = super.createStaticObject(act, objId, x, y);
    Objects.Entry base = mObject.get(id).base;

    String name;
    if ((base.SubClass & Engine.Object.SUBCLASS_WAYPOINT) == Engine.Object.SUBCLASS_WAYPOINT) {
      Map.Zone zone = map.getZone(x, y);
      mMapWrapper.create(id).set(map, zone);
      String levelName = Riiablo.string.lookup(zone.level.LevelName);
      String objectName = Riiablo.string.lookup(base.Name);
      name = String.format("%s\n%s", levelName, objectName);
    } else {
      name = base.Name.equalsIgnoreCase("dummy") ? base.Description : Riiablo.string.lookup(base.Name);
    }

    if (base.Draw) {
      mCofComponentDescriptors.create(id);
      mAnimationWrapper.create(id);
    }

    BBoxWrapper boxWrapper = mBBoxWrapper.create(id);
    if ((base.SubClass & Engine.Object.SUBCLASS_WAYPOINT) == Engine.Object.SUBCLASS_WAYPOINT) {
      BBox box = boxWrapper.box = new BBox();
      box.xMin = -70;
      box.yMin = -30;
      box.xMax = -box.xMin;
      box.yMax = -box.yMin;
      box.width = Math.abs(2 * box.xMin);
      box.height = Math.abs(2 * box.yMin);
    } else if (mAnimationWrapper.has(id)) {
      boxWrapper.box = mAnimationWrapper.get(id).animation.getBox();
    }

    Label label = mLabel.create(id);
    label.offset.y = -base.NameOffset;
    label.actor = createLabel(name);
    label.actor.setUserObject(id);

    mBox2DBody.create(id);
    return id;
  }

  @Override
  public int createMonster(int monsterId, float x, float y) {
    int id = super.createMonster(monsterId, x, y);
    Monster monster = mMonster.get(id);
    MonStats.Entry monstats = monster.monstats;
    MonStats2.Entry monstats2 = monster.monstats2;

    String name = monstats.NameStr.equalsIgnoreCase("dummy")
        ? monstats.Id : Riiablo.string.lookup(monstats.NameStr);

    mCofComponentDescriptors.create(id);

    mAnimationWrapper.create(id);
    mBBoxWrapper.create(id).box = mAnimationWrapper.get(id).animation.getBox();
    mBox2DBody.create(id);

    if (monstats.Align == 1) {
      Label label = mLabel.create(id);
      label.offset.y = monstats2.pixHeight;
      label.actor = createLabel(name);
      label.actor.setUserObject(id);
    }

    if (monstats2.isSel) mSelectable.create(id);

    AI ai = mAIWrapper.get(id).ai;
    if (ai instanceof Npc) {
      ((Npc) ai).createMenu(menuManager, dialogManager);
    }

    return id;
  }

  @Override
  public int createWarp(int index, float x, float y) {
    final int mainIndex   = DT1.Tile.Index.mainIndex(index);
    final int subIndex    = DT1.Tile.Index.subIndex(index);
    final int orientation = DT1.Tile.Index.orientation(index);

    int id = super.createWarp(index, x, y);
    Warp warp = mWarp.get(id);
    LvlWarp.Entry entry = warp.warp;

    BBox box = new BBox();
    box.xMin = entry.SelectX;
    box.yMin = entry.SelectY;
    box.width = entry.SelectDX;
    box.height = entry.SelectDY;
    box.xMax = box.width + box.xMin;
    box.yMax = box.height + box.yMin;

    String name = Riiablo.string.lookup(warp.dstLevel.LevelWarp);

    IntIntMap substs = warp.substs;
    if (entry.LitVersion) {
      // FIXME: Below will cover overwhelming majority of cases -- need to solve act 5 ice cave case where 3 tiles are used
      //        I think this can be done by checking if there's a texture with the same id, else it's a floor warp
      if (subIndex < 2) {
        for (int i = 0; i < 2; i++) {
          substs.put(DT1.Tile.Index.create(orientation, mainIndex, i), DT1.Tile.Index.create(orientation, mainIndex, i + entry.Tiles));
        }
      } else {
        substs.put(DT1.Tile.Index.create(0, subIndex, 0), DT1.Tile.Index.create(0, subIndex, 4));
        substs.put(DT1.Tile.Index.create(0, subIndex, 1), DT1.Tile.Index.create(0, subIndex, 5));
        substs.put(DT1.Tile.Index.create(0, subIndex, 2), DT1.Tile.Index.create(0, subIndex, 6));
        substs.put(DT1.Tile.Index.create(0, subIndex, 3), DT1.Tile.Index.create(0, subIndex, 7));
      }
    }

    mBBoxWrapper.create(id).box = box;

    Label label = mLabel.create(id);
    label.offset.set(box.xMin + box.width / 2, -box.yMax + box.height / 2);
    label.actor = createLabel(name);
    label.actor.setUserObject(id);

    mSelectable.create(id);
    return id;
  }

  @Override
  public int createItem(com.riiablo.item.Item item, float x, float y) {
    int id = super.createItem(item, x, y);
    Item itemWrapper = mItem.get(id);
    Riiablo.assets.load(itemWrapper.flippyDescriptor);
    /**
     * FIXME: at least some items appear to be about a half subtile too high after their drop
     *        animations finish -- is this expected? or some issue with offsets? It's happening with
     *        runes -- but keys are placed correctly and other items I've tried look fine.
     */
    itemWrapper.item.load();
    return id;
  }

  @Override
  public int createMissile(int missileId, Vector2 angle, Vector2 position) {
    int id = super.createMissile(missileId, angle, position);
    Missile missileWrapper = mMissile.get(id);
    Riiablo.assets.load(missileWrapper.missileDescriptor);
    mBox2DBody.create(id);

    Missiles.Entry missile = mMissile.get(id).missile;
    if (!missile.TravelSound.isEmpty()) { // FIXME: how to handle this audio for aoe spell effects?
//      mSoundEmitter.create(id).set(Riiablo.audio.play(missile.TravelSound, true), Interpolation.pow2OutInverse);
    }

    return id;
  }

  private static com.riiablo.widget.Label createLabel(String text) {
    com.riiablo.widget.Label label = new com.riiablo.widget.Label(Riiablo.fonts.font16);
    label.setAlignment(Align.center);
    label.getStyle().background = com.riiablo.widget.Label.MODAL;
    label.setText(text);
    return label;
  }
}
