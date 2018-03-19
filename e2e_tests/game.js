"use strict";
import { Selector, ClientFunction } from 'testcafe';
import MainPage from './main_page.js';
import GamePage from './game_page.js';

import config from './config.js'
import wait_for_frontend from './wait-for-frontend.js'

fixture("Game")
    .page("http://" + config.servers["frontend"])
    .before(wait_for_frontend);

const mainPage = new MainPage();
const gamePage = new GamePage();
const getWindowLocation = ClientFunction(() => window.location);

test('Sample Game', async testCase => {

    await testCase.click(mainPage.createGame)
    const opponentLink = await gamePage.joinLink.textContent;
    const myGameURL = await getWindowLocation();

    await testCase
        .navigateTo(opponentLink)
        .expect(gamePage.cardsInHand.exists).ok()
    const opponentURL  = await getWindowLocation();

    await testCase
        .navigateTo(myGameURL.href)
        .expect(gamePage.cardsInHand.exists).ok()
        .expect(gamePage.myScore.innerText).eql("0")
        .expect(gamePage.opponentScore.innerText).eql("0");

    var cardsInPlay = 0;
    var maxHandNum = await gamePage.cardsInHand.count;

    while (await gamePage.cardsInHand.exists) {
        await gamePage.checkState(testCase, maxHandNum, cardsInPlay)
            .expect(gamePage.gameStatus.innerText).eql(config.messages["play"])
            .dragToElement(
                gamePage.cardsInHand.nth(0),
                gamePage.rows.nth(0))
            .expect(gamePage.gameStatus.innerText).eql(config.messages["wait"])
            .navigateTo(opponentURL.href)

        await gamePage.checkState(testCase, maxHandNum, cardsInPlay)
            .expect(gamePage.gameStatus.innerText).eql(config.messages["play"])
            .dragToElement(
                gamePage.cardsInHand.nth(0),
                gamePage.rows.nth(1))
            .expect(gamePage.gameStatus.innerText).eql(config.messages["play"])

        await gamePage.checkState(testCase, maxHandNum-1, cardsInPlay+1)
            .navigateTo(myGameURL.href);

        cardsInPlay += 1;
        maxHandNum -= 1;
    }

    await testCase
        .expect(gamePage.myScore.innerText).eql("1")
        .expect(gamePage.opponentScore.innerText).eql("1")
})
