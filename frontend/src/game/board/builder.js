"use strict";

var status = require("game/status.js");
var play = require("game/play/play.js");
const baseRow = document.getElementById("row-template")
    .content.querySelector("div");
const myRows = document.getElementById("my-rows");
const oppRows = document.getElementById("opp-rows");

const baseScore = document.getElementById("score-template")
    .content.querySelector("div");
const rowScores = document.getElementById("row-scores");
const limits = document.getElementById("limits");

module.exports = {
    buildRows() {
        for (var i = 0; i < 5; i++) {
            var myNewRow = baseRow.cloneNode(true);
            myNewRow.setAttribute("rownum", i);

            myRows.appendChild(myNewRow);

            var oppRow = baseRow.cloneNode(true);
            oppRow.onclick = null;
            oppRow.ondragover = null;
            oppRow.ondrop = null;
            oppRows.appendChild(oppRow);

            var newScore = baseScore.cloneNode(true);
            newScore.setAttribute("rownum", i);
            rowScores.appendChild(newScore);

            var newScore2 = baseScore.cloneNode(true);
            newScore2.setAttribute("rownum", i);
            limits.appendChild(newScore2);
        }
    },
    buildCard(baseCard, cardData){
        var newCard = baseCard.cloneNode(true);

        newCard.setAttribute("draggable","false");
        newCard.classList.remove("col-1");
        newCard.classList.add("col-2");
        newCard.innerHTML = cardData["power"];
        newCard.addEventListener('click', function() {
            if (status.clickedCard &&
                status.clickedCard.hasAttribute("row-played") &&
                !status.clickedCard.hasAttribute("target")) {
                status.clickedCard.setAttribute("target", 1);
                play.playCard(status.clickedCard);
            }
        });

        return newCard;
    }
};
