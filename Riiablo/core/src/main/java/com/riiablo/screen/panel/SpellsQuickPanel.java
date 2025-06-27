package com.riiablo.screen.panel;

import org.apache.commons.lang3.ArrayUtils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;

import com.riiablo.CharacterClass;
import com.riiablo.Keys;
import com.riiablo.Riiablo;
import com.riiablo.attributes.StatRef;
import com.riiablo.codec.DC;
import com.riiablo.codec.DC6;
import com.riiablo.codec.excel.SkillDesc;
import com.riiablo.codec.excel.Skills;
import com.riiablo.graphics.BlendMode;
import com.riiablo.item.Item;
import com.riiablo.key.MappedKey;
import com.riiablo.key.MappedKeyStateAdapter;
import com.riiablo.loader.DC6Loader;
import com.riiablo.save.CharData;
import com.riiablo.save.ItemData;
import com.riiablo.widget.HotkeyButton;

public class SpellsQuickPanel extends Table implements Disposable, CharData.SkillListener {
  private static final String SPELLS_PATH = "data\\global\\ui\\SPELLS\\";

  final AssetDescriptor<DC6> SkilliconDescriptor = new AssetDescriptor<>("data\\global\\ui\\SPELLS\\Skillicon.DC6", DC6.class);
  DC6 Skillicon;

  final AssetDescriptor<DC6> CharSkilliconDescriptor[];
  DC6 CharSkillicon[];

  private static int getClassId(String charClass) {
    if (charClass.isEmpty()) return -1;
    switch (charClass.charAt(0)) {
      case 'a': return charClass.charAt(1) == 'm' ? CharacterClass.AMAZON.id : CharacterClass.ASSASSIN.id;
      case 'b': return CharacterClass.BARBARIAN.id;
      case 'd': return CharacterClass.DRUID.id;
      case 'n': return CharacterClass.NECROMANCER.id;
      case 'p': return CharacterClass.PALADIN.id;
      case 's': return CharacterClass.SORCERESS.id;
      default:  return -1;
    }
  }

  private DC getSkillicon(String charClass, int i) {
    int classId = getClassId(charClass);
    DC icons = classId == -1 ? Skillicon : CharSkillicon[classId];
    return i < icons.getNumPages() ? icons : null;
  }

  ObjectMap<MappedKey, HotkeyButton> keyMappings;
  MappedKeyStateAdapter mappedKeyListener;
  HotkeyButton observer;
  boolean leftSkills;
  Table[] tables;
  final float SIZE;
  final int ALIGN;
  IntMap<HotkeyButton> buttons;
  final int buttonId;

  public SpellsQuickPanel(final HotkeyButton o, final boolean leftSkills) {
    this.observer = o;
    this.leftSkills = leftSkills;
    this.buttonId = leftSkills ? Input.Buttons.LEFT : Input.Buttons.RIGHT;

    Riiablo.assets.load(SkilliconDescriptor);
    Riiablo.assets.finishLoadingAsset(SkilliconDescriptor);
    Skillicon = Riiablo.assets.get(SkilliconDescriptor);

    CharSkilliconDescriptor = new AssetDescriptor[7];
    CharSkillicon = new DC6[CharSkilliconDescriptor.length];
    for (int i = 0; i < CharSkilliconDescriptor.length; i++) {
      CharSkilliconDescriptor[i] = new AssetDescriptor<>(SPELLS_PATH + CharacterClass.get(i).spellIcons + ".DC6", DC6.class, DC6Loader.DC6Parameters.COMBINE);
      Riiablo.assets.load(CharSkilliconDescriptor[i]);
      Riiablo.assets.finishLoadingAsset(CharSkilliconDescriptor[i]);
      CharSkillicon[i] = Riiablo.assets.get(CharSkilliconDescriptor[i]);
    }

    SIZE = Gdx.app.getType() == Application.ApplicationType.Android ? 64 : 48;
    ALIGN = leftSkills ? Align.left : Align.right;

    buttons = new IntMap<>(31);
    keyMappings = new ObjectMap<>(31);
    tables = new Table[5];
    for (int i = tables.length - 1; i >= 0; i--) {
      Table table = tables[i] = new Table();
      add(table).align(ALIGN).row();
    }
    pack();
    //setDebug(true, true);

    mappedKeyListener = new MappedKeyStateAdapter() {
      @Override
      public void onPressed(MappedKey key, int keycode) {
        HotkeyButton button = keyMappings.get(key);
        if (button == null) return;
        observer.copy(button);
        Riiablo.charData.setAction(buttonId, button.getSkill());
      }
    };
    for (MappedKey Skill : Keys.Skill) Skill.addStateListener(mappedKeyListener);
    Riiablo.charData.addSkillListener(this);

    Riiablo.charData.getItems().addAlternateListener(new ItemData.AlternateListener() {
      @Override
      public void onAlternated(ItemData items, int alternate, Item LH, Item RH) {
        int skillId = Riiablo.charData.getAction(buttonId);
        HotkeyButton button = buttons.get(skillId);
        if (observer != null) observer.copy(button); // observer is null on android
      }
    });
  }

  @Override
  public void onChanged(CharData client, IntIntMap skills, Array<StatRef> chargedSkills) {
    buttons.clear();
    keyMappings.clear();
    for (Table table : tables) {
      for (Actor child : table.getChildren()) child.clear();
      table.clear();
    }
    for (StatRef chargedSkill : chargedSkills) {
      if (chargedSkill.param0() <= 0) continue; // level <= 0

      final Skills.Entry skill = Riiablo.files.skills.get(chargedSkill.param1());
      if (leftSkills && !skill.leftskill) continue;
      if (skill.passive) continue;

      final SkillDesc.Entry desc = Riiablo.files.skilldesc.get(skill.skilldesc);
      if (desc.ListRow < 0) continue;

      int ListRow = 4;
      Table table = tables[ListRow];
      int iconCel = desc.IconCel;
      DC icons = getSkillicon(skill.charclass, iconCel);
      if (icons == null) {
        icons = Skillicon;
        iconCel = 20;
      }
      final HotkeyButton button = new HotkeyButton(icons, iconCel, skill.Id, chargedSkill);
      if (skill.aura) {
        button.setBlendMode(BlendMode.DARKEN, Riiablo.colors.darkenGold);
      }

      int index = Riiablo.charData.getHotkey(buttonId, chargedSkill.param1());
      if (index != ArrayUtils.INDEX_NOT_FOUND) {
        MappedKey mapping = Keys.Skill[index];
        button.map(mapping);
        keyMappings.put(mapping, button);
      }

      button.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          observer.copy(button);
          Riiablo.charData.setAction(buttonId, button.getSkill());
          SpellsQuickPanel.this.setVisible(false);
        }
      });
      table.add(button).size(SIZE);
      buttons.put(button.getSkill(), button);
    }
    for (IntIntMap.Entry skillId : skills) {
      if (skillId.value <= 0) continue; // level <= 0

      final Skills.Entry skill = Riiablo.files.skills.get(skillId.key);
      if (leftSkills && !skill.leftskill) continue;
      if (skill.passive) continue;

      final SkillDesc.Entry desc = Riiablo.files.skilldesc.get(skill.skilldesc);
      if (desc.ListRow < 0) continue;

      Table table = tables[desc.ListRow];
      int iconCel = desc.IconCel;
      DC icons = getSkillicon(skill.charclass, iconCel);
      if (icons == null) {
        icons = Skillicon;
        iconCel = 20;
      }
      final HotkeyButton button = new HotkeyButton(icons, iconCel, skill.Id);
      if (skill.aura) {
        button.setBlendMode(BlendMode.DARKEN, Riiablo.colors.darkenGold);
      }

      int index = Riiablo.charData.getHotkey(buttonId, skillId.key);
      if (index != ArrayUtils.INDEX_NOT_FOUND) {
        MappedKey mapping = Keys.Skill[index];
        button.map(mapping);
        keyMappings.put(mapping, button);
      }

      button.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          observer.copy(button);
          Riiablo.charData.setAction(buttonId, button.getSkill());
          SpellsQuickPanel.this.setVisible(false);
        }
      });
      table.add(button).size(SIZE);
      buttons.put(button.getSkill(), button);
    }
    float x = getX(ALIGN);
    float y = getY();
    pack();
    setPosition(x, y, Align.bottom | ALIGN);
  }

  public void setObserver(HotkeyButton observer) {
    this.observer = observer;
  }

  @Override
  public void dispose() {
    for (MappedKey Skill : Keys.Skill) Skill.removeStateListener(mappedKeyListener);
    Riiablo.assets.unload(SkilliconDescriptor.fileName);
    for (AssetDescriptor assetDescriptor : CharSkilliconDescriptor) Riiablo.assets.unload(assetDescriptor.fileName);
  }
}
