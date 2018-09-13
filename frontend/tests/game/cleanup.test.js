"use strict";
const cleanup = require("game/cleanup.js");

test("Cleanup children", () => {
    var elem = document.createElement("div");
    var potato = document.createElement("h1");

    elem.appendChild(potato);
    cleanup.clearChildren(elem);

    expect(elem.firstChild).toBe(null);
});
