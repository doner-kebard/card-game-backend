"use strict";

define(function (require) {
    var helper = require('../helper.js');
    var builder = require('./builder.js');
    var drop = require('./drop.js');

    function fetchRow(finder, index) {
        return document.querySelectorAll(finder)[index];
    }

    return {
        buildRows: builder.buildRows,
        setBoard(boardState) {
            var gameRows = document.querySelectorAll(".game-row");

            gameRows.forEach(function (gameRow) {
                helper.clearChildren(gameRow);
            })

            boardState.forEach(function (row, rownum) {
                row.forEach(function (cardInRow) {
                    var newCard = builder.buildCard(helper.baseCard, cardInRow);

                    if (cardInRow["owner"] === "me") {
                        fetchRow("#my-rows .game-row", rownum).appendChild(newCard);
                    } else {
                        fetchRow("#opp-rows .game-row", rownum).appendChild(newCard);
                    }
                });
            });
        },
        allowDrop: drop.allow,
        dropOnRow: drop.onRow,
    }
});
