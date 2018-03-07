"use strict";

document.addEventListener('DOMContentLoaded', function() {

    var req = new XMLHttpRequest();
    req.open('POST', 'http://backend:3000/games');
    req.responseType = "json";

    // Define the action to take when the response is loaded
    req.onload = function() {
        const gameID = req.response["game-id"];
        const playerID = req.response["player-id"];
        const url = `/lobby/waiting/?gameID=${gameID}&playerID=${playerID}`;

        //Use replace to remove ourselves from browser history
        window.location.replace(url);
    }

    req.send();
});
