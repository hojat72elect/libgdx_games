[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-red.svg)](http://kotlinlang.org/)
[![Fleks](https://img.shields.io/badge/Fleks-2.11-purple.svg)](https://github.com/Quillraven/Fleks)

[![LibGDX](https://img.shields.io/badge/LibGDX-1.13.1-green.svg)](https://libgdx.com/)
[![LibKTX](https://img.shields.io/badge/LibKTX-1.13.1--rc1-blue.svg)](https://github.com/libktx/ktx)
[![Tiled](https://img.shields.io/badge/Tiled-1.11.0-teal.svg)](https://www.mapeditor.org/)

# Available on itch.io

itch.io [link](https://quillraven.itch.io/masamune)

# Masamune

In Masamune you play the hero **Alexxius**. Your home village was
safe for many decades thanks to your ancestors' magic and the **Masamune** sword.

However, the darkness seems to return and the protective magic of the sword is fading.

This is where your story begins...

### Controls

- Move with W/A/S/D
- Interact with SPACE
- Open menu with CTRL_LEFT
- Close/Cancel menu with CTRL_LEFT or ESCAPE

### Screenshots
<p float="left">
    <img src="https://github.com/user-attachments/assets/fc17f8fa-ff93-4a9d-98cc-7bfc39a78428" width="500">
    <img src="https://github.com/user-attachments/assets/9b592595-b955-4ce4-a26d-926bbd5cb59b" width="500">
</p>
<p float="left">
    <img src="https://github.com/user-attachments/assets/fa9e03c4-8224-48b1-be90-3fa229072f3d" width="500">
    <img src="https://github.com/user-attachments/assets/533f5136-c29e-44b3-ae20-2c21c6fd8131" width="500">
</p>
<p float="left">
    <img src="https://github.com/user-attachments/assets/3a0ff295-bb5a-45f0-a178-919d3bd9abb1" width="500">
    <img src="https://github.com/user-attachments/assets/e677d211-9920-4c0f-b888-186a668c40be" width="500">
</p>

### Credits

- [ImageMagick](https://imagemagick.org/index.php)
  ```
  // split Idle.png and Walk.png into single frames
  magick '.\Idle.png' -crop 16x16 'idle_%02d.png'; magick '.\Walk.png' -crop 16x16 'walk_%02d.png'
  
  // combine images together in a tile layout
  // in this example two 16x16 images are combined into a single image with 1 image per row and 2 rows in total (=-tile 1x2)
  magick montage .\FlagBlue16x16.png .\FlagGray16x16.png -tile 1x2 -geometry 16x16+0+0< -background none Flags.png
  ```
- PowerShell
  ```shell
  // rename files called idle_40.png, idle_41.png to idle_00.png, idle_01.png, ...
  // used in combination with image magick scripts from above
  $i=0; Get-ChildItem "idle_*.png" | Sort-Object Name | % { Rename-Item $_ ("idle_{0:D2}.png" -f $i); $i++ }
  ```
- [Ninja Adventure Asset Pack](https://pixel-boy.itch.io/ninja-adventure-asset-pack)
- [8-Bit Fantasy Adventure Music Pack](https://xdeviruchi.itch.io/8-bit-fantasy-adventure-music-pack)
- [Cherry Cream Soda font](https://fonts.google.com/specimen/Cherry+Cream+Soda)
- [Fipps font](https://www.dafont.com/de/search.php?q=fipps)
- [Typing label](https://github.com/rafaskb/typing-label)
- [RPG Icon Pack](https://clockworkraven.itch.io/free-rpg-icon-pack-100-accessories-and-armor-clockwork-raven-studios)
- [JRPG Collection](https://opengameart.org/content/jrpg-collection)
- [Minifantasy Dungeon SFX Pack](https://leohpaz.itch.io/minifantasy-dungeon-sfx-pack)
- [jsfxr](https://sfxr.me/)
- [Effect and FX Pixel Part 11](https://bdragon1727.itch.io/64x64-pixel-effect-rpg-part-11)
- [Effect and FX Pixel Part 12](https://bdragon1727.itch.io/effect-and-fx-pixel-part-12)
- [Effect and FX Pixel Part 13](https://bdragon1727.itch.io/effect-and-fx-pixel-part-13)
- [Effect and FX Pixel Part 17](https://bdragon1727.itch.io/64x64-pixel-effect-rpg-part-17)
- [Free Pixel Art Weapon Icons](https://medievalmore.itch.io/free-weapon-icons)
- [Cut Scene Intro Song](https://opengameart.org/content/mystical-theme)
- [Kenney input prompts pixel](https://kenney-assets.itch.io/input-prompts-pixel-16)
