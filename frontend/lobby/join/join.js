"use strict";

let params = new URLSearchParams(document.location.search.substring(1));
const gameID = params.get("gameID");

document.addEventListener('DOMContentLoaded', function() {

    var req = new XMLHttpRequest();
    req.open('POST', 'http://backend:3000/games/' + gameID);
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

