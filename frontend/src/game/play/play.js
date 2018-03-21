"use strict";

const fetch = require('node-fetch');
var params = require('game/params.js');
var status = require('game/status.js');
const config = require('config/config.js');
const backend = config.servers["backend"];

module.exports = {
    playCard(rownum, cardindex, target) {
        const playData = {
            index: cardindex,
            row: rownum,
            target: target,
        };
        document.querySelector('.card[index="'+cardindex+'"]').style.background = "yellow";

        fetch(
            `http://${backend}/games/${params.gameID}/player/${params.playerID}`,
            {
                method: 'POST',
                body: JSON.stringify(playData),
                headers: { 'Content-Type': 'application/json' }
            }
        )
            .then( () => status.setStatus() )
            .catch(error => console.log(error))
    }
}
