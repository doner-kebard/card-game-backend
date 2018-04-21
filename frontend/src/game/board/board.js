"use strict";

var cleanup = require('game/cleanup.js');
var templates = require('game/templates.js');
var builder = require('game/board/builder.js');
var play = require('game/play/board.js');

function fetchRow(finder, index) {
    return document.querySelectorAll(finder)[index];
}

module.exports = {
    buildRows: builder.buildRows,
    setBoard(boardState) {
        var gameRows = document.querySelectorAll(".game-row");

        gameRows.forEach(function (gameRow) {
            cleanup.clearChildren(gameRow);
        })

        boardState.forEach(function (row, rownum) {
            row["cards"].forEach(function (cardInRow) {
                var newCard = builder.buildCard(templates.baseCard, cardInRow);

                if (cardInRow["owner"] === "me") {
                    fetchRow("#my-rows .game-row", rownum).appendChild(newCard);
                } else {
                    fetchRow("#opp-rows .game-row", rownum).appendChild(newCard);
                }
            });
            document.querySelectorAll("#limits .scores-row")[rownum].innerText = "(lim: " + row["limit"] + ")"
        });
    },
    allowDrop: play.allowDrop,
    dropOnRow: play.dropOnRow,
    clickRow: play.clickRow
}
