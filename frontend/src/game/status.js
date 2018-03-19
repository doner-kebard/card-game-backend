"use strict";

const fetch = require('node-fetch');
const config = require('config/config.js');
const params = require('game/params.js');
const backend = config.servers["backend"];

function onGetStatus (action) {
    fetch(`http://${backend}/games/${params.gameID}/player/${params.playerID}`)
        .then(res => res.json())
        .then(json => action(json["status"]));
}

module.exports = {
    clickedCard: undefined,
    onGetStatus,
    setStatus() {
        onGetStatus(function(status) {
            document.getElementById("game-status").innerHTML = status;
        })
    }
}
