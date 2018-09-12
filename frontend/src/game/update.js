"use strict";

const fetch = require("node-fetch");
const config = require("config/config.js");
const backend = config.servers["backend"];

var board = require("game/board/board.js");
var hand = require("game/hand.js");
var scores = require("game/scores.js");
var status = require("game/status.js");
const params = require("game/params.js");

function setState(state) {
    hand.setHand(state["hand"]);
    board.setBoard(state["rows"]);
    scores.setScores(state["scores"]);
    scores.setScoresByRow(state["rows-power"]);

    status.setStatus();
}

var lastnum = 0;
function updateGame() {
    fetch( `http://${backend}/games/${params.gameID}/player/${params.playerID}`)
        .then(res => res.json())
        .then(json => {
            var num = json["hand"].length;
            if (num !== lastnum){
                lastnum = num;
                setState(json);
            }
        })
}

module.exports = updateGame;
