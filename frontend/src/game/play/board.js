"use strict";
const config = require('config/config.js');

var status = require('game/status.js');
var play = require('game/play/play.js');

module.exports = {
    clickRow(event) {
        if (status.clickedCard !== undefined) {
            event.preventDefault();

            play.playCard(event.target.getAttribute("rownum"),
                status.clickedCard.getAttribute("index"));
        }
    },
    allowDrop(event) {
        event.preventDefault();
    },
    dropOnRow(event) {
        event.preventDefault();
        const rownum = event.target.getAttribute("rownum");
        const cardindex = event.dataTransfer.getData("handIndex");

        status.onGetStatus(function(status) {
            if (status === config.messages["play"]) {
                play.playCard(rownum, cardindex);
            }
        })
    }
}
