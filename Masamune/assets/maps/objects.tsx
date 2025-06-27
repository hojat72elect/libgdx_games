<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.0" name="objects" tilewidth="50" tileheight="50" tilecount="39" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="PlayerObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="combatActions" propertytype="ActionType" value="ATTACK_SINGLE,FIREBOLT,USE_ITEM"/>
   <property name="objType" propertytype="MapObjectType" value="HERO"/>
   <property name="speed" type="float" value="5"/>
   <property name="stats" type="class" propertytype="CharStats">
    <properties>
     <property name="agility" type="float" value="3"/>
     <property name="arcaneStrike" type="float" value="0.1"/>
     <property name="baseLife" type="float" value="30"/>
     <property name="baseMana" type="float" value="20"/>
     <property name="constitution" type="float" value="2"/>
     <property name="criticalStrike" type="float" value="0.1"/>
     <property name="intelligence" type="float" value="3"/>
     <property name="strength" type="float" value="3"/>
    </properties>
   </property>
  </properties>
  <image source="objects/hero.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="2" type="FixtureDefinition" x="2" y="13" width="12" height="3">
    <ellipse/>
   </object>
   <object id="4" type="FixtureDefinition" x="-8" y="-8" width="32" height="32">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="interact"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="2" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="ELDER"/>
   <property name="triggerName" value="elder"/>
  </properties>
  <image source="objects/elder.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="1" y="12" width="14" height="4">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="3" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="FLOWER_GIRL"/>
   <property name="triggerName" value="flower_girl"/>
  </properties>
  <image source="objects/flower_girl.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="2" y="13" width="12" height="3">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="4" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="MERCHANT"/>
   <property name="triggerName" value="merchant"/>
  </properties>
  <image source="objects/merchant.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="2" y="12" width="12" height="4">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="5" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="SMITH"/>
   <property name="triggerName" value="smith"/>
  </properties>
  <image source="objects/smith.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="2" y="13" width="12" height="3">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="6" type="EnemyObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="combatActions" propertytype="ActionType" value="ATTACK_SINGLE,FIREBOLT"/>
   <property name="level" type="int" value="1"/>
   <property name="objType" propertytype="MapObjectType" value="BUTTERFLY"/>
   <property name="stats" type="class" propertytype="CharStats">
    <properties>
     <property name="agility" type="float" value="2"/>
     <property name="baseLife" type="float" value="8"/>
     <property name="baseMana" type="float" value="5"/>
     <property name="damage" type="float" value="3"/>
    </properties>
   </property>
   <property name="talons" type="int" value="25"/>
   <property name="xp" type="int" value="15"/>
  </properties>
  <image source="objects/butterfly.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="0" y="1" width="16" height="15">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="7" type="EnemyObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="combatActions" propertytype="ActionType" value="ATTACK_SINGLE"/>
   <property name="level" type="int" value="1"/>
   <property name="objType" propertytype="MapObjectType" value="LARVA"/>
   <property name="stats" type="class" propertytype="CharStats">
    <properties>
     <property name="agility" type="float" value="2"/>
     <property name="baseLife" type="float" value="10"/>
     <property name="damage" type="float" value="4"/>
     <property name="resistance" type="float" value="20"/>
    </properties>
   </property>
   <property name="talons" type="int" value="30"/>
   <property name="xp" type="int" value="12"/>
  </properties>
  <image source="objects/larva.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="1" y="0" width="14" height="15">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="8" type="EnemyObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="combatActions" propertytype="ActionType" value="ATTACK_SINGLE"/>
   <property name="level" type="int" value="2"/>
   <property name="objType" propertytype="MapObjectType" value="MUSHROOM"/>
   <property name="stats" type="class" propertytype="CharStats">
    <properties>
     <property name="agility" type="float" value="5"/>
     <property name="armor" type="float" value="10"/>
     <property name="baseLife" type="float" value="6"/>
     <property name="damage" type="float" value="6"/>
     <property name="physicalEvade" type="float" value="0.1"/>
    </properties>
   </property>
   <property name="talons" type="int" value="33"/>
   <property name="xp" type="int" value="17"/>
  </properties>
  <image source="objects/mushroom.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="0" y="0" width="16" height="16">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="9" type="ItemObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="WEAPON"/>
   <property name="itemType" propertytype="ItemType" value="ELDER_SWORD"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="constitution" type="float" value="5"/>
     <property name="damage" type="float" value="3"/>
     <property name="intelligence" type="float" value="1"/>
     <property name="lifeMax" type="float" value="0"/>
     <property name="manaMax" type="float" value="0"/>
    </properties>
   </property>
  </properties>
  <image source="objects/elder_sword.png" width="6" height="15"/>
 </tile>
 <tile id="10" type="PropObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
  </properties>
  <image source="objects/tree_green.png" width="48" height="48"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="14" y="36" width="20" height="12"/>
  </objectgroup>
 </tile>
 <tile id="11" type="ItemObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="BOOTS"/>
   <property name="cost" type="int" value="150"/>
   <property name="itemType" propertytype="ItemType" value="BOOTS"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="armor" type="float" value="3"/>
    </properties>
   </property>
  </properties>
  <image source="objects/boots.png" width="16" height="16"/>
 </tile>
 <tile id="12" type="ItemObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="HELMET"/>
   <property name="cost" type="int" value="150"/>
   <property name="itemType" propertytype="ItemType" value="HELMET"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="armor" type="float" value="3"/>
    </properties>
   </property>
  </properties>
  <image source="objects/helmet.png" width="16" height="16"/>
 </tile>
 <tile id="13" type="ItemObject">
  <properties>
   <property name="action" propertytype="ActionType" value="ITEM_MANA_RESTORE"/>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="OTHER"/>
   <property name="consumableType" propertytype="ConsumableType" value="COMBAT_AND_INVENTORY"/>
   <property name="cost" type="int" value="20"/>
   <property name="itemType" propertytype="ItemType" value="SMALL_MANA_POTION"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="mana" type="float" value="15"/>
    </properties>
   </property>
  </properties>
  <image source="objects/small_mana_potion.png" width="9" height="11"/>
 </tile>
 <tile id="14" type="ItemObject">
  <properties>
   <property name="action" propertytype="ActionType" value="SCROLL_INFERNO"/>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="OTHER"/>
   <property name="consumableType" propertytype="ConsumableType" value="COMBAT_ONLY"/>
   <property name="cost" type="int" value="100"/>
   <property name="itemType" propertytype="ItemType" value="SCROLL_INFERNO"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="damage" type="float" value="20"/>
    </properties>
   </property>
  </properties>
  <image source="objects/scroll_inferno.png" width="16" height="16"/>
 </tile>
 <tile id="15" type="ItemObject">
  <properties>
   <property name="action" propertytype="ActionType" value="ITEM_HEALTH_RESTORE"/>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="OTHER"/>
   <property name="consumableType" propertytype="ConsumableType" value="COMBAT_AND_INVENTORY"/>
   <property name="cost" type="int" value="10"/>
   <property name="itemType" propertytype="ItemType" value="SMALL_HEALTH_POTION"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="life" type="float" value="30"/>
    </properties>
   </property>
  </properties>
  <image source="objects/small_health_potion.png" width="9" height="11"/>
 </tile>
 <tile id="16" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="TRIGGER"/>
   <property name="triggerName" value="terealis_flower"/>
  </properties>
  <image source="objects/terealis_plant.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="0" y="3" width="16" height="13">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="17" type="ItemObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="QUEST"/>
   <property name="itemType" propertytype="ItemType" value="TEREALIS_FLOWER"/>
  </properties>
  <image source="objects/terealis_plant.png" width="16" height="16"/>
 </tile>
 <tile id="18" type="ItemObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="consumableType" propertytype="ConsumableType" value="INVENTORY_ONLY"/>
   <property name="itemType" propertytype="ItemType" value="SMALL_STRENGTH_POTION"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="strength" type="float" value="3"/>
    </properties>
   </property>
  </properties>
  <image source="objects/small_strength_potion.png" width="9" height="11"/>
 </tile>
 <tile id="19" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="MAN_GREEN"/>
   <property name="triggerName" value="man_green"/>
  </properties>
  <image source="objects/man_green.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="1" y="12" width="14" height="4">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="20" type="ItemObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="consumableType" propertytype="ConsumableType" value="INVENTORY_ONLY"/>
   <property name="itemType" propertytype="ItemType" value="INTELLIGENCE_POTION"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="intelligence" type="float" value="5"/>
     <property name="manaMax" type="float" value="10"/>
    </properties>
   </property>
  </properties>
  <image source="objects/intelligence_potion.png" width="9" height="11"/>
 </tile>
 <tile id="21" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="TRIGGER"/>
   <property name="triggerName" value="masamune_forest"/>
  </properties>
  <image source="objects/masamune_forest.png" width="35" height="33"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="0" y="17" width="25" height="16">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="22" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="DEMON_FIRE"/>
  </properties>
  <image source="objects/demon_fire.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="1" y="12" width="14" height="4">
    <properties>
     <property name="density" type="float" value="50000"/>
     <property name="isSensor" type="bool" value="false"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="23" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="DEMON_SPIRIT"/>
  </properties>
  <image source="objects/demon_spirit.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="1" y="12" width="14" height="4">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="24" type="EnemyObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="behavior" value="cyclops"/>
   <property name="combatActions" propertytype="ActionType" value="ATTACK_SINGLE,SLOW,DEMI1,REGENERATE1,DOUBLE_STRIKE"/>
   <property name="level" type="int" value="5"/>
   <property name="objType" propertytype="MapObjectType" value="CYCLOPS"/>
   <property name="stats" type="class" propertytype="CharStats">
    <properties>
     <property name="agility" type="float" value="2"/>
     <property name="armor" type="float" value="10"/>
     <property name="baseLife" type="float" value="120"/>
     <property name="baseMana" type="float" value="120"/>
     <property name="damage" type="float" value="14"/>
     <property name="physicalEvade" type="float" value="0.1"/>
     <property name="resistance" type="float" value="10"/>
    </properties>
   </property>
   <property name="talons" type="int" value="500"/>
   <property name="xp" type="int" value="250"/>
  </properties>
  <image source="objects/cyclops.png" width="50" height="50"/>
 </tile>
 <tile id="25" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="CYCLOPS_NPC"/>
  </properties>
  <image source="objects/cyclops_npc.png" width="16" height="16"/>
 </tile>
 <tile id="26" type="EnemyObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="behavior" value="support"/>
   <property name="combatActions" propertytype="ActionType" value="ATTACK_SINGLE,HEAL"/>
   <property name="level" type="int" value="3"/>
   <property name="objType" propertytype="MapObjectType" value="AXOLOT"/>
   <property name="stats" type="class" propertytype="CharStats">
    <properties>
     <property name="agility" type="float" value="3"/>
     <property name="baseLife" type="float" value="12"/>
     <property name="baseMana" type="float" value="7"/>
     <property name="damage" type="float" value="5"/>
     <property name="physicalEvade" type="float" value="0.2"/>
    </properties>
   </property>
   <property name="talons" type="int" value="40"/>
   <property name="xp" type="int" value="25"/>
  </properties>
  <image source="objects/axolot.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="0" y="0" width="16" height="15">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="27" type="EnemyObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="combatActions" propertytype="ActionType" value="ATTACK_SINGLE,SLOW"/>
   <property name="level" type="int" value="2"/>
   <property name="objType" propertytype="MapObjectType" value="SLIME"/>
   <property name="stats" type="class" propertytype="CharStats">
    <properties>
     <property name="agility" type="float" value="2"/>
     <property name="baseLife" type="float" value="8"/>
     <property name="damage" type="float" value="4"/>
     <property name="physicalEvade" type="float" value="0.5"/>
    </properties>
   </property>
   <property name="talons" type="int" value="20"/>
   <property name="xp" type="int" value="39"/>
  </properties>
  <image source="objects/slime.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="0" y="6" width="16" height="9">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="28" type="EnemyObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="behavior" value="spider"/>
   <property name="combatActions" propertytype="ActionType" value="ATTACK_SINGLE,POISON_ATTACK"/>
   <property name="level" type="int" value="2"/>
   <property name="objType" propertytype="MapObjectType" value="SPIDER"/>
   <property name="stats" type="class" propertytype="CharStats">
    <properties>
     <property name="agility" type="float" value="3"/>
     <property name="baseLife" type="float" value="9"/>
     <property name="damage" type="float" value="5"/>
     <property name="resistance" type="float" value="40"/>
    </properties>
   </property>
   <property name="talons" type="int" value="27"/>
   <property name="xp" type="int" value="17"/>
  </properties>
  <image source="objects/spider.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="0" y="2" width="16" height="13">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="29" type="ItemObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="ARMOR"/>
   <property name="cost" type="int" value="150"/>
   <property name="itemType" propertytype="ItemType" value="STUDDED_LEATHER"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="armor" type="float" value="3"/>
    </properties>
   </property>
  </properties>
  <image source="objects/studded_leather.png" width="16" height="16"/>
 </tile>
 <tile id="30" type="ItemObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="ARMOR"/>
   <property name="cost" type="int" value="250"/>
   <property name="itemType" propertytype="ItemType" value="CHAIN_MAIL"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="armor" type="float" value="5"/>
    </properties>
   </property>
  </properties>
  <image source="objects/chain_mail.png" width="32" height="32"/>
 </tile>
 <tile id="31" type="ItemObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="ARMOR"/>
   <property name="cost" type="int" value="250"/>
   <property name="itemType" propertytype="ItemType" value="MAGIC_VEST"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="armor" type="float" value="3"/>
     <property name="magicalEvade" type="float" value="0.1"/>
     <property name="resistance" type="float" value="10"/>
    </properties>
   </property>
  </properties>
  <image source="objects/magic_vest.png" width="32" height="32"/>
 </tile>
 <tile id="32" type="ItemObject">
  <properties>
   <property name="action" propertytype="ActionType" value="HEAL"/>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="WEAPON"/>
   <property name="cost" type="int" value="400"/>
   <property name="itemType" propertytype="ItemType" value="SWORD_HEAL"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="damage" type="float" value="6"/>
     <property name="intelligence" type="float" value="2"/>
    </properties>
   </property>
  </properties>
  <image source="objects/sword_heal.png" width="32" height="32"/>
 </tile>
 <tile id="33" type="ItemObject">
  <properties>
   <property name="action" propertytype="ActionType" value="REGENERATE_MANA_RING"/>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="ACCESSORY"/>
   <property name="cost" type="int" value="500"/>
   <property name="itemType" propertytype="ItemType" value="RING"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="manaMax" type="float" value="10"/>
    </properties>
   </property>
  </properties>
  <image source="objects/ring.png" width="16" height="16"/>
 </tile>
 <tile id="34" type="ItemObject">
  <properties>
   <property name="action" propertytype="ActionType" value="STR_BOOSTER"/>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="WEAPON"/>
   <property name="cost" type="int" value="500"/>
   <property name="itemType" propertytype="ItemType" value="SWORD_STRENGTH"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="damage" type="float" value="8"/>
     <property name="strength" type="float" value="2"/>
    </properties>
   </property>
  </properties>
  <image source="objects/sword_strength.png" width="32" height="32"/>
 </tile>
 <tile id="35" type="ItemObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="category" propertytype="ItemCategory" value="BOOTS"/>
   <property name="cost" type="int" value="100"/>
   <property name="itemType" propertytype="ItemType" value="BOOTS_SPEED"/>
   <property name="speed" type="float" value="4"/>
   <property name="stats" type="class" propertytype="Stats">
    <properties>
     <property name="armor" type="float" value="1"/>
    </properties>
   </property>
  </properties>
  <image source="objects/boots_speed.png" width="32" height="32"/>
 </tile>
 <tile id="36" type="PropObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="regionPrefix" value="items"/>
  </properties>
  <image source="objects/small_health_potion.png" width="9" height="11"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="0" y="0" width="9" height="11">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="37" type="PropObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="regionPrefix" value="items"/>
  </properties>
  <image source="objects/small_mana_potion.png" width="9" height="11"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="0" y="0" width="9" height="11">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="38" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="SNOWY"/>
   <property name="triggerName" value="snowy"/>
  </properties>
  <image source="objects/snowy.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="1" y="11" width="14" height="5">
    <properties>
     <property name="density" type="float" value="50000"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
 <tile id="39" type="PropObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
  </properties>
  <image source="objects/spirit.png" width="32" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="10" y="5" width="14" height="25">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
</tileset>
