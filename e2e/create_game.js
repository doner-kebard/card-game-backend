"use strict";
import { Selector, ClientFunction } from 'testcafe';
import MainPage from './main_page.js';
import GamePage from './game_page.js';

fixture("Game creation")
    .page("http://localhost:8080");

const mainPage = new MainPage();
const gamePage = new GamePage();
const getWindowLocation = ClientFunction(() => window.location);

test('Create a game', async t => {
    await t
        .expect(gamePage.joinLink.exists).notOk()
        .expect(gamePage.gameStatus.exists).notOk()
        .click(mainPage.createGame)
        .expect(gamePage.joinLink.exists).ok()
        .expect(gamePage.gameStatus.innerText).eql("Waiting for opponent")
        .expect(mainPage.createGame.exists).notOk()
});

test('Join a game', async t => {
    await t
        .click(mainPage.createGame)
        .expect(gamePage.cardsInHand.exists).notOk()
        .expect(gamePage.rows.exists).notOk()
        .expect(gamePage.myScore.exists).notOk()
        .expect(gamePage.opponentScore.exists).notOk();
    const url = await gamePage.joinLink.textContent;
    await t
        .navigateTo(url)
        .expect(gamePage.cardsInHand.exists).ok()
        .expect(gamePage.rows.exists).ok()
        .expect(gamePage.myScore.exists).ok()
        .expect(gamePage.opponentScore.exists).ok();
})

test('Game links are different', async t => {
    await t
        .click(mainPage.createGame)
    const link1 = await gamePage.joinLink.textContent;
    await t
	.navigateTo("/")
        .click(mainPage.createGame)
    const link2 = await gamePage.joinLink.textContent;
    await t
	.expect(link2).notEql(link1);

});

test('Sample Game', async t=> {
    await t
        .click(mainPage.createGame)
    const opponentLink = await gamePage.joinLink.textContent;
    const myGameURL = await getWindowLocation();
    await t
        .navigateTo(opponentLink)
        .expect(gamePage.cardsInHand.exists).ok()
    const opponentURL  = await getWindowLocation();
    await t
        .navigateTo(myGameURL.href)
        .expect(gamePage.cardsInHand.exists).ok();
    var cardsInPlay = 0;
    while (await gamePage.cardsInHand.exists) {
        let num = await gamePage.cardsInHand.count;
        await t
            .expect(gamePage.rows.at(0).find('.card').count).eql(cardsInPlay)
            .dragToElement(
                gamePage.cardsInHand.at(0),
                gamePage.rows.at(0))
            .expect(gamePage.cardsInHand.count).eql(num-1)
            .expect(gamePage.rows.at(0).find('.card').count).eql(cardsInPlay+1)
            .navigateTo(opponentURL.href)
            .expect(gamePage.rows.at(0).find('.card').count).eql(cardsInPlay)
            .expect(gamePage.cardsInHand.count).eql(num)
            .dragToElement(
                gamePage.cardsInHand.at(0),
                gamePage.rows.at(0))
            .expect(gamePage.cardsInHand.count).eql(num-1)
            .expect(gamePage.rows.at(0).find('.card').count).eql(cardsInPlay+2)
            .nagivateTo(myGameURL.href);
        cardsInPlay += 2;
    }
    await t
        .expect(gamePage.myScore.textContent).eql(0)
        .expect(gamePage.opponentScore.textContent).eql(0)
})
