package com.riiablo.engine.client;

import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import com.riiablo.Riiablo;
import com.riiablo.attributes.Attributes;
import com.riiablo.attributes.Stat;
import com.riiablo.camera.IsometricCamera;
import com.riiablo.codec.excel.MonStats;
import com.riiablo.engine.client.component.Hovered;
import com.riiablo.engine.server.component.AttributesWrapper;
import com.riiablo.engine.server.component.Monster;
import com.riiablo.engine.server.component.Position;
import com.riiablo.graphics.PaletteIndexedBatch;
import com.riiablo.graphics.PaletteIndexedColorDrawable;
import com.riiablo.profiler.GpuSystem;
import com.riiablo.widget.Label;

@GpuSystem
@All({Monster.class, Hovered.class, Position.class})
@Exclude(com.riiablo.engine.client.component.Label.class)
public class MonsterLabelManager extends BaseEntitySystem {
  protected ComponentMapper<Monster> mMonster;
  protected ComponentMapper<AttributesWrapper> mAttributesWrapper;

  @Wire(name = "iso")
  protected IsometricCamera iso;

  @Wire(name = "batch")
  protected PaletteIndexedBatch batch;

  private final Vector2 tmpVec2 = new Vector2();
  private final MonsterLabel monsterLabel = new MonsterLabel();

  @Override
  protected void begin() {
    monsterLabel.setVisible(false);
  }

  @Override
  protected void end() {
    if (!monsterLabel.isVisible()) return;
    batch.begin();
    tmpVec2.set(Gdx.graphics.getWidth() / 2, iso.viewportHeight * 0.05f);
    iso.unproject(tmpVec2);
    monsterLabel.setPosition(tmpVec2.x, tmpVec2.y, Align.top | Align.center);
    monsterLabel.draw(batch, 1);
    batch.end();
  }

  @Override
  protected void processSystem() {
    IntBag entities = getEntityIds();
    if (entities.size() > 0) {
      for (int i = 0, s = entities.size(); i < s; i++) {
        final int entityId = entities.get(i);
        final float percent = monsterLabel.set(entityId);
        final boolean visible = percent > 0f;
        monsterLabel.setVisible(visible);
        if (visible) break;
      }
    }
  }

  private class MonsterLabel extends Table {
    static final float HORIZONTAL_PADDING = 16;
    static final float VERTICAL_PADDING = 2;

    Table label;
    Label name;
    Label type;
    StringBuilder typeBuilder = new StringBuilder(32);
    PaletteIndexedColorDrawable background;

    MonsterLabel() {
      label = new Table();
      label.setBackground(background = new PaletteIndexedColorDrawable(
          Riiablo.colors.darkRed, Riiablo.colors.modal50) {{
        setTopHeight(VERTICAL_PADDING);
        setBottomHeight(VERTICAL_PADDING);
        setLeftWidth(HORIZONTAL_PADDING);
        setRightWidth(HORIZONTAL_PADDING);
      }});
      label.add(name = new com.riiablo.widget.Label(Riiablo.fonts.font16));
      label.pack();

      add(label).space(4).center().row();
      add(type = new Label(Riiablo.fonts.ReallyTheLastSucker)).row();
      pack();
    }

    float set(int entityId) {
      MonStats.Entry monstats = mMonster.get(entityId).monstats;
      name.setText(Riiablo.string.lookup(monstats.NameStr));
      typeBuilder.setLength(0);
      if (monstats.lUndead || monstats.hUndead) {
        typeBuilder.append(Riiablo.string.lookup("UndeadDescriptX")).append(' ');
      } else if (monstats.demon) {
        typeBuilder.append(Riiablo.string.lookup("DemonID")).append(' ');
      }

      if (!monstats.DescStr.isEmpty()) {
        typeBuilder.append(Riiablo.string.lookup(monstats.DescStr)).append(' ');
      }

      if (typeBuilder.length() > 0) typeBuilder.setLength(typeBuilder.length() - 1);
      type.setText(typeBuilder);
      //pack();

      Attributes attrs = mAttributesWrapper.get(entityId).attrs;
      final float hitpoints = attrs.get(Stat.hitpoints).asFixed();
      final float maxhp = attrs.get(Stat.maxhp).asFixed();
      return background.percent = hitpoints / maxhp;
    }
  }
}
