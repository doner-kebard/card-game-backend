"use strict";

const play = require("game/play/play.js");

test("Playing cards changes background as intended", () => {
    var card = document.createElement("div");
    var mockedStatus = document.createElement("div");
    mockedStatus.setAttribute("id", "game-status");
    document.body.appendChild(mockedStatus);

    card.setAttribute("add-power", "undefined");
    play.playCard(card);
    expect(card.style.background).toBe("green");
    
    card.setAttribute("add-power", 1);
    play.playCard(card);
    expect(card.style.background).toBe("yellow");

    card.setAttribute("target", 1);
    play.playCard(card);
    expect(card.style.background).toBe("green");
});
