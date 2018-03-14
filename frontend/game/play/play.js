"use strict";

define(function(require) {
    var helper = require('../helper.js');
    var backend = config.servers["backend"];

    return {
        playCard(rownum, cardindex) {
            var req = new XMLHttpRequest();
            req.open("POST", `http://${backend}/games/${helper.gameID}/player/${helper.playerID}`);
            req.setRequestHeader("Content-type", "application/json");
            req.responseType = "json";

            const playData = {
                index: cardindex,
                row: rownum,
            };

            document.querySelector('.card[index="'+cardindex+'"]').style.background = "yellow";

            req.onload = helper.setStatus;

            req.send(JSON.stringify(playData));
        }
    }
});