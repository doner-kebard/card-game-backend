"use strict";

const config = require("config/config.js");
const backend = config.servers["backend"];
const frontend = config.servers["frontend"];

let params = new URLSearchParams(document.location.search.substring(1));
const gameID = params.get("gameID");
const playerID = params.get("playerID");
const gameURL = `/game.html?gameID=${gameID}&playerID=${playerID}`;

async function checkForOpponent() {
    const res = await fetch(`http://${backend}/games/${gameID}/player/${playerID}`);
    const json = await res.json();
    const status = json["status"];

    if (status !== config.messages["no-opp"]) {
        window.location.href = gameURL;
    }
}

var joinLink = document.getElementById("joinTemplate")
    .content.querySelector("#join-link");

var text = document.createTextNode(`http://${frontend}/lobby_join.html?gameID=${gameID}`);
joinLink.appendChild(text);

document.getElementById("waiting-for-opponent").appendChild(joinLink);
document.getElementById("game-status").innerHTML = config.messages["no-opp"];

setInterval(checkForOpponent, 1000);
