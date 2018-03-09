"use strict";

let params = new URLSearchParams(document.location.search.substring(1));

define(function() {
    return {
        clearChildren(elem) {
            while (elem.firstChild) {
                elem.removeChild(elem.firstChild);
            }
        },
        clickedCard: undefined,
        gameID: params.get("gameID"),
        playerID: params.get("playerID"),
        baseCard: document.getElementById("card-template")
                .content.querySelector(".card")
    }
});
