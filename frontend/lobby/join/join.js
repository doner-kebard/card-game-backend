"use strict";

let params = new URLSearchParams(document.location.search.substring(1));
const gameID = params.get("gameID");

onConfigLoad(function() {

    var backend = config.servers["backend"]
    var req = new XMLHttpRequest();
    req.open('POST', `http://${backend}/games/` + gameID);
    req.responseType = "json";

    // Define the action to take when the response is loaded
    req.onload = function() {
        const playerID = req.response["player-id"];
        const url = `/game/?gameID=${gameID}&playerID=${playerID}`;

        //Use replace to remove ourselves from browser history
        window.location.replace(url);
    }

    req.send();
});

