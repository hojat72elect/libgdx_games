<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.0" name="objects" tilewidth="48" tileheight="48" tilecount="5" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="PlayerObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="atlasRegionKey" value="hero"/>
   <property name="objType" propertytype="MapObjectType" value="HERO"/>
  </properties>
  <image source="objects/hero.png" width="16" height="16"/>
 </tile>
 <tile id="1" type="PropObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
  </properties>
  <image source="objects/tree_green.png" width="48" height="48"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="14" y="36" width="20" height="12"/>
  </objectgroup>
 </tile>
 <tile id="2" type="PlayerObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="HERO"/>
  </properties>
  <image source="objects/hero.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="14" width="16" height="2"/>
   <object id="2" x="0" y="0" width="5" height="5">
    <ellipse/>
   </object>
   <object id="3" x="0" y="11">
    <polyline points="0,0 16,0"/>
   </object>
   <object id="4" x="1" y="9">
    <polygon points="0,0 14,0 13,-2 11,-1 8,-2 5,-1 2,-2"/>
   </object>
   <object id="5" x="6" y="1" width="10" height="4">
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="3" type="PlayerObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="HERO"/>
   <property name="speed" type="float" value="7"/>
  </properties>
  <image source="objects/hero.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="3" type="FixtureDefinition" x="2" y="13" width="12" height="3">
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
 <tile id="4" type="EnemyObject">
  <properties>
   <property name="animationType" propertytype="AnimationType" value="IDLE"/>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="level" type="int" value="1"/>
   <property name="numEnemies" type="int" value="1"/>
   <property name="objType" propertytype="MapObjectType" value="MUSHROOM"/>
   <property name="stats" type="class" propertytype="CharStats">
    <properties>
     <property name="agility" type="float" value="1"/>
    </properties>
   </property>
  </properties>
  <image source="objects/mushroom.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="2" y="11" width="12" height="5"/>
  </objectgroup>
 </tile>
</tileset>
