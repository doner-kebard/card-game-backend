"use strict";

define(function(require) {
    var helper = require('../helper.js');
    var play = require('./play.js');

    function clearBackground() {
        document.querySelectorAll(".card").forEach(function(element) {
            element.style.background = "white"
        });
    }

    return {
        clickCard(card) {
            helper.onGetStatus(function(status) {
                if (status === config.messages["play"]) {
                    clearBackground();
                    if (helper.clickedCard === card) {
                        helper.clickedCard = undefined;
                    } else {
                        helper.clickedCard = card;
                        card.style.background = "red";
                    }
                }
            })
        }  
    }
});
