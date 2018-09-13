"use strict";

const config = require("config/config.js");
const backend = config.servers["backend"];

var board = require("game/board/board.js");
var hand = require("game/hand.js");
var updateGame = require("game/update.js");

board.buildRows();
updateGame();
setInterval(updateGame, 1000);

// Make functions visible to the browser window so they can be seen from HTML events
window.allowDrop = board.allowDrop;
window.dropOnRow = board.dropOnRow;
window.clickOnRow = board.clickRow;
window.dragCardFromHand = hand.dragCard;
