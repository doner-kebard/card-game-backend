"use strict";

let params = new URLSearchParams(document.location.search.substring(1));
const gameID = params.get("gameID");
const playerID = params.get("playerID");

function onGetStatus (action) {
    var req = new XMLHttpRequest();
    req.open("GET", `http://backend:3000/games/${gameID}/player/${playerID}`);
    req.responseType = "json";

    req.onload = function () {
        action(req.response["status"]);
    }

    req.send();
}

define(function() {
    return {
        clearChildren(elem) {
            while (elem.firstChild) {
                elem.removeChild(elem.firstChild);
            }
        },
        clickedCard: undefined,
        gameID: gameID,
        playerID: playerID,
        baseCard: document.getElementById("card-template")
                .content.querySelector(".card"),
        onGetStatus: onGetStatus,
        setStatus() {
            onGetStatus(function(status) {
                document.getElementById("game-status").innerHTML = status;
           })
        }   
    }
});
