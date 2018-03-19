module.exports = {
    module: {
        rules: [
            {
                test: /\.pug$/,
                use: {
                    loader: 'pug-loader',
                    options: {}
                }
            },
            {
                test: /\.yml$/,
                use: [
                    { loader: 'json-loader' },
                    { loader: 'yaml-loader' }
                ]
            }
        ]
    }
};
