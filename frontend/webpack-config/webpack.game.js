const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = {
    entry: {
        game: './src/game/game.js'
    },
    plugins: [
        new HtmlWebpackPlugin({
            filename: 'game.html',
            template: './src/game/game.pug',
            chunks: ['game']
        })
    ]
}
