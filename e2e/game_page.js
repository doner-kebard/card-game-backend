"use strict";
import { Selector } from 'testcafe'; // first import testcafe selectors

export default class GamePage {
    constructor () {
        this.hand = Selector('.hand');
        this.cardsInHand = this.hand.find('.card');
        this.joinLink = Selector('#join-link');
        this.rows = Selector('#my-rows .game-row');
        this.opponentRows = Selector('#opp-rows .game-row');
        this.myScore = Selector('#my-score');
        this.opponentScore = Selector('#opponent-score');
        this.gameStatus = Selector('#game-status');

        this.cardsInMyRow = this.rows.nth(0).find('.card')
        this.cardsInOpposingRow = this.opponentRows.nth(0).find('.card')
    }

    checkState(testCase, inHand, inPlay){
        return testCase
            .expect(this.cardsInHand.count).eql(inHand)
            .expect(this.cardsInMyRow.count).eql(inPlay)
            .expect(this.cardsInOpposingRow.count).eql(inPlay)
    }

}

