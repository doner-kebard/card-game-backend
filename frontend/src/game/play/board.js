"use strict";
const config = require("config/config.js");

var status = require("game/status.js");
var play = require("game/play/play.js");

module.exports = {
    clickRow(event) {
        if (status.clickedCard !== undefined) {
            event.preventDefault();

            var rownum = event.target.getAttribute("rownum");
            status.clickedCard.setAttribute("row-played", rownum);
            play.playCard(status.clickedCard);
        }
    },
    allowDrop(event) {
        event.preventDefault();
    },
    dropOnRow(event) {
        event.preventDefault();
        var card = document.querySelector(".card[index=\""+
            event.dataTransfer.getData("handIndex")+
            "\"]");
        card.setAttribute("row-played",
            event.target.getAttribute("rownum"));

        status.onGetStatus(function(status) {
            if (status === config.messages["play"]) {
                play.playCard(card);
            }
        });
    }
};
