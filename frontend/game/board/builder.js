"use strict";

define(function (require) {

    const baseRow = document.getElementById("row-template")
        .content.querySelector("div");
    const myRows = document.getElementById("my-rows");
    const oppRows = document.getElementById("opp-rows");

    return {
        buildRows() {
            for (var i = 0; i < 5; i++) {
                var myNewRow = baseRow.cloneNode(true);
                myNewRow.setAttribute("rownum", i);

                myRows.appendChild(myNewRow);

                var oppRow = baseRow.cloneNode(true);
                oppRows.appendChild(oppRow);
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
});
