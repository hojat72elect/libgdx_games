<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.0" name="interactTestObjects" tilewidth="16" tileheight="16" tilecount="4" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="PlayerObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="HERO"/>
  </properties>
  <image source="objects/hero.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="FixtureDefinition" x="-54" y="-54" width="128" height="128">
    <properties>
     <property name="isSensor" type="bool" value="true"/>
     <property name="userData" value="interact"/>
    </properties>
    <ellipse/>
   </object>
   <object id="2" type="FixtureDefinition" x="1" y="13" width="14" height="3"/>
  </objectgroup>
 </tile>
 <tile id="1" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="SMITH"/>
   <property name="triggerName" value="smith"/>
  </properties>
  <image source="objects/smith.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="1" y="13" width="15" height="3"/>
  </objectgroup>
 </tile>
 <tile id="2" type="NpcObject">
  <properties>
   <property name="atlas" propertytype="AtlasAsset" value="CHARS_AND_PROPS"/>
   <property name="objType" propertytype="MapObjectType" value="MERCHANT"/>
   <property name="triggerName" value="merchant"/>
  </properties>
  <image source="objects/merchant.png" width="16" height="16"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="13" width="16" height="3"/>
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
   <object id="1" x="0" y="13" width="16" height="3"/>
  </objectgroup>
 </tile>
</tileset>
