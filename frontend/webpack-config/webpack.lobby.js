const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = {
    entry: {
        lobby_create: './src/lobby/create/create.js',
        lobby_waiting: './src/lobby/waiting/waiting.js',
        lobby_join: './src/lobby/join/join.js',
    },
    plugins: [
        new HtmlWebpackPlugin({
            filename: 'lobby_create.html',
            template: './src/lobby/create/create.pug',
            chunks: ['lobby_create']
        }),
        new HtmlWebpackPlugin({
            filename: 'lobby_waiting.html',
            template: './src/lobby/waiting/waiting.pug',
            chunks: ['lobby_waiting']
        }),
        new HtmlWebpackPlugin({
            filename: 'lobby_join.html',
            template: './src/lobby/join/join.pug',
            chunks: ['lobby_join']
        })
    ]
}
