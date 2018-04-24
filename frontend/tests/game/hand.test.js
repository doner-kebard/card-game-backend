"use strict";

document.body.innerHTML =
    `<div class="hand"></div>
    <template id="card-template">
        <div class="card"></div>
    </template>`

const hand = require('game/hand.js');

test('Dragging a card sets the data', () => {
    var event = {
        dataTransfer: {
            setData: jest.fn()
        },
        target: {
            getAttribute: jest.fn()
        }
    }

    event.target.getAttribute
        .mockReturnValueOnce("10")
        .mockReturnValueOnce("2");

    hand.dragCard(event);
    hand.dragCard(event);

    expect(
        event.dataTransfer.setData.mock.calls[0][1]
    ).toBe("10");
    expect(
        event.dataTransfer.setData.mock.calls[1][1]
    ).toBe("2");
})

test('Set hand obeys the state', () => {
    // Card is added
    var sampleHand = [{power: 3}];
    hand.setHand(sampleHand);
    expect(
        document.querySelectorAll('.hand .card').length
    ).toBe(1);
    expect(
        document.querySelectorAll('.hand .card')[0].innerHTML
    ).toBe("3");

    // Cards show ability
    var sampleHand = [{power: 5, "add-power": -3}, {power: 1, "add-power": 7}];
    hand.setHand(sampleHand);
    expect(
        document.querySelectorAll('.hand .card').length
    ).toBe(2);
    expect(
        document.querySelectorAll('.hand .card')[0].innerHTML
    ).toBe("5 (-3)");
    expect(
        document.querySelectorAll('.hand .card')[1].innerHTML
    ).toBe("1 (+7)");

    // Cards are deleted
    var sampleHand = [];
    hand.setHand(sampleHand);
    expect(
        document.querySelectorAll('.hand .card').length
    ).toBe(0);
});
