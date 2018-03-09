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

            play.playCard(event.target.getAttribute("rownum"),
                          event.dataTransfer.getData("handIndex"));
        }
    }
});
