"use strict";

requirejs(['./board/board.js', './hand.js', './scores.js', './helper.js'], function (board, hand, scores, helper) {

    function setState(state) {
        hand.setHand(state["hand"]);
        board.setBoard(state["rows"]);
        scores.setScores(state["scores"]);

        helper.setStatus();
    }

    var lastnum = 0;
    function updateGame() {
        var req = new XMLHttpRequest();
        req.open("GET", `http://backend:3000/games/${helper.gameID}/player/${helper.playerID}`);
        req.responseType = "json";

        req.onload = function () {
            var num = req.response["hand"].length;
            if (num !== lastnum){
                lastnum = num;
                setState(req.response);
            }
        }

        req.send();
    }

    board.buildRows();
    updateGame();
    setInterval(updateGame, 1000);

    // Make functions visible to the browser window so they can be seen from HTML events
    window.allowDrop = board.allowDrop;
    window.dropOnRow = board.dropOnRow;
    window.clickOnRow = board.clickRow;
    window.dragCardFromHand = hand.dragCard;
});
