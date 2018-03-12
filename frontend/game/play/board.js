"use strict";

define(function(require) {
    var helper = require('../helper.js');
    var play = require('./play.js');

    return {
        clickRow(event) {
            if (helper.clickedCard !== undefined) {
                event.preventDefault();

                play.playCard(event.target.getAttribute("rownum"),
                              helper.clickedCard.getAttribute("index"));
            }
        },
        allowDrop(event) {
            event.preventDefault();
        },
        dropOnRow(event) {
            event.preventDefault();
            const rownum = event.target.getAttribute("rownum");
            const cardindex = event.dataTransfer.getData("handIndex");

            helper.onGetStatus(function(status) {
                if (status === config.messages["play"]) {
                    play.playCard(rownum, cardindex);
                }
            })
        }
    }
});
