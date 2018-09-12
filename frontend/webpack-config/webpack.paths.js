const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = {
    output: {
        filename: "[name].bundle.js",
    },
    resolve: {
        modules: [path.resolve(__dirname, "../src"), "node_modules"],
        extensions: [".js", ".jsx", ".json"]
    },
    plugins: [
        new HtmlWebpackPlugin({
            filename: "index.html",
            template: "./src/index.pug",
            chunks: []
        }),
    ]
};
