"use strict";

let params = new URLSearchParams(document.location.search.substring(1));
const gameID = params.get("gameID");
const playerID = params.get("playerID");
const gameURL = `/game/?gameID=${gameID}&playerID=${playerID}`;

function checkForOpponent() {
    var backend = config.servers["backend"]
    var req = new XMLHttpRequest();
    req.open("GET", `http://${backend}/games/${gameID}/player/${playerID}`);
    req.responseType = "json";
    
    req.onload = function () {
        const status = req.response["status"];
        
        if (status !== config.messages["no-opp"]) {
            window.location.href = gameURL;
        }
    }

    req.send();
}

function setup () {
    var frontend = config.servers["frontend"]
    var joinLink = document.getElementById("joinTemplate")
        .content.querySelector('#join-link');

    var text = document.createTextNode(`http://${frontend}/lobby/join/?gameID=${gameID}`);
    joinLink.appendChild(text);
    
    document.getElementById("waiting-for-opponent").appendChild(joinLink);
    document.getElementById("game-status").innerHTML = config.messages["no-opp"];

    setInterval(checkForOpponent, 1000);
}

onConfigLoad(setup);
