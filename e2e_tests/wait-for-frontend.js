"use strict";
import fetch from 'node-fetch';
import delay from 'delay';
import config from './config.js'

export default async function() {
    for(var i = 0; i < 50; i++) {
        try {
            const response = await fetch("http://" + config.servers["frontend"]);
            if (response.ok) {
                break
            } else {
                await delay(1000)
            }
        } catch (error) {
            await delay(1000)
        }
    }
}
