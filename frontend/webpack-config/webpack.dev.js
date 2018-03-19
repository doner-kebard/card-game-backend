const lobby = require('./webpack.lobby.js');
const game = require('./webpack.game.js');
const loaders = require('./webpack.loaders.js');
const paths = require('./webpack.paths.js');
const merge = require('webpack-merge');

module.exports = merge(lobby, game, loaders, paths, {
    mode: "development",
    devtool: 'inline-source-map',
    devServer: {
        allowedHosts: [
            "localhost",
            "frontend",
            ".rhoton.es"
        ],
        host: "0.0.0.0",
        port: 8880
    },
});
