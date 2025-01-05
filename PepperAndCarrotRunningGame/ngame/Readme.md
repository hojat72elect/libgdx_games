#nGame

nGame is a bunch code snippets, classes and a half-baked screen manager for libgdx.


##Dependencies

*libGDX 1.5.3
*libGDX Controllers

##Instruction

You can either download the repo, pick some specific class or integrate the whole project as submodule.

###Git Submodule

Add ngame as submodule, in your projct folder exec:

```
git submodule add https://github.com/n0iz/ngame.git
```

If you do a "git status" you should get something like:

´´´
$ git status
On branch develop
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

        modified:   .gitmodules
        new file:   ngame
´´´

Now do a commit an you are fine.

For further reading about git submodules, see [Git-Tools-Submodules]: http://git-scm.com/book/en/v2/Git-Tools-Submodules.

###Project Setup
To integrate nGame into your project as gradle module consider the following.

Add 'ngame' in settings.gradle:

```gradle
include 'desktop', 'android', 'ios', 'html', 'core', 'ngame'
```

In build.gradle add:

```gradle
project(":ngame") {
    apply plugin: "java"


    dependencies {
        compile "com.badlogicgames.gdx:gdx:$gdxVersion"
        compile "com.badlogicgames.gdx:gdx-controllers:$gdxVersion"
		compile fileTree(dir: 'libs', include: '*.jar')
    }
}
```

In build.gradle core dependencies add:
```gradle
dependencies {
	...
	compile project(":ngame")
}
```
