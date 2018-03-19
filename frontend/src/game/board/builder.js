"use strict";

const baseRow = document.getElementById("row-template")
    .content.querySelector("div");
const myRows = document.getElementById("my-rows");
const oppRows = document.getElementById("opp-rows");

const baseScore = document.getElementById("score-template")
    .content.querySelector("div");
const myScores = document.getElementById("my-scores");
const oppScores = document.getElementById("opp-scores");

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
            myScores.appendChild(newScore);

            var newScore2 = baseScore.cloneNode(true);
            newScore2.setAttribute("rownum", i);
            oppScores.appendChild(newScore2);
        }
    },
    buildCard(baseCard, cardData){
        var newCard = baseCard.cloneNode(true);

        newCard.setAttribute("draggable","false");
        newCard.classList.remove("col-1");
        newCard.classList.add("col-2");
        newCard.innerHTML = cardData["power"];

        return newCard;
    }
}
