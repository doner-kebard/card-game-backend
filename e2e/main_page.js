"use strict";
import { Selector } from 'testcafe'; // first import testcafe selectors

export default class MainPage {
    constructor () {
        this.createGame = Selector('#create-game');
    }
}
