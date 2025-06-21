# Box2DLights

[![Latest Version](https://img.shields.io/nexus/r/com.badlogicgames.box2dlights/box2dlights?nexusVersion=2&server=https%3A%2F%2Foss.sonatype.org&label=Version)](https://search.maven.org/artifact/com.badlogicgames.box2dlights/box2dlights)
[![Snapshots](https://img.shields.io/nexus/s/com.badlogicgames.box2dlights/box2dlights?server=https%3A%2F%2Foss.sonatype.org&label=Snapshots)](https://oss.sonatype.org/#nexus-search;gav~com.badlogicgames.box2dlights~box2dlights~~~~kw,versionexpand)

[![screenshot](http://img.youtube.com/vi/lfT8ajGbzk0/0.jpg)](http://www.youtube.com/watch?v=lfT8ajGbzk0)

Kalle Hameleinen's Box2DLights is a 2D lighting framework that uses [box2d](http://box2d.org/) for
raycasting and OpenGL ES 2.0 for rendering. This library is intended to be used
with [libgdx](http://libgdx.com).

## Features

* Arbitrary number of lights
* Gaussian blurred light maps
* Point light
* Cone Light
* Directional Light
* Chain Light
* Shadows
* Dynamic/static/xray light
* Culling
* Colored ambient light
* Gamma corrected colors
* Handler class to do all the work
* Query method for testing is point inside of light/shadow

This library offers an easy way to add soft dynamic 2d lights to your physics based game. Rendering
uses
libgdx, but it would be easy to port this to other frameworks or pure openGl too.
