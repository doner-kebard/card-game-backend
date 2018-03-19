"use strict";
import { Selector } from 'testcafe'; // first import testcafe selectors

export default class MainPage {
    constructor () {
        this.title = Selector('#app h1.display-1');
        this.createGame = Selector('#create-game');
    }
}
