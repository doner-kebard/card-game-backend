"use strict";

define(function (require) {
    var helper = require('../helper.js');

    return {
        allow(event) {
            event.preventDefault();
        },
        onRow(event) {
            event.preventDefault();
            var req = new XMLHttpRequest();
            req.open("POST", `http://backend:3000/games/${helper.gameID}/player/${helper.playerID}`);
            req.setRequestHeader("Content-type", "application/json");
            req.responseType = "json";

            const data = event.dataTransfer.getData("handIndex");
            const playData = {
                index: data,
                row: event.target.getAttribute("rownum")
            };

            req.send(JSON.stringify(playData));
        }
    }
});
