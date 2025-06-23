// dynamically creates the color patterns for every level

var iwanthue = require('iwanthue');
 
var fs = require('fs');

for(var i = 2; i < 6; i++){
  for(var j = 1; j < 16; j++){
    var palette = iwanthue(i*i, {
      clustering: 'force-vector',
        seed: 'cool-palette',
        colorSpace : [(24*(j-1))%360, (24*j)%360, 10, 100, 30, 100],
        quality: 100
    });

  var json = {};

  switch(i){
    case 2:
      json.patternType = "TWO_X_TWO";
      break;
    case 3:
        json.patternType = "THREE_X_THREE";
        break;
    case 4:
        json.patternType = "FOUR_X_FOUR";
        break;
    default:
        json.patternType = "FIVE_X_FIVE";
        break;
  }

  json.colors = listToMatrix(palette,i);

    var dictstring = JSON.stringify(json);
    fs.writeFileSync("results/"+i+"x"+i+"/"+i+"x"+i+"-"+j+".json", dictstring);
  }
}



function listToMatrix(list, elementsPerSubArray) {
  var matrix = [], i, k;

  for (i = 0, k = -1; i < list.length; i++) {
      if (i % elementsPerSubArray === 0) {
          k++;
          matrix[k] = [];
      }

      matrix[k].push(list[i].replace("#",""));
  }

  return matrix;
}
