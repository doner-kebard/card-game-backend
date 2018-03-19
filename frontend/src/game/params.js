"use strict";

let params = new URLSearchParams(document.location.search.substring(1));
const gameID = params.get("gameID");
const playerID = params.get("playerID");

module.exports = { gameID, playerID };
