// Defines the start positions and colors of balls for each level
'use strict';

var fs = require('fs');

var colors = [1, 2, 3, 4];

var numberBalls = 36;
var whiteBalls = 6;

var maxTime = 0;

function compare(a, b) {
  if (a.time < b.time)
    return -1;
  if (a.time > b.time)
    return 1;
  return 0;
}

for (var i = 1; i <= 100; i++) {

  if (i % 4 == 0) {
    numberBalls += 6;
    whiteBalls++;
  }

  var json = {
    ballInfos: []
  };

  //other balls
  for (var j = 0; j < numberBalls - whiteBalls; j++) {
    var x, y;

    if (Math.random() >= 0.5) {
      if (Math.random() >= 0.5) {
        x = 0;
        y = Math.floor(Math.random() * 800)
      } else {
        x = 480;
        y = Math.floor(Math.random() * 800)
      }
    } else {
      if (Math.random() >= 0.5) {
        y = 0;
        x = Math.floor(Math.random() * 480)
      } else {
        y = 800;
        x = Math.floor(Math.random() * 480)
      }
    }

    var speed = Math.floor((Math.random() * i * 2) + 125);

    //to check max speed
    if (speed > 300)
      speed = 300;

    var color = colors[Math.floor(Math.random() * colors.length)];
    var time = Math.floor(Math.random() * 50) + j * (50 - (i*0.25));

    if(time > maxTime)
      maxTime = time;

    var ballInfo = {
      'x': x,
      'y': y,
      'time': time,
      'speed': speed,
      'color': color
    };
    json.ballInfos.push(ballInfo);
  }

  //white balls
  for (var j = 0; j < whiteBalls; j++) {
    var x, y;

    if (Math.random() >= 0.5) {
      if (Math.random() >= 0.5) {
        x = 0;
        y = Math.floor(Math.random() * 800)
      } else {
        x = 480;
        y = Math.floor(Math.random() * 800)
      }
    } else {
      if (Math.random() >= 0.5) {
        y = 0;
        x = Math.floor(Math.random() * 480)
      } else {
        y = 800;
        x = Math.floor(Math.random() * 480)
      }
    }

    var time = Math.floor(Math.random() * 50) + (j*5) * (50 - (i*0.25));
    var speed = Math.floor((Math.random() * i * 2) + 125);
    var color = 0;

    //to check max speed
    if (speed > 300)
      speed = 300;
    var ballInfo = {
      'x': x,
      'y': y,
      'time': time,
      'speed': speed,
      'color': color
    };
    json.ballInfos.push(ballInfo);
  }

  json.ballInfos.sort(compare);
  var dictstring = JSON.stringify(json);
  fs.writeFileSync("levels/level" + i + ".json", dictstring);
}
