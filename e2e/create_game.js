"use strict";
import { Selector } from 'testcafe'; // first import testcafe selectors
import MainPage from './main_page.js';
import GamePage from './game_page.js';

fixture `Create a game`// declare the fixture
    .page `http://localhost:8080`;  // specify the start page

const mainPage = new MainPage();
const gamePage = new GamePage();

//then create a test and place your code there
test('Create a game', async t => {
    await t
        .click(mainPage.createGame)
        .expect(gamePage.cardsInHand.count).eql(12)
        .expect(gamePage.joinLink.exists).ok()
        .expect(gamePage.rows.count).eql(5)
        .expect(gamePage.myScore.innerText).eql("0")
        .expect(gamePage.opponentScore.innerText).eql("0")
});
