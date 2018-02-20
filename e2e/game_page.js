"use strict";
import { Selector } from 'testcafe'; // first import testcafe selectors

export default class GamePage {
    constructor () {
        this.hand = Selector('.hand');
        this.cardsInHand = this.hand.find('.card');
        this.joinLink = Selector('#join-link');
        this.rows = Selector('.game-row');
        this.myScore = Selector('#my-score');
        this.opponentScore = Selector('#opponent-score');
    }
}

