"use strict";
import { Selector, ClientFunction } from 'testcafe';
import MainPage from './main_page.js';
import GamePage from './game_page.js';

import config from './config.js'
import wait_for_frontend from './wait-for-frontend.js'

fixture("Lobby")
    .page("http://" + config.servers["frontend"])
    .before(wait_for_frontend);

const mainPage = new MainPage();
const gamePage = new GamePage();

test('Create a game', async testCase => {
    await testCase
        .expect(mainPage.title.exists).ok()
        .expect(gamePage.joinLink.exists).notOk()
        .expect(gamePage.gameStatus.exists).notOk()
        .click(mainPage.createGame)
        .expect(gamePage.joinLink.exists).ok()
        .expect(gamePage.gameStatus.innerText).eql(config.messages["no-opp"])
        .expect(mainPage.createGame.exists).notOk()
});

test('Join a game', async testCase => {
    await testCase
        .click(mainPage.createGame)
        .expect(gamePage.cardsInHand.exists).notOk()
        .expect(gamePage.rows.exists).notOk()
        .expect(gamePage.myScore.exists).notOk()
        .expect(gamePage.opponentScore.exists).notOk();
    const url = await gamePage.joinLink.textContent;
    await testCase
        .navigateTo(url)
        .expect(gamePage.cardsInHand.exists).ok()
        .expect(gamePage.rows.exists).ok()
        .expect(gamePage.myScore.exists).ok()
        .expect(gamePage.opponentScore.exists).ok();
})

test('Game links are different', async testCase => {
    await testCase
        .click(mainPage.createGame)
    const link1 = await gamePage.joinLink.textContent;
    await testCase
        .navigateTo("/")
        .click(mainPage.createGame)
    const link2 = await gamePage.joinLink.textContent;
    await testCase
        .expect(link2).notEql(link1);

});
