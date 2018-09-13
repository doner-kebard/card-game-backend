"use strict";

const config = require("config/config.js");
const fetch = require("node-fetch");
const backend = config.servers["backend"];

async function createGame() {
    try {
        const res = await fetch(`http://${backend}/games`, { method: "POST" });
        const response = await res.json();
        const gameID = response["game-id"];
        const playerID = response["player-id"];
        const url = `/lobby_waiting.html?gameID=${gameID}&playerID=${playerID}`;

        //Use replace to remove ourselves from browser history
        window.location.replace(url);

    } catch (error) { // Server not found
        console.log(error);
        setTimeout(createGame, 3000); // Retry in 3 seconds
    }
}

createGame();
