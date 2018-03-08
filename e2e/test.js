"use strict";
import { Selector, ClientFunction } from 'testcafe';
import MainPage from './main_page.js';
import GamePage from './game_page.js';

import yaml from 'js-yaml';
import fs from 'fs';

const config = yaml.safeLoad(fs.readFileSync('/configs/config.yml', 'utf8'));

fixture("Game creation")
    .page("http://frontend:8080");

const mainPage = new MainPage();
const gamePage = new GamePage();
const getWindowLocation = ClientFunction(() => window.location);

test('Create a game', async testCase => {
    await testCase
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
            .dragToElement(
                gamePage.cardsInHand.nth(0),
                gamePage.rows.nth(0))
            .navigateTo(opponentURL.href)

        await gamePage.checkState(testCase, maxHandNum, cardsInPlay)
            .dragToElement(
                gamePage.cardsInHand.nth(0),
                gamePage.rows.nth(1))
        
        await gamePage.checkState(testCase, maxHandNum-1, cardsInPlay+1)
            .navigateTo(myGameURL.href);

        cardsInPlay += 1;
        maxHandNum -= 1;
    }

    await testCase
        .expect(gamePage.myScore.innerText).eql("1")
        .expect(gamePage.opponentScore.innerText).eql("1")
})
